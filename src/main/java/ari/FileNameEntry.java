package ari;

import utils.Base64Utils;

import java.io.File;

/**
 * 文件名加密解密
 *
 * @author wangqing
 * @since 16-2-23 上午10:52
 */
public class FileNameEntry {

    public static final String ENTRY_SUFFIX = ".lk";

    public static String encode(File file) {

        String name = file.getName();
        String newName = Base64Utils.encode(name) + ENTRY_SUFFIX;

        return file.getAbsolutePath().replace(file.getName(), newName);
    }


    public static String decode(File file) {

        String name = file.getName().replace(ENTRY_SUFFIX, "");
        String sourceName = Base64Utils.decode(name);

        return file.getAbsolutePath().replace(file.getName(), sourceName);
    }

}
