package GUI;

public class UnreferencedIDException extends RuntimeException {

    public UnreferencedIDException(String message) {
        super("Unreferenced ID: \"" + message + "\"");
    }
}
