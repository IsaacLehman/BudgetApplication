package com.isaacsmobiledevelopment.lehmanit17.budgetapplication;

/**
 * Created by MD.ISRAFIL MAHMUD on 12/27/2017.
 */
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String Item[];
    String SubItem[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] Item, String[] SubItem ) {
        this.context = context;
        this.Item = Item;
        this.SubItem = SubItem;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Item.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_list_item, null);
        TextView item = (TextView) view.findViewById(R.id.item);
        item.setGravity(1);
        TextView subitem = (TextView) view.findViewById(R.id.subitem);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        item.setText(Item[i]);
        subitem.setText(SubItem[i]);
        return view;
    }
}
