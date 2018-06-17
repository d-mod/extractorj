package net.kritsu.lib;

import net.kritsu.constants.ColorBits;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Colors {
    public static List<byte[]> readPalette(InputStream in, int count) throws IOException {
        List<byte[]> palette=new ArrayList<>();
        for (int i=0; i < count; i++) {
            byte[] data=new byte[4];
            in.read(data);
            palette.add(data);
        }
        return palette;
    }
    
    static byte[] readColor(InputStream in, int bits) throws IOException {
        if (bits == ColorBits.ARGB_8888) {
            byte[] bs=new byte[4];
            in.read(bs);
            return bs;
        }
        byte a=0;
        byte r=0;
        byte g=0;
        byte b=0;
        byte[] bs=new byte[2];
        in.read(bs);
        switch (bits) {
            case ColorBits.ARGB_1555:
                a=(byte) (bs[1] >> 7);
                r=(byte) ((bs[1] >> 2) & 0x1f);
                g=(byte) ((bs[0] >> 5) | (bs[1] & 3) << 3);
                b=(byte) (bs[0] & 0x1f);
                a=(byte) (a * 0xff);
                r=(byte) (r << 3 | r >> 2);
                g=(byte) (g << 3 | g >> 2);
                b=(byte) (b << 3 | b >> 2);
                break;
            case ColorBits.ARGB_4444:
                a=(byte) (bs[1] & 0xf0);
                r=(byte) ((bs[1] & 0xf) << 4);
                g=(byte) (bs[0] & 0xf0);
                b=(byte) ((bs[0] & 0xf) << 4);
                break;
        }
        return new byte[]{b, g, r, a};
    }
    
    
}
