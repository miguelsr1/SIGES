CREATE SCHEMA IF NOT EXISTS estadisticas;

-- Indicadores
CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_indicadores_ind_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores ( ind_pk bigint NOT NULL DEFAULT nextval( 'estadisticas.sg_indicadores_ind_pk_seq' :: regclass ), ind_codigo character varying(45), ind_habilitado boolean, ind_definicion character varying(500), ind_metodo_calculo character varying(500), ind_formula bigint, ind_descripcion character varying(255), ind_fuente character varying(500), ind_desagregacion character varying(500), ind_observaciones character varying(500), ind_ult_mod_fecha timestamp without time zone, ind_ult_mod_usuario character varying(45), ind_version integer, CONSTRAINT sg_indicadores_pkey PRIMARY KEY (ind_pk), CONSTRAINT ind_codigo_uk UNIQUE (ind_codigo), CONSTRAINT sg_indicadores_formula_fk FOREIGN KEY (ind_formula) REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );
COMMENT ON TABLE estadisticas.sg_indicadores IS 'Tabla para el registro de indicadores.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_codigo IS 'Código del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_definicion IS 'Definición del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_metodo_calculo IS 'Metodo de calculo del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_formula IS 'Formula del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_descripcion IS 'Descripcion del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_fuente IS 'Fuente del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_desagregacion IS 'Desagregacion del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_observaciones IS 'Observaciones del registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN estadisticas.sg_indicadores.ind_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores_aud (ind_pk bigint NOT NULL, ind_codigo character varying(45),ind_habilitado boolean,ind_definicion character varying(500), ind_metodo_calculo character varying(500), ind_formula bigint, ind_descripcion character varying(255), ind_fuente character varying(500), ind_desagregacion character varying(500), ind_observaciones character varying(500),ind_nombre_busqueda character varying(255), ind_ult_mod_fecha timestamp without time zone, ind_ult_mod_usuario character varying(45), ind_version integer, rev bigint, revtype smallint);
ALTER TABLE estadisticas.sg_indicadores_aud ADD PRIMARY KEY (ind_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_ESTADISTICA','W6', '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INDICADORES','ES1',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INDICADORES','ES2',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INDICADORES','ES3',  '', 6, true, null, null,0);

ALTER TABLE estadisticas.sg_indicadores add column ind_nombre character varying(45);
ALTER TABLE estadisticas.sg_indicadores_aud add column ind_nombre character varying(45);

-- 0.3.0

DROP TABLE IF EXISTS estadisticas.sg_archivos_aud;
DROP TABLE IF EXISTS estadisticas.sg_archivos;


ALTER TABLE estadisticas.sg_indicadores ADD COLUMN ind_categoria_fk bigint;
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN ind_categoria_fk bigint;
ALTER TABLE estadisticas.sg_indicadores ADD CONSTRAINT sg_ind_categoria_fkey FOREIGN KEY (ind_categoria_fk) REFERENCES catalogo.sg_est_categorias_indicadores(cin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO catalogo.sg_est_datasets (dat_codigo, dat_nombre, dat_habilitado, dat_nombre_busqueda, dat_ult_mod_fecha, dat_ult_mod_usuario, dat_version) VALUES ('EST', 'Estudiantes', true, 'estudiantes', now(), 'admin', 0);
INSERT INTO catalogo.sg_est_datasets (dat_codigo, dat_nombre, dat_habilitado, dat_nombre_busqueda, dat_ult_mod_fecha, dat_ult_mod_usuario, dat_version) VALUES ('PER', 'Personal', true, 'docentes', now(), 'admin', 0);
INSERT INTO catalogo.sg_est_datasets (dat_codigo, dat_nombre, dat_habilitado, dat_nombre_busqueda, dat_ult_mod_fecha, dat_ult_mod_usuario, dat_version) VALUES ('SED', 'Sedes', true, 'sedes', now(), 'admin', 0);

CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_extracciones_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_extracciones ( 
    ext_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_extracciones_pk_seq' :: regclass ),
    ext_anio integer,
    ext_dataset_fk bigint,
    ext_nombre_fk bigint,
    ext_ult_mod_fecha timestamp without time zone,
    ext_ult_mod_usuario character varying(45),
    ext_version integer,
    CONSTRAINT sg_extracciones_pkey PRIMARY KEY (ext_pk),
    CONSTRAINT sg_ext_dataset_fk FOREIGN KEY (ext_dataset_fk) REFERENCES catalogo.sg_est_datasets(dat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_ext_nombre_fk FOREIGN KEY (ext_nombre_fk) REFERENCES catalogo.sg_est_nombres_extracciones(nex_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_ext_anio_ds_nom_uk UNIQUE (ext_anio, ext_dataset_fk, ext_nombre_fk)
);

CREATE TABLE IF NOT EXISTS estadisticas.sg_extracciones_aud ( 
    ext_pk bigint NOT NULL,
    ext_anio integer,
    ext_dataset_fk bigint,
    ext_nombre_fk bigint,
    ext_ult_mod_fecha timestamp without time zone,
    ext_ult_mod_usuario character varying(45),
    ext_version integer,
    rev bigint,
    revtype smallint
);
ALTER TABLE estadisticas.sg_extracciones_aud ADD PRIMARY KEY (ext_pk, rev);


ALTER TABLE estadisticas.sg_extracciones ADD COLUMN ext_estado character varying(45);
ALTER TABLE estadisticas.sg_extracciones_aud ADD COLUMN ext_estado character varying(45);
CREATE INDEX IF NOT EXISTS sg_grado_aplica_est_idx ON centros_educativos.sg_grados (gra_aplica_estadisticas) ;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EXTRACCIONES','ES4', '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EXTRACCIONES','ES5',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EXTRACCIONES','ES6',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INDICADORES','ESM1',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_EXTRACCIONES','ESM2',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ESTADISTICAS','ESM3',  '', 6, true, null, null,0);


CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores_desagregaciones (
    ind_pk bigint,
    ind_desagregacion character varying(100),
    CONSTRAINT sg_des_ind_fk FOREIGN KEY (ind_pk) REFERENCES estadisticas.sg_indicadores(ind_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE 
);

CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores_desagregaciones_aud (
    ind_pk bigint,
    ind_desagregacion character varying(100),
    rev bigint,
    revtype smallint
);



ALTER TABLE estadisticas.sg_indicadores DROP COLUMN ind_desagregacion;
ALTER TABLE estadisticas.sg_indicadores_aud DROP COLUMN ind_desagregacion;

CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_ext_estudiantes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_estudiantes ( 
 ext_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_ext_estudiantes_pk_seq' :: regclass ),
 ext_cabezal_fk bigint,
 ext_est_pk bigint,
 ext_est_sexo bigint,
 ext_est_sexo_nom character varying(45),
 ext_est_edad_al_extraer integer,
 ext_est_edad_al_matricularse integer,
 ext_est_org_curricular bigint,
 ext_est_org_curricular_nom character varying(100),
 ext_est_nivel bigint,
 ext_est_nivel_nom character varying(100),
 ext_est_ciclo bigint,
 ext_est_ciclo_nom character varying(100),
 ext_est_modalidad_educativa bigint,
 ext_est_modalidad_educativa_nom character varying(100),
 ext_est_modalidad_atencion bigint,
 ext_est_modalidad_atencion_nom character varying(100),
 ext_est_grado bigint,
 ext_est_grado_nom character varying(100),
 ext_est_opcion bigint,
 ext_est_opcion_nom character varying(100),
 ext_est_programa_educ bigint,
 ext_est_programa_educ_nom character varying(100),
 ext_est_sede bigint,
 ext_est_sede_nom character varying(300),
 ext_est_sede_tipo character varying(100),
 ext_est_sede_dep bigint,
 ext_est_sede_dep_nom character varying(200),
 ext_est_trabaja boolean,
 ext_est_embarazo boolean,
 ext_est_repetidor boolean,
 ext_est_tiene_acceso_internet boolean,
 ext_est_tiene_computadora boolean,
 ext_est_realizo_parvularia boolean,
 ext_est_estado character varying(45),
 ext_est_con_sobreedad boolean,
 CONSTRAINT sg_ext_estudiantes_pkey PRIMARY KEY (ext_pk),
 CONSTRAINT sg_ext_cabezal_fk FOREIGN KEY (ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );


ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_ultima_matricula bigint;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_ultima_matricula_estado character varying(45);


CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_ext_personal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_personal ( 
 ext_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_ext_personal_pk_seq' :: regclass ),
 ext_cabezal_fk bigint,
 ext_per_pk bigint,
 ext_per_sexo bigint,
 ext_per_sexo_nom character varying(45),
 ext_per_tipo character varying(100),
 ext_per_edad_al_extraer integer,
 ext_per_antiguedad_func integer,
 ext_per_nivel_educativo bigint,
 ext_per_nivel_educativo_nom character varying(100),
 CONSTRAINT sg_ext_personal_pkey PRIMARY KEY (ext_pk),
 CONSTRAINT sg_ext_per_cabezal_fk FOREIGN KEY (ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );

CREATE INDEX IF NOT EXISTS sg_ext_estudiantes_cabezal_idx ON estadisticas.sg_ext_estudiantes (ext_cabezal_fk) ;
CREATE INDEX IF NOT EXISTS sg_ext_personal_cabezal_idx ON estadisticas.sg_ext_personal (ext_cabezal_fk) ;


CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_ext_sedes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_sedes ( 
 ext_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_ext_sedes_pk_seq' :: regclass ),
 ext_cabezal_fk bigint,
 ext_sed_pk bigint,
 ext_sed_departamento bigint,   
 ext_sed_departamento_nom character varying(100),
 ext_sed_municipio bigint,   
 ext_sed_municipio_nom character varying(100),
 ext_sed_zona bigint,   
 ext_sed_zona_nom character varying(100),
 ext_sed_tipo character varying(100),
 ext_sed_subvencionado boolean,
 ext_sed_fines_de_lucro boolean,
 ext_sed_tipo_org_adm_escolar bigint,
 ext_sed_tipo_org_adm_escolar_nom character varying(100),
 ext_sed_tipo_calendario bigint,
 ext_sed_tipo_calendario_nom character varying(100),
 CONSTRAINT sg_ext_sedes_pkey PRIMARY KEY (ext_pk),
 CONSTRAINT sg_ext_sed_cabezal_fk FOREIGN KEY (ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );

CREATE INDEX IF NOT EXISTS sg_ext_sed_cabezal_idx ON estadisticas.sg_ext_sedes (ext_cabezal_fk) ;


ALTER TABLE estadisticas.sg_extracciones ADD COLUMN ext_error character varying(2000);
ALTER TABLE estadisticas.sg_extracciones_aud ADD COLUMN ext_error character varying(2000);

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
	BEGIN
                DELETE FROM estadisticas.sg_ext_estudiantes where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_estudiantes (
			ext_cabezal_fk,
			ext_est_pk,
			ext_est_sexo,
			ext_est_sexo_nom,
			ext_est_edad_al_extraer,
			ext_est_edad_al_matricularse,
			ext_est_org_curricular,
			ext_est_org_curricular_nom,
			ext_est_nivel,
			ext_est_nivel_nom,
			ext_est_ciclo,
			ext_est_ciclo_nom,
			ext_est_modalidad_educativa,
			ext_est_modalidad_educativa_nom,
			ext_est_modalidad_atencion,
			ext_est_modalidad_atencion_nom,
			ext_est_grado,
			ext_est_grado_nom,
			ext_est_opcion,
			ext_est_opcion_nom,
			ext_est_programa_educ,
			ext_est_programa_educ_nom,
			ext_est_sede,
			ext_est_sede_nom,
			ext_est_sede_tipo,
			ext_est_sede_dep,
			ext_est_sede_dep_nom,
			ext_est_trabaja,
			ext_est_embarazo,
			ext_est_repetidor,
			ext_est_tiene_acceso_internet,
			ext_est_tiene_computadora,
			ext_est_realizo_parvularia,
			ext_est_estado,
			ext_est_con_sobreedad,
                        ext_est_ultima_matricula,
                        ext_est_ultima_matricula_estado)
                select 
			cabezalPk,
			est.est_pk,
			sex.sex_pk,
			sex.sex_nombre,
			date_part('year',age(per.per_fecha_nacimiento)),
			date_part('year',age(m.mat_fecha_ingreso, per.per_fecha_nacimiento)),
			org.ocu_pk,
			org.ocu_nombre,
			niv.niv_pk,
			niv.niv_nombre,
			cic.cic_pk,
			cic.cic_nombre,
			modedu.mod_pk,
			modedu.mod_nombre,
			modaten.mat_pk,
			modaten.mat_nombre,
			gra.gra_pk,
			gra.gra_nombre,
			op.opc_pk,
			op.opc_nombre,
			pr.ped_pk,
			pr.ped_nombre,
			sed.sed_pk,
			concat(sed.sed_codigo,' - ',sed.sed_nombre),
			(case when (sed.sed_tipo = 'CED_OFI') then 'Público' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
			per.per_trabaja,
			per.per_embarazo,
			null,
			per.per_acceso_internet,
			sec.sec_acceso_computadora,
			null,
			per.per_estado,
			(case when (gra.gra_edad_maxima is not null and gra.gra_edad_maxima < date_part('year',age(per.per_fecha_nacimiento))) then true else false end) as sobreedad,
                        m.mat_pk,
                        m.mat_estado
	FROM centros_educativos.sg_estudiantes est
	INNER JOIN centros_educativos.sg_personas per ON (est.est_persona = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_matriculas m ON (est.est_ultima_matricula_fk = m.mat_pk)
	INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
	LEFT OUTER JOIN catalogo.sg_programas_educativos pr ON (se.sdu_programa_educativo_fk = pr.ped_pk)
	LEFT OUTER JOIN centros_educativos.sg_opciones op ON (se.sdu_opcion_fk = op.opc_pk)
	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
	INNER JOIN catalogo.sg_modalidades_atencion modaten ON (relmodaten.rea_modalidad_atencion_fk = modaten.mat_pk)
	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
	INNER JOIN centros_educativos.sg_organizaciones_curricular org ON (niv.niv_organizacion_curricular = org.ocu_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
	WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI') and gra.gra_aplica_estadisticas = true ;

    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_personal(cabezalPk bigint) 
RETURNS void AS $$
	BEGIN
        DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_personal (
			ext_cabezal_fk,
			ext_per_pk,
			ext_per_sexo,
			ext_per_sexo_nom,
			ext_per_tipo,
			ext_per_edad_al_extraer,
			ext_per_antiguedad_func,
			ext_per_nivel_educativo,
			ext_per_nivel_educativo_nom
			)
                select 
			cabezalPk,
			pers.pse_pk,
			sex.sex_pk,
			sex.sex_nombre,
			pers.pse_tipo,
			date_part('year',age(per.per_fecha_nacimiento)),
			null,
			mnea.mne_pk,
			mnea.mne_nombre
	FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_estudios_realizados er ON (pers.pse_pk = er.ere_personal_sede_fk)
	INNER JOIN catalogo.sg_maximo_nivel_educativo_alcanzado mnea ON (er.ere_maximo_nivel_educativo_alcanzado_fk = mnea.mne_pk);
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_sedes(cabezalPk bigint) 
RETURNS void AS $$
	BEGIN
        DELETE FROM estadisticas.sg_ext_sedes where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_sedes (
			ext_cabezal_fk,
			ext_sed_pk,
			ext_sed_tipo,
			ext_sed_departamento,
			ext_sed_departamento_nom,
			ext_sed_municipio,
			ext_sed_municipio_nom,
			ext_sed_zona,
			ext_sed_zona_nom,
			ext_sed_subvencionado,
			ext_sed_fines_de_lucro,
			ext_sed_tipo_org_adm_escolar,
			ext_sed_tipo_org_adm_escolar_nom,
			ext_sed_tipo_calendario,
			ext_sed_tipo_calendario_nom																	
			)
            select 
			cabezalPk,
			sed.sed_pk,
			(case when (sed.sed_tipo = 'CED_OFI') then 'Público' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
			mun.mun_pk,
			mun.mun_nombre,
			zon.zon_pk,
			zon.zon_nombre,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
			toa.toa_pk,
			toa.toa_nombre,
			tce.tce_pk,
			tce.tce_nombre	
	FROM centros_educativos.sg_sedes sed
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced sed_cen ON (sed_cen.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
	LEFT OUTER JOIN catalogo.sg_municipios mun on (dir.dir_municipio=mun.mun_pk)
	LEFT OUTER JOIN catalogo.sg_zonas zon on (dir.dir_zona=zon.zon_pk)
	INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
	LEFT OUTER JOIN catalogo.sg_tipos_organismo_administrativo toa ON (sed_cen.ced_tipo_organismo_administrativo_fk = toa.toa_pk)
        WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI');
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION estadisticas.procesar_extracciones_pendientes() RETURNS void AS $$
DECLARE
unlocked boolean;
extraccion record;
err_context text;
lock_id INTEGER DEFAULT 77;
    BEGIN
        unlocked := (SELECT pg_try_advisory_lock(lock_id));
        if (unlocked = false) then
            RAISE NOTICE 'Procesar extracciones pendientes: Locked. Skipping...';
            return;
        end if;
            for extraccion in (select ext_pk, dat_codigo from estadisticas.sg_extracciones INNER JOIN catalogo.sg_est_datasets ds ON (ext_dataset_fk = dat_pk) where ext_estado = 'PENDIENTE')
            LOOP     	
                BEGIN
                    CASE
                        WHEN extraccion.dat_codigo = 'EST' THEN
                            RAISE NOTICE 'Procesando ext estudiantes (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_estudiantes(extraccion.ext_pk);
                        WHEN extraccion.dat_codigo = 'SED' THEN
                            RAISE NOTICE 'Procesando ext sedes (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_sedes(extraccion.ext_pk);
                        WHEN extraccion.dat_codigo = 'PER' THEN
                            RAISE NOTICE 'Procesando ext personal (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_personal(extraccion.ext_pk);
                        END CASE;
                        update estadisticas.sg_extracciones set ext_estado = 'FINALIZADA' where ext_pk = extraccion.ext_pk;
                    EXCEPTION WHEN OTHERS THEN
                        GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
                        RAISE NOTICE 'Query exception';
                        RAISE INFO 'Error Name:%',SQLERRM;
                        RAISE INFO 'Error State:%', SQLSTATE;
                        RAISE INFO 'Error Context:%', err_context;
                        update estadisticas.sg_extracciones set ext_estado = 'ERROR', ext_error = concat('Error Name: ', SQLERRM, ' Error State: ', SQLSTATE, ' Error Context: ', err_context) where ext_pk = extraccion.ext_pk;
                    END;    
            END LOOP;	
            PERFORM pg_advisory_unlock(lock_id);
        EXCEPTION 
        WHEN QUERY_CANCELED THEN
		PERFORM pg_advisory_unlock(lock_id);
        WHEN OTHERS THEN
                GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
                RAISE NOTICE 'Query exception';
                RAISE INFO 'Error Name:%',SQLERRM;
                RAISE INFO 'Error State:%', SQLSTATE;
                RAISE INFO 'Error Context:%', err_context;
		PERFORM pg_advisory_unlock(lock_id);
    END;
$$ LANGUAGE plpgsql;
-- Extraer sedes. SOlo oficiales y privadas. El resto se ignoran (educame, alf, inf).


ALTER TABLE  estadisticas.sg_indicadores ALTER COLUMN ind_nombre type character varying(1000);
ALTER TABLE  estadisticas.sg_indicadores_aud ALTER COLUMN ind_nombre type character varying(1000);

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre) VALUES ('M-01', true, now(), 'ADMIN', 0, 'Matrícula por nivel educativo');
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre) VALUES ('P-01', true, now(), 'ADMIN', 0, 'Población no escolarizada por edad');
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre) VALUES ('I-01', true, now(), 'ADMIN', 0, 'Tasa bruta de ingreso al primer grado de Educación Básica');
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre) VALUES ('T-01', true, now(), 'ADMIN', 0, 'Porcentaje de repetidores');

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DATOS_EXTERNOS','ESM4',  '', 6, true, null, null,0);


INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'AREA_GEOGRAFICA' from estadisticas.sg_indicadores where ind_codigo = 'M-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'M-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'I-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'P-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'AREA_GEOGRAFICA' from estadisticas.sg_indicadores where ind_codigo = 'T-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-01';

CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_proyecciones_poblacion_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_proyecciones_poblacion ( 
 pro_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_proyecciones_poblacion_pk_seq' :: regclass ),
 pro_sexo_nom character varying(45),
 pro_edad integer,
 pro_anio integer,
 pro_poblacion integer,
 CONSTRAINT sg_proyecciones_poblacionl_pkey PRIMARY KEY (pro_pk));




-- 1.3.0

ALTER TABLE estadisticas.sg_ext_estudiantes DROP COLUMN ext_est_ultima_matricula_estado;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_nivel_orden integer;

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_sede_zon_nom character varying(100);

ALTER TABLE estadisticas.sg_extracciones ADD COLUMN ext_descripcion character varying(2000);
ALTER TABLE estadisticas.sg_extracciones_aud ADD COLUMN ext_descripcion character varying(2000);
ALTER TABLE estadisticas.sg_extracciones ADD COLUMN ext_fecha_matriculados timestamp without time zone;
ALTER TABLE estadisticas.sg_extracciones_aud ADD COLUMN ext_fecha_matriculados timestamp without time zone;

TRUNCATE catalogo.sg_est_categorias_indicadores cascade;

INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('01', true, 'Población fuera del sistema educativo', 'poblacion fuera del sistema educativo', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('02', true, 'Ingreso al sistema', 'ingreso al sistema', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('03', true, 'Cobertura matricular', 'cobertura matricular', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('04', true, 'Trayectoria', 'trayectoria', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('05', true, 'Condición y contexto de los estudiantes', 'condicion y contexto de los estudiantes', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('06', true, 'Finalización', 'finalizacion', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('07', true, 'Docentes', 'docentes', now(), 'ADMIN', 0);
INSERT INTO catalogo.sg_est_categorias_indicadores(cin_codigo, cin_habilitado, cin_nombre, cin_nombre_busqueda, cin_ult_mod_fecha, cin_ult_mod_usuario, cin_version) VALUES ('08', true, 'Recursos escolares', 'recursos escolares', now(), 'ADMIN', 0);



INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'M-01', true, now(), 'ADMIN', 0, 'Matrícula por nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '03';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'P-01', true, now(), 'ADMIN', 0, 'Población no escolarizada por edad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '01';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'I-01', true, now(), 'ADMIN', 0, 'Tasa bruta de ingreso al primer grado de Educación Básica', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '02';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-01', true, now(), 'ADMIN', 0, 'Porcentaje de repetidores', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-01', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes trabajadores', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'P-02', true, now(), 'ADMIN', 0, 'Porcentaje de población no escolarizada por edad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '01';



INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'M-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'M-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'M-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'I-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'P-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'T-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'T-01';




INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'GRADO' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'NIVEL' from estadisticas.sg_indicadores where ind_codigo = 'CC-01';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'P-02';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'I-02', true, now(), 'ADMIN', 0, 'Tasa neta de ingreso al primer grado de Educación Básica', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'I-02';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'I-03', true, now(), 'ADMIN', 0, 'Tasa específica de escolarización por edad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'I-03';
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'I-04', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes de primer grado con experiencia en Educación Parvularia', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'I-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'I-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'I-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'I-04';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'M-02', true, now(), 'ADMIN', 0, 'Distribución porcentual de la matrícula por nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'M-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'M-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'M-02';


ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_trabajo_nom character varying(300);
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_trabajo integer;

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_secc_nom character varying(300);
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_secc integer;

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'P-03', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes desertores', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'P-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'P-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'P-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'P-03';


-- 1.5.0

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_grado_edad_min integer;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_grado_edad_max integer;

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_tiene_discapacidad boolean;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_persona_pk bigint;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_ciclo_orden integer;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_grado_orden integer;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_mod_orden integer;

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_mat_promocion_grado character varying(100);

CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_est_discapacidades ( 
 dis_ext_cabezal_fk bigint,
 dis_ext_fk bigint,
 dis_dis_fk bigint,
 dis_dis_nombre character varying(100),   
 CONSTRAINT sg_ext_est_discapacidades_pkey PRIMARY KEY (dis_ext_fk, dis_dis_fk),
 CONSTRAINT sg_ext_fk FOREIGN KEY (dis_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
 CONSTRAINT sg_ext_est_discap_est_fk FOREIGN KEY (dis_ext_fk) REFERENCES estadisticas.sg_ext_estudiantes(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );


CREATE INDEX IF NOT EXISTS sg_dis_ext_cabezal_idx ON estadisticas.sg_ext_est_discapacidades (dis_ext_cabezal_fk) ;

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-09', true, now(), 'ADMIN', 0, 'Tasa bruta de ingreso por grado', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-09';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-02', true, now(), 'ADMIN', 0, 'Tasa de repetición', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'T-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'T-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-02';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-05', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes con acceso a computadora', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'RE-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'RE-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'RE-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'RE-05';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-03', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes con acceso a internet', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'RE-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'RE-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'RE-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'RE-03';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-07', true, now(), 'ADMIN', 0, 'Estudiantes por sección', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'RE-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'RE-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'RE-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'GRADO' from estadisticas.sg_indicadores where ind_codigo = 'RE-07';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-01', true, now(), 'ADMIN', 0, 'Centros educativos según nivel educativo que atienden', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'RE-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'RE-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'RE-01';




INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-02', true, now(), 'ADMIN', 0, 'Distribución porcentual de estudiantes según actividad laboral', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-02';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-03', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes con discapacidad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'GRADO' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'NIVEL' from estadisticas.sg_indicadores where ind_codigo = 'CC-03';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'M-03', true, now(), 'ADMIN', 0, 'Tasa bruta de matrícula por nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-03';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'M-04', true, now(), 'ADMIN', 0, 'Tasa neta de matrícula por nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-04';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'M-05', true, now(), 'ADMIN', 0, 'Tasa específica de matrícula por grado', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'M-05';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-04', true, now(), 'ADMIN', 0, 'Distribución porcentual de estudiantes con discapacidad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-04';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-06', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes con sobreedad', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'GRADO' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'NIVEL' from estadisticas.sg_indicadores where ind_codigo = 'CC-06';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'D-04', true, now(), 'ADMIN', 0, 'Promedio de estudiantes por docente.', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '07';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'D-04';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-04', true, now(), 'ADMIN', 0, 'Tasa de transición por ciclo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'T-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'T-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-04';



INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-05', true, now(), 'ADMIN', 0, 'Tasa de transición por nivel', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'T-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'T-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-05';

CREATE INDEX IF NOT EXISTS sg_ext_est_grado_idx ON estadisticas.sg_ext_estudiantes (ext_est_grado) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_nivel_idx ON estadisticas.sg_ext_estudiantes (ext_est_nivel) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_nivel_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_nivel_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_ciclo_idx ON estadisticas.sg_ext_estudiantes (ext_est_ciclo) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_ciclo_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_ciclo_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_sexo_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_sexo_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_org_idx ON estadisticas.sg_ext_estudiantes (ext_est_org_curricular) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_repetidor_idx ON estadisticas.sg_ext_estudiantes (ext_est_repetidor) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_sed_dep_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_sede_dep_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_sed_zon_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_sede_zon_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_sed_tipo_idx ON estadisticas.sg_ext_estudiantes (ext_est_sede_tipo) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_grado_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_grado_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_mod_aten_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_modalidad_atencion_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_mod_educ_nom_idx ON estadisticas.sg_ext_estudiantes (ext_est_modalidad_educativa_nom) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_mat_prom_idx ON estadisticas.sg_ext_estudiantes (ext_est_mat_promocion_grado) ;


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'F-01', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes aprobados', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '06';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'F-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'F-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'F-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'F-01';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'F-02', true, now(), 'ADMIN', 0, 'Tasa bruta de aprobación', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '06';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'F-02';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'F-03', true, now(), 'ADMIN', 0, 'Tasa de promoción', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '06';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'F-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'F-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'F-03';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'F-03';


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'D-05', true, now(), 'ADMIN', 0, 'Promedio de docentes por grado académico alcanzado', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'D-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'D-05';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'T-07', true, now(), 'ADMIN', 0, 'Tasa de deserción', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '04';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'T-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'T-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'T-07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'T-07';


CREATE TABLE estadisticas.sg_ext_pers_sec (
	sec_ext_cabezal_fk int8 NULL,
	sec_sec_fk int8 NOT NULL,
	sec_sec_nombre varchar(100) NULL,
	sec_per_fk int8 NULL,
	CONSTRAINT sg_ext_fk FOREIGN KEY (sec_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT
);
CREATE INDEX sg_sec_ext_cabezal_idx ON estadisticas.sg_ext_pers_sec USING btree (sec_ext_cabezal_fk);

-- elimino fk innecesaria a extracciones fk en tablas secundarias (discapacidades y secciones docente)
ALTER TABLE estadisticas.sg_ext_est_discapacidades DROP COLUMN dis_ext_fk;
-- agrego fk a estudainte desde ext_discapacidades
ALTER TABLE estadisticas.sg_ext_est_discapacidades ADD COLUMN dis_est_fk bigint NULL;
-- agrego edad al 30 de octubre
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_edad_al_30_oct_mat integer;

ALTER TABLE estadisticas.sg_ext_personal ADD ext_per_tiene_acceso_internet bool NULL;
INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-04', true, now(), 'ADMIN', 0, 'Porcentaje de docentes con acceso a internet', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'RE-04';

-- agrego acceso a internet en ext_personal
CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_personal(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	begin
	DELETE FROM estadisticas.sg_ext_pers_sec where sec_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = cabezalPk;
	INSERT INTO estadisticas.sg_ext_personal (
			ext_cabezal_fk,
			ext_per_pk,
			ext_per_sexo,
			ext_per_sexo_nom,
			ext_per_tipo,
			ext_per_edad_al_extraer,
			ext_per_antiguedad_func,
			ext_per_nivel_educativo,
			ext_per_nivel_educativo_nom,
			ext_per_tiene_acceso_internet
			)
                        select
			cabezalPk,
			pers.pse_pk,
			sex.sex_pk,
			sex.sex_nombre,
			pers.pse_tipo,
			date_part('year',age(per.per_fecha_nacimiento)),
			null,
			mnea.mne_pk,
			mnea.mne_nombre,
			per.per_acceso_internet
	FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_estudios_realizados er ON (pers.pse_pk = er.ere_personal_sede_fk)
	LEFT OUTER JOIN catalogo.sg_maximo_nivel_educativo_alcanzado mnea ON (er.ere_maximo_nivel_educativo_alcanzado_fk = mnea.mne_pk)
        ;

        INSERT INTO estadisticas.sg_ext_pers_sec (sec_ext_cabezal_fk, sec_per_fk, sec_sec_fk, sec_sec_nombre)
        SELECT cabezalPk, pers.pse_pk, sec.sec_pk, sec.sec_nombre
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
           JOIN (select z.docente, z.seccion from (select hes.hes_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            where hes.hes_unico_docente is true
            group by hes.hes_docente_fk, hes.hes_seccion_fk
            union all 
           select cd.cdo_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            join centros_educativos.sg_componentes_docentes cd on (cd.cdo_horario_escolar_fk=hes.hes_pk)
            where hes.hes_unico_docente is false
            group by cd.cdo_docente_fk, hes.hes_seccion_fk) z group by z.docente, z.seccion) x ON (x.docente=pers.pse_pk)
        INNER JOIN centros_educativos.sg_secciones sec ON (x.seccion = sec.sec_pk);
    END;
$function$
;

-- 1.5.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EXTRAER_FORMA_MANUAL','ES7',  '', 6, true, null, null,0);

-- 1.5.2

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'D-03', true, now(), 'ADMIN', 0, 'Distribución porcentual de docentes según años de servicio', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '07';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'D-03';

-- N-N sede-servicios básicos.
CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_sede_servicios_basicos ( 
 sba_ext_cabezal_fk bigint,
 sba_sede_fk bigint,
 sba_sba_fk bigint,
 sba_sba_nombre character varying(100),   
 CONSTRAINT sg_ext_est_sede_servicios_pkey PRIMARY KEY (sba_sede_fk, sba_sba_fk),
 CONSTRAINT sg_ext_fk FOREIGN KEY (sba_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE cascade);
CREATE INDEX IF NOT EXISTS sg_sba_ext_cabezal_idx ON estadisticas.sg_ext_sede_servicios_basicos (sba_ext_cabezal_fk) ;
ALTER TABLE estadisticas.sg_ext_sede_servicios_basicos ADD sba_tiene_servicio bool NULL;

-- agregar en function sedes los servicios básicos de la misma. rel N-N
CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_sedes(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	BEGIN
        DELETE FROM estadisticas.sg_ext_sede_servicios_basicos where sba_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_sedes where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_sedes (
			ext_cabezal_fk,
			ext_sed_pk,
			ext_sed_tipo,
			ext_sed_departamento,
			ext_sed_departamento_nom,
			ext_sed_municipio,
			ext_sed_municipio_nom,
			ext_sed_zona,
			ext_sed_zona_nom,
			ext_sed_subvencionado,
			ext_sed_fines_de_lucro,
			ext_sed_tipo_org_adm_escolar,
			ext_sed_tipo_org_adm_escolar_nom,
			ext_sed_tipo_calendario,
			ext_sed_tipo_calendario_nom																	
			)
            select 
			cabezalPk,
			sed.sed_pk,
			(case when (sed.sed_tipo = 'CED_OFI') then 'Público' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
			mun.mun_pk,
			mun.mun_nombre,
			zon.zon_pk,
			zon.zon_nombre,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
			toa.toa_pk,
			toa.toa_nombre,
			tce.tce_pk,
			tce.tce_nombre	
	FROM centros_educativos.sg_sedes sed
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced sed_cen ON (sed_cen.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
	LEFT OUTER JOIN catalogo.sg_municipios mun on (dir.dir_municipio=mun.mun_pk)
	LEFT OUTER JOIN catalogo.sg_zonas zon on (dir.dir_zona=zon.zon_pk)
	INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
	LEFT OUTER JOIN catalogo.sg_tipos_organismo_administrativo toa ON (sed_cen.ced_tipo_organismo_administrativo_fk = toa.toa_pk)
        WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI');

        INSERT INTO estadisticas.sg_ext_sede_servicios_basicos (sba_ext_cabezal_fk, sba_sede_fk, sba_sba_fk, sba_sba_nombre,sba_tiene_servicio)
        SELECT cabezalPk, sed.sed_pk, inf.sin_pk, inf.sin_nombre, sedeserv.rss_tiene_servicio
        FROM centros_educativos.sg_sedes sed
        JOIN centros_educativos.sg_rel_sede_servicio_infra sedeserv on (sedeserv.rss_sede_fk = sed.sed_pk )
        JOIN catalogo.sg_servicios_infraestructura inf on (inf.sin_pk = sedeserv.rss_servicio_infra_fk);
    END;
$function$
;

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'RE-02', true, now(), 'ADMIN', 0, 'Porcentaje de centros educativos con acceso a servicios básicos', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '08';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'RE-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'RE-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'RE-02';


-- creo tabla docente - tipo sede N-N
CREATE TABLE estadisticas.sg_ext_pers_tipo_sede (
	pts_ext_cabezal_fk int8 NULL,
	pts_per_fk int8 NOT NULL,
	pts_tipo varchar(100) NULL,
	CONSTRAINT sg_ext_fk FOREIGN KEY (pts_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT
);
CREATE INDEX sg_per_tipo_ext_cabezal_idx ON estadisticas.sg_ext_pers_tipo_sede USING btree (pts_ext_cabezal_fk);
ALTER TABLE estadisticas.sg_ext_pers_tipo_sede ADD pts_tipo_cantidad int8 NULL;


INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'D-04';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'D-03';

ALTER TABLE estadisticas.sg_ext_personal ADD ext_per_tiene_nip bool NULL;

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'D-02', true, now(), 'ADMIN', 0, 'Porcentaje de docentes certificados por nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'D-02';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'D-02';

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'D-01', true, now(), 'ADMIN', 0, 'Distribución docentes según nivel educativo', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '07';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'D-01';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'D-01';

CREATE TABLE IF NOT EXISTS estadisticas.sg_ext_est_tipos_parentesco ( 
 tpa_ext_cabezal_fk bigint,
 tpa_est_fk bigint,
 tpa_tpa_fk bigint,
 tpa_tpa_nombre character varying(100), 
 tpa_vive_con boolean,  
 tpa_per_allegada_fk bigint,
 CONSTRAINT sg_ext_fk FOREIGN KEY (tpa_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
CREATE INDEX IF NOT EXISTS sg_allegados_persona_ref_idx ON centros_educativos.sg_allegados (all_persona_ref) ;

CREATE INDEX IF NOT EXISTS sg_allegados_persona_ref_idx ON centros_educativos.sg_allegados (all_persona_ref) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_tipos_parentesco_vive_con_idx ON estadisticas.sg_ext_est_tipos_parentesco (tpa_vive_con) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_tipos_parentesco_cabezal_idx ON estadisticas.sg_ext_est_tipos_parentesco (tpa_ext_cabezal_fk) ;

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
        cabezal_ext_fecha timestamp;
	BEGIN
                cabezal_ext_fecha := (Select ext_fecha_matriculados from estadisticas.sg_extracciones where ext_pk = cabezalPk);
                DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
                DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
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
                        ext_est_mat_promocion_grado)
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
                        niv.niv_orden,
			cic.cic_pk,
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
			(case when (sed.sed_tipo = 'CED_OFI') then 'Público' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end) as tipo_sede,
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
			(case when (gra.gra_edad_maxima is not null and gra.gra_edad_maxima < date_part('year',age(per.per_fecha_nacimiento))) then true else false end) as sobreedad,
                        m.mat_pk,
                        sec.sec_nombre,
                        sec.sec_pk,
                        EXISTS (select 1 from centros_educativos.sg_personas_discapacidades disc where disc.per_pk = per.per_pk),
                        cic.cic_orden,
                        modedu.mod_orden,
                        gra.gra_orden,
                        m.mat_promocion_grado
	FROM centros_educativos.sg_estudiantes est
	INNER JOIN centros_educativos.sg_personas per ON (est.est_persona = per.per_pk)
        LEFT OUTER JOIN catalogo.sg_tipos_trabajo tiptrab ON (per.per_tipo_trabajo_fk = tiptrab.ttr_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_matriculas m ON (est.est_ultima_matricula_fk = m.mat_pk)
	INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
	LEFT OUTER JOIN catalogo.sg_programas_educativos pr ON (se.sdu_programa_educativo_fk = pr.ped_pk)
	LEFT OUTER JOIN centros_educativos.sg_opciones op ON (se.sdu_opcion_fk = op.opc_pk)
	INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
	INNER JOIN catalogo.sg_modalidades_atencion modaten ON (relmodaten.rea_modalidad_atencion_fk = modaten.mat_pk)
	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
	INNER JOIN centros_educativos.sg_organizaciones_curricular org ON (niv.niv_organizacion_curricular = org.ocu_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
        LEFT OUTER JOIN catalogo.sg_zonas zona on (dir.dir_zona=zona.zon_pk)
	WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI') 
        and m.mat_fecha_ingreso <= cabezal_ext_fecha and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= cabezal_ext_fecha)
        and gra.gra_aplica_estadisticas = true;

        INSERT INTO estadisticas.sg_ext_est_discapacidades (dis_ext_cabezal_fk, dis_est_fk, dis_dis_fk, dis_dis_nombre)
        SELECT cabezalPk, est.est_pk, disc.dis_pk, dc.dis_nombre
        FROM centros_educativos.sg_estudiantes est
        INNER JOIN centros_educativos.sg_personas_discapacidades disc ON (disc.per_pk = est.est_persona)
        INNER JOIN catalogo.sg_discapacidades dc ON (disc.dis_pk = dc.dis_pk)
        INNER JOIN centros_educativos.sg_matriculas m ON (est.est_ultima_matricula_fk = m.mat_pk)
        INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
        INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
        WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI') 
        and m.mat_fecha_ingreso <= cabezal_ext_fecha and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= cabezal_ext_fecha)
        and gra.gra_aplica_estadisticas = true;

        INSERT INTO estadisticas.sg_ext_est_tipos_parentesco (tpa_ext_cabezal_fk, tpa_est_fk, tpa_tpa_fk, tpa_tpa_nombre, tpa_vive_con, tpa_per_allegada_fk)
        SELECT cabezalPk, est.est_pk, tpa.tpa_pk, tpa.tpa_nombre, alle.all_vive_con_allegado, alle.all_persona
        FROM centros_educativos.sg_estudiantes est
        INNER JOIN centros_educativos.sg_personas p ON (est.est_persona = p.per_pk)
        INNER JOIN centros_educativos.sg_allegados alle ON (alle.all_persona_ref = p.per_pk)
        INNER JOIN catalogo.sg_tipos_parentesco tpa ON (tpa.tpa_pk = alle.all_tipo_parentesco)
        INNER JOIN centros_educativos.sg_matriculas m ON (est.est_ultima_matricula_fk = m.mat_pk)
        INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
        INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
        WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI') 
        and m.mat_fecha_ingreso <= cabezal_ext_fecha and (m.mat_fecha_hasta is null or m.mat_fecha_hasta >= cabezal_ext_fecha)
        and gra.gra_aplica_estadisticas = true;
        
    END;
$$ LANGUAGE plpgsql;

INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'CC-05', true, now(), 'ADMIN', 0, 'Distribución porcentual de estudiantes según convivencia familiar', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '05';

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SEXO' from estadisticas.sg_indicadores where ind_codigo = 'CC-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'DEPARTAMENTO' from estadisticas.sg_indicadores where ind_codigo = 'CC-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'ZONA' from estadisticas.sg_indicadores where ind_codigo = 'CC-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'SECTOR' from estadisticas.sg_indicadores where ind_codigo = 'CC-05';
INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) Select ind_pk, 'NIVEL' from estadisticas.sg_indicadores where ind_codigo = 'CC-05';

-- filtro secciones que sean abiertas 
CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_personal(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	declare
		aux_ext_anio int8;
	begin
		aux_ext_anio := (Select ext_anio from estadisticas.sg_extracciones where ext_pk = cabezalPk);
        DELETE FROM estadisticas.sg_ext_pers_tipo_sede where pts_ext_cabezal_fk = cabezalPk;
	DELETE FROM estadisticas.sg_ext_pers_sec where sec_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = cabezalPk;
	INSERT INTO estadisticas.sg_ext_personal (
			ext_cabezal_fk,
			ext_per_pk,
			ext_per_sexo,
			ext_per_sexo_nom,
			ext_per_tipo,
			ext_per_edad_al_extraer,
			ext_per_antiguedad_func,
			ext_per_nivel_educativo,
			ext_per_nivel_educativo_nom,
			ext_per_tiene_acceso_internet,
			ext_per_tiene_nip
			)
                        select
			cabezalPk,
			pers.pse_pk,
			sex.sex_pk,
			sex.sex_nombre,
			pers.pse_tipo,
			date_part('year',age(per.per_fecha_nacimiento)),
			pers.pse_anio_servicio,
			mnea.mne_pk,
			mnea.mne_nombre,
			per.per_acceso_internet,
			(case when per.per_nip is not null then true else false end)
	FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_estudios_realizados er ON (pers.pse_pk = er.ere_personal_sede_fk)
	LEFT OUTER JOIN catalogo.sg_maximo_nivel_educativo_alcanzado mnea ON (er.ere_maximo_nivel_educativo_alcanzado_fk = mnea.mne_pk)
        ;

        INSERT INTO estadisticas.sg_ext_pers_sec (sec_ext_cabezal_fk, sec_per_fk, sec_sec_fk, sec_sec_nombre)
        SELECT cabezalPk, pers.pse_pk, sec.sec_pk, sec.sec_nombre
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
           JOIN (select z.docente, z.seccion from (select hes.hes_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            where hes.hes_unico_docente is true
            group by hes.hes_docente_fk, hes.hes_seccion_fk
            union all 
           select cd.cdo_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            join centros_educativos.sg_componentes_docentes cd on (cd.cdo_horario_escolar_fk=hes.hes_pk)
            where hes.hes_unico_docente is false
            group by cd.cdo_docente_fk, hes.hes_seccion_fk) z group by z.docente, z.seccion) x ON (x.docente=pers.pse_pk)
            INNER JOIN centros_educativos.sg_secciones sec ON (x.seccion = sec.sec_pk)
         	inner join centros_educativos.sg_anio_lectivo al on (al.ale_pk=sec.sec_anio_lectivo_fk)
         	where al.ale_anio=aux_ext_anio and sec.sec_estado='ABIERTA';

        INSERT INTO estadisticas.sg_ext_pers_tipo_sede (pts_ext_cabezal_fk, pts_per_fk, pts_tipo)
        SELECT  cabezalPk, pers.pse_pk, (case when (sed.sed_tipo = 'CED_OFI') then 'Público' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end)
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
        inner join centros_educativos.sg_datos_empleado de on (de.dem_pk = pers.pse_dato_empleado_fk)
        inner join centros_educativos.sg_datos_contratacion dc on (dc.dco_dato_empleado_fk=de.dem_pk)
        inner join centros_educativos.sg_sedes sed on(sed.sed_pk=dc.dco_centro_educativo_fk)
        group by pers.pse_pk,sed.sed_tipo;
        
    END;
$function$
;


ALTER TABLE estadisticas.sg_indicadores ADD COLUMN ind_es_publico boolean;
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN ind_es_publico boolean;

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_sedes(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	BEGIN
        DELETE FROM estadisticas.sg_ext_sede_servicios_basicos where sba_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_sedes where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_sedes (
			ext_cabezal_fk,
			ext_sed_pk,
			ext_sed_tipo,
			ext_sed_departamento,
			ext_sed_departamento_nom,
			ext_sed_municipio,
			ext_sed_municipio_nom,
			ext_sed_zona,
			ext_sed_zona_nom,
			ext_sed_subvencionado,
			ext_sed_fines_de_lucro,
			ext_sed_tipo_org_adm_escolar,
			ext_sed_tipo_org_adm_escolar_nom,
			ext_sed_tipo_calendario,
			ext_sed_tipo_calendario_nom																	
			)
            select 
			cabezalPk,
			sed.sed_pk,
			(case when (sed.sed_tipo = 'CED_OFI') then 'Oficial' when (sed.sed_tipo = 'CED_PRI') then 'Privado' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
			mun.mun_pk,
			mun.mun_nombre,
			zon.zon_pk,
			zon.zon_nombre,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
			toa.toa_pk,
			toa.toa_nombre,
			tce.tce_pk,
			tce.tce_nombre	
	FROM centros_educativos.sg_sedes sed
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced sed_cen ON (sed_cen.sed_pk = sed.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
	LEFT OUTER JOIN catalogo.sg_municipios mun on (dir.dir_municipio=mun.mun_pk)
	LEFT OUTER JOIN catalogo.sg_zonas zon on (dir.dir_zona=zon.zon_pk)
	INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
	LEFT OUTER JOIN catalogo.sg_tipos_organismo_administrativo toa ON (sed_cen.ced_tipo_organismo_administrativo_fk = toa.toa_pk)
        WHERE (sed.sed_tipo = 'CED_OFI' or sed.sed_tipo = 'CED_PRI') and sed.sed_habilitado;

        INSERT INTO estadisticas.sg_ext_sede_servicios_basicos (sba_ext_cabezal_fk, sba_sede_fk, sba_sba_fk, sba_sba_nombre,sba_tiene_servicio)
        SELECT cabezalPk, sed.sed_pk, inf.sin_pk, inf.sin_nombre, sedeserv.rss_tiene_servicio
        FROM centros_educativos.sg_sedes sed
        JOIN centros_educativos.sg_rel_sede_servicio_infra sedeserv on (sedeserv.rss_sede_fk = sed.sed_pk )
        JOIN catalogo.sg_servicios_infraestructura inf on (inf.sin_pk = sedeserv.rss_servicio_infra_fk);
    END;
$function$
;

-- 1.5.4
UPDATE estadisticas.sg_indicadores set ind_es_publico = false where ind_es_publico is null;

CREATE OR REPLACE FUNCTION estadisticas.procesar_extracciones_pendientes() RETURNS void AS $$
DECLARE
unlocked boolean;
extraccion record;
err_context text;
lock_id INTEGER DEFAULT 77;
    BEGIN
        unlocked := (SELECT pg_try_advisory_lock(lock_id));
        if (unlocked = false) then
            RAISE NOTICE 'Procesar extracciones pendientes: Locked. Skipping...';
            return;
        end if;
            for extraccion in (select ext_pk, dat_codigo from estadisticas.sg_extracciones INNER JOIN catalogo.sg_est_datasets ds ON (ext_dataset_fk = dat_pk) where ext_estado = 'PENDIENTE')
            LOOP     	
                BEGIN
                    CASE
                        WHEN extraccion.dat_codigo = 'EST' THEN
                            RAISE NOTICE 'Procesando ext estudiantes (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_estudiantes(extraccion.ext_pk);
                        WHEN extraccion.dat_codigo = 'SED' THEN
                            RAISE NOTICE 'Procesando ext sedes (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_sedes(extraccion.ext_pk);
                        WHEN extraccion.dat_codigo = 'PER' THEN
                            RAISE NOTICE 'Procesando ext personal (%)',extraccion.ext_pk;
                            PERFORM estadisticas.procesar_extraccion_personal(extraccion.ext_pk);
                        END CASE;
                        update estadisticas.sg_extracciones set ext_estado = 'FINALIZADA' where ext_pk = extraccion.ext_pk;
                    EXCEPTION WHEN OTHERS THEN
                        GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
                        RAISE NOTICE 'Query exception';
                        RAISE INFO 'Error Name:%',SQLERRM;
                        RAISE INFO 'Error State:%', SQLSTATE;
                        RAISE INFO 'Error Context:%', err_context;
                        update estadisticas.sg_extracciones set ext_estado = 'ERROR', ext_error = concat('Error Name: ', SQLERRM, ' Error State: ', SQLSTATE, ' Error Context: ', err_context) where ext_pk = extraccion.ext_pk;
                    END;    
            END LOOP;	
            PERFORM pg_advisory_unlock(lock_id);
        EXCEPTION 
        WHEN QUERY_CANCELED THEN
		PERFORM pg_advisory_unlock(lock_id);
        WHEN OTHERS THEN
                GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
                RAISE NOTICE 'Query exception';
                RAISE INFO 'Error Name:%',SQLERRM;
                RAISE INFO 'Error State:%', SQLSTATE;
                RAISE INFO 'Error Context:%', err_context;
		PERFORM pg_advisory_unlock(lock_id);
                RAISE EXCEPTION 'Error al procesar.';
    END;
$$ LANGUAGE plpgsql;


-- 1.6.0

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('EDAD_MAXIMA_POBLACION_ESTADISTICAS', 'Edad máxima que será tenida en cuenta para las estadísticas con datos de población', 'edad maxima que sera tenida en cuenta para las estadisticas con datos de poblacion', 0, '24', false);

CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_alcance_extraccion_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_alcance_extraccion ( 
    alc_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_alcance_extraccion_pk_seq' :: regclass ),
    alc_organizacion bigint,
    alc_nivel bigint,
    alc_ciclo bigint,
    alc_modalidad bigint,
    alc_modalidad_atencion bigint,
    alc_submodalidad_atencion bigint,
    alc_grado bigint,
    alc_fecha_matriculas date,
    alc_extraccion bigint,
    alc_ult_mod_fecha timestamp without time zone,
    alc_ult_mod_usuario character varying(45),
    alc_version integer,
    CONSTRAINT sg_alcance_extraccion_pkey PRIMARY KEY (alc_pk),
    CONSTRAINT sg_alcance_extraccion_extraccion_fk FOREIGN KEY (alc_extraccion) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS estadisticas.sg_alcance_extraccion_aud ( 
    alc_pk bigint NOT NULL,
    alc_organizacion bigint,
    alc_nivel bigint,
    alc_ciclo bigint,
    alc_modalidad bigint,
    alc_modalidad_atencion bigint,
    alc_submodalidad_atencion bigint,
    alc_grado bigint,
    alc_fecha_matriculas date,
    alc_extraccion bigint,
    alc_ult_mod_fecha timestamp without time zone,
    alc_ult_mod_usuario character varying(45),
    alc_version integer,
    rev bigint,
    revtype smallint
);
ALTER TABLE estadisticas.sg_alcance_extraccion_aud ADD PRIMARY KEY (alc_pk, rev);



CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_grado_reporta_en_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_grado_reporta_en ( 
    rep_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_grado_reporta_en_pk_seq' :: regclass ),
    rep_grado_origen bigint,
    rep_grado_destino bigint,
    rep_extraccion bigint,
    rep_ult_mod_fecha timestamp without time zone,
    rep_ult_mod_usuario character varying(45),
    rep_version integer,
    CONSTRAINT sg_grado_reporta_en_pkey PRIMARY KEY (rep_pk),
    CONSTRAINT sg_grado_reporta_en_extraccion_fk FOREIGN KEY (rep_extraccion) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);   

CREATE TABLE IF NOT EXISTS estadisticas.sg_grado_reporta_en_aud ( 
    rep_pk bigint NOT NULL,
    rep_grado_origen bigint,
    rep_grado_destino bigint,
    rep_extraccion bigint,
    rep_ult_mod_fecha timestamp without time zone,
    rep_ult_mod_usuario character varying(45),
    rep_version integer,
    rev bigint,
    revtype smallint
);
ALTER TABLE estadisticas.sg_grado_reporta_en_aud ADD PRIMARY KEY (rep_pk, rev);

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
	BEGIN
        DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
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
                        ext_est_mat_promocion_grado)
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
			(case when (sed.sed_tipo = 'CED_OFI') then 'Oficial' when (sed.sed_tipo = 'CED_PRI') then 'Privado' 
                        when (sed.sed_tipo = 'CIR_INF') then 'Oficial' when (sed.sed_tipo = 'CIR_ALF') then 'Alfabetización' when (sed.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede,
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
			(case when (gra.gra_edad_maxima is not null and gra.gra_edad_maxima < date_part('year',age(per.per_fecha_nacimiento))) then true else false end) as sobreedad,
                        m.mat_pk,
                        sec.sec_nombre,
                        sec.sec_pk,
                        EXISTS (select 1 from centros_educativos.sg_personas_discapacidades disc where disc.per_pk = per.per_pk),
                        cic.cic_orden,
                        modedu.mod_orden,
			gra.gra_orden,
                        m.mat_promocion_grado
	FROM centros_educativos.sg_matriculas m 
        INNER JOIN centros_educativos.sg_estudiantes est ON (m.mat_estudiante_fk = est.est_pk)
	INNER JOIN centros_educativos.sg_personas per ON (est.est_persona = per.per_pk)
        LEFT OUTER JOIN catalogo.sg_tipos_trabajo tiptrab ON (per.per_tipo_trabajo_fk = tiptrab.ttr_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
	INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
	INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)
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
        
    END;
$$ LANGUAGE plpgsql;

-- 1.6.1

ALTER TABLE estadisticas.sg_indicadores ADD ind_mostrar_total_sin_desagregacion boolean default false;
ALTER TABLE estadisticas.sg_indicadores_aud ADD ind_mostrar_total_sin_desagregacion boolean;

ALTER TABLE estadisticas.sg_indicadores ADD ind_mostrar_total_desagregacion_por_fila boolean default false;
ALTER TABLE estadisticas.sg_indicadores_aud ADD ind_mostrar_total_desagregacion_por_fila boolean;

ALTER TABLE estadisticas.sg_indicadores ADD ind_mostrar_total_desagregacion_por_columna boolean default false;
ALTER TABLE estadisticas.sg_indicadores_aud ADD ind_mostrar_total_desagregacion_por_columna boolean;


-- 1.7.0

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_sed_subvencionado boolean;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_sed_fines_de_lucro boolean;
ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_sed_sector character varying (30);

ALTER TABLE estadisticas.sg_ext_sedes ADD COLUMN ext_sed_sector character varying (30);

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
	BEGIN
        DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
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
                        ext_sed_sector)
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
			(case when (gra.gra_edad_maxima is not null and gra.gra_edad_maxima < date_part('year',age(per.per_fecha_nacimiento))) then true else false end) as sobreedad,
                        m.mat_pk,
                        sec.sec_nombre,
                        sec.sec_pk,
                        EXISTS (select 1 from centros_educativos.sg_personas_discapacidades disc where disc.per_pk = per.per_pk),
                        cic.cic_orden,
                        modedu.mod_orden,
			gra.gra_orden,
                        m.mat_promocion_grado,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
                        (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede
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
        
    END;
$$ LANGUAGE plpgsql;


ALTER TABLE estadisticas.sg_indicadores ADD COLUMN ind_es_externo boolean;
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN ind_es_externo boolean;

UPDATE estadisticas.sg_indicadores set ind_es_externo = false where ind_es_externo is null;


CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_sedes(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	BEGIN
        DELETE FROM estadisticas.sg_ext_sede_servicios_basicos where sba_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_sedes where ext_cabezal_fk = cabezalPk;
		INSERT INTO estadisticas.sg_ext_sedes (
			ext_cabezal_fk,
			ext_sed_pk,
			ext_sed_tipo,
			ext_sed_departamento,
			ext_sed_departamento_nom,
			ext_sed_municipio,
			ext_sed_municipio_nom,
			ext_sed_zona,
			ext_sed_zona_nom,
			ext_sed_subvencionado,
			ext_sed_fines_de_lucro,
			ext_sed_tipo_org_adm_escolar,
			ext_sed_tipo_org_adm_escolar_nom,
			ext_sed_tipo_calendario,
			ext_sed_tipo_calendario_nom,
                        ext_sed_sector
			)
            select 
			cabezalPk,
			sed.sed_pk,
			(case when (sedeProvieneTipo.sed_tipo = 'CED_OFI') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CED_PRI') then 'Privado' 
			 when (sedeProvieneTipo.sed_tipo = 'CIR_INF') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CIR_ALF') then 'Alfabetización' 
			 when (sedeProvieneTipo.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede,
			depart.dep_pk,
			depart.dep_nombre,
			mun.mun_pk,
			mun.mun_nombre,
			zon.zon_pk,
			zon.zon_nombre,
			COALESCE(sed_pri.pri_subvencionada, FALSE),
			COALESCE(sed_cen.ced_fines_de_lucro, FALSE),
			toa.toa_pk,
			toa.toa_nombre,
			tce.tce_pk,
			tce.tce_nombre,
                        (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede
	FROM centros_educativos.sg_sedes sed
	LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced sed_cen ON (sed_cen.sed_pk = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_direcciones dir on (sed.sed_direccion_fk = dir.dir_pk)
	LEFT OUTER JOIN catalogo.sg_departamentos depart on (dir.dir_departamento=depart.dep_pk)
	LEFT OUTER JOIN catalogo.sg_municipios mun on (dir.dir_municipio=mun.mun_pk)
	LEFT OUTER JOIN catalogo.sg_zonas zon on (dir.dir_zona=zon.zon_pk)
	INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
	LEFT OUTER JOIN catalogo.sg_tipos_organismo_administrativo toa ON (sed_cen.ced_tipo_organismo_administrativo_fk = toa.toa_pk)
        WHERE sed.sed_habilitado;

        INSERT INTO estadisticas.sg_ext_sede_servicios_basicos (sba_ext_cabezal_fk, sba_sede_fk, sba_sba_fk, sba_sba_nombre,sba_tiene_servicio)
        SELECT cabezalPk, sed.sed_pk, inf.sin_pk, inf.sin_nombre, sedeserv.rss_tiene_servicio
        FROM centros_educativos.sg_sedes sed
        JOIN centros_educativos.sg_rel_sede_servicio_infra sedeserv on (sedeserv.rss_sede_fk = sed.sed_pk )
        JOIN catalogo.sg_servicios_infraestructura inf on (inf.sin_pk = sedeserv.rss_servicio_infra_fk);
    END;
$function$
;


UPDATE estadisticas.sg_indicadores_desagregaciones set  ind_desagregacion = 'SECTOR_OFI_PRI' where ind_desagregacion = 'SECTOR';


INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) 
select ind_pk, 'SECTOR_PUB_PRI' from estadisticas.sg_indicadores_desagregaciones where ind_desagregacion = 'SECTOR_OFI_PRI'; 


CREATE INDEX IF NOT EXISTS sg_ext_est_sed_sector_idx ON estadisticas.sg_ext_estudiantes (ext_sed_sector) ;
CREATE INDEX IF NOT EXISTS sg_ext_est_sed_subvencionado_idx ON estadisticas.sg_ext_estudiantes (ext_sed_subvencionado) ;


CREATE TABLE estadisticas.sg_ext_pers_sector_sede (
	pts_ext_cabezal_fk int8 NULL,
	pts_per_fk int8 NOT NULL,
	pts_sector varchar(100) NULL,
	CONSTRAINT sg_ext_sector_fk FOREIGN KEY (pts_ext_cabezal_fk) REFERENCES estadisticas.sg_extracciones(ext_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT
);
CREATE INDEX sg_per_sector_ext_cabezal_idx ON estadisticas.sg_ext_pers_sector_sede USING btree (pts_ext_cabezal_fk);


CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_personal(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	declare
		aux_ext_anio int8;
	begin
		aux_ext_anio := (Select ext_anio from estadisticas.sg_extracciones where ext_pk = cabezalPk);
        DELETE FROM estadisticas.sg_ext_pers_tipo_sede where pts_ext_cabezal_fk = cabezalPk;
	DELETE FROM estadisticas.sg_ext_pers_sec where sec_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = cabezalPk;
	INSERT INTO estadisticas.sg_ext_personal (
			ext_cabezal_fk,
			ext_per_pk,
			ext_per_sexo,
			ext_per_sexo_nom,
			ext_per_tipo,
			ext_per_edad_al_extraer,
			ext_per_antiguedad_func,
			ext_per_nivel_educativo,
			ext_per_nivel_educativo_nom,
			ext_per_tiene_acceso_internet,
			ext_per_tiene_nip
			)
                        select
			cabezalPk,
			pers.pse_pk,
			sex.sex_pk,
			sex.sex_nombre,
			pers.pse_tipo,
			date_part('year',age(per.per_fecha_nacimiento)),
			pers.pse_anio_servicio,
			mnea.mne_pk,
			mnea.mne_nombre,
			per.per_acceso_internet,
			(case when per.per_nip is not null then true else false end)
	FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_estudios_realizados er ON (pers.pse_pk = er.ere_personal_sede_fk)
	LEFT OUTER JOIN catalogo.sg_maximo_nivel_educativo_alcanzado mnea ON (er.ere_maximo_nivel_educativo_alcanzado_fk = mnea.mne_pk)
        ;

        INSERT INTO estadisticas.sg_ext_pers_sec (sec_ext_cabezal_fk, sec_per_fk, sec_sec_fk, sec_sec_nombre)
        SELECT cabezalPk, pers.pse_pk, sec.sec_pk, sec.sec_nombre
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
           JOIN (select z.docente, z.seccion from (select hes.hes_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            where hes.hes_unico_docente is true
            group by hes.hes_docente_fk, hes.hes_seccion_fk
            union all 
           select cd.cdo_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            join centros_educativos.sg_componentes_docentes cd on (cd.cdo_horario_escolar_fk=hes.hes_pk)
            where hes.hes_unico_docente is false
            group by cd.cdo_docente_fk, hes.hes_seccion_fk) z group by z.docente, z.seccion) x ON (x.docente=pers.pse_pk)
            INNER JOIN centros_educativos.sg_secciones sec ON (x.seccion = sec.sec_pk)
         	inner join centros_educativos.sg_anio_lectivo al on (al.ale_pk=sec.sec_anio_lectivo_fk)
         	where al.ale_anio=aux_ext_anio;

        INSERT INTO estadisticas.sg_ext_pers_tipo_sede (pts_ext_cabezal_fk, pts_per_fk, pts_tipo)
        SELECT  cabezalPk, pers.pse_pk, 
        (case when (sedeProvieneTipo.sed_tipo = 'CED_OFI') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CED_PRI') then 'Privado' 
			 when (sedeProvieneTipo.sed_tipo = 'CIR_INF') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CIR_ALF') then 'Alfabetización' 
			 when (sedeProvieneTipo.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
        inner join centros_educativos.sg_datos_empleado de on (de.dem_pk = pers.pse_dato_empleado_fk)
        inner join centros_educativos.sg_datos_contratacion dc on (dc.dco_dato_empleado_fk=de.dem_pk)
        inner join centros_educativos.sg_sedes sed on(sed.sed_pk=dc.dco_centro_educativo_fk)
        LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
        group by pers.pse_pk,tipo_sede;

        INSERT INTO estadisticas.sg_ext_pers_sector_sede (pts_ext_cabezal_fk, pts_per_fk, pts_sector)
        SELECT  cabezalPk, pers.pse_pk, 
        (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
        inner join centros_educativos.sg_datos_empleado de on (de.dem_pk = pers.pse_dato_empleado_fk)
        inner join centros_educativos.sg_datos_contratacion dc on (dc.dco_dato_empleado_fk=de.dem_pk)
        inner join centros_educativos.sg_sedes sed on(sed.sed_pk=dc.dco_centro_educativo_fk)
        LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sedeProvieneTipo.sed_pk)
        group by pers.pse_pk,sector_sede;
        
    END;
$function$
;

 -- 1.8.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CARGAS_EXTERNAS','ESM5', '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARGAS_EXTERNAS','ES8',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARGAS_EXTERNAS','ES9',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARGAS_EXTERNAS','ES10',  '', 6, true, null, null,0);


 
CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_cargas_externas_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_cargas_externas ( 
    car_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_cargas_externas_pk_seq' :: regclass ),
    car_anio integer,
    car_indicador_fk bigint,
    car_desagregacion character varying(45),
    car_nombre_fk bigint,
    car_descripcion character varying(2000),
    car_ult_mod_fecha timestamp without time zone,
    car_ult_mod_usuario character varying(45),
    car_version integer,
    CONSTRAINT sg_cargas_externas_pkey PRIMARY KEY (car_pk),
    CONSTRAINT sg_car_indicador_fk FOREIGN KEY (car_indicador_fk) REFERENCES estadisticas.sg_indicadores(ind_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_car_nombre_fk FOREIGN KEY (car_nombre_fk) REFERENCES catalogo.sg_est_nombres_extracciones(nex_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_car_anio_ind_des_nom_uk UNIQUE (car_anio, car_indicador_fk, car_desagregacion, car_nombre_fk)
);

CREATE UNIQUE INDEX IF NOT EXISTS sg_car_anio_ind_desnull_nom_uk ON estadisticas.sg_cargas_externas (car_anio, car_indicador_fk,  car_nombre_fk) where car_desagregacion is null;

CREATE TABLE IF NOT EXISTS estadisticas.sg_cargas_externas_aud ( 
    car_pk bigint NOT NULL,
    car_anio integer,
    car_indicador_fk bigint,
    car_desagregacion character varying(45),
    car_nombre_fk bigint,
    car_descripcion character varying(2000),
    car_ult_mod_fecha timestamp without time zone,
    car_ult_mod_usuario character varying(45),
    car_version integer,
    rev bigint,
    revtype smallint
);
ALTER TABLE estadisticas.sg_cargas_externas_aud ADD PRIMARY KEY (car_pk, rev);


CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_tuplas_carga_externa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_tuplas_carga_externa ( 
    tup_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_tuplas_carga_externa_pk_seq' :: regclass ),
    tup_carga_externa_fk bigint,
    tup_identificador character varying(200),
    tup_valor numeric(16,2),
    tup_desagregacion character varying(200),
    tup_version integer,
    CONSTRAINT sg_tuplas_carga_externa_pkey PRIMARY KEY (tup_pk),
    CONSTRAINT sg_tupla_carga_externa_fk FOREIGN KEY (tup_carga_externa_fk) REFERENCES estadisticas.sg_cargas_externas(car_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE INDEX IF NOT EXISTS sg_tup_carga_externa_ce_idx ON estadisticas.sg_tuplas_carga_externa (tup_carga_externa_fk) ;
CREATE INDEX IF NOT EXISTS sg_tup_carga_externa_des_idx ON estadisticas.sg_tuplas_carga_externa (tup_desagregacion) ;
CREATE INDEX IF NOT EXISTS sg_tup_carga_externa_id_idx ON estadisticas.sg_tuplas_carga_externa (tup_identificador) ;

ALTER TABLE estadisticas.sg_cargas_externas ADD COLUMN car_tipo_numerico_valor character varying(45);
ALTER TABLE estadisticas.sg_cargas_externas_aud ADD COLUMN car_tipo_numerico_valor character varying(45);


-- 1.9.2

ALTER TABLE estadisticas.sg_indicadores ADD COLUMN  ind_tipo_resultado character varying (45);
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN  ind_tipo_resultado character varying (45);

UPDATE estadisticas.sg_indicadores set ind_tipo_resultado = 'CANTIDAD';

CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
	BEGIN
        DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
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
                        ext_sed_sector)
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
			(case when (gra.gra_edad_maxima is not null and gra.gra_edad_maxima < date_part('year',age(per.per_fecha_nacimiento))) then true else false end) as sobreedad,
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
                        else 'Público' end) as sector_sede
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
        
    END;
$$ LANGUAGE plpgsql;

ALTER TABLE estadisticas.sg_indicadores ALTER COLUMN ind_definicion TYPE TEXT;
ALTER TABLE estadisticas.sg_indicadores_aud ALTER COLUMN ind_definicion TYPE TEXT;

ALTER TABLE estadisticas.sg_indicadores ALTER COLUMN ind_metodo_calculo TYPE TEXT;
ALTER TABLE estadisticas.sg_indicadores_aud ALTER COLUMN ind_metodo_calculo TYPE TEXT;

ALTER TABLE estadisticas.sg_indicadores ALTER COLUMN ind_descripcion TYPE TEXT;
ALTER TABLE estadisticas.sg_indicadores_aud ALTER COLUMN ind_descripcion TYPE TEXT;

ALTER TABLE estadisticas.sg_indicadores ALTER COLUMN ind_fuente TYPE TEXT;
ALTER TABLE estadisticas.sg_indicadores_aud ALTER COLUMN ind_fuente TYPE TEXT;

ALTER TABLE estadisticas.sg_indicadores ALTER COLUMN ind_observaciones TYPE TEXT;
ALTER TABLE estadisticas.sg_indicadores_aud ALTER COLUMN ind_observaciones TYPE TEXT;


-- 1.10.0


CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_indicadores_materializados_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores_materializados ( 
    ind_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_indicadores_materializados_pk_seq' :: regclass ),
    ind_anio integer,
    ind_indicador_fk bigint,
    ind_desagregacion character varying(45),
    ind_nombre_fk bigint,
    ind_ult_mod_fecha timestamp without time zone,
    ind_ult_mod_usuario character varying(45),
    ind_version integer,
    CONSTRAINT sg_ind_mat_pkey PRIMARY KEY (ind_pk),
    CONSTRAINT sg_ind_mat_indicador_fk FOREIGN KEY (ind_indicador_fk) REFERENCES estadisticas.sg_indicadores(ind_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_ind_mat_nombre_fk FOREIGN KEY (ind_nombre_fk) REFERENCES catalogo.sg_est_nombres_extracciones(nex_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_ind_mat_anio_ind_des_uk UNIQUE (ind_anio, ind_indicador_fk, ind_desagregacion)
);

CREATE UNIQUE INDEX IF NOT EXISTS sg_ind_mat_anio_ind_desnull_uk ON estadisticas.sg_indicadores_materializados (ind_anio, ind_indicador_fk) where ind_desagregacion is null;

CREATE TABLE IF NOT EXISTS estadisticas.sg_indicadores_materializados_aud ( 
    ind_pk bigint NOT NULL,
    ind_anio integer,
    ind_indicador_fk bigint,
    ind_desagregacion character varying(45),
    ind_nombre_fk bigint,
    ind_ult_mod_fecha timestamp without time zone,
    ind_ult_mod_usuario character varying(45),
    ind_version integer,
    rev bigint,
    revtype smallint
);
ALTER TABLE estadisticas.sg_indicadores_materializados_aud ADD PRIMARY KEY (ind_pk, rev);

ALTER TABLE estadisticas.sg_indicadores_materializados ADD COLUMN ind_tipo_numerico_valor character varying(45);
ALTER TABLE estadisticas.sg_indicadores_materializados_aud ADD COLUMN ind_tipo_numerico_valor character varying(45);


CREATE SEQUENCE IF NOT EXISTS estadisticas.sg_tuplas_indicador_materializado_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS estadisticas.sg_tuplas_indicador_materializado ( 
    tup_pk bigint NOT NULL DEFAULT nextval('estadisticas.sg_tuplas_indicador_materializado_pk_seq' :: regclass ),
    tup_indicador_materializado_fk bigint,
    tup_identificador character varying(200),
    tup_valor numeric(16,2),
    tup_desagregacion character varying(200),
    tup_version integer,
    CONSTRAINT sg_tuplas_indicador_mat_pkey PRIMARY KEY (tup_pk),
    CONSTRAINT sg_tuplas_indicador_mat_indmat_fk FOREIGN KEY (tup_indicador_materializado_fk) REFERENCES estadisticas.sg_indicadores_materializados(ind_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);

ALTER TABLE estadisticas.sg_cargas_externas DROP CONSTRAINT sg_car_anio_ind_des_nom_uk;
DROP INDEX estadisticas.sg_car_anio_ind_desnull_nom_uk;

CREATE UNIQUE INDEX IF NOT EXISTS sg_car_anio_ind_des_uk ON estadisticas.sg_cargas_externas (car_anio, car_indicador_fk, car_desagregacion);
CREATE UNIQUE INDEX IF NOT EXISTS sg_car_anio_ind_desnull_uk ON estadisticas.sg_cargas_externas (car_anio, car_indicador_fk) where car_desagregacion is null;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INDICADORES_MATERIALIZADOS','ES11',  '', 6, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INDICADORES_MATERIALIZADOS','ES12',  '', 6, true, null, null,0);

-- 1.11.0

ALTER TABLE estadisticas.sg_ext_estudiantes ADD COLUMN ext_est_sobreedad_anios integer;

INSERT INTO estadisticas.sg_indicadores_desagregaciones (ind_pk, ind_desagregacion) VALUES (28, 'CANTIDAD_ANIOS');


CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_estudiantes(cabezalPk bigint) 
RETURNS void AS $$
        DECLARE
	BEGIN
        DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = cabezalPk;
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
        
    END;
$$ LANGUAGE plpgsql;


INSERT INTO estadisticas.sg_indicadores(ind_codigo, ind_habilitado, ind_ult_mod_fecha, ind_ult_mod_usuario, ind_version, ind_nombre, ind_categoria_fk) Select 'F-04', true, now(), 'ADMIN', 0, 'Porcentaje de estudiantes según nivel de logro en la PAES', cin_pk from catalogo.sg_est_categorias_indicadores where cin_codigo = '06';


UPDATE estadisticas.sg_indicadores set ind_es_publico = false, ind_es_externo = false where ind_codigo = 'F-04';


ALTER TABLE estadisticas.sg_indicadores ADD COLUMN ind_precision integer;
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN ind_precision integer;

update estadisticas.sg_indicadores SET ind_precision = 0;

ALTER TABLE estadisticas.sg_indicadores ADD COLUMN ind_aplica_paridad_genero boolean;
ALTER TABLE estadisticas.sg_indicadores_aud ADD COLUMN ind_aplica_paridad_genero boolean;

update estadisticas.sg_indicadores SET ind_aplica_paridad_genero = false;


CREATE OR REPLACE FUNCTION estadisticas.procesar_extraccion_personal(cabezalpk bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
	declare
		aux_ext_anio int8;
	begin
		aux_ext_anio := (Select ext_anio from estadisticas.sg_extracciones where ext_pk = cabezalPk);
        DELETE FROM estadisticas.sg_ext_pers_tipo_sede where pts_ext_cabezal_fk = cabezalPk;
	DELETE FROM estadisticas.sg_ext_pers_sec where sec_ext_cabezal_fk = cabezalPk;
        DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = cabezalPk;
	INSERT INTO estadisticas.sg_ext_personal (
			ext_cabezal_fk,
			ext_per_pk,
			ext_per_sexo,
			ext_per_sexo_nom,
			ext_per_tipo,
			ext_per_edad_al_extraer,
			ext_per_antiguedad_func,
			ext_per_nivel_educativo,
			ext_per_nivel_educativo_nom,
			ext_per_tiene_acceso_internet,
			ext_per_tiene_nip
			)
                        select
			cabezalPk,
			pers.pse_pk,
			sex.sex_pk,
			sex.sex_nombre,
			pers.pse_tipo,
			date_part('year',age(per.per_fecha_nacimiento)),
			pers.pse_anio_servicio,
			mnea.mne_pk,
			mnea.mne_nombre,
			per.per_acceso_internet,
			(case when per.per_nip is not null then true else false end)
	FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk)
	INNER JOIN catalogo.sg_sexos sex ON (per.per_sexo_fk = sex.sex_pk)
	INNER JOIN centros_educativos.sg_estudios_realizados er ON (pers.pse_pk = er.ere_personal_sede_fk)
	LEFT OUTER JOIN catalogo.sg_maximo_nivel_educativo_alcanzado mnea ON (er.ere_maximo_nivel_educativo_alcanzado_fk = mnea.mne_pk)
        ;

        INSERT INTO estadisticas.sg_ext_pers_sec (sec_ext_cabezal_fk, sec_per_fk, sec_sec_fk, sec_sec_nombre)
        SELECT cabezalPk, pers.pse_pk, sec.sec_pk, sec.sec_nombre
        FROM centros_educativos.sg_personal_sede_educativa pers
           INNER JOIN (select z.docente, z.seccion from (select hes.hes_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            where hes.hes_unico_docente is true
            group by hes.hes_docente_fk, hes.hes_seccion_fk
            union all 
           select cd.cdo_docente_fk docente, hes.hes_seccion_fk seccion from centros_educativos.sg_horarios_escolares hes
            join centros_educativos.sg_componentes_docentes cd on (cd.cdo_horario_escolar_fk=hes.hes_pk)
            where hes.hes_unico_docente is false
            group by cd.cdo_docente_fk, hes.hes_seccion_fk) z group by z.docente, z.seccion) x ON (x.docente=pers.pse_pk)
            INNER JOIN centros_educativos.sg_secciones sec ON (x.seccion = sec.sec_pk)
         	inner join centros_educativos.sg_anio_lectivo al on (al.ale_pk=sec.sec_anio_lectivo_fk)
         	where al.ale_anio=aux_ext_anio;

        INSERT INTO estadisticas.sg_ext_pers_tipo_sede (pts_ext_cabezal_fk, pts_per_fk, pts_tipo)
        SELECT  cabezalPk, pers.pse_pk, 
        (case when (sedeProvieneTipo.sed_tipo = 'CED_OFI') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CED_PRI') then 'Privado' 
			 when (sedeProvieneTipo.sed_tipo = 'CIR_INF') then 'Oficial' when (sedeProvieneTipo.sed_tipo = 'CIR_ALF') then 'Alfabetización' 
			 when (sedeProvieneTipo.sed_tipo = 'UBI_EDUC') then 'Educame' end) as tipo_sede
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
        inner join centros_educativos.sg_datos_empleado de on (de.dem_pk = pers.pse_dato_empleado_fk)
        inner join centros_educativos.sg_datos_contratacion dc on (dc.dco_dato_empleado_fk=de.dem_pk)
        inner join centros_educativos.sg_sedes sed on(sed.sed_pk=dc.dco_centro_educativo_fk)
        LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
        group by pers.pse_pk,tipo_sede;

        INSERT INTO estadisticas.sg_ext_pers_sector_sede (pts_ext_cabezal_fk, pts_per_fk, pts_sector)
        SELECT  cabezalPk, pers.pse_pk, 
        (case when (sedeProvieneTipo.sed_tipo = 'CED_PRI' and sed_pri.pri_subvencionada = false) then 'Privado' 
                        else 'Público' end) as sector_sede
        FROM centros_educativos.sg_personal_sede_educativa pers
	INNER JOIN centros_educativos.sg_personas per ON (pers.pse_persona_fk = per.per_pk) 
        inner join centros_educativos.sg_datos_empleado de on (de.dem_pk = pers.pse_dato_empleado_fk)
        inner join centros_educativos.sg_datos_contratacion dc on (dc.dco_dato_empleado_fk=de.dem_pk)
        inner join centros_educativos.sg_sedes sed on(sed.sed_pk=dc.dco_centro_educativo_fk)
        LEFT OUTER JOIN centros_educativos.sg_sedes sedPadre ON (sed.sed_centro_adscrito = sedPadre.sed_pk)
	INNER JOIN centros_educativos.sg_sedes sedeProvieneTipo ON (case when sedPadre is null then sed.sed_pk else sedPadre.sed_pk end = sedeProvieneTipo.sed_pk)
	LEFT OUTER JOIN centros_educativos.sg_sedes_ced_pri sed_pri ON (sed_pri.sed_pk = sedeProvieneTipo.sed_pk)
        group by pers.pse_pk,sector_sede;
        
    END;
$function$
;