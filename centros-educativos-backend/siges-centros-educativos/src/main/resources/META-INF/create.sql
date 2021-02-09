-- 4.1.0

ALTER TABLE centros_educativos.sg_datos_contratacion ADD COLUMN dco_tipo character varying(25);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_tipo character varying(25);


DELETE FROM centros_educativos.sg_estudios_realizados where ere_personal_sede_fk IN (109684, 109824);
DELETE FROM centros_educativos.sg_docente where pse_pk IN (109684, 109824);
DELETE FROM centros_educativos.sg_personal_sede_educativa where pse_pk IN (109684, 109824);

UPDATE centros_educativos.sg_datos_contratacion dd set dco_tipo =
(select p.pse_tipo from centros_educativos.sg_datos_contratacion dc INNER JOIN centros_educativos.sg_datos_empleado de ON (dc.dco_dato_empleado_fk = de.dem_pk)
INNER JOIN centros_educativos.sg_personal_sede_educativa p ON (de.dem_pk = p.pse_dato_empleado_fk)
where dc.dco_pk = dd.dco_pk);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PERSONAL_SEDE_EDUCATIVA','E106',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PERSONAL_SEDE_EDUCATIVA','E107',  '', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_datos_empleado ADD COLUMN dem_puede_aplicar_plaza boolean;
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD COLUMN dem_puede_aplicar_plaza boolean;

UPDATE centros_educativos.sg_datos_empleado de set dem_puede_aplicar_plaza = true where 
EXISTS (select 1 from centros_educativos.sg_personal_sede_educativa where pse_dato_empleado_fk = de.dem_pk and pse_tipo = 'DOC');

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_INDICAR_SI_PERSONAL_APLICA_PARA_PLAZA','EP91',  'Permite indicar si personal puede aplicar a plazas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_TIPO_PERSONAL_DATO_CONTRATACION','EP92',  'Permite modificar el tipo de personal de un dato de contratación', 1, true, null, null,0);

ALTER TABLE seguridad.sg_usuario_rol DROP CONSTRAINT sg_personas_usuario_contexto_fk;
ALTER TABLE seguridad.sg_usuario_rol
    ADD CONSTRAINT sg_personas_usuario_contexto_fk FOREIGN KEY (pur_contexto_fk)
    REFERENCES seguridad.sg_contextos (con_pk) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

