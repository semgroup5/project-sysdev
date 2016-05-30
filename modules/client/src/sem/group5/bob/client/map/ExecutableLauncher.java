package sem.group5.bob.client.map;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.ws.server.UnsupportedMediaException;

import java.io.File;
import java.io.IOException;

/**
 * Class that will launches a command line program on the desktop and passes it and argument
 */
public class ExecutableLauncher {


    /**
     * Launches a desktop app depending on the operating system, used to launch Carmen map generator.
     * @throws IOException
     */
    private void OSDiscoverer() throws IOException
    {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win"))
        {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://tinyurl.com/6fhuqvk"});
        } else if(OS.contains("mac"))
        {
            String cmd = "open -a Safari http://tinyurl.com/6fhuqvk";
            Runtime.getRuntime().exec(cmd);
        }else if(OS.contains("unix"))
        {
            Runtime.getRuntime().exec(new String[] {"google-chrome", "http://tinyurl.com/6fhuqvk/"});
        }
    }

    private static void runCommand(String command, String[] args) throws IOException{
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win"))
        {
            throw new UnsupportedOperationException("Sorry we don't support Windows yet");
            //Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://tinyurl.com/6fhuqvk"});
        } else if(OS.contains("mac"))
        {
            throw new UnsupportedOperationException("Sorry, we don't support Mac yet");
        }else if(OS.contains("linux"))
        {
            Runtime.getRuntime().exec(command, args);
        }
    }

    public static void runGmappingGUI(File logFile)
    {
        try{
            runCommand("gfs/gfs_simplegui", new String[]{"-filename", logFile.getAbsolutePath()});
        }catch(Exception e)
        {
            //TODO: Handle
        }


    }

    /**
     * Main function to compile the class.
     * @param args todo
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        ExecutableLauncher ex = new ExecutableLauncher();
        ex.OSDiscoverer();

    }

}
