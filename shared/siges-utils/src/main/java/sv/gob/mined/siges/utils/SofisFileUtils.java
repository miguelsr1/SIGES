/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.utils;

/**
 *
 * @author Sofis Solutions
 */
public class SofisFileUtils {

    public static String getPathFromPk(Long pk) {
        String path = pk.toString();
        if (path.length() < 12){
            Integer length = path.length();
            for (int i = 0; i <  (12 - length); i++){
                path = "0" + path;
            }
        } else {
            path = path.substring(path.length() - 12);
        }
        path = path.substring(0, 3) + "/" + path.substring(3, 6) + "/" + path.substring(6, 9) + "/" + pk;
        return path;
    }

}
