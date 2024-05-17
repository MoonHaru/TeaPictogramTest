package com.example.teacommunication;

import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

public class ImageButtonViewHolder extends RecyclerView.ViewHolder {
    public ImageButton imageButton;

    public ImageButtonViewHolder(View itemView) {
        super(itemView);
        imageButton = itemView.findViewById(R.id.imageButton);
    }
}