package com.example.q.tab6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.Jwts;

public class login extends Activity{
    private EditText Accesskey;
    private EditText Secretkey;
    private Button Login;
    private TextView Login_info;
    private int counter = 5;
    int tmoney;
    int bmoney;
    JSONParser parser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        connect_upbit("1", "1");
        Log.e("test", "asdfsfdsaaaf");

        Accesskey = (EditText) findViewById (R.id.Accesskey);
        Secretkey = (EditText) findViewById(R.id.secretkey);
        Login = (Button) findViewById(R.id.login);
        Login_info = (TextView) findViewById(R.id.Login_info);

        Login_info.setText("Num of attempts remaining: 5");


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Accesskey.getText().toString(), Secretkey.getText().toString());

            }
        });
    }
    class MyThreadTask1 implements Runnable{
        String accessKey;
        String secretKey;
        public MyThreadTask1(String accessKey, String secretKey){
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }
        @Override
        public void run() {
            SecretKeySpec secretKey1;
            Mac sha256_HMAC;
            String authenticationToken = "";
            try {
                sha256_HMAC = Mac.getInstance("HmacSHA256");
                secretKey1 = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
                Log.e("test", secretKey1.toString());
                sha256_HMAC.init(secretKey1);
                Log.e("test", secretKey1.toString());
                String jwtString = Jwts.builder().claim("access_key", accessKey).claim("nonce", new Date().getTime()).signWith(secretKey1).compact();
                authenticationToken = "Bearer " + jwtString;
            }catch (NoSuchAlgorithmException e){
            }catch (UnsupportedEncodingException e){
            }catch(InvalidKeyException e){
            }
            Log.e("test", authenticationToken);

            try {
                HttpURLConnection connection = null;
                URL url = null;
                String response = null;
                String parameters = "";
                url = new URL("https://api.upbit.com/v1/accounts");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", authenticationToken);

                int responseCode = connection.getResponseCode();
                String line = "";
                InputStreamReader isr = new InputStreamReader(
                        connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response = sb.toString();
                isr.close();
                reader.close();
                Bundle bun = new Bundle();
                bun.putString("HTML_DATA", response);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            } catch (Exception e) {
                Log.e("test", e.toString());

            }
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String account_info = bun.getString("HTML_DATA");
            JSONArray acc_info;
            int total_money = 0;
            int buy_money = 0;
            try {
                acc_info = (JSONArray) parser.parse(account_info);
                for (int i = 0; i<acc_info.size(); i++) {
                    int j = 1;
                    JSONObject actor = (JSONObject) acc_info.get(i);
                    String currency = (String) actor.get("currency");

                    if(currency.equals("KRW")){
                        j = 1;
                    }else if(currency.equals("BTC")){
                        j = 4279000;
                    }else if(currency.equals("ETH")){
                        j = 157300;
                    }else if(currency.equals("EOS")){
                        j = 2885;
                    }
                    String balance = (String) actor.get("balance");
                    double balance1 = Double.valueOf(balance);
                    total_money += (balance1*j);
                    String avg_buyprice = (String) actor.get("avg_buy_price");
                    double avg_buyprice1 = Double.valueOf(avg_buyprice);
                    buy_money += (balance1*avg_buyprice1);
                }
                Log.e("test", total_money+"");
                Log.e("test", buy_money+"");
                tmoney = total_money;
                bmoney = buy_money;
            }catch (ParseException e){
            }
        }
    };

    private void connect_upbit(String accessKey, String secretKey){
        accessKey = "UNLuG7CLvmuGIvQPDmKBQiQVXFzMjEL3pdNli8qD";
        secretKey = "1QLH5A628yhjxmsEgzpr9dqxftqYa63txpIH1Gxr";
        //Algorithm algorithm = Algorithm.HMAC256(secretKey);
        //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Runnable r1 = new MyThreadTask1(accessKey, secretKey);
        Thread t1 = new Thread(r1);
        t1.start();
    }

    private void validate(String userName, String userPassword) {

        if((userName.equals("Admin") && userPassword.equals("1234"))||(userName.equals("UNLuG7CLvmuGIvQPDmKBQiQVXFzMjEL3pdNli8qD") && userPassword.equals("1QLH5A628yhjxmsEgzpr9dqxftqYa63txpIH1Gxr"))) {
            Intent intent2 = new Intent (login.this, showinfo.class);
            intent2.putExtra("total_buy", tmoney);
            intent2.putExtra("account", bmoney);
            startActivity(intent2);
        }else{
            counter--;
            Login_info.setText("Num of attempts remaining:" + String.valueOf(counter));
            if(counter == 0){
                Login.setEnabled(false);
            }
        }
    }
    public void onClick(View view) {
        finish();
    }
}

