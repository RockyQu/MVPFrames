package com.frame.mvp.entity.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BaseEntity
 */
public interface BaseEntity {

    class Bean implements Parcelable {

        // ID
        private int id;

        public Bean() {
            ;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Bean(Parcel source) {
            this.id = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
        }

        public static final Creator<Bean> CREATOR = new Creator<Bean>() {

            @Override
            public Bean createFromParcel(Parcel source) {
                return new Bean(source);
            }

            @Override
            public Bean[] newArray(int size) {
                return new Bean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }
}