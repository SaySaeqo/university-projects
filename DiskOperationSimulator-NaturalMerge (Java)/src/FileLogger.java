import java.io.IOException;
import java.util.logging.*;

public class FileLogger extends Logger {

    public FileLogger(String name) {
        super(name, null);
        setUseParentHandlers(false);
        try {
            FileHandler fh = new FileHandler("resources//natural_merge.log");
            SimpleFormatter sf = new SimpleFormatter(){
                @Override
                public String format(LogRecord record) {
                    return record.getMessage() + "\r\n";
                }
            };
            sf.formatMessage(new LogRecord(Level.INFO, "Start natural merge"));
            fh.setFormatter(sf);
            addHandler(fh);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
