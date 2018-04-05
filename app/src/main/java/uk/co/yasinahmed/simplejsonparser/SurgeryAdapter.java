package uk.co.yasinahmed.simplejsonparser;

// Created by yasinahmed on 22/03/2018.

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

// For the recyclerView we need to bind the data to the ViewHolder
// The data needs an adapter, then bound to the ViewHolder (or the row layout)

public class SurgeryAdapter extends RecyclerView.Adapter<SurgeryAdapter.SurgeryViewHolder> {

    // Context here is needed for the LayoutInflater and for instantiating the DetailActivity
    private Context context;
    private List<Surgery> surgeries;
    private HashMap<Integer, Bitmap> imageCache;

    @SuppressLint("UseSparseArrays")
    SurgeryAdapter(Context context, List<Surgery> surgeries) {
        this.context = context;
        this.surgeries = surgeries;
        this.imageCache = new HashMap<>();
    }

    // These 3 methods need to be implemented
    // This method returns the viewHolder (SurgeryViewHolder) which has our elements inside it
    @Override
    public SurgeryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create a layout inflater from the context that gets passed in
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Inflate the layout and return it with a new instance of ViewHolder with view (row layout) as param
        View surgeryRowLayout = layoutInflater.inflate(R.layout.surgery_row, parent, false);
        return new SurgeryViewHolder(surgeryRowLayout);
    }

    // This method does all the binding, the holder gets the UI elements and the position is
    // used to get the specific data from List<Surgery>
    @Override
    public void onBindViewHolder(SurgeryViewHolder holder, int position) {

        Surgery surgery = surgeries.get(position);

        holder.surgeryProcedureTextView.setText(surgery.getSurgeryName());

        // Cache the images to avoid multiple network calls
        if (imageCache.containsKey(position)) {

            Bitmap image = imageCache.get(position);
            holder.surgeryImageIcon.setImageBitmap(image);
        } else {
            new FetchImageFromURL(surgery.getSurgeryImageURL(), holder.surgeryImageIcon, imageCache, position);
        }

        // When the cardView is tapped we can grab the details and present it in another activity
        holder.surgeryCardView.setTag(position);
        holder.surgeryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailActivity(view);
            }
        });
    }

    // CardView onCLickListener method.
    private void startDetailActivity(View view) {

        Intent intent = new Intent(context, DetailActivity.class);

        int surgeryPosition = (int) view.getTag();
        Bitmap image = imageCache.get(surgeryPosition);

        if (image == null) {
            return;
        }

        // To pass images to another activity, first create an output stream, then read it in
        // the other activity.
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bs);
        intent.putExtra("surgeryImage", bs.toByteArray());

        Surgery surgery = surgeries.get(surgeryPosition);
        intent.putExtra("surgery", surgery);

        context.startActivity(intent);
    }

    // This method is the size of the list (number of rows in RecyclerView)
    @Override
    public int getItemCount() {
        return surgeries.size();
    }

    // Get all the UI elements in here
    class SurgeryViewHolder extends RecyclerView.ViewHolder {

        ImageView surgeryImageIcon;
        TextView surgeryProcedureTextView;
        CardView surgeryCardView;

        // This constructor must be implemented
        SurgeryViewHolder(View itemView) {
            super(itemView);

            // Use the itemView constant to find the views
            surgeryImageIcon = itemView.findViewById(R.id.surgeryImageIcon);
            surgeryProcedureTextView = itemView.findViewById(R.id.surgeryProcedureTextView);
            surgeryCardView = itemView.findViewById(R.id.surgeryCardView);
        }
    }

}
