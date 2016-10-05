package com.arrg.android.app.ugalleryvault.view.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends DragSelectRecyclerViewAdapter<GalleryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder, View itemView, int position);

        void onLongItemClick(ViewHolder viewHolder, View itemView, int position);
    }

    public static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    public static final String MP3_REG = "^.*\\.(mp3|wav)$";
    public static final String JPG_REG = "^.*\\.(gif|jpg|png)$";

    private ArrayList<PhoneAlbum> phoneAlbumArrayList;
    private DisplayMetrics displaymetrics;
    private FragmentActivity fragmentActivity;
    private Integer height;
    private Integer width;
    private OnItemClickListener onItemClickListener;

    public GalleryAdapter(FragmentActivity fragmentActivity, ArrayList<PhoneAlbum> phoneAlbumArrayList) {
        this.fragmentActivity = fragmentActivity;
        this.phoneAlbumArrayList = phoneAlbumArrayList;

        this.displaymetrics = new DisplayMetrics();
        this.fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        this.height = displaymetrics.heightPixels;
        this.width = displaymetrics.widthPixels;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PhoneAlbum phoneAlbum = phoneAlbumArrayList.get(position);

        Glide.with(fragmentActivity).load(phoneAlbum.getCoverMedia()).crossFade().into(holder.ivCover);

        holder.container.setLayoutParams(new CardView.LayoutParams(width / fragmentActivity.getResources().getInteger(R.integer.grid_count_gallery), Math.round((width / fragmentActivity.getResources().getInteger(R.integer.grid_count_gallery)) * 1.5f)));
        holder.tvAlbumName.setText(phoneAlbum.getAlbumName());
        holder.tvNumberOfFiles.setText(String.format(Locale.US, "(%d)", phoneAlbum.getPhoneMedias().size()));

        if (phoneAlbum.getCoverMedia().matches(MTV_REG)) {
            holder.ivPlay.setVisibility(View.VISIBLE);
        }

        holder.cbIsSelected.setVisibility(isIndexSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.cbIsSelected.setChecked(phoneAlbum.getSelected());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout, parent, false), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return phoneAlbumArrayList.size();
    }

    public PhoneAlbum getAlbum(int position) {
        return phoneAlbumArrayList.get(position);
    }

    public void setChecked(int position, boolean isChecked) {
        phoneAlbumArrayList.get(position).setSelected(isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private OnItemClickListener onItemClickListener;

        @BindView(R.id.card_container)
        CardView container;

        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.ivPlay)
        ImageView ivPlay;

        @BindView(R.id.tvAlbumName)
        TextView tvAlbumName;

        @BindView(R.id.tvNumberOfFiles)
        TextView tvNumberOfFiles;

        @BindView(R.id.cbIsSelected)
        CheckBox cbIsSelected;

        ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onItemClickListener = onItemClickListener;
            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(this, itemView, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onLongItemClick(this, itemView, getAdapterPosition());
            }
            return true;
        }
    }
}
