package Entity;

/**
 * Created by liyang on 2017/10/23.
 */

public class card_image {
    private String name;
    private int imageId;
    private String image_url;
    private String info_other;
    private String category;
    private String url;
    public card_image(String name,int imageId){
        this.name = name;
        this.imageId = imageId;
        this.image_url = "";
        this.info_other = "";
        this.url="";
        this.category="";
    }
    public void setName(String name){
        this.name = name;
    }
    public void setImageId(int id){
        this.imageId = id;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getInfo_other() {
        return info_other;
    }

    public void setInfo_other(String info_other) {
        this.info_other = info_other;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
