package net.kritsu.lib;

import net.kritsu.constants.ColorBits;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static net.kritsu.lib.Colors.readColor;

public class Bitmaps {
    
    public static byte[] createFromArray(byte[] data, int width, int height, int bits) throws IOException {
        InputStream ms=new ByteArrayInputStream(data);
        BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            for (int y=0; y < height; y++) {
                for (int x=0; x < width; x++) {
                    byte[] temp=readColor(ms, bits);
                    int r=Byte.toUnsignedInt(temp[2]);
                    int g=Byte.toUnsignedInt(temp[1]);
                    int b=Byte.toUnsignedInt(temp[0]);
                    int a=Byte.toUnsignedInt(temp[3]);
                    Color rgba=new Color(r, g, b, a);
                    image.setRGB(x, y, rgba.getRGB());
                }
            }
        ms.close();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        out.close();
        return out.toByteArray();
    }
    
    public static void main(String[] args) throws IOException {
        BufferedImage image=ImageIO.read(new File("d:/test.png"));
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        for(int x=0;x<image.getWidth();x++) {
            for (int y=0; y < image.getHeight(); y++) {
                int rgba=image.getRGB(x, y);
                byte a=(byte) (rgba >> 24);
                byte r=(byte) ((rgba >> 16) & 0xff);
                byte g=(byte) ((rgba >> 8) & 0xff);
                byte b=(byte) ((rgba) & 0xff);
                out.write(new byte[]{b, g, r, a});
            }
        }
        out.close();
        byte[] data=out.toByteArray();
        data=createFromArray(data,image.getWidth(),image.getHeight(),ColorBits.ARGB_8888);
        OutputStream fs=new FileOutputStream("d:/test1.png");
        fs.write(data);
        fs.close();
    }
}
