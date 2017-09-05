package Old;

import com.pisoft.sharememory.ShareMemory;

/**
 * Created by xdcao on 2017/8/29.
 */
public class Utils {

    public static boolean Init_shareMemory(ShareMemory shareMemory){
        shareMemory=new ShareMemory();
        int ret=shareMemory.Init(Parameters.MEMORY_NAME,true,Parameters.timeOut,Parameters.memSize);
        if (ret<0){
            System.err.println("共享内存出错");
            return false;
        }
        return true;
    }


    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

}
