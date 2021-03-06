/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.mensajes.Etiquetas;

@Named
@SessionScoped
public class MenuViewBean implements Serializable {

    private Locale locale;
    private MenuModel model;

    @Inject
    private SessionBean sessionBean;

    public MenuViewBean() {
    }

    @PostConstruct
    public void init() {
        try {

            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            model = new DefaultMenuModel();

            DefaultMenuItem item2;
            DefaultSubMenu submenu;

            item2 = new DefaultMenuItem(Etiquetas.getValue("inicio", locale), null, ConstantesPaginas.IR_A_INICIO);
            model.addElement(item2);

            submenu = new DefaultSubMenu(Etiquetas.getValue("hPlanes", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ORGANIZACION_CURRICULAR)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("horganizacionesCurriculares", locale), null, ConstantesPaginas.ORGANIZACIONES_CURRICULAR);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLANES_ESTUDIO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hplanesEstudio", locale), null, ConstantesPaginas.PLANES_ESTUDIO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLANES_ESTUDIO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcomponentePlanEstudio", locale), null, ConstantesPaginas.COMPONENTE_PLAN_ESTUDIO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REGLAS_EQUIVALENCIAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hreglasEquivalencias", locale), null, ConstantesPaginas.REGLAS_EQUIVALENCIAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DIPLOMADO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hdiplomados", locale), null, ConstantesPaginas.DIPLOMADOS);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(true);
                model.addElement(submenu);
            }

            submenu = new DefaultSubMenu(Etiquetas.getValue("hAnioLectivo", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ANIO_LECTIVO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionAnioLectivo", locale), null, ConstantesPaginas.ANIOS_LECTIVOS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CIERRE_ANIO_LECTIVO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcierreaniolectivo", locale), null, ConstantesPaginas.CIERRE_ANIOS_LECTIVOS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALENDARIOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcalendarios", locale), null, ConstantesPaginas.CALENDARIOS);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(true);
                model.addElement(submenu);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ORGANISMO_ORGANIZACION_ESCOLAR)) {
                submenu = new DefaultSubMenu(Etiquetas.getValue("organismoOrganizacionEscolarMenu", locale));

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ORGANISMOS_ADMINISTRACION_ESCOLAR)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hOrganismos", locale), null, ConstantesPaginas.ORGANISMOS_ADMINISTRACION_ESCOLAR);
                    submenu.addElement(item2);
                }

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EVALUACIONES_ORGANISMOS_ADMINISTRACION_ESCOLAR)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hEvaluarOrganismos", locale), null, ConstantesPaginas.EVALUACIONES_ORGANISMOS_ADMINISTRACION_ESCOLAR);
                    submenu.addElement(item2);
                }
                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EVALUAR_SUSTITUCION_MIEMBROS)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("evaluarSustitucionMiembros", locale), null, ConstantesPaginas.SUSTITUCIONES_MIEMBRO_OAE);
                    submenu.addElement(item2);
                }
                
                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOL_DESHABILITAR_OAE)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hPersoneriaJuridica", locale), null, ConstantesPaginas.SOLICITUDES_OAE);
                    submenu.addElement(item2);
                }

                

                if (submenu.getElements().size() > 0) {
                    submenu.setExpanded(false);
                    model.addElement(submenu);
                }
            }

            submenu = new DefaultSubMenu(Etiquetas.getValue("hSedes", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SEDES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hsedes", locale), null, ConstantesPaginas.SEDES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SECCION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hsecciones", locale), null, ConstantesPaginas.SECCIONES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SECCION_LISTADO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hseccionesListado", locale), null, ConstantesPaginas.SECCIONES_LISTADO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SERVICIO_EDUCATIVO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hserviciosEducativos", locale), null, ConstantesPaginas.SERVICIOS_EDUCATIVOS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_HORARIOS_ESCOLARES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionHorarioEscolar", locale), null, ConstantesPaginas.HORARIOS_ESCOLARES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOMINA_DOCENTE)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hnominaDocente", locale), null, ConstantesPaginas.NOMINA_DOCENTE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PROPUESTA_PEDAGOGICA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionPropuestaPedagogica", locale), null, ConstantesPaginas.PROPUESTAS_PEDAGOGICAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAN_ESCOLAR_ANUAL)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionPlanEscolarAnual", locale), null, ConstantesPaginas.PLANES_ESCOLAR_ANUAL);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_SEDES_ASISTENCIAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionReporteAsistenciasSedes", locale), null, ConstantesPaginas.REPORTE_ASISTENCIA_SEDE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ALERTAS_TEMPRANAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("halertasTempranas", locale), null, ConstantesPaginas.ALERTAS_TEMPRANAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ALERTAS_TEMPRANAS_CONFIG)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("halertasTempranasConfig", locale), null, ConstantesPaginas.ALERTAS_TEMPRANAS_CONFIG);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONSULTA_SECCION_CIERRE_ANIO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("consultaCierreAnioSecciones", locale), null, ConstantesPaginas.CONSULTA_SECCION_CIERRE_ANIO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONSULTA_ASISTENCIAS_SEDES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hconsultaAsistencias", locale), null, ConstantesPaginas.CONSULTA_ASISTENCIAS_SEDE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONSULTA_CALIFICACIONES_PENDIENTES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hconsultaCalificacionesPendientes", locale), null, ConstantesPaginas.CONSULTA_CALIFICACIONES_PENDIENTES_SEDE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_SOLICITUD_CORRECCION_CALIFICACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hsolicitarCorreccionCalificacion", locale), null, ConstantesPaginas.HABILITACIONES_PERIODOS_CALIFICACION);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_HABILITACION_PERIODO_MATRICULA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("solicitHabilitacionMatricula", locale), null, ConstantesPaginas.HABILITACIONES_PERIODOS_MATRICULA);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ASIGNACION_PERFIL)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("asignacionPerfiles", locale), null, ConstantesPaginas.ASIGNACIONES_PERFIL);
                submenu.addElement(item2);
            }
            


            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }
            
            
            submenu = new DefaultSubMenu(Etiquetas.getValue("hencuestaAResponsables", locale));
            
                        
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ENCUESTAS_ESTUDIANTES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hconsultaActualizaciones", locale), null, ConstantesPaginas.ENCUESTAS_ESTUDIANTES);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ENCUESTAS_ESTUDIANTES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hactualizacionDatosDelEstudiante", locale), null, ConstantesPaginas.ENCUESTA_ESTUDIANTE);
                submenu.addElement(item2);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ENCUESTAS_ESTUDIANTES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hseguimientoEstudiantes", locale), null, ConstantesPaginas.ENCUESTAS_ESTUDIANTES_SEGUIMIENTO);
                submenu.addElement(item2); 
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MONITOREO_ENCUESTAS_ESTUDIANTES)){
                item2 = new DefaultMenuItem(Etiquetas.getValue("hmonitoreo", locale), null, ConstantesPaginas.ENCUESTAS_ESTUDIANTES_MONITOREO);
                submenu.addElement(item2);
            }
            
            
                        
            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }
            
            
            

            submenu = new DefaultSubMenu(Etiquetas.getValue("hestudiantes", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTUDIANTES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hestudiantesSede", locale), null, ConstantesPaginas.ESTUDIANTES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_HALLAR_ESTUDIANTES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hhallarEstudiantes", locale), null, ConstantesPaginas.HALLAR_ESTUDIANTES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTUDIANTES_SIN_NIE)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionEstudiantesSinNie", locale), null, ConstantesPaginas.ESTUDIANTES_SIN_NIE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MATRICULA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hmatricula", locale), null, ConstantesPaginas.MATRICULA);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONFIRMACIONES_MATRICULAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionConfirmacionMatriculas", locale), null, ConstantesPaginas.CONFIRMACIONES_MATRICULAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONFIRMACION_MATRICULAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hconfirmacionMatricula", locale), null, ConstantesPaginas.CONFIRMACION_MATRICULAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ASISTENCIAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcontrolAsistencia", locale), null, ConstantesPaginas.CONTROLES_ASISTENCIA);
                submenu.addElement(item2);
            }

