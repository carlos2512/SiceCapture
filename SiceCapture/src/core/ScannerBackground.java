package core;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 *
 * @author Carlos Romero
 */
public class ScannerBackground implements Runnable {

    final static Logger logger = Logger.getLogger(ScannerBackground.class);
    private int index = 0;
    private boolean asds;
    private boolean active;
    private final Imaging imaging;
    private OutputStream output;
    private Properties propScannerSelected;
    private final Properties prop;

    public ScannerBackground() {
        prop = new Properties();
        imaging = new Imaging("SiceCapture", 0);
        try {
            prop.load(ClassLoader.getSystemResourceAsStream("ascan_trans_es.properties"));
            imaging.setI18n(prop);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ScannerBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void saveImage(BufferedImage image) {
        try {
            index++;
            ImageIO.write(image, "jpg", new File("scn" + index + ".jpg"));
        } catch (Exception e) {
            logger.error("Error in storing process: " + e.getMessage());
        }
    }

    public boolean isScannerSelected() {
        Properties property = new Properties();
        boolean response = true;
        InputStream input = null;
        String scanner = null;
        try {
            input = new FileInputStream("config.properties");
            // load a properties file
            property.load(input);
        } catch (IOException ex) {
            response = false;
            java.util.logging.Logger.getLogger(ScannerBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
        scanner = property.getProperty("scanner");
        if (input == null || scanner.equalsIgnoreCase("")) {
            response = false;
        }
        if (response){
            System.out.println(imaging.scanGetDefaultSourceName());
        }
        return response;
    }

    public void selectScanner() {
        try {
            output = new FileOutputStream("config.properties");
            propScannerSelected = new Properties();
            propScannerSelected.setProperty("scanner", imaging.scanSelectSource());
            propScannerSelected.store(output, null);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScannerBackground.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ScannerBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        String JSON_REQUEST = "{\n"
                + "  \"twain_cap_setting\" : {\n"
                + "    \"ICAP_PIXELTYPE\" : \"TWPT_RGB\", \n"
                + "    \"ICAP_XRESOLUTION\" : \"100\", \n"
                + "    \"ICAP_YRESOLUTION\" : \"100\",\n"
                + "    \"ICAP_SUPPORTEDSIZES\" : \"TWSS_USLETTER\"\n"
                + "  },\n"
                + "  \"output_settings\" : [ {\n"
                + "    \"type\" : \"save\",\n"
                + "    \"format\" : \"jpg\",\n"
                + "    \"save_path\" : \"${TMP}\\\\${TMS}${EXT}\"\n"
                + "  } ]\n"
                + "}";
        Result result = imaging.scan(JSON_REQUEST, "default", false, false);
        System.out.println(result == null ? "(null)" : result.toJson(true));
    }

}
