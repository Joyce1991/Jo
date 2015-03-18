package com.jalen.jo.beans;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * Created by jh on 2015/3/18.
 * <br/>
 * <p/>
 * 图书JavaBean
 */
public class Book {
    /**
     * 图书评价
     */
    private Map<String, Object> rating;
    /**
     * 图书子标题
     */
    private String subtitle;
    /**
     * 作者
     */
    private List<String> author;
    /**
     * 出版日期
     */
    private String pubdate;
    /**
     * 标签
     */
    private List<Map<String, Object>> tags;
    /**
     * 原标题
     */
    private String origin_title;
    /**
     * 图书图片url
     */
    private String image;
    /**
     * 书籍装帧形式
     */
    private String binding;
    /**
     * 译者
     */
    private List<String> translator;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 图书页数
     */
    private String pages;
    /**
     * 多大小规格图片集合
     */
    private Map<String, String> images;
    /**
     * 豆瓣上该书的页面
     */
    private String alt;
    /**
     * 豆瓣库ID
     */
    private String id;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * ISBN-10
     */
    private String isbn10;
    /**
     * ISBN-13
     */
    private String isbn13;
    /**
     * 图书标题
     */
    private String title;
    /**
     * 按id查询book的url
     */
    private String url;
    /**
     * 原作名
     */
    private String alt_title;
    /**
     * 作者信息
     */
    private String author_intro;
    /**
     * 图书简介
     */
    private String summary;
    /**
     * 图书系列
     */
    private Map<String, String> series;
    /**
     * 价格
     */
    private String price;

    public Map<String, Object> getRating() {
        return rating;
    }

    public void setRating(Map<String, Object> rating) {
        this.rating = rating;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public List<Map<String, Object>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, Object>> tags) {
        this.tags = tags;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public List<String> getTranslator() {
        return translator;
    }

    public void setTranslator(List<String> translator) {
        this.translator = translator;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Map<String, String> getSeries() {
        return series;
    }

    public void setSeries(Map<String, String> series) {
        this.series = series;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
