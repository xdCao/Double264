package Old;

import java.io.*;
import java.net.Socket;

/**
 * Created by xdcao on 2017/9/5.
 */
public class ThirdLinkSendingThread extends Thread{

    private Socket socket=null;
    private File videoFile=null;

    public ThirdLinkSendingThread(Socket socket, File videoFile) {
        this.socket = socket;
        this.videoFile = videoFile;
    }

    @Override
    public void run() {

        RandomAccessFile raf=null;


        if (videoFile!=null){
            try {
                raf=new RandomAccessFile(videoFile,"rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (raf!=null){

            OutputStream outputStream=null;

            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] buffer=new byte[1024*50];
            int count=0;

            while (true){

                int read=0;

                try {

                    raf.seek(count*1024*50);
                    read = raf.read(buffer,0,buffer.length);
                    count++;

                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("文件读取失败！");
                }

                System.out.println("read bytes from file : -----------------"+read);

                if (read<=0){
                    break;
                }


                if (outputStream!=null){
                    try {
                        outputStream.write(buffer,0,read);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("网络传输失败");
                    }
                }
            }

            System.out.println("something is wrong or data has been finished!!!");

        }


    }
}
