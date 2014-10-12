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
 * @return String
 * @throws IllegalAccessException
 * @throws InvocationTargetException
 * @throws InstantiationException
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

            field.setAccessible(true); // Делаем значение метода доступным, независимо от его модификатора приватности

            if (field.isAnnotationPresent(MaxAmountOfDisplayedObjects.class)) {  // Проверка на аннотацию MaxAmountOfDisplayedObjects

                if (field.getType().equals(Map.class)) {
                    fieldValue = getFieldValue(field, ValueType.MAP_VALUE );     // Возвращение значений Map'a

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

            method.setAccessible(true);  // Делаем значение метода доступным, независимо от его модификатора приватности

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

    /**
     * Возвращает имя поля
     *
     * @param field Обрабатываемое поле
     * @return String
     */
    protected String getFieldName(Field field){

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

    /**
     * Возвращает значение поля типа Map
     * С учетом аннотации MaxAmountOfDisplayedObjects
     *
     * @param field Обрабатываемое поле
     * @return String
     * @throws IllegalAccessException
     */
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

    /**
     * Возвращает значение maxAmount
     * из аннотации MaxAmountOfDisplayedObjects
     *
     * @param field Обрабатываемое поле
     * @return int
     */
    protected  int getMaxAmount(Field field){
        return field.getAnnotation(MaxAmountOfDisplayedObjects.class).maxAmount();
    }

    /**
     * Возвращает значение метода
     *
     * @param type Тип этого поля
     * @param method Обрабатуемый метод
     * @return String
     */
    protected  String getFieldValue(ValueType type, Method method){
        return getFieldValue(null,type,method);
    }

    /**
     * Возвращает значение поля,
     * в зависимости от типа поля
     *
     * @param type Тип этого поля
     * @return String
     */
    protected  String getFieldValue(Field field, ValueType type){
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
    protected  String getFieldValue(Field field, ValueType type, Method method){

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
