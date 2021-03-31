package org.coredb.portal.service;

import java.util.*;
import java.time.Instant;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import org.springframework.web.client.ResourceAccessException;

import static org.coredb.portal.NameRegistry.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;

import org.coredb.portal.model.ServiceAccess;
import org.coredb.portal.model.LinkMessage;
import org.coredb.portal.model.UserEntry;
import org.coredb.portal.model.AmigoToken;
import org.coredb.portal.model.Pass;

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

  private static final int TIMEOUT = 15000;

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
    RestTemplate rest = new RestTemplateBuilder().setConnectTimeout(TIMEOUT).setReadTimeout(TIMEOUT).build();
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
      try {
        link = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("app identity error");
      }
    }

    // construct account create url
    String node = "https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp();
    String emigoUrl = node + "/access/accounts/created?token=" + device.getAccessToken();

    // post for emigo token
    HttpEntity<LinkMessage> linkMessage = new HttpEntity<LinkMessage>(link, headers);
    AmigoToken emigo;
    try {
      emigo = rest.postForObject(emigoUrl, linkMessage, AmigoToken.class);
    }
    catch(Exception e) {
      try {
        emigo = rest.postForObject(emigoUrl, linkMessage, AmigoToken.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("identity host error");
      }
    }

    // construct user url
    String userUrl = emigoNode + "/access/services/tokens?token=" + nodeToken;

    // post for user entry
    HttpEntity<AmigoToken> emigoToken = new HttpEntity<AmigoToken>(emigo, headers);
    UserEntry user;
    try {
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }
    catch(Exception e) {
      try {
        user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("registration error");
      }
    }

    // store new pass
    String salt = Password.salt();
    String pass = Password.prepare(password, salt);

    // create new account enntry
    Account account = accountRepository.findOneByEmigoId(emigo.getAmigoId());
    if(account == null) {
      account = new Account();
    }
    account.setDevice(device);
    account.setEmigoId(emigo.getAmigoId());
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
    RestTemplate rest = new RestTemplateBuilder().setConnectTimeout(TIMEOUT).setReadTimeout(TIMEOUT).build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // construct link request url
    String nodeToken = configService.getStrValue(CONFIG_EMIGOTOKEN, null);
    String emigoNode = configService.getStrValue(CONFIG_EMIGONODE, null);
    String linkUrl = emigoNode + "/access/services/attached?token=" + nodeToken + "&amigoId=" + emigoId;

    // post for link message
    HttpEntity<ServiceAccess> serviceAccess = new HttpEntity<ServiceAccess>(getAccess(), headers);
    LinkMessage link;
    try {
      link  = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
    }
    catch(Exception e) {
      try {
        link  = rest.postForObject(linkUrl, serviceAccess, LinkMessage.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("app identity error");
      }
    }

    // construct account create url
    String node = "https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp();
    String emigoUrl = node + "/admin/accounts/" + emigoId + "/service?token=" + device.getAccountToken();

    // post for emigo token
    HttpEntity<LinkMessage> linkMessage = new HttpEntity<LinkMessage>(link, headers);
    ResponseEntity<AmigoToken> response;
    try {
      response = rest.exchange(emigoUrl, HttpMethod.PUT, linkMessage, AmigoToken.class);
    }
    catch(Exception e) {
      try {
        response = rest.exchange(emigoUrl, HttpMethod.PUT, linkMessage, AmigoToken.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("app identity error");
      }
    }
    AmigoToken emigo = response.getBody();

    // construct user url
    String userUrl = emigoNode + "/access/services/tokens?token=" + nodeToken;

    // post for user entry
    HttpEntity<AmigoToken> emigoToken = new HttpEntity<AmigoToken>(emigo, headers);
    UserEntry user;
    try {
      user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
    }
    catch(Exception e) {
      try {
        user = rest.postForObject(userUrl, emigoToken, UserEntry.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("registration error");
      }
    }

    // store new pass
    String salt = Password.salt();
    String pass = Password.prepare(password, salt);

    // create new account enntry
    Account account = accountRepository.findOneByEmigoId(emigo.getAmigoId());
    if(account == null) {
      account = new Account();
    }
    account.setDevice(device);
    account.setEmigoId(emigo.getAmigoId());
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

  public String getRegistry() {
    return configService.getStrValue(CONFIG_EMIGOREGISTRY, null);
  }

  public String setPassCode(String amigoId, String password) throws NotFoundException, AccessDeniedException, Exception {

    Account account = accountRepository.findOneByEmigoId(amigoId);
    if(account == null) {
      throw new NotFoundException(404, "id not found");
    }
    String pass = Password.prepare(password, account.getSalt());
    if(!account.getPassword().equals(pass)) {
      throw new AccessDeniedException("login failed");
    }

    Device device = account.getDevice();
    String passUrl = "https://" + device.getDns() + ":" + device.getPort() + "/" + device.getApp() + "/access/accounts/tokens?token="
        + account.getToken() + "&expire=3600";

    // post for pass
    Pass code;
    RestTemplate rest = new RestTemplateBuilder().setConnectTimeout(TIMEOUT).setReadTimeout(TIMEOUT).build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ServiceAccess> serviceAccess = new HttpEntity<ServiceAccess>(getAccess(), headers);
    try {
      code = rest.postForObject(passUrl, serviceAccess, Pass.class);
    }
    catch(Exception e) {
      try {
        code = rest.postForObject(passUrl, serviceAccess, Pass.class);
      }
      catch (Exception f) {
        System.out.println(f.toString());
        throw new Exception("host node error");
      }
    }
    return code.getData();
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


