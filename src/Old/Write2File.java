package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.*;
import java.lang.annotation.ElementType;
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
        Queue<DataPacket> queue3=new LinkedList<>();

        int count=0;

        try {

            while (true){

                while (hashMap.containsKey(waitingNum)){
                    DataPacket dataPacket=hashMap.remove(waitingNum);
                    if (dataPacket.getDataSize()>0){

                        ////////////////////////////////////双队列缓存

//                        if (count%2==0){
//                            queue1.add(dataPacket);
//                        }else {
//                            queue2.add(dataPacket);
//                        }
//
//                        if (waitingNum%10==9){
//
//                            if (count%2==0){
//                                while (!queue1.isEmpty()){
//                                    DataPacket packet = queue1.poll();
//                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
//                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
//                                }
//                            }else {
//                                while (!queue2.isEmpty()){
//                                    DataPacket packet = queue2.poll();
//                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
//                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
//                                }
//                            }
//                            count++;
//                        }

                        /////////////////////////////////////////////////////////////////////////////////////

                        //////////////////////////////////三队列缓存

                        if (count%3==0){
                            queue1.add(dataPacket);
                        }else if (count%3==1){
                            queue2.add(dataPacket);
                        }else {
                            queue3.add(dataPacket);
                        }

                        if (waitingNum%5==4){
                            if (count==0){

                            }else if (count==1){
                                while (!queue1.isEmpty()){
                                    DataPacket packet = queue1.poll();
                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                }
                                while (!queue2.isEmpty()){
                                    DataPacket packet = queue2.poll();
                                    shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                    System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                }
                            }else {
                                if (count%3==2){
                                    while (!queue3.isEmpty()){
                                        DataPacket packet = queue3.poll();
                                        shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                        System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                    }
                                }else if (count%3==1){
                                    while (!queue2.isEmpty()){
                                        DataPacket packet = queue2.poll();
                                        shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                        System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                    }
                                }else {
                                    while (!queue1.isEmpty()){
                                        DataPacket packet = queue1.poll();
                                        shareMemory.Write(packet.getDataBytes(),packet.getDataSize());
                                        System.out.println("第"+packet.getTag()+"个包写入完毕"+"size: "+packet.getDataSize());
                                    }
                                }
                            }
                            count++;
                        }


                        ////////////////////////////////////////////////////////////////////////////////////

                        /////////////////////////////////////无缓存

//                        shareMemory.Write(dataPacket.getDataBytes(),dataPacket.getDataSize());
//                        System.out.println("第"+waitingNum+"个包写入完毕"+"size: "+dataPacket.getDataSize());

                        ////////////////////////////////////////////////////////////////////////////////////

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
