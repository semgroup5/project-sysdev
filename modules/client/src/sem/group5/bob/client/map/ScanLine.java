package sem.group5.bob.client.map;

/**
 *
 */
public class ScanLine {
    int[] distances;

    /**
     * todo
     * @param distances d
     */
    public ScanLine(int[] distances) {
        this.distances = distances;
    }

    /**
     * todo
     * @return d
     */
    int getDistanceCount()
    {
        return distances.length;
    }
}
