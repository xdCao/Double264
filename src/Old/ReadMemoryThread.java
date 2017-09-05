package Old;

import com.pisoft.sharememory.ShareMemory;
import sun.security.util.Length;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/26.
 */
public class ReadMemoryThread extends Thread {


    private ShareMemory shareMemory=null;
    private ConcurrentLinkedQueue<DataPacket> queue=null;
    private File videoFile=null;

    public ReadMemoryThread(ShareMemory shareMemory, ConcurrentLinkedQueue<DataPacket> queue,File file) {
        this.shareMemory = shareMemory;
        this.queue = queue;
        this.videoFile=file;
    }

    @Override
    public void run() {

        FileOutputStream fos=null;

        if (Parameters.IS_THREE_LINK){
            try {
                fos=new FileOutputStream(videoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        byte[] buffer = new byte[Parameters.memSize];
        int tag = 0;


        while (true) {
            byte[] buffer1 = new byte[1024 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int count = 0;
            int length = 0;
            int read = 0;

            while (count < 1)//拼接
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

                if (fos!=null){
                    try {
                        fos.write(baos.toByteArray(),0,length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("文件写入失败！");
                    }
                }


            }


        }

    }

}
