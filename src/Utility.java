import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;

//вспомагательный класс, для получения внешнего IP
public class Utility {

    public static String getCurrentIP() {
        String ip=null;
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
}







