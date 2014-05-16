package cz.ctu.pda.ivelge;

import java.io.File;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Log {

    private long id;
    private long timestamp;
    private int priority;
    private String location; //TODO change regarding the gMaps API
    private String description;
    private File photo;

    public Log(long id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Log(long id, long timestamp, int priority, String location, String description, File photo) {
        this.id = id;
        this.timestamp = timestamp;
        this.priority = priority;
        this.location = location;
        this.description = description;
        this.photo = photo;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
