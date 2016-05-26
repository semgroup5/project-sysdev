package sem.group5.bob.client.map;

/**
 *
 */
public class Telemetry {
    Pose pose;
    private ScanLine scanLine;

    /**
     * todo
     * @param pose p
     * @param scanLine s
     */
    public Telemetry(Pose pose, ScanLine scanLine) {
        this.pose = pose;
        this.scanLine = scanLine;
    }

    /**
     * todo
     * @return pose
     */
    Pose getPose(){
        return pose;
    }

    /**
     * todo
     * @return s
     */
    ScanLine getScanLine(){
        return scanLine;
    }
}