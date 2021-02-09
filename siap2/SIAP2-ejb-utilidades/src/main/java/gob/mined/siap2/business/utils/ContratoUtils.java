package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.DataDistribuccionProgramacionPagosContrato;
import gob.mined.siap2.business.datatype.DistribucionMontoAdjudicado;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosInsumos;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosItem;
import gob.mined.siap2.business.datatype.DistribucionProgramacionPagosMes;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionItem;
import gob.mined.siap2.entities.data.impl.RelacionProAdqItemInsumo;
import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.exceptions.BusinessException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
public class ContratoUtils {

    /**
     * Método que se encarga de calcular la distribución por fuentes de un
     * insumo adjudicado. La calcula en base al monto comprometido
     *
     * @param poInsumo
     * @param montoAdjudicado
     * @return
     */
    public static List<DistribucionMontoAdjudicado> getDistribucionAdjudicado(POInsumos poInsumo, BigDecimal montoAdjudicado) {
        List<DistribucionMontoAdjudicado> res = new LinkedList();

        BigDecimal totalCertificado = poInsumo.getMontoTotalCertificado();
        for (POMontoFuenteInsumo fuente : poInsumo.getMontosFuentes()) {
            if (fuente.getCertificadoDisponibilidadPresupuestariaAprobada() != null && fuente.getCertificadoDisponibilidadPresupuestariaAprobada().equals(Boolean.TRUE)) {
                //para calcular cuanto aporta la fuente realiza una regla de tres
                BigDecimal aporteFuente = BigDecimal.ZERO;
                if (fuente.getCertificado() != null && fuente.getCertificado().compareTo(BigDecimal.ZERO) != 0) {
                    //fixme: arreglar redondeo
                    aporteFuente = fuente.getCertificado().multiply(montoAdjudicado);
                    aporteFuente = aporteFuente.divide(totalCertificado, 2, BigDecimal.ROUND_HALF_UP);
                }

                //aniade la fuente a la distribuccion
                DistribucionMontoAdjudicado dist = new DistribucionMontoAdjudicado();
                dist.setFuente(fuente);
                dist.setMonto(aporteFuente);
                res.add(dist);

            }
        }
        return res;

    }

