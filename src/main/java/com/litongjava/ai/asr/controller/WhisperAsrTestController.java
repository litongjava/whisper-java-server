package com.litongjava.ai.asr.controller;

import java.net.URL;
import java.util.List;

import com.litongjava.ai.asr.model.WhisperSegment;
import com.litongjava.ai.asr.service.WhisperCppBaseService;
import com.litongjava.ai.asr.service.WhisperCppLargeService;
import com.litongjava.ai.asr.service.WhisperCppService;
import com.litongjava.ai.asr.service.WhisperCppTinyService;
import com.litongjava.annotation.EnableCORS;
import com.litongjava.annotation.RequestPath;
import com.litongjava.jfinal.aop.Aop;
import com.litongjava.model.resp.Resp;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.server.util.Resps;
import com.litongjava.tio.utils.hutool.ResourceUtil;

import cn.hutool.core.util.ClassUtil;
import io.github.givimad.whisperjni.WhisperFullParams;

@EnableCORS
@RequestPath("/whisper")
public class WhisperAsrTestController {
  private WhisperCppLargeService whisperCppLargeService = Aop.get(WhisperCppLargeService.class);
  private WhisperCppService whisperCppService = Aop.get(WhisperCppService.class);
  private WhisperCppTinyService whisperCppTinyService = Aop.get(WhisperCppTinyService.class);
  private WhisperCppBaseService whisperCppBaseService = Aop.get(WhisperCppBaseService.class);

  @RequestPath("/test")
  public HttpResponse test(HttpRequest request, WhisperFullParams params) {
    // String urlStr = "https://raw.githubusercontent.com/litongjava/whisper.cpp/master/samples/jfk.wav";
    URL resource = ClassUtil.getClassLoader().getResource("audios/jfk.wav");
    if (resource != null) {
      List<WhisperSegment> list = whisperCppService.index(resource, params);
      return Resps.json(request, Resp.ok(list));
    }
    return null;
  }

  @RequestPath("/test/tiny")
  public HttpResponse testTiny(HttpRequest request, WhisperFullParams params) {
    URL resource = ResourceUtil.getResource("audios/jfk.wav");
    if (resource != null) {
      List<WhisperSegment> list = whisperCppTinyService.index(resource, params);
      return Resps.json(request, Resp.ok(list));
    }
    return null;
  }

  @RequestPath("/test/base")
  public HttpResponse testBase(HttpRequest request, WhisperFullParams params) {
    // String urlStr = "https://raw.githubusercontent.com/litongjava/whisper.cpp/master/samples/jfk.wav";
    URL resource = ClassUtil.getClassLoader().getResource("audios/jfk.wav");
    if (resource != null) {
      List<WhisperSegment> list = whisperCppBaseService.index(resource, params);
      return Resps.json(request, Resp.ok(list));
    }
    return null;
  }

  @RequestPath("/test/large")
  public HttpResponse testLarge(HttpRequest request, WhisperFullParams params) {
    // String urlStr = "https://raw.githubusercontent.com/litongjava/whisper.cpp/master/samples/jfk.wav";
    URL resource = ClassUtil.getClassLoader().getResource("audios/jfk.wav");
    if (resource != null) {
      List<WhisperSegment> list = whisperCppLargeService.index(resource, params);
      return Resps.json(request, Resp.ok(list));
    }
    return null;
  }
}
