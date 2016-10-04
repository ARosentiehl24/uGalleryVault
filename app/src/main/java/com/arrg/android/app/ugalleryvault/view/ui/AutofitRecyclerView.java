package com.arrg.android.app.ugalleryvault.view.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceRecyclerView;


public class AutoFitRecyclerView extends MultiChoiceRecyclerView {

    private Integer columnWidth = -1;
    private GridLayoutManager gridLayoutManager;

    public AutoFitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {android.R.attr.columnWidth};

            TypedArray typedArray = context.obtainStyledAttributes(attrs, attrsArray);

            columnWidth = typedArray.getDimensionPixelSize(0, -1);

            typedArray.recycle();
        }

        gridLayoutManager = new GridLayoutManager(getContext(), 1);

        setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        if (columnWidth > 0) {
            int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);

            gridLayoutManager.setSpanCount(spanCount);
        }
    }
}
