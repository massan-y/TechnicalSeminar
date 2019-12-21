

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by MF17037 on 2017/12/04.
 */

public class UDPHolePunchingServer extends Thread implements UDPHolePunchingServerReceiveListener,UDPHolePunchingServerSendListener{
    final static int SERVER_PORT = 55554;
    private DatagramSocket serverSocket;
    private UDPHolePanchingFinishListener udpHolePanchingFinishListener;
    UDPHolePunchingServer(UDPHolePanchingFinishListener udpHolePanchingFinishListener){
        this.udpHolePanchingFinishListener = udpHolePanchingFinishListener;
        try {
            serverSocket = new DatagramSocket(SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        UDPHolePunchingServerReceive udpHolePunchingServerReceive = new UDPHolePunchingServerReceive(serverSocket,this);
        udpHolePunchingServerReceive.start();
    }


    @Override
    public void onReceiveMsg( DatagramPacket receivePacket){
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();

        System.out.println("IPandPort"+IPAddress+"----"+port);
        startUDPHoleServerSend(IPAddress,port);

    }

    private void startUDPHoleServerSend(InetAddress IPAddress,int port){
                UDPHolePunchingServerSend udpHolePunchingServerSend = new UDPHolePunchingServerSend(serverSocket, this, IPAddress, port);
                udpHolePunchingServerSend.start();
    }



    @Override
    public void onSendFinishMsg(){
        System.out.println("UDPHolePunchingServer:最後まで");
        udpHolePanchingFinishListener.onUDPHolePanchingFinish();
    }



}
