import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class Worker extends Thread implements Runnable {

    protected Socket clientSocket = null;
    protected File history;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    protected Utility.TypeConection type; // отвечает за правильный порядок создания oos и ois
    protected FileWriter fileWriter;//осуществляет запись в файл истории сообщений
    protected String pathToHistory=null;

    protected MyGUI gui;


    public Worker(Socket clientSocket, MyGUI gui, Utility.TypeConection type) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        this.type = type;

    }

    @Override
    public void run() {
        try {

           pathToHistory = System.getProperty("user.home");
            pathToHistory += File.separator + "p2p-chat" + File.separator + gui.k+"history.txt";
            history = new File(pathToHistory);
            history.getParentFile().mkdirs();
            history.createNewFile();
            fileWriter=new FileWriter(pathToHistory,true);

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

                this.get();// собственно эти потоки создаются только для того, чтобы постоянно ожидать сообщения

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get() {

        try {

            MessageObject  mesObject = (MessageObject) ois.readObject();
            System.out.println( mesObject.senderName + ":" +  mesObject.message);
            ////ниже  работа с Gui
            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
            gui.chatArea.append( mesObject.senderName+"("+ mesObject.date+")");//отображаем информацию о сообщении в поле чата
            gui.chatArea.append("\n");
            gui.chatArea.append( mesObject.message);//отображаем сообщение
            gui.chatArea.append("\n");

            fileWriter.write( mesObject.senderName+"\r\n"+"("+ mesObject.date+")\r\n");
            fileWriter.write( mesObject.message+"\r\n");
            fileWriter.flush();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void send() {
        try {

            MessageObject mesObject = new MessageObject();
            mesObject.set(gui.jtfMessage.getText());//инициализируем датой, именем и самим сообщением

            System.out.println("send class worker");
            oos.writeObject(mesObject);
            oos.flush();
            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
            gui.chatArea.append("Вы "+"("+mesObject.date+"):");
            gui.chatArea.append("\n");
            gui.chatArea.append(mesObject.message);
            gui.chatArea.append("\n");

            fileWriter.write( mesObject.senderName+"\r\n"+"("+ mesObject.date+")\r\n");
            fileWriter.write( mesObject.message+"\r\n");
            fileWriter.flush();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public Worker() {

    }
}