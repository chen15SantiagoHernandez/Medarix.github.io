package com.example.medarix.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medarix.models.MenuItem;
import com.example.medarix.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuList;
    //private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public MenuAdapter(/*Context context,*/ List<MenuItem> menuList, OnItemClickListener listener) {
        //this.context = context;
        this.menuList = menuList;
        this.listener = listener;
    }

    // 🔹 Aquí está tu clase interna ViewHolder
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.titleTextView);
        }

        public void bind(MenuItem menuItem, OnItemClickListener listener) {
            icon.setImageResource(menuItem.getIconResId());
            title.setText(menuItem.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(menuItem));
        }
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(menuList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


}


