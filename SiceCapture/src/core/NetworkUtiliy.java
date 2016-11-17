/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author intec
 */
public class NetworkUtiliy {
    
    InetAddress ip;
    String macAddress;

    public NetworkUtiliy() {
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                macAddress = sb.toString();
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkUtiliy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(NetworkUtiliy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIpAddress() {
        return ip.getHostAddress();
    }

    public String getHardwareAddress() {

        return macAddress;

    }

}
