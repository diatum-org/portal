package org.coredb.portal.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.coredb.portal.jpa.repository.ConfigRepository;
import org.coredb.portal.jpa.entity.Config;
import static org.coredb.portal.NameRegistry.*;

@Service
public class ConfigService {

  public class ConfigServiceValue {
    public String strValue;
    public Long numValue;
    public Boolean boolValue;
    ConfigServiceValue() { }
    ConfigServiceValue(String val) { strValue = val; }
    ConfigServiceValue(Long val) { numValue = val; }
    ConfigServiceValue(Boolean val) { boolValue = val; }
    ConfigServiceValue(String s, Long n, Boolean b) {
      this.strValue = s;
      this.numValue = n;
      this.boolValue = b;
    }
  }

  private Map<String, ConfigServiceValue> portalConfigs =
      new HashMap<String, ConfigServiceValue>();

  @Autowired
  private ConfigRepository configRepository;

  // to be invoked on configured interval
  public long refresh() {

    // refresh all server configs
    List<Config> configs = configRepository.findAll();
    portalConfigs.clear();
    for(Config config: configs) {
      String configId = config.getConfigId();

      portalConfigs.put(configId, new ConfigServiceValue(config.getStrValue(), config.getNumValue(), config.getBoolValue()));
    }
 
    return getNumValue(REFRESH_INTERVAL, (long)60).longValue();
  }

  public String getStrValue(String configId, String unset) {
    ConfigServiceValue val = getConfig(configId);
    if(val == null || val.strValue == null) {
      return unset;
    }
    else {
      return val.strValue;
    }
  }

  public Long getNumValue(String configId, Long unset) {
    ConfigServiceValue val = getConfig(configId);
    if(val == null || val.numValue == null) {
      return unset;
    }
    else {
      return val.numValue;
    }
  }

  public Boolean getBoolValue(String configId, Boolean unset) {
    ConfigServiceValue val = getConfig(configId);
    if(val == null || val.boolValue == null) {
      return unset;
    }
    else {
      return val.boolValue;
    }
  }

  public ConfigServiceValue getConfig(String configId) {

    // return value if present
    return portalConfigs.get(configId);
  }
}


