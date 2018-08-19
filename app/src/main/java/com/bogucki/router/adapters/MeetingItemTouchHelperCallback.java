package com.bogucki.router.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.bogucki.router.R;

/**
 * Created by M. bogucki
 */

public class MeetingItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private MeetingsAdapter mAdapter;
    private Context mContext;


    public MeetingItemTouchHelperCallback(MeetingsAdapter mAdapter, Context mContext) {
        this.mAdapter = mAdapter;
        this.mContext = mContext;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.onItemClear();
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            Bitmap icon;
            Bitmap phoneIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.phone);

            if (viewHolder.getAdapterPosition() == 0) {
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.done);
            } else {
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.delete);
            }

            if (dX > 0) {
                c.drawBitmap(phoneIcon,
                        (float) itemView.getLeft() + convertDpToPx(16),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - phoneIcon.getHeight()) / 2,
                        p);
            } else {
                c.drawBitmap(icon,
                        (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                        p);
            }

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }


    private int convertDpToPx(int dp) {
        return Math.round(dp * (mContext.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
