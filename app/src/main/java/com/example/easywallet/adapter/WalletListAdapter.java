package com.example.easywallet.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easywallet.R;
import com.example.easywallet.model.WalletItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by GUNNER on 10/12/2560.
 */

public class WalletListAdapter extends ArrayAdapter<WalletItem> {
    private Context mContext;
    private int mLayoutResId;
    private ArrayList<WalletItem> mWalletItemList;

    public WalletListAdapter(@NonNull Context context, int layoutResId, @NonNull ArrayList<WalletItem> walletItemList) {
        super(context, layoutResId, walletItemList);

        this.mContext = context;
        this.mLayoutResId = layoutResId;
        this.mWalletItemList = walletItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemLayout = inflater.inflate(mLayoutResId, null);

        WalletItem item = mWalletItemList.get(position);

        ImageView imageItem = itemLayout.findViewById(R.id.image_item);
        TextView textDes = itemLayout.findViewById(R.id.text_des);
        TextView textCost = itemLayout.findViewById(R.id.text_cost);

        textDes.setText(item.des);
        String cost = format(item.cost);
        textCost.setText(cost);

        String pictureFileName = item.picture;
        AssetManager am = mContext.getAssets();
        try {
            InputStream stream = am.open(pictureFileName);
            Drawable drawable = Drawable.createFromStream(stream, null);
            imageItem.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();

            File pictureFile = new File(mContext.getFilesDir(), pictureFileName);
            Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
            imageItem.setImageDrawable(drawable);
        }

        return itemLayout;
    }

    private String format(int cost) {
        DecimalFormat form = new DecimalFormat("#,###");
        return String.valueOf(form.format(cost));
    }
}
