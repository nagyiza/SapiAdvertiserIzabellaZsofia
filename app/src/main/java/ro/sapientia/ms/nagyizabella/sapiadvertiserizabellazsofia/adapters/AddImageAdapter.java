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
 * This adapter display the images in grid view, when the user adding new advertisement
 */

public class AddImageAdapter extends ArrayAdapter {
    /**
     * This is the activity context, which use the adapter
     */
    private Context context;
    /**
     * The layout id, in which is the image view xml
     * The image view's id
     */
    private int layoutResourceId;
    /**
     * List of the pictures
     * This adapter display this pictures
     */
    private List<Bitmap> data = new ArrayList<Bitmap>();

    /**
     * Class constructor
     * @param context Activity context
     * @param layoutResourceId The image view's id
     * @param data Pictures
     */
    public AddImageAdapter(Context context, int layoutResourceId, List<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    /**
     * Get the view, in which the adapter display the pictures
     * @param position The picture's position from the list, which want to show
     * @param convertView  The convert view
     * @param parent  The view group
     * @return The gid view's row
     */
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

