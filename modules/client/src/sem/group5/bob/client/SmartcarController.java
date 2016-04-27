package sem.group5.bob.client;

import java.io.IOException;

class SmartcarController {
    private Smartcar smartcar;
    private boolean forwardPressed;
    private boolean backPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    /**
     *
     * @param connectionManager connectionManager
     * @throws IOException
     */
    SmartcarController(ConnectionManager connectionManager) throws IOException {
        smartcar = connectionManager.getSmartCar();
    }

    void pressForward(int speed) throws IOException{
        Thread thread = new Thread(() -> {
            if(!backPressed) {
                try {
                    forwardPressed = true;
                    smartcar.setSpeed(speed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void pressBack(int speed) throws IOException {
        Thread thread = new Thread(() -> {
            if(!forwardPressed) {
                try {
                    backPressed = true;
                    smartcar.setSpeed(-speed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void pressLeft() throws IOException {
        Thread thread = new Thread(() -> {
            if(!rightPressed) {
                try {
                    leftPressed = true;
                    if (forwardPressed || backPressed) {
                        smartcar.setAngle(-90);
                    }else {
                        smartcar.rotate(-1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void pressRight() throws IOException{
        Thread thread = new Thread(() -> {
            if(!leftPressed) {
                try {
                    rightPressed = true;
                    if (forwardPressed || backPressed) {
                        smartcar.setAngle(90);
                    } else {
                        smartcar.rotate(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void releaseForward() throws IOException{
        Thread thread = new Thread(() -> {
            try {
                forwardPressed = false;
                smartcar.setSpeed(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    void releaseBack() throws IOException{
        Thread thread = new Thread(() -> {
            try {
                backPressed = false;
                smartcar.setSpeed(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    void releaseLeft() throws IOException{
        Thread thread = new Thread(() -> {
            try {
                leftPressed = false;

                if (forwardPressed || backPressed) {
                    smartcar.setAngle(0);
                } else {
                    smartcar.rotate(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    void releaseRight() throws IOException{
        Thread thread = new Thread(() -> {
            try {
                rightPressed = false;

                if (forwardPressed || backPressed) {
                    smartcar.setAngle(0);
                } else {
                    smartcar.rotate(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
