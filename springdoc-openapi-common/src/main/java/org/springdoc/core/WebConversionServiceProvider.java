package org.springdoc.core;

import java.util.Optional;

import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.Nullable;

/**
 * The type Web conversion service provider.
 * @author bnasslashen
 */
public class WebConversionServiceProvider {

	/**
	 * The Formatting conversion service.
	 */
	private final FormattingConversionService formattingConversionService;

	/**
	 * Instantiates a new Web conversion service provider.
	 *
	 * @param webConversionServiceOptional the web conversion service optional
	 */
	public WebConversionServiceProvider(Optional<WebConversionService> webConversionServiceOptional) {
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
}
