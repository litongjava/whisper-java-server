package com.litongjava.ai.asr.controller;

import com.litongjava.annotation.EnableCORS;
import com.litongjava.annotation.RequestPath;
import com.litongjava.jfinal.aop.Aop;
import com.litongjava.model.resp.Resp;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.server.util.Resps;
import com.litongjava.tio.utils.environment.EnvUtils;

@EnableCORS
@RequestPath("/env")
public class EnviormentController {
  @RequestPath("/{key}")
  public HttpResponse get(String key, HttpRequest request) {
    return Resps.json(request, Resp.ok(EnvUtils.get(key)));
  }

  @RequestPath("/beans")
  public HttpResponse beans(HttpRequest request) {
    String[] beans = Aop.beans();
    return Resps.json(request, Resp.ok(beans));

  }
}
