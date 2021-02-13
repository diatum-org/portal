package org.coredb.portal;

import java.util.*;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.coredb.portal.NameRegistry;
import org.coredb.portal.service.ConfigService;
import org.coredb.portal.service.TokenService;
import org.coredb.portal.jpa.repository.TokenRepository;

@Component
public class ScheduledTasks {

  @Autowired
  private ConfigService configService;

  @Autowired
  private TokenService tokenService;
  
  private long intervalCount = 0;
  private long configInterval = 60;
  private long tokenInterval = (24 * 60);

  @Scheduled(fixedRate = 60000)
  public void reportCurrentTime() {
    if(intervalCount % configInterval == 0) {
      configInterval = configService.refresh();
    }
    if(intervalCount % tokenInterval == 0) {
      tokenService.expire();
    }
    intervalCount++;
  }
}

