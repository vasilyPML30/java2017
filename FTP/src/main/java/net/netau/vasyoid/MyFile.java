package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * MyFile object contains information about a file --
 * file name and a flag determining whether the file is a directory.
 */
public class MyFile {

    private final String name;
    private final boolean isDirectory;

    public MyFile(@NotNull String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    private boolean isDirectory() {
        return isDirectory;
    }

    /**
     * {@inheritDoc}
     * @return file name as a string
     */
    @Override
    public String toString() {
        return name + (isDirectory ? "/" : "");
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MyFile &&
                name.equals(((MyFile) o).name) &&
                isDirectory == ((MyFile) o).isDirectory;
    }
}
