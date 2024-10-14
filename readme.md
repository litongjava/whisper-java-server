## whisper-java-server
## 简介


## 安装
下载模型
下载地址 https://huggingface.co/ggerganov/whisper.cpp/tree/main
保存地址
```
~/.cache/whisper
```
安装java-boot
cd /usr/bin
wget https://github.com/litongjava/java-boot/releases/download/v1.0/java-boot
chmod u+x java-boot 

启动

mkdir /data/apps/whisper-java-server
cd /data/apps/whisper-java-server
## api文档
### 在线文档
https://apifox.com/apidoc/shared-98cc5675-f1a3-4250-a940-cfe060854ef4/api-121475073

### 测试接口
测试接口无需上传文件,使用内部自带文件进行识别
http://localhost/whispser/test/tiny
http://localhost/whispser/test/base
http://localhost/whispser/test/large

访问地址:http://localhost/whispser/test/tiny
返回数据
```
{
    "data": [
        {
            "end": 1088,
            "sentence": " And so, my fellow Americans, ask not what your country can do for you, ask what you can do for your country.",
            "start": 0
        }
    ],
    "ok": true
}
```
### 识别接口
识别接口需要上传文件音频文件

访问地址:
http://localhost/whispser/transcribe
http://localhost/whispser/transcribe/tiny
http://localhost/whispser/transcribe/base
http://localhost/whispser/transcribe/large

#### POST /whispser/asr/test

> Body Parameters

```yaml
file: filedata
inputType: wav
outputType: default

```

#### Params

|Name|Location|Type|Required|Description|
|---|---|---|---|---|
|body|body|object| no |none|
|» file|body|string(binary)| yes |上传的音频文件|
|» inputType|body|string| no |上传的音频格式wav和mp3|
|» outputType|body|string| no |返回的文本格式,支持default,irc,vtt,srt|
|» outputFormat|body|string| no |返回的数据格式,输出的格式,默认是json,如果需要字幕数据输出txt|

> Response Examples

> 成功

```json
{
  "data": [
    {
      "end": 800,
      "sentence": " And so my fellow Americans, ask not what your country can do for you.",
      "start": 0
    },
    {
      "end": 1100,
      "sentence": " Ask what you can do for your country.",
      "start": 800
    }
  ],
  "ok": true
}
```
### build

required
/usr/lib/x86_64-linux-gnu/libstdc++.so.6: version `GLIBCXX_3.4.21'

build
```
# Set java version
export JAVA_HOME=/usr/java/jdk-11.0.8
export PATH=$JAVA_HOME/bin:$PATH

#build jar
set JAVA_HOME=D:\\java\\jdk1.8.0_121
mvn clean install -DskipTests -Dgpg.skip -Pdevelopment
mvn clean package -DskipTests -Dgpg.skip -Pproduction
```
### run
```
java -jar whisper-asr-server/target/whisper-asr-server-1.0.2.jar
```
The default loaded model is `/root/.cache/whisper/ggml-base.en.bin`
downlaod model form huggingface https://huggingface.co/ggerganov/whisper.cpp


specify the model name
```
java -jar whisper-asr-server/target/whisper-asr-server-1.0.2.jar --model.name=ggml-large.bin
```

### convert file with ffmpeg
Note that the server currently runs only with 16-bit audio files, so make sure to convert your input before running the tool.
For example, you can use `ffmpeg` like this:

```java
ffmpeg -i input.mp3 -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

```
ffmpeg -i input.mp4 -ar 16000 -ac 1 -c:a pcm_s16le output.wav
```

### Docker

build

```
docker build -f docker/1.0.2 -t litongjava/whisper-asr-server:1.0.2 .
```

run

```
docker run -dit -p 8080:80 litongjava/whisper-asr-server:1.0.2
```

test
```
curl -v http://localhost:8080/whispser/asr/test
```