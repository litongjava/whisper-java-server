package com.litongjava.ai.asr.controller;

import com.litongjava.annotation.EnableCORS;
import com.litongjava.annotation.RequestPath;

@EnableCORS
@RequestPath(value = "/")
public class IndexController {
  @RequestPath()
  public String respText() {
    return "whisper-asr-server";
  }
}