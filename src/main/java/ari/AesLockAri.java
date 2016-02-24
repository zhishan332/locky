package ari;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 文件加锁算法
 *
 * @author wangqing
 * @since 16-2-23 上午9:25
 */
public class AesLockAri {

    private static final Logger logger = LoggerFactory.getLogger(AesLockAri.class);

    private static final String sKey = "ValenciaVeronicaVivienneZerlinda";

    private static AesLockAri aesLockAri;

    private  Cipher enCipher;

    private  Cipher unCipher;

    private AesLockAri() {
        initAESCipher(sKey);
    }

    public static AesLockAri getInstance() {
        if (aesLockAri == null) aesLockAri = new AesLockAri();
        return aesLockAri;
    }


    /**
     * 初始化 AES Cipher
     */
    private void initAESCipher(String sKey) {
        //创建Key gen
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] codeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
            enCipher = Cipher.getInstance("AES");
            //初始化
            enCipher.init(Cipher.ENCRYPT_MODE, key);

            unCipher = Cipher.getInstance("AES");
            //初始化
            unCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化AES秘钥异常", e);
        } catch (NoSuchPaddingException e) {
            logger.error("初始化AES秘钥异常", e);
        } catch (InvalidKeyException e) {
            logger.error("初始化AES秘钥异常", e);
        }
    }


    public boolean lock(File file) {
        boolean result = false;
        //新建临时加密文件
        File encrypfile = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            String destPath = FileNameEntry.encode(file);
            encrypfile = new File(destPath);
            outputStream = new FileOutputStream(encrypfile);
            //以加密流写入文件
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, enCipher);
            byte[] cache = new byte[1024 * 64];
            int nRead = 0;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
            cipherInputStream.close();

            result = true;
        } catch (FileNotFoundException e) {
            logger.error("AES加密文件时异常", e);
        } catch (IOException e) {
            logger.error("AES加密文件时异常", e);
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                boolean isDel = file.delete();
                if (!isDel) {
                    assert encrypfile != null;
                    boolean isDelTem = encrypfile.delete();
                    logger.info("AES加密删除源文件失败执行删除加密文件" + isDelTem);
                    result = false;
                }
            } catch (IOException e) {
                logger.error("AES加密文件时异常", e);
            }
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                logger.error("AES加密文件时异常", e);
            }
        }
        return result;
    }


    public boolean unLock(File file) {

        File decryptFile = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        boolean result = false;
        try {
            String srcPath = FileNameEntry.decode(file);
            decryptFile = new File(srcPath);
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(decryptFile);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, unCipher);
            byte[] buffer = new byte[1024 * 64];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }
            cipherOutputStream.close();
            result = true;
        } catch (IOException e) {
            logger.error("AES解密文件时异常", e);
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
                boolean isDel = file.delete();
                if (!isDel) {
                    boolean isDelTem = decryptFile.delete();
                    logger.info("AES解密删除源文件失败执行删除解密文件" + isDelTem);
                    result = false;
                }
            } catch (IOException e) {
                logger.error("AES解密文件时异常", e);
            }
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                logger.error("AES解密文件时异常", e);
            }
        }
        return result;
    }


    public static void main(String[] args) throws InterruptedException {

        long t1 = System.currentTimeMillis();
        System.out.println(AesLockAri.getInstance().lock(new File("D:\\download2\\test\\IMG_0511.MOV")));
        long t2 = System.currentTimeMillis();
        System.out.println("cost:" + (t2 - t1));
//
//        long t1 = System.currentTimeMillis();
//        System.out.println(AesLockAri.getInstance().unLock(new File("D:\\download2\\test\\SU1HXzA1MTEuTU9W")));
//        long t2 = System.currentTimeMillis();
//        System.out.println("cost:" + (t2 - t1));
    }
}
