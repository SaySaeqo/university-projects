import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static boolean questedOrder(Record a, Record b) {
        int a_mean = 0;
        for (int i : a) {
            a_mean += i;
        }
        a_mean /= Record.SIZE;
        int b_mean = 0;
        for (int i : b) {
            b_mean += i;
        }
        b_mean /= Record.SIZE;
        return a_mean <= b_mean;
    }

    public static Record questedOrderMin() {
        int[] data = new int[Record.SIZE];
        for (int i = 0; i < Record.SIZE; i++) {
            data[i] = Integer.MIN_VALUE;
        }
        return new Record(data);
    }

    public static void main(String[] args) {
        // start experiment
        experimentInteractive(12);

//        List<List<Integer>> results = new ArrayList<>();
//        // print progressbar
//        System.out.print("0\t/1000");
//        for (int i = 0; i < 1000; i++) {
//            results.add(experiment(50));
//            System.out.print("\r" + (i+1) + "\t/1000");
//        }
//
//        // save results to file
//        try(FileWriter fw = new FileWriter("resources//results.txt")) {
//            for (List<Integer> result : results) {
//                for (Integer integer : result) {
//                    fw.write(integer + "\t");
//                }
//                fw.write("\n");
//            }
//        } catch (IOException e) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
//        }

        // read results from file
//        List<List<Integer>> results = new ArrayList<>();
//        try(FileReader fr = new FileReader("resources//results.txt")) {
//            Scanner sc = new Scanner(fr);
//            while (sc.hasNextLine()) {
//                String[] line = sc.nextLine().split("\t");
//                List<Integer> result = new ArrayList<>();
//                for (String s : line) {
//                    result.add(Integer.parseInt(s));
//                }
//                results.add(result);
//            }
//        } catch (IOException e) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
//        }
//
//        List<Integer> roundsCounters = new ArrayList<>();
//        List<Integer> reads = new ArrayList<>();
//        List<Integer> writes = new ArrayList<>();
//        for (List<Integer> result : results) {
//            roundsCounters.add(result.get(0));
//            reads.add(result.get(1));
//            writes.add(result.get(2));
//        }

//        Plot plt = Plot.create();
//        plt.hist()
//                .add(roundsCounters).add(reads).add(writes)
//                .bins(20)
//                .stacked(false)
//                .color("#66DD66", "#6688FF");
//        plt.xlim(0, 1000);
//        plt.title("reads/writes over 1000 experiments");
//        try {
//            plt.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (PythonExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static List<Integer> experiment(int numberOfPages) {
        // Create disk and tapes
        Disk disk = new Disk("resources//disk.txt");
        List<Disk> tapes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tapes.add(new Disk("resources//tape" + i + ".txt"));
        }

        // Write random pages to disk
        for (int i = 0; i < numberOfPages; i++) {
            disk.write(new Record(true));
        }

        // Sort pages on disk
        int howManyRounds = natural_merge(disk, "2+1", tapes, true);

        // return statistics
        int read_count = disk.getReadCount();
        for (Disk tape : tapes) {
            read_count += tape.getReadCount();
        }
        int write_count = disk.getWriteCount();
        for (Disk tape : tapes) {
            write_count += tape.getWriteCount();
        }
        return Arrays.asList(howManyRounds, read_count, write_count);
    }

    public static void experimentInteractive(int numberOfPages) {
        // Create disk and tapes
        Disk disk = new Disk("resources//disk.txt");
        List<Disk> tapes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tapes.add(new Disk("resources//tape" + i + ".txt"));
        }

        // Choose pages generation method
        System.out.println("Choose records generation method:");
        System.out.println("1. Random");
        System.out.println("2. From file");
        System.out.println("3. From console");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("1")) {
            // Write random pages to disk
            for (int i = 0; i < numberOfPages; i++) {
                disk.write(new Record(true));
            }
        }
        if (answer.equals("3")) {
            // Write pages from keyboard to disk
            System.out.print("Enter " + numberOfPages * Record.SIZE + " numbers: ");
            loadDisk(numberOfPages, disk, scanner);
        }
        if (answer.equals("2")) {
            // Write pages from file to disk
            System.out.print("Enter file path: ");
            String file_name = scanner.nextLine();
            try {
                Scanner file_scanner = new Scanner(new java.io.File(file_name));
                loadDisk(numberOfPages, disk, file_scanner);
            } catch (IOException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
            }
        }
        // Show parameters of experiment
        System.out.println("Records on disk: " + numberOfPages);
        System.out.println("Disk before sorting: " + disk);

        // Sort pages on disk
        int howManyRounds = natural_merge(disk, "2+1", tapes);

        // Show statistics
        int read_count = disk.getReadCount();
        for (Disk tape : tapes) {
            read_count += tape.getReadCount();
        }
        int write_count = disk.getWriteCount();
        for (Disk tape : tapes) {
            write_count += tape.getWriteCount();
        }
        System.out.println("Number of rounds: " + Integer.toString(howManyRounds));
        System.out.println("Read count: " + read_count);
        System.out.println("Write count: " + write_count);
        // check if disk is sorted
        System.out.println("Disk is sorted? " + isSorted(disk));

        System.out.println("Disk after sorting: " + disk);
    }

    private static void loadDisk(int numberOfPages, Disk disk, Scanner scanner) {
        for (int i = 0; i < numberOfPages * Record.SIZE; i++) {
            int[] data = new int[Record.SIZE];
            for (int j = 0; j < Record.SIZE; j++) {
                data[j] = scanner.nextInt();
            }
            disk.write(new Record(data));
        }
    }

    public static boolean isSorted(Disk disk) {
        Disk backup = new Disk("resources//backup.txt");
        Record prev = questedOrderMin();
        for (Record record : disk) {
            if (!questedOrder(prev, record)) {
                return false;
            }
            prev = record;
            backup.write(record);
        }
        // restore disk
        disk.write(backup);
        return true;
    }

    // distribute data from disk to tape 1 and 2
    public static void distribute(Disk disk, Disk tape1, Disk tape2) {
        Disk[] tapes = new Disk[2];
        tapes[0] = tape1;
        tapes[1] = tape2;
        int current = 0;
        // copy all data from disk to tape until quested order is met then change tape
        Record prev = questedOrderMin();
        for (Record r : disk) {
            if (!questedOrder(prev, r)) {
                current = (current + 1) % 2;
            }
            tapes[current].write(r);
            prev = r;
        }
    }

    public static void merge(Disk input1, Disk input2, Disk output) {
        // LOCAL VARIABLES
        final Disk[] inputs = new Disk[]{ input1, input2 };
        final Record[] currentRecords;
        try {
            currentRecords = new Record[]{ inputs[0].read(), inputs[1].read() };
        } catch (EmptyBufferException e) {
            throw new RuntimeException("Merging with empty tape.", e);
        }
        int smaller;
        Record nextVal;

        // LOGIC
        while (true) {
            // choose tape with the smallest value
            smaller = questedOrder(currentRecords[1], currentRecords[0]) ? 1 : 0;
            // write value to output`
            output.write(currentRecords[smaller]);
            // then read next value from tape
            try {
                nextVal = inputs[smaller].read();
            } catch (EmptyBufferException e) {
                // if tape is empty then write rest of another tape to output
                output.write(currentRecords[1 - smaller]);
                for (Record r : inputs[1 - smaller]) {
                    output.write(r);
                }
                return;
            }
            // check if set ended already
            try {
                if (!questedOrder(currentRecords[smaller], nextVal)) { // if set is over
                    // write rest of the current set from another tape
                    output.write(currentRecords[1 - smaller]);
                    Record nextVal_other = inputs[1 - smaller].read();
                    // currentRecords holds previous record
                    // nextVal holds the next one
                    while (questedOrder(currentRecords[1 - smaller], nextVal_other)) { // while in set
                        output.write(nextVal_other);
                        currentRecords[1 - smaller] = nextVal_other;
                        nextVal_other = inputs[1 - smaller].read();
                    }
                    currentRecords[1 - smaller] = nextVal_other;
                }
            } catch (EmptyBufferException e){
                // if tape became empty then another whole tape is going to output
                output.write(nextVal);
                for (Record r : inputs[smaller]) {
                    output.write(r);
                }
                return;
            }
            // prep for next loop round
            currentRecords[smaller] = nextVal;
        }
    }

    public static int natural_merge(Disk disk, String scheme, List<Disk> tapes) {
        return natural_merge(disk, scheme, tapes, false);
    }

    public static int natural_merge(Disk disk, String scheme, List<Disk> tapes, boolean silent) {
        // create Logger for file logs
        Logger fileLog = Logger.getLogger("fileLog");
        fileLog.setLevel(Level.OFF);
        if (!silent) {
            fileLog = new FileLogger(Main.class.getName());
        }

        // check if scheme is valid
        switch (scheme) {
            case "2+1":
                break;
            case "2+2":
                throw new RuntimeException("Not implemented yet");
            default:
                Logger.getLogger("natural_merge").log(Level.WARNING, "Wrong scheme");
                return 0;
        }

        // logs
        fileLog.log(Level.INFO, "Disk before distribute: " + disk);

        {
            int read_count = disk.getReadCount();
            for (Disk tape : tapes) {
                read_count += tape.getReadCount();
            }
            int write_count = disk.getWriteCount();
            for (Disk tape : tapes) {
                write_count += tape.getWriteCount();
            }
            fileLog.log(Level.INFO, "Reads-Writes : " + read_count + "-" + write_count);
            fileLog.log(Level.INFO, "");
        }
        int round = 0;
        while (true) {
            round++;

            // distribute data from disk to tape 1 and 2 if round is 1
            if (round == 1) {
                distribute(disk, tapes.get(0), tapes.get(1));
            } else {
                // else distribute data from tape 3 to tape 1 and 2
                distribute(tapes.get(2), tapes.get(0), tapes.get(1));
            }

            // logs
            fileLog.log(Level.INFO, "Round " + round);
            fileLog.log(Level.INFO, "Tape1: " + tapes.get(0));
            fileLog.log(Level.INFO, "Tape2: " + tapes.get(1));

            // if already sorted then write to disk and finito
            if (tapes.get(0).isEmpty() || tapes.get(1).isEmpty()) {
                disk.write(tapes.get(0));
                disk.write(tapes.get(1));
                break;
            }

            // merge to output tape
            merge(tapes.get(0), tapes.get(1), tapes.get(2));

            // logs
            fileLog.log(Level.INFO, "Tape3: " + tapes.get(2));
            int read_count = disk.getReadCount();
            for (Disk tape : tapes) {
                read_count += tape.getReadCount();
            }
            int write_count = disk.getWriteCount();
            for (Disk tape : tapes) {
                write_count += tape.getWriteCount();
            }
            fileLog.log(Level.INFO, "Reads-Writes : " + read_count + "-" + write_count);
            fileLog.log(Level.INFO, "");
        }

        return round;
    }
}