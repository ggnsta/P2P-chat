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

public class MyGUI extends JFrame {

    //объекты gui'a
    protected JFrame panel2;
    protected JLabel myIP;
    protected JTextArea jtfMessage;
    protected JTextField jtfName;
    protected JTextPane chatArea;// поле чата
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
    protected JPanel mainPanel;
    protected JLabel lblMain;
    protected JScrollPane contactsScroll;
    protected JList list;
    protected JPopupMenu editContact=new JPopupMenu();
    protected int k = 0;
    protected JButton confirmConnect = new JButton("Ok");



    protected ArrayList<String> selectedFiles;// лист, в котором хранятся пути к файлам
    protected ArrayList<DefaultListModel> contactList = new ArrayList<DefaultListModel>();


    public MyGUI() {

        MultiServer server = new MultiServer(49005, this);
        new Thread(server).start();// сразу создаем поток, ожидающий подключения

        Container my_panel = getContentPane();
        my_panel.setLayout(null);


        chatArea = new JTextPane();// поле чата
        chatArea.setEditable(false);//делаем нередактируемым

        chatScroll=new JScrollPane(chatArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//добавляем его к ScrollPane
        chatScroll.setBounds(215,5,445,555);
        my_panel.add(chatScroll);


        jtfMessage = new JTextArea("Введите ваше сообщение: ");
        messageScroll = new JScrollPane(jtfMessage,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jtfMessage.setEditable(true);
        jtfMessage.setLineWrap(true);
        jtfMessage.setWrapStyleWord(true);
        messageScroll.setBounds(215,560,445,100);
        my_panel.add(messageScroll);


        DefaultListModel listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        list.setFont(new Font("Monospaced", Font.PLAIN, 15)); //задаем шрифт и размер шрифта
        list.setComponentPopupMenu(editContact);//!!!!!!!!!!!!!!!!!!!!!

        JMenuItem deleteItem = new JMenuItem("Удалить");
        editContact.add(deleteItem);


        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                    //словить тут эксшепшен
                listModel.remove(list.getSelectedIndex());
                contactList.remove(list.getSelectedIndex());
                //worker.close
            }
        });

        contactsScroll=(new JScrollPane(list));
        contactsScroll.setBounds(10,30,201,630);
        my_panel.add(contactsScroll);




        bAdd = new JButton("+");
        bAdd.setBounds(10, 5, 200, 25);
        my_panel.add(bAdd);
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                listModel.addElement("Элемент списка " + k);
                contactList.add(listModel);//добавляем поле в список
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






        jtfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {

                  //  List<Worker> contacts = server.getContacts();
                  //  Worker a = contacts.get(0);
                   // a.send();
                    jtfMessage.setText("");

                }
            }
        });

        setSize(680, 700);
        setVisible(true);
    }

}