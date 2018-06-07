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
    protected String notMyIp;//ip второй стороны
    protected String myIp = null;//мой ip
    protected SuperNode superNode;//экземпляр класса SuperNode (для пересылки сообщений адрессованных не нам и раздачи контактов)
    protected boolean isDirect = true;//флаг, является ли соединенеи прямым (не через суперузел)
    protected String superNodeIP = null;//если сообщение пересылаются через супер узел, то здесь будет его ip
    protected MyGUI gui;


    //конструктор прямых подключений
    public P2Pconnection(Socket clientSocket, MyGUI gui, Utility.TypeConection type, SuperNode sn) {
        try {
            this.clientSocket = clientSocket;
            this.gui = gui;
            this.type = type;
            this.myIp = clientSocket.getLocalAddress().toString();
            this.superNode = sn;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    //конструктор подключений через суперузел
    public P2Pconnection(String SuperNodeIP) {
        this.isDirect = false;
        this.superNodeIP = superNodeIP;
        try {
            this.myIp = InetAddress.getByName(Utility.getHostIP()).toString();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void run() {
        try {

            System.out.println(Thread.currentThread().getName());
            pathToHistory = System.getProperty("user.home");
            pathToHistory += File.separator + "p2p-chat" + Thread.currentThread().getName() + ".txt";
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

            tradeIp();
            getIP();

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
            mesObject.show();

            if ((mesObject.senderName.equals(this.notMyIp.toString()))) {//если имя отправителя сообщения != имени второй стороны, значит вторая сторона - суперузел и пересылает нам это сообщение//вот тут что-то
                //этот иф добавляет в спсиок контактов фейковый контакт
                System.out.print("check check check");
                ErrorNotification er=new ErrorNotification();
                er.eConnect();
                P2Pconnection buf = new P2Pconnection(clientSocket.getInetAddress().toString());//создаем объект в конструктор которого передаем Ip суперзула
                superNode.updateContacts(null, buf);

            }

            if (mesObject.ifShared == true) {
                //если флаг установлен, значит вторая сторона хочет получить наш спсиок контактов;

                superNode.shareContacts(this);
                // FileTransmit fileTransmit= new FileTransmit(this, true);// передаем текущий сокет и true, означающий что будем принимать файл
                // fileTransmit.start();

            }
            if (mesObject.ipList != null) {//если данный спсисок не пустой,значит нам передали список контактов

                System.out.print("get workera");
                //передаем полученный список ip, и ip того, кто нам этот список отправил
                superNode.transferContacts(mesObject.ipList, this.notMyIp);
                System.out.println("get workera 2");
                return;

            }
            System.out.println("reciver "+mesObject.recieverName);
            System.out.println("sender " + mesObject.senderName);
            System.out.println("host " + Utility.getHostIP());
            System.out.println("cur " + Utility.getCurrentIP());
            System.out.println("myip " + myIp.toString());
            if (!(mesObject.recieverName.equals(this.myIp.toString())))
            {
                if(!(mesObject.recieverName.equals(Utility.getCurrentIP()))){
                    if(!(mesObject.recieverName.equals(Utility.getHostIP().toString())))
                    {
                    //вот тут нул поинтер валится, скорее всего объект приходит пустым
                        System.out.println("проверка");
                        superNode.transmitOverNat(mesObject);
                        System.out.println("tut");
                    }
                }
            } else {
                System.out.println(mesObject.senderName + ":" + mesObject.message);
                gui.updateChatArea(mesObject, null);
                writeToHistory(mesObject);//вызов метода записи сообщений в файл
            }

        } catch (Exception x) {
            x.printStackTrace();
            //вот тут
        }
    }

    ////метод отправки сообщений
    public void send(MessageObject mesObject) {
        try {

            //mesObject.recieverName = clientSocket.getInetAddress().toString();
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


    public void tradeIp() {
        try {
            MessageObject mesObject = new MessageObject();
            mesObject.set("Подключение произошло");
            oos.writeObject(mesObject);//пишем в поток
            oos.flush();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void getIP() {
        try {
            MessageObject mesObject = (MessageObject) ois.readObject();
            this.notMyIp = InetAddress.getByName(mesObject.senderName).toString();
            System.out.println(notMyIp);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public P2Pconnection() {

    }

    public void show() {
        System.out.println("myip " + this.myIp);
        System.out.println("not main " + this.notMyIp);
        System.out.println("super " + this.superNodeIP);
    }
}