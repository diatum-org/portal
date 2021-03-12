package org.coredb.portal.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import org.coredb.portal.model.Domain;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PortalConfig
 */
@Validated


public class PortalConfig   {
  @JsonProperty("emigoToken")
  private String emigoToken = null;

  @JsonProperty("emigoNode")
  private String emigoNode = null;

  @JsonProperty("defaultRegistry")
  private String defaultRegistry = null;

  @JsonProperty("domains")
  @Valid
  private List<Domain> domains = null;

  public PortalConfig emigoToken(String emigoToken) {
    this.emigoToken = emigoToken;
    return this;
  }

  /**
   * Get emigoToken
   * @return emigoToken
   **/
  
    public String getEmigoToken() {
    return emigoToken;
  }

  public void setEmigoToken(String emigoToken) {
    this.emigoToken = emigoToken;
  }

  public PortalConfig emigoNode(String emigoNode) {
    this.emigoNode = emigoNode;
    return this;
  }

  /**
   * Get emigoNode
   * @return emigoNode
   **/
  
    public String getEmigoNode() {
    return emigoNode;
  }

  public void setEmigoNode(String emigoNode) {
    this.emigoNode = emigoNode;
  }

  public PortalConfig defaultRegistry(String defaultRegistry) {
    this.defaultRegistry = defaultRegistry;
    return this;
  }

  /**
   * Get defaultRegistry
   * @return defaultRegistry
   **/
  
    public String getDefaultRegistry() {
    return defaultRegistry;
  }

  public void setDefaultRegistry(String defaultRegistry) {
    this.defaultRegistry = defaultRegistry;
  }

  public PortalConfig domains(List<Domain> domains) {
    this.domains = domains;
    return this;
  }

  public PortalConfig addDomainsItem(Domain domainsItem) {
    if (this.domains == null) {
      this.domains = new ArrayList<Domain>();
    }
    this.domains.add(domainsItem);
    return this;
  }

  /**
   * Get domains
   * @return domains
   **/
      @Valid
    public List<Domain> getDomains() {
    return domains;
  }

  public void setDomains(List<Domain> domains) {
    this.domains = domains;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortalConfig portalConfig = (PortalConfig) o;
    return Objects.equals(this.emigoToken, portalConfig.emigoToken) &&
        Objects.equals(this.emigoNode, portalConfig.emigoNode) &&
        Objects.equals(this.defaultRegistry, portalConfig.defaultRegistry) &&
        Objects.equals(this.domains, portalConfig.domains);
  }

  @Override
  public int hashCode() {
    return Objects.hash(emigoToken, emigoNode, defaultRegistry, domains);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortalConfig {\n");
    
    sb.append("    emigoToken: ").append(toIndentedString(emigoToken)).append("\n");
    sb.append("    emigoNode: ").append(toIndentedString(emigoNode)).append("\n");
    sb.append("    defaultRegistry: ").append(toIndentedString(defaultRegistry)).append("\n");
    sb.append("    domains: ").append(toIndentedString(domains)).append("\n");
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

