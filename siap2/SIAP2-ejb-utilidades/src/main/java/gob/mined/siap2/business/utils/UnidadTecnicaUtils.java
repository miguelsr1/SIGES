/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class UnidadTecnicaUtils {

    private static Gson gson = new Gson();
    private static Type listType = new TypeToken<List<Integer>>() {
    }.getType();

    public static List<Integer> getUTTienenAcceso(UnidadTecnica ut) {
        if (TextUtils.isEmpty(ut.getListaUTinenAcceso())) {
            return new LinkedList<>();
        }
        return gson.fromJson(ut.getListaUTinenAcceso(), listType);
    }

    public static void setUTTienenAcceso(UnidadTecnica ut, List<Integer> idsUT) {
        ut.setListaUTinenAcceso(gson.toJson(idsUT));
    }

    /**
     * chequea si la lista de unidades técnicas de un usuario es tiene acceso a
     * otra
     *
     * @param unidadeDelUsuario
     * @param utCheuqeaAcceso
     * @return
     */
    public static boolean tieneAccesoAUT(List<UnidadTecnica> unidadeDelUsuario, UnidadTecnica utCheuqeaAcceso) {
        List<Integer> utDisponibles = getUTTienenAcceso(utCheuqeaAcceso);
        utDisponibles.add(utCheuqeaAcceso.getId());

        for (UnidadTecnica utUsuario : unidadeDelUsuario) {
            if (utUsuario != null) {
                if (utDisponibles.contains(utUsuario.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
