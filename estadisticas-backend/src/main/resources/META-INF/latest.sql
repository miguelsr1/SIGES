CREATE VIEW centros_educativos.primeros_grados_por_modalidad AS


Select a.niv_pk, a.cic_pk, a.mod_pk, a.gra_pk from 
(SELECT     niv.niv_pk,
           cic.cic_pk,
		   modedu.mod_pk,
           gra.gra_pk,
           gra.gra_orden
FROM       centros_educativos.sg_grados gra 
INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)) a

INNER JOIN

(SELECT     niv.niv_pk,
		   niv.niv_orden,
           cic.cic_pk,
		   cic.cic_orden,
		   modedu.mod_pk,
		   modedu.mod_orden,
           Min(gra.gra_orden) min_gra
FROM       centros_educativos.sg_grados gra 
INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
GROUP BY   niv.niv_pk, cic.cic_pk, modedu.mod_pk
) b

ON (a.niv_pk = b.niv_pk and a.cic_pk = b.cic_pk and a.mod_pk = b.mod_pk and a.gra_orden = b.min_gra);


CREATE VIEW centros_educativos.primeros_ciclos_por_nivel AS
select a.niv_pk, a.cic_pk from 

(SELECT     niv.niv_pk,
           cic.cic_pk,
		   cic.cic_orden
FROM  centros_educativos.sg_ciclos cic
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)) a

INNER JOIN 

(SELECT    niv.niv_pk,
		   niv.niv_orden,
           MIN(cic_orden) min_ciclo
FROM       centros_educativos.sg_ciclos cic 
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
GROUP BY   niv.niv_pk) b

ON (a.niv_pk = b.niv_pk and a.cic_orden = b.min_ciclo) ;


CREATE VIEW centros_educativos.primeros_grados_por_nivel AS
select b.* from 
centros_educativos.primeros_ciclos_por_nivel a
INNER JOIN
centros_educativos.primeros_grados_por_modalidad b
ON (a.cic_pk = b.cic_pk);


CREATE VIEW centros_educativos.ultimos_grados_por_modalidad AS


Select a.niv_pk, a.cic_pk, a.mod_pk, a.gra_pk from 
(SELECT     niv.niv_pk,
           cic.cic_pk,
		   modedu.mod_pk,
           gra.gra_pk,
           gra.gra_orden
FROM       centros_educativos.sg_grados gra 
INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)) a

INNER JOIN

(SELECT     niv.niv_pk,
		   niv.niv_orden,
           cic.cic_pk,
		   cic.cic_orden,
		   modedu.mod_pk,
		   modedu.mod_orden,
           MAX(gra.gra_orden) max_gra
FROM       centros_educativos.sg_grados gra 
INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
GROUP BY   niv.niv_pk, cic.cic_pk, modedu.mod_pk
) b

ON (a.niv_pk = b.niv_pk and a.cic_pk = b.cic_pk and a.mod_pk = b.mod_pk and a.gra_orden = b.max_gra);


CREATE VIEW centros_educativos.ultimos_ciclos_por_nivel AS
select a.niv_pk, a.cic_pk from 

(SELECT     niv.niv_pk,
           cic.cic_pk,
		   cic.cic_orden
FROM  centros_educativos.sg_ciclos cic
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)) a

INNER JOIN 

(SELECT    niv.niv_pk,
		   niv.niv_orden,
           MAX(cic_orden) max_ciclo
FROM       centros_educativos.sg_ciclos cic 
INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
GROUP BY   niv.niv_pk) b

ON (a.niv_pk = b.niv_pk and a.cic_orden = b.max_ciclo) ;


CREATE VIEW centros_educativos.ultimos_grados_por_nivel AS
select b.* from 
centros_educativos.ultimos_ciclos_por_nivel a
INNER JOIN
centros_educativos.ultimos_grados_por_modalidad b
ON (a.cic_pk = b.cic_pk);



CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_est_mat_retiradas ( 
 mre_ext_cabezal_fk bigint,
 mre_est_fk bigint,
 mre_mat_fk bigint,
 mre_mre_fk bigint,
 mre_mre_nombre character varying(255), 
 CONSTRAINT sg_mre_ext_fk FOREIGN KEY (mre_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);

CREATE INDEX IF NOT EXISTS sg_mre_est_idx ON estadisticas.sg_ext_est_mat_retiradas (mre_est_fk) ;
CREATE INDEX IF NOT EXISTS sg_mre_mre_idx ON estadisticas.sg_ext_est_mat_retiradas (mre_mre_fk) ;
CREATE INDEX IF NOT EXISTS sg_mre_mre_nombre_idx ON estadisticas.sg_ext_est_mat_retiradas (mre_mre_nombre) ;

ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_sede_dep_nom character varying(200);
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_sede_zon_nom character varying(200);
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_sede_tipo character varying(100);
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_sede_sector character varying(100);
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_sexo_nom character varying(50);

ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_nivel_nom character varying(200);
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_nivel integer;
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_nivel_orden integer;
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_ciclo integer;
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_ciclo_orden integer;
ALTER TABLE  estadisticas.sg_ext_est_mat_retiradas ADD COLUMN mre_est_ciclo_nom character varying(200);





CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
	BEGIN
        DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_mat_retiradas where mre_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_estudiantes where ext_cabezal_fk = cabezalPk;
                
		INSERT INTO estadisticas.sg_ext_estudiantes (
			ext_cabezal_fk,
			ext_est_pk,
                        ext_est_persona_pk,
			ext_est_sexo,
			ext_est_sexo_nom,
			ext_est_edad_al_extraer,
			ext_est_edad_al_matricularse,
                        ext_est_edad_al_30_oct_mat,
			ext_est_org_curricular,
			ext_est_org_curricular_nom,
			ext_est_nivel,
			ext_est_nivel_nom,
                        ext_est_nivel_orden,
			ext_est_ciclo,
			ext_est_ciclo_nom,
			ext_est_modalidad_educativa,
			ext_est_modalidad_educativa_nom,
			ext_est_modalidad_atencion,
			ext_est_modalidad_atencion_nom,
			ext_est_grado,
			ext_est_grado_nom,
                        ext_est_grado_edad_min,
                        ext_est_grado_edad_max,
			ext_est_opcion,
			ext_est_opcion_nom,
			ext_est_programa_educ,
			ext_est_programa_educ_nom,
			ext_est_sede,
			ext_est_sede_nom,
			ext_est_sede_tipo,
			ext_est_sede_dep,
			ext_est_sede_dep_nom,
                        ext_est_sede_zon_nom,
			ext_est_trabaja,
                        ext_est_trabajo_nom,
                        ext_est_trabajo,
			ext_est_embarazo,
			ext_est_repetidor,
			ext_est_tiene_acceso_internet,
			ext_est_tiene_computadora,
			ext_est_realizo_parvularia,
			ext_est_estado,
			ext_est_con_sobreedad,
                        ext_est_ultima_matricula,
                        ext_est_secc_nom,
                        ext_est_secc,
                        ext_est_tiene_discapacidad,
                        ext_est_ciclo_orden,
                        ext_est_mod_orden,
                        ext_est_grado_orden,
                        ext_est_mat_promocion_grado,
			ext_sed_subvencionado,
			ext_sed_fines_de_lucro,
                        ext_sed_sector,
                        ext_est_sobreedad_anios)
                select 
			cabezalPk,
			est.est_pk,
                        per.per_pk,
			sex.sex_pk,
			sex.sex_nombre,
			date_part('year',age(per.per_fecha_nacimiento)),
			date_part('year',age(m.mat_fecha_ingreso, per.per_fecha_nacimiento)),
                        date_part('year',age(to_date(CONCAT(EXTRACT(YEAR FROM m.mat_fecha_ingreso),'-10-30'),'yyyy-mm-dd'), per.per_fecha_nacimiento)),
			org.ocu_pk,
			org.ocu_nombre,
			niv.niv_pk,
			niv.niv_nombre,
			niv.niv_orden ,
			cic.cic_pk ,
			cic.cic_nombre,
			modedu.mod_pk,
			modedu.mod_nombre,
			modaten.mat_pk,
			modaten.mat_nombre,
			gra.gra_pk,
			gra.gra_nombre,
                        gra.gra_edad_minima,
			gra.gra_edad_maxima,
			op.opc_pk,
			op.opc_nombre,
			pr.ped_pk,
			pr.ped_nombre,
			sed.sed_pk,
			concat(sed.sed_codigo,' - ',sed.sed_nombre),
			(case when (sedeProvieneTipo.sed_tipo = 'CED_OFI') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CED_PRI') then 'Privado' 
			 when (sedeProvieneTipo.sed_tipo = 'CIR_INF') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CIR_ALF') then 'Alfabetización' 
			 when (sedeProvieneTipo.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
                        zona.zon_nombre,
			per.per_trabaja,
                        tiptrab.ttr_nombre,
                        tiptrab.ttr_pk,
			per.per_embarazo,
			m.mat_repetidor,
			per.per_acceso_internet,
			sec.sec_acceso_computadora,
                        EXISTS (select 1 from centros_educativos.sg_matriculas m1  
                                INNER JOIN centros_educativos.sg_estudiantes est1 ON (m1.mat_estudiante_fk = est1.est_pk)
                                INNER JOIN centros_educativos.sg_secciones sec1 ON (m1.mat_seccion_fk = sec1.sec_pk)
                                INNER JOIN centros_educativos.sg_servicio_educativo se1 ON (sec1.sec_servicio_educativo_fk = se1.sdu_pk)
                                INNER JOIN centros_educativos.sg_grados gra1 ON (se1.sdu_grado_fk = gra1.gra_pk)
                                INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten1 ON (gra1.gra_relacion_modalidad_fk = relmodaten1.rea_pk)    
                                INNER JOIN centros_educativos.sg_modalidades modedu1 ON (relmodaten1.rea_modalidad_educativa_fk = modedu1.mod_pk)
                                INNER JOIN centros_educativos.sg_ciclos cic1 ON (modedu1.mod_ciclo = cic1.cic_pk)
                                INNER JOIN centros_educativos.sg_niveles niv1 ON (cic1.cic_nivel = niv1.niv_pk)
                                where (niv1.niv_pk = 2 or niv1.niv_pk = 7) and est1.est_pk = est.est_pk
                                ),
			per.per_estado,
			(case when (gra.gra_edad_minima is not null and gra.gra_edad_minima < date_part('year',age(m.mat_fecha_ingreso, per.per_fecha_nacimiento))) then true else false end) as sobreedad,
                        m.mat_pk,
                        sec.sec_nombre,
                        sec.sec_pk,
                        per.per_tiene_discapacidad,
                        cic.cic_orden,
                        modedu.mod_orden,
			gra.gra_orden,
                        m.mat_promocion_grado,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
                        (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede,
			(case when (gra.gra_edad_minima is not null and gra.gra_edad_minima < date_part('year',age(m.mat_fecha_ingreso, per.per_fecha_nacimiento))) then (date_part('year',age(m.mat_fecha_ingreso, per.per_fecha_nacimiento)) - gra.gra_edad_minima) else 0 end) as anios_sobreedad
	FROM centros_educativos.sg_matriculas m 
        INNER JOIN centros_educativos.sg_estudiantes est ON (m.mat_estudiante_fk = est.est_pk)
	INNER JOIN centros_educativos.sg_personas per ON (est.est_persona = per.per_pk)
        LEFT OUTER JOIN catalogo.sg_tipos_trabajo tiptrab ON (per.per_tipo_trabajo_fk = tiptrab.ttr_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
        LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced sed_cen ON (sed_cen.sed_pk = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN catalogo.sg_programas_educativos pr ON (se.sdu_programa_educativo_fk = pr.ped_pk)
	LEFT OUTER JOIN centros_educativos.sg_opciones op ON (se.sdu_opcion_fk = op.opc_pk)
	INNER JOIN centros_educativos.sg_grados gra_previo ON (se.sdu_grado_fk = gra_previo.gra_pk)
	LEFT OUTER JOIN estadisticas.sg_grado_reporta_en gre ON (gre.rep_grado_origen = gra_previo.gra_pk and rep_extraccion = cabezalPk)
	INNER JOIN centros_educativos.sg_grados gra ON (case when gre is null then gra_previo.gra_pk else gre.rep_grado_destino end = gra.gra_pk)
	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
	INNER JOIN catalogo.sg_modalidades_atencion modaten ON (relmodaten.rea_modalidad_atencion_fk = modaten.mat_pk)
	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
	INNER JOIN centros_educativos.sg_organizaciones_curricular org ON (niv.niv_organizacion_curricular = org.ocu_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
        LEFT OUTER JOIN catalogo.sg_zonas zona on (dir.dir_zona=zona.zon_pk)
	INNER JOIN estadisticas.sg_alcance_extraccion alcance ON (alcance.alc_grado = gra_previo.gra_pk and alc_extraccion = cabezalPk)
        and m.mat_fecha_ingreso <= alcance.alc_fecha_matriculas and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= alcance.alc_fecha_matriculas)
        and m.mat_anulada = false;

        INSERT INTO estadisticas.sg_ext_est_discapacidades (dis_ext_cabezal_fk, dis_est_fk, dis_dis_fk, dis_dis_nombre)
        SELECT cabezalPk, est.est_pk, disc.dis_pk, dc.dis_nombre
        FROM centros_educativos.sg_matriculas m 
        INNER JOIN centros_educativos.sg_estudiantes est ON (m.mat_estudiante_fk = est.est_pk)
        INNER JOIN centros_educativos.sg_personas_discapacidades disc ON (disc.per_pk = est.est_persona)
        INNER JOIN catalogo.sg_discapacidades dc ON (disc.dis_pk = dc.dis_pk)
        INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
        INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
	INNER JOIN estadisticas.sg_alcance_extraccion alcance ON (alcance.alc_grado = gra.gra_pk and alc_extraccion = cabezalPk)
        and m.mat_fecha_ingreso <= alcance.alc_fecha_matriculas and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= alcance.alc_fecha_matriculas)
        and m.mat_anulada = false;

        INSERT INTO estadisticas.sg_ext_est_tipos_parentesco (tpa_ext_cabezal_fk, tpa_est_fk, tpa_tpa_fk, tpa_tpa_nombre, tpa_vive_con, tpa_per_allegada_fk)
        SELECT cabezalPk, est.est_pk, tpa.tpa_pk, tpa.tpa_nombre, alle.all_vive_con_allegado, alle.all_persona
        FROM centros_educativos.sg_matriculas m 
        INNER JOIN centros_educativos.sg_estudiantes est ON (m.mat_estudiante_fk = est.est_pk)
        INNER JOIN centros_educativos.sg_personas p ON (est.est_persona = p.per_pk)
        INNER JOIN centros_educativos.sg_allegados alle ON (alle.all_persona_ref = p.per_pk)
        INNER JOIN catalogo.sg_tipos_parentesco tpa ON (tpa.tpa_pk = alle.all_tipo_parentesco)
        INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
        INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
	INNER JOIN estadisticas.sg_alcance_extraccion alcance ON (alcance.alc_grado = gra.gra_pk and alc_extraccion = cabezalPk)
        and m.mat_fecha_ingreso <= alcance.alc_fecha_matriculas and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= alcance.alc_fecha_matriculas)
        and m.mat_anulada = false;


        INSERT INTO estadisticas.sg_ext_est_mat_retiradas (
                mre_ext_cabezal_fk,
                mre_est_fk,
                mre_mat_fk,
                mre_mre_fk,
                mre_mre_nombre,
                mre_est_sede_dep_nom,
                mre_est_sede_zon_nom,
                mre_est_sede_tipo,
                mre_est_sede_sector,
                mre_est_sexo_nom,
                mre_est_nivel,
                mre_est_nivel_nom,
                mre_est_nivel_orden,
                mre_est_ciclo,
                mre_est_ciclo_nom,
                mre_est_ciclo_orden )
        SELECT cabezalPk,
                est.est_pk,
                m.mat_pk,
                mre.mre_pk,
                mre.mre_nombre,
                depart.dep_nombre,
                zona.zon_nombre,
		(case when (sedeProvieneTipo.sed_tipo = 'CED_OFI') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CED_PRI') then 'Privado' 
			 when (sedeProvieneTipo.sed_tipo = 'CIR_INF') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CIR_ALF') then 'Alfabetización' 
			 when (sedeProvieneTipo.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede,
                (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede,
                sex.sex_nombre,
                niv.niv_pk,
		niv.niv_nombre,
		niv.niv_orden ,
		cic.cic_pk ,
		cic.cic_nombre,
                cic.cic_orden
        FROM centros_educativos.sg_matriculas m 
        INNER JOIN centros_educativos.sg_estudiantes est ON (m.mat_estudiante_fk = est.est_pk)
        INNER JOIN centros_educativos.sg_personas per ON (est.est_persona = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
        INNER JOIN catalogo.sg_motivos_retiro mre ON (m.mat_motivo_retiro_fk = mre.mre_pk)
        INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
        INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
        INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
        LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sedeProvieneTipo.sed_pk)
        INNER JOIN centros_educativos.sg_grados gra_previo ON (se.sdu_grado_fk = gra_previo.gra_pk)
	LEFT OUTER JOIN estadisticas.sg_grado_reporta_en gre ON (gre.rep_grado_origen = gra_previo.gra_pk and rep_extraccion = cabezalPk)
	INNER JOIN centros_educativos.sg_grados gra ON (case when gre is null then gra_previo.gra_pk else gre.rep_grado_destino end = gra.gra_pk)
	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
	INNER JOIN catalogo.sg_modalidades_atencion modaten ON (relmodaten.rea_modalidad_atencion_fk = modaten.mat_pk)
	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
        LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
        LEFT OUTER JOIN catalogo.sg_zonas zona on (dir.dir_zona=zona.zon_pk)
        INNER JOIN estadisticas.sg_alcance_extraccion alcance ON (alcance.alc_grado = gra_previo.gra_pk and alc_extraccion = cabezalPk)
        where m.mat_retirada = true and m.mat_anulada = false and mre.mre_cambiosecc = false and mre.mre_traslado = false
        and m.mat_fecha_ingreso <= alcance.alc_fecha_matriculas and ale.ale_anio = extract(year from alcance.alc_fecha_matriculas)
        and not exists(select 1 from centros_educativos.sg_matriculas mm 
					   INNER JOIN centros_educativos.sg_secciones secc ON (mm.mat_seccion_fk = secc.sec_pk)
					   INNER JOIN centros_educativos.sg_anio_lectivo alee ON (secc.sec_anio_lectivo_fk = alee.ale_pk)
					   where mm.mat_anulada = false 
                                           and mm.mat_fecha_ingreso > m.mat_fecha_ingreso 
                                           and mm.mat_fecha_ingreso <= alcance.alc_fecha_matriculas
                                           and alee.ale_anio = extract(year from alcance.alc_fecha_matriculas)
					   and mm.mat_estudiante_fk = m.mat_estudiante_fk
					  );
        
    END;
$$ LANGUAGE plpgsql;


                                                                                   
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-07', true, now(), 'ADMIN', 0, 'Distribución porcentual de estudiantes según causa de retiro del centro educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'NIVEL' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR_PUB_PRI' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR_OFI_PRI' from estadisticas.sg_indicadores where ind_codigo = 'CC-07';

UPDATE estadisticas.sg_indicadores set ind_es_publico = false, ind_es_externo = false, ind_precision = 0, ind_tipo_resultado = 'PORCENTAJE' where ind_codigo = 'CC-07';

