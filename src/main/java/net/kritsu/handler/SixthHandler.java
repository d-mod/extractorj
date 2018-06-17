package net.kritsu.handler;

import net.kritsu.model.Album;

import java.io.IOException;
import java.io.InputStream;

import static net.kritsu.lib.Streams.readInt;

public class SixthHandler extends FourthHandler{
    public SixthHandler(Album album,InputStream in) throws IOException {
        super(album,in);
    }
    
    public void init(InputStream in) throws IOException {
        paletteCount=readInt(in);
        super.init(in);
    }
    
}
