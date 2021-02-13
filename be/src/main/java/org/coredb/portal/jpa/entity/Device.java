package org.coredb.portal.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "device", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Device implements Serializable {
  private Integer id;
  private String login;
  private String password;
  private String salt;
  private Long created;
  private Long updated;
  private String address;
  private String dnsStatus;
  private String dns;
  private Integer port;
  private String app;
  private String portalToken;
  private String accountToken;
  private String accessToken;
  private String statToken;
  private String confToken;
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @JsonIgnore
  public Integer getId() {
    return this.id;
  }
  public void setId(Integer value) {
    this.id = value;
  }

  @JsonIgnore
  public String getLogin() {
    return this.login;
  }
  public void setLogin(String value) {
    this.login = value;
  }

  @JsonIgnore
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String value) {
    this.password = value;
  }

  @JsonIgnore
  public String getSalt() {
    return this.salt;
  }
  public void setSalt(String value) {
    this.salt = value;
  }

  @JsonIgnore
  public Long getUpdated() {
    return this.updated;
  }
  public void setUpdated(Long value) {
    this.updated = value;
  }

  @JsonIgnore
  public Long getCreated() {
    return this.created;
  }
  public void setCreated(Long value) {
    this.created = value;
  }

  @JsonIgnore
  public String getAddress() {
    return this.address;
  }
  public void setAddress(String value) {
    this.address = value;
  }

  @JsonIgnore
  public String getDnsStatus() {
    return this.dnsStatus;
  }
  public void setDnsStatus(String value) {
    this.dnsStatus = value;
  }

  @JsonIgnore
  public String getDns() {
    return this.dns;
  }
  public void setDns(String value) {
    this.dns = value;
  }

  @JsonIgnore
  public Integer getPort() {
    return this.port;
  }
  public void setPort(Integer value) {
    this.port = value;
  }

  @JsonIgnore
  public String getApp() {
    return this.app;
  }
  public void setApp(String value) {
    this.app = value;
  }

  @JsonIgnore
  public String getPortalToken() {
    return this.portalToken;
  }
  public void setPortalToken(String value) {
    this.portalToken = value;
  }

  @JsonIgnore
  public String getAccountToken() {
    return this.accountToken;
  }
  public void setAccountToken(String value) {
    this.accountToken = value;
  }

  @JsonIgnore
  public String getAccessToken() {
    return this.accessToken;
  }
  public void setAccessToken(String value) {
    this.accessToken = value;
  }

  @JsonIgnore
  public String getStatToken() {
    return this.statToken;
  }
  public void setStatToken(String value) {
    this.statToken = value;
  }

  @JsonIgnore
  public String getConfToken() {
    return this.confToken;
  }
  public void setConfToken(String value) {
    this.confToken = value;
  }
}

