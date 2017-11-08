package com.cnswan.juggle.module.arouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ARouter 携带parcelable对象
 * Created by cnswan on 2017/11/8.
 */

public class ARouterObjectParcelable implements Parcelable {

    public int    id;
    public String name;

    public ARouterObjectParcelable(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected ARouterObjectParcelable(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Creator<ARouterObjectParcelable> CREATOR = new Creator<ARouterObjectParcelable>() {
        @Override
        public ARouterObjectParcelable createFromParcel(Parcel in) {
            return new ARouterObjectParcelable(in);
        }

        @Override
        public ARouterObjectParcelable[] newArray(int size) {
            return new ARouterObjectParcelable[size];
        }
    };
}
