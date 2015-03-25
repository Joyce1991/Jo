package com.jalen.jo.beans;

import java.util.List;

/**
 * Created by jh on 2015/3/20.
 * <br/>
 * 图书馆的JavaBean
 */
public class JoLibrary {
    /**
     * 图书馆id
     */
    private String library_id;
    /**
     * 图书馆类型
     */
    private String library_type;
    /**
     * 图书总量
     */
    private String counts;
    /**
     * 图书集合(图书对象id集合)
     */
    private List<String> books;

}
