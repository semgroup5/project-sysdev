package sem.group5.bob.client;

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
