package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Zone;
import org.coredb.portal.jpa.entity.Hostname;

public interface HostnameRepository extends JpaRepository<Hostname, Integer> {
  Long countByZoneAndTimestampGreaterThan(Zone zone, Long timestamp);
}

