package com.eju.hookserver.test;

import com.google.common.primitives.Bytes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Created by zcm on 2021/3/12.
 * @version v0.1.0
 * @see <font color="#0000FF">hook-server-api</font>
 */
public class RSAKey {

    public BigInteger dmp1;
    protected Boolean canDecrypt;
    public BigInteger d;
    public int e;
    public BigInteger dmq1;
    public BigInteger n;
    public BigInteger p;
    public BigInteger q;
    protected Boolean canEncrypt;
    public BigInteger coeff;

    public RSAKey(BigInteger param1, int param2, BigInteger param3, BigInteger param4, BigInteger param5, BigInteger param6, BigInteger param7, BigInteger param8) {
        this.n = param1;
        this.e = param2;
        this.d = param3;
        this.p = param4;
        this.q = param5;
        this.dmp1 = param6;
        this.dmq1 = param7;
        this.coeff = param8;

        canEncrypt = (n!=null&&e!=0);
        canDecrypt = (canEncrypt&&d!=null);
    }

    public  RSAKey(BigInteger param1, int param2, BigInteger param3){
        this.n = param1;
        this.e = param2;
        this.d = param3;
    }

    public void decrypt(List<Byte> param1, List<Byte> param2, int param3, Function param4){
        Function<BigInteger,BigInteger> function = x -> {
            if (p==null && q==null) {
                return x.modPow(d,n);
            }

            //flash 与 java mod不一样
            BigInteger xp = x.mod(p).modPow(dmp1, p);
            BigInteger xq = x.mod(q).modPow(dmq1, q);

            while (xp.compareTo(xq)<0) {
                xp = xp.add(p);
            }
            BigInteger r = xp.subtract(xq).multiply(coeff).mod(p).multiply(q).add(xq);

            return r;
        };
        _decrypt(function, param1, param2, param3, param4, 0x02);
        return;
    }


    private List<Byte> pkcs1unpad(BigInteger src,int  n, int type) {
        byte[] b = src.toByteArray();
        List<Byte> out = new ArrayList<>();
        int i = 0;
        while (i<b.length && b[i]==0) ++i;
        if (b.length-i != n-1 || b[i]>2) {
            //trace("PKCS#1 unpad: i="+i+", expected b[i]==[0,1,2], got b[i]="+b[i].toString(16));
            return null;
        }
        ++i;
        while (b[i]!=0) {
            if (++i>=b.length) {
                //trace("PKCS#1 unpad: i="+i+", b[i-1]!=0 (="+b[i-1].toString(16)+")");
                return null;
            }
        }
        while (++i < b.length) {
            out.add(b[i]);
        }
        return out;
    }

    private void _decrypt(Function<BigInteger,BigInteger> param1,List<Byte>  param2, List<Byte>  param3, int length, Function param5,int param6) {


        int _loc_7 = 0;
        int _loc_8 = 0;
        BigInteger _loc_9 = null;
        BigInteger _loc_10 = null;
        List<Byte> _loc_11 = null;

        /*if (param2.position >= param2.length)
        {
            param2.position = 0;
        }*/
        _loc_7 = ((n.bitLength() + 7) / 8) & 0xffff;
        _loc_8 = length&0xffff;
        int size = param2.size();
        for(int position=0;position <_loc_8; position+=size){
            _loc_9 = new BigInteger(Bytes.toArray(param2));
            //_loc_9 = new BigInteger(array, _loc_8);
            _loc_10 = param1.apply(_loc_9);
            _loc_11 = this.pkcs1unpad(_loc_10, _loc_7,0x02);
            param3.addAll(_loc_11);
        }
        return;
    }// end function



    public static RSAKey parsePrivateKey(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8) {
        if (param4 == null) {
            return new RSAKey(new BigInteger(param1, 16), Integer.parseInt(param2, 16), new BigInteger(param3, 16));
        }
        return new RSAKey(new BigInteger(param1,16), Integer.parseInt(param2), new BigInteger(param3,16), new BigInteger(param4,16), new BigInteger(param5,16), new BigInteger(param6,16), new BigInteger(param7,16), new BigInteger(param8,16));
        //return new RSAKey(new BigInteger(toArray(param1)), Integer.parseInt(param2, 16), new BigInteger(toArray(param3)), new BigInteger(toArray(param4)), new BigInteger(toArray(param5)), new BigInteger(toArray(param6)), new BigInteger(toArray(param7)), new BigInteger(toArray(param8)));
    }


}
