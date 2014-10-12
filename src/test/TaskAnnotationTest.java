package test;

import main.TaskAnnotation;
import org.junit.Test;
import test.resourses.*;

import static junit.framework.Assert.assertEquals;

public class TaskAnnotationTest {



    @Test
    public void testMkStringMaxAmountIterable() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("MaxAmountIterableClass [IterableWithAnnot = {e0, e1, e2}," +
                        " IterableWithoutAnnot = [e0, e1, e2, e3, e4, e5]]",
                test.mkString(MaxAmountIterableClass.class));
    }
    @Test
    public void testMkStringMaxAmountMap() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("MaxAmountMapClass [mapWithAnnot = {2=e2, 1=e1, 0=e0}," +
                        " mapWithoutAnnot = {0=e0, 1=e1, 2=e2, 3=e3, 4=e4, 5=e5}]",
                test.mkString(MaxAmountMapClass.class));
    }

    @Test
    public void testMkStringModificators() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("ModificatorsClass [publicInt = 1, protectedInt = 2, defaultInt = 3, privateInr = 4]",
                test.mkString(ModificatorsClass.class));
    }

    @Test
    public void testMkStringNeedNull() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("NeedNullClass [nullValueWithAnnot = null]",
                test.mkString(NeedNullClass.class));
    }

    @Test
    public void testMkStringResultOfTheMethod() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("ResultOfTheMethodClass [metthodWithAnnot = With]",
                test.mkString(ResultOfTheMethodClass.class));
    }

    @Test
    public void testMkStringSetFieldName() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("SetFieldNameClass [newName = oldName]",
                test.mkString(SetFieldNameClass.class));
    }

    @Test
    public void testMkStringAbstractFullTest() throws Exception {
        TaskAnnotation test = new TaskAnnotation();
        assertEquals("AbstractFullTestClass [publicInt = 1, protectedInt = 2, " +
                "defaultInt = 3, nullValueWithAnnot = null]",
                test.mkString(AbstractFullTestClass.class));
    }




}