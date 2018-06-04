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
            listenContacts();

        }

    }

    public synchronized void listenContacts() {

        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).isReady) {
                contacts.get(i).getMessage();
            } else return;
        }


    }

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

    public void shareContacts(P2Pconnection p2p) {
        List<InetAddress> ipToshare = new ArrayList<InetAddress>();
        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());
        for (int i = 0; i < contacts.size(); i++) {
            P2Pconnection buf = contacts.get(i);
            ipToshare.set(i,buf.notMyIp);
        }
        MessageObject mesObj=new MessageObject();
        mesObj.setIpList(ipToshare);
        mesObj.setIfShared(true);
        mesObj.set("Ip list:");
    }

    public void requestContacts(P2Pconnection p2p) {

        MessageObject messageObject=new MessageObject();
        messageObject.setIfShared(true);
        p2p.send(messageObject);

    }
}