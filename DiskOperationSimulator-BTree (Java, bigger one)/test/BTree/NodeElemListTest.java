package BTree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeElemListTest {

    @Test
    void indexOf() {
        NodeElemList list = new NodeElemList(3);
        list.add(0x12, 1, 10);
        list.add(0x23, 3, 10);
        list.add(0x45, 5, 10);
        list.add(0x67);
        assertEquals(0, list.find(0));
        assertEquals(0, list.find(1));
        assertEquals(1, list.find(2));
        assertEquals(1, list.find(3));
        assertEquals(2, list.find(4));
        assertEquals(2, list.find(5));
        assertEquals(3, list.find(6));
        assertEquals(3, list.find(7));
    }
}