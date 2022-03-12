package org.springdoc.core.providers;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * The type Web conversion service provider.
 * @author bnasslashen
 */
public class WebConversionServiceProvider {

	private static final String CONVERTERS = "converters";

	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebConversionServiceProvider.class);
	/**
	 * The Formatting conversion service.
	 */
	private final GenericConversionService formattingConversionService;

	/**
	 * Instantiates a new Web conversion service provider.
	 *
	 * @param webConversionServiceOptional the web conversion service optional
	 */
	public WebConversionServiceProvider(Optional<GenericConversionService> webConversionServiceOptional) {
		if (webConversionServiceOptional.isPresent())
			this.formattingConversionService = webConversionServiceOptional.get();
		else
			formattingConversionService = new DefaultFormattingConversionService();
	}

	/**
	 * Attempts to convert {@code source} into the target type as described by {@code targetTypeDescriptor}.
	 *
	 * @param source the source
	 * @param targetTypeDescriptor the target type descriptor
	 * @return the converted source
	 */
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor targetTypeDescriptor) {
		return formattingConversionService.convert(source, targetTypeDescriptor);
	}

	/**
	 * Gets spring converted type.
	 *
	 * @param clazz the clazz
	 * @return the spring converted type
	 */
	public Class<?> getSpringConvertedType(Class<?> clazz) {
		Class<?> result = clazz;
		Field convertersField = FieldUtils.getDeclaredField(GenericConversionService.class, CONVERTERS, true);
		Object converters;
		try {
			converters = convertersField.get(formattingConversionService);
			convertersField = FieldUtils.getDeclaredField(converters.getClass(), CONVERTERS, true);
			Map<ConvertiblePair, Object> springConverters = (Map) convertersField.get(converters);
			Optional<ConvertiblePair> convertiblePairOptional = springConverters.keySet().stream().filter(convertiblePair -> convertiblePair.getTargetType().equals(clazz)).findAny();
			if (convertiblePairOptional.isPresent()) {
				ConvertiblePair convertiblePair = convertiblePairOptional.get();
				result = convertiblePair.getSourceType();
			}
		}
		catch (IllegalAccessException e) {
			LOGGER.warn(e.getMessage());
		}
		return result;
	}
}
