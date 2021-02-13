package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Domain
 */
@Validated


public class Domain   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("region")
  private String region = null;

  @JsonProperty("key")
  private String key = null;

  @JsonProperty("keyId")
  private String keyId = null;

  @JsonProperty("zoneId")
  private String zoneId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  public Domain id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  
    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Domain region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   **/
  
    public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Domain key(String key) {
    this.key = key;
    return this;
  }

  /**
   * Get key
   * @return key
   **/
  
    public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Domain keyId(String keyId) {
    this.keyId = keyId;
    return this;
  }

  /**
   * Get keyId
   * @return keyId
   **/
  
    public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public Domain zoneId(String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   **/
  
    public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  public Domain name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   **/
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Domain enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   **/
  
    public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Domain domain = (Domain) o;
    return Objects.equals(this.id, domain.id) &&
        Objects.equals(this.region, domain.region) &&
        Objects.equals(this.key, domain.key) &&
        Objects.equals(this.keyId, domain.keyId) &&
        Objects.equals(this.zoneId, domain.zoneId) &&
        Objects.equals(this.name, domain.name) &&
        Objects.equals(this.enabled, domain.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, region, key, keyId, zoneId, name, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Domain {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    keyId: ").append(toIndentedString(keyId)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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

