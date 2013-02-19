/**
 * 
 */
package jabara.it_inoculation_questions.util;

import jabara.general.ArgUtil;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author jabaraster
 */
public final class DateUtil {

    private static final int TZ_ASIA_TOKYO_RAW_OFFSET = TimeZone.getTimeZone("Asia/Tokyo").getRawOffset(); //$NON-NLS-1$
    private static final int TZ_DEFAULT_RAW_OFFSET    = TimeZone.getDefault().getRawOffset();

    private DateUtil() {
        //
    }

    /**
     * 任意の日時を東京時刻に変換します.
     * 
     * @param pSource 変換前日時.
     * @return 変換後日時.
     */
    public static Date convertAsiaTokyo(final Date pSource) {
        ArgUtil.checkNull(pSource, "pSource"); //$NON-NLS-1$
        return new Date(pSource.getTime() - TZ_DEFAULT_RAW_OFFSET + TZ_ASIA_TOKYO_RAW_OFFSET);
    }

}
