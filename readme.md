# whisper-java-server

## 简介

`whisper-java-server` 是一个基于 Whisper 模型的 Java 语音识别服务器。通过封装 `WhisperJNI`，它提供了高效、线程安全的音频转录服务，支持多种音频格式和输出文本格式，适用于构建高并发的语音识别应用。

## 安装

### 1. 下载模型

前往 [Whisper.cpp 模型库](https://huggingface.co/ggerganov/whisper.cpp/tree/main) 下载所需的模型文件。

将下载的模型文件保存到以下路径：

```
~/.cache/whisper
```

### 2. 安装 `jar-boot`

在终端中执行以下命令安装 `jar-boot`：

```bash
cd /usr/bin
wget https://github.com/litongjava/jar-boot/releases/download/v1.0/jar-boot
chmod u+x jar-boot
```

### 3. 启动服务器

创建应用目录并启动服务器：

```bash
mkdir /data/apps/whisper-java-server
cd /data/apps/whisper-java-server
jar-boot -jar --fork whisper-java-server-1.0.0.jar
```

## API 文档

### 在线文档

访问 [APIFox 在线文档](https://apifox.com/apidoc/shared-98cc5675-f1a3-4250-a940-cfe060854ef4/api-121475073) 查看详细的 API 接口说明。

### 测试接口

测试接口无需上传文件，使用服务器内部自带的音频文件进行识别。

- 测试 Tiny 模型：

  ```
  http://localhost/whisper/test/tiny
  ```

- 测试 Base 模型：

  ```
  http://localhost/whisper/test/base
  ```

- 测试 Large 模型：

  ```
  http://localhost/whisper/test/large
  ```

**访问示例：**

```
http://localhost/whisper/test/tiny
```

**示例响应：**

```json
{
    "data": [
        {
            "end": 1088,
            "sentence": "And so, my fellow Americans, ask not what your country can do for you, ask what you can do for your country.",
            "start": 0
        }
    ],
    "ok": true
}
```

### 识别接口

识别接口需要上传音频文件进行转录。

**访问地址：**

- 通用转录接口：

  ```
  http://localhost/whisper/transcribe
  ```

- 指定模型类型：

  ```
  http://localhost/whisper/transcribe/tiny
  http://localhost/whisper/transcribe/base
  http://localhost/whisper/transcribe/large
  ```

#### POST `/whisper/asr/test`

**请求体参数：**

```yaml
file: filedata        # 必须，上传的音频文件（二进制）
inputType: wav        # 可选，上传的音频格式，支持 wav 和 mp3
outputType: default   # 可选，返回的文本格式，支持 default、lrc、vtt、srt
outputFormat: json    # 可选，返回的数据格式，默认是 json，如果需要字幕数据输出为 txt，请设置为 txt
```

**请求参数说明：**

| 参数名称      | 位置  | 类型           | 是否必需 | 描述                              |
| ------------- | ----- | -------------- | -------- | --------------------------------- |
| `file`        | body  | string (binary)| 是       | 上传的音频文件                    |
| `inputType`   | body  | string         | 否       | 上传的音频格式，支持 `wav` 和 `mp3` |
| `outputType`  | body  | string         | 否       | 返回的文本格式，支持 `default`、`lrc`、`vtt`、`srt` |
| `outputFormat`| body  | string         | 否       | 返回的数据格式，默认是 `json`，如果需要字幕数据输出为 `txt` |

**响应示例：**

**成功响应：**

```json
{
  "data": [
    {
      "end": 800,
      "sentence": "And so my fellow Americans, ask not what your country can do for you.",
      "start": 0
    },
    {
      "end": 1100,
      "sentence": "Ask what you can do for your country.",
      "start": 800
    }
  ],
  "ok": true
}
```

## 构建

### 依赖

确保系统中安装了所需的依赖库，尤其是 `libstdc++.so.6`，需要版本 `GLIBCXX_3.4.21` 或更高版本。

#### 构建步骤

1. 设置 Java 版本环境变量：

   ```bash
   # 对于 Linux/macOS
   export JAVA_HOME=/usr/java/jdk-11.0.8
   export PATH=$JAVA_HOME/bin:$PATH

   # 对于 Windows (在命令提示符中)
   set JAVA_HOME=D:\java\jdk1.8.0_121
   set PATH=%JAVA_HOME%\bin;%PATH%
   ```

2. 构建项目：

   ```bash
   # 清理并安装依赖，跳过测试和 GPG 签名，使用开发配置
   mvn clean install -DskipTests -Dgpg.skip -Pdevelopment

   # 打包生产版本，跳过测试和 GPG 签名
   mvn clean package -DskipTests -Dgpg.skip -Pproduction
   ```

## 运行

使用以下命令启动服务器：

```bash
java -jar whisper-asr-server/target/whisper-asr-server-1.0.2.jar
```

**默认加载的模型路径：**

```
/root/.cache/whisper/ggml-base.en.bin
```

**指定模型名称启动服务器：**

```bash
java -jar whisper-asr-server/target/whisper-asr-server-1.0.2.jar --model.name=ggml-large.bin
```

## 音频文件转换

注意，服务器当前仅支持 16 位音频文件，因此在运行工具前请确保转换音频文件格式。可以使用 `ffmpeg` 进行转换，例如：

```bash
ffmpeg -i input.mp3 -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

```bash
ffmpeg -i input.mp4 -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

## Docker

#### 构建 Docker 镜像

执行以下命令构建 Docker 镜像：

```bash
docker build -f docker/1.0.2 -t litongjava/whisper-asr-server:1.0.2 .
```

#### 运行 Docker 容器

使用以下命令运行 Docker 容器：

```bash
docker run -dit -p 8080:80 litongjava/whisper-asr-server:1.0.2
```

#### 测试 Docker 容器

使用 `curl` 测试接口是否正常运行：

```bash
curl -v http://localhost:8080/whisper/asr/test
```

### 注意事项

- **模型下载**：确保从 [Hugging Face](https://huggingface.co/ggerganov/whisper.cpp) 下载合适的 Whisper 模型，并放置在 `~/.cache/whisper` 目录下。
- **音频格式**：服务器仅支持 16 位的单声道音频文件。使用 `ffmpeg` 进行必要的格式转换。
- **资源配置**：根据服务器的硬件配置（如 CPU 核心数），合理配置线程池，以优化并发处理性能。

### 常见问题

1. **启动时报错 `GLIBCXX_3.4.21` 未找到**

   这是因为系统中 `libstdc++.so.6` 的版本过低。可以通过以下命令检查当前版本：

   ```bash
   strings /usr/lib/x86_64-linux-gnu/libstdc++.so.6 | grep GLIBCXX
   ```

   如果版本不足，请更新 `libstdc++` 或升级系统。

2. **内存不足导致转录失败**

   Whisper 模型对内存有一定要求，尤其是较大的模型。请确保服务器有足够的内存资源，或选择较小的模型进行部署。

3. **音频转换失败**

   确保已安装 `ffmpeg` 并配置在系统 `PATH` 中。可以通过以下命令验证：

   ```bash
   ffmpeg -version
   ```

## 贡献

欢迎提交问题和贡献代码！请访问 [GitHub 仓库](https://github.com/litongjava/whisper-java-server) 获取更多信息。

## 许可证

`whisper-java-server` 遵循 [MIT 许可证](https://opensource.org/licenses/MIT)。详细信息请参阅仓库中的 `LICENSE` 文件。