    /**
     * Método que se encarga de calcular la distribución por fuentes en base a
     * la programación de pagos.
     *
     * @param contrato
     * @return
     */
    public static List<DataDistribuccionProgramacionPagosContrato> getDistribuccionFinanciera(CompromisoPresupuestario compromiso) {
        List<DataDistribuccionProgramacionPagosContrato> res = new LinkedList();

        //trae los contratos dependiendo del tipo
        List<ContratoOC> contratosEnCompromiso = new LinkedList();
        if (compromiso != null) {
            if (compromiso.getTipo() == TipoCompromisoPresupuestario.PROCESO) {
                CompromisoPresupuestarioProceso compromisoProceso = (CompromisoPresupuestarioProceso) compromiso;
                contratosEnCompromiso.addAll(compromisoProceso.getProcesoAdquisicion().getContratos());
            } else {
                CompromisoPresupuestarioModificativa compromisoModificativa = (CompromisoPresupuestarioModificativa) compromiso;
                contratosEnCompromiso.add(compromisoModificativa.getModificativaContrato().getContratoOC());
            }
        }

        for (ContratoOC contrato : contratosEnCompromiso) {

            DataDistribuccionProgramacionPagosContrato distContrato = new DataDistribuccionProgramacionPagosContrato();
            distContrato.setContrato(contrato);
            distContrato.setDistribuccionMeses(new LinkedList());
            distContrato.setTotales(new LinkedList());

            //saca el flujo de caja mensual dependiendo del tipo
            Set<FlujoCajaAnio> programacionPagos = null;
            if (compromiso != null) {
                if (compromiso.getTipo() == TipoCompromisoPresupuestario.PROCESO) {
                    programacionPagos = contrato.getProgramacionPagosProceso();
                } else {
                    CompromisoPresupuestarioModificativa compromisoModificativa = (CompromisoPresupuestarioModificativa) compromiso;
                    programacionPagos = compromisoModificativa.getModificativaContrato().getProgramacionPagos();
                }
            }

            for (FlujoCajaAnio programacionAnio : programacionPagos) {
                for (POFlujoCajaMenusal programacionMes : programacionAnio.getFlujoCajaMenusal()) {
                    if (programacionMes.getMonto() != null && programacionMes.getMonto().compareTo(BigDecimal.ZERO) != 0) {

                        DistribucionProgramacionPagosMes distMes = new DistribucionProgramacionPagosMes();
                        distMes.setAnio(programacionAnio.getAnio());
                        distMes.setMes(programacionMes.getMes());
                        distMes.setDistribuccionItems(new LinkedList());
                        distMes.setTotales(new LinkedList());
                        distContrato.getDistribuccionMeses().add(distMes);

                        for (ProcesoAdquisicionItem item : contrato.getItems()) {
                            DistribucionProgramacionPagosItem distItem = new DistribucionProgramacionPagosItem();
                            distItem.setItem(item);
                            distItem.setDistribuccionInsumos(new LinkedList());
                            distItem.setTotales(new LinkedList<DistribucionMontoAdjudicado>());
                            distMes.getDistribuccionItems().add(distItem);

                            for (RelacionProAdqItemInsumo relInsumo : item.getRelItemInsumos()) {
                                //chequea que el insumo no venga de una modificativa del contrato. Solo toma en cuenta los insumos que se crearon en el proceso
                                if (compromiso != null) {
                                    if (insumoPerteneceACompromisoPresupuestario(relInsumo.getInsumo().getPoInsumo(), compromiso)) {

                                        DistribucionProgramacionPagosInsumos distInsumo = new DistribucionProgramacionPagosInsumos();
                                        distInsumo.setInsumo(relInsumo);
                                        distInsumo.setDistribuccion(new LinkedList());
                                        distItem.getDistribuccionInsumos().add(distInsumo);

                                        //para ver cuanto le corresponde a ese insumo en el mes hace una regla de tres                        
                                        //fixme: arreglar redondeo
                                        BigDecimal montoAdjudicadoMes = relInsumo.getMontoTotalAdjudicado().multiply(programacionMes.getMonto());
                                        montoAdjudicadoMes = montoAdjudicadoMes.divide(contrato.getMontoAdjudicado(), 2, BigDecimal.ROUND_HALF_UP);
                                        List<DistribucionMontoAdjudicado> dist = getDistribucionAdjudicado(relInsumo.getInsumo().getPoInsumo(), montoAdjudicadoMes);
                                        distInsumo.setDistribuccion(dist);

                                        sumarDistribucion(distItem.getTotales(), distInsumo.getDistribuccion());

                                    }
                                }
                            }
                            sumarDistribucion(distMes.getTotales(), distItem.getTotales());
                        }
                        sumarDistribucion(distContrato.getTotales(), distMes.getTotales());
                    }
                }
            }

            //se ordenan los meses
            Collections.sort(distContrato.getDistribuccionMeses(), new Comparator<DistribucionProgramacionPagosMes>() {
                @Override
                public int compare(DistribucionProgramacionPagosMes o1, DistribucionProgramacionPagosMes o2) {
                    return o1.getMes().compareTo(o2.getMes());
                }
            });
            Collections.sort(distContrato.getDistribuccionMeses(), new Comparator<DistribucionProgramacionPagosMes>() {
                @Override
                public int compare(DistribucionProgramacionPagosMes o1, DistribucionProgramacionPagosMes o2) {
                    return o1.getAnio().compareTo(o2.getAnio());
                }
            });

            res.add(distContrato);
        }

        return res;

    }

    private static void sumarDistribucion(List<DistribucionMontoAdjudicado> colecion, List<DistribucionMontoAdjudicado> sumar) {
        for (DistribucionMontoAdjudicado asumar : sumar) {
            DistribucionMontoAdjudicado item = findFuente(colecion, asumar.getFuente());
            if (item == null) {
                item = new DistribucionMontoAdjudicado();
                item.setFuente(asumar.getFuente());
                item.setMonto(BigDecimal.ZERO);
                colecion.add(item);
            }
            item.setMonto(item.getMonto().add(asumar.getMonto()));

        }

    }

    public static DistribucionMontoAdjudicado findFuente(List<DistribucionMontoAdjudicado> colecion, POMontoFuenteInsumo fuente) {
        for (DistribucionMontoAdjudicado iter : colecion) {
            if (iter.getFuente().equals(fuente)) {
                return iter;
            }
        }
        return null;
    }

