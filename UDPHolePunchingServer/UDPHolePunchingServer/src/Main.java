
public class Main implements UDPHolePanchingFinishListener{

	public static void main(String args[]){
		Main main = new Main();
		main.start();
	  }


	private void start() {
		UDPHolePunchingServer udpHolePunchingServer;
        udpHolePunchingServer = new UDPHolePunchingServer(this);
        udpHolePunchingServer.start();
	}

    @Override
    public void onUDPHolePanchingFinish(){
        //udpHolePunchingServer.start();
    }
}
