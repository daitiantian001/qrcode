package com.ddbes.qrcode.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daitian on 2018/5/31.
 */
public class DateKit {

    /**
     * 获取当前时间
     * @return
     */
    public static Date now(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取几天后时间
     * @param day
     * @return
     */
    public static Date dayAfter(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,day);
        return calendar.getTime();
    }

    public static Date getNextStartDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        return date;
    }
}
