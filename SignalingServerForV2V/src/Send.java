

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.json.JSONObject;

/**
 * Created by MF17037 on 2017/12/14.
 */

public class Send extends Thread{
    private DatagramSocket socket;//UDP通信ソケット
    private UserInfo userInfo;//送信元ユーザ情報
    private ArrayList<UserInfo> userInfos;//NAT通過その他ユーザ情報リスト
    private String replyData;

//    Send(DatagramSocket socket,ArrayList<UserInfo> userInfos){
//        this.socket = socket;
//        this.userInfos = userInfos;
//    }

    Send(DatagramSocket socket,UserInfo userInfo, ArrayList<UserInfo> userInfos, String replyData){
        this.socket = socket;
        this.userInfo = userInfo;
        this.userInfos = userInfos;
        this.replyData = replyData;
    }



    /**
     * Sendする
     * 送信元に返信，また，NAT制御用のsend
     * @param type//送信元に返信，また，NAT制御用のsend
     * @return//今回は使わない
     */

    @Override
    public void run() {
        /**
         * srcAddrPortRegisterToNatの場合
         * 送信元ユーザのUserInfoクラスをJSONObjectに変換し，該当範囲のユーザ（userInfos）にJSONObjectを送信する
         * 該当ユーザはデータを受信後，送信元ユーザのAddr,portをNATに記憶させる
         */
        if(replyData.equals("srcAddrPortRegisterToNat")){
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getSrcUserInfo(userInfo);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                for(int i = 0; i < userInfos.size(); i++) {
                    sendPacket = new DatagramPacket(sendData,
                            sendData.length, InetAddress.getByName(userInfos.get(i).getPublicIP()), userInfos.get(i).getPublicPort());
                    socket.send(sendPacket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * replyFromMainActivityの場合
         * 送信元ユーザに検索結果を返す
         * 検索結果はJSONObjectに変換されてから送信される
         */
        else if(replyData.equals("replyFromMainActivity")){
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getUserInfoList(userInfos);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData,
                        sendData.length, InetAddress.getByName(userInfo.getPublicIP()), userInfo.getPublicPort());
                socket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
