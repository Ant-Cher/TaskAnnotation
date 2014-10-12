package test.resourses;

import Annotations.MaxAmountOfDisplayedObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspera on 13.10.2014.
 */
public class MaxAmountIterableClass {
    public MaxAmountIterableClass() {
        IterableWithAnnot = new ArrayList<String>();
        IterableWithoutAnnot = new ArrayList<String>();
        int i = 0;
        while (i < 6) {
            IterableWithAnnot.add("e" + i);
            IterableWithoutAnnot.add(i, "e" + i);
            i++;
        }
    }

    @MaxAmountOfDisplayedObjects(maxAmount = 3)
    List<String> IterableWithAnnot;

    List<String> IterableWithoutAnnot;
}
