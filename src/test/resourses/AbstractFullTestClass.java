package test.resourses;

import Annotations.MaxAmountOfDisplayedObjects;
import Annotations.NeedNull;
import Annotations.ResultOfTheMethod;
import Annotations.SetFieldName;
import sun.awt.util.IdentityArrayList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aspera on 12.10.2014.
 */
public abstract class AbstractFullTestClass {

    public AbstractFullTestClass(){
        mapWithAnnot = new HashMap<Integer, String>();
        iterableWithAnnot = new IdentityArrayList<String>();
        int i = 0;
        while (i<5){
            mapWithAnnot.put(i, "e"+i);
            iterableWithAnnot.add("e"+i);
            i++;
        }
    }

    static public int publicInt = 1;
    static protected int protectedInt =2;
    static int defaultInt = 3;
    private int privateInr = 4;

    @NeedNull
    String nullValueWithAnnot = null;
    String nullValueWithoutAnnot = null;

    @SetFieldName(name = "newName")
    String oldName = "oldName";

    @MaxAmountOfDisplayedObjects (maxAmount = 2)
    IdentityArrayList<String> iterableWithAnnot;
    Map<Integer,String> mapWithAnnot;

}
