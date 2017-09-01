package cn.tendata.batch.webpower.item;

import cn.tendata.batch.webpower.item.util.RegexUtil;
import org.springframework.util.StringUtils;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/29.
 */
public class WebpowerCsvItemRecordLineHandler implements CsvItemRecordLineHandler {


    private static String DEFAULT_CSV_SEPARATE_CHARACTER =",";

    private static String DEFAULT_IGNORE_CHARACTER = "\"";


    private String delimiter =  DEFAULT_CSV_SEPARATE_CHARACTER;

    private String ignoreCharacter = DEFAULT_IGNORE_CHARACTER;


    @Override
    public String handle(String line) {

        String cleanLine = line.replaceAll(ignoreCharacter,"");

        int CharacterPositionAsc = RegexUtil.getCharacterPosition(delimiter,cleanLine,7);
        int characterPositionDesc = RegexUtil.getCharacterPositionFromLast(delimiter,line, 1);
        String midleDesc = line.substring(CharacterPositionAsc +1, characterPositionDesc -1);

        if(StringUtils.hasText(midleDesc)){
            String front = line.substring(0, CharacterPositionAsc +1);
            String end = line.substring(characterPositionDesc -1);
            return front + midleDesc + end;
        }
        return cleanLine;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setIgnoreCharacter(String ignoreCharacter) {
        this.ignoreCharacter = ignoreCharacter;
    }
}
