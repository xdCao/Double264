import java.io.File;
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
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                DataPacket dataPacket=(DataPacket)ois.readObject();
                System.out.println("--------------------网络接受包序号："+dataPacket.getTag());
                hashMap.put(dataPacket.getTag(),dataPacket);

                synchronized (Lock.class){
                    try {
                        Lock.class.notifyAll();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
