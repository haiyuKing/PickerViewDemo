package com.why.project.pickerviewdemo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Used 实现日期和字符串之间的转换以及日期的相关操作
 * @注意 new SimpleDateFormat的构造函数中必须含有Locale.CHINA或者Locale.getDefault()
 *  SimpleDateFormat format = new SimpleDateFormat(parse,Locale.CHINA);
 *  SimpleDateFormat format = new SimpleDateFormat(parse,Locale.getDefault());
 *  解决黄色感叹号：http://www.blogchen.com/archives/392.html
 */
public class DateTimeHelper {
	
    /**
     * 将未指定格式的字符串转换成日期类型
     * @param date - 20151123
     * @return Mon Nov 23 00:00:00 CST 2015
     */
    public static Date parseStringToDate(String date) throws ParseException {
        Date result = null;
        String parse = date;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");
        SimpleDateFormat format = new SimpleDateFormat(parse, Locale.CHINA);
        result = format.parse(date);
        return result;
    }
    /**
     * 将日期以指定格式输出
     * @param date - new Date()
     * @param format - "yyyy-MM-dd"
     * @return 2015-11-23
     */
    public static String formatToString(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.CHINA);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 将日期格式的字符串以指定格式输出
     * @param date - "2015/11/23"
     * @param format - "yyyy-MM-dd"
     * @return 2015-11-23
     */
    public static String formatToString(String date, String format) {
        try {
            Date dt = DateTimeHelper.parseStringToDate(date);
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.CHINA);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 将时间戳转化为固定格式的日期 （yyyy-MM-dd HH:mm）
     * @param time - new Date().getTime()+""
     * @return 2015-11-23 14:05
     */
    public static String getStrTime(String time) {
        if (time.trim().equals("") || time == null)
            return "";
        String strTime = null;
        time = time.substring(0, 10);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.CHINA);
        long ltime = Long.valueOf(time);
        strTime = mFormat.format(new Date(ltime * 1000L));
        return strTime;
    }
    /**
     * 将时间戳转化为指定格式日期
     * @param time - new Date().getTime()+""
     * @param format - "yyyy/MM/dd hh:mm:ss"
     * @return 2015/11/23 02:05:36
     */
    public static String getStrTime(String time, String format) {
        if (time.trim().equals("") || time == null || time.equals("null"))
            return "";
        String strTime = null;
        time = time.substring(0, 10);
        SimpleDateFormat mFormat = new SimpleDateFormat(format, Locale.CHINA);
        long ltime = Long.valueOf(time);
        strTime = mFormat.format(new Date(ltime * 1000L));
        return strTime;
    }
    
    /**
     * 当前时间提前一个月
     * @return 2015-10-23
     */
    public static String getPerMonthDate(){
    	Date date = new Date();//当前日期
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//格式化对象
    	Calendar calendar = Calendar.getInstance();//日历对象
    	calendar.setTime(date);			 		   //设置当前日期
    	calendar.add(Calendar.MONTH, -1);		   //月份减一
    	return sdf.format(calendar.getTime());
    }
    
    /**
     * 当前时间延后一个月
     * @return 2015-12-23
     */
    public static String getLastMonthDate(){
    	Date date = new Date();//当前日期
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);//格式化对象
    	Calendar calendar = Calendar.getInstance();//日历对象
    	calendar.setTime(date);			 		   //设置当前日期
    	calendar.add(Calendar.MONTH, 1);		   //月份加一
    	return sdf.format(calendar.getTime());
    }
    
    /**
     * 计算时间差（单位：天）
     * @param startDate - "2015-11-23"
     * @param endDate - "2015-12-20"
     * @return 27
     */
    public static String getTimeDifferenceDate(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date d1 = df.parse(endDate);
            Date d2 = df.parse(startDate);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days + "";
        } catch (Exception e) {
        }
        return "";
    }
    /**
     * 计算两个日期型的时间相差多少时间
     * @param startDate - new Date()
     * @param endDate - DateTimeHelper.parseStringToDate("2015-12-20")
     * @return 3周前
     */
    public static String twoDateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000l)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 7 * 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 4 * 7 * 24 * 60 * 60 * 1000l) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            return sdf.format(startDate);
        }
    }
    /**
     * 判断两个日期的大小
     * 
     * @param DATE1 -- "2015-11-23
     * @param DATE2 --"2015-12-20"
     * @return true 默认第一个比第二个时间小，所以如果第一个大于第二个，返回false
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        //DateFormat df = new SimpleDateFormat();
    	
        //getDateInstance方法——获取日期格式器 2015-11-23
        //getDateTimeInstance方法——获取日期/时间格式器  2015-11-23 09:37:50
        //getInstance方法用于获取为日期和时间使用SHORT风格的默认日期/时间格式器
    	DateFormat df = DateFormat.getDateInstance();
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                return true;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    
    /**
     * 将时间time字符串转化为Date对象
     * @param strTime - 1462772155198
     * @return Date
     */
    public static Date parseFormatTimeToDate(String strTime) {
    	
    	Date date = new Date();
    	try{
    		date.setTime(Long.parseLong(strTime));
    	}
        catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }
    	
    	return date;
    }
    
    /**
     * 获取格式化后Date的Time值
     * @param dateStr 2015-11-23
     * @return 1448208000000
     * */
    public static long getParseDateTime(String dateStr){
    	long daterTime = 0;
    	DateFormat df = DateFormat.getDateInstance();
    	try {
            Date dt1 = df.parse(dateStr);
            daterTime = dt1.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return daterTime;
    }
    
    /**
     * 当前时间延后一个星期
     * @param startDate 2016-03-16
     * @return 2015-03-23
     */
    public static String getLastWeekDate(String startDate){
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    	String endDate = "";
        try {
            Date date = df.parse(startDate);
            long startTime = date.getTime();
            long endTime = startTime + 7 * 24 * 60 * 60 * 1000;
            endDate = getStrTime(endTime+"","yyyy-MM-dd");
        } catch (Exception e) {
        }
        return endDate;
    }
    
    /**
     * 判断是否同一天
     * @param date 
     * @param sameDate 
     * @return boolean
     */
    public static boolean isSameDay(Date date, Date sameDate) {
    	if (null == date || null == sameDate) {
    		return false;
    	}
    	Calendar nowCalendar = Calendar.getInstance();
    	nowCalendar.setTime(sameDate);
    	Calendar dateCalendar = Calendar.getInstance();
    	dateCalendar.setTime(date);
    	if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
    			&& nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
    			&& nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {
    		return true;
    	}
    	return false;
    }
}