package org.coredb.portal.service;

import java.util.*;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.coredb.portal.api.NotFoundException;
import java.nio.file.AccessDeniedException;

import static org.coredb.portal.NameRegistry.*;

import org.coredb.portal.model.IdentityToken;

import org.coredb.portal.service.util.Password;

import org.coredb.portal.service.ConfigService;

import org.coredb.portal.jpa.entity.Device;
import org.coredb.portal.jpa.repository.DeviceRepository;

import org.coredb.portal.jpa.entity.Token;
import org.coredb.portal.jpa.repository.TokenRepository;

import org.coredb.portal.jpa.entity.Zone;
import org.coredb.portal.jpa.repository.ZoneRepository;

import org.coredb.portal.jpa.entity.Hostname;
import org.coredb.portal.jpa.repository.HostnameRepository;

import org.coredb.portal.model.IdentityToken;
import org.coredb.portal.model.DeviceEntry;
import org.coredb.portal.model.DeviceParams;
import org.coredb.portal.jpa.entity.Device;
import org.coredb.portal.jpa.repository.DeviceRepository;

@Service
public class DeviceService {

  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private ZoneRepository zoneRepository;

  @Autowired
  private HostnameRepository hostnameRepository;
 
  @Autowired
  private ConfigService configService;

  private DeviceEntry setDevice(Device device) {

    DeviceEntry entry = new DeviceEntry();
    entry.setStatus(device.getDnsStatus());
    entry.setTimestamp(device.getUpdated());
    entry.setAddress(device.getAddress());
    entry.setHostname(device.getDns());
    entry.setPort(device.getPort());
    entry.setApp(device.getApp());
    entry.setIdentity(new IdentityToken());
    entry.getIdentity().setAccountToken(device.getAccountToken());
    entry.getIdentity().setAccessToken(device.getAccessToken());
    entry.getIdentity().setStatToken(device.getStatToken());
    entry.getIdentity().setConfToken(device.getConfToken());
    return entry;
  }

  private Device setLogin(String login, String password) throws NotFoundException, AccessDeniedException, Exception { 

    Device device = deviceRepository.findOneByLogin(login);
    if(device == null) {
      throw new NotFoundException(404, "login not found");
    }
    String pass = Password.prepare(password, device.getSalt());
    if(!device.getPassword().equals(pass)) {
      throw new AccessDeniedException("login failed");
    }
    return device;
  }

  @Transactional
  public String createAccount(String login, String password, Integer expire) throws NotFoundException, AccessDeniedException, Exception {

    Device device = setLogin(login, password);
    Long cur = Instant.now().getEpochSecond();
    Token token = new Token();
    token.setToken(Password.token());
    token.setIssued(cur);
    token.setExpires(cur + Long.valueOf(expire));
    token.setExpired(false);
    token.setDevice(device);
    tokenRepository.save(token);  

    return "\"" + configService.getStrValue(CONFIG_BASEURL, "") + "/#/account?create=" + token.getToken() + "\"";
  }

  @Transactional
  public String resetAccount(String login, String password, Integer expire, String emigoId) throws NotFoundException, AccessDeniedException, Exception {

    Device device = setLogin(login, password);
    Long cur = Instant.now().getEpochSecond();
    Token token = new Token();
    token.setToken(Password.token());
    token.setEmigoId(emigoId);
    token.setIssued(cur);
    token.setExpires(cur + Long.valueOf(expire));
    token.setExpired(false);
    token.setDevice(device);
    tokenRepository.save(token);

    return "\"" + configService.getStrValue(CONFIG_BASEURL, "") + "/#/account?reset=" + token.getToken() + "\"";
  }

  public Long getAvailable() {

    // count of available certs for each zone
    Long available = (long)0;
    Long limit = configService.getNumValue(CONFIG_RATELIMIT, (long)50);
    Long ts = Instant.now().getEpochSecond() - configService.getNumValue(CONFIG_RATEPERIOD, (long)604800);
    List<Zone> zones = zoneRepository.findByEnabled(true);
    for(Zone zone: zones) {
      Long count = hostnameRepository.countByZoneAndTimestampGreaterThan(zone, ts);
      if(limit > count) {
        available += (limit - count);
      }
    }
    return available;
  }