//            item2 = new DefaultMenuItem(Etiquetas.getValue("hestudiantesSinNie", locale), null, ConstantesPaginas.ESTUDIANTES_SIN_NIE);
//            submenu.addElement(item2);
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACIONES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionCalificacion", locale), null, ConstantesPaginas.CALIFICACIONES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PROMOCIONES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionPromociones", locale), null, ConstantesPaginas.PROMOCIONES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PERIODOS_CALIFICACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("calificacionPeriodo", locale), null, ConstantesPaginas.CALIFICACION_PERIODO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_POR_ESTUDIANTE)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("calificacionPorEstudiante", locale), null, ConstantesPaginas.CALIFICACION_POR_ESTUDIANTE);
                submenu.addElement(item2);
            }

            /*    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_POR_ESTUDIANTE_PAES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("calificacionPorEstudiantePAES", locale), null, ConstantesPaginas.CALIFICACION_ESTUDIANTE_PAES);
                submenu.addElement(item2);
            }  */
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_PAES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("archivoCalificacionPAES", locale), null, ConstantesPaginas.ARCHIVO_CALIFICACION_PAES);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTRUCTURA_CALIFICACION_PAES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("estructuraCalificacionPaes", locale), null, ConstantesPaginas.ESTRUCTURA_CALIFICACION_PAES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MATRICULA_SIGUIENTE_ANIO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hmatriculaSiguienteAnio", locale), null, ConstantesPaginas.MATRICULA_SIGUIENTE_ANIO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CAMBIO_JORNADA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("cambioSeccion", locale), null, ConstantesPaginas.CAMBIO_SECCION);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CAMBIAR_JORNADA_MASIVO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("cambioSeccionMasivo", locale), null, ConstantesPaginas.CAMBIO_SECCION_MASIVO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUD_TRASLADO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionSolicitudesTraslado", locale), null, ConstantesPaginas.SOLICITUDES_TRASLADO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ESTUDIANTES_VALIDACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hvalidacionAcademica", locale), null, ConstantesPaginas.ESTUDIANTES_VALIDACION);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_IMPORT_BIT_MATRICULA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("himportarBitMatricula", locale), null, ConstantesPaginas.IMPORT_BIT_MATRICUAL);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SERVICIO_SOCIAL)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("servicioSocial", locale), null, ConstantesPaginas.SERVICIO_SOCIAL);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_ACREDITACION)) {
                submenu = new DefaultSubMenu(Etiquetas.getValue("hacreditacion", locale));

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_HISTORICO)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hcalificaciones", locale), null, ConstantesPaginas.CALIFICACION_HISTORICO);
                    submenu.addElement(item2);
                }

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_ESTUDIANTE_HISTORICO)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hcalificacionEstudiante", locale), null, ConstantesPaginas.CALIFICACION_ESTUDIANTE_HISTORICO);
                    submenu.addElement(item2);
                }
              

                if (submenu.getElements().size() > 0) {
                    submenu.setExpanded(false);
                    model.addElement(submenu);
                }
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DIPLOMADO_SUBMENU)) {
                submenu = new DefaultSubMenu(Etiquetas.getValue("hdiplomados", locale));

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_INSCRIBIR_DIPLOMADO)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hinscripciones", locale), null, ConstantesPaginas.INSCRIBIR_DIPLOMADO);
                    submenu.addElement(item2);
                }

                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_DIPLOMADO)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hcalificaciones", locale), null, ConstantesPaginas.CALIFICACIONES_DIPLOMADO);
                    submenu.addElement(item2);
                }
                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_PERIODO_DIPLOMADO)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hcalificacionesperiodo", locale), null, ConstantesPaginas.CALIFICACION_PERIODO_DIPLOMADO);
                    submenu.addElement(item2);
                }
                if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DIPLOMAS)) {
                    item2 = new DefaultMenuItem(Etiquetas.getValue("hdiplomas", locale), null, ConstantesPaginas.DIPLOMAS);
                    submenu.addElement(item2);
                }

                if (submenu.getElements().size() > 0) {
                    submenu.setExpanded(false);
                    model.addElement(submenu);
                }
            }
            submenu = new DefaultSubMenu(Etiquetas.getValue("hpersonalcentroeducativo", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PERSONAL_SEDE_EDUCATIVA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("personalCE", locale), null, ConstantesPaginas.PERSONAL_SEDES_EDUCATIVA);
                submenu.addElement(item2);
            }

            if (sessionBean.getEntidadUsuario().getUsuPersonalPk() != null) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hficha", locale), null, ConstantesPaginas.PERSONAL_SEDE_EDUCATIVA + "?id=" + sessionBean.getEntidadUsuario().getUsuPersonalPk());
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DOCENTES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hregistroEscalafonario", locale), null, ConstantesPaginas.DOCENTES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_PLAZA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("plazas", locale), null, ConstantesPaginas.SOLICITUDES_PLAZAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAZA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("plazasAprobadas", locale), null, ConstantesPaginas.PLAZAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAZAS_DISPONIBLES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("plazasDisponibles", locale), null, ConstantesPaginas.PLAZAS_DISPONIBLES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CONTROL_ASISTENCIA_PERSONAL)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcontrolAsistencia", locale), null, ConstantesPaginas.CONTROLES_ASISTENCIA_PERSONAL);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MODULO_FORMACION_DOCENTE)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionModuloFormacionDocente", locale), null, ConstantesPaginas.MODULOS_FORMACION_DOCENTE);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CURSOS_DOCENTES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcursoDocente", locale), null, ConstantesPaginas.CURSOS_DOCENTES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CURSOS_DOCENTES_DISPONIBLES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hcursoDocenteDisponible", locale), null, ConstantesPaginas.CURSOS_DOCENTES_DISPONIBLES);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }

            submenu = new DefaultSubMenu(Etiquetas.getValue("hmisc", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PERSONA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hpersonas", locale), null, ConstantesPaginas.PERSONAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOTICIA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hnoticias", locale), null, ConstantesPaginas.NOTICIAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOTIFICACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("gestionNotificacion", locale), null, ConstantesPaginas.NOTIFICACIONES);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }
 //<<INSERTAR_SUB_MENU_VALORES>>

            submenu = new DefaultSubMenu(Etiquetas.getValue("htitulos", locale));

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SELLOS_FIRMAS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hsellosFirmas", locale), null, ConstantesPaginas.SELLOS_FIRMAS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SELLOS_FIRMAS_TITULAR)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("selloFirmaTitular", locale), null, ConstantesPaginas.SELLOS_FIRMAS_TITULAR);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SELLOS_FIRMAS_AUTENTICA)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("selloFirmaAutentica", locale), null, ConstantesPaginas.SELLOS_FIRMAS_AUTENTICA);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SOLICITUDES_IMPRESIONES)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("solicitudImpresion", locale), null, ConstantesPaginas.SOLICITUDES_IMPRESIONES);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_GENERACION_TITULO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("generacionTitulo", locale), null, ConstantesPaginas.GENERACION_TITULOS);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REIMPRESION_TITULO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hreimpresionTitulo", locale), null, ConstantesPaginas.REIMPRESION_TITULO);
                submenu.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPOSICION_TITULO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hreposicionTitulo", locale), null, ConstantesPaginas.REPOSICION_TITULO);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_SIN_IMPRIMIR_TITULO)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("hsinImprimirTitulo", locale), null, ConstantesPaginas.SIN_IMPRIMIR_TITULO);
                submenu.addElement(item2);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_TITULOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("htitulos", locale), null, ConstantesPaginas.TITULOS);
                submenu.addElement(item2);
            }

            if (submenu.getElements().size() > 0) {
                submenu.setExpanded(false);
                model.addElement(submenu);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REVISION_INFORMACION)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("revisionInformacion", locale), null, ConstantesPaginas.REVISION_INFORMACION);
                model.addElement(item2);
            }

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REVISION_DATOS)) {
                item2 = new DefaultMenuItem(Etiquetas.getValue("revisionDeDatos", locale), null, ConstantesPaginas.REPORTE_DIRECTOR);
                model.addElement(item2);
            }

            model.generateUniqueIds();

        } catch (Exception ex) {

        }
    }

    public MenuModel getModel() {
        return model;
    }

}
