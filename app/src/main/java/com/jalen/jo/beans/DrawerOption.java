package com.jalen.jo.beans;

/**
 * Created by jh on 2015/3/24.
 * <br/>
 * Drawer item 所对应的数据对象模型
 */
public class DrawerOption {
    private String option;
    private int count;

    public DrawerOption(String option, int count){
        this.option = option;
        this.count = count;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
