package com.example.quochuy.cry.Coin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quochuy.cry.CoinAPI;
import com.example.quochuy.cry.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Quoc Huy on 4/5/2018.
 */

public class CoinAdapter extends ArrayAdapter<Coin> {

    public CoinAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CoinAdapter(Context context, int resource, List<Coin> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_custom_coin, null);
        }

        Coin p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri

            TextView tvName = (TextView) v.findViewById(R.id.id_tv_name);
            TextView tvPrice=(TextView) v.findViewById(R.id.id_tv_price);
            TextView tvChange=v.findViewById(R.id.id_tv_change);

            ImageView imgSymbol=v.findViewById(R.id.id_img_symbol);

            tvName.setText(p.getNameCoin().toString());
            tvPrice.setText(p.getLastValues().getPrice().toString());
            tvChange.setText("("+p.getLastValues().getChange_1h()+")");
            if(Double.parseDouble( p.getLastValues().getChange_1h())>0)
            {
                tvChange.setTextColor(Color.GREEN);
            }
            else
            {
                tvChange.setTextColor(Color.RED);
            }
            CoinAPI coinAPI=new CoinAPI();
            Picasso.with(getContext()).
                    load(coinAPI.getCointImage(p.getSymbol())).
                    placeholder(R.drawable.ic_attach_money_black_24dp).
                    error(R.drawable.ic_error_black_24dp).
                    into(imgSymbol);
        }

        return v;
    }

}