ALTER TABLE catalogo.sg_ayudas ADD COLUMN ayu_resaltada boolean;
ALTER TABLE catalogo.sg_ayudas_aud ADD COLUMN ayu_resaltada boolean;
UPDATE catalogo.sg_ayudas set ayu_resaltada = false;

-- Tipos de Parentesco
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_parentesco_tpa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_parentesco (tpa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_parentesco_tpa_pk_seq'::regclass), tpa_codigo character varying(45), tpa_habilitado boolean, tpa_nombre character varying(255), tpa_nombre_busqueda character varying(255), tpa_ult_mod_fecha timestamp without time zone, tpa_ult_mod_usuario character varying(45), tpa_version integer, CONSTRAINT sg_tipos_parentesco_pkey PRIMARY KEY (tpa_pk), CONSTRAINT tpa_codigo_uk UNIQUE (tpa_codigo), CONSTRAINT tpa_nombre_uk UNIQUE (tpa_nombre));
COMMENT ON TABLE catalogo.sg_tipos_parentesco IS 'Tabla para el registro de tipos de parentesco.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_parentesco_aud (tpa_pk bigint NOT NULL, tpa_codigo character varying(45), tpa_habilitado boolean, tpa_nombre character varying(255), tpa_nombre_busqueda character varying(255), tpa_ult_mod_fecha timestamp without time zone, tpa_ult_mod_usuario character varying(45), tpa_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_tipos_parentesco_aud ADD PRIMARY KEY (tpa_pk, rev);

--Canton
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_cantones_can_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_cantones(can_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_cantones_can_pk_seq'::regclass),can_municipio_fk bigint NOT NULL, can_codigo CHARACTER varying(6),can_habilitado BOOLEAN,can_nombre CHARACTER varying(255),can_nombre_busqueda CHARACTER varying(255),can_ult_mod_fecha timestamp without TIME zone,can_ult_mod_usuario CHARACTER varying(45),can_version INTEGER,CONSTRAINT sg_canton_pkey PRIMARY KEY (can_pk), CONSTRAINT sg_can_municipio_fkey FOREIGN KEY (can_municipio_fk) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT can_codigo_nombre_uk UNIQUE (can_codigo,can_nombre));
CREATE TABLE IF NOT EXISTS    catalogo.sg_cantones_aud(can_pk bigint NOT NULL, can_municipio_fk bigint NOT NULL,can_codigo CHARACTER varying(6),can_habilitado BOOLEAN,can_nombre CHARACTER varying(255),can_nombre_busqueda CHARACTER varying(255),can_ult_mod_fecha timestamp without TIME zone,can_ult_mod_usuario CHARACTER varying(45),can_version INTEGER,rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_cantones IS 'Tabla para el registro de los cantones de El Salvador.';
COMMENT ON COLUMN catalogo.sg_cantones.can_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_cantones.can_codigo IS 'Código de identificación del cantón.';
COMMENT ON COLUMN catalogo.sg_cantones.can_municipio_fk IS 'Clave foránea del municipio al que pertenece el cantón.';
COMMENT ON COLUMN catalogo.sg_cantones.can_nombre IS 'Nombre con que del cantón.';
COMMENT ON COLUMN catalogo.sg_cantones.can_nombre_busqueda IS 'Nombre del cantón normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_cantones.can_habilitado IS 'Indica si el cantón se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_cantones.can_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_cantones.can_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_cantones.can_version IS 'Versión del catalogo que se uso para el registo.';

-- Estados Familiares
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_estados_civil_eci_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_estados_civil (eci_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_estados_civil_eci_pk_seq'::regclass), eci_codigo character varying(45), eci_habilitado boolean, eci_nombre character varying(255), eci_nombre_busqueda character varying(255), eci_ult_mod_fecha timestamp without time zone, eci_ult_mod_usuario character varying(45), eci_version integer, CONSTRAINT sg_estados_civil_pkey PRIMARY KEY (eci_pk), CONSTRAINT eci_codigo_uk UNIQUE (eci_codigo), CONSTRAINT eci_nombre_uk UNIQUE (eci_nombre));
COMMENT ON TABLE catalogo.sg_estados_civil IS 'Tabla para el registro de estados familiares.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_estados_civil_aud (eci_pk bigint NOT NULL, eci_codigo character varying(45), eci_habilitado boolean, eci_nombre character varying(255), eci_nombre_busqueda character varying(255), eci_ult_mod_fecha timestamp without time zone, eci_ult_mod_usuario character varying(45), eci_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_estados_civil_aud ADD PRIMARY KEY (eci_pk, rev);
-- Medios de Transporte
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_medios_transporte_mtr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_medios_transporte (mtr_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_medios_transporte_mtr_pk_seq'::regclass), mtr_codigo character varying(45), mtr_habilitado boolean, mtr_nombre character varying(255), mtr_nombre_busqueda character varying(255), mtr_ult_mod_fecha timestamp without time zone, mtr_ult_mod_usuario character varying(45), mtr_version integer, CONSTRAINT sg_medios_transporte_pkey PRIMARY KEY (mtr_pk), CONSTRAINT mtr_codigo_uk UNIQUE (mtr_codigo), CONSTRAINT mtr_nombre_uk UNIQUE (mtr_nombre));
COMMENT ON TABLE catalogo.sg_medios_transporte IS 'Tabla para el registro de medios de transporte.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_version IS 'Versión del registro.';

--Estado Civil
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_estados_civil_eci_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_estados_civil(eci_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_estados_civil_eci_pk_seq'::regclass),eci_codigo CHARACTER varying(4),eci_habilitado BOOLEAN,eci_nombre CHARACTER varying(255),eci_nombre_busqueda CHARACTER varying(255),eci_ult_mod_fecha timestamp without TIME zone,eci_ult_mod_usuario CHARACTER varying(45),eci_version INTEGER,CONSTRAINT sg_estados_civil_pkey PRIMARY KEY (eci_pk),CONSTRAINT eci_codigo_uk UNIQUE (eci_codigo),CONSTRAINT eci_nombre_uk UNIQUE (eci_nombre));
COMMENT ON TABLE catalogo.sg_estados_civil IS 'Tabla para el registro de los estados civiles.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_codigo IS 'Código del estado civil.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_nombre IS 'Nombre del estado civil.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_nombre_busqueda IS 'Nombre del estado civil normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_habilitado IS 'Indica si el estado civil se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_civil.eci_version IS 'Versión del estado civil.';

CREATE SEQUENCE IF NOT EXISTS catalogo.sg_jornadas_laborales_jla_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_jornadas_laborales (jla_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_jornadas_laborales_jla_pk_seq'::regclass), jla_codigo character varying(45), jla_habilitado boolean, jla_nombre character varying(255), jla_nombre_busqueda character varying(255), jla_ult_mod_fecha timestamp without time zone, jla_ult_mod_usuario character varying(45), jla_version integer, CONSTRAINT sg_jornadas_laborales_pkey PRIMARY KEY (jla_pk), CONSTRAINT jla_codigo_uk UNIQUE (jla_codigo), CONSTRAINT jla_nombre_uk UNIQUE (jla_nombre));
COMMENT ON TABLE catalogo.sg_jornadas_laborales IS 'Tabla para el registro de turnos.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_version IS 'Versión del registro.';


-- ModalidadesAtencion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_modalidades_atencion_mat_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_modalidades_atencion (mat_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_modalidades_atencion_mat_pk_seq'::regclass), mat_codigo character varying(10), mat_nombre character varying(255), mat_nombre_busqueda character varying(255), mat_descripcion CHARACTER VARYING(500), mat_habilitado boolean,  mat_ult_mod_fecha timestamp without time zone, mat_ult_mod_usuario character varying(45), mat_version integer, CONSTRAINT sg_modalidades_atencion_pkey PRIMARY KEY (mat_pk), CONSTRAINT mat_codigo_uk UNIQUE (mat_codigo), CONSTRAINT mat_nombre_uk UNIQUE (mat_nombre));
    COMMENT ON TABLE  catalogo.sg_modalidades_atencion                          IS 'Tabla para el registro de las modalidades de atención.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_codigo               IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_nombre               IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_nombre_busqueda      IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_descripcion          IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_habilitado           IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_atencion.mat_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_modalidades_atencion_aud (mat_pk bigint NOT NULL, mat_codigo character varying(10), mat_nombre character varying(255), mat_nombre_busqueda character varying(255), mat_descripcion CHARACTER VARYING(500), mat_habilitado boolean,  mat_ult_mod_fecha timestamp without time zone, mat_ult_mod_usuario character varying(45), mat_version integer, rev bigint, revtype smallint);

-- SubModalidades
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_sub_modalidades_smo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_sub_modalidades (smo_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_sub_modalidades_smo_pk_seq'::regclass), smo_codigo character varying(10), smo_modalidad_fk bigint,smo_nombre character varying(255), smo_nombre_busqueda character varying(255), smo_descripcion CHARACTER VARYING(500), smo_habilitado boolean,  smo_ult_mod_fecha timestamp without time zone, smo_ult_mod_usuario character varying(45), smo_version integer, CONSTRAINT sg_sub_modalidades_pkey PRIMARY KEY (smo_pk), CONSTRAINT smo_codigo_uk UNIQUE (smo_codigo), CONSTRAINT smo_nombre_uk UNIQUE (smo_nombre),CONSTRAINT sg_sub_modalidades_mod_fkey FOREIGN KEY (smo_modalidad_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  catalogo.sg_sub_modalidades                          IS 'Tabla para el registro de las sub modalidades de atencion.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_codigo               IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_modalidad_fk         IS 'Clave foránea que referencia a la modalidad.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_nombre               IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_nombre_busqueda      IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_descripcion          IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_habilitado           IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sub_modalidades.smo_version              IS 'Versión del registro.';


-- Modalidad estudio
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_modalidades_estudio_mes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_modalidades_estudio (mes_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_modalidades_estudio_mes_pk_seq'::regclass), mes_codigo character varying(45), mes_habilitado boolean, mes_nombre character varying(255), mes_nombre_busqueda character varying(255), mes_ult_mod_fecha timestamp without time zone, mes_ult_mod_usuario character varying(45), mes_version integer, CONSTRAINT sg_modalidades_estudio_pkey PRIMARY KEY (mes_pk), CONSTRAINT mes_codigo_uk UNIQUE (mes_codigo), CONSTRAINT mes_nombre_uk UNIQUE (mes_nombre));
    COMMENT  ON TABLE catalogo.sg_modalidades_estudio IS 'Tabla para el registro de las modalidades de estudio.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modalidades_estudio.mes_version IS 'Versión del registro.';



-- Subsidios Preferenciales
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_subsidios_preferenciales_spr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_subsidios_preferenciales (spr_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_subsidios_preferenciales_spr_pk_seq'::regclass), spr_codigo character varying(45), spr_habilitado boolean, spr_nombre character varying(255), spr_nombre_busqueda character varying(255), spr_ult_mod_fecha timestamp without time zone, spr_ult_mod_usuario character varying(45), spr_version integer, CONSTRAINT sg_subsidios_preferenciales_pkey PRIMARY KEY (spr_pk), CONSTRAINT spr_codigo_uk UNIQUE (spr_codigo), CONSTRAINT spr_nombre_uk UNIQUE (spr_nombre));
COMMENT ON TABLE catalogo.sg_subsidios_preferenciales IS 'Tabla para el registro de subsidios preferenciales.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_subsidios_preferenciales.spr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_subsidios_preferenciales_aud (spr_pk bigint NOT NULL, spr_codigo character varying(45), spr_habilitado boolean, spr_nombre character varying(255), spr_nombre_busqueda character varying(255), spr_ult_mod_fecha timestamp without time zone, spr_ult_mod_usuario character varying(45), spr_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_subsidios_preferenciales_aud ADD PRIMARY KEY (spr_pk, rev);


--Tasa de homicidios
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tasa_homicidios_tah_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;



CREATE TABLE IF NOT EXISTS    catalogo.sg_tasa_homicidios(
	tah_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tasa_homicidios_tah_pk_seq'::regclass),
	tah_anio int not null,
	tah_fase int,
	tah_total_homicidios int,
	tah_proy_people int,
	tah_tasa_homicidio numeric(5,2),
	tah_municipio_fk bigint NOT NULL, 
	tah_pess BOOLEAN,
	tah_version INTEGER,
tah_ult_mod_fecha timestamp without time zone, tah_ult_mod_usuario character varying(45)
	CONSTRAINT sg_tasa_homicidio_pkey PRIMARY KEY (tah_pk),
	 CONSTRAINT sg_tah_municipio_fkey FOREIGN KEY (tah_municipio_fk) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
	 CONSTRAINT tah_anio_municipio_uk UNIQUE (tah_anio,tah_municipio_fk));

COMMENT ON TABLE catalogo.sg_tasa_homicidios IS 'Tabla para el registro de las estadísticas de homicios por cada municipio  de El Salvador.';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_anio IS 'Año al que pertenece la estadística';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_fase IS 'Fase del PEES';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_proy_people IS 'Proyeccion de población del municipio';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_tasa_homicidio IS 'Tasa de homicios por cada 100 mil habitantes';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_municipio_fk IS 'Clave foránea del municipio al que pertenece el cantón.';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_pess IS 'Indica si el municipio está cubierto por el plan el Salvador seguro.';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_version IS 'Versión  que se uso para el registro.';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_ult_mod_fecha IS 'Fecha en que se modificó por última vez';
COMMENT ON COLUMN catalogo.sg_tasa_homicidios.tah_ult_mod_usuario IS 'Usuario que ';


CREATE TABLE IF NOT EXISTS catalogo.sg_tasa_homicidios_aud (
	tah_pk bigint NOT NULL,
	tah_anio int not null,
	tah_fase int,
	tah_total_homicidios int,
	tah_proy_people int,
	tah_tasa_homicidio numeric(5,2),
	tah_municipio_fk bigint NOT NULL, 
	tah_pess BOOLEAN,
	tah_version INTEGER,
tah_ult_mod_fecha timestamp without time zone, tah_ult_mod_usuario character varying(45),
rev bigint, revtype smallint
);
ALTER TABLE catalogo.sg_subsidios_preferenciales_aud ADD PRIMARY KEY (tah_pk, rev);
