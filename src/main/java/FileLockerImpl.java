import ari.AesLockAri;
import ari.FileLockAriFactory;
import ari.FileNameEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 加锁实现
 *
 * @author wangqing
 * @since 16-2-23 上午9:17
 */
public class FileLockerImpl implements FileLocker {
    private static final Logger logger = LoggerFactory.getLogger(FileLockerImpl.class);

    @Override
    public List<LockResult> lock(File file) {
        if (!file.exists()) throw new RuntimeException("文件或文件夹不存在");

        List<LockResult> resList = new ArrayList<LockResult>();
        AesLockAri aesAri = FileLockAriFactory.createAesLockAri();
        if (file.isFile()) {
            boolean isLock = aesAri.lock(file);
            if (!isLock) {
                resList.add(new LockResult(file.getName(), isLock));
            }
        }

        File[] fileList = file.listFiles(new LockFileNameFilter());

        if (fileList != null) {

            for (File ff : fileList) {
                boolean isLock = aesAri.lock(ff);
                if (!isLock) {
                    resList.add(new LockResult(ff.getName(), isLock));
                }
            }
        }
        return resList;
    }

    @Override
    public List<LockResult> recLock(File file) {
        if (!file.exists()) throw new RuntimeException("文件或文件夹不存在");

        List<LockResult> resList = new ArrayList<LockResult>();
        AesLockAri aesAri = FileLockAriFactory.createAesLockAri();
        if (file.isFile()) {
            boolean isLock = aesAri.unLock(file);
            if (!isLock) {
                resList.add(new LockResult(file.getName(), isLock));
            }
        }

        File[] fileList = file.listFiles(new reLockFileNameFilter());

        if (fileList != null) {

            for (File ff : fileList) {
                logger.info("当前解密"+ff.getName());
                boolean isSuc = aesAri.unLock(ff);
                logger.info("当前解密"+ff.getName()+"结果："+isSuc);
                if (!isSuc) {
                    resList.add(new LockResult(ff.getName(), isSuc));
                }
            }
        }
        return resList;
    }

    static class LockFileNameFilter implements FilenameFilter {
        private String noAcceptType1="jar";
        private String noAcceptType2="log";

        public boolean accept(File dir,String name){
            return !name.endsWith(noAcceptType1) && name.contains(".") && !name.endsWith(noAcceptType2) && !name.endsWith(FileNameEntry.ENTRY_SUFFIX);
        }
    }

    static class reLockFileNameFilter implements FilenameFilter {

        public boolean accept(File dir,String name){
            return name.endsWith(FileNameEntry.ENTRY_SUFFIX);
        }
    }
}
