

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by MF17037 on 2017/12/04.
 */

public class UDPHolePunchingServerSend extends Thread {
    private DatagramSocket datagramSocket;
    private UDPHolePunchingServerSendListener udpHolePunchingServerSendListener;
    private InetAddress IPAddress;
    private int port;

    UDPHolePunchingServerSend(DatagramSocket datagramSocket,UDPHolePunchingServerSendListener udpHolePunchingServerSendListener,InetAddress IPAddress,int port){
        this.datagramSocket = datagramSocket;
        this.udpHolePunchingServerSendListener = udpHolePunchingServerSendListener;
        this.IPAddress = IPAddress;
        this.port= port;
    }



    public void run() {
        String addrPort = IPAddress.getHostAddress().toString() + "-" + String.valueOf(port);
        //for(int i = 0; i < 10; i++) {
            try {
                datagramSocket.send(new DatagramPacket(addrPort.getBytes(),
                        addrPort.getBytes().length, IPAddress, port));
                System.out.println("hogehoge:送信成功");
            } catch (IOException e) {
            	System.out.println( "送信失敗");
            }
            //sleep();
            udpHolePunchingServerSendListener.onSendFinishMsg();

        //}
    }



    private void sleep(){
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
