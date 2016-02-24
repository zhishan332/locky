package ari;

/**
 * 工厂
 *
 * @author wangqing
 * @since 16-2-23 上午9:29
 */
public class FileLockAriFactory {

    public static AesLockAri createAesLockAri() {
        return AesLockAri.getInstance();
    }

}
