package no.fint.graphql;

import com.google.common.hash.Hashing;
import graphql.language.AstPrinter;
import graphql.language.AstSignature;
import graphql.language.Document;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * The shape of a query, safe to log: every literal value is blanked, and aliases and comments are
 * gone. Queries differing only in those values share text and hash, so they group as one query.
 * Variable values are not part of a document and never appear here.
 */
record QuerySignature(String operation, String text, String hash) {

    private static final int MAX_TEXT_LENGTH = 10_000;

    /** Empty for a document that cannot be parsed or has no such operation; it never ran either. */
    static Optional<QuerySignature> of(String document, String operationName) {
        try {
            Document parsed = Parser.parse(document);
            String operation = StringUtils.defaultIfBlank(operationName, onlyOperationName(parsed));
            String signature = AstPrinter.printAstCompact(new AstSignature().signatureQuery(parsed, operation));
            if (StringUtils.isBlank(signature)) {
                return Optional.empty();
            }
            String hash = Hashing.sha256().hashString(signature, StandardCharsets.UTF_8).toString().substring(0, 16);
            return Optional.of(new QuerySignature(operation, StringUtils.abbreviate(signature, MAX_TEXT_LENGTH), hash));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    /** AstSignature treats no name as "the anonymous operation" and would drop a lone named one. */
    private static String onlyOperationName(Document document) {
        List<OperationDefinition> operations = document.getDefinitionsOfType(OperationDefinition.class);
        return operations.size() == 1 ? operations.getFirst().getName() : null;
    }
}
