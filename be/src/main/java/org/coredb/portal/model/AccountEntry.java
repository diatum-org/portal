package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AccountEntry
 */
@Validated


public class AccountEntry   {
  @JsonProperty("node")
  private String node = null;

  @JsonProperty("token")
  private String token = null;

  public AccountEntry node(String node) {
    this.node = node;
    return this;
  }

  /**
   * Get node
   * @return node
  **/
  @ApiModelProperty(value = "")
  
    public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }

  public AccountEntry token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
  **/
  @ApiModelProperty(value = "")
  
    public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountEntry accountEntry = (AccountEntry) o;
    return Objects.equals(this.node, accountEntry.node) &&
        Objects.equals(this.token, accountEntry.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(node, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountEntry {\n");
    
    sb.append("    node: ").append(toIndentedString(node)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

