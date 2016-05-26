package sem.group5.bob.client;


public class Pose {
    public double x;
    public double y;
    public double theta;

    public Pose(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public Pose(String s)
    {
        s = s.substring(s.indexOf(':'));
        this.x = Double.parseDouble(s.substring(s.indexOf('X') + 1, s.indexOf('Y')));
        s = s.substring(s.indexOf('Y') + 1);
        this.y= Double.parseDouble(s.substring(0, s.indexOf('A')));
        s = s.substring(s.indexOf('g') + 1);
        this.theta = Double.parseDouble(s);
    }

    public double getX(){
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getTheta()
    {
        return theta;
    }
}
