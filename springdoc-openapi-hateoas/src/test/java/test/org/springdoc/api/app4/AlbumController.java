package test.org.springdoc.api.app4;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController {

	@Autowired
	private AlbumModelAssembler albumModelAssembler;

	@Autowired
	private PagedResourcesAssembler pagedResourcesAssembler;

	@GetMapping("/api/albums")
	public PagedModel<Album> getAllAlbums() {
		Album album1 = new Album("album-title-1", "album-description-1", "album-release-date-1");
		Album album2 = new Album("album-title-2", "album-description-2", "album-release-date-2");
		Page<Album> albumPage = new PageImpl<>(Arrays.asList(album1, album2));

		return pagedResourcesAssembler.toModel(albumPage, albumModelAssembler);
	}

}
