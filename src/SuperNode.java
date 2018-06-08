import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class SuperNode {
    protected MultiServer ms;


    public SuperNode(MultiServer ms) {
        this.ms = ms;
    }


    public void transmitOverNat(MessageObject mesobj) {

        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());
        for (int i = 0; i < contacts.size(); i++) {

            System.out.println("reciver "+mesobj.recieverName);
            System.out.println("contcat "+contacts.get(i).notMyIp.toString());


            if (contacts.get(i).notMyIp.toString().equals(mesobj.recieverName)) {
                P2Pconnection p2p = contacts.get(i);
                p2p.send(mesobj);
                return;
            }


        }

    }

    //функция раздачи своих контактов другой стороне
    public void shareContacts(P2Pconnection p2p) {
        System.out.println("Раздаю контакты");
        List<String> ipToshare = new ArrayList<String>();
        List<P2Pconnection> bufContacts = new ArrayList<P2Pconnection>();
        bufContacts.addAll(0, ms.getContacts());
       // bufContacts.remove(p2p);//достаем из списка наше соединение, чтобы не отпавлять второй стороне самого осебя
        for (int i = 0; i < bufContacts.size(); i++) {
            P2Pconnection buf = bufContacts.get(i);
            ipToshare.add(i, buf.notMyIp);
        }
        MessageObject mesObj = new MessageObject();
        mesObj.setIpList(ipToshare);
        mesObj.setIfShared(false);
        mesObj.set(ipToshare.toString());
        p2p.send(mesObj);
        System.out.println("Раздал контакты");
    }

    //функция запроса контактов, у другой стороны
    public void requestContacts(P2Pconnection p2p) {

        MessageObject messageObject = new MessageObject();
        messageObject.setIfShared(true);
        p2p.send(messageObject);
        System.out.println("запросил контакты");

    }

    //функция получения и применения запрошенного списка контактов в multiserver
    public void transferContacts(List<String> sharedIP, String ownerOfThisList) {
        List<P2Pconnection> buf = new ArrayList<P2Pconnection>();//заводим список
        buf.addAll(0, ms.getContacts());//копирем в него список контактов
        for (int i = 0; i < sharedIP.size(); i++)//прозодим по обоим листам в поисках уникальных адрессов(которых у нас не было)
        {
            int k = 0;
            for (int j = 0; j < sharedIP.size(); j++) {
                P2Pconnection p2p = buf.get(j);
                if (sharedIP.get(j).equals(p2p.notMyIp) == true) {
                    System.out.println("есть дубликаты");
                } else {
                    k++;
                    if (k == buf.size()) {
                        P2Pconnection bufp2p = new P2Pconnection(ownerOfThisList);//создаем через конструктор c 1 параметром(тогда isDirect=false)
                        bufp2p.notMyIp = sharedIP.get(j);//записываем уникальный ip, которого у нас раньше не было
                        buf.add(buf.size(), bufp2p);//добавляем в список контактов
                        System.out.println("новый контакт?");

                    }
                }
            }
        }
        if (buf.size() > 0) {

            updateContacts(buf, null);
        }
    }

    public void updateContacts(List contacts, P2Pconnection contact) // 2 параметра, 1 - если передаем список соединений, 2 - если одно соединение
    {
        if (contact == null) {
            List<P2Pconnection> bufList =ms.getContacts();
            contacts.removeAll(bufList);//удаляем повторы
            bufList.addAll(bufList.size(),contacts);
            ms.setContacts(bufList);
            ms.gui.updateContactList();
            System.out.println("обновил список");
        } else {
            List<P2Pconnection> buf = new ArrayList<P2Pconnection>();
            buf = ms.getContacts();
            buf.add(buf.size(), contact);
            ms.setContacts(buf);
            ms.gui.updateContactList();
            System.out.println("обновил список одним контактом");
        }
    }

}