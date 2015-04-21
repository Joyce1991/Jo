package com.jalen.jo.library;

import java.util.List;

/**
 * Created by jh on 2015/3/20.
 * <br/>
 * 图书馆的JavaBean
 */
public class JoLibrary {
/*
    {
            "updatedAt": "2015-04-19T13:28:32.033Z",
            "createdAt": "2015-04-19T13:28:32.033Z",
            "objectId": "5533ad80e4b07b48cfb9ee6e",
            "libraryName": "JavaScript",
            "libraryType": "企业图书馆",
            "libraryBrief": "师父大哥低功耗",
            "counts": "0",
            "libraryManager": "joyceit1991@gmail.com",
            "libraryPic": "http://ac-lgt86x4n.clouddn.com/MVC2WoDrQJ5tMMlWWIw6bMz6WcwG6ixxvC5Dbhwu.jpg"
    }
*/

    /**
     * 图书馆id
     */
    private String objectId;
    /**
     * 最近一次更新时间
     */
    private String updatedAt;
    /**

     * 图书馆创建时间
     */
    private String createdAt;
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
     * 图书总量
     */
    private String counts;
    /**
     * 图书集合(图书对象id集合)
     */
    private List<String> books;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
