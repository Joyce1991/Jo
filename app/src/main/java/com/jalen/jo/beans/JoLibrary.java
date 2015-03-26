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
    private String libraryId;
    /**
     * 图书馆类型
     */
    private String libraryType;
    /**
     * 图书馆名称
     */
    private String libraryName;
    /**
     * 图书馆图片
     */
    private String libraryPic;
    /**
     * 图书馆简介
     */
    private String libraryBrief;
    /**
     * 图书馆管理员
     */
    private String libraryManager;
    /**
     * 管理者头像
     */
    private String libraryManagerPicUrl;
    /**
     * 图书总量
     */
    private String counts;
    /**
     * 图书集合(图书对象id集合)
     */
    private List<String> books;

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getLibraryType() {
        return libraryType;
    }

    public void setLibraryType(String libraryType) {
        this.libraryType = libraryType;
    }

    public String getLibraryName() {
        return libraryName;
    }
    public String getLibraryPic() {
        return libraryPic;
    }

    public void setLibraryPic(String libraryPic) {
        this.libraryPic = libraryPic;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryBrief() {
        return libraryBrief;
    }

    public void setLibraryBrief(String libraryBrief) {
        this.libraryBrief = libraryBrief;
    }

    public String getLibraryManager() {
        return libraryManager;
    }

    public void setLibraryManager(String libraryManager) {
        this.libraryManager = libraryManager;
    }

    public String getLibraryManagerPicUrl() {
        return libraryManagerPicUrl;
    }

    public void setLibraryManagerPicUrl(String libraryManagerPicUrl) {
        this.libraryManagerPicUrl = libraryManagerPicUrl;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
