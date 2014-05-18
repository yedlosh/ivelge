package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Category {

    private long id;
    private String name;
    private List<String> subcategories;

    public Category(String name) {
        id = -1;
        this.name = name;
        subcategories = new ArrayList<String>();
    }

    public Category(long id, String name, List<String> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
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

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(String subcategory){
        subcategories.add(subcategory);
    }

    public String getSubcategory(int index){
        return subcategories.get(index);
    }
}
