package cn.tendata.batch.northamerica.item.data;

import java.text.ParseException;
import java.util.Map;

import cn.tendata.batch.util.JodaDateTimeUtils;

public final class MongoQueryUtils {

    private static final String JOB_PARAMS_QUERY = "query";
    private static final String JOB_PARAMS_DATE_QUERY = "dateQuery";
    
    private static final String PARSE_PARTERN = "yyyyMMdd";
    
    private MongoQueryUtils(){
        
    }
    
    public static String createQuery(Map<String, Object> jobParameters) throws ParseException{
        String query = "{}";
        if(jobParameters.containsKey(JOB_PARAMS_QUERY)){
            query = (String)jobParameters.get(JOB_PARAMS_QUERY);
        }
        if(jobParameters.containsKey(JOB_PARAMS_DATE_QUERY)){
            String dateQuery = (String)jobParameters.get(JOB_PARAMS_DATE_QUERY);
            query = parseQuery(dateQuery);
            if(query == null){
                throw new IllegalStateException(
                        "parse date query error, pattern [field:begin-end], dateQuery: " + dateQuery);
            }
        }
        return query;
    }
    
    private static String parseQuery(String dateQuery) throws ParseException{
        String query = null;
        if(dateQuery.indexOf(':') > 1){
            String[] parts = dateQuery.split(":");
            if(parts.length == 2){
                String fieldPart = parts[0];
                String datePart = parts[1];
                if(datePart.indexOf('-') > 1){
                    String[] rangeParts = datePart.split("-");
                    query = String.format("{%s : {$gte : {$date:'%s'}, $lte : {$date:'%s'}}}", 
                            fieldPart,
                            JodaDateTimeUtils.parseDateTime(rangeParts[0], PARSE_PARTERN),
                            JodaDateTimeUtils.parseDateTime(rangeParts[1], PARSE_PARTERN));
                }
                else{
                    query = String.format("{%s : {$date: '%s'}}", 
                            fieldPart,
                            JodaDateTimeUtils.parseDateTime(datePart, PARSE_PARTERN));
                }
            }
        }
        return query;
    }
}
