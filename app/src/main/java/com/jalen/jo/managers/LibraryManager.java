package com.jalen.jo.managers;

import com.avos.avoscloud.AVUser;
import com.jalen.jo.book.Book;
import com.jalen.jo.library.JoLibrary;

/**
 * Created by jh on 2015/3/20.
 * <br/>
 * 图书馆管理类   <br/>
 * 1、添加图书    <br/>
 * 2、删除图书    <br/>
 * 3、创建图书馆  <br/>
 * 4、删除图书馆  <br/>
 * 5、移除图书馆
 */
public class LibraryManager {

    private LibraryManager(){

    }

    /**
     * 创建图书馆
     * @param user 创建者
     * @param library   图书馆对象
     * @return 创建成功与否
     */
    public boolean createLibrary(AVUser user, JoLibrary library){

        return false;
    }

    /**
     * 删除一个图书馆
     * @param user  删除者
     * @param library_id    图书馆id
     * @return 是否删除成功
     */
    public boolean deleteLibrary(AVUser user, String library_id){

        return false;
    }

    public boolean addBook(AVUser user, Book book, JoLibrary library){
        return false;
    }

}
