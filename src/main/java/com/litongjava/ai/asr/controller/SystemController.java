package com.litongjava.ai.asr.controller;

import java.util.Properties;

import com.litongjava.annotation.EnableCORS;
import com.litongjava.annotation.RequestPath;
import com.litongjava.model.resp.Resp;
import com.litongjava.tio.http.common.HttpRequest;
import com.litongjava.tio.http.common.HttpResponse;
import com.litongjava.tio.http.server.util.Resps;

import cn.hutool.core.util.SystemPropsUtil;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;

@EnableCORS
@RequestPath("/system")
public class SystemController {

  @RequestPath("/props")
  public HttpResponse props(HttpRequest request) {
    Properties props = SystemPropsUtil.getProps();
    return Resps.json(request, Resp.ok(props));
  }

  @RequestPath("/runtimeInfo")
  public HttpResponse runtimeInfo(HttpRequest request) {
    RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
    return Resps.json(request, Resp.ok(runtimeInfo));
  }

  @RequestPath("/availableProcessors")
  public HttpResponse availableProcessors(HttpRequest request) {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    return Resps.json(request, Resp.ok(availableProcessors));
  }

}
