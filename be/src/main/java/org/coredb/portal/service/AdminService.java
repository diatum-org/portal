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

import org.coredb.portal.api.NotFoundException;

import org.coredb.portal.model.Domain;
import org.coredb.portal.model.PortalStat;
import org.coredb.portal.model.PortalConfig;

import org.coredb.portal.jpa.entity.Stat;
import org.coredb.portal.jpa.entity.Config;
import org.coredb.portal.jpa.entity.Zone;

import org.coredb.portal.service.ConfigService;

import org.coredb.portal.jpa.repository.StatRepository;
import org.coredb.portal.jpa.repository.ConfigRepository;
import org.coredb.portal.jpa.repository.DeviceRepository;
import org.coredb.portal.jpa.repository.AccountRepository;
import org.coredb.portal.jpa.repository.ZoneRepository;

@Service
public class AdminService {

  @Autowired
  private StatRepository statRepository;

  @Autowired
  private ConfigRepository configRepository;

  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ZoneRepository zoneRepository;

  @Autowired
  private ConfigService configService;

  @Autowired
  private DeviceService deviceService;

  private long requests = (long)0;

  public void incrementRequests() {
    requests++;
  }

  public PortalConfig getConfig() {

    // retrieve all config values
    PortalConfig portalConfig = new PortalConfig();
    List<Config> configs = configRepository.findAll();
    for(Config config: configs) {
      if(config.getConfigId().equals(CONFIG_EMIGONODE)) {
        portalConfig.setEmigoNode(config.getStrValue());
      }
      if(config.getConfigId().equals(CONFIG_EMIGOTOKEN)) {
        portalConfig.setEmigoToken(config.getStrValue());
      }
      if(config.getConfigId().equals(CONFIG_EMIGOREGISTRY)) {
        portalConfig.setDefaultRegistry(config.getStrValue());
      }
    }

    // retreive domains
    List<Zone> zones = zoneRepository.findAll();
    List<Domain> domains = new ArrayList<Domain>();
    for(Zone zone: zones) {
      domains.add(zone);
    }
    portalConfig.setDomains(domains);

    return portalConfig;
  }

  @Transactional
  public void setConfigRegistry(String registry) {

    Config emigoRegistry = configRepository.findOneByConfigId(CONFIG_EMIGOREGISTRY);
    if(emigoRegistry == null) {
      emigoRegistry = new Config();
      emigoRegistry.setConfigId(CONFIG_EMIGOREGISTRY);
    }
    emigoRegistry.setStrValue(registry);
    configRepository.save(emigoRegistry); 
  }

  @Transactional
  public void setConfigNode(String node) {

    Config emigoNode = configRepository.findOneByConfigId(CONFIG_EMIGONODE);
    if(emigoNode == null) {
      emigoNode = new Config();
      emigoNode.setConfigId(CONFIG_EMIGONODE);
    }
    emigoNode.setStrValue(node);
    configRepository.save(emigoNode); 
  }

  @Transactional
  public void setConfigToken(String token) {

    Config emigoToken = configRepository.findOneByConfigId(CONFIG_EMIGOTOKEN);
    if(emigoToken == null) {
      emigoToken = new Config();
      emigoToken.setConfigId(CONFIG_EMIGOTOKEN);
    }
    emigoToken.setStrValue(token);
    configRepository.save(emigoToken);    
  }

  @Transactional
  public Domain addDomain() {
    Zone zone = new Zone();
    zone.setEnabled(false);
    return zoneRepository.save(zone);
  }

  @Transactional
  public Domain updateDomain(Domain domain) throws NotFoundException {
    Zone zone = zoneRepository.findOneById(domain.getId());
    if(zone == null) {
      throw new NotFoundException(404, "domain not found");
    }
    zone.setName(domain.getName());
    zone.setRegion(domain.getRegion());
    zone.setKeyValue(domain.getKey());
    zone.setKeyId(domain.getKeyId());
    zone.setZone(domain.getZoneId());
    zone.setEnabled(domain.isEnabled());
    return zoneRepository.save(zone);
  }

  public List<PortalStat> getStats() {
    
    List<Stat> stats = statRepository.findAllByOrderByTimestampDesc();
    
    @SuppressWarnings("unchecked")
    List<PortalStat> portalStats = (List<PortalStat>)(List<?>)stats;

    return portalStats;
  }

  @Transactional
  public void addStat(Integer processor, Long memory, Long storage) {

    Stat stat = new Stat();
    Date date = new Date();
    stat.setTimestamp((int)(date.getTime()/1000));
    stat.setProcessor(processor);
    stat.setMemory(memory);
    stat.setStorage(storage);
    stat.setRequests(requests);
    stat.setAccounts(accountRepository.count());
    stat.setDevices(deviceRepository.count());
    stat.setCerts(deviceService.getAvailable());
    statRepository.save(stat);
  
    // reset request count
    requests = 0;

    List<Stat> stats = statRepository.findAllByOrderByTimestampDesc();
    if(stats.size() > STAT_COUNT) {
      statRepository.deleteByTimestampLessThanEqual(stats.get(STAT_COUNT).getTimestamp());
    }
  }
  
}