--4.3.0
--Columna que indica si la persona tiene hijos
alter table centros_educativos.sg_personas add per_tiene_hijos bool;
alter table centros_educativos.sg_personas_aud add per_tiene_hijos bool;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tiene_hijos IS 'Bandera que indica si la persona tiene hijos.';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO','E429',  'Permite calificar sin que el usuario tenga horario escolar asignado en el componente y sección', 1, true, null, null,0);
-- Asignación perfiles
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_asignacion_perfil_ape_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignacion_perfil (ape_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_asignacion_perfil_ape_pk_seq'::regclass), 
ape_sede_fk bigint, ape_ult_mod_fecha timestamp without time zone, ape_ult_mod_usuario character varying(45), ape_version integer, 
CONSTRAINT sg_asignacion_perfil_pkey PRIMARY KEY (ape_pk),
CONSTRAINT sg_ape_sede_fk FOREIGN KEY (ape_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_asignacion_perfil IS 'Tabla para el registro de asignación perfiles.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil.ape_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil.ape_sede_fk IS 'Sede del registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil.ape_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil.ape_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil.ape_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignacion_perfil_aud (ape_pk bigint NOT NULL,ape_sede_fk bigint, ape_perfil_fk bigint, ape_personal_fk bigint, ape_ult_mod_fecha timestamp without time zone, ape_ult_mod_usuario character varying(45), ape_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_asignacion_perfil_aud ADD PRIMARY KEY (ape_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ASIGNACION_PERFIL','EB94',  'Buscar asignacion perfil', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASIGNACION_PERFIL','E423',  'Crear asignación perfil', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASIGNACION_PERFIL','E424',  'Actualizar asignación perfil', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASIGNACION_PERFIL','E425',  'Eliminar asignación perfil', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ASIGNACION_PERFIL','EM79',  'MENU:Asignación perfil', 1, true, null, null,0);


-- Asignación perfiles Personal
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_asignacion_perfil_personal_app_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignacion_perfil_personal (app_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_asignacion_perfil_personal_app_pk_seq'::regclass), 
app_asignacion_perfil_fk bigint, app_perfil_fk bigint, app_personal_fk bigint,  app_ult_mod_fecha timestamp without time zone, app_ult_mod_usuario character varying(45), app_version integer,
 CONSTRAINT sg_asignacion_perfil_personal_pkey PRIMARY KEY (app_pk),
CONSTRAINT sg_app_asignacion_perfil_fk FOREIGN KEY (app_asignacion_perfil_fk) REFERENCES centros_educativos.sg_asignacion_perfil(ape_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ape_perfil_fk FOREIGN KEY (app_perfil_fk) REFERENCES catalogo.sg_perfiles_usuarios_ingresados_ce(pui_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_ape_personal_fk FOREIGN KEY (app_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_asignacion_perfil_personal IS 'Tabla para el registro de asignación perfiles personal.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_perfil_fk IS 'Perfil del registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_personal_fk IS 'Personal del registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_asignacion_perfil_personal.app_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignacion_perfil_personal_aud (app_pk bigint NOT NULL, app_asignacion_perfil_fk bigint, app_perfil_fk bigint, app_personal_fk bigint,   app_ult_mod_fecha timestamp without time zone, app_ult_mod_usuario character varying(45), app_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_asignacion_perfil_personal_aud ADD PRIMARY KEY (app_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASIGNACION_PERFIL_PERSONAL','E426',  'Crear asignación perfil personal', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASIGNACION_PERFIL_PERSONAL','E427',  'Actualizar asignación perfil personal', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASIGNACION_PERFIL_PERSONAL','E428',  'Eliminar asignación perfil personal', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_asignacion_perfil ADD CONSTRAINT sg_asignacion_perfil_sede_uk UNIQUE (ape_sede_fk);
ALTER TABLE centros_educativos.sg_asignacion_perfil_personal ADD CONSTRAINT sg_asignacion_perfil_personal_perfil_uk UNIQUE (app_asignacion_perfil_fk,app_perfil_fk);


-- 4.3.5

--Permiso para anular un título
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_ANULAR_TITULO','EP93',  'Permite anular un título', 1, true, null, null,0);	

UPDATE centros_educativos.sg_confirmaciones_matriculas set cma_firmada = false where cma_firmada is null;

--4.3.6
--Permiso para anular un título
ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_fecha_cierre_seccion date;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_fecha_cierre_seccion date;


--4.3.7

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_todas_promociones_grado_realizadas boolean;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_todas_promociones_grado_realizadas boolean;

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_todas_promociones_grado_realizadas boolean default false;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_todas_promociones_grado_realizadas boolean;


ALTER TABLE centros_educativos.sg_grados DROP COLUMN gra_aplica_estadisticas;
ALTER TABLE centros_educativos.sg_grados_aud DROP COLUMN gra_aplica_estadisticas;

UPDATE centros_educativos.sg_calificaciones c SET cal_todas_promociones_grado_realizadas =
(SELECT
EXISTS (SELECT 1 FROM centros_educativos.sg_calificaciones_estudiante ce WHERE ce.cae_calificacion_fk = c.cal_pk)
AND NOT
EXISTS (SELECT 1 FROM centros_educativos.sg_calificaciones_estudiante ce WHERE ce.cae_calificacion_fk = c.cal_pk
AND (ce.cae_promovido_calificacion is null or ce.cae_promovido_calificacion = 'PENDIENTE')))
where c.cal_tipo_periodo_calificacion = 'GRA';
 
UPDATE centros_educativos.sg_secciones s SET sec_todas_promociones_grado_realizadas =
(select cal_todas_promociones_grado_realizadas from centros_educativos.sg_calificaciones c
 where c.cal_seccion_fk = s.sec_pk AND c.cal_tipo_periodo_calificacion = 'GRA')
 where s.sec_pk IN (select s2.sec_pk from centros_educativos.sg_secciones s2 INNER JOIN centros_educativos.sg_calificaciones c2 ON (c2.cal_seccion_fk = s2.sec_pk)
 where c2.cal_tipo_periodo_calificacion = 'GRA');


--4.4.0
--Operación para opción de menú
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ORGANISMO_ORGANIZACION_ESCOLAR','EM82',  'MENU: Organismo de organización escolar', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_modulos_diplomado ADD COLUMN mdi_escala_calificacion_fk bigint;
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD COLUMN mdi_escala_calificacion_fk bigint;
ALTER TABLE centros_educativos.sg_modulos_diplomado ADD CONSTRAINT sg_mdi_escala_calificacion_fk FOREIGN KEY (mdi_escala_calificacion_fk) REFERENCES catalogo.sg_escalas_calificacion(eca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_modulos_diplomado ADD COLUMN mdi_periodos_calificacion bigint;
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD COLUMN mdi_periodos_calificacion bigint;

ALTER TABLE centros_educativos.sg_modulos_diplomado ADD COLUMN mdi_calculo_nota_institucional CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_calculo_nota_institucional IS 'Tipo de calculo nota institucional del registro.';
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD COLUMN mdi_calculo_nota_institucional CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_modulos_diplomado ADD COLUMN mdi_funcion_redondeo CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_funcion_redondeo IS 'Tipo de funcion de redondeo del registro.';
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD COLUMN mdi_funcion_redondeo CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_modulos_diplomado ADD COLUMN mdi_precision integer;
COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_precision  IS 'Precision del registro.';
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD COLUMN mdi_precision integer;



-- Calificación diplomado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado_cad_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado (cad_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_calificacion_diplomado_cad_pk_seq'::regclass),cad_sede_fk bigint,cad_anio_lectivo_fk bigint,cad_modulo_diplomado_fk bigint,cad_numero_periodo integer,cad_tipo_calificacion varchar(10), cad_ult_mod_fecha timestamp without time zone, cad_ult_mod_usuario character varying(45), cad_version integer, 
CONSTRAINT sg_calificacion_diplomado_pkey PRIMARY KEY (cad_pk),
CONSTRAINT sg_cad_sede_fk FOREIGN KEY (cad_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_cad_anio_lectivo_fk FOREIGN KEY (cad_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_cad_modulo_diplomado_fk FOREIGN KEY (cad_modulo_diplomado_fk) REFERENCES centros_educativos.sg_modulos_diplomado(mdi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_calificacion_diplomado IS 'Tabla para el registro de calificación diplomado.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado.cad_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado.cad_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado.cad_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado.cad_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado_aud (cad_pk bigint NOT NULL,cad_sede_fk bigint,cad_anio_lectivo_fk bigint,cad_modulo_diplomado_fk bigint,cad_numero_periodo integer,cad_tipo_calificacion varchar(10), cad_ult_mod_fecha timestamp without time zone, cad_ult_mod_usuario character varying(45), cad_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_calificacion_diplomado_aud ADD PRIMARY KEY (cad_pk, rev);

-- Calificación diplomado estudiante
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado_estudiante_cde_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado_estudiante (cde_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_calificacion_diplomado_estudiante_cde_pk_seq'::regclass), cde_estudiante_fk bigint,cde_calificacion_diplomado_fk bigint,cde_calificacion_numerica character varying(45),cde_calificacion_conceptual_fk bigint,cde_observacion character varying(255),cde_fecha_realizado date, cde_ult_mod_fecha timestamp without time zone, cde_ult_mod_usuario character varying(45), cde_version integer, 
CONSTRAINT sg_calificacion_diplomado_estudiante_pkey PRIMARY KEY (cde_pk),
CONSTRAINT sg_cde_estudiante_fk FOREIGN KEY (cde_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_cde_calificacion_diplomado_fk FOREIGN KEY (cde_calificacion_diplomado_fk) REFERENCES centros_educativos.sg_calificacion_diplomado(cad_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_cde_calificacion_conceptual_fk FOREIGN KEY (cde_calificacion_conceptual_fk) REFERENCES catalogo.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_calificacion_diplomado_estudiante IS 'Tabla para el registro de calificación diplomado estudiante.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado_estudiante.cde_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado_estudiante.cde_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado_estudiante.cde_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificacion_diplomado_estudiante.cde_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificacion_diplomado_estudiante_aud (cde_pk bigint NOT NULL, cde_estudiante_fk bigint,cde_calificacion_diplomado_fk bigint,cde_calificacion_numerica character varying(45),cde_calificacion_conceptual_fk bigint,cde_observacion character varying(255),cde_fecha_realizado date,  cde_ult_mod_fecha timestamp without time zone, cde_ult_mod_usuario character varying(45), cde_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_calificacion_diplomado_estudiante_aud ADD PRIMARY KEY (cde_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACION_DIPLOMADO_ESTUDIANTE','EB90',  'Buscar calificacion diplomado estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACION_DIPLOMADO','EB91',  'Buscar calificación diplomado', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACION_DIPLOMADO','E394',  'crear calificación diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACION_DIPLOMADO','E395',  'actualizar caificación diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACION_DIPLOMADO','E396',  'eliminar calificación diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACION_DIPLOMADO_ESTUDIANTE','E397',  'Crear calificación diplomado estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACION_DIPLOMADO_ESTUDIANTE','E398',  'Actualizar calificación diplomado estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACION_DIPLOMADO_ESTUDIANTE','E399',  'Eliminar calificación diplomado estudiante', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_DIPLOMADO','EM80',  'MENU: Calificacion diplomado', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_diplomados_estudiante ADD COLUMN dep_sede_fk bigint;
ALTER TABLE centros_educativos.sg_diplomados_estudiante_aud ADD COLUMN dep_sede_fk bigint;
ALTER TABLE centros_educativos.sg_diplomados_estudiante ADD CONSTRAINT sg_dep_sede_fk FOREIGN KEY (dep_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_diplomados_estudiante ADD CONSTRAINT sg_sg_diplomados_estudiante_diplomado_est_anio_uk UNIQUE (dep_anio_lectivo_fk, dep_estudiante_fk, dep_diplomado_fk);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_PERIODO_DIPLOMADO','EM81',  'MENU: Calificacion período diplomado', 1, true, null, null,0);

CREATE INDEX sg_calificacion_diplomado_sede_idx ON centros_educativos.sg_calificacion_diplomado USING btree (cad_sede_fk);
CREATE INDEX sg_calificacion_diplomado_anio_lect_idx ON centros_educativos.sg_calificacion_diplomado USING btree (cad_anio_lectivo_fk);
CREATE INDEX sg_calificacion_diplomado_modulo_dipl_idx ON centros_educativos.sg_calificacion_diplomado USING btree (cad_modulo_diplomado_fk);
CREATE INDEX sg_calificacion_diplomado_numero_periodo_idx ON centros_educativos.sg_calificacion_diplomado USING btree (cad_numero_periodo);
CREATE INDEX sg_calificacion_diplomado_tipo_cal_idx ON centros_educativos.sg_calificacion_diplomado USING btree (cad_tipo_calificacion);

CREATE INDEX sg_calificacion_diplomado_estudiante_estudiante_idx ON centros_educativos.sg_calificacion_diplomado_estudiante USING btree (cde_estudiante_fk);
CREATE INDEX sg_calificacion_diplomado_estudiante_calificacion_dip_idx ON centros_educativos.sg_calificacion_diplomado_estudiante USING btree (cde_calificacion_diplomado_fk);



  update centros_educativos.sg_diplomados_estudiante des set dep_sede_fk=(
  select sed.sed_pk from centros_educativos.sg_matriculas mat inner join centros_educativos.sg_secciones sec on mat.mat_seccion_fk=sec.sec_pk
  inner join centros_educativos.sg_servicio_educativo serv on serv.sdu_pk=sec.sec_servicio_educativo_fk
  inner join centros_educativos.sg_sedes sed on sed.sed_pk=serv.sdu_sede_fk 
  where mat.mat_estado='ABIERTO' and mat.mat_estudiante_fk=des.dep_estudiante_fk) where dep_sede_fk is null;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DIPLOMADO_SUBMENU','EM83',  'MENU: Muestra el submenú de diplomados', 1, true, null, null,0);
--Permiso para anular un título
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_ANULAR_TITULO','EP93',  'Permite anular un título', 1, true, null, null,0);	

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_fecha_cierre_seccion date;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_fecha_cierre_seccion date;


update seguridad.sg_operaciones set ope_nombre='BUSCAR_INSCRIPCION_DIPLOMADO' where ope_nombre='BUSCAR_INCRIPCION_DIPLOMADO' ;
update seguridad.sg_operaciones set ope_nombre='CREAR_INSCRIPCION_DIPLOMADO' where ope_nombre='CREAR_INCRIPCION_DIPLOMADO' ;
update seguridad.sg_operaciones set ope_nombre='ACTUALIZAR_INSCRIPCION_DIPLOMADO' where ope_nombre='ACTUALIZAR_INCRIPCION_DIPLOMADO' ;
update seguridad.sg_operaciones set ope_nombre='ELIMINAR_INSCRIPCION_DIPLOMADO' where ope_nombre='ELIMINAR_INCRIPCION_DIPLOMADO' ;


--owner
ALTER table centros_educativos.sg_calificacion_diplomado_estudiante owner to siges;
ALTER table centros_educativos.sg_calificacion_diplomado owner to siges;
ALTER table centros_educativos.sg_calificacion_diplomado_estudiante_aud owner to siges;
ALTER table centros_educativos.sg_calificacion_diplomado_aud owner to siges;
ALTER table centros_educativos.sg_calificacion_diplomado_cad_pk_seq owner to siges;
ALTER table centros_educativos.sg_calificacion_diplomado_estudiante_cde_pk_seq owner to siges;


--4.5.0

--Se corrige el nombre del menú
update seguridad.sg_operaciones set ope_descripcion = 'MENU: Organismos de administración escolar' where ope_codigo = 'EM82';

  update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=lower(cpe_nombre) where cpe_nombre_busqueda is null;
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'ñ','n');
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'á','a');
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'é','e');
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'í','i');
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'ó','o');
update centros_educativos.sg_componente_plan_estudio  set cpe_nombre_busqueda=replace(cpe_nombre_busqueda,'ú','u');


ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN acc_fecha_calificado date;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN acc_fecha_calificado date;

--4.6.1
--Se corrige el nombre del menú
update seguridad.sg_operaciones set ope_descripcion = 'MENU: Organismos de administración escolar' where ope_codigo = 'EM82';

-- Relación sede diplomado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_sede_diplomado_rsd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_diplomado (rsd_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_sede_diplomado_rsd_pk_seq'::regclass),rsd_habilitado boolean,rsd_sede_fk bigint,rsd_diplomado_fk bigint,rsd_numero_tramite CHARACTER VARYING(20),  rsd_ult_mod_fecha timestamp without time zone, rsd_ult_mod_usuario character varying(45), rsd_version integer, 
CONSTRAINT sg_rel_sede_diplomado_pkey PRIMARY KEY (rsd_pk),
CONSTRAINT sg_rsd_sede_fk FOREIGN KEY (rsd_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rsd_diplomado_fk FOREIGN KEY (rsd_diplomado_fk) REFERENCES centros_educativos.sg_diplomados(dip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_rel_sede_diplomado IS 'Tabla para el registro de relación sede diplomado.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_sede_fk IS 'Relación con sede.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_diplomado_fk IS 'Relación de diplomado.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_diplomado.rsd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_diplomado_aud (rsd_pk bigint NOT NULL,rsd_habilitado boolean, rsd_sede_fk bigint,rsd_diplomado_fk bigint,rsd_numero_tramite CHARACTER VARYING(20), rsd_ult_mod_fecha timestamp without time zone, rsd_ult_mod_usuario character varying(45), rsd_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_sede_diplomado_aud ADD PRIMARY KEY (rsd_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_SEDE_DIPLOMADO','E430',  'Crear relacion sede y diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_SEDE_DIPLOMADO','E431',  'Actualizar relacion sede y diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_SEDE_DIPLOMADO','E432',  'Eliminar relacion sede y diplomado', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_SEDE_DIPLOMADO','EB95',  'Buscar relación sede diplomado', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_rel_sede_diplomado ADD CONSTRAINT sg_rel_sede_diplomado_sede_diplomado_uk UNIQUE (rsd_sede_fk, rsd_diplomado_fk);


insert into centros_educativos.sg_rel_sede_diplomado (rsd_pk,rsd_habilitado,rsd_sede_fk,rsd_diplomado_fk,rsd_version)
select (nextval('centros_educativos.sg_rel_sede_diplomado_rsd_pk_seq'::regclass)), true, sed.sed_pk, dip.dip_pk,0 from centros_educativos.sg_sedes sed, centros_educativos.sg_diplomados dip
where  sed.sed_codigo in (
    '10698',
    '10608',
    '11280',
    '11104',
    '11495',
    '70042',
    '80086',
    '13372',
    '13434',
    '13475'
)ON CONFLICT DO NOTHING;


--4.7.0

update seguridad.sg_operaciones set ope_nombre='BUSCAR_DIPLOMADO' where ope_codigo='EB22' ;
update seguridad.sg_operaciones set ope_nombre='CREAR_DIPLOMADO' where ope_codigo='E67' ;
update seguridad.sg_operaciones set ope_nombre='ACTUALIZAR_DIPLOMADO' where ope_codigo='E68' ;
update seguridad.sg_operaciones set ope_nombre='ELIMINAR_DIPLOMADO' where ope_codigo='E69' ; 
update seguridad.sg_operaciones set ope_nombre='MENU_DIPLOMADO' where ope_codigo='EM4' ; 


-- Diploma
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_diplomas_dil_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomas (dil_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_diplomas_dil_pk_seq'::regclass),
dil_anio_lectivo_fk bigint, dil_diplomado_fk bigint, dil_sede_fk bigint,
 dil_ult_mod_fecha timestamp without time zone, dil_ult_mod_usuario character varying(45), dil_version integer, 
CONSTRAINT sg_diplomas_pkey PRIMARY KEY (dil_pk),
CONSTRAINT sg_dil_anio_lectivo_fk FOREIGN KEY (dil_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_dil_diplomado_fk FOREIGN KEY (dil_diplomado_fk) REFERENCES centros_educativos.sg_diplomados(dip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_dil_sede_fk FOREIGN KEY (dil_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_diplomas IS 'Tabla para el registro de diploma.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_anio_lectivo_fk IS 'Anio lectivo.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_diplomado_fk IS 'Diplomado.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_sede_fk IS 'Sede.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomas_aud (dil_pk bigint NOT NULL, dil_anio_lectivo_fk bigint, dil_diplomado_fk bigint, dil_sede_fk bigint, dil_ult_mod_fecha timestamp without time zone, dil_ult_mod_usuario character varying(45), dil_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_diplomas_aud ADD PRIMARY KEY (dil_pk, rev);
-- Diploma Estudiante
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_diplomas_estudiante_die_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomas_estudiante (die_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_diplomas_estudiante_die_pk_seq'::regclass), 
die_confirmado boolean, die_estudiante_fk bigint, die_diploma_fk bigint,
 die_ult_mod_fecha timestamp without time zone, die_ult_mod_usuario character varying(45), die_version integer, CONSTRAINT sg_diplomas_estudiante_pkey PRIMARY KEY (die_pk), 
CONSTRAINT sg_die_estudiante_fk FOREIGN KEY (die_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_die_diploma_fk FOREIGN KEY (die_diploma_fk) REFERENCES centros_educativos.sg_diplomas(dil_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_diplomas_estudiante IS 'Tabla para el registro de diploma estudiante.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_confirmado IS 'Confirmado.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_estudiante_fk IS 'Estudiante.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_diploma_fk IS 'Diploma.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_diplomas_estudiante.die_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomas_estudiante_aud (die_pk bigint NOT NULL,
die_confirmado boolean, die_estudiante_fk bigint, die_diploma_fk bigint,
die_ult_mod_fecha timestamp without time zone, die_ult_mod_usuario character varying(45), die_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_diplomas_estudiante_aud ADD PRIMARY KEY (die_pk, rev);

ALTER TABLE centros_educativos.sg_diplomas ADD CONSTRAINT sg_diplomadas_sede_diplomado_anio_uk UNIQUE (dil_anio_lectivo_fk, dil_diplomado_fk, dil_sede_fk);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIPLOMAS','E433',  'Crear diplomas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIPLOMAS','E434',  'Actualizar diplomas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIPLOMAS','E435',  'Eliminar diplomas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIPLOMAS_ESTUDIANTE','E436',  'Crear diplomas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIPLOMAS_ESTUDIANTE','E437',  'Actualizar diplomas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIPLOMAS_ESTUDIANTE','E438',  'Eliminar diplomas estudiante', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIPLOMAS','EB96',  'Buscar diplomas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIPLOMAS_ESTUDIANTE','EB97',  'Buscar diplomas estudiante', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DIPLOMAS','EM84',  'MENU: Diplomas', 1, true, null, null,0);


update seguridad.sg_operaciones set ope_nombre='CREAR_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='CREAR_HABILITACION_PERIODO_CALIFICACION' ;
update seguridad.sg_operaciones set ope_nombre='ACTUALIZAR_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='ACTUALIZAR_HABILITACION_PERIODO_CALIFICACION' ;
update seguridad.sg_operaciones set ope_nombre='ELIMINAR_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='ELIMINAR_HABILITACION_PERIODO_CALIFICACION' ;
update seguridad.sg_operaciones set ope_nombre='BUSCAR_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='BUSCAR_HABILITACION_PERIODO_CALIFICACION' ; 
update seguridad.sg_operaciones set ope_nombre='MENU_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='MENU_SOLICITUDES_HABILITACION_PERIODO_CALIFICACION' ; 
update seguridad.sg_operaciones set ope_nombre='EVALUAR_SOLICITUD_CORRECCION_CALIFICACION' where ope_nombre='EVALUAR_HABILITACION_PERIODO_CALIFICACION' ; 



ALTER TABLE centros_educativos.sg_diplomas ADD COLUMN dil_sello_firma_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_sello_firma_fk IS 'Llave foránea que hace referencia a sello firma.';
ALTER TABLE centros_educativos.sg_diplomas ADD CONSTRAINT sg_dil_sello_firma_fk FOREIGN KEY (dil_sello_firma_fk) REFERENCES centros_educativos.sg_sellos_firmas(sfi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_diplomas_aud ADD COLUMN dil_sello_firma_fk bigint;

ALTER TABLE centros_educativos.sg_diplomas ADD COLUMN dil_sello_firma_titular_ministro_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_diplomas.dil_sello_firma_titular_ministro_fk IS 'Llave foránea que hace referencia a sello firma titular ministro.';
ALTER TABLE centros_educativos.sg_diplomas ADD CONSTRAINT sg_dil_sello_firma_titular_ministro_fk FOREIGN KEY (dil_sello_firma_titular_ministro_fk) REFERENCES centros_educativos.sg_sello_firma_titular(sft_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_diplomas_aud ADD COLUMN dil_sello_firma_titular_ministro_fk bigint;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_DIPLOMA','R30',  '', 5, true, null, null,0);

--4.7.6
insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('IMPORTACION_CALIFICACION_MSJ', 'Mensaje de advertencia de importación de calificaciones','Mensaje de advertencia de importación de calificaciones','Mensaje advertencia');    

--Columna que indica si la persona tiene discapacidades
alter table centros_educativos.sg_personas add per_tiene_discapacidad bool;
alter table centros_educativos.sg_personas_aud add per_tiene_discapacidad bool;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tiene_discapacidad IS 'Bandera que indica si la persona tiene discapacidades.';
--Columna que indica si la persona tiene diagnóstico
alter table centros_educativos.sg_personas add per_tiene_diagnostico bool;
alter table centros_educativos.sg_personas_aud add per_tiene_diagnostico bool;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tiene_diagnostico IS 'Bandera que indica si la persona tiene diagnóstico.';

--Tabla de relación de persona con terapia
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_terapias (per_pk bigint NOT NULL,ter_pk bigint NOT NULL, CONSTRAINT sg_personas_terapias_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_terapias_terapia_fk FOREIGN KEY (ter_pk) REFERENCES catalogo.sg_terapias (ter_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_terapias_aud (per_pk bigint NOT NULL,ter_pk bigint NOT NULL, rev bigint, revtype smallint);

--Tabla de relación de persona con referencias de apoyo
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_referencias_apoyo (per_pk bigint NOT NULL,rea_pk bigint NOT NULL, CONSTRAINT sg_personas_referencias_apoyo_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_referencias_apoyo_referencia_fk FOREIGN KEY (rea_pk) REFERENCES catalogo.sg_referencias_apoyo (rea_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_referencias_apoyo_aud (per_pk bigint NOT NULL,rea_pk bigint NOT NULL, rev bigint, revtype smallint);

--Se actualiza el campo de discapacidad para quienes posean alguna registrada
update centros_educativos.sg_personas set per_tiene_discapacidad = true
where per_pk in ( select distinct pd.per_pk from centros_educativos.sg_personas_discapacidades pd);


--4.7.8
--Se corrige el nombre del menú
update seguridad.sg_operaciones set ope_descripcion = 'MENU: Organismos de administración escolar' where ope_codigo = 'EM82';


ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_es_anterior_2008 boolean;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_es_anterior_2008 boolean;


ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_nombre_titulo_posterior_2008 character varying(100);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_nombre_titulo_posterior_2008 character varying(100);

update centros_educativos.sg_reposicion_titulo set ret_es_anterior_2008=false;


ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_es_anterior_2008 boolean;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_es_anterior_2008 boolean;


ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_nombre_titulo_posterior_2008 character varying(100);
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_nombre_titulo_posterior_2008 character varying(100);


--4.7.11
--Tabla de relación de persona con terapia
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_terapias (per_pk bigint NOT NULL,ter_pk bigint NOT NULL, CONSTRAINT sg_personas_terapias_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_terapias_terapia_fk FOREIGN KEY (ter_pk) REFERENCES catalogo.sg_terapias (ter_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_terapias_aud (per_pk bigint NOT NULL,ter_pk bigint NOT NULL, rev bigint, revtype smallint);

--Tabla de relación de persona con referencias de apoyo
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_referencias_apoyo (per_pk bigint NOT NULL,rea_pk bigint NOT NULL, CONSTRAINT sg_personas_referencias_apoyo_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_referencias_apoyo_referencia_fk FOREIGN KEY (rea_pk) REFERENCES catalogo.sg_referencias_apoyo (rea_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_referencias_apoyo_aud (per_pk bigint NOT NULL,rea_pk bigint NOT NULL, rev bigint, revtype smallint);

--Se actualiza el campo de discapacidad para quienes posean alguna registrada
update centros_educativos.sg_personas set per_tiene_discapacidad = true
where per_pk in ( select distinct pd.per_pk from centros_educativos.sg_personas_discapacidades pd);

--Permiso para marcar la plaza como desierta
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MARCAR_PLAZA_DESIERTA','EP94',  'Permite marcar una plaza como desierta', 1, true, null, null,0);

alter table centros_educativos.sg_plazas add pla_desierta bool;
alter table centros_educativos.sg_plazas_aud add pla_desierta bool;
COMMENT ON COLUMN centros_educativos.sg_plazas_aud.pla_desierta IS 'Indica si la plaza fue marcada como desierta.';

--Tabla para almacenar los motivos de selección para cada docente
create table centros_educativos.sg_motivo_aplicacion_plaza (
	apl_pk bigint not null,
	msp_pk bigint not null,
	constraint motivo_aplicacion_plaza_fk foreign key (apl_pk) references centros_educativos.sg_aplicaciones_plaza (apl_pk),
	constraint motivo_aplicacion_motivo_fk foreign key (msp_pk) references catalogo.sg_motivos_seleccion_plaza (msp_pk)
);

create table centros_educativos.sg_motivo_aplicacion_plaza_aud (
	apl_pk bigint not null,
	msp_pk bigint not null
);



--Mensaje de configuración para matriz comparativa
insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('MENSAJE_MATRIZ_COMPARATIVA', 'Mensaje que se agrega en pantalla de matriz comparativa','mensaje que se agrega en pantalla de matriz comparativa','Los datos de la matriz corresponden a los docentes al momento de aplicar a la plaza, pudiendo tener diferencias con los datos de la ficha del docente');

--4.10.0
insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('MENSAJE_EVOLUCION_ESTUDIANTE', 'Mensaje que se agrega en tab evolución de pantalla estudiante','mensaje que se agrega en tab evolucion de pantalla estudiante','[EDIT]', 0, CURRENT_TIMESTAMP, 'casuser');

--Permiso para seleccionar la calidad del docente que aplica a la plaza
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PERMITE_SELECCION_CALIDAD_DOCENTE_PLAZA','EP95',  'Permite seleccionar la calidad del docente que aplica a la plaza', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_EVOLUCION_ESTUDIANTE','EP96',  'Permite ver tab de evolución de estudiante', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_aplica_grafica_evolucion boolean default false;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_aplica_grafica_evolucion boolean;


update centros_educativos.sg_componente_plan_grado set cpg_aplica_grafica_evolucion=true where cpg_objeto_promocion=true;

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version)
values('IMPORTACION_CALIFICACION_SELECCION_METODO', 'Seleccionar método de importacion de calificación 0-único componente 1-mas de un componente','Seleccionar método de importacion de calificación 0-único componente 1-mas de un componente','0',0);    


ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN acc_metodo_importacion character varying(45);
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN acc_metodo_importacion character varying(45);

INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('IMPORTACION_CALIFICACIONES_MAS_DE_UN_COMP', 'Plantilla de importación de calificaciones más de un componente', 'Plantilla de importación de calificaciones más de un componente', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('IMPORTACION_CALIFICACIONES_UNICO_COMP', 'Plantilla de importación de calificaciones único componente', 'Plantilla de importación de calificaciones único componente', true, CURRENT_TIMESTAMP, 'admin', '0');

-- 4.11.0

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('MENSAJE_EVOLUCION_ESTUDIANTE', 'Mensaje que se agrega en tab evolución de pantalla estudiante','mensaje que se agrega en tab evolucion de pantalla estudiante','[EDIT]', 0, CURRENT_TIMESTAMP, 'casuser');

--Permiso para seleccionar la calidad del docente que aplica a la plaza
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PERMITE_SELECCION_CALIDAD_DOCENTE_PLAZA','EP95',  'Permite seleccionar la calidad del docente que aplica a la plaza', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_EVOLUCION_ESTUDIANTE','EP96',  'Permite ver tab de evolución de estudiante', 1, true, null, null,0);

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('CONSUMO_SERVICIO_DUI_RNPN_HABILITADO', 'Consumo de servicio de DUI al RNPN habilitado','consumo de servicio de dui al rnpn habilitado',
'false', 0, CURRENT_TIMESTAMP, 'casuser');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('CONSUMO_SERVICIO_CUN_RNPN_HABILITADO', 'Consumo de servicio de CUN al RNPN habilitado','consumo de servicio de cun al rnpn habilitado',
'false', 0, CURRENT_TIMESTAMP, 'casuser');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('CONSUMO_SERVICIO_PARTIDA_NAC_RNPN_HABILITADO', 'Consumo de servicio de partida de nacimiento al RNPN habilitado','consumo de servicio de partida de nacimiento al rnpn habilitado',
'false', 0, CURRENT_TIMESTAMP, 'casuser');

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_dui_validado_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_dui_validado_rnpn boolean;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_dui_pendiente_validacion_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_dui_pendiente_validacion_rnpn boolean;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_cun_validado_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_cun_validado_rnpn boolean;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_cun_pendiente_validacion_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_cun_pendiente_validacion_rnpn boolean;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nac_validada_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nac_validada_rnpn boolean;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nac_pendiente_validacion_rnpn boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nac_pendiente_validacion_rnpn boolean;

CREATE INDEX sg_personas_pendientes_validacion_dui_rnpn_idx ON centros_educativos.sg_personas USING btree (per_dui_pendiente_validacion_rnpn) where per_dui_pendiente_validacion_rnpn = true;
CREATE INDEX sg_personas_pendientes_validacion_cun_rnpn_idx ON centros_educativos.sg_personas USING btree (per_cun_pendiente_validacion_rnpn) where per_cun_pendiente_validacion_rnpn = true;
CREATE INDEX sg_personas_pendientes_validacion_part_nac_rnpn_idx ON centros_educativos.sg_personas USING btree (per_partida_nac_pendiente_validacion_rnpn) where per_partida_nac_pendiente_validacion_rnpn = true;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_DUI_PERSONA_RNPN','E439',  'Permite validar DUI de persona en RNPN', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_CUN_PERSONA_RNPN','E440',  'Permite validar CUN de persona en RNPN', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_PARTIDA_NAC_PERSONA_RNPN','E441',  'Permite validar partida de nacimiento de persona en RNPN', 1, true, null, null,0);


CREATE SEQUENCE IF NOT EXISTS auditoria.sg_registros_auditoria_consumo_rnpn_aud_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS auditoria.sg_registros_auditoria_consumo_rnpn
 (aud_pk bigint NOT NULL DEFAULT nextval('auditoria.sg_registros_auditoria_consumo_rnpn_aud_pk_seq'::regclass),
 aud_persona_documento character varying(45),
 aud_operacion character varying(45),
 aud_cuerpo_enviado text,
 aud_cuerpo_recibido text,
 aud_resultado_operacion character varying(100),
 aud_fecha timestamp without time zone,
 aud_mensaje character varying(1000),
 aud_codigo_usuario character varying(45),
 aud_hash_md5 character varying(300),
 CONSTRAINT sg_registros_auditoria_consumo_rnpn_pkey PRIMARY KEY (aud_pk));


INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('PLANTILLA_IMPORTACION_ASISTENCIAS', 'Plantilla de importación de asistencias', 'Plantilla de importación de asistencias', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('DIPLOMAS', 'Plantilla de reporte de diplomas', 'Plantilla de reporte de diplomas', true, CURRENT_TIMESTAMP, 'admin', '0');


--4.12.0

update catalogo.sg_configuraciones set  con_codigo='IMPORTACION_CALIFICACION_VARIOS_COMPONENTES' where con_codigo='IMPORTACION_CALIFICACION_SELECCION_METODO';
ALTER TABLE auditoria.sg_registros_auditoria_consumo_rnpn ADD COLUMN aud_endpoint text;

ALTER TABLE auditoria.sg_registros_auditoria_consumo_rnpn ALTER COLUMN aud_persona_documento TYPE text;

--4.12.4
 -- Relación OAE Identificación
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_oae_identificacion_oae_rio_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_oae_identificacion_oae (rio_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_oae_identificacion_oae_rio_pk_seq'::regclass), 
rio_oae_fk bigint, rio_identificacion_oae_fk bigint, rio_valor character varying(60)
, rio_ult_mod_fecha timestamp without time zone, rio_ult_mod_usuario character varying(45), rio_version integer, CONSTRAINT sg_rel_oae_identificacion_oae_pkey PRIMARY KEY (rio_pk),
CONSTRAINT sg_rio_oae_fk FOREIGN KEY (rio_oae_fk) REFERENCES centros_educativos.sg_organismo_administracion_escolar(oae_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rio_identificacion_oae_fk FOREIGN KEY (rio_identificacion_oae_fk) REFERENCES catalogo.sg_identificacion_oae(ioa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_rel_oae_identificacion_oae IS 'Tabla para el registro de relación oae identificación.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_oae_fk IS 'foreing key a oea.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_identificacion_oae_fk IS 'foreing key a identificacion oae.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_valor IS 'Valor del registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_oae_identificacion_oae.rio_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_oae_identificacion_oae_aud (rio_pk bigint NOT NULL, rio_oae_fk bigint, rio_identificacion_oae_fk bigint, rio_valor character varying(60), rio_ult_mod_fecha timestamp without time zone, rio_ult_mod_usuario character varying(45), rio_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_oae_identificacion_oae_aud ADD PRIMARY KEY (rio_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_OAE_IDENTIFICACION_OAE','E442',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_OAE_IDENTIFICACION_OAE','E443',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_OAE_IDENTIFICACION_OAE','E444',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_rel_oae_identificacion_oae ADD CONSTRAINT sg_oae_identificacion_uk UNIQUE (rio_oae_fk,rio_identificacion_oae_fk);



ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_dui_expedido_en character varying(50);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_dui_expedido_en character varying(50);

ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_fecha_expedicion_dui date;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_fecha_expedicion_dui date;

-- 4.13.0

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('RANGO_COLORES_GAUGE_PREDICTIBILIDAD_DESERCION', 'Rango de colores para gauge de predictibilidad de deserción','rango de colores para gauge de predictibilidad de desercion','70,80,100', 0, CURRENT_TIMESTAMP, 'casuser');


CREATE OR REPLACE FUNCTION alertas.procesar_alertas_asistencia(alerta_cnf_id INTEGER) RETURNS void AS $$
DECLARE
alertas_asistencia alertas.sg_config_alerta_asistencia%ROWTYPE;
    BEGIN
        RAISE NOTICE 'Procesando alerta ASISTENCIA';
		FOR alertas_asistencia IN(Select * from alertas.sg_config_alerta_asistencia where cnf_cabezal = alerta_cnf_id
		) loop
			RAISE NOTICE '----------- ASI PK (%)----',alertas_asistencia.cnf_pk;
			RAISE NOTICE '----------- CICLO PK (%)----',alertas_asistencia.cnf_ciclo_fk;
			RAISE NOTICE '----------- MUY ALTO (%)----',alertas_asistencia.cnf_riesgo_muy_alto;
			RAISE NOTICE '----------- ALTO (%)----',alertas_asistencia.cnf_riesgo_alto;
			RAISE NOTICE '----------- MEDIO (%)----',alertas_asistencia.cnf_riesgo_medio;
		    IF (alertas_asistencia.cnf_riesgo_muy_alto IS NOT NULL 
				OR alertas_asistencia.cnf_riesgo_alto IS NOT NULL 
				OR alertas_asistencia.cnf_riesgo_medio IS NOT NULL) then
				
			INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
			select 'ASISTENCIA',
			CASE WHEN count(*) >= alertas_asistencia.cnf_riesgo_muy_alto THEN 'MUY_ALTO' 
    			WHEN count(*) >= alertas_asistencia.cnf_riesgo_alto THEN 'ALTO' 
				WHEN count(*) >= alertas_asistencia.cnf_riesgo_medio THEN 'MEDIO' 
			END, asi_estudiante, now()
			from centros_educativos.sg_asistencias a
                        INNER JOIN centros_educativos.sg_control_asistencia_cabezal cac ON (a.asi_control_fk = cac.cac_pk)
			INNER JOIN centros_educativos.sg_estudiantes e ON (e.est_pk = a.asi_estudiante)		
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk and cac.cac_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)	
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
			where asi_inasistencia and ocu.ocu_aplica_alertas_tempranas and cic.cic_pk = alertas_asistencia.cnf_ciclo_fk and m.mat_estado = 'ABIERTO'
			group by asi_estudiante
			having count(*) >= LEAST(alertas_asistencia.cnf_riesgo_muy_alto, alertas_asistencia.cnf_riesgo_alto, alertas_asistencia.cnf_riesgo_medio);	
			end if;	
		END LOOP;
    END;
$$ LANGUAGE plpgsql;


-- 4.14.0

-- Medidas antropométricas y examen físico
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_medidas_examen_fisico_mef_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_medidas_examen_fisico (mef_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_medidas_examen_fisico_mef_pk_seq'::regclass),
mef_ficha_salud_fk bigint, mef_edad integer, mef_peso numeric(5,2), mef_talla numeric(5,2), mef_imc numeric(5,2), mef_cbd numeric(5,2), mef_ult_mod_fecha timestamp without time zone, mef_ult_mod_usuario character varying(45), mef_version integer, 
CONSTRAINT sg_medidas_examen_fisico_pkey PRIMARY KEY (mef_pk),
CONSTRAINT sg_mef_ficha_salud_fk FOREIGN KEY (mef_ficha_salud_fk) REFERENCES centros_educativos.sg_fichas_salud(fsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_medidas_examen_fisico IS 'Tabla para el registro de medidas antropométricas y examen físico.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_edad IS 'Edad de estudiante.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_peso IS 'Peso de estudiante.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_talla IS 'Talla de estudiante.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_imc IS 'Indice de masa corporal';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_medidas_examen_fisico.mef_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_medidas_examen_fisico_aud (mef_pk bigint NOT NULL,mef_ficha_salud_fk bigint, mef_edad integer, mef_peso numeric(5,2), mef_talla numeric(5,2), mef_imc numeric(5,2), mef_cbd numeric(5,2), mef_ult_mod_fecha timestamp without time zone, mef_ult_mod_usuario character varying(45), mef_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_medidas_examen_fisico_aud ADD PRIMARY KEY (mef_pk, rev);


-- Habitos de alimentación
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_habitos_alimentacion_hal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habitos_alimentacion (hal_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_habitos_alimentacion_hal_pk_seq'::regclass),
hal_ficha_salud_fk bigint,hal_anio_lectivo_fk bigint, hal_consume_frutas_verduras boolean, hal_frecuencia_consumo_frutas integer, hal_consume_agua boolean, hal_cantidad_vasos integer,
hal_ult_mod_fecha timestamp without time zone, hal_ult_mod_usuario character varying(45), hal_version integer, 
CONSTRAINT sg_habitos_alimentacion_pkey PRIMARY KEY (hal_pk),
CONSTRAINT sg_hal_ficha_salud_fk FOREIGN KEY (hal_ficha_salud_fk) REFERENCES centros_educativos.sg_fichas_salud(fsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_hal_anio_lectivo_fk FOREIGN KEY (hal_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_habitos_alimentacion IS 'Tabla para el registro de habitos de alimentación.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_ficha_salud_fk IS 'Ficha estudiante.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_anio_lectivo_fk IS 'Anio estudiante.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habitos_alimentacion.hal_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habitos_alimentacion_aud (hal_pk bigint NOT NULL,hal_anio_lectivo_fk bigint, hal_ficha_salud_fk bigint, hal_consume_frutas_verduras boolean, hal_frecuencia_consumo_frutas integer, hal_consume_agua boolean, hal_cantidad_vasos integer,
 hal_ult_mod_fecha timestamp without time zone, hal_ult_mod_usuario character varying(45), hal_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_habitos_alimentacion_aud ADD PRIMARY KEY (hal_pk, rev);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_habito_alimentacion_tiempo_comida_dia (
	hal_pk bigint not null,
	tcd_pk bigint not null,
	constraint motivo_aplicacion_plaza_fk foreign key (hal_pk) references centros_educativos.sg_habitos_alimentacion (hal_pk),
	constraint tiempos_comida_dia_fk  foreign key (tcd_pk) references catalogo.sg_tiempos_comida_dia (tcd_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habito_alimentacion_tiempo_comida_dia_aud(hal_pk bigint NOT NULL,tcd_pk bigint NOT NULL, rev bigint, revtype smallint);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_ficha_salud_enfer_no_transm (
	fsa_pk bigint not null,
	ent_pk bigint not null,
	constraint ficha_salud_fk foreign key (fsa_pk) references centros_educativos.sg_fichas_salud(fsa_pk),
	constraint enfermedad_no_trans_fk  foreign key (ent_pk) references catalogo.sg_enfer_no_transm (ent_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_ficha_salud_enfer_no_transm_aud(fsa_pk bigint NOT NULL,ent_pk bigint NOT NULL, rev bigint, revtype smallint);



-- Habitos de personales
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_habitos_personales_hap_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habitos_personales (hap_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_habitos_personales_hap_pk_seq'::regclass),
hap_ficha_salud_fk bigint, hap_anio_fk bigint, hap_tiempo_frente_pantalla numeric(3,1), hap_tiempo_compartido_con_padres numeric(3,1), hap_tiempo_tareas_escuela numeric(3,1), 
hap_tiempo_recreacion numeric(3,1),hap_tiempo_actividad_fisica numeric(3,1), hap_tipo_actividad_fisica character varying(250), hap_tiempo_dormir numeric(3,1), hap_observaciones character varying(250),
hap_ult_mod_fecha timestamp without time zone, hap_ult_mod_usuario character varying(45), hap_version integer,
 CONSTRAINT sg_habitos_personales_pkey PRIMARY KEY (hap_pk),
CONSTRAINT sg_hap_ficha_salud_fk FOREIGN KEY (hap_ficha_salud_fk) REFERENCES centros_educativos.sg_fichas_salud(fsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_hap_anio_fk FOREIGN KEY (hap_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_habitos_personales IS 'Tabla para el registro de habitos de personales.';
COMMENT ON COLUMN centros_educativos.sg_habitos_personales.hap_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_habitos_personales.hap_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habitos_personales.hap_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habitos_personales.hap_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habitos_personales_aud (hap_pk bigint NOT NULL, 
hap_ficha_salud_fk bigint, hap_anio_fk bigint, hap_tiempo_frente_pantalla numeric(3,1), hap_tiempo_compartido_con_padres numeric(3,1), hap_tiempo_tareas_escuela numeric(3,1), 
hap_tiempo_recreacion numeric(3,1), hap_tiempo_actividad_fisica numeric(3,1),hap_tipo_actividad_fisica character varying(250), hap_tiempo_dormir numeric(3,1), hap_observaciones character varying(250),
 hap_ult_mod_fecha timestamp without time zone, hap_ult_mod_usuario character varying(45), hap_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_habitos_personales_aud ADD PRIMARY KEY (hap_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MEDIDAS_EXAMEN_FISICO','E445',  'Crear medidas antraopologicas y examen fisico', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MEDIDAS_EXAMEN_FISICO','E446',  'Actualizar medidas antraopologicas y examen fisico', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MEDIDAS_EXAMEN_FISICO','E447',  'Eliminar medidas antraopologicas y examen fisico', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_HABITOS_PERSONALES','E448',  'Crear habitos personales', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_HABITOS_PERSONALES','E449',  'Actualizar habitos personales', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_HABITOS_PERSONALES','E450',  'Eliminar habitos personales', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_HABITOS_ALIMENTACION','E451',  'Crear habitos alimentacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_HABITOS_ALIMENTACION','E452',  'Actualizar habitos alimentacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_HABITOS_ALIMENTACION','E453',  'Eliminar habitos alimentacion', 1, true, null, null,0);



--4.16.1

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor, con_es_editor)
VALUES( 'FICHA_SALUD_MSJ_CONF', 'Mensaje en ficha de salud del estudiante', 'mensaje en ficha de salud del estudiante', '2020-05-08 18:05:05.081', 'casuser', 1, ' Mensaje configuracion FICHA_SALUD_MSJ_CONF', false);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SIN_IMPRIMIR_TITULO','EM85',  'MENU:Titulos sin imprimir', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONFIRMACION_PROMOCION','EB98',  'Buscar confirmaciones de promoción', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CONFIRMAR_PROMOCION','E454',  'Confirmar promoción', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONFIRMACION_PROMOCION','E455',  'Eliminar confirmación de promoción', 1, true, null, null,0);


INSERT INTO catalogo.sg_configuraciones_firma_electronica(con_codigo, con_activada, con_nombre, con_version) values ('CONF_PROMOCION', false, 'Confirmación de promoción', 0);

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_confirmaciones_promociones_cpr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_promociones (
cpr_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_confirmaciones_promociones_cpr_pk_seq'::regclass),
 cpr_fecha_confirmacion timestamp without time zone,
 cpr_usuario_confirmacion character varying(45),
 cpr_seccion_fk bigint,
 cpr_cabezal_fk bigint,
 cpr_ult_mod_fecha timestamp without time zone,
 cpr_ult_mod_usuario character varying(45),
 cpr_version integer,
 cpr_archivo_firmado_fk bigint,
 cpr_firmada boolean,
 cpr_firma_transaction_id character varying(50),
 CONSTRAINT sg_confirmaciones_promociones_pkey PRIMARY KEY (cpr_pk));


ALTER TABLE centros_educativos.sg_confirmaciones_promociones ADD CONSTRAINT sg_confmat_seccion_uk UNIQUE (cpr_seccion_fk);
ALTER TABLE centros_educativos.sg_confirmaciones_promociones ADD CONSTRAINT sg_confmat_cabezal_uk UNIQUE (cpr_cabezal_fk);
ALTER TABLE centros_educativos.sg_confirmaciones_promociones ADD CONSTRAINT sg_confirmaciones_prom_seccion_fkey FOREIGN KEY (cpr_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_confirmaciones_promociones ADD CONSTRAINT sg_confirmaciones_prom_cabezal_fkey FOREIGN KEY (cpr_cabezal_fk) REFERENCES centros_educativos.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_confirmaciones_promociones ADD CONSTRAINT sg_conf_prom_archivo_firmado_fk FOREIGN KEY (cpr_archivo_firmado_fk) REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


COMMENT ON TABLE centros_educativos.sg_confirmaciones_promociones IS 'Tabla para el registro de confirmaciones de matrículas.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_fecha_confirmacion IS 'Fecha de confirmación.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_usuario_confirmacion IS 'Usuario que confirmó.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_seccion_fk IS 'Sección del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_cabezal_fk IS 'Cabezal GRA del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_version IS 'Versión del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_archivo_firmado_fk IS 'Llave foránea que hace referencia al archivo firmado.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_firmada IS 'Indica si la confirmación está firmada.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_promociones.cpr_firma_transaction_id IS 'Id de la transacción en el servicio de firma electrónica';


CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_promociones_aud (
cpr_pk bigint NOT NULL,
 cpr_fecha_confirmacion timestamp without time zone,
 cpr_usuario_confirmacion character varying(45),
 cpr_seccion_fk bigint,
 cpr_cabezal_fk bigint,
 cpr_ult_mod_fecha timestamp without time zone,
 cpr_ult_mod_usuario character varying(45),
 cpr_version integer,
 cpr_archivo_firmado_fk bigint,
 cpr_firmada boolean,
 cpr_firma_transaction_id character varying(50),
 rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_confirmaciones_promociones_aud ADD PRIMARY KEY (cpr_pk, rev);


INSERT INTO catalogo.sg_configuraciones_firma_electronica(con_codigo, con_activada, con_nombre, con_version) values ('CONF_CIERRE_ANIO', false, 'Confirmación de cierre de año', 0);
INSERT INTO catalogo.sg_configuraciones_firma_electronica(con_codigo, con_activada, con_nombre, con_version) values ('CONF_APROBACION', false, 'Confirmación de aprobación', 0);
INSERT INTO catalogo.sg_configuraciones_firma_electronica(con_codigo, con_activada, con_nombre, con_version) values ('CONF_TRASLADO', false, 'Confirmación de traslado', 0);




ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_fecha_cierre timestamp without time zone; 
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_fecha_cierre timestamp without time zone; 

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_usuario_cierre character varying(45); 
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_usuario_cierre character varying(45); 

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_archivo_firmado_fk bigint; 
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_archivo_firmado_fk bigint; 

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_firmado boolean; 
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_firmado boolean; 

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_firma_transaction_id character varying(50); 
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_firma_transaction_id character varying(50); 

update centros_educativos.sg_cierre_anio_lectivo_sede set cal_fecha_cierre = cal_ult_mod_fecha;
update centros_educativos.sg_cierre_anio_lectivo_sede set cal_usuario_cierre = cal_ult_mod_usuario;
update centros_educativos.sg_cierre_anio_lectivo_sede set cal_firmado = false;


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_confirmaciones_aprobaciones_cpr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_aprobaciones (
cpr_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_confirmaciones_aprobaciones_cpr_pk_seq'::regclass),
 cpr_fecha_confirmacion timestamp without time zone,
 cpr_usuario_confirmacion character varying(45),
 cpr_seccion_fk bigint,
 cpr_componente_plan_estudio_fk bigint,
 cpr_ult_mod_fecha timestamp without time zone,
 cpr_ult_mod_usuario character varying(45),
 cpr_version integer,
 cpr_archivo_firmado_fk bigint,
 cpr_firmada boolean,
 cpr_firma_transaction_id character varying(50),
 CONSTRAINT sg_confirmaciones_aprobaciones_pkey PRIMARY KEY (cpr_pk));


ALTER TABLE centros_educativos.sg_confirmaciones_aprobaciones ADD CONSTRAINT sg_confapr_seccion_componente_uk UNIQUE (cpr_seccion_fk, cpr_componente_plan_estudio_fk);
ALTER TABLE centros_educativos.sg_confirmaciones_aprobaciones ADD CONSTRAINT sg_confirmaciones_apr_seccion_fkey FOREIGN KEY (cpr_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_confirmaciones_aprobaciones ADD CONSTRAINT sg_conf_apr_archivo_firmado_fk FOREIGN KEY (cpr_archivo_firmado_fk) REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


COMMENT ON TABLE centros_educativos.sg_confirmaciones_aprobaciones IS 'Tabla para el registro de confirmaciones de matrículas.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_fecha_confirmacion IS 'Fecha de confirmación.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_usuario_confirmacion IS 'Usuario que confirmó.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_seccion_fk IS 'Sección del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_componente_plan_estudio_fk IS 'Componente plan estudio del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_version IS 'Versión del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_archivo_firmado_fk IS 'Llave foránea que hace referencia al archivo firmado.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_firmada IS 'Indica si la confirmación está firmada.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_aprobaciones.cpr_firma_transaction_id IS 'Id de la transacción en el servicio de firma electrónica';


CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_aprobaciones_aud (
cpr_pk bigint NOT NULL,
 cpr_fecha_confirmacion timestamp without time zone,
 cpr_usuario_confirmacion character varying(45),
 cpr_seccion_fk bigint,
 cpr_componente_plan_estudio_fk bigint,
 cpr_ult_mod_fecha timestamp without time zone,
 cpr_ult_mod_usuario character varying(45),
 cpr_version integer,
 cpr_archivo_firmado_fk bigint,
 cpr_firmada boolean,
 cpr_firma_transaction_id character varying(50),
 rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_confirmaciones_aprobaciones_aud ADD PRIMARY KEY (cpr_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CONFIRMAR_APROBACION','E456',  'Confirmar aprobación', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONFIRMACION_APROBACION','E457',  'Eliminar confirmación de aprobación', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONFIRMACION_APROBACION','EB99',  'Buscar confirmaciones de aprobación', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD COLUMN sot_fecha_confirmacion timestamp without time zone;
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD COLUMN sot_fecha_confirmacion timestamp without time zone;

ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD COLUMN sot_usuario_confirmacion character varying(45);
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD COLUMN sot_usuario_confirmacion character varying(45);

ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD COLUMN sot_archivo_firmado_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD COLUMN sot_archivo_firmado_fk bigint;

ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD COLUMN sot_firmada boolean;
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD COLUMN sot_firmada boolean;


update centros_educativos.sg_solicitudes_traslado set sot_fecha_confirmacion = sot_ult_mod_fecha where sot_estado = 'CONFIRMADO';
update centros_educativos.sg_solicitudes_traslado set sot_usuario_confirmacion = sot_ult_mod_usuario where sot_estado = 'CONFIRMADO';



CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_confirmaciones_solicitudes_traslado_sot_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_solicitudes_traslado (
sot_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_confirmaciones_solicitudes_traslado_sot_pk_seq'::regclass),
 sot_fecha_traslado timestamp without time zone,
 sot_solicitud_traslado_fk bigint,
 sot_seccion_fk bigint,
 sot_observacion character varying(500),
 sot_ult_mod_fecha timestamp without time zone,
 sot_ult_mod_usuario character varying(45),
 sot_version integer,
 sot_firma_transaction_id character varying(50),
 CONSTRAINT sg_confirmaciones_sol_traslado_pkey PRIMARY KEY (sot_pk));

CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_solicitudes_traslado_aud (
sot_pk bigint NOT NULL,
 sot_fecha_traslado timestamp without time zone,
 sot_solicitud_traslado_fk bigint,
 sot_seccion_fk bigint,
 sot_observacion character varying(500),
 sot_ult_mod_fecha timestamp without time zone,
 sot_ult_mod_usuario character varying(45),
 sot_version integer,
 sot_firma_transaction_id character varying(50),
 rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_confirmaciones_solicitudes_traslado_aud ADD PRIMARY KEY (sot_pk, rev);


ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD COLUMN sot_confirmacion_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD COLUMN sot_confirmacion_fk bigint;

ALTER TABLE centros_educativos.sg_confirmaciones_solicitudes_traslado ADD COLUMN sot_firmada boolean;
ALTER TABLE centros_educativos.sg_confirmaciones_solicitudes_traslado_aud ADD COLUMN sot_firmada boolean;

ALTER TABLE centros_educativos.sg_solicitudes_traslado ADD CONSTRAINT sg_confirmaciones_sol_traslado_fkey FOREIGN KEY (sot_confirmacion_fk) REFERENCES centros_educativos.sg_confirmaciones_solicitudes_traslado(sot_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_confirmaciones_solicitudes_traslado RENAME COLUMN sot_observacion to sot_resolucion;
ALTER TABLE centros_educativos.sg_confirmaciones_solicitudes_traslado_aud RENAME COLUMN sot_observacion to sot_resolucion;

UPDATE centros_educativos.sg_solicitudes_traslado  SET sot_firmada = false;

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('MENSAJE_EVOLUCION_ESTUDIANTE_ALERTAS', 'Mensaje que se agrega en tab evolución de pantalla estudiante, en sección de alertas tempranas','mensaje que se agrega en tab evolucion de pantalla estudiante, en seccion de alertas tempranas','[EDIT]', 0, CURRENT_TIMESTAMP, 'casuser');


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('MENSAJE_EVOLUCION_ESTUDIANTE_DESERCION', 'Mensaje que se agrega en tab evolución de pantalla estudiante, en sección de predictibilidad de deserción','mensaje que se agrega en tab evolucion de pantalla estudiante, en seccion de predictibilidad de desercion','[EDIT]', 0, CURRENT_TIMESTAMP, 'casuser');


ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_numero_tramite_revocatoria character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_numero_tramite_revocatoria character varying (255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_numero_tramite_cambio_nominacion character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_numero_tramite_cambio_nominacion character varying (255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_numero_tramite_cambio_domicilio character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_numero_tramite_cambio_domicilio character varying (255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_numero_tramite_creacion character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_numero_tramite_creacion character varying (255);

CREATE UNIQUE INDEX sg_sedes_numero_tramite_revocatoria_uk ON centros_educativos.sg_sedes (sed_numero_tramite_revocatoria);
CREATE UNIQUE INDEX sg_sedes_numero_tramite_nominacion_uk ON centros_educativos.sg_sedes (sed_numero_tramite_cambio_nominacion);
CREATE UNIQUE INDEX sg_sedes_numero_tramite_domicilio_uk ON centros_educativos.sg_sedes (sed_numero_tramite_cambio_domicilio);
CREATE UNIQUE INDEX sg_sedes_numero_tramite_creacion_uk ON centros_educativos.sg_sedes (sed_numero_tramite_creacion);


ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_numero_acta_cierre character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_numero_acta_cierre character varying (255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_motivo_cierre_fk integer;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_motivo_cierre_fk integer;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_observacion_cierre character varying (255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_observacion_cierre character varying (255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_fecha_cierre date;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_fecha_cierre date;
    
ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sedes_motivo_cierre_fkey FOREIGN KEY (sed_motivo_cierre_fk) REFERENCES catalogo.sg_motivos_cierre_sede(mcs_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE SCHEMA acreditacion;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('REVOCAR_SEDE','E461',  'Revocar sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_NOMINACION_SEDE','E462',  'Cambiar nominación de sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_DOMICILIO_SEDE','E463',  'Cambiar domicilio de sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PROCESAR_VALIDACIONES_RNPN_PENDIENTES','E464',  'Procesar validaciones RNPN pendientes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTIVAR_SEDE','E465',  'Activa una sede revocada', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE','E458',  'Crear calificaciones historicas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE','E459',  'Actualizar calificaciones historicas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE','E460',  'Eliminar calificaciones historicas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACIONES_HISTORICAS_ESTUDIANTE','EB100',  'Buscar calificaciones historicas estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ACREDITACION','EM86',  'MENU: Acreditacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_HISTORICO','EM87',  'MENU: Calificaciones dentro de acreditacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_ESTUDIANTE_HISTORICO','EM88',  'MENU: Calificacion estudiante dentro de acreditacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_CALIFICACIONES_HISTORICO','R34',  '', 4, true, null, null,0);

CREATE SEQUENCE IF NOT EXISTS acreditacion.sg_calificaciones_historicas_estudiante_che_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1; 

CREATE TABLE acreditacion.sg_calificaciones_historicas_estudiante (
    che_pk bigint NOT NULL DEFAULT nextval('acreditacion.sg_calificaciones_historicas_estudiante_che_pk_seq'::regclass),
    che_estudiante_fk int8 NULL,
    che_matricula_externa_id int8 NULL,
    che_anio_matricula smallint NULL,
    che_observacion varchar(1000) NULL,
    che_ult_mod_fecha timestamp NULL,
    che_ult_mod_fecha_aux varchar(10) NULL,
    che_ult_mod_usuario varchar(45) NULL,
    che_version int4 NULL,
    che_fecha_realizado date NULL,
    che_fecha_realizado_aux varchar(10) NULL,
    che_proceso_de_creacion varchar(20) NULL,
    che_sede_fk int8 NULL,
    che_sede_externa_codigo varchar (15) NULL,
    che_sede_externa_nombre varchar (500) NULL,
    che_sede_externa_nombre_busqueda varchar (500) NULL,
    che_servicio_educativo_externo_descripcion varchar (500) NULL,
    che_servicio_educativo_externo_id int8 NULL,
    che_servicio_educativo_externo_etiqueta_impresion varchar (500) NULL,
    che_plan_estudio_externo_id int8 NULL,
    che_plan_estudio_externo_descripcion varchar (500) NULL,
    che_componente varchar (250) NULL,
    che_componente_busqueda varchar (250) NULL,    
    che_PI varchar (45) NULL,
    che_NFI varchar (45) NULL,
    che_NPAESI varchar (45) NULL,
    che_RIX varchar (45) NULL,
    che_RIR varchar (45) NULL,
    che_RIRE varchar (45) NULL,
    che_NF varchar (45) NULL,
    che_NPAES varchar (45) NULL,
    che_PI_MODIF character(1) NULL,
    che_PI_R varchar (45) NULL,
    che_PIR     varchar (45) null,
    che_evaluacion_externa_id int8 NULL,
    
    CONSTRAINT sg_calificaciones_historicas_estudiante_pkey PRIMARY KEY (che_pk)
);
--CREATE INDEX sg_calificaciones_historicas_estudiante_fk_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_estudiante_fk);
--CREATE INDEX sg_calificaciones_historicas_sede_fk_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_sede_fk);

 

--ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD CONSTRAINT sg_calificaciones_historicas_estudiante_estudiante_fk FOREIGN KEY (che_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;
--ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD CONSTRAINT sg_calificaciones_historicas_estudiante_sede_fk FOREIGN KEY (che_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;

 

COMMENT ON TABLE acreditacion.sg_calificaciones_historicas_estudiante is 'Tabla para el registro de calificaciones migradas de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_pk is 'Número corrlelativo autogenerado.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_estudiante_fk is 'Llave foránea que hace referencia al estudiante.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_matricula_externa_id is 'Id de matrícula en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_anio_matricula is 'Año de matrícula en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_plan_estudio_externo_id is 'Id de plan de estudio en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_plan_estudio_externo_descripcion is 'Nombre de plan de estudio en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_observacion is 'Observación';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_ult_mod_fecha is 'Fecha de modificación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_ult_mod_usuario is 'Usuario de la última modificación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_version is 'Número de versión.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_fecha_realizado is 'Fecha de creación del registro.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_PI is 'Valor PI en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_NFI is 'Valor NFI en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_NPAESI is 'Valor NPAESI en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_RIX is 'Valor RIX en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_RIR is 'Valor RIR en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_RIRE is 'Valor RIRE en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_NF is 'Valor NF en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_NPAES is 'Valor NPAES en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_PI_MODIF is 'Valor PI_MODIF en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_PI_R is 'Valor PI_R en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_PIR    is 'Valor PIIR en tabla evaluación de acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_sede_fk is 'Llave foránea que hace referencia a la sede educativa.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_sede_externa_codigo is 'Código de sede en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_sede_externa_nombre is 'Nombre de sede en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_sede_externa_nombre_busqueda is 'Nombre búsqueda de sede en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_servicio_educativo_externo_descripcion is 'Servicio educativo, campo descripción en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_servicio_educativo_externo_etiqueta_impresion is 'Servicio educativo, campo etiqueta impresión en acreditación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_componente is 'Nombre del componente de la calificación.';
COMMENT ON COLUMN acreditacion.sg_calificaciones_historicas_estudiante.che_componente_busqueda is 'Nombre búsqueda del componente de la calificación.';

 


CREATE TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud (

 

    che_pk int8 NOT NULL,
    che_estudiante_fk int8 NULL,
    che_matricula_externa_id int8 NULL,
    che_anio_matricula smallint NULL,
    che_observacion varchar(1000) NULL,
    che_ult_mod_fecha timestamp NULL,
    che_ult_mod_usuario varchar(45) NULL,
    che_version int4 NULL,
    che_fecha_realizado date NULL,
    che_proceso_de_creacion varchar(20) NULL,
    che_sede_fk int8 NULL,
    che_sede_externa_codigo varchar (15) NULL,
    che_sede_externa_nombre varchar (500) NULL,
    che_sede_externa_nombre_busqueda varchar (500) NULL,
    che_servicio_educativo_externo_id int8 NULL,
    che_servicio_educativo_externo_descripcion varchar (500) NULL,
    che_servicio_educativo_externo_etiqueta_impresion varchar (500) NULL,
    che_plan_estudio_externo_id int8 NULL,
    che_plan_estudio_externo_descripcion varchar (500) NULL,    
    che_componente varchar (250) NULL,
    che_componente_busqueda varchar (250) NULL,    
    che_PI varchar (45) NULL,
    che_NFI varchar (45) NULL,
    che_NPAESI varchar (45) NULL,
    che_RIX varchar (45) NULL,
    che_RIR varchar (45) NULL,
    che_RIRE varchar (45) NULL,
    che_NF varchar (45) NULL,
    che_NPAES varchar (45) NULL,
    che_PI_MODIF character(1) NULL,
    che_PI_R varchar (45) NULL,
    che_PIR     varchar (45) NULL,
    che_evaluacion_externa_id int8 NULL,
    rev bigint NOT NULL,
    revtype smallint,
    CONSTRAINT sg_calificaciones_historicas_estudiante_aud_pkey PRIMARY KEY (che_pk, rev)
    --CONSTRAINT calificaciones_estudiante_revinfo_fk FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

 

CREATE INDEX sg_cal_hist_anio_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_anio_matricula);
CREATE INDEX sg_cal_hist_sede_codigo_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_sede_externa_codigo);
CREATE INDEX sg_cal_hist_sede_nombre_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_sede_externa_nombre);
CREATE INDEX sg_cal_hist_sede_fk_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_sede_fk);
CREATE INDEX sg_cal_hist_componente_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_componente);


INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('PLANTILLA_CALIFICACIONES_HISTORICO_CODIGO', 'Calificacion hitorico estudiante', 'calificacion hitorico estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');

ALTER TABLE centros_educativos.sg_plan_escolar_anual
ALTER COLUMN pea_evaluacion_uno  TYPE character varying(500);

ALTER TABLE centros_educativos.sg_plan_escolar_anual
ALTER COLUMN pea_evaluacion_dos  TYPE character varying(500);

ALTER TABLE centros_educativos.sg_plan_escolar_anual_aud
ALTER COLUMN pea_evaluacion_uno  TYPE character varying(500);

ALTER TABLE centros_educativos.sg_plan_escolar_anual_aud
ALTER COLUMN pea_evaluacion_dos  TYPE character varying(500);

CREATE OR REPLACE FUNCTION centros_educativos.procesar_vistas_materializadas_asistencias() RETURNS void AS $$
DECLARE
asistencias_anio_actual TEXT;
asistencias_anio_anterior TEXT;
asistencias_meses_anio_actual TEXT;
asistencias_meses_anio_anterior TEXT;
mot_inasistencias_meses_anio_actual TEXT;
mot_inasistencias_meses_anio_anterior TEXT;
BEGIN
        
asistencias_meses_anio_actual = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_asistencias_meses_' || to_char(CURRENT_DATE,'yyyy');
raise notice 'Ejecutando: %', asistencias_meses_anio_actual;
EXECUTE asistencias_meses_anio_actual;

asistencias_meses_anio_anterior = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_asistencias_meses_' || to_char(CURRENT_DATE - INTERVAL '1 YEAR','yyyy');
raise notice 'Ejecutando: %', asistencias_meses_anio_anterior;
EXECUTE asistencias_meses_anio_anterior;

-- MOTIVOS INASISTENCIAS. MESES EN AÑO

mot_inasistencias_meses_anio_actual = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_mot_inasistencias_meses_' || to_char(CURRENT_DATE,'yyyy');
raise notice 'Ejecutando: %', mot_inasistencias_meses_anio_actual;
EXECUTE mot_inasistencias_meses_anio_actual;

mot_inasistencias_meses_anio_anterior = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_mot_inasistencias_meses_' || to_char(CURRENT_DATE - INTERVAL '1 YEAR','yyyy');
raise notice 'Ejecutando: %', mot_inasistencias_meses_anio_anterior;
EXECUTE mot_inasistencias_meses_anio_anterior;

-- ASISTENCIAS. AÑO

asistencias_anio_actual = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_asistencias_' || to_char(CURRENT_DATE,'yyyy');
raise notice 'Ejecutando: %', asistencias_anio_actual;
EXECUTE asistencias_anio_actual;

asistencias_anio_anterior = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_estudiantes_asistencias_' || to_char(CURRENT_DATE - INTERVAL '1 YEAR','yyyy');
raise notice 'Ejecutando: %', asistencias_anio_anterior;
EXECUTE asistencias_anio_anterior;

END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION centros_educativos.procesar_vistas_materializadas_promedios_grados() RETURNS void AS $$
DECLARE
promedios_grados_sedes_anio_anterior TEXT;
promedios_grados_sedes_anio_actual TEXT;
promedios_grados_anio_anterior TEXT;
promedios_grados_anio_actual TEXT;
BEGIN
        
-- GRADOS POR SEDE		
		
promedios_grados_sedes_anio_actual = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_promedios_grados_sedes_cal_apr_' || to_char(CURRENT_DATE,'yyyy');
raise notice 'Ejecutando: %', promedios_grados_sedes_anio_actual;
EXECUTE promedios_grados_sedes_anio_actual;

promedios_grados_sedes_anio_anterior = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_promedios_grados_sedes_cal_apr_' || to_char(CURRENT_DATE - INTERVAL '1 YEAR','yyyy');
raise notice 'Ejecutando: %', promedios_grados_sedes_anio_anterior;
EXECUTE promedios_grados_sedes_anio_anterior;

-- GRADOS

promedios_grados_anio_actual = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_promedios_grados_cal_apr_' || to_char(CURRENT_DATE,'yyyy');
raise notice 'Ejecutando: %', promedios_grados_anio_actual;
EXECUTE promedios_grados_anio_actual;

promedios_grados_anio_anterior = 'REFRESH MATERIALIZED VIEW centros_educativos.sg_promedios_grados_cal_apr_' || to_char(CURRENT_DATE - INTERVAL '1 YEAR','yyyy');
raise notice 'Ejecutando: %', promedios_grados_anio_anterior;
EXECUTE promedios_grados_anio_anterior;


END;
$$ LANGUAGE plpgsql;

ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD CONSTRAINT sg_che_sede_fk FOREIGN KEY (che_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD CONSTRAINT sg_che_estudiante_fk FOREIGN KEY (che_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT;



--VISTA MATERIALIZADA PARA SERVICIOS DE MATRICULA
CREATE MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac AS          
select dir.dir_departamento ,per.per_sexo_fk ,per.per_estado_civil_fk ,rel.rea_sub_modalidad_atencion_fk,per.per_fecha_nacimiento, count(mat_pk) as cantidad from centros_educativos.sg_matriculas mat inner join centros_educativos.sg_estudiantes est on est.est_pk=mat.mat_estudiante_fk
inner join centros_educativos.sg_personas per on per.per_pk=est.est_persona
inner join centros_educativos.sg_secciones sec on sec.sec_pk=mat.mat_seccion_fk
inner join centros_educativos.sg_servicio_educativo serv on serv.sdu_pk=sec.sec_servicio_educativo_fk
inner join centros_educativos.sg_sedes sed on sed.sed_pk=serv.sdu_sede_fk
inner join centros_educativos.sg_direcciones dir on dir.dir_pk=sed.sed_direccion_fk
inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
where mat.mat_estado='ABIERTO' and rel.rea_modalidad_atencion_flexible=true     
group by dir.dir_departamento,per.per_sexo_fk,per.per_estado_civil_fk ,rel.rea_sub_modalidad_atencion_fk,per.per_fecha_nacimiento;

CREATE OR REPLACE FUNCTION centros_educativos.procesar_vistas_materializadas_genericas() RETURNS void AS $$
DECLARE

BEGIN
        
REFRESH MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac;

END;
$$ LANGUAGE plpgsql;

ALTER TABLE centros_educativos.sg_acuerdos_sedes ADD COLUMN ase_solo_lectura boolean;
ALTER TABLE centros_educativos.sg_acuerdos_sedes_aud ADD COLUMN ase_solo_lectura boolean;


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tiene_trastorno_aprendizaje boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tiene_trastorno_aprendizaje boolean;

update centros_educativos.sg_personas p set per_tiene_trastorno_aprendizaje = true where
exists (select 1 from centros_educativos.sg_personas_trastornos_aprendizaje t where t.per_pk = p.per_pk);


--4.17.7
ALTER TABLE centros_educativos.sg_medidas_examen_fisico ADD COLUMN mef_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_medidas_examen_fisico_aud ADD COLUMN mef_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_medidas_examen_fisico ADD CONSTRAINT sg_mef_estudiante_fk FOREIGN KEY (mef_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_habitos_alimentacion ADD COLUMN hal_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_habitos_alimentacion_aud ADD COLUMN hal_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_habitos_alimentacion ADD CONSTRAINT sg_hal_estudiante_fk FOREIGN KEY (hal_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_habitos_personales ADD COLUMN hap_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_habitos_personales_aud ADD COLUMN hap_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_habitos_personales ADD CONSTRAINT sg_hap_estudiante_fk FOREIGN KEY (hap_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


update centros_educativos.sg_medidas_examen_fisico mef set mef_estudiante_fk=(select fich.fsa_estudiante from 
centros_educativos.sg_medidas_examen_fisico mef_aux inner join
centros_educativos.sg_fichas_salud fich on mef_aux.mef_ficha_salud_fk=fich.fsa_pk
where mef_aux.mef_pk=mef.mef_pk
); 

update centros_educativos.sg_habitos_alimentacion mef set hal_estudiante_fk=(select fich.fsa_estudiante from 
centros_educativos.sg_habitos_alimentacion mef_aux inner join
centros_educativos.sg_fichas_salud fich on mef_aux.hal_ficha_salud_fk=fich.fsa_pk
where mef_aux.hal_pk=mef.hal_pk
) ;

update centros_educativos.sg_habitos_personales mef set hap_estudiante_fk=(select fich.fsa_estudiante from 
centros_educativos.sg_habitos_personales mef_aux inner join
centros_educativos.sg_fichas_salud fich on mef_aux.hap_ficha_salud_fk=fich.fsa_pk
where mef_aux.hap_pk=mef.hap_pk
); 

ALTER TABLE centros_educativos.sg_medidas_examen_fisico DROP COLUMN mef_ficha_salud_fk;
ALTER TABLE centros_educativos.sg_habitos_alimentacion DROP COLUMN hal_ficha_salud_fk;
ALTER TABLE centros_educativos.sg_habitos_personales DROP COLUMN hap_ficha_salud_fk;
ALTER TABLE centros_educativos.sg_medidas_examen_fisico_aud DROP COLUMN mef_ficha_salud_fk;
ALTER TABLE centros_educativos.sg_habitos_alimentacion_aud DROP COLUMN hal_ficha_salud_fk;
ALTER TABLE centros_educativos.sg_habitos_personales_aud DROP COLUMN hap_ficha_salud_fk;

-- 4.18.0


ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_miembros_renovado_padre_fk bigint;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_miembros_renovado_padre_fk bigint;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD CONSTRAINT sg_oae_miembros_renovado_padre_fk FOREIGN KEY (oae_miembros_renovado_padre_fk) REFERENCES centros_educativos.sg_organismo_administracion_escolar(oae_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RENOVAR_MIEMBROS','E467',  'Permite renovar miembros oae', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_es_miembro_renovado boolean default false;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_es_miembro_renovado boolean;

update centros_educativos.sg_organismo_administracion_escolar set oae_es_miembro_renovado=true where oae_miembros_renovado_padre_fk is not null;

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_numero_resolucion character varying(30);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_numero_resolucion character varying(30);

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_fecha_resolucion date;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_fecha_resolucion date;


CREATE UNIQUE INDEX sg_oae_aprobada_sede_uk ON centros_educativos.sg_organismo_administracion_escolar USING btree (oae_sede_fk) WHERE ((oae_estado)::text = 'APROBADO'::text);

ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD COLUMN che_estudiante_nie int8 NULL;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD COLUMN che_estudiante_per_pk int8 NULL;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud ADD COLUMN che_estudiante_nie int8 NULL;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud ADD COLUMN che_estudiante_per_pk int8 NULL;

 

UPDATE acreditacion.sg_calificaciones_historicas_estudiante
set che_estudiante_nie = (select per_nie from centros_educativos.sg_personas p join centros_educativos.sg_estudiantes e on p.per_pk = e.est_persona where p.per_nie is not null and e.est_pk = che_estudiante_fk);

 

UPDATE acreditacion.sg_calificaciones_historicas_estudiante
set che_estudiante_per_pk = (select per_pk from centros_educativos.sg_personas p join centros_educativos.sg_estudiantes e on p.per_pk = e.est_persona where p.per_nie is not null and e.est_pk = che_estudiante_fk);

 


DROP INDEX IF EXISTS sg_cal_hist_componente_idx;
CREATE INDEX sg_cal_hist_nie_idx ON acreditacion.sg_calificaciones_historicas_estudiante USING btree (che_estudiante_nie);


CREATE OR REPLACE FUNCTION centros_educativos.evaluar_vencimiento_miembros_oae() RETURNS void AS $$
DECLARE
oae record;
miembro record;
err_context text;
    BEGIN
            for oae in (select oae_pk from centros_educativos.sg_organismo_administracion_escolar where oae_fecha_vencimiento <= now() and oae_miembros_vigentes and oae_estado = 'APROBADO')
            LOOP     	
                BEGIN
					RAISE NOTICE 'PROCESANDO OAE FECHA VENCIDA, %', oae.oae_pk;
					for miembro in (select poa_pk from centros_educativos.sg_persona_organismo_administracion where 
									poa_organismo_administrativo_escolar = oae.oae_pk and poa_hasta <= now() and poa_habilitado)
					LOOP
						BEGIN
							RAISE NOTICE 'DESHABILITANDO MIEMBRO VENCIDO, %', miembro.poa_pk;
							update centros_educativos.sg_persona_organismo_administracion set poa_habilitado = false where poa_pk = miembro.poa_pk;
						END;
					END LOOP;
					
					IF NOT EXISTS (select 1 from centros_educativos.sg_persona_organismo_administracion where 
									poa_organismo_administrativo_escolar = oae.oae_pk and poa_habilitado)
						 THEN 
						 RAISE NOTICE 'OAE SIN MIEMBROS HABILITADOS, SE MARCA MIEMBROS NO VIGENTES, %', oae.oae_pk; 
						 update centros_educativos.sg_organismo_administracion_escolar set oae_miembros_vigentes = false where oae_pk = oae.oae_pk;
					END IF;
                END;    
            END LOOP;	
        EXCEPTION 
        WHEN OTHERS THEN
                GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
                RAISE NOTICE 'Query exception';
                RAISE INFO 'Error Name:%',SQLERRM;
                RAISE INFO 'Error State:%', SQLSTATE;
                RAISE INFO 'Error Context:%', err_context;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION centros_educativos.procesar_vistas_materializadas_genericas() RETURNS void AS $$
DECLARE

BEGIN
        
REFRESH MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac;
PERFORM centros_educativos.evaluar_vencimiento_miembros_oae(); 

END;
$$ LANGUAGE plpgsql;


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('DIAS_ANTICIPO_ALERTA_VENCIMIENTO_OAE', 'Días de anticipación con el que se muestra una alerta sobre el vencimiento de una OAE','dias de anticipacion con el que se muestra una alerta sobre el vencimiento de una oae','30', 0, CURRENT_TIMESTAMP, 'casuser');


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('DIAS_VISUALIZACION_CAMBIO_ESTADO_ALERTAS_OAE', 'Días que una alerta de cambio de estado de OAE es visualizada','días que una alerta de cambio de estado de OAE es visualizada','5', 0, CURRENT_TIMESTAMP, 'casuser');


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_APROBADA','EP87',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_MASDATOS','EP88',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_RECHAZADA','EP89',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_ENVIADA','EP90',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);

--4.19.0


-- Sustitucion de miembros OAE
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sustitucion_miembro_oae_smo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sustitucion_miembro_oae (smo_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_sustitucion_miembro_oae_smo_pk_seq'::regclass), 
smo_fecha date, smo_oae_fk bigint, smo_estado character varying(50), smo_numero_resolucion character varying(50), smo_fecha_resolucion date,smo_motivo_rechazo character varying(500),
smo_ult_mod_fecha timestamp without time zone, smo_ult_mod_usuario character varying(45), smo_version integer, 
CONSTRAINT sg_sustitucion_miembro_oae_pkey PRIMARY KEY (smo_pk),
CONSTRAINT sg_smo_oae_fk FOREIGN KEY (smo_oae_fk) REFERENCES centros_educativos.sg_organismo_administracion_escolar(oae_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_sustitucion_miembro_oae IS 'Tabla para el registro de sustitucion de miembros oae.';
COMMENT ON COLUMN centros_educativos.sg_sustitucion_miembro_oae.smo_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_sustitucion_miembro_oae.smo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sustitucion_miembro_oae.smo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sustitucion_miembro_oae.smo_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sustitucion_miembro_oae_aud (smo_pk bigint NOT NULL, 
smo_fecha date, smo_oae_fk bigint, smo_estado character varying(50), smo_numero_resolucion character varying(50), smo_fecha_resolucion date,smo_motivo_rechazo character varying(500),
smo_ult_mod_fecha timestamp without time zone, smo_ult_mod_usuario character varying(45), smo_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_sustitucion_miembro_oae_aud ADD PRIMARY KEY (smo_pk, rev);

-- Relación sustitución miembro OAE
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_sustitucion_miembro_oae_rsm_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sustitucion_miembro_oae (rsm_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_sustitucion_miembro_oae_rsm_pk_seq'::regclass), 
rsm_miembro_a_sustituir_fk bigint, rsm_miembro_sustituyente_fk bigint, rsm_sustitucion_miembro_fk bigint,
rsm_ult_mod_fecha timestamp without time zone, rsm_ult_mod_usuario character varying(45), rsm_version integer, 
CONSTRAINT sg_rel_sustitucion_miembro_oae_pkey PRIMARY KEY (rsm_pk),
CONSTRAINT sg_rsm_miembro_a_sustituir_fk FOREIGN KEY (rsm_miembro_a_sustituir_fk) REFERENCES centros_educativos.sg_persona_organismo_administracion(poa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rsm_miembro_sustituyente_fk FOREIGN KEY (rsm_miembro_sustituyente_fk) REFERENCES centros_educativos.sg_persona_organismo_administracion(poa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rsm_sustitucion_miembro_fk FOREIGN KEY (rsm_sustitucion_miembro_fk) REFERENCES centros_educativos.sg_sustitucion_miembro_oae(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_rel_sustitucion_miembro_oae IS 'Tabla para el registro de relación sustitución miembro oae.';
COMMENT ON COLUMN centros_educativos.sg_rel_sustitucion_miembro_oae.rsm_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_sustitucion_miembro_oae.rsm_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sustitucion_miembro_oae.rsm_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sustitucion_miembro_oae.rsm_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sustitucion_miembro_oae_aud (rsm_pk bigint NOT NULL, 
rsm_miembro_a_sustituir_fk bigint, rsm_miembro_sustituyente_fk bigint, rsm_sustitucion_miembro_fk bigint,
rsm_ult_mod_fecha timestamp without time zone, rsm_ult_mod_usuario character varying(45), rsm_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_sustitucion_miembro_oae_aud ADD PRIMARY KEY (rsm_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_SISTEMA_INFORMACION_GERENCIAL','W19',  '', 4, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_SUSTITUCION_MIEMBRO_OAE','E469',  'Crear relación de sustitución de miembros', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_SUSTITUCION_MIEMBRO_OAE','E471',  'Actualizar relación de sustitución de miembros', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_SUSTITUCION_MIEMBRO_OAE','E473',  'Eliminar relación de sustitución de miembros', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SUSTITUCION_MIEMBRO_OAE','E475',  'Crear cabezal de sustitución de miembro', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SUSTITUCION_MIEMBRO_OAE','E477',  'Actualizar cabezal de sustitución de miembro', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SUSTITUCION_MIEMBRO_OAE','E479',  'Eliminar cabezal de sustitución de miembro', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SUSTITUCION_MIEMBRO_OAE','EB101',  'Buscar sustitucion de miembro oae', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_SUSTITUCION_MIEMBRO_OAE','EB102',  'Buscar relación de sustitución de miembros oae', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_EVALUAR_SUSTITUCION_MIEMBROS','EM89',  'MENU: Evaluar sustitución miembros', 1, true, null, null,0);

-- 4.20.0


ALTER TABLE centros_educativos.sg_modulos_diplomado ADD CONSTRAINT sg_modulo_diplomado_diplomado_fk FOREIGN KEY (mdi_diplomado_fk) REFERENCES centros_educativos.sg_diplomados(dip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_acuerdos_sedes RENAME COLUMN ase_id_resolucion to ase_numero_resolucion;
ALTER TABLE centros_educativos.sg_acuerdos_sedes_aud RENAME COLUMN ase_id_resolucion to ase_numero_resolucion;

ALTER TABLE centros_educativos.sg_acuerdos_sedes ALTER COLUMN ase_numero_resolucion TYPE varchar(50);
ALTER TABLE centros_educativos.sg_acuerdos_sedes_aud ALTER COLUMN ase_numero_resolucion TYPE varchar(50);


ALTER TABLE centros_educativos.sg_acuerdos_sedes ADD COLUMN ase_fecha_resolucion date;
ALTER TABLE centros_educativos.sg_acuerdos_sedes_aud ADD COLUMN ase_fecha_resolucion date;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACUERDO_SEDE_TRAMITE','E483',  'Elimina acuerdo sede generado por trámite', 1, true, null, null,0);


-- FUNCIONALIDAD DE ENCUESTAS

CREATE TABLE IF NOT EXISTS centros_educativos.sg_datos_residenciales_personas (per_pk bigint NOT NULL, per_version integer, CONSTRAINT sg_datos_residenciales_personas_pkey PRIMARY KEY (per_pk), CONSTRAINT sg_datos_residenciales_personas_fkey FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_datos_residenciales_personas_aud (per_pk bigint NOT NULL, per_version integer, rev bigint, revtype smallint);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_elementos_hogar (per_pk bigint NOT NULL,eho_pk bigint NOT NULL, CONSTRAINT sg_personas_elementos_hogar_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_elementos_hogar_elemento FOREIGN KEY (eho_pk) REFERENCES catalogo.sg_elementos_hogar (eho_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_elementos_hogar_aud (per_pk bigint NOT NULL,eho_pk bigint NOT NULL, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD PRIMARY KEY (per_pk, rev);


ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_cantidad_dormitorios_casa integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_tiene_servicio_energia_electrica_residencial boolean;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_tiene_conexion_internet_residencial boolean;

ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_compania_internet_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_tipo_servicio_sanitario_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_fuente_abastecimiento_agua_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_material_piso_residencial integer;
    
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_tipo_servicio_sanitario_residencial_otro character varying (250);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_fuente_abastecimiento_agua_residencial_otra character varying (250);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD COLUMN per_material_piso_residencial_otro character varying (250);


ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD CONSTRAINT sg_personas_compania_internet_residencial_fk FOREIGN KEY (per_compania_internet_residencial) REFERENCES catalogo.sg_companias_telecomunicacion (cte_pk);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD CONSTRAINT sg_personas_tipo_servicio_sanitario_residencial_fk FOREIGN KEY (per_tipo_servicio_sanitario_residencial) REFERENCES catalogo.sg_tipos_servicio_sanitario (tss_pk);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD CONSTRAINT sg_personas_fuente_abastecimiento_agua_residencial_fk FOREIGN KEY (per_fuente_abastecimiento_agua_residencial) REFERENCES catalogo.sg_fuentes_abastecimiento_agua (faa_pk);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas ADD CONSTRAINT sg_personas_material_piso_residencial_fk FOREIGN KEY (per_material_piso_residencial) REFERENCES catalogo.sg_materiales_piso (map_pk);

ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_cantidad_dormitorios_casa integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_tiene_servicio_energia_electrica_residencial boolean;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_tiene_conexion_internet_residencial boolean;

ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_compania_internet_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_tipo_servicio_sanitario_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_fuente_abastecimiento_agua_residencial integer;
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_material_piso_residencial integer;
    
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_tipo_servicio_sanitario_residencial_otro character varying (250);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_fuente_abastecimiento_agua_residencial_otra character varying (250);
ALTER TABLE centros_educativos.sg_datos_residenciales_personas_aud ADD COLUMN per_material_piso_residencial_otro character varying (250);


INSERT INTO centros_educativos.sg_datos_residenciales_personas (per_pk, per_tipo_servicio_sanitario_residencial, per_version) select per_pk, per_tipo_servicio_sanitario_fk, 0 from centros_educativos.sg_personas;



CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_encuestas_estudiantes_enc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE IF NOT EXISTS centros_educativos.sg_encuestas_estudiantes (enc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_encuestas_estudiantes_enc_pk_seq'::regclass), enc_version integer, CONSTRAINT sg_encuestas_estudiantes_pkey PRIMARY KEY (enc_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_encuestas_estudiantes_aud (enc_pk bigint NOT NULL, enc_version integer, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD PRIMARY KEY (enc_pk, rev);

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_cantidad_dormitorios_casa integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_tiene_servicio_energia_electrica_residencial boolean;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_tiene_conexion_internet_residencial boolean;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_compania_internet_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_tipo_servicio_sanitario_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_fuente_abastecimiento_agua_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_material_piso_residencial integer;
    
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_tipo_servicio_sanitario_residencial_otro character varying (250);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_fuente_abastecimiento_agua_residencial_otra character varying (250);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_material_piso_residencial_otro character varying (250);


ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD CONSTRAINT sg_encuestas_compania_internet_residencial_fk FOREIGN KEY (enc_compania_internet_residencial) REFERENCES catalogo.sg_companias_telecomunicacion (cte_pk);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD CONSTRAINT sg_encuestas_tipo_servicio_sanitario_residencial_fk FOREIGN KEY (enc_tipo_servicio_sanitario_residencial) REFERENCES catalogo.sg_tipos_servicio_sanitario (tss_pk);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD CONSTRAINT sg_encuestas_fuente_abastecimiento_agua_residencial_fk FOREIGN KEY (enc_fuente_abastecimiento_agua_residencial) REFERENCES catalogo.sg_fuentes_abastecimiento_agua (faa_pk);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD CONSTRAINT sg_encuestas_material_piso_residencial_fk FOREIGN KEY (enc_material_piso_residencial) REFERENCES catalogo.sg_materiales_piso (map_pk);

ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_cantidad_dormitorios_casa integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_tiene_servicio_energia_electrica_residencial boolean;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_tiene_conexion_internet_residencial boolean;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_compania_internet_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_tipo_servicio_sanitario_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_fuente_abastecimiento_agua_residencial integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_material_piso_residencial integer;
    
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_tipo_servicio_sanitario_residencial_otro character varying (250);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_fuente_abastecimiento_agua_residencial_otra character varying (250);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_material_piso_residencial_otro character varying (250);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_encuestas_elementos_hogar (enc_pk bigint NOT NULL,eho_pk bigint NOT NULL, CONSTRAINT sg_encuestas_elementos_hogar_enc_fk FOREIGN KEY (enc_pk) REFERENCES centros_educativos.sg_encuestas_estudiantes (enc_pk), CONSTRAINT sg_encuestas_elementos_hogar_elemento_fk FOREIGN KEY (eho_pk) REFERENCES catalogo.sg_elementos_hogar (eho_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_encuestas_elementos_hogar_aud (enc_pk bigint NOT NULL,eho_pk bigint NOT NULL, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_estudiante_fk bigint;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_fecha_ingreso timestamp without time zone;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_fecha_ingreso timestamp without time zone;  
 
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_zona_residencia integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_zona_residencia integer;  
  
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_vive_con_cantidad integer;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_vive_con_cantidad integer;    
    
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_estudiante_dis_km_centro NUMERIC(10,2);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_estudiante_dis_km_centro NUMERIC(10,2);


ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_ult_mod_fecha timestamp without time zone;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_ult_mod_fecha timestamp without time zone;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_ult_mod_usuario character varying(45);
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_ult_mod_usuario character varying(45);




ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD CONSTRAINT sg_encuesta_estudiante_fkey FOREIGN KEY (enc_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes (est_pk);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ENCUESTA_ESTUDIANTE','EB103',  'Buscar encuesta de estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ENCUESTA_ESTUDIANTE','E480',  'Crear encuesta de estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ENCUESTA_ESTUDIANTE','E481',  'Actualizar encuesta de estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ENCUESTA_ESTUDIANTE','E482',  'Eliminar encuesta de estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ENCUESTAS_ESTUDIANTES','EM90',  'MENU: Encuestas estudiantes', 1, true, null, null,0);



CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_menores_encuestas_estudiantes_men_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE IF NOT EXISTS centros_educativos.sg_menores_encuesta_estudiante (
men_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_menores_encuestas_estudiantes_men_pk_seq'::regclass),
men_matriculado_siges boolean,
men_tiene_nie boolean,
men_nie bigint,
men_estudia boolean,
men_nombre_completo character varying (500),
men_fecha_nacimiento date,
men_encuesta_estudiante_fk bigint,
men_sede_fk integer,
men_medio_transporte_fk integer,
men_ult_mod_fecha timestamp without time zone,
men_ult_mod_usuario character varying (45),
men_version integer,
CONSTRAINT sg_menores_encuestas_estudiantes_pkey PRIMARY KEY (men_pk),
 CONSTRAINT sg_menores_encuestas_estudiantes_sede_fk FOREIGN KEY (men_sede_fk) REFERENCES centros_educativos.sg_sedes (sed_pk),
 CONSTRAINT sg_menores_encuestas_estudiantes_encuesta_fk FOREIGN KEY (men_encuesta_estudiante_fk) REFERENCES centros_educativos.sg_encuestas_estudiantes (enc_pk),
 CONSTRAINT sg_menores_encuestas_estudiantes_medio_trans_fk FOREIGN KEY (men_medio_transporte_fk) REFERENCES catalogo.sg_medios_transporte (mtr_pk));


CREATE TABLE IF NOT EXISTS centros_educativos.sg_menores_encuesta_estudiante_aud (men_pk bigint NOT NULL,
men_matriculado_siges boolean,
men_tiene_nie boolean,
men_nie bigint,
men_estudia boolean,
men_nombre_completo character varying (500),
men_fecha_nacimiento date,
men_encuesta_estudiante_fk bigint,
men_sede_fk integer,
men_medio_transporte_fk integer,
men_ult_mod_fecha timestamp without time zone,
men_ult_mod_usuario character varying (45),
men_version integer,
rev bigint, revtype smallint,
CONSTRAINT sg_menores_encuestas_estudiantes_aud_pkey PRIMARY KEY (men_pk, rev));

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_validado_siges boolean;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_validado_siges boolean;


--4.20.1 
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD COLUMN che_observacion_edicion character varying(1000);
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud ADD COLUMN che_observacion_edicion character varying(1000);

ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD COLUMN che_archivo_edicion_fk bigint;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud ADD COLUMN che_archivo_edicion_fk bigint;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD CONSTRAINT sg_che_archivo_edicion_fk FOREIGN KEY (che_archivo_edicion_fk) REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_TRAMITE_SIMPLE','EP97',  '', 1, true, null, null,0);


-- 4.21.0


CREATE MATERIALIZED VIEW centros_educativos.sg_matriculas_abiertas_por_dep_mun_sexo as
 SELECT 
    dir.dir_departamento,
	dir.dir_municipio,
    per.per_sexo_fk,
    count(mat.mat_pk) AS cantidad
   FROM centros_educativos.sg_matriculas mat
     INNER JOIN centros_educativos.sg_estudiantes est ON est.est_pk = mat.mat_estudiante_fk
     INNER JOIN centros_educativos.sg_personas per ON per.per_pk = est.est_persona
     INNER JOIN centros_educativos.sg_secciones sec ON sec.sec_pk = mat.mat_seccion_fk
     INNER JOIN centros_educativos.sg_servicio_educativo serv ON serv.sdu_pk = sec.sec_servicio_educativo_fk
     INNER JOIN centros_educativos.sg_sedes sed ON sed.sed_pk = serv.sdu_sede_fk
     INNER JOIN centros_educativos.sg_direcciones dir ON dir.dir_pk = sed.sed_direccion_fk
  WHERE mat.mat_estado = 'ABIERTO'
  GROUP BY dir.dir_departamento, dir.dir_municipio, per.per_sexo_fk;

 CREATE MATERIALIZED VIEW centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo as
 SELECT 
    dir.dir_departamento,
	dir.dir_municipio,
    per.per_sexo_fk,
    count(*) AS cantidad
   FROM centros_educativos.sg_encuestas_estudiantes enc
     INNER JOIN centros_educativos.sg_estudiantes est ON est.est_pk = enc.enc_estudiante_fk
     INNER JOIN centros_educativos.sg_personas per ON per.per_pk = est.est_persona
	 INNER JOIN centros_educativos.sg_matriculas mat ON (est.est_ultima_matricula_fk = mat.mat_pk)
     INNER JOIN centros_educativos.sg_secciones sec ON sec.sec_pk = mat.mat_seccion_fk
     INNER JOIN centros_educativos.sg_servicio_educativo serv ON serv.sdu_pk = sec.sec_servicio_educativo_fk
     INNER JOIN centros_educativos.sg_sedes sed ON sed.sed_pk = serv.sdu_sede_fk
     INNER JOIN centros_educativos.sg_direcciones dir ON dir.dir_pk = sed.sed_direccion_fk
	 WHERE mat.mat_estado = 'ABIERTO'
  GROUP BY dir.dir_departamento, dir.dir_municipio, per.per_sexo_fk;

CREATE OR REPLACE FUNCTION centros_educativos.procesar_vistas_materializadas_genericas() RETURNS void AS $$
DECLARE

BEGIN
        
REFRESH MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac;
PERFORM centros_educativos.evaluar_vencimiento_miembros_oae(); 
REFRESH MATERIALIZED VIEW centros_educativos.sg_matriculas_abiertas_por_dep_mun_sexo;
REFRESH MATERIALIZED VIEW centros_educativos.sg_encuestas_matriculas_abiertas_por_dep_mun_sexo;

END;
$$ LANGUAGE plpgsql;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_vive_con_personas_menores boolean;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_vive_con_personas_menores boolean;   

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_MONITOREO_ENCUESTAS_ESTUDIANTES','EM91',  'MENU: Monitoreo encuestas estudiantes', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_ultima_encuesta_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_ultima_encuesta_fk bigint;   


ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_ultima_encuesta_fk FOREIGN KEY (est_ultima_encuesta_fk) REFERENCES centros_educativos.sg_encuestas_estudiantes (enc_pk);

CREATE INDEX sg_estudiantes_ultima_encuesta_idx ON centros_educativos.sg_estudiantes USING btree (est_ultima_encuesta_fk);


ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_estudiante_fk bigint;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_estudiante_fk FOREIGN KEY (men_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes (est_pk);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_persona_fk bigint;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_persona_fk bigint;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_persona_fk FOREIGN KEY (men_persona_fk) REFERENCES centros_educativos.sg_personas (per_pk);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante DROP COLUMN men_nombre_completo;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud DROP COLUMN men_nombre_completo;



ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_es_familiar boolean;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_es_familiar boolean;


ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_tipo_parentesco integer;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_tipo_parentesco integer;


ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_primer_nombre character varying (100);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_primer_nombre character varying (100);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_segundo_nombre character varying (100);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_segundo_nombre character varying (100);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_primer_apellido character varying (100);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_primer_apellido character varying (100);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_segundo_apellido character varying (100);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_segundo_apellido character varying (100);

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_sexo_fk integer;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_sexo_fk integer;

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_estado_civil_fk integer;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_estado_civil_fk integer;

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD COLUMN men_nacionalidad_fk integer;
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante_aud ADD COLUMN men_nacionalidad_fk integer;

ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_sexo_fk FOREIGN KEY (men_sexo_fk) REFERENCES catalogo.sg_sexos (sex_pk);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_nacionalidad_fk FOREIGN KEY (men_nacionalidad_fk) REFERENCES catalogo.sg_nacionalidades (nac_pk);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_estado_civil_fk FOREIGN KEY (men_estado_civil_fk) REFERENCES catalogo.sg_estados_civil (eci_pk);
ALTER TABLE centros_educativos.sg_menores_encuesta_estudiante ADD CONSTRAINT sg_menores_tipo_parentesco_fk FOREIGN KEY (men_tipo_parentesco) REFERENCES catalogo.sg_tipos_parentesco (tpa_pk);

-- 4.21.3

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('MENSAJE_ENCUESTA_ESTUDIANTE', 'Mensaje que se agrega en pantalla de encuesta de estudiante','mensaje que se agrega en pantalla de encuesta de estudiante',null, 0, CURRENT_TIMESTAMP, 'admin');


DROP MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ;

CREATE MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac AS          
select dir.dir_departamento,dir.dir_municipio ,per.per_sexo_fk ,per.per_estado_civil_fk ,rel.rea_sub_modalidad_atencion_fk,per.per_fecha_nacimiento, count(mat_pk) as cantidad from centros_educativos.sg_matriculas mat inner join centros_educativos.sg_estudiantes est on est.est_pk=mat.mat_estudiante_fk
inner join centros_educativos.sg_personas per on per.per_pk=est.est_persona
inner join centros_educativos.sg_secciones sec on sec.sec_pk=mat.mat_seccion_fk
inner join centros_educativos.sg_servicio_educativo serv on serv.sdu_pk=sec.sec_servicio_educativo_fk
inner join centros_educativos.sg_sedes sed on sed.sed_pk=serv.sdu_sede_fk
inner join centros_educativos.sg_direcciones dir on dir.dir_pk=sed.sed_direccion_fk
inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
where mat.mat_estado='ABIERTO' and rel.rea_modalidad_atencion_flexible=true     
group by dir.dir_departamento,dir.dir_municipio,per.per_sexo_fk,per.per_estado_civil_fk ,rel.rea_sub_modalidad_atencion_fk,per.per_fecha_nacimiento;

--4.21.7
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CERRAR_SEDE','E491',  'Permite cerrar una sede', 1, true, null, null,0);

--4.22.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('IMPORTAR_CALIFICACION_HISTORICA','E493',  'Permite importación de calificaciones historicas', 1, true, null, null,0);

ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante ADD COLUMN che_es_importado boolean;
ALTER TABLE acreditacion.sg_calificaciones_historicas_estudiante_aud ADD COLUMN che_es_importado boolean;

-- 4.23.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('REINDEXAR_PERSONAS_LUCENE','E484',  'Reindexar personas en lucene', 1, true, null, null,0);



insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('BUSQUEDA_PERSONAS_LUCENE_HABILITADA', 'Búsqueda de personas con lucene habilitada','busqueda de personas con lucene habilitada','false', 0, CURRENT_TIMESTAMP, 'admin');


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('BUSQUEDA_PERSONAS_SIMILARES_LUCENE_HABILITADA', 'Búsqueda de personas similares con lucene habilitada','busqueda de personas similares con lucene habilitada','false', 0, CURRENT_TIMESTAMP, 'admin');



ALTER TABLE seguridad.sg_usuarios ADD CONSTRAINT sg_usuarios_personas_fk FOREIGN KEY (usu_persona_pk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


--4.23.1


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor,con_version, con_ult_mod_fecha, con_ult_mod_usuario)
values('AUDIENCE_MAX_RESULTS', 'El valor de max result dependiendo de la audiencia','El valor de max result dependiendo de la audiencia',
'sigocentro:1000,sigocatalogo:2000', 0, CURRENT_TIMESTAMP, 'casuser');

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PROPUESTA_PEDAGOGICA','EB104',  'Buscar propuesta pedagógica', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PLAN_ESCOLAR_ANUAL','EB105',  'Buscar plan escolar anual', 1, true, null, null,0);


--4.23.5 
ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_sintoniza_canal_10 boolean;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_sintoniza_canal_10 boolean;

ALTER TABLE centros_educativos.sg_encuestas_estudiantes ADD COLUMN enc_sintoniza_franja_educativa boolean;
ALTER TABLE centros_educativos.sg_encuestas_estudiantes_aud ADD COLUMN enc_sintoniza_franja_educativa boolean;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_sintoniza_canal_10 boolean;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_sintoniza_canal_10 boolean;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_sintoniza_franja_educativa boolean;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_sintoniza_franja_educativa boolean;

--4.24.0 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('UNIR_PERSONA','E495',  'Permite unir personas', 1, true, null, null,0);