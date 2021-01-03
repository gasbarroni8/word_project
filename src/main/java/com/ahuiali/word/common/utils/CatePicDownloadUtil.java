package com.ahuiali.word.common.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * CatePicDownloadUtil
 *
 * @author ZhengChaoHui
 * @date 2021/1/3 22:48
 */
public class CatePicDownloadUtil {
    //下载图片
    public static void downloadPicture(String u, String name) {
        //将下载的图片保存在 E:\spider 路径中
        String baseDir = "d:\\temp\\";
        URL url = null;
        try {
            url = new URL(u);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            File file = new File(baseDir + name);

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024 * 1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            System.out.println("已经下载：" + baseDir + name);
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
