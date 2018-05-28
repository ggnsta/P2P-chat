import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class Worker extends Thread implements Runnable {

    protected Socket clientSocket = null;
    //  protected DataInputStream in;
    // protected DataOutputStream out;
    protected ObjectOutputStream oos;
    protected ObjectInputStream oin;
    protected ArrayList<String> fileList = new ArrayList<String>();

    protected MyGUI gui;


    public Worker(Socket clientSocket, MyGUI gui) {
        this.clientSocket = clientSocket;
        this.gui = gui;

    }

    @Override
    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            oin = new ObjectInputStream(input);
            oos = new ObjectOutputStream(output);
            //  in = new DataInputStream(input);
            // out = new DataOutputStream(output);
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
            MessageObject buf= (MessageObject) oin.readObject();

            System.out.println(buf.senderName+":"+buf.message);
            gui.jtaTextAreaMessage.append(buf.message + "\n");

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void send() {
        try {

            MessageObject mesObject = new MessageObject();
            mesObject.message=gui.jtfMessage.getText();// заносим сообщения из gui в объект
            System.out.println("send class worker");
            oos.writeObject(mesObject);
            oos.flush();
            oos.close();
            gui.jtaTextAreaMessage.append(mesObject.message + "\n");//отображаем его в своём поле чата


        } catch (Exception x) {
            x.printStackTrace();
        }
    }


    public Worker()
    {

    }
}