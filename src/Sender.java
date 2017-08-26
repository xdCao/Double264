import com.pisoft.sharememory.ShareMemory;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
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
            socket1=new Socket("127.0.0.1",9995);
            socket2=new Socket("127.0.0.1",9996);
        } catch (IOException e) {
            System.err.println("socket连接失败，请检查网络");
        }

    }

    private void readByteFromMemory() {
        byte[] buffer=null;
        int tag=0;

        while (true){
            buffer=new byte[Parameters.memSize];
            int read = shareMemory.Read(buffer, Parameters.memSize);
            System.out.println("read bytes from sharedMemory: "+read+"tag: "+tag);
            DataPacket dataPacket=new DataPacket();
            dataPacket.setTag(tag++);
            dataPacket.setDataSize(read);
            dataPacket.setDataBytes(buffer);
            queue.add(dataPacket);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Sender sender=new Sender();
        sender.init();
        sender.readByteFromMemory();
    }


}
