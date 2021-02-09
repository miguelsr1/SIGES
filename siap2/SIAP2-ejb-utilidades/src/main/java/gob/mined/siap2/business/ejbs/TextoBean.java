/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.business.validations.TextoValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.data.daos.TextoDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Idioma;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.entities.data.impl.TextoAyuda;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos que permiten gestionar los textos que se
 * utilizan en la interfaz de usuario de la aplicación.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "TextoBean")
@LocalBean
public class TextoBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private IdiomaBean idiomaBean;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private TextoDAO textoDAO;

    /**
     * Este método guarda un elemento de tipo Texto. Se aplica para la creación
     * de la entidad y para la modificación de una entidad existente.
     *
     * @param tho
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public Texto guardar(Texto tho) throws GeneralException {
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion

            if (TextoValidacion.validar(tho)) {
                if (tho.getTexId() == null) {
                    logger.info("creo Texto codigo: " + tho.getTexCodigo() + " - idioma: " + tho.getTexIdiId().getIdiDescripcion() + " - valor:" + tho.getTexValor());
                    if (tho.getTexCodigo().length()>255) {
                        tho.setTexCodigo(tho.getTexCodigo().substring(0,255));
                        tho.setTexValor(tho.getTexValor().substring(0,255));
                    }
                    tho = textoDAO.create(tho);
                } else {
                    tho = textoDAO.update(tho);
                }
            }
            return tho;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
         
    }

    /**
     * Devuelve el elemento configuracion por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public Texto obtenerTextoPorId(Integer id) throws GeneralException {
        try {
            return textoDAO.findById(Texto.class, id);
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
    }

    /**
     * Este método permite eliminar el texto que tiene el id indicado
     * @param id del texto a eliminar
     * @throws GeneralException 
     */
    public void eliminarTexto(Integer id) throws GeneralException {
        try {
            Texto aux = textoDAO.findById(Texto.class, id);
            if (aux != null) {
                textoDAO.delete(aux);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
    }

    /**
     * Este método permite obtener un texto con un determinado código en el idioma indicado
     * @param codigoTexto es el código del texto.
     * @param idIdioma es el id del idioma.
     * @return
     * @throws GeneralException 
     */
    @TransactionAttribute(REQUIRES_NEW)
    public String obtenerTextoPorCodigos(String codigoTexto, Integer idIdioma) throws GeneralException {
        try {
            String result = textoDAO.obtenerTextoPorCodigosId(codigoTexto, idIdioma);
            if (result == null) {
                Idioma idi = idiomaBean.obtenerIdiomaPorId(idIdioma);
                Texto t = new Texto();
                t.setTexCodigo(codigoTexto);
                t.setTexIdiId(idi);
                t.setTexHabilitado(Boolean.TRUE);
                t.setTexValor("??" + codigoTexto + "_" + idi.getIdiCodigo() + "??");
                t = guardar(t);
                result = t.getTexValor();
            }

            return result;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
    }

    /**
     * Este método permite obtener los textos en el idioma indicado.
     * @param idIdioma
     * @return 
     */
    public List<Texto> obtenerTodosLosTextos(Integer idIdioma) {
        List<Texto> respuesta = null;
        try {
            CriteriaTO to = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "texIdiId.idiId", idIdioma);

            respuesta = generalDAO.findEntityByCriteria(Texto.class, to, null, null, null, null);

        } catch (Exception ex) {
           logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
        return respuesta;
    }
 
    /**
     * Este método permite obtener el texto de una ayuda con el codigo indicado en el idioma indicado.
     * @param codigoTexto
     * @param idIdioma
     * @return
     * @throws GeneralException 
     */
    public String obtenerCrearTextoAyudaPorCodigos(String codigoTexto, Integer idIdioma) throws GeneralException {
        try {
            TextoAyuda t = textoDAO.obtenerTextoAyudaPorCodigosId(codigoTexto, idIdioma);

            if (t == null) {
                Idioma idi = idiomaBean.obtenerIdiomaPorId(idIdioma);
                t = new TextoAyuda();
                t.setCodigo(codigoTexto);
                t.setIdioma(idi);
                t.setHabilitado(Boolean.TRUE);
                t.setNombre("??" + codigoTexto + "_" + idi.getIdiCodigo() + "??");
                t = (TextoAyuda) generalDAO.update(t);

            }
            return t.getNombre();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    public String obtenerMensajeByProperty(String msj, Integer idiomaId) {
        try {
            String[] params = GeneralException.getParams(msj);
            String mensaje = msj;
            if (params != null) {//El mensaje tiene parametros
                mensaje = GeneralException.getTextError(msj);
            }
            String texto = getMensajeByKey(mensaje, idiomaId);
            if (params != null && texto.indexOf("{") != -1) {//Hay parametros para sustituir en el texto
                texto = MessageFormat.format(texto, params);
            }
            return texto;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
        return "";
    }
    
   
    public String getMensajeByKey(String key, Integer idiomaId) {
        try {
            return obtenerTextoPorCodigos(key, idiomaId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[getMensajeByKey] Error " + e.getMessage(), e);
            return key;
        }
    }
}
