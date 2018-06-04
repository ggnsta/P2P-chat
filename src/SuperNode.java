import java.util.ArrayList;
import java.util.List;

public class SuperNode implements Runnable {
    protected MultiServer ms;


    public SuperNode(MultiServer multiServer) {
        this.ms = ms;
    }


    @Override
    public void run() {

        while (true) {
            listenContacts();
            System.out.println(Thread.currentThread().getName());
        }

    }

    public void listenContacts() {
        List<P2Pconnection> contacts = new ArrayList<P2Pconnection>();
        contacts.addAll(0, ms.getContacts());

        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).get();
        }
    }
}
