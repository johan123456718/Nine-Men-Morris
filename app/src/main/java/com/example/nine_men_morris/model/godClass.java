package com.example.nine_men_morris.model;


import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

public class godClass {

    private ImageView checkerId;
    private boolean isDeleted;
    private int currentHitbox;

    public godClass(ImageView checkerId){
        this.checkerId = checkerId;
        this.isDeleted = false;
        this.currentHitbox = 0;
    }

    public ImageView getCheckerId() {
        return checkerId;
    }

    public int getCurrentHitbox() {
        return currentHitbox;
    }

    public boolean isDeleted(){
        return this.isDeleted;
    }

    public void setCheckerId(ImageView checkerId) {
        this.checkerId = checkerId;
    }

    public void setCurrentHitbox(int currentHitbox) {
        Log.d("TAG", "setCurrentHitbox: " + currentHitbox);
        this.currentHitbox = currentHitbox;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
