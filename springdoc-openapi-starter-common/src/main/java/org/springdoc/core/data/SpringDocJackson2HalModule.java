package org.springdoc.core.data;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.CollectionModelMixin;
import org.springframework.hateoas.mediatype.hal.LinkMixin;
import org.springframework.hateoas.mediatype.hal.RepresentationModelMixin;
import org.springframework.util.Assert;

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


	/**
	 * Is already registered in boolean.
	 *
	 * @param mapper the mapper
	 * @return the boolean
	 */
	public static boolean isAlreadyRegisteredIn(ObjectMapper mapper) {

		Assert.notNull(mapper, "ObjectMapper must not be null!");
		return LinkMixin.class.equals(mapper.findMixInClassFor(Link.class));
	}
	


}