package me.mvp.frame.frame;

public interface IValues {

    /**
     * 如果 Fragment 想获取到 Activity 中用户输入的某些值，Activity 可以实现这个 IValues 接口，通过 getValues 获取，当然你也可以使用 EventBus
     */
    String[] getValues();
}