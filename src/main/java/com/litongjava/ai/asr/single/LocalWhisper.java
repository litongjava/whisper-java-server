package com.litongjava.ai.asr.single;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.litongjava.ai.asr.model.WhisperSegment;
import com.litongjava.ai.asr.property.WhiserAsrProperties;
import com.litongjava.ai.asr.service.WhisperJniService;
import com.litongjava.ai.asr.utils.WhisperExecutorServiceUtils;
import com.litongjava.jfinal.aop.Aop;

import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LocalWhisper {
  INSTANCE;

  private ThreadLocal<WhisperJniService> threadLocalWhisper;
  private WhisperFullParams defaultPararams = new WhisperFullParams();

  LocalWhisper() {
    try {
      WhisperJNI.loadLibrary();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    // C:\Users\Administrator\.cache\whisper
    String userHome = System.getProperty("user.home");
    String modelName = Aop.get(WhiserAsrProperties.class).getModelName();
    Path path = Paths.get(userHome, ".cache", "whisper", modelName);

    threadLocalWhisper = ThreadLocal.withInitial(() -> {
      WhisperJniService whisper = new WhisperJniService();
      try {
        whisper.initContext(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return whisper;
    });
    defaultPararams.printProgress = false;
  }

  public List<WhisperSegment> fullTranscribeWithTime(float[] audioData, int numSamples, WhisperFullParams params) {
    Callable<List<WhisperSegment>> task = () -> {
      WhisperJniService whisper = null;
      whisper = threadLocalWhisper.get();
      if (params != null) {
        log.info("params:{}", params);
        return whisper.fullTranscribeWithTime(params, audioData, numSamples);
      } else {
        return whisper.fullTranscribeWithTime(defaultPararams, audioData, numSamples);
      }

    };

    try {
      return WhisperExecutorServiceUtils.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<WhisperSegment> fullTranscribeWithTime(float[] floats, WhisperFullParams params) {
    return fullTranscribeWithTime(floats, floats.length, params);
  }
}