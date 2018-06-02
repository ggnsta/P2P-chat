
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

    public FileTransmit(Socket socket, boolean type) {
        try {
            this.type=type;
            this.clientSocket = new Socket(socket.getInetAddress(), MultiServer.serverPort);//получаем из уже имеющегося сокета, ip и создаем второй сокет по которому будут передаваться только файлы
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
            sendFile(fileName);
        }

    }

    public void sendFile(String filename) {


        try {

            File file = new File(filename);

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

            gui.chatArea.append(file.getName() + " передан");
            out.flush();
            out.close();
            fileIn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getFile() {
        try {
            MessageObject buf = (MessageObject) ois.readObject();//считываем первый объект с размерои и именем файла

            Properties properties = System.getProperties();
            String userHome = properties.getProperty("user.home");//получаем путь к домашенй папке пользователя

            long fileSize = buf.fileSize; // получаем размер файла
            String fileName = buf.fileName; //прием имени файла
            byte[] buffer = new byte[32 * 1024];

            int iter = toIntExact(fileSize) / 32 * 1024;

            InputStream input = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(input);


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


