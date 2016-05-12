package sem.group5.bob.client;


import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Created by chyj1 on 2016-5-12.
 */

public class LogToFile {
    public static void main(String[] args) {

        Logger logger = Logger.getLogger("BobCarLog");
        FileHandler fh;

        try {

            File createDir1 = new File(System.getenv("APPDATA") + "\\BobCar");
            createDir1.mkdir();
            fh = new FileHandler(System.getenv("APPDATA") + "\\BobCar" + "\\BobCarLog.txt");
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            LogFormatter formatter = new LogFormatter();
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            logger.addHandler(consoleHandler);
            fh.setFormatter(formatter);
            logger.info("BobCarLog");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("BobCar");
    }

}