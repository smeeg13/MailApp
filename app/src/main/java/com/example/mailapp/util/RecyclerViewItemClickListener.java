package com.example.mailapp.util;

import android.view.View;

public interface RecyclerViewItemClickListener {
    void onItemClick(String todo,View v, int position);
    void onItemLongClick(View v, int position);
}
