/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb;

import com.sofis.persistence.dao.GeneralDAO;
import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import com.sofis.utils.TextUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.inject.Inject;
import uy.com.sofis.pfea.entities.Texto;
import uy.com.sofis.pfea.anotaciones.JPADAO;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.exceptions.BusinessException;
import uy.com.sofis.pfea.exceptions.GeneralException;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 * Esta clase incluye los métodos que permiten gestionar los textos que se
 * utilizan en la interfaz de usuario de la aplicación.
 *
 * @author Sofis Solutions
 */
@Stateless
@LocalBean
public class TextoSB {

    private static final Logger logger = Logger.getLogger(TextoSB.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    /**
     * Este método guarda un elemento de tipo Texto. Se aplica para la creación
     * de la entidad y para la modificación de una entidad existente.
     *
     * @param tho
     * @return
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public Texto guardar(Texto tho) throws GeneralException {
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion

            if (validar(tho)) {
                if (tho.getTexId() == null) {
                    logger.log(Level.INFO, "creo Texto codigo: {0} - valor:{1}", new Object[]{tho.getTexCodigo(), tho.getTexValor()});
                    if (tho.getTexCodigo().length() > 255) {
                        tho.setTexCodigo(tho.getTexCodigo().substring(0, 255));
                        tho.setTexValor(tho.getTexValor().substring(0, 255));
                    }
                    tho = (Texto) generalDAO.create(tho);
                } else {
                    tho = (Texto) generalDAO.update(tho);
                }
            }
            return tho;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
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
            return (Texto) generalDAO.findById(Texto.class, id);
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
     *
     * @param id del texto a eliminar
     * @throws GeneralException
     */
    public void eliminarTexto(Integer id) throws GeneralException {
        try {
            Texto aux = (Texto) generalDAO.findById(Texto.class, id);
            if (aux != null) {
                generalDAO.delete(aux);
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
     * Este método permite obtener un texto con un determinado código en el
     * idioma indicado
     *
     * @param codigoTexto es el código del texto.
     * @return
     * @throws GeneralException
     */
    @TransactionAttribute(REQUIRES_NEW)
    public String obtenerTextoPorCodigos(String codigoTexto) throws GeneralException {
        try {
            String result = null;
            CriteriaTO to = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "texCodigo", codigoTexto);
            List<Texto> lista = generalDAO.findEntityByCriteria(Texto.class, to, null, null, null, null);
            if (lista == null || lista.isEmpty()) {
                Texto t = new Texto();
                t.setTexCodigo(codigoTexto);
                t.setTexValor("??" + codigoTexto + "_??");
                t = guardar(t);
                result = t.getTexValor();
            } else {
                result = lista.get(0).getTexValor();
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
     * Este método permite obtener los textos.
     *
     * @return
     */
    public List<Texto> obtenerTodosLosTextos() {
        List<Texto> respuesta = null;
        try {

            respuesta = generalDAO.findAll(Texto.class);
            //CriteriaTO to = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "texIdiId.idiId", idIdioma);
            //respuesta = generalDAO.findEntityByCriteria(Texto.class, to, null, null, null, null);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
        return respuesta;
    }

    private boolean validar(Texto tho) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tho == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            String aux = normalizarString(tho.getTexCodigo());
            tho.setTexCodigo(aux);
            if (TextUtils.isEmpty(tho.getTexCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }

            aux = normalizarString(tho.getTexValor());
            tho.setTexValor(aux);
            if (TextUtils.isEmpty(tho.getTexValor())) {
                ge.addError(ConstantesErrores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

//    private boolean compararParaGrabar(Texto c1, Texto c2) {
//        boolean respuesta = true;
//        if (c1 != null) {
//            if (c2 != null) {
//                respuesta = TextUtils.sonStringIguales(c1.getTexCodigo(), c2.getTexCodigo());
//                respuesta = respuesta && TextUtils.sonStringIguales(c1.getTexValor(), c2.getTexValor());
//                respuesta = respuesta && BooleanUtils.sonBooleanIguales(c1.getTexHabilitado(), c2.getTexHabilitado());
//
//            }
//        } else {
//            respuesta = c2 == null;
//        }
//        return respuesta;
//    }
    /**
     * Normaliza una cadena de caracteres de la siguiente manera: Elimina los
     * espacios en blanco al inicio, al final y, si ocurren más de un espacio en
     * blanco consecutivo, deja uno solo.
     *
     * @param s
     * @return
     */
    private String normalizarString(String s) {
        if (s != null) {
            return s.replaceAll("[ ]+", " ").trim();
        }
        return null;
    }

}
