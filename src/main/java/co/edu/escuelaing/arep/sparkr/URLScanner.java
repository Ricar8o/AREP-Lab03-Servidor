package co.edu.escuelaing.arep.sparkr;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Ricar8o
 */
public class URLScanner {

    public static void main(String[] args){
        scanURL("https://ldbn.is.escuelaing.edu.co:80/AREP/Mitarea.html");
        scanURL("https://daniel@ldbn.is.escuelaing.edu.co:80/AREP/Mitarea.html");
        scanURL("https://daniel@ldbn.is.escuelaing.edu.co:80/AREP/Mitarea.html?val=5");
        scanURL("https://daniel@ldbn.is.escuelaing.edu.co:80/AREP/Mitarea.html#Figures");
    }

    public static void scanURL(String site) {
        try {
            URL siteURL = new URL(site);
            System.out.println("URL: " + siteURL);
            System.out.println("Protocol: " + siteURL.getProtocol());
            System.out.println("Host: " + siteURL.getHost());
            System.out.println("Port: " + siteURL.getPort());
            System.out.println("Path: " + siteURL.getPath());
            System.out.println("File: " + siteURL.getFile());
            System.out.println("Query: " + siteURL.getQuery());
            System.out.println("Ref: " + siteURL.getRef());
            System.out.println("Autority: " + siteURL.getAuthority());
        } catch (MalformedURLException e) {
            Logger.getLogger(URLScanner.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("\n------------------------------");
    }
}