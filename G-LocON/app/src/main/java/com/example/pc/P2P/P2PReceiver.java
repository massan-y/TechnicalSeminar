package com.example.pc.P2P;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by pc on 2018/06/09.
 */

public class P2PReceiver extends AsyncTask<String, String, Void> {
    private DatagramSocket socket;
    private IP2PReceiver iP2PReceiver;

    P2PReceiver(DatagramSocket socket,IP2PReceiver iP2PReceiver){
        this.socket = socket;
        this.iP2PReceiver = iP2PReceiver;
    }

    @Override
    protected Void doInBackground(String... text) {
        Log.d("Receive", "Recieveクラスまで来た");
        final String GET_PERIPHERAL_USER = "getPeripheralUserInfoList";
        final String RECEIVE_MSG_PEER = "peermsg";
        final String DO_UDP_HOLE_PUNCHING = "doUDPHolePunching";
        final String SEND_DATA = "SendLocation";
        final String ACK = "Ack";
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
        do {


                Log.d("P2P", "P2Pレシーブ起動直前");
                try{
                socket.receive(receivePacket);
                } catch (IOException e) {
                    Log.d("P2P", "エラー:"+e);
                }
                String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
            try{
                JSONObject jsonObject = new JSONObject(result);

                SignalingJSONObject signalingJSONObject = new SignalingJSONObject(jsonObject);
                String processType = signalingJSONObject.getProcessType();
                //Log.d("Receive","送信元IP:"+receivePacket.getAddress());


                if(processType.equals(GET_PERIPHERAL_USER)){
                    iP2PReceiver.onGetPeripheralUser(signalingJSONObject.getPerioheralUsers());
                }

                else if(processType.equals(DO_UDP_HOLE_PUNCHING)){
                    iP2PReceiver.onDoUDPHolePunching(signalingJSONObject.getSrcUser());
                }

                //プロセスタイプで識別している　　送信するときはsendlocationになっている
                else if(processType.equals(SEND_DATA)){
                    P2PJSONObject p2pJSONObject = new P2PJSONObject(jsonObject);
                        Log.d("Receive", "送信元:" + receivePacket.getAddress());
                        Location location = p2pJSONObject.getPeripheralUserLocation();
                        iP2PReceiver.onGetPeripheralUserLocation(p2pJSONObject.getLocationCount(),receivePacket.getAddress().getHostAddress(),receivePacket.getPort(),location,p2pJSONObject.getPeerId(),p2pJSONObject.getSpeed());

                }

                else if(processType.equals(ACK)){
                    P2PJSONObject p2pJSONObject = new P2PJSONObject(jsonObject);
                        Log.d("Receive", "送信元:" + receivePacket.getAddress());
                        iP2PReceiver.onGetAck(p2pJSONObject.getLocationCount(), receivePacket.getAddress().getHostAddress(), receivePacket.getPort());
                }

            } catch (JSONException e) {
                Log.d("P2P", "エラー:"+e);
            }

        }while (true);
    }

    /**
     * 完了処理
     */
    @Override
    protected void onPostExecute(Void a) {

    }

}
