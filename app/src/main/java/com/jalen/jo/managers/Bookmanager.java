package com.jalen.jo.managers;

import com.jalen.jo.beans.Book;

/**
 * Created by jh on 2015/3/23.
 * 图书信息管理类<br/>
 * 1、图书信息更改<br/>
 * 2、图书添加<br/>
 * 3、图书删除<br/>
 * 4、借书（服务端需要对资源进行锁定，统一时间只能有一个用户操作）<br/>
 * 5、还书<br/>
 * 6、转借<br/>
 */
public class Bookmanager{
    private static final String TAG = "Bookmanager";

    private volatile static Bookmanager mBookmanager;
    private Book book;

    private Bookmanager(Book book){
        this.book = book;
    }

    public static Bookmanager getInstance(Book book){
        if (mBookmanager == null){
            synchronized (Bookmanager.class){
                if (mBookmanager == null){
                    mBookmanager = new Bookmanager(book);
                }
            }
        }
        return mBookmanager;
    }

}
