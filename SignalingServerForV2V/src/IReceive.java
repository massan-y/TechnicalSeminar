/**
 * Created by MF17037 on 2017/12/13.
 */

/**
 * Receiveクラスのインターフェース
 * 各処理に合わせてMainActivityに処理を渡す
 */
public interface IReceive {
    void onRegister(UserInfo userInfo); //登録
    void onUpdate(UserInfo userInfo);//更新
    void onSearch(UserInfo userInfo,double searchDistance);//検索
    void onDelete(UserInfo userInfo);//削除
}
