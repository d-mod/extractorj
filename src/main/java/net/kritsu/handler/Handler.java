package net.kritsu.handler;

import net.kritsu.model.Sprite;

import java.io.IOException;
import java.io.InputStream;

public interface Handler {
    void init(InputStream in) throws IOException;
    
    byte[] decode(int index) throws IOException;
}
