package cn.tendata.batch.webpower.item;

import cn.tendata.batch.webpower.item.util.RegexUtil;
import org.springframework.util.StringUtils;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/29.
 */
public class WebpowerCsvItemRecordLineHandler implements CsvItemRecordLineHandler {
    @Override
    public String handle(String line) {
        int fromCharactPosition = RegexUtil.getCharacterPosition(line,15);
        int characterPositionFromLast = RegexUtil.getCharacterPositionFromLast(line, 4);

        String midleDesc = line.substring(fromCharactPosition+1,characterPositionFromLast-1);
        if(StringUtils.hasText(midleDesc)){
            String front = line.substring(0,fromCharactPosition+1);
            String midle = midleDesc.replaceAll("\"","\\\\\"");
            String end = line.substring(characterPositionFromLast-1);
            return front+midle+end;
        }
        return line;
    }
}
