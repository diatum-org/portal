package org.coredb.portal.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAcceptableException;
import org.coredb.portal.api.NotFoundException;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import static org.coredb.portal.NameRegistry.*;

@Service
public class AuthService {

  @Autowired
  private ConfigService configService;

  public void accessToken(String token) throws AccessDeniedException {

    // check if token has been set
    String console = configService.getStrValue(CONFIG_CONSOLETOKEN, null);
    if(console == null) {
      throw new AccessDeniedException("console token not set");
    }
    if(!console.equals(token)) {
      throw new AccessDeniedException("incorrect console token");
    }
  }

}


