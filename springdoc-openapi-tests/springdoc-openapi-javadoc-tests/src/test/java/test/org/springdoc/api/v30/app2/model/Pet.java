/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *  
 */

package test.org.springdoc.api.v30.app2.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * The type Pet.
 */
public class Pet {

	/**
	 * The Id.
	 */
	@Schema(description = "")
	private Long id = null;

	/**
	 * The Category.
	 */
	@Schema(description = "")
	private Category category = null;

	/**
	 * The Name.
	 */
	@Schema(example = "doggie", required = true, description = "")
	private String name = null;

	/**
	 * The Photo urls.
	 */
	@Schema(required = true, description = "")
	private List<String> photoUrls = new ArrayList<String>();

	/**
	 * The Tags.
	 */
	@Schema(description = "")
	private List<Tag> tags = null;

	/**
	 * The Status.
	 */
	@Schema(description = "pet status in the store")
	/**
	 * pet status in the store
	 **/
	private StatusEnum status = null;

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 * @param o the o
	 * @return the string
	 */
	private static String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	/**
	 * Get id
	 *
	 * @return id id
	 */
	@JsonProperty("id")
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Id pet.
	 *
	 * @param id the id
	 * @return the pet
	 */
	public Pet id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Get category
	 *
	 * @return category category
	 */
	@JsonProperty("category")
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets category.
	 *
	 * @param category the category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Category pet.
	 *
	 * @param category the category
	 * @return the pet
	 */
	public Pet category(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name name
	 */
	@JsonProperty("name")
	@NotNull
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Name pet.
	 *
	 * @param name the name
	 * @return the pet
	 */
	public Pet name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get photoUrls
	 *
	 * @return photoUrls photo urls
	 */
	@JsonProperty("photoUrls")
	@NotNull
	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	/**
	 * Sets photo urls.
	 *
	 * @param photoUrls the photo urls
	 */
	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	/**
	 * Photo urls pet.
	 *
	 * @param photoUrls the photo urls
	 * @return the pet
	 */
	public Pet photoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
		return this;
	}

	/**
	 * Add photo urls item pet.
	 *
	 * @param photoUrlsItem the photo urls item
	 * @return the pet
	 */
	public Pet addPhotoUrlsItem(String photoUrlsItem) {
		this.photoUrls.add(photoUrlsItem);
		return this;
	}

	/**
	 * Get tags
	 *
	 * @return tags tags
	 */
	@JsonProperty("tags")
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * Sets tags.
	 *
	 * @param tags the tags
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * Tags pet.
	 *
	 * @param tags the tags
	 * @return the pet
	 */
	public Pet tags(List<Tag> tags) {
		this.tags = tags;
		return this;
	}

	/**
	 * Add tags item pet.
	 *
	 * @param tagsItem the tags item
	 * @return the pet
	 */
	public Pet addTagsItem(Tag tagsItem) {
		if (this.tags == null) {
			this.tags = new ArrayList<>();
		}
		this.tags.add(tagsItem);
		return this;
	}

	/**
	 * pet status in the store
	 *
	 * @return status status
	 */
	@JsonProperty("status")
	public StatusEnum getStatus() {
		if (status == null) {
			return null;
		}
		return status;
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	/**
	 * Status pet.
	 *
	 * @param status the status
	 * @return the pet
	 */
	public Pet status(StatusEnum status) {
		this.status = status;
		return this;
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Pet {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    category: ").append(toIndentedString(category)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    photoUrls: ").append(toIndentedString(photoUrls)).append("\n");
		sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * The enum Status enum.
	 */
	public enum StatusEnum {
		/**
		 *Available status enum.
		 */
		AVAILABLE("available"),
		/**
		 *Pending status enum.
		 */
		PENDING("pending"),
		/**
		 *Sold status enum.
		 */
		SOLD("sold");

		/**
		 * The Value.
		 */
		private String value;

		/**
		 * Instantiates a new Status enum.
		 *
		 * @param value the value
		 */
		StatusEnum(String value) {
			this.value = value;
		}

		/**
		 * From value status enum.
		 *
		 * @param text the text
		 * @return the status enum
		 */
		@JsonCreator
		public static StatusEnum fromValue(String text) {
			for (StatusEnum b : StatusEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		@JsonValue
		public String getValue() {
			return value;
		}

		/**
		 * To string string.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
}
