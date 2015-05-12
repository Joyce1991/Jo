package com.jalen.jo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.jo.R;
import com.jalen.jo.beans.DrawerOption;

import java.util.List;

/**
 * Created by jh on 2015/3/24.
 */
public class SimpleListAdapter extends ArrayAdapter<DrawerOption>{

    private Context mContext;
    private int mResourceId;
    private List<DrawerOption> mOptions;

    public SimpleListAdapter(Context context, int resource, List<DrawerOption> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourceId = resource;
        mOptions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(mResourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        DrawerOption option = mOptions.get(position);
        holder.icon.setImageResource(option.getIconId());
        holder.text.setText(option.getOption());
        holder.count.setText(String.valueOf(option.getCount()));

        return convertView;
    }


    class ViewHolder {
        ImageView icon;
        TextView text;
        TextView count;

        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.option_icon);
            text = (TextView) view.findViewById(R.id.option_text);
            count = (TextView) view.findViewById(R.id.option_count);
            view.setTag(this);
        }
    }
}
