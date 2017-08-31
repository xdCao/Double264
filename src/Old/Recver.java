package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xdcao on 2017/8/26.
 */
public class Recver {

    private ServerSocket serverSocket1=null;
    private ServerSocket serverSocket2=null;
    private Socket socket1=null;
    private Socket socket2=null;
    private ShareMemory shareMemory=null;

    private File file=null;

    private ConcurrentHashMap<Integer,DataPacket> hashMap=new ConcurrentHashMap<>();

    public boolean init(){

        shareMemory=new ShareMemory();

        int ret=shareMemory.Init(Parameters.MEMORY_NAME_RECV,false,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("接收共享内存出错");
        }

        try {
            serverSocket1=new ServerSocket(Parameters.PORT1);
            serverSocket2=new ServerSocket(Parameters.PORT2);
            socket1=serverSocket1.accept();
            socket2=serverSocket2.accept();
            if (socket1!=null&&socket2!=null){
                System.out.println("连接成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

//        if (socket1!=null&&socket2!=null){
//            file=new File("video.264");
//        }

        return true;

    }

    public void recv2HashMap(){
        new RecvingThread(socket1,hashMap).start();
        new RecvingThread(socket2,hashMap).start();
    }


    public void write2File(){
        new Write2File(hashMap,file,shareMemory).start();
    }


    public static void main(String[] args) {
        Recver recver=new Recver();
        if (recver.init()){
            recver.recv2HashMap();
            recver.write2File();
        }
    }


}
