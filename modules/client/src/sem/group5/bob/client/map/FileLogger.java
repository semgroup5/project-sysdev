package sem.group5.bob.client.map;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


/**
 * Output a log file in a PC directory containing the data needed to supply Carmen App in order to generate a map.
 */
public class FileLogger implements Observer{
    private String fileLocation;
    private String OS = System.getProperty("os.name", "").toUpperCase();
    private PrintWriter writer;
    private File logDirectory;
    private File logFile;
    /**
     * Creates a PrintWriter and calls upon createFile
     * @throws IOException
     */
    public FileLogger() throws IOException{
        createFile();
       writer = new PrintWriter(fileLocation, "UTF-8");
    }

    /**
     *
     * @return Current log file
     */
    public File getLogFile(){ return logFile; }

    /**
     * Connects the string containing telemtry data
     * and write it to the log file
     * @param telemetry t
     */
    private void logTelemetry(Telemetry telemetry){
        String result;
//        # ROBOTLASER1 laser_type start_angle field_of_view angular_resolution
//        #   maximum_range accuracy remission_mode
//        #   num_readings [range_readings] laser_pose_x laser_pose_y laser_pose_theta
//        #   robot_pose_x robot_pose_y robot_pose_theta
//        #   laser_tv laser_rv forward_safety_dist side_safty_dist
        int laser_type              = 99;
        double start_angle          = Math.toRadians(telemetry.getPose().getTheta() - 27.5);
        double field_of_view        = Math.toRadians(55);
        double angular_resolution   = Math.toRadians(0.0859375);
        int maximum_range           = 4096;
        int accuracy                = 50;
        int remission_mode          = 1;
        int num_readings            = telemetry.getScanLine().getDistanceCount();
        int[] range_readings        = telemetry.getScanLine().distances;
        double laser_x              = telemetry.pose.getX()/100;
        double laser_y              = telemetry.pose.getY()/100;
        double laser_theta          = telemetry.pose.getTheta()/100;
        double odom_x               = telemetry.pose.getX()/100;
        double odom_y               = telemetry.pose.getY()/100;
        double odom_theta           = telemetry.pose.getTheta()/100;

        result = "ROBOTLASER1 " + laser_type + " " + start_angle + " " + field_of_view + " " + angular_resolution + " ";
        result += maximum_range + " " + accuracy + " " + remission_mode + " " + num_readings +" ";

        for(int range_reading : range_readings) {
            result += range_reading / 1000 + " ";
        }
        result += laser_x + " " + laser_y + " " + laser_theta + " " + odom_x + " " + odom_y + " " + odom_theta;

        try{
            writer.println(result);
            writer.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a folder named "BobCar" with different paths depending on the OS the program is currently running on.
     * Support for Windows, Linux and Mac.
     */
    private void createDirectory() {
        if (OS.startsWith("WINDOWS")) {
            this.logDirectory = new File(System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH")
                    + File.separator +"BobCar");
            logDirectory.mkdirs();
        } else if (OS.startsWith("LINUX"))
        {
            this.logDirectory = new File(System.getProperty("user.home")+File.separator+ "BobCar");
            logDirectory.mkdirs();
        } else if (OS.startsWith("MAC"))
        {
            this.logDirectory = new File(System.getProperty("user.home")+File.separator+"Documents"
                    +File.separator+"BobCar");
            logDirectory.mkdirs();
        } else
        {
            System.err.println("Sorry, We can't recognize your Operating System!");
        }
    }

    /**
     * Creates a txt file according to the operating system
     * at location stored in this.logDirectory
     * with the name formatted as: "HH：mm：ss@yyyy-MM-dd" Hours : mins : secs @(ar)Year-Month-date
     */
    private void createFile() {
        createDirectory();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH：mm：ss@yyyy-MM-dd");
        String time = sdf.format(cal.getTime());
        String fileName = "BobCarLog" + time + ".txt";

        if(OS.startsWith("WINDOWS"))
        {
            this.fileLocation = this.logDirectory + File.separator + fileName;
        }
        else if(OS.startsWith("LINUX"))
        {
            this.fileLocation =  this.logDirectory + File.separator+ fileName;
        }
        else if(OS.startsWith("MAC"))
        {
            this.fileLocation =  this.logDirectory + File.separator+ fileName;
        }

        this.logFile = new File(this.fileLocation);
    }

    /**
     * Observes the Telemetry(); and update to logs telemetry when it gets an observed instant of it.
     * @see Telemetry
     * @param obs Telemetry
     * @param o Object
     */
    @Override
    public void update(Observable obs, Object o)
    {
        if(o instanceof Telemetry)
        {
            logTelemetry((Telemetry)o);
        }
    }
}