package sem.group5.bob.client;



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by GeoffreyC on 2016-5-12.
 */

public class LogToFile {

    private String fileLocation;
    private String OS = System.getProperty("os.name", "").toUpperCase();

    ArrayList<String> logData = new ArrayList<String>() ;

    public static void main(String arg[]) throws IOException {
        LogToFile log = new LogToFile();
        log.crtDirl();

        for (int i = 0; i < 5; i++) {
            int j = i+1;
            log.addToList("This is the "+j+"time(s) it writes now...");
      }
      log.logWriter();
    }

    public void addToList(String string){
        this.logData.add(string);
    }

    public void resetList(){
        this.logData.clear();
    }


    public void logWriter() {
        crtFile(OS);
        try{
        PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
        for (int i = 0; i < logData.size(); i++) {
            writer.println(logData.get(i));
        }
        writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void crtDirl() {
        if (OS.startsWith("WINDOWS")) {
            File createDir1 = new File(System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar");
            createDir1.mkdirs();
        } else if (OS.startsWith("LINUX")) {
            File createDir1 = new File("/home/BobCar");
            createDir1.mkdirs();
        } else if (OS.startsWith("MAC")) {
            File createDir1 = new File(System.getProperty("user.home")+File.separator+"Documents" +File.separator+"BobCar");
            createDir1.mkdirs();
        } else {
            System.out.println("Sorry, your operating system is different");
        }
    }

    public void crtFile(String OS){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH：mm：ss@yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        String fileName = "BobCarLog" + time + ".txt";
        this.fileLocation = "~/Documents/BobCar/"+ fileName;
        if(OS.startsWith("WINDOWS")) {
            this.fileLocation = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar" + File.separator + fileName;
        }
        else if(OS.startsWith("LINUX")){
            this.fileLocation = "/home/BobCar/"+ fileName;
        }
        else if(OS.startsWith("MAC")){
            this.fileLocation = System.getProperty("user.home")+File.separator+"Documents"
                    + File.separator +"BobCar"+ File.separator+ fileName;
        }
    }
}
