import java.io.Serializable;

/**
 * Created by xdcao on 2017/8/26.
 */
public class DataPacket implements Serializable{

    public int tag;
    public int dataSize;
    public byte[] dataBytes;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }
}
