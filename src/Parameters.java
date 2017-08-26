/**
 * Created by xdcao on 2017/8/26.
 */
public interface Parameters {

    int memSize=1024*1024;//共享内存一次获取最大数据
    int timeOut=60000;//单位毫秒，共享内存超时
    String MEMORY_NAME="pano_player_share_mem1";//共享内存名称


}
