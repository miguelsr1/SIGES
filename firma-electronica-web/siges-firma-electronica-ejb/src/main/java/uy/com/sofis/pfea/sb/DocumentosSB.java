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
import com.sofis.utils.UtilVarios;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.agesic.firma.datatypes.InfoFirma;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import uy.com.sofis.pfea.anotaciones.JPADAO;
import uy.com.sofis.pfea.binarios.Repositorio;
import uy.com.sofis.pfea.binarios.RepositorioFactory;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.entities.Configuracion;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;
import uy.com.sofis.pfea.exceptions.GeneralException;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 *
 * @author sofis3
 */
@Stateless
@LocalBean
public class DocumentosSB {

    private static final Logger logger = Logger.getLogger(DocumentosSB.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Resource
    SessionContext ctx;

    @EJB
    private ConfiguracionSB configuracionSB;

    @EJB
    private FirmaSB firmaSB;

    public Documento crearDocumento(Documento doc) {
        try {
            if (doc == null) {
                return null;
            }
            Repositorio repositorio = RepositorioFactory.getRepositorio();
            repositorio.guardarDocumento(doc.getNombreArchivo(), doc.getDocumentoBytes());
            doc = (Documento) generalDAO.update(doc);
            return doc;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método solo actualiza el documento en la base de datos; no cambia
     * ningún dato (código pac, bytes, etc) que no esté en el propio objeto.
     *
     * @param doc
     * @return
     */
    public Documento actualizarDocumento(Documento doc) {
        try {
            if (doc == null) {
                return null;
            }
            doc = (Documento) generalDAO.update(doc);
            return doc;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Elimina el documento. El parámetro eliminarBytes indica si se debe borrar
     * el archivo de disco o no; si es true, se borra el archivo, y si estaba
     * firmado también se borra el archivo firmado. Esta bandera no se pasa en
     * cascada.
     *
     * @param doc
     * @param eliminarBytes
     * @return
     */
    public Documento eliminarDocumento(Documento doc) {
        try {
            if (doc == null) {
                return null;
            }
            Repositorio repositorio = RepositorioFactory.getRepositorio();
            doc.setFechaEliminacion(new Date());
            doc.setCodigopac(null); //Si estaba enviado al PAC se "quita"
            doc = (Documento) generalDAO.update(doc);
            //Eliminar el archivo físico (y si está firmado también éste)
            repositorio.eliminarDocumento(doc.getNombreArchivo());
            if (BooleanUtils.isTrue(doc.getFirmado())) {
                String nombreFirmado = UtilVarios.addCharFirmado(doc.getNombreArchivo());
                repositorio.eliminarDocumento(nombreFirmado);
            }
            //Si el documento fue enviado por otro usuario, notificar al original
            if (!StringUtils.isBlank(doc.getRetorno())) {
                if (doc.getRetorno().startsWith("http://") || doc.getRetorno().startsWith("https://")) {
                    //Si es una URL, invocarla enviando el identificador del documento
                    try {
                        String urlRetorno = doc.getRetorno();
                        if (!urlRetorno.endsWith("/")) {
                            urlRetorno = urlRetorno + "/";
                        }
                        urlRetorno = urlRetorno + doc.getIdentificador();
                        URL obj = new URL(urlRetorno);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("User-Agent", "Agesic PFEA");
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String respuesta = in.readLine();
                        in.close();
                        doc.setRespuesta(respuesta);
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "Error al notificar al sistema externo (" + doc.getOrigen() + "/" + doc.getIdentificador() + ")", ex);
                        doc.setRespuesta(ex.getMessage());
                    }
                } else {
                    //Sino, verificar si existe un documento con el identificador original, y si existe marcarlo como rechazado
                    if (doc.getRetorno().startsWith("doc://")) {
                        try {
                            String idDoc = doc.getRetorno().replace("doc://", "");
                            Documento doc1 = obtenerDocumentoPorIdentificador(idDoc, false);
                            if (doc1 != null) {
                                //Se restablece la fecha de expiración
                                doc1.setFechaExpiracion(UtilVarios.sumarDiasFecha(new Date(), 10));
                                doc1.setEstadoDocumento(EstadoDocumentoBandeja.RE);
                                doc1.setUltimoUsuario(doc.getUltimoUsuario());
                                doc1.setUltimaModificacion(new Date());
                                //doc1.setUltimaAccion("Documento rechazado por "+UtilesUsuarios.armarNombreCompleto(doc.getUsuario()));
                                generalDAO.update(doc1);
                                doc.setRespuesta("Documento " + idDoc + " modificado.");
                            }
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, "Error al notificar al sistema externo (" + doc.getOrigen() + "/" + doc.getIdentificador() + ")", ex);
                            doc.setRespuesta(ex.getMessage());
                        }
                    }
                }
            }

            return doc;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public void enviarNotificacionDocumento(Documento doc) {

        try {
            String urlRetorno = doc.getRetorno();
            if (!urlRetorno.endsWith("/")) {
                urlRetorno = urlRetorno + "/";
            }
            //Si es una URL, invocarla enviando el identificador del documento
            urlRetorno = urlRetorno + doc.getIdentificador();
            URL obj = new URL(urlRetorno);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Agesic PFEA");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String respuesta = in.readLine();
            in.close();
            doc.setRespuesta(respuesta);

            if (con.getResponseCode() < 200 || con.getResponseCode() > 299) {
                doc.setRespuestaError(true);
            } else {
                doc.setRespuestaError(false);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al notificar al sistema externo (" + doc.getOrigen() + "/" + doc.getIdentificador() + ")", ex);
            doc.setRespuesta(ex.getMessage());
            doc.setRespuestaError(true);
        }
    }

    public Documento guardarDocumentoFirmado(Documento doc) {
        try {
            if (doc == null) {
                return null;
            }

            List<InfoFirma> infolist = firmaSB.obtenerInfoFirmas(doc);
            
            if (infolist == null || infolist.isEmpty()){
                GeneralException g = new GeneralException("El documento no tiene firmas");
                throw g;
            }

            Boolean usuarioExistente = Boolean.FALSE;
            for (InfoFirma in : infolist) {
                if (in.getSerialNumber() != null && in.getSerialNumber().contains(doc.getDebeFirmarUsuario())) {
                    usuarioExistente = Boolean.TRUE;
                    break;
                }
            }

            if (!usuarioExistente) {
                GeneralException g = new GeneralException("Firma inválida. El usuario que debe firmar el documento es " + doc.getDebeFirmarUsuario());
                throw g;
            }

            for (InfoFirma in : infolist) {
                if (!in.isValida()) {
                    GeneralException g = new GeneralException("Firma inválida. " + in.getMensaje());
                    throw g;
                }
            }

            String nombreFirmado = UtilVarios.addCharFirmado(doc.getNombreArchivo());
            Repositorio repositorio = RepositorioFactory.getRepositorio();
            repositorio.guardarDocumento(nombreFirmado, doc.getDocumentoBytes());
            //Si tiene definido el retorno, enviarlo
            if (!StringUtils.isBlank(doc.getRetorno())) {
                if (doc.getRetorno().startsWith("http://") || doc.getRetorno().startsWith("https://")) {
                    enviarNotificacionDocumento(doc);
                } else {
                    //Sino, verificar si existe un documento con el identificador original, y si existe firmarlo
                    if (doc.getRetorno().startsWith("doc://")) {
                        //Si es una URL, invocarla enviando el identificador del documento
                        try {
                            String idDoc = doc.getRetorno().replace("doc://", "");
                            Documento doc1 = obtenerDocumentoPorIdentificador(idDoc, false);
                            if (doc1 == null) {
                                doc.setRespuesta("Documento " + idDoc + " no encontrado.");
                            } else {
                                doc1.setDocumentoBytes(doc.getDocumentoBytes());
                                doc1.setFechaExpiracion(doc.getFechaExpiracion());
                                doc1.setEstadoDocumento(doc.getEstadoDocumento());
                                doc1.setEliminado(doc.getEliminado());
                                doc1.setFirmado(doc.getFirmado());
                                doc1.setUltimoUsuario(doc.getUltimoUsuario());
                                doc1.setUltimaModificacion(new Date());
                                doc1.setUltimaAccion(doc.getUltimaAccion());
                                guardarDocumentoFirmado(doc1);
                                doc.setRespuesta("Documento " + idDoc + " modificado.");
                            }
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, "Error al notificar al sistema externo (" + doc.getOrigen() + "/" + doc.getIdentificador() + ")", ex);
                            doc.setRespuesta(ex.getMessage());
                        }
                    }
                }
            }
            doc.setCodigopac(null); //Si estaba enviado al PAC se "quita"
            doc = (Documento) generalDAO.update(doc);
            return doc;
        } catch (GeneralException g) {
            throw g;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<byte[]> obtenerBytesDocumento(String idDocumento, boolean firmado) {
        Documento documento = obtenerDocumentoPorIdentificador(idDocumento, false);
        if (documento == null) {
            return null;
        }
        Repositorio repositorio = RepositorioFactory.getRepositorio();
        String nombreArchivo;
        if (firmado) {
            nombreArchivo = UtilVarios.addCharFirmado(documento.getNombreArchivo());
        } else {
            nombreArchivo = documento.getNombreArchivo();
        }
        return repositorio.obtenerBytesDocumento(nombreArchivo);
    }

    /**
     * Devuelve el documento SIN incluir los bytes del archivo.
     *
     * @param id
     * @return
     */
    public Documento obtenerDocumentoPorId(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            Documento doc = (Documento) generalDAO.findById(Documento.class, id);
            if (doc == null) {
                return null;
            }
            return doc;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public Documento obtenerDocumentoPorIdentificador(String idUnico, boolean excluirExpirados) {
        if (idUnico == null) {
            return null;
        }
        try {
            List<Documento> docs = generalDAO.findByOneProperty(Documento.class, "identificador", idUnico);
            if (docs == null || docs.isEmpty()) {
                return null;
            }
            Documento d = docs.get(0);

            if (excluirExpirados) {
                Configuracion c = configuracionSB.obtenerCnfPorCodigo("EXPIRAR_DOCUMENTOS_MINUTOS");
                if (c != null && c.getCnfValor() != null) {

                    Integer minutos = Integer.parseInt(c.getCnfValor());

                    Date now = new Date();
                    long MAX_DURATION = TimeUnit.MILLISECONDS.convert(minutos, TimeUnit.MINUTES);

                    long duration = now.getTime() - d.getFechaCreacion().getTime();

                    if (duration >= MAX_DURATION) {
                        return null;
                    }
                }
            }
            return d;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<Documento> obtenerDocumentosVencidos(Integer usuarioId) {
        try {
            List<CriteriaTO> criteriosAnd = new ArrayList<>();
            criteriosAnd.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "eliminado", true));
            criteriosAnd.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuario.id", usuarioId));
            criteriosAnd.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESS, "fechaExpiracion", new Date()));
            CriteriaTO condicion = CriteriaTOUtils.createANDTO(criteriosAnd.toArray(new CriteriaTO[0]));
            String[] orderBy = {"fechaCreacion", "id"};
            boolean[] asc = {false, false};
            List<Documento> docs = generalDAO.findEntityByCriteria(Documento.class, condicion, orderBy, asc, null, null);
            if (docs == null || docs.isEmpty()) {
                return null;
            }
            return docs;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
