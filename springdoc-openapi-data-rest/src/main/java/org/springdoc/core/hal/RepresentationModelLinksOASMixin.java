package org.springdoc.core.hal;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.mediatype.hal.RepresentationModelMixin;

public abstract class RepresentationModelLinksOASMixin extends RepresentationModelMixin {
    @Override
    @Schema(ref = "#/components/schemas/Links")
    public abstract Links getLinks();
}
