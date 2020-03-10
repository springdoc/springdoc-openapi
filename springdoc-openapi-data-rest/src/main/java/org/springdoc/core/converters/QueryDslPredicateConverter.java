package org.springdoc.core.converters;

import com.fasterxml.jackson.databind.JavaType;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.core.converter.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.springdoc.core.SpringDocAnnotationsUtils;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.CastUtils;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Mar, 2020
 **/
public class QueryDslPredicateConverter implements ModelConverter {
    private QuerydslBindingsFactory querydslBindingsFactory;
    private Optional<OpenAPI> openAPI;

    public QueryDslPredicateConverter(QuerydslBindingsFactory querydslBindingsFactory, Optional<OpenAPI> openAPI) {
        this.querydslBindingsFactory = querydslBindingsFactory;
        this.openAPI = openAPI;
    }

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext modelConverterContext, Iterator<ModelConverter> iterator) {
        if (iterator.hasNext() && !(annotatedType.getType() instanceof Predicate) && (annotatedType.getCtxAnnotations() == null || annotatedType.getCtxAnnotations().length < 1)) {
            return iterator.next().resolve(annotatedType, modelConverterContext, iterator);
        }

        QuerydslPredicate predicate = null;
        for (Annotation ctxAnnotation : annotatedType.getCtxAnnotations()) {
            if (ctxAnnotation instanceof QuerydslPredicate) {
                predicate = (QuerydslPredicate) ctxAnnotation;
            }
        }

        if (predicate == null) {
            return iterator.hasNext() ? iterator.next().resolve(annotatedType, modelConverterContext, iterator) : null;
        }

        ClassTypeInformation<?> classTypeInformation = ClassTypeInformation.from(predicate.root());

        Optional<QuerydslPredicate> annotation = Optional.of(predicate);
        TypeInformation<?> domainType = classTypeInformation.getRequiredActualType();

        Optional<Class<? extends QuerydslBinderCustomizer<?>>> bindingsAnnotation = annotation //
                .map(QuerydslPredicate::bindings) //
                .map(CastUtils::cast);

        QuerydslBindings bindings = bindingsAnnotation //
                .map(it -> querydslBindingsFactory.createBindingsFor(domainType, it)) //
                .orElseGet(() -> querydslBindingsFactory.createBindingsFor(domainType));

        String generatedClassName = "com.springdoc.core." + predicate.bindings().getSimpleName() + "G";

        Class<?> tClass = null;
        try {
            tClass = Class.forName(generatedClassName);
        } catch (ClassNotFoundException cnfe) {
            ClassPool classPool = ClassPool.getDefault();

            CtClass classPoolOrNull = classPool.getOrNull(generatedClassName);

            if (classPoolOrNull == null) {

                classPoolOrNull = classPool.makeClass(generatedClassName);

                Set<String> fieldsToAdd = Arrays.stream(predicate.root().getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());

                //remove blacklisted fields
                Set<String> blacklist = getBindingFieldValues(bindings, "blackList");
                fieldsToAdd.removeIf(blacklist::contains);

                Set<String> whiteList = getBindingFieldValues(bindings, "whiteList");
                Set<String> aliases = getBindingFieldValues(bindings, "aliases");

                fieldsToAdd.addAll(aliases);
                fieldsToAdd.addAll(whiteList);

                for (String fieldName : fieldsToAdd) {
                    try {
                        CtField f = new CtField(CtClass.charType, fieldName, classPoolOrNull);
                        f.setModifiers(Modifier.PUBLIC);
                        classPoolOrNull.addField(f);
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }

            }

            try {
                tClass = classPoolOrNull.toClass(this.getClass().getClassLoader(), this.getClass().getProtectionDomain());
            } catch (CannotCompileException e) {
                e.printStackTrace();
                return iterator.hasNext() ? iterator.next().resolve(annotatedType, modelConverterContext, iterator) : null;
            }
        }

        if (!openAPI.isPresent()) {
            throw new IllegalArgumentException("Open api not found");
        }

        return SpringDocAnnotationsUtils.resolveSchemaFromType(tClass, openAPI.get().getComponents(), null);
    }

    private Set<String> getBindingFieldValues(QuerydslBindings instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            return (Set<String>) field.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private boolean isQueryDslPredicate(JavaType ct) {
        return ct.isTypeOrSubTypeOf(Predicate.class);
    }
}
