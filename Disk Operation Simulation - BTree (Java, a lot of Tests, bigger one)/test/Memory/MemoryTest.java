package Memory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {

    @AfterEach
    void tearDown() {
        Memory.clearMemory();
    }

    @Test
    void resolveRecordAddress() {
        assertEquals(0x0101, Memory.recordAddress(1, 1));
        assertEquals(2, Memory.pageNumber(0x0234));
        assertEquals(0x34, Memory.recordIdx(0x0234));
        assertEquals(0x1234,
                Memory.recordAddress(Memory.pageNumber(0x1234), Memory.recordIdx(0x1234)));
    }

    @Test
    void addingReadingRecords() {
        int[] data = new int[]{1, 2, 3, 4, 5};
        try {
            List<Integer> addresses = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                addresses.add(Memory.getInstance().addRecord(new Record(i, data)));
            }
            for (int i = 0; i < addresses.size(); i++) {
                assertEquals(new Record(i, data), Memory.getInstance().getRecord(addresses.get(i)));
            }
            for (int i = 0; i < addresses.size(); i++) {
                assertEquals(new Record(i, data), Memory.getInstance().getRecord(addresses.get(i)));
            }
        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }

    }

}