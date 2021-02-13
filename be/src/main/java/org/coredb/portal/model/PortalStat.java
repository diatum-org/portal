package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PortalStat
 */
@Validated


public class PortalStat   {
  @JsonProperty("timestamp")
  private Integer timestamp = null;

  @JsonProperty("processor")
  private Integer processor = null;

  @JsonProperty("memory")
  private Long memory = null;

  @JsonProperty("storage")
  private Long storage = null;

  @JsonProperty("requests")
  private Long requests = null;

  @JsonProperty("devices")
  private Long devices = null;

  @JsonProperty("accounts")
  private Long accounts = null;

  @JsonProperty("certs")
  private Long certs = null;

  public PortalStat timestamp(Integer timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  
    public Integer getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Integer timestamp) {
    this.timestamp = timestamp;
  }

  public PortalStat processor(Integer processor) {
    this.processor = processor;
    return this;
  }

  /**
   * Get processor
   * @return processor
   **/
  
    public Integer getProcessor() {
    return processor;
  }

  public void setProcessor(Integer processor) {
    this.processor = processor;
  }

  public PortalStat memory(Long memory) {
    this.memory = memory;
    return this;
  }

  /**
   * Get memory
   * @return memory
   **/
  
    public Long getMemory() {
    return memory;
  }

  public void setMemory(Long memory) {
    this.memory = memory;
  }

  public PortalStat storage(Long storage) {
    this.storage = storage;
    return this;
  }

  /**
   * Get storage
   * @return storage
   **/
  
    public Long getStorage() {
    return storage;
  }

  public void setStorage(Long storage) {
    this.storage = storage;
  }

  public PortalStat requests(Long requests) {
    this.requests = requests;
    return this;
  }

  /**
   * Get requests
   * @return requests
   **/
  
    public Long getRequests() {
    return requests;
  }

  public void setRequests(Long requests) {
    this.requests = requests;
  }

  public PortalStat devices(Long devices) {
    this.devices = devices;
    return this;
  }

  /**
   * Get devices
   * @return devices
   **/
  
    public Long getDevices() {
    return devices;
  }

  public void setDevices(Long devices) {
    this.devices = devices;
  }

  public PortalStat accounts(Long accounts) {
    this.accounts = accounts;
    return this;
  }

  /**
   * Get accounts
   * @return accounts
   **/
  
    public Long getAccounts() {
    return accounts;
  }

  public void setAccounts(Long accounts) {
    this.accounts = accounts;
  }

  public PortalStat certs(Long certs) {
    this.certs = certs;
    return this;
  }

  /**
   * Get certs
   * @return certs
   **/
  
    public Long getCerts() {
    return certs;
  }

  public void setCerts(Long certs) {
    this.certs = certs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalStat portalStat = (PortalStat) o;
    return Objects.equals(this.timestamp, portalStat.timestamp) &&
        Objects.equals(this.processor, portalStat.processor) &&
        Objects.equals(this.memory, portalStat.memory) &&
        Objects.equals(this.storage, portalStat.storage) &&
        Objects.equals(this.requests, portalStat.requests) &&
        Objects.equals(this.devices, portalStat.devices) &&
        Objects.equals(this.accounts, portalStat.accounts) &&
        Objects.equals(this.certs, portalStat.certs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, processor, memory, storage, requests, devices, accounts, certs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalStat {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    processor: ").append(toIndentedString(processor)).append("\n");
    sb.append("    memory: ").append(toIndentedString(memory)).append("\n");
    sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
    sb.append("    requests: ").append(toIndentedString(requests)).append("\n");
    sb.append("    devices: ").append(toIndentedString(devices)).append("\n");
    sb.append("    accounts: ").append(toIndentedString(accounts)).append("\n");
    sb.append("    certs: ").append(toIndentedString(certs)).append("\n");
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

