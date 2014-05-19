package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Test implements Comparable<Test> {

    private long id;
    private String name;
    private List<String> tasks;
    private List<Category> categories;
    private long timestamp;
    private boolean uploaded;
    private List<Session> sessions;

    //For new instances
    public Test(long timestamp) {
        id = -1;
        this.timestamp = timestamp;
        tasks = new ArrayList<String>();
        categories = new ArrayList<Category>();
        sessions = new ArrayList<Session>();
    }

    //For DB import
    public Test(long id, String name, List<String> tasks, List<Category> categories, long timestamp,boolean uploaded, List<Session> sessions) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.categories = categories;
        this.timestamp=timestamp;
        this.uploaded=uploaded;
        this.sessions=sessions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public void addTask(String task){
        tasks.add(task);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public long getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(long timestamp){
        this.timestamp=timestamp;
    }

    public boolean isUploaded(){
        return uploaded;
    }

    public void setUploaded(boolean uploaded){
        this.uploaded=uploaded;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session){
        sessions.add(session);
    }

    @Override
    public int compareTo(Test t) {
        return (this.id < t.id ) ? -1 : (this.id > t.id) ? 1 : 0 ;
    }
}
