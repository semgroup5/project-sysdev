package sem.group5.bob.client.mappGenerator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class LogToFile implements Observer
{

    private String fileLocation;
    private String OS = System.getProperty("os.name", "").toUpperCase();

    private ArrayList<String> logData = new ArrayList<>() ;

    public static void main(String arg[]) throws IOException
    {
        LogToFile log = new LogToFile();
        log.crtDirc();

        for (int i = 0; i < 5; i++)
        {
            int j = i+1;
            log.addToList("This is the "+j+"time(s) it writes to the log now...");
        }
        log.logWriter();
    }

    /**
     * todo
     * @param string todo
     */
    public void addToList(String string)
    {
        this.logData.add(string);
    }

    /**
     * todo
     */
    public void resetList()
    {
        this.logData.clear();
    }


    /**
     * todo
     */
    private void logWriter()
    {
        crtFile(OS);
        try
        {
            PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
            for (String aLogData : logData)
            {
                writer.println(aLogData);
            }
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void logOdometryMessage(double x, double y, double theta)
    {
        String result = "ODOMETRY";
        result += " " + x;
        result += " " + y;
        result += " " + theta;
        this.addToList(result);
    }


    private void crtDirc() {
        if (OS.startsWith("WINDOWS")) {
            File createDirc = new File(System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar");
            createDirc.mkdirs();
        } else if (OS.startsWith("LINUX"))
        {
            File createDirc = new File(System.getProperty("user.home")+File.separator+ "BobCar");
            createDirc.mkdirs();
        } else if (OS.startsWith("MAC"))
        {
            File createDirc = new File(System.getProperty("user.home")+File.separator+"Documents"
                    +File.separator+"BobCar");
            createDirc.mkdirs();
        } else
        {
            System.err.println("Sorry, We can't recognize your Operating System!");
        }
    }

    /**
     * todo
     * @param OS todo
     */
    private void crtFile(String OS)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH：mm：ss@yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        String fileName = "BobCarLog" + time + ".txt";

        if(OS.startsWith("WINDOWS"))
        {
            this.fileLocation = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar" + File.separator + fileName;
        }
        else if(OS.startsWith("LINUX"))
        {
            this.fileLocation = System.getProperty("user.home")+File.separator+
                    "BobCar"+ File.separator+ fileName;
        }
        else if(OS.startsWith("MAC"))
        {
            this.fileLocation = System.getProperty("user.home")+File.separator+"Documents"
                    + File.separator +"BobCar"+ File.separator+ fileName;
        }
    }

    /**
     * todo
     * @param obs todo
     * @param o todo
     */
    @Override
    public void update(Observable obs, Object o)
    {
        String pixels = (String) o;
        addToList(pixels);
    }
}