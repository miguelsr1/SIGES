/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.ws.to;

import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Sofis Solutions
 */
public class ReprogramacionACFinanciera implements Serializable {

    private POInsumos insumo;
    private ReprogramacionDetalle reprog;
    private BigDecimal mes1 = BigDecimal.ZERO;
    private BigDecimal mes2 = BigDecimal.ZERO;
    private BigDecimal mes3 = BigDecimal.ZERO;
    private BigDecimal mes4 = BigDecimal.ZERO;
    private BigDecimal mes5 = BigDecimal.ZERO;
    private BigDecimal mes6 = BigDecimal.ZERO;
    private BigDecimal mes7 = BigDecimal.ZERO;
    private BigDecimal mes8 = BigDecimal.ZERO;
    private BigDecimal mes9 = BigDecimal.ZERO;
    private BigDecimal mes10 = BigDecimal.ZERO;
    private BigDecimal mes11 = BigDecimal.ZERO;
    private BigDecimal mes12 = BigDecimal.ZERO;

    private BigDecimal originalmes1 = BigDecimal.ZERO;
    private BigDecimal originalmes2 = BigDecimal.ZERO;
    private BigDecimal originalmes3 = BigDecimal.ZERO;
    private BigDecimal originalmes4 = BigDecimal.ZERO;
    private BigDecimal originalmes5 = BigDecimal.ZERO;
    private BigDecimal originalmes6 = BigDecimal.ZERO;
    private BigDecimal originalmes7 = BigDecimal.ZERO;
    private BigDecimal originalmes8 = BigDecimal.ZERO;
    private BigDecimal originalmes9 = BigDecimal.ZERO;
    private BigDecimal originalmes10 = BigDecimal.ZERO;
    private BigDecimal originalmes11 = BigDecimal.ZERO;
    private BigDecimal originalmes12 = BigDecimal.ZERO;

    private BigDecimal pacmes1 = BigDecimal.ZERO;
    private BigDecimal pacmes2 = BigDecimal.ZERO;
    private BigDecimal pacmes3 = BigDecimal.ZERO;
    private BigDecimal pacmes4 = BigDecimal.ZERO;
    private BigDecimal pacmes5 = BigDecimal.ZERO;
    private BigDecimal pacmes6 = BigDecimal.ZERO;
    private BigDecimal pacmes7 = BigDecimal.ZERO;
    private BigDecimal pacmes8 = BigDecimal.ZERO;
    private BigDecimal pacmes9 = BigDecimal.ZERO;
    private BigDecimal pacmes10 = BigDecimal.ZERO;
    private BigDecimal pacmes11 = BigDecimal.ZERO;
    private BigDecimal pacmes12 = BigDecimal.ZERO;

    private BigDecimal pepmes1 = BigDecimal.ZERO;
    private BigDecimal pepmes2 = BigDecimal.ZERO;
    private BigDecimal pepmes3 = BigDecimal.ZERO;
    private BigDecimal pepmes4 = BigDecimal.ZERO;
    private BigDecimal pepmes5 = BigDecimal.ZERO;
    private BigDecimal pepmes6 = BigDecimal.ZERO;
    private BigDecimal pepmes7 = BigDecimal.ZERO;
    private BigDecimal pepmes8 = BigDecimal.ZERO;
    private BigDecimal pepmes9 = BigDecimal.ZERO;
    private BigDecimal pepmes10 = BigDecimal.ZERO;
    private BigDecimal pepmes11 = BigDecimal.ZERO;
    private BigDecimal pepmes12 = BigDecimal.ZERO;
    
    private BigDecimal total=BigDecimal.ZERO;
    private BigDecimal totalPAC=BigDecimal.ZERO;
    private BigDecimal totalPEP=BigDecimal.ZERO;
    

    public ReprogramacionACFinanciera() {

    }

    public BigDecimal restar(BigDecimal a, BigDecimal b) {
        BigDecimal respuesta = BigDecimal.ZERO;
        if (a != null) {
            if (b != null) {
                respuesta = a.subtract(b);
            } else {
                respuesta = a;
            }
        } else {
            if (b != null) {
                respuesta = b.multiply(new BigDecimal(-1));
            }
        }
        return respuesta;
    }

