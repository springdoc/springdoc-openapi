package org.springdoc.core.converters;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springdoc.core.providers.ObjectMapperProvider;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Multiple models with the same class name will be parsed into the same.
 * For example, given 2 models crm.model.User and sys.model.User as model classes,
 * only 1 will appear in openapi specification. The resulting specification may be incorrect.
 * </p>
 * Finds model classes with same name but different package, and renames model in specification.
 *
 * @author dsankouski
 */
public class SameModelNameConverter implements ModelConverter {

    private final Logger logger = LogManager.getLogger(SameModelNameConverter.class);

    private List<String> packagesToScan = new ArrayList<>();
    /**
     * The Spring doc object mapper.
     */
    private final ObjectMapperProvider springDocObjectMapper;
    private Map<String, String> models = new HashMap<>();
    /**
     * Openapi doc generator calls model converters several times for each type.
     * This lead to log polluting with same log entries.
     * <p>
     * Saving already reported duplications, to log problem only once.
     */
    private final Set<String> reportedDuplications = new HashSet<>();

    public SameModelNameConverter(
            ObjectMapperProvider springDocObjectMapper,
            List<String> packagesToScan
    ) {
        this.packagesToScan = packagesToScan;
        this.springDocObjectMapper = springDocObjectMapper;
    }

    /**
     * Instantiates a new Polymorphic model converter.
     *
     * @param springDocObjectMapper the spring doc object mapper
     */
    public SameModelNameConverter(ObjectMapperProvider springDocObjectMapper) {
        this.springDocObjectMapper = springDocObjectMapper;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Schema resolve(
            AnnotatedType type,
            ModelConverterContext context,
            Iterator<ModelConverter> chain) {
        final Schema schema = chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
        JavaType javaType = springDocObjectMapper.jsonMapper().constructType(type.getType());
        var rawClass = javaType.getRawClass();
        var customSchemaName = Arrays.stream(rawClass.getAnnotations())
                .filter(a -> a instanceof io.swagger.v3.oas.annotations.media.Schema)
                .map(a -> (io.swagger.v3.oas.annotations.media.Schema) a)
                .map(io.swagger.v3.oas.annotations.media.Schema::name)
                .reduce(String::concat)
                .orElse("");
        var fullTypeName = rawClass.getCanonicalName();
        var isEnum = rawClass.isEnum();
        var modelName = customSchemaName.isEmpty() ? rawClass.getSimpleName() : customSchemaName;
        var shouldScan = packagesToScan.stream().anyMatch(p -> {
            Pattern pattern = Pattern.compile(wildcardPatternToRegex(p));
            Matcher m = pattern.matcher(fullTypeName);

            return m.find();
        });

        logger.debug(String.format("model: %s", modelName));
        if (!shouldScan) {
            return schema;
        }
        if (!models.containsKey(fullTypeName) && !isEnum && schema != null) {
            if (models.containsValue(modelName)) {
                var duplicateType = models.entrySet().stream().filter(e -> e.getValue().equals(modelName)).findFirst().orElseThrow();
                var errorMessage = String.format("Cannot add %s model, because model with name '%s' already present (%s). Please, rename",
                        fullTypeName, modelName, duplicateType);
                if (!reportedDuplications.contains(errorMessage)) {
                    logger.error(errorMessage);
                    reportedDuplications.add(errorMessage);
                }
                var renameCandidateName = String.format("%sRenameCandidate", modelName);
                schema.name(renameCandidateName)
                        .description(errorMessage)
                        .type("object")
                        .set$ref(null);
                context.defineModel(renameCandidateName, schema);
                return schema;
            } else {
                models.put(fullTypeName, modelName);
            }
        }
        return schema;
    }

    private String wildcardPatternToRegex(String wildcardPattern) {
        return ("\\Q" + wildcardPattern + "\\E").replaceAll("\\*", "\\E.*\\Q");
    }
}