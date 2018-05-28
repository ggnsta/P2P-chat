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

    protected MyGUI gui;


    public Worker(Socket clientSocket, MyGUI gui) {
        this.clientSocket = clientSocket;
        this.gui = gui;

    }

    @Override
    public void run() {
        try {


            String patch = System.getProperty("user.home");
            patch += File.separator + "p2p-chat" + File.separator + "temp.in";
            history = new File(patch);
            history.getParentFile().mkdirs();
            history.createNewFile();

            fis = new FileInputStream(history);
            fos = new FileOutputStream(history);
           // oin = new ObjectInputStream(fis);
            //oos = new ObjectOutputStream(fos);

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
        System.out.println("проверочка");


        try {
            ObjectInputStream oin = new ObjectInputStream(fis);
            MessageObject buf = (MessageObject) oin.readObject();

            System.out.println(buf.senderName + ":" + buf.message);
            gui.jtaTextAreaMessage.append(buf.message + "\n");
            oin.close();

        } catch (Exception x) {

        }
    }

    public void send() {
        try {

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            MessageObject mesObject = new MessageObject();
            mesObject.message = gui.jtfMessage.getText();// заносим сообщения из gui в объект
            System.out.println("send class worker");
            oos.writeObject(mesObject);
            oos.flush();
            oos.close();
            gui.jtaTextAreaMessage.append(mesObject.message + "\n");//отображаем его в своём поле чата


        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public Worker() {

    }
}