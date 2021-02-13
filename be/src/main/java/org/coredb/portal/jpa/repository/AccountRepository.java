package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

import org.coredb.portal.jpa.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  Account findOneByEmigoId(String emigoId);
  long count();
}

