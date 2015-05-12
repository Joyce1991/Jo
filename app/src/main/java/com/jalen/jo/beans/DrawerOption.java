package com.jalen.jo.beans;

/**
 * Created by jh on 2015/3/24.
 * <br/>
 * Drawer item 所对应的数据对象模型
 */
public class DrawerOption {
    private String option;  // 选项标题
    private int count;  // 选项计数
    private int iconId; // 选项iconId



    public DrawerOption(int iconId, String option, int count){
        this.iconId = iconId;
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
    public int getIconId() {
        return iconId;
    }
}
