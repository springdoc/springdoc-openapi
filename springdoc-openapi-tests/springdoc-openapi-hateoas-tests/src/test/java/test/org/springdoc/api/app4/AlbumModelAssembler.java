package test.org.springdoc.api.app4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AlbumModelAssembler implements RepresentationModelAssembler<Album, EntityModel<Album>> {

	@Override
	public EntityModel<Album> toModel(Album entity) {
		List<Link> links = new ArrayList<>();
		return EntityModel.of(entity, links);
	}

}
