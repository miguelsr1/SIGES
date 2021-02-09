/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import com.google.gson.Gson;
import gob.mined.siap2.business.datatype.gantt.DataGantt;
import gob.mined.siap2.business.ejbs.impl.ReporteBean;
import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.GanttUtils;
import gob.mined.siap2.business.utils.PACUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsLock;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.NodoPlantillaDeInsumos;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ReportePAC;
import gob.mined.siap2.entities.data.impl.ReportePEP;
import gob.mined.siap2.entities.enums.EstadoGrupoPAC;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.InsumoDAO;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.PACDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.PlantillaDeInsumosDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos correspondientes a la gestión del PAC.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "PACBean")
@LocalBean
public class PACBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private MetodoAdquisicionDAO metodoAdquisicionDAO;
    @Inject
    @JPADAO
    private POADAO poaDAO;

    @Inject
    @JPADAO
    private PlantillaDeInsumosDAO plantillaDeInsumosDAO;
    @Inject
    @JPADAO
    private PACDAO pacdao;
    @Inject
    @JPADAO
    private InsumoDAO insumodao;
    @Inject
    private ReporteBean reporteBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método devuelve un PAC dado su id.
     *
     * @param idPAC
     * @return
     */
    public PAC getPAC(Integer idPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            for (PACGrupo g : pac.getGrupos()) {
                g.getInsumos().isEmpty();
            }
            pac.initInsumos();
            return pac;
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
     * Este método devuelve un PAC con el flujo asociado.
     *
     * @param idPAC
     * @return
     */
    public PAC getPACForFCM(Integer idPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            for (PACGrupo g : pac.getGrupos()) {
                for (POInsumos i : g.getInsumos()) {
                    i.getFlujosDeCajaAnio().isEmpty();
                }
            }
            return pac;
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
     * Este método inicializa una PAC.
     *
     * @param pac
     * @return
     */
    public PAC iniciarPAC(PAC pac) {
        try {
            checkSiPACBloqueadoParaAnio(pac);

            generarGruposSUgeridos(pac);
            pac.setIniciado(true);

            pac = (PAC) generalDAO.update(pac);
            for (PACGrupo g : pac.getGrupos()) {
                g.getInsumos().isEmpty();
            }
            pac.initInsumos();
            return pac;
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
     * Este método guarda un PAC u devuelve el PAC actualizado.
     *
     * @param pac
     * @return
     */
    public PAC guardarPAC(PAC pac) {
        try {
            //chequea que el pac no este bloqueado
            checkSiPACBloqueadoParaAnio(pac);

            if (pac.getFechaFin() != null && pac.getFechaInicio() != null) {
                if (pac.getFechaInicio().compareTo(pac.getFechaFin()) > 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_FECHA_DESDE_MAYOR_QUE_HASTA);
                    throw b;
                }
            }

            pac = (PAC) generalDAO.update(pac);
            for (PACGrupo g : pac.getGrupos()) {
                g.getInsumos().isEmpty();
            }
            pac.initInsumos();
            return pac;
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
     * Este método bloquea un PAC dado su id.
     *
     * @param idPAC
     * @return
     */
    public PAC bloquearPAC(Integer idPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            //chequea que el pac no este bloqueado
            checkSiPACBloqueadoParaAnio(pac);
            pac.setEstado(EstadoPAC.CERRADO);

            //que no exista grupo en estado disntinto a cerrado
            for (PACGrupo g : pac.getGrupos()) {
                if (g.getEstado() != EstadoGrupoPAC.CERRADO) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_GUARDAR_UN_PAC_CON_ESTADO_CERRADO_TODOS_SUS_GRUPOS_TIENEN_QUE_ESTAR_CERRADOS);
                    throw b;
                }
            }
            //que todos los insumos pertenezcan a un grupo
            pac.initInsumos();
            for (POInsumos i : pac.getInsumos()) {
                if (i.getPacGrupo() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_GUARDAR_UN_PAC_CON_ESTADO_CERRADO_TODOS_SUS_INSUMOS_TIENEN_QUE_TENER_GRUPO);
                    throw b;
                }
            }

            return pac;
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
     * Este método desbloquea un PAC bloqueado
     *
     * @param idPAC
     * @return
     */
    public PAC desbloquear(Integer idPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            //chequea que el pac no este bloqueado
            checkSiPACBloqueadoParaAnio(pac);
            pac.setEstado(EstadoPAC.EN_FORMULACION);

            pac.initInsumos();
            return pac;
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
     * Este método guarda el FCM de un PAC
     *
     * @param pac
     */
    public void guardarFCMPAC(PAC pac) {
        try {
            for (PACGrupo grupo : pac.getGrupos()) {
                for (POInsumos insumo : grupo.getInsumos()) {
                    FlujoCajaMensualUtils.validarFCMPlanificadoInsumo(insumo, insumo.getFlujosDeCajaAnio());
                    generalDAO.update(insumo);
                }
            }
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
     * Método encargado de crear un PAC
     *
     * @param poas
     * @param anio
     * @return
     */
    private void checkSiPACEditable(PAC pac) throws Exception {
        if (pac.getEstado() == EstadoPAC.CERRADO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_GRUPO_NO_SE_PUEDE_ABRIR_MIENTRAS_PAC_CERRADO);
            throw b;
        }
        checkSiPACBloqueadoParaAnio(pac);
    }

    /**
     * Este método genera los grupos sugeridos según la plantilla de grupos.
     *
     * @param idPAC
     */
    public void generarGruposSugeridos(Integer idPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            //chequea que el pac no este bloqueado
            checkSiPACEditable(pac);

            generarGruposSUgeridos(pac);
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
     * Este método genera los grupos sugeridos de la plantilla de insumos.
     *
     * @param pac
     */
    private void generarGruposSUgeridos(PAC pac) {

        pac.initInsumos();
        for (POInsumos poaInsumo : pac.getInsumos()) {
            poaInsumo.setPacGrupo(null);
        }
        pac.getGrupos().clear();
        HashMap<String, PACGrupo> mapaDeGrupos = new HashMap();
        for (POInsumos poaInsumo : pac.getInsumos()) {

            //busca el nodo en la plantilla
            NodoPlantillaDeInsumos nodo = plantillaDeInsumosDAO.getNodoInsumo(poaInsumo.getInsumo().getId());
            String nombreGrupo = null;
            while (nodo != null) {
                if (TextUtils.isEmpty(nombreGrupo)) {
                    nombreGrupo = nodo.getNombre();
                } else {
                    nombreGrupo = nodo.getNombre() + " - " + nombreGrupo;
                }
                nodo = nodo.getPadre();
            }

            //si tiene grupo lo tiene que agregar
            if (!TextUtils.isEmpty(nombreGrupo)) {
                PACGrupo grupo = null;
                if (mapaDeGrupos.containsKey(nombreGrupo)) {
                    grupo = mapaDeGrupos.get(nombreGrupo);
                } else {
                    grupo = new PACGrupo();
                    grupo.setIniciado(Boolean.TRUE);
                    grupo.setEstado(EstadoGrupoPAC.PENDIENTE);
                    grupo.setInsumos(new LinkedList());
                    grupo.setNombre(nombreGrupo);
                    grupo.setPac(pac);
                    grupo.setTotal(BigDecimal.ZERO);

                    pac.getGrupos().add(grupo);
                    mapaDeGrupos.put(nombreGrupo, grupo);
                }

                calcularAsociarInsumo(poaInsumo, grupo);

            }
        }

    }

    /**
     * Este método devuelve un grupo PAC a partir de su id
     *
     * @param idGrupo
     * @return
     */
    public PACGrupo getGrupo(Integer idGrupo) {
        try {
            PACGrupo g = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo);
            g.getInsumos().isEmpty();

            return g;
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
     * Este método determina si un grupo de un PAC es editable.
     *
     * @param pac
     * @param grupo
     * @throws Exception
     */
    private void checkSiGrupoEditable(PAC pac, PACGrupo grupo) throws Exception {
        if (pac.getEstado() == EstadoPAC.CERRADO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_GRUPO_NO_SE_PUEDE_ABRIR_MIENTRAS_PAC_CERRADO);
            throw b;
        }
        if (grupo.getEstado() == EstadoGrupoPAC.CERRADO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_GRUPO_SE_ENCUENTRA_CERRADO);
            throw b;
        }
        checkSiPACBloqueadoParaAnio(pac);
    }

    /**
     * Este método inicia un grupo de un PAC.
     *
     * @param idPAC
     * @param grupo
     * @return
     */
    public PACGrupo iniciarGrupo(Integer idPAC, PACGrupo grupo) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            checkSiGrupoEditable(pac, grupo);

            grupo.setIniciado(true);

            if (grupo.getPac() == null) {
                grupo.setPac(pac);
                pac.getGrupos().add(grupo);
            } else {
                grupo = (PACGrupo) generalDAO.update(grupo);
            }
            return grupo;

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
     * Este método elimina un grupo de PAC.
     *
     * @param idGrupo
     */
    public void eliminarGrupo(Integer idGrupo) {
        try {
            PACGrupo g = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo);
            //chequea que el pac no este bloqueado
            checkSiGrupoEditable(g.getPac(), g);

            //se desasocian todos los insumos
            for (POInsumos i : g.getInsumos()) {
                i.setPacGrupo(null);
            }

            //se elimina el grupo
            g.getPac().getGrupos().remove(g);
            generalDAO.delete(g);

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
     * Este método cambia el estado de un grupo de un PAC.
     *
     * @param idPAC
     * @param idGrupo
     * @param estadoPAC
     * @return
     */
    public PACGrupo cambiarEstadoGrupo(Integer idPAC, Integer idGrupo, EstadoGrupoPAC estadoPAC) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);
            //chequea que el pac no este bloqueado
            checkSiPACEditable(pac);

            PACGrupo grupo = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo);
            grupo.setEstado(estadoPAC);
            return grupo;
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
     * Este método guarda un grupo de un PAC.
     *
     * @param grupo
     * @param idPAC
     * @param idMetodoAdquisicion
     * @param gant
     * @return
     */
    public PACGrupo guardarGrupo(PACGrupo grupo, Integer idPAC, Integer idMetodoAdquisicion, String gant) {
        try {
            PAC pac = (PAC) generalDAO.find(PAC.class, idPAC);

            //chequea que el pac no este bloqueado
            checkSiGrupoEditable(pac, grupo);

            MetodoAdquisicion m = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, idMetodoAdquisicion);
            grupo.setMetodoAdquisicion(m);

            Gson gson = new Gson();
            DataGantt datGantt = gson.fromJson(gant, DataGantt.class);

            Gantt g = GanttUtils.getGantt(datGantt);
            grupo.setGantt(g);

            //se setea la fecha maxima del gruupo
            Date maxGant = GanttUtils.getFechaMaxima(g);
            grupo.setFechaMaximaGant(maxGant);

            MetodoAdquisicionRango metodoAdqRango = PACUtils.getRangoParaAnio(grupo.getMetodoAdquisicion().getRangos(), pac.getAnio());
            if (metodoAdqRango == null
                    || metodoAdqRango.getMontoMin().compareTo(grupo.getTotal()) > 0
                    || metodoAdqRango.getMontoMax().compareTo(grupo.getTotal()) < 0) {

                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_MONTO_DEL_GRUPO_NO_CUMPLE_CON_LOS_RANGOS_DEL_METODO_SELECIONADO);
                throw b;
            }

            if (grupo.getEstado() == EstadoGrupoPAC.CERRADO) {
                if (TextUtils.isEmpty(grupo.getNombre())) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_CERRAR_GRUPO_EL_GRUPO_NECESITA_NOMBRE);
                    throw b;
                }
                if (grupo.getMetodoAdquisicion() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PARA_CERRAR_GRUPO_NECESITA_METODO_ADQUISICION);
                    throw b;
                }
            }

            if (grupo.getPac() == null) {
                grupo.setPac(pac);
                pac.getGrupos().add(grupo);
            } else {
                grupo = (PACGrupo) generalDAO.update(grupo);
            }
            return grupo;
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
     * Este método asocia un grupo a un insumo del PAC.
     *
     * @param insumo
     * @param grupo
     */
    public void calcularAsociarInsumo(POInsumos insumo, PACGrupo grupo) {
        calcularDesasociarInsumo(insumo);
        //se asocia el insumo
        if (grupo != null) {
            if (grupo.getInsumos() == null) {
                grupo.setInsumos(new LinkedList<POInsumos>());
            }
            grupo.getInsumos().add(insumo);
            insumo.setPacGrupo(grupo);
            //recalcula monto
            grupo.setTotal(grupo.getTotal().add(insumo.getMontoTotal()));
            //reclacula fecha de contratacion
            if (grupo.getFechaMinimoInsumo() == null || (insumo.getFechaContratacion().compareTo(grupo.getFechaMinimoInsumo()) < 0)) {
                grupo.setFechaMinimoInsumo(insumo.getFechaContratacion());
            }
        }
    }

    /**
     * Este método calcula el monto correspondiente para desasociar un grupo.
     *
     * @param insumo
     */
    public void calcularDesasociarInsumo(POInsumos insumo) {
        if (insumo.getPacGrupo() != null) {
            PACGrupo grupoViejo = insumo.getPacGrupo();
            //recalcula el monto
            grupoViejo.setTotal(insumo.getPacGrupo().getTotal().subtract(insumo.getMontoTotal()));
            //reclacula la fecha de cotnratacion
            if (Objects.equals(insumo.getFechaContratacion(), grupoViejo.getFechaMinimoInsumo())) {
                Date nuevaFecha = null;
                for (POInsumos iter : grupoViejo.getInsumos()) {
                    if (!iter.equals(insumo)) {
                        if (nuevaFecha == null || (nuevaFecha.compareTo(iter.getFechaContratacion()) < 0)) {
                            nuevaFecha = iter.getFechaContratacion();
                        }
                    }
                }
                grupoViejo.setFechaMinimoInsumo(nuevaFecha);
            }
            grupoViejo.getInsumos().remove(insumo);
            insumo.setPacGrupo(null);
        }

    }

    /**
     * Este método asocia una lista de insumos a un grupo.
     *
     * @param idGrupo
     * @param insumos
     */
    public void asociarInsumo(Integer idGrupo, List<Integer> insumos) {
        try {
            for (Integer idInsumo : insumos) {
                POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
                PACGrupo grupo = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo);

                //chequea que el pac no este bloqueado
                checkSiPACBloqueadoParaAnio(grupo.getPac());

                if (grupo.getEstado() == EstadoGrupoPAC.CERRADO) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_GRUPO_SE_ENCUENTRA_CERRADO);
                    throw b;
                }

                calcularAsociarInsumo(insumo, grupo);

            }

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
     * Este método desasocia un insumo de un grupo (el único al que pertenece).
     *
     * @param idInsumo
     */
    public void desacociarInsumo(Integer idInsumo) {
        try {
            POInsumos i = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
            if (i.getPacGrupo() != null) {
                if (i.getPacGrupo().getEstado() == EstadoGrupoPAC.CERRADO) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_GRUPO_SE_ENCUENTRA_CERRADO);
                    throw b;
                }

                //chequea que el pac no este bloqueado
                checkSiPACBloqueadoParaAnio(i.getPacGrupo().getPac());
                calcularDesasociarInsumo(i);
            }
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
     * Este método devuelve los PAC que cumplen con un determinado criterio.
     *
     * @param criteria
     * @param startPosition
     * @param maxResult
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<PAC> filtrarPAC(CriteriaTO criteria, Long startPosition, Long maxResult, String[] orderBy, boolean[] ascending) {
        try {
            List<PAC> l = generalDAO.findEntityByCriteria(PAC.class, criteria, orderBy, ascending, startPosition, maxResult);
            for (PAC pac : l) {
                for (PACGrupo g : pac.getGrupos()) {
                    g.getInsumos().isEmpty();
                }
                pac.initInsumos();
            }
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

    /**
     * Este método realiza la unificación de dos grupos en un único grupo.
     *
     * @param idGrupo1
     * @param idGrupo2
     */
    public void unificarGrupos(Integer idGrupo1, Integer idGrupo2) {
        try {
            PACGrupo grupoToAdd = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo1);
            PACGrupo grupoToElimar = (PACGrupo) generalDAO.find(PACGrupo.class, idGrupo2);
            //chequea que el pac no este bloqueado
            checkSiPACBloqueadoParaAnio(grupoToAdd.getPac());

            if (grupoToAdd.getEstado() == EstadoGrupoPAC.CERRADO || grupoToElimar.getEstado() == EstadoGrupoPAC.CERRADO) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_GRUPO_SE_ENCUENTRA_CERRADO);
                throw b;
            }

            while (!grupoToElimar.getInsumos().isEmpty()) {
                calcularAsociarInsumo(grupoToElimar.getInsumos().get(0), grupoToAdd);
            }
            grupoToAdd.setNombre(grupoToAdd.getNombre() + " + " + grupoToElimar.getNombre());

            grupoToElimar.getPac().getGrupos().remove(grupoToElimar);
            generalDAO.delete(grupoToElimar);

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
     * Este método devuelve todos los PAC cerrados.
     *
     * @return
     * @throws GeneralException
     */
    public List<PAC> getPACsCerrados() throws GeneralException {
        try {
            List<PAC> pacsCerrados = (List<PAC>) generalDAO.findByOneProperty(PAC.class, "estado", EstadoPAC.CERRADO);

            return pacsCerrados;
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;
        }

    }

    /**
     * Este método bloquea los PAC de un año particular.
     *
     * @param bloquear
     * @param anio
     */
    public void setBloquearPAC(boolean bloquear, Integer anio) {
        try {
            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_GENERAR_BLOQUEOS_DE_PAC);
            List<ReportePAC> reportes = generalDAO.findByOneProperty(ReportePAC.class, "anio", anio);
            ReportePAC reporte;
            if (reportes.isEmpty()) {
                reporte = new ReportePAC();
                reporte.setAnio(anio);
                reporte.setReporteEmitido(false);
            } else {
                reporte = reportes.get(0);
            }
            reporte.setPacBloqueados(bloquear);
            generalDAO.update(reporte);

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
     * Este método determina si un PAC está bloqueado
     *
     * @param pac
     * @throws Exception
     */
    public void checkSiPACBloqueadoParaAnio(PAC pac) throws Exception {
        List<ReportePAC> reportes = generalDAO.findByOneProperty(ReportePAC.class, "anio", pac.getAnio());
        if (!reportes.isEmpty()) {
            ReportePAC reporte = reportes.get(0);
            if (reporte.getPacBloqueados()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LOS_PAC_SE_ENCUENTRAN_CERRADOS_NO_PUEDE_MODIFICARSE);
                throw b;
            }
        }
    }

    /**
     * Este método genera el reporte PAC.
     *
     * @param anio
     * @param borrador si el reporte se genera en modo borrador o definitivo.
     */
    public void generarReportePAC(Integer anio, boolean borrador) {
        try {
            //comentado para que siempre se pueda emitir reporte pac en la capacitacion
            List<ReportePAC> reportes = generalDAO.findByOneProperty(ReportePAC.class, "anio", anio);
            if (reportes.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRAN_BLOQUEADOS_LOS_PAC_PARA_EL_ANIO);
                throw b;
            }
            if (!reportes.get(0).getPacBloqueados()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRAN_BLOQUEADOS_LOS_PAC_PARA_EL_ANIO);
                throw b;
            }

            List<PAC> pacs = generalDAO.findByOneProperty(PAC.class, "anio", anio);
            for (PAC pac : pacs) {
                Archivo archivo = reporteBean.generarReportePAC(pac.getId());
                if (borrador) {
                    pac.setBorradorPAC(archivo);
                } else {
                    pac.setReportePAC(archivo);
                }

            }
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
     * *
     * fixme: verificar con presupuesto que el reporte sea correcto
     *
     * @param anio
     * @param borrador
     */
    public void generarReportePEP(Integer anio, boolean borrador) {
        try {

            List<ReportePEP> reportes = generalDAO.findByOneProperty(ReportePEP.class, "anio", anio);
            if (!borrador) {
                if (reportes.isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRAN_BLOQUEADOS_LOS_FCM_PARA_EL_ANIO);
                    throw b;
                }
                if (!reportes.get(0).getPepBloqueados()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_ENCUENTRAN_BLOQUEADOS_LOS_FCM_PARA_EL_ANIO);
                    throw b;
                }
            }

            List<PAC> pacs = generalDAO.findByOneProperty(PAC.class, "anio", anio);

            for (PAC pac : pacs) {

                Archivo archivo = reporteBean.generarReportePEP(pac.getId());

                if (borrador) {

                    pac.setBorradorPEP(archivo);
                } else {

                    pac.setReportePEP(archivo);
                }

            }

            if (!borrador) {

                reportes.get(0).setReporteEmitido(true);
            }

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
     * Este método determinado si está habilitado el reporte PEP.
     *
     * @param idAnioFiscal
     * @return
     */
    public boolean estaHabilitadoReportePEP(Integer idAnioFiscal) {
        try {
            if (pacdao.hayPACSinCerrar(idAnioFiscal)) {
                return false;
            }
            List<PACGrupo> pacSinFCM = poaDAO.getPEPGrupoSinFCM(idAnioFiscal, 0, 3);
            if (!pacSinFCM.isEmpty()) {
                return false;
            }
            List<POInsumos> insumosSinFCM = poaDAO.getInsumosPEPISinFCM(idAnioFiscal, 0, 3);
            if (!insumosSinFCM.isEmpty()) {
                return false;
            }
            return true;
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
     * Este método determina si para un año dado hay un PAC sin cerrar.
     *
     * @param anio
     * @return
     */
    public boolean hayPACSinCerrar(Integer anio) {
        try {
            return pacdao.hayPACSinCerrar(anio);
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
     * Este método obtiene los PAC que no tienen FCM
     *
     * @param idAnioFiscal
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<PACGrupo> getPACGrupoSinFCM(Integer idAnioFiscal, Integer firstResult, Integer maxResults) {
        try {
            List<PACGrupo> l = poaDAO.getPEPGrupoSinFCM(idAnioFiscal, firstResult, maxResults);
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

    /**
     * Este método obtiene los Insumos UACI que no tienen FCM.
     *
     * @param idAnioFiscal
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<POInsumos> getInsumosUACISinFCM(Integer idAnioFiscal, Integer firstResult, Integer maxResults) {
        try {
            List<POInsumos> l = poaDAO.getInsumosPEPISinFCM(idAnioFiscal, firstResult, maxResults);
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

    /**
     * Este método calcula la cantidad de grupos sin FCM para el PAC.
     *
     * @param idAnioFiscal
     * @return
     */
    public long countPACGrupoSinFCM(Integer idAnioFiscal) {
        return poaDAO.countPEPGrupoSinFCM(idAnioFiscal);
    }

    /**
     * Este método calcula la cantidad de insumos UACI sin FCM en el año dado.
     *
     * @param idAnioFiscal
     * @return
     */
    public long countInsumosUACISinFCM(Integer idAnioFiscal) {
        return poaDAO.countInsumosPEPISinFCM(idAnioFiscal);
    }

    /**
     * Este método bloquea la PEP.
     *
     * @param bloquear
     * @param idAnioFiscal
     */
    public void setBloquearPEP(boolean bloquear, Integer idAnioFiscal) {
        try {
            generalDAO.lock(SsLock.class, ConstantesEstandares.LOCK_GENERAR_BLOQUEOS_PEP);

            //se marca como bloqueado y se genera el reporte para el año
            AnioFiscal anio = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);

            List<ReportePEP> reportes = generalDAO.findByOneProperty(ReportePEP.class, "anio", anio.getAnio());
            ReportePEP reporte;
            if (reportes.isEmpty()) {
                reporte = new ReportePEP();
                reporte.setAnio(anio.getAnio());
                reporte.setReporteEmitido(false);
            } else {
                reporte = reportes.get(0);
            }
            reporte.setPepBloqueados(bloquear);
            reporte = (ReportePEP) generalDAO.update(reporte);

            //una vez bloqueado se setea el monto pep para todos los insumos  
            if (bloquear) {
                List<POInsumos> insumosPEP = poaDAO.getInsumosParaPEP(anio.getId());
                for (POInsumos poInsumo : insumosPEP) {
                    poInsumo.setMontoPep(poInsumo.getMontoTotal());
                }
            }

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
     * Este método obtiene el flujo de un insumo dado.
     *
     * @param idInsumo
     * @return
     */
    public Set<FlujoCajaAnio> getFCMPlanificadoInsumo(Integer idInsumo) {
        POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
        for (FlujoCajaAnio fcmAnio : insumo.getFlujosDeCajaAnio()) {
            fcmAnio.getFlujoCajaMenusal().isEmpty();
        }
        return insumo.getFlujosDeCajaAnio();
    }

    /**
     * Este método elimina el flujo de caja para un insumo y año dado.
     *
     * @param idInsumo
     * @param idFCanio
     * @throws GeneralException
     */
    public void eliminarFlujoDeCajaPlanificado(Integer idInsumo, Integer idFCanio) throws GeneralException {
        try {
            POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
            FlujoCajaAnio fc = (FlujoCajaAnio) generalDAO.find(FlujoCajaAnio.class, idFCanio);
            insumo.getFlujosDeCajaAnio().remove(fc);
            generalDAO.delete(fc);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los insumos de un PAC.
     *
     * @param idPAC
     * @param nombreGrupo
     * @return
     */
    public List<HashMap> obtenerInsumosDelPAC(Integer idPAC, String nombreGrupo) {

        List<HashMap> respuesta = new ArrayList<>();
        try {
            PAC pac = null;
            if (idPAC != null) {
                pac = (PAC) generalDAO.find(PAC.class, idPAC);
                if (pac == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_PAC);
                    throw b;
                }
            }

            HashMap fila1 = new HashMap();
            fila1.put("col1", "Número Grupo");
            fila1.put("col2", "Nombre del grupo");
            fila1.put("col3", "Método de adquisición");
            fila1.put("col4", "Código del insumo");
            fila1.put("col5", "Nombre del insumo");
            fila1.put("col6", "OEG");
            fila1.put("col7", "Cantidad");
            fila1.put("col8", "Monto total");
            fila1.put("col9", "Fecha estimada de contratación");
            fila1.put("styleClass", "tblFilaTitulo2");

            completarFilaLinea(fila1, 7);
            respuesta.add(fila1);

            List<PAC> todosLosPACs = pacdao.filtrarPACs(idPAC, nombreGrupo);
            for (PAC unPAC : todosLosPACs) {
                unPAC.initInsumos();

                if (unPAC.getInsumos() != null) {
                    for (POInsumos insumo : unPAC.getInsumos()) {
                        HashMap fila = cargarMapConInsumoDelPac(insumo);
                        respuesta.add(fila);
                    }
                }
            }

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    /**
     *
     * @param fila
     * @param cantidadColumnas
     */
    private void completarFilaLinea(HashMap fila, int cantidadColumnas) {
        if (fila == null) {
            return;
        }
        for (int i = 1; i <= cantidadColumnas; i++) {
            if (fila.get("col" + i) == null) {
                fila.put("col" + i, "");
            }
        }
    }

    private HashMap cargarMapConInsumoDelPac(POInsumos insumo) {
        HashMap fila = new HashMap();

        String numeroGrupo = "";
        String nombreGrupo = "";
        String metodoAdquisicion = "";

        if (insumo.getPacGrupo() != null) {
            numeroGrupo = insumo.getPacGrupo().getId().toString();
            nombreGrupo = insumo.getPacGrupo().getNombre();
            if (insumo.getPacGrupo().getMetodoAdquisicion() != null) {
                metodoAdquisicion = insumo.getPacGrupo().getMetodoAdquisicion().getNombre();
            }
        }

        fila.put("col1", numeroGrupo);
        fila.put("col2", nombreGrupo);
        fila.put("col3", metodoAdquisicion);
        fila.put("col4", insumo.getId());

        String nombreInsumo = "";
        if (insumo.getInsumo() != null && insumo.getInsumo().getNombre() != null) {
            nombreInsumo = insumo.getInsumo().getNombre();
        }

        fila.put("col5", nombreInsumo);

        String OEG = "";
        if (insumo.getInsumo() != null) {
            OEG = insumo.getInsumo().getObjetoEspecificoGasto() != null ? insumo.getInsumo().getObjetoEspecificoGasto().getClasificador().toString() : "";
        }

        fila.put("col6", OEG);
        fila.put("col7", insumo.getCantidad());

        String montoTotal = NumberUtils.nomberToString(insumo.getMontoTotal());
        fila.put("col8", montoTotal);

        String fechaContratacion = "";
        if (insumo.getFechaContratacion() != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            fechaContratacion = formateador.format(insumo.getFechaContratacion());
        }
        fila.put("col9", fechaContratacion);

        fila.put("styleClass", "tblFilaContenido");
        completarFilaLinea(fila, 9);

        return fila;
    }

}
