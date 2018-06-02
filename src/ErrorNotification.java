import javax.swing.*;
import java.awt.*;

public class ErrorNotification {

    protected JFrame errorFrame;

    public void eOS()
    {
        JOptionPane.showMessageDialog(errorFrame,"Неверный ip или закрыт порт","Ошибка открытия порта",JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    public void eOS2()
    {
        JOptionPane.showMessageDialog(errorFrame,"Роутинг или фаервол","Ошибка открытия порта",JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    public void eConnect()
    {
        JOptionPane.showMessageDialog(errorFrame,"Время ожидания подключения вышло","Ошибка подключения",JOptionPane.ERROR_MESSAGE);
    }

    public void eFileWrite(String path){
        JOptionPane.showMessageDialog(errorFrame,"Не удается найти файл"+path,"Файловая ошибка",JOptionPane.ERROR_MESSAGE);
    }
    public void nInConnect()
    {
        JOptionPane.showMessageDialog(null,"Есть входящее подключение","Уведомление",JOptionPane.PLAIN_MESSAGE );
    }
    public void nOutConnect()
    {
        JOptionPane.showMessageDialog(null,"Есть подключение","Уведомление",JOptionPane.PLAIN_MESSAGE );
    }
}