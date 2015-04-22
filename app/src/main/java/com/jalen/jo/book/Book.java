package com.jalen.jo.book;

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
/*
    {
        "summary": "与我们生命有隐隐暗合或联接的地方，其实最终都会抵达。那些人与物，他们总是等待在此地。每一刻相会都值得珍重。时间有限，我们为迎接彼此已做了漫长的准备。\n记得那日，我为你供奉鲜花和清水，心怀感激，眼目清凉。这一刻交会，我们将不会分离。此后，即便离开，也是一种完成。\n我们所需要寻找的心灵道路，在任何一个时刻就可以开始。而你所需要的，不过是一个开端。一个出发前的准备。\n——安妮宝贝\n全新感觉，再度出发！\n【汇聚两岸三地顶尖作家  世界文坛大师巨匠】\n余华、董启章、陈雪、胡晴舫、马世芳\n诺贝尔文学奖得主   V.S.奈保尔\n日本文学巨匠   太宰治\n美国文学巨擎   乔纳森·弗兰岑\n美国汉学大家   比尔·波特\n非洲文学之父   钦努阿·阿契贝\n英国当代诗人  大卫·康斯坦丁",
        "catalog": "随笔    非洲的假面剧            V.S.奈保尔\n更远                    乔纳森•弗兰岑\n飘飘歧路间              比尔•波特\n从美丽岛到龙的传人      马世芳\n小说    Goodbye                 太宰治\n战地女郎                钦努阿•阿契贝\n米德兰的下午茶          大卫•康斯坦丁\n与作                    董启章\n沙之书                  陈雪\n文本    给塞缪尔•费舍尔讲故事   余华\n未来之城                胡晴舫\n专栏    在印度   安妮宝贝\n绘本    The House in the Night   苏珊•玛丽•斯万森  贝斯•克罗姆斯",
        "pubdate": "2011-1",
        "author_intro": "V. S. 奈保尔（V. S. Naipaul） 英国作家，生于特立尼达和多巴哥。诺贝尔文学奖和两次布克奖的获奖者。代表作有小说《河湾》《毕司沃斯先生的房子》等。2010年10月出版最新创作的游记《非洲的假面剧——非洲信仰面面观》（The Masque of Africa: Glimpses of African Belief），从乌干达、加纳一直写到南非。《非洲的假面剧》系其中的南非部分。\n钦努阿•阿契贝（Chinua Achebe） 尼日利亚作家，被誉为“非洲现代文学之父”。代表作有《瓦解》《动荡》《神箭》《人民公仆》。《战地女郎》选自短篇小说集《战地女郎及其他》（Girls at War and Other Stories），作者以冷峻的笔触，记述了战争对平民造成的悲剧性毁灭。\n余华 生于浙江。代表作有《许三观卖血记》《活着》《在细雨中呼喊》《兄弟》等，作品被翻译成二十多种文字，在近三十个国家出版。《给塞缪尔•费舍尔讲故事》是他应德国费舍尔出版社邀请，为纪念出版社成立125周年所写的。\n安妮宝贝 生于浙江。著有《告别薇安》《八月未央》《彼岸花》《蔷薇岛屿》《二三事》《清醒纪》《莲花》《素年锦时》，2011年合为“十年修订典藏文集”，8月出版最新30万字长篇小说《春宴》。《在印度》是她最新的游记，以独特的文字与摄影，展示了印度人生活和信仰的一个侧面。\n乔纳森•弗兰岑（Jonathan Franzen） 美国小说家。代表作有《纠正》《自由》，2010年《时代周刊》誉之为“美国最伟大的小说家”。《更远》（Farther Away）首刊于2011年4月18日《纽约客》，他带着《鲁滨逊漂流记》和作家大卫•福斯特•华莱士的骨灰，来到一个无人岛，对孤独、自我、自由等一系列命题进行了反思和探问。\n大卫•康斯坦丁（David Constantine） 英国当代诗人、翻译家和小说家，荷尔德林、布莱希特、歌德、克莱斯特等人诗作的译者，著有诗集《守候海豚》《碧玉房子》《关于鬼魂》，小说《戴维斯》《小屋》等。短篇小说《米德兰的下午茶》（Tea at the Midland）获2010年英国BBC国家短篇小说奖。\n董启章 生于香港。著有《地图集》《体育时期》《天工开物•栩栩如真》《时间繁史•哑瓷之光》等，后两书分别获得第1届和第2届红楼梦奖评审团奖，他本人获“2008香港艺术发展奖”年度最佳艺术家奖。《与作》是他的短篇小说新作。\n陈雪 生于台中。著有《桥上的孩子》《陈春天》《附魔者》等，其中《桥上的孩子》获得2004年“中国时报”开卷十大好书奖，《附魔者》入围第34届金鼎奖。短篇小说《沙之书》曾在一个学术研讨会上少量印发，此次重新修订、正式发表。\n胡晴舫 生于台北，现旅居日本。著有《旅人》《她》《办公室》等。《未来之城》写于日本3•11地震之后，探讨城市与自然的关系，以及现代人的生存困境。\n苏珊•玛丽•斯万森（Susan Marie Swanson） 美国诗人、绘本作家。作品《妈妈告诉我的第一件事》获《纽约时报》最佳插画作品奖及夏洛特•佐洛托奖。受《这是王国的钥匙》等传统童谣的启发，她创作了《夜下的房子》这首诗。\n贝斯•克罗姆斯（Beth Krommes） 美国插画家。作品《灯，冰块和名叫“鱼”的船》获金风筝插画奖及蓝丝带奖，《蝴蝶的眼睛和草原的秘密》获亨利•伯格童书奖。2008年与苏珊•玛丽•斯万森合作的《夜下的房子》（The House in the Night）获2009年凯迪克金奖。\n比尔•波特（Bill Porter） 美国汉学家、翻译家，将《寒山诗集》《石屋山居诗集》等译为英文出版，并将自己在中国大陆的旅行见闻写成《空谷幽兰》《禅的行囊》等作品。《飘飘歧路间》是其新作，别出心裁地解读了寒山与韦应物的诗歌。\n马世芳 生于台北。电台节目主持人，著有《地下乡愁蓝调》《昨日书》等。《从美丽岛到龙的传人》借民歌的演变，勾勒了两岸的音乐脉络和历史轨迹。\n太宰治 日本“无赖派”作家，著有《斜阳》《维庸之妻》《人间失格》等。生于1909年，一生五次自杀，1948年终于与山崎富荣在玉川上水情死。在日本，太宰治迄今仍是拥有最多读者的作家。《Goodbye》系未完成的绝笔之作，首次译为中文。",
        "pages": "180",
        "image": "http://img3.douban.com/mpic/s7662452.jpg",
        "publisher": "北京十月文艺出版社",
        "updatedAt": "2015-02-09T07:04:05.148Z",
        "author": "[韩寒 主编]",
        "title": "最小说",
        "translator": "[]",
        "createdAt": "2015-02-09T07:04:05.148Z",
        "objectId": "54d85be5e4b097fc8f8dc22a",
        "subtitle": "",
        "isbn13": "9787532142767",
        "images": "{\"large\":\"http://img3.douban.com/lpic/s7662452.jpg\",\"medium\":\"http://img3.douban.com/mpic/s7662452.jpg\",\"small\":\"http://img3.douban.com/spic/s7662452.jpg\"}"
    }
*/
    private String createdAt;
    private String updatedAt;
    private String objectId;

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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

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
