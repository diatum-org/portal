package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Integer> {
  List<Zone> findAll();
  List<Zone> findByEnabled(Boolean enabled);
  Zone findOneById(Integer id);
}

