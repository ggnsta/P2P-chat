import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class SuperNode extends Thread {
    protected MultiServer ms;


    public SuperNode(MultiServer ms) {
        this.ms = ms;
    }


    @Override
    public void run() {

        while (true) {

        }

    }

   /* public  void listenContacts() {

        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).isReady) {
                contacts.get(i).getMessage();//чета надо делать с этим циклическим опросом
            } else return;
        }
            }

*/


    public void transmitOverNat(MessageObject mesobj) {
        if (mesobj.recieverName != Utility.getHostIP()) {
            List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
            contacts.addAll(0, ms.getContacts());
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).myIp.toString().equals(mesobj.recieverName)) {
                    P2Pconnection p2p = contacts.get(i);
                    p2p.send(mesobj);
                }


            }

        }
    }
    //функция  раздачи своих контактов другой стороне
    public void shareContacts(P2Pconnection p2p) {
        List<InetAddress> ipToshare = new ArrayList<InetAddress>();
        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());
        for (int i = 0; i < contacts.size(); i++) {
            P2Pconnection buf = contacts.get(i);
            ipToshare.add(i,buf.notMyIp);
        }
        MessageObject mesObj=new MessageObject();
        mesObj.setIpList(ipToshare);
        mesObj.setIfShared(false);
        mesObj.set("Ip list:");
        p2p.send(mesObj);
    }
    //функция запроса контактов, у другой стороны
    public void requestContacts(P2Pconnection p2p) {

        MessageObject messageObject=new MessageObject();
        messageObject.setIfShared(true);
        p2p.send(messageObject);

    }

    //функция получения и применения запрошенного списка контактов в multiserver
    public void transferContacts(List<InetAddress> sharedIP, InetAddress ownerOfThisList)
    {
        List<P2Pconnection> buf = new ArrayList<P2Pconnection>();//заводим список
        buf.addAll(0, ms.getContacts());//копирем в него список контактов
        for(int i=0;i<sharedIP.size();i++)//прозодим по обоим листам в поисках уникальных адрессов(которых у нас не было)
        {
            int k=0;
           for(int j=0; j<sharedIP.size();j++)
           {
               P2Pconnection p2p = buf.get(j);
               if(sharedIP.get(i).equals(p2p.notMyIp)==true)
               {
                    System.out.println("есть дубликаты");
                    break;
               }
               else {
                   k++;
                   if (k == buf.size())
                   {
                       P2Pconnection bufp2p=new P2Pconnection(ownerOfThisList);//создаем через конструктор c 1 параметром(тогда isDirect=false)
                       bufp2p.notMyIp=sharedIP.get(i);//записываем уникальный ip, которого у нас раньше не было
                       buf.add(buf.size(),bufp2p);//добавляем в список контактов
                       System.out.println("новый контакт?");

                   }
               }
           }
        }
        ms.setContacts(buf);
        ms.gui.updateContactList();
        System.out.println("обновил список");
    }

}