  public Boolean checkToken(String token) throws NotFoundException, Exception {
  
    Token tok = tokenRepository.findOneByToken(token);
    if(tok == null) {
      throw new NotFoundException(404, "token not found");
    }
    if(tok.getExpired()) {
      return false;
    }
    Long cur = Instant.now().getEpochSecond();
    if(cur + configService.getNumValue(CONFIG_SKEW, (long)0) < tok.getIssued()) {
      return false;
    }
    if(tok.getExpires() + configService.getNumValue(CONFIG_SKEW, (long)0) < cur) {
      return false;
    }
    return true;
  }    

  public DeviceEntry getDevice(String login, String password) throws NotFoundException, AccessDeniedException, Exception {

    Device device = setLogin(login, password); 
    return setDevice(device);
  }

  public Boolean checkLogin(String login) {

    Device device = deviceRepository.findOneByLogin(login);
    if(device == null) {
      return true;
    }
    return false;
  }

  @Transactional
  public String addDevice(DeviceParams params, String address) throws IllegalStateException, Exception {

    if(deviceRepository.countByAddress(address) > DEVICE_COUNT) {
      throw new IllegalStateException("too many devices at this address");
    }

    Long cur = Instant.now().getEpochSecond();
    String salt = Password.salt();
    String pass = Password.prepare(params.getPassword(), salt);
    String token = Password.token();

    // find available domain
    Zone domain = null;
    Long limit = configService.getNumValue(CONFIG_RATELIMIT, (long)50);
    Long used = limit;
    Long ts = Instant.now().getEpochSecond() - configService.getNumValue(CONFIG_RATEPERIOD, (long)604800);
    List<Zone> zones = zoneRepository.findByEnabled(true);
    for(Zone zone: zones) {
      Long count = hostnameRepository.countByZoneAndTimestampGreaterThan(zone, ts);
      if(limit > count && used > count) {
        used = count;
        domain = zone;
      }
    }
    if(domain == null) {
      throw new IllegalStateException("no available domain certificates");
    }

    Device device = new Device();
    device.setLogin(params.getLogin());
    device.setPassword(pass);
    device.setSalt(salt);
    device.setCreated(cur);
    device.setUpdated(cur);
    device.setAddress(address);
    device.setDnsStatus("not_set");
    device.setPort(params.getPort());
    device.setApp(params.getApp());
    device.setPortalToken(token);
    device.setAccountToken(params.getIdentity().getAccountToken());
    device.setAccessToken(params.getIdentity().getAccessToken());
    device.setStatToken(params.getIdentity().getStatToken());
    device.setConfToken(params.getIdentity().getConfToken());
    device = deviceRepository.save(device);

    // launch dns registration
    String[] cmd = { "bash", "/opt/diatum/register.sh", device.getId().toString(), domain.getId().toString() };
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(cmd);
    Process process = processBuilder.start();
 
    return token;
  }

  @Transactional
  public void setPassword(String login, String current, String next) throws NotFoundException, AccessDeniedException, Exception {
  
    Device device = deviceRepository.findOneByLogin(login);
    if(device == null) {
      throw new NotFoundException(404, "login not found");
    }
    String pass = Password.prepare(current, device.getSalt());
    if(!device.getPassword().equals(pass)) {
      throw new AccessDeniedException("login failed");
    }

    String salt = Password.salt();
    String updated = Password.prepare(next, salt);
    device.setSalt(salt);
    device.setPassword(updated);
    deviceRepository.save(device);    
  }

  @Transactional
  public void setAddress(String token, String address) throws NotFoundException, Exception {
  
    Device device = deviceRepository.findOneByPortalToken(token);
    if(device == null) { 
      throw new NotFoundException(404, "device not found");
    }
    Boolean update = false;
    if(!device.getAddress().equals(address)) {
      update = true;
    }
    Long cur = Instant.now().getEpochSecond();
    device.setAddress(address);
    device.setUpdated(cur);
    deviceRepository.save(device);

    // launch dns update
    if(update) {
      String[] cmd = { "bash", "/opt/diatum/update.sh", device.getId().toString() };
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.command(cmd);
      Process process = processBuilder.start();
    }
  }

  public void testPort(String address, Integer port) throws Exception {
    
    System.out.println("port test: " + address + ":" + port.toString());

    // launch port test
    String[] cmd = { "bash", "/opt/diatum/test.sh", address, port.toString() };
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command(cmd);
    Process process = processBuilder.start();
  }
}


