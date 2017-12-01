package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models;

import java.util.List;

/**
 * Created by Izabella on 2017-11-20.
 */

public class Advertisement {
    private String title;
    private String detail;
    private String location;
    private String userId;
    private List<String> photos;

    public Advertisement(String title, String detail, String location, String user, List<String> photos){
        this.title = title;
        this.detail = detail;
        this.location = location;
        this.userId = user;
        this.photos = photos;
    }

    public String getTitle(){
        return title;
    }
    public String getDetail(){
        return detail;
    }
    public String getLocation(){
        return location;
    }
    public String getUserId(){
        return userId;
    }
    public List<String> getPhoto(){
        return photos;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDetail(String detail){
        this.detail = detail;
    }
    public void setLocation(String location){
        this.location= location;
    }
    public void setUserId(String id){
        this.userId= id;
    }
    public void setPhoto(List<String> photos){
        this.photos= photos;
    }
}
