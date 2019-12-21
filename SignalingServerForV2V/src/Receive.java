

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.JSONObject;

/**
 * Created by MF17037 on 2017/12/13.
 */

public class Receive extends Thread {
    private DatagramSocket socket;
    private IReceive iReceive;

    Receive(DatagramSocket socket,IReceive iReceive){
        this.socket = socket;
        this.iReceive = iReceive;
    }



    /**
     * データを受信し，プロセスタイプに合わせてMainActivityにインターフェースする
     * 常にループしデータを受信する
     * @param items//適当に作ったから使わない
     * @return //適当に作ったから使わない
     */
    @Override
    public void run() {
        final String REGISTER = "REGISTER";
        final String UPDATE = "UPDATE";
        final String SEARCH = "SEARCH";
        final String DELETE = "DELETE";

        while(true) {
            // receive Data
            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(receivePacket);

                String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
                JSONObject jsonObject = new JSONObject(result);
                ProcessJSONObject processJSONObject = new ProcessJSONObject(jsonObject);
                String processType = processJSONObject.getProcessType();

                if(processType.equals(REGISTER)){
                    iReceive.onRegister(processJSONObject.getUserInfo());
                    //System.out.println(jsonObject.toString(4));
                }
                else if(processType.equals(UPDATE)){
                    iReceive.onUpdate(processJSONObject.getUserInfo());
                    //System.out.println(jsonObject.toString(4));
                }
                else if(processType.equals(SEARCH)){
                    iReceive.onSearch(processJSONObject.getUserInfo(),processJSONObject.getSearchDistance());
                    //System.out.println(jsonObject.toString(4));
                }
                else if(processType.equals(DELETE)){
                    iReceive.onDelete(processJSONObject.getUserInfo());
                    //System.out.println(jsonObject.toString(4));
                }
            } catch (Exception e) {

            }
        }
    }

}
