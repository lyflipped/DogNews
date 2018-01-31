package tools;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by liyang on 2017/11/6.
 */

public class ParseNews {
    public static Document document = null;
    public static void setDocument(String source){
        try{
            document = Jsoup.parse(source);

        }catch (Exception e){
            Log.d("ParseNews","解析出错");
            e.printStackTrace();
        }
    }
    public static ArrayList<String> getText(){
        ArrayList<String> section_text = new ArrayList<>();
        if(document!=null){
            Elements texts = document.getElementsByTag("p");
            for(int i=0;i<texts.size();i++){
                section_text.add(texts.get(i).text());
                Log.d("ParseNews",section_text.get(i));
            }
        }
        return section_text;
    }
    public static ArrayList<String> getImageUrls(){
        ArrayList<String> ImageUrls = new ArrayList<>();
        if(document!=null){
            Elements images = document.getElementsByTag("image");
            for(int i=0;i<images.size();i++){
                ImageUrls.add(images.get(i).attr("src"));
            }
        }
        return ImageUrls;
    }
    public static String getArtical(){
        String artical="";
        if(document!=null){
            Element artical_news = document.getElementById("J_article");
            artical = artical_news.text();
        }
        return artical;
    }
}
