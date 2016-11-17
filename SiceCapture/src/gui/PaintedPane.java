/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author intec
 */
public class PaintedPane extends JPanel {

    private BufferedImage img;
    private Rectangle drawRectangle;
    private boolean highlight = false;
    private boolean imageSelected = false;
    private File file;

    public void removeImage() {
        this.setVisible(false);
        imageSelected = false;
        img = null;
        file = null;
    }

    public PaintedPane() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Color color = new Color(57, 105, 138);
                setBorder(javax.swing.BorderFactory.createLineBorder(color, 3));
                imageSelected = true;
            }
        });
    }

    public boolean isImageSelected() {
        return imageSelected;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public BufferedImage getSelectedImage() {
        return img;
    }

    public void loadImage(File file) {
        try {
            this.file = file;
            img = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(PaintedPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isImageLoaded() {
        return img != null;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();
    }
}
