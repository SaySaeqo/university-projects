package BTree;

import java.io.IOException;
import java.io.RandomAccessFile;

class NodeElem {
    public int beforeChild;
    public int key;
    public int address;

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof NodeElem o)) {
            return false;
        }
        return address == o.address &&
                key == o.key &&
                beforeChild == o.beforeChild;
    }

    public static int sizeInBytes = 7;

    public void writeByte(RandomAccessFile raf, int counter) throws IOException {
        switch (counter) {
            case 0 -> raf.writeByte(beforeChild >> Byte.SIZE);
            case 1 -> raf.writeByte(beforeChild);
            case 2 -> raf.writeByte(key >> Byte.SIZE);
            case 3 -> raf.writeByte(key);
            case 4 -> raf.writeByte(address >> Byte.SIZE*2);
            case 5 -> raf.writeByte(address >> Byte.SIZE);
            case 6 -> raf.writeByte(address);
        }
    }

    static public NodeElem fromBytes(byte[] bytes) {
        if (bytes.length < sizeInBytes) throw new IllegalArgumentException("Size of bytes is too small.");
        final NodeElem elem = new NodeElem();
        elem.beforeChild = (Byte.toUnsignedInt(bytes[0]) << Byte.SIZE) + Byte.toUnsignedInt(bytes[1]);
        elem.key = (Byte.toUnsignedInt(bytes[2]) << Byte.SIZE) + Byte.toUnsignedInt(bytes[3]);
        elem.address = Byte.toUnsignedInt(bytes[4]);
        elem.address <<= Byte.SIZE;
        elem.address += Byte.toUnsignedInt(bytes[5]);
        elem.address <<= Byte.SIZE;
        elem.address += Byte.toUnsignedInt(bytes[6]);
        return elem;
    }
}
