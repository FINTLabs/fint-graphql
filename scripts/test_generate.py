"""Exercise staging and the custom service override without running Docker."""
import os
import shutil
import subprocess
import tempfile
import unittest
from pathlib import Path


REPOSITORY = Path(__file__).resolve().parent.parent


class GenerateTest(unittest.TestCase):
    def setUp(self):
        directory = tempfile.TemporaryDirectory()
        self.addCleanup(directory.cleanup)
        self.root = Path(directory.name)
        self.bin = self.root / 'bin'
        self.bin.mkdir()
        self.output = self.root / 'output'
        self.output.mkdir()
        shutil.copyfile(REPOSITORY / 'generate.sh', self.root / 'generate.sh')
        shutil.copyfile(REPOSITORY / 'PersonService.txt', self.root / 'PersonService.txt')
        (self.root / 'gradle.properties').write_text('apiVersion=4.1.0\n')
        self.checked_in_service = self.root / 'src/main/java/no/fint/graphql/model/model/person/PersonService.java'
        self.checked_in_service.parent.mkdir(parents=True)
        self.checked_in_service.write_text('existing source must remain untouched\n')

        # Mimic only the output layout of the container, not model generation.
        self.tool('docker', '''#!/usr/bin/env bash
set -euo pipefail
printf '%s\n' "$@" > docker-arguments
while (( $# )); do
  if [[ "$1" == '-v' && "$2" == *:/src/graphql/model ]]; then
    destination="${2%:/src/graphql/model}"
    mkdir -p "$destination/model/person"
    printf 'generated single-source service\n' > "$destination/model/person/PersonService.java"
  fi
  shift
done
''')
        # Generation must consume the modern CLI output without Python conversion.
        self.tool('python3', '''#!/usr/bin/env bash
echo 'Unexpected Python transformation' >&2
exit 1
''')

    def tool(self, name, contents):
        path = self.bin / name
        path.write_text(contents)
        path.chmod(0o755)

    def generate(self):
        environment = dict(os.environ, PATH=str(self.bin) + os.pathsep + os.environ['PATH'],
                           TMPDIR=str(self.output))
        return subprocess.run(['bash', str(self.root / 'generate.sh')], cwd=self.root,
                              env=environment, capture_output=True, text=True, timeout=10)

    def test_custom_service_replaces_generated_candidate_without_touching_source(self):
        result = self.generate()
        self.assertEqual(0, result.returncode, result.stdout + result.stderr)
        candidates = list(self.output.glob('fint-graphql-generation.*/model/model/person/PersonService.java'))
        self.assertEqual(1, len(candidates))
        self.assertEqual((self.root / 'PersonService.txt').read_bytes(), candidates[0].read_bytes())
        self.assertEqual('existing source must remain untouched\n', self.checked_in_service.read_text())
        arguments = (self.root / 'docker-arguments').read_text().splitlines()
        self.assertIn('fint-graphql-cli:2.0.0', arguments)
        self.assertEqual(['--tag', 'v4.1.0', 'generate', '--exclude', 'Fravar', '--exclude', 'Fravarstype'], arguments[-7:])

    def test_unexpected_generator_layout_fails_instead_of_silently_losing_custom_service(self):
        self.tool('docker', '#!/usr/bin/env bash\nexit 0\n')
        result = self.generate()
        self.assertNotEqual(0, result.returncode)
        self.assertIn('Expected generated PersonService', result.stderr)
        self.assertEqual('existing source must remain untouched\n', self.checked_in_service.read_text())

    def test_missing_template_fails_before_invoking_generator(self):
        (self.root / 'PersonService.txt').unlink()
        self.tool('docker', '#!/usr/bin/env bash\ntouch docker-invoked\nexit 1\n')
        result = self.generate()
        self.assertNotEqual(0, result.returncode)
        self.assertFalse((self.root / 'docker-invoked').exists())
        self.assertEqual([], list(self.output.iterdir()))


if __name__ == '__main__':
    unittest.main()
