package cz.ctu.pda.ivelge;

import java.io.File;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Log {

    private long id;
    private long timestamp;
    private int priority;
    private double latitude;
    private double longitude;
    private String description;
    private File photo;
    private long sessionId;
    private long categoryId;
    private int subcategoryIndex;
    private int taskIndex;

    public Log(long timestamp, long sessionId) {
        id = -1;
        this.timestamp = timestamp;
        this.sessionId = sessionId;
    }

    public Log(long id, long timestamp, int priority, double latitude ,double longitude, String description, File photo, long sessionId, long categoryId, int subcategoryIndex, int taskIndex) {
        this.id = id;
        this.timestamp = timestamp;
        this.priority = priority;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.photo = photo;
        this.sessionId = sessionId;
        this.categoryId = categoryId;
        this.subcategoryIndex = subcategoryIndex;
        this.taskIndex = taskIndex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubcategoryIndex() {
        return subcategoryIndex;
    }

    public void setSubcategoryIndex(int subcategoryIndex) {
        this.subcategoryIndex = subcategoryIndex;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
