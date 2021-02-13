package org.coredb.portal.jpa.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "config", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Config implements Serializable {
  private Integer id;
  private String configId;
  private String strValue;
  private Long numValue;
  private Boolean boolValue;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @JsonIgnore
  public Integer getId() {
    return id;
  }
  public void setId(Integer value) {
    id = value;
  }

  @JsonIgnore
  public String getConfigId() {
    return configId;
  }
  public void setConfigId(String value) {
    configId = value;
  }

  @JsonIgnore
  public String getStrValue() {
    return strValue;
  }
  public void setStrValue(String value) {
    strValue = value;
  }

  @JsonIgnore
  public Long getNumValue() {
    return numValue;
  }
  public void setNumValue(Long value) {
    numValue = value;
  }

  @JsonIgnore
  public Boolean getBoolValue() {
    return boolValue;
  }
  public void setBoolValue(Boolean value) {
    boolValue = value;
  }
}

