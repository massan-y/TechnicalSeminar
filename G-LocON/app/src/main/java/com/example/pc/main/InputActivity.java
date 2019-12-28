package com.example.pc.main;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        final EditText id = findViewById(R.id.ID);
        final RadioGroup radio = findViewById(R.id.radio);

        Button start = findViewById(R.id.start2);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int checkRadio = radio.getCheckedRadioButtonId();
                //ラジオグループの値を取得
                RadioButton radioButton = findViewById(checkRadio);

                if(checkRadio == -1) {
                    //ラジオボタンの入力の要求をtoastで表示
                    Toast.makeText(InputActivity.this, "ハンターか逃走者を選択してください！", Toast.LENGTH_LONG).show();
                }

                else if(id.getText().toString().isEmpty())
                    //IDの入力要求をtoastで表示
                    Toast.makeText(InputActivity.this,"IDを入力してください！",Toast.LENGTH_LONG).show();


                //2つとも入力済み
                else{

                    String radioMsg;

                    if(checkRadio == R.id.hunter)
                        radioMsg = "hunter";
                    else
                        radioMsg = "fugitive";


                    String str = null;
                    //apiが足りないとjoinメソッドが呼べないらしい
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        str = String.join("/" , radioMsg , id.getText().toString());
                    }
                    else{
                        str = radioMsg +"/"+id.getText().toString();
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra("inputData",str);
                    setResult(RESULT_OK,intent2);
                    finish();
                }

            }
        });

    }
}