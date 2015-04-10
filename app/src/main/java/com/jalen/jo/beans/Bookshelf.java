package com.jalen.jo.beans;

import java.util.List;

/**
 * Created by jh on 2015/3/30.
 */
public class Bookshelf {
    /**
     * 书架名称（书架1、书架2……）
     */
    private String shelfName;
    /**
     * 书架Id
     */
    private String shelfId;
    /**
     * 书架所有者
     */
    private String shelfOwner;
    /**
     * 书架图书
     */
    private List<String> books;
}
