import com.pisoft.sharememory.ShareMemory;
import com.sun.beans.editors.ByteEditor;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
        byte[] buffer=new byte[Parameters.memSize];
        int count=0;
        int tag=0;

        while (true){
            byte[] temp=new byte[Parameters.memSize];
            int read = shareMemory.Read(temp, Parameters.memSize);
            if (read<0){
                break;
            }
            Utils.byteMerger(buffer,temp);
            count++;
            if (count>=10){
                DataPacket dataPacket=new DataPacket();
                dataPacket.setTag(tag);
                tag++;
                dataPacket.setDataSize(read);
                dataPacket.setDataBytes(buffer);
                queue.add(dataPacket);
                count=0;
            }

//            System.out.println("read bytes from sharedMemory: "+read+"tag: "+tag);


        }

        System.out.println("something is wrong or data has been finished!!!");

    }



}
