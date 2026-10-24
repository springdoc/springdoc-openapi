package org.springdoc.core.data;

import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.CollectionModelMixin;
import org.springframework.hateoas.mediatype.hal.LinkMixin;
import org.springframework.hateoas.mediatype.hal.RepresentationModelMixin;

/**
 * Jackson 2 module implementation
 *
 * @author bnasslahsen
 */
public class SpringDocJackson2HalModule extends SimpleModule {

	private static final long serialVersionUID = 7806951456457932384L;

	/**
	 * Instantiates a new Spring doc jackson 2 hal module.
	 */
	public SpringDocJackson2HalModule() {

		super("json-hal-module", new Version(1, 0, 0, null, "org.springframework.hateoas", "spring-hateoas"));

		setMixInAnnotation(Link.class, LinkMixin.class);
		setMixInAnnotation(RepresentationModel.class, RepresentationModelMixin.class);
		setMixInAnnotation(CollectionModel.class, CollectionModelMixin.class);
	}


	


}