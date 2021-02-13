package org.coredb.portal.service;

import java.util.*;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.coredb.portal.NameRegistry.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import org.coredb.portal.model.ServiceAccess;
import org.coredb.portal.model.LinkMessage;
import org.coredb.portal.model.UserEntry;
import org.coredb.portal.model.EmigoToken;

import org.coredb.portal.model.AccountEntry;

import org.coredb.portal.service.ConfigService;
import org.coredb.portal.service.util.Password;

import org.coredb.portal.jpa.entity.Token;
import org.coredb.portal.jpa.repository.TokenRepository;

import org.coredb.portal.api.NotFoundException;
import java.nio.file.AccessDeniedException;
import org.springframework.web.client.RestClientException;

import org.coredb.portal.jpa.entity.Account;
import org.coredb.portal.jpa.repository.AccountRepository;

import org.coredb.portal.jpa.entity.Device;
import org.coredb.portal.jpa.repository.DeviceRepository;

import org.springframework.web.client.RestClientException;

@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private ConfigService configService;

  private ServiceAccess getAccess() {
    ServiceAccess access = new ServiceAccess();
    access.setEnableShow(true);
    access.setEnableIdentity(true);
    access.setEnableProfile(true);
    access.setEnableGroup(true);
    access.setEnableShare(true);
    access.setEnablePrompt(true);
    access.setEnableService(true);
    access.setEnableIndex(true);
    access.setEnableUser(true);
    access.setEnableAccess(true);
    access.setEnableAccount(true);
    return access;
  }

  public AccountEntry createIdentity(String token, String password) throws NotFoundException, AccessDeniedException, RestClientException, Exception {

    // validate create
    Token create = tokenRepository.findOneByToken(token);
    if(create == null) {
      throw new NotFoundException(404, "invalid create");
    }
    Long cur = Instant.now().getEpochSecond();
    if(create.getExpires() + configService.getNumValue(CONFIG_SKEW, (long)0) < cur) {
      throw new AccessDeniedException("create has expired");
    }
    if(create.getExpired()) {
      throw new AccessDeniedException("create has been used");
    }
    create.setExpired(true);
    tokenRepository.save(create);

    // extract device
    Device device = create.getDevice();

    // rest object
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    // construct link request url
    String nodeToken = configService.getStrValue(CONFIG_EMIGOTOKEN, null);
    String emigoNode = configService.getStrValue(CONFIG_EMIGONODE, null);
    String linkUrl = emigoNode + "/access/services/created?token=" + nodeToken;

    // post for link message
    HttpEntity<ServiceAccess> serviceAccess = new HttpEntity<ServiceAccess>(getAccess(), headers);
    LinkMessage link;
    try {
      link = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      link = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
    }

    // construct account create url
    String node = "https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp();
    String emigoUrl = node + "/access/accounts/created?token=" + device.getAccessToken();

    // post for emigo token
    HttpEntity<LinkMessage> linkMessage = new HttpEntity<LinkMessage>(link, headers);
    EmigoToken emigo;
    try {
      emigo = rest.postForObject(emigoUrl, linkMessage, EmigoToken.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      emigo = rest.postForObject(emigoUrl, linkMessage, EmigoToken.class);
    }

    // construct user url
    String userUrl = emigoNode + "/access/services/tokens?token=" + nodeToken;

    // post for user entry
    HttpEntity<EmigoToken> emigoToken = new HttpEntity<EmigoToken>(emigo, headers);
    UserEntry user;
    try {
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }

    // store new pass
    String salt = Password.salt();
    String pass = Password.prepare(password, salt);

    // create new account enntry
    Account account = accountRepository.findOneByEmigoId(emigo.getEmigoId());
    if(account == null) {
      account = new Account();
    }
    account.setDevice(device);
    account.setEmigoId(emigo.getEmigoId());
    account.setPassword(pass);
    account.setSalt(salt);
    account.setCreated(cur);
    account.setToken(user.getAccountToken());
    accountRepository.save(account);

    // return entry object
    AccountEntry entry = new AccountEntry();
    entry.setToken(account.getToken());
    entry.setNode(node);
    return entry;
  }

  public AccountEntry setIdentity(String token, String password) throws RestClientException, AccessDeniedException, RestClientException, Exception {

    // validate create
    Token reset = tokenRepository.findOneByToken(token);
    if(reset == null) {
      throw new NotFoundException(404, "invalid reset");
    }
    Long cur = Instant.now().getEpochSecond();
    if(reset.getExpires() + configService.getNumValue(CONFIG_SKEW, (long)0) < cur) {
      throw new AccessDeniedException("reset has expired");
    }
    if(reset.getExpired()) {
      throw new AccessDeniedException("reset has been used");
    }
    reset.setExpired(true);
    tokenRepository.save(reset);

    // extract device
    Device device = reset.getDevice();
    String emigoId = reset.getEmigoId();

    // rest object
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // construct link request url
    String nodeToken = configService.getStrValue(CONFIG_EMIGOTOKEN, null);
    String emigoNode = configService.getStrValue(CONFIG_EMIGONODE, null);
    String linkUrl = emigoNode + "/access/services/attached?token=" + nodeToken + "&emigoId=" + emigoId;

    // post for link message
    HttpEntity<ServiceAccess> serviceAccess = new HttpEntity<ServiceAccess>(getAccess(), headers);
    LinkMessage link;
    try {
      link  = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      link  = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
    }

    // construct account create url
    String node = "https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp();
    String emigoUrl = node + "/admin/accounts/" + emigoId + "/service?token=" + device.getAccountToken();

    // post for emigo token
    HttpEntity<LinkMessage> linkMessage = new HttpEntity<LinkMessage>(link, headers);
    ResponseEntity<EmigoToken> response;
    try {
      response = rest.exchange(emigoUrl, HttpMethod.PUT, linkMessage, EmigoToken.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      response = rest.exchange(emigoUrl, HttpMethod.PUT, linkMessage, EmigoToken.class);
    }
    EmigoToken emigo = response.getBody();

    // construct user url
    String userUrl = emigoNode + "/access/services/tokens?token=" + nodeToken;

    // post for user entry
    HttpEntity<EmigoToken> emigoToken = new HttpEntity<EmigoToken>(emigo, headers);
    UserEntry user;
    try {
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }
    catch(Exception e) {
      // give request another chance
      System.out.println(e.toString());
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }

    // store new pass
    String salt = Password.salt();
    String pass = Password.prepare(password, salt);

    // create new account enntry
    Account account = accountRepository.findOneByEmigoId(emigo.getEmigoId());
    if(account == null) {
      account = new Account();
    }
    account.setDevice(device);
    account.setEmigoId(emigo.getEmigoId());
    account.setPassword(pass);
    account.setSalt(salt);
    account.setCreated(cur);
    account.setToken(user.getAccountToken());
    accountRepository.save(account);

    // return entry object
    AccountEntry entry = new AccountEntry();
    entry.setToken(account.getToken());
    entry.setNode(node);
    return entry;
  }

  public AccountEntry getIdentity(String emigoId, String password) throws NotFoundException, AccessDeniedException, Exception {

    Account account = accountRepository.findOneByEmigoId(emigoId);
    if(account == null) {
      throw new NotFoundException(404, "id not found");
    }
    String pass = Password.prepare(password, account.getSalt());
    if(!account.getPassword().equals(pass)) {
      throw new AccessDeniedException("login failed");
    }

    // return entry object
    Device device = account.getDevice();
    AccountEntry entry = new AccountEntry();
    entry.setToken(account.getToken());
    entry.setNode("https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp());
    return entry;
  }

  public void changePassword(String emigoId, String current, String next) throws NotFoundException, AccessDeniedException, Exception {

    Account account = accountRepository.findOneByEmigoId(emigoId);
    if(account == null) {
      throw new NotFoundException(404, "id not found");
    }

    String cur = Password.prepare(current, account.getSalt());
    if(!account.getPassword().equals(cur)) {
      throw new AccessDeniedException("login failed");
    }

    String salt = Password.salt();
    String pass = Password.prepare(next, salt);
    account.setSalt(salt);
    account.setPassword(pass);
    accountRepository.save(account);
  }
  
}


