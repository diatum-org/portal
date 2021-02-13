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
 * IdentityToken
 */
@Validated


public class IdentityToken   {
  @JsonProperty("accountToken")
  private String accountToken = null;

  @JsonProperty("accessToken")
  private String accessToken = null;

  @JsonProperty("statToken")
  private String statToken = null;

  @JsonProperty("confToken")
  private String confToken = null;

  public IdentityToken accountToken(String accountToken) {
    this.accountToken = accountToken;
    return this;
  }

  /**
   * Get accountToken
   * @return accountToken
  **/
  @ApiModelProperty(value = "")
  
    public String getAccountToken() {
    return accountToken;
  }

  public void setAccountToken(String accountToken) {
    this.accountToken = accountToken;
  }

  public IdentityToken accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Get accessToken
   * @return accessToken
  **/
  @ApiModelProperty(value = "")
  
    public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public IdentityToken statToken(String statToken) {
    this.statToken = statToken;
    return this;
  }

  /**
   * Get statToken
   * @return statToken
  **/
  @ApiModelProperty(value = "")
  
    public String getStatToken() {
    return statToken;
  }

  public void setStatToken(String statToken) {
    this.statToken = statToken;
  }

  public IdentityToken confToken(String confToken) {
    this.confToken = confToken;
    return this;
  }

  /**
   * Get confToken
   * @return confToken
  **/
  @ApiModelProperty(value = "")
  
    public String getConfToken() {
    return confToken;
  }

  public void setConfToken(String confToken) {
    this.confToken = confToken;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentityToken identityToken = (IdentityToken) o;
    return Objects.equals(this.accountToken, identityToken.accountToken) &&
        Objects.equals(this.accessToken, identityToken.accessToken) &&
        Objects.equals(this.statToken, identityToken.statToken) &&
        Objects.equals(this.confToken, identityToken.confToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountToken, accessToken, statToken, confToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentityToken {\n");
    
    sb.append("    accountToken: ").append(toIndentedString(accountToken)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    statToken: ").append(toIndentedString(statToken)).append("\n");
    sb.append("    confToken: ").append(toIndentedString(confToken)).append("\n");
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
