package org.springdoc.kotlin;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import kotlin.jvm.functions.Function1;
import kotlin.reflect.KClass;
import kotlinx.serialization.DeserializationStrategy;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.SerializationStrategy;
import kotlinx.serialization.SerializersKt;
import kotlinx.serialization.descriptors.PolymorphicKind;
import kotlinx.serialization.descriptors.PrimitiveKind;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.descriptors.SerialKind;
import kotlinx.serialization.descriptors.StructureKind;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.modules.SerializersModule;
import kotlinx.serialization.modules.SerializersModuleCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.swagger.v3.core.util.RefUtils.constructRef;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

/**
 * <p>ModelConverter implementation to integrate with kotlinx.serialization as JSON implementation.</p>
 * <br/>
 * <p>Define a bean of type {@link Json} containing the relevant {@link SerializersModule} and {@link kotlinx.serialization.json.JsonConfiguration} to
 * enable this converter.</p>
 * <p>There are some restrictions on the Json configuration to be compatible with the Open API spec. In particular the following properties if set to true are unsupported:</p>
 * <ul>
 *     <li>structuredMapKeys</li>
 *     <li>allowSpecialFloatingPointValues</li>
 *     <li>useArrayPolymorphism</li>
 * </ul>
 * <p>The name for a Json schemas is equal to the serialName of the corresponding descriptor with package name removed.</p>
 * <p>In case of a naming conflict an index is appended for each successive encountered conflict, e.g.: Foo, Foo1, Foo2 for classes named com.bar1.Foo, com.bar2.Foo, com.bar3.Foo.</p>
 * <p>To use fully qualified names instead, set the property <code>springdoc.use-fqn</code> to true</p>
 *
 * @author Werner Altewischer
 */
@Component
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
@ConditionalOnBean(value = Json.class)
public class KotlinxSerializationTypeConverter implements ModelConverter {

    private static final Pattern polymorphicNamePattern = Pattern.compile("kotlinx\\.serialization\\.(Polymorphic|Sealed)<(.*)>");
    private final SerializersModule module;
    private final Map<String, List<SerialDescriptor>> polymorphicMap;
    private final Map<String, String> qualifiedNameMap = new ConcurrentHashMap<>();
    private final Map<String, Schema<?>> resolvedSchemaCache = new ConcurrentHashMap<>();
    private final String discriminatorProperty;
    private final boolean useQualifiedNames;

    @Autowired
    public KotlinxSerializationTypeConverter(@NotNull Json json, @Value("${springdoc.use-fqn:false}") boolean useQualifiedNames) {
        // Inspect the module to collect all the subclasses for polymorphism
        if (json.getConfiguration().getAllowStructuredMapKeys()) {
            throw new IllegalStateException("Structured map keys are not supported by the Open API Json specification");
        }
        if (json.getConfiguration().getAllowSpecialFloatingPointValues()) {
            throw new IllegalStateException("Special floating point values such as NaN and Inf are not compatible with the Open API Json specification");
        }
        if (json.getConfiguration().getUseArrayPolymorphism()) {
            throw new IllegalStateException("Array polymorphism is not compatible with the Open API Json specification");
        }
        final Map<String, List<SerialDescriptor>> polymorphicMap = new HashMap<>();
        this.useQualifiedNames = useQualifiedNames;
        this.discriminatorProperty = json.getConfiguration().getClassDiscriminator();
        this.module = json.getSerializersModule();
        this.module.dumpTo(new SerializersModuleCollector() {
            @Override
            public <Base> void polymorphicDefaultDeserializer(@NotNull KClass<Base> kClass, @NotNull Function1<? super String, ? extends DeserializationStrategy<? extends Base>> function1) {
            }

            @Override
            public <Base> void polymorphicDefaultSerializer(@NotNull KClass<Base> kClass, @NotNull Function1<? super Base, ? extends SerializationStrategy<? super Base>> function1) {
            }

            @Override
            public <T> void contextual(@NotNull KClass<T> kClass, @NotNull KSerializer<T> kSerializer) {
            }

            @Override
            public <T> void contextual(@NotNull KClass<T> kClass, @NotNull Function1<? super List<? extends KSerializer<?>>, ? extends KSerializer<?>> function1) {

            }

            @Override
            public <Base, Sub extends Base> void polymorphic(@NotNull KClass<Base> baseClass, @NotNull KClass<Sub> subClass, @NotNull KSerializer<Sub> subSerializer) {
                KSerializer<Base> baseSerializer = SerializersKt.serializer(baseClass);
                polymorphicMap.computeIfAbsent(getName(baseSerializer.getDescriptor()), (k) -> new ArrayList<>()).add(subSerializer.getDescriptor());
            }

            @Override
            public <Base> void polymorphicDefault(@NotNull KClass<Base> kClass, @NotNull Function1<? super String, ? extends DeserializationStrategy<? extends Base>> function1) {
            }
        });
        this.polymorphicMap = Collections.unmodifiableMap(polymorphicMap);
    }

