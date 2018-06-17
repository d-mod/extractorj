package net.kritsu.model;

import net.kritsu.constants.FileVersion;
import net.kritsu.handler.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Album implements Iterable<Sprite>{
    private int version;
    private long indexLength;
    private int count;
    private List<Sprite> list;
    private int paletteIndex;
    private List<List<byte[]>> palettes;
    private Handler handler;
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version=version;
    }
    
    public long getIndexLength() {
        return indexLength;
    }
    
    public void setIndexLength(long indexLength) {
        this.indexLength=indexLength;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count=count;
    }
    
    public List<Sprite> getList() {
        if(list!=null) {
            return list;
        }
        return  list=new ArrayList<Sprite>();
    }
    
    public void setList(List<Sprite> list) {
        this.list=list;
    }
    
    public Iterator<Sprite> iterator() {
        return getList().iterator();
    }
    
    public Handler getHandler() {
        return handler;
    }
    
    public void setHandler(Handler handler) {
        this.handler=handler;
    }
    
    public byte[] getImage(int index) throws IOException {
        if(handler!=null){
            return  handler.decode(index);
        }
        return new byte[0];
    }
    
    public int getPaletteIndex() {
        return paletteIndex;
    }
    
    public void setPaletteIndex(int paletteIndex) {
        this.paletteIndex=paletteIndex;
    }
    
    public List<List<byte[]>> getPalettes() {
        return palettes;
    }
    
    public void setPalettes(List<List<byte[]>> palettes) {
        this.palettes=palettes;
    }
    
    public List<byte[]> getCurrentPalette(){
        if(paletteIndex<palettes.size()&&paletteIndex>-1){
            return  palettes.get(paletteIndex);
        }
        return new ArrayList<byte[]>();
    }
}
