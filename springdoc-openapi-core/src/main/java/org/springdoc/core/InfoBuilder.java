package org.springdoc.core;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

public class InfoBuilder {

	public static Info build() {

		final Info info = new Info().title("Simple API").version("my version").description("This is a simple API")
				.termsOfService("termsOfService").version("1.0.0")
				.contact(new Contact().name("contact-name").email("you@your-company.com").url("compan-myUrl"));

		if (info.getLicense() == null) {
			License license = new License();
			license.setName(SwaggerProperties.DEFAULT_LICENSE_VALUE);
			license.setUrl(SwaggerProperties.DEFAULT_LICENSE_URL);
			info.license(license);
		}
		return info;
	}

}
