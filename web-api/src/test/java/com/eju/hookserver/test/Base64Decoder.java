package com.eju.hookserver.test;

import com.google.common.primitives.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by zcm on 2021/3/16.
 * @version v0.1.0
 * @see <font color="#0000FF">hook-server-api</font>
 */
public class Base64Decoder {

    private final static int ESCAPE_CHAR_CODE = 61;

    private final static int[] inverse = {64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,62,64,64,64,63,52,53,54,55,56,57,58,59,60,61,64,64,64,64,64,64,64,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,64,64,64,64,64,64,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64};

    private int count = 0;

    private List<Byte> data =  new ArrayList<>();

    private int filled = 0;

    private int[] work =  {0,0,0,0};

    //private var resourceManager:IResourceManager;

    public  Base64Decoder()
    {
        //this.resourceManager = ResourceManager.getInstance();
        super();
    }

    public void decode(String encoded){
        char c;
        for(int i = 0; i < encoded.length(); i++)
        {
            c = encoded.charAt(i);
            if(c == ESCAPE_CHAR_CODE)
            {
                this.work[this.count++] = -1;
                if (isBreak()) break;
            }
            else if(inverse[c] != 64)
            {

                this.work[this.count++] = inverse[c];
                if (isBreak()) break;
            }
        }
    }

    private boolean isBreak() {
        if(this.count == 4)
        {
            this.count = 0;
            this.data.add((byte)(this.work[0] << 2 | (this.work[1] & 255) >> 4));
            this.filled++;
            if(this.work[2] == -1)
            {
                return true;
            }
            this.data.add((byte)(this.work[1] << 4 | (this.work[2] & 255) >> 2));
            this.filled++;
            if(this.work[3] == -1)
            {
                return true;
            }
            this.data.add((byte)(this.work[2] << 6 | this.work[3]));
            this.filled++;
        }
        return false;
    }

    public static List<Byte> intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return Bytes.asList(result);
    }

    public List<Byte> drain(){

        return this.data;
    }

    public List<Byte> flush(){
        String message = null;
        if(this.count > 0)
        {
            //message = this.resourceManager.getString("utils","partialBlockDropped",[this.count]);
            throw new Error(message);
        }
        return this.drain();
    }

    public void reset()
    {
        this.data.clear();
        this.count = 0;
        this.filled = 0;
    }

    public byte[] toByteArray()
    {
        byte[] byteList = Bytes.toArray(this.flush());
        this.reset();
        return byteList;
    }
}
