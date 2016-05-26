package sem.group5.bob.client;


public class Telemetry {
    public Pose pose;
    public ScanLine scanLine;

    public Telemetry(Pose pose, ScanLine scanLine) {
        this.pose = pose;
        this.scanLine = scanLine;
    }

    public Pose getPose(){
        return pose;
    }

    public ScanLine getScanLine(){
        return scanLine;
    }
}