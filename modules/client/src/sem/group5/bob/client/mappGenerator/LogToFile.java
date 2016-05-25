package sem.group5.bob.client.mappGenerator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;


public class LogToFile implements Observer{
    private String fileLocation;
    private String OS = System.getProperty("os.name", "").toUpperCase();
    private ArrayList<String> logData = new ArrayList<>() ;
    private PrintWriter writer;
    private int[] scanLineArray;
    private  File createDirc;

    /**
     * The following function create a PringWriter and calls upon crtFile
     * @throws IOException
     */
    public LogToFile() throws IOException{
        crtFile();
       writer = new PrintWriter(fileLocation, "UTF-8");
    }

    /**
     * The following function write the odometry data into the file
     * @param x It's a double passed from PoseManager, the X coordinate of the car
     * @param y It's a double passed from PoseManager, the Y coordinate of the car
     * @param theta It's a double passed from PoseManager, the angle of the car
     **/
    public void logOdometryFormatter(double x, double y, double theta){
        try{
            writer.println(x + y + theta);
            writer.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The following function write the DepthData into the file
     * @param array the array that will be assigned as the scanLineArray which is containing the array of Red Pixels
     */
    public void logDepthData(int [] array){
        this.scanLineArray = array;
        try{
            writer.println(/*add depth data here*/);
            writer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Add the string into logData
     * @param string The string that need to be stored in the logData
     */
    public void addToList(String string){
        this.logData.add(string);
    }

    /**
     * The following function reset the ArrayList logData
     */
    public void resetList(){
        this.logData.clear();
    }

    /**
     * The following function creates a folder named "BobCar" according to the OS the program is currently running on
     */
    private void crtDirc() {
        if (OS.startsWith("WINDOWS")) {
            this.createDirc = new File(System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar");
            createDirc.mkdirs();
        } else if (OS.startsWith("LINUX"))
        {
            this.createDirc = new File(System.getProperty("user.home")+File.separator+ "BobCar");
            createDirc.mkdirs();
        } else if (OS.startsWith("MAC"))
        {
            this.createDirc = new File(System.getProperty("user.home")+File.separator+"Documents"
                    +File.separator+"BobCar");
            createDirc.mkdirs();
        } else
        {
            System.err.println("Sorry, We can't recognize your Operating System!");
        }
    }

    /**
     * The following functions creates a txt file according to the operating system
     * at location stored in this.createDirc
     * with the name formatted as:
     * "HH：mm：ss@yyyy-MM-dd" Hours : mins : secs @(ar)Year-Month-date
     */
    private void crtFile() {
        crtDirc();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH：mm：ss@yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        String fileName = "BobCarLog" + time + ".txt";

        if(OS.startsWith("WINDOWS"))
        {
            this.fileLocation = this.createDirc + File.separator + fileName;
        }
        else if(OS.startsWith("LINUX"))
        {
            this.fileLocation =  this.createDirc + File.separator+ fileName;
        }
        else if(OS.startsWith("MAC"))
        {
            this.fileLocation =  this.createDirc + File.separator+ fileName;
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