package com.example.easywallet.model;

/**
 * Created by GUNNER on 10/12/2560.
 */

public class WalletItem {
    public final int id;
    public final String picture;
    public final String des;
    public final int cost;


    public WalletItem(int id, String picture, String des, int cost) {
        this.id = id;
        this.picture = picture;
        this.des = des;
        this.cost = cost;
    }
}
