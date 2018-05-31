import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class Worker extends Thread implements Runnable {

    protected Socket clientSocket = null;
    protected ArrayList<String> fileList = new ArrayList<String>();
    protected File history;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    protected Utility.TypeConection type; // отвечает за правильный порядок создания oos и ois


    protected MyGUI gui;


    public Worker(Socket clientSocket, MyGUI gui, Utility.TypeConection type) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        this.type = type;

    }

    @Override
    public void run() {
        try {

            String patch = System.getProperty("user.home");
            patch += File.separator + "p2p-chat" + File.separator + "history.txt";

            history = new File(patch);
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

            String str = this.getName();
            System.out.println("Есть контакт : " + str);
            while (true) {

                this.get();// собственно эти потоки создаются только для того, чтобы постоянно получать сообщения

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get() {

        try {

            MessageObject buf = (MessageObject) ois.readObject();
            System.out.println(buf.senderName + ":" + buf.message);
            ////ниже гуи
            if(buf.message=="")return;
            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 11)); //задаем шрифт и размер шрифта
            Utility.appendToPane(gui.chatArea,buf.senderName+buf.date,Color.RED, Color.WHITE);
            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 15)); //задаем шрифт и размер шрифта
            Utility.appendToPane(gui.chatArea,buf.message,Color.RED, Color.black);



        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void send() {
        try {

            MessageObject mesObject = new MessageObject();
            mesObject.message = gui.jtfMessage.getText();// заносим сообщения из gui в объект
            System.out.println("send class worker");
            oos.writeObject(mesObject);
            oos.flush();
            Utility.appendToPane(gui.chatArea,mesObject.message,Color.RED, Color.black);



        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public Worker() {

    }
}