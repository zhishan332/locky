package utils;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * base64工具
 *
 * @author wangqing
 * @since 14-12-5 下午1:01
 */
public class Base64Utils {

    public static String decode(String data) {
        if (data == null) return null;
        Base64 base = new Base64();
        try {
            //Base64解码
            byte[] b = base.decode(data);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            return new String(b,"UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String encode(String data) {
        Base64 dd = new Base64();
        try {
            return dd.encodeToString(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        String ss=Base64Utils.encode("我是中国人.mkv");

        System.out.println(ss);

        System.out.println(Base64Utils.decode(ss));
    }
}
