package com.barran.sample.mdtest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.Test;

/**
 * 测试新的时间库功能
 *
 * Created by tanwei on 2017/3/30.
 */

public class TimeTest {

    @Test
    public void testDate(){
        LocalDate date = LocalDate.parse("2017-3-30");// 默认解析格式，如果解析20170330，则会解析成20170330-01-01
        print(date);

        print(date.withDayOfWeek(7));// 得到2017-4-2，即7表示周日
    }

    @Test
    public void testTime(){
        LocalTime time = LocalTime.now();
        print(time);
        print(time.plusHours(2));
        print(time.plusHours(20));
//        16:42:33.837
//        18:42:33.837
//        12:42:33.837

//        LocalTime timeParsed = LocalTime.parse("8:20");
        LocalTime timeParsed = LocalTime.parse("18:20");
        print(timeParsed);
        // 08:20:00.000
    }

    @Test
    public void testDateTime(){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

//        LocalDateTime dt1 = LocalDateTime.of(2014, Month.JUNE, 10, 20, 30);
//        LocalDateTime dt2 = LocalDateTime.of(date, time);
//        LocalDateTime dt3 = date.atTime(20, 30);
//        LocalDateTime dt4 = date.atTime(time);
    }

    @Test
    public void testInstant(){
        Instant instant = Instant.now();
        print(instant);

        int year = instant.get(DateTimeFieldType.year());
        print(year);

        DateTime dateTime = instant.toDateTime();
        print(dateTime);
    }

    @Test
    public void testPeriod(){
        Period period = Period.hours(1);
        print(period);

        Duration duration = Duration.millis(1567);
        print(duration);

        LocalDateTime dateTime = LocalDateTime.now();
        print(dateTime);
        print(dateTime.plus(period));

        print(dateTime.plus(duration));
    }

    @Test
    public void testFormat(){
    }

    private void print(Object str) {
        System.out.println(str);
    }
}
