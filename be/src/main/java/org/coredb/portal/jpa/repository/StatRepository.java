package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Stat;

public interface StatRepository extends JpaRepository<Stat, Integer> {
  List<Stat> findAllByOrderByTimestampDesc();
  Long deleteByTimestampLessThanEqual(Integer ts);
}

