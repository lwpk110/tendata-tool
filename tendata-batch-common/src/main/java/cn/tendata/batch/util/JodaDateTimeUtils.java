package cn.tendata.batch.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public abstract class JodaDateTimeUtils {

    public static DateTime parseDateTime(String text, String... patterns) throws ParseException{
        Date date = DateUtils.parseDate(text, patterns);
        return LocalDate.fromDateFields(date).toDateTimeAtStartOfDay().withZoneRetainFields(DateTimeZone.UTC);
    }
}
