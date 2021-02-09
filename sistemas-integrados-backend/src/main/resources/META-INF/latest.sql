DROP MATERIALIZED VIEW sistemas_integrados.sg_promedio_calificaciones_si;

CREATE MATERIALIZED VIEW sistemas_integrados.sg_promedio_calificaciones_si AS

select 
sistemas.sin_pk,h1.sed_pk, h1.ocu_pk, h1.niv_pk, h1.cic_pk,h1.gra_pk, h1.cpe_tipo, h1.cpe_pk, h1.cpe_nombre, h1.opc_pk,h1.mat_pk,h1.smo_pk,h1.ped_pk,h1.mod_pk,
AVG(H1.cal_promedio_calificaciones)  from
(select cabezal.cal_pk,rfe_fecha_desde, sed.sed_pk, org.ocu_pk, cic.cic_pk,niv.niv_pk,gra.gra_pk,cpe.cpe_pk, asig_area_asignatura_modulo_fk, mod_area_asignatura_modulo_fk, 
(case when asig_area_asignatura_modulo_fk is not null then asig_area_asignatura_modulo_fk else mod_area_asignatura_modulo_fk end)  AS area,
cpe.cpe_tipo,cpe.cpe_nombre,op.opc_pk,modedu.mod_pk,modat.mat_pk,submodat.smo_pk,progedu.ped_pk,cabezal.cal_promedio_calificaciones
 					from centros_educativos.sg_calificaciones cabezal
					INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) 
					LEFT OUTER JOIN centros_educativos.sg_modulos modulos ON (modulos.cpe_pk = cpe.cpe_pk)
					LEFT OUTER JOIN centros_educativos.sg_asignaturas asign ON (asign.cpe_pk = cpe.cpe_pk)
                    INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)
                    INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)
		    INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk)
		    INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)
                    INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
                    INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
                    INNER JOIN catalogo.sg_modalidades_atencion modat on (relmodaten.rea_modalidad_atencion_fk = modat.mat_pk)                  
                    LEFT OUTER JOIN catalogo.sg_sub_modalidades submodat on (relmodaten.rea_sub_modalidad_atencion_fk = submodat.smo_pk)
                    LEFT OUTER JOIN catalogo.sg_programas_educativos progedu ON (sdu.sdu_programa_educativo_fk = progedu.ped_pk)
                    LEFT OUTER JOIN centros_educativos.sg_opciones op ON (sdu.sdu_opcion_fk = op.opc_pk)
                    INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
                    INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
                    INNER JOIN centros_educativos.sg_organizaciones_curricular org ON (niv.niv_organizacion_curricular = org.ocu_pk)
                    INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk and rf.rfe_fecha_desde < now())
					where (cpe.cpe_tipo = 'ASI' or cpe.cpe_tipo = 'MOD') and (asig_area_asignatura_modulo_fk = 1 or mod_area_asignatura_modulo_fk = 1) and cabezal.cal_tipo_periodo_calificacion = 'ORD' ) as h1
					
					INNER JOIN
					
					(SELECT * from centros_educativos.sg_sedes sed
					INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
                                        INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal INNER JOIN centros_educativos.sg_anio_lectivo ale ON (cal.cal_anio_lectivo_fk = ale.ale_pk)
                                                                                                                           where cal.cal_tipo_calendario_fk = tce.tce_pk AND ale.ale_habilitado_promedios
                                                                                                                           order by cal_fecha_inicio desc limit 1))) as h2
					ON (h1.sed_pk = h2.sed_pk and h1.rfe_fecha_desde >= h2.cal_fecha_inicio AND  h1.rfe_fecha_desde <= h2.cal_fecha_fin)					
					LEFT OUTER JOIN sistemas_integrados.sg_sistemas_sedes sistemas ON (sistemas.sed_pk = h1.sed_pk)

					group by sistemas.sin_pk,h1.sed_pk, h1.ocu_pk, h1.niv_pk, h1.cic_pk,h1.gra_pk, h1.cpe_tipo, h1.cpe_pk, h1.opc_pk,h1.mat_pk,h1.smo_pk,h1.ped_pk,h1.mod_pk,h1.cpe_nombre;
														