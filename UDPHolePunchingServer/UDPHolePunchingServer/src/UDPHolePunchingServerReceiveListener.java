

import java.net.DatagramPacket;

/**
 * Created by MF17037 on 2017/12/04.
 */

public interface UDPHolePunchingServerReceiveListener {
    void onReceiveMsg(DatagramPacket datagramPacket);
}
