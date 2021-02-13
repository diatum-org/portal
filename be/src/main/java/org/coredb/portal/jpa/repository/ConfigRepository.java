package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Config;

public interface ConfigRepository extends JpaRepository<Config, Integer> {
  List<Config> findAll();
  Config findOneByConfigId(String configId);
}

