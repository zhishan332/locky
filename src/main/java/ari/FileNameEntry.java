package ari;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件名加密解密
 *
 * @author wangqing
 * @since 16-2-23 上午10:52
 */
public class FileNameEntry {

    private static final Logger logger = LoggerFactory.getLogger(FileNameEntry.class);

    public static final String ENTRY_SUFFIX = ".lk";

    public static String encode(File file) {

        String name = file.getName();
        logger.info("【文件名编码】获取到原文件名："+name);
//        String newName = Base64Utils.encode(name) + ENTRY_SUFFIX;
        String newName = Hex.encodeHexStr(name.getBytes()) + ENTRY_SUFFIX;
        logger.info("【文件名编码】生成新文件名："+newName);
        String newPath=file.getAbsolutePath().replace(file.getName(), newName);
        logger.info("【文件名编码】生成新路径："+newPath);
        return newPath;
    }


    public static String decode(File file) {

        String name = file.getName().replace(ENTRY_SUFFIX, "");
//        String sourceName = Base64Utils.decode(name);
        String sourceName = new String(Hex.decodeHex(name.toCharArray()));
        logger.info("【文件名解码】获取到原文件名："+sourceName);
        String sourcePath = file.getAbsolutePath().replace(file.getName(), sourceName);
        logger.info("【文件名解码】获取到原路径："+sourcePath);
        return sourcePath;
    }

}
