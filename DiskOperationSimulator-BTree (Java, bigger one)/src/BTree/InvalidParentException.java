package BTree;

public class InvalidParentException extends RuntimeException {
    public InvalidParentException(String msg) {
        super(msg);
    }
}
