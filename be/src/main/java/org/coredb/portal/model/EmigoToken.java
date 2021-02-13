package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.portal.model.EmigoMessage;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmigoToken
 */
@Validated
public class EmigoToken   {
  @JsonProperty("emigoId")
  private String emigoId = null;

  @JsonProperty("emigo")
  private EmigoMessage emigo = null;

  @JsonProperty("signature")
  private String signature = null;

  @JsonProperty("token")
  private String token = null;

  public EmigoToken emigoId(String emigoId) {
    this.emigoId = emigoId;
    return this;
  }

  /**
   * Get emigoId
   * @return emigoId
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getEmigoId() {
    return emigoId;
  }

  public void setEmigoId(String emigoId) {
    this.emigoId = emigoId;
  }

  public EmigoToken emigo(EmigoMessage emigo) {
    this.emigo = emigo;
    return this;
  }

  /**
   * Get emigo
   * @return emigo
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    @Valid
    public EmigoMessage getEmigo() {
    return emigo;
  }

  public void setEmigo(EmigoMessage emigo) {
    this.emigo = emigo;
  }

  public EmigoToken signature(String signature) {
    this.signature = signature;
    return this;
  }

  /**
   * Get signature
   * @return signature
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

    public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public EmigoToken token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
  **/
  @ApiModelProperty(required = true, value = "")
      @NotNull

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
    EmigoToken emigoToken = (EmigoToken) o;
    return Objects.equals(this.emigoId, emigoToken.emigoId) &&
        Objects.equals(this.emigo, emigoToken.emigo) &&
        Objects.equals(this.signature, emigoToken.signature) &&
        Objects.equals(this.token, emigoToken.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(emigoId, emigo, signature, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmigoToken {\n");
    
    sb.append("    emigoId: ").append(toIndentedString(emigoId)).append("\n");
    sb.append("    emigo: ").append(toIndentedString(emigo)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
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

