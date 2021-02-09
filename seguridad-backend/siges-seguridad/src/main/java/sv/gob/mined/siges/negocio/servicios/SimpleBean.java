/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SimpleBean {

//    @Inject
//    private SimpleRestClient simpleClient;

    @Inject
    private UsuarioBean usuarioBean;

    private static final Logger LOGGER = Logger.getLogger(SimpleBean.class.getName());

//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public void enviarUsuariosSimple(Long rolPk, Long maxResults, Long first) throws GeneralException {
//        try {
//            FiltroUsuario filtro = new FiltroUsuario();
//            filtro.setRol(rolPk);
//            filtro.setMaxResults(maxResults);
//            filtro.setFirst(first);
//            filtro.setOrderBy(new String[]{"usuPk"});
//            filtro.setAscending(new boolean[]{true});
//            List<SgUsuario> usuarios = usuarioBean.obtenerPorFiltro(filtro);
//            LOGGER.log(Level.INFO, "Enviando usuarios: First: "+ first + ". Max: " + maxResults);
//            Integer i = 0;
//            for (SgUsuario s : usuarios) {
//                try {
//                    LOGGER.log(Level.INFO, "Count: "+ i +". ENVIANDO USUARIO. PK: " +s.getUsuPk() + ". Cod: "+ s.getUsuCodigo());
//                    enviarRolesUsuario(s);   
//                } catch (Exception ex) {
//                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//                }
//                i++;
//            }
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
//        }
//    }
//
//    public SpMensajeRespuesta enviarRolesUsuario(SgUsuario entity) throws GeneralException {
//        try {
//
//            if ((entity.getUsuPk() != null) && (entity.getPusPersonaUsuarioRol() == null)) {
//                SgUsuario entity_aux = usuarioBean.obtenerPorId(entity.getUsuPk());
//                entity.setPusPersonaUsuarioRol(entity_aux.getPusPersonaUsuarioRol());
//            }
//
//            SpMensajeRespuesta mr = null;
//            if (BooleanUtils.isNotFalse(entity.getUsuHabilitado())) {
//                SpRolesUsuario rolUsuario = new SpRolesUsuario();
//                SpUsuario usuarioSimple = new SpUsuario();
//                usuarioSimple.setUsuario(entity.getUsuCodigo());
//                usuarioSimple.setNombres(entity.getUsuNombre());
//                usuarioSimple.setEmail(entity.getUsuEmail());
//                List<SpContexto> listcon = new ArrayList();
//
//                if (entity.getPusPersonaUsuarioRol() != null) {
//                    for (SgUsuarioRol usuRol : entity.getPusPersonaUsuarioRol()) {
//                        SpContexto con = new SpContexto();
//                        con.setRol(usuRol.getPurRol().getRolCodigo());
//                        con.setAmbito(usuRol.getPurContexto().getConAmbito().toString());
//                        con.setContexto(usuRol.getPurContexto().getContextoId());
//                        listcon.add(con);
//                    }
//
//                }
//                usuarioSimple.setContexto(listcon);
//                rolUsuario.setUsuario(usuarioSimple);
//                rolUsuario.setToken(System.getProperty("service." + ConstantesServicioRest.SERVICIO_SIMPLE + ".token"));
//                mr = simpleClient.enviarRolesUsuario(rolUsuario);
//                MensajeRespuestaValidacionNegocio.validar(mr);
//            }
//            return mr;
//        } catch (BusinessException be) {
//            throw be;
//        } catch (Exception ex) {
//            throw new TechnicalException(ex);
//        }
//        // return null;
//    }
//
//    public SpMensajeRespuesta eliminarRolesUsuario(String codigoUsu) throws GeneralException {
//        try {
//            SpUsuarioEliminar rolUsuario = new SpUsuarioEliminar();
//            rolUsuario.setToken(System.getProperty("service." + ConstantesServicioRest.SERVICIO_SIMPLE + ".token"));
//            rolUsuario.setUsuario(codigoUsu);
//
//            SpMensajeRespuesta mr = simpleClient.eliminarUsuario(rolUsuario);
//
//            if (!mr.getEstado().equals("404") && !mr.getEstado().equals("200")) {
//                MensajeRespuestaValidacionNegocio.validar(mr);
//            }
//            return mr;
//        } catch (BusinessException be) {
//            throw be;
//        } catch (Exception ex) {
//            throw new TechnicalException(ex);
//        }
//        // return null;
//    }

}
