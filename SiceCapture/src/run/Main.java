package run;

import gui.MainGui;
import javax.swing.UIManager;
import org.apache.log4j.BasicConfigurator;

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
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            new MainGui().setVisible(true);
//            new TwainAppletExample("Twain Applet Example [2007-11-02]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
