package org.coredb.portal.service;

import java.util.*;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.coredb.portal.jpa.repository.TokenRepository;
import org.coredb.portal.jpa.entity.Token;
import static org.coredb.portal.NameRegistry.*;

@Service
public class TokenService {

  @Autowired
  private TokenRepository tokenRepository;
  
  @Transactional
  public void expire() {
    Long cur = Instant.now().getEpochSecond();
    tokenRepository.deleteByExpiresLessThanEqual(cur);
  }

}


