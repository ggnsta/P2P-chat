/*

import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class FileTransmit extends Worker {

protected boolean type;
protected String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileTransmit(Worker worker, boolean type) {
        try {
            this.type=type;
            this.clientSocket = new Socket(worker.clientSocket.getInetAddress(), MultiServer.serverPort);//получаем из уже имеющегося сокета, ip и создаем второй сокет по которому будут передаваться только файлы
            this.oos=worker.oos;
            System.out.println("сокет для передачи файлов");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        if(type==true)
        {
            getFile();
        }
        else{
            sendFile();
        }

    }

    public void sendFile() {


        try {

            File file = new File(fileName);
            MessageObject mesObject=new MessageObject();
            mesObject.set("File###Transmit###Indeficator");//даём понять принимающей стороне что дальше будет файл
            oos.writeObject(mesObject);//пишем в поток

            OutputStream output = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(output);

            out.writeLong(file.length());//отсылаем размер файла
            out.writeUTF(file.getName());//отсылаем имя файла

            FileInputStream fileIn = new FileInputStream(file);
            byte[] buffer = new byte[32 * 1024]; // размер буфера будет 32кб
            int count, total = 0;//count - количество прочитанных байтов (=размеры буфера)

            while ((count = fileIn.read(buffer)) != -1) {//read вернет -1, когда дойдет до конца файла

                total += count;
                out.write(buffer, 0, count);

                //gui.model.setValue(100 * total / toIntExact(file.length()));//отображаем прогресс передачи
            }
            System.out.println("му тут");
          //  gui.chatArea.append(file.getName() + " передан"); короче он не знает в какой чат писать, надо в контактах тыкать на строку

            out.flush();
            out.close();
            fileIn.close();
            System.out.println("файд передан");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getFile() {
        try {


            Properties properties = System.getProperties();
            String userHome = properties.getProperty("user.home");//получаем путь к домашенй папке пользователя

            InputStream input = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(input);

            long fileSize = in.readLong(); // получаем размер файла
            String fileName = in.readUTF(); //прием имени файла
            byte[] buffer = new byte[32 * 1024];

            int iter = toIntExact(fileSize) / 32 * 1024;




            FileOutputStream outFile = new FileOutputStream(userHome + "\\downloads\\" + fileName);
            int count, total = 0;

            while ((count = in.read(buffer)) != -1) {
                total += count;
                gui.model.setValue(100 * total / toIntExact(fileSize));//отображаем прогресс передачи
                outFile.write(buffer, 0, count);

                if (total == fileSize) {
                    break;
                }
            }

            gui.chatArea.append(fileName + " принят");

            outFile.flush();
            outFile.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


*/
