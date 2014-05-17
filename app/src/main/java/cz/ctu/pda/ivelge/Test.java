package cz.ctu.pda.ivelge;

import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Test implements Comparable<Test> {

    private long id;
    private String name;
    private List<String> participants;
    private List<String> tasks;
    private List<Category> categories;

    public Test(long id, String name, List<String> participants, List<String> tasks, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.tasks = tasks;
        this.categories = categories;
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

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int compareTo(Test t) {
        return (this.id < t.id ) ? -1 : (this.id > t.id) ? 1 : 0 ;
    }
}
