import com.sun.prism.shader.Solid_TextureYV12_Loader;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.File;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultCaret;

public class MyGUI extends JFrame {

    //объекты gui'a
    protected JFrame panel2;
    protected JLabel myIP;
    protected JTextArea jtfMessage;
    protected JTextField jtfName;
    protected JTextArea chatArea;// поле чата
    protected JTextField jtfIP = new JTextField("");
    protected JTextField jtfport;
    protected JButton bAdd;
    protected JButton jb3;
    protected JScrollPane chatScroll;
    protected JScrollPane messageScroll;
    protected JLabel readyLabel;
    protected JLabel labelIP = new JLabel("Введите IP");
    protected JButton bAddFile;
    protected JProgressBar transmitProgress;
    protected BoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 100);
    protected JPanel jp1;
    protected JLabel lblMain;
    protected JScrollPane contactsScroll;

    protected int k = 0;
    protected JButton confirmConnect = new JButton("Ok");



    protected ArrayList<String> selectedFiles;// лист, в котором хранятся пути к файлам
    protected ArrayList<JButton> buttonList = new ArrayList<JButton>();


    public MyGUI() {

        MultiServer server = new MultiServer(49005, this);
        new Thread(server).start();// сразу создаем поток, ожидающий подключения

        final JSplitPane splitPanel = new JSplitPane();// Главная разделяемая панель
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setDividerSize(4);   // Размер разделяемой панели
        splitPanel.setDividerLocation(260);  // Положение разделяемой панели


        chatArea = new JTextArea();// поле чата
        chatArea.setEditable(false);//делаем нередактируемым
        chatArea.setLineWrap(true);// разрешаем перенос строк
        chatArea.setWrapStyleWord(true);// и перенос слов
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 15)); //задаем шрифт и размер шрифта


        JPanel rightPanel=new JPanel();
        rightPanel.setBackground(Color.cyan);//задаем цвет фона
        rightPanel.setLayout(null);//менеджер компоновки
        chatArea.setBounds(0,0,616,550);
        rightPanel.add(chatArea);
        chatScroll=new JScrollPane(rightPanel);//добавляем его к ScrollPane

        jtfMessage = new JTextArea("Введите ваше сообщение: ");
        messageScroll = new JScrollPane(jtfMessage);
        jtfMessage.setEditable(true);
        jtfMessage.setLineWrap(true);
        jtfMessage.setWrapStyleWord(true);
        messageScroll.setBounds(0, 550, 616, 150);
        rightPanel.add(messageScroll);

        JPanel leftPanel = new JPanel();//создаем левую панель
        leftPanel.setBackground(Color.cyan);//задаем цвет фона
        leftPanel.setLayout(null);//менеджер компоновки
        contactsScroll = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contactsScroll.setPreferredSize(new Dimension(246, 400));//добавляем левую панель к scroll pane

        bAdd = new JButton("+");
        bAdd.setBounds(20, 10, 206, 25);
        leftPanel.add(bAdd);
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                panel2 = new JFrame("Добавление соединения");//новое окно
                panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
                panel2.setSize(580, 150);
                jtfIP.setColumns(16);
                panel2.add(labelIP);
                panel2.add(jtfIP);//поля для ip
                panel2.add(confirmConnect);
                panel2.setVisible(true);

                JButton contactButton = new JButton();//каждому контакту будет соотвтствовать кнопка
                contactButton.setBounds(10, 35 + (k * 40), 225, 40);//которая будет расположена ниже предыдущей
                contactsScroll.setPreferredSize(new Dimension(255, 400 + (k * 40)));//задаем желаемые размеры области для появления scroll bar
                buttonList.add(contactButton);//добавляем кнопку в список
                leftPanel.add(contactButton);//добавляем кнопку на панель
                k++;


            }
        });

        confirmConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                server.runClient();
                System.out.println("SUB");
            }
        });

        splitPanel.setLeftComponent(new JScrollPane(contactsScroll)); // Настройка левой панели
        splitPanel.setRightComponent(new JScrollPane(chatScroll)); // Настройка правой панели


        getContentPane().add(splitPanel);// Размещение панели в интерфейсе и вывод окна на экран
        setSize(680, 700);
        setVisible(true);

        jtfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {

                    List<Worker> contacts = server.getContacts();
                    Worker a = contacts.get(0);
                    a.send();
                    jtfMessage.setText("");

                }
            }
        });


    }
}