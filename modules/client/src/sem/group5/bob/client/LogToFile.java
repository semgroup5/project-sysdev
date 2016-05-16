package sem.group5.bob.client;



import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by chyj1 on 2016-5-12.
 */

public class LogToFile {
    String time;
    String fileName;
    String fileLocation;
    PrintWriter writer;

    public static void main(String arg[]) throws IOException {

        LogToFile log = new LogToFile();
        log.LogToFile();
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            log.writer("hahahha");
        }
        log.close();
    }

    public void LogToFile() {
        try {
            fileLocation = fileName();
            writer = new PrintWriter(fileLocation, "UTF-8");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void writer(String string) {
        try {

            writer.println(string);

        } catch (Exception e) {

        }
    }

    public void close() {
        writer.close();
    }

    public String fileName() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH：mm：ss@yyyy-MM-dd");
        time = sdf.format(cal.getTime());
        fileName = "BobCarLog" + time + ".txt";
        fileLocation = System.getenv("APPDATA") + "\\BobCar" + "\\" + fileName;
        return fileLocation;
    }
}