import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class MainSignaling implements IReceive{
    private final static int myPort = 17053;
    private ArrayList<UserInfo> userInfos;
    private DatagramSocket socket;



	public static void main(String args[]){
		MainSignaling main = new MainSignaling();
		main.start();
	  }

	void start() {
		Receive receive;

        userInfos = new ArrayList<>();

        try {
            socket = new DatagramSocket(myPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        receive = new Receive(socket,this);
        receive.start();
	}



	/**
     * ユーザ情報を登録する
     * @param userInfo //ユーザ情報
     */
    @Override
    public void onRegister(UserInfo userInfo){
        userInfos.add(userInfo);
        //System.out.println("REGISTER:現在のuserInfosのサイズ"+userInfos.size());
    }

    /**
     * ユーザ情報を更新する
     * @param userInfo //ユーザ情報
     */
    @Override
    public void onUpdate(UserInfo userInfo){
        for(int i = 0; i < userInfos.size(); i++){
            if(userInfos.get(i).getPublicIP().equals(userInfo.getPublicIP()) && userInfos.get(i).getPublicPort() == userInfo.getPublicPort() &&
                    userInfos.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfos.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
                userInfos.set(i, userInfo);
                break;
            }
        }
    }

    /**
     * 1.指定されたユーザの地点からか検索半径の円に存在するユーザ情報を検索し，該当ユーザを検索リストに格納
     * 2.検索リストに格納されたユーザにNAT通過のために接続先ユーザのIPとポートを送信し，"ping"のような空データを送信させる
     * 3.送信元のユーザに検索結果を返却する
     * @param userInfo//検索元のユーザ情報
     * @param searchDistance//検索半径
     */
    @Override
    public void onSearch(UserInfo userInfo,double searchDistance){
        ArrayList<UserInfo> searchResult = new ArrayList<>();


        for(int i = 0; i < userInfos.size(); i++) {
           double distance = new HubenyDistance().calcDistance(userInfo.getLatitude(),userInfo.getLongitude(),userInfos.get(i).getLatitude(),userInfos.get(i).getLongitude());
           if(distance <= searchDistance){
               if (userInfos.get(i).getPublicIP().equals(userInfo.getPublicIP()) && userInfos.get(i).getPublicPort() == userInfo.getPublicPort() &&
                        userInfos.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfos.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
                } else {
                	//System.out.println("2点の距離は:"+distance+"m");
                    searchResult.add(userInfos.get(i));
                }
           }
        }

        //System.out.println("MainActivity_onSearch:検索結果の個数:"+searchResult.size());

        //つぎにP2P通信を行うためにNATに検索元ユーザの情報を登録させる必要があるのでこれを行うよう促す
        //ここでSendクラスを呼び呼び出し元のユーザに検索結果を返す．引数はpublicIP,publicPort,serchResult

        Send UDPHolePunchingOtherUsers = new Send(socket,userInfo,searchResult,"srcAddrPortRegisterToNat");
        UDPHolePunchingOtherUsers.start();
        Send reply = new Send(socket,userInfo,searchResult,"replyFromMainActivity");
        reply.start();
    }

    /**
     * 指定されたユーザ情報を破棄する
     * @param userInfo//ユーザ情報
     */
    @Override
    public void onDelete(UserInfo userInfo){
        for(int i = 0; i < userInfos.size(); i++){
            if(userInfos.get(i).getPublicIP().equals(userInfo.getPublicIP()) && userInfos.get(i).getPublicPort() == userInfo.getPublicPort() &&
                    userInfos.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfos.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
                userInfos.remove(i);
                //System.out.println("DELETE:現在のuserInfosのサイズ"+userInfos.size());
                break;
            }
        }
    }
}

