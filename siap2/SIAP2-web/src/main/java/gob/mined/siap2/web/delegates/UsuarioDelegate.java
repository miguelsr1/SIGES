/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.datatype.DataRolUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.entities.data.SsRelRolOperacion;
import gob.mined.siap2.entities.data.SsRol;
import gob.mined.siap2.entities.data.SsTokenPassword;
import gob.mined.siap2.entities.data.SsUsuOfiRoles;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.filtros.FiltroUsuario;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de usuarios entre la capa lógica y la de
 * presentación
 *
 * @author Sofis Solutions
 */
@Named
public class UsuarioDelegate implements Serializable {

    @Inject
    private UsuarioBean usuBean;

    public SsUsuario guardarUsuario(SsUsuario editando) throws GeneralException {
        return usuBean.guardarUsuario(editando);
    }

    public void cambiarContrasenia(String codigoUsuario, String nuevaContrasenia) throws GeneralException {
        usuBean.cambiarContrasenia(codigoUsuario, nuevaContrasenia);
    }

    public void cambiarContrasenia(String codigoUsuario, String nuevaContrasenia, String viejaContrasenia) {
        usuBean.cambiarContrasenia(codigoUsuario, nuevaContrasenia, viejaContrasenia);
    }

    public List<String> obtenerOperaciones(String codigoUsuario) {
        return usuBean.obtenerOperaciones(codigoUsuario);
    }

    public List<DataRolUsuario> obtenerOperacionesAgrupadasPorUT(String codigoUsuario) {
        return usuBean.obtenerOperacionesAgrupadasPorUT(codigoUsuario);
    }

    public SsUsuario obtenerSsUsuarioPorCodigo(String codigo) throws GeneralException {
        return usuBean.obtenerSsUsuarioPorCodigo(codigo);
    }

    public void actualizarPermisos(SsRol editando, Set<Integer> relRolToDelate, List<SsRelRolOperacion> relRol) {
        usuBean.actualizarPermisos(editando, relRolToDelate, relRol);
    }

    public List<SsUsuOfiRoles> getUsuOfiRoles(int idUsuario) {
        return usuBean.getUsuOfiRoles(idUsuario);
    }

    public SsRol obtenerRolPorCodigo(String codigo) {
        return usuBean.obtenerRolPorCodigo(codigo);
    }

    public List<UnidadTecnica> getUnidadesTecniasDeUsuario(Integer usuId) {
        return usuBean.getUnidadesTecniasDeUsuario(usuId);
    }

    public List<UnidadTecnica> getUTDeUsuarioConOperacion(Integer idUsuario, String opeCodigo) {
        return usuBean.getUTDeUsuarioConOperacion(idUsuario, opeCodigo);
    }
    public List<UnidadTecnica> getUTDeUsuarioConOperacionByNombre(FiltroUnidadTecnica filtro) {
        return usuBean.getUTDeUsuarioConOperacionByNombre(filtro);
    }

    public List<SsUsuario> getUsuariosConOperacion(String operCD) {
        return usuBean.getUsuariosConOperacion(operCD);
    }

    public List<SsUsuario> getUsusariosPorUnidadTecnica(Integer unidadTecnicaId) {
        return usuBean.getUsusariosPorUnidadTecnica(unidadTecnicaId);
    }

    public void recordarContrasenia(String usuCod, String url) {

        usuBean.recordarContrasenia(usuCod, url);
    }

    public SsTokenPassword obtenerToken(String token) {
        return usuBean.obtenerToken(token);
    }

    public PoliticaContrasenia obtenerPoliticaContrasenia() {
        return usuBean.obtenerPoliticaContrasenia();
    }

    public List<SsUsuario> getUsuariosConNombreEnUT(Integer idUT, String codigo) {
        return usuBean.getUsuariosConNombreEnUT(idUT, codigo);
    }
    
        
    public List<SsUsuario> getUsuariosByFiltro(FiltroUsuario filtro) {
        return usuBean.getUsuariosByFiltro(filtro);
    }
    
    public boolean getUsuarioTienePermisoEnUTConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        return usuBean.getUsuarioTienePermisoEnUTConOperacion(idUT,opeCodigo,codUsuario);
    }
    public boolean getUsuarioTienePermisoEnUTPadreConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        return usuBean.getUsuarioTienePermisoEnUTPadreConOperacion(idUT,opeCodigo,codUsuario);
    }
    
    public boolean getUsuarioTienePermisoEnUTOperativaConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        return usuBean.getUsuarioTienePermisoEnUTOperativaConOperacion(idUT,opeCodigo,codUsuario);
    }
    
}
