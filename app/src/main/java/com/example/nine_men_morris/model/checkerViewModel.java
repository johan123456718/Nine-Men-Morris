package com.example.nine_men_morris.model;

import android.widget.ImageView;

/**
 * Viewmodel to map an ImageView to a specific hitbox.
 */
public class checkerViewModel{

    private ImageView checkerId;
    private boolean isDeleted;
    private int currentHitbox;

    public checkerViewModel(){
        this.checkerId = null;
        this.isDeleted = false;
        this.currentHitbox = 0;
    }

    public checkerViewModel(ImageView checkerId){
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
        this.currentHitbox = currentHitbox;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
