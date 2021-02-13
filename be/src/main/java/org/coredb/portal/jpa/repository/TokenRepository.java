package org.coredb.portal.jpa.repository;

import org.springframework.data.jpa.repository.*;

import org.coredb.portal.jpa.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  Token findOneByToken(String token);
  Long deleteByExpiresLessThanEqual(Long expire);
}

