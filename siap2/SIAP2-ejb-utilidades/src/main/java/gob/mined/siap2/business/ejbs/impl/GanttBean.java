/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import com.google.gson.Gson;
import gob.mined.siap2.business.datatype.gantt.DataGantt;
import gob.mined.siap2.business.datatype.gantt.DataGanttTask;
import gob.mined.siap2.business.utils.GanttUtils;
import gob.mined.siap2.business.utils.MetodoAdquisicionUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionTarea;
import gob.mined.siap2.entities.enums.GanttStatus;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes a diagramas de Gantt
 * @author Sofis Solutions
 */
@Stateless(name = "GanttBean")
@LocalBean
public class GanttBean {

    @Inject
    @JPADAO
    private MetodoAdquisicionDAO metodoAdquisicionDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método devuelve un diagrama de Gantt a partir de su id
     * @param idGantt
     * @return 
     */
    public String getGantt(Integer idGantt) {
        try {
            Gantt g = (Gantt) generalDAO.find(Gantt.class, idGantt);
            DataGantt dg = GanttUtils.getDataGantt(g);
            Gson gson = new Gson();
            return gson.toJson(dg);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método regenera un diagrama de Gantt a partir de la fecha de inicio, según el cronograma definido en un método de adquisición.
     * @param idMetodoAdquisicion método de adquisición
     * @param fechaInicio fecha de inicio para el cronograma
     * @return 
     */
    public String regenerarGantt(Integer idMetodoAdquisicion, Date fechaInicio) {
        try {
            MetodoAdquisicion m = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, idMetodoAdquisicion);
            MetodoAdquisicionUtils.ordenarTareasPorInicio(m.getTareas());
            
            DataGantt g = new DataGantt();
            g.setSelectedRow(0);
            g.setCanWrite(true);
            g.setCanWriteOnParent(true);
            g.setTasks(new LinkedList());
            if (fechaInicio == null) {
                fechaInicio = new Date();
            }
            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTime(fechaInicio);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Long timeInicio = cal.getTimeInMillis();

            int levelCount = 0;
            for (MetodoAdquisicionTarea mat : m.getTareas()) {

                DataGanttTask gt = new DataGanttTask();
                int id = m.getTareas().indexOf(mat) + 1;
                gt.setId(String.valueOf(id));
                gt.setName(mat.getNombre());
                gt.setProgress(0);
                gt.setDescription("");
                gt.setLevel(0);
                gt.setHoraFin("");

                
                if (mat.getTipoTarea() != null) {
                    gt.setCode(String.valueOf(mat.getTipoTarea()));
                    if (mat.getTipoTarea() == TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS) {
                        gt.setStatus(GanttStatus.STATUS_FAILED);
                    } else if (mat.getTipoTarea() == TipoTarea.CONTRATO_ORDEN_DE_COMPRA) {
                        gt.setStatus(GanttStatus.STATUS_DONE);
                    }else{
                        gt.setStatus(GanttStatus.STATUS_ACTIVE);
                    }
                }else{
                    gt.setCode("");
                    gt.setStatus(GanttStatus.STATUS_ACTIVE);
                }               

                if (mat.getPredecesora() != null) {
                    int posPrecesora = m.getTareas().indexOf(mat.getPredecesora()) + 1;
                    gt.setDepends(String.valueOf(posPrecesora));
                } else {
                    gt.setDepends("");
                }
                gt.setCanWrite(true);
                Long inicio = timeInicio + (mat.getInicioEnDias() * 24 * 60 * 60 * 1000);
                gt.setStart(inicio);
                gt.setDuration(mat.getDuracionEnDias());
                Long fin = timeInicio + ((mat.getInicioEnDias() + mat.getDuracionEnDias()) * 24 * 60 * 60 * 1000);
                gt.setEnd(fin);
                gt.setStartIsMilestone(false);
                gt.setEndIsMilestone(false);
                gt.setCollapsed(false);

                g.getTasks().add(gt);
                levelCount++;
            }

            Gson gson = new Gson();
            String jsonInString = gson.toJson(g);
            return jsonInString;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método adiciona n días laborales a una fecha.
     * @param date
     * @param diasLaborables
     * @return 
     */
    public Long sumarDias(Calendar date, int diasLaborables) {
        if (diasLaborables < 1) {
            return date.getTimeInMillis();
        }
        Calendar result = (Calendar) date.clone();
        int addedDays = 0;
        while (addedDays < diasLaborables) {
            result.add(Calendar.DATE, 1);
            if (!(result.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || result.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                ++addedDays;
            }
        }

        return result.getTimeInMillis();
    }

    /**
     * Este método devuelve todos los métodos de adquisición que corresponden con un monto dado en un anio dado
     * @param montoGrupo
     * @param anio
     * @return 
     */
    public List<MetodoAdquisicion> getMetodosCumplenRango(BigDecimal montoGrupo, Integer anio) {
        try {
            List<MetodoAdquisicion> l = metodoAdquisicionDAO.getMetodosQueCumplanRango(montoGrupo, anio);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
