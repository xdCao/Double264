package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xdcao on 2017/8/26.
 */
public class Sender {

    private ShareMemory shareMemory=null;
    private Socket socket1=null;
    private Socket socket2=null;

    private Socket singleLinkSocket=null;

    File videoFile=null;

    private ConcurrentLinkedQueue<DataPacket> queue=new ConcurrentLinkedQueue<DataPacket>();

    public void initTCP() throws InterruptedException {


        if (Utils.Init_shareMemory(shareMemory)){
            try {
                socket1=new Socket(Parameters.IP1,Parameters.PORT1);
                socket2=new Socket(Parameters.IP2,Parameters.PORT2);

                if (Parameters.IS_THREE_LINK){
                    singleLinkSocket=new Socket(Parameters.SINGLE_LINK_IP,Parameters.SINGLE_LINK_PORT);
                    videoFile=new File("video.264");
                }

            } catch (IOException e) {
                System.err.println("socket连接失败，请检查网络");
            }
        }

    }


    public void readByteFromMemory() {
        new ReadMemoryThread(shareMemory,queue,videoFile).start();
    }

    public void sendPacket2AirTCP(){
        new SendingThread(queue,socket1).start();
        new SendingThread(queue,socket2).start();
    }

    public void send2TheThirdLink(){
        new ThirdLinkSendingThread(singleLinkSocket,videoFile).start();
    }



    public static void main(String[] args) throws InterruptedException {
        Sender sender=new Sender();
        sender.initTCP();
        sender.readByteFromMemory();
        if (Parameters.IS_THREE_LINK){
            sender.send2TheThirdLink();
        }
        sender.sendPacket2AirTCP();
    }


}
