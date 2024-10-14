package com.litongjava.ai.asr;

import com.litongjava.annotation.AComponentScan;
import com.litongjava.tio.boot.TioApplication;

@AComponentScan
public class WhisperAsrServer {

  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    TioApplication.run(WhisperAsrServer.class, args);
    long end = System.currentTimeMillis();
    System.out.println("started:" + (end - start) + "(ms)");
  }
}