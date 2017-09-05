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

        FileInputStream fis=null;


        if (videoFile!=null){
            try {
                fis=new FileInputStream(videoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (fis!=null){

            OutputStream outputStream=null;

            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] buffer=new byte[1024];

            while (true){

                int read=0;

                try {
                    read = fis.read(buffer, 0, buffer.length);
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
