package com.example.q.tab6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModifyActivity extends Activity {

    private TextView information;
    private EditText modify_name;
    private EditText modify_address;
    private Button complete;
    String name_information;
    String address_information;
    String check;


    @Override
    protected void onCreate(Bundle savedInstanceStated){
        super.onCreate(savedInstanceStated);
        setContentView(R.layout.activity_modify);

        Intent intent = getIntent();
        name_information = intent.getStringExtra("information_name");
        address_information = intent.getStringExtra("information_address");
        check = intent.getStringExtra("information_check");
        Log.d("mod@@@",name_information + " / " + address_information);


        bindView();
        //if(name_information.indexOf(" : from contacts") == -1){
            complete.setOnClickListener(onClickListener);
        //}else{
            //String [] item = {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};


        //}
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view_modify){
            //클릭리스너가 작동시 수정할 정보를 SubActivity java에 전달

            String modify_named = modify_name.getText().toString();
            String modify_addressed = modify_address.getText().toString();
            Log.d("mod@","before");
                if(modify_named.length() == 0 || modify_addressed.length() == 0) {
                    Log.d("mod@","okay");
                    Toast.makeText(getApplicationContext(),"you can't input blank!!",Toast.LENGTH_SHORT).show();
                }else {
                    checkingDelievery(modify_named, modify_addressed, check);
                    Log.d("mod@",modify_named + " / " + modify_addressed + " / " + check);
                }
        }
    };// modify 버튼 정상 작동 확인(완)

    public void checkingDelievery(String modify_named, String modify_addressed, String check){
        Intent complete = new Intent(ModifyActivity.this, SubActivity.class);
        /*if(address_information.indexOf("from contacts") == -1){*/
            try {
                JSONObject temp = new JSONObject();
                JSONObject deliever = new JSONObject();
                JSONArray temporary = new JSONArray();
                temp.put("name",modify_named);
                temp.put("address",modify_addressed);
                temp.put("check",check);
                temp.put("information_name",name_information);
                temp.put("information_address",address_information);
                temporary.put(temp);
                deliever.put("list",temporary);
                complete.putExtra("address_data", deliever.toString());
                startActivity(complete);
            }catch(JSONException e){
                e.printStackTrace();
            }
    }

    private void bindView(){
        information = (TextView)findViewById(R.id.textView);
        information.setText(name_information+" : " + address_information);
        modify_name = (EditText)findViewById(R.id.modify_name);
        modify_address = (EditText)findViewById(R.id.modify_address);
        complete = (Button)findViewById(R.id.complete);

    }
}