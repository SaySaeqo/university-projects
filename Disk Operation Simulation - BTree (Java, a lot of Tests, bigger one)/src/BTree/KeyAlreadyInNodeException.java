package BTree;

public class KeyAlreadyInNodeException extends Exception {
    public final int atPosition;

    public KeyAlreadyInNodeException(int atPosition) {
        super();
        this.atPosition = atPosition;
    }
}
