import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

public class MultiServer implements Runnable {
    protected static int serverPort = 49005;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected MyGUI gui;
    protected List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
    protected SuperNode superNode;
    protected Socket socket;
    protected Utility.TypeConection type;



    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName());
        openServerSocket();
        this.superNode = new SuperNode(this);

        while (!isStopped()) {

            Socket clientSocket = null;
            try {
                System.out.println("Waiting.");
                clientSocket = this.serverSocket.accept(); // ждем клиента

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            System.out.println("Как сервер.");
            type = Utility.TypeConection.Server;
            P2Pconnection p2pConnection = new P2Pconnection(clientSocket, this.gui, type, superNode);
            contacts.add(p2pConnection);
            p2pConnection.start();

            gui.updateContactList();
        }
        System.out.println("Server Stopped.");
    }

    public void runClient() {
        try {
            System.out.println("Как клиент.");
            type = Utility.TypeConection.Client;
            if (checkRepeatIp() == true) {
                this.socket = new Socket(InetAddress.getByName(gui.jtfIP.getText()), serverPort); // подключаемся к серверу

                P2Pconnection p2pConnection = new P2Pconnection(socket, this.gui, type, superNode);
                contacts.add(p2pConnection);
                p2pConnection.start();
            } else {
                socket = null;
                return;
            }

        } catch (Exception x) {
            x.printStackTrace();
            ErrorNotification error = new ErrorNotification();
               error.eConnect();
             this.socket=null;
        }

    }


    private boolean isStopped() {
        return this.isStopped;
    }

    public  void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {

        System.out.println("Opening server socket...");
        try {

            this.serverSocket = new ServerSocket(this.serverPort);

        } catch (ConnectException e) {
            ErrorNotification error = new ErrorNotification();
            error.eOS();
        } catch (IOException e)// (включает в себя SocketTimeoutException )
        {
            e.printStackTrace();
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

    //метод проверка на дублирование подключений
    public boolean checkRepeatIp() {
        if (Utility.getHostIP().equals(gui.jtfIP.getText()))//сравниваем введеный ip с собственным
        {
            System.out.println("подключение к самому себе");
            return false;
        }
        for (int i = 0; i < contacts.size(); i++) {
            P2Pconnection buf = contacts.get(i);
            if (buf.myIp.toString() != gui.jtfIP.getText())//сравниваем введенный ip с ip всех контактов
            {
                System.out.println("уже подключен к этому адресу");
                return false;
            }
        }
        return true;
    }


}