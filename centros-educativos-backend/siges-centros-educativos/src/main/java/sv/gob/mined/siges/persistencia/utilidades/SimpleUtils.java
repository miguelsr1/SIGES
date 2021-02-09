/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroServicioEducativo;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.daos.ServicioEducativoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgOpcion;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCanton;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;

/**
 *
 * @author Sofis Solutions
 */
public class SimpleUtils {
    
    public static void cargarEntidadesSegunId(SgSede entity, EntityManager em) {
        SgDireccion dir = entity.getSedDireccion();
        if (dir != null){
            if (dir.getDirCanton() != null){
                dir.setDirCanton(em.find(SgCanton.class, dir.getDirCanton().getCanPk()));
            }
            if (dir.getDirMunicipio() != null){
                dir.setDirMunicipio(em.find(SgMunicipio.class, dir.getDirMunicipio().getMunPk()));
            }
            if (dir.getDirDepartamento() != null){
                dir.setDirDepartamento(em.find(SgDepartamento.class, dir.getDirDepartamento().getDepPk()));
            }
            if (dir.getDirZona() != null){
                dir.setDirZona(em.find(SgZona.class, dir.getDirZona().getZonPk()));
            }
        }
        
        if (entity.getSedTipoCalendario() != null){
            entity.setSedTipoCalendario(em.find(SgTipoCalendarioEscolar.class, entity.getSedTipoCalendario().getTcePk()));
        }
        
        if (entity.getSedJornadasLaborales() != null){
            List<SgJornadaLaboral> jor = new ArrayList<>();
            for (SgJornadaLaboral j : entity.getSedJornadasLaborales()){
                jor.add(em.find(SgJornadaLaboral.class, j.getJlaPk()));
            }
            entity.setSedJornadasLaborales(jor);
        }
    }

    //TODO: cambiar em.find por em.getReference
    public static void cargarEntidadesSegunId(SgServicioEducativo entity, EntityManager em, SeguridadBean segBean) {
        try{
            if (entity.getSduPk() != null){
                SgServicioEducativo sdb = em.find(SgServicioEducativo.class, entity.getSduPk());
                entity.setSduVersion(sdb.getSduVersion());
            }else{
                if(entity.getSduNumeroTramite()!=null){
                    ServicioEducativoDAO DAO = new ServicioEducativoDAO(em, segBean);
                    FiltroServicioEducativo fse=new FiltroServicioEducativo();
                    fse.setSduNumeroTramite(entity.getSduNumeroTramite());
                    List<SgServicioEducativo> serEd=DAO.obtenerPorFiltro(fse, null);
                    if(!serEd.isEmpty()){
                        SgServicioEducativo ser=serEd.get(0);
                        entity.setSduPk(ser.getSduPk());
                        entity.setSduVersion(ser.getSduVersion());
                    }
                }
                
            }

            //Si no intercambiamos por dato de la bd, envers crea una nueva revisión de la sede pero solamente con los datos que vienen en el objeto, o sea la pk y la versión.
            //Pasa porque sede tiene servicios por mappedBy
            
            //TODO: lo de la revisión luego del mappedBy se solucionó con @NotAudited. Ver si se puede sacar los em.find()
            if (entity.getSduSede() != null) {
                entity.setSduSede(em.find(SgSede.class, entity.getSduSede().getSedPk()));
            }

            //Grado también tiene mappedBy de servicios
            if (entity.getSduGrado() != null) {
                entity.setSduGrado(em.find(SgGrado.class, entity.getSduGrado().getGraPk()));
            }

            if (entity.getSduProgramaEducativo() != null) {
                entity.setSduProgramaEducativo(em.find(SgProgramaEducativo.class, entity.getSduProgramaEducativo().getPedPk()));
            }

            if (entity.getSduOpcion() != null) {
                entity.setSduOpcion(em.find(SgOpcion.class, entity.getSduOpcion().getOpcPk()));
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
}
