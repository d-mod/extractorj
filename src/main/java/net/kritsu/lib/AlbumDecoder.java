package net.kritsu.lib;

import net.kritsu.constants.FileVersion;
import net.kritsu.exception.FileInvalidException;
import net.kritsu.handler.*;
import net.kritsu.model.Album;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static net.kritsu.lib.Streams.readInt;
import static net.kritsu.lib.Streams.readLong;

public class AlbumDecoder {
    public static final String NPK_FlAG = "NeoplePack_Bill";
    
    public static final String IMG_FLAG="Neople Img File";
    
    public static final String IMAGE_FLAG="Neople Image File";
    
    private static char[] key;
    
    static {
        key=new char[256];
        char[] temp="puchikon@neople dungeon and fighter ".toCharArray();
        System.arraycopy(temp, 0, key, 0, temp.length);
        char[] ds={'D', 'N', 'F'};
        for (int i=temp.length; i < 255; i++) {
            key[i]=ds[i % 3];
        }
        key[255]='\0';
    }

    public static List<Album> readNpk(InputStream in) throws IOException {
        List<Album> list=readInfo(in);
        for (Album album : list) {
            read(in, album);
        }
        return list;
    }

    
    public static Album read(InputStream in,Album album) throws IOException {
        String flag=readString(in);
        if (IMG_FLAG.equals(flag)){
            if(album==null){
                album=new Album();
            }
            album.setIndexLength(readLong(in));
            album.setVersion(readInt(in));
            album.setCount(readInt(in));
            initHandler(album,in);
        }
        if(IMAGE_FLAG.equals(flag)){
            if(album==null){
                album=new Album();
            }
            album.setIndexLength(readInt(in));
            in.skip(2);
            album.setVersion(readInt(in));
            album.setCount(readInt(in));
            initHandler(album, in);
        }
        if(album==null) {
            throw new FileInvalidException("File is invalid");
        }
        return  album;
    };
    
    public static  Album read(InputStream in) throws IOException {
        return read(in, null);
    }
    
    
    private static List<Album> readInfo(InputStream in) throws IOException {
        String flag=readString(in);
        List<Album> list=new ArrayList<>();
        if (!NPK_FlAG.equals(flag)) {
            return list;
        }
        int count=readInt(in);
        for (int i=0; i < count; i++) {
            Album album=new Album();
            album.setOffset(readInt(in));
            album.setLength(readInt(in));
            album.setPath(readPath(in));
            list.add(album);
        }
        if (count > 0) {
            in.skip(32);
        }
        return list;
    }
    
    
    private static String readPath(InputStream in) throws IOException {
        byte[] data=new byte[256];
        int i=0;
        while (i < 256) {
            data[i]=(byte) (in.read() ^ key[i]);
            if (data[i] == 0) {
                break;
            }
            i++;
        }
        in.skip(255 - i);//防止因加密导致的文件名读取错误
        return new String(data).replace("\0", "");
    }
    
    
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
