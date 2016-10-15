package appcustomizer.appcustomizer.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import appcustomizer.appcustomizer.R;

/**
 * Created by wuv66 on 9/16/2016.
 */
public class GvAdapter extends BaseDynamicGridAdapter {

    public ViewHolder holder;

    public GvAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new ViewHolder(convertView, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.init((Application) getItem(position));

        return convertView;
    }

    static class ViewHolder {
        private TextView letterText;
        private ImageView img;

        private ViewHolder(View text, View icon) {
            letterText = (TextView) text.findViewById(R.id.text);
            img = (ImageView) icon.findViewById(R.id.img);
        }


        void init(Application app){
            String title = app.appname;
            Drawable icon = app.icon;
            letterText.setText(title);
            img.setImageDrawable(icon);
        }


    }

}
