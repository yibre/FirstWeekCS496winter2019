package com.example.q.tab6;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.lang.Runnable;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {

    private static final String TAG = "MainActivity";
    Coin_Name john1 = new Coin_Name("John1", "12-20-1998", "Male");
    Coin_Name john2 = new Coin_Name("John2", "12-20-1998", "Male");
    Coin_Name john3 = new Coin_Name("John3", "12-20-1998", "Male");
    Coin_Name john4 = new Coin_Name("John4", "12-20-1998", "Male");
    Coin_Name john5 = new Coin_Name("John5", "12-20-1998", "Male");
    ListView mListView;
    View view;
    CoinListAdapter adapter;
    JSONParser parser = new JSONParser();


    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        Button button = (Button) view.findViewById(R.id.btn);

        String BTC_Price1 = "";
        String name = "BTC";
        Runnable r = new MyThreadTask();
        Thread t = new Thread(r);
        t.start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "sdfsfsdfsd");
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });
        return view;
        //return inflater.inflate(R.layout.fragment_three, container, false);

    }

    class MyThreadTask implements Runnable{

        public void run(){
            //Log.d("test", Integer.toString(Thread.currentThread().getPriority()));

            String BTC_Price = get_BTC("BTC");
            String ETH_Price = get_BTC("ETH");
            String XRP_Price = get_BTC("XRP");
            String BCH_Price = get_BTC("BCH");
            String EOS_Price =  get_BTC("EOS");
            Bundle bun = new Bundle();
            JSONObject all_coin = new JSONObject();

            all_coin.put("BTC", BTC_Price);
            all_coin.put("ETH", ETH_Price);
            all_coin.put("XRP", XRP_Price);
            all_coin.put("BCH", BCH_Price);
            all_coin.put("EOS", EOS_Price);
            Log.e("test", all_coin.toString());

            bun.putString("HTML_DATA", all_coin.toString());
            Message msg = handler.obtainMessage();
            msg.setData(bun);
            handler.sendMessage(msg);
            Log.e("test", "11111");
        }
    }
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            ArrayList<Coin_Name> coinList = new ArrayList<>();
            Bundle bun = msg.getData();
            String BTC_Price = bun.getString("HTML_DATA");
            //Log.e("test", BTC_Price);
            try {
                JSONObject obj = (JSONObject) parser.parse(BTC_Price);
                //set btc info
                String btc_info = (String) obj.get("BTC");
                JSONObject btc_obj = (JSONObject) parser.parse(btc_info);
                JSONObject btc_obj2 = (JSONObject) btc_obj.get("data");
                String btc_opening_price = (String) btc_obj2.get("opening_price");
                String btc_closing_price = (String) btc_obj2.get("closing_price");
                int btc_a = Integer.parseInt(btc_opening_price);
                int btc_b = Integer.parseInt(btc_closing_price);
                double btc_Percentage = (btc_b-btc_a)*100/btc_a;
                double btc_Percentage1 = Double.parseDouble(String.format("%.2f",btc_Percentage));

                john1 = new Coin_Name("Bitcoin",btc_closing_price+" 원", Double.toString(btc_Percentage1)+" %");
                coinList.add(john1);

                //set eth info
                String eth_info = (String) obj.get("ETH");
                JSONObject eth_obj = (JSONObject) parser.parse(eth_info);
                JSONObject eth_obj2 = (JSONObject) eth_obj.get("data");
                String eth_opening_price = (String) eth_obj2.get("opening_price");
                String eth_closing_price = (String) eth_obj2.get("closing_price");
                int eth_a = Integer.parseInt(eth_opening_price);
                int eth_b = Integer.parseInt(eth_closing_price);
                double eth_Percentage = (eth_b-eth_a)*100/eth_a;
                double eth_Percentage1 = Double.parseDouble(String.format("%.2f",eth_Percentage));

                john2 = new Coin_Name("Etherium",eth_closing_price+" 원", Double.toString(eth_Percentage1)+" %");

                //set xrp info
                String xrp_info = (String) obj.get("XRP");
                JSONObject xrp_obj = (JSONObject) parser.parse(xrp_info);
                JSONObject xrp_obj2 = (JSONObject) xrp_obj.get("data");
                String xrp_opening_price = (String) xrp_obj2.get("opening_price");
                String xrp_closing_price = (String) xrp_obj2.get("closing_price");
                int xrp_a = Integer.parseInt(xrp_opening_price);
                int xrp_b = Integer.parseInt(xrp_closing_price);
                double xrp_Percentage = (xrp_b-xrp_a)*100/xrp_a;
                double xrp_Percentage1 = Double.parseDouble(String.format("%.2f",xrp_Percentage));

                john3 = new Coin_Name("Riple",xrp_closing_price+" 원", Double.toString(xrp_Percentage1)+" %");

                //set bch info
                String bch_info = (String) obj.get("BCH");
                JSONObject bch_obj = (JSONObject) parser.parse(bch_info);
                JSONObject bch_obj2 = (JSONObject) bch_obj.get("data");
                String bch_opening_price = (String) bch_obj2.get("opening_price");
                String bch_closing_price = (String) bch_obj2.get("closing_price");
                int bch_a = Integer.parseInt(bch_opening_price);
                int bch_b = Integer.parseInt(bch_closing_price);
                double bch_Percentage = (bch_b-bch_a)*100/bch_a;
                double bch_Percentage1 = Double.parseDouble(String.format("%.2f",bch_Percentage));

                john4 = new Coin_Name("Bitcoin Cash",bch_closing_price+" 원", Double.toString(bch_Percentage1)+" %");

            }catch(ParseException e){
                e.printStackTrace();
            }
            //BTC_Price1 = BTC_Price;


            john5 = new Coin_Name("계좌 검색","", "");


            coinList.add(john2);
            coinList.add(john3);
            coinList.add(john4);
            coinList.add(john5);
            Log.e("test", "aaaaaa");
            adapter = new CoinListAdapter(getActivity(),R.layout.adapter_view_layout, coinList);
            mListView.setAdapter(adapter);
        }

    };
    private String get_BTC(String coin_name){
        try {
            HttpURLConnection connection = null;
            URL url = null;
            String response = null;
            String parameters = "";
            url = new URL("https://api.bithumb.com/public/ticker/"+coin_name);
            //create the connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            //set the request method to GET
            connection.setRequestMethod("GET");
            //get the output stream from the connection you created
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter request = new OutputStreamWriter(os, "UTF-8");
            //write your data to the ouputstream
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            //create your inputsream
            InputStreamReader isr = new InputStreamReader(
                    connection.getInputStream());
            //read in the data from input stream, this can be done a variety of ways
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            //get the string version of the response data
            response = sb.toString();
            isr.close();
            reader.close();
            return response;
            //do what you want with the data now

            //always remember to close your input and output streams

        } catch (Exception e) {
            Log.e("test", "ddddddd");
            Log.e("test", e.toString());
            return "error";
        }
    }
}
