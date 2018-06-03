
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;



public class MessageObject implements Serializable {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected Date date;// дата, время;
    protected List<String> sharedList=null;

    //System.out.printf("%1$s %2$td %2$tB %2$tY", "Дата:", date);

    public void set(String message) {
        this.message = message;
        this.senderName=Utility.getLocalIP();
        this.date=new Date();


    }

    public MessageObject()
    {

    }


}

