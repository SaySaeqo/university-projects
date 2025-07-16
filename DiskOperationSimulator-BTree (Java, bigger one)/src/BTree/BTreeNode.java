package BTree;

import Memory.Memory;
import Memory.MemoryOverflowException;
import Memory.Page;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.function.Consumer;

public class BTreeNode extends Page {

    final NodeElemList elems;
    private final int size;
    private BTreeNode parent;
    private boolean fullyAllocated;

    public BTreeNode(int halfSizeOfData) throws MemoryOverflowException {
        super();
        size = halfSizeOfData * 2;
        elems = new NodeElemList(size);
        Memory.getInstance().cacheNode(this);
    }

    private BTreeNode(int halfSizeOfData, int pageNumber) throws IOException {
        super(pageNumber);
        size = halfSizeOfData * 2;
        elems = new NodeElemList(size);
        fullyAllocated = true;
        Memory.getInstance().readPage(this);
    }

    static BTreeNode fromPageNumber(int halfSizeOfData, int pageNumber, boolean cache) throws IOException {
        if (pageNumber == Memory.nonExistingPage) return null;

        BTreeNode node = Memory.getInstance().getNodeFromCache(pageNumber);
        if (node != null) return node;  // node founded in cache

        node = new BTreeNode(halfSizeOfData, pageNumber);
        if (cache) Memory.getInstance().cacheNode(node);
        return node;
    }

    /**
     * Redistributes nodes from left, right and his parent's node elem, making left node has 1 more elements
     * than right or with the equal size.
     *
     * @return A dummy of parent element.
     */
    private static NodeElem compensate(BTreeNode left, BTreeNode right) {
        NodeElem ourParent = parentElemOf(left, right);

        if (left.elems.size() < right.elems.size()) {
            // making 1 array split to 2 sub arrays by removing end point of left one
            left.elems.setLast(ourParent.key, ourParent.address);

            while (left.elems.size() < right.elems.size() - 2) {
                left.elems.add(right.elems.pick());
            }
            // now left has 1 or 2 fewer elements than right (each loop changes difference by 2)

            ourParent = right.elems.pick();
            left.elems.add(ourParent.beforeChild);
            // now left has 1 more or equal to right
        } else { // left size > right size
            if (left.elems.size() == right.elems.size() + 1) {
                // compensation is done, nothing to change
                return ourParent;
            }
            // analogy to above
            right.elems.add(0, left.elems.poll().beforeChild, ourParent.key, ourParent.address);

            while (right.elems.size() < left.elems.size() - 1) {
                right.elems.add(0, left.elems.poll());
            }
            // now right has 1 fewer elements or equal to left (each loop changes difference by 2)
            // left has 1 more or equal to right

            ourParent = left.elems.poll();
            left.elems.add(ourParent.beforeChild);
            // still left has 1 more or equal to right
        }
        return ourParent;
        // left has 1 more element or equal size to right at the end
    }

    private static NodeElem merge(BTreeNode left, BTreeNode right) {
        NodeElem parentElem = parentElemOf(left, right);

        left.elems.setLast(parentElem.key, parentElem.address);
        right.elems.addAll(0, left.elems);
        left.elems.clear();

        return parentElem;
    }

    private static int parentIndexOf(BTreeNode left, BTreeNode right) {
        try {
            if (left != null) {
                return left.getParent().elems.indexOf((el) -> el.beforeChild == left.myAddress);
            } else if (right != null) {
                return right.getParent().elems.indexOf((el) -> el.beforeChild == right.myAddress);
            }
        } catch (ElementNotFoundException e) {
            throw new InvalidParentException("Assigned parent is not mine.");
        }
        throw new InvalidParentException("Nodes doesn't exists.");
    }

    private static int parentIndexOf(BTreeNode node) {
        return parentIndexOf(node, node);
    }

    private static NodeElem parentElemOf(BTreeNode left, BTreeNode right) {
        return left.getParent().elems.get(parentIndexOf(left, right));
    }

    private static NodeElem parentElemOf(BTreeNode node) {
        return parentElemOf(node, node);
    }

