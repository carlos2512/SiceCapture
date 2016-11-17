/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author intec
 */
public class DirectoryHandler {

    private final String propertiesNameFile = "sice.config";
    private Properties propertiesFile;
    private InputStream input;
    private OutputStream output;

    public DirectoryHandler() {
        propertiesFile = new Properties();
        try {
            input = new FileInputStream(propertiesNameFile);
            propertiesFile.load(input);
        } catch (FileNotFoundException ex) {
            try {
                output = new FileOutputStream(propertiesNameFile);
                output.close();
                input = new FileInputStream(propertiesNameFile);
                propertiesFile.load(input);
                input.close();
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(DirectoryHandler.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                Logger.getLogger(DirectoryHandler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            Logger.getLogger(DirectoryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDirectory(String name) {
        String path = null;
        if (propertiesFile != null && propertiesFile.containsKey(name)) {
            path = propertiesFile.getProperty(name);
        }
        return path;
    }

    public static void delete(File file)
            throws IOException {
        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : "
                       + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }
        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public void addDirectory(String name, String url) {
        try {
            if (propertiesFile != null) {
                output = new FileOutputStream(propertiesNameFile);
                propertiesFile.setProperty(name, url);
                propertiesFile.store(output, null);
                output.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(DirectoryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
