/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEncuestaEstudiante;

public class EncuestaEstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersona
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEncuestaEstudiante entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {

            if (entity.getEncEstudiante() == null || entity.getEncEstudiante().getEstPk() == null) {
                ge.addError("encEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }

            if (entity.getEncZonaResidencia() == null) {
                ge.addError("encZonaResidencia", Errores.ERROR_ZONA_VACIO);
            }

            if (entity.getEncViveConCantidad() == null || entity.getEncViveConCantidad() < 0) {
                ge.addError("encViveConCantidad", Errores.ERROR_VIVE_CON_CANTIDAD_VACIO);

            }

            if (entity.getEncCantidadDormitoriosCasa() == null || entity.getEncCantidadDormitoriosCasa() < 0) {
                ge.addError("encCantidadDormitoriosCasa", Errores.ERROR_CANTIDAD_DORMITORIOS_CASA_VACIO);
            }

            if (entity.getEncTieneServicioEnergiaElectricaResidencial() == null) {
                ge.addError("encTieneServicioEnergiaElectricaResidencial", Errores.ERROR_TIENE_ENERGIA_ELECTRICA_RESIDENCIAL_VACIO);
            }

            if (entity.getEncFuenteAbastecimientoAguaResidencial() == null) {
                ge.addError("encFuenteAbastecimientoAguaResidencial", Errores.ERROR_FUENTE_ABASTECIMIENTO_AGUA_RESIDENCIAL_VACIA);
            }

            if (entity.getEncFuenteAbastecimientoAguaResidencialOtra() != null && entity.getEncFuenteAbastecimientoAguaResidencialOtra().length() > 250) {
                ge.addError("encFuenteAbastecimientoAguaResidencialOtra", Errores.ERROR_LARGO_FUENTE_ABASTECIMIENTO_AGUA_RESIDENCIAL_OTRA_250);
            }

            if (entity.getEncMaterialPisoResidencial() == null) {
                ge.addError("encMaterialPisoResidencial", Errores.ERROR_MATERIAL_PISO_RESIDENCIAL_VACIO);
            }

            if (entity.getEncMaterialPisoResidencialOtro() != null && entity.getEncMaterialPisoResidencialOtro().length() > 250) {
                ge.addError("encMaterialPisoResidencialOtro", Errores.ERROR_LARGO_MATERIAL_PISO_RESIDENCIAL_OTRO_250);
            }

            if (entity.getEncTipoServicioSanitarioResidencial() == null) {
                ge.addError("encMaterialPisoResidencial", Errores.ERROR_TIPO_SERVICIO_SANITARIO_RESIDENCIAL_VACIO);
            }

            if (entity.getEncTipoServicioSanitarioResidencialOtro() != null && entity.getEncTipoServicioSanitarioResidencialOtro().length() > 250) {
                ge.addError("encMaterialPisoResidencial", Errores.ERROR_LARGO_TIPO_SERVICIO_SANITARIO_RESIDENCIAL_250);
            }

            if (entity.getEncTieneConexionInternetResidencial() == null) {
                ge.addError("encTieneConexionInternetResidencial", Errores.ERROR_TIENE_CONEXION_INTERNET_RESIDENCIAL_VACIO);
            } else {
                if (entity.getEncTieneConexionInternetResidencial() && entity.getEncCompaniaInternetResidencial() == null) {
                    ge.addError("encCompaniaInternetResidencial", Errores.ERROR_COMPANIA_INTERNET_RESIDENCIAL_VACIO);
                }
            }

            if (entity.getEncEstudianteDisKmCentro() == null || entity.getEncEstudianteDisKmCentro() < 0) {
                ge.addError("encEstudianteDisKmCentro", Errores.ERROR_DISTANCIA_ESTUDIANTE_CENTRO_VACIO);
            }
            
            if (entity.getEncViveConPersonasMenores() == null){
                ge.addError("encViveConPersonasMenores", Errores.ERROR_VIVE_CON_PERSONAS_MENORES_VACIO);
            } else if (entity.getEncViveConPersonasMenores() && (entity.getEncMenores() == null || entity.getEncMenores().isEmpty())){
                ge.addError("encViveConPersonasMenores", Errores.ERROR_MENORES_VACIO);
            }
            
            if (entity.getEncSintonizaCanal10()== null) {
                ge.addError("encMaterialPisoResidencial", Errores.ERROR_SINTONIZA_CANAL_10_VACIO);
            }
            
            if (entity.getEncSintonizaFranjaEducativa()== null) {
                ge.addError("encMaterialPisoResidencial", Errores.ERROR_SINTONIZA_FRANJA_EDUCATIVA_VACIO);
            }

        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }

}
