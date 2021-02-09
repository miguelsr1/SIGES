package sv.gob.mined.siges.web.utilidades;

import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author fabricio
 */
public class NumberFormatUtils {

    public static String formatDouble(Double d, Integer precision) {
        DecimalFormat df = null;
        if (precision != null && precision > 0) {
            df = new DecimalFormat("##,##0." + StringUtils.repeat("0", precision));
        } else {
            df = new DecimalFormat("#");
        }
        return df.format(d);
    }

}

