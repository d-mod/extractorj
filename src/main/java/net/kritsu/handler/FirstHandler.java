package net.kritsu.handler;

import net.kritsu.constants.ColorBits;
import net.kritsu.constants.CompressMode;
import net.kritsu.model.Album;
import net.kritsu.model.Sprite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static net.kritsu.lib.Streams.readInt;

public class FirstHandler extends SecondHandler{
    public FirstHandler(Album album, InputStream in) throws IOException {
        super(album, in);
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
            if (sprite.getCompressMode() == CompressMode.NONE) {
                sprite.setLength(sprite.getWidth() * sprite.getHeight() * (sprite.getColorBits() == ColorBits.ARGB_8888 ? 4 : 2));
            }
            byte[] data=new byte[sprite.getLength()];;
            in.read(data);
            sprite.setData(data);
        }
        album.setList(list);
    }
    
}
