package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
  Device findOneByLogin(String login);
  Device findOneByPortalToken(String portalToken);
  long countByAddress(String address);
  long count();
}

