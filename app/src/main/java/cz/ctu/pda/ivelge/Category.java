package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Category {

    private long id;
    private String name;
    private List<Category> subcategories;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
        subcategories = new ArrayList<Category>();
    }

    public Category(long id, String name, List<Category> subcategories) {
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

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
