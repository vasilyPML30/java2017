import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;

public class Main {

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
