package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.R;

/**
 * Created by Izabella on 2017-11-20.
 */

public class AddImageAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Bitmap> data = new ArrayList<Bitmap>();

    public AddImageAdapter(Context context, int layoutResourceId, List<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageView imView = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            imView = row.findViewById(R.id.image_it);

            row.setTag(imView);
        } else {
            imView = (ImageView) row.getTag();
        }

        //imView.setImageBitmap(data.get(position));
        imView.setImageBitmap(Bitmap.createScaledBitmap(data.get(position), 250, 250, false));
        return row;
    }

}

