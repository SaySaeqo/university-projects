import BTree.BTree;
import Memory.Memory;
import Memory.MemoryOverflowException;
import Memory.Record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Experiment {


    static final private Random rand = new Random();

    public static int count = 0;

    public static void main(String[] args) throws MemoryOverflowException, IOException {

        File f = new File("experiment_results.csv");
        try (FileWriter fw = new FileWriter(f); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("N,Dodaj_rekord,Odczyty,Zapisy,Usun_rekord,Odczyty,Zapisy," +
                    "Znajdz_rekord,Odczyty,Zapisy,Wyswietl_sekwencyjnie_wszystko,Odczyty,Zapisy");
            bw.newLine();

            final int N = 10_000;
            final int triesOnMethod = 1;
            final int DMax = 3;
            StringBuilder pre;
            List<Integer> list = new ArrayList<>();
            list.add(10);
            list.add(20);
            list.add(50);
            list.add(100);
            list.add(200);
            list.add(500);
            list.add(1000);
            list.add(2000);
            list.add(5000);
            list.add(10_000);
            for (int n : list) {
                bw.write(String.format("%d,", n));

                for (int d = 3; d <= DMax; d++) {
                    Memory.setMemory(134_217_728, 512);
                    //bw.write(String.format("%d,", d));

                    pre = new StringBuilder(String.format("\r%5d/%d Adding... ", n, N));
                    //pre = new StringBuilder("\r[" + "▮".repeat(d - 1) + " ".repeat(DMax - d + 1) + "]\tAdding... ");
                    //---------------
                    System.out.print(pre);
                    //--------------
                    // ADDING RECORD
                    long totalTime = 0;
                    long totalReadCount = 0;
                    long totalWriteCount = 0;
                    for (int i = 0; i < triesOnMethod; i++) {
                        //---------------
                        pre.append("I");
                        System.out.print(pre + " Distributing...");
                        //--------------
                        // randomly distributed tree
                        BTree bTree = new BTree(d);
                        int[] keys = rand.ints(n, 0, 64_000).toArray();
                        count = 0;
                        String pree = pre.toString();
                        int finalN = n;
                        Arrays.stream(keys).forEach((key) -> {
                            try {
                                bTree.add(Record.randomRecord(key));
                                System.out.print(pree + String.format(" Distributing... %4d/%d", ++count, finalN));
                            } catch (IOException | MemoryOverflowException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        //---------------
                        System.out.print(pre + " Finding...");
                        //--------------
                        // find key not already in tree
                        int rKey = 0;
                        boolean founded = false;
                        do {
                            rKey = rand.nextInt(64_000);
                            final int finalRKey = rKey;
                            founded = Arrays.stream(keys).parallel().noneMatch((key) -> key == finalRKey);
                        } while (!founded);

                        //---------------
                        System.out.print(pre + " Adding...");
                        //--------------
                        // measure time
                        int r = Memory.getInstance().getReadCount();
                        int w = Memory.getInstance().getWriteCount();
                        long start = System.nanoTime();
                        bTree.add(Record.randomRecord(rKey));
                        totalTime += System.nanoTime() - start;
                        totalWriteCount += Memory.getInstance().getWriteCount() - w;
                        totalReadCount += Memory.getInstance().getReadCount() - r;
                    }
                    bw.write(String.format("%d,%d,%d,", totalTime / triesOnMethod, totalReadCount / triesOnMethod,
                            totalWriteCount / triesOnMethod));

                    //---------------
                    System.out.printf("\r%5d/%d Deleting... ", n, N);
                    //System.out.print("\r[" + "▮".repeat(d - 1) + " ".repeat(DMax - d + 1) + "]\tDeleting...\r");
                    //--------------
                    // DELETING RECORD
                    totalTime = 0;
                    totalReadCount = 0;
                    totalWriteCount = 0;
                    for (int i = 0; i < triesOnMethod; i++) {
                        // randomly distributed tree
                        BTree bTree = new BTree(d);
                        int[] keys = rand.ints(n, 0, 64_000).toArray();
                        Arrays.stream(keys).forEach((key) -> {
                            try {
                                bTree.add(Record.randomRecord(key));
                            } catch (IOException | MemoryOverflowException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        // find key already in tree
                        int rKey = 0;
                        boolean founded = false;
                        do {
                            rKey = rand.nextInt(64_000);
                            final int finalRKey = rKey;
                            founded = Arrays.stream(keys).parallel().anyMatch((key) -> key == finalRKey);
                        } while (!founded);

                        // measure time
                        int r = Memory.getInstance().getReadCount();
                        int w = Memory.getInstance().getWriteCount();
                        long start = System.nanoTime();
                        bTree.remove(rKey);
                        totalTime += System.nanoTime() - start;
                        totalWriteCount += Memory.getInstance().getWriteCount() - w;
                        totalReadCount += Memory.getInstance().getReadCount() - r;
                    }
                    bw.write(String.format("%d,%d,%d,", totalTime / triesOnMethod, totalReadCount / triesOnMethod,
                            totalWriteCount / triesOnMethod));

                    //---------------
                    System.out.printf("\r%5d/%d Finding... ", n, N);
                    //System.out.print("\r[" + "▮".repeat(d - 1) + " ".repeat(DMax - d + 1) + "]\tFinding...\r");
                    //--------------
                    // FIND RECORD
                    totalTime = 0;
                    totalReadCount = 0;
                    totalWriteCount = 0;
                    for (int i = 0; i < triesOnMethod; i++) {
                        // randomly distributed tree
                        BTree bTree = new BTree(d);
                        int[] keys = rand.ints(n, 0, 64_000).toArray();
                        Arrays.stream(keys).forEach((key) -> {
                            try {
                                bTree.add(Record.randomRecord(key));
                            } catch (IOException | MemoryOverflowException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        // get any key
                        int rKey = rand.nextInt(64_000);

                        // measure time
                        int r = Memory.getInstance().getReadCount();
                        int w = Memory.getInstance().getWriteCount();
                        long start = System.nanoTime();
                        bTree.find(rKey);
                        totalTime += System.nanoTime() - start;
                        totalWriteCount += Memory.getInstance().getWriteCount() - w;
                        totalReadCount += Memory.getInstance().getReadCount() - r;
                    }
                    bw.write(String.format("%d,%d,%d,", totalTime / triesOnMethod, totalReadCount / triesOnMethod,
                            totalWriteCount / triesOnMethod));

                    //---------------
                    System.out.printf("\r%5d/%d Printing All Records... ", n, N);
                    //System.out.print("\r[" + "▮".repeat(d - 1) + " ".repeat(DMax - d + 1) + "]\tPrinting All Records...\r");
                    //--------------
                    // SHOW ALL RECORDS
                    totalTime = 0;
                    totalReadCount = 0;
                    totalWriteCount = 0;
                    for (int i = 0; i < triesOnMethod; i++) {
                        // randomly distributed tree
                        BTree bTree = new BTree(d);
                        int[] keys = rand.ints(n, 0, 64_000).toArray();
                        Arrays.stream(keys).forEach((key) -> {
                            try {
                                bTree.add(Record.randomRecord(key));
                            } catch (IOException | MemoryOverflowException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        // measure time
                        int r = Memory.getInstance().getReadCount();
                        int w = Memory.getInstance().getWriteCount();
                        long start = System.nanoTime();
                        bTree.readAllRecords();
                        totalTime += System.nanoTime() - start;
                        totalWriteCount += Memory.getInstance().getWriteCount() - w;
                        totalReadCount += Memory.getInstance().getReadCount() - r;
                    }
                    bw.write(String.format("%d,%d,%d", totalTime / triesOnMethod, totalReadCount / triesOnMethod,
                            totalWriteCount / triesOnMethod));
                    bw.newLine();


                }
                //---------------
                //System.out.println("\r[" + "▮".repeat(DMax) + "]\t Done");
                //--------------
            }
            //------------------
            System.out.printf("\r%5d/%d Done\n", N, N);
            //------------------
        }
    }
}
