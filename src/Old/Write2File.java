package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xdcao on 2017/8/26.
 */
public class Write2File extends Thread {

    private ConcurrentHashMap<Integer,DataPacket> hashMap=null;

    private File file=null;

    private ShareMemory shareMemory;

    private volatile int waitingNum=0;


    public Write2File(ConcurrentHashMap<Integer, DataPacket> hashMap, File file, ShareMemory shareMemory) {
        this.hashMap = hashMap;
        this.file = file;
        this.shareMemory=shareMemory;
    }

    @Override
    public void run() {

        Queue<DataPacket> queue1=new LinkedList<>();
        Queue<DataPacket> queue2=new LinkedList<>();

        int count=0;

        try {

            while (true){

                while (hashMap.containsKey(waitingNum)){
                    DataPacket dataPacket=hashMap.remove(waitingNum);
                    if (dataPacket.getDataSize()>0){

                        ////////////////////////////////////

                        if (count%2==0){
                            queue1.add(dataPacket);
                        }else {
                            queue2.add(dataPacket);
                        }

                        if (waitingNum%10==9){

                            if (count%2==0){
                                while (!queue1.isEmpty()){
                                    DataPacket packet = queue1.poll();
                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                }
                            }else {
                                while (!queue2.isEmpty()){
                                    DataPacket packet = queue2.poll();
                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                }
                            }
                            count++;
                        }

                        //////////////////////////////////

//                        shareMemory.Write(dataPacket.getDataBytes(),dataPacket.getDataSize());
//                        System.out.println("第"+waitingNum+"个包写入完毕"+"size: "+dataPacket.getDataSize());
                    }
                    waitingNum++;
                }

                synchronized (Lock.class)
                {
                    try {
                        Lock.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
