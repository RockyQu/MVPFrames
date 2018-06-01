package me.mvp.demo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import me.mvp.demo.entity.User;
import me.mvp.demo.entity.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
}
