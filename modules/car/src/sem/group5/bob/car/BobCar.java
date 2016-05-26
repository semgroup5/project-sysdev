package sem.group5.bob.car;

import sem.group5.bob.car.smartCarManager.BobCarConnectionManager;
//TODO add specifically what is being updated in the update methods across all the classes.
/**
 * Main class for BobCar connections
 */
public class BobCar{

    /**
     * Main method.
     * @see BobCarConnectionManager
     * @param args argument
     */
    public static void main(String[] args)
    {
        BobCarConnectionManager manager = new BobCarConnectionManager();
        manager.initialize();
    }
}
