/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.GanttTask;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Sofis Solutions
 */
public class ReportesUtils implements Serializable {

    private static final Logger logger = Logger.getLogger(ReportesUtils.class.getName());

    public ReportesUtils() {
    }

    public static byte[] generarBytesPDF(Object o, Map parcol, JasperReport jasperReport) throws GeneralException {

        List col = new ArrayList<>();

        if (o instanceof List) {
            col = (List<Object>) o;
        } else {

            col.add(o);
        }
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(col);

        try {
            if (jasperReport != null) {

                return JasperRunManager.runReportToPdf(jasperReport, parcol, ds);
            } else {
                BusinessException bs = new BusinessException();
                bs.addError(ConstantesErrores.ERROR_REPORTE_NO_ENCONTRADO);
                throw bs;
            }

        } catch (JRException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            logger.log(Level.SEVERE, e.getMessage(), printWriter);
            logger.log(Level.SEVERE, e.getMessage(), e);
            BusinessException bs = new BusinessException(e.getMessage());
            bs.addError(ConstantesErrores.ERROR_REPORTE_GENERANDO);
            throw bs;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;
        }

    }

    public static void generarPDF(Object o, Map parcol, JasperReport jasperReport, File file) throws GeneralException {
        try {
            List col = new ArrayList<>();
            col.add(o);

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(col);
            JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parcol, ds);

            JasperExportManager.exportReportToPdfFile(jprint, file.getAbsolutePath());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            // display stack trace in the browser
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            logger.log(Level.SEVERE, e.getMessage(), printWriter);
            logger.log(Level.SEVERE, e.getMessage(), e);
            BusinessException bs = new BusinessException(e.getMessage());
            bs.addError(ConstantesErrores.ERROR_REPORTE_GENERANDO);
            throw bs;

        }

    }

    public static String getText(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }

    public static BigDecimal getBigDecimal(BigDecimal number) {
        if (number != null) {
            number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return number;
    }

    public static String dateToString(Date d) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(d);
    }

    public static String dateToStringCompleto(Date d) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat formatterCompleto = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return formatterCompleto.format(d);
    }

    public static String convertToHora(Date d) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat formatterHora = new SimpleDateFormat("hh:mm a");
        return formatterHora.format(d);
    }

    public static Date getFechaTareaGantt(Gantt gantt, TipoTarea tipoTarea) {
        if (gantt != null && gantt.getTasks() != null) {
            for (GanttTask t : gantt.getTasks()) {
                if (t.getTipoTarea() == tipoTarea) {
                    return new Date(t.getEnd());
                }
            }
        }
        return null;
    }

    public static String getIdProceso(POInsumos insumo) {
        if (insumo.getProcesoInsumo() == null) {
            return "";
        }
        if (insumo.getProcesoInsumo().getProcesoAdquisicion() == null) {
            return "";
        }
        return insumo.getProcesoInsumo().getProcesoAdquisicion().getSecuenciaAnio() + " " + insumo.getProcesoInsumo().getProcesoAdquisicion().getSecuenciaNumero();
    }

    public static String getNombreUsuario(SsUsuario usuario) {
        String nombre = "";
        if (usuario != null) {
            if (!TextUtils.isEmpty(usuario.getUsuPrimerNombre())) {
                nombre = usuario.getUsuPrimerNombre() + " ";
            }
            if (!TextUtils.isEmpty(usuario.getUsuSegundoNombre())) {
                nombre = nombre + usuario.getUsuSegundoNombre() + " ";
            }
            if (!TextUtils.isEmpty(usuario.getUsuPrimerApellido())) {
                nombre = nombre + usuario.getUsuPrimerApellido() + " ";
            }
            if (!TextUtils.isEmpty(usuario.getUsuSegundoApellido())) {
                nombre = nombre + usuario.getUsuSegundoApellido();
            }
        }
        return nombre;
    }

    public static String getSiNo(Boolean valor) {
        if (valor == null) {
            return "";
        }
        return (valor ? "Si" : "No");
    }

    public static String getString(String valor) {
        if (TextUtils.isEmpty(valor)) {
            return "";
        }
        return (valor);
    }

    private static DecimalFormat currencyFormatter;

    static {
        currencyFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("es", "SV"));
        DecimalFormatSymbols symbols = currencyFormatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(""); // Don't use null.
        currencyFormatter.setDecimalFormatSymbols(symbols);
    }

    public static String getNumber(BigDecimal numero) {
        if (numero == null) {
            return "";
        }
        return currencyFormatter.format(numero);

    }

    public static String getNumber(Integer i) {
        if (i == null) {
            return "";
        }
        return String.valueOf(i);
    }

    public static String getRazonSocialONombre(Proveedor p) {
        if (p == null) {
            return "";
        }
        if (!TextUtils.isEmpty(p.getRazonSocial())) {
            return p.getRazonSocial();
        } else {
            return p.getNombreComercial();
        }
    }

    public static String getNombreFirmante(SsUsuario usuario) {
        String firmante = usuario.getUsuPrefijo() != null ? usuario.getUsuPrefijo() : "";
        firmante += usuario.getUsuPrimerNombre() != null ? " " + usuario.getUsuPrimerNombre().toUpperCase() : "";
        firmante += usuario.getUsuSegundoNombre() != null ? " " + usuario.getUsuSegundoNombre().toUpperCase() : "";
        firmante += usuario.getUsuPrimerApellido() != null ? " " + usuario.getUsuPrimerApellido().toUpperCase() : "";
        firmante += usuario.getUsuSegundoApellido() != null ? " " + usuario.getUsuSegundoApellido().toUpperCase() : "";
        return firmante;
    }

}
