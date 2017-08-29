package cn.tendata.batch.webpower.item.util;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/28.
 */
public class WebpowerCnToNldStatusUtils {
    /**
     * webpower 邮件状态字典 v1.0
     */
    public static final String SENT_SUCCESS_CN = "SENT";
    public static final String OPEN_CN = "OPEN";
    public static final String CLICK_CN = "CLICK";
    public static final String UN_SUBSCRIBE_CN = "UNSUBSCRIBE";
    public static final String SOFT_BOUNCE_1X_CN = "SOFTBOUNCE 1X";
    public static final String SOFT_BOUNCE_2X_CN = "SOFTBOUNCE 2X";
    public static final String SOFT_BOUNCE_FINAL_CN = "SOFTBOUNCE FINAL";
    public static final String HARD_BOUNCE_FINAL_CN = "HARDBOUNCE FINAL";
    public static final String SPAM_COMPLAINT_CN = "SPAMCOMPLAINT";

    /**
     * webpower 邮件状态字典 v1.1
     */
    public static final String SENT_SUCCESS = "SENDOUT";
    public static final String OPEN = "OPEN";
    public static final String CLICK = "CLICK";
    public static final String UN_SUBSCRIBE = "UNSUBSCRIBER";
    public static final String SOFT_BOUNCE_1X = "SOFTBOUNCE1X";
    public static final String SOFT_BOUNCE_2X = "SOFTBOUNCE2X";
    public static final String SOFT_BOUNCE_FINAL = "SOFTBOUNCE3X";
    public static final String HARD_BOUNCE_FINAL = "HARDBOUNCE3X";
    public static final String SPAM_COMPLAINT = "SPAMCOMPLAINT";

    public static String transform(String item) {
        switch (item) {
            case SENT_SUCCESS_CN:
                return SENT_SUCCESS;
            case OPEN_CN:
                return OPEN;
            case CLICK_CN:
                return CLICK;
            case UN_SUBSCRIBE_CN:
                return UN_SUBSCRIBE;
            case SOFT_BOUNCE_1X_CN:
                return SOFT_BOUNCE_1X;
            case SOFT_BOUNCE_2X_CN:
                return SOFT_BOUNCE_2X;
            case SOFT_BOUNCE_FINAL_CN:
                return SOFT_BOUNCE_FINAL;
            case HARD_BOUNCE_FINAL_CN:
                return HARD_BOUNCE_FINAL;
            case SPAM_COMPLAINT_CN:
                return SPAM_COMPLAINT;
             default:
                 return null;
        }
    }
}
