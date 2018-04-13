import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Multi Thread MD5 evaluator based on a Fork/Join pool.
 * Recursively walks through all subdirectories.
 */
public class MD5SingleThread extends MD5Evaluator {

    /**
     * Recursive method evaluating the checksum.
     * @param file file to evaluate.
     * @return MD5 checksum.
     */
    @Nullable
    private static byte[] evaluate(@NotNull File file) {
        MessageDigest messageDigest;
        try {
             messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
            return null;
        }
        messageDigest.update(file.getName().getBytes());
        if (file.isFile()) {
            byte[] result = evaluateFile(file);
            if (result == null) {
                return null;
            }
            messageDigest.update(result);
        } else {
            File[] content = file.listFiles();
            if (content == null) {
                return null;
            }
            for (File nextFile : content) {
                byte[] result = evaluate(nextFile);
                if (result == null) {
                    return null;
                }
                messageDigest.update(result);
            }
        }
        return messageDigest.digest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public byte[] get(@NotNull String path) {
        return evaluate(new File(path));
    }

}