    public static void validarProgramacionPagosParaContrato(ContratoOC contrato, Set<FlujoCajaAnio> nuevaProgramacionPagos) {
        //si el contrato tiene fecha de inicio verifica que la programacion de pagos sea mayor a la fecha de inicio
        if (contrato.getFechaInicio() != null) {
            Calendar fechaInicioContrato = new GregorianCalendar();
            fechaInicioContrato.setTime(contrato.getFechaInicio());
            Integer anioInicioContrato = fechaInicioContrato.get(Calendar.YEAR);
            Integer mesInicioContrato = fechaInicioContrato.get(Calendar.MONTH) + 1;
            //que todos los pagos se hagan luego de la fecha de inicio del contrato

            for (FlujoCajaAnio fc : nuevaProgramacionPagos) {
                if (fc.getAnio() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_PROGRAMACION_PAGO_SIN_ANIO);
                    throw b;
                }
                if (fc.getAnio().compareTo(anioInicioContrato) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                    throw b;
                }
                //Si es el mismo año verifico que los meses sean posteriores al del inicio del contrato
                if (fc.getAnio().equals(anioInicioContrato)) {
                    for (POFlujoCajaMenusal fcm : fc.getFlujoCajaMenusal()) {
                        if (fcm.getMonto() != null && fcm.getMonto().compareTo(BigDecimal.ZERO) > 0 && fcm.getMes().compareTo(mesInicioContrato) < 0) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_PAGOS_DEBEN_SER_POSTERIORES_A_FECHA_INCIO_CONTRATO);
                            throw b;
                        }
                    }
                }
            }
        }

        //que todas tengan años
        for (FlujoCajaAnio fc : nuevaProgramacionPagos) {
            if (fc.getAnio() == null || fc.getAnio().intValue() <= 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_PROGRAMACION_PAGO_SIN_ANIO);
                throw b;
            }
        }

        //que no existan anios repetidos
        for (FlujoCajaAnio fc1 : nuevaProgramacionPagos) {
            for (FlujoCajaAnio fc2 : nuevaProgramacionPagos) {
                if (!fc1.equals(fc2) && fc1.getAnio().equals(fc2.getAnio())) {
                    String[] params = {fc1.getAnio().toString()};
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTEN_DOS_PROGRAMACIONES_PAGO_PARA_ANIO_0, params);
                    throw b;
                }
            }
        }

        //que no se pase del monto del contrato
        BigDecimal totalProgramado = BigDecimal.ZERO;
        for (FlujoCajaAnio anio : nuevaProgramacionPagos) {
            for (POFlujoCajaMenusal mes : anio.getFlujoCajaMenusal()) {
                if (mes.getMonto() != null) {
                    totalProgramado = totalProgramado.add(mes.getMonto());
                }
            }
        }
        if (totalProgramado.compareTo(contrato.getMontoAdjudicado()) > 0) {
            BusinessException b = new BusinessException();
            String[] params = {totalProgramado.toString(), contrato.getMontoAdjudicado().toString()};
            b.addError(ConstantesErrores.ERR_MONTO_DE_PROGRAMACION_PAGOS_0_DISTINTO_DEL_MOTNO_ADJUDICADO_1, params);
            throw b;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (FlujoCajaAnio fc : nuevaProgramacionPagos) {
            for (POFlujoCajaMenusal pago : fc.getFlujoCajaMenusal()) {
                if (pago.getMonto() == null) {
                    pago.setMonto(BigDecimal.ZERO);
                }
                total = total.add(pago.getMonto());
            }
        }

        if (contrato.getMontoAdjudicado().compareTo(total) != 0) {
            String[] params = {total.toString(), contrato.getMontoAdjudicado().toString()};
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_LA_SUMA_DE_PROGRAMACION_PAGOS_0_NO_COINCIDE_CON_MONTO_ADJ_DEL_CONTRATO_1, params);
            throw b;
        }
    }

    /**
     * Método que se encarga de retornar el ítem al que va a ser asociado el
     * insumo dentro de una modificativa
     *
     * @param contrato
     * @param pOInsumos
     * @return
     */
    public static ProcesoAdquisicionItem getItemDeInsumoParaModificativa(ContratoOC contrato, POInsumos pOInsumos) {
        //ordena la colección para retornar siempre lo mismo
        Collections.sort(contrato.getItems(), new Comparator<ProcesoAdquisicionItem>() {
            @Override
            public int compare(ProcesoAdquisicionItem o1, ProcesoAdquisicionItem o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        for (ProcesoAdquisicionItem item : contrato.getItems()) {
            for (RelacionProAdqItemInsumo rel : item.getRelItemInsumos()) {
                if (rel.getInsumo().getInsumo().equals(pOInsumos.getInsumo())) {
                    return item;
                }
            }
        }
        return null;

    }

    /**
     * METOOD QUE RETORNA TRUE SI UN POINSUMO PERTENECE A UN COMPROMISO
     * PRESUPUESTARIO
     *
     * @param pOInsumos
     * @param compromisoPresupestario
     * @return
     */
    public static boolean insumoPerteneceACompromisoPresupuestario(POInsumos pOInsumos, CompromisoPresupuestario compromisoPresupestario) {

        if (compromisoPresupestario.getTipo() == TipoCompromisoPresupuestario.MODIFICATIVA) {
            CompromisoPresupuestarioModificativa compromisoModificativa = (CompromisoPresupuestarioModificativa) compromisoPresupestario;
            if (pOInsumos.getModificativa() != null && pOInsumos.getModificativa().equals(compromisoModificativa.getModificativaContrato())) {
                return true;
            }

        } else {
            CompromisoPresupuestarioProceso compromisoProceso = (CompromisoPresupuestarioProceso) compromisoPresupestario;
            if (pOInsumos.getProcesoInsumo() != null && pOInsumos.getProcesoInsumo().getProcesoAdquisicion().equals(compromisoProceso.getProcesoAdquisicion())) {
                return true;
            }
        }
        return false;

    }

}
