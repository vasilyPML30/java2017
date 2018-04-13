import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 checksum evaluator.
 */
public abstract class MD5Evaluator {

    private static final int BUFF_SIZE = 1024;

    /**
     * Method evaluating checksum of a single file.
     * @param file file to evaluate.
     * @return chechsum of the file.
     */
    @Nullable
    protected static byte[] evaluateFile(@NotNull File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[BUFF_SIZE];
            while (true) {
                int length = inputStream.read(buffer);
                if (length < 0) {
                    break;
                }
                messageDigest.update(buffer, 0, length);
            }
            return messageDigest.digest();
        } catch(NoSuchAlgorithmException ignored) {
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    @Nullable
    public abstract byte[] get(@NotNull String path);
}
