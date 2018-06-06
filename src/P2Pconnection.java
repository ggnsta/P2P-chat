import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class P2Pconnection extends Thread {

    protected Socket clientSocket = null;
    protected File history;//файл в котором хранится история сообщений
    protected ObjectOutputStream oos = null;//выходной
    protected ObjectInputStream ois = null;//входной поток
    protected Utility.TypeConection type; // отвечает за правильный порядок создания oos и ois
    protected String pathToHistory = null;//путь до истории сообщений
    protected InetAddress notMyIp;//ip второй стороны
    protected InetAddress myIp = null;//мой ip
    protected SuperNode superNode;//экземпляр класса SuperNode (для пересылки сообщений адрессованных не нам и раздачи контактов)
    protected boolean isDirect = true;//флаг, является ли соединенеи прямым (не через суперузел)
    protected InetAddress superNodeIP = null;//если сообщение пересылаются через супер узел, то здесь будет его ip
    protected MyGUI gui;


    //конструктор прямых подключений
    public P2Pconnection(Socket clientSocket, MyGUI gui, Utility.TypeConection type, SuperNode sn) {
        try {
            this.clientSocket = clientSocket;
            this.gui = gui;
            this.type = type;
            this.myIp = clientSocket.getLocalAddress();
            this.superNode = sn;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    //конструктор подключений через суперузел
    public P2Pconnection(InetAddress superNodeIP) {
        this.isDirect = false;
        this.superNodeIP = superNodeIP;
    }

    public void run() {
        try {

            System.out.println(Thread.currentThread().getName());
            pathToHistory = System.getProperty("user.home");
            pathToHistory += File.separator + "p2p-chat" + File.separator + notMyIp.toString() + ".txt";
            System.out.println(File.separator);
            history = new File(pathToHistory);
            history.getParentFile().mkdirs();
            history.createNewFile();


            /*ObjectInputStream, читает из указанного InputStream. Заголовок
            потока сериализации считывается из потока и проверяется.
            Этот конструктор будет блокироваться до тех пор, пока соответствующий
            objectOutputStream не запишет и не сбросит заголовок.
            В итоге, если и у клиента и у сервера будет одинаковый порядок создания
            OOS и OIS, они оба будут ждать друг от друга заголовка потока и заблокируют
            друг друга.
            */
            if (type == Utility.TypeConection.Server) {

                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(clientSocket.getInputStream());

            } else {
                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
            }

            ErrorNotification notification = new ErrorNotification();
            if (type == Utility.TypeConection.Server) {
                notification.nInConnect();// выводим сообщение о успешном входящем подключении
            } else {
                notification.nOutConnect();// выводим сообщение о успешном исходящем подключении
            }

            while (true) {

                this.getMessage();// собственно эти потоки создаются только для того, чтобы постоянно ожидать сообщения

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "завершен");
    }

    public void getMessage() {

        try {

            MessageObject mesObject = (MessageObject) ois.readObject();
            if(this.notMyIp==null)
            {
                System.out.println("proverka");
                getSenderIp(mesObject);

            }

            if (mesObject.ifShared == true) {
                //если флаг установлен, значит вторая сторона хочет получить наш спсиок контактов
                superNode.shareContacts(this);
                // FileTransmit fileTransmit= new FileTransmit(this, true);// передаем текущий сокет и true, означающий что будем принимать файл
                // fileTransmit.start();

            }
            if (mesObject.ipList != null)//если данный спсисок не пустой,значит нам передали список контактов
            {
                System.out.print("get workera");
                this.clientSocket.getInetAddress();
                //передаем полученный список ip, и ip того, кто нам этот список отправил
                superNode.transferContacts(mesObject.ipList, this.clientSocket.getInetAddress());
                return;
            }
            if ((mesObject.senderName.equals(clientSocket.getInetAddress())))//если имя отправителя сообщения != имени второй стороны, значит вторая сторона - суперузел и пересылает нам это сообщение
            {  //этот иф добавляет в спсиок контактов фейковый контакт
                System.out.print("check check check");
                P2Pconnection buf = new P2Pconnection(clientSocket.getInetAddress());//создаем объект в конструктор которого передаем Ip суперзула
                superNode.updateContacts(null,buf);

            }
            if (mesObject.recieverName == Utility.getHostIP()) {
                superNode.transmitOverNat(mesObject);
            } else {
                System.out.println(mesObject.senderName + ":" + mesObject.message);
                gui.updateChatArea(mesObject, null);
                writeToHistory(mesObject);//вызов метода записи сообщений в файл
            }

        } catch (Exception x) {
            x.printStackTrace();
            ///вот тут
        }
    }

    ////метод отправки сообщений
    public void send(MessageObject mesObject) {
        try {

            mesObject.recieverName = clientSocket.getInetAddress().toString();
            oos.writeObject(mesObject);//пишем в поток
            oos.flush();
            gui.updateChatArea(mesObject, "Вы:");
            writeToHistory(mesObject);

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    //метод записи сообщений в файл
    public void writeToHistory(MessageObject mesObject) {
        try {
            FileWriter fileWriter = new FileWriter(pathToHistory, true);//осуществляет запись в файл истории сообщений c дозаписью
            fileWriter.write(mesObject.senderName + "(" + mesObject.date + ")\n");
            fileWriter.write(mesObject.message + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception x) {
            ErrorNotification error = new ErrorNotification();
            error.eFileWrite(this.pathToHistory);
        }
    }

    public InetAddress getSenderIp(MessageObject mesObj) {
        try {
            notMyIp = InetAddress.getByName(mesObj.senderName);
            System.out.println(notMyIp);

        } catch (Exception x) {
            x.printStackTrace();
        }
        return notMyIp;
    }

    public P2Pconnection() {

    }
}