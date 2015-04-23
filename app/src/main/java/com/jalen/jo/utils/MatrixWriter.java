package com.jalen.jo.utils;

import android.graphics.Bitmap;

import com.google.zxing.common.BitMatrix;

/**
 * Created by jalenzhang on 2015/4/19.
 */
public class MatrixWriter {
    private static final int RGB_BLACK = 0xFF000000;    // RGB black
    private static final int RGB_WHITE = 0xFFFFFFFF;    // RGBֵΪwhite

    private MatrixWriter() {}

    public static Bitmap toBitmap(BitMatrix matrix){
        Bitmap bmp = null;
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        // ���ص㼯��
        int[] pixels = new int[width * height];
        for (int y=0; y<width; y++){
            for (int x=0; x<height; x++){
                if (matrix.get(x,y)){
                    // Ϊ1��˵���Ǻڵ�
                    pixels[y*width+x] = RGB_BLACK;
                }else {
                    // Ϊ0��˵���ǰ׵�
                    pixels[y*width+x] = RGB_WHITE;
                }
            }
        }
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

/*    public Bitmap createBitmap(String str) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
            int width = matrix.width;
            int height = matrix.height;
            int[] pixels = new int[width*height];
            for(int y = 0; y<width; ++y){
                for(int x = 0; x<height; ++x){
                    if(matrix.get(x, y)){
                        pixels[y*width+x] = 0xff000000; // black pixel
                    } else {
                        pixels[y*width+x] = 0xffffffff; // white pixel
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }*/
/*
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }


    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }


    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
*/


}
