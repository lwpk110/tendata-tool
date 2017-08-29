package cn.tendata.batch.compress;

public class ArchiveExtractingException extends RuntimeException {

    private static final long serialVersionUID = -8923164284269250080L;

    public ArchiveExtractingException() {
        super();
    }

    public ArchiveExtractingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiveExtractingException(String message) {
        super(message);
    }

    public ArchiveExtractingException(Throwable cause) {
        super(cause);
    }
}
