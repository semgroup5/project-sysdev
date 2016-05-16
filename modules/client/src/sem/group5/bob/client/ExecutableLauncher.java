package sem.group5.bob.client;

import java.io.IOException;

/**
 * Created by Emanuel on 5/16/2016.
 */
public class ExecutableLauncher {


    public void OSDiscoverer() throws IOException{
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            System.out.println("os is windows");
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://tinyurl.com/6fhuqvk"});
        } else if(OS.contains("mac"))  {
            System.out.println("os is mac");
            Runtime.getRuntime().exec("/usr/bin/open -a \"/Applications/Google Chrome.app\",  'http://tinyurl.com/6fhuqvk'");
        }else if(OS.contains("unix")){
            System.out.println("os is linux");
            Runtime.getRuntime().exec(new String[] { "google-chrome", "http://tinyurl.com/6fhuqvk/" });
        }

    }
    public static void main(String[] args) throws IOException {
        ExecutableLauncher ex = new ExecutableLauncher();
        ex.OSDiscoverer();

    }

}
