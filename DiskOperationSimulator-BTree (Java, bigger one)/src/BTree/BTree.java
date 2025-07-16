package BTree;

import Memory.Memory;
import Memory.MemoryOverflowException;
import Memory.Record;

import java.io.IOException;

public class BTree {

    private final int dParam;
    private BTreeNode root;
    private int hParam;
    private int elemsNb;

    public BTree(int halfSizeOfKeysArrayInNode) {
        dParam = halfSizeOfKeysArrayInNode;
        root = null;
        hParam = 0;
    }

    public String readAllRecords() throws IOException {
        StringBuilder result = new StringBuilder();
        root.forEach((elem) -> {
            result.append("\n");
            try {
                result.append(Memory.getInstance().getRecord(elem.address));
            } catch (IOException e) {
                result.append("Couldn't read.");
            } catch (MemoryOverflowException e) {
                throw new RuntimeException("Memory overflow while reading.");
            }
        });
        return result.deleteCharAt(0).toString();
    }

    public void add(Record record) throws IOException, MemoryOverflowException {
        // root doesn't exist (h==0)
        if (root == null) {
            root = new BTreeNode(dParam);
            root.elems.add(Memory.nonExistingPage, record.getKey(), Memory.getInstance().addRecord(record));
            root.elems.add(Memory.nonExistingPage); // afterChild
            hParam = 1;
            elemsNb++;
            Memory.getInstance().writeAllNodes();
            return;
        }
        final BTreeNode node = _find(record.getKey());
        try {
            node.add(record.getKey(), Memory.getInstance().addRecord(record));
        } catch (KeyAlreadyInNodeException inNode) {
            // replace record in-place
            Memory.getInstance().setRecord(node.elems.get(inNode.atPosition).address, record);
            return;
        }
        if (root.hasParent()) {
            root.save();
            root = root.getParent();
            hParam++;
        }
        elemsNb++;
        Memory.getInstance().writeAllNodes();
    }

    public void remove(int key) throws IOException, MemoryOverflowException {
        if (root == null) return;

        final BTreeNode node = _find(key);
        if (!node.remove(key)) return;

        if (root.isEmpty() && root.isLeaf()) {
            root.free();
            root = null;
            hParam = 0;
        } else if (root.isEmpty()) {    // only element in root is his afterChild (no key+record elements)
            BTreeNode candidate = root.getChild(0);
            candidate.setRoot();
            root.free();
            root = candidate;
            hParam--;
        }
        elemsNb--;
        Memory.getInstance().writeAllNodes();
    }

    private BTreeNode _find(int key) throws IOException {
        BTreeNode node = root;
        while (!node.isLeaf()) {
            int idx = node.find(key);
            if (node.elems.get(idx).key == key && idx != node.elems.size() - 1) { // is not afterChild.key (can be random)
                return node;
            }
            node = node.getChild(idx);
        }
        return node;
    }

    public Record find(int key) throws IOException, MemoryOverflowException {
        if (root == null) return null;
        BTreeNode node = _find(key);
        int idx = node.find(key);
        Memory.getInstance().clearSavedNodes();
        if (node.elems.get(idx).key == key) {
            return Memory.getInstance().getRecord(node.elems.get(idx).address);
        }
        return null;
    }

    private BTreeNode _first() throws IOException {
        BTreeNode node = root;
        while (!node.isLeaf()) {
            node = node.getChild(0);
        }
        return node;
    }

    public Record first() throws IOException {
        if (root == null) return null;

        Record record;
        try {
            record = Memory.getInstance().getRecord(_first().elems.get(0).address);
        } catch (MemoryOverflowException e) {
            throw new RuntimeException("Memory overflow while reading.");
        }
        Memory.getInstance().clearSavedNodes();
        return record;
    }


    public String asTree() throws IOException {
        return root.asTree();
    }

    public String statistics() {
        return String.format("D|H|Total: %2d | %2d | %d", dParam, hParam, elemsNb);
    }

    public BTreeNode getRoot() {
        return root;
    }
}
