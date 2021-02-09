--Organizacion curricular
INSERT INTO centros_educativos.sg_organizaciones_curricular("ocu_codigo", "ocu_nombre","ocu_nombre_busqueda", "ocu_descripcion", "ocu_habilitado", "ocu_ult_mod_fecha", "ocu_ult_mod_usuario" , "ocu_version") VALUES ('1', 'Organización Curricular 2018','organizacion curricular 2018','Organización curricular del 2018',true, CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_organizaciones_curricular_aud ("rev", "revtype", "ocu_pk", "ocu_codigo", "ocu_nombre","ocu_nombre_busqueda", "ocu_descripcion", "ocu_habilitado", "ocu_ult_mod_fecha", "ocu_ult_mod_usuario" , "ocu_version") select 1, 0, ocu_pk, ocu_codigo, ocu_nombre,ocu_nombre_busqueda, ocu_descripcion, ocu_habilitado, ocu_ult_mod_fecha, ocu_ult_mod_usuario, ocu_version from centros_educativos.sg_organizaciones_curricular;

--Nivel
INSERT INTO centros_educativos.sg_niveles("niv_organizacion_curricular", "niv_codigo", "niv_nombre", "niv_orden", "niv_admite_ciclos", "niv_obligatorio", "niv_habilitado", "niv_ult_mod_fecha", "niv_ult_mod_usuario", "niv_version") VALUES ('1','1', 'Inicial','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_niveles("niv_organizacion_curricular", "niv_codigo", "niv_nombre", "niv_orden", "niv_admite_ciclos", "niv_obligatorio","niv_habilitado", "niv_ult_mod_fecha", "niv_ult_mod_usuario", "niv_version") VALUES ('1','2', 'Parvularia','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_niveles("niv_organizacion_curricular", "niv_codigo", "niv_nombre", "niv_orden", "niv_admite_ciclos", "niv_obligatorio",  "niv_habilitado", "niv_ult_mod_fecha", "niv_ult_mod_usuario", "niv_version") VALUES ('1','3', 'Básica','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_niveles("niv_organizacion_curricular", "niv_codigo", "niv_nombre", "niv_orden", "niv_admite_ciclos", "niv_obligatorio", "niv_habilitado", "niv_ult_mod_fecha", "niv_ult_mod_usuario", "niv_version") VALUES ('1','4', 'Media','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_niveles_aud ("rev", "revtype", "niv_pk", "niv_organizacion_curricular", "niv_codigo", "niv_nombre", "niv_orden", "niv_admite_ciclos", "niv_obligatorio",  "niv_habilitado", "niv_ult_mod_fecha", "niv_ult_mod_usuario", "niv_version") select 1, 0, niv_pk, niv_organizacion_curricular, niv_codigo, niv_nombre, niv_orden, niv_admite_ciclos, niv_obligatorio,  niv_habilitado, niv_ult_mod_fecha, niv_ult_mod_usuario, niv_version from centros_educativos.sg_niveles;

--Ciclo
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio",  "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('1','1', 'Único','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio", "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('2','2', 'Único','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio", "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('3','3', 'Ciclo I','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio",  "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('3','4', 'Ciclo II','2', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio", "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('3','5', 'Ciclo III','3', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_ciclos("cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio",  "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") VALUES ('4 ','6', 'Único','1', true,true,true, CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_ciclos_aud ("rev", "revtype", "cic_pk", "cic_nivel", "cic_codigo", "cic_nombre", "cic_orden", "cic_admite_modalidad", "cic_obligatorio", "cic_habilitado", "cic_ult_mod_fecha", "cic_ult_mod_usuario", "cic_version") select 1, 0, cic_pk, cic_nivel, cic_codigo, cic_nombre, cic_orden, cic_admite_modalidad, cic_obligatorio,  cic_habilitado, cic_ult_mod_fecha, cic_ult_mod_usuario, cic_version from centros_educativos.sg_ciclos;

--Modalidad
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",   "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('1','1', 'Único','1',true, true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",  "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('2','2', 'Único','1', true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",  "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('3','3', 'Único','1',true, true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion", "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('4','4', 'Único','1', true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",   "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('5','5', 'Único','1', true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",  "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('6','6', 'General','1', true,true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modalidades("mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion",  "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") VALUES ('6','7', 'Técnica Vocacional','2',true, true, CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_modalidades_aud ("rev", "revtype", "mod_pk", "mod_ciclo", "mod_codigo", "mod_nombre", "mod_orden","mod_admite_opcion", "mod_habilitado", "mod_ult_mod_fecha", "mod_ult_mod_usuario", "mod_version") select 1, 0, mod_pk, mod_ciclo, mod_codigo, mod_nombre, mod_orden,mod_admite_opcion, mod_habilitado, mod_ult_mod_fecha, mod_ult_mod_usuario, mod_version from centros_educativos.sg_modalidades;

--Opcion
INSERT INTO centros_educativos.sg_opciones("opc_modalidad_fk","opc_sector_economico", "opc_codigo", "opc_nombre","opc_nombre_busqueda", "opc_descripcion",  "opc_habilitado", "opc_ult_mod_fecha", "opc_ult_mod_usuario", "opc_version") VALUES ('1','1','1', 'Agropecuario','agropecuario','desc',true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_opciones("opc_modalidad_fk","opc_sector_economico", "opc_codigo", "opc_nombre","opc_nombre_busqueda", "opc_descripcion",  "opc_habilitado", "opc_ult_mod_fecha", "opc_ult_mod_usuario", "opc_version") VALUES ('1','1','2', 'Lácteos y Cárnicos','lacteos y carnicos','desc',true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_opciones("opc_modalidad_fk","opc_sector_economico", "opc_codigo", "opc_nombre","opc_nombre_busqueda","opc_descripcion",   "opc_habilitado", "opc_ult_mod_fecha", "opc_ult_mod_usuario", "opc_version") VALUES ('2','1','3', 'Acuicultura','acuicultura','desc',true, CURRENT_TIMESTAMP, 'admin', '0');


INSERT INTO centros_educativos.sg_opciones_aud ("rev", "revtype", "opc_pk", "opc_modalidad_fk","opc_sector_economico", "opc_codigo", "opc_nombre","opc_nombre_busqueda", "opc_descripcion",  "opc_habilitado", "opc_ult_mod_fecha", "opc_ult_mod_usuario", "opc_version") select 1, 0, opc_pk,opc_modalidad_fk, opc_sector_economico, opc_codigo, opc_nombre,opc_nombre_busqueda, opc_descripcion, opc_habilitado, opc_ult_mod_fecha, opc_ult_mod_usuario, opc_version from centros_educativos.sg_opciones;


--Diplomado
INSERT INTO centros_educativos.sg_diplomados("dip_codigo","dip_nombre","dip_nombre_busqueda","dip_descripcion","dip_habilitado","dip_ult_mod_fecha","dip_ult_mod_usuario","dip_version") VALUES ('1','Exportaciones','exportaciones', 'Diplomado en exportaciones',true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_diplomados("dip_codigo","dip_nombre","dip_nombre_busqueda","dip_descripcion","dip_habilitado","dip_ult_mod_fecha","dip_ult_mod_usuario","dip_version") VALUES ('2','Convivencia escolar','convivencia escolar', 'Diplomado en convivencia escolar',true, CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_diplomados_aud ("rev", "revtype", "dip_pk", "dip_codigo","dip_nombre","dip_nombre_busqueda","dip_descripcion","dip_habilitado","dip_ult_mod_fecha","dip_ult_mod_usuario","dip_version") select 1, 0, "dip_pk", "dip_codigo","dip_nombre","dip_nombre_busqueda","dip_descripcion","dip_habilitado","dip_ult_mod_fecha","dip_ult_mod_usuario","dip_version" from centros_educativos.sg_diplomados;

--ModuloDiplomado
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M1','Identificación de oportunidades comerciales de exportación y su mercado.', 'Modulo 1 del diplomado en exportaciones','1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M2','Aplicación del marco legal en el proceso de exportación de bienes y servicios.', 'Modulo 2 del diplomado en exportaciones','1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M3','Diseño del plan de marketing para productos o servicios de exportación.', 'Modulo 3 del diplomado en exportaciones','1', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M4','Determinación de la logística y estimación de costos de operación internacional.', 'Modulo 4 del diplomado en exportaciones','1', CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M1','Fundamentos teóricos y enfoques de la convivencia escolar.', 'Modulo 1 del diplomado en convivencia escolar','2', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M2','Conflicto y paz.', 'Modulo 2 del diplomado en convivencia escolar','2', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M3','Políticas públicas para la convivencia escolar.', 'Modulo 3 del diplomado en convivencia escolar','2', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO centros_educativos.sg_modulos_diplomado("mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") VALUES ('M4','Convivencia y cultura de paz en la escuela.', 'Modulo 4 del diplomado en convivencia escolar','2', CURRENT_TIMESTAMP, 'admin', '0');

INSERT INTO centros_educativos.sg_modulos_diplomado_aud ("rev", "revtype", "mdi_pk", "mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version") select 1, 0, "mdi_pk", "mdi_codigo","mdi_nombre","mdi_descripcion","mdi_diplomado_fk","mdi_ult_mod_fecha","mdi_ult_mod_usuario","mdi_version" from centros_educativos.sg_modulos_diplomado;


INSERT INTO centros_educativos.sg_rel_mod_ed_mod_aten(rea_modalidad_educativa_fk, rea_modalidad_atencion_fk, rea_sub_modalidad_atencion_fk, rea_habilitado, rea_ult_mod_fecha, rea_ult_mod_usuario, rea_version)VALUES(1, 2, null, true, '2018-08-25 15:23:35', 'casuser', 0);

INSERT INTO centros_educativos.sg_grados(gra_codigo, gra_nombre, gra_descripcion, gra_relacion_modalidad_fk, gra_orden, gra_habilitado, gra_ult_mod_fecha, gra_ult_mod_usuario, gra_version)VALUES('1', 'gra1', 'gra1', 1, 1, true, '2018-08-25 15:23:35', 'casuser', 0);

INSERT INTO centros_educativos.sg_planes_estudio(pes_opcion_fk, pes_programa_educativo_fk, pes_codigo, pes_nombre, pes_nombre_busqueda, pes_descripcion, pes_vigente, pes_relacion_modalidad_fk, pes_ult_mod_fecha, pes_ult_mod_usuario, pes_version)VALUES(null, null, '1', 'pe1', 'pe1', null, true, 1,'2018-08-25 15:23:35', 'casuser', 0);

INSERT INTO centros_educativos.sg_componente_plan_estudio(cpe_codigo, cpe_nombre, cpe_nombre_busqueda, cpe_nombre_publicable, cpe_descripcion, cpe_contenido_tematico, cpe_tipo, cpe_habilitado, cpe_ult_mod_fecha, cpe_ult_mod_usuario, cpe_version)VALUES('1', 'matematica', 'matematica', 'matematica', 'matematica', 'matematica', 'ASI', true,'2018-08-25 15:23:35', 'casuser', 0);

INSERT INTO centros_educativos.sg_componente_plan_grado(cpg_pk, cpg_plan_estudio_fk, cpg_componente_plan_estudio_fk, cpg_grado_fk, cpg_programa_educativo_fk, cpg_opcion_fk, cpg_objeto_promocion, cpg_escala_calificacion_fk, cpg_nombre_publicable, cpg_cantidad_horas_semanales, cpg_codigo_sirai, cpg_requiere_nie, cpg_periodos_calificacion, cpg_cantidad_primera_prueba, cpg_cantidad_primera_suficiencia, cpg_cantidad_segunda_prueba, cpg_cantidad_segunda_suficiencia, cpg_ult_mod_fecha, cpg_ult_mod_usuario, cpg_version)VALUES(1, 1, 1, 1, NULL, NULL, false, 1, 'cpg1', 1, NULL, false, 1, 11, 11, 11, 11, '2018-08-25 16:54:53.236', 'casuser', 0);

INSERT INTO centros_educativos.sg_asignaturas(cpe_pk, asig_area_asignatura_modulo_fk)VALUES(1, null);

INSERT INTO centros_educativos.sg_sedes(sed_pk, sed_codigo, sed_nombre, sed_nombre_busqueda, sed_telefono, sed_telefono_movil, sed_correo_electronico, sed_anio_inicio_act, sed_propietario, sed_nota, sed_centro_adscrito, sed_clasificacion_fk, sed_habilitado, sed_ult_mod_fecha, sed_ult_mod_usuario, sed_version, sed_tipo, sed_migracion, sed_tipo_calendario_fk, sed_direccion_fk)VALUES(1, '1', 'centro1', 'centro1', '', '', 'dsada', 1234, 'dsa', NULL, NULL, 2, true, '2018-08-25 16:53:00.942', 'casuser', 0, 'CED_OFI', false, NULL, NULL);

INSERT INTO centros_educativos.sg_personas(per_pk, per_primer_nombre, per_segundo_nombre, per_tercer_nombre, per_primer_apellido, per_segundo_apellido, per_tercer_apellido, per_nombre_busqueda, per_fecha_nacimiento, per_email, per_etnia_fk, per_estado_civil_fk, per_sexo_fk, per_tipo_sangre_fk, per_cantidad_hijos, per_habilitado, per_dui, per_cun, per_nie, per_partida_nacimiento, per_partida_nacimiento_folio, per_partida_nacimiento_libro, per_departamento_nacimiento_fk, per_municipio_nacimiento_fk, per_ult_mod_fecha, per_ult_mod_usuario, per_version)VALUES(1, 'rodri', '', '', 'dsads', '', '', 'rodri dsads', '2018-08-01', '', NULL, 6, 1, NULL, NULL, NULL, NULL, NULL, 1231, NULL, NULL, NULL, NULL, NULL, '2018-08-25 18:25:36.873', 'casuser', 1);

INSERT INTO centros_educativos.sg_estudiantes(est_pk, est_persona, est_dis_km_centro, est_fac_riesgo, est_trabaja, est_embarazo, est_tipo_trabajo_fk, est_medio_transporte_fk, est_habilitado, est_ult_mod_fecha, est_ult_mod_usuario, est_version)VALUES(1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2018-08-25 18:25:36.873', 'casuser', 1);

INSERT INTO centros_educativos.sg_servicio_educativo(sdu_pk, sdu_opcion_fk, sdu_programa_educativo_fk, sdu_grado_fk, sdu_estado, sdu_fecha_habilitado, sdu_numero_tramite, sdu_sede_fk, sdu_ult_mod_fecha, sdu_ult_mod_usuario, sdu_version)VALUES(1, 1, 1, 1, 'HABILITADO', '2018-08-22', '1', 1, '2018-08-22 14:04:13.000', 'casuser', 0);

INSERT INTO centros_educativos.sg_anio_lectivo(ale_pk, ale_anio, ale_descripcion, ale_estado, ale_ult_mod_fecha, ale_ult_mod_usuario, ale_version)VALUES(1, 2018, 'DSADA', 'ABIERTO', '2018-08-25 16:58:52.311', 'casuser', 0);

INSERT INTO centros_educativos.sg_secciones(sec_pk, sec_codigo, sec_nombre, sec_estado, sec_jornada_fk, sec_plan_estudio_fk, sec_anio_lectivo_fk, sec_servicio_educativo_fk, sec_ult_mod_fecha, sec_ult_mod_usuario, sec_version)VALUES(1, '123', 'secc1', 'ABIERTA', 3, 1, 1, 1, '2018-08-25 16:59:09.790', 'casuser', 0);

INSERT INTO centros_educativos.sg_matriculas(mat_pk, mat_seccion_fk, mat_estudiante_fk, mat_motivo_retiro_fk, mat_estado, mat_observaciones, mat_fecha_hasta, mat_ult_mod_fecha, mat_ult_mod_usuario, mat_version)VALUES(1, 1, 1, NULL, 'ABIERTO', 'dsa', '2018-08-01', '2018-08-25 18:25:36.873', 'casuser', 1);

