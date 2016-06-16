package nars.main;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 28.05.2010
 * Time: 9:44:56
 * To change this template use File | Settings | File Templates.
 */
public class NARS_ALT implements Runnable {

    public static final String INFO =
            "     Open-NARS     Version 1.3.2     May 2010  \n";

    public static final String WEBSITE =
            " Open-NARS website:  http://code.google.com/p/open-nars/ \n" +
                    "      NARS website:  http://sites.google.com/site/narswang/ ";

    Thread narsThread = null;

    // Search for [REF_Aidar] to find modificated places
    public static void main(String args[]) {
        NARS_ALT nars = new NARS_ALT();
        nars.init();
        nars.start();
    }

    public void init() {
        Center.start();
    }

    public void start() {
        if (narsThread == null) {
            narsThread = new Thread(this);
            narsThread.start();
        }
    }

    public void stop() {
        narsThread = null;
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        while (narsThread == thisThread) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
            Center.tick();
        }
    }

    public static boolean isStandAlone() {
        return true;    // make the application and the applet identical, for now
    }

    public String getAppletInfo() {
        return INFO;
    }
}

