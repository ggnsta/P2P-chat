/*

import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.toIntExact;

public class FileTransmit extends Worker {

    protected static int numOfFiles = 0;// количество переданных файлов, static, для того чтобы все экземпляры этого класса знали сколько передано файлов

    public FileTransmit(Worker worker) {
        this.fileList = worker.fileList;
        this.fis=worker.fis;
        this.fos=worker.fos;
    }

    ;

    public void sendFile() {


        try {
            MessageObject mesObj = new MessageObject();
            mesObj.countFiles = this.fileList.size();

            //  out.writeInt(countFiles);//отсылаем количество файлов

            for (; numOfFiles < mesObj.countFiles; numOfFiles++) {
                File file = new File(this.fileList.get(numOfFiles));
                mesObj.fileSize = file.length();
                mesObj.fileName = file.getName();
                mesObj.message=null;//первый передаваемый объект, будет без сообщения для удобства принятия
                oos.writeObject(mesObj);//отсылаем первый объект с размерои и именем файла
                FileInputStream fileIn = new FileInputStream(file);
                byte[] buffer = new byte[32 * 1024]; // размер буфера будет 32кб
                int count, total = 0;//count - количество прочитанных байтов (=размеры буфера)
                while ((count = fileIn.read(buffer)) != -1) {//read вернет -1, когда дойдет до конца файла

                    total += count;
                    mesObj.message = buffer.toString();
                    oos.writeObject(mesObj);

                    gui.model.setValue(100 * total / toIntExact(mesObj.fileSize));//отображаем прогресс передачи
                }

                gui.jtaTextAreaMessage.append(file.getName() + " передан");
                oos.flush();
                oos.close();
                fileIn.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   */
/* public void getFile() {
        try {
            MessageObject buf= (MessageObject) oin.readObject();//считываем первый объект с размерои и именем файла


            Properties properties = System.getProperties();
            String userHome = properties.getProperty("user.home");//получаем путь к домашенй папке пользователя

            for (int i = 0; i < buf.countFiles; i++) {
                long fileSize = buf.fileSize; // получаем размер файла
                String fileName = buf.fileName; //прием имени файла
                byte[] buffer = new byte[32 * 1024];



                FileOutputStream outFile = new FileOutputStream(userHome + "\\downloads\\" + fileName);
                int count, total = 0;

                //while ((count = oin.readObject(buffer, 0, Math.min(buffer.length, toIntExact(fileSize) - total))) != -1) {
                while ((count = oin.readObject(buffer, 0, Math.min(buffer.length, toIntExact(fileSize) - total))) != -1) {


                    total += count;
                    gui.model.setValue(100 * total / toIntExact(fileSize));//отображаем прогресс передачи
                    outFile.write(buffer, 0, count);

                    if (total == fileSize) {
                        break;
                    }
                }
                gui.jtaTextAreaMessage.append(fileName + " принят");

                outFile.flush();
                outFile.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*//*

}
*/
