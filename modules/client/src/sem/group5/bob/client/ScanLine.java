package sem.group5.bob.client;

/**
 * Created by jpp on 25/05/16.
 */
public class ScanLine {
    public int[] distances;

    public ScanLine(int[] distances) {
        this.distances = distances;
    }

    public int getDistanceCount()
    {
        return distances.length;
    }
}
