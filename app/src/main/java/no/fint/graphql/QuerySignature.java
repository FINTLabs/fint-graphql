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
 * Turns a query into a version that is safe to log. It blanks every value written in the query
 * and removes aliases and comments. Two queries that only differ in those things get the same text
 * and the same hash, so they can be grouped as one query. Variable values are sent separately from
 * the query, so they never end up here.
 */
record QuerySignature(String operation, String text, String hash) {

    private static final int MAX_TEXT_LENGTH = 10_000;

    /** Returns nothing if the query cannot be parsed or does not contain the requested operation. */
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

    /**
     * Finds the operation name when the query only has one operation. AstSignature needs this
     * because it removes named operations when it is not given a name, which would leave nothing
     * to log.
     */
    private static String onlyOperationName(Document document) {
        List<OperationDefinition> operations = document.getDefinitionsOfType(OperationDefinition.class);
        return operations.size() == 1 ? operations.getFirst().getName() : null;
    }
}
