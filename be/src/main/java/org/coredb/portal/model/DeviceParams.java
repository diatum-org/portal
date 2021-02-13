package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.coredb.portal.model.IdentityToken;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DeviceParams
 */
@Validated


public class DeviceParams   {
  @JsonProperty("login")
  private String login = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("port")
  private Integer port = null;

  @JsonProperty("app")
  private String app = null;

  @JsonProperty("identity")
  private IdentityToken identity = null;

  public DeviceParams login(String login) {
    this.login = login;
    return this;
  }

  /**
   * Get login
   * @return login
  **/
  @ApiModelProperty(value = "")
  
    public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public DeviceParams password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(value = "")
  
    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public DeviceParams port(Integer port) {
    this.port = port;
    return this;
  }

  /**
   * Get port
   * @return port
  **/
  @ApiModelProperty(value = "")
  
    public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public DeviceParams app(String app) {
    this.app = app;
    return this;
  }

  /**
   * Get app
   * @return app
  **/
  @ApiModelProperty(value = "")
  
    public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public DeviceParams identity(IdentityToken identity) {
    this.identity = identity;
    return this;
  }

  /**
   * Get identity
   * @return identity
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public IdentityToken getIdentity() {
    return identity;
  }

  public void setIdentity(IdentityToken identity) {
    this.identity = identity;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceParams deviceParams = (DeviceParams) o;
    return Objects.equals(this.login, deviceParams.login) &&
        Objects.equals(this.password, deviceParams.password) &&
        Objects.equals(this.port, deviceParams.port) &&
        Objects.equals(this.app, deviceParams.app) &&
        Objects.equals(this.identity, deviceParams.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login, password, port, app, identity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceParams {\n");
    
    sb.append("    login: ").append(toIndentedString(login)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    app: ").append(toIndentedString(app)).append("\n");
    sb.append("    identity: ").append(toIndentedString(identity)).append("\n");
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

