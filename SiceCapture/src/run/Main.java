package run;

import gui.MainGui;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("SiceCapturePU");
            new MainGui(emf).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
