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
 * DeviceEntry
 */
@Validated


public class DeviceEntry   {
  @JsonProperty("status")
  private String status = null;

  @JsonProperty("timestamp")
  private Long timestamp = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("hostname")
  private String hostname = null;

  @JsonProperty("port")
  private Integer port = null;

  @JsonProperty("app")
  private String app = null;

  @JsonProperty("identity")
  private IdentityToken identity = null;

  public DeviceEntry status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  
    public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DeviceEntry timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public DeviceEntry address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(value = "")
  
    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public DeviceEntry hostname(String hostname) {
    this.hostname = hostname;
    return this;
  }

  /**
   * Get hostname
   * @return hostname
  **/
  @ApiModelProperty(value = "")
  
    public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public DeviceEntry port(Integer port) {
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

  public DeviceEntry app(String app) {
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

  public DeviceEntry identity(IdentityToken identity) {
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
    DeviceEntry deviceEntry = (DeviceEntry) o;
    return Objects.equals(this.status, deviceEntry.status) &&
        Objects.equals(this.timestamp, deviceEntry.timestamp) &&
        Objects.equals(this.address, deviceEntry.address) &&
        Objects.equals(this.hostname, deviceEntry.hostname) &&
        Objects.equals(this.port, deviceEntry.port) &&
        Objects.equals(this.app, deviceEntry.app) &&
        Objects.equals(this.identity, deviceEntry.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, timestamp, address, hostname, port, app, identity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceEntry {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    hostname: ").append(toIndentedString(hostname)).append("\n");
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

