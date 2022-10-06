package com.anahuac.awsdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    private OnElementListener mOnElementListener;

    public ListAdapter(List<ListElement> itemList, Context context, OnElementListener onElementListener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.mOnElementListener = onElementListener;
    }

    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.aws_elements, null);
        return new ListAdapter.ViewHolder(view, mOnElementListener);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElement> items){mData = items;}



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button editButton;
        TextView name, email;
        OnElementListener onElementListener;

        public ViewHolder(View itemView, OnElementListener onElementListener){
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            email = itemView.findViewById(R.id.emailTextView);
            editButton = itemView.findViewById(R.id.editBtn);
            this.onElementListener = onElementListener;

            editButton.setOnClickListener(this);
        }

        void bindData(final ListElement item){
            name.setText(item.getName());
            email.setText(item.getEmail());
        }

        @Override
        public void onClick(View view) {
            onElementListener.onElementClick(getAdapterPosition());
        }
    }

    public interface OnElementListener{
        void onElementClick(int position);
    }

}
