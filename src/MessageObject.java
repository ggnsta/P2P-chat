
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;



public class MessageObject implements Serializable {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected String recieverName;//имя получателя
    protected Date date;// дата, время;
    protected List<InetAddress> ipList=null;//лист адрессов, которым будем делиться при запросе
    protected boolean ifShared=false;// флаг запроса на получения ipList


    //System.out.printf("%1$s %2$td %2$tB %2$tY", "Дата:", date);

    public void set(String message) {
        this.message = message;
        this.senderName=Utility.getHostIP();
        this.date=new Date();

    }

    public void setIfShared(boolean ifShared) {
        this.ifShared = ifShared;
    }

    public void setIpList(List<InetAddress> ipList) {
        this.ipList = ipList;
    }

    public MessageObject()
    {
        this.senderName=Utility.getHostIP();
    }

    public void show()
    {
        System.out.println(this.message);
        System.out.println(this.senderName);
        System.out.println(this.recieverName);
        System.out.println(this.date);
        System.out.println(this.ipList);
        System.out.println(this.ifShared);



    }


}