    private void calcularDiferenciaInsumo(ReprogramacionDetalle det) {
        mes1 = det.getRdeMes1();
        mes2 = det.getRdeMes2();
        mes3 = det.getRdeMes3();
        mes4 = det.getRdeMes4();
        mes5 = det.getRdeMes5();
        mes6 = det.getRdeMes6();
        mes7 = det.getRdeMes7();
        mes8 = det.getRdeMes8();
        mes9 = det.getRdeMes9();
        mes10 = det.getRdeMes10();
        mes11 = det.getRdeMes11();
        mes12 = det.getRdeMes12();

        if (insumo != null) {
            for (FlujoCajaAnio f : insumo.getFlujosDeCajaAnio()) {
                if (f.getAnio() != null && det.getPoa() != null && det.getPoa().getAnioFiscal() != null) {
                    if (f.getAnio().equals(det.getPoa().getAnioFiscal().getAnio())) {
                        for (POFlujoCajaMenusal fm : f.getFlujoCajaMenusal()) {
                            switch (fm.getMes()) {
                                case 1:
                                    mes1 = restar(mes1, fm.getMonto());
                                    break;
                                case 2:
                                    mes2 = restar(mes2, fm.getMonto());
                                    break;
                                case 3:
                                    mes3 = restar(mes3, fm.getMonto());

                                    break;
                                case 4:
                                    mes4 = restar(mes4, fm.getMonto());

                                    break;
                                case 5:
                                    mes5 = restar(mes5, fm.getMonto());

                                    break;
                                case 6:
                                    mes6 = restar(mes6, fm.getMonto());

                                    break;
                                case 7:
                                    mes7 = restar(mes7, fm.getMonto());

                                    break;
                                case 8:
                                    mes8 = restar(mes8, fm.getMonto());

                                    break;
                                case 9:
                                    mes9 = restar(mes9, fm.getMonto());

                                    break;
                                case 10:
                                    mes10 = restar(mes10, fm.getMonto());

                                    break;
                                case 11:
                                    mes11 = restar(mes11, fm.getMonto());

                                    break;
                                case 12:
                                    mes12 = restar(mes12, fm.getMonto());

                                    break;
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     *
     * Insumo Nuevo es UACI Viejo es UACI Acción Nuevo Si -- Todo al PAC Nuevo
     * No -- Nada al PAC Existe Si Si Diferencia al PAC Existe Si No Nuevo al
     * PAC Existe No Si Quitar viejo del PAC Existe No No No se puede dar
     *
     * @param det
     */
    private void calcularDiferenciaPAC(ReprogramacionDetalle det) {    
        if (!BooleanUtils.isTrue(det.getInsumoNuevoNoUaci())) {
            if (BooleanUtils.isTrue (det.getNuevoInsumo())) {
                //si es un insumo nuevo y es UACI, impacta al PAC
                pacmes1 = det.getRdeMes1();
                pacmes2 = det.getRdeMes2();
                pacmes3 = det.getRdeMes3();
                pacmes4 = det.getRdeMes4();
                pacmes5 = det.getRdeMes5();
                pacmes6 = det.getRdeMes6();
                pacmes7 = det.getRdeMes7();
                pacmes8 = det.getRdeMes8();
                pacmes9 = det.getRdeMes9();
                pacmes10 = det.getRdeMes10();
                pacmes11 = det.getRdeMes11();
                pacmes12 = det.getRdeMes12();
            } else {
                //Si habia un insumo y el actual es UACI, el PAC varia 
                //entre lo que estaba y lo actual
                if (det.getPoaInsumo()!=null && !BooleanUtils.isTrue(det.getPoaInsumo().getNoUACI())) {
                    //El insumo original es UACI

                    for (FlujoCajaAnio f : insumo.getFlujosDeCajaAnio()) {
                        if (f.getAnio() != null && det.getPoa() != null && det.getPoa().getAnioFiscal() != null) {
                            if (f.getAnio().equals(det.getPoa().getAnioFiscal().getAnio())) {
                                for (POFlujoCajaMenusal fm : f.getFlujoCajaMenusal()) {
                                    BigDecimal valor = fm.getMonto();
                                    if (BooleanUtils.isTrue(det.getPoaInsumo().getNoUACI())) {
                                        valor = BigDecimal.ZERO;
                                    }
                                    switch (fm.getMes()) {
                                        case 1:
                                            pacmes1 = restar(mes1, valor);
                                            break;
                                        case 2:
                                            pacmes2 = restar(mes2, valor);
                                            break;
                                        case 3:
                                            pacmes3 = restar(mes3, valor);

                                            break;
                                        case 4:
                                            pacmes4 = restar(mes4, valor);

                                            break;
                                        case 5:
                                            pacmes5 = restar(mes5, valor);

                                            break;
                                        case 6:
                                            pacmes6 = restar(mes6, valor);

                                            break;
                                        case 7:
                                            pacmes7 = restar(mes7, valor);

                                            break;
                                        case 8:
                                            pacmes8 = restar(mes8, valor);

                                            break;
                                        case 9:
                                            pacmes9 = restar(mes9, valor);

                                            break;
                                        case 10:
                                            pacmes10 = restar(mes10, valor);

                                            break;
                                        case 11:
                                            pacmes11 = restar(mes11, valor);

                                            break;
                                        case 12:
                                            pacmes12 = restar(mes12, valor);

                                            break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    pacmes1 = det.getRdeMes1();
                    pacmes2 = det.getRdeMes2();
                    pacmes3 = det.getRdeMes3();
                    pacmes4 = det.getRdeMes4();
                    pacmes5 = det.getRdeMes5();
                    pacmes6 = det.getRdeMes6();
                    pacmes7 = det.getRdeMes7();
                    pacmes8 = det.getRdeMes8();
                    pacmes9 = det.getRdeMes9();
                    pacmes10 = det.getRdeMes10();
                    pacmes11 = det.getRdeMes11();
                    pacmes12 = det.getRdeMes12();
                }
            }
        } else {
            //El nuevo es No UACI
            //Determino si el original es UACI
            //En este caso, se debe quitar el valor original del PAC
            if (det.getPoaInsumo()!= null && !BooleanUtils.isTrue(det.getPoaInsumo().getNoUACI())) {
                for (FlujoCajaAnio f : insumo.getFlujosDeCajaAnio()) {
                    if (f.getAnio() != null && det.getPoa() != null && det.getPoa().getAnioFiscal() != null) {
                        if (f.getAnio().equals(det.getPoa().getAnioFiscal().getAnio())) {
                            for (POFlujoCajaMenusal fm : f.getFlujoCajaMenusal()) {

                                switch (fm.getMes()) {
                                    case 1:
                                        pacmes1 = restar(BigDecimal.ZERO, fm.getMonto());
                                        break;
                                    case 2:
                                        pacmes2 = restar(BigDecimal.ZERO, fm.getMonto());
                                        break;
                                    case 3:
                                        pacmes3 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 4:
                                        pacmes4 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 5:
                                        pacmes5 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 6:
                                        pacmes6 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 7:
                                        pacmes7 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 8:
                                        pacmes8 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 9:
                                        pacmes9 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 10:
                                        pacmes10 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 11:
                                        pacmes11 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                    case 12:
                                        pacmes12 = restar(BigDecimal.ZERO, fm.getMonto());

                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void calcularDiferenciaPEP(ReprogramacionDetalle det) {
        pepmes1 = det.getRdeMes1();
        pepmes2 = det.getRdeMes2();
        pepmes3 = det.getRdeMes3();
        pepmes4 = det.getRdeMes4();
        pepmes5 = det.getRdeMes5();
        pepmes6 = det.getRdeMes6();
        pepmes7 = det.getRdeMes7();
        pepmes8 = det.getRdeMes8();
        pepmes9 = det.getRdeMes9();
        pepmes10 = det.getRdeMes10();
        pepmes11 = det.getRdeMes11();
        pepmes12 = det.getRdeMes12();

        if (insumo != null) {
            for (FlujoCajaAnio f : insumo.getFlujosDeCajaAnio()) {
                if (f.getAnio() != null && det.getPoa() != null && det.getPoa().getAnioFiscal() != null) {
                    if (f.getAnio().equals(det.getPoa().getAnioFiscal().getAnio())) {
                        for (POFlujoCajaMenusal fm : f.getFlujoCajaMenusal()) {
                            switch (fm.getMes()) {
                                case 1:
                                    pepmes1 = restar(pepmes1, fm.getMonto());
                                    break;
                                case 2:
                                    pepmes2 = restar(pepmes2, fm.getMonto());
                                    break;
                                case 3:
                                    pepmes3 = restar(pepmes3, fm.getMonto());

                                    break;
                                case 4:
                                    pepmes4 = restar(pepmes4, fm.getMonto());

                                    break;
                                case 5:
                                    pepmes5 = restar(pepmes5, fm.getMonto());

                                    break;
                                case 6:
                                    pepmes6 = restar(pepmes6, fm.getMonto());

                                    break;
                                case 7:
                                    pepmes7 = restar(pepmes7, fm.getMonto());

                                    break;
                                case 8:
                                    pepmes8 = restar(pepmes8, fm.getMonto());

                                    break;
                                case 9:
                                    pepmes9 = restar(pepmes9, fm.getMonto());

                                    break;
                                case 10:
                                    pepmes10 = restar(pepmes10, fm.getMonto());

                                    break;
                                case 11:
                                    pepmes11 = restar(pepmes11, fm.getMonto());

                                    break;
                                case 12:
                                    pepmes12 = restar(pepmes12, fm.getMonto());

                                    break;
                            }
                        }
                    }
                }
            }
        }

    }

//Calcula la diferencia en cada mes
// Calcula la variacion del PAC
// Calcula la variacion de la PEP
    public ReprogramacionACFinanciera(ReprogramacionDetalle det) {
        insumo = det.getPoaInsumo();
        reprog = det;
        calcularDiferenciaInsumo(det);
        calcularDiferenciaPAC(det);
        calcularDiferenciaPEP(det);
    }

    public POInsumos getInsumo() {
        return insumo;
    }

    public void setInsumo(POInsumos insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getMes1() {
        return mes1;
    }

    public void setMes1(BigDecimal mes1) {
        this.mes1 = mes1;
    }

    public BigDecimal getMes2() {
        return mes2;
    }

    public void setMes2(BigDecimal mes2) {
        this.mes2 = mes2;
    }

    public BigDecimal getMes3() {
        return mes3;
    }

    public void setMes3(BigDecimal mes3) {
        this.mes3 = mes3;
    }

    public BigDecimal getMes4() {
        return mes4;
    }

    public void setMes4(BigDecimal mes4) {
        this.mes4 = mes4;
    }

    public BigDecimal getMes5() {
        return mes5;
    }

    public void setMes5(BigDecimal mes5) {
        this.mes5 = mes5;
    }

    public BigDecimal getMes6() {
        return mes6;
    }

    public void setMes6(BigDecimal mes6) {
        this.mes6 = mes6;
    }

    public BigDecimal getMes7() {
        return mes7;
    }

    public void setMes7(BigDecimal mes7) {
        this.mes7 = mes7;
    }

    public BigDecimal getMes8() {
        return mes8;
    }

    public void setMes8(BigDecimal mes8) {
        this.mes8 = mes8;
    }

    public BigDecimal getMes9() {
        return mes9;
    }

    public void setMes9(BigDecimal mes9) {
        this.mes9 = mes9;
    }

    public BigDecimal getMes10() {
        return mes10;
    }

    public void setMes10(BigDecimal mes10) {
        this.mes10 = mes10;
    }

    public BigDecimal getMes11() {
        return mes11;
    }

    public void setMes11(BigDecimal mes11) {
        this.mes11 = mes11;
    }

    public BigDecimal getMes12() {
        return mes12;
    }

    public void setMes12(BigDecimal mes12) {
        this.mes12 = mes12;
    }

    public ReprogramacionDetalle getReprog() {
        return reprog;
    }

    public void setReprog(ReprogramacionDetalle reprog) {
        this.reprog = reprog;
    }

    public BigDecimal getPacmes1() {
        return pacmes1;
    }

    public void setPacmes1(BigDecimal pacmes1) {
        this.pacmes1 = pacmes1;
    }

    public BigDecimal getPacmes2() {
        return pacmes2;
    }

    public void setPacmes2(BigDecimal pacmes2) {
        this.pacmes2 = pacmes2;
    }

    public BigDecimal getPacmes3() {
        return pacmes3;
    }

    public void setPacmes3(BigDecimal pacmes3) {
        this.pacmes3 = pacmes3;
    }

    public BigDecimal getPacmes4() {
        return pacmes4;
    }

    public void setPacmes4(BigDecimal pacmes4) {
        this.pacmes4 = pacmes4;
    }

    public BigDecimal getPacmes5() {
        return pacmes5;
    }

    public void setPacmes5(BigDecimal pacmes5) {
        this.pacmes5 = pacmes5;
    }

    public BigDecimal getPacmes6() {
        return pacmes6;
    }

    public void setPacmes6(BigDecimal pacmes6) {
        this.pacmes6 = pacmes6;
    }

    public BigDecimal getPacmes7() {
        return pacmes7;
    }

    public void setPacmes7(BigDecimal pacmes7) {
        this.pacmes7 = pacmes7;
    }

    public BigDecimal getPacmes8() {
        return pacmes8;
    }

    public void setPacmes8(BigDecimal pacmes8) {
        this.pacmes8 = pacmes8;
    }

    public BigDecimal getPacmes9() {
        return pacmes9;
    }

    public void setPacmes9(BigDecimal pacmes9) {
        this.pacmes9 = pacmes9;
    }

    public BigDecimal getPacmes10() {
        return pacmes10;
    }

    public void setPacmes10(BigDecimal pacmes10) {
        this.pacmes10 = pacmes10;
    }

    public BigDecimal getPacmes11() {
        return pacmes11;
    }

    public void setPacmes11(BigDecimal pacmes11) {
        this.pacmes11 = pacmes11;
    }

    public BigDecimal getPacmes12() {
        return pacmes12;
    }

    public void setPacmes12(BigDecimal pacmes12) {
        this.pacmes12 = pacmes12;
    }

    public BigDecimal getPepmes1() {
        return pepmes1;
    }

    public void setPepmes1(BigDecimal pepmes1) {
        this.pepmes1 = pepmes1;
    }

    public BigDecimal getPepmes2() {
        return pepmes2;
    }

    public void setPepmes2(BigDecimal pepmes2) {
        this.pepmes2 = pepmes2;
    }

    public BigDecimal getPepmes3() {
        return pepmes3;
    }

    public void setPepmes3(BigDecimal pepmes3) {
        this.pepmes3 = pepmes3;
    }

    public BigDecimal getPepmes4() {
        return pepmes4;
    }

    public void setPepmes4(BigDecimal pepmes4) {
        this.pepmes4 = pepmes4;
    }

    public BigDecimal getPepmes5() {
        return pepmes5;
    }

    public void setPepmes5(BigDecimal pepmes5) {
        this.pepmes5 = pepmes5;
    }

    public BigDecimal getPepmes6() {
        return pepmes6;
    }

    public void setPepmes6(BigDecimal pepmes6) {
        this.pepmes6 = pepmes6;
    }

    public BigDecimal getPepmes7() {
        return pepmes7;
    }

    public void setPepmes7(BigDecimal pepmes7) {
        this.pepmes7 = pepmes7;
    }

    public BigDecimal getPepmes8() {
        return pepmes8;
    }

    public void setPepmes8(BigDecimal pepmes8) {
        this.pepmes8 = pepmes8;
    }

    public BigDecimal getPepmes9() {
        return pepmes9;
    }

    public void setPepmes9(BigDecimal pepmes9) {
        this.pepmes9 = pepmes9;
    }

    public BigDecimal getPepmes10() {
        return pepmes10;
    }

    public void setPepmes10(BigDecimal pepmes10) {
        this.pepmes10 = pepmes10;
    }

    public BigDecimal getPepmes11() {
        return pepmes11;
    }

    public void setPepmes11(BigDecimal pepmes11) {
        this.pepmes11 = pepmes11;
    }

    public BigDecimal getPepmes12() {
        return pepmes12;
    }

    public void setPepmes12(BigDecimal pepmes12) {
        this.pepmes12 = pepmes12;
    }

    public BigDecimal getOriginalmes1() {
        return originalmes1;
    }

    public void setOriginalmes1(BigDecimal originalmes1) {
        this.originalmes1 = originalmes1;
    }

    public BigDecimal getOriginalmes2() {
        return originalmes2;
    }

    public void setOriginalmes2(BigDecimal originalmes2) {
        this.originalmes2 = originalmes2;
    }

    public BigDecimal getOriginalmes3() {
        return originalmes3;
    }

    public void setOriginalmes3(BigDecimal originalmes3) {
        this.originalmes3 = originalmes3;
    }

    public BigDecimal getOriginalmes4() {
        return originalmes4;
    }

    public void setOriginalmes4(BigDecimal originalmes4) {
        this.originalmes4 = originalmes4;
    }

    public BigDecimal getOriginalmes5() {
        return originalmes5;
    }

    public void setOriginalmes5(BigDecimal originalmes5) {
        this.originalmes5 = originalmes5;
    }

    public BigDecimal getOriginalmes6() {
        return originalmes6;
    }

    public void setOriginalmes6(BigDecimal originalmes6) {
        this.originalmes6 = originalmes6;
    }

    public BigDecimal getOriginalmes7() {
        return originalmes7;
    }

    public void setOriginalmes7(BigDecimal originalmes7) {
        this.originalmes7 = originalmes7;
    }

    public BigDecimal getOriginalmes8() {
        return originalmes8;
    }

    public void setOriginalmes8(BigDecimal originalmes8) {
        this.originalmes8 = originalmes8;
    }

    public BigDecimal getOriginalmes9() {
        return originalmes9;
    }

    public void setOriginalmes9(BigDecimal originalmes9) {
        this.originalmes9 = originalmes9;
    }

    public BigDecimal getOriginalmes10() {
        return originalmes10;
    }

    public void setOriginalmes10(BigDecimal originalmes10) {
        this.originalmes10 = originalmes10;
    }

    public BigDecimal getOriginalmes11() {
        return originalmes11;
    }

    public void setOriginalmes11(BigDecimal originalmes11) {
        this.originalmes11 = originalmes11;
    }

    public BigDecimal getOriginalmes12() {
        return originalmes12;
    }

    public void setOriginalmes12(BigDecimal originalmes12) {
        this.originalmes12 = originalmes12;
    }

    public BigDecimal getTotal() {
        total=BigDecimal.ZERO;
        total=total.add(mes1);
        total=total.add(mes2);
        total=total.add(mes3);
        total=total.add(mes4);
        total=total.add(mes5);
        total=total.add(mes6);
        total=total.add(mes7);
        total=total.add(mes8);
        total=total.add(mes9);
        total=total.add(mes10);
        total=total.add(mes11);
        total=total.add(mes12);
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalPAC() {        
        totalPAC=totalPAC=BigDecimal.ZERO;
        totalPAC=totalPAC.add(mes1);
        totalPAC=totalPAC.add(mes2);
        totalPAC=totalPAC.add(mes3);
        totalPAC=totalPAC.add(mes4);
        totalPAC=totalPAC.add(mes5);
        totalPAC=totalPAC.add(mes6);
        totalPAC=totalPAC.add(mes7);
        totalPAC=totalPAC.add(mes8);
        totalPAC=totalPAC.add(mes9);
        totalPAC=totalPAC.add(mes10);
        totalPAC=totalPAC.add(mes11);
        totalPAC=totalPAC.add(mes12);
        return totalPAC;
    }

    public void setTotalPAC(BigDecimal totalPAC) {
        this.totalPAC = totalPAC;
    }

    public BigDecimal getTotalPEP() {
        totalPEP=BigDecimal.ZERO;
        totalPEP=totalPEP.add(mes1);
        totalPEP=totalPEP.add(mes2);
        totalPEP=totalPEP.add(mes3);
        totalPEP=totalPEP.add(mes4);
        totalPEP=totalPEP.add(mes5);
        totalPEP=totalPEP.add(mes6);
        totalPEP=totalPEP.add(mes7);
        totalPEP=totalPEP.add(mes8);
        totalPEP=totalPEP.add(mes9);
        totalPEP=totalPEP.add(mes10);
        totalPEP=totalPEP.add(mes11);
        totalPEP=totalPEP.add(mes12);
        return totalPEP;
    }

    public void setTotalPEP(BigDecimal totalPEP) {
        this.totalPEP = totalPEP;
    }

    //Funciones auxiliares
    public boolean trasciendePeriodoInsumo(Integer cantidad) {
        //TODO Ver esta lógica
        return true;
    }
}
