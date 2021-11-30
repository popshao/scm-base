package com.gangling.scm.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

/**
 * .
 * @date: Created by gangling on 12:02 2018/10/16.
 */
@Slf4j
public class CloseAble {

    /**
     * 关闭输入输出流
     * @param closeAbles
     */
    public static void close(Closeable... closeAbles){
        if (closeAbles == null || closeAbles.length <= 0) {
            return;
        }
        for (Closeable closeAble : closeAbles) {
            if (closeAble != null) {
                try {
                    closeAble.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
