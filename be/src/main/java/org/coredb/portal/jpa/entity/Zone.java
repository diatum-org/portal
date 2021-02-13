package org.coredb.portal.jpa.entity;

import org.coredb.portal.model.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "domain", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Zone extends Domain implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @JsonIgnore
  public Integer getId() {
    return super.getId();
  }
  public void setId(Integer value) {
    super.setId(value);
  }

  @JsonIgnore
  public String getName() {
    return super.getName();
  }
  public void setName(String value) {
    super.setName(value);
  }

  @JsonIgnore
  public String getZone() {
    return super.getZoneId();
  }
  public void setZone(String value) {
    super.setZoneId(value);
  }

  @JsonIgnore
  public String getRegion() {
    return super.getRegion();
  }
  public void setRegion(String value) {
    super.setRegion(value);
  }

  @JsonIgnore
  public String getKeyValue() {
    return super.getKey();
  }
  public void setKeyValue(String value) {
    super.setKey(value);
  }

  @JsonIgnore
  public String getKeyId() {
    return super.getKeyId();
  }
  public void setKeyId(String value) {
    super.setKeyId(value);
  }

  @JsonIgnore
  public Boolean getEnabled() {
    return super.isEnabled();
  }
  public void setEnabled(Boolean value) {
    super.setEnabled(value);
  }
}

