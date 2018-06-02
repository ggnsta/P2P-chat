import sun.net.www.protocol.http.AuthCacheValue;

import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;

public class MultiServer implements Runnable {
    protected int serverPort=49005 ;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;
    private List<Worker> contacts = new ArrayList<Worker>();
    protected Socket socket;
    protected Utility.TypeConection type;
    protected List<ServerSocket> ssList=new ArrayList<>();
    protected  int i = 0;

    @Override
    public void run() {

        while (!isStopped()) {
            openServerSocket();
            Socket clientSocket = null;
            try {

                clientSocket = this.serverSocket.accept(); // ждем клиента
                System.out.println("Waiting.");

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            System.out.println("Как сервер.");
            type = Utility.TypeConection.Server;
            Worker worker = new Worker(clientSocket, this.gui, type);
            contacts.add(worker);
            worker.start();

            ///ниже работа с GUI
            gui.listModel.addElement("Элемент списка " + gui.k);
            gui.contactList.add(gui.listModel);//добавляем поле в список
            gui.k++;
        }
        System.out.println("Server Stopped.");
    }

    public void runClient() {
        try {
            System.out.println("Как клиент.");
            type = Utility.TypeConection.Client;


            this.socket = new Socket(InetAddress.getByName(gui.jtfIP.getText()),serverPort); // конектимся к серверу
           System.out.println(gui.jtfIP.getText());
            Worker worker = new Worker(socket, this.gui, type);
            contacts.add(worker);
            worker.start();

        } catch (Exception x) {
            Error error = new Error();
            error.eConnect();
            x.printStackTrace();

        }

    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {


        System.out.println("Opening server socket...");
        if (i == 0)
        { try {

                this.serverSocket = new ServerSocket(this.serverPort);
                i++;
            } catch (ConnectException e) {
                Error error = new Error();
                error.eOS();
            } catch (IOException e)// (включает в себя SocketTimeoutException )
            {

                e.printStackTrace();
            }
    }
    }

    public List<Worker> getContacts() {
        return contacts;
    }

    public void setContacts(List<Worker> contacts) {
        this.contacts = contacts;
    }


    public MultiServer(int port, MyGUI gui) {

        this.serverPort = port;
        this.gui = gui;

    }

}