package com.testtask.fb.view;

import android.content.Context;

public interface GalleryView {
    void setImagesToView();
    Context getContext ( );
    void showToast (String msg);
    void notifyDataSetChanged();
}
