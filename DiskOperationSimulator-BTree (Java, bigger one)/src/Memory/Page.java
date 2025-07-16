package Memory;

import javax.imageio.spi.IIOServiceProvider;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.PublicKey;

public abstract class Page {

    private boolean changed;
    final public int myAddress;

    public Page() throws MemoryOverflowException {
        changed = true;
        myAddress = Memory.getInstance().allocPage();
    }

    public Page(int number) {
        myAddress = number;
    }


    public void allowSaving() {
        changed = true;
    }

    public boolean wasModified() {
        return changed;
    }

    public void writeBytes(RandomAccessFile raf) throws IOException, MemoryOverflowException {
    }

    protected void readBytes(RandomAccessFile raf) throws IOException {
    }

}
