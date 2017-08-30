import com.pisoft.sharememory.ShareMemory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xdcao on 2017/8/30.
 */
public class PureReceiver {

    private ServerSocket serverSocket=null;
    private Socket socket=null;
    private ShareMemory shareMemory=null;
    private InputStream inputStream=null;

    public boolean init(){

        shareMemory=new ShareMemory();
        int ret=shareMemory.Init(Parameters.MEMORY_NAME_RECV,false,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("接收共享内存出错");
        }

        try {
            serverSocket=new ServerSocket(Parameters.PORT1);
            socket=serverSocket.accept();
            inputStream=socket.getInputStream();
            if (socket!=null){
                System.out.println("连接成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void receive(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    byte[] buffer=new byte[Parameters.memSize];
                    try {
                        int read = inputStream.read(buffer, 0, inputStream.available());
                        if (read>0){
//                            System.out.println("receive from the sender : "+read+" bytes");
                        int write = shareMemory.Write(buffer, read);
                        System.out.println("receive from the sender : "+read+" bytes"+"----------------write into the memory : "+write+" bytes");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        }).start();
    }

    public static void main(String[] args) {
        PureReceiver pureReceiver=new PureReceiver();
        pureReceiver.init();
        pureReceiver.receive();
    }


}
