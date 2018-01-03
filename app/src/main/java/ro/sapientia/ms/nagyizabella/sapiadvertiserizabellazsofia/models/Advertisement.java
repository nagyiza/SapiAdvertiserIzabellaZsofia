package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Izabella on 2017-11-20.
 * Model of user data
 */

public class Advertisement implements Serializable{
    /**
     * The advertisement id
     */
    private String id;
    /**
     * The advertisement's title
     */
    private String title;
    /**
     * The advertisement's detail
     */
    private String detail;
    /**
     * The advertisement's location, Google Maps
     */
    private String location;
    /**
     * The user id, which take from firebase
     */
    private String userId;
    /**
     * The cursor Image list
     */
    private List<String> photos;
    private String hide;

    /**
     * Class constructor
     * @param title  The advertisement's title
     * @param detail The advertisement's detail
     * @param location The advertisement's location
     * @param user The advertisement's user
     * @param photos The advertisement's photos
     */
    public Advertisement(String title, String detail, String location, String user, List<String> photos){
        this.title = title;
        this.detail = detail;
        this.location = location;
        this.userId = user;
        this.photos = photos;
        this.hide = "false";
    }
/**
 * Getters
 */
    /**
     * Get the advertisement's title
     * @return title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Get the advertisement's detail
     * @return detail
     */
    public String getDetail(){
        return detail;
    }

    /**
     * Get the advertisement's location
     * @return location
     */
    public String getLocation(){
        return location;
    }

    /**
     * Get the userId
     * @return userId
     */
    public String getUserId(){
        return userId;
    }

    /**
     * Get the advertisement's photos
     * @return photos
     */
    public List<String> getPhoto(){
        return photos;
    }

    /**
     * Get the advertisement's id
     * @return id
     */
    public String getId(){
        return id;
    }

    /**
     * Get the advertisement's hide
     * @return hide
     */
    public String getHide(){return hide;}


/**
 * Setters
 */

    /**
     * Set the advertisement's title
     * @param title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Set the advertisement's detail
     * @param detail
     */
    public void setDetail(String detail){
        this.detail = detail;
    }

    /**
     * Set the advertisement's location
     * @param location
     */
    public void setLocation(String location){
        this.location= location;
    }

    /**
     * Set the User id
     * @param id
     */
    public void setUserId(String id){
        this.userId= id;
    }

    /**
     * Set the advertisement's photos
     * @param photos
     */
    public void setPhoto(List<String> photos){
        this.photos= photos;
    }

    /**
     * Set the advertisement's id
     * @param id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Set the advertisement's hide
     * @param hide
     */
    public void setHide(String hide){
        this.hide = hide;
    }
}
