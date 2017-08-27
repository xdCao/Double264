import sun.rmi.transport.tcp.TCPTransport;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xdcao on 2017/8/26.
 */
public class RecvingThread extends Thread {

    private Socket socket;

    private ConcurrentHashMap<Integer,DataPacket> hashMap=null;


    public RecvingThread(Socket socket, ConcurrentHashMap<Integer, DataPacket> hashMap) {
        this.socket = socket;
        this.hashMap = hashMap;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            dos.writeInt(1);
            dos.flush();
            while (true){
                DataPacket dataPacket=(DataPacket)ois.readObject();
                hashMap.put(dataPacket.getTag(),dataPacket);
                dos.writeInt(1);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
