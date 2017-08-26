import com.pisoft.sharememory.ShareMemory;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/26.
 */
public class ReadMemoryThread extends Thread {


    private ShareMemory shareMemory=null;
    private ConcurrentLinkedQueue<DataPacket> queue=null;

    public ReadMemoryThread(ShareMemory shareMemory, ConcurrentLinkedQueue<DataPacket> queue) {
        this.shareMemory = shareMemory;
        this.queue = queue;
    }

    @Override
    public void run() {
        byte[] buffer=null;
        int tag=0;

        while (true){
            buffer=new byte[Parameters.memSize];
            int read = shareMemory.Read(buffer, Parameters.memSize);
            System.out.println("read bytes from sharedMemory: "+read+"tag: "+tag);
            DataPacket dataPacket=new DataPacket();
            dataPacket.setTag(tag);
            tag++;
            dataPacket.setDataSize(read);
            dataPacket.setDataBytes(buffer);
            queue.add(dataPacket);
        }
    }
}
