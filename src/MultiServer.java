import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;

public class MultiServer implements Runnable {
    protected static int serverPort = 49005;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;
    private List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
    protected Socket socket;
    protected Utility.TypeConection type;
    protected int i = 0;

    //попробую сделать лист сокетов clientsocket  и ассептить их
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
            P2Pconnection p2pConnection = new P2Pconnection(clientSocket, this.gui, type);
            contacts.add(p2pConnection);
            p2pConnection.start();

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


            this.socket = new Socket(InetAddress.getByName(gui.jtfIP.getText()), serverPort); // подключаемся к серверу
            System.out.println(gui.jtfIP.getText());
            P2Pconnection p2pConnection = new P2Pconnection(socket, this.gui, type);
            contacts.add(p2pConnection);
            p2pConnection.start();

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
        if (i == 0) {
            try {

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

    public List<P2Pconnection> getContacts() {
        return contacts;
    }

    public void setContacts(List<P2Pconnection> contacts) {
        this.contacts = contacts;
    }


    public MultiServer(int port, MyGUI gui) {

        this.serverPort = port;
        this.gui = gui;

    }

}