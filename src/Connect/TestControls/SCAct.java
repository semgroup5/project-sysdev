package Connect.TestControls;


import java.io.IOException;
import java.io.PrintWriter;

public class SCAct {


    public void setSpeed(int speed)throws IOException{
        String a = "The speed is set to "+speed;
        Print(a);
    }
    public void setAngle(int angle){
        String a = "The speed is set to "+angle;
        Print(a);
    }
    public void rotate(int angle){
        String a = "The speed is set to "+angle;
        Print(a);
    }
    public static void Print(String string){
        System.out.println(string);
    }


}

