package com.litongjava.ai.server.utils;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import io.github.givimad.whisperjni.WhisperContext;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import io.github.givimad.whisperjni.WhisperSamplingStrategy;

public class WhisperSpeechRecognitionExample {

  public static void main(String[] args) throws Exception {
    // 加载模型文件
    Path modelFile = Paths.get("ggml-large-v3-turbo.bin");
    File file = modelFile.toFile();
    if (!file.exists() || !file.isFile()) {
      throw new RuntimeException("缺少模型文件: " + file.getAbsolutePath());
    }

    // 加载音频文件
    Path audioFilePath = Paths.get("samples/jfk.wav");
    File sampleFile = audioFilePath.toFile();
    if (!sampleFile.exists() || !sampleFile.isFile()) {
      throw new RuntimeException("缺少音频样本文件");
    }

    // 初始化 Whisper 库
    WhisperJNI.LoadOptions loadOptions = new WhisperJNI.LoadOptions();
    loadOptions.logger = System.out::println;
    WhisperJNI.loadLibrary(loadOptions);
    WhisperJNI.setLibraryLogger(null);

    WhisperJNI whisper = new WhisperJNI();
    WhisperContext ctx = whisper.init(modelFile);

    // 读取音频样本数据
    float[] samples = readAudioSamples(sampleFile);

    // 设置推理参数
    WhisperFullParams params = new WhisperFullParams(WhisperSamplingStrategy.GREEDY);
    int result = whisper.full(ctx, params, samples, samples.length);

    if (result != 0) {
      throw new RuntimeException("识别失败，错误码: " + result);
    }

    // 输出识别结果
    String transcription = whisper.fullGetSegmentText(ctx, 0);
    System.out.println("识别结果: " + transcription);

    ctx.close();
  }

  // 读取音频样本数据
  private static float[] readAudioSamples(File audioFile) throws UnsupportedAudioFileException, IOException {
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

    ByteBuffer captureBuffer = ByteBuffer.allocate(audioInputStream.available());
    captureBuffer.order(ByteOrder.LITTLE_ENDIAN);

    int read = audioInputStream.read(captureBuffer.array());
    if (read == -1) {
      throw new IOException("文件为空");
    }

    ShortBuffer shortBuffer = captureBuffer.asShortBuffer();
    float[] samples = new float[captureBuffer.capacity() / 2];
    int i = 0;
    while (shortBuffer.hasRemaining()) {
      samples[i++] = Math.max(-1f, Math.min(((float) shortBuffer.get()) / (float) Short.MAX_VALUE, 1f));
    }
    return samples;
  }
}
