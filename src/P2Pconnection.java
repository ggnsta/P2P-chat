import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class P2Pconnection extends Thread implements Runnable {

    protected Socket clientSocket = null;
    protected File history;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    protected Utility.TypeConection type; // отвечает за правильный порядок создания oos и ois
    protected String pathToHistory = null;
    protected InetAddress notMyIp = null;
    protected InetAddress myIp = null;


    protected MyGUI gui;


    public P2Pconnection(Socket clientSocket, MyGUI gui, Utility.TypeConection type) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        this.type = type;
        this.notMyIp = clientSocket.getInetAddress();
        this.myIp = clientSocket.getLocalAddress();


    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName());
            pathToHistory = System.getProperty("user.home");
            pathToHistory += File.separator + "p2p-chat" + File.separator + notMyIp.toString()+".txt";
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
            System.out.println(Thread.currentThread().getName()+"завершен");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get() {

        try {

            MessageObject mesObject = (MessageObject) ois.readObject();
            System.out.println(mesObject.senderName + ":" + mesObject.message);
            if (mesObject.message == "File###Transmit###Indeficator") {
                System.out.print("get workera");
                // FileTransmit fileTransmit= new FileTransmit(this, true);// передаем текущий сокет и true, означающий что будем принимать файл
                // fileTransmit.start();
                System.out.print("get workera close");
            }
            gui.updateChatArea(mesObject, null);

            writeToHistory(mesObject);//вызов метода записи сообщений в файл

        } catch (Exception x) {
            x.printStackTrace();

        }
    }

    ////метод отправки сообщений
    public void send(String mes) {
        try {

            MessageObject mesObject = new MessageObject();
            mesObject.set(mes);//инициализируем датой, именем и самим сообщением

            oos.writeObject(mesObject);//пишем в поток
            oos.flush();

            gui.updateChatArea(mesObject, "you");

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

    public P2Pconnection() {

    }
}