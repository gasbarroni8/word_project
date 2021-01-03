package com.ahuiali.word.common.utils;

import org.springframework.stereotype.Component;

/**
 * ImagePathUtil
 *
 * @author ZhengChaoHui
 * @date 2021/1/3 22:53
 */
@Component
public class ImagePathUtil {

    public static String getImageName(String url) {

        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
