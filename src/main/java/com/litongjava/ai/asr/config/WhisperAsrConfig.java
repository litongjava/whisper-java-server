package com.litongjava.ai.asr.config;

import com.litongjava.ai.asr.property.WhiserAsrProperties;
import com.litongjava.annotation.ABean;
import com.litongjava.annotation.AConfiguration;
import com.litongjava.tio.utils.environment.EnvUtils;

import lombok.extern.slf4j.Slf4j;

@AConfiguration
@Slf4j
public class WhisperAsrConfig {

  @ABean
  public WhiserAsrProperties aiServiceProperties() {
    WhiserAsrProperties aiServiceProperties = new WhiserAsrProperties();
    String modelName = EnvUtils.get("model.name");
    if (modelName != null) {
      log.info("modelName:{}", modelName);
      aiServiceProperties.setModelName(modelName);
    }
    return aiServiceProperties;

  }
}
