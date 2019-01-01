package com.example.q.tab6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CoinListAdapter extends ArrayAdapter<Coin_Name> {
    private static final String TAG = "CoinListAdapter";
    private Context mContext;
    int mResource;

    public CoinListAdapter(Context context, int resource, ArrayList<Coin_Name> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String birthday = getItem(position).getBirthday();
        String sex = getItem(position).getSex();

        Coin_Name person = new Coin_Name(name, birthday, sex);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvBirthday = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvSex = (TextView) convertView.findViewById(R.id.textView3);

        tvName.setText(name);
        tvBirthday.setText(birthday);
        tvSex.setText(sex);

        return convertView;
    }
}
