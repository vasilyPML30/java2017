package net.netau.vasyoid;

import javafx.application.Application;
import org.jetbrains.annotations.NotNull;

/**
 * Main class.
 * Launches the application.
 */
public class Main {

    /**
     * Main function.
     * @param args command line arguments.
     */
    public static void main(@NotNull String[] args) {
        Application.launch(MyView.class, args);
    }
}
