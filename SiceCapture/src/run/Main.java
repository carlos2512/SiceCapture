package run;

import examples.TwainAppletExample;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author Ing. Carlos Romero
 */
public class Main {


    /**
     * @param args
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            new TwainAppletExample("Twain Applet Example [2007-11-02]");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
