package net.kritsu.lib;

import net.kritsu.constants.FileVersion;
import net.kritsu.handler.*;
import net.kritsu.model.Album;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static net.kritsu.lib.Streams.readInt;
import static net.kritsu.lib.Streams.readLong;

public class AlbumDecoder {
    
    public static final String IMG_FLAG="Neople Img File";
    
    public static final String IMAGE_FLAG="Neople Image File";
    
    
    public static Album read(InputStream in) throws IOException {
        String flag=readString(in);
        Album album=null;
        if (IMG_FLAG.equals(flag)){
            album=new Album();
            album.setIndexLength(readLong(in));
            album.setVersion(readInt(in));
            album.setCount(readInt(in));
            initHandler(album,in);
        }
        if(IMAGE_FLAG.equals(flag)){
            album=new Album();
            album.setIndexLength(readInt(in));
            in.skip(2);
            album.setVersion(readInt(in));
            album.setCount(readInt(in));
            initHandler(album, in);
        }
        if(album==null) {
            throw new RuntimeException("Invalid File");
        }
        return  album;
    };
    
    private  static String readString(InputStream in) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        int j ;
        while ((j = in.read()) != 0 && j != -1) {
            out.write(j);
        }
        out.close();
        return new String(out.toByteArray());
    }
    
    
    private static void initHandler(Album album, InputStream in) throws IOException {
        Handler handler=null;
        switch (album.getVersion()) {
            case FileVersion.VER_01:
                handler=new FirstHandler(album, in);
                break;
            case FileVersion.VER_02:
                handler=new SecondHandler(album, in);
                break;
            case FileVersion.VER_04:
                handler=new FourthHandler(album, in);
                break;
            case FileVersion.VER_06:
                handler=new SixthHandler(album, in);
                break;
        }
        album.setHandler(handler);
    }

    
}
