package TestInternet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xdcao on 2017/9/3.
 */
public class TestReceiver {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(9995);
        Socket socket=serverSocket.accept();
        if (socket!=null){
            System.out.println("连接成功");
        }
    }


}
