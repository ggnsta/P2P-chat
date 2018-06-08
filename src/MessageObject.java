
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;



public class MessageObject {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected String recieverName;//имя получателя
    protected Date date;// дата, время;
    protected List<String> ipList=null;//лист адрессов, которым будем делиться при запросе
    protected boolean ifShared=false;// флаг запроса на получения ipList



    public void set(String message) {
        this.message = message;
        this.senderName=Utility.getHostIP();
        this.date=new Date();

    }

    public void setIfShared(boolean ifShared) {
        this.ifShared = ifShared;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    public MessageObject()
    {
        this.senderName=Utility.getHostIP();
    }




}

