import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Disk implements Iterable<Record> {
    private final String path_to_file;
    private int read_count = 0;
    private int write_count = 0;

    public Disk(String from_path) {
        path_to_file = from_path;
        new File(path_to_file).delete();
    }

    // Get first record from start of disk and remove it
    public Record read() throws EmptyBufferException {
        String all_data = "";
        try (FileReader fr = new FileReader(path_to_file);
             BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                all_data += br.readLine();
            }
        } catch (Exception e) {
            throw new EmptyBufferException(this);
        }
        try (FileWriter fw = new FileWriter(path_to_file);
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(all_data.substring(all_data.indexOf("}") + 1));
        } catch (Exception e) {
            Logger.getLogger(Disk.class.getName()).log(java.util.logging.Level.WARNING, e.getMessage());
        }
        // if empty throw EmptyBufferException
        if (all_data.isEmpty()) {
            throw new EmptyBufferException(this);
        }
        read_count++;
        return new Record(all_data.substring(0, all_data.indexOf("}") + 1));
    }

    // Write record to end of disk
    public void write(Record record){
        try (FileWriter fw = new FileWriter(path_to_file, true);
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(record.toString());
        } catch (Exception e) {
            Logger.getLogger(Disk.class.getName()).log(java.util.logging.Level.WARNING, null, e);
        }
        write_count++;
    }

    public void write(Disk disk) {
        for (Record record : disk) {
            write(record);
        }
    }

    public int getReadCount() {
        return read_count;
    }

    public int getWriteCount() {
        return write_count;
    }

    @Override
    public Iterator<Record> iterator() {
        return new Iterator<Record>() {
            private Record next = null;

            @Override
            public boolean hasNext() {
                try {
                    next = read();
                } catch (EmptyBufferException e) {
                    return false;
                }
                return true;
            }

            @Override
            public Record next() {
                return next;
            }
        };
    }

    public String getPathToFile() {
        return path_to_file;
    }

    @Override
    public String toString() {
        try (FileReader fr = new FileReader(path_to_file);
             BufferedReader br = new BufferedReader(fr)) {
            String all_data = "";
            while (br.ready()) {
                all_data += br.readLine();
            }
            return all_data;
        } catch (Exception e) {
            Logger.getLogger(Disk.class.getName()).log(java.util.logging.Level.SEVERE, e.getMessage());
        }
        return "Error";
    }

    public boolean isEmpty() {
        return toString().isEmpty();
    }
}
