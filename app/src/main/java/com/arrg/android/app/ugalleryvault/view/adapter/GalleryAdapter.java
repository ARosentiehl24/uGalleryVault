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

import com.arrg.android.app.ugalleryvault.R;
import com.arrg.android.app.ugalleryvault.model.entity.PhoneAlbum;
import com.bumptech.glide.Glide;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryAdapter extends MultiChoiceAdapter<GalleryAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private ArrayList<PhoneAlbum> phoneAlbumArrayList;
    private DisplayMetrics displaymetrics;
    private Integer height;
    private Integer width;

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

        holder.container.setLayoutParams(new CardView.LayoutParams(width / 3, Math.round((width / 3) * 1.5f)));
        holder.tvAlbumName.setText(phoneAlbum.getAlbumName());
        holder.tvNumberOfFiles.setText(String.format(Locale.US, "(%d)", phoneAlbum.getPhoneMedias().size()));

        if (phoneAlbum.getPhoneMedias().get(0).getMediaType() == 3) {
            holder.ivPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return phoneAlbumArrayList.size();
    }

    @Override
    protected void setActive(View view, boolean state) {
        super.setActive(view, state);
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(ViewHolder holder, int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
