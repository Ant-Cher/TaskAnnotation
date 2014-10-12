package test.resourses;

import Annotations.ResultOfTheMethod;

/**
 * Created by Aspera on 12.10.2014.
 */
public class ResultOfTheMethodClass {

    @ResultOfTheMethod
    String metthodWithAnnot(){
        return "With";
    }

    String metthodWithoutAnnot(){
        return "Without";
    }

}
