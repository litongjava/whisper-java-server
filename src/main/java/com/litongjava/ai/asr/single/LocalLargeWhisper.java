package com.litongjava.ai.asr.single;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.litongjava.ai.asr.model.WhisperSegment;
import com.litongjava.ai.asr.service.WhisperJniService;

import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum LocalLargeWhisper {
  INSTANCE;

  private ExecutorService executorService;
  private ThreadLocal<WhisperJniService> threadLocalWhisper;
  private WhisperFullParams defaultPararams = new WhisperFullParams();

  LocalLargeWhisper() {
    try {
      WhisperJNI.loadLibrary();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    // C:\Users\Administrator\.cache\whisper
    String userHome = System.getProperty("user.home");
    String modelName = "ggml-large-v3-turbo.bin";
    Path path = Paths.get(userHome, ".cache", "whisper", modelName);

    this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
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
      return executorService.submit(task).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<WhisperSegment> fullTranscribeWithTime(float[] floats, WhisperFullParams params) {
    return fullTranscribeWithTime(floats, floats.length, params);
  }
}