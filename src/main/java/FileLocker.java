import java.io.File;
import java.util.List;

/**
 * 加锁
 *
 * @author wangqing
 * @since 16-2-23 上午9:10
 */
public interface FileLocker {

    public List<LockResult> lock(File file);

    public List<LockResult> recLock(File file);

}


class LockResult {

    private String file;
    private boolean lockResult;

    LockResult(String file, boolean lockResult) {
        this.file = file;
        this.lockResult = lockResult;
    }

    String getFile() {
        return file;
    }

    void setFile(String file) {
        this.file = file;
    }

    boolean isLockResult() {
        return lockResult;
    }

    void setLockResult(boolean lockResult) {
        this.lockResult = lockResult;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LockResult{");
        sb.append("file='").append(file).append('\'');
        sb.append(", lockResult=").append(lockResult);
        sb.append('}');
        return sb.toString();
    }
}
