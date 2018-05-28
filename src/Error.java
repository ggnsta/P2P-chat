import javax.swing.*;

public class Error {

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
}