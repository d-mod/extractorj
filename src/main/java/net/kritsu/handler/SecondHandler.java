package net.kritsu.handler;

import net.kritsu.constants.ColorBits;
import net.kritsu.constants.CompressMode;
import net.kritsu.lib.Bitmaps;
import net.kritsu.lib.Zlibs;
import net.kritsu.model.Album;
import net.kritsu.model.Sprite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kritsu.lib.Streams.readInt;

/**
 *
 *  Ver2 IMG处理器
 * @author kritsu
 */
public class SecondHandler implements Handler{
    protected Album album;
    
    public SecondHandler(Album album,InputStream in) throws IOException {
        this.album=album;
        init(in);
    }
    
    public void init(InputStream in) throws IOException {
        List<Sprite> list=new ArrayList<>();
        for (int i=0; i < album.getCount(); i++) {
            Sprite sprite=new Sprite();
            sprite.setColorBits(readInt(in));
            list.add(sprite);
            if (sprite.getColorBits() == ColorBits.LINK) {
                sprite.setTargetIndex(readInt(in));
                continue;
            }
            sprite.setCompressMode(readInt(in));
            sprite.setWidth(readInt(in));
            sprite.setHeight(readInt(in));
            sprite.setLength(readInt(in));
            sprite.setX(readInt(in));
            sprite.setY(readInt(in));
            sprite.setFrameWidth(readInt(in));
            sprite.setFrameHeight(readInt(in));
        }
        for (Sprite sprite : list) {
            if (sprite.getColorBits() == ColorBits.LINK) {
                continue;
            }
            if (sprite.getCompressMode() == CompressMode.NONE) {
                sprite.setLength(sprite.getWidth() * sprite.getHeight() * (sprite.getColorBits() == ColorBits.ARGB_8888 ? 4 : 2));
            }
            byte[] data=new byte[sprite.getLength()];;
            in.read(data);
            sprite.setData(data);
        }
        album.setList(list);
    }
    
    protected Sprite getSprite(int index){
        Sprite sprite=album.getList().get(index);
        if (sprite.getColorBits() == ColorBits.LINK) {
            index=sprite.getTargetIndex();
            sprite=album.getList().get(index);
        }
        return  sprite;
    }
    
    public byte[] decode(int index) throws IOException {
        Sprite sprite=getSprite(index);
        byte[] data=sprite.getData();
        int colorBits=sprite.getColorBits();
        int width=sprite.getWidth();
        int height=sprite.getHeight();
        int len=width * height * (colorBits == ColorBits.ARGB_8888 ? 4 : 2);
        if (sprite.getCompressMode() == CompressMode.ZLIB) {
            data=Zlibs.decompress(data, len);
        }
        return Bitmaps.createFromArray(data, width, height, colorBits);
    }
    
    
}
