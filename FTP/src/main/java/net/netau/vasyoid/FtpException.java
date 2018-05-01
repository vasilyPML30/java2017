package net.netau.vasyoid;

public class FtpException extends Exception {
    public FtpException(Exception e) {
        addSuppressed(e);
    }
}
