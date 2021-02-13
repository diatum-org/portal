package org.coredb.portal.jpa.entity;

import org.coredb.portal.model.PortalStat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "token", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Token implements Serializable {
  private Integer id;
  private String token;
  private Long issued;
  private Long expires;
  private Boolean expired;
  private Device device;
  private String emigoId;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  public Integer getId() {
    return this.id;
  }
  public void setId(Integer value) {
    this.id = value;
  }

  public String getToken() {
    return this.token;
  }
  public void setToken(String value) {
    this.token = value;
  }

  public Long getIssued() {
    return this.issued;
  }
  public void setIssued(Long value) {
    this.issued = value;
  }

  public Long getExpires() {
    return this.expires;
  }
  public void setExpires(Long value) {
    this.expires = value;
  }

  public Boolean getExpired() {
    return this.expired;
  }
  public void setExpired(Boolean value) {
    this.expired = value;
  }

  public String getEmigoId() {
    return this.emigoId;
  }
  public void setEmigoId(String value) {
    this.emigoId = value;
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

