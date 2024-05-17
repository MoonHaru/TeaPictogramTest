package com.example.teacommunication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageButtonAdapter extends RecyclerView.Adapter<ImageButtonViewHolder> {
    private int[] imageResources;
    private View.OnClickListener clickListener;

    // Constructor
    public ImageButtonAdapter(int[] imageResources, View.OnClickListener clickListener) {
        this.imageResources = imageResources;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ImageButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_button, parent, false);
        return new ImageButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageButtonViewHolder holder, int position) {
        // Configurar el ImageButton con la imagen correspondiente
        holder.imageButton.setImageResource(imageResources[position]);
        holder.imageButton.setTag(imageResources[position]);
        holder.imageButton.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return imageResources.length;
    }


    public void addItem(int imageResource, int position) {
        if (position >= 0 && position <= imageResources.length) {
            int[] newImageResources = new int[imageResources.length + 1];
            System.arraycopy(imageResources, 0, newImageResources, 0, position);
            newImageResources[position] = imageResource;
            System.arraycopy(imageResources, position, newImageResources, position + 1, imageResources.length - position);
            imageResources = newImageResources;
            notifyItemInserted(position);
        }
    }
}

