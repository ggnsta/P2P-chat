
import java.io.Serializable;
import java.util.Date;



public class MessageObject implements Serializable {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected Date date;// дата, время;
    protected boolean isFile=false;// является ли передаваемое сообщение файлом.
    protected String fileName;// имя файла, если передаём файл
    protected long fileSize;// длинна
    protected int countFiles;//количество передаваемых файлов

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

