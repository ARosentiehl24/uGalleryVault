package com.arrg.android.app.ugalleryvault.view.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends MultiChoiceAdapter<GalleryAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private ArrayList<PhoneAlbum> phoneAlbumArrayList;

    public GalleryAdapter(FragmentActivity fragmentActivity, ArrayList<PhoneAlbum> phoneAlbumArrayList) {
        this.fragmentActivity = fragmentActivity;
        this.phoneAlbumArrayList = phoneAlbumArrayList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        PhoneAlbum phoneAlbum = phoneAlbumArrayList.get(position);

        Picasso.with(fragmentActivity).load(phoneAlbum.getPhoneMedias().get(0).getMediaPath()).into(holder.ivCover);

        holder.tvAlbumName.setText(phoneAlbum.getAlbumName());
        holder.tvNumberOfFiles.setText(String.format(Locale.US, "(%d)", phoneAlbum.getPhoneMedias().size()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return phoneAlbumArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCover)
        ImageView ivCover;

        @BindView(R.id.tvAlbumName)
        TextView tvAlbumName;

        @BindView(R.id.tvNumberOfFiles)
        TextView tvNumberOfFiles;

        @BindView(R.id.cbIsSelected)
        CheckBox cbIsSelected;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
