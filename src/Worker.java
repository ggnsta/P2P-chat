import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class Worker extends Thread implements Runnable {

    protected Socket clientSocket = null;
    protected FileInputStream fis;
    protected FileOutputStream fos;
    protected ArrayList<String> fileList = new ArrayList<String>();

    protected File history;
    protected File bufFile;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    protected Utility.TypeConection type;


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

            patch = File.separator + "p2p-chat" + File.separator + "in.txt";
            bufFile = new File(patch);
            bufFile.getParentFile().mkdir();
            bufFile.createNewFile();

            if (type == Utility.TypeConection.Server) {

                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(clientSocket.getInputStream());

            } else {
                System.out.println("client");
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
            gui.jtaTextAreaMessage.append(buf.message + "\n");

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
            gui.jtaTextAreaMessage.append(mesObject.message + "\n");//отображаем его в своём поле чата


        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public Worker() {

    }
}