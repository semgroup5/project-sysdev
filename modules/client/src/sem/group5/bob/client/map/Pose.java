package sem.group5.bob.client.map;

/**
 * todo
 */
public class Pose {
    private double x;
    private double y;
    private double theta;

    /**
     * todo
     * @param x x
     * @param y y
     * @param theta theta
     */
    public Pose(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    /**
     * todo
     * @param s s
     */
    public Pose(String s)
    {
        s = s.substring(s.indexOf(':'));
        this.x = Double.parseDouble(s.substring(s.indexOf('X') + 1, s.indexOf('Y')));
        s = s.substring(s.indexOf('Y') + 1);
        this.y= Double.parseDouble(s.substring(0, s.indexOf('A')));
        s = s.substring(s.indexOf('g') + 1);
        this.theta = Double.parseDouble(s);
    }

    /**
     *
     * @return x
     */
    double getX(){
        return x;
    }

    /**
     *
     * @return y
     */
    double getY()
    {
        return y;
    }

    /**
     *
     * @return theta
     */
    double getTheta()
    {
        return theta;
    }
}
