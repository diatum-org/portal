package org.coredb.portal.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "hostname", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }))
public class Hostname implements Serializable {
  private Integer id;
  private Zone zone;
  private Long timestamp;
  private String name;

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

  @ManyToOne
  @JoinColumn(name = "domain_id")
  public Zone getZone() {
    return this.zone;
  }
  public void setZone(Zone value) {
    this.zone = value;
  }

  public String getName() {
    return this.name;
  }
  public void setName(String value) {
    this.name = value;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }
  public void setTimestamp(Long value) {
    this.timestamp = value;
  }

}

