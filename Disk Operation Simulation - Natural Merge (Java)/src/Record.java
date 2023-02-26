import java.util.Iterator;
import java.util.Random;

public class Record implements Iterable<Integer>{
    public static final int SIZE = 5;
    private static final int MAX_DATA_SIZE = 100;
    private int[] data = new int[Record.SIZE];

    public Record(int[] from_data) {
        data = from_data;
    }

    public Record(){}

    public Record(boolean random) {
        if (random) {
            Random rand = new Random();
            for (int i = 0; i < Record.SIZE; i++) {
                data[i] = rand.nextInt(MAX_DATA_SIZE);
            }
        }
    }

    public Record(String string_data) {
        String[] str_data = string_data.split(" ");
        for (int i = 0; i < Record.SIZE; i++) {
            data[i] = Integer.parseInt(str_data[i+1]);
        }
    }

    public String toString() {
        String result = "{ ";
        for (int i = 0; i < Record.SIZE; i++) {
            result += data[i] + " ";
        }
        result += "}";
        return result;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = -1;

            @Override
            public boolean hasNext() {
                return current+1 < Record.SIZE;
            }

            @Override
            public Integer next() {
                current++;
                return data[current];
            }
        };
    }

    @Override
    public boolean equals(Object other){
        // if other isn't a Page, return false
        if(!(other instanceof Record)){
            return false;
        }
        // if other is a Page, compare data
        Record other_record = (Record) other;
        for(int i = 0; i < Record.SIZE; i++){
            if(data[i] != other_record.data[i]){
                return false;
            }
        }
        return true;
    }


}
