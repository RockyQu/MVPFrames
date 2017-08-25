package com.tool.common.frame;

public interface IValues {

    /**
     * 如果Fragment想获取到Activity中用户输入的某些值，Activity可以实现这个IValues接口，通过getValues获取
     */
    String[] getValues();
}