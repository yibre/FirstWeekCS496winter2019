package com.example.q.tab6;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubActivity extends Activity
{
    private Button rearrangement;
    private Button modify_delete;
    private Button extracontent;
    private ListView listView;
    private ListViewAdapter adapter = new ListViewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);

        listView = (ListView)findViewById(R.id.address_view);

        bindview();

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout)

        Intent intent = getIntent();
        String data = intent.getStringExtra("address_data");
        Log.d("data@@@subTrydata", data);

        JSONObject interObject;
        JSONArray interArray;
        String[] getName;
        String[] getAddress;
        String check;
        final String check_standard = "a";

        try{
            interObject = new JSONObject(data);
            interArray = new JSONArray(interObject.getString("list"));


            //json array로 변환 : interArray에 intent로 전달된 데이터 저장
            int number_of_add = interArray.length();
            getName = new String[number_of_add];
            getAddress = new String[number_of_add];
            Log.d("data@@@subTryCheck", data);
            //interArray 길이 반환 및 저장 위치 지정 (getName, getAddress에 정보 저장)

                    //intent에 의해 전달되는 data 하나하나 contacts에 속해있는지 확인해야 함으로 for문내에 if문 설정
            for(int i =0; i<number_of_add; i++) {
                JSONObject jsonobject = interArray.getJSONObject(i);
                check = jsonobject.getString("check");
                Log.d("data@@@subFORcheck", check);


                if( check.equals(check_standard)) { // contacts에서 받은 연락처
                    String getContactModifiedName = jsonobject.getString("name");
                    String getContactModifiedAddress = jsonobject.getString("address");

                    //주소록 수정 파트
                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                    String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Contacts.Data.MIMETYPE + "= ?";
                    String[] nameParams = new String[]{
                            //수정전 주소록에서 주소 위치 확인문자배열
                            jsonobject.getString("information_name"),ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    };
                    Context context = getApplicationContext();


                    Cursor nameCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,null, where, nameParams, null);
                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(where, nameParams).withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            getContactModifiedName).build());

                    String[] phoneNumParams = new String[]{
                            jsonobject.getString("information_address"),ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    };

                    Cursor phoneNumCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,null, where, phoneNumParams, null);
                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(where, nameParams).withValue(ContactsContract.CommonDataKinds.Phone.DATA,
                            getContactModifiedAddress).build());

                    try{
                        if(ops.size() > 0){
                            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                            Toast.makeText(getApplicationContext(),"sync complete",Toast.LENGTH_SHORT).show();
                        }
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }catch(OperationApplicationException e) {
                        e.printStackTrace();
                    }
                }

                getName[i] = jsonobject.getString("name");
                getAddress[i] = jsonobject.getString("address");
                Log.d("data@@@subFOR", getName[i] +" / " + getAddress[i]);
                //전달받은 data를 json으로 변환, getName과 getAddress에 data정보 저장

                adapter.addItem(getName[i], getAddress[i],check);
            }
            getContactLists();


            listView.setAdapter(adapter);



        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void getContactLists() throws JSONException {
        Log.d("data@@@contact", "online");
        ArrayList dataList = new ArrayList<JSONObject>();
        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        JSONObject map;

        while(c.moveToNext()){
            map = new JSONObject();
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            map.put("name", name );
            Log.d("data@@@contName", name);

            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

            if(phoneCursor.moveToFirst()){//
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("phone",number);
                Log.d("data@@@contPN", number);
            }

            phoneCursor.close();
            dataList.add(map);

        }//end while
        c.close();


        for(int cou = 0; cou<dataList.size(); cou++) {
            JSONObject temp = (JSONObject) dataList.get(cou);
            JSONObject contacts_check = new JSONObject();
            String numstr = String.valueOf(cou);
            contacts_check.put("check",numstr);
            adapter.addItemContacts(temp.getString("name"),temp.getString("phone" ));
            Log.d("data@@@contend", "okay");
        }

    }
    //핸드폰에서 앱으로 db전달 (main activity 에서permission을 따로 부여받아야 한다.)

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.modify_delete:
                    int count, checked;
                    count = adapter.getCount();
                    if(count >0){
                        checked = listView.getCheckedItemPosition();
                        if(checked > -1 && checked < count){
                            //intent를 이용해 modify_activity로 정보를 이동
                            Intent intent_to_modify = new Intent(SubActivity.this, ModifyActivity.class);
                            Listviewitem temp = adapter.getItem(checked);

                            intent_to_modify.putExtra("information_name",temp.getName());
                            intent_to_modify.putExtra("information_address",temp.getaddress());
                            intent_to_modify.putExtra("information_check",temp.getContacts_check());

                            adapter.notifyDataSetChanged();
                            Log.d("check@@@", temp.getName());
                            Log.d("check@@@", temp.getaddress());
                            startActivity(intent_to_modify);
                        }
                    }

                    break;
                case R.id.extracontent:
                        PopupMenu popupMenu = new PopupMenu(SubActivity.this,view);

                    MenuInflater inflater = popupMenu.getMenuInflater();
                    Menu menu = popupMenu.getMenu();

                    inflater.inflate(R.menu.mainmenu, menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.goingHome:
                                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                                startActivity(intent);
                                        break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    break;


            }


        }
    };

    View.OnLongClickListener longClicklistner = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            switch(v.getId()){
                case R.id.modify_delete:
                    int count, checked;
                    count = adapter.getCount();

                    if(count >0){
                        checked = listView.getCheckedItemPosition();
                        if(checked > -1 && checked < count){
                            //아이템 삭제
                            adapter.remove(checked);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case R.id.rearrangement:
                    /*PopupMenu p = new PopupMenu(getApplicationContext(),v);
                    getMenuInflater().inflate(android.R.menu.menu,p.getMenu());*/


                    break;
            }
            return false;
        }
    };

    public void bindview(){
         rearrangement = (Button)findViewById(R.id.rearrangement);
         modify_delete = (Button)findViewById(R.id.modify_delete);
         extracontent = (Button)findViewById(R.id.extracontent);

        rearrangement.setOnLongClickListener(longClicklistner);
        modify_delete.setOnClickListener(onClickListener);
        modify_delete.setOnLongClickListener(longClicklistner);
        extracontent.setOnClickListener(onClickListener);
    }
    //findviewbyid, onclick


}
