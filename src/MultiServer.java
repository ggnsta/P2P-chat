import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;

<<<<<<< HEAD
<<<<<<< HEAD
 public class  MultiServer implements Runnable {
    protected static int serverPort = 49005;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;

=======
public class MultiServer implements Runnable {
    protected static int serverPort=49005 ;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;
    private List<Worker> contacts = new ArrayList<Worker>();
>>>>>>> parent of 7bbe32d... test
=======
public class MultiServer implements Runnable {
    protected static int serverPort=49005 ;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;
    private List<Worker> contacts = new ArrayList<Worker>();
>>>>>>> parent of 7bbe32d... test
    protected Socket socket;
    protected Utility.TypeConection type;
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
<<<<<<< HEAD
<<<<<<< HEAD
            P2Pconnection p2pConnection = new P2Pconnection(clientSocket, this.gui, type);
            gui.contacts.add(p2pConnection);
            p2pConnection.start();
=======
            Worker worker = new Worker(clientSocket, this.gui, type);
            contacts.add(worker);
            worker.start();
>>>>>>> parent of 7bbe32d... test
=======
            Worker worker = new Worker(clientSocket, this.gui, type);
            contacts.add(worker);
            worker.start();
>>>>>>> parent of 7bbe32d... test

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


<<<<<<< HEAD
<<<<<<< HEAD
            this.socket = new Socket(InetAddress.getByName(gui.jtfIP.getText()), serverPort); // подключаемся к серверу
            System.out.println(gui.jtfIP.getText());
            P2Pconnection p2pConnection = new P2Pconnection(socket, this.gui, type);
            gui.contacts.add(p2pConnection);
            p2pConnection.start();
=======
=======
>>>>>>> parent of 7bbe32d... test
            this.socket = new Socket(InetAddress.getByName(gui.jtfIP.getText()),serverPort); // подключаемся к серверу
           System.out.println(gui.jtfIP.getText());
            Worker worker = new Worker(socket, this.gui, type);
            contacts.add(worker);
            worker.start();
<<<<<<< HEAD
>>>>>>> parent of 7bbe32d... test
=======
>>>>>>> parent of 7bbe32d... test

        } catch (Exception x) {
            ErrorNotification error = new ErrorNotification();
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
                ErrorNotification error = new ErrorNotification();
                error.eOS();
            } catch (IOException e)// (включает в себя SocketTimeoutException )
            {

                e.printStackTrace();
            }
    }
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> parent of 7bbe32d... test
    public List<Worker> getContacts() {
        return contacts;
    }

    public void setContacts(List<Worker> contacts) {
        this.contacts = contacts;
    }
>>>>>>> parent of 7bbe32d... test


    public MultiServer(int port, MyGUI gui) {

        this.serverPort = port;
        this.gui = gui;

    }

    public void reqContacts()
    {
        for(int i=0;i<gui.contacts.size();i++)
        {

            P2Pconnection p2pConnection = gui.contacts.get(i);
            p2pConnection.send("###Request%For%Contacts###");
            p2pConnection.acceptContacts();

        }

    }
}