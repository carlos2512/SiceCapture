package core;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;

/**
 *
 * @author Carlos Romero
 */
public class ScannerBackground  implements ScannerListener,Runnable  {

    final static Logger logger = Logger.getLogger(ScannerBackground.class);
    private final Scanner scanner;
    private int index = 0;
    private boolean active;

    public ScannerBackground(Scanner scanner) {
        this.scanner = scanner;
    }
    
    private void saveImage(BufferedImage image) {
        try {
            index++;
            ImageIO.write(image, "jpg", new File("scn" + index + ".jpg"));
        } catch (Exception e) {
            logger.error("Error in storing process: " + e.getMessage());
        }
    }
    
    @Override
     public void run() {
        try {
            logger.info("Starting ScannerBackground Process");
            scanner.acquire();
        } catch (ScannerIOException se) {
            logger.error("Error image adquisition: " + se.getMessage());
        }
    }
     
    /*
     *Exit from any background process
     */
    public void close() throws ScannerIOException {
        scanner.setCancel(true);
        logger.info("Exit Background Process");
    }
    
    @Override
    public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
        ScannerDevice sd = metadata.getDevice();
        if (type.equals(ScannerIOMetadata.ACQUIRED)) {
            logger.info("ScannerHandler: Image acquired");
            saveImage(metadata.getImage());
        } else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
            try {
                logger.info("ScannerHandler: Setup settings");
                sd.setShowUserInterface(true);
                sd.setResolution(100);
            } catch (ScannerIOException ex) {
                sd.setCancel(true);
                logger.error("Error closing scanner default interface");
            }
        } else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
            if (metadata.isFinished()) {
                System.exit(0);
            }
            logger.info("ScannerHandler: state has been changed" + metadata.getStateStr());
        } else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
            metadata.getException().printStackTrace();
        }
    }
}
