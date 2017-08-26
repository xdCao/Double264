import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xdcao on 2017/8/26.
 */
public class Write2File extends Thread {

    private ConcurrentHashMap<Integer,DataPacket> hashMap=null;

    private File file=null;

    private volatile int waitingNum=0;

    public Write2File(ConcurrentHashMap<Integer, DataPacket> hashMap, File file) {
        this.hashMap = hashMap;
        this.file = file;
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
                        fos.write(dataPacket.getDataBytes());
                        fos.flush();
                        System.out.println("第"+waitingNum+"个包接收完毕"+"size: "+dataPacket.getDataSize());
                    }

                    waitingNum++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
