package net.kritsu.handler;

import net.kritsu.constants.ColorBits;
import net.kritsu.constants.CompressMode;
import net.kritsu.lib.Bitmaps;
import net.kritsu.lib.Zlibs;
import net.kritsu.model.Album;
import net.kritsu.model.Sprite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static net.kritsu.lib.Colors.readPalette;
import static net.kritsu.lib.Streams.readInt;

public class FourthHandler extends SecondHandler {
    
    protected int paletteCount;
    
    public FourthHandler(Album album,InputStream in) throws IOException {
        super(album, in);
    }
    
    public void init(InputStream in) throws IOException {
        List<List<byte[]>> palettes=new ArrayList<>();
        for (int i=0; i < Math.max(1,paletteCount); i++) {
            int paletteSize=readInt(in);
            List<byte[]> table=readPalette(in, paletteSize);
            palettes.add(table);
        }
        album.setPalettes(palettes);
        super.init(in);
    }
    
    public byte[] decode(int index) throws IOException {
        Sprite sprite=getSprite(index);
        byte[] data=sprite.getData();
        int colorBits=sprite.getColorBits();
        int compressMode=sprite.getCompressMode();
        int width=sprite.getWidth();
        int height=sprite.getHeight();
        if (colorBits == ColorBits.ARGB_1555 && compressMode == CompressMode.ZLIB) {
            data=Zlibs.decompress(data, width * height);

            List<byte[]> palette=album.getCurrentPalette();
            int paletteSize=palette.size();
            if (paletteSize > 0 && paletteSize < 256) {
                ByteArrayOutputStream out=new ByteArrayOutputStream();
                for (byte b : data) {
                    byte[] color=palette.get(b % paletteSize);
                    out.write(new byte[]{color[2], color[1], color[0], color[3]});
                }
                out.close();
                data=out.toByteArray();
                return Bitmaps.createFromArray(data, width, height, ColorBits.ARGB_8888);
            }
        }
        return super.decode(index);
    }
}
