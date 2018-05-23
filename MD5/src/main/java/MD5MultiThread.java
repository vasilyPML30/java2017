import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Multi Thread MD5 evaluator based on a Fork/Join pool.
 */
public class MD5MultiThread extends MD5Evaluator {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public byte[] get(@NotNull String path) {
        try {
            ForkJoinEvaluator evaluator = new ForkJoinEvaluator(new File(path));
            evaluator.fork();
            return evaluator.join();
        } catch (Exception e) {
            System.out.println("Something is wrong: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fork/Join task that computes the checksum.
     */
    private static class ForkJoinEvaluator extends RecursiveTask<byte[]> {

        private File file;

        ForkJoinEvaluator(@NotNull File file) {
            this.file = file;
        }

        /**
         * {@inheritDoc}
         * @return resulting check sum.
         */
        @Override
        @Nullable
        protected byte[] compute() {
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
                Arrays.sort(content);
                List<ForkJoinEvaluator> subTasks = new LinkedList<>();
                for (File nextFile : content) {
                    ForkJoinEvaluator task = new ForkJoinEvaluator(nextFile);
                    subTasks.add(task);
                    task.fork();
                }
                for (ForkJoinEvaluator task : subTasks) {
                    byte[] result = task.join();
                    if (result == null) {
                        return null;
                    }
                    messageDigest.update(result);
                }
            }
            return messageDigest.digest();
        }
    }

}
