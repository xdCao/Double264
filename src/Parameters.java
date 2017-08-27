/**
 * Created by xdcao on 2017/8/26.
 */
public interface Parameters {

    int memSize=1024*1024;//共享内存一次获取最大数据
    int timeOut=60000;//单位毫秒，共享内存超时
    String MEMORY_NAME="pano_player_share_mem1";//共享内存名称
    String MEMORY_NAME_RECV="pano_player_share_mem";//收端共享内存名称

    int PORT1=9995;
    int PORT2=9996;

    String IP1="127.0.0.1";
    String IP2="127.0.0.1";


}
