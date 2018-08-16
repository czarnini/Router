package com.bogucki.router.adapters;

/**
 * Created by boguc on 31.03.2018.
 */

interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void onItemClear();

}
