package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.*;
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

        byte[] tempNormal=new byte[Parameters.memSize];
        byte[] tempLast=new byte[Parameters.memSize];

        try {

            while (true){

                while (hashMap.containsKey(waitingNum)){
                    DataPacket dataPacket=hashMap.get(waitingNum);
                    if (dataPacket.getDataSize()>0){
//                        for (int i=0;i<Parameters.SPLIT;i++){
//                            if (i==9){
//                                System.arraycopy(dataPacket.getDataBytes(),i*(dataPacket.getDataSize()/Parameters.SPLIT),tempLast,0,dataPacket.getDataSize()-dataPacket.getDataSize()/Parameters.SPLIT*(Parameters.SPLIT-1));
//                                int write = shareMemory.Write(tempLast, dataPacket.getDataSize()-dataPacket.getDataSize()/Parameters.SPLIT*(Parameters.SPLIT-1));
//                                System.out.println("包号： "+dataPacket.getTag()+"帧号： "+i+"帧长： "+write);
//                            }else {
//                                System.arraycopy(dataPacket.getDataBytes(),i*(dataPacket.getDataSize()/Parameters.SPLIT),tempNormal,0,dataPacket.getDataSize()/Parameters.SPLIT);
//                                int write = shareMemory.Write(tempNormal, dataPacket.getDataSize()/Parameters.SPLIT);
//                                System.out.println("包号： "+dataPacket.getTag()+"帧号： "+i+"帧长： "+write);
//                            }
//                            Thread.sleep(Parameters.WRITING_FREQ); //------------延时设置-------------
//                        }
                        shareMemory.Write(dataPacket.getDataBytes(),dataPacket.getDataSize());
                        System.out.println("第"+waitingNum+"个包写入完毕"+"size: "+dataPacket.getDataSize());
                        hashMap.remove(waitingNum);
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
