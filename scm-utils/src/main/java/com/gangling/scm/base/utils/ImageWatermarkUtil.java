package com.gangling.scm.base.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class ImageWatermarkUtil {
    // 水印透明度
    private static float alpha = 0.3f;
    // 水印文字大小
    public static final int FONT_SIZE = 28;
    // 水印文字字体
    //private static Font font = new Font("微软雅黑", Font.BOLD, FONT_SIZE);
    // 水印文字颜色
    private static Color color = Color.white;
    // 水印之间的间隔
    private static final int XMOVE = 80;
    // 水印之间的间隔
    private static final int YMOVE = 80;

    /**
     * 获取文本长度。汉字为1:1，英文和数字为2:1
     */
    private static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     */
    public static void ImageByText(Color color, String word, Integer degree, InputStream in, OutputStream out, Font font) throws Exception {
        BufferedImage buffImg = null;
        try {
            // 源图片
            Image srcImg = ImageIO.read(in);
            int width = srcImg.getWidth(null);// 原图宽度
            int height = srcImg.getHeight(null);// 原图高度
            buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH),
                    0, 0, null);
            // 设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 设置水印文字颜色
            g.setColor(color);
            // 设置水印文字Font
            g.setFont(font);
            // 设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            int x = -width / 2;
            int y = -height / 2;
            int markWidth = FONT_SIZE * getTextLength(word);// 字体长度
            int markHeight = FONT_SIZE;// 字体高度

            // 循环添加水印
            while (x < width * 1.5) {
                y = -height / 2;
                while (y < height * 1.5) {
                    g.drawString(word, x, y);

                    y += markHeight + YMOVE;
                }
                x += markWidth + XMOVE;
            }
            // 释放资源
            g.dispose();
            //输出图片
            if (!ImageIO.write(buffImg, "jpg", out)) {// 保存图片
                throw new Exception("添加水印失败！");
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            //BufferedImage非常消耗内存，尽快GC
            buffImg = null;
        }
    }
}