package BTree;

import java.util.ArrayList;
import java.util.function.Predicate;

public class NodeElemList extends ArrayList<NodeElem> {

    public NodeElemList(int size) {
        super(size + 1);
    }

    public NodeElem getLast() {
        return get(size() - 1);
    }

    public void set(int index, int beforeChild, int key, int address) {
        NodeElem elem = new NodeElem();
        elem.address = address;
        elem.key = key;
        elem.beforeChild = beforeChild;
        set(index, elem);
    }

    public void set(int index, int key, int address) {
        set(index, get(index).beforeChild, key, address);
    }

    public void setLast(int beforeChild, int key, int address) {
        set(size() - 1, beforeChild, key, address);
    }

    /**
     * sets key and address values to the last NodeElem from list without changing the beforeChild
     */
    public void setLast(int key, int address) {
        setLast(getLast().beforeChild, key, address);
    }

    public void setLast(int afterChild) {
        setLast(afterChild, 0, 0);
    }

    public void add(int index, int beforeChild, int key, int address) {
        NodeElem elem = new NodeElem();
        elem.address = address;
        elem.key = key;
        elem.beforeChild = beforeChild;
        add(index, elem);
    }

    public void add(int beforeChild, int key, int address) {
        NodeElem elem = new NodeElem();
        elem.address = address;
        elem.key = key;
        elem.beforeChild = beforeChild;
        add(elem);
    }

    public void add(int afterChild) {
        add(afterChild, 0, 0);
    }

    public void removeLast() {
        remove(size() - 1);
    }

    public NodeElem poll(boolean fromRight) {
        if (fromRight) {
            NodeElem node = getLast();
            removeLast();
            return node;
        }
        NodeElem node = get(0);
        remove(0);
        return node;
    }

    public NodeElem poll() {
        return poll(true);
    }

    public NodeElem pick() {
        return poll(false);
    }

    // by quicksort
    public int find(int key) {
        int start = 0, end = size() - 1;
        while (start < end) {
            int half = (start + end) / 2;
            if (get(half).key == key) {
                return half;
            } else if (get(half).key < key) {
                start = half + 1;
            } else {
                end = half;
            }
        }
        return start;
    }

    public int indexOf(Predicate<NodeElem> predicate) throws ElementNotFoundException {
        for (int i = 0; i < size(); i++) {
            if (predicate.test(get(i))) {
                return i;
            }
        }
        throw new ElementNotFoundException();
    }
}
