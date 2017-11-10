package tk.osmthemes;

/**
 * Created by jimeet29 on 10-07-2017.
 */
public class DataModel {

    String id = "";
    String theme_name="";
    String made_by="";
    String ss1 = "";
    String ss2="";
    String ss3="";
    String down_count = "";
    String like_count="";
    String dislike_count="";
    String down_link = "";

    public DataModel(String id, String theme_name, String made_by, String ss1,String ss2, String ss3, String down_count, String like_count, String dislike_count, String down_link ) {
        this.id=id;
        this.theme_name=theme_name;
        this.made_by=made_by;
        this.ss1=ss1;
        this.ss2=ss2;
        this.ss3=ss3;
        this.down_count=down_count;
        this.like_count=like_count;
        this.dislike_count=dislike_count;
        this.down_link=down_link;

    }

    public String getId() {
        return id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public String getMade_by() {
        return made_by;
    }

    public String getSs1() {
        return ss1;
    }

    public String getSs2() {
        return ss2;
    }

    public String getSs3() {
        return ss3;
    }

    public String getDown_count() {
        return down_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getDislike_count() {
        return dislike_count;
    }

    public String getDown_link() {
        return down_link;
    }


}

