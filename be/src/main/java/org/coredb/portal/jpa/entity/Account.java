package org.coredb.portal.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Account implements Serializable {
  private Integer id;
  private String emigoId;
  private String password;
  private String salt;
  private Long created;
  private String token;
  private Device device;
  
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
  public String getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(String value) {
    this.emigoId = value;
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
  public Long getCreated() {
    return this.created;
  }
  public void setCreated(Long value) {
    this.created = value;
  }

  @JsonIgnore
  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
  }

  @ManyToOne
  @JoinColumn(name = "device_id")
  public Device getDevice() {
    return this.device;
  }
  public void setDevice(Device value) {
    this.device = value;
  }
}

