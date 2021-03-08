package org.springdoc.core;

import java.util.Optional;

import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties.Format;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

/**
 * The type Web conversion service provider.
 * @author bnasslashen
 */
public class WebConversionServiceProvider {

	/**
	 * The Web conversion service.
	 */
	private final WebConversionService webConversionService;

	/**
	 * Instantiates a new Web conversion service provider.
	 *
	 * @param webConversionServiceOptional the web conversion service optional
	 */
	public WebConversionServiceProvider(Optional<WebConversionService> webConversionServiceOptional) {
		if (webConversionServiceOptional.isPresent())
			this.webConversionService = webConversionServiceOptional.get();
		else {
			final Format format = new Format();
			this.webConversionService = new WebConversionService(new DateTimeFormatters()
					.dateFormat(format.getDate()).timeFormat(format.getTime()).dateTimeFormat(format.getDateTime()));
		}
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
		return webConversionService.convert(source, targetTypeDescriptor);
	}
}
