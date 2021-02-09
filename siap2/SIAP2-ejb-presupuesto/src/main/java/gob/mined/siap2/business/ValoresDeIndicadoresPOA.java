/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.datatypes.DataMetaIndicadorPOA;
import gob.mined.siap2.business.datatypes.DataTipoIndicadorPOA;
import gob.mined.siap2.business.datatypes.DataVerIndicadorPOA;
import gob.mined.siap2.business.datatypes.DataVerIndicadorTipo;
import gob.mined.siap2.business.datatypes.DataVerValoresIndicadoresPOA;
import gob.mined.siap2.business.datatypes.DataVerValoresValor;
import gob.mined.siap2.business.datatypes.DataVerValoresValorPOA;
import gob.mined.siap2.business.datatypes.DataVerValoresValorUTPOA;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.data.impl.ValoresDeIndicador;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAOUT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos asociados a valores de indicadores.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ValoresDeIndicadoresPOA")
@LocalBean
public class ValoresDeIndicadoresPOA {
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

   // @Inject
   // @JPADAO
   // private ValorDeIndicadorDAO valorDeIndicadorDAO;
    
    @Inject
    @JPADAO
    private POADAOUT poadao;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
   /**
     * Método encargado para la visualización de los valores de los indicadores
     *
     * @param unidadesTecnicas
     * @param idAnioFiscal
     * @return
     */
    public List<DataVerIndicadorPOA> getVisualizacionDeValoresDeIndicadoresPOA(Integer idAnioFiscal, Integer idProgramaInstitucional) {
        try {
            Map<Categoria, DataVerIndicadorPOA> map = new LinkedHashMap();

            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            boolean filtraPorPrograma = (idProgramaInstitucional != null);
            List<POA> poas = new LinkedList();
            List<Integer> poasAnalizados = new LinkedList<>();
            if(filtraPorPrograma) {
                poas = poadao.getPOAParaValoresIndicadoresAnioAndPrograma(idAnioFiscal, idProgramaInstitucional);
            } else {
                poas = poadao.getPOAParaValoresIndicadores(idAnioFiscal);
            }
            
            for (POA poa : poas) {
                if (!poasAnalizados.contains(poa.getId())) {
                    poasAnalizados.add(poa.getId());
                    List<Integer> totalesMeta = new ArrayList();
                    List<Integer> totalesValores = new ArrayList();
                    for(POAMetaAnual meta : poa.getMetasAnuales()) {
                        addElementToVerValoresIndicadores(map, meta, poa, poa.getUnidadTecnica(), anioFiscal, totalesMeta,totalesValores);
                    }
                }
            }

            List<DataVerIndicadorPOA> l = new LinkedList<>();
            for (Map.Entry<Categoria, DataVerIndicadorPOA> entry : map.entrySet()) {
                DataVerIndicadorPOA tipoIndicador = entry.getValue();
                l.add(tipoIndicador);

                //se calculan los totales, esto se tendria que hacer a medida que se agregan
                for (DataVerValoresIndicadoresPOA valorIndicador : tipoIndicador.getIndicadores()) {
                    valorIndicador.setTotalValor(0);
                    valorIndicador.setTotalMeta(0);
                    for (DataVerValoresValorPOA valor : valorIndicador.getValores()) {
                        if (valor.getMeta() != null) {
                            valorIndicador.setTotalMeta(valorIndicador.getTotalMeta() + valor.getMeta());
                        }
                        if (valor.getValor() != null) {
                            valorIndicador.setTotalValor(valorIndicador.getTotalValor() + valor.getValor());
                        }
                    }

                    //ahora se suman totales a nivel de ut
                    for (DataVerValoresValorUTPOA valorUT : valorIndicador.getDesgloce()) {
                        valorUT.setTotalValor(0);
                        valorUT.setTotalMeta(0);
                        for (DataVerValoresValorPOA valor : valorUT.getValores()) {
                            if (valor.getMeta() != null) {
                                valorUT.setTotalMeta(valorUT.getTotalMeta() + valor.getMeta());
                            }
                            if (valor.getValor() != null) {
                                valorUT.setTotalValor(valorUT.getTotalValor() + valor.getValor());
                            }
                        }
                    }
                }
            }

            //se ordena la lista por tipo de indicadores
            Collections.sort(l, new Comparator<DataVerIndicadorPOA>() {
                @Override
                public int compare(DataVerIndicadorPOA o1, DataVerIndicadorPOA o2) {
                    return o1.getTipoIndicador().getNombre().compareTo(o2.getTipoIndicador().getNombre());
                }
            });
            Collections.sort(l, new Comparator<DataVerIndicadorPOA>() {
                @Override
                public int compare(DataVerIndicadorPOA o1, DataVerIndicadorPOA o2) {
                    if (o1.getTipoIndicador().getOrden() != null && o2.getTipoIndicador().getOrden() != null) {
                        return o1.getTipoIndicador().getOrden().compareTo(o2.getTipoIndicador().getOrden());
                    }
                    return 0;
                }
            });
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
     * Este método agrega los valores a visualizar para un indicador en
     * particular.
     *
     * @param map
     * @param element
     * @param proyecto
     * @param ut
     * @param anioFiscal
     */
    private void addElementToVerValoresIndicadores(Map<Categoria, DataVerIndicadorPOA> map, POAMetaAnual element, POA poa, UnidadTecnica ut, AnioFiscal anioFiscal, List<Integer> totalesMeta,List<Integer> totalesValores) {
        Indicador indicador = element.getIndicador();
       
        DataVerIndicadorPOA tipoIndicador = map.get(indicador.getTipo());
        if (tipoIndicador == null) {
            tipoIndicador = new DataVerIndicadorPOA();
            tipoIndicador.setIndicadores(new LinkedList());
            indicador.getTipo().setTipoSeguimiento(TipoSeguimiento.TRIMESTRAL);
            tipoIndicador.setTipoIndicador(indicador.getTipo());

            map.put(indicador.getTipo(), tipoIndicador);
        }

        DataVerValoresIndicadoresPOA dataValoresIndicador = buscarIndicador(tipoIndicador, anioFiscal, indicador);
        if (dataValoresIndicador == null) {
            dataValoresIndicador = new DataVerValoresIndicadoresPOA();
            dataValoresIndicador.setValores(new LinkedList());
            dataValoresIndicador.setDesgloce(new LinkedList<DataVerValoresValorUTPOA>());
            dataValoresIndicador.setAnioFiscal(anioFiscal);
            dataValoresIndicador.setIndicador(indicador);
            dataValoresIndicador.setAlcanceYLimitante(element.getAlcanceLimitaciones());
            dataValoresIndicador.setMedioVerificacion(element.getMedioVerificacionIndicador());
            
            tipoIndicador.getIndicadores().add(dataValoresIndicador);
        }

        DataVerValoresValorUTPOA dataValoresUT = new DataVerValoresValorUTPOA();
        dataValoresUT.setAnioFiscal(anioFiscal);
        dataValoresUT.setPoa(poa);
        dataValoresUT.setUt(ut);
        dataValoresUT.setValores(new LinkedList<DataVerValoresValorPOA>());

        dataValoresIndicador.getDesgloce().add(dataValoresUT);
        
        List<DataVerValoresValorPOA> valores = new LinkedList<>();
       
        DataVerValoresValorPOA val1 = new DataVerValoresValorPOA();
        val1.setMeta(element.getProgramaPrimerTrimestre() != null ? element.getProgramaPrimerTrimestre() : 0);
        val1.setValor(element.getProgramaPrimerTrimestreReal() != null ? element.getProgramaPrimerTrimestreReal() : 0);
        valores.add(val1);
        
        
        DataVerValoresValorPOA val2 = new DataVerValoresValorPOA();
        val2.setMeta(element.getProgramaSegundoTrimestre() != null ? element.getProgramaSegundoTrimestre() : 0);
        val2.setValor(element.getProgramaSegundoTrimestreReal()!= null ? element.getProgramaSegundoTrimestreReal() : 0);
        valores.add(val2);
        
        DataVerValoresValorPOA val3 = new DataVerValoresValorPOA();
        val3.setMeta(element.getProgramaTercerTrimestre() != null ? element.getProgramaTercerTrimestre() : 0);
        val3.setValor(element.getProgramaTercerTrimestreReal() != null ? element.getProgramaTercerTrimestreReal() : 0);
        valores.add(val3);
        
        DataVerValoresValorPOA val4 = new DataVerValoresValorPOA();
        val4.setMeta(element.getProgramaCuartoTrimestre() != null ? element.getProgramaCuartoTrimestre() : 0);
        val4.setValor(element.getProgramaCuartoTrimestreReal() != null ? element.getProgramaCuartoTrimestreReal() : 0);
        valores.add(val4);
       
        
        for (int i = 0; i < valores.size(); i++) {
            DataVerValoresValorPOA valor = valores.get(i);

            //si aun no se ha creado el valor(meta o valor) dentro del indicador, sino lo crea
            DataVerValoresValorPOA dataValorIndicador = null;
            if (i < dataValoresIndicador.getValores().size()) {
                dataValorIndicador = dataValoresIndicador.getValores().get(i);
            } else {
                dataValorIndicador = new DataVerValoresValorPOA();
                dataValorIndicador.setMeta(0);
                dataValorIndicador.setValor(0);
                dataValoresIndicador.getValores().add(dataValorIndicador);
            }

            DataVerValoresValorPOA dataValorUt = new DataVerValoresValorPOA();
            dataValorUt.setMeta(valor.getMeta());
            dataValorUt.setValor(valor.getValor());
            dataValorUt.setAlcanceYLimitante(valor.getAlcanceYLimitante());
            dataValorUt.setMedioVerificacion(valor.getMedioVerificacion());
            dataValoresUT.getValores().add(dataValorUt);

            if (valor.getMeta() != null) {
                dataValorIndicador.setMeta(dataValorIndicador.getMeta() + valor.getMeta());
            }
            if (valor.getValor() != null) {
                dataValorIndicador.setValor(dataValorIndicador.getValor() + valor.getValor());
            }
        }
    }
    
    /**
     * Este método obtiene todos los indicadores de un tipo para un año dado.
     *
     * @param tipoIndicador
     * @param anioFiscal
     * @param indicador
     * @return
     */
    public DataVerValoresIndicadoresPOA buscarIndicador(DataVerIndicadorPOA tipoIndicador, AnioFiscal anioFiscal, Indicador indicador) {
        for (DataVerValoresIndicadoresPOA data : tipoIndicador.getIndicadores()) {
            if (data.getIndicador().equals(indicador) && data.getAnioFiscal().equals(anioFiscal)) {
                return data;
            }
        }
        return null;
    }
    
}
