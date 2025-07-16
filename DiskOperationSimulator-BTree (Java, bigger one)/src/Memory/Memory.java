package Memory;

import BTree.BTreeNode;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// simulate disk memory
public class Memory {

    private static Memory singleton = null;

    public static int nonExistingPage = 0xFFFF;
    public final int pageSizeInBytes;
    private final Stack<Integer> pagesWithDeletedRecords;

    private final Stack<Integer> emptyPages;

    private final List<BTreeNode> savedNodes = new ArrayList<>(10);
    private final File file;
    private RecordListPage savedPage = null;
    private int savedPageAddress;
    private int readCount = 0;
    private int writeCount = 0;

    public Memory(String path, int totalSizeInBytes, int pageSizeInBytes) throws IOException {
        this.pageSizeInBytes = Math.max(pageSizeInBytes, 2);
        pagesWithDeletedRecords = new Stack<>();
        emptyPages = new Stack<>();
        for (int pageNumber = totalSizeInBytes / pageSizeInBytes - 1;
             pageNumber >= 0;
             pageNumber -= 1) {
            emptyPages.push(pageNumber);
        }
        file = new File(path);
        file.delete();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(totalSizeInBytes);
        }
    }

    public static Memory getInstance() {
        if (singleton == null) {
            try {
                singleton = new Memory("Memory", 1024, 32);
            } catch (IOException important) {
                Logger.getLogger("log").warning("Memory not reachable. Retrying in 10s...");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                getInstance();
            }
        }
        return singleton;
    }

    /**
     * record address has 2 bytes: 1st (older) is the page number
     */
    public static int pageNumber(int fromRecordAddress) {
        return fromRecordAddress >> Byte.SIZE;
    }

    /**
     * record address has 2 bytes: 2nd (younger) is the recordIdx in RecordList-type-Page
     */
    public static int recordIdx(int fromRecordAddress) {
        return fromRecordAddress & 0xFF;
    }

    /**
     * combines record address from pageNumber and recordIdx in RecordList-type-Page
     */
    public static int recordAddress(int pageNumber, int recordIdx) {
        return (pageNumber << Byte.SIZE) + recordIdx;
    }

    public Record getRecord(int address) throws IOException, MemoryOverflowException {
        return cache(pageNumber(address)).get(recordIdx(address));
    }

    public void setRecord(int address, Record record) throws IOException, MemoryOverflowException {
        cache(pageNumber(address)).set(recordIdx(address), record);
    }

    public int addRecord(Record record) throws IOException, MemoryOverflowException {
        if (savedPage == null || savedPage.isFull()) {
            if (!pagesWithDeletedRecords.empty()) {
                cache(pagesWithDeletedRecords.pop());
            } else if (!emptyPages.empty()) {
                flush();
                savedPage = new RecordListPage();
                savedPageAddress = emptyPages.pop();
            } else {
                throw new MemoryOverflowException();
            }
        }
        return recordAddress(savedPageAddress, savedPage.add(record));
    }

    public void removeRecord(int address) throws IOException, MemoryOverflowException {
        cache(pageNumber(address)).remove(recordIdx(address));
        if (cache(pageNumber(address)).empty()) {
            savedPage = null;
            emptyPages.push(savedPageAddress);
        }
    }


    public void flush() throws IOException, MemoryOverflowException {
        if (savedPage == null) {
            return;
        }
        writePage(savedPageAddress, savedPage);
        savedPage = null;
    }

    public int writePage(int number, Page page) throws IOException, MemoryOverflowException {
        if (page.wasModified()) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                raf.seek((long) number * pageSizeInBytes);
                page.writeBytes(raf);
            }
            writeCount++;
        }
        return number;
    }

    public int writePage(Page page) throws IOException, MemoryOverflowException {
        return writePage(emptyPages.pop(), page);
    }

    public int allocPage() throws MemoryOverflowException {
        try {
            return emptyPages.pop();
        } catch (EmptyStackException e){
            throw new MemoryOverflowException();
        }
    }

    public void readPage(Page page) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek((long) page.myAddress * pageSizeInBytes);
            page.readBytes(raf);
        }
        readCount++;
    }

    /**
     * if page is in cache: returns page, otherwise: saves previous page, puts page in cache and then returns page
     */
    private RecordListPage cache(int pageNumber) throws IOException, MemoryOverflowException {
        if (savedPage == null || !isCached(pageNumber)) {
            if (savedPage != null) {
                writePage(savedPageAddress, savedPage);
            }
            savedPage = new RecordListPage(pageNumber);
            savedPageAddress = pageNumber;
        }
        return savedPage;
    }

    private boolean isCached(int savedPageAddress) {
        return savedPageAddress == this.savedPageAddress;
    }

    /**
     * returns pageNumber to pool of empty pages
     */
    public void free(int pageNumber) {
        if (!emptyPages.contains(pageNumber) && pageNumber != Memory.nonExistingPage) {
            emptyPages.push(pageNumber);
        }
    }

    public String statistics() {
        return String.format("W|R: %3d | %-3d", writeCount, readCount);
    }

    public int getWriteCount() {
        return writeCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public static void clearMemory() {
        singleton = null;
    }

    public static void setMemory(int sizeInBytes, int pageSizeInBytes) throws IOException {
        singleton = new Memory("Memory", sizeInBytes, pageSizeInBytes);
    }

    public void cacheNode(BTreeNode node) {
        savedNodes.add(node);
    }

    public BTreeNode getNodeFromCache(int pageNumber) {
        for (BTreeNode node : savedNodes) {
            if (node.myAddress == pageNumber) {
                return node;
            }
        }
        return null;
    }

    public void writeAllNodes() throws IOException {
        for (BTreeNode node : savedNodes) {
            try {
                node.save();
            } catch (MemoryOverflowException e) {
                throw new RuntimeException("Memory overflow while overriding pages.");
            }
        }
        savedNodes.clear();
    }

    public void clearSavedNodes() {
        savedNodes.clear();
    }
}
