package org.springdoc.sample2;

import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * InventoryItem
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-07-08T09:37:36.546Z[GMT]")
public class InventoryItem   {
  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("releaseDate")
  private String releaseDate = null;

  @JsonProperty("manufacturer")
  private Manufacturer manufacturer = null;

  public InventoryItem id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
	@Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true)
  @NotNull

  @Valid
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public InventoryItem name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
	@Schema(example = "Widget Adapter", required = true)
  @NotNull

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public InventoryItem releaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
    return this;
  }

  /**
   * Get releaseDate
   * @return releaseDate
  **/
	@Schema(example = "2016-08-29T09:12:33.001Z", required = true)
  @NotNull

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public InventoryItem manufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
    return this;
  }

  /**
   * Get manufacturer
   * @return manufacturer
  **/
	@Schema(required = true)
  @NotNull

  @Valid
  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer manufacturer) {
    this.manufacturer = manufacturer;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryItem inventoryItem = (InventoryItem) o;
    return Objects.equals(this.id, inventoryItem.id) &&
        Objects.equals(this.name, inventoryItem.name) &&
        Objects.equals(this.releaseDate, inventoryItem.releaseDate) &&
        Objects.equals(this.manufacturer, inventoryItem.manufacturer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, releaseDate, manufacturer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryItem {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
    sb.append("    manufacturer: ").append(toIndentedString(manufacturer)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
