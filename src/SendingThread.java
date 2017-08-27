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
        while (true){
            synchronized (concurrentLinkedQueue){
                if (!concurrentLinkedQueue.isEmpty()){
                    DataPacket dataPacket=concurrentLinkedQueue.poll();
                    ObjectOutputStream oos=null;
                    try {
                        oos=new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(dataPacket);
                        oos.flush();
//                        System.out.println("发送对象： "+dataPacket.getTag());
                    } catch (IOException e) {
                        System.err.println("data trans err!");
                        try {
                            oos.close();
                            socket.close();
                        } catch (IOException e1) {
                            System.err.println("err in closing stream!");
                        }
                    }

                }
            }

        }
    }
}
