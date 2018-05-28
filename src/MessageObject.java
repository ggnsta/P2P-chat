
import java.io.Serializable;
import java.util.Date;


public class MessageObject implements Serializable {
    protected String message;//само сообщение;
    protected String senderName;//имя отправителя;
    protected Date date = new Date();// дата, время;
    protected boolean isFile;// является ли передаваемое сообщение файлом.
    protected String fileName;// имя файла, если передаём файл
    protected long fileSize;// длинна
    protected int countFiles;//количество передаваемых файлов
    // protected byte[] buffer = new byte[32 * 1024];// передача файла будет осуществляться через этот буфер в 32кб
    //System.out.printf("%1$s %2$td %2$tB %2$tY", "Дата:", date);
}
