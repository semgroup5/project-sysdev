package sem.group5.bob.car.network;

import sem.group5.bob.car.smartcar.RemoteControlListener;

/**
 * The Timer class allows a graceful exit when an application
 * is stalled due to a networking timeout. Once the timer is
 * set, it must be cleared via the reset() method, or the
 * timeout() method is called.
 * <p>
 * The timeout length is customizable, by changing the 'length'
 * property, or through the constructor. The length represents
 * the length of the timer in milliseconds.
 *
 */
public class BobCarSocketTimer extends Thread
{
    private RemoteControlListener rmt;
    // Length of timeout
    private int m_length;
    private boolean countingDown;

    // Time elapsed
    private int m_elapsed;

    /**
     * Creates a timer of a specified length
     * @param	length	Length of time before timeout occurs
     * @see RemoteControlListener
     */
    public BobCarSocketTimer(int length, RemoteControlListener rmt)
    {
        this.rmt = rmt;
        // Assign to member variable
        m_length = length;

        // Set time elapsed
        m_elapsed = 0;
        countingDown = true;
    }


    /**
     * Resets the timer back to zero
     */
    public synchronized void reset()
    {
        m_elapsed = 0;
    }

    /**
     * Performs timer specific code
     */
    public void run()
    {
        // Keep looping
        while (countingDown)
        {
            /* Rate at which timer is checked */
            int m_rate = 100;
            try
            {
                //Put the timer to sleep
                Thread.sleep(m_rate);
            }
            catch (InterruptedException ignore) { }

            // Use 'synchronized' to prevent conflicts
            synchronized ( this )
            {
                // Increment time remaining
                m_elapsed += m_rate;

                // Check to see if the time has been exceeded
                if (m_elapsed > m_length)
                {
                    // Trigger a timeout
                    timeout();
                    break;
                }
            }

        }
    }

    // Override this to provide custom functionality
    private void timeout()
    {
        System.err.println ("Network timeout occurred.... restarting connection");
        rmt.closeConnections();
    }

    /**
     * Start a timer to be used to time out the connection.
     * @param b Set to true to start counting down.
     */
    public void setCountingDown(boolean b)
    {
        countingDown = b;
    }
}