/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.data.SsRelRolOperacion;
import gob.mined.siap2.entities.data.SsTokenPassword;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de acceso a datos de Usuarios.
 * @author Sofis Solutions
 */
@JPADAO
public class UsuarioDAO extends EclipselinkJpaDAOImp<SsUsuario, Integer> {

    /**
     * Este método devuelve un usuario por código de usuario
     * @param codigo código del usuario.
     * @return
     * @throws DAOGeneralException 
     */
    public SsUsuario getUsuarioByCodigo(String codigo) throws DAOGeneralException {

        return (SsUsuario) entityManager.createQuery("SELECT u FROM SsUsuario u WHERE u.usuCod= :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }
    
    /**
     * Este método comprueba si existe un usuario con determinado código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public boolean existeUsuarioByCodigo(String codigo) throws DAOGeneralException {

        List result = entityManager.createQuery("SELECT u "
                + "FROM SsUsuario u WHERE u.usuCod= :codigo")
                .setParameter("codigo", codigo)
                .getResultList();
        return !result.isEmpty();
    }

    /**
     * Este método devuelve todas las operaciones asociadas  a un rol
     * @param roid id del rol
     * @return lista de operaciones del rol
     */
    public List<SsRelRolOperacion> getSsRelRolOperacion(Integer roid) {
        String consulta = "select s from SsRelRolOperacion s where s.relRolOperacionRolId.rolId=:rolId";
        Query query = entityManager.createQuery(consulta);
        query.setParameter("rolId", roid);
        return query.getResultList();
    }

    //retorna los usuarios que tienen una operacion, también permite filtrar por usuario para verificar si un user tiene la operacion
    /**
     * Este método retorna los usuarios que tienen una operación, también permite filtrar por usuario para verificar si un usuario tiene la operación
     * @param opeCodigo código de la operación
     * @param codigoUsuario código del usuario
     * @return lista de todos los usuarios que tienen la operación indicada.
     */
    public List<SsUsuario> getUsuariosConOperacion(String opeCodigo, String codigoUsuario) {
        String query = "SELECT DISTINCT(u) FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + "usuOfiRoles.usuOfiRolesRol IN "
                + " ("
                + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                + " WHERE operacion.opeCodigo= :opeCodigo"
                + ")";

        if (codigoUsuario != null && codigoUsuario.length() > 0) {
            query = query + " AND u.usuCod = :codigoUsuario";
        }

        Query q = entityManager.createQuery(query);

        q.setParameter("opeCodigo", opeCodigo);
        if (codigoUsuario != null && codigoUsuario.length() > 0) {
            q.setParameter("codigoUsuario", codigoUsuario);
        }

        return q.getResultList();
    }
        
    /**
     * retorna las unidades técnicas del usuario con tal operación
     * @param opeCodigo
     * @param codigoUsuario
     * @return 
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacion( Integer idUsuario, String opeCodigo) {
        return entityManager.createQuery("SELECT usuOfiRoles.usuOfiRolesUnidadTecnica FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + "usuOfiRoles.usuOfiRolesRol IN "
                + " ("
                + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                + " WHERE operacion.opeCodigo= :opeCodigo"
                + ")  AND u.usuId = :idUsuario "
                + " AND usuOfiRoles.usuOfiRolesUnidadTecnica IS NOT NULL")
                .setParameter("opeCodigo", opeCodigo)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }
    
    /**
     * retorna las unidades técnicas del usuario con tal operación
     * @param opeCodigo
     * @param codigoUsuario
     * @return 
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacionByNombre(FiltroUnidadTecnica filtro) {
        if(filtro.getNombre() == null) {
            filtro.setNombre("");
        }
        
        return entityManager.createQuery("SELECT usuOfiRoles.usuOfiRolesUnidadTecnica FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + "usuOfiRoles.usuOfiRolesRol IN "
                + " ("
                + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                + " WHERE operacion.opeCodigo= :opeCodigo"
                + ")  AND u.usuCod = :usuCod "
                + " AND usuOfiRoles.usuOfiRolesUnidadTecnica IS NOT NULL")
                .setParameter("opeCodigo", filtro.getOperacion())
                .setParameter("usuCod", filtro.getCodUsuario())
               // .setParameter("query", "%"+filtro.getNombre().trim().toLowerCase()+"%")
                .setMaxResults(filtro.getMaxResults().intValue())
                .getResultList();
    }
    
    
    /**
     * retorna las unidades técnicas del usuario con tal operación
     * @param opeCodigo
     * @param codigoUsuario
     * @return 
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacionByNombre(List<Integer> ids, String query, Integer maxResult) {
        List<UnidadTecnica> resultado = new ArrayList();
        if(query == null) {
            query = "";
        }
        if(ids == null || ids.isEmpty() || ids.size() == 0) {
            return new ArrayList();
        }
        String idsString = "";
        Integer count = 1;
        for(Integer id : ids) {
            if(count > 1 && ((count - 1) < (ids.size()))) {
                idsString += ",";
            }
            
            idsString += id;
            count ++;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(
            "SELECT distinct t1.uni_id,t1.uni_codigo,t1.uni_nombre,t1.uni_padre,t1.uni_operativa,t1.uni_version "+
            "FROM " + Constantes.SCHEMA_SIAP2 +".ss_unidad_tecnica t1  "+
           // "INNER JOIN " + Constantes.SCHEMA_SIAP2 +".ss_unidad_tecnica t2 ON (t2.uni_id=t1.uni_padre)  "+
           //   "WHERE 1 = 1 and t2.uni_operativa=true and t1.uni_padre in (" +idsString + ") and t1.uni_nombre_busqueda like ('%"+query.trim().toLowerCase()+"%') order by t1.uni_nombre asc LIMIT " + maxResult);
           "WHERE (t1.uni_padre in (" +idsString + ") and t1.uni_nombre_busqueda like ('%"+query.trim().toLowerCase()+"%') and t1.uni_operativa=true and t1.uni_padre is not null)"
                    + " or (t1.uni_id in (" +idsString + ") and t1.uni_nombre_busqueda like ('%"+query.trim().toLowerCase()+"%') and t1.uni_padre is not null) order by t1.uni_nombre asc LIMIT " + maxResult);
            Query q = entityManager.createNativeQuery(builder.toString());
           // q.setParameter(1, idsString);
           // q.setParameter(2, "%"+query.trim().toLowerCase()+"%");
           // q.setParameter(3, maxResult);
        
        UnidadTecnica tp;
        for(Object obj : q.getResultList()){
            Object[] obar = (Object[]) obj;
            tp = new UnidadTecnica();
            
            Integer idUnidad = null;
            Integer version = null; 
            if(obar[0] != null) {
                idUnidad = Integer.parseInt(String.valueOf(obar[0]));
            }
            if(obar[5] != null) {
                version = Integer.parseInt(String.valueOf(obar[5]));
            }
            tp.setId(idUnidad);
            tp.setCodigo((String) obar[1]);
            tp.setNombre((String) obar[2]);
            //tp.setPadre(tp);
            tp.setUnidadOperativa((Boolean) obar[4]);
            tp.setVersion(version);
            resultado.add(tp);
        }
        
        return resultado;
    }
    
    public List<SsTokenPassword> getTokenPassword(String token){
        String query = "SELECT u FROM SsTokenPassword u WHERE u.token = :token";
        Query q = entityManager.createQuery(query);
        q.setParameter("token", token);
        return q.getResultList();
    }

    /**
     * Este método devuelve la lista de usuarios con un código dado y una id de unidad técnica
     * @param idUT id de la unidad técnica
     * @param codigoUsuario código del usuario
     * @return lista de usuarios
     */
    public List<SsUsuario> getUsuariosConUT(Integer idUT, String codigoUsuario) {
        String query = "SELECT DISTINCT(u) FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + "usuOfiRoles.usuOfiRolesUnidadTecnica.id = :idUT ";

        if (codigoUsuario != null && codigoUsuario.length() > 0) {
            query = query + " AND u.usuCod = :codigoUsuario";
        }

        Query q = entityManager.createQuery(query);

        q.setParameter("idUT", idUT);
        if (codigoUsuario != null && codigoUsuario.length() > 0) {
            q.setParameter("codigoUsuario", codigoUsuario);
        }

        return q.getResultList();
    }

    
    /**
     * Este método devuelve una lista de usuarios que tienen algún rol en la unidad técnica indicada y con el código indicado
     * @param idUT id de la unidad técnica
     * @param codigo subcadena en el nombre del usuario
     * @return
     * @throws DAOGeneralException 
     */
    public List<SsUsuario> getUsuariosConNombreEnUT(Integer idUT, String codigo) throws DAOGeneralException {
        return entityManager.createQuery("SELECT u FROM SsUsuario u , u.ssUsuOfiRolesCollection roles "
                + "WHERE roles.usuOfiRolesUnidadTecnica.id =:idUT "
                + "AND (u.usuCod like :codigo OR u.usuPrimerApellido like :codigo OR u.usuPrimerNombre like :codigo OR u.usuSegundoNombre like :codigo OR u.usuSegundoApellido like :codigo)")
                 .setParameter("idUT", idUT )
                .setParameter("codigo", "%" + codigo + "%" )
                .getResultList();
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Este método devuelve la lista de usuarios con un que tienen una operación en una UT
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public List<SsUsuario> getUsuariosEnUTConOperacion(Integer idUT, String opeCodigo) {
        String query = "SELECT DISTINCT(u) FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + " usuOfiRoles.usuOfiRolesUnidadTecnica.id = :idUT "
                + " AND usuOfiRoles.usuOfiRolesRol IN "
                + " ("
                + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                + " WHERE operacion.opeCodigo= :opeCodigo"
                + " )";

        Query q = entityManager.createQuery(query);
        q.setParameter("idUT", idUT);
        q.setParameter("opeCodigo", opeCodigo);
        return q.getResultList();
    }
    
    /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        String query = "SELECT 1 FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE 1=1";
                if(idUT != null && idUT > 0) {
                    query += " and usuOfiRoles.usuOfiRolesUnidadTecnica.id = :idUT";
                }
                if(codUsuario != null && StringUtils.isNotBlank(codUsuario)) {
                    query += " and u.usuCod = :codigoUsuario";
                }
               //query += " usuOfiRoles.usuOfiRolesUnidadTecnica.id = :idUT and u.usuCod = :codigoUsuario "
               if(opeCodigo != null && StringUtils.isNotEmpty(opeCodigo)) {
                   query += " AND usuOfiRoles.usuOfiRolesRol IN "
                    + " ("
                    + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                    + " WHERE operacion.opeCodigo= :opeCodigo"
                    + " )";
               }
                

        Query q = entityManager.createQuery(query);
        if(idUT != null && idUT > 0) {
             q.setParameter("idUT", idUT);
        }
        if(codUsuario != null && StringUtils.isNotBlank(codUsuario)) {
            q.setParameter("codigoUsuario", codUsuario);
        }
        if(opeCodigo != null && StringUtils.isNotEmpty(opeCodigo)) {
            q.setParameter("opeCodigo", opeCodigo);
        }
        
        List listado = q.getResultList();
        return !listado.isEmpty();

    }
    
   /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTPadreConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
        filtro.setCodUsuario(codUsuario);
        filtro.setOperacion(opeCodigo);
        filtro.setMaxResults(100L);
        List<UnidadTecnica> listadoRaiz = getUTDeUsuarioConOperacionByNombre(filtro);
        List<Integer> listaIds = new ArrayList();
       // listaIds.add(idUT);
        Boolean permiso = Boolean.FALSE;    
        for(UnidadTecnica ut:listadoRaiz) {
            if(idUT.equals(ut.getId())) {
                return true;
            }
            if(!listaIds.contains(ut.getId())) {
                listaIds.add(ut.getId());
            }
            if(ut.getPadre() != null && ut.getPadre().getId() != null) {
                if(!listaIds.contains(ut.getPadre().getId())) {
                    listaIds.add(ut.getPadre().getId());
                }
            }
        }
        
        if(listaIds == null || listaIds.isEmpty() || listaIds.size() == 0) {
            return false;
        }
        String idsString = "";
        Integer count = 1;
        for(Integer id : listaIds) {
            if(count > 1 && ((count - 1) < (listaIds.size()))) {
                idsString += ",";
            }
            
            idsString += id;
            count ++;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT distinct t1.uni_id FROM " + Constantes.SCHEMA_SIAP2 +".ss_unidad_tecnica t1 WHERE t1.uni_padre in (" +idsString + ")");
        Query q = entityManager.createNativeQuery(builder.toString());
           
        for(Object obj : q.getResultList()){
            //Object obar =  obj;
          
            Integer idUnidad = null;
            if(obj!= null) {
                idUnidad = Integer.parseInt(String.valueOf(obj));
            }
            if(idUnidad != null && idUnidad.equals(idUT)) {
                permiso = Boolean.TRUE;
                break;
            }
        }
        
        return permiso;
    }
    /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTOperativaConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        String query = "SELECT DISTINCT(u) FROM SsUsuario u, u.ssUsuOfiRolesCollection usuOfiRoles WHERE "
                + " usuOfiRoles.usuOfiRolesUnidadTecnica.id = :idUT and usuOfiRoles.usuOfiRolesUnidadTecnica.unidadOperativa=true and u.usuCod = :codigoUsuario "
                + " AND usuOfiRoles.usuOfiRolesRol IN "
                + " ("
                + " SELECT relRolOpe.relRolOperacionRolId FROM SsRelRolOperacion relRolOpe, relRolOpe.relRolOperacionOperacionId operacion "
                + " WHERE operacion.opeCodigo= :opeCodigo"
                + " )";

        Query q = entityManager.createQuery(query);
        q.setParameter("idUT", idUT);
        q.setParameter("codigoUsuario", codUsuario);
        q.setParameter("opeCodigo", opeCodigo);
        List listado = q.getResultList();
        return !listado.isEmpty();

    }
    
    
    /**
     * Este método devuelve la lista de usuarios cuya unidadTecnica sea la seleccionada
     * 
     * @param idUT id de la unidad técnica
     * @return lista de usuarios
     */
    public List<SsUsuario> getUsuariosPorIdUnidadTenica(Integer idUT) {
        Query q = entityManager.createNativeQuery(
                "select distinct t1.usu_id, t1.usu_cod, t1.usu_correo_electronico,t1.usu_telefono from siap2.ss_usuario t1 inner join siap2.ss_unidad_tecnica t2 "
                + "on t1.usu_id = t2.uni_usuario "
                + "or t1.usu_id = t2.uni_padre "
                + "or t2.uni_padre  is null "
                + "where (t2.uni_id  = ?1 or uni_id = ?2)");
        q.setParameter("1", idUT);
        q.setParameter("2", idUT);
        List<Object[]> listaUsuarios = q.getResultList();
        SsUsuario usuario;
        List<SsUsuario> listaUsuariosEncontrados  = new ArrayList<SsUsuario>();
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
            for (Object[] u : listaUsuarios) {
                usuario = new SsUsuario();
                usuario.setUsuId(((Long) u[0]).intValue());
                usuario.setUsuCod(((String) u[1]));
                usuario.setUsuCorreoElectronico(((String) u[2]));
                usuario.setUsuTelefono(((String) u[3]));
                listaUsuariosEncontrados.add(usuario);
            }
        }
        return listaUsuariosEncontrados;
    }

    
    
}
