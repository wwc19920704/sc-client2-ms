package com.wwc.spring.cloud.client2.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * Java语音工具类
 * 第一步：把文件夹中  jacob-1.18-x64.dll 文件放到 jdk-bin目录下
 *
 * 第二步：在项目的pom.xml这添加maven坐标依赖
 * maven依赖：
 *     文字转语音依赖
 *     <dependency>
 *         <groupId>com.hynnet</groupId>
 *         <artifactId>jacob</artifactId>
 *         <version>1.18</version>
 *     </dependency>
 */
public class VoiceUtils {

    public static void main(String[] args) {
        textToSpeech("那你 说说看 咱去哪里玩?","D:/file");
        System.err.println("---------------结束");
    }

    /**
     * @author
     * @date: 2019年
     * 文字转语音并生成语音文件方法
     * input：	data：需要转的文字对象，path：语音文件保存位置对象
     */
    public static void textToSpeech(String data,String path) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");

            // 运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            // 音量 0-100
            ax.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            ax.setProperty("Rate", new Variant(0));
            // 执行朗读
//            Dispatch.call(spVoice, "Speak", new Variant(data));

            // 下面是构建文件流把生成语音文件
            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            // 设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            // 设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            // 调用输出 文件流打开方法，创建一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant(path+"/voice.mp3"), new Variant(3), new Variant(true));
            // 设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            // 设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            // 设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(0));
            // 开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(data));

            // 关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);
            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
