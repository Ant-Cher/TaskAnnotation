package main;

import Annotations.MaxAmountOfDisplayedObjects;
import Annotations.NeedNull;
import Annotations.ResultOfTheMethod;
import Annotations.SetFieldName;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Aspera on 08.10.2014.
 */

public class TaskAnnotation {

    private static final Logger logger = Logger.getLogger(TaskAnnotation.class.getName());
    private static Object obj;
    private static String p = " = ";


/**
 * Представляет класс в виде строки типа:
 * имяКласса [имяПоля1 = значение поля1, имяПоля2 = значение поля2]
 *
 * @param clazz Класс который нужно обработать
 * @return String
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 * @throws InstantiationException
 */
    public String mkString(Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        List<String> list = new ArrayList<String>();
        String result = clazz.getSimpleName() + " ";

        if (!Modifier.isAbstract(clazz.getModifiers())){
            obj = clazz.newInstance();
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = getFieldName(field);
            String fieldValue = "";
            field.setAccessible(true);

            if (field.isAnnotationPresent(MaxAmountOfDisplayedObjects.class)) {
                if (Map.class.isAssignableFrom(field.getType())) {
                    fieldValue = getFieldValue(field, ValueType.MAP_VALUE );
                } else if (Iterable.class.isAssignableFrom(field.getType())) {
                   fieldValue = getFieldValue(field, ValueType.ITERABLE_VALUE );
                } else
                   fieldValue = getFieldValue(field,ValueType.OTHER_VALUE);
            } else {
                fieldValue = getFieldValue(field,ValueType.OTHER_VALUE);
            }

            if (field.isAnnotationPresent(NeedNull.class) || fieldValue != null ) {
                list.add(fieldName + p + fieldValue);
            }
        }

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(ResultOfTheMethod.class)) {
                list.add(getMethod(method));
            }
        }

     return result + list.toString();
    }

    /**
     * Если oбрабатываемый метод не принимает параметров,
     * то возвращается его имя и значение в виде:
     * имяМетода = результатМетода
     *
     * @param method Обрабатываемый метод
     * @return String
     */
    public String getMethod(Method method){

        String result = null;
        method.setAccessible(true);
        Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == 0) {
                String resultOfMethod = "";
                resultOfMethod = getFieldValue(ValueType.METHOD_VALUE,method);
                result = method.getName() + " = " + resultOfMethod;
            }
        return result;
    }
    /**
     * Возвращает имя поля
     *
     * @param field Обрабатываемое поле
     * @return String
     */
    public String getFieldName(Field field){

        String fieldName;
        if (field.isAnnotationPresent(SetFieldName.class)) {
            fieldName = field.getAnnotation(SetFieldName.class).name();
        } else {
            fieldName = field.getName();
        }
        return fieldName;
    }

    /**
     * Возвращает значение поля типа Iterable
     * С учетом аннотации MaxAmountOfDisplayedObjects
     *
     * @param field Обрабатываемое поле
     * @return String
     * @throws IllegalAccessException
     */
    public  String getIterableValue(Field field) throws IllegalAccessException{

        String iterableValue = "{";
        Iterable<?> iterable = (Iterable)field.get(obj);
        int m = 0;

        for (Object anIterable : iterable) {
            if (m < getMaxAmount(field)) {
                iterableValue += anIterable;
            } else break;
            if (m < getMaxAmount(field) - 1) {
                iterableValue += ", ";
            }
            m++;
        }
        return  iterableValue + "}";
    }

    /**
     * Возвращает значение поля типа Map
     * С учетом аннотации MaxAmountOfDisplayedObjects
     *
     * @param field Обрабатываемое поле
     * @return String
     * @throws IllegalAccessException
     */
    public  String getMapValue(Field field) throws IllegalAccessException{

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

    /**
     * Возвращает значение maxAmount
     * из аннотации MaxAmountOfDisplayedObjects
     *
     * @param field Обрабатываемое поле
     * @return int
     */
    public  int getMaxAmount(Field field){
        return field.getAnnotation(MaxAmountOfDisplayedObjects.class).maxAmount();
    }

    /**
     * Возвращает значение метода
     *
     * @param type Тип этого поля
     * @param method Обрабатуемый метод
     * @return String
     */
    public  String getFieldValue(ValueType type, Method method){
        return getFieldValue(null,type,method);
    }

    /**
     * Возвращает значение поля,
     * в зависимости от типа поля
     *
     * @param type Тип этого поля
     * @return String
     */
    public  String getFieldValue(Field field, ValueType type){
        return getFieldValue(field,type,null);
    }

    /**
     * Возвращает значение полей или методов
     *
     * @param field Обрабатываемое поле
     * @param type Тип этого поля
     * @param method Обрабатуемый метод
     * @return String
     */
    public  String getFieldValue(Field field, ValueType type, Method method){

            String fieldValue = "";

                try {
                    switch (type) {
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
                } catch (IllegalAccessException e){
                    fieldValue = null;
                    logger.log(Level.SEVERE, "IllegalAccessException" ,e);
                } catch (InvocationTargetException e){
                    fieldValue = null;
                    logger.log(Level.INFO, "InvocationTargetException ", e);
                } catch (IllegalArgumentException e){
                    fieldValue = null;
                    logger.log(Level.SEVERE, "IllegalArgumentException" , e);
                }

            return fieldValue;
    }

}
