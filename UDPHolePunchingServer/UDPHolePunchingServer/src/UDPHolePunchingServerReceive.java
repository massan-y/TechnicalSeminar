

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


/**
 * Created by MF17037 on 2017/12/04.
 */

public class UDPHolePunchingServerReceive extends Thread {
    DatagramSocket datagramSocket;
    UDPHolePunchingServerReceiveListener udpHolePunchingServerReceiveListener;
    UDPHolePunchingServerReceive(DatagramSocket datagramSocket,UDPHolePunchingServerReceiveListener udpHolePunchingServerReceiveListener){
        this.datagramSocket = datagramSocket;
        this.udpHolePunchingServerReceiveListener = udpHolePunchingServerReceiveListener;
    }


    public void run(){
    	
    	if(datagramSocket == null)
    		System.out.println( "socket is  null");
        while(true) {
            // receive Data
            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            try {
                datagramSocket.receive(receivePacket);
                String getMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if(getMsg.equals("Hello")){
                    udpHolePunchingServerReceiveListener.onReceiveMsg(receivePacket);
                }else{
                	System.out.println("UDPHolePunchingReceive;getMsg:"+getMsg);
                }
            } catch (IOException e) {
                System.out.println( "受信失敗"+e);
            }
        }
    }
}
