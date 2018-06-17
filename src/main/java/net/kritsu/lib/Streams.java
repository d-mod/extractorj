package net.kritsu.lib;

import java.io.IOException;
import java.io.InputStream;

public class Streams {
    public static int readInt(InputStream in) throws IOException {
        return (int) readNumber(in, 4);
    }
    
    public static long readLong(InputStream in) throws IOException {
        return  readNumber(in,8);
    }
    
    private static long readNumber(InputStream in,int len) throws IOException {
        byte[] buf=new byte[len];
        in.read(buf);
        long rs=0;
        for (int i=0; i < buf.length; i++) {
            rs|=(buf[i] & 0xff) << (i * 8);
        }
        return rs;
    }
}