    @Override
    protected void readBytes(RandomAccessFile raf) throws IOException {

        int bytesLeftInPage = Memory.getInstance().pageSizeInBytes;

        int elemsToRead = raf.readUnsignedByte();
        bytesLeftInPage--;
        if (elemsToRead == 0) throw new IllegalArgumentException("Trying to read node with 0 elements.");

        int pagesToRead = getNumberOfAllocatedPages() - 1;

        byte[] elemBuffer = new byte[NodeElem.sizeInBytes];
        int elemBufferIdx = 0;

        while (bytesLeftInPage > 0) {
            if (bytesLeftInPage == 2 && pagesToRead > 0) {
                bytesLeftInPage = Memory.getInstance().pageSizeInBytes;
                raf.seek((long) raf.readUnsignedShort() * Memory.getInstance().pageSizeInBytes);
                pagesToRead--;
            }
            if (elemsToRead > 0) {
                elemBuffer[elemBufferIdx] = raf.readByte();
                elemBufferIdx += 1;
                elemBufferIdx %= NodeElem.sizeInBytes;
                if (elemBufferIdx == 0) {
                    elems.add(NodeElem.fromBytes(elemBuffer));
                    elemsToRead--;
                }
            } else {
                elemBuffer[elemBufferIdx] = raf.readByte();
                elemBufferIdx += 1;
                if (elemBufferIdx == 2) {
                    elems.add(NodeElem.fromBytes(elemBuffer));
                    elemBufferIdx = 0;
                    break;
                }
            }
            bytesLeftInPage--;
        }
        if (elemBufferIdx != 0) throw new RuntimeException("Element in node not created completely.");
    }

    private BTreeNode getOther(int pageNumber, boolean cache) throws IOException {
        return BTreeNode.fromPageNumber(size / 2, pageNumber, cache);
    }

    private BTreeNode getOther(int pageNumber) throws IOException {
        return BTreeNode.fromPageNumber(size / 2, pageNumber, true);
    }

    private BTreeNode makeNode() throws IOException, MemoryOverflowException {
        return new BTreeNode(size / 2);
    }

    private BTreeNode makeSibling() throws IOException, MemoryOverflowException {
        BTreeNode sibling = makeNode();
        sibling.parent = getParent();
        return sibling;
    }

    private void add(int beforeChild, int key, int address) throws IOException, KeyAlreadyInNodeException, MemoryOverflowException {
        int index = find(key);
        if (index != elems.size() - 1 && elems.get(index).key == key) throw new KeyAlreadyInNodeException(index);

        if (!isFull()) {
            elems.add(index, beforeChild, key, address);
        } else {

            // COMPENSATION OR MERGE ON DUMMY OF PARENT-CHILDREN RELATION
            NodeElem parentElem;
            BTreeNode left;
            BTreeNode right;
            if (getLeft() != null && !getLeft().isFull()) {     // compensation possible
                left = getLeft();
                right = this;
                parentElem = compensate(left, right);
            } else if (getRight() != null && !getRight().isFull()) {
                left = this;
                right = getRight();
                parentElem = compensate(left, right);
            } else {                                            // compensation impossible
                left = makeSibling();
                right = this;
                parentElem = split(left);
            }
            // left node has 1 more or equal size to right node

            // ADDING THE RECORD
            if (key < parentElem.key) {
                int idx = left.find(key);
                // parent to right
                right.elems.add(0, left.elems.poll().beforeChild, parentElem.key, parentElem.address);
                // add record to left
                left.elems.add(idx, beforeChild, key, address);
                // left last as parent
                parentElem.key = left.elems.getLast().key;
                parentElem.address = left.elems.getLast().address;
                left.elems.setLast(left.elems.getLast().beforeChild);
            } else {
                right.add(beforeChild, key, address);
            }
            // now record is added and right node has 1 more elements or is equal size to left

            // SETTING DUMMY IN PARENT NODE
            if (left.fullyAllocated) {      // compensation was done
                getParent().elems.set(parentIndexOf(left, right), parentElem.key, parentElem.address);
            } else {                        // split was done
                parentElem.beforeChild = left.myAddress;
                if (hasParent()) {
                    getParent().add(parentElem);
                } else {
                    parent = makeNode();
                    left.parent = getParent();
                    getParent().elems.add(parentElem);
                    getParent().elems.add(right.myAddress);
                }
            }
        }
    }

    /**
     * returns the dummy of new parent element
     */
    private NodeElem split(BTreeNode leftEmptySiblingNode) {
        for (int i = 0; i < size / 2; i++) {
            leftEmptySiblingNode.elems.add(this.elems.pick());
        }
        NodeElem parentElem = this.elems.pick();
        leftEmptySiblingNode.elems.add(parentElem.beforeChild); // it is an after child
        // leftSiblingNode has 1 more element

        return parentElem;
    }

