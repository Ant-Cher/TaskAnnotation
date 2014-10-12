package main;

import Annotations.MaxAmountOfDisplayedObjects;
import Annotations.NeedNull;
import Annotations.ResultOfTheMethod;
import Annotations.SetFieldName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Aspera on 08.10.2014.
 */

public class TaskAnnotation {

    private static Object obj;


/**
 * Представляет класс в виде строки типа:
 * имяКласса [имяПоля1 = значение поля1, имяПоля2 = значение поля2]
 *
 * @param clazz Класс который нужно обработать
 *
 * @return String
 */
    public String mkString(Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        List<String> list = new ArrayList<String>();
        String result = clazz.getSimpleName() + " ";
        String p = " = ";

        if (!Modifier.isAbstract(clazz.getModifiers())){          // Если класс не абстрактный - создаем объект класса
            obj = clazz.newInstance();
        }


        Field[] fields = clazz.getDeclaredFields();


        for (Field field : fields) {

            String fieldName = getFieldName(field);     // Получаем имя поля

            String fieldValue = "";


            if (isAnnotationMaxAmount(field)) {     // Проверка на аннотацию MaxAmountOfDisplayedObjects

                if (field.getType().equals(Map.class)) {
                    fieldValue = getFieldValue(field, ValueType.MAP_VALUE );      // Возвращение значений Map'a

                } else if (field.getType().equals(Iterable.class)) {
                   fieldValue = getFieldValue(field, ValueType.ITERABLE_VALUE ); // Возвращение значений Iterable

                }
            } else {
                fieldValue = getFieldValue(field,ValueType.OTHER_VALUE);   // Возвращение значений остальных полей
            }


            if (field.isAnnotationPresent(NeedNull.class) || fieldValue != null) { // Проверка на аннотацию NeedNull

                list.add(fieldName + p + fieldValue);

            }
        }

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(ResultOfTheMethod.class)) { // Проверка на аннотацию ResultOfTheMethod

                Class[] paramTypes = method.getParameterTypes();

                if (paramTypes.length == 0) {
                    String resultOfMethod = "";

                        resultOfMethod = getFieldValue(ValueType.METHOD_VALUE,method);


                    list.add(method.getName() + p + resultOfMethod);

                }
            }
        }

   return result + list.toString();

    }

    protected String getFieldName(Field field){

        String fieldName;

        if (field.isAnnotationPresent(SetFieldName.class)) {
            fieldName = field.getAnnotation(SetFieldName.class).name();
        } else {
            fieldName = field.getName();
        }

        return fieldName;

    }

    protected  String getIterableValue(Field field) throws IllegalAccessException{

        String iterableValue = "{ ";

        Iterable<?> iterable = (Iterable)field.get(obj);

        int m = 0;

        for (Iterator<?> iter = iterable.iterator(); iter.hasNext(); ) {
            if (m < getMaxAmount(field)) {
                iterableValue += iter.next() + " ";
                m++;
            }else break;
        }

        return iterableValue +  "}";
    }

    protected  String getMapValue(Field field) throws IllegalAccessException{

        Map<String, String> newMap = new HashMap<String, String>();

        Map<?, ?> map = (Map)field.get(obj);

        int m = 0;

        for (Map.Entry entry : map.entrySet()) {

            if (m < getMaxAmount(field)) {
                newMap.put(entry.getKey().toString(), entry.getValue().toString());
            }

            m++;
        }

       return newMap.toString();
    }

    protected  int getMaxAmount(Field field){
        return field.getAnnotation(MaxAmountOfDisplayedObjects.class).maxAmount();
    }

    protected  boolean isAnnotationMaxAmount(Field field){
        return field.isAnnotationPresent(MaxAmountOfDisplayedObjects.class);
    }

    protected  String getFieldValue(ValueType type, Method method) throws IllegalAccessException{
        return getFieldValue(null,type,method);
    }
    protected  String getFieldValue(Field field, ValueType type) throws IllegalAccessException{
        return getFieldValue(field,type,null);
    }
    protected  String getFieldValue(Field field, ValueType type, Method method) throws IllegalAccessException{

            String fieldValue = "";

                try {
                    switch (type) {                               // Свич нужен чтобы не пришлось дублировать код
                                                                  // создавая несколько TryCatch'ев.
                        case MAP_VALUE:{
                            fieldValue = getMapValue(field);
                            break;
                        }
                        case ITERABLE_VALUE:{
                            fieldValue =  getIterableValue(field);
                            break;
                        }
                        case OTHER_VALUE:{
                            fieldValue = field.get(obj).toString();
                            break;
                        }
                        case METHOD_VALUE:
                            fieldValue = method.invoke(obj).toString();
                            break;

                    }

                } catch (NullPointerException e) {
                    fieldValue = null;
                } catch (IllegalAccessException c){   // Ловим исключение, когда модификатор доступа
                    fieldValue = "Illegal Access!";   // не позволяет получить значение поля
                } catch (InvocationTargetException i){
                    System.err.print("InvocationTargetException");
                }

            return fieldValue;

    }
}
