package com.litongjava.ai.asr.controller;

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
import com.litongjava.tio.http.common.UploadFile;
import com.litongjava.tio.http.server.util.Resps;

import io.github.givimad.whisperjni.WhisperFullParams;

@EnableCORS
@RequestPath("/whisper")
public class WhisperAsrController {
  private WhisperCppService whisperCppService = Aop.get(WhisperCppService.class);
  private WhisperCppTinyService whisperCppTinyService = Aop.get(WhisperCppTinyService.class);
  private WhisperCppBaseService whisperCppBaseService = Aop.get(WhisperCppBaseService.class);
  private WhisperCppLargeService whisperCppLargeService = Aop.get(WhisperCppLargeService.class);

  @RequestPath(value = "/transcribe")
  public HttpResponse index(HttpRequest request, UploadFile file, String inputType, String outputType, String outputFormat, WhisperFullParams params) throws Exception {

    if (file != null) {
      Object data = whisperCppService.index(file.getData(), inputType, outputType, params);
      if ("txt".equals(outputFormat)) {
        if (data instanceof String) {
          return Resps.txt(request, (String) data);
        }

      } else {
        return Resps.json(request, Resp.ok(data));
      }

    } else {
      return Resps.json(request, Resp.fail("uplod file can't be null"));
    }
    return Resps.json(request, Resp.fail("unknow error"));
  }

  @RequestPath(value = "/transcribe/tiny")
  public HttpResponse tiny(HttpRequest request, UploadFile file, String inputType, String outputType, String outputFormat, WhisperFullParams params) throws Exception {
    if (file != null) {
      Object data = whisperCppTinyService.index(file.getData(), inputType, outputType, params);
      if ("txt".equals(outputFormat)) {
        if (data instanceof String) {
          return Resps.txt(request, (String) data);
        }

      } else {
        return Resps.json(request, Resp.ok(data));
      }

    } else {
      return Resps.json(request, Resp.fail("uplod file can't be null"));
    }
    return Resps.json(request, Resp.fail("unknow error"));
  }

  @RequestPath(value = "/transcribe/base")
  public HttpResponse recBase(HttpRequest request, UploadFile file, String inputType, String outputType, String outputFormat, WhisperFullParams params) throws Exception {
    if (file != null) {
      Object data = whisperCppBaseService.index(file.getData(), inputType, outputType, params);
      if ("txt".equals(outputFormat)) {
        if (data instanceof String) {
          return Resps.txt(request, (String) data);
        }

      } else {
        return Resps.json(request, Resp.ok(data));
      }

    } else {
      return Resps.json(request, Resp.fail("uplod file can't be null"));
    }
    return Resps.json(request, Resp.fail("unknow error"));
  }

  @RequestPath(value = "/transcribe/large")
  public HttpResponse recLarge(UploadFile file, String inputType, String outputType, String outputFormat, HttpRequest request, WhisperFullParams params) throws Exception {
    if (file != null) {
      Object data = whisperCppLargeService.index(file.getData(), inputType, outputType, params);
      if ("txt".equals(outputFormat)) {
        if (data instanceof String) {
          return Resps.txt(request, (String) data);
        }

      } else {
        return Resps.json(request, Resp.ok(data));
      }

    } else {
      return Resps.json(request, Resp.fail("uplod file can't be null"));
    }
    return Resps.json(request, Resp.fail("unknow error"));
  }
}
