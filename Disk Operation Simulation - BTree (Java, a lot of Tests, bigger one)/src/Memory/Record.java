package Memory;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Record implements Iterable<Integer> {
    public static final int SIZE = 5;
    private static final int MAX_DATA_SIZE = 255;
    private int key;
    private int[] data = new int[Record.SIZE];


    // empty record by default
    private Record() {
    }

    // for easy making object
    public Record(int withKey, int[] fromData) {
        key = withKey;
        data = fromData;
    }

    // randomize record
    static public Record randomRecord(int withKey) {
        final int[] data = new int[Record.SIZE];
        Random rand = new Random();
        for (int i = 0; i < Record.SIZE; i++) {
            data[i] = rand.nextInt(Record.MAX_DATA_SIZE);
        }
        return new Record(withKey, data);
    }

    // ex. { 10 1 2 3 4 5 } generates Memory.Record with key 10 and numbers 1,2,3,4,5 in it
    // otherwise generates format exception
    static public Record parse(String fromString) throws RecordFormatException, NumberFormatException {
        if (fromString == null) {
            return null;
        }
        final int[] data = new int[Record.SIZE];
        final String[] elems = fromString.split(" ");
        if (elems.length != Record.SIZE + 3) {
            throw new RecordFormatException("Rekord jest zbyt krotki.");
        }
        if (!elems[0].equals("{")) {
            throw new RecordFormatException("Brakuje znaku { na koncu rekordu.");
        }
        final int key = Integer.parseInt(elems[1]);
        for (int i = 0; i < Record.SIZE; i++) {
            data[i] = Integer.parseInt(elems[i + 2]);
        }
        if (!elems[Record.SIZE + 2].equals("}")) {
            throw new RecordFormatException("Brakuje znaku '}' na koncu rekordu.");
        }
        return new Record(key, data);
    }

    // Memory.Record with numbers 1,2,3,4,5 and key 10 has representation of { 10 1 2 3 4 5 }
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("key: %2d { ", key));
        for (int i = 0; i < Record.SIZE; i++) {
            result.append(String.format("%2d", data[i])).append(" ");
        }
        result.append("}");
        return result.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(data).iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Record other_record)) {
            return false;
        }
        for (int i = 0; i < Record.SIZE; i++) {
            if (data[i] != other_record.data[i]) {
                return false;
            }
        }
        return key == other_record.key;
    }


    public int getKey() {
        return key;
    }


    public static int sizeInBytes = SIZE + 2;

    static public void writeBytes(DataOutput out, Record record) throws IOException {
        out.writeShort(record.key);
        for (int i = 0; i < Record.SIZE; i++) {
            out.write(record.data[i]);
        }
    }

    static public Record readBytes(DataInput in) throws IOException {
        final Record r = new Record();
        r.key = in.readUnsignedShort();
        for (int i = 0; i < Record.SIZE; i++) {
            r.data[i] = in.readUnsignedByte();
        }
        return r;
    }

    public int[] getData() {
        return data.clone();
    }
}