    @Override
    public Schema<?> resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> iterator) {
        // Only process types that are annotated with @Serializable
        if (annotatedType.getType() instanceof Class) {
            final Class<?> cls = (Class<?>)annotatedType.getType();
            if (Arrays.stream(cls.getAnnotations()).anyMatch(it -> it instanceof Serializable)) {
                KSerializer<?> serializer = SerializersKt.serializer(module, cls);
                SerialDescriptor serialDescriptor = serializer.getDescriptor();
                return resolveNullableSchema(context, serialDescriptor, null);
            }
        }
        if (iterator.hasNext()) {
            return iterator.next().resolve(annotatedType, context, iterator);
        } else {
            return null;
        }
    }

    private Schema<?> resolveNullableSchema(@NotNull ModelConverterContext context, @NotNull SerialDescriptor serialDescriptor, @Nullable Schema<?> baseSchema) {
        return resolveSchema(context, serialDescriptor, baseSchema).nullable(serialDescriptor.isNullable());
    }

    @NotNull
    private Schema<?> resolveSchema(@NotNull ModelConverterContext context, @NotNull SerialDescriptor serialDescriptor, @Nullable Schema<?> baseSchema) {
        final SerialKind kind = serialDescriptor.getKind();
        final Schema<?> resolved = resolveRef(context, serialDescriptor);
        if (resolved != null) {
            return resolved;
        } else if (PrimitiveKind.STRING.INSTANCE.equals(kind)) {
            return new StringSchema();
        } else if (PrimitiveKind.BOOLEAN.INSTANCE.equals(kind)) {
            return new BooleanSchema();
        } else if (PrimitiveKind.INT.INSTANCE.equals(kind) ||
                PrimitiveKind.LONG.INSTANCE.equals(kind) ||
                PrimitiveKind.SHORT.INSTANCE.equals(kind) ||
                PrimitiveKind.BYTE.INSTANCE.equals(kind) ||
                PrimitiveKind.CHAR.INSTANCE.equals(kind)) {
            return new IntegerSchema();
        } else if (PrimitiveKind.FLOAT.INSTANCE.equals(kind) ||
                PrimitiveKind.DOUBLE.INSTANCE.equals(kind)) {
            return new NumberSchema();
        } else if (StructureKind.CLASS.INSTANCE.equals(kind) || StructureKind.OBJECT.INSTANCE.equals(kind)) {
            final Schema<?> schema = createSchema(baseSchema, ObjectSchema::new);
            for (int i = 0; i < serialDescriptor.getElementsCount(); ++i) {
                final SerialDescriptor elementDescriptor = serialDescriptor.getElementDescriptor(i);
                final String elementName = serialDescriptor.getElementName(i);
                if (isNotDefined(context, baseSchema, elementName)) {
                    schema.addProperties(
                            elementName,
                            resolveNullableSchema(context, elementDescriptor, null)
                    );
                    if (!serialDescriptor.isElementOptional(i)) {
                        schema.addRequiredItem(elementName);
                    }
                }
            }
            return defineRef(context, serialDescriptor, schema);
        } else if (StructureKind.LIST.INSTANCE.equals(kind)) {
            final ArraySchema schema = new ArraySchema();
            final SerialDescriptor elementDescriptor = serialDescriptor.getElementDescriptor(0);
            schema.setItems(
                    resolveNullableSchema(context, elementDescriptor, null)
            );
            return schema;
        } else if (StructureKind.MAP.INSTANCE.equals(kind)) {
            if (serialDescriptor.getElementsCount() != 2) {
                throw new IllegalStateException("Expected exactly two elements for a Map serial descriptor");
            }
            // Key should always be a string
            if (!PrimitiveKind.STRING.INSTANCE.equals(serialDescriptor.getElementDescriptor(0).getKind())) {
                throw new IllegalStateException("Key type should be string for a Map serial descriptor to be able to support JSON mappings");
            }
            final Schema<?> schema = new ObjectSchema();
            final SerialDescriptor elementDescriptor = serialDescriptor.getElementDescriptor(1);
            final Schema<?> valueSchema = resolveNullableSchema(context, elementDescriptor, null);
            schema.additionalProperties(valueSchema);
            return schema;
        } else if (SerialKind.CONTEXTUAL.INSTANCE.equals(kind)) {
            throw new IllegalStateException("Contextual mappings are only allowed in the context of polymorphism");
        } else if (SerialKind.ENUM.INSTANCE.equals(kind)) {
            if (baseSchema != null) {
                throw new IllegalStateException("Enum classes do not support inheritance, use sealed classes instead");
            }
            StringSchema schema = new StringSchema();
            for (int i = 0; i < serialDescriptor.getElementsCount(); ++i) {
                schema.addEnumItem(serialDescriptor.getElementName(i));
            }
            return defineRef(context, serialDescriptor, schema);
        } else if (PolymorphicKind.SEALED.INSTANCE.equals(kind) || PolymorphicKind.OPEN.INSTANCE.equals(kind)) {
            if (serialDescriptor.getElementsCount() < 2) {
                throw new IllegalStateException("Expected at least two fields for a polymorphic class descriptor");
            }
            final ComposedSchema composedSchema = (ComposedSchema) createSchema(baseSchema, ComposedSchema::new);
            final Discriminator discriminator = new Discriminator().propertyName(discriminatorProperty);
            composedSchema.discriminator(discriminator);
            final Schema<?> refSchema = defineRef(context, serialDescriptor, composedSchema);
            for (int i = 0; i < serialDescriptor.getElementsCount(); ++i) {
                final String elementName = i == 0 ? discriminatorProperty : serialDescriptor.getElementName(i);
                final SerialDescriptor elementDescriptor = serialDescriptor.getElementDescriptor(i);
                if (elementDescriptor.getKind().equals(SerialKind.CONTEXTUAL.INSTANCE)) {
                    final Collection<SerialDescriptor> allKnownSubDescriptors;
                    if (PolymorphicKind.SEALED.INSTANCE.equals(kind)) {
                        List<SerialDescriptor> list = new ArrayList<>();
                        for (int j = 0; j < elementDescriptor.getElementsCount(); ++j) {
                            list.add(elementDescriptor.getElementDescriptor(j));
                        }
                        allKnownSubDescriptors = list;
                    } else {
                        allKnownSubDescriptors = Optional.ofNullable(polymorphicMap.get(getName(serialDescriptor)))
                                .orElse(Collections.emptyList());
                    }
                    for (SerialDescriptor subDescriptor : allKnownSubDescriptors) {
                        final Schema<?> subSchema = resolveNullableSchema(context, subDescriptor, refSchema);
                        discriminator.mapping(getName(subDescriptor), subSchema.get$ref());
                    }
                } else {
                    if (isNotDefined(context, baseSchema, elementName)) {
                        composedSchema.addProperties(
                                elementName,
                                resolveNullableSchema(context, elementDescriptor, null)
                        );
                        if (!serialDescriptor.isElementOptional(i)) {
                            composedSchema.addRequiredItem(elementName);
                        }
                    }
                }
            }
            return refSchema;
        }
        throw new IllegalStateException("Unsupported serialDescriptor: " + serialDescriptor);
    }

    @NotNull
    private static Schema<?> createSchema(@Nullable Schema<?> baseSchema, @NotNull Supplier<Schema<?>> schemaFactory) {
        if (baseSchema != null) {
            return new ComposedSchema().addAllOfItem(baseSchema);
        } else {
            return schemaFactory.get();
        }
    }

    private boolean isNotDefined(@NotNull ModelConverterContext context, @Nullable Schema<?> baseSchema, String elementName) {
        if (baseSchema == null) return true;
        String ref = baseSchema.get$ref();
        if (ref != null) {
            if (ref.startsWith(Components.COMPONENTS_SCHEMAS_REF)) {
                ref = ref.substring(Components.COMPONENTS_SCHEMAS_REF.length());
            }
            Schema<?> resolvedSchema = context.getDefinedModels().get(ref);
            if (resolvedSchema == null) throw new IllegalStateException("Could not resolve schema: " + ref);
            baseSchema = resolvedSchema;
        }
        return baseSchema.getProperties() == null || !baseSchema.getProperties().containsKey(elementName);
    }

    @Nullable
    private Schema<?> resolveRef(@NotNull ModelConverterContext context, @NotNull SerialDescriptor serialDescriptor) {
        final String name = getName(serialDescriptor);
        Schema<?> resolvedSchema = resolvedSchemaCache.get(name);
        if (resolvedSchema != null) {
            if (!context.getDefinedModels().containsKey(name)) {
                context.defineModel(name, resolvedSchema);
            }
            return new Schema<>().$ref(constructRef(name));
        }
        return null;
    }

    @NotNull
    private Schema<?> defineRef(@NotNull ModelConverterContext context, @NotNull SerialDescriptor serialDescriptor, @NotNull Schema<?> schema) {
        // Store off the ref and add the enum as a top-level model
        final String name = getName(serialDescriptor);
        Schema<?> namedSchema = schema.name(name);
        context.defineModel(name, namedSchema);
        resolvedSchemaCache.put(name, namedSchema);
        return new Schema<>().$ref(constructRef(name));
    }

    @NotNull
    private String getName(@NotNull SerialDescriptor serialDescriptor) {
        final String serialName = serialDescriptor.getSerialName();
        String name = Objects.requireNonNull(serialName).replace("?", "").trim();
        Matcher matcher = polymorphicNamePattern.matcher(name);
        if (matcher.matches()) {
            // As fallback take the simple name defined between the brackets
            name = matcher.group(2);
            try {
                // Try to detect the kClass name from the ContextSerialDescriptor (which is a private class unfortunately)
                final Field field = serialDescriptor.getClass().getField("kClass");
                field.setAccessible(true);
                final KClass<?> clazz = (KClass<?>)field.get(serialDescriptor);
                if (clazz != null && clazz.getQualifiedName() != null) {
                    name = clazz.getQualifiedName();
                }
            } catch (Exception ex) {
                // ignore
            }
        }
        return useQualifiedNames ? name : simplifyName(name);
    }

    @NotNull
    private String simplifyName(String name) {
        final String qualifiedName = name;
        if (name.length() > 0 && !Character.isUpperCase(name.charAt(0))) {
            int pos = -1;
            while (true) {
                pos = name.indexOf('.', pos + 1);
                if (pos < 0 || pos == name.length() - 1) break;
                if (Character.isUpperCase(name.charAt(pos + 1))) {
                    name = name.substring(pos + 1);
                    break;
                }
            }
        }
        final String simpleName = name;
        int simpleNameIndex = 0;
        // Ensure that the simple name is unique
        while (true) {
            final String existingQualifiedName = qualifiedNameMap.putIfAbsent(name, qualifiedName);
            if (existingQualifiedName == null || existingQualifiedName.equals(qualifiedName)) {
                break;
            } else {
                name = simpleName + (++simpleNameIndex);
            }
        }
        return name;
    }
}
