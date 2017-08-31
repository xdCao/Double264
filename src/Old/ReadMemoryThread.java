package Old;

import com.pisoft.sharememory.ShareMemory;

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
        byte[] buffer = null;
        int tag = 0;


        while (true) {
            buffer = new byte[Parameters.memSize];
            byte[] buffer1 = new byte[1024 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 0;
            int length = 0;
            int read = 0;

            while (count < Parameters.SPLIT)//拼接
            {
                read = shareMemory.Read(buffer, Parameters.memSize);

                System.arraycopy(buffer, 0, buffer1, length, read);
                length = length + read;
                count++;
            }
            baos.write(buffer1, 0, length);

            if (length > 0) {
                DataPacket dataPacket = new DataPacket();
                dataPacket.setTag(tag);
                dataPacket.setDataSize(length);
                dataPacket.setDataBytes(baos.toByteArray());
                queue.add(dataPacket);
                tag++;
                System.out.println("queue add new packet" + dataPacket.getTag());
            }


        }

    }

}
