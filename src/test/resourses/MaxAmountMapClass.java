package test.resourses;

import Annotations.MaxAmountOfDisplayedObjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aspera on 12.10.2014.
 */
public class MaxAmountMapClass {
    public MaxAmountMapClass() {
        mapWithAnnot = new HashMap<Integer, String>();
        mapWithoutAnnot = new HashMap<Integer, String>();
        int i = 0;
        while (i < 6) {
            mapWithAnnot.put(i, "e" + i);
            mapWithoutAnnot.put(i, "e" + i);
            i++;
        }
    }

    @MaxAmountOfDisplayedObjects(maxAmount = 3)
       Map<Integer,String> mapWithAnnot;

       Map<Integer,String> mapWithoutAnnot;


}
