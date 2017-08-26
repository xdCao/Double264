import com.pisoft.sharememory.ShareMemory;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/26.
 */
public class Sender {

    private ShareMemory shareMemory=null;
    private Socket socket1=null;
    private Socket socket2=null;
    private ConcurrentLinkedQueue<DataPacket> queue=new ConcurrentLinkedQueue<DataPacket>();

    public void init() throws InterruptedException {


        shareMemory=new ShareMemory();
        int ret=shareMemory.Init(Parameters.MEMORY_NAME,true,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("共享内存出错");
        }

        try {
            socket1=new Socket(Parameters.IP1,Parameters.PORT1);
            socket2=new Socket(Parameters.IP2,Parameters.PORT2);
        } catch (IOException e) {
            System.err.println("socket连接失败，请检查网络");
        }

    }

    public void readByteFromMemory() {
        new ReadMemoryThread(shareMemory,queue).start();
    }

    public void sendPacket2Air(){
        new SendingThread(queue,socket1).start();
        new SendingThread(queue,socket2).start();
    }

    public static void main(String[] args) throws InterruptedException {
        Sender sender=new Sender();
        sender.init();
        sender.readByteFromMemory();
        sender.sendPacket2Air();
    }


}
