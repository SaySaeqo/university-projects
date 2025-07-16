package Memory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordListPage extends Page {

    private final List<Record> records;

    public RecordListPage() throws MemoryOverflowException {
        super();
        int size = Memory.getInstance().pageSizeInBytes / (Record.sizeInBytes + 1);
        records = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            records.add(null);
        }
    }

    public RecordListPage(int pageNumber) throws IOException {
        super(pageNumber);
        records = new ArrayList<>(Memory.getInstance().pageSizeInBytes / (Record.sizeInBytes + 1));
        Memory.getInstance().readPage(this);
    }

    @Override
    protected void readBytes(RandomAccessFile raf) throws IOException {
        // each record have 5 elements (Record.SIZE), key and validity byte
        for (int i = 0; i + Record.sizeInBytes < Memory.getInstance().pageSizeInBytes; i += Record.sizeInBytes + 1) {
            if (raf.readBoolean()) { // record wasn't deleted
                records.add(Record.readBytes(raf));
            } else {
                records.add(null);
                raf.skipBytes(Record.sizeInBytes);
            }
        }
    }

    @Override
    public void writeBytes(RandomAccessFile raf) throws IOException {
        for (Record record : records) {
            if (record != null) {
                raf.writeBoolean(true);
                Record.writeBytes(raf, record);
            } else {
                raf.writeBoolean(false);
                raf.skipBytes(Record.sizeInBytes);
            }
        }
    }

    public Record get(int index) {
        return records.get(index);
    }

    public void set(int index, Record record) {
        allowSaving();
        records.set(index, record);
    }

    public boolean isFull() {
        return !records.contains(null);
    }

    public int add(Record record) {
        allowSaving();
        int idx = records.indexOf(null);
        records.set(idx, record);
        return idx;
    }

    public void remove(int index) {
        allowSaving();
        records.set(index, null);
    }

    public boolean empty() {
        return records.stream().allMatch(Objects::isNull);
    }
}
