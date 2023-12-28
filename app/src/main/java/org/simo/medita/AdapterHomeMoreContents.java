package org.simo.medita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterHomeMoreContents  extends RecyclerView.Adapter<AdapterHomeMoreContents.ViewHolder> {

    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    AdapterHomeMoreContents(Context context, JSONArray data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_more_contents, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = mData.optJSONObject(position).optString("title");
        holder.myTextView.setText(data);
        if (position == 0){
            holder.myImageView.setImageResource(R.drawable.more_content1);
        }else if (position == 1){
            holder.myImageView.setImageResource(R.drawable.more_content2);
        }else if (position == 2){
            holder.myImageView.setImageResource(R.drawable.more_content3);
        }else if (position == 3){
            holder.myImageView.setImageResource(R.drawable.more_content4 );
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.length();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.id_row_more_contents_title);
            myImageView = itemView.findViewById(R.id.id_row_more_contents_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
