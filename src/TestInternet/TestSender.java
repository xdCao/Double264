package TestInternet;

import Old.DataPacket;
import Old.Parameters;
import Old.ThirdLinkSendingThread;
import Old.Utils;
import com.pisoft.sharememory.ShareMemory;
import sun.security.provider.SHA;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.net.Socket;

/**
 * Created by xdcao on 2017/9/5.
 */
public class TestSender {

    public static Socket socket=null;
    public static ShareMemory shareMemory=null;
    public static File videoFile=null;

    public void init(){
        Utils.Init_shareMemory(shareMemory);
        try {
            socket=new Socket(Parameters.SINGLE_LINK_IP,Parameters.SINGLE_LINK_PORT);
            videoFile=new File("video.264");
        }catch (IOException e){
            System.err.println("socket连接失败，请检查网络");
        }
    }


    public static void main(String[] args) {

      TestSender testSender=new TestSender();
      testSender.init();
      new Thread(new Runnable() {
          @Override
          public void run() {
              FileOutputStream fos=null;


              try {
                  fos=new FileOutputStream(videoFile);
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }


              byte[] buffer = new byte[Parameters.memSize];
              int tag = 0;


              while (true) {
                  byte[] buffer1 = new byte[1024 * 1024];
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                  int count = 0;
                  int length = 0;
                  int read = 0;

                  while (count < 1)//拼接
                  {
                      read = shareMemory.Read(buffer, Parameters.memSize);
                      System.arraycopy(buffer, 0, buffer1, length, read);
                      length = length + read;
                      count++;
                  }
                  baos.write(buffer1, 0, length);

                  if (length > 0) {
                      try {
                          fos.write(baos.toByteArray(),0,length);
                      } catch (IOException e) {
                          e.printStackTrace();
                          System.err.println("文件写入失败！");
                      }
                  }


              }
          }
      }).start();

      new ThirdLinkSendingThread(socket,videoFile).start();

    }


}
