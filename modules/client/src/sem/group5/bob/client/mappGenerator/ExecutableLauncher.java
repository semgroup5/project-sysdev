package sem.group5.bob.client.mappGenerator;

import java.io.IOException;

/**
 * Class that will launches a command line program on the desktop and passes it and argument
 */
public class ExecutableLauncher {


    /**
     * Method will launches a desktop app depends on the operating system.
     * @throws IOException
     */
    private void OSDiscoverer() throws IOException
    {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win"))
        {
            System.out.println("os is windows");
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://tinyurl.com/6fhuqvk"});
        } else if(OS.contains("mac"))
        {
            System.out.println("os is mac");
            String cmd = "open -a Safari http://tinyurl.com/6fhuqvk";
            Process p = Runtime.getRuntime().exec(cmd);
        }else if(OS.contains("unix"))
        {
            System.out.println("os is linux");
            Runtime.getRuntime().exec(new String[] {"google-chrome", "http://tinyurl.com/6fhuqvk/"});
        }

    }

    /**
     * Main function.
     * @param args todo
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        ExecutableLauncher ex = new ExecutableLauncher();
        ex.OSDiscoverer();

    }

}
