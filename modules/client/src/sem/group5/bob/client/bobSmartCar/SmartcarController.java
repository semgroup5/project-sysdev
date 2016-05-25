package sem.group5.bob.client.bobSmartCar;

import sem.group5.bob.client.clientManager.ConnectionManager;

import java.io.IOException;

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
     * @param connectionManager connectionManager
     * @throws IOException
     */
    public SmartcarController(ConnectionManager connectionManager) throws IOException
    {
        smartcar = connectionManager.getSmartCar();
        driving = false;
        turning = false;
        tilt = 0;
    }

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

    public void releaseForward() throws IOException
    {
        try
        {
            forwardPressed = false;
            driving = false;
            smartcar.setSpeed(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void releaseBack() throws IOException
    {
        try
        {
            backPressed = false;
            driving = false;
            smartcar.setSpeed(0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

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

    public void tiltKinectReleased()
    {
        kinectTilting = false;
    }
}
