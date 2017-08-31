package Old;

import com.pisoft.sharememory.ShareMemory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by xdcao on 2017/8/30.
 */
public class PureSender {

    private ShareMemory shareMemory=null;

    private Socket socket=null;

    private OutputStream os=null;

//    private Queue<byte[]> queue=new LinkedList<>();

    public void init(){


        shareMemory=new ShareMemory();
        int ret=shareMemory.Init(Parameters.MEMORY_NAME,true,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("共享内存出错");
        }

        try {
            socket=new Socket(Parameters.IP1,Parameters.PORT1);
            os=socket.getOutputStream();
        } catch (IOException e) {
            System.err.println("socket连接失败，请检查网络");
        }

    }


    public void transmit(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                byte[] buffer=new byte[Parameters.memSize];

                while (true){

                    int read = shareMemory.Read(buffer, Parameters.memSize);
                    System.out.println("read bytes from memory : -----------------"+read);
                    if (read<=0){
                        break;
                    }
                    try {
                        os.write(buffer,0,read);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("网络传输失败");
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("something is wrong or data has been finished!!!");

            }
        }).start();
    }


    public static void main(String[] args) {
        PureSender pureSender=new PureSender();
        pureSender.init();
        pureSender.transmit();
    }


}
