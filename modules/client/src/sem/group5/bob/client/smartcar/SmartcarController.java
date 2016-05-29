package sem.group5.bob.client.smartcar;

import java.io.IOException;

/**
 * Class to set the control commands to BobCar and the viewing angle of the kinect.
 */
public class SmartcarController
{
    private Smartcar smartcar;
    private boolean forwardPressed;
    private boolean backPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean driving;
    private boolean turning;
    private boolean kinectTilting;
    private double tilt;

    /**
     *
     * @param connectionManager BobCar connection manager
     * @throws IOException
     */
    SmartcarController(ConnectionManager connectionManager) throws IOException
    {
        smartcar = connectionManager.getSmartCar();
        driving = false;
        turning = false;
        tilt = 0;
    }

    /**
     * Method to move the car forward
     * @param speed the car speed
     * @throws IOException
     */
    public void pressForward(int speed) throws IOException
    {
        if(!backPressed && !driving)
        {
            try
            {
                forwardPressed = true;
                driving = true;
                smartcar.setSpeed(speed);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to move the car back
     * @param speed the car speed
     * @throws IOException
     */
    public void pressBack(int speed) throws IOException
    {
        if(!forwardPressed && !driving)
        {
            try
            {
                backPressed = true;
                driving = true;
                smartcar.setSpeed(-speed);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to move left
     * @throws IOException
     */
    public void pressLeft() throws IOException
    {
        if(!rightPressed)
        {
            try
            {
                leftPressed = true;
                if ((forwardPressed || backPressed) && !turning)
                {
                    turning = true;
                    smartcar.setAngle(-90);
                }else if (!turning)
                {
                    turning = true;
                    smartcar.rotate(-1);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to move the car right
     * @throws IOException
     */
    public void pressRight() throws IOException
    {
        if(!leftPressed)
        {
            try
            {
                rightPressed = true;
                if ((forwardPressed || backPressed) && !turning)
                {
                    turning = true;
                    smartcar.setAngle(90);
                } else if (!turning)
                {
                    turning = true;
                    smartcar.rotate(1);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to stop the car on button release.
     * @throws IOException
     */
    public void releaseForward() throws IOException
    {
        try
        {
            forwardPressed = false;
            driving = false;
            smartcar.setSpeed(0);
            smartcar.setAngle(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to stop the car on button release.
     * @throws IOException
     */
    public void releaseBack() throws IOException
    {
        try
        {
            backPressed = false;
            driving = false;
            smartcar.setSpeed(0);
            smartcar.setAngle(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to stop the car on button release.
     * @throws IOException
     */
    public void releaseLeft() throws IOException
    {
        try
        {
            leftPressed = false;
            turning = false;

            if (forwardPressed || backPressed)
            {
                smartcar.setAngle(0);
            } else {
                smartcar.rotate(0);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to stop the car on button release.
     * @throws IOException
     */
    public void releaseRight() throws IOException
    {
        try
        {
            rightPressed = false;
            turning = false;

            if (forwardPressed || backPressed)
            {
                smartcar.setAngle(0);
            } else
            {
                smartcar.rotate(0);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Control the motor to move the viewing area of the kinect up.
     * @throws IOException
     */
    public void pressTiltKinectUp() throws IOException
    {
        if (!kinectTilting)
        {
            if (!(tilt >= 30))
            {
                tilt += 15;
                smartcar.tiltkinect(tilt);
                kinectTilting = true;
            }
        }
    }

    /**
     * Control the motor to move the viewing area of the kinect down.
     * @throws IOException
     */
    public void pressTiltKinectDown() throws IOException
    {
        if (!kinectTilting)
        {
            if (!(tilt <= 0))
            {
                tilt -= 15;
                smartcar.tiltkinect(tilt);
                kinectTilting = true;
            }
        }
    }

    /**
     * Snaps the kinect to its original position.
     */
    public void tiltKinectReleased()
    {
        kinectTilting = false;
    }

    public void resetArduino() throws IOException {
        smartcar.resetArduino();
    }
}

