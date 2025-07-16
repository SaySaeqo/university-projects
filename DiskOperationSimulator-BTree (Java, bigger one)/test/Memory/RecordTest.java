package Memory;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @org.junit.jupiter.api.Test
    void randomRecord() {
        // should return random filled record
        Record r = Record.randomRecord(1);
        assert 1 == r.getKey();
        assert r.getData().length == Record.SIZE;
    }

    @org.junit.jupiter.api.Test
    void parse() {
        // record is correct after parse
        Record correct = new Record(10, new int[]{1, 2, 3, 4, 5});
        Record to_check = Record.parse("{ 10 1 2 3 4 5 }");
        assert to_check.equals(correct);
        // throw if incorrect format
        // empty string
        assertThrows(RecordFormatException.class,
                () -> Record.parse(""));
        // empty record
        assertThrows(RecordFormatException.class,
                () -> Record.parse("{ }"));
        // without braces
        assertThrows(RecordFormatException.class,
                () -> Record.parse("10 1 2 3 4 5"));
        // only last brace
        assertThrows(RecordFormatException.class,
                () -> Record.parse("10 1 2 3 4 5 }"));
        // only first brace
        assertThrows(RecordFormatException.class,
                () -> Record.parse("{ 10 1 2 3 4 5"));
        // too little elements
        assertThrows(RecordFormatException.class,
                () -> Record.parse("{ 1 2 3 4 5 }"));
        // too many elements
        assertThrows(RecordFormatException.class,
                () -> Record.parse("{ 10 1 2 3 4 5 6 }"));
        // data elem is not a number
        assertThrows(NumberFormatException.class,
                () -> Record.parse("{ 10 1 2 q 4 5 }"));
        // key is not a number
        assertThrows(NumberFormatException.class,
                () -> Record.parse("{ q 1 2 3 4 5 }"));

    }

    @org.junit.jupiter.api.Test
    void testToString() {
        // string is in correct format
        Record r = new Record(10, new int[]{1, 2, 3, 4, 5});
        assertEquals("key: 10 {  1  2  3  4  5 }", r.toString());
    }

    @org.junit.jupiter.api.Test
    void iterator() {
        // iterate structure works
        Record r = new Record(10, new int[]{1, 2, 3, 4, 5});
        int i = 1;
        for (int data : r) {
            assert i == data;
            i++;
        }
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        // test record
        Record first = new Record(10, new int[]{1, 2, 3, 4, 5});
        // same as test
        Record second = new Record(10, new int[]{1, 2, 3, 4, 5});
        // different key
        Record third = new Record(5, new int[]{1, 2, 3, 4, 5});
        // last elem in data
        Record fourth = new Record(10, new int[]{1, 2, 3, 4, 6});
        // first elem
        Record fifth = new Record(10, new int[]{2, 2, 3, 4, 5});
        // mid elem
        Record sixth = new Record(10, new int[]{1, 2, 2, 4, 5});
        // different type
        Object seventh = new Object();
        // empty structure
        Record eight = null;

        assert first.equals(first);
        assert first.equals(second);
        assert !first.equals(third);
        assert !first.equals(fourth);
        assert !first.equals(fifth);
        assert !first.equals(sixth);
        assert !first.equals(seventh);
        assert !first.equals(eight);
    }

    @Test
    void serializeDeserializeCorrectly() {
        Record r0 = Record.randomRecord(10);
        Record r1 = Record.randomRecord(10);
        Record r2 = Record.randomRecord(10);
        Record r3 = Record.randomRecord(180);
        Record r4 = Record.randomRecord(255);
        File f = new File("test//testRecordSerialization.bin");
        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {

            Record.writeBytes(raf, r0);
            Record.writeBytes(raf, r1);
            Record.writeBytes(raf, r2);
            Record.writeBytes(raf, r3);
            Record.writeBytes(raf, r4);

            raf.seek(0);
            assert r0.equals(Record.readBytes(raf));
            assert r1.equals(Record.readBytes(raf));
            assert r2.equals(Record.readBytes(raf));
            assert r3.equals(Record.readBytes(raf));
            assert r4.equals(Record.readBytes(raf));

        } catch (Exception e) {
            fail("Shouldn't throw.");
        }

        assert f.delete();
    }
}