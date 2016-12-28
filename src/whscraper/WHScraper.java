/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whscraper;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author kyle
 */
public class WHScraper {

    private enum OS{
        WIN, MAC, LINUX
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OS curOS = getOS();
        JFrame frame = new JFrame("picture");
        int number = getLastImageID() - 1;
        Random rand = new Random();
        Image i;
        while(true){
            try{
                i = getImage(rand.nextInt(number) + 1);
                break;
            } catch (InvalidIDException e){
                e.printStackTrace();
            }
        }
        JLabel image = new JLabel(new ImageIcon(i));
        image.setBounds(0, 0, 800, 600);
        frame.add(image);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public static final String[] FILETYPES = {".jpeg", ".jpg", ".png", ".bmp"};
    public static final String WHURL = "https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-";
    public static Image getImage(int imageID) throws InvalidIDException{
        Image img;
        for (String ft : FILETYPES){
            try{
                img = ImageIO.read(new URL(WHURL + imageID + ft));
                return img;
            } catch(Exception e){
                System.out.println("ID failed: " + imageID);
                e.printStackTrace();
            } 
        }

        throw new InvalidIDException();
    }
    static Pattern target = Pattern.compile("https://alpha.wallhaven.cc/wallpaper/(.+?)\"");
    public static int getLastImageID(){
        int largestNum = 0;
        try {
            URL u = new URL("https://alpha.wallhaven.cc/latest");
            URLConnection conn = u.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()))) {
                String input;
                while((input = in.readLine()) != null){
                    Matcher m = target.matcher(input);
                    while(m.find()){
                        System.out.println("KEK: " + m.group(1));
                        int tmp = Integer.parseInt(m.group(1));
                        if(tmp > largestNum) largestNum = tmp;
                    }
                }
            }
            
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(WHScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return largestNum;
    }
    
    private static OS getOS(){
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")) return OS.WIN;
        if(os.startsWith("Mac")) return OS.MAC;
        return OS.LINUX;
    }
    
    private static boolean isNSFW(int imageID){
        throw new UnsupportedOperationException("NSFW checking is not currently implemented. At the time of building this wallhaven was down so i could find the tags in the html");
    }
    
    private static void setWallpaper(Image img){
        throw new UnsupportedOperationException("This is a lot more difficult than anticipated and it is midnight");
    }
    
    private static class InvalidIDException extends Exception {}
    
}
