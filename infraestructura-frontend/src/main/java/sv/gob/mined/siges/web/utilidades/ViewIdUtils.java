/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.web.excepciones.BusinessException;

public class ViewIdUtils {

    private static final Logger LOGGER = Logger.getLogger(ViewIdUtils.class.getName());

    public static void generateViewId(ViewDto newObject, List<? extends ViewDto> objects) {
        if (newObject.getId() == null) {
            Long maxId = 0L;
            if (objects != null && !objects.isEmpty()) {
                ViewDto ult = objects.stream()
                        .filter(f -> f.getId() != null)
                        .collect(Collectors.maxBy(Comparator.comparing(f -> f.getId())))
                        .orElse(null);
                maxId = ult != null ? ult.getId() : 0L;
            }
            newObject.setId(++maxId);
            newObject.setIdForView(Boolean.TRUE);
        }
    }

    public static void generateViewsId(List<? extends ViewDto> objects) {
        Long maxId = 0L;
        if (!objects.isEmpty()) {
            ViewDto ult = objects.stream()
                    .filter(f -> f.getId() != null)
                    .collect(Collectors.maxBy(Comparator.comparing(f -> f.getId())))
                    .orElse(null);
            maxId = ult != null ? ult.getId() : 0L;
        }
        for (ViewDto v : objects) {
            if (v.getId() == null) {
                v.setId(++maxId);
                v.setIdForView(Boolean.TRUE);
            }
        }
    }

    //No recursivo
    public static void clearChildViewIds(Object o) throws BusinessException {
        try {
            for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                field.setAccessible(true);
                if (Collection.class.isAssignableFrom(field.getType())) {
                    Object value = field.get(o);
                    if (value != null) {
                        for (Object co : Collection.class.cast(value)) {
                            if (ViewDto.class.isAssignableFrom(co.getClass())) {
                                ViewDto vdto = ((ViewDto) co);
                                if (vdto.getIdForView() != null && vdto.getIdForView()) {
                                    vdto.setId(null);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new BusinessException("Error al limpiar ids de objeto");
        }
    }

}
