/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import core.ScannerBackground;
import java.awt.Button;
import java.awt.GridLayout;
import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import javax.swing.JFrame;

public class TwainAppletExample extends Applet implements ActionListener, ScannerListener {
    int index = 0;
    String filename;
    ScannerBackground sc;
    Scanner scanner;
    Button acquireButton, selectButton, cancelButton;

    public TwainAppletExample() {
    }

    public TwainAppletExample(String title) {
        JFrame frame = new JFrame(title);
//    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                stop();
                System.exit(0);
            }
        });

        init();
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        start();
    }

    public void init() {
        setLayout(new GridLayout(1, 3));

        selectButton = new Button("select");
        add(selectButton);
        selectButton.addActionListener(this);

        acquireButton = new Button("acquire");
        add(acquireButton);
        acquireButton.addActionListener(this);

        cancelButton = new Button("cancel next scan");
        add(cancelButton);
        cancelButton.addActionListener(this);

        filename = System.getProperty("user.home") + "\\My Documents\\test";

        scanner = Scanner.getDevice();
        sc = new ScannerBackground(scanner);
        scanner.addListener(sc);

//    scanner.select("TWAIN_32 Sample Source");
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            if (evt.getSource() == acquireButton) {
                sc.run();
            } else if (evt.getSource() == selectButton) {
                scanner.select();
            } else if (evt.getSource() == cancelButton) {
                sc.close();
            }
        } catch (ScannerIOException se) {
            se.printStackTrace();
        }
    }

    public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
        
        if (type.equals(ScannerIOMetadata.ACQUIRED)) {
            BufferedImage image = metadata.getImage();
            System.out.println("Have an image now!");
            try {
                ImageIO.write(image, "jpg", new File("scn" + index + ".jpg"));
                index++;

//        new uk.co.mmscomputing.concurrent.Semaphore(0,true).tryAcquire(2000,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
            ScannerDevice device = metadata.getDevice();
            /*
             try{
             device.setResolution(100);
             //        device.setRegionOfInterest(0.0,0.0,40.0,50.0);       // top-left corner 40x50 mm
             device.setRegionOfInterest(0,0,400,500);               // top-left corner 400x500 pixels
             device.setShowUserInterface(false);
             device.setShowProgressBar(false);
             }catch(Exception e){
             e.printStackTrace();
             }
             */
        } else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
            System.err.println(metadata.getStateStr());
        } else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
            metadata.getException().printStackTrace();
        }
    }

}
