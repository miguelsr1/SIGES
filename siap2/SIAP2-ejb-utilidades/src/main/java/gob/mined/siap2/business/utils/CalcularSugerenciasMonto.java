/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoParipassuTramo;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoParipassu;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mariuszgromada.math.mxparser.Expression;

/**
 *
 * @author Sofis Solutions
 */
public class CalcularSugerenciasMonto {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    public static boolean tieneSugerencia(POMontoFuenteInsumo monto) {
        return true;
    }

    public static BigDecimal caclularSugerenciaPOG(Proyecto proyecto, POMontoFuenteInsumo monto, BigDecimal montoTotal) {
        try {

            if (montoTotal == null) {
                return BigDecimal.ZERO;
            }
            ProyectoAporte aporte = (ProyectoAporte) monto.getFuente();
            ProyectoCategoriaConvenio proyectoCategoria = ProyectoUtils.getProyectoCategoria(proyecto, aporte.getCategoriaConvenio());

            if (aporte.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_PORCENTAJE) {
                //SI ES POR PORCENTAJE
                if (proyectoCategoria.getTipoParipassu() == TipoParipassu.PORCENTAJE) {
                    return calcularPorcentaje(aporte.getMontoParipassu(), montoTotal);
                } //SI ES POR PARIPASSU
                else if (proyectoCategoria.getTipoParipassu() == TipoParipassu.FORMULA) {
                    Map<String, String> formulas = new LinkedHashMap<>();
                    for (POMontoFuenteInsumo montoFuente : monto.getInsumo().getMontosFuentes()) {
                        String formulaFuente = aporte.getFormulaParipassu();
                        formulas.put("MONTO_" + montoFuente.getFuente().getFuenteRecursos().getCodigo(), remplazarMontoTotal(formulaFuente, montoTotal));
                    }
                    String formulaACalcular = remplazarMontoTotal(aporte.getFormulaParipassu(), montoTotal);
                    return evaluarFormula(monto, formulas, formulaACalcular, montoTotal);

                }

            } else if (aporte.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_TRAMOS) {
                //PRIMERO HAY QUE RESOLVER EN QUE TRAMO SE ENCUENTRA
                ProyectoAporteTramoTramo tramoActual = monto.getInsumo().getTramo();

                ProyectoParipassuTramo paripassuTramo = ProyectoUtils.getParipassuTramo(tramoActual, monto.getFuente().getFuenteRecursos());

                if (proyectoCategoria.getTipoParipassu() == TipoParipassu.PORCENTAJE) {
                    return calcularPorcentaje(paripassuTramo.getMontoParipassu(), montoTotal);
                } else if (proyectoCategoria.getTipoParipassu() == TipoParipassu.FORMULA) {
                    Map<String, String> formulas = new LinkedHashMap<>();
                    for (ProyectoParipassuTramo iterTramo : tramoActual.getParipassus()) {
                        String formulaFuente = iterTramo.getFormulaParipassu();
                        formulas.put("MONTO_" + iterTramo.getFuenteRecursos().getCodigo(), remplazarMontoTotal(formulaFuente, montoTotal));
                    }
                    String formulaACalcular = remplazarMontoTotal(aporte.getFormulaParipassu(), montoTotal);
                    return evaluarFormula(monto, formulas, formulaACalcular, montoTotal);
                }

            }

        } catch (Throwable e) {
        }

        return BigDecimal.ZERO;
    }

    /**
     * Método que se encarga de calcular el porcentaje
     *
     * @param montoParipasssu
     * @param montoInsumo
     * @return
     */
    public static BigDecimal calcularPorcentaje(BigDecimal montoParipasssu, BigDecimal montoInsumo) {
        return NumberUtils.porcentaje(montoParipasssu, montoInsumo, RoundingMode.DOWN);
    }

    public static BigDecimal evaluarFormula(POMontoFuenteInsumo monto, Map<String, String> formulas, String formulaACalcular, BigDecimal montoTotal) throws Exception {
        List<String> dependencias = new LinkedList<>();
        dependencias.add("MONTO_" + monto.getFuente().getFuenteRecursos().getCodigo());
        calcularDependencias(formulas, formulaACalcular, dependencias);
        Collections.reverse(dependencias);

        Map<String, BigDecimal> mapaResultados = new HashMap();
        BigDecimal ultimoResltado = null;
        for (String dependencia : dependencias) {
            String formula = formulas.get(dependencia);
            for (Map.Entry<String, BigDecimal> entry : mapaResultados.entrySet()) {
                formula = formula.replace(entry.getKey(), entry.getValue().toString());
            }

            Expression ex = new Expression(formula);
            Double res = ex.calculate();
            ultimoResltado = new BigDecimal(res);
            mapaResultados.put(dependencia, ultimoResltado);
        }

        return ultimoResltado;
    }

    public static String remplazarMontoTotal(String formula, BigDecimal motnoTotal) {
        return formula.replace("MONTO ", motnoTotal.toString());
    }

    /**
     *
     * @param formulas mapa que tiene el código de la formula ejemplo monto y el
     * valor
     * @param formula a la que hay que calcular dependencias
     * @param utilizados la lista de dependencias
     * @throws Exception
     */
    public static void calcularDependencias(Map<String, String> formulas, String formula, List<String> utilizados) throws Exception {
        List<String> dependencias = obtenerVariablesEnExpresion(formula);

        //primero se añade y se chequea circularidad
        for (String dependencia : dependencias) {
            if (utilizados.contains(dependencia)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_CIRCULARIDAD_EN_FORMULA);
                throw b;
            }
            utilizados.add(dependencia);
        }

        //luego se llama al paso recursivo
        for (String dependencia : dependencias) {
            calcularDependencias(formulas, formulas.get(dependencia), utilizados);
        }
    }

    /**
     * Este método permite obtener las variables en una expresión. Las variables
     * pueden tener como nombres letras, underscore y dígitos.
     *
     *
     */
    public static List<String> obtenerVariablesEnExpresion(String expresion) {
        String pattern = "(?:^|(?<=[=+\\-*/()]))\\s*([a-z_][a-z_0-9]*)\\s*(?:$|(?=[=+\\-*/()]))";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(expresion);
        List<String> resultado = new ArrayList();
        while (m.find()) {
            resultado.add(m.group(1));

        }
        return resultado;
    }

}
