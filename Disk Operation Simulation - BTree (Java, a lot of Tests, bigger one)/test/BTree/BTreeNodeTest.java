package BTree;

import Memory.Memory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BTreeNodeTest {

    @AfterEach
    void tearDown() {
        Memory.clearMemory();
    }

    @Test
    void serializeDeserialize() {
        try {
            BTreeNode node = new BTreeNode(2);
            node.elems.add(Memory.nonExistingPage, 10, 10);
            node.elems.add(Memory.nonExistingPage, 11, 10);
            node.elems.add(Memory.nonExistingPage);
            node.save();
            assertEquals((Object) node, (Object) BTreeNode.fromPageNumber(2, node.myAddress, false));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    void serializationDeserializationOnSmallPages() {
        try {
            BTreeNode node = new BTreeNode(4);
            Memory.setMemory(256, 2);
            node.elems.add(Memory.nonExistingPage, 10, 1);
            node.elems.add(Memory.nonExistingPage, 11, 10);
            node.elems.add(Memory.nonExistingPage, 12, 10);
            node.elems.add(Memory.nonExistingPage, 13, 10);
            node.elems.add(Memory.nonExistingPage, 14, 10);
            node.elems.add(Memory.nonExistingPage, 15, 10);
            node.elems.add(Memory.nonExistingPage, 16, 10);
            node.elems.add(Memory.nonExistingPage);
            node.allowSaving();
            int nb = Memory.getInstance().writePage(node);
            assertEquals((Object) node, (Object) BTreeNode.fromPageNumber(4, nb, false));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}