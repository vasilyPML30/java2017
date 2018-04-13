import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

/**
 * Console application that evaluates MD5 checksum of a directory.
 */
public class Main {

    /**
     * Run an evaluator.
     * @param evaluator evaluator to run.
     * @param path path to pass to evaluator.
     */
    private static void runEvaluator(@NotNull MD5Evaluator evaluator, String path) {
        long startTime = System.currentTimeMillis();
        byte[] result = evaluator.get(path);
        if (result == null) {
            return;
        }
        try {
            System.out.println(new String(result, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {}
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime - startTime);
    }

    /**
     * Runs two types of evaluators and outputs the result and time.
     * @param args directory path.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("wrong arguments");
            System.exit(-1);
        }
        System.out.println("Single thread evaluator:");
        runEvaluator(new MD5SingleThread(), args[0]);
        System.out.println("Multi thread evaluator:");
        runEvaluator(new MD5MultiThread(), args[0]);
    }
}
