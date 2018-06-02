import com.sun.prism.shader.Solid_TextureYV12_Loader;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.File;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultCaret;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.io.*;

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
    protected JButton bAddFile = new JButton("Ф");
    protected JProgressBar transmitProgress;
    protected BoundedRangeModel model = new DefaultBoundedRangeModel(0, 0, 0, 100);
    protected JPanel mainPanel;
    protected JLabel lblMain;
    protected JScrollPane contactsScroll;
    protected JList list;
    protected JPopupMenu editContact = new JPopupMenu();
    protected int k = 0;
    protected JButton confirmConnect = new JButton("Ok");
    protected boolean firstclick = false;
    DefaultListModel listModel = new DefaultListModel();


    protected ArrayList<DefaultListModel> contactList = new ArrayList<DefaultListModel>();


    public MyGUI() {

        MultiServer server = new MultiServer(49005, this);
        new Thread(server).start();// сразу создаем поток, ожидающий подключения

        Container my_panel = getContentPane();
        my_panel.setLayout(null);


        chatArea = new JTextArea();// поле чата
        chatArea.setEditable(false);//делаем нередактируемым
        chatArea.setLineWrap(true);// разрешаем перенос строк
        chatArea.setWrapStyleWord(true);// и перенос слов

        chatScroll = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//добавляем его к ScrollPane
        chatScroll.setBounds(215, 5, 445, 555);
        my_panel.add(chatScroll);


        jtfMessage = new JTextArea("Введите ваше сообщение: ");
        messageScroll = new JScrollPane(jtfMessage, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jtfMessage.setEditable(true);
        jtfMessage.setLineWrap(true);
        jtfMessage.setWrapStyleWord(true);
        messageScroll.setBounds(215, 560, 415, 100);
        my_panel.add(messageScroll);


        list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        list.setFont(new Font("Monospaced", Font.PLAIN, 15)); //задаем шрифт и размер шрифта
        list.setComponentPopupMenu(editContact);//!!!!!!!!!!!!!!!!!!!!!

        JMenuItem deleteItem = new JMenuItem("Удалить");
        editContact.add(deleteItem);


        bAddFile.setBounds(630, 560, 30, 100);
        my_panel.add(bAddFile);

        myIP = new JLabel("Ваш IP:" + Utility.getLocalIP());
        myIP.setBounds(10, 664, 120, 10);
        myIP.setFont(new Font("Arial", Font.PLAIN, 11)); //задаем шрифт и размер шрифта
        my_panel.add(myIP);


        //Удаление контакта
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                //словить тут эксшепшен
                listModel.remove(list.getSelectedIndex());
                contactList.remove(list.getSelectedIndex());
                //worker.close
            }
        });

        contactsScroll = (new JScrollPane(list));
        contactsScroll.setBounds(10, 30, 201, 630);
        my_panel.add(contactsScroll);


        //вывод окна для ввода IP
        bAdd = new JButton("+");
        bAdd.setBounds(10, 5, 200, 25);
        my_panel.add(bAdd);
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


            }
        });

        //создание исходящего подключения
        confirmConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                server.runClient();
                listModel.addElement("Элемент списка " + k);
                contactList.add(listModel);//добавляем поле в список
                k++;
                panel2.setVisible(false);
            }
        });


        //удаление началаьной записи в поле для ввода сообщений
        jtfMessage.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (firstclick == false) {
                    firstclick = true;
                    jtfMessage.setText("");

                } else return;

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //обработка нажатия на кнопку добавление файла
        bAddFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();//объект выбора файлов
                chooser.setMultiSelectionEnabled(true);

                List<Worker> contacts = server.getContacts();//получаем список подключений
                Worker worker = contacts.get(list.getSelectedIndex());//получаем  нужное подключение( выбранное слева из списка контактов)

                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] file = chooser.getSelectedFiles();
                    for (File directory : file) {// получаем все вложенные объекты в каталоге
                        FileTransmit filetransmit=new FileTransmit(worker.clientSocket,false);
                        filetransmit.sendFile(directory + "");//задаем имя отправляемого файла
                        filetransmit.start();
                    }


                    //  System.out.print(selectedFiles.size());
                }
            }

        });

        //обработчик клавиши enter(отпраква сообщений)
        jtfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {


                    if (k == 0) {// если это первое подключение
                        System.out.println("abc");
                        List<Worker> contacts = server.getContacts();//получаем список всех контактов
                        Worker worker = contacts.get(0);//берем последнее(и единственное), сделано для удобства, чтобы не надо было нажимать лишний раз
                    } else {
                        List<Worker> contacts = server.getContacts();
                        Worker worker = contacts.get(list.getSelectedIndex());
                        worker.send();
                    }


                    jtfMessage.setText("");

                }
            }
        });

        //обработчик выбора контакта

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                chatArea.setText("");//очищаем поле чата
                List<Worker> contacts = server.getContacts();//получаем список контактов
                Worker worker = contacts.get(list.getSelectedIndex());//выбираем нужный
                try {
                    File f =new File(worker.pathToHistory);
                    BufferedReader fileReader = new BufferedReader(new FileReader(f));
                   String line;
                   chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
                    while ((line = fileReader.readLine()) != null) {
                        System.out.println(line);
                        chatArea.append(line);
                        chatArea.append("\r");

                    }
                    fileReader.close();
                } catch (Exception ex) {

                    System.out.println(ex.getMessage());
                }


            }
        });

        setSize(680, 720);
        setVisible(true);
    }

}