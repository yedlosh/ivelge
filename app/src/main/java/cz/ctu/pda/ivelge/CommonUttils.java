package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 18.5.2014.
 */
public class CommonUttils {

    public static List<Integer> getAllPriority() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(1);
        result.add(2);
        result.add(3);
        result.add(4);
        result.add(5);
        return result;
    }

    public static List<String> categoryListToString(List<Category> categories) {
        List<String> stringCat = new ArrayList();
        for (Category category : categories) {
            stringCat.add(category.getName());

        }
        return stringCat;
    }
}