    /**
     * removes Record from node
     * @return : true if key was successfully deleted from this node
     */
    boolean remove(int key) throws IOException, MemoryOverflowException {
        int index = find(key);
        if (elems.get(index).key != key || index == elems.size()-1) return false; // nothing to delete, key is absent in node
        Memory.getInstance().removeRecord(elems.get(index).address);

        if (isLeaf()) {
            _remove(index);
        } else {
            BTreeNode next = nextFor(index);
            if (next.elems.get(0).key <= elems.get(index).key)
                throw new RuntimeException("Node with next element doesn't have next element.");
            elems.set(index, next.elems.get(0).key, next.elems.get(0).address);
            next._remove(0);
        }
        return true;
    }

    /**
     * removes entire nodeElem from node
     */
    private void _remove(int index) throws IOException {
        elems.remove(index);
        if (elems.size() - 1 < size / 2) { // -1 because of the after child at the end, which
            // does not contain own key

            NodeElem parentElem;
            BTreeNode left;
            BTreeNode right;
            if (getLeft() != null && !getLeft().isHalfFull()) {
                left = getLeft();
                right = this;
                parentElem = compensate(left, right);
            } else if (getRight() != null && !getRight().isHalfFull()) {
                left = this;
                right = getRight();
                parentElem = compensate(left, right);
            } else if (getLeft() != null) {
                left = getLeft();
                right = this;
                parentElem = merge(left, right);
            } else if (getRight() != null) {
                left = this;
                right = getRight();
                parentElem = merge(left, right);
            } else { // the node is root
                return;
            }

            if (!left.elems.isEmpty()) {    // compensation was done
                getParent().elems.set(parentIndexOf(left, right), parentElem.key, parentElem.address);
            } else {                        // merge was done
                getParent()._remove(parentIndexOf(left, right));
            }
        }
    }

    void add(int key, int address) throws IOException, KeyAlreadyInNodeException, MemoryOverflowException {
        add(Memory.nonExistingPage, key, address);
    }

    private void add(NodeElem elem) throws IOException, KeyAlreadyInNodeException, MemoryOverflowException {
        add(elem.beforeChild, elem.key, elem.address);
    }

    private int getNumberOfAllocatedPages() {
        int neededBytes = 1 + size * NodeElem.sizeInBytes + 2;
        // number of elements in node
        // each element
        // after child on the end
        int neededPages = 1;
        neededBytes -= Memory.getInstance().pageSizeInBytes;
        while (neededBytes > 0) {
            neededBytes -= Memory.getInstance().pageSizeInBytes;
            neededBytes += 2; //address of new page at the end of previous
            neededPages += 1;
        }
        return neededPages;
    }

    @Override
    public void writeBytes(RandomAccessFile raf) throws IOException, MemoryOverflowException {
        int bytesToWrite = Memory.getInstance().pageSizeInBytes;
        int pagesToWrite = getNumberOfAllocatedPages() - 1; // first is currently written on
        int counter = 0;
        int elemIdx = 0;
        raf.writeByte(elems.size() - 1);
        bytesToWrite--;
        while (bytesToWrite > 0) {
            if (bytesToWrite == 2 && pagesToWrite > 0) {
                pagesToWrite--;
                bytesToWrite = Memory.getInstance().pageSizeInBytes;
                int pageNumber;
                if (fullyAllocated) {
                    pageNumber = raf.readUnsignedShort();
                } else {
                    pageNumber = Memory.getInstance().allocPage();
                    raf.writeShort(pageNumber);
                }
                raf.seek((long) pageNumber * Memory.getInstance().pageSizeInBytes);
            }
            if (elemIdx < elems.size() - 1) {
                elems.get(elemIdx).writeByte(raf, counter);
                if (++counter == NodeElem.sizeInBytes){
                    elemIdx++;
                    counter = 0;
                }
            } else if (elemIdx == elems.size() - 1) {
                // after child at the end of each node (in not leaf also)
                elems.get(elemIdx).writeByte(raf, counter);
                if (++counter == 2){
                    elemIdx++;
                    counter = 0;
                }
            } else {
                if (!fullyAllocated && pagesToWrite > 0) {
                    raf.skipBytes(bytesToWrite - 2);
                    bytesToWrite = 2;
                    continue;
                }
                break;
            }
            bytesToWrite--;
        }
        fullyAllocated = true;

        if (counter != 0) throw new RuntimeException("Element in node not written completely.");
    }

