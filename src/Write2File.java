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

    public Write2File(ConcurrentHashMap<Integer, DataPacket> hashMap, File file,ShareMemory shareMemory) {
        this.hashMap = hashMap;
        this.file = file;
        this.shareMemory=shareMemory;
    }

    @Override
    public void run() {
        FileOutputStream fos=null;
        try {
           fos=new FileOutputStream(file);
            while (true){
                if (hashMap.containsKey(waitingNum)){
                    DataPacket dataPacket=hashMap.get(waitingNum);
                    if (dataPacket.getDataSize()>0){
                        fos.write(dataPacket.getDataBytes(),0,dataPacket.getDataSize());
                        fos.flush();
//                        shareMemory.Write(dataPacket.getDataBytes(),dataPacket.getDataSize());
                        System.out.println("第"+waitingNum+"个包写入完毕"+"size: "+dataPacket.getDataSize());
                        hashMap.remove(waitingNum);
                    }

                    waitingNum++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
