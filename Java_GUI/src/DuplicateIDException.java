public class DuplicateIDException extends RuntimeException {

    public DuplicateIDException(String message) {
        super("ID has already been used for a [" + message + "]");
    }
}
