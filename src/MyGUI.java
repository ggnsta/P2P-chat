import com.sun.prism.shader.Solid_TextureYV12_Loader;
import javafx.event.ActionEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.File;

public class MyGUI extends JFrame {

    //объекты gui'a
    protected JTextArea jtfMessage;
    protected JTextField jtfName;
    protected JTextArea jtaTextAreaMessage;// поле чата
    protected JTextField jtfIP;
    protected JTextField jtfport;
    protected JButton bAdd2;
    protected JButton jb3;
    protected JScrollPane chatScroll;
    protected JScrollPane messageScroll;
    protected JLabel readyLabel;
    protected JButton bAddFile;
    protected JProgressBar transmitProgress;
    protected BoundedRangeModel model=new DefaultBoundedRangeModel(0, 0, 0, 100);


    protected ArrayList<String> selectedFiles;// лист, в котором хранятся пути к файлам


    public MyGUI() {

        MultiServer server = new MultiServer(49005, this);
        new Thread(server).start();


        Container my_panel = getContentPane();
        setBounds(20, 20, 500, 600);
        my_panel.setLayout(null);


        transmitProgress = new JProgressBar( model );
        transmitProgress.setBounds( 350, 170, 100, 15 );
        transmitProgress.setStringPainted( true );
        my_panel.add(transmitProgress);

        jtaTextAreaMessage = new JTextArea();
        chatScroll = new JScrollPane(jtaTextAreaMessage);
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        jtaTextAreaMessage.setWrapStyleWord(true);
        chatScroll.setBounds(20, 20, 250, 400);
        my_panel.add(chatScroll);

        jtfMessage = new JTextArea("Введите ваше сообщение: ");
        messageScroll = new JScrollPane(jtfMessage);
        jtfMessage.setEditable(true);
        jtfMessage.setLineWrap(true);
        jtfMessage.setWrapStyleWord(true);
        messageScroll.setBounds(20, 420, 250, 50);
        my_panel.add(messageScroll);

        jtfIP = new JTextField("IP: ");
        jtfIP.setBounds(350, 60, 100, 50);
        my_panel.add(jtfIP);

        jtfport = new JTextField("");
        jtfport.setBounds(350, 120, 100, 50);
        my_panel.add(jtfport);

        readyLabel = new JLabel("");
        readyLabel.setBounds(350, 400, 80, 20);
        my_panel.add(readyLabel);

        bAddFile = new JButton("файл");
        bAddFile.setBounds(350, 30, 100, 25);
        my_panel.add(bAddFile);
        bAddFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                selectedFiles = new ArrayList<String>();
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File[] file = chooser.getSelectedFiles();
                    for (File directory : file){// получаем все вложенные объекты в каталоге
                        selectedFiles.add(directory+"");
                    }
                    List<Worker> contacts = server.getContacts();
                    Worker worker = contacts.get(0);
                    model.setValue(0);
                    //  System.out.print(selectedFiles.size());
                }
            }

        });
        bAdd2 = new JButton("Добавить ");
        bAdd2.setBounds(350, 280, 100, 25);
        my_panel.add(bAdd2);
        bAdd2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                server.runClient();
            }

        });
        jtfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {

                    List<Worker> contacts = server.getContacts();
                    Worker a = contacts.get(0);
                    // String str=a.getName();
                    //System.out.print(str);
                    a.send();
                    jtfMessage.setText("");

                }
            }
        });


    }


}