package org.springdoc.core;

import java.util.Optional;

import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties.Format;
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
	 * Convert t.
	 *
	 * @param <T>  the type parameter
	 * @param source the source
	 * @param targetType the target type
	 * @return the t
	 */
	@Nullable
	public <T> T convert(@Nullable Object source, Class<T> targetType) {
		return webConversionService.convert(source, targetType);
	}
}
