package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//takes data from a certain position and adds to the viewholder
public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder>
{



    public interface onClickListener
    {
        void onItemClicked (int position);
    }


    public interface OnLongClickListener
    {
        void onItemLongClicked (int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    onClickListener clickListener;

    public itemsAdapter(List<String> items, OnLongClickListener longClickListener, onClickListener clickListener)
    {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator
        View todoView = LayoutInflater.from (parent.getContext()).inflate (android.R.layout.simple_list_item_1, parent, false);

        //wrap inside a view holder and return it
        return new ViewHolder (todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull itemsAdapter.ViewHolder holder, int position) {
        //grab the item at position
        String item = items.get (position);
        //bind item into specified viewholder
        holder.bind (item);
    }

    //tells the recycler view how many items in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //This container will give easy access to views in the list
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById (android.R.id.text1);
        }

        //update the view inside the viewholder with the string data
        public void bind(String item) {
            tvItem.setText (item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked (getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //notify the listener as to which item was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
