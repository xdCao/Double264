package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/27.
 */
public class SingleSender {

    private ShareMemory shareMemory=null;
    private Socket socket1=null;
    private ConcurrentLinkedQueue<DataPacket> queue=new ConcurrentLinkedQueue<DataPacket>();

    public void init() throws InterruptedException {


        shareMemory=new ShareMemory();
        int ret=shareMemory.Init(Parameters.MEMORY_NAME,true,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("共享内存出错");
        }

        try {
            socket1=new Socket(Parameters.IP1,Parameters.PORT1);
        } catch (IOException e) {
            System.err.println("socket连接失败，请检查网络");
        }

    }

    public void readByteFromMemory() {
//        new ReadMemoryThread(shareMemory,queue).start();
    }

    public void sendPacket2Air(){
        new SendingThread(queue,socket1).start();
    }

    public static void main(String[] args) throws InterruptedException {
        SingleSender singleSender=new SingleSender();
        singleSender.init();
        singleSender.readByteFromMemory();
        singleSender.sendPacket2Air();
    }

}
