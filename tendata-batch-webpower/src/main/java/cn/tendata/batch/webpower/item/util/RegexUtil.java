package cn.tendata.batch.webpower.item.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/29.
 */
public class RegexUtil {
    public static void main(String[] args) {
        String testStr ="\"tendata_01\",\"d4277f61-f7ff-4cb5-8d7b-4b8a14b5ed45\",\"HardBounce final\",\"mail.oled-info.com@oled-info.com\",\"2017-08-28 12:14:30\",\"\",\"\",\"Hard bounce. Status: 5.0.0 (Other undefined status). Diagnostic-Code: 550 No Such User Here\"\" Final-Recipient: rfc822; mail.oled-info.com@oled-info.com Action: failed. Moved to Hardbounce group, as this bounce is a result of the 1st mailing sent to this recipient in this campaign.\",\"\",\"\"\n";
        int characterPosition = getCharacterPosition(testStr,15);
        int characterPositionFromLast = getCharacterPositionFromLast(testStr, 4);
        System.out.println(characterPosition);
        System.out.println(characterPositionFromLast);
        String front = testStr.substring(0,characterPosition+1);
        String midle = testStr.substring(characterPosition+1,characterPositionFromLast-1).replaceAll("\"","\\\\\"");
        String end = testStr.substring(characterPositionFromLast-1);
        String whole = front + midle+end;
        System.out.println(whole);

    }

    public static int getCharacterPosition(String string,int fromIndex){
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile("\"").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if(mIdx == fromIndex){
                break;
            }
        }
        return slashMatcher.start();
    }

    public static int getCharacterPositionFromLast(String testStr ,int fromLastIndex ){
        char[] source = testStr.toCharArray();
        int length = source.length;
            for(int index = length;index >=0;index--){
                char c1 = source[index-1];
                if(c1=='"'){
                    if(fromLastIndex ==0){
                        return index;
                    }
                    fromLastIndex --;
                }
            }
        return 0;

    }
}
