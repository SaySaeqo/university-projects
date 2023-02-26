public class EmptyBufferException extends Exception{

    private final Object emptyBuffer;
    public EmptyBufferException(Object obj){
        super("Buffer is empty");
        emptyBuffer = obj;
    }

    public boolean isFor(Object obj){
        return obj.equals(emptyBuffer);
    }
}
