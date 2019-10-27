package test.org.springdoc.api.app2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class Body  {
  
  @Schema(description = "Updated name of the pet")
 /**
   * Updated name of the pet  
  **/
  private String name = null;
  
  @Schema(description = "Updated status of the pet")
 /**
   * Updated status of the pet  
  **/
  private String status = null;
 /**
   * Updated name of the pet
   * @return name
  **/
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Body name(String name) {
    this.name = name;
    return this;
  }

 /**
   * Updated status of the pet
   * @return status
  **/
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Body status(String status) {
    this.status = status;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
