package sem.group5.bob.client.mappGenerator;

import sem.group5.bob.client.Pose;
import sem.group5.bob.client.Telemetry;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;


public class FileLogger implements Observer{
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
    public FileLogger() throws IOException{
        crtFile();
       writer = new PrintWriter(fileLocation, "UTF-8");
    }

    public void logTelemetry(Telemetry telemetry){
        String result;

        // (old) # FLASER num_readings [range_readings] x y theta odom_x odom_y odom_theta

//        # ROBOTLASER1 laser_type start_angle field_of_view angular_resolution
//        #   maximum_range accuracy remission_mode
//        #   num_readings [range_readings] laser_pose_x laser_pose_y laser_pose_theta
//        #   robot_pose_x robot_pose_y robot_pose_theta
//        #   laser_tv laser_rv forward_safety_dist side_safty_dist


        int laser_type              = 99;
        double start_angle          = telemetry.getPose().getTheta() - 27.5;
        double field_of_view        = 55;
        double angular_resolution   = 0.0859375;
        int maximum_range           = 4096;
        int accuracy                = 50;
        int remission_mode          = 1;
        int num_readings            = telemetry.getScanLine().getDistanceCount();
        int[] range_readings        = telemetry.getScanLine().distances;
        double laser_x              = telemetry.pose.getX();
        double laser_y              = telemetry.pose.getY();
        double laser_theta          = telemetry.pose.getTheta();
        double odom_x               = telemetry.pose.getX();
        double odom_y               = telemetry.pose.getY();
        double odom_theta           = telemetry.pose.getTheta();

        result = "ROBOTLASER1 " + laser_type + " " + start_angle + " " + field_of_view + " " + angular_resolution + " ";
        result += maximum_range + " " + accuracy + " " + remission_mode + " " + num_readings +" ";

        for(int range_reading : range_readings) {
            result += range_reading + " ";
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
        if(o instanceof Telemetry)
        {
            logTelemetry((Telemetry)o);
        }
    }
}