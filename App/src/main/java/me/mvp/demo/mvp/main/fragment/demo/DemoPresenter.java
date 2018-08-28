package me.mvp.demo.mvp.main.fragment.demo;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;

public class DemoPresenter extends BasePresenter<DemoRepository> {

    public DemoPresenter(AppComponent component) {
        super(component.getRepositoryManager().createRepository(DemoRepository.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}