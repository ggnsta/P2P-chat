
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;



public class MessageObject implements Serializable {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected String recieverName;
    protected Date date;// дата, время;
    protected List<InetAddress> ipList=null;

    //System.out.printf("%1$s %2$td %2$tB %2$tY", "Дата:", date);

    public void set(String message) {
        this.message = message;
        this.senderName=Utility.getHostIP();
        this.date=new Date();

    }

    public void setIpList(List<InetAddress> ipList) {
        this.ipList = ipList;
    }

    public MessageObject()
    {

    }


}

