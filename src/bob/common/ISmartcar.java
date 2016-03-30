package bob.common;

/**
 * Created by jpp on 30/03/16.
 */
public interface ISmartcar {
    void setSpeed(int speed) throws Exception;
    void setAngle(int angle);
    void rotate(int angle);
}
