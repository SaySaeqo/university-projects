package BTree;

import Memory.Memory;
import Memory.MemoryOverflowException;
import Memory.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BTreeTest {

    final int[] data = new int[]{1, 2, 3, 4, 5};


    @AfterEach
    void tearDown() {
        Memory.clearMemory();
    }

    @Test
    void addInTheMiddle() {
        BTree bTree = new BTree(2);
        try {
            bTree.add(new Record(1, data));
            bTree.add(new Record(3, data));
            bTree.add(new Record(4, data));
            bTree.add(new Record(2, data));
            assertEquals(" 1  2  3  4", bTree.asTree());
        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addAsFirst() {
        BTree bTree = new BTree(2);
        try {
            bTree.add(new Record(1, data));
            bTree.add(new Record(3, data));
            bTree.add(new Record(4, data));
            bTree.add(new Record(0, data));
            assertEquals(" 0  1  3  4", bTree.asTree());
        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addWithSplit() {
        BTree bTree = new BTree(2);
        try {
            bTree.add(new Record(1, data));
            bTree.add(new Record(2, data));
            bTree.add(new Record(3, data));
            bTree.add(new Record(4, data));
            bTree.add(new Record(5, data));
            assertEquals("""
                            |0|  3 |1|
                            |0|:  1  2
                            |1|:  4  5""",
                    bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addWithCompensation() {
        BTree bTree = new BTree(2);
        try {
            for (int i = 5; i < 10; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  7 |1|
                            |0|:  5  6
                            |1|:  8  9""",
                    bTree.asTree());
            for (int i = 2; i < 4; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  7 |1|
                            |0|:  2  3  5  6
                            |1|:  8  9""",
                    bTree.asTree());
            bTree.add(new Record(4, data));
            assertEquals("""
                            |0|  5 |1|
                            |0|:  2  3  4
                            |1|:  6  7  8  9""",
                    bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeWithMerge() {
        BTree bTree = new BTree(2);
        try {
            bTree.add(new Record(1, data));
            bTree.add(new Record(2, data));
            bTree.add(new Record(3, data));
            bTree.add(new Record(4, data));
            bTree.add(new Record(5, data));
            bTree.remove(3);
            assertEquals(" 1  2  4  5", bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeWithCompensation() {
        BTree bTree = new BTree(2);
        try {
            for (int i = 0; i < 7; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  2 |1|
                            |0|:  0  1
                            |1|:  3  4  5  6""",
                    bTree.asTree());
            bTree.remove(0);
            assertEquals("""
                            |0|  4 |1|
                            |0|:  1  2  3
                            |1|:  5  6""",
                    bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeNotLeaf() {
        BTree bTree = new BTree(2);
        try {
            for (int i = 0; i < 7; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  2 |1|
                            |0|:  0  1
                            |1|:  3  4  5  6""",
                    bTree.asTree());
            bTree.remove(2);
            assertEquals("""
                            |0|  3 |1|
                            |0|:  0  1
                            |1|:  4  5  6""",
                    bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeWithNewRootAssignment() {
        BTree bTree = new BTree(2);
        try {
            for (int i = 0; i < 5; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  2 |1|
                            |0|:  0  1
                            |1|:  3  4""",
                    bTree.asTree());
            bTree.remove(2);
            assertEquals(" 0  1  3  4", bTree.asTree());
        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeRootCompletely() {
        BTree bTree = new BTree(2);
        try {
            for (int i = 0; i < 5; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  2 |1|
                            |0|:  0  1
                            |1|:  3  4""",
                    bTree.asTree());
            for (int i = 0; i < 5; i++) {
                bTree.remove(i);
            }
            bTree.add(new Record(10, data));
            assertEquals("10", bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void removeNotExistingAndAlreadyDeleted(){
        BTree bTree = new BTree(2);
        try {
            for (int i = 0; i < 10; i++) {
                bTree.add(new Record(i*2, data));
            }
            assertEquals("""
                            |0|  8 |1| 14 |2|
                            |0|:  0  2  4  6
                            |1|: 10 12
                            |2|: 16 18""",
                    bTree.asTree());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                stringBuilder.append(String.format("\nkey: %2d {  1  2  3  4  5 }", i*2));
            }
            stringBuilder.deleteCharAt(0);
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());

            bTree.remove(15);   // remove not existing
            assertEquals("""
                            |0|  8 |1| 14 |2|
                            |0|:  0  2  4  6
                            |1|: 10 12
                            |2|: 16 18""",
                    bTree.asTree());
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());

            bTree.remove(0);
            assertEquals("""
                            |0|  8 |1| 14 |2|
                            |0|:  2  4  6
                            |1|: 10 12
                            |2|: 16 18""",
                    bTree.asTree());
            stringBuilder.delete(0,"\nkey: %2d {  1  2  3  4  5 }".length()-1);
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());
            bTree.remove(0);    // delete already deleted
            assertEquals("""
                            |0|  8 |1| 14 |2|
                            |0|:  2  4  6
                            |1|: 10 12
                            |2|: 16 18""",
                    bTree.asTree());
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void BTreeWithMinimalDParam() {
        BTree bTree = new BTree(1);
        try {
            for (int i = 5; i < 10; i++) {
                bTree.add(new Record(i, data));
            }
            assertEquals("""
                            |0|  7 |1|
                            |0|:  5  6
                            |1|:  8  9""",
                    bTree.asTree());
            bTree.add(new Record(10, data));    // split
            assertEquals("""
                            |0|  7 |1|  9 |2|
                            |0|:  5  6
                            |1|:  8
                            |2|: 10""",
                    bTree.asTree());
            bTree.add(new Record(3, data)); // compensation on add
            assertEquals("""
                            |0|  6 |1|  9 |2|
                            |0|:  3  5
                            |1|:  7  8
                            |2|: 10""",
                    bTree.asTree());
            bTree.add(new Record(4, data)); // multi split
            assertEquals("""
                            |0|  6 |1|
                            |0|: |0|  4 |1|
                              |0|:  3
                              |1|:  5
                            |1|: |0|  9 |1|
                              |0|:  7  8
                              |1|: 10""",
                    bTree.asTree());

            bTree.remove(10);   // compensation on remove
            assertEquals("""
                            |0|  6 |1|
                            |0|: |0|  4 |1|
                              |0|:  3
                              |1|:  5
                            |1|: |0|  8 |1|
                              |0|:  7
                              |1|:  9""",
                    bTree.asTree());
            bTree.remove(3);    // multi merge
            assertEquals("""
                            |0|  6 |1|  8 |2|
                            |0|:  4  5
                            |1|:  7
                            |2|:  9""",
                    bTree.asTree());
            bTree.remove(8);    // remove a not leaf with merge
            assertEquals("""
                            |0|  6 |1|
                            |0|:  4  5
                            |1|:  7  9""",
                    bTree.asTree());
            bTree.remove(9);
            bTree.remove(6);    // remove a not leaf with compensation
            assertEquals("""
                            |0|  5 |1|
                            |0|:  4
                            |1|:  7""",
                    bTree.asTree());

        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void massiveBTreeTest() {
        BTree bTree = new BTree(1);
        try {
            Memory.setMemory(4096, 16);
            for (int i = 1; i < 150; i += 2) {
                bTree.add(new Record(i, data));
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < 150; i += 2) {
                stringBuilder.append(String.format("\nkey: %2d {  1  2  3  4  5 }", i));
            }
            stringBuilder.deleteCharAt(0);
            assertEquals(stringBuilder.toString(), bTree.readAllRecords());

            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1| 65 |2|
                                |0|: 55 57
                                |1|: 61 63
                                |2|: 67 69
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 137 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 143 |1| 147 |2|
                                |0|: 139 141
                                |1|: 145
                                |2|: 149""",
                    bTree.asTree());
            bTree.remove(147);  // delete not leaf with merge
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1| 65 |2|
                                |0|: 55 57
                                |1|: 61 63
                                |2|: 67 69
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 137 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 143 |1|
                                |0|: 139 141
                                |1|: 145 149""",
                    bTree.asTree());
            bTree.remove(137);  // delete not leaf 2 level
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1| 65 |2|
                                |0|: 55 57
                                |1|: 61 63
                                |2|: 67 69
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 139 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 143 |1|
                                |0|: 141
                                |1|: 145 149""",
                    bTree.asTree());
            bTree.remove(139);  // delete not leaf with compensation
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1| 65 |2|
                                |0|: 55 57
                                |1|: 61 63
                                |2|: 67 69
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 141 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 145 |1|
                                |0|: 143
                                |1|: 149""",
                    bTree.asTree());
            bTree.remove(57);
            bTree.remove(63);
            bTree.remove(69);
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1| 65 |2|
                                |0|: 55
                                |1|: 61
                                |2|: 67
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 141 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 145 |1|
                                |0|: 143
                                |1|: 149""",
                    bTree.asTree());
            bTree.remove(67);   // delete leaf with merge
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1| 89 |2|
                              |0|: |0| 59 |1|
                                |0|: 55
                                |1|: 61 65
                              |1|: |0| 77 |1| 83 |2|
                                |0|: 73 75
                                |1|: 79 81
                                |2|: 85 87
                              |2|: |0| 95 |1| 101 |2|
                                |0|: 91 93
                                |1|: 97 99
                                |2|: 103 105
                            |2|: |0| 125 |1| 141 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 145 |1|
                                |0|: 143
                                |1|: 149""",
                    bTree.asTree());
            bTree.remove(65);   // prep for sth bigger
            bTree.remove(81);
            bTree.remove(87);
            bTree.remove(85);
            bTree.remove(93);
            bTree.remove(99);
            bTree.remove(105);
            bTree.remove(103);
            bTree.remove(101);
            bTree.remove(97);
            bTree.remove(95);
            bTree.remove(91);
            bTree.remove(89);
            assertEquals("""
                            |0| 53 |1| 107 |2|
                            |0|: |0| 17 |1| 35 |2|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                              |2|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                            |1|: |0| 71 |1|
                              |0|: |0| 59 |1|
                                |0|: 55
                                |1|: 61
                              |1|: |0| 77 |1|
                                |0|: 73 75
                                |1|: 79 83
                            |2|: |0| 125 |1| 141 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 145 |1|
                                |0|: 143
                                |1|: 149""",
                    bTree.asTree());
            bTree.remove(53);       // remove not leaf with multiple merges and compensation
            assertEquals("""
                            |0| 35 |1| 107 |2|
                            |0|: |0| 17 |1|
                              |0|: |0|  5 |1| 11 |2|
                                |0|:  1  3
                                |1|:  7  9
                                |2|: 13 15
                              |1|: |0| 23 |1| 29 |2|
                                |0|: 19 21
                                |1|: 25 27
                                |2|: 31 33
                            |1|: |0| 55 |1|
                              |0|: |0| 41 |1| 47 |2|
                                |0|: 37 39
                                |1|: 43 45
                                |2|: 49 51
                              |1|: |0| 71 |1| 77 |2|
                                |0|: 59 61
                                |1|: 73 75
                                |2|: 79 83
                            |2|: |0| 125 |1| 141 |2|
                              |0|: |0| 113 |1| 119 |2|
                                |0|: 109 111
                                |1|: 115 117
                                |2|: 121 123
                              |1|: |0| 131 |1|
                                |0|: 127 129
                                |1|: 133 135
                              |2|: |0| 145 |1|
                                |0|: 143
                                |1|: 149""",
                    bTree.asTree());
        } catch (IOException | MemoryOverflowException e) {
            fail(e.getMessage());
        }
    }


}