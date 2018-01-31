package Entity;

/**
 * Created by liyang on 2017/10/23.
 */

public class News {
    private String uniquekey;
    private String title;
    private String date;
    private String category;
    private String authou_name;
    private String url;
    private String thumbnail_pic_s;

    public News() {
        this.uniquekey="";
        this.title="";
        this.date="";
        this.category="";
        this.authou_name="";
        this.url="";
        this.thumbnail_pic_s="";
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthou_name() {
        return authou_name;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }


    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAuthou_name(String authou_name) {
        this.authou_name = authou_name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

}
