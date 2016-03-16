import ari.AesLockAri;
import ari.FileLockAriFactory;
import ari.FileNameEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
        } else {
            List<File> fileList = new ArrayList<File>();
            listLockDirectory(file, fileList);

            logger.info("获取到递归待加密文件列表："+fileList);

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
        } else {
            List<File> fileList = new ArrayList<File>();
            listReLockDirectory(file, fileList);
            logger.info("获取到递归待解密文件列表："+fileList);
            for (File ff : fileList) {
                boolean isSuc = aesAri.unLock(ff);
                if (!isSuc) {
                    resList.add(new LockResult(ff.getName(), isSuc));
                }
            }
        }

        return resList;
    }

    /**
     * 遍历目录及其子目录下的所有文件并保存
     *
     * @param path  目录全路径
     * @param files 列表：保存文件对象
     */
    private void listLockDirectory(File path, List<File> files) {
        if (path.isDirectory()) {
            File[] fileList = path.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isDirectory()) {
                        listLockDirectory(file, files);
                    } else {
                        if(isValidLockFile(file)){
                            files.add(file);
                        }

                    }
                }
            }
        } else {
            if(isValidLockFile(path)){
                files.add(path);
            }
        }
    }

    /**
     * 遍历目录及其子目录下的所有文件并保存
     *
     * @param path  目录全路径
     * @param files 列表：保存文件对象
     */
    private void listReLockDirectory(File path, List<File> files) {
        if (path.isDirectory()) {
            File[] fileList = path.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isDirectory()) {
                        listReLockDirectory(file, files);
                    } else {
                        if(isValidReLockFile(file)){
                            files.add(file);
                        }
                    }
                }
            }
        } else {
            if(isValidReLockFile(path)){
                files.add(path);
            }
        }
    }


    private boolean isValidLockFile(File file) {

        String noAcceptType1 = ".jar";
        String noAcceptType2 = ".log";
        String noAcceptType3 = ".class";

        String name = file.getName();

        return !name.endsWith(noAcceptType1)
                && name.contains(".")
                &&!name.endsWith(noAcceptType2)
                && !name.endsWith(noAcceptType3)
                && !name.endsWith(FileNameEntry.ENTRY_SUFFIX);
    }


    private boolean isValidReLockFile(File file) {


        String name = file.getName();

        return name.endsWith(FileNameEntry.ENTRY_SUFFIX);
    }

//    static class LockFileNameFilter implements FilenameFilter {
//        private String noAcceptType1 = "jar";
//        private String noAcceptType2 = "log";
//        private String noAcceptType3 = "class";
//
//        public boolean accept(File dir, String name) {
//            return dir.isDirectory() || !name.endsWith(noAcceptType1) && name.contains(".") && !name.endsWith(noAcceptType2) && !name.endsWith(noAcceptType3) && !name.endsWith(FileNameEntry.ENTRY_SUFFIX);
//        }
//    }
//
//    static class reLockFileNameFilter implements FilenameFilter {
//
//        public boolean accept(File dir, String name) {
//            return dir.isDirectory() || name.endsWith(FileNameEntry.ENTRY_SUFFIX);
//        }
//    }
}
