package sem.group5.bob.car;

import sem.group5.bob.car.smartcar.BobCarConnectionManager;

/**
 * Main class for BobCar connections
 */
public class BobCar{

    /**
     * Constructor
     * @see BobCarConnectionManager
     * @param args argument
     */
    public static void main(String[] args)
    {
        BobCarConnectionManager manager = new BobCarConnectionManager();
        manager.initialize();
    }
}
