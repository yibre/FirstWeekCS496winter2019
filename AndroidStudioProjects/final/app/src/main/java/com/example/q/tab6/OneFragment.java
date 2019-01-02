package com.example.q.tab6;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment{
    private EditText jsonName;
    private EditText jsonAddress;
    private Button buttonname;
    private Button buttonmove;
    private JSONArray data = new JSONArray();
    private JSONObject objectData = new JSONObject();


    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_one,container,false);
        bindView(view);
        //return inflater.inflate(R.layout.fragment_one, container, false);
        return view;
    }



    View.OnClickListener onClickListener = new View.OnClickListener(){
        //버튼 실행 리스너
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_move:
                    Intent intent = new Intent(getActivity(), SubActivity.class);
                    //jsonArray를 list 키값을 사용하여 Object로 변환
                    try {
                        objectData.put("list", data);

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //intent를 이용한 json Object data 전달(String으로 변환하여 전달)
                    String dataconvert = objectData.toString();
                    intent.putExtra("address_data", dataconvert);
                    startActivity(intent);
                    break;

                case R.id.button_name:
                    // 주소록 추가 버튼, Object를 Array에 추가
                    data.put(insertObject());
                    Toast.makeText(getActivity(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                    Log.d("data@", data.toString());
            }
        }
    };

    private JSONObject insertObject(){
        //insert data of address
        JSONObject jsonObject = new JSONObject();
        try{
            int array_length = data.length();
            //중복 주소록 검사
            boolean check_overlap = check(data, jsonName.getText().toString(), jsonAddress.getText().toString(), array_length);
            if(check_overlap) {
                jsonObject.put("name", jsonName.getText().toString());
                jsonObject.put("address", jsonAddress.getText().toString());
                jsonObject.put("check","b");
                Log.d("data@@", jsonObject.toString());
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private boolean check(JSONArray data_check, String name_check, String address_check, int array_length){
        for(int init =0; init<array_length; init++){
            try {
                if (name_check == data_check.getJSONObject(init).getString("name") && address_check == data_check.getJSONObject(init).getString("address")){
                    Toast.makeText(getContext(), "address already existed",Toast.LENGTH_LONG).show();
                    Log.d("data@@check", data_check.getJSONObject(init).getString("name") + " / " + data_check.getJSONObject(init).getString("address"));
                    return false;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        Log.d("data@@check", "true");
        return true;
    }

    private void bindView(View view){
        jsonName = (EditText)view.findViewById(R.id.insert_name);
        jsonAddress = (EditText)view.findViewById(R.id.insert_address);
        buttonname = (Button)view.findViewById(R.id.button_name);
        buttonmove = (Button)view.findViewById(R.id.button_move);

        buttonmove.setOnClickListener(onClickListener);
        buttonname.setOnClickListener(onClickListener);
    }
}
