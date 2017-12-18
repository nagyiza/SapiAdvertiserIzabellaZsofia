package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.R;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

/**
 * Created by Izabella on 2017-11-26.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Advertisement> values;

    private LayoutInflater inflater;
    //private List<Uri> photos = null;
    private ItemClickListener mClick;
    private ItemLongClickListener mLongClick;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        // each data item is just a string in this case
        public TextView txtTitle;
        public TextView txtDetail;
        public ImageView mImageView;
        public ImageView profileImage;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtTitle = (TextView) v.findViewById(R.id.tv_title);
            txtDetail = (TextView) v.findViewById(R.id.tv_detail);
            mImageView = (ImageView) v.findViewById(R.id.iv_images);
            profileImage = (ImageView) v.findViewById(R.id.profile_creator);


            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClick != null) mClick.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if(mLongClick != null) mLongClick.onItemLongClick(view, getAdapterPosition());
            return true;
        }
    }

    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setClickListener(ItemClickListener item){
        this.mClick = item;
    }

    public interface ItemLongClickListener{
        void onItemLongClick(View v, int position);
    }

    public void setLongClickListener(ItemLongClickListener item){
        this.mLongClick = item;
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(Context context, List<Advertisement> myDataset) {
        this.context = context;
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.advertisement_item , parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Advertisement name = values.get(position);
        holder.txtTitle.setText(name.getTitle());


        holder.txtDetail.setText(name.getDetail());
        List<String> photos = values.get(position).getPhoto();
        Log.d("GLIDE", "Glide elott");
        if(photos != null) {
            Glide.with(inflater.getContext()).load(photos.get(0))
                    .into(holder.mImageView);
            Log.d("GLIDE", photos.get(0));
        }

        //profile image
        FirebaseDatabase database;
        DatabaseReference myRef;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userKey : dataSnapshot.getChildren()) {
                    if(userKey.getKey().equals(values.get(position).getUserId())) {
                        /*String email = (String) userKey.child("email").getValue().toString();
                        String firstName = (String) userKey.child("firstName").getValue().toString();
                        String lastName = (String) userKey.child("lastName").getValue().toString();
                        String phoneNumber = (String) userKey.child("phoneNumber").getValue().toString();*/
                        String profilImage = (String) userKey.child("profilImage").getValue().toString();

                        if (profilImage != null && profilImage.length() != 0) {
                            Glide.with(context).load(profilImage)
                                    .override(50, 50)
                                    .into(holder.profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public String getItem(int pos) {
        return values.get(pos).getId();
    }
    public String getUserId(int pos){
        return values.get(pos).getUserId();
    }

    public void remove(int pos){
        values.remove(pos);
        notifyItemRemoved(pos);
    }

}
