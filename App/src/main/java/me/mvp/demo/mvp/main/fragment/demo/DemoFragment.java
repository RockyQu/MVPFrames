package me.mvp.demo.mvp.main.fragment.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.logg.Logg;
import me.mvp.demo.R;
import me.mvp.demo.db.AppDatabase;
import me.mvp.demo.entity.User;
import me.mvp.demo.entity.UserDao;
import me.mvp.demo.ui.widget.dialog.DialogDefault;
import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.utils.ProjectUtils;
import me.mvp.frame.widget.imageloader.glide.GlideImageConfig;
import me.mvp.frame.widget.imageloader.glide.ImageScaleType;

/**
 * Demo
 */
public class DemoFragment extends BaseFragment {

    @BindView(R.id.btn_dialog)
    AppCompatButton btnDialog;

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        DemoFragment fragment = new DemoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        Logg.e("DemoFragment");

        User user = new User();
        user.setUserId("testid");
        user.setName("test");

        UserDao dao = AppDatabase.get(component).userDao();
        dao.insertAll(user);

        List<User> users = dao.getAll();
        Logg.e(users);

        component.getImageLoader().clear(component.getApplication(), GlideImageConfig.builder()
                .isClearDiskCache(true)
                .isClearMemory(true)
                .build());
    }

    @OnClick({R.id.btn_dialog})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog:
                DialogDefault dialogFont = DialogDefault.newInstance();
                dialogFont.show(getFragmentManager(), DialogDefault.TAG);
                break;
        }
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_demo;
    }
}