    public String asTree() throws IOException {
        return asTree("");
    }

    public String asTree(String prefix) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < elems.size(); i++) {
            if (!isLeaf()) {
                result.append(String.format("|%d| ", i));
            }
            if (i < elems.size() - 1) {
                result.append(String.format("%2d ", elems.get(i).key));
            }
        }
        result.deleteCharAt(result.length() - 1);
        if (!isLeaf()) {
            for (int i = 0; i < elems.size(); i++) {
                result.append(String.format("\n%s|%d|: ", prefix, i));
                result.append(getChild(i, false).asTree(prefix + "  "));
            }
        }
        return result.toString();
    }


    /**
     * return node which has element with next key than one from given index
     */
    public BTreeNode nextFor(int index) throws IOException {
        if (isLeaf()) {
            BTreeNode next = this;
            if (index == elems.size() - 2) { // size - 1 is always the afterChild (no key, no record)
                next = getParent();
                while (next.find(elems.get(0).key) == next.elems.size() - 1) { // while is in parent's afterChild
                    next = next.getParent();
                }
            }
            return next;
        }

        BTreeNode next = getChild(index + 1);
        while (!next.isLeaf()) {
            next = next.getChild(0);
        }
        return next;

    }

    public void forEach(Consumer<? super NodeElem> action) throws IOException {
        for (int i = 0; i < elems.size(); i++) {
            if (!isLeaf()) {
                getChild(i, false).forEach(action);
            }
            if (i != elems.size() - 1) {
                action.accept(elems.get(i));
            }
        }
    }

    public int find(int key) {
        return elems.find(key);
    }

    /**
     * checks if list of his elements with valid keys reached the size of this node
     */
    public boolean isFull() {
        return elems.size() - 1 == size;
    }

    public boolean isHalfFull() {
        return elems.size() - 1 == Math.ceilDiv(size, 2);
    }

    /**
     * checks if only element in node is his afterChild
     */
    public boolean isEmpty() {
        return elems.size() == 1;
    }

    public boolean isLeaf() {
        try {
            return elems.get(0).beforeChild == Memory.nonExistingPage;
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Node doesn't have elements at all (not even the afterChild)");
        }
    }

    public BTreeNode getParent() {
        return parent;
    }

    private BTreeNode getSibling(int distance, boolean fromCacheOnly) throws IOException {
        if (!hasParent()) return null;   // i am root, i dont have siblings

        int siblingAddress;
        try {
            siblingAddress = getParent().elems.get(parentIndexOf(this) + distance).beforeChild;
        } catch (IndexOutOfBoundsException doesntHaveThisSibling) {
            return null;
        }

        BTreeNode node;
        if (fromCacheOnly) {
            node = Memory.getInstance().getNodeFromCache(siblingAddress);
        } else {
            node = getOther(siblingAddress);
        }
        if (node == null) return null;   // sibling not found (I am leaf or not in cache)
        node.parent = getParent();
        return node;
    }

    public BTreeNode getLeft(boolean fromCacheOnly) throws IOException {
        return getSibling(-1, fromCacheOnly);
    }

    public BTreeNode getLeft() throws IOException {
        return getLeft(false);
    }

    public BTreeNode getRight(boolean fromCacheOnly) throws IOException {
        return getSibling(+1, fromCacheOnly);
    }

    public BTreeNode getRight() throws IOException {
        return getRight(false);
    }

    public void save() throws IOException, MemoryOverflowException {
        if (elems.size() > 1) {
            allowSaving();
            Memory.getInstance().writePage(myAddress, this);
        } else {
            free();
        }
    }

    public void free() {
        Memory.getInstance().free(myAddress);
    }

    public BTreeNode getChild(int index, boolean cache) throws IOException {
        BTreeNode child = getOther(elems.get(index).beforeChild, cache);
        if (child == null) return null; // i am leaf, i don't have children
        child.parent = this;
        return child;
    }

    public BTreeNode getChild(int index) throws IOException {
        return getChild(index, true);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BTreeNode o)) {
            return false;
        }
        return elems.equals(o.elems) && size == o.size;
    }

    public void setRoot() {
        parent = null;
    }

    public boolean hasParent() {
        return getParent() != null;
    }
}
