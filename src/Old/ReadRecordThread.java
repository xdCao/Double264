package Old;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadRecordThread extends Thread{

    private ConcurrentLinkedQueue<DataPacket> queue=null;
    private File record=null;

    public ReadRecordThread(File record, ConcurrentLinkedQueue<DataPacket> queue) {
        this.record=record;
        this.queue=queue;
    }

    @Override
    public void run() {

        RandomAccessFile raf=null;

        try {
            raf=new RandomAccessFile(record,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (raf!=null){

            byte[] buffer = new byte[1024*50];
            int count=0;
            int tag=0;

            while (true) {
                int read=0;
                try {
                    raf.seek(count*1024);
                    read = raf.read(buffer,0,1024);
                    if (read<=0){
                        break;
                    }else {
                        count++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (read > 0) {
                    DataPacket dataPacket = new DataPacket();
                    dataPacket.setTag(tag);
                    dataPacket.setDataSize(read);
                    dataPacket.setDataBytes(buffer);
                    queue.add(dataPacket);
                    tag++;
                    System.out.println("queue add new packet" + dataPacket.getTag());
                }


            }
        }



    }
}
