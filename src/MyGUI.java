import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.File;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;

public class MyGUI extends JFrame {

    //объекты gui'a
    protected JFrame panel2;
    protected JLabel myIP;
    protected JTextArea jtfMessage;
    protected JTextArea chatArea;// поле чата
    protected JTextField jtfIP = new JTextField("");
    protected JButton bAdd;
    protected JScrollPane chatScroll;
    protected JScrollPane messageScroll;
    protected JLabel labelIP = new JLabel("Введите IP");
    protected JButton bAddFile = new JButton("Ф");
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
        JMenuItem reqContacrs = new JMenuItem("Запросить контакты");
        editContact.add(reqContacrs);
        JMenuItem deleteHistory = new JMenuItem("Удалить историю");
        editContact.add(deleteHistory);


        bAddFile.setBounds(630, 560, 30, 100);
        my_panel.add(bAddFile);

        myIP = new JLabel("Ваш IP:" + Utility.getHostIP());
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
                list.remove(list.getSelectedIndex());
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
                listModel.addElement("Подлкючение № " + k);
                contactList.add(listModel);//добавляем поле в список
                k++;
                panel2.setVisible(false);
                if (server.socket == null) {
                    k--;
                    listModel.remove(k);
                    contactList.remove(k);

                }
                jtfIP.setText("");

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

                List<P2Pconnection> contacts = server.getContacts();//получаем список подключений
                P2Pconnection p2pConnection = contacts.get(list.getSelectedIndex());//получаем  нужное подключение( выбранное слева из списка контактов)

                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] file = chooser.getSelectedFiles();
                    for (File directory : file) {// получаем все вложенные объекты в каталоге
                        //    FileTransmit filetransmit=new FileTransmit(p2pConnection,false);
                        // filetransmit.setFileName(directory + "");
                        // filetransmit.sendFile();//задаем имя отправляемого файла
                        // filetransmit.start();
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
                        List<P2Pconnection> contacts = server.getContacts();//получаем список всех контактов
                        P2Pconnection p2pConnection = contacts.get(0);//берем последнее(и единственное), сделано для удобства, чтобы не надо было нажимать лишний раз
                        MessageObject mesObject = new MessageObject();
                        mesObject.set(jtfMessage.getText());//инициализируем датой, именем и самим сообщением
                        p2pConnection.send(mesObject);
                    } else {
                        List<P2Pconnection> contacts = server.getContacts();

                        P2Pconnection p2pConnection = contacts.get(list.getSelectedIndex());
                        MessageObject mesObject = new MessageObject();
                        mesObject.set(jtfMessage.getText());//инициализируем датой, именем и самим сообщением
                        mesObject.recieverName = p2pConnection.notMyIp.toString();//////////////////////////// вот тут чета сделать

                        if (p2pConnection.isDirect == false)//если подключение не прямое то будем вызывать метод send другого экземпляра
                        {
                            for (int i = 0; i < contacts.size(); i++) {
                                P2Pconnection buf = new P2Pconnection();
                                buf = contacts.get(i);
                                if (buf.notMyIp == p2pConnection.superNodeIP) {
                                    System.out.println("nat");
                                    buf.send(mesObject);
                                }
                            }
                        } else {
                            p2pConnection.send(mesObject);
                        }
                    }


                    jtfMessage.setText("");

                }
            }
        });
        //обработчик пунтка меню удалить историю сообщений
        deleteHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<P2Pconnection> contacts = server.getContacts();
                P2Pconnection p2pConnection = contacts.get(list.getSelectedIndex());
                try {
                    FileWriter fstream1 = new FileWriter(p2pConnection.pathToHistory);// конструктор с одним параметром - для перезаписи
                    BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
                    out1.write(""); // очищаем, перезаписав поверх пустую строку
                    out1.close(); // закрываем
                    chatArea.setText("");
                } catch (Exception ex) {
                    System.err.println("Error in file cleaning: " + ex.getMessage());
                }
            }
        });
        //обработчик пунтка меню запросить контакты
        reqContacrs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<P2Pconnection> contacts = server.getContacts();
                P2Pconnection p2pConnection = contacts.get(list.getSelectedIndex());
                server.superNode.requestContacts(p2pConnection);
            }
        });
        //обработчик выбора контакта
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                chatArea.setText("");//очищаем поле чата
                List<P2Pconnection> contacts = server.getContacts();//получаем список контактов
                P2Pconnection p2pConnection = contacts.get(list.getSelectedIndex());//выбираем нужный (эксепшен)
                try {
                    File f = new File(p2pConnection.pathToHistory);
                    //короче надо пройтись по папке считаь имена всех файлов и если есть совпадения отработать вот это
                    //if (f.getName().equals(p2pConnection.pathToHistory)) {
                    BufferedReader fileReader = new BufferedReader(new FileReader(f));
                    String line;
                    chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
                    while ((line = fileReader.readLine()) != null) {

                        chatArea.append(line);
                        chatArea.append("\r");

                    }
                    chatArea.append("\r\n");
                    fileReader.close();

                } catch (Exception ex) {

                    System.out.println(ex.getMessage());
                }


            }
        });


        setSize(680, 720);
        setVisible(true);


    }

    public void updateChatArea(MessageObject mesObject, String who) {

        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); //задаем шрифт и размер шрифта
        if (who == null) {
            chatArea.append(mesObject.senderName + "(" + mesObject.date + "):\r");//вывод даты и нашего имени на экран
        } else {
            chatArea.append("Вы " + "(" + mesObject.date + "):\r");//вывод даты и нашего имени на экран
        }
        chatArea.append(mesObject.message);//ыввод нашего сообщения на наш экран
        chatArea.append("\r\n");
        JScrollBar vertical = chatScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());//прокручиваем окно чата вниз при получении сообщения
    }

    public void updateContactList() {
        ///ниже работа с GUI
        listModel.addElement("Подлкючение №" + k);
        contactList.add(listModel);//добавляем поле в список
        k++;
    }

    public void informAboutclosing() {
        chatArea.append("Пользователь вышел");
        chatArea.append("\r\n");
    }
}