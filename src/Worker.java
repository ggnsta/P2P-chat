import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Thread implements Runnable {

    protected Socket clientSocket = null;
    protected File history;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    protected Utility.TypeConection type; // отвечает за правильный порядок создания oos и ois
    protected String pathToHistory = null;


    protected MyGUI gui;


    public Worker(Socket clientSocket, MyGUI gui, Utility.TypeConection type) {
        this.clientSocket = clientSocket;
        this.gui = gui;
        this.type = type;

    }

    @Override
    public void run() {
        try {
          //  System.out.println(clientSocket.getInetAddress());//вернет не мой ip
           // System.out.println(clientSocket.getLocalAddress());//вернет мой ip
          //  System.out.println(clientSocket.getLocalPort());//вернет порт


            pathToHistory = System.getProperty("user.home");
            pathToHistory += File.separator + "p2p-chat" + File.separator + gui.k + "history.txt";
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
            while (true) {

                this.get();// собственно эти потоки создаются только для того, чтобы постоянно ожидать сообщения

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //метод приема сообщений
    public void get() {

        try {
            MessageObject mesObject = (MessageObject) ois.readObject();
            System.out.println(mesObject.senderName + ":" + mesObject.message);
<<<<<<< HEAD:src/P2Pconnection.java

=======
            if(mesObject.message=="File###Transmit###Indeficator")
            {
                System.out.print("get workera");
                FileTransmit fileTransmit= new FileTransmit(this, true);// передаем текущий сокет и true, означающий что будем принимать файл
                fileTransmit.start();
                System.out.print("get workera close");
            }
>>>>>>> parent of 7bbe32d... test:src/Worker.java
            ////ниже  работа с Gui
            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
            gui.chatArea.append(mesObject.senderName + "(" + mesObject.date + ")");//отображаем информацию о полученном сообщении в поле чата

            gui.chatArea.append(mesObject.message);//отображаем сообщение
            gui.chatArea.append("\n");

            writeToHistory(mesObject);//вызов метода записи сообщений в файл

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    ////метод отправки сообщений
    public void send(String mesasge) {
        try {

            MessageObject mesObject = new MessageObject();
            mesObject.set(mesasge);//инициализируем датой, именем и самим сообщением

            System.out.println("send class worker");
            oos.writeObject(mesObject);//пишем в поток
            oos.flush();

            gui.chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
            gui.chatArea.append("Вы " + "(" + mesObject.date + "):");//вывод даты и нашего имени на экран
            gui.chatArea.append(mesObject.message);//ыввод нашего сообщения на наш экран
            gui.chatArea.append("\n");

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

<<<<<<< HEAD:src/Worker.java
<<<<<<< HEAD:src/P2Pconnection.java

public void shareContacte()
{
    List<String> sharedContacts = new ArrayList<String>();//создаем лист контактов который отправим

    for(int i = 0 ; i<gui.contacts.size();i++)

    {
        System.out.println( gui.contacts.get(i).clientSocket.getInetAddress());
        sharedContacts.add(gui.contacts.get(i).clientSocket.getInetAddress().toString());
        System.out.println(sharedContacts.get(i));
        send(sharedContacts.toString());
    }

}
public void acceptContacts()
{
    try {
        MessageObject mesObject = (MessageObject) ois.readObject();
        System.out.println(mesObject.message);
    }catch (Exception x) {
        x.printStackTrace();
    }
}
    public P2Pconnection() {
=======
    public Worker() {
>>>>>>> parent of 7bbe32d... test:src/Worker.java
=======
    public Worker() {
>>>>>>> parent of 7bbe32d... test:src/Worker.java

    }


}