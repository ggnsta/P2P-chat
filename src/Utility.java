import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

//вспомагательный класс, для получения внешнего IP
public class Utility {
    protected enum TypeConection {Server, Client}


    public static String getCurrentIP() {
        String ip = null;
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ip;
    }

    public static String getHostIP() {
        String ip = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }


}







