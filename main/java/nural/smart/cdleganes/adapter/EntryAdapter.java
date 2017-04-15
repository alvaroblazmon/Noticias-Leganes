package nural.smart.cdleganes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nural.smart.cdleganes.R;
import nural.smart.cdleganes.Utils;
import nural.smart.cdleganes.data.XMLParser;

/**
 * Created by alvaro on 16/2/17.
 */

public class EntryAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<XMLParser.Entry> mDataSource;

    public EntryAdapter(Context context, List<XMLParser.Entry> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(mDataSource  != null)
            return mDataSource.size();

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.list_entry_recipe, parent, false);

            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.recipe_list_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.recipe_list_title);
            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.recipe_list_subtitle);
            holder.detailTextView = (TextView) convertView.findViewById(R.id.recipe_list_detail);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = holder.titleTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.detailTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        XMLParser.Entry entry = (XMLParser.Entry) getItem(position);

        titleTextView.setText(entry.title);
        subtitleTextView.setText(entry.description);

        String timeAgo = Utils.getTimeDiff(entry.date);
        String detailText = entry.origen + " - " + timeAgo;
        detailTextView.setText(detailText);

        Picasso.with(mContext).load(entry.imageURL).placeholder(R.mipmap.ic_launcher_bn).into(thumbnailImageView);

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView subtitleTextView;
        public TextView detailTextView;
        public ImageView thumbnailImageView;
    }
}
