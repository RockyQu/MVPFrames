package me.mvp.demo.mvp.main.fragment.tab2;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import me.logg.Logg;
import me.mvp.demo.R;
import me.mvp.demo.db.AppDatabase;
import me.mvp.demo.entity.User;
import me.mvp.demo.entity.UserDao;
import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.frame.IPresenter;

/**
 *
 */
public class Tab2Fragment extends BaseFragment {

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        Tab2Fragment fragment = new Tab2Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        Logg.e("Tab2Fragment");

        User user = new User();
        user.setUserId("testid");
        user.setName("test");
        UserDao dao = AppDatabase.get(component).userDao();
        dao.insertAll(user);

        List<User> users = dao.getAll();
        Logg.e(users);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab2;
    }
}