package com.ddbes.qrcode.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daitian on 2018/5/31.
 */
public class ConvertKit {
    public static final Map obj2map(Object o) {
        Map map = new HashMap();
        Field[] fields = o.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        try {
            for (Field field : fields) {
                if(field.get(o)!=null){
                    map.put(field.getName(), field.get(o));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static final List props(Class clazz){
        List list = new ArrayList();
        Field[] fields = clazz.getDeclaredFields();
        Field.setAccessible(fields, true);
        for (Field field : fields) {
            list.add(field.getName());
        }
        return list;
    }

    public static String[] objectArray2StrArray(Object[] objArray){
        String[] strArray = new String[objArray.length];
        for (int i = 0; i <objArray.length ; i++) {
            strArray[i]=objArray[i].toString();
        }
        return strArray;
    }


}
