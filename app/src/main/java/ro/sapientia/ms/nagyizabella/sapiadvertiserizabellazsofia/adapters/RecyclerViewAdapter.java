package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.R;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

/**
 * Created by Izabella on 2017-11-26.
 * This adapter display the advertisement list in recycler view, when the user watching the advertisements
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  implements Filterable {
    /**
     * This is the activity context, which use the adapter
     */
    private Context context;
    /**
     * List of advertisement
     * A advertisement is a modell class, which contain title, detail, pictures and userid
     */
    private List<Advertisement> values;
    /**
     * Copy of the values
     */
    private List<Advertisement> values2;
    /**
     * For the create the new view
     */
    private LayoutInflater inflater;
    /**
     * Listener object for the item click
     */
    private ItemClickListener mClick;
    /**
     * Listener object for the item long click
     */
    private ItemLongClickListener mLongClick;

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and you provide access to all the views for a data item in a view holder
     */
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

    /**
     * Interface for item click listener
     */
    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

    /**
     * Set the item click listener object
     * @param item
     */
    public void setClickListener(ItemClickListener item){
        this.mClick = item;
    }
    /**
     * Interface for item long click listener
     */
    public interface ItemLongClickListener{
        void onItemLongClick(View v, int position);
    }
    /**
     * Set the item long click listener object
     * @param item
     */
    public void setLongClickListener(ItemLongClickListener item){
        this.mLongClick = item;
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset)
     * @param context  The activity context, which use this adapter
     * @param myDataset  The data of advertisements
     */
    public RecyclerViewAdapter(Context context, List<Advertisement> myDataset) {
        super();
        this.context = context;
        values = myDataset;
        values2 = myDataset;
    }

    /**
     * Create new views (invoked by the layout manager)
     * Create a view holder
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * Get element from your dataset at this position
     * Replace the contents of the view with that element
     * Set the fields(title, detail) and display a picture with glide
     * Get the user, who added the advertisement, and download the user's profile picture and display in a cycle view
     * @param holder The view holder, the new view
     * @param position The advertisement's position from the list, which want to show
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Advertisement name = values.get(position);
        holder.txtTitle.setText(name.getTitle());

        if(name.getDetail().length()>35){
            holder.txtDetail.setText(name.getDetail().substring(0,35)+"...");
        }else{
            holder.txtDetail.setText(name.getDetail());
        }

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


    /**
     * Get the size of advertisements
     * @return Return the size of your dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return values.size();
    }

    /**
     * Get a advertisement from the list
     * @param pos The advertisement's position from the list
     * @return The advertisement
     */
    public String getItem(int pos) {
        return values.get(pos).getId();
    }

    /**
     * Get a user id from the list
     * @param pos The advertisement's position from the list
     * @return The user id.
     */
    public String getUserId(int pos){
        return values.get(pos).getUserId();
    }

    /**
     * Update the advertisement list
     * If the advertisement is amended or is deleted (hided)
     * @param advertisements  New list of advertisement
     */
    public void updateData(List<Advertisement> advertisements) {
        values.clear();
        values.addAll(advertisements);
        notifyDataSetChanged();
    }

    /**
     * Get the filter
     * For the search
     * @return The Filter, in which is the search result
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    values = values2;
                } else {

                    ArrayList<Advertisement> filteredList = new ArrayList<>();

                    for (Advertisement adv : values2) {

                        if (adv.getTitle().toLowerCase().contains(charString) || adv.getDetail().toLowerCase().contains(charString)) {

                            filteredList.add(adv);
                        }
                    }

                    values = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = values;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                values = (ArrayList<Advertisement>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
