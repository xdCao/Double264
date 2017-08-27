import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/26.
 */
public class SendingThread extends Thread {

    private ConcurrentLinkedQueue<DataPacket> concurrentLinkedQueue=null;
    private Socket socket=null;

    public SendingThread(ConcurrentLinkedQueue<DataPacket> concurrentLinkedQueue, Socket socket) {
        this.concurrentLinkedQueue = concurrentLinkedQueue;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("进入线程: "+this.getId());
        DataInputStream dis=null;
        ObjectOutputStream oos=null;
        try {
            dis=new DataInputStream(socket.getInputStream());
            oos=new ObjectOutputStream(socket.getOutputStream());
            while (true) {

                if (!concurrentLinkedQueue.isEmpty()) {
                    int flag=dis.readInt();
                    if (flag==1){
                        flag=0;
                        DataPacket dataPacket = concurrentLinkedQueue.poll();
                        oos.writeObject(dataPacket);
                        oos.flush();
                        System.out.println("线程" + this.getId() + "发送对象： " + dataPacket.getTag());
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
