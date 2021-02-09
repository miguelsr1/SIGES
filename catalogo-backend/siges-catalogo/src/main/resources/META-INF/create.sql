CREATE SCHEMA IF NOT EXISTS catalogo;
CREATE SCHEMA IF NOT EXISTS auditoria;
CREATE SCHEMA IF NOT EXISTS public;

CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence INCREMENT 1 START 1;
CREATE TABLE IF NOT EXISTS public.REVINFO (rev bigint, revtstmp bigint, usuario CHARACTER varying(255), CONSTRAINT revinfo_pkey PRIMARY KEY (rev));
INSERT INTO public.REVINFO (rev, revtstmp) values (nextval('public.hibernate_sequence'::regclass), extract(epoch FROM CURRENT_TIMESTAMP));

-- Auditoría operaciones. Van en el esquema público.
CREATE SEQUENCE IF NOT EXISTS auditoria.sg_registros_auditoria_aud_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS auditoria.sg_registros_auditoria (aud_pk bigint NOT NULL DEFAULT nextval('auditoria.sg_registros_auditoria_aud_pk_seq'::regclass), aud_ip character varying(45), aud_clase character varying(255), aud_operacion character varying(255), aud_entidad_id bigint, aud_comentario character varying(1000), aud_resultado_operacion character varying(100), aud_fecha timestamp without time zone, aud_excepcion character varying(1000), aud_codigo_usuario character varying(45), aud_hash_md5 character varying(300), CONSTRAINT sg_registros_auditoria_pkey PRIMARY KEY (aud_pk));

-- Para cada entidad crear, secuencia, tabla, tabla de auditoría.

-- Tipos calendario escolar
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_calendario_escolar_tce_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_calendario_escolar (tce_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_calendario_escolar_tce_pk_seq'::regclass), tce_codigo character varying(4), tce_habilitado boolean, tce_nombre character varying(255), tce_nombre_busqueda character varying(255), tce_ult_mod_fecha timestamp without time zone, tce_ult_mod_usuario character varying(45), tce_version integer, CONSTRAINT sg_tipo_calendario_escolar_pkey PRIMARY KEY (tce_pk), CONSTRAINT tce_codigo_uk UNIQUE (tce_codigo), CONSTRAINT tce_nombre_uk UNIQUE (tce_nombre));
COMMENT ON TABLE catalogo.sg_tipos_calendario_escolar IS 'Tabla para el registro de los tipos de calendario escolar.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_codigo IS 'Código del tipo de calendario escolar.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_nombre IS 'Nombre del tipo de calendario escolar.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_nombre_busqueda IS 'Nombre del tipo de calendario escolar normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_habilitado IS 'Indica si el tipo de calendario escolar se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_version IS 'Versión del tipo de calendario escolar';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_calendario_escolar_aud (tce_pk bigint NOT NULL, tce_codigo character varying(4), tce_habilitado boolean, tce_nombre character varying(255), tce_nombre_busqueda character varying(255), tce_ult_mod_fecha timestamp without time zone, tce_ult_mod_usuario character varying(45), tce_version integer, rev bigint, revtype smallint);

-- TBL SIGES_PAISES
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_paises_pai_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_paises (pai_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_paises_pai_pk_seq'::regclass), pai_codigo character varying(4), pai_habilitado boolean, pai_nombre character varying(255), pai_nombre_busqueda character varying(255), pai_ult_mod_fecha timestamp without time zone, pai_ult_mod_usuario character varying(45), pai_version integer, CONSTRAINT sg_pais_pkey PRIMARY KEY (pai_pk), CONSTRAINT pai_codigo_uk UNIQUE (pai_codigo), CONSTRAINT pai_nombre_uk UNIQUE (pai_nombre));
CREATE TABLE IF NOT EXISTS catalogo.sg_paises_aud (pai_pk bigint NOT NULL, pai_codigo character varying(4), pai_habilitado boolean, pai_nombre character varying(255), pai_nombre_busqueda character varying(255), pai_ult_mod_fecha timestamp without time zone, pai_ult_mod_usuario character varying(45), pai_version integer, rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_paises IS 'Tabla para registrar los países.';
COMMENT ON COLUMN catalogo.sg_paises.pai_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_paises.pai_codigo IS 'Código de identificación del país (ISO 3166-1).';
COMMENT ON COLUMN catalogo.sg_paises.pai_nombre IS 'Nombre del país.';
COMMENT ON COLUMN catalogo.sg_paises.pai_nombre_busqueda IS 'Nombre del país normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_paises.pai_habilitado IS 'Indica si el país se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_paises.pai_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_paises.pai_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_paises.pai_version IS 'Versión del catalogo que se uso para el registo.';


-- TBL SIGES_ZONAS
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_zonas_zon_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_zonas (zon_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_zonas_zon_pk_seq'::regclass), zon_codigo character varying(4), zon_habilitado boolean, zon_nombre character varying(255), zon_nombre_busqueda character varying(255), zon_ult_mod_fecha timestamp without time zone, zon_ult_mod_usuario character varying(45), zon_version integer, CONSTRAINT sg_zona_pkey PRIMARY KEY (zon_pk), CONSTRAINT zon_codigo_uk UNIQUE (zon_codigo), CONSTRAINT zon_nombre_uk UNIQUE (zon_nombre));
CREATE TABLE IF NOT EXISTS catalogo.sg_zonas_aud (zon_pk bigint NOT NULL, zon_codigo character varying(4), zon_habilitado boolean, zon_nombre character varying(255), zon_nombre_busqueda character varying(255), zon_ult_mod_fecha timestamp without time zone, zon_ult_mod_usuario character varying(45), zon_version integer, rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_zonas IS 'Tabla para registrar el tipo de zona.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_codigo IS 'Código de identificación de zona.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_nombre IS 'Nombre del tipo de zona.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_nombre_busqueda IS 'Nombre del tipo de zona normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_habilitado IS 'Indica si la zona se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_zonas.zon_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_version IS 'Versión del catalogo que se uso para el registo.';

--Identificacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_identificacion_tin_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_identificacion(tin_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_identificacion_tin_pk_seq'::regclass),tin_codigo CHARACTER varying(4),tin_habilitado BOOLEAN,tin_nombre CHARACTER varying(255),tin_nombre_busqueda CHARACTER varying(255),tin_ult_mod_fecha timestamp without TIME zone,tin_ult_mod_usuario CHARACTER varying(45),tin_version INTEGER,CONSTRAINT sg_tipo_identificacion_pkey PRIMARY KEY (tin_pk),CONSTRAINT tin_codigo_uk UNIQUE (tin_codigo),CONSTRAINT tin_nombre_uk UNIQUE (tin_nombre));
COMMENT ON TABLE catalogo.sg_tipos_identificacion IS 'Tabla para el registro de los tipos de documentos de identificación de El Salvador.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_codigo IS 'Código del tipo de identificación.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_nombre IS 'Nombre del tipo de identificación.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_nombre_busqueda IS 'Nombre del tipo de identificación normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_habilitado IS 'Indica si el tipo de identificación se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_identificacion.tin_version IS 'Versión del tipo de identificación';
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_identificacion_aud(tin_pk bigint NOT NULL,tin_codigo CHARACTER varying(4),tin_habilitado BOOLEAN,tin_nombre CHARACTER varying(255),tin_nombre_busqueda CHARACTER varying(255),tin_ult_mod_fecha timestamp without TIME zone,tin_ult_mod_usuario CHARACTER varying(45),tin_version INTEGER,rev bigint, revtype smallint);

--Etnia
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_etnias_etn_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_etnias(etn_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_etnias_etn_pk_seq'::regclass),etn_codigo CHARACTER varying(4),etn_habilitado BOOLEAN,etn_nombre CHARACTER varying(255),etn_nombre_busqueda CHARACTER varying(255),etn_ult_mod_fecha timestamp without TIME zone,etn_ult_mod_usuario CHARACTER varying(45),etn_version INTEGER,CONSTRAINT sg_etnias_pkey PRIMARY KEY (etn_pk),CONSTRAINT etn_codigo_uk UNIQUE (etn_codigo),CONSTRAINT etn_nombre_uk UNIQUE (etn_nombre));
COMMENT ON TABLE catalogo.sg_etnias IS 'Tabla para el registro de las etnias.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_codigo IS 'Código de la etnia.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_nombre IS 'Nombre de la etnia.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_nombre_busqueda IS 'Nombre de la etnia normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_habilitado IS 'Indica si la etnia se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_etnias.etn_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_etnias.etn_version IS 'Versión de la etnia';
CREATE TABLE IF NOT EXISTS    catalogo.sg_etnias_aud(etn_pk bigint NOT NULL,etn_codigo CHARACTER varying(4),etn_habilitado BOOLEAN,etn_nombre CHARACTER varying(255),etn_nombre_busqueda CHARACTER varying(255),etn_ult_mod_fecha timestamp without TIME zone,etn_ult_mod_usuario CHARACTER varying(45),etn_version INTEGER,rev bigint, revtype smallint);

--Sexo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_sexos_sex_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_sexos(sex_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_sexos_sex_pk_seq'::regclass),sex_codigo CHARACTER varying(4),sex_habilitado BOOLEAN,sex_nombre CHARACTER varying(255),sex_nombre_busqueda CHARACTER varying(255),sex_ult_mod_fecha timestamp without TIME zone,sex_ult_mod_usuario CHARACTER varying(45),sex_version INTEGER,CONSTRAINT sg_sexo_pkey PRIMARY KEY (sex_pk),CONSTRAINT sex_codigo_uk UNIQUE (sex_codigo),CONSTRAINT sex_nombre_uk UNIQUE (sex_nombre));
COMMENT ON TABLE catalogo.sg_sexos IS 'Tabla para el registro de los sexos.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_codigo IS 'Código del sexo.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_nombre IS 'Nombre del sexo.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_nombre_busqueda IS 'Nombre del sexo normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_habilitado IS 'Indica si el sexo se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_sexos.sex_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_sexos.sex_version IS 'Versión del sexo.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_sexos_aud(sex_pk bigint NOT NULL,sex_codigo CHARACTER varying(4),sex_habilitado BOOLEAN,sex_nombre CHARACTER varying(255),sex_nombre_busqueda CHARACTER varying(255),sex_ult_mod_fecha timestamp without TIME zone,sex_ult_mod_usuario CHARACTER varying(45),sex_version INTEGER,rev bigint, revtype smallint);

-- Institucion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_instituciones_ins_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_instituciones (ins_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_instituciones_ins_pk_seq'::regclass), ins_codigo character varying(45), ins_habilitado boolean, ins_nombre character varying(255), ins_nombre_busqueda character varying(255), ins_ult_mod_fecha timestamp without time zone, ins_ult_mod_usuario character varying(45), ins_version integer, CONSTRAINT sg_institucion_pkey PRIMARY KEY (ins_pk), CONSTRAINT ins_codigo_uk UNIQUE (ins_codigo), CONSTRAINT ins_nombre_uk UNIQUE (ins_nombre));
CREATE TABLE IF NOT EXISTS catalogo.sg_instituciones_aud (ins_pk bigint NOT NULL, ins_codigo character varying(45), ins_habilitado boolean, ins_nombre character varying(255), ins_nombre_busqueda character varying(255), ins_ult_mod_fecha timestamp without time zone, ins_ult_mod_usuario character varying(45), ins_version integer, rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_instituciones IS 'Tabla para registrar el tipo de instituciones.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_codigo IS 'Código de identificación de tipo de institución.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_nombre IS 'Nombre del tipo de institución.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_nombre_busqueda IS 'Nombre del tipo de institución normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_habilitado IS 'Indica si la institución se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_instituciones.ins_version IS 'Versión de la institución.';

--Parentesco
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_parentesco_tpa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_parentesco(tpa_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_parentesco_tpa_pk_seq'::regclass),tpa_codigo CHARACTER varying(4),tpa_habilitado BOOLEAN,tpa_nombre CHARACTER varying(255),tpa_nombre_busqueda CHARACTER varying(255),tpa_ult_mod_fecha timestamp without TIME zone,tpa_ult_mod_usuario CHARACTER varying(45),tpa_version INTEGER,CONSTRAINT sg_tipo_parentesco_pkey PRIMARY KEY (tpa_pk),CONSTRAINT tpa_codigo_uk UNIQUE (tpa_codigo),CONSTRAINT tpa_nombre_uk UNIQUE (tpa_nombre));
COMMENT ON TABLE catalogo.sg_tipos_parentesco IS 'Tabla para el registro de tipos de parentesco.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_codigo IS 'Código del parentesco.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_nombre IS 'Nombre del parentesco.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_nombre_busqueda IS 'Nombre del parentesco normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_habilitado IS 'Indica si el parentesco se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_parentesco.tpa_version IS 'Versión del parentesco.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_parentesco_aud(tpa_pk bigint NOT NULL,tpa_codigo CHARACTER varying(4),tpa_habilitado BOOLEAN,tpa_nombre CHARACTER varying(255),tpa_nombre_busqueda CHARACTER varying(255),tpa_ult_mod_fecha timestamp without TIME zone,tpa_ult_mod_usuario CHARACTER varying(45),tpa_version INTEGER,rev bigint, revtype smallint);

--Telefono
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_telefono_tto_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_telefono(tto_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_telefono_tto_pk_seq'::regclass),tto_codigo CHARACTER varying(4),tto_habilitado BOOLEAN,tto_nombre CHARACTER varying(255),tto_nombre_busqueda CHARACTER varying(255),tto_ult_mod_fecha timestamp without TIME zone,tto_ult_mod_usuario CHARACTER varying(45),tto_version INTEGER,CONSTRAINT sg_tipo_telefono_pkey PRIMARY KEY (tto_pk),CONSTRAINT tto_codigo_uk UNIQUE (tto_codigo),CONSTRAINT tto_nombre_uk UNIQUE (tto_nombre));
COMMENT ON TABLE catalogo.sg_tipos_telefono IS 'Tabla para el registro de tipos de teléfono.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_codigo IS 'Código del teléfono.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_nombre IS 'Nombre del teléfono.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_nombre_busqueda IS 'Nombre del tipo de teléfono normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_habilitado IS 'Indica si el tipo de teléfono se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_version IS 'Versión del tipo de teléfono.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_telefono_aud(tto_pk bigint NOT NULL,tto_codigo CHARACTER varying(4),tto_habilitado BOOLEAN,tto_nombre CHARACTER varying(255),tto_nombre_busqueda CHARACTER varying(255),tto_ult_mod_fecha timestamp without TIME zone,tto_ult_mod_usuario CHARACTER varying(45),tto_version INTEGER, rev bigint, revtype smallint);

--Departamento
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_departamentos_dep_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_departamentos(dep_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_departamentos_dep_pk_seq'::regclass),dep_codigo CHARACTER varying(4),dep_habilitado BOOLEAN,dep_nombre CHARACTER varying(255),dep_nombre_busqueda CHARACTER varying(255),dep_ult_mod_fecha timestamp without TIME zone,dep_ult_mod_usuario CHARACTER varying(45),dep_version INTEGER,CONSTRAINT sg_departamento_pkey PRIMARY KEY (dep_pk),CONSTRAINT dep_codigo_uk UNIQUE (dep_codigo),CONSTRAINT dep_nombre_uk UNIQUE (dep_nombre));
CREATE TABLE IF NOT EXISTS    catalogo.sg_departamentos_aud(dep_pk bigint NOT NULL,dep_codigo CHARACTER varying(4),dep_habilitado BOOLEAN,dep_nombre CHARACTER varying(255),dep_nombre_busqueda CHARACTER varying(255),dep_ult_mod_fecha timestamp without TIME zone,dep_ult_mod_usuario CHARACTER varying(45),dep_version INTEGER,rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_departamentos IS 'Tabla para el registro de los departamentos de El Salvador.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_codigo IS 'Código de identificación del departamento.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_nombre IS 'Nombre con que del departamento.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_nombre_busqueda IS 'Nombre formateado para hacer la búsqueda.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_habilitado IS 'Indica si el departamento se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_version IS 'Versión del departamento.';

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
CREATE TABLE IF NOT EXISTS    catalogo.sg_estados_civil_aud(eci_pk bigint NOT NULL,eci_codigo CHARACTER varying(4),eci_habilitado BOOLEAN,eci_nombre CHARACTER varying(255),eci_nombre_busqueda CHARACTER varying(255),eci_ult_mod_fecha timestamp without TIME zone,eci_ult_mod_usuario CHARACTER varying(45),eci_version INTEGER, rev bigint, revtype smallint);

--Motivos de egreso
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_egreso_meg_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_egreso(meg_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_motivos_egreso_meg_pk_seq'::regclass),meg_codigo CHARACTER varying(4),meg_habilitado BOOLEAN,meg_nombre CHARACTER varying(255),meg_nombre_busqueda CHARACTER varying(255),meg_ult_mod_fecha timestamp without TIME zone,meg_ult_mod_usuario CHARACTER varying(45),meg_version INTEGER,CONSTRAINT sg_motivos_egreso_pkey PRIMARY KEY (meg_pk),CONSTRAINT meg_codigo_uk UNIQUE (meg_codigo),CONSTRAINT meg_nombre_uk UNIQUE (meg_nombre));
COMMENT ON TABLE catalogo.sg_motivos_egreso IS 'Tabla para el registro de los motivos de egreso.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_codigo IS 'Código del motivo de egreso.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_nombre IS 'Nombre del motivo de egreso.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_nombre_busqueda IS 'Nombre del motivo de egreso normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_habilitado IS 'Indica si el motivo de egreso se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_egreso.meg_version IS 'Versión del motivo de egreso.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_egreso_aud(meg_pk bigint NOT NULL,meg_codigo CHARACTER varying(4),meg_habilitado BOOLEAN,meg_nombre CHARACTER varying(255),meg_nombre_busqueda CHARACTER varying(255),meg_ult_mod_fecha timestamp without TIME zone,meg_ult_mod_usuario CHARACTER varying(45),meg_version INTEGER,rev bigint, revtype smallint);

--Jornada Laboral
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_jornadas_laborales_jla_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_jornadas_laborales(jla_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_jornadas_laborales_jla_pk_seq'::regclass),jla_codigo CHARACTER varying(4),jla_habilitado BOOLEAN,jla_nombre CHARACTER varying(255),jla_nombre_busqueda CHARACTER varying(255),jla_ult_mod_fecha timestamp without TIME zone,jla_ult_mod_usuario CHARACTER varying(45),jla_version INTEGER,CONSTRAINT sg_jornadas_laborales_pkey PRIMARY KEY (jla_pk),CONSTRAINT jla_codigo_uk UNIQUE (jla_codigo),CONSTRAINT jla_nombre_uk UNIQUE (jla_nombre));
COMMENT ON TABLE catalogo.sg_jornadas_laborales IS 'Tabla para el registro de las jornadas laborales.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_codigo IS 'Código de la jornada laboral.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_nombre IS 'Nombre de la jornada laboral.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_nombre_busqueda IS 'Nombre de la jornada laboral  normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_habilitado IS 'Indica si la jornada laboral se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_jornadas_laborales.jla_version IS 'Versión de la jornada laboral.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_jornadas_laborales_aud(jla_pk bigint NOT NULL,jla_codigo CHARACTER varying(4),jla_habilitado BOOLEAN,jla_nombre CHARACTER varying(255),jla_nombre_busqueda CHARACTER varying(255),jla_ult_mod_fecha timestamp without TIME zone,jla_ult_mod_usuario CHARACTER varying(45),jla_version INTEGER,rev bigint, revtype smallint);

--Municipio
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_municipios_mun_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_municipios(mun_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_municipios_mun_pk_seq'::regclass),mun_departamento_fk bigint NOT NULL, mun_codigo CHARACTER varying(4),mun_habilitado BOOLEAN,mun_nombre CHARACTER varying(255),mun_nombre_busqueda CHARACTER varying(255),mun_ult_mod_fecha timestamp without TIME zone,mun_ult_mod_usuario CHARACTER varying(45),mun_version INTEGER,CONSTRAINT sg_municipio_pkey PRIMARY KEY (mun_pk), CONSTRAINT sg_mun_departamento_fkey FOREIGN KEY (mun_departamento_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT mun_codigo_nombre_uk UNIQUE (mun_codigo,mun_nombre));
CREATE TABLE IF NOT EXISTS    catalogo.sg_municipios_aud(mun_pk bigint NOT NULL, mun_departamento_fk bigint NOT NULL,mun_codigo CHARACTER varying(4),mun_habilitado BOOLEAN,mun_nombre CHARACTER varying(255),mun_nombre_busqueda CHARACTER varying(255),mun_ult_mod_fecha timestamp without TIME zone,mun_ult_mod_usuario CHARACTER varying(45),mun_version INTEGER,rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_municipios IS 'Tabla para el registro de los municipios de El Salvador.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_codigo IS 'Código de identificación del municipio.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_departamento_fk IS 'Clave foránea del departamento al que pertenece el municipio.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_nombre IS 'Nombre del municipio.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_nombre_busqueda IS 'Nombre del municipio normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_habilitado IS 'Indica si el municipio se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_municipios.mun_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_version IS 'Versión del catalogo que se uso para el registo.';

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

--Caserio
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_caserios_cas_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_caserios(cas_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_caserios_cas_pk_seq'::regclass), cas_canton_fk bigint NOT NULL, cas_codigo CHARACTER varying(8),cas_habilitado BOOLEAN,cas_nombre CHARACTER varying(255),cas_nombre_busqueda CHARACTER varying(255),cas_ult_mod_fecha timestamp without TIME zone,cas_ult_mod_usuario CHARACTER varying(45),cas_version INTEGER,CONSTRAINT sg_caserio_pkey PRIMARY KEY (cas_pk), CONSTRAINT sg_cas_canton_fkey FOREIGN KEY (cas_canton_fk) REFERENCES catalogo.sg_cantones(can_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT cas_codigo_uk UNIQUE (cas_codigo),CONSTRAINT cas_nombre_uk UNIQUE (cas_nombre));
CREATE TABLE IF NOT EXISTS    catalogo.sg_caserios_aud(cas_pk bigint NOT NULL, cas_canton_fk bigint NOT NULL, cas_codigo CHARACTER varying(8),cas_habilitado BOOLEAN,cas_nombre CHARACTER varying(255),cas_nombre_busqueda CHARACTER varying(255),cas_ult_mod_fecha timestamp without TIME zone,cas_ult_mod_usuario CHARACTER varying(45),cas_version INTEGER,rev bigint, revtype smallint);

COMMENT ON TABLE catalogo.sg_caserios IS 'Tabla para el registro de los caseríos de El Salvador.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_codigo IS 'Código de identificación del caserio.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_canton_fk IS 'Clave del cantón al que pertenece el caserío.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_nombre IS 'Nombre del caserio.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_nombre_busqueda IS 'Nombre del caserío normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_habilitado IS 'Indica si el caserío se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_caserios.cas_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_version IS 'Versión del catalogo que se uso para el registo.';

--Tipo de sangre
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_sangre_tsa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_sangre(tsa_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_sangre_tsa_pk_seq'::regclass), tsa_codigo CHARACTER varying(4), tsa_nombre CHARACTER varying(20),tsa_nombre_busqueda CHARACTER varying(20), tsa_habilitado BOOLEAN, tsa_ult_mod_fecha timestamp without TIME zone, tsa_ult_mod_usuario CHARACTER varying(45), tsa_version INTEGER, CONSTRAINT sg_tipos_sangre_pkey PRIMARY KEY (tsa_pk), CONSTRAINT tsa_codigo_uk UNIQUE (tsa_codigo), CONSTRAINT tsa_nombre_uk UNIQUE (tsa_nombre));
COMMENT ON TABLE catalogo.sg_tipos_sangre IS 'Tabla para el registro de los tipos de sangre.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_nombre_busqueda             IS 'Nombre del tipo de sangre normalizado para búsquedas..';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_sangre.tsa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_sangre_aud(tsa_pk bigint NOT NULL, tsa_codigo CHARACTER varying(4), tsa_nombre CHARACTER varying(255),tsa_nombre_busqueda CHARACTER varying(20), tsa_habilitado BOOLEAN, tsa_ult_mod_fecha timestamp without TIME zone, tsa_ult_mod_usuario CHARACTER varying(45), tsa_version INTEGER, rev bigint, revtype smallint);

--Mensajes
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_mensajes_msj_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_mensajes(msj_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_mensajes_msj_pk_seq'::regclass), msj_codigo CHARACTER varying(4), msj_descripcion CHARACTER varying(255),msj_valor CHARACTER varying(500), msj_habilitado BOOLEAN, msj_ult_mod_fecha timestamp without TIME zone, msj_ult_mod_usuario CHARACTER varying(45), msj_version INTEGER,CONSTRAINT sg_mensajes_pkey PRIMARY KEY (msj_pk),CONSTRAINT msj_codigo_uk UNIQUE (msj_codigo));
COMMENT ON TABLE catalogo.sg_mensajes IS 'Tabla para el registro de los mensajes.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_codigo IS 'Código del mensaje.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_descripcion IS 'Descripción del mensaje.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_valor IS 'Texto en HTML que almacena el mensaje.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_habilitado IS 'Indica si el mensaje se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_mensajes.msj_version IS 'Versión del mensaje.';
CREATE TABLE IF NOT EXISTS catalogo.sg_mensajes_aud(msj_pk bigint NOT NULL,msj_codigo CHARACTER varying(4), msj_descripcion CHARACTER varying(255),msj_valor CHARACTER varying(500), msj_habilitado BOOLEAN, msj_ult_mod_fecha timestamp without TIME zone, msj_ult_mod_usuario CHARACTER varying(45), msj_version INTEGER,rev bigint, revtype smallint);

--Etiqueta
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_etiquetas_eti_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_etiquetas(eti_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_etiquetas_eti_pk_seq'::regclass),eti_codigo CHARACTER varying(45), eti_valor CHARACTER varying(255), eti_habilitado BOOLEAN, eti_ult_mod_fecha timestamp without TIME zone, eti_ult_mod_usuario CHARACTER varying(45), eti_version INTEGER, CONSTRAINT sg_etiquetas_pkey PRIMARY KEY (eti_pk),CONSTRAINT eti_codigo_uk UNIQUE (eti_codigo));
COMMENT ON TABLE catalogo.sg_etiquetas IS 'Tabla para el registro de las etiquetas.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_codigo IS 'Código de la etiqueta.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_valor IS 'Valor de la etiqueta.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_habilitado IS 'Indica si la etiqueta se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_etiquetas.eti_version IS 'Versión de la etiqueta.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_etiquetas_aud(eti_pk bigint NOT NULL,eti_codigo CHARACTER varying(45), eti_valor CHARACTER varying(255),eti_habilitado BOOLEAN,eti_ult_mod_fecha timestamp without TIME zone,eti_ult_mod_usuario CHARACTER varying(45),eti_version INTEGER,rev bigint, revtype smallint);

--Medios de transporte
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_medios_transporte_mtr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_medios_transporte(mtr_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_medios_transporte_mtr_pk_seq'::regclass), mtr_codigo CHARACTER varying(4), mtr_nombre CHARACTER varying(255), mtr_nombre_busqueda CHARACTER VARYING(255), mtr_habilitado BOOLEAN, mtr_ult_mod_fecha timestamp without TIME zone, mtr_ult_mod_usuario CHARACTER varying(45), mtr_version INTEGER, CONSTRAINT sg_medios_transporte_pkey PRIMARY KEY (mtr_pk), CONSTRAINT mtr_codigo_uk UNIQUE (mtr_codigo), CONSTRAINT mtr_nombre_uk UNIQUE (mtr_nombre));
COMMENT ON  TABLE catalogo.sg_medios_transporte IS 'Tabla para el registro de los medios de transporte.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_medios_transporte.mtr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_medios_transporte_aud(mtr_pk bigint NOT NULL, mtr_codigo CHARACTER varying(4), mtr_nombre CHARACTER varying(255),mtr_nombre_busqueda CHARACTER VARYING(255), mtr_habilitado BOOLEAN, mtr_ult_mod_fecha timestamp without TIME zone, mtr_ult_mod_usuario CHARACTER varying(45), mtr_version INTEGER, rev bigint, revtype smallint);

--Discapacidad
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_discapacidades_dis_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_discapacidades(dis_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_discapacidades_dis_pk_seq'::regclass),dis_codigo CHARACTER varying(4), dis_nombre CHARACTER varying(255), dis_nombre_busqueda CHARACTER VARYING(255), dis_descripcion CHARACTER varying(255), dis_habilitado BOOLEAN, dis_ult_mod_fecha timestamp without TIME zone, dis_ult_mod_usuario CHARACTER varying(45), dis_version INTEGER, CONSTRAINT sg_discapacidades_pkey PRIMARY KEY (dis_pk),CONSTRAINT dis_codigo_uk UNIQUE (dis_codigo));
COMMENT ON TABLE  catalogo.sg_discapacidades IS 'Tabla para el registro de las discapacidades.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_descripcion IS 'Descripción del registro.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_discapacidades_aud(dis_pk bigint NOT NULL,dis_codigo CHARACTER varying(4),dis_nombre CHARACTER varying(255),dis_nombre_busqueda CHARACTER VARYING(255),dis_descripcion CHARACTER varying(255),dis_habilitado BOOLEAN,dis_ult_mod_fecha timestamp without TIME zone,dis_ult_mod_usuario CHARACTER varying(45),dis_version INTEGER,rev bigint, revtype smallint);

--Dependencia Economica
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_dependencias_economica_dec_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_dependencias_economica(dec_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_dependencias_economica_dec_pk_seq'::regclass),dec_codigo CHARACTER varying(4), dec_nombre CHARACTER varying(255), dec_nombre_busqueda CHARACTER VARYING(255), dec_descripcion CHARACTER varying(255), dec_habilitado BOOLEAN, dec_ult_mod_fecha timestamp without TIME zone, dec_ult_mod_usuario CHARACTER varying(45), dec_version INTEGER, CONSTRAINT sg_dependencias_economica_pkey PRIMARY KEY (dec_pk),CONSTRAINT dec_codigo_uk UNIQUE (dec_codigo));
COMMENT ON TABLE  catalogo.sg_dependencias_economica IS 'Tabla para el registro de las dependencias economicas';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_descripcion IS 'Descripción del registro.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_dependencias_economica.dec_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_dependencias_economica_aud(dec_pk bigint NOT NULL,dec_codigo CHARACTER varying(4),dec_nombre CHARACTER varying(255),dec_nombre_busqueda CHARACTER VARYING(255),dec_descripcion CHARACTER varying(255),dec_habilitado BOOLEAN,dec_ult_mod_fecha timestamp without TIME zone,dec_ult_mod_usuario CHARACTER varying(45),dec_version INTEGER,rev bigint, revtype smallint);

--Tipo Trabajo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_trabajo_ttr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_trabajo(ttr_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_trabajo_ttr_pk_seq'::regclass),ttr_codigo CHARACTER varying(4), ttr_nombre CHARACTER varying(255),ttr_nombre_busqueda CHARACTER varying(255), ttr_habilitado BOOLEAN, ttr_ult_mod_fecha timestamp without TIME zone, ttr_ult_mod_usuario CHARACTER varying(45), ttr_version INTEGER, CONSTRAINT sg_tipo_trabajo_pkey PRIMARY KEY (ttr_pk),CONSTRAINT ttr_codigo_uk UNIQUE (ttr_codigo));
COMMENT ON TABLE  catalogo.sg_tipos_trabajo IS 'Tabla para el registro del tipo de trabajo.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_nombre IS 'Página del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_nombre_busqueda             IS 'Nombre del tipo de trabajo normalizado para búsquedas..';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_trabajo.ttr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_trabajo_aud(ttr_pk bigint NOT NULL,ttr_codigo CHARACTER varying(4),ttr_nombre CHARACTER varying(255),ttr_nombre_busqueda CHARACTER varying(255),ttr_habilitado BOOLEAN,ttr_ult_mod_fecha timestamp without TIME zone,ttr_ult_mod_usuario CHARACTER varying(45),ttr_version INTEGER,rev bigint, revtype smallint);

--Escolaridad
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_escolaridades_esc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_escolaridades(esc_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_escolaridades_esc_pk_seq'::regclass),esc_codigo CHARACTER varying(4), esc_nombre CHARACTER varying(255), esc_nombre_busqueda CHARACTER VARYING(255), esc_habilitado BOOLEAN, esc_ult_mod_fecha timestamp without TIME zone, esc_ult_mod_usuario CHARACTER varying(45), esc_version INTEGER, CONSTRAINT sg_escolaridades_pkey PRIMARY KEY (esc_pk),CONSTRAINT esc_codigo_uk UNIQUE (esc_codigo));
COMMENT ON TABLE catalogo.sg_escolaridades IS 'Tabla para el registro de la escolaridad.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_escolaridades.esc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_escolaridades_aud(esc_pk bigint NOT NULL,esc_codigo CHARACTER varying(4),esc_nombre CHARACTER varying(255),esc_nombre_busqueda CHARACTER VARYING(255),esc_habilitado BOOLEAN,esc_ult_mod_fecha timestamp without TIME zone,esc_ult_mod_usuario CHARACTER varying(45),esc_version INTEGER,rev bigint, revtype smallint);


CREATE SEQUENCE IF NOT EXISTS catalogo.sg_profesiones_pro_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_profesiones(pro_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_profesiones_pro_pk_seq'::regclass), pro_codigo CHARACTER varying(4), pro_nombre CHARACTER varying(255), pro_nombre_busqueda CHARACTER VARYING(255), pro_habilitado BOOLEAN, pro_ult_mod_fecha timestamp without TIME zone, pro_ult_mod_usuario CHARACTER varying(45), pro_version INTEGER, CONSTRAINT sg_profesiones_pkey PRIMARY KEY (pro_pk),CONSTRAINT pro_codigo_uk UNIQUE (pro_codigo));
COMMENT ON TABLE  catalogo.sg_profesiones                        IS 'Tabla para el registro de las profesiones.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_pk                 IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_codigo             IS 'Código de la profesión.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_nombre             IS 'Nombre de la profesión.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_nombre_busqueda    IS 'Nombre de la profesión normalizado para busquedas.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_habilitado         IS 'Indica si la profesión se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_ult_mod_fecha      IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_ult_mod_usuario    IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_version            IS 'Versión de la profesión.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_profesiones_aud(pro_pk bigint NOT NULL, pro_codigo CHARACTER varying(4), pro_nombre CHARACTER varying(255), pro_nombre_busqueda CHARACTER VARYING(255), pro_habilitado BOOLEAN, pro_ult_mod_fecha timestamp without TIME zone, pro_ult_mod_usuario CHARACTER varying(45), pro_version INTEGER, rev bigint, revtype smallint);

--Motivo Retiro
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_retiro_mre_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_retiro(mre_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_motivos_retiro_mre_pk_seq'::regclass), mre_codigo CHARACTER varying(4), mre_nombre CHARACTER varying(255), mre_nombre_busqueda CHARACTER varying(255),mre_habilitado BOOLEAN, mre_ult_mod_fecha timestamp without TIME zone, mre_ult_mod_usuario CHARACTER varying(45), mre_version INTEGER, CONSTRAINT sg_motivos_retiro_pkey PRIMARY KEY (mre_pk),CONSTRAINT mre_codigo_uk UNIQUE (mre_codigo));
COMMENT ON TABLE  catalogo.sg_motivos_retiro                                 IS 'Tabla para el registro de los motivos de retiro.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_pk                          IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_codigo                      IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_nombre                      IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_nombre_busqueda             IS 'Nombre del motivo de retiro normalizado para búsquedas..';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_habilitado                  IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_ult_mod_fecha               IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_ult_mod_usuario             IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_version                     IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_retiro_aud(mre_pk bigint NOT NULL, mre_codigo CHARACTER varying(4), mre_nombre CHARACTER varying(255), mre_nombre_busqueda CHARACTER varying(255),mre_habilitado BOOLEAN, mre_ult_mod_fecha timestamp without TIME zone, mre_ult_mod_usuario CHARACTER varying(45), mre_version INTEGER, rev bigint, revtype smallint);

--Motivo Inasistencia
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_inasistencia_min_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_inasistencia(min_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_motivos_inasistencia_min_pk_seq'::regclass), min_codigo CHARACTER varying(4), min_nombre CHARACTER varying(255), min_nombre_busqueda CHARACTER VARYING(255), min_habilitado BOOLEAN, min_ult_mod_fecha timestamp without TIME zone, min_ult_mod_usuario CHARACTER varying(45), min_version INTEGER, CONSTRAINT sg_motivos_inasistencia_pkey PRIMARY KEY (min_pk),CONSTRAINT min_codigo_uk UNIQUE (min_codigo));
COMMENT ON TABLE  catalogo.sg_motivos_inasistencia                           IS 'Tabla para el registro de los motivos de inasistencia.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_pk                    IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_codigo                IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_nombre                IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_nombre_busqueda       IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_habilitado            IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_version               IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_inasistencia_aud(min_pk bigint NOT NULL, min_codigo CHARACTER varying(4), min_nombre CHARACTER varying(255), min_nombre_busqueda CHARACTER VARYING(255),min_habilitado BOOLEAN, min_ult_mod_fecha timestamp without TIME zone, min_ult_mod_usuario CHARACTER varying(45), min_version INTEGER, rev bigint, revtype smallint);

-- Tipos de Manifestación
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_manifestacion_tma_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_manifestacion (tma_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_manifestacion_tma_pk_seq'::regclass), tma_codigo character varying(45), tma_habilitado boolean, tma_nombre character varying(255), tma_nombre_busqueda character varying(255), tma_ult_mod_fecha timestamp without time zone, tma_ult_mod_usuario character varying(45), tma_version integer, CONSTRAINT sg_tipos_manifestacion_pkey PRIMARY KEY (tma_pk), CONSTRAINT tma_codigo_uk UNIQUE (tma_codigo), CONSTRAINT tma_nombre_uk UNIQUE (tma_nombre));
COMMENT ON TABLE catalogo.sg_tipos_manifestacion IS 'Tabla para el registro de tipos de manifestación.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_manifestacion_aud (tma_pk bigint NOT NULL, tma_codigo character varying(4), tma_habilitado boolean, tma_nombre character varying(255), tma_nombre_busqueda character varying(255), tma_ult_mod_fecha timestamp without time zone, tma_ult_mod_usuario character varying(45), tma_version integer, rev bigint, revtype smallint);

-- Tipos de Valoración
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_valoracion_tva_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_valoracion (tva_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_valoracion_tva_pk_seq'::regclass), tva_codigo character varying(45), tva_habilitado boolean, tva_nombre character varying(255), tva_nombre_busqueda character varying(255), tva_ult_mod_fecha timestamp without time zone, tva_ult_mod_usuario character varying(45), tva_version integer, CONSTRAINT sg_tipos_valoracion_pkey PRIMARY KEY (tva_pk), CONSTRAINT tva_codigo_uk UNIQUE (tva_codigo), CONSTRAINT tva_nombre_uk UNIQUE (tva_nombre));
COMMENT ON TABLE catalogo.sg_tipos_valoracion IS 'Tabla para el registro de tipos de valoración.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_valoracion.tva_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_valoracion_aud (tva_pk bigint NOT NULL, tva_codigo character varying(4), tva_habilitado boolean, tva_nombre character varying(255), tva_nombre_busqueda character varying(255), tva_ult_mod_fecha timestamp without time zone, tva_ult_mod_usuario character varying(45), tva_version integer, rev bigint, revtype smallint);

-- Ocupaciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_ocupaciones_ocu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_ocupaciones (ocu_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_ocupaciones_ocu_pk_seq'::regclass), ocu_codigo character varying(45), ocu_habilitado boolean, ocu_nombre character varying(255), ocu_nombre_busqueda character varying(255), ocu_ult_mod_fecha timestamp without time zone, ocu_ult_mod_usuario character varying(45), ocu_version integer, CONSTRAINT sg_ocupaciones_pkey PRIMARY KEY (ocu_pk), CONSTRAINT ocu_codigo_uk UNIQUE (ocu_codigo), CONSTRAINT ocu_nombre_uk UNIQUE (ocu_nombre));
COMMENT ON TABLE catalogo.sg_ocupaciones IS 'Tabla para el registro de ocupaciones.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ocupaciones.ocu_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_ocupaciones_aud (ocu_pk bigint NOT NULL, ocu_codigo character varying(4), ocu_habilitado boolean, ocu_nombre character varying(255), ocu_nombre_busqueda character varying(255), ocu_ult_mod_fecha timestamp without time zone, ocu_ult_mod_usuario character varying(45), ocu_version integer, rev bigint, revtype smallint);

-- TiposVacuna
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_vacuna_tvc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_vacuna (tvc_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_vacuna_tvc_pk_seq'::regclass), tvc_codigo character varying(45), tvc_habilitado boolean, tvc_nombre character varying(255), tvc_nombre_busqueda character varying(255), tvc_ult_mod_fecha timestamp without time zone, tvc_ult_mod_usuario character varying(45), tvc_version integer, CONSTRAINT sg_tipos_vacuna_pkey PRIMARY KEY (tvc_pk), CONSTRAINT tvc_codigo_uk UNIQUE (tvc_codigo), CONSTRAINT tvc_nombre_uk UNIQUE (tvc_nombre));
COMMENT ON TABLE catalogo.sg_tipos_vacuna IS 'Tabla para el registro de tipos de vacuna.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_vacuna.tvc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_vacuna_aud (tvc_pk bigint NOT NULL, tvc_codigo character varying(4), tvc_habilitado boolean, tvc_nombre character varying(255), tvc_nombre_busqueda character varying(255), tvc_ult_mod_fecha timestamp without time zone, tvc_ult_mod_usuario character varying(45), tvc_version integer, rev bigint, revtype smallint);

-- EnfermedadesNoTransmisible
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_enfer_no_transm_ent_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_enfer_no_transm (ent_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_enfer_no_transm_ent_pk_seq'::regclass), ent_codigo character varying(45), ent_habilitado boolean, ent_nombre character varying(255), ent_nombre_busqueda character varying(255), ent_ult_mod_fecha timestamp without time zone, ent_ult_mod_usuario character varying(45), ent_version integer, CONSTRAINT sg_enfermedades_no_transmisible_pkey PRIMARY KEY (ent_pk), CONSTRAINT ent_codigo_uk UNIQUE (ent_codigo), CONSTRAINT ent_nombre_uk UNIQUE (ent_nombre));
COMMENT ON TABLE  catalogo.sg_enfer_no_transm IS 'Tabla para el registro de las enfermedades no transmisibles.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_enfer_no_transm.ent_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_enfer_no_transm_aud (ent_pk bigint NOT NULL, ent_codigo character varying(4), ent_habilitado boolean, ent_nombre character varying(255), ent_nombre_busqueda character varying(255), ent_ult_mod_fecha timestamp without time zone, ent_ult_mod_usuario character varying(45), ent_version integer, rev bigint, revtype smallint);

-- Ayudas
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_ayudas_ayu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_ayudas (ayu_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_ayudas_ayu_pk_seq'::regclass), ayu_codigo character varying(45), ayu_texto_uso CHARACTER VARYING(4000), ayu_texto_normativa CHARACTER VARYING(4000), ayu_vinculos CHARACTER VARYING(4000), ayu_habilitado boolean, ayu_nombre character varying(255), ayu_nombre_busqueda character varying(255), ayu_ult_mod_fecha timestamp without time zone, ayu_ult_mod_usuario character varying(45), ayu_version integer, CONSTRAINT sg_ayudas_pkey PRIMARY KEY (ayu_pk), CONSTRAINT ayu_codigo_uk UNIQUE (ayu_codigo), CONSTRAINT ayu_nombre_uk UNIQUE (ayu_nombre));
COMMENT ON TABLE catalogo.sg_ayudas IS 'Tabla para el registro de ayudas.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_texto_uso IS 'Texto uso del registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_texto_normativa IS 'Texto normativa del registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_vinculos IS 'Vinculos del registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ayudas.ayu_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_ayudas_aud (ayu_pk bigint NOT NULL, ayu_codigo character varying(45), ayu_texto_uso CHARACTER VARYING(4000), ayu_texto_normativa CHARACTER VARYING(4000), ayu_vinculos CHARACTER VARYING(4000), ayu_habilitado boolean, ayu_nombre character varying(255), ayu_nombre_busqueda character varying(255), ayu_ult_mod_fecha timestamp without time zone, ayu_ult_mod_usuario character varying(45), ayu_version integer, rev bigint, revtype smallint);

-- EscalasCalificacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_escalas_calificacion_eca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_escalas_calificacion (eca_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_escalas_calificacion_eca_pk_seq'::regclass), eca_codigo character varying(45), eca_habilitado boolean, eca_nombre character varying(255), eca_nombre_busqueda character varying(255),eca_tipo_escala CHARACTER VARYING(50), eca_valor_minimo bigint, eca_ult_mod_fecha timestamp without time zone, eca_ult_mod_usuario character varying(45), eca_version integer, CONSTRAINT sg_escalas_calificacion_pkey PRIMARY KEY (eca_pk), CONSTRAINT eca_codigo_uk UNIQUE (eca_codigo), CONSTRAINT eca_nombre_uk UNIQUE (eca_nombre));
    COMMENT ON TABLE  catalogo.sg_escalas_calificacion                          IS 'Tabla para el registro de escalas de calificación.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_codigo               IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_nombre               IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_nombre_busqueda      IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_habilitado           IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_tipo_escala          IS 'Tipo de escala de calificacion, toma los valores NUMÉRICA o CONCEPTUAL.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_valor_minimo         IS 'Valor mínimo de aprobación de la escala de califiación.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_escalas_calificacion_aud (eca_pk bigint NOT NULL, eca_codigo character varying(45), eca_habilitado boolean, eca_nombre character varying(255), eca_nombre_busqueda character varying(255),eca_tipo_escala CHARACTER VARYING(50), eca_valor_minimo bigint, eca_ult_mod_fecha timestamp without time zone, eca_ult_mod_usuario character varying(45), eca_version integer, rev bigint, revtype smallint);

-- Calificacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_calificaciones_cal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_calificaciones (cal_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_calificaciones_cal_pk_seq'::regclass), cal_codigo CHARACTER VARYING(45), cal_escala_fk bigint, cal_valor CHARACTER VARYING(25), cal_orden int,cal_habilitado boolean,cal_ult_mod_fecha timestamp without time zone, cal_ult_mod_usuario character varying(45), cal_version integer, CONSTRAINT sg_calificaciones_pkey PRIMARY KEY (cal_pk), CONSTRAINT sg_calificaciones_escalas_calificacion_fkey FOREIGN KEY (cal_escala_fk) REFERENCES catalogo.sg_escalas_calificacion(eca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  catalogo.sg_calificaciones                          IS 'Tabla para el registro de calificaciones.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_codigo               IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_escala_fk               IS 'Clave foranea que referencia a la escala a la que pertenece la calificación.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_valor                IS 'Valor del registro.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_orden                IS 'Orden del registro.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_habilitado           IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_calificaciones.cal_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_calificaciones_aud (cal_pk bigint NOT NULL,cal_codigo CHARACTER VARYING(45), cal_escala_fk bigint, cal_valor CHARACTER VARYING(25), cal_orden int,cal_habilitado boolean,cal_ult_mod_fecha timestamp without time zone, cal_ult_mod_usuario character varying(45), cal_version integer, rev bigint, revtype smallint);

ALTER TABLE catalogo.sg_escalas_calificacion ADD CONSTRAINT sg_escalas_calificaciones_fkey FOREIGN KEY (eca_valor_minimo) REFERENCES catalogo.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- SectorEconomico
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_sectores_economicos_sec_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_sectores_economicos (sec_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_sectores_economicos_sec_pk_seq'::regclass), sec_codigo character varying(45), sec_habilitado boolean, sec_nombre character varying(255), sec_nombre_busqueda character varying(255), sec_ult_mod_fecha timestamp without time zone, sec_ult_mod_usuario character varying(45), sec_version integer, CONSTRAINT sg_sectores_economicos_pkey PRIMARY KEY (sec_pk), CONSTRAINT sec_codigo_uk UNIQUE (sec_codigo), CONSTRAINT sec_nombre_uk UNIQUE (sec_nombre));
    COMMENT ON TABLE  catalogo.sg_sectores_economicos IS 'Tabla para el registro de sector económico.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sectores_economicos.sec_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_sectores_economicos_aud (sec_pk bigint NOT NULL, sec_codigo character varying(4), sec_habilitado boolean, sec_nombre character varying(255), sec_nombre_busqueda character varying(255), sec_ult_mod_fecha timestamp without time zone, sec_ult_mod_usuario character varying(45), sec_version integer, rev bigint, revtype smallint);

-- ProgramaEducativo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_programas_educativos_ped_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_programas_educativos (ped_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_programas_educativos_ped_pk_seq'::regclass), ped_codigo character varying(10), ped_habilitado boolean, ped_nombre character varying(255), ped_nombre_busqueda character varying(255), ped_descripcion character varying(500), ped_ult_mod_fecha timestamp without time zone, ped_ult_mod_usuario character varying(45), ped_version integer, CONSTRAINT sg_programas_pkey PRIMARY KEY (ped_pk), CONSTRAINT ped_codigo_uk UNIQUE (ped_codigo), CONSTRAINT ped_nombre_uk UNIQUE (ped_nombre));
    COMMENT ON TABLE  catalogo.sg_programas_educativos IS 'Tabla para el registro de programas educativos.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_programas_educativos.ped_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_programas_educativos_aud (ped_pk bigint NOT NULL, ped_codigo character varying(10), ped_habilitado boolean, ped_nombre character varying(255), ped_nombre_busqueda character varying(255), ped_descripcion character varying(500), ped_ult_mod_fecha timestamp without time zone, ped_ult_mod_usuario character varying(45), ped_version integer, rev bigint, revtype smallint);

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
CREATE TABLE IF NOT EXISTS catalogo.sg_sub_modalidades_aud (smo_pk bigint NOT NULL, smo_codigo character varying(10), smo_modalidad_fk bigint,smo_nombre character varying(255), smo_nombre_busqueda character varying(255), smo_descripcion CHARACTER VARYING(500), smo_habilitado boolean,  smo_ult_mod_fecha timestamp without time zone, smo_ult_mod_usuario character varying(45), smo_version integer, rev bigint, revtype smallint);


-- Clasificaciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_clasificaciones_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_clasificaciones (cla_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_clasificaciones_pk_seq'::regclass),cla_codigo character varying(10), cla_nombre character varying(255), cla_nombre_busqueda character varying(255),  cla_habilitado boolean, cla_ult_mod_fecha timestamp without time zone, cla_ult_mod_usuario character varying(45), cla_version integer, CONSTRAINT sg_clasificaciones_pkey PRIMARY KEY (cla_pk), CONSTRAINT cla_codigo_uk UNIQUE (cla_codigo), CONSTRAINT cla_nombre_uk UNIQUE (cla_nombre));
    COMMENT ON TABLE  catalogo.sg_clasificaciones                          IS 'Tabla para el registro de clasificaciones.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_codigo               IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_nombre               IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_nombre_busqueda      IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_habilitado           IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_clasificaciones.cla_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_clasificaciones_aud (cla_pk bigint NOT NULL, cla_codigo character varying(10), cla_modalidad_fk bigint,cla_nombre character varying(255), cla_nombre_busqueda character varying(255),  cla_habilitado boolean,  cla_ult_mod_fecha timestamp without time zone, cla_ult_mod_usuario character varying(45), cla_version integer, rev bigint, revtype smallint);


-- Configuraciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_configuraciones_con_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_configuraciones (con_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_configuraciones_con_pk_seq'::regclass), con_codigo character varying(45), con_habilitado boolean, con_nombre character varying(255), con_nombre_busqueda character varying(255), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer, CONSTRAINT sg_configuraciones_pkey PRIMARY KEY (con_pk), CONSTRAINT con_codigo_uk UNIQUE (con_codigo), CONSTRAINT con_nombre_uk UNIQUE (con_nombre));
COMMENT ON TABLE catalogo.sg_configuraciones IS 'Tabla para el registro de configuraciones.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones.con_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_configuraciones_aud (con_pk bigint NOT NULL, con_codigo character varying(45), con_habilitado boolean, con_nombre character varying(255), con_nombre_busqueda character varying(255), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer, rev bigint, revtype smallint);


-- AFP
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_afp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_afp (afp_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_afp_pk_seq'::regclass), afp_codigo character varying(45), afp_nombre character varying(255), afp_nombre_busqueda character varying(255), afp_habilitado boolean, afp_ult_mod_fecha timestamp without time zone, afp_ult_mod_usuario character varying(45), afp_version integer, CONSTRAINT sg_afp_pkey PRIMARY KEY (afp_pk), CONSTRAINT afp_codigo_uk UNIQUE (afp_codigo), CONSTRAINT afp_nombre_uk UNIQUE (afp_nombre));
    COMMENT  ON TABLE catalogo.sg_afp IS 'Tabla para el registro de AFP.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_afp.afp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_afp.afp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_afp_aud (afp_pk bigint NOT NULL, afp_codigo character varying(45), afp_nombre character varying(255), afp_nombre_busqueda character varying(255), afp_habilitado boolean, afp_ult_mod_fecha timestamp without time zone, afp_ult_mod_usuario character varying(45), afp_version integer, rev bigint, revtype smallint);

-- NIVEL ESCALAFON
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_nivel_escalafon_nes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_nivel_escalafon (nes_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_nivel_escalafon_nes_pk_seq'::regclass), nes_codigo character varying(45), nes_nombre character varying(255), nes_nombre_busqueda character varying(255), nes_habilitado boolean, nes_ult_mod_fecha timestamp without time zone, nes_ult_mod_usuario character varying(45), nes_version integer, CONSTRAINT sg_nivel_escalafon_pkey PRIMARY KEY (nes_pk), CONSTRAINT nes_codigo_uk UNIQUE (nes_codigo), CONSTRAINT nes_nombre_uk UNIQUE (nes_nombre));
    COMMENT  ON TABLE catalogo.sg_nivel_escalafon IS 'Tabla para el registro de nivel escalafón.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_escalafon.nes_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_nivel_escalafon_aud (nes_pk bigint NOT NULL, nes_codigo character varying(45), nes_nombre character varying(255), nes_nombre_busqueda character varying(255), nes_habilitado boolean, nes_ult_mod_fecha timestamp without time zone, nes_ult_mod_usuario character varying(45), nes_version integer, rev bigint, revtype smallint);

-- CATEGORIA ESCALAFON
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_categoria_escalafon_ces_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_escalafon (ces_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_categoria_escalafon_ces_pk_seq'::regclass), ces_codigo character varying(45), ces_habilitado boolean, ces_nombre character varying(255), ces_nombre_busqueda character varying(255), ces_ult_mod_fecha timestamp without time zone, ces_ult_mod_usuario character varying(45), ces_version integer, CONSTRAINT sg_categoria_escalafon_pkey PRIMARY KEY (ces_pk), CONSTRAINT ces_codigo_uk UNIQUE (ces_codigo), CONSTRAINT ces_nombre_uk UNIQUE (ces_nombre));
    COMMENT  ON TABLE catalogo.sg_categoria_escalafon IS 'Tabla para el registro de categoría de escalafón.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_escalafon.ces_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_escalafon_aud (ces_pk bigint NOT NULL, ces_codigo character varying(45), ces_habilitado boolean, ces_nombre character varying(255), ces_nombre_busqueda character varying(255), ces_ult_mod_fecha timestamp without time zone, ces_ult_mod_usuario character varying(45), ces_version integer, rev bigint, revtype smallint);-- Areas Indicador

--AREA INDICADOR
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_areas_indicador_ari_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_areas_indicador (ari_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_areas_indicador_ari_pk_seq'::regclass), ari_codigo character varying(45), ari_habilitado boolean, ari_nombre character varying(255), ari_nombre_busqueda character varying(255), ari_ult_mod_fecha timestamp without time zone, ari_ult_mod_usuario character varying(45), ari_version integer, CONSTRAINT sg_areas_indicador_pkey PRIMARY KEY (ari_pk), CONSTRAINT ari_codigo_uk UNIQUE (ari_codigo), CONSTRAINT ari_nombre_uk UNIQUE (ari_nombre));
COMMENT ON TABLE catalogo.sg_areas_indicador IS 'Tabla para el registro de areas indicador.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_areas_indicador.ari_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_areas_indicador_aud (ari_pk bigint NOT NULL, ari_codigo character varying(45), ari_habilitado boolean, ari_nombre character varying(255), ari_nombre_busqueda character varying(255), ari_ult_mod_fecha timestamp without time zone, ari_ult_mod_usuario character varying(45), ari_version integer, rev bigint, revtype smallint);

-- Areas Asignatura Modulo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_areas_asignatura_modulo_aam_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_areas_asignatura_modulo (aam_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_areas_asignatura_modulo_aam_pk_seq'::regclass), aam_codigo character varying(45), aam_habilitado boolean, aam_nombre character varying(255), aam_nombre_busqueda character varying(255), aam_ult_mod_fecha timestamp without time zone, aam_ult_mod_usuario character varying(45), aam_version integer, CONSTRAINT sg_areas_asignatura_modulo_pkey PRIMARY KEY (aam_pk), CONSTRAINT aam_codigo_uk UNIQUE (aam_codigo), CONSTRAINT aam_nombre_uk UNIQUE (aam_nombre));
COMMENT ON TABLE catalogo.sg_areas_asignatura_modulo IS 'Tabla para el registro de areas asignatura modulo.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_areas_asignatura_modulo.aam_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_areas_asignatura_modulo_aud (aam_pk bigint NOT NULL, aam_codigo character varying(45), aam_habilitado boolean, aam_nombre character varying(255), aam_nombre_busqueda character varying(255), aam_ult_mod_fecha timestamp without time zone, aam_ult_mod_usuario character varying(45), aam_version integer, rev bigint, revtype smallint);
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_escalafon_aud (ces_pk bigint NOT NULL, ces_codigo character varying(45), ces_habilitado boolean, ces_nombre character varying(255), ces_nombre_busqueda character varying(255), ces_ult_mod_fecha timestamp without time zone, ces_ult_mod_usuario character varying(45), ces_version integer, rev bigint, revtype smallint);


-- NIVEL EMPLEADO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_nivel_empleado_nem_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_nivel_empleado (nem_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_nivel_empleado_nem_pk_seq'::regclass), nem_codigo character varying(45), nem_habilitado boolean, nem_nombre character varying(255), nem_nombre_busqueda character varying(255), nem_ult_mod_fecha timestamp without time zone, nem_ult_mod_usuario character varying(45), nem_version integer, CONSTRAINT sg_nivel_empleado_pkey PRIMARY KEY (nem_pk), CONSTRAINT nem_codigo_uk UNIQUE (nem_codigo), CONSTRAINT nem_nombre_uk UNIQUE (nem_nombre));
    COMMENT  ON TABLE catalogo.sg_nivel_empleado IS 'Tabla para el registro del nivel del empleado.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_empleado.nem_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_nivel_empleado_aud (nem_pk bigint NOT NULL, nem_codigo character varying(45), nem_habilitado boolean, nem_nombre character varying(255), nem_nombre_busqueda character varying(255), nem_ult_mod_fecha timestamp without time zone, nem_ult_mod_usuario character varying(45), nem_version integer, rev bigint, revtype smallint);


-- CATEGORIA EMPLEADO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_categoria_empleado_cem_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_empleado (cem_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_categoria_empleado_cem_pk_seq'::regclass), cem_codigo character varying(45), cem_habilitado boolean, cem_nombre character varying(255), cem_nombre_busqueda character varying(255), cem_ult_mod_fecha timestamp without time zone, cem_ult_mod_usuario character varying(45), cem_version integer, CONSTRAINT sg_categoria_empleado_pkey PRIMARY KEY (cem_pk), CONSTRAINT cem_codigo_uk UNIQUE (cem_codigo), CONSTRAINT cem_nombre_uk UNIQUE (cem_nombre));
    COMMENT  ON TABLE catalogo.sg_categoria_empleado IS 'Tabla para el registro de categoría del empleado.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_empleado.cem_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_empleado_aud (cem_pk bigint NOT NULL, cem_codigo character varying(45), cem_habilitado boolean, cem_nombre character varying(255), cem_nombre_busqueda character varying(255), cem_ult_mod_fecha timestamp without time zone, cem_ult_mod_usuario character varying(45), cem_version integer, rev bigint, revtype smallint);


-- CARGO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_cargo_car_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_cargo (car_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_cargo_car_pk_seq'::regclass), car_codigo character varying(45), car_habilitado boolean, car_nombre character varying(255), car_nombre_busqueda character varying(255), car_ult_mod_fecha timestamp without time zone, car_ult_mod_usuario character varying(45), car_version integer, CONSTRAINT sg_cargo_pkey PRIMARY KEY (car_pk), CONSTRAINT car_codigo_uk UNIQUE (car_codigo), CONSTRAINT car_nombre_uk UNIQUE (car_nombre));
    COMMENT  ON TABLE catalogo.sg_cargo IS 'Tabla para el registro de los cargos.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_cargo.car_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_cargo.car_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_cargo_aud (car_pk bigint NOT NULL, car_codigo character varying(45), car_habilitado boolean, car_nombre character varying(255), car_nombre_busqueda character varying(255), car_ult_mod_fecha timestamp without time zone, car_ult_mod_usuario character varying(45), car_version integer, rev bigint, revtype smallint);


-- TIPO DE CONTRATO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_contrato_tco_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipo_contrato (tco_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_contrato_tco_pk_seq'::regclass), tco_codigo character varying(45), tco_habilitado boolean, tco_nombre character varying(255), tco_nombre_busqueda character varying(255), tco_ult_mod_fecha timestamp without time zone, tco_ult_mod_usuario character varying(45), tco_version integer, CONSTRAINT sg_tipo_contrato_pkey PRIMARY KEY (tco_pk), CONSTRAINT tco_codigo_uk UNIQUE (tco_codigo), CONSTRAINT tco_nombre_uk UNIQUE (tco_nombre));
    COMMENT  ON TABLE catalogo.sg_tipo_contrato IS 'Tabla para el registro del tipo de contrato.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_contrato.tco_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_contrato_aud (tco_pk bigint NOT NULL, tco_codigo character varying(45), tco_habilitado boolean, tco_nombre character varying(255), tco_nombre_busqueda character varying(255), tco_ult_mod_fecha timestamp without time zone, tco_ult_mod_usuario character varying(45), tco_version integer, rev bigint, revtype smallint);

-- LEY DE SALARIO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_ley_salario_lsa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_ley_salario (lsa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_ley_salario_lsa_pk_seq'::regclass), lsa_codigo character varying(45), lsa_habilitado boolean, lsa_nombre character varying(255), lsa_nombre_busqueda character varying(255), lsa_ult_mod_fecha timestamp without time zone, lsa_ult_mod_usuario character varying(45), lsa_version integer, CONSTRAINT sg_ley_salario_pkey PRIMARY KEY (lsa_pk), CONSTRAINT lsa_codigo_uk UNIQUE (lsa_codigo), CONSTRAINT lsa_nombre_uk UNIQUE (lsa_nombre));
    COMMENT  ON TABLE catalogo.sg_ley_salario IS 'Tabla para el registro de ley de salario.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_ley_salario.lsa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_ley_salario_aud (lsa_pk bigint NOT NULL, lsa_codigo character varying(45), lsa_habilitado boolean, lsa_nombre character varying(255), lsa_nombre_busqueda character varying(255), lsa_ult_mod_fecha timestamp without time zone, lsa_ult_mod_usuario character varying(45), lsa_version integer, rev bigint, revtype smallint);


-- FUENTE DE FINANCIAMIENTO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_fuente_financiamiento_ffi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_fuente_financiamiento (ffi_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_fuente_financiamiento_ffi_pk_seq'::regclass), ffi_codigo character varying(45), ffi_habilitado boolean, ffi_nombre character varying(255), ffi_nombre_busqueda character varying(255), ffi_ult_mod_fecha timestamp without time zone, ffi_ult_mod_usuario character varying(45), ffi_version integer, CONSTRAINT sg_fuente_financiamiento_pkey PRIMARY KEY (ffi_pk), CONSTRAINT ffi_codigo_uk UNIQUE (ffi_codigo), CONSTRAINT ffi_nombre_uk UNIQUE (ffi_nombre));
    COMMENT  ON TABLE catalogo.sg_fuente_financiamiento IS 'Tabla para el registro de ley de salario.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_fuente_financiamiento.ffi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_fuente_financiamiento_aud (ffi_pk bigint NOT NULL, ffi_codigo character varying(45), ffi_habilitado boolean, ffi_nombre character varying(255), ffi_nombre_busqueda character varying(255), ffi_ult_mod_fecha timestamp without time zone, ffi_ult_mod_usuario character varying(45), ffi_version integer, rev bigint, revtype smallint);


-- INSTITUCION PAGA SALARIO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_institucion_paga_salario_ips_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_institucion_paga_salario (ips_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_institucion_paga_salario_ips_pk_seq'::regclass), ips_codigo character varying(45), ips_habilitado boolean, ips_nombre character varying(255), ips_nombre_busqueda character varying(255), ips_ult_mod_fecha timestamp without time zone, ips_ult_mod_usuario character varying(45), ips_version integer, CONSTRAINT sg_institucion_paga_salario_pkey PRIMARY KEY (ips_pk), CONSTRAINT ips_codigo_uk UNIQUE (ips_codigo), CONSTRAINT ips_nombre_uk UNIQUE (ips_nombre));
    COMMENT  ON TABLE catalogo.sg_institucion_paga_salario IS 'Tabla para el registro de la institucion que paga el salario.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_institucion_paga_salario.ips_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_institucion_paga_salario_aud (ips_pk bigint NOT NULL, ips_codigo character varying(45), ips_habilitado boolean, ips_nombre character varying(255), ips_nombre_busqueda character varying(255), ips_ult_mod_fecha timestamp without time zone, ips_ult_mod_usuario character varying(45), ips_version integer, rev bigint, revtype smallint);


-- TIPO INSTITUCION QUE PAGA
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_institucion_paga_tip_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipo_institucion_paga (tip_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_institucion_paga_tip_pk_seq'::regclass), tip_codigo character varying(45), tip_habilitado boolean, tip_nombre character varying(255), tip_nombre_busqueda character varying(255), tip_ult_mod_fecha timestamp without time zone, tip_ult_mod_usuario character varying(45), tip_version integer, CONSTRAINT sg_tipo_institucion_paga_pkey PRIMARY KEY (tip_pk), CONSTRAINT tip_codigo_uk UNIQUE (tip_codigo), CONSTRAINT tip_nombre_uk UNIQUE (tip_nombre));
    COMMENT  ON TABLE catalogo.sg_tipo_institucion_paga IS 'Tabla para el registro del tipo de institucion que paga el salario.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_institucion_paga.tip_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_institucion_paga_aud (tip_pk bigint NOT NULL, tip_codigo character varying(45), tip_habilitado boolean, tip_nombre character varying(255), tip_nombre_busqueda character varying(255), tip_ult_mod_fecha timestamp without time zone, tip_ult_mod_usuario character varying(45), tip_version integer, rev bigint, revtype smallint);


-- TIPO NOMBRAMIENTO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_nombramiento_tno_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipo_nombramiento (tno_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_nombramiento_tno_pk_seq'::regclass), tno_codigo character varying(45), tno_habilitado boolean, tno_nombre character varying(255), tno_nombre_busqueda character varying(255), tno_ult_mod_fecha timestamp without time zone, tno_ult_mod_usuario character varying(45), tno_version integer, CONSTRAINT sg_tipo_nombramiento_pkey PRIMARY KEY (tno_pk), CONSTRAINT tno_codigo_uk UNIQUE (tno_codigo), CONSTRAINT tno_nombre_uk UNIQUE (tno_nombre));
    COMMENT  ON TABLE catalogo.sg_tipo_nombramiento IS 'Tabla para el registro del tipo de nombramiento.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipo_nombramiento.tno_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_nombramiento_aud (tno_pk bigint NOT NULL, tno_codigo character varying(45), tno_habilitado boolean, tno_nombre character varying(255), tno_nombre_busqueda character varying(255), tno_ult_mod_fecha timestamp without time zone, tno_ult_mod_usuario character varying(45), tno_version integer, rev bigint, revtype smallint);


-- MODO PAGO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_modo_pago_mpa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_modo_pago (mpa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_modo_pago_mpa_pk_seq'::regclass), mpa_codigo character varying(45), mpa_habilitado boolean, mpa_nombre character varying(255), mpa_nombre_busqueda character varying(255), mpa_ult_mod_fecha timestamp without time zone, mpa_ult_mod_usuario character varying(45), mpa_version integer, CONSTRAINT sg_modo_pago_pkey PRIMARY KEY (mpa_pk), CONSTRAINT mpa_codigo_uk UNIQUE (mpa_codigo), CONSTRAINT mpa_nombre_uk UNIQUE (mpa_nombre));
    COMMENT  ON TABLE catalogo.sg_modo_pago IS 'Tabla para el registro del modo de pago.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modo_pago.mpa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_modo_pago_aud (mpa_pk bigint NOT NULL, mpa_codigo character varying(45), mpa_habilitado boolean, mpa_nombre character varying(255), mpa_nombre_busqueda character varying(255), mpa_ult_mod_fecha timestamp without time zone, mpa_ult_mod_usuario character varying(45), mpa_version integer, rev bigint, revtype smallint);

-- Sistemas de gestion de contenido
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_sistemas_gestion_contenido_sgc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_sistemas_gestion_contenido (sgc_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_sistemas_gestion_contenido_sgc_pk_seq'::regclass), sgc_codigo character varying(45), sgc_habilitado boolean, sgc_nombre character varying(255), sgc_nombre_busqueda character varying(255), sgc_ult_mod_fecha timestamp without time zone, sgc_ult_mod_usuario character varying(45), sgc_version integer, CONSTRAINT sg_sistemas_gestion_contenido_pkey PRIMARY KEY (sgc_pk), CONSTRAINT sgc_codigo_uk UNIQUE (sgc_codigo), CONSTRAINT sgc_nombre_uk UNIQUE (sgc_nombre));
    COMMENT  ON TABLE catalogo.sg_sistemas_gestion_contenido IS 'Tabla para el registro de los sistemas de gestion de contenido.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_sistemas_gestion_contenido.sgc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_sistemas_gestion_contenido_aud (sgc_pk bigint NOT NULL, sgc_codigo character varying(45), sgc_habilitado boolean, sgc_nombre character varying(255), sgc_nombre_busqueda character varying(255), sgc_ult_mod_fecha timestamp without time zone, sgc_ult_mod_usuario character varying(45), sgc_version integer, rev bigint, revtype smallint);


-- Idiomas
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_idiomas_idi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_idiomas (idi_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_idiomas_idi_pk_seq'::regclass), idi_codigo character varying(45), idi_habilitado boolean, idi_nombre character varying(255), idi_nombre_busqueda character varying(255), idi_ult_mod_fecha timestamp without time zone, idi_ult_mod_usuario character varying(45), idi_version integer, CONSTRAINT sg_idiomas_pkey PRIMARY KEY (idi_pk), CONSTRAINT idi_codigo_uk UNIQUE (idi_codigo), CONSTRAINT idi_nombre_uk UNIQUE (idi_nombre));
    COMMENT  ON TABLE catalogo.sg_idiomas IS 'Tabla para el registro de los idiomas.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_idiomas.idi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_idiomas_aud (idi_pk bigint NOT NULL, idi_codigo character varying(45), idi_habilitado boolean, idi_nombre character varying(255), idi_nombre_busqueda character varying(255), idi_ult_mod_fecha timestamp without time zone, idi_ult_mod_usuario character varying(45), idi_version integer, rev bigint, revtype smallint);

-- Nivel Idioma
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_niveles_idioma_nid_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_niveles_idioma (nid_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_niveles_idioma_nid_pk_seq'::regclass), nid_codigo character varying(45), nid_habilitado boolean, nid_nombre character varying(255), nid_nombre_busqueda character varying(255), nid_ult_mod_fecha timestamp without time zone, nid_ult_mod_usuario character varying(45), nid_version integer, CONSTRAINT sg_niveles_idioma_pkey PRIMARY KEY (nid_pk), CONSTRAINT nid_codigo_uk UNIQUE (nid_codigo), CONSTRAINT nid_nombre_uk UNIQUE (nid_nombre));
    COMMENT  ON TABLE catalogo.sg_niveles_idioma IS 'Tabla para el registro de los niveles de idiomas.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_niveles_idioma.nid_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_niveles_idioma_aud (nid_pk bigint NOT NULL, nid_codigo character varying(45), nid_habilitado boolean, nid_nombre character varying(255), nid_nombre_busqueda character varying(255), nid_ult_mod_fecha timestamp without time zone, nid_ult_mod_usuario character varying(45), nid_version integer, rev bigint, revtype smallint);

-- Tipo de estudio
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_estudio_tes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_estudio (tes_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_estudio_tes_pk_seq'::regclass), tes_codigo character varying(45), tes_habilitado boolean, tes_nombre character varying(255), tes_nombre_busqueda character varying(255), tes_ult_mod_fecha timestamp without time zone, tes_ult_mod_usuario character varying(45), tes_version integer, CONSTRAINT sg_tipos_estudio_pkey PRIMARY KEY (tes_pk), CONSTRAINT tes_codigo_uk UNIQUE (tes_codigo), CONSTRAINT tes_nombre_uk UNIQUE (tes_nombre));
    COMMENT  ON TABLE catalogo.sg_tipos_estudio IS 'Tabla para el registro de los tipos de estudio.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_estudio.tes_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_estudio_aud (tes_pk bigint NOT NULL, tes_codigo character varying(45), tes_habilitado boolean, tes_nombre character varying(255), tes_nombre_busqueda character varying(255), tes_ult_mod_fecha timestamp without time zone, tes_ult_mod_usuario character varying(45), tes_version integer, rev bigint, revtype smallint);

-- Carrera
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_carreras_crr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_carreras (crr_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_carreras_crr_pk_seq'::regclass), crr_codigo character varying(45), crr_habilitado boolean, crr_nombre character varying(255), crr_nombre_busqueda character varying(255), crr_ult_mod_fecha timestamp without time zone, crr_ult_mod_usuario character varying(45), crr_version integer, CONSTRAINT sg_carreras_pkey PRIMARY KEY (crr_pk), CONSTRAINT crr_codigo_uk UNIQUE (crr_codigo), CONSTRAINT crr_nombre_uk UNIQUE (crr_nombre));
    COMMENT  ON TABLE catalogo.sg_carreras IS 'Tabla para el registro de las carreras.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_carreras.crr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_carreras_aud (crr_pk bigint NOT NULL, crr_codigo character varying(45), crr_habilitado boolean, crr_nombre character varying(255), crr_nombre_busqueda character varying(255), crr_ult_mod_fecha timestamp without time zone, crr_ult_mod_usuario character varying(45), crr_version integer, rev bigint, revtype smallint);

-- Especialidad
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_especialidades_esp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_especialidades (esp_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_especialidades_esp_pk_seq'::regclass), esp_codigo character varying(45), esp_habilitado boolean, esp_nombre character varying(255), esp_nombre_busqueda character varying(255), esp_ult_mod_fecha timestamp without time zone, esp_ult_mod_usuario character varying(45), esp_version integer, CONSTRAINT sg_especialidades_pkey PRIMARY KEY (esp_pk), CONSTRAINT esp_codigo_uk UNIQUE (esp_codigo), CONSTRAINT esp_nombre_uk UNIQUE (esp_nombre));
    COMMENT  ON TABLE catalogo.sg_especialidades IS 'Tabla para el registro de las especialidades.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_especialidades.esp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_especialidades_aud (esp_pk bigint NOT NULL, esp_codigo character varying(45), esp_habilitado boolean, esp_nombre character varying(255), esp_nombre_busqueda character varying(255), esp_ult_mod_fecha timestamp without time zone, esp_ult_mod_usuario character varying(45), esp_version integer, rev bigint, revtype smallint);

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
CREATE TABLE IF NOT EXISTS catalogo.sg_modalidades_estudio_aud (mes_pk bigint NOT NULL, mes_codigo character varying(45), mes_habilitado boolean, mes_nombre character varying(255), mes_nombre_busqueda character varying(255), mes_ult_mod_fecha timestamp without time zone, mes_ult_mod_usuario character varying(45), mes_version integer, rev bigint, revtype smallint);

-- Tipo de capacitación
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_capacitacion_tca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_capacitacion (tca_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_capacitacion_tca_pk_seq'::regclass), tca_codigo character varying(45), tca_habilitado boolean, tca_nombre character varying(255), tca_nombre_busqueda character varying(255), tca_ult_mod_fecha timestamp without time zone, tca_ult_mod_usuario character varying(45), tca_version integer, CONSTRAINT sg_tipos_capacitacion_pkey PRIMARY KEY (tca_pk), CONSTRAINT tca_codigo_uk UNIQUE (tca_codigo), CONSTRAINT tca_nombre_uk UNIQUE (tca_nombre));
    COMMENT  ON TABLE catalogo.sg_tipos_capacitacion IS 'Tabla para el registro de los tipos de capacitación.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_capacitacion.tca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_capacitacion_aud (tca_pk bigint NOT NULL, tca_codigo character varying(45), tca_habilitado boolean, tca_nombre character varying(255), tca_nombre_busqueda character varying(255), tca_ult_mod_fecha timestamp without time zone, tca_ult_mod_usuario character varying(45), tca_version integer, rev bigint, revtype smallint);

-- Alcance
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_alcance_capacitacion_aca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_alcance_capacitacion (aca_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_alcance_capacitacion_aca_pk_seq'::regclass), aca_codigo character varying(45), aca_habilitado boolean, aca_nombre character varying(255), aca_nombre_busqueda character varying(255), aca_ult_mod_fecha timestamp without time zone, aca_ult_mod_usuario character varying(45), aca_version integer, CONSTRAINT sg_alcance_capacitacion_pkey PRIMARY KEY (aca_pk), CONSTRAINT aca_codigo_uk UNIQUE (aca_codigo), CONSTRAINT aca_nombre_uk UNIQUE (aca_nombre));
    COMMENT  ON TABLE catalogo.sg_alcance_capacitacion IS 'Tabla para el registro del alcance de las capacitaciones.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_alcance_capacitacion.aca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_alcance_capacitacion_aud (aca_pk bigint NOT NULL, aca_codigo character varying(45), aca_habilitado boolean, aca_nombre character varying(255), aca_nombre_busqueda character varying(255), aca_ult_mod_fecha timestamp without time zone, aca_ult_mod_usuario character varying(45), aca_version integer, rev bigint, revtype smallint);

-- Nivel formacion docente
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_nivel_formacion_docente_nfd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_nivel_formacion_docente (nfd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_nivel_formacion_docente_nfd_pk_seq'::regclass), nfd_codigo character varying(45), nfd_habilitado boolean, nfd_nombre character varying(255), nfd_nombre_busqueda character varying(255), nfd_ult_mod_fecha timestamp without time zone, nfd_ult_mod_usuario character varying(45), nfd_version integer, CONSTRAINT sg_nivel_formacion_docente_pkey PRIMARY KEY (nfd_pk), CONSTRAINT nfd_codigo_uk UNIQUE (nfd_codigo), CONSTRAINT nfd_nombre_uk UNIQUE (nfd_nombre));
    COMMENT  ON TABLE catalogo.sg_nivel_formacion_docente IS 'Tabla para el registro de los niveles de la formación docente.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_nivel_formacion_docente.nfd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_nivel_formacion_docente_aud (nfd_pk bigint NOT NULL, nfd_codigo character varying(45), nfd_habilitado boolean, nfd_nombre character varying(255), nfd_nombre_busqueda character varying(255), nfd_ult_mod_fecha timestamp without time zone, nfd_ult_mod_usuario character varying(45), nfd_version integer, rev bigint, revtype smallint);

-- Categoria formacion docente
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_categoria_formacion_docente_cfd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_categoria_formacion_docente (cfd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_categoria_formacion_docente_cfd_pk_seq'::regclass), cfd_codigo character varying(45), cfd_habilitado boolean, cfd_nombre character varying(255), cfd_nombre_busqueda character varying(255), cfd_ult_mod_fecha timestamp without time zone, cfd_ult_mod_usuario character varying(45), cfd_version integer, CONSTRAINT sg_categoria_formacion_docente_pkey PRIMARY KEY (cfd_pk), CONSTRAINT cfd_codigo_uk UNIQUE (cfd_codigo), CONSTRAINT cfd_nombre_uk UNIQUE (cfd_nombre));
    COMMENT  ON TABLE catalogo.sg_categoria_formacion_docente IS 'Tabla para el registro de las categorias de la formación docente.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categoria_formacion_docente.cfd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_formacion_docente_aud (cfd_pk bigint NOT NULL, cfd_codigo character varying(45), cfd_habilitado boolean, cfd_nombre character varying(255), cfd_nombre_busqueda character varying(255), cfd_ult_mod_fecha timestamp without time zone, cfd_ult_mod_usuario character varying(45), cfd_version integer, rev bigint, revtype smallint);

-- Modulo formacion docente
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_modulo_formacion_docente_mfd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_modulo_formacion_docente (mfd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_modulo_formacion_docente_mfd_pk_seq'::regclass), mfd_codigo character varying(45), mfd_habilitado boolean, mfd_nombre character varying(255), mfd_nombre_busqueda character varying(255), mfd_ult_mod_fecha timestamp without time zone, mfd_ult_mod_usuario character varying(45), mfd_version integer, CONSTRAINT sg_modulo_formacion_docente_pkey PRIMARY KEY (mfd_pk), CONSTRAINT mfd_codigo_uk UNIQUE (mfd_codigo), CONSTRAINT mfd_nombre_uk UNIQUE (mfd_nombre));
    COMMENT  ON TABLE catalogo.sg_modulo_formacion_docente IS 'Tabla para el registro de los modulos de la formación docente.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_modulo_formacion_docente.mfd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_modulo_formacion_docente_aud (mfd_pk bigint NOT NULL, mfd_codigo character varying(45), mfd_habilitado boolean, mfd_nombre character varying(255), mfd_nombre_busqueda character varying(255), mfd_ult_mod_fecha timestamp without time zone, mfd_ult_mod_usuario character varying(45), mfd_version integer, rev bigint, revtype smallint);








-- Glosarios
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_glosarios_glo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_glosarios (glo_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_glosarios_glo_pk_seq'::regclass), glo_codigo character varying(45), glo_habilitado boolean, glo_nombre character varying(255), glo_nombre_busqueda character varying(255),glo_fuente character varying(255), glo_ult_mod_fecha timestamp without time zone, glo_ult_mod_usuario character varying(45), glo_version integer, CONSTRAINT sg_glosarios_pkey PRIMARY KEY (glo_pk), CONSTRAINT glo_codigo_uk UNIQUE (glo_codigo), CONSTRAINT glo_nombre_uk UNIQUE (glo_nombre));
COMMENT ON TABLE catalogo.sg_glosarios IS 'Tabla para el registro de glosarios.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_glosarios.glo_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_glosarios_aud (glo_pk bigint NOT NULL, glo_codigo character varying(45), glo_habilitado boolean, glo_nombre character varying(255), glo_nombre_busqueda character varying(255),glo_fuente character varying(255), glo_ult_mod_fecha timestamp without time zone, glo_ult_mod_usuario character varying(45), glo_version integer, rev bigint, revtype smallint);
-- Categorias Violencia
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_categoria_violencia_cav_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_violencia (cav_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_categoria_violencia_cav_pk_seq'::regclass), cav_codigo character varying(45), cav_habilitado boolean, cav_nombre character varying(255), cav_nombre_busqueda character varying(255), cav_ult_mod_fecha timestamp without time zone, cav_ult_mod_usuario character varying(45), cav_version integer, CONSTRAINT sg_categoria_violencia_pkey PRIMARY KEY (cav_pk), CONSTRAINT cav_codigo_uk UNIQUE (cav_codigo), CONSTRAINT cav_nombre_uk UNIQUE (cav_nombre));
COMMENT ON TABLE catalogo.sg_categoria_violencia IS 'Tabla para el registro de categorias violencia.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_categoria_violencia.cav_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_categoria_violencia_aud (cav_pk bigint NOT NULL, cav_codigo character varying(45), cav_habilitado boolean, cav_nombre character varying(255), cav_nombre_busqueda character varying(255), cav_ult_mod_fecha timestamp without time zone, cav_ult_mod_usuario character varying(45), cav_version integer, rev bigint, revtype smallint);

--Alter table: catalogo.sg_tipos_calendario_escolar
ALTER TABLE catalogo.sg_tipos_calendario_escolar ADD "tce_descripcion" character varying(500);
COMMENT ON COLUMN catalogo.sg_tipos_calendario_escolar.tce_descripcion IS 'Descripcion del Registro.';
ALTER TABLE catalogo.sg_tipos_calendario_escolar_aud ADD "tce_descripcion" character varying(500);

--Alter table: catalogo.sg_discapacidades
ALTER TABLE catalogo.sg_discapacidades ADD "dis_clasificacion" character varying(500);
COMMENT ON COLUMN catalogo.sg_discapacidades.dis_clasificacion IS 'Clasificacion del Registro.';
ALTER TABLE catalogo.sg_discapacidades_aud ADD "dis_clasificacion" character varying(500);

--Alter table: catalogo.sg_motivos_inasistencia 
ALTER TABLE catalogo.sg_motivos_inasistencia ADD "min_motivo_justificado" boolean;
COMMENT ON COLUMN catalogo.sg_motivos_inasistencia.min_motivo_justificado IS 'Motivo justificado del Registro.';
ALTER TABLE catalogo.sg_motivos_inasistencia_aud ADD "min_motivo_justificado" boolean;

ALTER TABLE catalogo.sg_calificaciones ADD CONSTRAINT cal_codigo_uk UNIQUE (cal_codigo);
ALTER TABLE catalogo.sg_calificaciones ADD CONSTRAINT cal_orden_escala_uk UNIQUE (cal_orden,cal_escala_fk);

-- Nacionalidades
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_nacionalidades_nac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_nacionalidades (nac_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_nacionalidades_nac_pk_seq'::regclass), nac_codigo character varying(45), nac_habilitado boolean, nac_nombre character varying(255), nac_nombre_busqueda character varying(255), nac_ult_mod_fecha timestamp without time zone, nac_ult_mod_usuario character varying(45), nac_version integer, CONSTRAINT sg_nacionalidades_pkey PRIMARY KEY (nac_pk), CONSTRAINT nac_codigo_uk UNIQUE (nac_codigo), CONSTRAINT nac_nombre_uk UNIQUE (nac_nombre));
COMMENT ON TABLE catalogo.sg_nacionalidades IS 'Tabla para el registro de nacionalidades.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_nacionalidades.nac_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_nacionalidades_aud (nac_pk bigint NOT NULL, nac_codigo character varying(45), nac_habilitado boolean, nac_nombre character varying(255), nac_nombre_busqueda character varying(255), nac_ult_mod_fecha timestamp without time zone, nac_ult_mod_usuario character varying(45), nac_version integer, rev bigint, revtype smallint);


-- Tipos Calle
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_calle_tll_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_calle (tll_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_calle_tll_pk_seq'::regclass), tll_codigo character varying(45), tll_habilitado boolean, tll_nombre character varying(255), tll_nombre_busqueda character varying(255), tll_ult_mod_fecha timestamp without time zone, tll_ult_mod_usuario character varying(45), tll_version integer, CONSTRAINT sg_tipos_calle_pkey PRIMARY KEY (tll_pk), CONSTRAINT tll_codigo_uk UNIQUE (tll_codigo), CONSTRAINT tll_nombre_uk UNIQUE (tll_nombre));
    COMMENT  ON TABLE catalogo.sg_tipos_calle IS 'Tabla para el registro de los tipos de calle.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_calle.tll_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_calle_aud (tll_pk bigint NOT NULL, tll_codigo character varying(45), tll_habilitado boolean, tll_nombre character varying(255), tll_nombre_busqueda character varying(255), tll_ult_mod_fecha timestamp without time zone, tll_ult_mod_usuario character varying(45), tll_version integer, rev bigint, revtype smallint);


--Alter table GLOSARIO
ALTER TABLE catalogo.sg_glosarios DROP CONSTRAINT glo_codigo_uk;
ALTER TABLE catalogo.sg_glosarios ALTER COLUMN "glo_fuente" TYPE CHARACTER VARYING(500);
COMMENT ON COLUMN catalogo.sg_glosarios.glo_fuente IS 'Fuente del registro';
ALTER TABLE catalogo.sg_glosarios_aud ALTER COLUMN "glo_fuente" TYPE CHARACTER VARYING(500);
ALTER TABLE catalogo.sg_glosarios ALTER COLUMN "glo_nombre" TYPE CHARACTER VARYING(1000);
ALTER TABLE catalogo.sg_glosarios ALTER COLUMN "glo_nombre_busqueda" TYPE CHARACTER VARYING(1000);
ALTER TABLE catalogo.sg_glosarios_aud ALTER COLUMN "glo_nombre" TYPE CHARACTER VARYING(1000);
ALTER TABLE catalogo.sg_glosarios_aud ALTER COLUMN "glo_nombre_busqueda" TYPE CHARACTER VARYING(1000);

ALTER TABLE catalogo.sg_escalas_calificacion ADD COLUMN eca_minimo numeric(10,2);
ALTER TABLE catalogo.sg_escalas_calificacion_aud ADD COLUMN eca_minimo numeric(10,2);
ALTER TABLE catalogo.sg_escalas_calificacion ADD COLUMN eca_maximo numeric(10,2);
ALTER TABLE catalogo.sg_escalas_calificacion_aud ADD COLUMN eca_maximo numeric(10,2);
ALTER TABLE catalogo.sg_escalas_calificacion ADD COLUMN eca_precision integer;
ALTER TABLE catalogo.sg_escalas_calificacion_aud ADD COLUMN eca_precision integer;


-- Servicios Infraestructura
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_servicios_infraestructura_sin_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_servicios_infraestructura (sin_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_servicios_infraestructura_sin_pk_seq'::regclass), sin_codigo character varying(45), sin_habilitado boolean, sin_nombre character varying(255), sin_nombre_busqueda character varying(255), sin_ult_mod_fecha timestamp without time zone, sin_ult_mod_usuario character varying(45), sin_version integer, CONSTRAINT sg_servicios_infraestructura_pkey PRIMARY KEY (sin_pk), CONSTRAINT sin_codigo_uk UNIQUE (sin_codigo), CONSTRAINT sin_nombre_uk UNIQUE (sin_nombre));
COMMENT ON TABLE catalogo.sg_servicios_infraestructura IS 'Tabla para el registro de servicios infraestructura.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_servicios_infraestructura.sin_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_servicios_infraestructura_aud (sin_pk bigint NOT NULL, sin_codigo character varying(45), sin_habilitado boolean, sin_nombre character varying(255), sin_nombre_busqueda character varying(255), sin_ult_mod_fecha timestamp without time zone, sin_ult_mod_usuario character varying(45), sin_version integer, rev bigint, revtype smallint);


--Alter tipo de manifestacion de violencia
ALTER TABLE catalogo.sg_tipos_manifestacion ADD COLUMN tma_categoria_violencia_fk bigint;
COMMENT ON COLUMN catalogo.sg_tipos_manifestacion.tma_categoria_violencia_fk IS 'Llave foránea que hace referencia a la categoría que pertenece el tipo de violencia';
ALTER TABLE catalogo.sg_tipos_manifestacion_aud ADD COLUMN tma_categoria_violencia_fk bigint;
ALTER TABLE catalogo.sg_tipos_manifestacion ADD CONSTRAINT sg_tipo_manifestacion_categoria_fkey FOREIGN KEY (tma_categoria_violencia_fk) REFERENCES catalogo.sg_categoria_violencia(cav_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- Tipos de Organismo Administrativo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_organismo_administrativo_toa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_organismo_administrativo (toa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_organismo_administrativo_toa_pk_seq'::regclass), toa_codigo character varying(45), toa_habilitado boolean, toa_nombre character varying(255), toa_nombre_busqueda character varying(255), toa_ult_mod_fecha timestamp without time zone, toa_ult_mod_usuario character varying(45), toa_version integer, CONSTRAINT sg_tipos_organismo_administrativo_pkey PRIMARY KEY (toa_pk), CONSTRAINT toa_codigo_uk UNIQUE (toa_codigo), CONSTRAINT toa_nombre_uk UNIQUE (toa_nombre));
COMMENT ON TABLE catalogo.sg_tipos_organismo_administrativo IS 'Tabla para el registro de tipos de organismo administrativo.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_organismo_administrativo.toa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_organismo_administrativo_aud (toa_pk bigint NOT NULL, toa_codigo character varying(45), toa_habilitado boolean, toa_nombre character varying(255), toa_nombre_busqueda character varying(255), toa_ult_mod_fecha timestamp without time zone, toa_ult_mod_usuario character varying(45), toa_version integer, rev bigint, revtype smallint);


ALTER TABLE catalogo.sg_institucion_paga_salario ADD COLUMN ips_tipo_institucion bigint;
ALTER TABLE catalogo.sg_institucion_paga_salario_aud ADD COLUMN ips_tipo_institucion bigint;
ALTER TABLE catalogo.sg_institucion_paga_salario ADD CONSTRAINT sg_institucion_paga_salario_tipo_institucion_pago_fkey FOREIGN KEY (ips_tipo_institucion) REFERENCES catalogo.sg_tipo_institucion_paga(tip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE catalogo.sg_escalas_calificacion ADD COLUMN eca_minimo_aprobacion numeric(10,2);
ALTER TABLE catalogo.sg_escalas_calificacion_aud ADD COLUMN eca_minimo_aprobacion numeric(10,2);

ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_texto_uso type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_texto_uso type text;
ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_texto_normativa type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_texto_normativa type text;
ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_vinculos type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_vinculos type text;

-- 0.17.0

ALTER TABLE catalogo.sg_afp_aud                             ADD CONSTRAINT 	afp_revinfo_fk                              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_alcance_capacitacion_aud            ADD CONSTRAINT 	alcance_capacitacion_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_areas_asignatura_modulo_aud         ADD CONSTRAINT 	areas_asignatura_modulo_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_areas_indicador_aud                 ADD CONSTRAINT 	areas_indicador_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_ayudas_aud                          ADD CONSTRAINT 	ayudas_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_calificaciones_aud                  ADD CONSTRAINT 	calificaciones_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_cantones_aud                        ADD CONSTRAINT 	cantones_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_cargo_aud                           ADD CONSTRAINT 	cargo_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_carreras_aud                        ADD CONSTRAINT 	carreras_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_caserios_aud                        ADD CONSTRAINT 	caserios_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_categoria_empleado_aud              ADD CONSTRAINT 	categoria_empleado_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_categoria_escalafon_aud             ADD CONSTRAINT 	categoria_escalafon_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_categoria_formacion_docente_aud     ADD CONSTRAINT 	categoria_formacion_docente_revinfo_fk      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_categoria_violencia_aud             ADD CONSTRAINT 	categoria_violencia_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_clasificaciones_aud                 ADD CONSTRAINT 	clasificaciones_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_configuraciones_aud                 ADD CONSTRAINT 	configuraciones_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_departamentos_aud                   ADD CONSTRAINT 	departamentos_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_dependencias_economica_aud          ADD CONSTRAINT 	dependencias_economica_revinfo_fk           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_discapacidades_aud                  ADD CONSTRAINT 	discapacidades_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_enfer_no_transm_aud                 ADD CONSTRAINT 	enfer_no_transm_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_escalas_calificacion_aud            ADD CONSTRAINT 	escalas_calificacion_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_escolaridades_aud                   ADD CONSTRAINT 	escolaridades_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_especialidades_aud                  ADD CONSTRAINT 	especialidades_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_estados_civil_aud                   ADD CONSTRAINT 	estados_civil_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_etiquetas_aud                       ADD CONSTRAINT 	etiquetas_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_etnias_aud                          ADD CONSTRAINT 	etnias_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_fuente_financiamiento_aud           ADD CONSTRAINT 	fuente_financiamiento_revinfo_fk            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_glosarios_aud                       ADD CONSTRAINT 	glosarios_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_idiomas_aud                         ADD CONSTRAINT 	idiomas_revinfo_fk                          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_institucion_paga_salario_aud        ADD CONSTRAINT 	institucion_paga_salario_revinfo_fk         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_instituciones_aud                   ADD CONSTRAINT 	instituciones_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_jornadas_laborales_aud              ADD CONSTRAINT 	jornadas_laborales_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_ley_salario_aud                     ADD CONSTRAINT 	ley_salario_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_medios_transporte_aud               ADD CONSTRAINT 	medios_transporte_revinfo_fk                FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_mensajes_aud                        ADD CONSTRAINT 	mensajes_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_modalidades_atencion_aud            ADD CONSTRAINT 	modalidades_atencion_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_modalidades_estudio_aud             ADD CONSTRAINT 	modalidades_estudio_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_modo_pago_aud                       ADD CONSTRAINT 	modo_pago_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_modulo_formacion_docente_aud        ADD CONSTRAINT 	modulo_formacion_docente_revinfo_fk         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_motivos_egreso_aud                  ADD CONSTRAINT 	motivos_egreso_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_motivos_inasistencia_aud            ADD CONSTRAINT 	motivos_inasistencia_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_motivos_retiro_aud                  ADD CONSTRAINT 	motivos_retiro_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_municipios_aud                      ADD CONSTRAINT 	municipios_revinfo_fk                       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_nacionalidades_aud                  ADD CONSTRAINT 	nacionalidades_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_nivel_empleado_aud                  ADD CONSTRAINT 	nivel_empleado_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_nivel_escalafon_aud                 ADD CONSTRAINT 	nivel_escalafon_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_nivel_formacion_docente_aud         ADD CONSTRAINT 	nivel_formacion_docente_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_niveles_idioma_aud                  ADD CONSTRAINT 	niveles_idioma_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_ocupaciones_aud                     ADD CONSTRAINT 	ocupaciones_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_paises_aud                          ADD CONSTRAINT 	paises_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_profesiones_aud                     ADD CONSTRAINT 	profesiones_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_programas_educativos_aud            ADD CONSTRAINT 	programas_educativos_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_sectores_economicos_aud             ADD CONSTRAINT 	sectores_economicos_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_servicios_infraestructura_aud       ADD CONSTRAINT 	servicios_infraestructura_revinfo_fk        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_sexos_aud                           ADD CONSTRAINT 	sexos_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_sistemas_gestion_contenido_aud      ADD CONSTRAINT 	sistemas_gestion_contenido_revinfo_fk       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_sub_modalidades_aud                 ADD CONSTRAINT 	sub_modalidades_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipo_contrato_aud                   ADD CONSTRAINT 	tipo_contrato_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipo_institucion_paga_aud           ADD CONSTRAINT 	tipo_institucion_paga_revinfo_fk            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipo_nombramiento_aud               ADD CONSTRAINT 	tipo_nombramiento_revinfo_fk                FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_calendario_escolar_aud        ADD CONSTRAINT 	tipos_calendario_escolar_revinfo_fk         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_calle_aud                     ADD CONSTRAINT 	tipos_calle_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_capacitacion_aud              ADD CONSTRAINT 	tipos_capacitacion_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_estudio_aud                   ADD CONSTRAINT 	tipos_estudio_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_identificacion_aud            ADD CONSTRAINT 	tipos_identificacion_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_manifestacion_aud             ADD CONSTRAINT 	tipos_manifestacion_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_organismo_administrativo_aud  ADD CONSTRAINT 	tipos_organismo_administrativo_revinfo_fk   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_parentesco_aud                ADD CONSTRAINT 	tipos_parentesco_revinfo_fk                 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_sangre_aud                    ADD CONSTRAINT 	tipos_sangre_revinfo_fk                     FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_telefono_aud                  ADD CONSTRAINT 	tipos_telefono_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_trabajo_aud                   ADD CONSTRAINT 	tipos_trabajo_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_vacuna_aud                    ADD CONSTRAINT 	tipos_vacuna_revinfo_fk                     FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_tipos_valoracion_aud                ADD CONSTRAINT 	tipos_valoracion_revinfo_fk                 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_zonas_aud                           ADD CONSTRAINT 	zonas_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);


--Tipo de servicio sanitario
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_servicio_sanitario_tss_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_servicio_sanitario(tss_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_tipos_servicio_sanitario_tss_pk_seq'::regclass),tss_codigo CHARACTER varying(4),tss_habilitado BOOLEAN,tss_nombre CHARACTER varying(255),tss_nombre_busqueda CHARACTER varying(255),tss_ult_mod_fecha timestamp without TIME zone,tss_ult_mod_usuario CHARACTER varying(45),tss_version INTEGER,CONSTRAINT sg_tsso_pkey PRIMARY KEY (tss_pk),CONSTRAINT tss_codigo_uk UNIQUE (tss_codigo),CONSTRAINT tss_nombre_uk UNIQUE (tss_nombre));
    COMMENT ON TABLE  catalogo.sg_tipos_servicio_sanitario IS 'Tabla para el registro de los tipos de servicio sanitario.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_tipos_servicio_sanitario_aud(tss_pk bigint NOT NULL,tss_codigo CHARACTER varying(4),tss_habilitado BOOLEAN,tss_nombre CHARACTER varying(255),tss_nombre_busqueda CHARACTER varying(255),tss_ult_mod_fecha timestamp without TIME zone,tss_ult_mod_usuario CHARACTER varying(45),tss_version INTEGER,rev bigint, revtype smallint, CONSTRAINT 	tipos_servicio_sanitario_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));-- Tipos Representante

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_CALENDARIO_ESCOLAR','C1',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_CALENDARIO_ESCOLAR','C2',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_CALENDARIO_ESCOLAR','C3',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CANTON','C4',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CANTON','C5',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CANTON','C6',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CASERIO','C7',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CASERIO','C8',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CASERIO','C9',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEPARTAMENTO','C10',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEPARTAMENTO','C11',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEPARTAMENTO','C12',  '', 2, true, null, null,0);   
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_CIVIL','C13',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_CIVIL','C14',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_CIVIL','C15',  '', 2, true, null, null,0);       
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ETIQUETA','C16',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ETIQUETA','C17',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ETIQUETA','C18',  '', 2, true, null, null,0);        
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ETNIA','C19',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ETNIA','C20',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ETNIA','C21',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INSTITUCION','C22',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INSTITUCION','C23',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INSTITUCION','C24',  '', 2, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_JORNADA_LABORAL','C25',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_JORNADA_LABORAL','C26',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_JORNADA_LABORAL','C27',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MENSAJE','C28',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MENSAJE ','C29',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MENSAJE','C30',  '', 2, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_EGRESO','C31',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_EGRESO ','C32',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_EGRESO','C33',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MUNICIPIO','C34',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MUNICIPIO ','C35',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MUNICIPIO','C36',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PAIS','C37',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PAIS ','C38',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PAIS','C39',  '', 2, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SEXO','C40',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SEXO ','C41',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SEXO','C42',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_IDENTIFICACION','C43',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_IDENTIFICACION ','C44',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_IDENTIFICACION','C45',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_PARENTESCO','C46',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_PARENTESCO ','C47',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_PARENTESCO','C48',  '', 2, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_TELEFONO','C49',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_TELEFONO ','C50',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_TELEFONO','C51',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ZONA','C52',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ZONA ','C53',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ZONA','C54',  '', 2, true, null, null,0);          
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_SANGRE','C55',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_SANGRE ','C56',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_SANGRE','C57',  '', 2, true, null, null,0);   
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MEDIO_TRANSPORTE','C58',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MEDIO_TRANSPORTE ','C59',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MEDIO_TRANSPORTE','C60',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEPENDENCIA_ECONOMICA','C61',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEPENDENCIA_ECONOMICA ','C62',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEPENDENCIA_ECONOMICA','C63',  '', 2, true, null, null,0);      

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DISCAPACIADAD','C199',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DISCAPACIADAD ','C64',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DISCAPACIADAD','C65',  '', 2, true, null, null,0);       
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESCOLARIDAD','C66',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESCOLARIDAD ','C67',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESCOLARIDAD','C68',  '', 2, true, null, null,0);           
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_RETIRO','C69',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_RETIRO ','C70',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_RETIRO','C71',  '', 2, true, null, null,0);        
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_TRABAJO','C72',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_TRABAJO ','C73',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_TRABAJO','C74',  '', 2, true, null, null,0);   
     
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_INASISTENCIA','C75',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_INASISTENCIA ','C76',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_INASISTENCIA','C77',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROFESION','C78',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROFESION ','C79',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROFESION','C80',  '', 2, true, null, null,0);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_MANIFESTACION','C81',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_MANIFESTACION ','C82',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_MANIFESTACION','C83',  '', 2, true, null, null,0);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_VALORACION','C84',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_VALORACION ','C85',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_VALORACION','C86',  '', 2, true, null, null,0);  
   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_OCUPACIONES','C87',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_OCUPACIONES ','C88',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_OCUPACIONES','C89',  '', 2, true, null, null,0);   
   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_PARENTESCO','C90',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_PARENTESCO ','C91',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_PARENTESCO','C92',  '', 2, true, null, null,0);  
   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ENFERMEADES_NO_TRANSMISIBLE','C93',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ENFERMEADES_NO_TRANSMISIBLE ','C94',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ENFERMEADES_NO_TRANSMISIBLE','C95',  '', 2, true, null, null,0);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_VACUNA','C96',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_VACUNA ','C97',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_VACUNA','C98',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AYUDA','C99',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AYUDA','C100',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AYUDA','C101',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESCALAS_CALIFICACION','C102',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESCALAS_CALIFICACION ','C103',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESCALAS_CALIFICACION','C104',  '', 2, true, null, null,0);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACION','C105',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACION ','C106',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACION','C107',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONFIGURACIONES','C108',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONFIGURACIONES', 'C109',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONFIGURACIONES', 'C110',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODALIDADES_ATENCION','C111',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODALIDADES_ATENCION ','C112',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODALIDADES_ATENCION','C113',  '', 2, true, null, null,0);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SUB_MODALIDADES','C114',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SUB_MODALIDADES ','C115',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SUB_MODALIDADES','C116',  '', 2, true, null, null,0);  
    
 INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES (' CREAR_PROGRAMAS_EDUCATIVOS','C117',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES (' ACTUALIZAR_PROGRAMAS_EDUCATIVOS','C118',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES (' ELIMINAR_PROGRAMAS_EDUCATIVOS','C119',  '', 2, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SECTORES_ECONOMICOS','C120',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SECTORES_ECONOMICOS','C121',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SECTORES_ECONOMICOS','C122',  '', 2, true, null, null,0);
    
    

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CLASIFICACIONES','C123',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CLASIFICACIONES ','C124',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CLASIFICACIONES','C125',  '', 2, true, null, null,0);  
    

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AFP','C126',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AFP ','C127',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AFP','C128',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIA_ESCALAFON','C129',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIA_ESCALAFON ','C130',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIA_ESCALAFON','C131',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIVEL_ESCALAFON','C132',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIVEL_ESCALAFON ','C133',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIVEL_ESCALAFON','C134',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIVEL_EMPLEADO','C135',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIVEL_EMPLEADO ','C136',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIVEL_EMPLEADO','C137',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIA_EMPLEADO','C139',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIA_EMPLEADO ','C140',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIA_EMPLEADO','C141',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_CONTRATO','C142',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_CONTRATO ','C143',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_CONTRATO','C144',  '', 2, true, null, null,0);  
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARGO','C145',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARGO ','C146',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARGO','C147',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODO_PAGO','C148',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODO_PAGO ','C149',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODO_PAGO','C150',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_LEY_SALARIO','C151',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_LEY_SALARIO ','C152',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_LEY_SALARIO','C153',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FUENTE_FINANCIAMIENTO','C154',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FUENTE_FINANCIAMIENTO ','C155',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FUENTE_FINANCIAMIENTO','C156',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INSTITUCION_PAGO_SALARIO','C157',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INSTITUCION_PAGO_SALARIO ','C158',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INSTITUCION_PAGO_SALARIO','C159',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_INSTITUCION_PAGO','C160',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_INSTITUCION_PAGO ','C161',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_INSTITUCION_PAGO','C162',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_NOMBRAMIENTO','C163',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_NOMBRAMIENTO ','C164',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_NOMBRAMIENTO','C165',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AREAS_INDICADOR','C166',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AREAS_INDICADOR ','C167',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AREAS_INDICADOR','C168',  '', 2, true, null, null,0);  
            
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AREAS_ASIGNATURA_MODULO','C169',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AREAS_ASIGNATURA_MODULO ','C170',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AREAS_ASIGNATURA_MODULO','C171',  '', 2, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IDIOMAS','C172',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IDIOMAS ','C173',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IDIOMAS','C174',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SISTEMAS_GESTION_CONTENIDO','C175',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SISTEMAS_GESTION_CONTENIDO ','C176',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SISTEMAS_GESTION_CONTENIDO','C177',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIVELES_IDIOMAS','C178',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIVELES_IDIOMAS ','C179',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIVELES_IDIOMAS','C180',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_ESTUDIO','C181',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_ESTUDIO ','C182',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_ESTUDIO','C183',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARRERAS','C184',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARRERAS ','C185',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARRERAS','C186',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESPECIALIDADES','C187',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESPECIALIDADES ','C188',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESPECIALIDADES','C189',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODALIDADES_ESTUDIO','C190',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODALIDADES_ESTUDIO ','C191',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODALIDADES_ESTUDIO','C192',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_CAPACITACION','C193',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_CAPACITACION ','C194',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_CAPACITACION','C195',  '', 2, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ALCANCE_CAPACITACION','C196',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ALCANCE_CAPACITACION ','C197',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ALCANCE_CAPACITACION','C198',  '', 2, true, null, null,0); 


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_GLOSARIOS ','C200',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_GLOSARIOS','C201',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_GLOSARIOS','C202',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIA_VIOLENCIA ','C203',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIA_VIOLENCIA','C204',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIA_VIOLENCIA','C205',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIVELES_FORMACION_DOCENTE ','C206',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIVELES_FORMACION_DOCENTE','C207',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIVELES_FORMACION_DOCENTE','C208',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIAS_FORMACION_DOCENTE ','C209',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIAS_FORMACION_DOCENTE','C210',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIAS_FORMACION_DOCENTE','C211',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODULOS_FORMACION_DOCENTE ','C212',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODULOS_FORMACION_DOCENTE','C213',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODULOS_FORMACION_DOCENTE','C214',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NACIONALIDADES ','C215',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NACIONALIDADES','C216',  '', 2, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NACIONALIDADES','C217',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_CALLE','C218',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_CALLE ','C219',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_CALLE','C220',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SERVICIO_INFRAESTRUCTURA','C221',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SERVICIO_INFRAESTRUCTURA ','C222',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SERVICIO_INFRAESTRUCTURA','C223',  '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_ORGANISMO_ADMINISTRATIVO','C224', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_ORGANISMO_ADMINISTRATIVO','C225', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_ORGANISMO_ADMINISTRATIVO','C226', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_SERVICIO_SANITARIO','C227', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_SERVICIO_SANITARIO','C228', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_SERVICIO_SANITARIO','C229', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTOS_INSTITUCIONALES','C230', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTOS_INSTITUCIONALES','C231', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTOS_INSTITUCIONALES','C232', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_ASOCIACIONES','C233', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_ASOCIACIONES','C234', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_ASOCIACIONES','C235', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASOCIACIONES','C239', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASOCIACIONES','C240', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASOCIACIONES','C241', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTO_ASOCIACION','C242', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTO_ASOCIACION','C243', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTO_ASOCIACION','C244', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_LICENCIA','C245', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_LICENCIA','C246', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_LICENCIA','C247', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_PERMUTA','C248', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_PERMUTA','C249', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_PERMUTA','C250', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_REPRESENTANTE','C251', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_REPRESENTANTE','C252', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_REPRESENTANTE','C253', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARGO_TITULAR','C254', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARGO_TITULAR','C255', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARGO_TITULAR','C256', '', 2, true, null, null,0); 



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PLANTILLA','C257', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PLANTILLA','C258', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PLANTILLA','C259', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ARCHIVO','C260', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ARCHIVO','C261', '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ARCHIVO','C262', '', 2, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RECURSO_EDUACTIVO','C263',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RECURSO_EDUACTIVO','C264',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RECURSO_EDUACTIVO','C265',  '', 2, true, null, null,0);
    


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_TRASLADO','C266',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_TRASLADO','C267',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_TRASLADO','C268',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEFINICION_TITULO','C269',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEFINICION_TITULO','C270',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEFINICION_TITULO','C271',  '', 2, true, null, null,0);


INSERT INTO catalogo.sg_configuraciones("con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version") VALUES ('TERMINOS_CONDICIONES', 'Términos y condiciones', '[Términos y condiciones]', 'terminos y condiciones', CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_configuraciones_aud ("con_pk","con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version", "rev", "revtype") select con_pk, con_codigo, con_nombre, con_valor, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, 1, 0 from catalogo.sg_configuraciones;

--v 2.0.0

-- Proyectos institucionales
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_proyectos_institucionales_pin_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_proyectos_institucionales (pin_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_proyectos_institucionales_pin_pk_seq'::regclass), pin_codigo character varying(4), pin_habilitado boolean, pin_nombre character varying(100), pin_nombre_busqueda character varying(100), pin_descripcion character varying(255), pin_ult_mod_fecha timestamp without time zone, pin_ult_mod_usuario character varying(45), pin_version integer, CONSTRAINT sg_proyectos_institucionales_pkey PRIMARY KEY (pin_pk), CONSTRAINT pin_codigo_uk UNIQUE (pin_codigo), CONSTRAINT pin_nombre_uk UNIQUE (pin_nombre));
    COMMENT ON TABLE  catalogo.sg_proyectos_institucionales IS 'Tabla para el registro de los proyetos institucionales.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_proyectos_institucionales_aud (pin_pk bigint NOT NULL, pin_codigo character varying(4), pin_habilitado boolean, pin_nombre character varying(100), pin_nombre_busqueda character varying(100), pin_descripcion character varying(255), pin_ult_mod_fecha timestamp without time zone, pin_ult_mod_usuario character varying(45), pin_version integer, rev bigint, revtype smallint, CONSTRAINT proyectos_institucionales_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Tipo de asociaciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_asociaciones_tas_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_asociaciones (tas_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_asociaciones_tas_pk_seq'::regclass), tas_codigo character varying(4), tas_habilitado boolean, tas_nombre character varying(100), tas_nombre_busqueda character varying(100), tas_descripcion character varying(255), tas_ult_mod_fecha timestamp without time zone, tas_ult_mod_usuario character varying(45), tas_version integer, CONSTRAINT sg_tipos_asociaciones_pkey PRIMARY KEY (tas_pk), CONSTRAINT tas_codigo_uk UNIQUE (tas_codigo), CONSTRAINT tas_nombre_uk UNIQUE (tas_nombre));
    COMMENT ON TABLE  catalogo.sg_tipos_asociaciones IS 'Tabla para el registro de los tipos de asociaciones.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_asociaciones.tas_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_asociaciones_aud (tas_pk bigint NOT NULL, tas_codigo character varying(4), tas_habilitado boolean, tas_nombre character varying(100), tas_nombre_busqueda character varying(100), tas_descripcion character varying(255), tas_ult_mod_fecha timestamp without time zone, tas_ult_mod_usuario character varying(45), tas_version integer, rev bigint, revtype smallint, CONSTRAINT tipos_asociaciones_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

--Dirección
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_direcciones_dir_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE catalogo.sg_direcciones(dir_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_direcciones_dir_pk_seq'::regclass), dir_direccion character varying(500), dir_departamento bigint, dir_municipio bigint, dir_canton bigint, dir_caserio bigint, dir_zona bigint, dir_latitud numeric(11,9), dir_longitud numeric(11,9), dir_ult_mod_fecha timestamp without time zone, dir_ult_mod_usuario character varying(45), dir_version integer, dir_caserio_texto character varying(255), dir_tipo_calle_fk bigint, CONSTRAINT sg_direcciones_pkey PRIMARY KEY (dir_pk), CONSTRAINT sg_direcciones_tipo_calle_fk FOREIGN KEY (dir_tipo_calle_fk) REFERENCES catalogo.sg_tipos_calle (tll_pk) MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT);
    COMMENT ON TABLE catalogo.sg_direcciones                        IS 'Tabla para el registro de direcciones.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_pk                IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_direccion         IS 'Campo donde se almacena la dirección';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_departamento      IS 'Lleve foránea con que se identifica el departamento donde se ubica la dirección.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_municipio         IS 'Lleve foránea con que se identifica el municipio donde se ubica la dirección.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_canton            IS 'Clave foránea que identifica el cantón donde se ubica la dirección.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_caserio           IS 'Clave foránea que identifica el caserío donde se ubica la dirección.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_zona              IS 'Clave foránea que identifica la zona en donde se ubica la dirección.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_latitud           IS 'Almacena el valor de latitud para la geolocalización';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_longitud          IS 'Almacena el valor de longitud para la geolocalización';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_ult_mod_fecha     IS 'Última fecha en la que fue modificado el registro.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_ult_mod_usuario   IS 'Último usuario en modificar el registro.';
    COMMENT ON COLUMN catalogo.sg_direcciones.dir_version           IS 'Versión del centros_educativos que se uso para el registo.';
CREATE TABLE catalogo.sg_direcciones_aud(dir_pk bigint NOT NULL, dir_direccion character varying(500), dir_departamento bigint, dir_municipio bigint, dir_canton bigint, dir_caserio bigint, dir_zona bigint, dir_latitud numeric(11,9), dir_longitud numeric(11,9), dir_ult_mod_fecha timestamp without time zone, dir_ult_mod_usuario character varying(45), dir_version integer, rev bigint,revtype smallint,dir_caserio_texto character varying(255),dir_tipo_calle_fk bigint,CONSTRAINT direcciones_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo (rev) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);

 
-- Asociaciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_asociaciones_aso_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_asociaciones (aso_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_asociaciones_aso_pk_seq'::regclass), aso_codigo character varying(4), aso_nombre character varying(100), aso_nombre_busqueda character varying(100), aso_descripcion character varying(255), aso_tipo_asociacion_fk bigint, aso_extranjera boolean, aso_habilitado boolean, aso_ejecuta_fondos_mined boolean, aso_nit CHARACTER VARYING(20), aso_nombre_representante_legal CHARACTER VARYING(100), aso_direccion_fk bigint, aso_ult_mod_fecha timestamp without time zone, aso_ult_mod_usuario character varying(45), aso_version integer, CONSTRAINT sg_asociaciones_pkey PRIMARY KEY (aso_pk), CONSTRAINT aso_codigo_uk UNIQUE (aso_codigo), CONSTRAINT aso_nombre_uk UNIQUE (aso_nombre),CONSTRAINT sg_tipos_asociaciones_fkey FOREIGN KEY (aso_tipo_asociacion_fk) REFERENCES catalogo.sg_tipos_asociaciones(tas_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_asociaciones_direccion_fkey FOREIGN KEY (aso_direccion_fk) REFERENCES catalogo.sg_direcciones(dir_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  catalogo.sg_asociaciones IS 'Tabla para el registro de las asociaciones.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_tipo_asociacion_fk IS 'Llave foránea que hace referencia al tipo de asociación.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_extranjera IS 'Indica si el registro es extranjera.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_ejecuta_fondos_mined IS 'Indica si el registro ejecuta fondos del MINED.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_nit IS 'Número de NIT del registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_nombre_representante_legal IS 'Nombre del representante legal.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_direccion_fk IS 'Llave foránea que hace referencia a la dirección.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_asociaciones.aso_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_asociaciones_aud (aso_pk bigint NOT NULL, aso_codigo character varying(4), aso_nombre character varying(100), aso_nombre_busqueda character varying(100), aso_descripcion character varying(255), aso_tipo_asociacion_fk bigint, aso_extranjera boolean, aso_habilitado boolean, aso_ejecuta_fondos_mined boolean, aso_nit CHARACTER VARYING(20), aso_nombre_representante_legal CHARACTER VARYING(100), aso_direccion_fk bigint, aso_ult_mod_fecha timestamp without time zone, aso_ult_mod_usuario character varying(45), aso_version integer, rev bigint, revtype smallint, CONSTRAINT asociaciones_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Telefono
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_telefonos_tel_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_telefonos (tel_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_telefonos_tel_pk_seq'::regclass), tel_asociaciones bigint, tel_telefono character varying(15), tel_tipo_telefono_fk bigint, tel_ult_mod_fecha timestamp without time zone, tel_ult_mod_usuario character varying(45), tel_version integer, CONSTRAINT sg_telefonos_pkey PRIMARY KEY (tel_pk), CONSTRAINT sg_telefonos_fkey_asociaciones FOREIGN KEY (tel_asociaciones) REFERENCES catalogo.sg_asociaciones(aso_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_telefonos_tipo_telefono_fkey FOREIGN KEY (tel_tipo_telefono_fk) REFERENCES catalogo.sg_tipos_telefono(tto_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  catalogo.sg_telefonos                         IS 'Tabla para el registro de los telefonos.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_asociaciones        IS 'Clave foránea que hace referencia a la asoación que pertenece.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_telefono            IS 'Número de teléfono';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_tipo_telefono_fk    IS 'Clave foránea que hace referencia al tipo de teléfono.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_telefonos.tel_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_telefonos_aud (tel_pk bigint NOT NULL, tel_asociaciones bigint, tel_telefono character varying(15), tel_tipo_telefono_fk bigint, tel_ult_mod_fecha timestamp without time zone, tel_ult_mod_usuario character varying(45), tel_version integer, rev bigint, revtype smallint, CONSTRAINT telefonos_asociaciones_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));



CREATE TABLE IF NOT EXISTS catalogo.sg_asociaciones_proyectos (aso_pk bigint NOT NULL,pin_pk bigint NOT NULL, CONSTRAINT sg_asociaciones_proyectos_asociaciones_fk FOREIGN KEY (aso_pk) REFERENCES catalogo.sg_asociaciones (aso_pk), CONSTRAINT sg_asociaciones_proyectos_proyecto_fk FOREIGN KEY (pin_pk) REFERENCES catalogo.sg_proyectos_institucionales (pin_pk));
CREATE TABLE IF NOT EXISTS catalogo.sg_asociaciones_proyectos_aud(aso_pk bigint NOT NULL,pin_pk bigint NOT NULL, rev bigint, revtype smallint,CONSTRAINT telefonos_asociaciones_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Motivos de licencia
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_licencia_mli_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_licencia (mli_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivos_licencia_mli_pk_seq'::regclass), mli_codigo character varying(4), mli_habilitado boolean, mli_nombre character varying(100), mli_nombre_busqueda character varying(100), mli_descripcion character varying(255), mli_ult_mod_fecha timestamp without time zone, mli_ult_mod_usuario character varying(45), mli_version integer, CONSTRAINT sg_motivos_licencia_pkey PRIMARY KEY (mli_pk), CONSTRAINT mli_codigo_uk UNIQUE (mli_codigo), CONSTRAINT mli_nombre_uk UNIQUE (mli_nombre));
    COMMENT ON TABLE  catalogo.sg_motivos_licencia IS 'Tabla para el registro de los motivos de licencia.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_licencia.mli_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_licencia_aud (mli_pk bigint NOT NULL, mli_codigo character varying(4), mli_habilitado boolean, mli_nombre character varying(100), mli_nombre_busqueda character varying(100), mli_descripcion character varying(255), mli_ult_mod_fecha timestamp without time zone, mli_ult_mod_usuario character varying(45), mli_version integer, rev bigint, revtype smallint, CONSTRAINT proyectos_institucionales_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Motivos de Permuta
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_permuta_mpe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_permuta (mpe_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivos_permuta_mpe_pk_seq'::regclass), mpe_codigo character varying(4), mpe_habilitado boolean, mpe_nombre character varying(100), mpe_nombre_busqueda character varying(100), mpe_descripcion character varying(255), mpe_ult_mod_fecha timestamp without time zone, mpe_ult_mod_usuario character varying(45), mpe_version integer, CONSTRAINT sg_motivos_permuta_pkey PRIMARY KEY (mpe_pk), CONSTRAINT mpe_codigo_uk UNIQUE (mpe_codigo), CONSTRAINT mpe_nombre_uk UNIQUE (mpe_nombre));
    COMMENT ON TABLE  catalogo.sg_motivos_permuta IS 'Tabla para el registro de los motivos de permuta.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_permuta.mpe_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_permuta_aud (mpe_pk bigint NOT NULL, mpe_codigo character varying(4), mpe_habilitado boolean, mpe_nombre character varying(100), mpe_nombre_busqueda character varying(100), mpe_descripcion character varying(255), mpe_ult_mod_fecha timestamp without time zone, mpe_ult_mod_usuario character varying(45), mpe_version integer, rev bigint, revtype smallint, CONSTRAINT proyectos_institucionales_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

ALTER TABLE catalogo.sg_configuraciones DROP COLUMN IF EXISTS con_habilitado;
ALTER TABLE catalogo.sg_configuraciones_aud DROP COLUMN IF EXISTS con_habilitado;

ALTER TABLE catalogo.sg_configuraciones ADD COLUMN IF NOT EXISTS con_valor text;
ALTER TABLE catalogo.sg_configuraciones_aud ADD COLUMN IF NOT EXISTS con_valor text;

--Recursos Educativos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_recursos_educativos_red_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_recursos_educativos(red_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_recursos_educativos_red_pk_seq'::regclass),red_codigo CHARACTER varying(4),red_habilitado BOOLEAN,red_nombre CHARACTER varying(255),red_nombre_busqueda CHARACTER varying(255),red_ult_mod_fecha timestamp without TIME zone,red_ult_mod_usuario CHARACTER varying(45),red_version INTEGER,CONSTRAINT sg_recurso_educativo_pkey PRIMARY KEY (red_pk),CONSTRAINT red_codigo_uk UNIQUE (red_codigo),CONSTRAINT red_nombre_uk UNIQUE (red_nombre));
    COMMENT ON TABLE catalogo.sg_recursos_educativos IS 'Tabla para el registro de los recursos educativos.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_codigo IS 'Código del sexo.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_nombre IS 'Nombre del sexo.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_nombre_busqueda IS 'Nombre del sexo normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_habilitado IS 'Indica si el sexo se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_recursos_educativos.red_version IS 'Versión del sexo.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_recursos_educativos_aud(red_pk bigint NOT NULL,red_codigo CHARACTER varying(4),red_habilitado BOOLEAN,red_nombre CHARACTER varying(255),red_nombre_busqueda CHARACTER varying(255),red_ult_mod_fecha timestamp without TIME zone,red_ult_mod_usuario CHARACTER varying(45),red_version INTEGER,rev bigint, revtype smallint);

CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_representante_tre_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_representante (tre_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_representante_tre_pk_seq'::regclass), tre_codigo character varying(45), tre_habilitado boolean, tre_nombre character varying(255), tre_nombre_busqueda character varying(255), tre_ult_mod_fecha timestamp without time zone, tre_ult_mod_usuario character varying(45), tre_version integer, CONSTRAINT sg_tipo_representante_pkey PRIMARY KEY (tre_pk), CONSTRAINT tre_codigo_uk UNIQUE (tre_codigo), CONSTRAINT tre_nombre_uk UNIQUE (tre_nombre));
COMMENT ON TABLE catalogo.sg_tipo_representante IS 'Tabla para el registro de tipos representante.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_representante_aud (tre_pk bigint NOT NULL, tre_codigo character varying(45), tre_habilitado boolean, tre_nombre character varying(255), tre_nombre_busqueda character varying(255), tre_ult_mod_fecha timestamp without time zone, tre_ult_mod_usuario character varying(45), tre_version integer, rev bigint, revtype smallint);


-- Cargos Titular
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_cargo_titular_cti_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_cargo_titular (cti_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_cargo_titular_cti_pk_seq'::regclass), cti_codigo character varying(45), cti_habilitado boolean, cti_nombre character varying(255), cti_nombre_busqueda character varying(255), cti_ult_mod_fecha timestamp without time zone, cti_ult_mod_usuario character varying(45), cti_version integer, CONSTRAINT sg_cargo_titular_pkey PRIMARY KEY (cti_pk), CONSTRAINT cti_codigo_uk UNIQUE (cti_codigo), CONSTRAINT cti_nombre_uk UNIQUE (cti_nombre));
COMMENT ON TABLE catalogo.sg_cargo_titular IS 'Tabla para el registro de cargos titular.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargo_titular.cti_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_cargo_titular_aud (cti_pk bigint NOT NULL, cti_codigo character varying(45), cti_habilitado boolean, cti_nombre character varying(255), cti_nombre_busqueda character varying(255), cti_ult_mod_fecha timestamp without time zone, cti_ult_mod_usuario character varying(45), cti_version integer, rev bigint, revtype smallint);

-- Archivos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_archivo_ach_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_archivo (ach_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_archivo_ach_pk_seq'::regclass), ach_nombre character varying(255), ach_descripcion character varying(255),ach_content_type character varying(255),ach_path character varying(255),ach_ext character varying(255), ach_ult_mod_fecha timestamp without time zone, ach_ult_mod_usuario character varying(45), ach_version integer, CONSTRAINT sg_archivo_pkey PRIMARY KEY (ach_pk), CONSTRAINT ach_nombre_uk UNIQUE (ach_nombre));
COMMENT ON TABLE catalogo.sg_archivo IS 'Tabla para el registro de archivos.';
COMMENT ON COLUMN catalogo.sg_archivo.ach_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_archivo.ach_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_archivo.ach_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_archivo.ach_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_archivo.ach_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_archivo_aud (ach_pk bigint NOT NULL,  ach_nombre character varying(255),ach_descripcion character varying(255),ach_content_type character varying(255),ach_path character varying(255),ach_ext character varying(255),  ach_ult_mod_fecha timestamp without time zone, ach_ult_mod_usuario character varying(45), ach_version integer, rev bigint, revtype smallint);


-- Plantillas
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_plantilla_pla_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_plantilla (pla_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_plantilla_pla_pk_seq'::regclass),pla_archivo_fk bigint, pla_codigo character varying(45), pla_habilitado boolean, pla_nombre character varying(255), pla_descripcion character varying(500), pla_nombre_busqueda character varying(255), pla_ult_mod_fecha timestamp without time zone, pla_ult_mod_usuario character varying(45), pla_version integer, CONSTRAINT sg_plantilla_pkey PRIMARY KEY (pla_pk), CONSTRAINT pla_codigo_uk UNIQUE (pla_codigo), CONSTRAINT pla_nombre_uk UNIQUE (pla_nombre),CONSTRAINT sg_pla_archivo_fkey FOREIGN KEY (pla_archivo_fk) REFERENCES catalogo.sg_archivo(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE catalogo.sg_plantilla IS 'Tabla para el registro de plantillas.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_plantilla.pla_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_plantilla_aud (pla_pk bigint NOT NULL,pla_archivo_fk bigint, pla_codigo character varying(45), pla_habilitado boolean, pla_nombre character varying(255), pla_nombre_busqueda character varying(255),pla_descripcion character varying(500), pla_ult_mod_fecha timestamp without time zone, pla_ult_mod_usuario character varying(45), pla_version integer, rev bigint, revtype smallint);


ALTER TABLE catalogo.sg_plantilla  ADD COLUMN pla_fecha_habilitada DATE;
COMMENT ON COLUMN catalogo.sg_plantilla.pla_fecha_habilitada IS 'Fecha en que fue habilitada la plantilla.';
ALTER TABLE catalogo.sg_plantilla_aud ADD COLUMN pla_fecha_habilitada DATE;

ALTER TABLE catalogo.sg_plantilla  ADD COLUMN pla_fecha_deshabilitada DATE;
COMMENT ON COLUMN catalogo.sg_plantilla.pla_fecha_deshabilitada IS 'Fecha en que fue habilitada la plantilla.';
ALTER TABLE catalogo.sg_plantilla_aud ADD COLUMN pla_fecha_deshabilitada DATE;

ALTER TABLE catalogo.sg_motivos_retiro ADD COLUMN mre_definitivo boolean;
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_definitivo IS 'Indica si el motivo de retiro es definitivo o no.';
ALTER TABLE catalogo.sg_motivos_retiro_aud ADD COLUMN mre_definitivo boolean;


ALTER TABLE catalogo.sg_motivos_retiro ADD COLUMN mre_traslado boolean;
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_traslado IS 'Indica si el motivo de retiro es de traslado.';
ALTER TABLE catalogo.sg_motivos_retiro_aud ADD COLUMN mre_traslado boolean;


ALTER TABLE catalogo.sg_motivos_retiro ADD COLUMN mre_cambiosecc boolean;
COMMENT ON COLUMN catalogo.sg_motivos_retiro.mre_cambiosecc IS 'Indica si el motivo de retiro es de cambio de sección.';
ALTER TABLE catalogo.sg_motivos_retiro_aud ADD COLUMN mre_cambiosecc boolean;


--Motivos traslado
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_traslado_mot_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_traslado(mot_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_motivos_traslado_mot_pk_seq'::regclass),mot_codigo CHARACTER varying(4),mot_habilitado BOOLEAN,mot_nombre CHARACTER varying(255),mot_nombre_busqueda CHARACTER varying(255),mot_ult_mod_fecha timestamp without TIME zone,mot_ult_mod_usuario CHARACTER varying(45),mot_version INTEGER,CONSTRAINT sg_motivos_traslado_pkey PRIMARY KEY (mot_pk),CONSTRAINT mot_codigo_uk UNIQUE (mot_codigo),CONSTRAINT mot_nombre_uk UNIQUE (mot_nombre));
    COMMENT ON TABLE catalogo.sg_motivos_traslado IS 'Tabla para el registro de los motivos de traslado.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_traslado.mot_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_motivos_traslado_aud(mot_pk bigint NOT NULL,mot_codigo CHARACTER varying(4),mot_habilitado BOOLEAN,mot_nombre CHARACTER varying(255),mot_nombre_busqueda CHARACTER varying(255),mot_ult_mod_fecha timestamp without TIME zone,mot_ult_mod_usuario CHARACTER varying(45),mot_version INTEGER,rev bigint, revtype smallint, CONSTRAINT proyectos_institucionales_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


--v 2.0.1

-- Definiciones Titulo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_definiciones_titulo_dti_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_definiciones_titulo (dti_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_definiciones_titulo_dti_pk_seq'::regclass), dti_codigo character varying(45), dti_habilitado boolean, dti_nombre character varying(255), dti_nombre_busqueda character varying(255), dti_ult_mod_fecha timestamp without time zone, dti_ult_mod_usuario character varying(45), dti_version integer, CONSTRAINT sg_definiciones_titulo_pkey PRIMARY KEY (dti_pk), CONSTRAINT dti_codigo_uk UNIQUE (dti_codigo), CONSTRAINT dti_nombre_uk UNIQUE (dti_nombre));
COMMENT ON TABLE catalogo.sg_definiciones_titulo IS 'Tabla para el registro de definiciones titulo.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_definiciones_titulo_aud (dti_pk bigint NOT NULL, dti_codigo character varying(45), dti_habilitado boolean, dti_nombre character varying(255), dti_nombre_busqueda character varying(255), dti_ult_mod_fecha timestamp without time zone, dti_ult_mod_usuario character varying(45), dti_version integer, rev bigint, revtype smallint);


ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_fecha_desde date;
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_fecha_desde IS 'Fecha desde del registro.';
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_fecha_desde date;

ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_fecha_hasta date;
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_fecha_hasta IS 'Fecha hasta del registro.';
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_fecha_hasta date;

ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_plantilla_fk bigint;
COMMENT ON COLUMN catalogo.sg_definiciones_titulo.dti_plantilla_fk IS 'Llave foránea que hace referencia a la calificacion.';
ALTER TABLE catalogo.sg_definiciones_titulo ADD CONSTRAINT sg_dti_plantilla_fk FOREIGN KEY (dti_plantilla_fk) REFERENCES catalogo.sg_plantilla(pla_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_plantilla_fk bigint;

-- Tipos Riesgo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_riesgo_tri_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_riesgo (tri_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_riesgo_tri_pk_seq'::regclass), tri_codigo character varying(45), tri_habilitado boolean, tri_nombre character varying(255), tri_nombre_busqueda character varying(255), tri_ult_mod_fecha timestamp without time zone, tri_ult_mod_usuario character varying(45), tri_version integer, CONSTRAINT sg_tipos_riesgo_pkey PRIMARY KEY (tri_pk), CONSTRAINT tri_codigo_uk UNIQUE (tri_codigo), CONSTRAINT tri_nombre_uk UNIQUE (tri_nombre));
COMMENT ON TABLE catalogo.sg_tipos_riesgo IS 'Tabla para el registro de tipos riesgo.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_riesgo_aud (tri_pk bigint NOT NULL, tri_codigo character varying(45), tri_habilitado boolean, tri_nombre character varying(255), tri_nombre_busqueda character varying(255), tri_ult_mod_fecha timestamp without time zone, tri_ult_mod_usuario character varying(45), tri_version integer, rev bigint, revtype smallint);
-- Grado Afectacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_grados_afectacion_gaf_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_grados_afectacion (gaf_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_grados_afectacion_gaf_pk_seq'::regclass), gaf_codigo character varying(45), gaf_habilitado boolean, gaf_nombre character varying(255), gaf_nombre_busqueda character varying(255), gaf_ult_mod_fecha timestamp without time zone, gaf_ult_mod_usuario character varying(45), gaf_version integer, CONSTRAINT sg_grados_afectacion_pkey PRIMARY KEY (gaf_pk), CONSTRAINT gaf_codigo_uk UNIQUE (gaf_codigo), CONSTRAINT gaf_nombre_uk UNIQUE (gaf_nombre));
COMMENT ON TABLE catalogo.sg_grados_afectacion IS 'Tabla para el registro de grado afectacion.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_grados_afectacion.gaf_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_grados_afectacion_aud (gaf_pk bigint NOT NULL, gaf_codigo character varying(45), gaf_habilitado boolean, gaf_nombre character varying(255), gaf_nombre_busqueda character varying(255), gaf_ult_mod_fecha timestamp without time zone, gaf_ult_mod_usuario character varying(45), gaf_version integer, rev bigint, revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_GRADO_AFECTACION','C272',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_GRADO_AFECTACION','C273',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_GRADO_AFECTACION','C274',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_RIESGO','C275',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_RIESGO','C276',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_RIESGO','C277',  '', 2, true, null, null,0);

-- 2.1.1

--Acciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_acciones_acc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_acciones(acc_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_acciones_acc_pk_seq'::regclass), acc_num INTEGER, acc_estado_origen CHARACTER VARYING(45), acc_estado_destino CHARACTER VARYING(45), acc_nombre_accion CHARACTER VARYING(45), acc_ult_mod_fecha timestamp without TIME zone, acc_ult_mod_usuario CHARACTER varying(45), acc_version INTEGER, CONSTRAINT sg_acciones_pkey PRIMARY KEY (acc_pk), CONSTRAINT acc_campos_uk UNIQUE (acc_num,acc_estado_origen,acc_estado_destino,acc_nombre_accion));
    COMMENT ON TABLE catalogo.sg_acciones IS 'Tabla para el registro de las acciones.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_num IS 'Número de la acción.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_estado_origen IS 'Estado origen del registro.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_estado_destino IS 'Estado destino del registro.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_nombre_accion IS 'Acción a realizar.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_acciones.acc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    catalogo.sg_acciones_aud(acc_pk bigint NOT NULL, acc_num INTEGER, acc_estado_origen CHARACTER VARYING(45), acc_estado_destino CHARACTER VARYING(45), acc_nombre_accion CHARACTER VARYING(45), acc_ult_mod_fecha timestamp without TIME zone,acc_ult_mod_usuario CHARACTER varying(45),acc_version INTEGER,rev bigint, revtype smallint, CONSTRAINT proyectos_institucionales_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACCION','C278',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACCION','C279',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACCION','C280',  '', 2, true, null, null,0);

INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('TITULO_ESTUDIANTE', 'Título estudiante', 'titulo estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('CARNET_ESTUDIANTE', 'Carnet estudiante', 'carnet estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla_aud ("pla_pk", "pla_codigo", "pla_nombre", "pla_nombre_busqueda",  "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version", "rev", "revtype") select pla_pk, pla_codigo, pla_nombre, pla_nombre_busqueda, pla_habilitado, pla_ult_mod_fecha, pla_ult_mod_usuario, pla_version, 1, 0 from catalogo.sg_plantilla;

INSERT INTO catalogo.sg_acciones (acc_num,acc_estado_origen,acc_estado_destino,acc_nombre_accion,acc_ult_mod_fecha,acc_ult_mod_usuario,acc_version) VALUES
(1,'PENDIENTE','AUTORIZADA','AUTORIZAR','2018-11-26 15:07:46.923',NULL,0)
,(2,'PENDIENTE','RECHAZADA','RECHAZAR','2018-11-26 15:08:19.096',NULL,0)
,(3,'PENDIENTE_RESOLUCION','AUTORIZADA','AUTORIZAR','2018-11-26 15:08:54.285',NULL,0)
,(4,'PENDIENTE_RESOLUCION','RECHAZADA','RECHAZAR','2018-11-26 15:09:13.371',NULL,0)
,(5,'AUTORIZADA','CONFIRMADO','CONFIRMAR','2018-11-26 15:10:27.658',NULL,0)
,(6,'AUTORIZADA','ANULADO','ANULAR','2018-11-26 15:10:49.244',NULL,0)
,(7,'RECHAZADA','RECHAZADA','-','2018-11-26 15:11:20.364',NULL,0)
,(8,'CONFIRMADO','CONFIRMADO','-','2018-11-26 15:11:36.078',NULL,0)
,(9,'ANULADO','ANULADO','-','2018-11-26 15:11:51.547',NULL,0);

INSERT INTO catalogo.sg_acciones_aud ("rev", "revtype", "acc_pk", "acc_num", "acc_estado_origen", "acc_estado_destino", "acc_nombre_accion", "acc_ult_mod_fecha", "acc_ult_mod_usuario", "acc_version") select 1, 0, acc_pk, acc_num,acc_estado_origen,acc_estado_destino,acc_nombre_accion,acc_ult_mod_fecha,acc_ult_mod_usuario,acc_version from catalogo.sg_acciones;

-- 2.2.1

ALTER TABLE catalogo.sg_tipos_trabajo ADD CONSTRAINT ttr_nombre_uk UNIQUE (ttr_nombre_busqueda);
ALTER TABLE catalogo.sg_motivos_retiro ADD CONSTRAINT mre_nombre_uk UNIQUE (mre_nombre_busqueda);
CREATE UNIQUE INDEX min_codigo_uk_idx on catalogo.sg_motivos_inasistencia (LOWER(min_codigo));  
ALTER TABLE catalogo.sg_motivos_inasistencia ADD CONSTRAINT min_nombre_uk UNIQUE (min_nombre_busqueda);
ALTER TABLE catalogo.sg_profesiones ADD CONSTRAINT pro_nombre_uk UNIQUE (pro_nombre_busqueda);
ALTER TABLE catalogo.sg_dependencias_economica ADD CONSTRAINT dec_nombre_uk UNIQUE (dec_nombre_busqueda);
ALTER TABLE catalogo.sg_discapacidades ADD CONSTRAINT dis_nombre_uk UNIQUE (dis_nombre_busqueda);
ALTER TABLE catalogo.sg_escolaridades ADD CONSTRAINT esc_nombre_uk UNIQUE (esc_nombre_busqueda);
CREATE UNIQUE INDEX mun_codigo_uk_idx on catalogo.sg_municipios (LOWER(mun_codigo));  
CREATE UNIQUE INDEX can_codigo_uk_idx on catalogo.sg_cantones (LOWER(can_codigo));
CREATE UNIQUE INDEX glo_codigo_uk_idx on catalogo.sg_glosarios (LOWER(glo_codigo));

ALTER TABLE  catalogo.sg_tipos_vacuna_aud  ALTER COLUMN tvc_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_tipos_valoracion_aud  ALTER COLUMN tva_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_tipos_parentesco  ALTER COLUMN tpa_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_tipos_parentesco_aud  ALTER COLUMN tpa_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_tipos_manifestacion_aud  ALTER COLUMN tma_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_sectores_economicos_aud  ALTER COLUMN sec_codigo TYPE varchar(45);
ALTER TABLE  catalogo.sg_ocupaciones_aud  ALTER COLUMN ocu_codigo TYPE varchar(45);

-- 2.2.2

INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('CALIFICACIONES_ESTUDIANTE', 'Calificaciones estudiante', 'calificaciones estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla("pla_codigo", "pla_nombre", "pla_nombre_busqueda", "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version") VALUES ('PROMOCION_ESTUDIANTE', 'Promoción estudiante', 'promocion estudiante', true, CURRENT_TIMESTAMP, 'admin', '0');
INSERT INTO catalogo.sg_plantilla_aud ("pla_pk", "pla_codigo", "pla_nombre", "pla_nombre_busqueda",  "pla_habilitado", "pla_ult_mod_fecha", "pla_ult_mod_usuario", "pla_version", "rev", "revtype") select pla_pk, pla_codigo, pla_nombre, pla_nombre_busqueda, pla_habilitado, pla_ult_mod_fecha, pla_ult_mod_usuario, pla_version, 1, 0 from catalogo.sg_plantilla where pla_codigo = 'CALIFICACIONES_ESTUDIANTE' or pla_codigo = 'PROMOCION_ESTUDIANTE';


-- 2.3.0

-- Reportes
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_reportes_rep_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_reportes (rep_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_reportes_rep_pk_seq'::regclass), rep_codigo character varying(45), rep_habilitado boolean, rep_nombre character varying(255), rep_nombre_busqueda character varying(255), rep_descripcion character varying(1000), rep_operacion_asociada character varying(100), rep_plantilla_fk bigint, rep_ult_mod_fecha timestamp without time zone, rep_ult_mod_usuario character varying(45), rep_version integer, CONSTRAINT sg_reportes_pkey PRIMARY KEY (rep_pk), CONSTRAINT rep_codigo_uk UNIQUE (rep_codigo), CONSTRAINT rep_nombre_uk UNIQUE (rep_nombre));
COMMENT ON TABLE catalogo.sg_reportes IS 'Tabla para el registro de reportes.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_descripcion IS 'Descripción del registro.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_operacion_asociada IS 'Operación asociada al reporte. Si el usuario la tiene podrá generarlo.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_plantilla_fk IS 'Clave foránea que hace referencia a la plantilla del reporte.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_reportes.rep_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_reportes.rep_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_reportes_aud (rep_pk bigint NOT NULL, rep_codigo character varying(45), rep_habilitado boolean, rep_nombre character varying(255), rep_nombre_busqueda character varying(255), rep_descripcion character varying(1000), rep_operacion_asociada character varying(100), rep_plantilla_fk bigint, rep_ult_mod_fecha timestamp without time zone, rep_ult_mod_usuario character varying(45), rep_version integer, rev bigint, revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REPORTE','C281',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REPORTE','C282',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REPORTE','C283',  '', 2, true, null, null,0);

-- 2.4.0

-- Formulas
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_formulas_fom_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_formulas (fom_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_formulas_fom_pk_seq'::regclass), fom_codigo character varying(45), fom_habilitado boolean, fom_nombre character varying(255), fom_nombre_busqueda character varying(255), fom_texto_largo character varying(4000), fom_descripcion character varying(255) , fom_ult_mod_fecha timestamp without time zone, fom_ult_mod_usuario character varying(45), fom_version integer, CONSTRAINT sg_formulas_pkey PRIMARY KEY (fom_pk), CONSTRAINT fom_codigo_uk UNIQUE (fom_codigo), CONSTRAINT fom_nombre_uk UNIQUE (fom_nombre));
    COMMENT ON TABLE catalogo.sg_formulas IS 'Tabla para el registro de fórmulas.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_texto_largo IS 'Texto largo del registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_formulas.fom_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_formulas_aud (fom_pk bigint NOT NULL, fom_codigo character varying(45), fom_habilitado boolean, fom_nombre character varying(255), fom_nombre_busqueda character varying(255), fom_texto_largo character varying(4000), fom_descripcion character varying(255), fom_ult_mod_fecha timestamp without time zone, fom_ult_mod_usuario character varying(45), fom_version integer, rev bigint, revtype smallint);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FORMULAS','C284',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FORMULAS','C285',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FORMULAS','C286',  '', 2, true, null, null,0);


-- 2.4.1

ALTER TABLE catalogo.sg_acciones_aud ADD PRIMARY KEY (acc_pk,rev);
ALTER TABLE catalogo.sg_afp_aud ADD PRIMARY KEY (afp_pk,rev);
ALTER TABLE catalogo.sg_alcance_capacitacion_aud ADD PRIMARY KEY (aca_pk,rev);
ALTER TABLE catalogo.sg_archivo_aud ADD PRIMARY KEY (ach_pk,rev);
ALTER TABLE catalogo.sg_areas_asignatura_modulo_aud ADD PRIMARY KEY (aam_pk,rev);
ALTER TABLE catalogo.sg_areas_indicador_aud ADD PRIMARY KEY (ari_pk,rev);
ALTER TABLE catalogo.sg_asociaciones_aud ADD PRIMARY KEY (aso_pk,rev);
ALTER TABLE catalogo.sg_asociaciones_proyectos_aud ADD PRIMARY KEY (aso_pk, pin_pk, rev);
ALTER TABLE catalogo.sg_ayudas_aud ADD PRIMARY KEY (ayu_pk,rev);
ALTER TABLE catalogo.sg_calificaciones_aud ADD PRIMARY KEY (cal_pk,rev);
ALTER TABLE catalogo.sg_cantones_aud ADD PRIMARY KEY (can_pk,rev);
ALTER TABLE catalogo.sg_cargo_aud ADD PRIMARY KEY (car_pk,rev);
ALTER TABLE catalogo.sg_cargo_titular_aud ADD PRIMARY KEY (cti_pk,rev);
ALTER TABLE catalogo.sg_carreras_aud ADD PRIMARY KEY (crr_pk,rev);
ALTER TABLE catalogo.sg_caserios_aud ADD PRIMARY KEY (cas_pk,rev);
ALTER TABLE catalogo.sg_categoria_empleado_aud ADD PRIMARY KEY (cem_pk,rev);
ALTER TABLE catalogo.sg_categoria_escalafon_aud ADD PRIMARY KEY (ces_pk,rev);
ALTER TABLE catalogo.sg_categoria_formacion_docente_aud ADD PRIMARY KEY (cfd_pk,rev);
ALTER TABLE catalogo.sg_categoria_violencia_aud ADD PRIMARY KEY (cav_pk,rev);
ALTER TABLE catalogo.sg_clasificaciones_aud ADD PRIMARY KEY (cla_pk,rev);
ALTER TABLE catalogo.sg_configuraciones_aud ADD PRIMARY KEY (con_pk,rev);
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD PRIMARY KEY (dti_pk,rev);
ALTER TABLE catalogo.sg_departamentos_aud ADD PRIMARY KEY (dep_pk,rev);
ALTER TABLE catalogo.sg_dependencias_economica_aud ADD PRIMARY KEY (dec_pk,rev);
ALTER TABLE catalogo.sg_direcciones_aud ADD PRIMARY KEY (dir_pk,rev);
ALTER TABLE catalogo.sg_discapacidades_aud ADD PRIMARY KEY (dis_pk,rev);
ALTER TABLE catalogo.sg_enfer_no_transm_aud ADD PRIMARY KEY (ent_pk,rev);
ALTER TABLE catalogo.sg_escalas_calificacion_aud ADD PRIMARY KEY (eca_pk,rev);
ALTER TABLE catalogo.sg_escolaridades_aud ADD PRIMARY KEY (esc_pk,rev);
ALTER TABLE catalogo.sg_especialidades_aud ADD PRIMARY KEY (esp_pk,rev);
ALTER TABLE catalogo.sg_estados_civil_aud ADD PRIMARY KEY (eci_pk,rev);
ALTER TABLE catalogo.sg_etiquetas_aud ADD PRIMARY KEY (eti_pk,rev);
ALTER TABLE catalogo.sg_etnias_aud ADD PRIMARY KEY (etn_pk,rev);
ALTER TABLE catalogo.sg_fuente_financiamiento_aud ADD PRIMARY KEY (ffi_pk,rev);
ALTER TABLE catalogo.sg_glosarios_aud ADD PRIMARY KEY (glo_pk,rev);
ALTER TABLE catalogo.sg_grados_afectacion_aud ADD PRIMARY KEY (gaf_pk,rev);
ALTER TABLE catalogo.sg_idiomas_aud ADD PRIMARY KEY (idi_pk,rev);
ALTER TABLE catalogo.sg_institucion_paga_salario_aud ADD PRIMARY KEY (ips_pk,rev);
ALTER TABLE catalogo.sg_instituciones_aud ADD PRIMARY KEY (ins_pk,rev);
ALTER TABLE catalogo.sg_jornadas_laborales_aud ADD PRIMARY KEY (jla_pk,rev);
ALTER TABLE catalogo.sg_ley_salario_aud ADD PRIMARY KEY (lsa_pk,rev);
ALTER TABLE catalogo.sg_medios_transporte_aud ADD PRIMARY KEY (mtr_pk,rev);
ALTER TABLE catalogo.sg_mensajes_aud ADD PRIMARY KEY (msj_pk,rev);
ALTER TABLE catalogo.sg_modalidades_atencion_aud ADD PRIMARY KEY (mat_pk,rev);
ALTER TABLE catalogo.sg_modalidades_estudio_aud ADD PRIMARY KEY (mes_pk,rev);
ALTER TABLE catalogo.sg_modo_pago_aud ADD PRIMARY KEY (mpa_pk,rev);
ALTER TABLE catalogo.sg_modulo_formacion_docente_aud ADD PRIMARY KEY (mfd_pk,rev);
ALTER TABLE catalogo.sg_motivos_egreso_aud ADD PRIMARY KEY (meg_pk,rev);
ALTER TABLE catalogo.sg_motivos_inasistencia_aud ADD PRIMARY KEY (min_pk,rev);
ALTER TABLE catalogo.sg_motivos_licencia_aud ADD PRIMARY KEY (mli_pk,rev);
ALTER TABLE catalogo.sg_motivos_permuta_aud ADD PRIMARY KEY (mpe_pk,rev);
ALTER TABLE catalogo.sg_motivos_retiro_aud ADD PRIMARY KEY (mre_pk,rev);
ALTER TABLE catalogo.sg_motivos_traslado_aud ADD PRIMARY KEY (mot_pk,rev);
ALTER TABLE catalogo.sg_municipios_aud ADD PRIMARY KEY (mun_pk,rev);
ALTER TABLE catalogo.sg_nacionalidades_aud ADD PRIMARY KEY (nac_pk,rev);
ALTER TABLE catalogo.sg_nivel_empleado_aud ADD PRIMARY KEY (nem_pk,rev);
ALTER TABLE catalogo.sg_nivel_escalafon_aud ADD PRIMARY KEY (nes_pk,rev);
ALTER TABLE catalogo.sg_nivel_formacion_docente_aud ADD PRIMARY KEY (nfd_pk,rev);
ALTER TABLE catalogo.sg_niveles_idioma_aud ADD PRIMARY KEY (nid_pk,rev);
ALTER TABLE catalogo.sg_ocupaciones_aud ADD PRIMARY KEY (ocu_pk,rev);
ALTER TABLE catalogo.sg_paises_aud ADD PRIMARY KEY (pai_pk,rev);
ALTER TABLE catalogo.sg_plantilla_aud ADD PRIMARY KEY (pla_pk,rev);
ALTER TABLE catalogo.sg_profesiones_aud ADD PRIMARY KEY (pro_pk,rev);
ALTER TABLE catalogo.sg_programas_educativos_aud ADD PRIMARY KEY (ped_pk,rev);
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD PRIMARY KEY (pin_pk,rev);
ALTER TABLE catalogo.sg_recursos_educativos_aud ADD PRIMARY KEY (red_pk,rev);
ALTER TABLE catalogo.sg_sectores_economicos_aud ADD PRIMARY KEY (sec_pk,rev);
ALTER TABLE catalogo.sg_servicios_infraestructura_aud ADD PRIMARY KEY (sin_pk,rev);
ALTER TABLE catalogo.sg_sexos_aud ADD PRIMARY KEY (sex_pk,rev);
ALTER TABLE catalogo.sg_sistemas_gestion_contenido_aud ADD PRIMARY KEY (sgc_pk,rev);
ALTER TABLE catalogo.sg_sub_modalidades_aud ADD PRIMARY KEY (smo_pk,rev);
ALTER TABLE catalogo.sg_telefonos_aud ADD PRIMARY KEY (tel_pk,rev);
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD PRIMARY KEY (tco_pk,rev);
ALTER TABLE catalogo.sg_tipo_institucion_paga_aud ADD PRIMARY KEY (tip_pk,rev);
ALTER TABLE catalogo.sg_tipo_nombramiento_aud ADD PRIMARY KEY (tno_pk,rev);
ALTER TABLE catalogo.sg_tipo_representante_aud ADD PRIMARY KEY (tre_pk,rev);
ALTER TABLE catalogo.sg_tipos_asociaciones_aud ADD PRIMARY KEY (tas_pk,rev);
ALTER TABLE catalogo.sg_tipos_calendario_escolar_aud ADD PRIMARY KEY (tce_pk,rev);
ALTER TABLE catalogo.sg_tipos_calle_aud ADD PRIMARY KEY (tll_pk,rev);
ALTER TABLE catalogo.sg_tipos_capacitacion_aud ADD PRIMARY KEY (tca_pk,rev);
ALTER TABLE catalogo.sg_tipos_estudio_aud ADD PRIMARY KEY (tes_pk,rev);
ALTER TABLE catalogo.sg_tipos_identificacion_aud ADD PRIMARY KEY (tin_pk,rev);
ALTER TABLE catalogo.sg_tipos_manifestacion_aud ADD PRIMARY KEY (tma_pk,rev);
ALTER TABLE catalogo.sg_tipos_organismo_administrativo_aud ADD PRIMARY KEY (toa_pk,rev);
ALTER TABLE catalogo.sg_tipos_parentesco_aud ADD PRIMARY KEY (tpa_pk,rev);
ALTER TABLE catalogo.sg_tipos_riesgo_aud ADD PRIMARY KEY (tri_pk,rev);
ALTER TABLE catalogo.sg_tipos_sangre_aud ADD PRIMARY KEY (tsa_pk,rev);
ALTER TABLE catalogo.sg_tipos_servicio_sanitario_aud ADD PRIMARY KEY (tss_pk,rev);
ALTER TABLE catalogo.sg_tipos_telefono_aud ADD PRIMARY KEY (tto_pk,rev);
ALTER TABLE catalogo.sg_tipos_trabajo_aud ADD PRIMARY KEY (ttr_pk,rev);
ALTER TABLE catalogo.sg_tipos_vacuna_aud ADD PRIMARY KEY (tvc_pk,rev);
ALTER TABLE catalogo.sg_tipos_valoracion_aud ADD PRIMARY KEY (tva_pk,rev);
ALTER TABLE catalogo.sg_zonas_aud ADD PRIMARY KEY (zon_pk,rev);
ALTER TABLE catalogo.sg_formulas_aud ADD PRIMARY KEY (fom_pk,rev);

-- 2.4.2


ALTER TABLE  catalogo.sg_glosarios  ALTER COLUMN glo_nombre TYPE TEXT;
ALTER TABLE  catalogo.sg_glosarios_aud  ALTER COLUMN glo_nombre TYPE TEXT;
ALTER TABLE  catalogo.sg_glosarios  ALTER COLUMN glo_nombre_busqueda TYPE TEXT;
ALTER TABLE  catalogo.sg_glosarios_aud  ALTER COLUMN glo_nombre_busqueda TYPE TEXT;
ALTER TABLE  catalogo.sg_glosarios  ALTER COLUMN glo_fuente TYPE TEXT;
ALTER TABLE  catalogo.sg_glosarios_aud  ALTER COLUMN glo_fuente TYPE TEXT;


-- 2.4.3

COMMENT ON COLUMN catalogo.sg_cantones.can_municipio_fk IS 'Clave foránea al municipio al que pertenece el cantón.';
COMMENT ON COLUMN catalogo.sg_cantones.can_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_cantones.can_version IS 'Versión del registro.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_nombre IS 'Nombre del caserío.';
COMMENT ON COLUMN catalogo.sg_caserios.cas_version IS 'Versión del registo.';
COMMENT ON TABLE  catalogo.sg_clasificaciones IS 'Tabla para el registro de clasificaciones de sedes.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_departamentos.dep_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON TABLE  catalogo.sg_dependencias_economica IS 'Tabla para el registro de las dependencias económicas';
COMMENT ON COLUMN catalogo.sg_escalas_calificacion.eca_tipo_escala IS 'Tipo de escala de calificación, toma los valores NUMÉRICA o CONCEPTUAL.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_departamento_fk IS 'Clave foránea al departamento al que pertenece el municipio.';
COMMENT ON COLUMN catalogo.sg_municipios.mun_version IS 'Versión del registro.';
COMMENT ON COLUMN catalogo.sg_paises.pai_version IS 'Versión del registro.';
COMMENT ON COLUMN catalogo.sg_profesiones.pro_nombre_busqueda IS 'Nombre de la profesión normalizado para búsquedas.';
COMMENT ON TABLE  catalogo.sg_sub_modalidades IS 'Tabla para el registro de las sub modalidades de atención.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_telefono.tto_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_zonas.zon_version IS 'Versión del registro.';


ALTER TABLE catalogo.sg_configuraciones ADD COLUMN IF NOT EXISTS con_es_editor BOOLEAN;
COMMENT ON COLUMN catalogo.sg_configuraciones.con_es_editor IS 'Indica si el valor a ingresar es por medio de un editor de texto o texto plano.';
ALTER TABLE catalogo.sg_configuraciones_aud ADD COLUMN IF NOT EXISTS con_es_editor BOOLEAN;

-- 2.4.5

ALTER TABLE catalogo.sg_escolaridades
ALTER COLUMN esc_codigo type CHARACTER VARYING(6);
ALTER TABLE catalogo.sg_escolaridades_aud
ALTER COLUMN esc_codigo type CHARACTER VARYING(6);

-- 3.0.0

-- Programas Institucionales
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_programas_institucional_pin_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_programas_institucional (pin_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_programas_institucional_pin_pk_seq'::regclass), pin_codigo character varying(45), pin_habilitado boolean, pin_nombre character varying(255), pin_nombre_busqueda character varying(255), pin_ult_mod_fecha timestamp without time zone, pin_ult_mod_usuario character varying(45), pin_version integer, CONSTRAINT sg_programas_institucional_pkey PRIMARY KEY (pin_pk), CONSTRAINT pins_codigo_uk UNIQUE (pin_codigo), CONSTRAINT pins_nombre_uk UNIQUE (pin_nombre));
    COMMENT ON TABLE catalogo.sg_programas_institucional IS 'Tabla para el registro de programas institucionales.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_programas_institucional.pin_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_programas_institucional_aud (pin_pk bigint NOT NULL, pin_codigo character varying(45), pin_habilitado boolean, pin_nombre character varying(255), pin_nombre_busqueda character varying(255), pin_ult_mod_fecha timestamp without time zone, pin_ult_mod_usuario character varying(45), pin_version integer, rev bigint, revtype smallint, CONSTRAINT sg_programas_institucional_aud_pkey PRIMARY KEY (pin_pk,rev),  CONSTRAINT sg_programas_institucional_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN IF NOT EXISTS pin_programa_institucional_fk bigint;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD CONSTRAINT sg_proyectos_institucionaes_programa_institucional_fk FOREIGN KEY (pin_programa_institucional_fk) REFERENCES catalogo.sg_programas_institucional(pin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN IF NOT EXISTS pin_fecha_inicio date;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN IF NOT EXISTS pin_fecha_fin date;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN IF NOT EXISTS pin_anio integer;
COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_programa_institucional_fk IS 'Llave foranea que hace referencia al programa institucional.';
COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_fecha_inicio IS 'Fecha de inicio del registro.';
COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_fecha_fin IS 'Fecha fin del registro.';
COMMENT ON COLUMN catalogo.sg_proyectos_institucionales.pin_anio IS 'Año del registro.';
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN IF NOT EXISTS pin_programa_institucional_fk bigint;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN IF NOT EXISTS pin_fecha_inicio date;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN IF NOT EXISTS pin_fecha_fin date;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN IF NOT EXISTS pin_anio integer;


-- Componentes
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_componentes_cpn_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_componentes (cpn_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_componentes_cpn_pk_seq'::regclass), cpn_proyecto_institucional_fk bigint, cpn_nombre character varying(100), cpn_descripcion character varying(255), cpn_ult_mod_fecha timestamp without time zone, cpn_ult_mod_usuario character varying(45), cpn_version integer, CONSTRAINT sg_componentes_pkey PRIMARY KEY (cpn_pk), CONSTRAINT sg_componentes_proyecto_institucional_fk FOREIGN KEY (cpn_proyecto_institucional_fk) REFERENCES catalogo.sg_proyectos_institucionales(pin_pk));
    COMMENT ON TABLE catalogo.sg_componentes IS 'Tabla para el registro de componentes.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_proyecto_institucional_fk IS 'Llave foranea que hace referencia al proyecto institucional al cual pertenece.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_componentes.cpn_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_componentes_aud (cpn_pk bigint NOT NULL, cpn_proyecto_institucional_fk bigint, cpn_nombre character varying(100), cpn_descripcion character varying(255), cpn_ult_mod_fecha timestamp without time zone, cpn_ult_mod_usuario character varying(45), cpn_version integer, rev bigint, revtype smallint, CONSTRAINT sg_componentes_aud_pkey PRIMARY KEY (cpn_pk,rev),  CONSTRAINT sg_componentes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Beneficios
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_beneficios_bnf_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_beneficios (bnf_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_beneficios_bnf_pk_seq'::regclass), bnf_proyecto_institucional_fk bigint, bnf_nombre character varying(100), bnf_descripcion character varying(255), bnf_objetivos character varying(500), bnf_tipo_beneficio character varying(45), bnf_cantidad_anual integer,bnf_ult_mod_fecha timestamp without time zone, bnf_ult_mod_usuario character varying(45), bnf_version integer, CONSTRAINT sg_beneficios_pkey PRIMARY KEY (bnf_pk), CONSTRAINT sg_beneficios_proyectos_institucionales_fk FOREIGN KEY (bnf_proyecto_institucional_fk) REFERENCES catalogo.sg_proyectos_institucionales(pin_pk));
    COMMENT ON TABLE catalogo.sg_beneficios IS 'Tabla para el registro de beneficios.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_proyecto_institucional_fk IS 'Llave foranea que hace referencia al proyecto institucional al cual pertenece.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_objetivos IS 'Objetivos del registro.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_tipo_beneficio IS 'Tipo de registro';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_cantidad_anual IS 'Cantidad de veces que se puede otorgar al año';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_beneficios.bnf_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_beneficios_aud (bnf_pk bigint NOT NULL, bnf_proyecto_institucional_fk bigint, bnf_nombre character varying(100), bnf_descripcion character varying(255), bnf_objetivos character varying(500), bnf_tipo_beneficio character varying(45), bnf_cantidad_anual integer,bnf_ult_mod_fecha timestamp without time zone, bnf_ult_mod_usuario character varying(45), bnf_version integer, rev bigint, revtype smallint, CONSTRAINT sg_beneficios_aud_pkey PRIMARY KEY (bnf_pk,rev),  CONSTRAINT sg_beneficios_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Tipos de Acciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_accion_tac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_accion (tac_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_accion_tac_pk_seq'::regclass), tac_codigo character varying(45), tac_habilitado boolean, tac_nombre character varying(255), tac_nombre_busqueda character varying(255), tac_ult_mod_fecha timestamp without time zone, tac_ult_mod_usuario character varying(45), tac_version integer, CONSTRAINT sg_tipos_accion_pkey PRIMARY KEY (tac_pk), CONSTRAINT tac_codigo_uk UNIQUE (tac_codigo), CONSTRAINT tac_nombre_uk UNIQUE (tac_nombre));
COMMENT ON TABLE catalogo.sg_tipos_accion IS 'Tabla para el registro de tipos de acciones.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_accion_aud (tac_pk bigint NOT NULL, tac_codigo character varying(45), tac_habilitado boolean, tac_nombre character varying(255), tac_nombre_busqueda character varying(255), tac_ult_mod_fecha timestamp without time zone, tac_ult_mod_usuario character varying(45), tac_version integer, rev bigint, revtype smallint, CONSTRAINT sg_tipos_accion_aud_pkey PRIMARY KEY (tac_pk,rev), CONSTRAINT sg_tipos_accion_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));
     
-- Tipos de Apoyos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_apoyo_tap_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_apoyo (tap_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_apoyo_tap_pk_seq'::regclass), tap_codigo character varying(45), tap_habilitado boolean, tap_nombre character varying(255), tap_nombre_busqueda character varying(255), tap_ult_mod_fecha timestamp without time zone, tap_ult_mod_usuario character varying(45), tap_version integer, CONSTRAINT sg_tipos_apoyo_pkey PRIMARY KEY (tap_pk), CONSTRAINT tap_codigo_uk UNIQUE (tap_codigo), CONSTRAINT tap_nombre_uk UNIQUE (tap_nombre));
COMMENT ON TABLE catalogo.sg_tipos_apoyo IS 'Tabla para el registro de tipos de apoyos.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_apoyo_aud (tap_pk bigint NOT NULL, tap_codigo character varying(45), tap_habilitado boolean, tap_nombre character varying(255), tap_nombre_busqueda character varying(255), tap_ult_mod_fecha timestamp without time zone, tap_ult_mod_usuario character varying(45), tap_version integer, rev bigint, revtype smallint, CONSTRAINT sg_tipos_apoyo_aud_pkey PRIMARY KEY (tap_pk,rev), CONSTRAINT sg_tipos_apoyo_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Tipos de Proveedores
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_proveedor_tpr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_proveedor (tpr_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_proveedor_tpr_pk_seq'::regclass), tpr_codigo character varying(45), tpr_habilitado boolean, tpr_nombre character varying(255), tpr_nombre_busqueda character varying(255), tpr_ult_mod_fecha timestamp without time zone, tpr_ult_mod_usuario character varying(45), tpr_version integer, CONSTRAINT sg_tipos_proveedor_pkey PRIMARY KEY (tpr_pk), CONSTRAINT tpr_codigo_uk UNIQUE (tpr_codigo), CONSTRAINT tpr_nombre_uk UNIQUE (tpr_nombre));
COMMENT ON TABLE catalogo.sg_tipos_proveedor IS 'Tabla para el registro de tipos de proveedores.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_proveedor.tpr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_proveedor_aud (tpr_pk bigint NOT NULL, tpr_codigo character varying(45), tpr_habilitado boolean, tpr_nombre character varying(255), tpr_nombre_busqueda character varying(255), tpr_ult_mod_fecha timestamp without time zone, tpr_ult_mod_usuario character varying(45), tpr_version integer, rev bigint, revtype smallint, CONSTRAINT sg_tipos_proveedor_aud_pkey PRIMARY KEY (tpr_pk,rev), CONSTRAINT sg_tipos_proveedor_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Grados de Riesgo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_grados_riesgo_gre_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_grados_riesgo (gre_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_grados_riesgo_gre_pk_seq'::regclass), gre_codigo character varying(45), gre_habilitado boolean, gre_nombre character varying(255), gre_nombre_busqueda character varying(255), gre_ult_mod_fecha timestamp without time zone, gre_ult_mod_usuario character varying(45), gre_version integer, CONSTRAINT sg_grados_riesgo_pkey PRIMARY KEY (gre_pk), CONSTRAINT gre_codigo_uk UNIQUE (gre_codigo), CONSTRAINT gre_nombre_uk UNIQUE (gre_nombre));
COMMENT ON TABLE catalogo.sg_grados_riesgo IS 'Tabla para el registro de grados de riesgo.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_grados_riesgo_aud (gre_pk bigint NOT NULL, gre_codigo character varying(45), gre_habilitado boolean, gre_nombre character varying(255), gre_nombre_busqueda character varying(255), gre_ult_mod_fecha timestamp without time zone, gre_ult_mod_usuario character varying(45), gre_version integer, rev bigint, revtype smallint, CONSTRAINT sg_grados_riesgo_aud_pkey PRIMARY KEY (gre_pk,rev), CONSTRAINT sg_grados_riesgo_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Motivos de Inasistencia del Personal
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_inasistencia_personal_mip_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_inasistencia_personal (mip_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivos_inasistencia_personal_mip_pk_seq'::regclass), mip_codigo character varying(45), mip_habilitado boolean, mip_motivo_justificado boolean, mip_nombre character varying(255), mip_nombre_busqueda character varying(255), mip_ult_mod_fecha timestamp without time zone, mip_ult_mod_usuario character varying(45), mip_version integer, CONSTRAINT sg_motivos_inasistencia_personal_pkey PRIMARY KEY (mip_pk), CONSTRAINT mip_codigo_uk UNIQUE (mip_codigo), CONSTRAINT mip_nombre_uk UNIQUE (mip_nombre));
    COMMENT ON TABLE catalogo.sg_motivos_inasistencia_personal IS 'Tabla para el registro de motivos de inasistencia del personal.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_motivo_justificado IS 'Indica si el motivo es justificado(true) ó no(false)';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_motivos_inasistencia_personal.mip_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_inasistencia_personal_aud (mip_pk bigint NOT NULL, mip_codigo character varying(45), mip_habilitado boolean, mip_motivo_justificado boolean, mip_nombre character varying(255), mip_nombre_busqueda character varying(255), mip_ult_mod_fecha timestamp without time zone, mip_ult_mod_usuario character varying(45), mip_version integer, rev bigint, revtype smallint, CONSTRAINT motivos_inasistencia_personal_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_inasistencia_personal_cargo (mip_pk bigint NOT NULL,car_pk bigint NOT NULL, CONSTRAINT sg_motivo_inasistencia_personal_cargo_motivo_fk FOREIGN KEY (mip_pk) REFERENCES catalogo.sg_motivos_inasistencia_personal (mip_pk), CONSTRAINT sg_motivo_inasistencia_personal_cargo_cargo_fk  FOREIGN KEY (car_pk) REFERENCES catalogo.sg_cargo (car_pk));
CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_inasistencia_personal_cargo_aud(mip_pk bigint NOT NULL,car_pk bigint NOT NULL, rev bigint, revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_INASISTENCIA_PERSONAL','C236',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_INASISTENCIA_PERSONAL','C237',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_INASISTENCIA_PERSONAL','C238',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_ACCION','C287',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_ACCION','C288',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_ACCION','C289',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_APOYO','C290',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_APOYO','C291',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_APOYO','C292',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_PROVEEDOR','C293',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_PROVEEDOR','C294',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_PROVEEDOR','C295',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_GRADO_RIESGO','C296',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_GRADO_RIESGO','C297',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_GRADO_RIESGO','C298',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROGRAMAS_INSTITUCIONAL','C299',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROGRAMAS_INSTITUCIONAL','C300',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROGRAMAS_INSTITUCIONAL','C301',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_COMPONENTES','C302',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_COMPONENTES','C303',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_COMPONENTES','C304',  '', 2, true, null, null,0);
        
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_BENEFICIOS','C305',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_BENEFICIOS','C306',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_BENEFICIOS','C307',  '', 2, true, null, null,0);
             
-- Recursos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_recursos_rcs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_recursos (rcs_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_recursos_rcs_pk_seq'::regclass), rcs_codigo character varying(45), rcs_habilitado boolean, rcs_nombre character varying(255), rcs_nombre_busqueda character varying(255), rcs_ult_mod_fecha timestamp without time zone, rcs_ult_mod_usuario character varying(45), rcs_version integer, CONSTRAINT sg_recursos_pkey PRIMARY KEY (rcs_pk), CONSTRAINT rcs_codigo_uk UNIQUE (rcs_codigo), CONSTRAINT rcs_nombre_uk UNIQUE (rcs_nombre));
    COMMENT ON TABLE catalogo.sg_recursos IS 'Tabla para el registro de recursos.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_recursos.rcs_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_recursos_aud (rcs_pk bigint NOT NULL, rcs_codigo character varying(45), rcs_habilitado boolean, rcs_nombre character varying(255), rcs_nombre_busqueda character varying(255), rcs_ult_mod_fecha timestamp without time zone, rcs_ult_mod_usuario character varying(45), rcs_version integer, rev bigint, revtype smallint, CONSTRAINT sg_recursos_aud_pkey PRIMARY KEY (rcs_pk,rev), CONSTRAINT sg_recursos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RECURSOS','C308',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RECURSOS','C309',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RECURSOS','C310',  '', 2, true, null, null,0);

-- 3.1.0

-- Velocidades de Internet
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_velocidades_internet_vin_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_velocidades_internet (vin_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_velocidades_internet_vin_pk_seq'::regclass), vin_codigo character varying(45), vin_habilitado boolean, vin_nombre character varying(255), vin_nombre_busqueda character varying(255), vin_ult_mod_fecha timestamp without time zone, vin_ult_mod_usuario character varying(45), vin_version integer, CONSTRAINT sg_velocidades_internet_pkey PRIMARY KEY (vin_pk), CONSTRAINT vin_codigo_uk UNIQUE (vin_codigo), CONSTRAINT vin_nombre_uk UNIQUE (vin_nombre));
    COMMENT ON TABLE catalogo.sg_velocidades_internet IS 'Tabla para el registro de velocidades de internet.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_velocidades_internet.vin_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_velocidades_internet_aud (vin_pk bigint NOT NULL, vin_codigo character varying(45), vin_habilitado boolean, vin_nombre character varying(255), vin_nombre_busqueda character varying(255), vin_ult_mod_fecha timestamp without time zone, vin_ult_mod_usuario character varying(45), vin_version integer, rev bigint, revtype smallint);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_VELOCIDAD_INTERNET','C311',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_VELOCIDAD_INTERNET','C312',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_VELOCIDAD_INTERNET','C313',  '', 2, true, null, null,0);


ALTER TABLE catalogo.sg_tipos_accion        ADD COLUMN tac_necesita_descripcion boolean;
ALTER TABLE catalogo.sg_tipos_accion_aud    ADD COLUMN tac_necesita_descripcion boolean;
COMMENT ON COLUMN catalogo.sg_tipos_accion.tac_necesita_descripcion IS 'Indica si el registro necesita descripción.';





-- Organismos de Coordinación Escolar
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_organismos_coordinacion_escolar_oce_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_organismos_coordinacion_escolar (oce_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_organismos_coordinacion_escolar_oce_pk_seq'::regclass), oce_codigo character varying(45), oce_habilitado boolean, oce_nombre character varying(255), oce_nombre_busqueda character varying(255), oce_ult_mod_fecha timestamp without time zone, oce_ult_mod_usuario character varying(45), oce_version integer, CONSTRAINT sg_organismos_coordinacion_escolar_pkey PRIMARY KEY (oce_pk), CONSTRAINT oce_codigo_uk UNIQUE (oce_codigo), CONSTRAINT oce_nombre_uk UNIQUE (oce_nombre));
    COMMENT ON TABLE catalogo.sg_organismos_coordinacion_escolar IS 'Tabla para el registro de organismos de coordinación escolar.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_organismos_coordinacion_escolar.oce_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_organismos_coordinacion_escolar_aud (oce_pk bigint NOT NULL, oce_codigo character varying(45), oce_habilitado boolean, oce_nombre character varying(255), oce_nombre_busqueda character varying(255), oce_ult_mod_fecha timestamp without time zone, oce_ult_mod_usuario character varying(45), oce_version integer, rev bigint, revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ORGANISMOS_COORDINACION_ESCOLAR','C314',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ORGANISMOS_COORDINACION_ESCOLAR','C315',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ORGANISMOS_COORDINACION_ESCOLAR','C316',  '', 2, true, null, null,0);


ALTER TABLE catalogo.sg_tipos_riesgo        ADD COLUMN IF NOT EXISTS tri_riesgo_ambiental boolean;
ALTER TABLE catalogo.sg_tipos_riesgo        ADD COLUMN IF NOT EXISTS tri_riesgo_social boolean;
ALTER TABLE catalogo.sg_tipos_riesgo_aud    ADD COLUMN IF NOT EXISTS tri_riesgo_ambiental boolean;
ALTER TABLE catalogo.sg_tipos_riesgo_aud    ADD COLUMN IF NOT EXISTS tri_riesgo_social boolean;
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_riesgo_ambiental IS 'Indica si el registro aplica riesgo ambiental.';
COMMENT ON COLUMN catalogo.sg_tipos_riesgo.tri_riesgo_social IS 'Indica si el registro aplica riesgo social.';


ALTER TABLE catalogo.sg_grados_riesgo       ADD COLUMN IF NOT EXISTS gre_riesgo_ambiental boolean;
ALTER TABLE catalogo.sg_grados_riesgo       ADD COLUMN IF NOT EXISTS gre_riesgo_social boolean;
ALTER TABLE catalogo.sg_grados_riesgo_aud   ADD COLUMN IF NOT EXISTS gre_riesgo_ambiental boolean;
ALTER TABLE catalogo.sg_grados_riesgo_aud   ADD COLUMN IF NOT EXISTS gre_riesgo_social boolean;
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_riesgo_ambiental IS 'Indica si el registro aplica riesgo ambiental.';
COMMENT ON COLUMN catalogo.sg_grados_riesgo.gre_riesgo_social IS 'Indica si el registro aplica riesgo social.';

-- 3.2.0

ALTER TABLE catalogo.sg_formulas ADD COLUMN fom_tipo_formula character varying(50);
ALTER TABLE catalogo.sg_formulas_aud ADD COLUMN fom_tipo_formula character varying(50);

-- 3.3.0

-- Cargos OAE
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_cargo_oae_coa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_cargo_oae (coa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_cargo_oae_coa_pk_seq'::regclass), coa_codigo character varying(45), coa_habilitado boolean, coa_nombre character varying(255), coa_nombre_busqueda character varying(255), coa_ult_mod_fecha timestamp without time zone, coa_ult_mod_usuario character varying(45), coa_version integer, CONSTRAINT sg_cargo_oae_pkey PRIMARY KEY (coa_pk), CONSTRAINT coa_codigo_uk UNIQUE (coa_codigo), CONSTRAINT coa_nombre_uk UNIQUE (coa_nombre));
COMMENT ON TABLE catalogo.sg_cargo_oae IS 'Tabla para el registro de cargos oae.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargo_oae.coa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_cargo_oae_aud (coa_pk bigint NOT NULL, coa_codigo character varying(45), coa_habilitado boolean, coa_nombre character varying(255), coa_nombre_busqueda character varying(255), coa_ult_mod_fecha timestamp without time zone, coa_ult_mod_usuario character varying(45), coa_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_cargo_oae_aud ADD PRIMARY KEY (coa_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARGO_OAE','C323',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARGO_OAE','C324',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARGO_OAE','C325',  '', 2, true, null, null,0);

-- ROLES LEY DE SALARIO
CREATE SEQUENCE catalogo.sg_ley_salario_roles_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_ley_salario_roles(
	lsr_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	lsr_ley_salario_fk int8 NOT NULL, -- Clave Foranea de Ley de Salario.
	lsr_rol_fk int8 NOT NULL, -- Clave Foranea de Rol.
	lsr_ult_mod_usuario varchar(45) NULL, -- Última fecha en la que se modificó el registro.
	lsr_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	lsr_version int4 NULL,
	CONSTRAINT sg_ley_salario_roles_pk PRIMARY KEY (lsr_pk),
	CONSTRAINT sg_ley_salario_roles_sg_ley_salario_fk FOREIGN KEY (lsr_ley_salario_fk) REFERENCES catalogo.sg_ley_salario(lsa_pk),
	CONSTRAINT sg_ley_salario_roles_sg_roles_fk FOREIGN KEY (lsr_rol_fk) REFERENCES seguridad.sg_roles(rol_pk)
);

-- Column comments

COMMENT ON COLUMN catalogo.sg_ley_salario_roles.lsr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_ley_salario_roles.lsr_ley_salario_fk IS 'Clave Foranea de Ley de Salario.';
COMMENT ON COLUMN catalogo.sg_ley_salario_roles.lsr_rol_fk IS 'Clave Foranea de Rol.';
COMMENT ON COLUMN catalogo.sg_ley_salario_roles.lsr_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ley_salario_roles.lsr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE catalogo.sg_ley_salario_roles ALTER lsr_pk SET DEFAULT nextval('catalogo.sg_ley_salario_roles_pk_seq');
CREATE TABLE IF NOT EXISTS catalogo.sg_ley_salario_roles_aud (
    lsr_pk bigserial NOT NULL,
	lsr_ley_salario_fk int8 NOT NULL, 
	lsr_rol_fk int8 NOT NULL, 
	lsr_ult_mod_usuario varchar(45) NULL, 
	lsr_ult_mod_fecha timestamp NULL,
	lsr_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_ley_salario_roles_aud_pkey PRIMARY KEY (lsr_pk, rev)
);

ALTER TABLE catalogo.sg_ley_salario_roles_aud ADD CONSTRAINT ley_salario_roles_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

-- 3.3.1

-- Categorías de curso
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_categorias_cursos_ccu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_categorias_cursos (ccu_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_categorias_cursos_ccu_pk_seq'::regclass), ccu_codigo character varying(45), ccu_habilitado boolean, ccu_nombre character varying(255), ccu_nombre_busqueda character varying(255), ccu_ult_mod_fecha timestamp without time zone, ccu_ult_mod_usuario character varying(45), ccu_version integer, CONSTRAINT sg_categorias_cursos_pkey PRIMARY KEY (ccu_pk), CONSTRAINT ccu_codigo_uk UNIQUE (ccu_codigo), CONSTRAINT ccu_nombre_uk UNIQUE (ccu_nombre));
    COMMENT ON TABLE catalogo.sg_categorias_cursos IS 'Tabla para el registro de categorías de curso.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_categorias_cursos.ccu_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_categorias_cursos_aud (ccu_pk bigint NOT NULL, ccu_codigo character varying(45), ccu_habilitado boolean, ccu_nombre character varying(255), ccu_nombre_busqueda character varying(255), ccu_ult_mod_fecha timestamp without time zone, ccu_ult_mod_usuario character varying(45), ccu_version integer, rev bigint, revtype smallint, CONSTRAINT sg_categorias_cursos_aud_pkey PRIMARY KEY (ccu_pk,rev), CONSTRAINT sg_recursos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));



-- Tipos de módulos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_modulos_tmo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_modulos (tmo_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_modulos_tmo_pk_seq'::regclass), tmo_codigo character varying(45), tmo_habilitado boolean, tmo_nombre character varying(255), tmo_nombre_busqueda character varying(255), tmo_ult_mod_fecha timestamp without time zone, tmo_ult_mod_usuario character varying(45), tmo_version integer, CONSTRAINT sg_tipos_modulos_pkey PRIMARY KEY (tmo_pk), CONSTRAINT tmo_codigo_uk UNIQUE (tmo_codigo), CONSTRAINT tmo_nombre_uk UNIQUE (tmo_nombre));
    COMMENT ON TABLE catalogo.sg_tipos_modulos IS 'Tabla para el registro de tipos de módulos.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_modulos.tmo_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_modulos_aud (tmo_pk bigint NOT NULL, tmo_codigo character varying(45), tmo_habilitado boolean, tmo_nombre character varying(255), tmo_nombre_busqueda character varying(255), tmo_ult_mod_fecha timestamp without time zone, tmo_ult_mod_usuario character varying(45), tmo_version integer, rev bigint, revtype smallint, CONSTRAINT sg_tipos_modulo_aud_pkey PRIMARY KEY (tmo_pk,rev), CONSTRAINT sg_recursos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIAS_CURSO','C317',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIAS_CURSO','C318',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIAS_CURSO','C319',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_MODULO','C320',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_MODULO','C321',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_MODULO','C322',  '', 2, true, null, null,0);

-- 3.4.0

--OPERACIONES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_CALIDAD','C326',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_CALIDAD','C327',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_CALIDAD','C328',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_DESCARGO','C329',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_DESCARGO','C330',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_DESCARGO','C331',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_TRASLADO','C332',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_TRASLADO','C333',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_TRASLADO','C334',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_BIENES','C335',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_BIENES','C336',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_BIENES','C337',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_INVENTARIO','C338',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_INVENTARIO','C339',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_INVENTARIO','C340',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FORMA_ADQUISICION','C341',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FORMA_ADQUISICION','C342',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FORMA_ADQUISICION','C343',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CLASIFICACION_BIENES','C344',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CLASIFICACION_BIENES','C345',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CLASIFICACION_BIENES','C346',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIA_BIENES','C347',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIA_BIENES','C348',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIA_BIENES','C349',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTOS','C350',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTOS','C351',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTOS','C352',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_OPCIONES_DESCARGO','C353',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_OPCIONES_DESCARGO','C354',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_OPCIONES_DESCARGO','C355',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_BIENES','C356',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_BIENES','C357',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_BIENES','C358',  '', 2, true, null, null,0);




INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_CENTROS_EDUCATIVOS','CP1',  '', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_ACTIVO_FIJO','CP2',  '', 2, true, null, null,0);

-- ESTADOS DE ACTIVOS
--ESTADOS CALIDAD DE ACTIVO
CREATE SEQUENCE catalogo.sg_estados_calidad_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_estados_calidad(
	eca_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	eca_codigo varchar(4) NULL, -- Código de la ecaia.
	eca_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	eca_nombre varchar(255) NULL, -- Nombre del registro.
	eca_nombre_busqueda varchar(255) NULL, -- Nombre de la ecaia normalizado para búsquedas.
	eca_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	eca_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	eca_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_estados_calidad_pk PRIMARY KEY (eca_pk),
	CONSTRAINT sg_estados_calidad_codigo_uk UNIQUE (eca_codigo),
	CONSTRAINT sg_estados_calidad_nombre_uk UNIQUE (eca_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_calidad IS 'Tabla para el registro de los estados de calidad de un activo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_calidad.eca_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_estados_calidad ALTER eca_pk SET DEFAULT nextval('catalogo.sg_estados_calidad_pk_seq');

CREATE TABLE catalogo.sg_estados_calidad_aud (
    eca_pk bigserial NOT NULL,
	eca_codigo varchar(4) NULL,
	eca_habilitado bool NULL,
	eca_nombre varchar(255) NULL,
	eca_nombre_busqueda varchar(255) NULL,
	eca_ult_mod_fecha timestamp NULL,
	eca_ult_mod_usuario varchar(45) NULL,
	eca_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_estados_calidad_aud_pkey PRIMARY KEY (eca_pk, rev)
);

--ESTADOS DESCARGO DE ACTIVO
CREATE SEQUENCE catalogo.sg_estados_descargo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_estados_descargo(
	ede_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	ede_codigo varchar(4) NULL, -- Código del registro.
	ede_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	ede_nombre varchar(255) NULL, -- Nombre del registro.
	ede_nombre_busqueda varchar(255) NULL, -- Nombre de la edeia normalizado para búsquedas.
	ede_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ede_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ede_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_estados_descargo_pk PRIMARY KEY (ede_pk),
	CONSTRAINT sg_estados_descargo_codigo_uk UNIQUE (ede_codigo),
	CONSTRAINT sg_estados_descargo_nombre_uk UNIQUE (ede_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_descargo IS 'Tabla para el registro de los estados de descargo de un activo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_descargo.ede_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_estados_descargo ALTER ede_pk SET DEFAULT nextval('catalogo.sg_estados_descargo_pk_seq');

CREATE TABLE catalogo.sg_estados_descargo_aud (
    ede_pk bigserial NOT NULL,
	ede_codigo varchar(4) NULL,
	ede_habilitado bool NULL,
	ede_nombre varchar(255) NULL,
	ede_nombre_busqueda varchar(255) NULL,
	ede_ult_mod_fecha timestamp NULL,
	ede_ult_mod_usuario varchar(45) NULL,
	ede_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_estados_descargo_aud_pkey PRIMARY KEY (ede_pk, rev)
);

--ESTADOS TRASLADO DE ACTIVO
CREATE SEQUENCE catalogo.sg_estados_traslado_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_estados_traslado(
	etr_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	etr_codigo varchar(4) NULL, -- Código del registro.
	etr_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	etr_nombre varchar(255) NULL, -- Nombre del registro.
	etr_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	etr_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	etr_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	etr_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_estados_traslado_pk PRIMARY KEY (etr_pk),
	CONSTRAINT sg_estados_traslado_codigo_uk UNIQUE (etr_codigo),
	CONSTRAINT sg_estados_traslado_nombre_uk UNIQUE (etr_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_traslado IS 'Tabla para el registro de los estados de descargo de un activo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_traslado.etr_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_estados_traslado ALTER etr_pk SET DEFAULT nextval('catalogo.sg_estados_traslado_pk_seq');

CREATE TABLE catalogo.sg_estados_traslado_aud (
    etr_pk bigserial NOT NULL,
	etr_codigo varchar(4) NULL,
	etr_habilitado bool NULL,
	etr_nombre varchar(255) NULL,
	etr_nombre_busqueda varchar(255) NULL,
	etr_ult_mod_fecha timestamp NULL,
	etr_ult_mod_usuario varchar(45) NULL,
	etr_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_estados_traslado_aud_pkey PRIMARY KEY (etr_pk, rev)
);


--ESTADOS INVENTARIO
CREATE SEQUENCE catalogo.sg_estados_inventario_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_estados_inventario(
	ein_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	ein_codigo varchar(4) NULL, -- Código del registro.
	ein_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	ein_nombre varchar(255) NULL, -- Nombre del registro.
	ein_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	ein_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ein_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ein_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_estados_inventario_pk PRIMARY KEY (ein_pk),
	CONSTRAINT sg_estados_inventario_codigo_uk UNIQUE (ein_codigo),
	CONSTRAINT sg_estados_inventario_nombre_uk UNIQUE (ein_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_inventario IS 'Tabla para el registro de los estados de inventario.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_codigo IS 'Código de la einia.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_nombre IS 'Nombre de la einia.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_inventario.ein_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_estados_inventario ALTER ein_pk SET DEFAULT nextval('catalogo.sg_estados_inventario_pk_seq');

CREATE TABLE catalogo.sg_estados_inventario_aud (
    ein_pk bigserial NOT NULL,
	ein_codigo varchar(4) NULL,
	ein_habilitado bool NULL,
	ein_nombre varchar(255) NULL,
	ein_nombre_busqueda varchar(255) NULL,
	ein_ult_mod_fecha timestamp NULL,
	ein_ult_mod_usuario varchar(45) NULL,
	ein_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_estados_inventario_aud_pkey PRIMARY KEY (ein_pk, rev)
);

--ESTADOS BIENES
CREATE SEQUENCE catalogo.sg_estados_bienes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_estados_bienes(
	ebi_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	ebi_codigo varchar(4) NULL, -- Código del registro.
	ebi_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	ebi_nombre varchar(255) NULL, -- Nombre de la ebiia.
	ebi_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	ebi_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ebi_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ebi_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_estados_bienes_pk PRIMARY KEY (ebi_pk),
	CONSTRAINT sg_estados_bienes_codigo_uk UNIQUE (ebi_codigo),
	CONSTRAINT sg_estados_bienes_nombre_uk UNIQUE (ebi_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_bienes IS 'Tabla para el registro de las Formas de Adquisición.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_codigo IS 'Código de la ebiia.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_bienes.ebi_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_estados_bienes ALTER ebi_pk SET DEFAULT nextval('catalogo.sg_estados_bienes_pk_seq');

CREATE TABLE catalogo.sg_estados_bienes_aud (
    ebi_pk bigserial NOT NULL,
	ebi_codigo varchar(4) NULL,
	ebi_habilitado bool NULL,
	ebi_nombre varchar(255) NULL,
	ebi_nombre_busqueda varchar(255) NULL,
	ebi_ult_mod_fecha timestamp NULL,
	ebi_ult_mod_usuario varchar(45) NULL,
	ebi_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_estados_bienes_aud_pkey PRIMARY KEY (ebi_pk, rev)
);


--FORMA DE ADQUISICIÓN
CREATE SEQUENCE catalogo.sg_fomas_adquisicion_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_fomas_adquisicion (
	fad_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	fad_codigo varchar(4) NULL, -- Código del registro.
	fad_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	fad_nombre varchar(255) NULL, -- Nombre del registro.
	fad_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	fad_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	fad_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	fad_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_fomas_adquisicion_pk PRIMARY KEY (fad_pk),
	CONSTRAINT sg_fomas_adquisicion_codigo_uk UNIQUE (fad_codigo),
	CONSTRAINT sg_fomas_adquisicion_nombre_uk UNIQUE (fad_nombre)
);
COMMENT ON TABLE catalogo.sg_fomas_adquisicion IS 'Tabla para el registro de las Formas de Adquisición.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_fomas_adquisicion.fad_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_fomas_adquisicion ALTER fad_pk SET DEFAULT nextval('catalogo.sg_fomas_adquisicion_pk_seq');

CREATE TABLE catalogo.sg_fomas_adquisicion_aud (
    fad_pk bigserial NOT NULL,
	fad_codigo varchar(4) NULL,
	fad_habilitado bool NULL,
	fad_nombre varchar(255) NULL,
	fad_nombre_busqueda varchar(255) NULL,
	fad_ult_mod_fecha timestamp NULL,
	fad_ult_mod_usuario varchar(45) NULL,
	fad_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_fomas_adquisicion_aud_pkey PRIMARY KEY (fad_pk, rev)
);


--PROYECTOS
CREATE SEQUENCE catalogo.sg_proyectos_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_proyectos(
	pro_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	pro_codigo varchar(4) NULL, -- Código del registro.
	pro_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	pro_nombre varchar(255) NULL, -- Nombre del registro.
	pro_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	pro_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	pro_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	pro_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_proyectos_pk PRIMARY KEY (pro_pk),
	CONSTRAINT sg_proyectos_codigo_uk UNIQUE (pro_codigo),
	CONSTRAINT sg_proyectos_nombre_uk UNIQUE (pro_nombre)
);
COMMENT ON TABLE catalogo.sg_proyectos IS 'Tabla para el registro de los proyectos.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_proyectos.pro_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proyectos.pro_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_proyectos ALTER pro_pk SET DEFAULT nextval('catalogo.sg_proyectos_pk_seq');

CREATE TABLE catalogo.sg_proyectos_aud (
    pro_pk bigserial NOT NULL,
	pro_codigo varchar(4) NULL,
	pro_habilitado bool NULL,
	pro_nombre varchar(255) NULL,
	pro_nombre_busqueda varchar(255) NULL,
	pro_ult_mod_fecha timestamp NULL,
	pro_ult_mod_usuario varchar(45) NULL,
	pro_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_proyectos_aud_pkey PRIMARY KEY (pro_pk, rev)
);

--OPCIONES DE DESCARGO
CREATE SEQUENCE catalogo.sg_opciones_descargo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_opciones_descargo(
    ode_pk bigserial NOT NULL, -- Número correlativo autogenerado.
    ode_codigo varchar(4) NULL, -- Código del registro.
    ode_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
    ode_nombre varchar(255) NULL, -- Nombre del registro.
    ode_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
    ode_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
    ode_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
    ode_version int4 NULL, -- Versión del registro
    CONSTRAINT sg_opciones_descargo_pk PRIMARY KEY (ode_pk),
    CONSTRAINT sg_opciones_descargo_codigo_uk UNIQUE (ode_codigo),
    CONSTRAINT sg_opciones_descargo_nombre_uk UNIQUE (ode_nombre)
);
COMMENT ON TABLE catalogo.sg_opciones_descargo IS 'Tabla para el registro de los Opciones de Descargo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_nombre_busqueda IS 'Nombre de la odeia normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_opciones_descargo.ode_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_opciones_descargo ALTER ode_pk SET DEFAULT nextval('catalogo.sg_opciones_descargo_pk_seq');

CREATE TABLE catalogo.sg_opciones_descargo_aud (
    ode_pk bigserial NOT NULL,
    ode_codigo varchar(4) NULL,
    ode_habilitado bool NULL,
    ode_nombre varchar(255) NULL,
    ode_nombre_busqueda varchar(255) NULL,
    ode_ult_mod_fecha timestamp NULL,
    ode_ult_mod_usuario varchar(45) NULL,
    ode_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_opciones_descargo_aud_pkey PRIMARY KEY (ode_pk, rev)
);

--CLASIFICACIONES DE BIENES
CREATE SEQUENCE catalogo.sg_clasificacion_bienes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_clasificacion_bienes (
	cbi_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	cbi_codigo varchar(4) NULL, -- Código del registro.
	cbi_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	cbi_nombre varchar(255) NULL, -- Nombre del registro.
	cbi_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	cbi_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	cbi_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	cbi_version int4 NULL, -- Versión del registro
	CONSTRAINT sg_clasificacion_bienes_pk PRIMARY KEY (cbi_pk),
	CONSTRAINT sg_clasificacion_bienes_codigo_uk UNIQUE (cbi_codigo),
	CONSTRAINT sg_clasificacion_bienes_nombre_uk UNIQUE (cbi_nombre)
);
COMMENT ON TABLE catalogo.sg_clasificacion_bienes IS 'Tabla para el registro de la Clasificación de Bienes';

-- Column comments

COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_clasificacion_bienes.cbi_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_clasificacion_bienes ALTER cbi_pk SET DEFAULT nextval('catalogo.sg_clasificacion_bienes_pk_seq');

CREATE TABLE catalogo.sg_clasificacion_bienes_aud (
        cbi_pk bigserial NOT NULL,
	cbi_codigo varchar(4) NULL,
	cbi_habilitado bool NULL,
	cbi_nombre varchar(255) NULL,
	cbi_nombre_busqueda varchar(255) NULL,
	cbi_ult_mod_fecha timestamp NULL,
	cbi_ult_mod_usuario varchar(45) NULL,
	cbi_version int4 null,
        rev int8 NOT NULL,
        revtype int2 NULL,
    CONSTRAINT sg_clasificacion_bienes_aud_pkey PRIMARY KEY (cbi_pk, rev)
);

--CATEGORIA DE BIENES
CREATE SEQUENCE catalogo.sg_categoria_bienes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_categoria_bienes (
	cab_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	cab_codigo varchar(4) NULL, -- Código del registro.
	cab_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
	cab_nombre varchar(255) NULL, -- Nombre del registro.
	cab_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
        cab_vida_util int4 NULL, --Vida Util del bien
	cab_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	cab_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	cab_version int4 NULL, -- Versión del registro.
	cab_clasificacion_bienes_fk int8 NULL, -- Clave Foranea de Clasificación de Bienes.
	
	CONSTRAINT sg_categoria_bienes_codigo_uk UNIQUE (cab_codigo),
	CONSTRAINT sg_categoria_bienes_nombre_uk UNIQUE (cab_nombre),
	CONSTRAINT sg_categoria_bienes_pk PRIMARY KEY (cab_pk),
	CONSTRAINT sg_categoria_bienes_sg_clasificacion_bienes_fk FOREIGN KEY (cab_clasificacion_bienes_fk) REFERENCES catalogo.sg_clasificacion_bienes(cbi_pk)
);
COMMENT ON TABLE catalogo.sg_categoria_bienes IS 'Tabla para el registro de las Categoria de Bienes.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_vida_util IS 'Vida Util del bien';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_clasificacion_bienes_fk IS 'Clave Foranea de Clasificación de Bienes.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_categoria_bienes.cab_version IS 'Versión del registro.';


ALTER TABLE catalogo.sg_categoria_bienes ALTER cab_pk SET DEFAULT nextval('catalogo.sg_categoria_bienes_pk_seq');

CREATE TABLE catalogo.sg_categoria_bienes_aud (
    cab_pk bigserial NOT NULL,
	cab_codigo varchar(4) NULL,
	cab_habilitado bool NULL,
	cab_nombre varchar(255) NULL,
	cab_nombre_busqueda varchar(255) NULL,
        cab_vida_util int4 NULL,
	cab_clasificacion_bienes_fk int8 NULL,
	cab_ult_mod_fecha timestamp NULL,
	cab_ult_mod_usuario varchar(45) NULL,
	cab_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_categoria_bienes_aud_pkey PRIMARY KEY (cab_pk, rev)
);

--TIPO DE BIENES
CREATE SEQUENCE catalogo.sg_tipo_bienes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_tipo_bienes (
    tbi_pk bigserial NOT NULL, -- Número correlativo autogenerado.
    tbi_codigo varchar(4) NULL, -- Código del registro.
    tbi_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
    tbi_es_tipo_vehiculo bool NULL, -- Indica si el tipo de bien es vehiculo(true) o no(false)
    tbi_nombre varchar(255) NULL, -- Nombre del registro.
    tbi_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
    tbi_categoria_bienes_fk int8 NULL, -- Clave Foranea de Categoria de Bienes.
    tbi_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
    tbi_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
    tbi_version int4 NULL, -- Versión del registro
    CONSTRAINT sg_tipo_bienes_pk PRIMARY KEY (tbi_pk),
    CONSTRAINT sg_tipo_bienes_codigo_uk UNIQUE (tbi_codigo),
    CONSTRAINT sg_tipo_bienes_nombre_uk UNIQUE (tbi_nombre),
    CONSTRAINT sg_tipo_bienes_sg_categoria_bienes_fk FOREIGN KEY (tbi_categoria_bienes_fk) REFERENCES catalogo.sg_categoria_bienes(cab_pk)
);

-- Column comments

COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_es_tipo_vehiculo IS 'Indica si el tipo de bien es vehiculo(true) o no(false).';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_categoria_bienes_fk IS 'Clave Foranea de Categoria de Bienes.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_bienes.tbi_version IS 'Versión del registro';

ALTER TABLE catalogo.sg_tipo_bienes ALTER tbi_pk SET DEFAULT nextval('catalogo.sg_tipo_bienes_pk_seq');

CREATE TABLE catalogo.sg_tipo_bienes_aud (
    tbi_pk bigserial NOT NULL, 
    tbi_codigo varchar(4) NULL, 
    tbi_habilitado bool NULL, 
    tbi_es_tipo_vehiculo bool NULL, 
    tbi_nombre varchar(255) NULL, 
    tbi_nombre_busqueda varchar(255) NULL, 
    tbi_categoria_bienes_fk int8 NULL,
    tbi_ult_mod_fecha timestamp NULL,
    tbi_ult_mod_usuario varchar(45) NULL, 
    tbi_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_tipo_bienes_aud_pkey PRIMARY KEY (tbi_pk, rev)
);

--3.5.0

ALTER TABLE catalogo.sg_tipo_contrato ADD COLUMN tco_es_interinato boolean;
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD COLUMN tco_es_interinato boolean;

ALTER TABLE catalogo.sg_tipo_contrato ADD COLUMN tco_requiere_fecha_hasta boolean;
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD COLUMN tco_requiere_fecha_hasta boolean;


-- 3.6.0

ALTER TABLE catalogo.sg_tipo_bienes DROP COLUMN tbi_es_tipo_vehiculo;
ALTER TABLE catalogo.sg_tipo_bienes_aud DROP COLUMN tbi_es_tipo_vehiculo;

ALTER TABLE catalogo.sg_categoria_bienes ADD cab_es_tipo_vehiculo bool NULL;
ALTER TABLE catalogo.sg_categoria_bienes_aud ADD cab_es_tipo_vehiculo bool NULL;

-- 3.7.0

ALTER TABLE catalogo.sg_estados_bienes RENAME TO sg_af_estados_bienes;
ALTER TABLE catalogo.sg_estados_calidad RENAME TO sg_af_estados_calidad;
ALTER TABLE catalogo.sg_estados_descargo RENAME TO sg_af_estados_descargo;
ALTER TABLE catalogo.sg_estados_inventario RENAME TO sg_af_estados_inventario;
ALTER TABLE catalogo.sg_estados_traslado RENAME TO sg_af_estados_traslado;
ALTER TABLE catalogo.sg_fomas_adquisicion RENAME TO sg_af_fomas_adquisicion;
ALTER TABLE catalogo.sg_categoria_bienes RENAME TO sg_af_categoria_bienes;
ALTER TABLE catalogo.sg_clasificacion_bienes RENAME TO sg_af_clasificacion_bienes;
ALTER TABLE catalogo.sg_tipo_bienes RENAME TO sg_af_tipo_bienes;
ALTER TABLE catalogo.sg_proyectos RENAME TO sg_af_proyectos;
ALTER TABLE catalogo.sg_opciones_descargo RENAME TO sg_af_opciones_descargo;

ALTER TABLE catalogo.sg_estados_bienes_aud RENAME TO sg_af_estados_bienes_aud;
ALTER TABLE catalogo.sg_estados_calidad_aud RENAME TO sg_af_estados_calidad_aud;
ALTER TABLE catalogo.sg_estados_descargo_aud RENAME TO sg_af_estados_descargo_aud;
ALTER TABLE catalogo.sg_estados_inventario_aud RENAME TO sg_af_estados_inventario_aud;
ALTER TABLE catalogo.sg_estados_traslado_aud RENAME TO sg_af_estados_traslado_aud;
ALTER TABLE catalogo.sg_fomas_adquisicion_aud RENAME TO sg_af_fomas_adquisicion_aud;
ALTER TABLE catalogo.sg_categoria_bienes_aud RENAME TO sg_af_categoria_bienes_aud;
ALTER TABLE catalogo.sg_clasificacion_bienes_aud RENAME TO sg_af_clasificacion_bienes_aud;
ALTER TABLE catalogo.sg_tipo_bienes_aud RENAME TO sg_af_tipo_bienes_aud;
ALTER TABLE catalogo.sg_proyectos_aud RENAME TO sg_af_proyectos_aud;
ALTER TABLE catalogo.sg_opciones_descargo_aud RENAME TO sg_af_opciones_descargo_aud;


ALTER SEQUENCE catalogo.sg_estados_bienes_pk_seq RENAME TO sg_af_estados_bienes_pk_seq;
ALTER SEQUENCE catalogo.sg_estados_calidad_pk_seq RENAME TO sg_af_estados_calidad_pk_seq;
ALTER SEQUENCE catalogo.sg_estados_descargo_pk_seq RENAME TO sg_af_estados_descargo_pk_seq;
ALTER SEQUENCE catalogo.sg_estados_inventario_pk_seq RENAME TO sg_af_estados_inventario_pk_seq;
ALTER SEQUENCE catalogo.sg_estados_traslado_pk_seq RENAME TO sg_af_estados_traslado_pk_seq;
ALTER SEQUENCE catalogo.sg_fomas_adquisicion_pk_seq RENAME TO sg_af_fomas_adquisicion_pk_seq;
ALTER SEQUENCE catalogo.sg_categoria_bienes_pk_seq RENAME TO sg_af_categoria_bienes_pk_seq;
ALTER SEQUENCE catalogo.sg_clasificacion_bienes_pk_seq RENAME TO sg_af_clasificacion_bienes_pk_seq;
ALTER SEQUENCE catalogo.sg_opciones_descargo_pk_seq RENAME TO sg_af_opciones_descargo_pk_seq;
ALTER SEQUENCE catalogo.sg_tipo_bienes_pk_seq RENAME TO sg_af_tipo_bienes_pk_seq;
ALTER SEQUENCE catalogo.sg_proyectos_pk_seq RENAME TO sg_af_proyectos_pk_seq;

ALTER TABLE catalogo.sg_af_estados_bienes ALTER ebi_pk SET DEFAULT nextval('catalogo.sg_af_estados_bienes_pk_seq');
ALTER TABLE catalogo.sg_af_estados_calidad ALTER eca_pk SET DEFAULT nextval('catalogo.sg_af_estados_calidad_pk_seq');
ALTER TABLE catalogo.sg_af_estados_descargo ALTER ede_pk SET DEFAULT nextval('catalogo.sg_af_estados_descargo_pk_seq');
ALTER TABLE catalogo.sg_af_estados_inventario ALTER ein_pk SET DEFAULT nextval('catalogo.sg_af_estados_inventario_pk_seq');
ALTER TABLE catalogo.sg_af_estados_traslado ALTER etr_pk SET DEFAULT nextval('catalogo.sg_af_estados_traslado_pk_seq');
ALTER TABLE catalogo.sg_af_fomas_adquisicion ALTER fad_pk SET DEFAULT nextval('catalogo.sg_af_fomas_adquisicion_pk_seq');
ALTER TABLE catalogo.sg_af_categoria_bienes ALTER cab_pk SET DEFAULT nextval('catalogo.sg_af_categoria_bienes_pk_seq');
ALTER TABLE catalogo.sg_af_clasificacion_bienes ALTER cbi_pk SET DEFAULT nextval('catalogo.sg_af_clasificacion_bienes_pk_seq');
ALTER TABLE catalogo.sg_af_tipo_bienes ALTER tbi_pk SET DEFAULT nextval('catalogo.sg_af_opciones_descargo_pk_seq');
ALTER TABLE catalogo.sg_af_proyectos ALTER pro_pk SET DEFAULT nextval('catalogo.sg_af_tipo_bienes_pk_seq');
ALTER TABLE catalogo.sg_af_opciones_descargo ALTER ode_pk SET DEFAULT nextval('catalogo.sg_af_proyectos_pk_seq');


-- 3.8.0

DROP TABLE catalogo.sg_ley_salario_roles;
DROP TABLE catalogo.sg_ley_salario_roles_aud;
DROP SEQUENCE catalogo.sg_ley_salario_roles_pk_seq;



CREATE SEQUENCE catalogo.sg_cargos_roles_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_cargos_roles(
	car_pk bigserial NOT NULL, 
	car_cargo_fk int8 NOT NULL, 
	car_rol_fk int8 NOT NULL,
	car_ult_mod_usuario varchar(45) NULL, 
	car_ult_mod_fecha timestamp NULL,
	car_version int4 NULL,
	CONSTRAINT sg_cargo_roles_pk PRIMARY KEY (car_pk),
	CONSTRAINT sg_cargo_roles_sg_cargo_fk FOREIGN KEY (car_cargo_fk) REFERENCES catalogo.sg_cargo(car_pk),
	CONSTRAINT sg_cargo_roles_sg_rol_fk FOREIGN KEY (car_rol_fk) REFERENCES seguridad.sg_roles(rol_pk)
);

-- Column comments

COMMENT ON COLUMN catalogo.sg_cargos_roles.car_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_cargos_roles.car_cargo_fk IS 'Clave foránea de Cargo.';
COMMENT ON COLUMN catalogo.sg_cargos_roles.car_rol_fk IS 'Clave Foranea de Rol.';
COMMENT ON COLUMN catalogo.sg_cargos_roles.car_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargos_roles.car_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE catalogo.sg_cargos_roles ALTER car_pk SET DEFAULT nextval('catalogo.sg_cargos_roles_pk_seq');
CREATE TABLE IF NOT EXISTS catalogo.sg_cargos_roles_aud (
    car_pk bigserial NOT NULL,
	car_cargo_fk int8 NOT NULL, 
	car_rol_fk int8 NOT NULL, 
	car_ult_mod_usuario varchar(45) NULL, 
	car_ult_mod_fecha timestamp NULL,
	car_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_cargos_roles_aud_pkey PRIMARY KEY (car_pk, rev)
);

ALTER TABLE catalogo.sg_cargos_roles_aud ADD CONSTRAINT cargos_roles_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev);


-- 3.9.0

-- Tipos de documentos de docentes
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_documentos_docente_tdd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_documentos_docente (tdd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_documentos_docente_tdd_pk_seq'::regclass), tdd_codigo character varying(45), tdd_habilitado boolean, tdd_nombre character varying(255), tdd_nombre_busqueda character varying(255), tdd_ult_mod_fecha timestamp without time zone, tdd_ult_mod_usuario character varying(45), tdd_version integer, CONSTRAINT sg_tipos_documentos_docente_pkey PRIMARY KEY (tdd_pk), CONSTRAINT tdd_codigo_uk UNIQUE (tdd_codigo), CONSTRAINT tdd_nombre_uk UNIQUE (tdd_nombre));
    COMMENT ON TABLE catalogo.sg_tipos_documentos_docente IS 'Tabla para el registro de tipos de documentos de docentes.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_tipos_documentos_docente.tdd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_documentos_docente_aud (tdd_pk bigint NOT NULL, tdd_codigo character varying(45), tdd_habilitado boolean, tdd_nombre character varying(255), tdd_nombre_busqueda character varying(255), tdd_ult_mod_fecha timestamp without time zone, tdd_ult_mod_usuario character varying(45), tdd_version integer, rev bigint, revtype smallint, CONSTRAINT sg_tipos_documentos_docente_aud_pkey PRIMARY KEY (tdd_pk,rev), CONSTRAINT sg_tipos_documentos_docente_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_DOCUMENTOS_DOCENTES','C365',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_DOCUMENTOS_DOCENTES','C366',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_DOCUMENTOS_DOCENTES','C367',  '', 2, true, null, null,0);

--VULNERABILIDADES
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_VULNERABILIDAD','C362',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_VULNERABILIDAD','C363',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_VULNERABILIDAD','C364',  '', 2, true, null, null,0);

CREATE SEQUENCE IF NOT EXISTS catalogo.sg_vulnerabilidades_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_UNIDAD','C359',  'Permiso para la creación de tipos de unidades', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_UNIDAD','C360',  'Permiso para la actualización de tipos de unidades', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_UNIDAD','C361',  'Permiso para la eliminación de tipos de unidades', 2, true, null, null,0);


--TIPOS DE UNIDADES DE ACTIVO FIJO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_af_tipos_unidades_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
    
CREATE TABLE IF NOT EXISTS catalogo.sg_vulnerabilidades (
	vul_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	vul_codigo varchar(4) NULL, -- Código del Registro.
	vul_habilitado bool NULL, -- Indica si el registro se encuentra habilitada(true) o inhabilitada(false).
	vul_nombre varchar(255) NULL, -- Nombre del registro.
	vul_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	vul_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	vul_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	vul_version int4 NULL, -- Versión del registro.
	CONSTRAINT vul_codigo_uk UNIQUE (vul_codigo),
	CONSTRAINT vul_nombre_uk UNIQUE (vul_nombre),
	CONSTRAINT vulnerabilidades_pk PRIMARY KEY (vul_pk)
);
COMMENT ON TABLE catalogo.sg_vulnerabilidades IS 'Tabla para el registro de vulnerabilidades.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_habilitado IS 'Indica si el registro se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_vulnerabilidades.vul_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_vulnerabilidades ALTER vul_pk SET DEFAULT nextval('catalogo.sg_vulnerabilidades_pk_seq');

CREATE TABLE IF NOT EXISTS catalogo.sg_vulnerabilidades_aud (
    vul_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	vul_codigo varchar(4) NULL, -- Código del Registro.
	vul_habilitado bool NULL, -- Indica si el registro se encuentra habilitada(true) o inhabilitada(false).
	vul_nombre varchar(255) NULL, -- Nombre del registro.
	vul_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	vul_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	vul_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	vul_version int4 NULL, -- Versión del registro.
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT vulnerabilidades_aud_pkey PRIMARY KEY (vul_pk, rev)
);

CREATE TABLE IF NOT EXISTS catalogo.sg_af_tipos_unidades(
    tun_pk bigserial NOT NULL, -- Número correlativo autogenerado.
    tun_codigo varchar(4) NULL, -- Código del registro.
    tun_habilitado bool NULL, -- Indica si el registro se encuentra habilitado(true) o inhabilitado(false).
    tun_es_unidad_administrativa bool NULL, -- Indica si el registro representa a una unidad administrativa(true) o no(false).
    tun_nombre varchar(255) NULL, -- Nombre del registro.
    tun_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
    tun_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
    tun_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
    tun_version int4 NULL, -- Versión del registro
    CONSTRAINT sg_af_tipos_unidades_pk PRIMARY KEY (tun_pk),
    CONSTRAINT tun_codigo_uk UNIQUE (tun_codigo),
    CONSTRAINT tun_nombre_uk UNIQUE (tun_nombre)
);
COMMENT ON TABLE catalogo.sg_af_tipos_unidades IS 'Tabla para el registro de los Opciones de Descargo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_es_unidad_administrativa IS 'Indica si el registro representa a una unidad administrativa(true) o no(false).';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_tipos_unidades.tun_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_af_tipos_unidades ALTER tun_pk SET DEFAULT nextval('catalogo.sg_af_tipos_unidades_pk_seq');

CREATE TABLE IF NOT EXISTS catalogo.sg_af_tipos_unidades_aud (
    tun_pk bigserial NOT NULL,
    tun_codigo varchar(4) NULL,
    tun_habilitado bool NULL,
    tun_es_unidad_administrativa bool NULL,
    tun_nombre varchar(255) NULL,
    tun_nombre_busqueda varchar(255) NULL,
    tun_ult_mod_fecha timestamp NULL,
    tun_ult_mod_usuario varchar(45) NULL,
    tun_version int4 null,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_af_tipos_unidades_aud_pkey PRIMARY KEY (tun_pk, rev)
);

--OPERACIONES UNIDADES DE ACTIVO FIJO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_UNIDAD_ACTIVO_FIJO','C368',  'Permiso para crear Unidades de Activo Fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_UNIDAD_ACTIVO_FIJO','C369',  'Permiso para actualizar Unidades de Activo Fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_UNIDAD_ACTIVO_FIJO','C370',  'Permiso para eliminar Unidades de Activo Fijo', 2, true, null, null,0);

--OPERACIONES UNIDADES ADMINISTRATIVAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_UNIDAD_ADMINISTRATIVA','C371',  'Permiso para crear Unidades Administrativas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_UNIDAD_ADMINISTRATIVA','C372',  'Permiso para actualizar Unidades Administrativas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_UNIDAD_ADMINISTRATIVA','C373',  'Permiso para eliminar Unidades Administrativas', 2, true, null, null,0);

--TABLA UNIDADES DE ACTIVO FIJO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_af_unidades_activo_fijo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_af_unidades_activo_fijo (
	uaf_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	uaf_codigo varchar(5) NULL, -- Código del registro.
	uaf_nombre varchar(255) NULL, -- Nombre del registro.
	uaf_nombre_busqueda varchar(255) NULL, -- Nombre del registro.
	uaf_habilitado bool NULL, -- Dertermina si el registro esta habilitado(true) o no(false).
	uaf_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	uaf_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	uaf_version int4 NULL, -- Versión del registro
	uaf_unidad_central bool NULL, -- Dertermina si el registro es de unidad central(true) o no(false).
	uaf_departamento_fk int8 NULL, -- LLave foranea del departamento
	CONSTRAINT auf_nombre_uk UNIQUE (uaf_nombre),
	CONSTRAINT uaf_codigo_uk UNIQUE (uaf_codigo),
	CONSTRAINT unidades_activo_fijo_pk PRIMARY KEY (uaf_pk),
	CONSTRAINT sg_unidad_activo_fijo_sg_departamentos_fk FOREIGN KEY (uaf_departamento_fk) REFERENCES catalogo.sg_departamentos(dep_pk)
);
COMMENT ON TABLE catalogo.sg_af_unidades_activo_fijo IS 'Tabla para el registro de Unidades de Activo Fijo.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_nombre_busqueda IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_habilitado IS 'Dertermina si el registro esta habilitado(true) o no(false).';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_version IS 'Versión del registro';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_unidad_central IS 'Dertermina si el registro es de unidad central(true) o no(false).';
COMMENT ON COLUMN catalogo.sg_af_unidades_activo_fijo.uaf_departamento_fk IS 'LLave foranea del departamento';

ALTER TABLE catalogo.sg_af_unidades_activo_fijo ALTER uaf_pk SET DEFAULT nextval('catalogo.sg_af_unidades_activo_fijo_pk_seq');

CREATE TABLE IF NOT EXISTS catalogo.sg_af_unidades_activo_fijo_aud (
	uaf_pk bigserial NOT NULL,
	uaf_codigo varchar(5) NULL,
	uaf_nombre varchar(255) NULL,
	uaf_nombre_busqueda varchar(255) NULL,
	uaf_habilitado bool NULL,
	uaf_ult_mod_fecha timestamp NULL,
	uaf_ult_mod_usuario varchar(45) NULL,
	uaf_version int4 NULL,
	uaf_unidad_central bool NULL,
	uaf_departamento_fk int8 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_unidades_activo_fijo_aud_pkey PRIMARY KEY (uaf_pk, rev)
);


CREATE SEQUENCE IF NOT EXISTS catalogo.sg_af_unidades_administrativas_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_af_unidades_administrativas (
	uad_pk int8 NOT NULL, -- Número correlativo autogenerado.
	uad_codigo varchar(5) NULL, -- Código del registro.
	uad_nombre varchar(255) NULL, -- Nombre del registro.
	uad_nombre_busqueda varchar(255) NULL, -- Nombre del registro normalizado para búsquedas.
	uad_habilitado bool NULL, -- Indica si el registro se encuentra habilitada(true) o inhabilitada(false).
	uad_direccion varchar(255) NULL, -- Dirección del registro.
	uad_nombre_director varchar(60) NULL, -- Nombre del director del registro.
	uad_cargo_director varchar(60) NULL, -- Cargo del director del registro.
	uad_telefono varchar(60) NULL, -- Telefono del registro.
	uad_responsable varchar(60) NULL, -- Responsable del registro.
	uad_fecha_inventario timestamp NULL, -- Fecha de inventario del registro.
	uad_unidad_activo_fijo_fk int8 NOT NULL, -- LLave foranea de Unidad de Activo Fijo.
        uad_tipo_unidad_fk int8 not NULL, --Llave foranea de Tipo de Unidad
	uad_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	uad_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	uad_version int4 NULL, -- Versión del registro
	CONSTRAINT uad_codigo_uk UNIQUE (uad_codigo),
	CONSTRAINT uad_nombre_uk UNIQUE (uad_nombre),
	CONSTRAINT unidades_administrativas_pk PRIMARY KEY (uad_pk),
	CONSTRAINT tipo_unidad_fk FOREIGN KEY (uad_tipo_unidad_fk) REFERENCES catalogo.sg_af_tipos_unidades(tun_pk),
	CONSTRAINT uad_unidad_activo_fijo_fk FOREIGN KEY (uad_unidad_activo_fijo_fk) REFERENCES catalogo.sg_af_unidades_activo_fijo(uaf_pk)
);


COMMENT ON TABLE catalogo.sg_af_unidades_administrativas IS 'Tabla para el registro de Unidades Administrativas.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_habilitado IS 'Indica si el registro se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_nombre IS 'Nombre del registro.';

COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_direccion IS 'Dirección del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_nombre_director IS 'Nombre del director del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_cargo_director IS 'Cargo del director del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_telefono IS 'Telefono del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_responsable IS 'Responsable del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_fecha_inventario IS 'Fecha de inventario del registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_unidad_activo_fijo_fk IS 'LLave foranea de Unidad de Activo Fijo.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_tipo_unidad_fk IS 'Llave foranea de Tipo de Unidad.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_unidades_administrativas.uad_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_af_unidades_administrativas ALTER uad_pk SET DEFAULT nextval('catalogo.sg_af_unidades_administrativas_pk_seq');

CREATE TABLE IF NOT EXISTS catalogo.sg_af_unidades_administrativas_aud (
    uad_pk int8 NOT NULL,
	uad_codigo varchar(5) NULL, 
	uad_nombre varchar(255) NULL,
	uad_nombre_busqueda varchar(255) NULL,
	uad_habilitado bool NULL,
	uad_direccion varchar(255) null,
	uad_nombre_director varchar(60) null,
	uad_cargo_director varchar(60) null,
	uad_telefono varchar(60) null,
	uad_responsable varchar(60) null,
	uad_fecha_inventario timestamp null,
	uad_unidad_activo_fijo_fk int8 not null,
        uad_tipo_unidad_fk int8 not NULL,
	uad_ult_mod_fecha timestamp NULL,
	uad_ult_mod_usuario varchar(45) NULL,
	uad_version int4 NULL,
        rev int8 NOT NULL,
        revtype int2 NULL,
    CONSTRAINT unidades_administrativas_aud_pkey PRIMARY KEY (uad_pk, rev)
);

--TABLA FUENTES DE FINANCIAMIENTO PARA ACTIVO FIJO
CREATE SEQUENCE catalogo.sg_af_fuente_financiamiento_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
    



CREATE TABLE catalogo.sg_af_fuente_financiamiento (
	ffi_pk int8 NOT NULL, 
	ffi_codigo varchar(45) NULL, 
	ffi_habilitado bool NULL, 
	ffi_nombre varchar(255) NULL, 
	ffi_nombre_busqueda varchar(255) NULL, 
	ffi_fuente_mined bool NULL,
	ffi_fuente_departamental bool NULL,
	ffi_fuente_centro_educativo bool NULL,
	ffi_ult_mod_fecha timestamp NULL, 
	ffi_ult_mod_usuario varchar(45) NULL, 
	ffi_version int4 NULL, 
	CONSTRAINT codigo_uk UNIQUE (ffi_codigo),
	CONSTRAINT fuente_pk PRIMARY KEY (ffi_pk),
	CONSTRAINT nombre_uk UNIQUE (ffi_nombre)
);

COMMENT ON TABLE catalogo.sg_af_fuente_financiamiento IS 'Tabla para el registro de fuentes de financiamiento de activo fijo.';

ALTER TABLE catalogo.sg_af_fuente_financiamiento ALTER ffi_pk SET DEFAULT nextval('catalogo.sg_af_fuente_financiamiento_pk_seq');

-- Column comments
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_fuente_mined IS 'Fuente financiamiento para MINED.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_fuente_departamental IS 'Fuente financiamiento para DEPARTAMENTAL.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_fuente_centro_educativo IS 'Fuente financiamiento para CENTRO EDUCATIVO.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_fuente_financiamiento.ffi_version IS 'Versión del registro.';


CREATE TABLE catalogo.sg_af_fuente_financiamiento_aud (
	ffi_pk int8 NOT NULL, 
	ffi_codigo varchar(45) NULL, 
	ffi_habilitado bool NULL, 
	ffi_nombre varchar(255) NULL, 
	ffi_nombre_busqueda varchar(255) NULL, 
	ffi_fuente_mined bool NULL,
	ffi_fuente_departamental bool NULL,
	ffi_fuente_centro_educativo bool NULL,
	ffi_ult_mod_fecha timestamp NULL, 
	ffi_ult_mod_usuario varchar(45) NULL, 
	ffi_version int4 NULL, 
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_fuente_financiamiento_aud_pkey PRIMARY KEY (ffi_pk, rev)
);


--OPERACIONES FUENTES FINANCIAMIENTO ACTIVO FIJO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FUENTE_FINANCIAMIENTO_AF','C374',  'Permiso para crear fuente de financiamiento de activo fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FUENTE_FINANCIAMIENTO_AF','C375',  'Permiso para actualizar fuente de financiamiento de activo fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FUENTE_FINANCIAMIENTO_AF','C376',  'Permiso para eliminar fuente de financiamiento de activo fijo', 2, true, null, null,0);

-- 3.10.0

-- Cargas de Archivos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_cargas_archivos_cga_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_cargas_archivos (
    cga_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_cargas_archivos_cga_pk_seq'::regclass), 
    cga_codigo character varying(45), 
    cga_habilitado boolean, 
    cga_nombre character varying(255), 
    cga_nombre_busqueda character varying(255), 
    cga_tipo_archivo character varying(25), 
    cga_formatos_permitidos character varying(100), 
    cga_ancho integer,
    cga_alto integer,
    cga_ult_mod_fecha timestamp without time zone, 
    cga_ult_mod_usuario character varying(45), 
    cga_version integer, 
    CONSTRAINT sg_cargas_archivos_pkey PRIMARY KEY (cga_pk), 
    CONSTRAINT cga_codigo_uk UNIQUE (cga_codigo), 
    CONSTRAINT cga_nombre_uk UNIQUE (cga_nombre)
);

COMMENT ON TABLE catalogo.sg_cargas_archivos IS 'Tabla para el registro de cargas de archivos.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_tipo_archivo IS 'Tipo de archivo.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_formatos_permitidos IS 'Formatos permitidos.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_ancho IS 'Ancho en pixeles de la imagen.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_alto IS 'Alto en pixeles de la imagen.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_cargas_archivos.cga_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS catalogo.sg_cargas_archivos_aud (
    cga_pk bigint NOT NULL, 
    cga_codigo character varying(45), 
    cga_habilitado boolean, 
    cga_nombre character varying(255), 
    cga_nombre_busqueda character varying(255), 
    cga_tipo_archivo character varying(25), 
    cga_formatos_permitidos character varying(100), 
    cga_ancho integer,
    cga_alto integer,
    cga_ult_mod_fecha timestamp without time zone, 
    cga_ult_mod_usuario character varying(45), 
    cga_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_cargas_archivos_aud_pkey PRIMARY KEY (cga_pk, rev),
    CONSTRAINT sg_cargas_archivos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CARGAS_ARCHIVO','C377',  'Cargas de archivo: Crear un nuevo tipo de carga', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CARGAS_ARCHIVO','C378',  'Cargas de archivo: Editar un nuevo tipo de carga', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CARGAS_ARCHIVO','C379',  'Cargas de archivo: Eliminar un nuevo tipo de carga', 2, true, null, null,0);

CREATE TABLE IF NOT EXISTS catalogo.sg_definicion_formulas (
    dti_pk bigint NOT NULL, 
    fom_pk bigint NOT NULL, 
    CONSTRAINT sg_definicion_formulas_definicion_fk FOREIGN KEY (dti_pk) 
        REFERENCES catalogo.sg_definiciones_titulo (dti_pk), 
    CONSTRAINT sg_definicion_formulas_formula_fk FOREIGN KEY (fom_pk) 
        REFERENCES catalogo.sg_formulas (fom_pk)
);
CREATE TABLE IF NOT EXISTS catalogo.sg_definicion_formulas_aud(
    dti_pk bigint NOT NULL,
    fom_pk bigint NOT NULL, 
    rev bigint, 
    revtype smallint
);

INSERT INTO catalogo.sg_cargas_archivos (cga_codigo,cga_habilitado,cga_nombre,cga_nombre_busqueda,cga_tipo_archivo,cga_formatos_permitidos,cga_ancho,cga_alto,cga_ult_mod_fecha,cga_ult_mod_usuario,cga_version) VALUES 
('01',true,'SELLOS Y FIRMAS','sellos y firmas','IMAGENES','/(\.|\/)(jpe?g|png|pdf)$/',500,500,'2019-05-14 22:04:14.204','casuser',4)
;

-- 3.11.0

-- Motivos de fallecimientos 
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_fallecimiento_mfa_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_fallecimiento
               ( 
                            mfa_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_motivos_fallecimiento_mfa_pk_seq'::regclass),
                            mfa_codigo CHARACTER varying(45), 
                            mfa_habilitado BOOLEAN, 
                            mfa_nombre CHARACTER varying(255), 
                            mfa_nombre_busqueda CHARACTER varying(255), 
                            mfa_ult_mod_fecha timestamp without TIME zone, 
                            mfa_ult_mod_usuario CHARACTER varying(45), 
                            mfa_version INTEGER, 
                            CONSTRAINT sg_motivos_fallecimiento_pkey PRIMARY KEY (mfa_pk), 
                            CONSTRAINT mfa_codigo_uk UNIQUE (mfa_codigo), 
                            CONSTRAINT mfa_nombre_uk UNIQUE (mfa_nombre) 
               );

COMMENT ON TABLE catalogo.sg_motivos_fallecimiento IS 'Tabla para el registro de motivos de fallecimientos.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_codigo IS  'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_fallecimiento.mfa_version IS 'Versión del registro.';

CREATE TABLE IF not EXISTS catalogo.sg_motivos_fallecimiento_aud 
               ( 
                            mfa_pk bigint NOT NULL, 
                            mfa_codigo CHARACTER varying(45), 
                            mfa_habilitado BOOLEAN, 
                            mfa_nombre CHARACTER varying(255), 
                            mfa_nombre_busqueda CHARACTER varying(255), 
                            mfa_ult_mod_fecha timestamp without TIME zone, 
                            mfa_ult_mod_usuario CHARACTER varying(45), 
                            mfa_version INTEGER, 
                            rev         bigint, 
                            revtype     SMALLINT,
                            CONSTRAINT sg_motivos_fallecimiento_aud_pkey PRIMARY KEY (mfa_pk, rev),
                            CONSTRAINT sg_motivos_fallecimiento_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
               );


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_FALLECIMIENTO','C383',  'Motivo de fallecimiento: crear', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_FALLECIMIENTO','C384',  'Motivo de fallecimiento: actualizar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_FALLECIMIENTO','C385',  'Motivo de fallecimiento: eliminar', 2, true, null, null,0);-- Máximo nivel educativo alcanzado


-- 3.11.1


CREATE SEQUENCE IF NOT EXISTS catalogo.sg_maximo_nivel_educativo_alcanzado_mne_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_maximo_nivel_educativo_alcanzado (mne_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_maximo_nivel_educativo_alcanzado_mne_pk_seq'::regclass), mne_codigo character varying(45), mne_habilitado boolean, mne_nombre character varying(255), mne_nombre_busqueda character varying(255), mne_ult_mod_fecha timestamp without time zone, mne_ult_mod_usuario character varying(45), mne_version integer, CONSTRAINT sg_maximo_nivel_educativo_alcanzado_pkey PRIMARY KEY (mne_pk), CONSTRAINT mne_codigo_uk UNIQUE (mne_codigo), CONSTRAINT mne_nombre_uk UNIQUE (mne_nombre));
COMMENT ON TABLE catalogo.sg_maximo_nivel_educativo_alcanzado IS 'Tabla para el registro de máximo nivel educativo alcanzado.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_maximo_nivel_educativo_alcanzado.mne_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_maximo_nivel_educativo_alcanzado_aud (mne_pk bigint NOT NULL, mne_codigo character varying(45), mne_habilitado boolean, mne_nombre character varying(255), mne_nombre_busqueda character varying(255), mne_ult_mod_fecha timestamp without time zone, mne_ult_mod_usuario character varying(45), mne_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_maximo_nivel_educativo_alcanzado_aud ADD PRIMARY KEY (mne_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MAXIMO_NIVEL_EDUCATIVO_ALCANZADO','C389',  'Maximo nivel educativo alcanzado: crear', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MAXIMO_NIVEL_EDUCATIVO_ALCANZADO','C390',  'Maximo nivel educativo alcanzado: actualizar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MAXIMO_NIVEL_EDUCATIVO_ALCANZADO','C391',  'Maximo nivel educativo alcanzado: eliminar', 2, true, null, null,0);


-- 3.12.0

--SE ELIMINAN COLUMNAS DE FUENTES DE FINANCIAMIENTO Y SE CREA UNA NUEVA
ALTER TABLE catalogo.sg_af_fuente_financiamiento DROP COLUMN ffi_fuente_mined;
ALTER TABLE catalogo.sg_af_fuente_financiamiento DROP COLUMN ffi_fuente_departamental;
ALTER TABLE catalogo.sg_af_fuente_financiamiento DROP COLUMN ffi_fuente_centro_educativo;
ALTER TABLE catalogo.sg_af_fuente_financiamiento ADD ffi_aplica_para varchar[] null;

ALTER TABLE catalogo.sg_af_fuente_financiamiento_aud DROP COLUMN ffi_fuente_mined;
ALTER TABLE catalogo.sg_af_fuente_financiamiento_aud DROP COLUMN ffi_fuente_departamental;
ALTER TABLE catalogo.sg_af_fuente_financiamiento_aud DROP COLUMN ffi_fuente_centro_educativo;
ALTER TABLE catalogo.sg_af_fuente_financiamiento_aud ADD ffi_aplica_para varchar[] null;

--UPDATE DE CATEGORIA DE BIENES - SE AGREGA CAMPO GASTO_ESPECIFICO
ALTER TABLE catalogo.sg_af_categoria_bienes ADD cab_cod_gasto_especifico varchar(10) NULL;
ALTER TABLE catalogo.sg_af_categoria_bienes_aud ADD cab_cod_gasto_especifico varchar(10) NULL;

--UPDATE DE TAMAÑO DE COLUMNA DE CODIGO DE ESTADOS BIENES
ALTER TABLE catalogo.sg_af_estados_bienes ALTER COLUMN ebi_codigo TYPE varchar(15);
ALTER TABLE catalogo.sg_af_estados_bienes_aud ALTER COLUMN ebi_codigo TYPE varchar(15);

--SE ELIMINA COLUMNA TIPO_UNIDAD DE UNIDADES ADMINISTRATIVAS
ALTER TABLE catalogo.sg_af_unidades_administrativas DROP COLUMN uad_tipo_unidad_fk;
ALTER TABLE catalogo.sg_af_unidades_administrativas_aud DROP COLUMN uad_tipo_unidad_fk;
DROP TABLE catalogo.sg_af_tipos_unidades CASCADE ;
DROP TABLE catalogo.sg_af_tipos_unidades_aud CASCADE;


--SE ELIMINA UNA COLUMNA  A CATEGORIA DE BIENES Y SE AGREGAN 1 COLUMNA (TIPO ARRAY) PARA DETERMINAR QUE SECCION MOSTRAR
ALTER TABLE catalogo.sg_af_categoria_bienes DROP COLUMN cab_es_tipo_vehiculo;
ALTER TABLE catalogo.sg_af_categoria_bienes_aud DROP COLUMN cab_es_tipo_vehiculo;

--SE AGREGAN 1 COLUMNA (TIPO ARRAY) PARA DETERMINAR QUE SECCION MOSTRAR
ALTER TABLE catalogo.sg_af_categoria_bienes ADD cab_secciones_bienes varchar[] null  default '{BIENES MUEBLES}';
ALTER TABLE catalogo.sg_af_categoria_bienes_aud ADD cab_secciones_bienes varchar[] null  default '{BIENES MUEBLES}';

ALTER TABLE catalogo.sg_af_estados_bienes ADD ebi_aplica_para varchar[] null  default '{Cargo}';
ALTER TABLE catalogo.sg_af_estados_bienes_aud ADD ebi_aplica_para varchar[] null  default '{Cargo}';


CREATE UNIQUE INDEX tipo_bien_index ON catalogo.sg_af_tipo_bienes(tbi_codigo,tbi_nombre,tbi_categoria_bienes_fk);

ALTER TABLE catalogo.sg_af_tipo_bienes ADD CONSTRAINT tipo_bien_uk UNIQUE USING INDEX tipo_bien_index;

--UPDATE CLAVE UNICA PARA UNIDADES ADMINISTRATIVAS
ALTER TABLE catalogo.sg_af_unidades_administrativas DROP CONSTRAINT uad_codigo_uk;
ALTER TABLE catalogo.sg_af_unidades_administrativas drop constraint uad_nombre_uk;
CREATE UNIQUE INDEX unidades_administrativas_index ON catalogo.sg_af_unidades_administrativas(uad_codigo,uad_nombre);

ALTER TABLE catalogo.sg_af_unidades_administrativas ADD CONSTRAINT unidades_administrativas_uk UNIQUE USING INDEX unidades_administrativas_index;


--CREA TABLA DE EMPLEADOS DE LAS UNIDADES ADMINISTRATIVAS
CREATE SEQUENCE catalogo.sg_af_empleados_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_af_empleados (
	emp_pk int8 NOT NULL,
	emp_nombres varchar(100) NULL,
	emp_apellidos varchar(100) NULL,
	emp_cargo varchar(200) NULL,
	emp_unidad_administrativa_fk int8 NULL,
	emp_habilitado bool NULL, 
	emp_ult_mod_fecha timestamp NULL,
	emp_ult_mod_usuario varchar(45) NULL,
	emp_version int4 NULL,
	CONSTRAINT empleados_pk PRIMARY KEY (emp_pk),
	CONSTRAINT unidad_administrativa_fk FOREIGN KEY (emp_unidad_administrativa_fk) REFERENCES catalogo.sg_af_unidades_administrativas(uad_pk)
);
COMMENT ON TABLE catalogo.sg_af_empleados IS 'Tabla para el registro de Bienes Depreciables (Activos).';

-- Column comments

COMMENT ON COLUMN catalogo.sg_af_empleados.emp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_nombres IS 'Nombres del empleado';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_apellidos IS 'Apellidos del empleado.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_cargo IS 'Cargo del empleado.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_unidad_administrativa_fk IS 'LLave foranea de la unidad administrativa a la que pertenece el empleado.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_habilitado IS 'Determina si el empleado está activo(Si) o inactivo(No).';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_empleados.emp_version IS 'Versión del registro.';

ALTER TABLE catalogo.sg_af_empleados ALTER emp_pk SET DEFAULT nextval('catalogo.sg_af_empleados_pk_seq');


CREATE TABLE catalogo.sg_af_empleados_aud (
	emp_pk int8 NOT NULL,
	emp_nombres varchar(100) NULL,
	emp_apellidos varchar(100) NULL,
	emp_cargo varchar(200) NULL,
	emp_unidad_administrativa_fk int8 NULL,
	emp_habilitado bool NULL,
	emp_ult_mod_fecha timestamp NULL,
	emp_ult_mod_usuario varchar(45) NULL,
	emp_version int4 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_empleados_aud_pkey PRIMARY KEY (emp_pk, rev)
);


--OPERACIONES CRUD EMPLEADOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EMPLEADO','C386',  'Permiso para crear empleados de Unidades Administrativas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EMPLEADO','C387',  'Permiso para actualizar empleados de Unidades Administrativas', 2, true, null, null,0);

--3.13.0

--Agrega columna a fuente de financiamiento de activo fijo
ALTER TABLE catalogo.sg_af_fuente_financiamiento ADD ffi_requiere_proyecto bool NULL;
ALTER TABLE catalogo.sg_af_fuente_financiamiento_aud ADD ffi_requiere_proyecto bool NULL;


CREATE SEQUENCE IF NOT EXISTS catalogo.sg_estado_inmueble_ein_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_estado_inmueble (ein_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_estado_inmueble_ein_pk_seq'::regclass), ein_codigo character varying(45), ein_habilitado boolean,ein_habilita_datos_inscripcion boolean, ein_nombre character varying(255), ein_nombre_busqueda character varying(255), ein_ult_mod_fecha timestamp without time zone, ein_ult_mod_usuario character varying(45), ein_version integer, CONSTRAINT sg_estado_inmueble_pkey PRIMARY KEY (ein_pk), CONSTRAINT ein_codigo_uk UNIQUE (ein_codigo), CONSTRAINT ein_nombre_uk UNIQUE (ein_nombre));
COMMENT ON TABLE catalogo.sg_estado_inmueble IS 'Tabla para el registro de estado inmueble.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_habilita_datos_inscripcion IS 'Indica si habilita datos inscripcion.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estado_inmueble.ein_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_estado_inmueble_aud (ein_pk bigint NOT NULL, ein_codigo character varying(45), ein_habilitado boolean,ein_habilita_datos_inscripcion boolean, ein_nombre character varying(255), ein_nombre_busqueda character varying(255), ein_ult_mod_fecha timestamp without time zone, ein_ult_mod_usuario character varying(45), ein_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_estado_inmueble_aud ADD PRIMARY KEY (ein_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_INMUEBLE','C392',  'Estado inmueble: Crear', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_INMUEBLE','C393',  'Estado inmueble: Actualizar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_INMUEBLE','C394',  'Estado inmueble: Eliminar', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_INFRAESTRUCTURA','CP3',  '', 2, true, null, null,0);

-- Espacio Común
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_espacios_comunes_eco_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_espacios_comunes (eco_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_espacios_comunes_eco_pk_seq'::regclass), eco_codigo character varying(45), eco_habilitado boolean,eco_aplica_terreno boolean,eco_aplica_edificio boolean,eco_aplica_aula boolean,eco_descripcion character varying(500), eco_nombre character varying(255), eco_nombre_busqueda character varying(255), eco_ult_mod_fecha timestamp without time zone, eco_ult_mod_usuario character varying(45), eco_version integer, CONSTRAINT sg_espacios_comunes_pkey PRIMARY KEY (eco_pk), CONSTRAINT eco_codigo_uk UNIQUE (eco_codigo), CONSTRAINT eco_nombre_uk UNIQUE (eco_nombre));
COMMENT ON TABLE catalogo.sg_espacios_comunes IS 'Tabla para el registro de espacio común.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_espacios_comunes_aud (eco_pk bigint NOT NULL, eco_codigo character varying(45), eco_habilitado boolean,eco_aplica_terreno boolean,eco_aplica_edificio boolean,eco_aplica_aula boolean, eco_descripcion character varying(500),eco_nombre character varying(255), eco_nombre_busqueda character varying(255), eco_ult_mod_fecha timestamp without time zone, eco_ult_mod_usuario character varying(45), eco_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_espacios_comunes_aud ADD PRIMARY KEY (eco_pk, rev);



-- Servicio Básico
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_servicios_basicos_sba_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_servicios_basicos (sba_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_servicios_basicos_sba_pk_seq'::regclass), sba_codigo character varying(45), sba_habilitado boolean,sba_aplica_terreno boolean,sba_aplica_edificio boolean,sba_aplica_aula boolean,sba_descripcion character varying(500),  sba_nombre character varying(255), sba_nombre_busqueda character varying(255), sba_ult_mod_fecha timestamp without time zone, sba_ult_mod_usuario character varying(45), sba_version integer, CONSTRAINT sg_servicios_basicos_pkey PRIMARY KEY (sba_pk), CONSTRAINT sba_codigo_uk UNIQUE (sba_codigo), CONSTRAINT sba_nombre_uk UNIQUE (sba_nombre));
COMMENT ON TABLE catalogo.sg_servicios_basicos IS 'Tabla para el registro de servicio básico.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_servicios_basicos.sba_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_servicios_basicos_aud (sba_pk bigint NOT NULL, sba_codigo character varying(45), sba_habilitado boolean,sba_aplica_terreno boolean,sba_aplica_edificio boolean,sba_aplica_aula boolean,sba_descripcion character varying(500),  sba_nombre character varying(255), sba_nombre_busqueda character varying(255), sba_ult_mod_fecha timestamp without time zone, sba_ult_mod_usuario character varying(45), sba_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_servicios_basicos_aud ADD PRIMARY KEY (sba_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESPACIOS_COMUNES','C395',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESPACIOS_COMUNES','C396',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESPACIOS_COMUNES','C397',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SERVICIOS_BASICOS','C398',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SERVICIOS_BASICOS','C399',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SERVICIOS_BASICOS','C400',  '', 2, true, null, null,0);

-- Propietarios de terreno
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_propietarios_terreno_pdt_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_propietarios_terreno (pdt_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_propietarios_terreno_pdt_pk_seq'::regclass), pdt_codigo character varying(45), pdt_habilitado boolean, pdt_nombre character varying(255), pdt_nombre_busqueda character varying(255), pdt_ult_mod_fecha timestamp without time zone, pdt_ult_mod_usuario character varying(45), pdt_version integer, CONSTRAINT sg_propietarios_terreno_pkey PRIMARY KEY (pdt_pk), CONSTRAINT pdt_codigo_uk UNIQUE (pdt_codigo), CONSTRAINT pdt_nombre_uk UNIQUE (pdt_nombre));
COMMENT ON TABLE catalogo.sg_propietarios_terreno IS 'Tabla para el registro de propietarios de terreno.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_propietarios_terreno.pdt_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_propietarios_terreno_aud (pdt_pk bigint NOT NULL, pdt_codigo character varying(45), pdt_habilitado boolean, pdt_nombre character varying(255), pdt_nombre_busqueda character varying(255), pdt_ult_mod_fecha timestamp without time zone, pdt_ult_mod_usuario character varying(45), pdt_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_propietarios_terreno_aud ADD PRIMARY KEY (pdt_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROPIETARIO_TERRENO','C401',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROPIETARIO_TERRENO','C402',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROPIETARIO_TERRENO','C403',  '', 2, true, null, null,0);

--3.14.0


-- Nombres de extracciones
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_est_nombres_extracciones_nex_pk_seq 
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_est_nombres_extracciones (
    nex_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_est_nombres_extracciones_nex_pk_seq'::regclass), 
    nex_codigo character varying(45), 
    nex_habilitado boolean, 
    nex_nombre character varying(255), 
    nex_nombre_busqueda character varying(255), 
    nex_ult_mod_fecha timestamp without time zone, 
    nex_ult_mod_usuario character varying(45), 
    nex_version integer, 
    CONSTRAINT sg_est_nombres_extracciones_pkey PRIMARY KEY (nex_pk), 
    CONSTRAINT nex_codigo_uk UNIQUE (nex_codigo), 
    CONSTRAINT nex_nombre_uk UNIQUE (nex_nombre)
);
    COMMENT ON TABLE catalogo.sg_est_nombres_extracciones IS 'Tabla para el registro de nombres de extracciones.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_codigo IS 'Código del registro.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN catalogo.sg_est_nombres_extracciones.nex_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_est_nombres_extracciones_aud (
    nex_pk bigint NOT NULL, 
    nex_codigo character varying(45), 
    nex_habilitado boolean, 
    nex_nombre character varying(255), 
    nex_nombre_busqueda character varying(255), 
    nex_ult_mod_fecha timestamp without time zone, 
    nex_ult_mod_usuario character varying(45), 
    nex_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_est_nombres_extracciones_aud_pkey PRIMARY KEY (nex_pk, rev),
    CONSTRAINT sg_est_nombres_extracciones_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NOMBRE_EXTRACCION','C407',  'Crear un nombre de extración', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NOMBRE_EXTRACCION','C408',  'Actualizar un nombre de extración', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NOMBRE_EXTRACCION','C409',  'Eliminar un nombre de extración', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_ESTADISTICA','CP4',  'Renderizar el menú de catálogos de estadísticas', 2, true, null, null,0);




-- Datasets
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_est_datasets_dat_pk_seq 
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_est_datasets (
    dat_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_est_datasets_dat_pk_seq'::regclass), 
    dat_codigo character varying(45), 
    dat_habilitado boolean, 
    dat_nombre character varying(255), 
    dat_nombre_busqueda character varying(255), 
    dat_ult_mod_fecha timestamp without time zone, 
    dat_ult_mod_usuario character varying(45), 
    dat_version integer, 
    CONSTRAINT sg_est_datasets_pkey PRIMARY KEY (dat_pk), 
    CONSTRAINT dat_codigo_uk UNIQUE (dat_codigo), 
    CONSTRAINT dat_nombre_uk UNIQUE (dat_nombre)
);
COMMENT ON TABLE catalogo.sg_est_datasets IS 'Tabla para el registro de datasets.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_est_datasets.dat_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_est_datasets_aud (
    dat_pk bigint NOT NULL, 
    dat_codigo character varying(45), 
    dat_habilitado boolean, 
    dat_nombre character varying(255), 
    dat_nombre_busqueda character varying(255), 
    dat_ult_mod_fecha timestamp without time zone, 
    dat_ult_mod_usuario character varying(45), 
    dat_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_est_datasets_aud_pkey PRIMARY KEY (dat_pk, rev),
    CONSTRAINT sg_est_datasets_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DATASETS','C410',  'Crear datasets', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DATASETS','C411',  'Actualizar datasets', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DATASETS','C412',  'Eliminar datasets', 2, true, null, null,0);


-- Categorías indicador
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_est_categorias_indicadores_cin_pk_seq 
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_est_categorias_indicadores (
    cin_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_est_categorias_indicadores_cin_pk_seq'::regclass), 
    cin_codigo character varying(45), 
    cin_habilitado boolean, 
    cin_nombre character varying(255), 
    cin_nombre_busqueda character varying(255), 
    cin_ult_mod_fecha timestamp without time zone, 
    cin_ult_mod_usuario character varying(45), 
    cin_version integer, 
    CONSTRAINT sg_est_categorias_indicadores_pkey PRIMARY KEY (cin_pk), 
    CONSTRAINT cin_codigo_uk UNIQUE (cin_codigo), 
    CONSTRAINT cin_nombre_uk UNIQUE (cin_nombre)
);
COMMENT ON TABLE catalogo.sg_est_categorias_indicadores IS 'Tabla para el registro de categorías indicador.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_est_categorias_indicadores.cin_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_est_categorias_indicadores_aud (
    cin_pk bigint NOT NULL, 
    cin_codigo character varying(45), 
    cin_habilitado boolean, 
    cin_nombre character varying(255), 
    cin_nombre_busqueda character varying(255), 
    cin_ult_mod_fecha timestamp without time zone, 
    cin_ult_mod_usuario character varying(45), 
    cin_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_est_categorias_indicadores_aud_pkey PRIMARY KEY (cin_pk, rev),
    CONSTRAINT sg_est_categorias_indicadores_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATEGORIA_INDICADOR','C413',  'Crear una categoria indicador', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CATEGORIA_INDICADOR','C414',  'Actualizar una categoria indicador', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATEGORIA_INDICADOR','C415',  'Eliminar una categoria indicador', 2, true, null, null,0);


-- Tipos de Construcción
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_construccion_tco_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_construccion (tco_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_construccion_tco_pk_seq'::regclass), tco_codigo character varying(45), tco_habilitado boolean, tco_nombre character varying(255), tco_nombre_busqueda character varying(255), tco_ult_mod_fecha timestamp without time zone, tco_ult_mod_usuario character varying(45), tco_version integer, CONSTRAINT sg_tipo_construccion_pkey PRIMARY KEY (tco_pk), CONSTRAINT tipo_cons_codigo_uk UNIQUE (tco_codigo), CONSTRAINT tipo_cons_nombre_uk UNIQUE (tco_nombre));
COMMENT ON TABLE catalogo.sg_tipo_construccion IS 'Tabla para el registro de tipos de construcción.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_construccion.tco_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_construccion_aud (tco_pk bigint NOT NULL, tco_codigo character varying(45), tco_habilitado boolean, tco_nombre character varying(255), tco_nombre_busqueda character varying(255), tco_ult_mod_fecha timestamp without time zone, tco_ult_mod_usuario character varying(45), tco_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_tipo_construccion_aud ADD PRIMARY KEY (tco_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_CONSTRUCCION','C404',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_CONSTRUCCION','C405',  '', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_CONSTRUCCION','C406',  '', 2, true, null, null,0);

-- 3.14.1
ALTER TABLE catalogo.sg_af_tipo_bienes DROP CONSTRAINT sg_tipo_bienes_codigo_uk;
ALTER TABLE catalogo.sg_af_tipo_bienes DROP CONSTRAINT sg_tipo_bienes_nombre_uk;


ALTER TABLE catalogo.sg_af_categoria_bienes ADD cab_porcentaje_valor_residual int2 NULL;
ALTER TABLE catalogo.sg_af_categoria_bienes_aud ADD cab_porcentaje_valor_residual int2 NULL;

ALTER TABLE catalogo.sg_af_proyectos ADD pro_proyecto_liquidado bool NULL;
ALTER TABLE catalogo.sg_af_proyectos_aud ADD pro_proyecto_liquidado bool NULL;

--3.15.0
-- Estados proceso legalizacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_estado_proceso_legalizacion_epl_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_estado_proceso_legalizacion (epl_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_estado_proceso_legalizacion_epl_pk_seq'::regclass), epl_codigo character varying(45), epl_habilitado boolean,epl_aplica_dato_presentacion boolean,epl_aplica_estado_proceso boolean, epl_nombre character varying(255), epl_nombre_busqueda character varying(255), epl_ult_mod_fecha timestamp without time zone, epl_ult_mod_usuario character varying(45), epl_version integer, CONSTRAINT sg_estado_proceso_legalizacion_pkey PRIMARY KEY (epl_pk), CONSTRAINT epl_codigo_uk UNIQUE (epl_codigo), CONSTRAINT epl_nombre_uk UNIQUE (epl_nombre));
COMMENT ON TABLE catalogo.sg_estado_proceso_legalizacion IS 'Tabla para el registro de estados proceso legalizacion.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_aplica_dato_presentacion IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_aplica_estado_proceso IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estado_proceso_legalizacion.epl_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_estado_proceso_legalizacion_aud (epl_pk bigint NOT NULL, epl_codigo character varying(45), epl_habilitado boolean,epl_aplica_dato_presentacion boolean,epl_aplica_estado_proceso boolean, epl_nombre character varying(255), epl_nombre_busqueda character varying(255), epl_ult_mod_fecha timestamp without time zone, epl_ult_mod_usuario character varying(45), epl_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_estado_proceso_legalizacion_aud ADD PRIMARY KEY (epl_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_PROCESO_LEGALIZACION','C416',  'Crea estado proceso legalizacion', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_PROCESO_LEGALIZACION','C417',  'Actualiza estado proceso legalizacion', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_PROCESO_LEGALIZACION','C418',  'Elimina estado proceso legalizacion', 2, true, null, null,0);


-- Ministerio otorgante
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_ministerio_otorgante_mio_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_ministerio_otorgante (mio_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_ministerio_otorgante_mio_pk_seq'::regclass), mio_codigo character varying(45), mio_habilitado boolean, mio_nombre character varying(255), mio_nombre_busqueda character varying(255), mio_ult_mod_fecha timestamp without time zone, mio_ult_mod_usuario character varying(45), mio_version integer, CONSTRAINT sg_inf_ministerio_otorgante_pkey PRIMARY KEY (mio_pk), CONSTRAINT mio_codigo_uk UNIQUE (mio_codigo), CONSTRAINT mio_nombre_uk UNIQUE (mio_nombre));
COMMENT ON TABLE catalogo.sg_inf_ministerio_otorgante IS 'Tabla para el registro de ministerio otorgante.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_ministerio_otorgante.mio_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_ministerio_otorgante_aud (mio_pk bigint NOT NULL, mio_codigo character varying(45), mio_habilitado boolean, mio_nombre character varying(255), mio_nombre_busqueda character varying(255), mio_ult_mod_fecha timestamp without time zone, mio_ult_mod_usuario character varying(45), mio_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_ministerio_otorgante_aud ADD PRIMARY KEY (mio_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MINISTERIO_OTORGANTE','C419',  'Crea ministerio otorgante', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MINISTERIO_OTORGANTE','C420',  'Actualiza ministerio otorgante', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MINISTERIO_OTORGANTE','C421',  'Elimina ministerio otorgante', 2, true, null, null,0);



-- Naturaleza contrato
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_naturaleza_contrato_nac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_naturaleza_contrato (nac_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_naturaleza_contrato_nac_pk_seq'::regclass), nac_codigo character varying(45), nac_habilitado boolean, nac_nombre character varying(255), nac_nombre_busqueda character varying(255), nac_ult_mod_fecha timestamp without time zone, nac_ult_mod_usuario character varying(45), nac_version integer, CONSTRAINT sg_inf_naturaleza_contrato_pkey PRIMARY KEY (nac_pk), CONSTRAINT naturaleza_codigo_uk UNIQUE (nac_codigo), CONSTRAINT naturaleza_nombre_uk UNIQUE (nac_nombre));
COMMENT ON TABLE catalogo.sg_inf_naturaleza_contrato IS 'Tabla para el registro de naturaleza contrato.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_naturaleza_contrato.nac_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_naturaleza_contrato_aud (nac_pk bigint NOT NULL, nac_codigo character varying(45), nac_habilitado boolean, nac_nombre character varying(255), nac_nombre_busqueda character varying(255), nac_ult_mod_fecha timestamp without time zone, nac_ult_mod_usuario character varying(45), nac_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_naturaleza_contrato_aud ADD PRIMARY KEY (nac_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NATURALEZA_CONTRATO','C422',  'Crea naturaleza contrato', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NATURALEZA_CONTRATO','C423',  'Actualiza naturaleza contrato', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NATURALEZA_CONTRATO','C424',  'Elimina naturaleza contrato', 2, true, null, null,0);


--3.15.1
ALTER TABLE catalogo.sg_espacios_comunes ADD eco_permite_ingresar_descripcion boolean;
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_permite_ingresar_descripcion IS 'Registro permite ingresar descripción.';
ALTER TABLE catalogo.sg_espacios_comunes_aud ADD eco_permite_ingresar_descripcion boolean;


--3.15.2
ALTER TABLE catalogo.sg_tipos_servicio_sanitario ADD tss_aplica_estudiante boolean;
COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_aplica_estudiante IS 'Registro aplica a estudiante.';
ALTER TABLE catalogo.sg_tipos_servicio_sanitario_aud ADD tss_aplica_estudiante boolean;

ALTER TABLE catalogo.sg_tipos_servicio_sanitario ADD tss_aplica_inmueble boolean;
COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_aplica_inmueble IS 'Registro aplica a inmueble.';
ALTER TABLE catalogo.sg_tipos_servicio_sanitario_aud ADD tss_aplica_inmueble boolean;

ALTER TABLE catalogo.sg_tipos_servicio_sanitario ADD tss_aplica_edificio boolean;
COMMENT ON COLUMN catalogo.sg_tipos_servicio_sanitario.tss_aplica_edificio IS 'Registro aplica a edificio.';
ALTER TABLE catalogo.sg_tipos_servicio_sanitario_aud ADD tss_aplica_edificio boolean;
-- Tipos de Acuerdos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_acuerdos_tao_pk_seq 
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_acuerdos (
    tao_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_acuerdos_tao_pk_seq'::regclass), 
    tao_codigo character varying(45), 
    tao_habilitado boolean, 
    tao_nombre character varying(255), 
    tao_nombre_busqueda character varying(255), 
    tao_ult_mod_fecha timestamp without time zone, 
    tao_ult_mod_usuario character varying(45), 
    tao_version integer, 
    CONSTRAINT sg_tipos_acuerdos_pkey PRIMARY KEY (tao_pk), 
    CONSTRAINT tao_codigo_uk UNIQUE (tao_codigo), 
    CONSTRAINT tao_nombre_uk UNIQUE (tao_nombre)
);
COMMENT ON TABLE catalogo.sg_tipos_acuerdos IS 'Tabla para el registro de tipos de acuerdos.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_acuerdos.tao_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_acuerdos_aud (
    tao_pk bigint NOT NULL, 
    tao_codigo character varying(45), 
    tao_habilitado boolean, 
    tao_nombre character varying(255), 
    tao_nombre_busqueda character varying(255), 
    tao_ult_mod_fecha timestamp without time zone, 
    tao_ult_mod_usuario character varying(45), 
    tao_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_tipos_acuerdos_aud_pkey PRIMARY KEY (tao_pk, rev),
    CONSTRAINT sg_tipos_acuerdos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_ACUERDO','C425',  'Crear un tipo de acuerdo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_ACUERDO','C426',  'Actualizar un tipo de acuerdo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_ACUERDO','C427',  'Eliminar un tipo de acuerdo', 2, true, null, null,0);

--3.16.0
-- Tiplogías construcción
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipologia_construccion_tic_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipologia_construccion (tic_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipologia_construccion_tic_pk_seq'::regclass), tic_codigo character varying(45), tic_habilitado boolean, tic_nombre character varying(255), tic_nombre_busqueda character varying(255), tic_ult_mod_fecha timestamp without time zone, tic_ult_mod_usuario character varying(45), tic_version integer, CONSTRAINT sg_inf_tipologia_construccion_pkey PRIMARY KEY (tic_pk), CONSTRAINT tic_codigo_uk UNIQUE (tic_codigo), CONSTRAINT tic_nombre_uk UNIQUE (tic_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipologia_construccion IS 'Tabla para el registro de tiplogías construcción.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipologia_construccion.tic_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipologia_construccion_aud (tic_pk bigint NOT NULL, tic_codigo character varying(45), tic_habilitado boolean, tic_nombre character varying(255), tic_nombre_busqueda character varying(255), tic_ult_mod_fecha timestamp without time zone, tic_ult_mod_usuario character varying(45), tic_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipologia_construccion_aud ADD PRIMARY KEY (tic_pk, rev);
-- Tipo imagen
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipo_imagen_tii_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_imagen (tii_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipo_imagen_tii_pk_seq'::regclass), tii_codigo character varying(45), tii_habilitado boolean, tii_nombre character varying(255), tii_nombre_busqueda character varying(255), tii_ult_mod_fecha timestamp without time zone, tii_ult_mod_usuario character varying(45), tii_version integer, CONSTRAINT sg_inf_tipo_imagen_pkey PRIMARY KEY (tii_pk), CONSTRAINT tii_codigo_uk UNIQUE (tii_codigo), CONSTRAINT tii_nombre_uk UNIQUE (tii_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipo_imagen IS 'Tabla para el registro de tipo imagen.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_imagen.tii_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_imagen_aud (tii_pk bigint NOT NULL, tii_codigo character varying(45), tii_habilitado boolean, tii_nombre character varying(255), tii_nombre_busqueda character varying(255), tii_ult_mod_fecha timestamp without time zone, tii_ult_mod_usuario character varying(45), tii_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipo_imagen_aud ADD PRIMARY KEY (tii_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_IMAGEN','C431',  'Crea tipo imagen', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_IMAGEN','C432',  'Actualiza tipo imagen', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_IMAGEN','C433',  'Elimina tipo imagen', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOLOGIA_CONSTRUCCION','C434',  'Crea tipologia construcción', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOLOGIA_CONSTRUCCION','C435',  'Actualiza tipología construcción', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOLOGIA_CONSTRUCCION','C436',  'Elimina tipología construccion', 2, true, null, null,0);


--3.16.2
ALTER TABLE catalogo.sg_espacios_comunes ADD COLUMN eco_orden int;
COMMENT ON COLUMN catalogo.sg_espacios_comunes.eco_orden IS 'Orden espacio comun';
ALTER TABLE catalogo.sg_espacios_comunes_aud ADD COLUMN eco_orden int;

--3.17.0
--tipo documentos infra
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipo_documento_tid_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_documento (tid_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipo_documento_tid_pk_seq'::regclass),
 tid_codigo character varying(45),
 tid_habilitado boolean, 
tid_nombre character varying(255),
 tid_nombre_busqueda character varying(255),
 tid_ult_mod_fecha timestamp without time zone, 
tid_ult_mod_usuario character varying(45), 
tid_version integer, CONSTRAINT sg_inf_tipo_documento_pkey PRIMARY KEY (tid_pk),
 CONSTRAINT tid_codigo_uk UNIQUE (tid_codigo), 
CONSTRAINT tid_nombre_uk UNIQUE (tid_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipo_documento IS 'Tabla para el registro de tipo imagen.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_documento.tid_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_documento_aud (tid_pk bigint NOT NULL, tid_codigo character varying(45), tid_habilitado boolean, tid_nombre character varying(255), tid_nombre_busqueda character varying(255), tid_ult_mod_fecha timestamp without time zone, tid_ult_mod_usuario character varying(45), tid_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipo_documento_aud ADD PRIMARY KEY (tid_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_DOCUMENTO_INFRA','C437', 'Crea tipo documento infraestructura', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_DOCUMENTO_INFRA','C438', 'Actualiza tipo documento infraestructura', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_DOCUMENTO_INFRA','C439', 'Elimina tipo documento infraestructura', 2, true, null, null,0);

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('TIPO_ARCHIVO_IMPORT_INFRA', 'tipo archivos importar infraestructura','tipo archivos importar infraestructura','/(\.|\/)(xlsx)$/');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('TAMANIO_ARCHIVO_IMPORT_INFRA', 'tamaño archivos importar infraestructura','tamaño archivos importar infraestructura','1048576');


-- 3.17.1
ALTER TABLE catalogo.sg_cargo ADD COLUMN car_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_cargo ADD COLUMN car_aplica_contrato boolean;
ALTER TABLE catalogo.sg_cargo ADD COLUMN car_aplica_otros boolean;
ALTER TABLE catalogo.sg_cargo_aud ADD COLUMN car_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_cargo_aud ADD COLUMN car_aplica_contrato boolean;
ALTER TABLE catalogo.sg_cargo_aud ADD COLUMN car_aplica_otros boolean;

ALTER TABLE catalogo.sg_tipo_nombramiento ADD COLUMN tno_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_tipo_nombramiento ADD COLUMN tno_aplica_contrato boolean;
ALTER TABLE catalogo.sg_tipo_nombramiento ADD COLUMN tno_aplica_otros boolean;
ALTER TABLE catalogo.sg_tipo_nombramiento_aud ADD COLUMN tno_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_tipo_nombramiento_aud ADD COLUMN tno_aplica_contrato boolean;
ALTER TABLE catalogo.sg_tipo_nombramiento_aud ADD COLUMN tno_aplica_otros boolean;

ALTER TABLE catalogo.sg_tipo_contrato ADD COLUMN tco_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_tipo_contrato ADD COLUMN tco_aplica_contrato boolean;
ALTER TABLE catalogo.sg_tipo_contrato ADD COLUMN tco_aplica_otros boolean;
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD COLUMN tco_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD COLUMN tco_aplica_contrato boolean;
ALTER TABLE catalogo.sg_tipo_contrato_aud ADD COLUMN tco_aplica_otros boolean;

ALTER TABLE catalogo.sg_fuente_financiamiento ADD COLUMN ffi_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_fuente_financiamiento ADD COLUMN ffi_aplica_contrato boolean;
ALTER TABLE catalogo.sg_fuente_financiamiento ADD COLUMN ffi_aplica_otros boolean;
ALTER TABLE catalogo.sg_fuente_financiamiento_aud ADD COLUMN ffi_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_fuente_financiamiento_aud ADD COLUMN ffi_aplica_contrato boolean;
ALTER TABLE catalogo.sg_fuente_financiamiento_aud ADD COLUMN ffi_aplica_otros boolean;

ALTER TABLE catalogo.sg_institucion_paga_salario ADD COLUMN ips_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_institucion_paga_salario ADD COLUMN ips_aplica_contrato boolean;
ALTER TABLE catalogo.sg_institucion_paga_salario ADD COLUMN ips_aplica_otros boolean;
ALTER TABLE catalogo.sg_institucion_paga_salario_aud ADD COLUMN ips_aplica_acuerdo boolean;
ALTER TABLE catalogo.sg_institucion_paga_salario_aud ADD COLUMN ips_aplica_contrato boolean;
ALTER TABLE catalogo.sg_institucion_paga_salario_aud ADD COLUMN ips_aplica_otros boolean;


-- Estados de datos de contratación
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_estados_datos_contratacion_edc_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_estados_datos_contratacion (
    edc_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_estados_datos_contratacion_edc_pk_seq'::regclass), 
    edc_codigo character varying(45), 
    edc_habilitado boolean, 
    edc_nombre character varying(255), 
    edc_nombre_busqueda character varying(255), 
    edc_ult_mod_fecha timestamp without time zone, 
    edc_ult_mod_usuario character varying(45), 
    edc_version integer, 
    CONSTRAINT sg_estados_datos_contratacion_pkey PRIMARY KEY (edc_pk), 
    CONSTRAINT edc_codigo_uk UNIQUE (edc_codigo), CONSTRAINT edc_nombre_uk UNIQUE (edc_nombre)
);
COMMENT ON TABLE catalogo.sg_estados_datos_contratacion IS 'Tabla para el registro de estados de datos de contratación.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_estados_datos_contratacion.edc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_estados_datos_contratacion_aud (
    edc_pk bigint NOT NULL, 
    edc_codigo character varying(45), 
    edc_habilitado boolean, 
    edc_nombre character varying(255), 
    edc_nombre_busqueda character varying(255), 
    edc_ult_mod_fecha timestamp without time zone, 
    edc_ult_mod_usuario character varying(45), 
    edc_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_estados_datos_contratacion_aud_pkey PRIMARY KEY (edc_pk, rev), 
    CONSTRAINT sg_estados_datos_contratacion_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTADO_DATO_CONTRATACION','C380',  'Estado dato contratacion: crear un nuevo estado', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTADO_DATO_CONTRATACION','C381',  'Estado dato contratacion: actualizar estado', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTADO_DATO_CONTRATACION','C382',  'Estado dato contratacion: eliminar estado', 2, true, null, null,0);


--3.18.0

-- Tipo Mejora
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipo_mejora_tme_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_mejora (tme_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipo_mejora_tme_pk_seq'::regclass),tme_aplica_inmueble boolean, tme_aplica_edificio boolean, tme_codigo character varying(45), tme_habilitado boolean, tme_nombre character varying(255), tme_nombre_busqueda character varying(255), tme_ult_mod_fecha timestamp without time zone, tme_ult_mod_usuario character varying(45), tme_version integer, CONSTRAINT sg_inf_tipo_mejora_pkey PRIMARY KEY (tme_pk), CONSTRAINT tme_codigo_uk UNIQUE (tme_codigo), CONSTRAINT tme_nombre_uk UNIQUE (tme_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipo_mejora IS 'Tabla para el registro de tipo mejora.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_mejora.tme_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_mejora_aud (tme_pk bigint NOT NULL,tme_aplica_inmueble boolean, tme_aplica_edificio boolean, tme_codigo character varying(45), tme_habilitado boolean, tme_nombre character varying(255), tme_nombre_busqueda character varying(255), tme_ult_mod_fecha timestamp without time zone, tme_ult_mod_usuario character varying(45), tme_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipo_mejora_aud ADD PRIMARY KEY (tme_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_MEJORA','C440',  'Habilita crear tipo mejora.', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_MEJORA','C441',  'Habilita actualizar tipo mejora.', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_MEJORA','C442',  'Habilita eliminar tipo mejora', 2, true, null, null,0);

-- Accesibilidad
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_accesibilidad_acc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_accesibilidad (acc_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_accesibilidad_acc_pk_seq'::regclass),acc_orden int, acc_aplica_otros boolean, acc_codigo character varying(45), acc_habilitado boolean, acc_nombre character varying(255), acc_nombre_busqueda character varying(255), acc_ult_mod_fecha timestamp without time zone, acc_ult_mod_usuario character varying(45), acc_version integer, CONSTRAINT sg_inf_accesibilidad_pkey PRIMARY KEY (acc_pk), CONSTRAINT acc_codigo_uk UNIQUE (acc_codigo), CONSTRAINT acc_nombre_uk UNIQUE (acc_nombre));
COMMENT ON TABLE catalogo.sg_inf_accesibilidad IS 'Tabla para el registro de accesibilidad.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_accesibilidad.acc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_accesibilidad_aud (acc_pk bigint NOT NULL, acc_orden int, acc_aplica_otros boolean, acc_codigo character varying(45), acc_habilitado boolean, acc_nombre character varying(255), acc_nombre_busqueda character varying(255), acc_ult_mod_fecha timestamp without time zone, acc_ult_mod_usuario character varying(45), acc_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_accesibilidad_aud ADD PRIMARY KEY (acc_pk, rev);

-- Tipo Abastecimiento
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipo_abastecimiento_tab_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_abastecimiento (tab_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipo_abastecimiento_tab_pk_seq'::regclass), tab_codigo character varying(45), tab_habilitado boolean, tab_nombre character varying(255), tab_nombre_busqueda character varying(255), tab_ult_mod_fecha timestamp without time zone, tab_ult_mod_usuario character varying(45), tab_version integer, CONSTRAINT sg_inf_tipo_abastecimiento_pkey PRIMARY KEY (tab_pk), CONSTRAINT tab_codigo_uk UNIQUE (tab_codigo), CONSTRAINT tab_nombre_uk UNIQUE (tab_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipo_abastecimiento IS 'Tabla para el registro de tipo abastecimiento.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_abastecimiento.tab_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_abastecimiento_aud (tab_pk bigint NOT NULL, tab_codigo character varying(45), tab_habilitado boolean, tab_nombre character varying(255), tab_nombre_busqueda character varying(255), tab_ult_mod_fecha timestamp without time zone, tab_ult_mod_usuario character varying(45), tab_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipo_abastecimiento_aud ADD PRIMARY KEY (tab_pk, rev);
-- Tratamiento Agua
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tratamiento_agua_tra_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tratamiento_agua (tra_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tratamiento_agua_tra_pk_seq'::regclass),tra_orden int, tra_aplica_otros boolean, tra_aplica_abastecimiento_agua boolean, tra_aplica_almacenamiento_agua boolean,tra_aplica_drenaje boolean,  tra_codigo character varying(45), tra_habilitado boolean, tra_nombre character varying(255), tra_nombre_busqueda character varying(255), tra_ult_mod_fecha timestamp without time zone, tra_ult_mod_usuario character varying(45), tra_version integer, CONSTRAINT sg_inf_tratamiento_agua_pkey PRIMARY KEY (tra_pk), CONSTRAINT tra_codigo_uk UNIQUE (tra_codigo), CONSTRAINT tra_nombre_uk UNIQUE (tra_nombre));
COMMENT ON TABLE catalogo.sg_inf_tratamiento_agua IS 'Tabla para el registro de tratamiento agua.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tratamiento_agua.tra_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tratamiento_agua_aud (tra_pk bigint NOT NULL,tra_orden int, tra_aplica_otros boolean, tra_aplica_abastecimiento_agua boolean, tra_aplica_almacenamiento_agua boolean,tra_aplica_drenaje boolean, tra_codigo character varying(45), tra_habilitado boolean, tra_nombre character varying(255), tra_nombre_busqueda character varying(255), tra_ult_mod_fecha timestamp without time zone, tra_ult_mod_usuario character varying(45), tra_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tratamiento_agua_aud ADD PRIMARY KEY (tra_pk, rev);
-- Tipo Manejo Desechos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_tipo_manejo_desechos_tmd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_manejo_desechos (tmd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_tipo_manejo_desechos_tmd_pk_seq'::regclass),tmd_orden int, tmd_aplica_otros boolean,  tmd_codigo character varying(45), tmd_habilitado boolean, tmd_nombre character varying(255), tmd_nombre_busqueda character varying(255), tmd_ult_mod_fecha timestamp without time zone, tmd_ult_mod_usuario character varying(45), tmd_version integer, CONSTRAINT sg_inf_tipo_manejo_desechos_pkey PRIMARY KEY (tmd_pk), CONSTRAINT tmd_codigo_uk UNIQUE (tmd_codigo), CONSTRAINT tmd_nombre_uk UNIQUE (tmd_nombre));
COMMENT ON TABLE catalogo.sg_inf_tipo_manejo_desechos IS 'Tabla para el registro de tipo manejo desechos.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_tipo_manejo_desechos.tmd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_tipo_manejo_desechos_aud (tmd_pk bigint NOT NULL, tmd_orden int, tmd_aplica_otros boolean, tmd_codigo character varying(45), tmd_habilitado boolean, tmd_nombre character varying(255), tmd_nombre_busqueda character varying(255), tmd_ult_mod_fecha timestamp without time zone, tmd_ult_mod_usuario character varying(45), tmd_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_tipo_manejo_desechos_aud ADD PRIMARY KEY (tmd_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACCESIBILIDAD','C443',  'Crea accesibilidad', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACCESIBILIDAD','C444',  'Actualiza accesibilidad', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACCESIBILIDAD','C445',  'Elimina accesibilidad', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_ABASTECIMIENTO','C446',  'Crea tipo abastecimiento', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_ABASTECIMIENTO','C447',  'Actualiza tipo abastecimiento', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_ABASTECIMIENTO','C448',  'Elimina tipo abastecimiento', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_MANEJO_DESECHOS','C449',  'Crea tipo manejo desechos', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_MANEJO_DESECHOS','C450',  'Actualiza tipo manejo desechos', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_MANEJO_DESECHOS','C451',  'Elimina tipo manejo desechos', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TRATAMIENTO_AGUA','C452',  'Crea tratamiento agua', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TRATAMIENTO_AGUA','C453',  'Actualiza tratamiento agua', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TRATAMIENTO_AGUA','C454',  'Elimina tratamiento agua', 2, true, null, null,0);

--3.19.0

-- Obras exteriores
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_obra_exterior_oex_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_obra_exterior (oex_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_obra_exterior_oex_pk_seq'::regclass),oex_orden integer,oex_aplica_otro boolean, oex_codigo character varying(45), oex_habilitado boolean, oex_nombre character varying(255), oex_nombre_busqueda character varying(255), oex_ult_mod_fecha timestamp without time zone, oex_ult_mod_usuario character varying(45), oex_version integer, CONSTRAINT sg_inf_obra_exterior_pkey PRIMARY KEY (oex_pk), CONSTRAINT oex_codigo_uk UNIQUE (oex_codigo));
COMMENT ON TABLE catalogo.sg_inf_obra_exterior IS 'Tabla para el registro de obras exteriores.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_obra_exterior.oex_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_obra_exterior_aud (oex_pk bigint NOT NULL, oex_orden integer,oex_aplica_otro boolean,oex_codigo character varying(45), oex_habilitado boolean, oex_nombre character varying(255), oex_nombre_busqueda character varying(255), oex_ult_mod_fecha timestamp without time zone, oex_ult_mod_usuario character varying(45), oex_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_obra_exterior_aud ADD PRIMARY KEY (oex_pk, rev);
-- Item obras exteriores
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_inf_item_obra_exterior_ioe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_item_obra_exterior (ioe_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_inf_item_obra_exterior_ioe_pk_seq'::regclass),ioe_orden integer,ioe_aplica_otro boolean,ioe_obra_exterior bigint, ioe_codigo character varying(45), ioe_habilitado boolean, ioe_nombre character varying(255), ioe_nombre_busqueda character varying(255), ioe_ult_mod_fecha timestamp without time zone, ioe_ult_mod_usuario character varying(45), ioe_version integer, 
CONSTRAINT sg_inf_item_obra_exterior_pkey PRIMARY KEY (ioe_pk),
CONSTRAINT sg_ioe_obra_exterior FOREIGN KEY (ioe_obra_exterior) REFERENCES catalogo.sg_inf_obra_exterior(oex_pk)
);
COMMENT ON TABLE catalogo.sg_inf_item_obra_exterior IS 'Tabla para el registro de item obras exteriores.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_inf_item_obra_exterior.ioe_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_inf_item_obra_exterior_aud (ioe_pk bigint NOT NULL,ioe_orden integer,ioe_aplica_otro boolean,ioe_obra_exterior bigint, ioe_codigo character varying(45), ioe_habilitado boolean, ioe_nombre character varying(255), ioe_nombre_busqueda character varying(255), ioe_ult_mod_fecha timestamp without time zone, ioe_ult_mod_usuario character varying(45), ioe_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_inf_item_obra_exterior_aud ADD PRIMARY KEY (ioe_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ITEM_OBRA_EXTERIOR','C455',  'Crea item obra exterior', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ITEM_OBRA_EXTERIOR','C456',  'Actualiza item obra exterior', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ITEM_OBRA_EXTERIOR','C457',  'Elimina item obra exterior', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_OBRA_EXTERIOR','C458',  'Crea obra exterior', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_OBRA_EXTERIOR','C459',  'Actualiza obra exterior', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_OBRA_EXTERIOR','C460',  'Elimina obra exterior', 2, true, null, null,0);

-- 3.20.0

-- Implementadoras
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_implementadoras_imp_pk_seq 
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_implementadoras (
    imp_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_implementadoras_imp_pk_seq'::regclass), 
    imp_codigo character varying(45), 
    imp_habilitado boolean, 
    imp_nombre character varying(255), 
    imp_nombre_busqueda character varying(255),  
    imp_orden integer, 
    imp_ult_mod_fecha timestamp without time zone, 
    imp_ult_mod_usuario character varying(45), 
    imp_version integer, 
    CONSTRAINT sg_implementadoras_pkey PRIMARY KEY (imp_pk), 
    CONSTRAINT imp_codigo_uk UNIQUE (imp_codigo), 
    CONSTRAINT imp_nombre_uk UNIQUE (imp_nombre), 
    CONSTRAINT imp_orden_uk UNIQUE (imp_orden)
);

COMMENT ON TABLE catalogo.sg_implementadoras IS 'Tabla para el registro de implementadoras.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_orden IS 'Orden del registro.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_implementadoras.imp_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS catalogo.sg_implementadoras_aud (
    imp_pk bigint NOT NULL, 
    imp_codigo character varying(45), 
    imp_habilitado boolean, 
    imp_nombre character varying(255), 
    imp_nombre_busqueda character varying(255),  
    imp_orden integer, 
    imp_ult_mod_fecha timestamp without time zone, 
    imp_ult_mod_usuario character varying(45), 
    imp_version integer, 
    rev bigint, 
    revtype smallint,
    CONSTRAINT sg_implementadoras_aud_pkey PRIMARY KEY (imp_pk, rev),
    CONSTRAINT sg_implementadoras_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IMPLEMENTADORAS','C461',  'Crear implementadora', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IMPLEMENTADORAS','C462',  'Actualizar implementadora', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IMPLEMENTADORAS','C463',  'Eliminar implementadora', 2, true, null, null,0);


-- 3.20.4

ALTER TABLE catalogo.sg_calificaciones ADD COLUMN cal_valor_certificado character varying(50);
ALTER TABLE catalogo.sg_calificaciones_aud ADD COLUMN cal_valor_certificado character varying(50);


-- 3.21.0

ALTER TABLE catalogo.sg_af_categoria_bienes ADD COLUMN cab_path_cargar character varying(50);
ALTER TABLE catalogo.sg_af_categoria_bienes_aud ADD COLUMN cab_path_cargar character varying(50);

-- 3.21.1

--TABLA DE SECCIONES DE CATEGORIAS DE LOS BIENES
CREATE SEQUENCE catalogo.sg_af_secciones_categoria_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_af_secciones_categoria (
	sca_pk int8 NOT NULL,
	sca_seccion varchar(50) NULL,
	sca_categoria_fk int8 null,
	sca_ult_mod_fecha timestamp NULL, 
	sca_ult_mod_usuario varchar(45) NULL, 
	sca_version int4 NULL, 
	CONSTRAINT sg_af_secciones_categoria_pk PRIMARY KEY (sca_pk),
	CONSTRAINT categoria_fk FOREIGN KEY (sca_categoria_fk) REFERENCES catalogo.sg_af_categoria_bienes(cab_pk)

);
COMMENT ON TABLE catalogo.sg_af_secciones_categoria IS 'Tabla para el registro de las secciones de las categorias de bienes.';

ALTER TABLE catalogo.sg_af_secciones_categoria ALTER sca_pk SET DEFAULT nextval('catalogo.sg_af_secciones_categoria_pk_seq');
-- Column comments

COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_seccion IS 'Nombre de la seccion.';
COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_categoria_fk IS 'LLave foranea de la categoria de la seccion.';
COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_af_secciones_categoria.sca_version IS 'Versión de la sección';

CREATE TABLE catalogo.sg_af_secciones_categoria_aud (
	sca_pk int8 NOT NULL,
	sca_seccion varchar(50) NULL,
	sca_categoria_fk int8 null,
	sca_ult_mod_fecha timestamp NULL, 
	sca_ult_mod_usuario varchar(45) NULL, 
	sca_version int4 NULL,  
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_secciones_categoria_aud_pkey PRIMARY KEY (sca_pk, rev)
);


-- 3.21.2
ALTER TABLE catalogo.sg_ayudas ADD COLUMN ayu_resaltada boolean;
ALTER TABLE catalogo.sg_ayudas_aud ADD COLUMN ayu_resaltada boolean;
UPDATE catalogo.sg_ayudas set ayu_resaltada = false;

ALTER TABLE catalogo.sg_calificaciones ADD COLUMN cal_valor_certificado character varying(50);
ALTER TABLE catalogo.sg_calificaciones_aud ADD COLUMN cal_valor_certificado character varying(50);

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('ALTO_ARCHIVO_SELLO_FIRMA_IMPORT', 'alto de archivo sello firma','alto de archivo sello firma','500');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('ANCHO_ARCHIVO_SELLO_FIRMA_IMPORT', 'ancho de archivo sello firma','ancho de archivo sello firma','500');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('TIPO_ARCHIVO_SELLO_FIRMA_IMPORT', 'tipo de archivo sello firma','tipo de archivo sello firma','/(\.|\/)(jpe?g|png)$/');

-- 3.22.0

ALTER TABLE catalogo.sg_tipos_organismo_administrativo ADD COLUMN toa_plazo_miembros boolean;
ALTER TABLE catalogo.sg_tipos_organismo_administrativo ADD COLUMN toa_plazo integer;
ALTER TABLE catalogo.sg_tipos_organismo_administrativo_aud ADD COLUMN toa_plazo_miembros boolean;
ALTER TABLE catalogo.sg_tipos_organismo_administrativo_aud ADD COLUMN toa_plazo integer;




-- Items Evaluar Organismo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_items_evaluar_organismos_ieo_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1 
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_items_evaluar_organismos (
    ieo_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_items_evaluar_organismos_ieo_pk_seq'::regclass), 
    ieo_codigo character varying(45), 
    ieo_orden integer, 
    ieo_nombre character varying(255), 
    ieo_nombre_busqueda character varying(255), 
    ieo_tipo_organismo_fk bigint, 
    ieo_ult_mod_fecha timestamp without time zone, 
    ieo_ult_mod_usuario character varying(45), 
    ieo_version integer, 
    CONSTRAINT sg_items_evaluar_organismos_pkey PRIMARY KEY (ieo_pk), 
    CONSTRAINT ieo_codigo_uk UNIQUE (ieo_codigo), 
    CONSTRAINT ieo_nombre_uk UNIQUE (ieo_nombre),
    CONSTRAINT sg_items_evaluar_organismos_sg_tipos_organismo_administrativo_fk 
        FOREIGN KEY (ieo_tipo_organismo_fk) 
        REFERENCES catalogo.sg_tipos_organismo_administrativo(toa_pk)
);
    
CREATE TABLE IF NOT EXISTS catalogo.sg_items_evaluar_organismos_aud (
    ieo_pk bigint NOT NULL, 
    ieo_codigo character varying(45), 
    ieo_orden integer, 
    ieo_nombre character varying(255), 
    ieo_nombre_busqueda character varying(255), 
    ieo_tipo_organismo_fk bigint, 
    ieo_ult_mod_fecha timestamp without time zone, 
    ieo_ult_mod_usuario character varying(45), 
    ieo_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_items_evaluar_organismos_aud_pkey PRIMARY KEY (ieo_pk,rev), 
    CONSTRAINT sg_items_evaluar_organismos_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

COMMENT ON TABLE catalogo.sg_items_evaluar_organismos IS 'Tabla para el registro de los items para evaluar los organismos de administracion escolar.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_tipo_organismo_fk IS 'Llave foránea que hace referencia al tipo de organismo de administración escolar.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_orden IS 'Orden del registro.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_items_evaluar_organismos.ieo_version IS 'Versión del registro.';


--Cargos OAE Tipos de organismos de administracion escolar
CREATE TABLE IF NOT EXISTS catalogo.sg_cargos_tipo_oae (
    coa_pk bigint NOT NULL, 
    toa_pk bigint NOT NULL, 
    CONSTRAINT sg_cargos_tipo_oae_cargo_fk FOREIGN KEY (coa_pk) 
        REFERENCES catalogo.sg_cargo_oae (coa_pk), 
    CONSTRAINT sg_cargos_tipo_oae_tipo_organismo_fk FOREIGN KEY (toa_pk) 
        REFERENCES catalogo.sg_tipos_organismo_administrativo (toa_pk)
);

CREATE TABLE IF NOT EXISTS catalogo.sg_cargos_tipo_oae_aud(
    coa_pk bigint NOT NULL,
    toa_pk bigint NOT NULL, 
    rev bigint, 
    revtype smallint
);

ALTER TABLE catalogo.sg_cargo_oae ADD COLUMN coa_orden integer;
ALTER TABLE catalogo.sg_cargo_oae_aud ADD COLUMN coa_orden integer;

--3.22.1
 -- Promotores
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_si_promotores_pro_pk_seq    
	INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_si_promotores (
    pro_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_si_promotores_pro_pk_seq'::regclass), 
    pro_codigo character varying(45), 
    pro_habilitado boolean, 
    pro_nombre character varying(255), 
    pro_nombre_busqueda character varying(255), 
    pro_ult_mod_fecha timestamp without time zone, 
    pro_ult_mod_usuario character varying(45), 
    pro_version integer, 
    CONSTRAINT sg_si_promotores_pkey PRIMARY KEY (pro_pk), 
    CONSTRAINT si_pro_codigo_uk UNIQUE (pro_codigo), 
    CONSTRAINT si_pro_nombre_uk UNIQUE (pro_nombre)
);
COMMENT ON TABLE catalogo.sg_si_promotores IS 'Tabla para el registro de promotores.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_si_promotores.pro_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_si_promotores_aud (
    pro_pk bigint NOT NULL, 
    pro_codigo character varying(45), 
    pro_habilitado boolean, 
    pro_nombre character varying(255), 
    pro_nombre_busqueda character varying(255), 
    pro_ult_mod_fecha timestamp without time zone, 
    pro_ult_mod_usuario character varying(45), 
    pro_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_si_promotores_aud_pkey PRIMARY KEY (pro_pk,rev), 
    CONSTRAINT sg_si_promotores_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_SI','CP5',  'Rederizar el menu de catálogos para los sistemas integrados', 2, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROMOTORES','C464',  'Crear promotores para los sistemas integrados', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROMOTORES','C465',  'Actualizar promotor para los sistemas integrados', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROMOTORES','C466',  'Eliminar promotor de los sistemas integrados', 2, true, null, null,0);


CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipos_requerimientos_infra_tri_pk_seq 
    INCREMENT 1 
    START 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    CACHE 1     
    NO CYCLE;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_requerimientos_infra (
    tri_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipos_requerimientos_infra_tri_pk_seq'::regclass), 
    tri_codigo character varying(45), 
    tri_habilitado boolean, 
    tri_nombre character varying(255), 
    tri_nombre_busqueda character varying(255), 
    tri_ult_mod_fecha timestamp without time zone, 
    tri_ult_mod_usuario character varying(45), 
    tri_version integer, 
    CONSTRAINT sg_tipos_requerimientos_infra_pkey PRIMARY KEY (tri_pk), 
    CONSTRAINT trin_codigo_uk UNIQUE (tri_codigo), 
    CONSTRAINT trin_nombre_uk UNIQUE (tri_nombre)
);
COMMENT ON TABLE catalogo.sg_tipos_requerimientos_infra IS 'Tabla para el registro de tipos de requerimientos de infraestructura.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipos_requerimientos_infra.tri_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipos_requerimientos_infra_aud (
    tri_pk bigint NOT NULL, 
    tri_codigo character varying(45), 
    tri_habilitado boolean, 
    tri_nombre character varying(255), 
    tri_nombre_busqueda character varying(255), 
    tri_ult_mod_fecha timestamp without time zone, 
    tri_ult_mod_usuario character varying(45), 
    tri_version integer, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_tipos_requerimientos_infra_aud_pkey PRIMARY KEY (tri_pk,rev), 
    CONSTRAINT sg_tipos_requerimientos_infra_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_REQUERIMIENTOS_INFRA','C467',  'Crear tipo de requerimiento de infraestructura', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_REQUERIMIENTOS_INFRA','C468',  'Actualizar tipo de requerimiento de infraestructura', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_REQUERIMIENTOS_INFRA','C469',  'Eliminar tipo de requerimiento de infraestructura', 2, true, null, null,0);

--CAMPO PARA EL ESTILO DE LOS ESTADOS DE LOS BIENES
ALTER TABLE catalogo.sg_af_estados_bienes ADD COLUMN ebi_estilo varchar(255);
ALTER TABLE catalogo.sg_af_estados_bienes_aud ADD COLUMN ebi_estilo varchar(255);


--3.23.0

-- Procesos nocturnos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_proceso_nocturno_prn_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_proceso_nocturno (prn_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_proceso_nocturno_prn_pk_seq'::regclass), prn_nombre character varying(255),prn_servicio character varying(255),prn_url character varying(255),  prn_ult_mod_fecha timestamp without time zone, prn_ult_mod_usuario character varying(45), prn_version integer, CONSTRAINT sg_proceso_nocturno_pkey PRIMARY KEY (prn_pk));
COMMENT ON TABLE catalogo.sg_proceso_nocturno IS 'Tabla para el registro de procesos nocturnos.';
COMMENT ON COLUMN catalogo.sg_proceso_nocturno.prn_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_proceso_nocturno.prn_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_proceso_nocturno.prn_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proceso_nocturno.prn_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proceso_nocturno.prn_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_proceso_nocturno_aud (prn_pk bigint NOT NULL, prn_nombre character varying(255),prn_servicio character varying(255),prn_url character varying(255),   prn_ult_mod_fecha timestamp without time zone, prn_ult_mod_usuario character varying(45), prn_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_proceso_nocturno_aud ADD PRIMARY KEY (prn_pk, rev);

-- Ejecución Procesos nocturnos
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_ejecucion_proceso_nocturno_epr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_ejecucion_proceso_nocturno (epr_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_ejecucion_proceso_nocturno_epr_pk_seq'::regclass),epr_proceso_nocturno_fk bigint,epr_comienzo_ejecucion timestamp,epr_fin_ejecucion timestamp,epr_ejecucion_correcta boolean,  epr_ult_mod_fecha timestamp without time zone, epr_ult_mod_usuario character varying(45), epr_version integer, CONSTRAINT sg_ejecucion_proceso_nocturno_pkey PRIMARY KEY (epr_pk),CONSTRAINT epr_proceso_nocturno_fkey FOREIGN KEY (epr_proceso_nocturno_fk) REFERENCES catalogo.sg_proceso_nocturno(prn_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE catalogo.sg_ejecucion_proceso_nocturno IS 'Tabla para el registro de ejecución procesos nocturnos.';
COMMENT ON COLUMN catalogo.sg_ejecucion_proceso_nocturno.epr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_ejecucion_proceso_nocturno.epr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ejecucion_proceso_nocturno.epr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ejecucion_proceso_nocturno.epr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_ejecucion_proceso_nocturno_aud (epr_pk bigint NOT NULL,epr_proceso_nocturno_fk bigint,epr_comienzo_ejecucion timestamp,epr_fin_ejecucion timestamp,epr_ejecucion_correcta boolean, epr_ult_mod_fecha timestamp without time zone, epr_ult_mod_usuario character varying(45), epr_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_ejecucion_proceso_nocturno_aud ADD PRIMARY KEY (epr_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROCESO_NOCTURNO','C470',  'Crear proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROCESO_NOCTURNO','C471',  'Actualizar proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROCESO_NOCTURNO','C472',  'Eliminar proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EJECUCION_PROCESO_NOCTURNO','C473',  'Crear ejecución proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EJECUCION_PROCESO_NOCTURNO','C474',  'Actualizar ejecución proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EJECUCION_PROCESO_NOCTURNO','C475',  'Eliminar ejecución proceso nocturno', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_PROCESOS_NOCTURNOS','CP6',  'Renderiza el menú procesos nocturnos', 2, true, null, null,0);

-- 3.23.1

--CAMPO PARA ELMONTO MAXIMO POR CATEGORIA
ALTER TABLE catalogo.sg_af_categoria_bienes ADD cab_valor_maximo numeric not null default 200000.00;
ALTER TABLE catalogo.sg_af_categoria_bienes_aud ADD cab_valor_maximo numeric not null default 200000.00;



--3.23.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_DOCUMENTO_SI','C476', 'Crea tipo documento sistemas integrados', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_DOCUMENTO_SI','C477', 'Actualiza tipo documento sistemas integrados', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_DOCUMENTO_SI','C478', 'Elimina tipo documento sistemas integrados', 2, true, null, null,0);


--tipo documentos sistemas integrados
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_si_tipo_documento_tid_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_si_tipo_documento (tid_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_si_tipo_documento_tid_pk_seq'::regclass),
 tid_codigo character varying(45),
 tid_habilitado boolean, 
tid_nombre character varying(255),
 tid_nombre_busqueda character varying(255),
 tid_ult_mod_fecha timestamp without time zone, 
tid_ult_mod_usuario character varying(45), 
tid_version integer, CONSTRAINT sg_si_tipo_documento_pkey PRIMARY KEY (tid_pk),
 CONSTRAINT tid_si_codigo_uk UNIQUE (tid_codigo), 
CONSTRAINT tid_si_nombre_uk UNIQUE (tid_nombre));
COMMENT ON TABLE catalogo.sg_si_tipo_documento IS 'Tabla para el registro de tipo imagen.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_si_tipo_documento.tid_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_si_tipo_documento_aud (tid_pk bigint NOT NULL, tid_codigo character varying(45), tid_habilitado boolean, tid_nombre character varying(255), tid_nombre_busqueda character varying(255), tid_ult_mod_fecha timestamp without time zone, tid_ult_mod_usuario character varying(45), tid_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_si_tipo_documento_aud ADD PRIMARY KEY (tid_pk, rev);

-- 3.24.1

--TABLA DE COMISION DE DESCARGO DE ACTIVO FIJO
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_af_comision_descargo_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_af_comision_descargo (
	cde_pk int8 NOT NULL,
	cde_nombre_representante varchar(255) NULL, 
	cde_cargo_representante varchar(255) NULL,
	cde_habilitado boolean null,
	cde_activo_fijo_fk int8 NULL,
	cde_ult_mod_fecha timestamp NULL,
	cde_ult_mod_usuario varchar(45) NULL, 
	cde_version int4 NULL, 
	CONSTRAINT comision_descargo PRIMARY KEY (cde_pk),
	CONSTRAINT comision_activo_fijo_fk FOREIGN KEY (cde_activo_fijo_fk) REFERENCES catalogo.sg_af_unidades_activo_fijo(uaf_pk)
);

COMMENT ON TABLE catalogo.sg_af_comision_descargo IS 'Tabla para el registro de Comision de descargo de Activo Fijo.';
-- Column comments

COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_nombre_representante IS 'nombdre del representante.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_cargo_representante IS 'cargo del representante.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_activo_fijo_fk IS 'Unidad de activo fijo.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_ult_mod_fecha IS 'Ultima fecha de modificacion.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_ult_mod_usuario IS 'Ultimo usuario de modificacion.';
COMMENT ON COLUMN catalogo.sg_af_comision_descargo.cde_version IS 'Version del registro.';


ALTER TABLE catalogo.sg_af_comision_descargo ALTER cde_pk SET DEFAULT nextval('catalogo.sg_af_comision_descargo_pk_seq');

CREATE TABLE IF NOT EXISTS catalogo.sg_af_comision_descargo_aud (
	cde_pk int8 NOT NULL,
	cde_nombre_representante varchar(255) NULL, 
	cde_cargo_representante varchar(255) NULL,
	cde_activo_fijo_fk int8 NULL,
	cde_habilitado boolean null,
	cde_ult_mod_fecha timestamp NULL,
	cde_ult_mod_usuario varchar(45) NULL, 
	cde_version int4 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_af_comision_descargo_aud_pkey PRIMARY KEY (cde_pk, rev)
);

--PERMISOS PARA CREAR COMISION DE DESCARGO
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_COMISION_DESCARGO_AF','C482',  'Permiso para crear una nueva comisión de descargo de activo fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_COMISION_DESCARGO_AF','C483',  'Permiso para Actualizar la comision de descargo de activo fijo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_COMISION_DESCARGO_AF','C484',  '   Permiso para Eliminar la comisión de descargo de activo fijo', 2, true, null, null,0);
                                                                                                                                                                                                                
--3.24.2
-- Motivo de anulación de título
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivo_anulacion_titulo_mat_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_anulacion_titulo (mat_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivo_anulacion_titulo_mat_pk_seq'::regclass), mat_codigo character varying(45), mat_habilitado boolean, mat_nombre character varying(255), mat_nombre_busqueda character varying(255), mat_ult_mod_fecha timestamp without time zone, mat_ult_mod_usuario character varying(45), mat_version integer, CONSTRAINT sg_motivo_anulacion_titulo_pkey PRIMARY KEY (mat_pk), CONSTRAINT mat_codigo_motivo_anulacion_titulo_uk UNIQUE (mat_codigo), CONSTRAINT mat_nombre_motivo_anulacion_titulo_uk UNIQUE (mat_nombre));
COMMENT ON TABLE catalogo.sg_motivo_anulacion_titulo IS 'Tabla para el registro de motivo de anulación de título.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivo_anulacion_titulo.mat_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_anulacion_titulo_aud (mat_pk bigint NOT NULL, mat_codigo character varying(45), mat_habilitado boolean, mat_nombre character varying(255), mat_nombre_busqueda character varying(255), mat_ult_mod_fecha timestamp without time zone, mat_ult_mod_usuario character varying(45), mat_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_motivo_anulacion_titulo_aud ADD PRIMARY KEY (mat_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_ANULACION_TITULO','C479',  'Crea motivo anulacion título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_ANULACION_TITULO','C480',  'Actualiza motivo anulacion título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_ANULACION_TITULO','C481',  'Elimina motivo anulacion título', 1, true, null, null,0);

ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_nombre_certificado character varying(255);
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_nombre_certificado character varying(255);

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('TITULO_FECHA_VALIDEZ_DESDE', 'Fecha de validez del título','Fecha de validez del título','17-12-2019');                                                                                                                                                                                                                
  
--3.25.0                                     
-- Proyectos Financiamiento Sistemas Integrados
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_proyecto_financiamiento_sistema_integrado_pfs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_proyecto_financiamiento_sistema_integrado (pfs_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_proyecto_financiamiento_sistema_integrado_pfs_pk_seq'::regclass), pfs_codigo character varying(45), pfs_habilitado boolean, pfs_nombre character varying(255), pfs_nombre_busqueda character varying(255), pfs_ult_mod_fecha timestamp without time zone, pfs_ult_mod_usuario character varying(45), pfs_version integer, CONSTRAINT sg_proyecto_financiamiento_sistema_integrado_pkey PRIMARY KEY (pfs_pk), CONSTRAINT pfs_codigo_uk UNIQUE (pfs_codigo), CONSTRAINT pfs_nombre_uk UNIQUE (pfs_nombre));
COMMENT ON TABLE catalogo.sg_proyecto_financiamiento_sistema_integrado IS 'Tabla para el registro de proyectos financiamiento sistemas integrados.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_proyecto_financiamiento_sistema_integrado.pfs_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_proyecto_financiamiento_sistema_integrado_aud (pfs_pk bigint NOT NULL, pfs_codigo character varying(45), pfs_habilitado boolean, pfs_nombre character varying(255), pfs_nombre_busqueda character varying(255), pfs_ult_mod_fecha timestamp without time zone, pfs_ult_mod_usuario character varying(45), pfs_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_proyecto_financiamiento_sistema_integrado_aud ADD PRIMARY KEY (pfs_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTOS_FINANCIAMIENTO_SISTEMA_INTEGRADO','C485', 'Permiso para crear un nuevo proyecto financiamiento', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTOS_FINANCIAMIENTO_SISTEMA_INTEGRADO','C486', 'Permiso para Actualizar un nuevo proyecto financiamiento', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTOS_FINANCIAMIENTO_SISTEMA_INTEGRADO','C487', 'Permiso para Eliminar un nuevo proyecto financiamiento', 1, true, null, null,0);
                                                                                                                                                                                                                

--3.25.1
-- Tipos Alfabetizador
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_alfabetizador_tal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_alfabetizador (tal_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_alfabetizador_tal_pk_seq'::regclass), tal_codigo character varying(45), tal_habilitado boolean, tal_nombre character varying(255), tal_nombre_busqueda character varying(255), tal_ult_mod_fecha timestamp without time zone, tal_ult_mod_usuario character varying(45), tal_version integer, CONSTRAINT sg_tipo_alfabetizador_pkey PRIMARY KEY (tal_pk), CONSTRAINT tal_codigo_uk UNIQUE (tal_codigo), CONSTRAINT tal_nombre_uk UNIQUE (tal_nombre));
COMMENT ON TABLE catalogo.sg_tipo_alfabetizador IS 'Tabla para el registro de tipos alfabetizador.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_alfabetizador.tal_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_alfabetizador_aud (tal_pk bigint NOT NULL, tal_codigo character varying(45), tal_habilitado boolean, tal_nombre character varying(255), tal_nombre_busqueda character varying(255), tal_ult_mod_fecha timestamp without time zone, tal_ult_mod_usuario character varying(45), tal_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_tipo_alfabetizador_aud ADD PRIMARY KEY (tal_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPO_ALFABETIZADOR','C488',  'Permiso para crear tipo alfabetizador', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPO_ALFABETIZADOR','C489',  'Permiso para actualizar tipo alfabetizador', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPO_ALFABETIZADOR','C490',  'Permiso para eliminar tipo alfabetizador', 1, true, null, null,0);

--3.25.2
-- Motivo de Reimpresión
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivo_reimpresion_mri_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_reimpresion (mri_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivo_reimpresion_mri_pk_seq'::regclass), mri_codigo character varying(45), mri_habilitado boolean, mri_nombre character varying(255), mri_nombre_busqueda character varying(255), mri_ult_mod_fecha timestamp without time zone, mri_ult_mod_usuario character varying(45), mri_version integer, CONSTRAINT sg_motivo_reimpresion_pkey PRIMARY KEY (mri_pk), CONSTRAINT mri_codigo_uk UNIQUE (mri_codigo), CONSTRAINT mri_nombre_uk UNIQUE (mri_nombre));
COMMENT ON TABLE catalogo.sg_motivo_reimpresion IS 'Tabla para el registro de motivo de reimpresión.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivo_reimpresion.mri_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivo_reimpresion_aud (mri_pk bigint NOT NULL, mri_codigo character varying(45), mri_habilitado boolean, mri_nombre character varying(255), mri_nombre_busqueda character varying(255), mri_ult_mod_fecha timestamp without time zone, mri_ult_mod_usuario character varying(45), mri_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_motivo_reimpresion_aud ADD PRIMARY KEY (mri_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVO_REIMPRESION','C491',  'Permiso para crear motivo reimpresion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVO_REIMPRESION','C492',  'Permiso para actualizar motivo reimpresion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVO_REIMPRESION','C493',  'Permiso para eliminar motivo reimpresion', 1, true, null, null,0);

-- 3.26.0

ALTER TABLE catalogo.sg_af_empleados ADD emp_persona_fk bigint;
ALTER TABLE catalogo.sg_af_empleados_aud ADD emp_persona_fk bigint;

ALTER TABLE catalogo.sg_af_empleados ADD CONSTRAINT persona_fk FOREIGN KEY (emp_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk);


--3.26.2
ALTER TABLE catalogo.sg_programas_institucional ADD pin_siap_pk bigint;
ALTER TABLE catalogo.sg_programas_institucional_aud ADD pin_siap_pk bigint;


ALTER TABLE catalogo.sg_fuente_financiamiento ADD ffi_siap_pk bigint;
ALTER TABLE catalogo.sg_fuente_financiamiento_aud ADD ffi_siap_pk bigint;

ALTER TABLE catalogo.sg_recursos_educativos ADD red_siap_pk bigint;
ALTER TABLE catalogo.sg_recursos_educativos_aud ADD red_siap_pk bigint;
DROP TABLE catalogo.sg_definicion_formulas; 
DROP TABLE catalogo.sg_definicion_formulas_aud; 

ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_formula_fk bigint;
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_formula_fk bigint;
ALTER TABLE catalogo.sg_definiciones_titulo ADD CONSTRAINT sg_dti_formula_fk FOREIGN KEY (dti_formula_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- 3.27.0

--UNIDADES ADMINISTRATIVAS
ALTER TABLE catalogo.sg_af_unidades_administrativas ADD COLUMN uad_cargo_responsable varchar(60) NULL;
ALTER TABLE catalogo.sg_af_unidades_administrativas_aud ADD COLUMN uad_cargo_responsable varchar(60) NULL;

--PROYECTOS INSTITUCIOANLES
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN pin_origen_transferencia varchar(100) NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN pin_convenio varchar(100) NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN pin_monto numeric NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales ADD COLUMN pin_condiciones_entrega varchar(100) NULL;

ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN pin_origen_transferencia varchar(100) NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN pin_convenio varchar(100) NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN pin_monto numeric NULL;
ALTER TABLE catalogo.sg_proyectos_institucionales_aud ADD COLUMN pin_condiciones_entrega varchar(100) NULL;


--ASOCIACIONES
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_anio_fundacion int2 NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_responsable_institucional varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_correo varchar(50) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_correo_alternativo varchar(50) NULL;

ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_nombre_coordiandor varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_telefono_coordiandor varchar(20) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_correo_coordiandor varchar(50) NULL;

ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_nombre_resp_adm varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_telefono_resp_adm varchar(20) NULL;
ALTER TABLE catalogo.sg_asociaciones ADD COLUMN aso_correo_resp_adm varchar(50) NULL;
	
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_anio_fundacion int2 NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_responsable_institucional varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_correo varchar(50) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_correo_alternativo varchar(50) NULL;

ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_nombre_coordiandor varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_telefono_coordiandor varchar(20) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_correo_coordiandor varchar(50) NULL;

ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_nombre_resp_adm varchar(100) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_telefono_resp_adm varchar(20) NULL;
ALTER TABLE catalogo.sg_asociaciones_aud ADD COLUMN aso_correo_resp_adm varchar(50) NULL;	
	
-- 3.27.2
ALTER TABLE catalogo.sg_sub_modalidades ADD COLUMN smo_integrada boolean default false;
ALTER TABLE catalogo.sg_sub_modalidades_aud ADD COLUMN smo_integrada boolean;	
	
-- 3.28.0
--PERMISOS PARA CREAR.ACTUALIZAR Y ELIMINAR IMPRESORAS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IMPRESORA','C494',  'Permiso para agregar impresoras', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IMPRESORA','C495',  'Permiso para actualizar impresoras', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IMPRESORA','C496',  'Permiso para eliminar impresoras', 1, true, null, null,0);


--IMPRESORAS
CREATE SEQUENCE catalogo.sg_impresoras_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_impresoras(
    imp_pk bigserial NOT NULL, 
    imp_codigo varchar(4) NULL,
    imp_habilitado bool NULL, 
    imp_nombre varchar(255) NULL, 
    imp_nombre_busqueda varchar(255) NULL,
    imp_descripcion varchar(255) NULL,
    imp_ult_mod_fecha timestamp NULL, 
    imp_ult_mod_usuario varchar(45) NULL, 
    imp_version int4 NULL,
    CONSTRAINT impresoras_pk PRIMARY KEY (imp_pk),
    CONSTRAINT impresoras_codigo_uk UNIQUE (imp_codigo),
    CONSTRAINT impresoras_nombre_uk UNIQUE (imp_nombre)
);
COMMENT ON TABLE catalogo.sg_impresoras IS 'Tabla para el registro de impresoras.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_impresoras.imp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_nombre IS 'Descripción del registro.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_nombre_busqueda IS 'Nombre de la odeia normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_impresoras.imp_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_impresoras ALTER imp_pk SET DEFAULT nextval('catalogo.sg_impresoras_pk_seq');

CREATE TABLE catalogo.sg_impresoras_aud (
    imp_pk bigserial NOT NULL, 
    imp_codigo varchar(4) NULL,
    imp_habilitado bool NULL, 
    imp_nombre varchar(255) NULL, 
    imp_nombre_busqueda varchar(255) NULL,
    imp_descripcion varchar(255) NULL,
    imp_ult_mod_fecha timestamp NULL, 
    imp_ult_mod_usuario varchar(45) NULL, 
    imp_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_impresoras_aud_pkey PRIMARY KEY (imp_pk, rev)
);



--PERMISOS PARA CREAR.ACTUALIZAR Y ELIMINAR ACCESOS A IPS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IP','C497',  'Permiso para agregar IP', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IP','C498',  'Permiso para actualizar IP', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IP','C499',  'Permiso para eliminar IP', 1, true, null, null,0);

--IPS DE ACCESO
CREATE SEQUENCE catalogo.sg_ips_acceso_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

CREATE TABLE catalogo.sg_ips_acceso(
    ipa_pk bigserial NOT NULL, 
    ipa_codigo varchar(4) NULL,
    ipa_habilitado bool NULL, 
    ipa_nombre varchar(255) NULL, 
    ipa_nombre_busqueda varchar(255) NULL,
    ipa_ip_acceso varchar(15) NULL,
    ipa_descripcion varchar(255) NULL,
    ipa_ult_mod_fecha timestamp NULL, 
    ipa_ult_mod_usuario varchar(45) NULL, 
    ipa_version int4 NULL,
    CONSTRAINT ip_acceso_pk PRIMARY KEY (ipa_pk),
    CONSTRAINT ip_acceso_codigo_uk UNIQUE (ipa_codigo),
    CONSTRAINT ip_acceso_nombre_uk UNIQUE (ipa_nombre)
);
COMMENT ON TABLE catalogo.sg_ips_acceso IS 'Tabla para el registro de ips de acceso.';

-- Column comments

COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_descripcion IS 'Descripción del registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_nombre_busqueda IS 'Nombre de la odeia normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_ip_acceso IS 'Ip de acceso del registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_ips_acceso.ipa_version IS 'Versión del registro';


ALTER TABLE catalogo.sg_ips_acceso ALTER ipa_pk SET DEFAULT nextval('catalogo.sg_ips_acceso_pk_seq');

CREATE TABLE catalogo.sg_ips_acceso_aud (
    ipa_pk bigserial NOT NULL, 
    ipa_codigo varchar(4) NULL,
    ipa_habilitado bool NULL, 
    ipa_nombre varchar(255) NULL, 
    ipa_nombre_busqueda varchar(255) NULL,
    ipa_ip_acceso varchar(15) NULL,
    ipa_descripcion varchar(255) NULL,
    ipa_ult_mod_fecha timestamp NULL, 
    ipa_ult_mod_usuario varchar(45) NULL, 
    ipa_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_iparesoras_aud_pkey PRIMARY KEY (ipa_pk, rev)
);
	
	
	
--3.28.1
ALTER TABLE catalogo.sg_cargo_titular ADD COLUMN cti_es_titular boolean ;
ALTER TABLE catalogo.sg_cargo_titular_aud ADD COLUMN cti_es_titular boolean;	

--3.28.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CATALOGOS_TITULOS','CP7',  'Renderiza el menú de títulos', 2, true, null, null,0);

CREATE TABLE IF NOT EXISTS catalogo.sg_rel_formula_formula (fom_pk bigint NOT NULL,sub_fom_pk bigint NOT NULL, CONSTRAINT sg_rel_formula_fk FOREIGN KEY (fom_pk) REFERENCES catalogo.sg_formulas (fom_pk), CONSTRAINT sg_rel_sub_formula_fk FOREIGN KEY (sub_fom_pk) REFERENCES catalogo.sg_formulas (fom_pk));
CREATE TABLE IF NOT EXISTS catalogo.sg_rel_formula_formula_aud(fom_pk bigint NOT NULL,sub_fom_pk bigint NOT NULL, rev bigint, revtype smallint);

ALTER TABLE catalogo.sg_formulas ADD COLUMN fom_tiene_subformula boolean default false;
ALTER TABLE catalogo.sg_formulas_aud ADD COLUMN fom_tiene_subformula boolean;

--3.29.0
INSERT INTO catalogo.sg_configuraciones("con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version") VALUES ('PROCESAR_PROMOCIONES_ASINC', 'Procesar promociones asincrónicas (valor=1 asincrónica,valor=0 sincrónica)', '1', 'Procesar promociones asincrónicas (valor=1 asincrónica,valor=0 sincrónica)', CURRENT_TIMESTAMP, 'admin', '0');

-- Preguntas de cierre de año
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_pregunta_cierre_anio_pca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_pregunta_cierre_anio (pca_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_pregunta_cierre_anio_pca_pk_seq'::regclass), pca_codigo character varying(45), pca_habilitado boolean, pca_nombre character varying(255), pca_nombre_busqueda character varying(255), pca_ult_mod_fecha timestamp without time zone, pca_ult_mod_usuario character varying(45), pca_version integer, CONSTRAINT sg_pregunta_cierre_anio_pkey PRIMARY KEY (pca_pk), CONSTRAINT pca_codigo_uk UNIQUE (pca_codigo), CONSTRAINT pca_nombre_uk UNIQUE (pca_nombre));
COMMENT ON TABLE catalogo.sg_pregunta_cierre_anio IS 'Tabla para el registro de preguntas de cierre de año.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_pregunta_cierre_anio.pca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_pregunta_cierre_anio_aud (pca_pk bigint NOT NULL, pca_codigo character varying(45), pca_habilitado boolean, pca_nombre character varying(255), pca_nombre_busqueda character varying(255), pca_ult_mod_fecha timestamp without time zone, pca_ult_mod_usuario character varying(45), pca_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_pregunta_cierre_anio_aud ADD PRIMARY KEY (pca_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PREGUNTA_CIERRE_ANIO','C500',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PREGUNTA_CIERRE_ANIO','C501',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PREGUNTA_CIERRE_ANIO','C502',  '', 1, true, null, null,0);


ALTER TABLE catalogo.sg_pregunta_cierre_anio add column pca_pregunta text;
ALTER TABLE catalogo.sg_pregunta_cierre_anio_aud add column pca_pregunta text;


--3.29.1

-- Catálogo de bancos
create sequence if not exists catalogo.sg_bancos_bnc_pk_seq increment 1 start 1 minvalue 1 maxvalue 2147483647 cache 1;
create table if not exists catalogo.sg_bancos (
bnc_pk bigint not null default nextval('catalogo.sg_bancos_bnc_pk_seq'::regclass),
bnc_codigo character varying(45),
bnc_nombre character varying(250),
bnc_codigo_busqueda character varying(45),
bnc_nombre_busqueda character varying(250),
bnc_habilitado boolean,
bnc_ult_mod_fecha timestamp without time zone,
bnc_ult_mod_usuario character varying(45),
bnc_version integer,
constraint sg_bancos_pkey primary key (bnc_pk)
);

COMMENT ON TABLE catalogo.sg_bancos IS 'Tabla para el registro de movimiento cuenta bancaria.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_codigo IS 'Código del banco.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_nombre IS 'Nombre del banco.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_codigo IS 'Código del banco para realizar búsquedas.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_nombre IS 'Nombre del banco para realizar búsquedas.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_habilitado IS 'Indica si el registro se ecuentra habilitado.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_version IS 'Versión del registro.';

create table if not exists catalogo.sg_bancos_aud (
bnc_pk bigint not null,
bnc_codigo character varying(45),
bnc_nombre character varying(250),
bnc_codigo_busqueda character varying(45),
bnc_nombre_busqueda character varying(250),
bnc_habilitado boolean,
bnc_ult_mod_fecha timestamp without time zone,
bnc_ult_mod_usuario character varying(45),
bnc_version integer,
rev bigint,
revtype smallint);

ALTER TABLE catalogo.sg_bancos_aud ADD PRIMARY KEY (bnc_pk, rev);

-- 3.29.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CATALOGO_BANCOS','C503',  'Permiso para crear un banco', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_CATALOGO_BANCOS','C504',  'Permiso para editar un banco', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CATALOGO_BANCOS','C505',  'Permiso para eliminar un banco', 1, true, null, null,0);

-- Tipos de Cuentas Bancarias
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tipo_cuenta_bancaria_tcb_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_cuenta_bancaria (tcb_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tipo_cuenta_bancaria_tcb_pk_seq'::regclass), tcb_codigo character varying(45), tcb_habilitado boolean, tcb_nombre character varying(255), tcb_nombre_busqueda character varying(255), tcb_ult_mod_fecha timestamp without time zone, tcb_ult_mod_usuario character varying(45), tcb_version integer, CONSTRAINT sg_tipo_cuenta_bancaria_pkey PRIMARY KEY (tcb_pk), CONSTRAINT tcb_codigo_uk UNIQUE (tcb_codigo), CONSTRAINT tcb_nombre_uk UNIQUE (tcb_nombre));
COMMENT ON TABLE catalogo.sg_tipo_cuenta_bancaria IS 'Tabla para el registro de tipos de cuentas bancarias.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tipo_cuenta_bancaria.tcb_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tipo_cuenta_bancaria_aud (tcb_pk bigint NOT NULL, tcb_codigo character varying(45), tcb_habilitado boolean, tcb_nombre character varying(255), tcb_nombre_busqueda character varying(255), tcb_ult_mod_fecha timestamp without time zone, tcb_ult_mod_usuario character varying(45), tcb_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_tipo_cuenta_bancaria_aud ADD PRIMARY KEY (tcb_pk, rev);

alter table catalogo.sg_bancos add CONSTRAINT bnc_codigo_uk UNIQUE (bnc_codigo);
alter table catalogo.sg_bancos add CONSTRAINT bnc_nombre_uk UNIQUE (bnc_nombre);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_CUENTAS_BANCARIAS','C506',  'Permiso para crear tipo cuenta bancaria', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_CUENTAS_BANCARIAS','C507',  'Permiso para editar tipo cuenta bancaria', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_CUENTAS_BANCARIAS','C508',  'Permiso para eliminar tipo cuenta bancaria', 1, true, null, null,0);

--3.29.3
--Se agregan campos de teléfono e email para los bancos
alter table catalogo.sg_bancos
add bnc_telefono varchar(20),
add bnc_correo_electronico varchar(50);

COMMENT ON COLUMN catalogo.sg_bancos.bnc_telefono IS 'Número de teléfono del banco.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_correo_electronico IS 'Correo electrónico del banco.';

alter table catalogo.sg_bancos_aud
add bnc_telefono varchar(20),
add bnc_correo_electronico varchar(50);

--3.30.0
-- Títulos anteriores a 2008
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_titulo_anterior_tan_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_titulo_anterior (tan_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_titulo_anterior_tan_pk_seq'::regclass), tan_codigo character varying(45), tan_habilitado boolean, tan_nombre character varying(255), tan_nombre_busqueda character varying(255), tan_ult_mod_fecha timestamp without time zone, tan_ult_mod_usuario character varying(45), tan_version integer, CONSTRAINT sg_titulo_anterior_pkey PRIMARY KEY (tan_pk), CONSTRAINT tan_codigo_uk UNIQUE (tan_codigo), CONSTRAINT tan_nombre_uk UNIQUE (tan_nombre));
COMMENT ON TABLE catalogo.sg_titulo_anterior IS 'Tabla para el registro de títulos anteriores a 2008.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_titulo_anterior.tan_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_titulo_anterior_aud (tan_pk bigint NOT NULL, tan_codigo character varying(45), tan_habilitado boolean, tan_nombre character varying(255), tan_nombre_busqueda character varying(255), tan_ult_mod_fecha timestamp without time zone, tan_ult_mod_usuario character varying(45), tan_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_titulo_anterior_aud ADD PRIMARY KEY (tan_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TITULO_ANTERIOR','C509',  'Permiso para crear título anterior', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TITULO_ANTERIOR','C510',  'Permiso para actualizar título anterior', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TITULO_ANTERIOR','C511',  'Permiso para eliminar título anterior', 1, true, null, null,0);

ALTER TABLE catalogo.sg_definiciones_titulo ADD COLUMN dti_es_tipo_reposicion boolean default false;
ALTER TABLE catalogo.sg_definiciones_titulo_aud ADD COLUMN dti_es_tipo_reposicion boolean;


-- 3.31.0

ALTER TABLE catalogo.sg_cargo_oae ADD COLUMN coa_nombre_masculino character varying(255);
ALTER TABLE catalogo.sg_cargo_oae_aud ADD COLUMN coa_nombre_masculino character varying(255);

ALTER TABLE catalogo.sg_cargo_oae ADD COLUMN coa_nombre_busqueda_masculino character varying(255);
ALTER TABLE catalogo.sg_cargo_oae_aud ADD COLUMN coa_nombre_busqueda_masculino character varying(255);

--COMENTARIOS DE TABLAS Y COLUMNAS FALTANTES

--sg_bancos
COMMENT ON TABLE catalogo.sg_bancos IS 'Tabla para el registro de bancos.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_codigo IS 'Código del banco.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_nombre IS 'Nombre del banco.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_codigo_busqueda IS 'Código del banco para realizar búsquedas.';
COMMENT ON COLUMN catalogo.sg_bancos.bnc_nombre_busqueda IS 'Nombre del banco para realizar búsquedas.';

--sg_bancos_aud
COMMENT ON TABLE catalogo.sg_bancos_aud IS 'Tabla de auditoría de la tabla catalogo.sg_bancos, el detalle de los campos se encuentra en la tabla principal.';

--sg_tipo_cuenta_bancaria_aud
COMMENT ON TABLE catalogo.sg_tipo_cuenta_bancaria_aud IS 'Tabla de auditoría de la tabla catalogo.sg_tipo_cuenta_bancaria, el detalle de los campos se encuentra en la tabla principal.';

ALTER TABLE catalogo.sg_tipos_apoyo        ADD COLUMN tap_aplica_sede boolean default true;
ALTER TABLE catalogo.sg_tipos_apoyo_aud    ADD COLUMN tap_aplica_sede boolean;
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_aplica_sede IS 'Registro aplica a sede.';

ALTER TABLE catalogo.sg_tipos_apoyo        ADD COLUMN tap_aplica_estudiante boolean default false;
ALTER TABLE catalogo.sg_tipos_apoyo_aud    ADD COLUMN tap_aplica_estudiante boolean;
COMMENT ON COLUMN catalogo.sg_tipos_apoyo.tap_aplica_estudiante IS 'Registro aplica a estudiante.';

-- 3.32.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CONFIGURAR_FIRMAS_ELECTRONICAS','C512',  'Permiso para configurar firmas electrónicas de SIGES', 2, true, null, null,0);

CREATE SEQUENCE IF NOT EXISTS catalogo.sg_configuraciones_firma_electronica_con_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_configuraciones_firma_electronica (con_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_configuraciones_firma_electronica_con_pk_seq'::regclass), con_codigo character varying(45), con_activada boolean, con_nombre character varying(255), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer, CONSTRAINT sg_configuraciones_firma_pkey PRIMARY KEY (con_pk), CONSTRAINT con_codigo_firma_uk UNIQUE (con_codigo), CONSTRAINT con_nombre_firma_uk UNIQUE (con_nombre));
COMMENT ON TABLE catalogo.sg_configuraciones_firma_electronica IS 'Tabla para el registro de configuraciones de firma electrónica.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_activada IS 'Indica si la firma electrónica se encuentra habilitada(true) o inhabilitada(false).';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_configuraciones_firma_electronica.con_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_configuraciones_firma_electronica_aud (con_pk bigint NOT NULL, con_codigo character varying(45), con_activada boolean, con_nombre character varying(255), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer, rev bigint, revtype smallint);

INSERT INTO catalogo.sg_configuraciones_firma_electronica(con_codigo, con_activada, con_nombre, con_version) values ('CONF_MAT', false, 'Confirmación de matrícula', 0);

update seguridad.sg_operaciones set ope_categoria_operacion_fk = 2 where ope_codigo like 'C50%';
update seguridad.sg_operaciones set ope_categoria_operacion_fk = 2 where ope_codigo like 'C51%';

-- 3.33.0

--Trastorno aprendizaje
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_trastornos_aprendizaje_tra_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    catalogo.sg_trastornos_aprendizaje(tra_pk bigint NOT NULL DEFAULT NEXTVAL('catalogo.sg_trastornos_aprendizaje_tra_pk_seq'::regclass),tra_codigo CHARACTER varying(4), tra_nombre CHARACTER varying(255), tra_nombre_busqueda CHARACTER VARYING(255), tra_habilitado BOOLEAN, tra_ult_mod_fecha timestamp without TIME zone, tra_ult_mod_usuario CHARACTER varying(45), tra_version INTEGER, CONSTRAINT sg_trastornos_aprendizaje_pkey PRIMARY KEY (tra_pk),CONSTRAINT tra__apr_codigo_uk UNIQUE (tra_codigo));
COMMENT ON TABLE  catalogo.sg_trastornos_aprendizaje IS 'Tabla para el registro de los trastornos de aprendizaje.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_trastornos_aprendizaje.tra_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_trastornos_aprendizaje_aud(tra_pk bigint NOT NULL,tra_codigo CHARACTER varying(4),tra_nombre CHARACTER varying(255),tra_nombre_busqueda CHARACTER VARYING(255),tra_habilitado BOOLEAN,tra_ult_mod_fecha timestamp without TIME zone,tra_ult_mod_usuario CHARACTER varying(45),tra_version INTEGER,rev bigint, revtype smallint);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TRASTORNO_APRENDIZAJE','C513',  'Permiso para crear trastorno de aprendizaje', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TRASTORNO_APRENDIZAJE','C514',  'Permiso para actualizar trastorno de aprendizaje', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TRASTORNO_APRENDIZAJE','C515',  'Permiso para eliminar trastorno de aprendizaje', 2, true, null, null,0);

--3.33.1


-- Perfiles de Usuarios Ingresados por el CE
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_perfiles_usuarios_ingresados_ce_pui_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists catalogo.sg_perfiles_usuarios_ingresados_ce 
(pui_pk bigint not null default nextval('catalogo.sg_perfiles_usuarios_ingresados_ce_pui_pk_seq'::regclass),
pui_codigo character varying(45),
pui_habilitado boolean,
pui_nombre character varying(255),
pui_nombre_busqueda character varying(255),
pui_ult_mod_fecha timestamp without time zone,
pui_ult_mod_usuario character varying(45),
pui_version integer,
constraint sg_perfiles_usuarios_ingresados_ce_pkey primary key (pui_pk),
constraint pui_codigo_uk unique (pui_codigo),
constraint pui_nombre_uk unique (pui_nombre)
);

COMMENT ON TABLE catalogo.sg_perfiles_usuarios_ingresados_ce IS 'Tabla para el registro de perfiles de usuarios ingresados por el ce.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_perfiles_usuarios_ingresados_ce.pui_version IS 'Versión del registro.';

create table if not exists catalogo.sg_perfiles_usuarios_ingresados_ce_aud (pui_pk bigint not null,
pui_codigo character varying(45),
pui_habilitado boolean,
pui_nombre character varying(255),
pui_nombre_busqueda character varying(255),
pui_ult_mod_fecha timestamp without time zone,
pui_ult_mod_usuario character varying(45),
pui_version integer,
rev bigint,
revtype smallint);
ALTER TABLE catalogo.sg_perfiles_usuarios_ingresados_ce_aud ADD PRIMARY KEY (pui_pk, rev);

---------------------------------------------------------------------------------
--Almacena la relación con roles
CREATE SEQUENCE catalogo.sg_perfiles_roles_pk_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 10 CACHE 1 NO CYCLE;

CREATE TABLE IF NOT EXISTS catalogo.sg_perfiles_roles(
	uir_pk  bigint not null default nextval('catalogo.sg_perfiles_roles_pk_seq'::regclass),
	uir_perfil_fk int8 NOT NULL, 
	uir_rol_fk int8 NOT NULL,
	uir_ult_mod_usuario varchar(45) NULL, 
	uir_ult_mod_fecha timestamp NULL,
	uir_version int4 NULL,
	CONSTRAINT sg_perfiles_roles_pk PRIMARY KEY (uir_pk),
	CONSTRAINT sg_perfiles_roles_sg_fk FOREIGN KEY (uir_perfil_fk) REFERENCES catalogo.sg_perfiles_usuarios_ingresados_ce(pui_pk),
	CONSTRAINT sg_perfiles_roles_sg_rol_fk FOREIGN KEY (uir_rol_fk) REFERENCES seguridad.sg_roles(rol_pk)
);

-- Column comments

COMMENT ON COLUMN catalogo.sg_perfiles_roles.uir_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_perfiles_roles.uir_perfil_fk IS 'Clave foránea del perfil.';
COMMENT ON COLUMN catalogo.sg_perfiles_roles.uir_rol_fk IS 'Clave Foranea de Rol.';
COMMENT ON COLUMN catalogo.sg_perfiles_roles.uir_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_perfiles_roles.uir_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';

CREATE TABLE IF NOT EXISTS catalogo.sg_perfiles_roles_aud (
    uir_pk bigserial NOT NULL,
	uir_perfil_fk int8 NOT NULL, 
	uir_rol_fk int8 NOT NULL, 
	uir_ult_mod_usuario varchar(45) NULL, 
	uir_ult_mod_fecha timestamp NULL,
	uir_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_perfiles_roles_aud_pkey PRIMARY KEY (uir_pk, rev)
);

ALTER TABLE catalogo.sg_perfiles_roles_aud ADD CONSTRAINT perfiles_roles_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

--Operaciones
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PERFILES_USUARIOS_INGRESADOS_CE','C516',  'Permiso para crear un perfil de usuario ingresado', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PERFILES_USUARIOS_INGRESADOS_CE','C517',  'Permiso para actualizar un perfil de usuario ingresado ', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PERFILES_USUARIOS_INGRESADOS_CE','C518',  'Permiso para eliminar un perfil de usuario ingresado ', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PERFILES_USUARIOS_INGRESADOS_CE','C519',  'Permiso para obtener perfiles de usuarios ingresados', 2, true, null, null,0);


-- 3.34.0

ALTER TABLE catalogo.sg_tipo_representante ADD COLUMN tre_cargo_fk bigint;
ALTER TABLE catalogo.sg_tipo_representante_aud ADD COLUMN tre_cargo_fk bigint;

COMMENT ON COLUMN catalogo.sg_tipo_representante.tre_cargo_fk IS 'Cargo asociado.';

ALTER TABLE catalogo.sg_tipo_representante  ADD CONSTRAINT sg_tipo_rep_cargo_fk FOREIGN KEY (tre_cargo_fk) REFERENCES catalogo.sg_cargo(car_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

UPDATE catalogo.sg_tipo_representante set tre_cargo_fk = 12 where tre_pk = 1; --Director
UPDATE catalogo.sg_tipo_representante set tre_cargo_fk = 44 where tre_pk = 2; --registro acad

--Se agrega el campo nombre búsqueda al catálogo
alter table catalogo.sg_proceso_nocturno add prn_nombre_busqueda varchar(255);
alter table catalogo.sg_proceso_nocturno_aud add prn_nombre_busqueda varchar(255);

--Se actualizan los nombres en el nuevo campo
update catalogo.sg_proceso_nocturno set prn_nombre_busqueda = lower(prn_nombre);
update catalogo.sg_proceso_nocturno_aud set prn_nombre_busqueda = lower(prn_nombre);

--Se cran indices para permitir eliminar registros de catálogos
--Motivo de inasistencia
CREATE INDEX sg_motivo_inasistencia_fk_idx ON 
centros_educativos.sg_asistencias USING btree (asi_motivo_inasistencia)

--Jornada laboral
CREATE INDEX sg_sede_jornada_laboral_fk_idx ON 
centros_educativos.sg_sedes_jornadas USING btree (jla_pk);

CREATE INDEX sg_contratacion_jornada_laboral_fk_idx ON 
centros_educativos.sg_datos_contratacion USING btree (dco_jornada_fk);

CREATE INDEX sg_seccion_jornada_laboral_fk_idx ON 
centros_educativos.sg_secciones USING btree (sec_jornada_fk);

CREATE INDEX sg_dato_contratacion_jornada_laboral_fk_idx ON 
centros_educativos.sg_dato_contratacion_jornada_laboral USING btree (jla_pk);

--Nacionalidad
CREATE INDEX sg_persona_nacionalidad_fk_idx ON 
centros_educativos.sg_personas USING btree (per_nacionalidad_fk);

--Tipo de trabajo
CREATE INDEX sg_persona_tipo_trabajo_fk_idx ON 
centros_educativos.sg_personas USING btree (per_tipo_trabajo_fk);

--Motivos de retiro
CREATE INDEX sg_matricula_motivo_retiro_fk_idx ON 
centros_educativos.sg_matriculas USING btree (mat_motivo_retiro_fk);

--Ocupación
CREATE INDEX sg_estudiantes_ocupacion_fk_idx ON 
centros_educativos.sg_estudiantes USING btree (est_ocupacion_fk);

CREATE INDEX sg_personas_ocupacion_fk_idx ON 
centros_educativos.sg_personas USING btree (per_ocupacion_fk);

--Profesión
CREATE INDEX sg_personas_profesion_fk_idx ON 
centros_educativos.sg_personas USING btree (per_profesion_fk);

--Motivos fallecimiento
CREATE INDEX sg_personas_motivo_fallecimiento_fk_idx ON 
centros_educativos.sg_personas USING btree (per_motivo_fallecimiento_fk);


--Responsable y cargo de unidad de activo fijo
ALTER TABLE catalogo.sg_af_unidades_activo_fijo ADD uaf_responsable_af varchar(100) NULL;
ALTER TABLE catalogo.sg_af_unidades_activo_fijo ADD uaf_cargo_responsable_af varchar(100) NULL;

ALTER TABLE catalogo.sg_af_unidades_activo_fijo_aud ADD uaf_responsable_af varchar(100) NULL;
ALTER TABLE catalogo.sg_af_unidades_activo_fijo_aud ADD uaf_cargo_responsable_af varchar(100) NULL;

-- 3.35.0

ALTER TABLE catalogo.sg_plantilla ADD COLUMN pla_organizacion_curricular_fk integer;
ALTER TABLE catalogo.sg_plantilla_aud ADD COLUMN pla_organizacion_curricular_fk integer;

ALTER TABLE catalogo.sg_plantilla ADD COLUMN pla_habilitada_reemplazo_org boolean default false;
ALTER TABLE catalogo.sg_plantilla_aud ADD COLUMN pla_habilitada_reemplazo_org boolean;

ALTER TABLE catalogo.sg_plantilla DROP CONSTRAINT pla_codigo_uk;
ALTER TABLE catalogo.sg_plantilla DROP CONSTRAINT pla_nombre_uk;

CREATE UNIQUE INDEX sg_plantilla_cod_uk ON catalogo.sg_plantilla (pla_codigo)
where pla_organizacion_curricular_fk is null;

CREATE UNIQUE INDEX sg_plantilla_cod_org_uk ON catalogo.sg_plantilla (pla_codigo, pla_organizacion_curricular_fk);


CREATE UNIQUE INDEX sg_plantilla_nom_uk ON catalogo.sg_plantilla (pla_nombre)
where pla_organizacion_curricular_fk is null;

CREATE UNIQUE INDEX sg_plantilla_nom_org_uk ON catalogo.sg_plantilla (pla_nombre, pla_organizacion_curricular_fk);


UPDATE catalogo.sg_plantilla SET pla_habilitada_reemplazo_org = true where pla_pk IN (3, 10, 2, 9, 44, 4);

--3.36.0
-- Referencias de apoyo
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_referencias_apoyo_rea_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_referencias_apoyo (rea_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_referencias_apoyo_rea_pk_seq'::regclass), rea_codigo character varying(45), rea_habilitado boolean, rea_nombre character varying(255), rea_nombre_busqueda character varying(255), rea_ult_mod_fecha timestamp without time zone, rea_ult_mod_usuario character varying(45), rea_version integer, CONSTRAINT sg_referencias_apoyo_pkey PRIMARY KEY (rea_pk), CONSTRAINT rea_codigo_uk UNIQUE (rea_codigo), CONSTRAINT rea_nombre_uk UNIQUE (rea_nombre));
COMMENT ON TABLE catalogo.sg_referencias_apoyo IS 'Tabla para el registro de referencias de apoyo.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_referencias_apoyo.rea_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_referencias_apoyo_aud (rea_pk bigint NOT NULL, rea_codigo character varying(45), rea_habilitado boolean, rea_nombre character varying(255), rea_nombre_busqueda character varying(255), rea_ult_mod_fecha timestamp without time zone, rea_ult_mod_usuario character varying(45), rea_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_referencias_apoyo_aud ADD PRIMARY KEY (rea_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REFERENCIAS_DE_APOYO','C520',  'Permiso para crear referencias de apoyo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REFERENCIAS_DE_APOYO','C521',  'Permiso para actualizar referencias de apoyo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REFERENCIAS_DE_APOYO','C522',  'Permiso para eliminar referencias de apoyo', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REFERENCIAS_DE_APOYO','C523',  'Permiso para buscar referencias de apoyo', 2, true, null, null,0);

--Valores iniciales
INSERT INTO catalogo.sg_referencias_apoyo
(rea_codigo, rea_habilitado, rea_nombre, rea_nombre_busqueda, rea_ult_mod_fecha, rea_ult_mod_usuario, rea_version)
VALUES('001', true, 'Docente de apoyo a la inclusión', 'docente de apoyo a la inclusion', current_timestamp, null, 0);

INSERT INTO catalogo.sg_referencias_apoyo
(rea_codigo, rea_habilitado, rea_nombre, rea_nombre_busqueda, rea_ult_mod_fecha, rea_ult_mod_usuario, rea_version)
VALUES('002', true, 'Centro de Orientación y Recursos (COR)', 'centro de orientación y recursos (cor)', current_timestamp, null, 0);

INSERT INTO catalogo.sg_referencias_apoyo_aud
(rea_pk, rea_codigo, rea_habilitado, rea_nombre, rea_nombre_busqueda, rea_ult_mod_fecha, rea_ult_mod_usuario, rea_version, rev, revtype)
VALUES(1,'001', true, 'Docente de apoyo a la inclusión', 'docente de apoyo a la inclusion', current_timestamp, null, 0, 1, 0);

INSERT INTO catalogo.sg_referencias_apoyo_aud
(rea_pk, rea_codigo, rea_habilitado, rea_nombre, rea_nombre_busqueda, rea_ult_mod_fecha, rea_ult_mod_usuario, rea_version, rev, revtype)
VALUES(2,'002', true, 'Centro de Orientación y Recursos (COR)', 'centro de orientación y recursos (cor)', current_timestamp, null, 0, 1, 0);


-- Terapias
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_terapias_ter_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_terapias (ter_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_terapias_ter_pk_seq'::regclass), ter_codigo character varying(45), ter_habilitado boolean, ter_nombre character varying(255), ter_nombre_busqueda character varying(255), ter_ult_mod_fecha timestamp without time zone, ter_ult_mod_usuario character varying(45), ter_version integer, CONSTRAINT sg_terapias_pkey PRIMARY KEY (ter_pk), CONSTRAINT ter_codigo_uk UNIQUE (ter_codigo), CONSTRAINT ter_nombre_uk UNIQUE (ter_nombre));
COMMENT ON TABLE catalogo.sg_terapias IS 'Tabla para el registro de terapias.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_terapias.ter_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_terapias.ter_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_terapias_aud (ter_pk bigint NOT NULL, ter_codigo character varying(45), ter_habilitado boolean, ter_nombre character varying(255), ter_nombre_busqueda character varying(255), ter_ult_mod_fecha timestamp without time zone, ter_ult_mod_usuario character varying(45), ter_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_terapias_aud ADD PRIMARY KEY (ter_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TERAPIAS','C524',  'Permiso para crear terapias', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TERAPIAS','C525',  'Permiso para actualizar terapias', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TERAPIAS','C526',  'Permiso para eliminar terapias', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TERAPIAS','C527',  'Permiso para buscar terapias', 2, true, null, null,0);

--Valores iniciales
INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('001', true, 'Terapia de lenguaje', 'terapia de lenguaje', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('002', true, 'Terapia de audición y lenguaje', 'terapia de audicion y lenguaje', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('003', true, 'Terapia de rehabilitación', 'terapia de rehabilitacion', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('004', true, 'Fisioterapia', 'fisioterapia', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('005', true, 'Atención psicológica', 'atencion psicologica', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('006', true, 'Atención psiquiátrica', 'atencion psiquiatrica', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('007', true, 'Atención neurológica', 'atencion neurologica', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias
(ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version)
VALUES('008', true, 'Otro', 'otro', current_timestamp, null, 0);

INSERT INTO catalogo.sg_terapias_aud
(ter_pk, ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version, rev, revtype)
select ter_pk, ter_codigo, ter_habilitado, ter_nombre, ter_nombre_busqueda, ter_ult_mod_fecha, ter_ult_mod_usuario, ter_version, 1, 0 from catalogo.sg_terapias 

-- Motivos de selección de plaza
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_seleccion_plaza_msp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_seleccion_plaza (msp_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivos_seleccion_plaza_msp_pk_seq'::regclass), msp_codigo character varying(45), msp_habilitado boolean, msp_nombre character varying(255), msp_nombre_busqueda character varying(255), msp_ult_mod_fecha timestamp without time zone, msp_ult_mod_usuario character varying(45), msp_version integer, CONSTRAINT sg_motivos_seleccion_plaza_pkey PRIMARY KEY (msp_pk), CONSTRAINT msp_codigo_uk UNIQUE (msp_codigo), CONSTRAINT msp_nombre_uk UNIQUE (msp_nombre));
COMMENT ON TABLE catalogo.sg_motivos_seleccion_plaza IS 'Tabla para el registro de motivos de selección de plaza.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_seleccion_plaza_aud (msp_pk bigint NOT NULL, msp_codigo character varying(45), msp_habilitado boolean, msp_nombre character varying(255), msp_nombre_busqueda character varying(255), msp_ult_mod_fecha timestamp without time zone, msp_ult_mod_usuario character varying(45), msp_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_motivos_seleccion_plaza_aud ADD PRIMARY KEY (msp_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_SELECCION_PLAZA','C528',  'Permiso para crear motivos de selección de plaza', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_SELECCION_PLAZA','C529',  'Permiso para actualizar motivos de selección de plaza', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_SELECCION_PLAZA','C530',  'Permiso para eliminar motivos de selección de plaza', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOTIVOS_SELECCION_PLAZA','C531',  'Permiso para buscar motivos de selección de plaza', 2, true, null, null,0);

ALTER TABLE catalogo.sg_motivos_seleccion_plaza ADD COLUMN msp_orden integer;
ALTER TABLE catalogo.sg_motivos_seleccion_plaza_aud ADD COLUMN msp_orden integer;
COMMENT ON COLUMN catalogo.sg_motivos_seleccion_plaza.msp_orden IS 'Registra el orden en que se presenta el catálogo.';

--3.36.1
-- Calidades de ingreso de aplicantes a plaza
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_calidades_ingreso_aplicantes_cia_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_calidades_ingreso_aplicantes (cia_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_calidades_ingreso_aplicantes_cia_pk_seq'::regclass), cia_codigo character varying(45), cia_habilitado boolean, cia_nombre character varying(255), cia_nombre_busqueda character varying(255), cia_ult_mod_fecha timestamp without time zone, cia_ult_mod_usuario character varying(45), cia_version integer, CONSTRAINT sg_calidades_ingreso_aplicantes_pkey PRIMARY KEY (cia_pk), CONSTRAINT cia_codigo_uk UNIQUE (cia_codigo), CONSTRAINT cia_nombre_uk UNIQUE (cia_nombre));
COMMENT ON TABLE catalogo.sg_calidades_ingreso_aplicantes IS 'Tabla para el registro de calidades de ingreso de aplicantes a plaza.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_calidades_ingreso_aplicantes.cia_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_calidades_ingreso_aplicantes_aud (cia_pk bigint NOT NULL, cia_codigo character varying(45), cia_habilitado boolean, cia_nombre character varying(255), cia_nombre_busqueda character varying(255), cia_ult_mod_fecha timestamp without time zone, cia_ult_mod_usuario character varying(45), cia_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_calidades_ingreso_aplicantes_aud ADD PRIMARY KEY (cia_pk, rev);

--Operaciones para el nuevo catálogo
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIDADES_INGRESO_APLICANTES','C532',  'Permiso para crear calidades de ingreso de aplicantes a plazas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIDADES_INGRESO_APLICANTES','C533',  'Permiso para actualizar calidades de ingreso de aplicantes a plazas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIDADES_INGRESO_APLICANTES','C534',  'Permiso para eliminar calidades de ingreso de aplicantes a plazas', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIDADES_INGRESO_APLICANTES','C535',  'Permiso para buscar calidades de ingreso de aplicantes a plazas', 2, true, null, null,0);

--Se agrega una relación en la tabla de aplicaciones de plaza
alter table centros_educativos.sg_aplicaciones_plaza add apl_cia_fk bigint;
alter table centros_educativos.sg_aplicaciones_plaza_aud add apl_cia_fk bigint;
alter table centros_educativos.sg_aplicaciones_plaza add constraint sg_aplicaciones_plaza_cia_fk 
foreign key (apl_cia_fk) references catalogo.sg_calidades_ingreso_aplicantes (cia_pk);

INSERT INTO catalogo.sg_calidades_ingreso_aplicantes
(cia_codigo, cia_habilitado, cia_nombre, cia_nombre_busqueda, cia_ult_mod_fecha, cia_ult_mod_usuario, cia_version)
VALUES('001', true, 'Traslado', 'traslado', current_timestamp, NULL, 0);

INSERT INTO catalogo.sg_calidades_ingreso_aplicantes
(cia_codigo, cia_habilitado, cia_nombre, cia_nombre_busqueda, cia_ult_mod_fecha, cia_ult_mod_usuario, cia_version)
VALUES('002', true, 'Reingreso', 'reingreso', current_timestamp , NULL, 0);

INSERT INTO catalogo.sg_calidades_ingreso_aplicantes
(cia_codigo, cia_habilitado, cia_nombre, cia_nombre_busqueda, cia_ult_mod_fecha, cia_ult_mod_usuario, cia_version)
VALUES('003', true, 'Ingreso', 'ingreso', current_timestamp, NULL, 0);


--3.37.0 
-- Identificación OAE
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_identificacion_oae_ioa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_identificacion_oae (ioa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_identificacion_oae_ioa_pk_seq'::regclass), ioa_codigo character varying(45), ioa_habilitado boolean,ioa_es_obligatorio boolean, ioa_nombre character varying(255), ioa_nombre_busqueda character varying(255), ioa_ult_mod_fecha timestamp without time zone, ioa_ult_mod_usuario character varying(45), ioa_version integer, CONSTRAINT sg_identificacion_oae_pkey PRIMARY KEY (ioa_pk), CONSTRAINT ioa_codigo_uk UNIQUE (ioa_codigo), CONSTRAINT ioa_nombre_uk UNIQUE (ioa_nombre));
COMMENT ON TABLE catalogo.sg_identificacion_oae IS 'Tabla para el registro de identificación oae.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_identificacion_oae.ioa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_identificacion_oae_aud (ioa_pk bigint NOT NULL, ioa_codigo character varying(45), ioa_habilitado boolean,ioa_es_obligatorio boolean, ioa_nombre character varying(255), ioa_nombre_busqueda character varying(255), ioa_ult_mod_fecha timestamp without time zone, ioa_ult_mod_usuario character varying(45), ioa_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_identificacion_oae_aud ADD PRIMARY KEY (ioa_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IDENTIFICACION_OAE','C536',  'Permite crear identificacionOAE', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IDENTIFICACION_OAE','C537',  'Actualizar identificacion OAE', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IDENTIFICACION_OAE','C538',  'Eliminar identificacion OAE', 2, true, null, null,0);


--3.37.1 
-- Tiempos de comida al día
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_tiempos_comida_dia_tcd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_tiempos_comida_dia (tcd_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_tiempos_comida_dia_tcd_pk_seq'::regclass), tcd_codigo character varying(45), tcd_habilitado boolean, tcd_nombre character varying(255), tcd_nombre_busqueda character varying(255), tcd_ult_mod_fecha timestamp without time zone, tcd_ult_mod_usuario character varying(45), tcd_version integer, CONSTRAINT sg_tiempos_comida_dia_pkey PRIMARY KEY (tcd_pk), CONSTRAINT tcd_codigo_uk UNIQUE (tcd_codigo), CONSTRAINT tcd_nombre_uk UNIQUE (tcd_nombre));
COMMENT ON TABLE catalogo.sg_tiempos_comida_dia IS 'Tabla para el registro de tiempos de comida al día.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_tiempos_comida_dia.tcd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_tiempos_comida_dia_aud (tcd_pk bigint NOT NULL, tcd_codigo character varying(45), tcd_habilitado boolean, tcd_nombre character varying(255), tcd_nombre_busqueda character varying(255), tcd_ult_mod_fecha timestamp without time zone, tcd_ult_mod_usuario character varying(45), tcd_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_tiempos_comida_dia_aud ADD PRIMARY KEY (tcd_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIEMPO_COMIDA_DIA','C539',  'Crear tiempo comida día', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIEMPO_COMIDA_DIA','C540',  'Actualizar tiempo comida día', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIEMPO_COMIDA_DIA','C541',  'Eliminar tiempo comida día', 2, true, null, null,0);


-- 3.38.0

-- Motivos de cierre de sede
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_motivos_cierre_sede_mcs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_cierre_sede (mcs_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_motivos_cierre_sede_mcs_pk_seq'::regclass), mcs_codigo character varying(45), mcs_habilitado boolean, mcs_nombre character varying(255), mcs_nombre_busqueda character varying(255), mcs_ult_mod_fecha timestamp without time zone, mcs_ult_mod_usuario character varying(45), mcs_version integer, CONSTRAINT sg_motivos_cierre_sede_pkey PRIMARY KEY (mcs_pk), CONSTRAINT mcs_codigo_uk UNIQUE (mcs_codigo), CONSTRAINT mcs_nombre_uk UNIQUE (mcs_nombre));
COMMENT ON TABLE catalogo.sg_motivos_cierre_sede IS 'Tabla para el registro de motivos de cierre de sede.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_motivos_cierre_sede.mcs_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_motivos_cierre_sede_aud (mcs_pk bigint NOT NULL, mcs_codigo character varying(45), mcs_habilitado boolean, mcs_nombre character varying(255), mcs_nombre_busqueda character varying(255), mcs_ult_mod_fecha timestamp without time zone, mcs_ult_mod_usuario character varying(45), mcs_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_motivos_cierre_sede_aud ADD PRIMARY KEY (mcs_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOTIVOS_CIERRE_SEDE','C542',  'Crear motivos de cierre de sede', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOTIVOS_CIERRE_SEDE','C543',  'Actualizar motivos de cierre de sede', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOTIVOS_CIERRE_SEDE','C544',  'Eliminar motivos de cierre de sede', 2, true, null, null,0);


-- 3.39.0

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_revocatoria_sede boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_revocatoria_sede boolean;

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_creacion_sede boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_creacion_sede boolean;

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_nominacion_sede boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_nominacion_sede boolean;

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_cambio_domicilio_sede boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_cambio_domicilio_sede boolean;

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_ampliacion_servicios boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_ampliacion_servicios boolean;

ALTER TABLE catalogo.sg_tipos_acuerdos ADD COLUMN  tao_tramite_disminucion_servicios boolean;
ALTER TABLE catalogo.sg_tipos_acuerdos_aud ADD COLUMN  tao_tramite_disminucion_servicios boolean;


-- 3.41.0

-- Items Liquidacion
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_items_liquidacion_ili_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table if not exists catalogo.sg_items_liquidacion (
	ili_pk bigint not null default nextval('catalogo.sg_items_liquidacion_ili_pk_seq'::regclass),
	ili_codigo smallint,
	ili_nombre character varying(255),
	ili_nombre_busqueda character varying(255),
	ili_habilitado boolean,
	ili_ult_mod_fecha timestamp without time zone,
	ili_ult_mod_usuario character varying(45),
	ili_version integer,
	constraint sg_items_liquidacion_pkey primary key (ili_pk),
	constraint ili_codigo_uk unique (ili_codigo),
	constraint ili_nombre_uk unique (ili_nombre));

COMMENT ON TABLE catalogo.sg_items_liquidacion IS 'Tabla para el registro de items liquidacion.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_items_liquidacion.ili_version IS 'Versión del registro.';

create table if not exists catalogo.sg_items_liquidacion_aud (
	ili_pk bigint not null,
	ili_codigo smallint,
	ili_nombre character varying(255),
	ili_nombre_busqueda character varying(255),
	ili_habilitado boolean,
	ili_ult_mod_fecha timestamp without time zone,
	ili_ult_mod_usuario character varying(45),
	ili_version integer,
	rev bigint,
	revtype smallint);


ALTER TABLE catalogo.sg_items_liquidacion_aud ADD PRIMARY KEY (ili_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ITEM_LIQUIDACION','C545',  'Crear Item Liquidación', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ITEM_LIQUIDACION','C546',  'Actualizar Item LIquidación', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ITEM_LIQUIDACION','C547',  'Eliminar Item Liquidación', 2, true, null, null,0);

CREATE TABLE catalogo.sg_menu_pentaho_aud (
	mpe_pk bigserial NOT NULL,
	mpe_tipo varchar(8) NOT NULL,
	mpe_nombre varchar(250) NOT NULL,
	mpe_ruta varchar(250) NOT NULL,
	mpe_habilitado bool NULL,
	mpe_ult_mod_fecha timestamp NULL,
	mpe_ult_mod_usuario varchar(45) NULL,
	mpe_version int4 NULL,
	mpe_operacion_fk int4 NULL,
	rev int8 NULL,
	revtype int2 NULL,
	CONSTRAINT sg_menu_pentaho_aud_pkey PRIMARY KEY (mpe_pk)
);


CREATE TABLE catalogo.sg_menu_pentaho (
	mpe_pk bigserial NOT NULL,
	mpe_tipo varchar(8) NOT NULL,
	mpe_nombre varchar(250) NOT NULL,
	mpe_ruta varchar(250) NOT NULL,
	mpe_habilitado bool NULL,
	mpe_ult_mod_fecha timestamp NULL,
	mpe_ult_mod_usuario varchar(45) NULL,
	mpe_version int4 NULL,
	mpe_operacion_fk int4 NULL,
	CONSTRAINT mpe_ruta_uk UNIQUE (mpe_ruta),
	CONSTRAINT sg_menu_pentaho_pkey PRIMARY KEY (mpe_pk)
);



ALTER TABLE catalogo.sg_menu_pentaho ADD CONSTRAINT sg_mpe_operacion_fkey FOREIGN KEY (mpe_operacion_fk) REFERENCES seguridad.sg_operaciones(ope_pk);

-- nueva categoria de operación.

INSERT INTO seguridad.sg_categorias_operacion
(cop_pk, cop_codigo, cop_nombre, cop_descripcion, cop_habilitado, cop_ult_mod_fecha, cop_ult_mod_usuario, cop_version)
VALUES(14, 'pe', 'Pentaho', 'Tablero Pentaho', true, NULL, NULL, 0);


-- operaciones.

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MENU_PENTAHO','PE1',  'Crear opciones para el tablero de pentaho', 14, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MENU_PENTAHO','PE2',  'Actualizar opciones para el tablero de pentaho', 14, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MENU_PENTAHO','PE3',  'Eliminar opciones para el tablero de pentaho', 14, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_MENU_PENTAHO','PE0',  'Operación para ver todas', 14, true, null, null,0);


-- 3.42.0

-- Elementos de hogar
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_elementos_hogar_eho_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_elementos_hogar (eho_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_elementos_hogar_eho_pk_seq'::regclass), eho_codigo character varying(45), eho_habilitado boolean, eho_nombre character varying(255), eho_nombre_busqueda character varying(255), eho_ult_mod_fecha timestamp without time zone, eho_ult_mod_usuario character varying(45), eho_version integer, CONSTRAINT sg_elementos_hogar_pkey PRIMARY KEY (eho_pk), CONSTRAINT eho_codigo_uk UNIQUE (eho_codigo), CONSTRAINT eho_nombre_uk UNIQUE (eho_nombre));
COMMENT ON TABLE catalogo.sg_elementos_hogar IS 'Tabla para el registro de elementos de hogar.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_elementos_hogar.eho_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_elementos_hogar_aud (eho_pk bigint NOT NULL, eho_codigo character varying(45), eho_habilitado boolean, eho_nombre character varying(255), eho_nombre_busqueda character varying(255), eho_ult_mod_fecha timestamp without time zone, eho_ult_mod_usuario character varying(45), eho_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_elementos_hogar_aud ADD PRIMARY KEY (eho_pk, rev);
-- Fuentes de abastecimiento de agua
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_fuentes_abastecimiento_agua_faa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_fuentes_abastecimiento_agua (faa_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_fuentes_abastecimiento_agua_faa_pk_seq'::regclass), faa_codigo character varying(45), faa_habilitado boolean, faa_nombre character varying(255), faa_nombre_busqueda character varying(255), faa_ult_mod_fecha timestamp without time zone, faa_ult_mod_usuario character varying(45), faa_version integer, CONSTRAINT sg_fuentes_abastecimiento_agua_pkey PRIMARY KEY (faa_pk), CONSTRAINT faa_codigo_uk UNIQUE (faa_codigo), CONSTRAINT faa_nombre_uk UNIQUE (faa_nombre));
COMMENT ON TABLE catalogo.sg_fuentes_abastecimiento_agua IS 'Tabla para el registro de fuentes de abastecimiento de agua.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_fuentes_abastecimiento_agua.faa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_fuentes_abastecimiento_agua_aud (faa_pk bigint NOT NULL, faa_codigo character varying(45), faa_habilitado boolean, faa_nombre character varying(255), faa_nombre_busqueda character varying(255), faa_ult_mod_fecha timestamp without time zone, faa_ult_mod_usuario character varying(45), faa_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_fuentes_abastecimiento_agua_aud ADD PRIMARY KEY (faa_pk, rev);
-- Materiales de piso
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_materiales_piso_map_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_materiales_piso (map_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_materiales_piso_map_pk_seq'::regclass), map_codigo character varying(45), map_habilitado boolean, map_nombre character varying(255), map_nombre_busqueda character varying(255), map_ult_mod_fecha timestamp without time zone, map_ult_mod_usuario character varying(45), map_version integer, CONSTRAINT sg_materiales_piso_pkey PRIMARY KEY (map_pk), CONSTRAINT map_codigo_uk UNIQUE (map_codigo), CONSTRAINT map_nombre_uk UNIQUE (map_nombre));
COMMENT ON TABLE catalogo.sg_materiales_piso IS 'Tabla para el registro de materiales de piso.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_materiales_piso.map_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_materiales_piso_aud (map_pk bigint NOT NULL, map_codigo character varying(45), map_habilitado boolean, map_nombre character varying(255), map_nombre_busqueda character varying(255), map_ult_mod_fecha timestamp without time zone, map_ult_mod_usuario character varying(45), map_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_materiales_piso_aud ADD PRIMARY KEY (map_pk, rev);
-- Compañías de telecomunicación
CREATE SEQUENCE IF NOT EXISTS catalogo.sg_companias_telecomunicacion_cte_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS catalogo.sg_companias_telecomunicacion (cte_pk bigint NOT NULL DEFAULT nextval('catalogo.sg_companias_telecomunicacion_cte_pk_seq'::regclass), cte_codigo character varying(45), cte_habilitado boolean, cte_nombre character varying(255), cte_nombre_busqueda character varying(255), cte_ult_mod_fecha timestamp without time zone, cte_ult_mod_usuario character varying(45), cte_version integer, CONSTRAINT sg_companias_telecomunicacion_pkey PRIMARY KEY (cte_pk), CONSTRAINT cte_codigo_uk UNIQUE (cte_codigo), CONSTRAINT cte_nombre_uk UNIQUE (cte_nombre));
COMMENT ON TABLE catalogo.sg_companias_telecomunicacion IS 'Tabla para el registro de compañías de telecomunicación.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_codigo IS 'Código del registro.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN catalogo.sg_companias_telecomunicacion.cte_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS catalogo.sg_companias_telecomunicacion_aud (cte_pk bigint NOT NULL, cte_codigo character varying(45), cte_habilitado boolean, cte_nombre character varying(255), cte_nombre_busqueda character varying(255), cte_ult_mod_fecha timestamp without time zone, cte_ult_mod_usuario character varying(45), cte_version integer, rev bigint, revtype smallint);
ALTER TABLE catalogo.sg_companias_telecomunicacion_aud ADD PRIMARY KEY (cte_pk, rev);



INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ELEMENTOS_HOGAR','C548',  'Crear elementos de hogar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ELEMENTOS_HOGAR','C549',  'Actualizar elementos de hogar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ELEMENTOS_HOGAR','C550',  'Eliminar elementos de hogar', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FUENTES_ABASTECIMIENTO_AGUA','C551',  'Crear fuentes de abastecimiento de agua', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FUENTES_ABASTECIMIENTO_AGUA','C552',  'Actualizar fuentes de abastecimiento de agua', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FUENTES_ABASTECIMIENTO_AGUA','C553',  'Eliminar fuentes de abastecimiento de agua', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_COMPANIAS_TELECOMUNICACION','C554',  'Crear compañias de telecomunicación', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_COMPANIAS_TELECOMUNICACION','C555',  'Actualizar compañias de telecomunicación', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_COMPANIAS_TELECOMUNICACION','C556',  'Eliminar compañias de telecomunicación', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MATERIALES_PISO','C557',  'Crear materiales de piso', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MATERIALES_PISO','C558',  'Actualizar materiales de piso', 2, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MATERIALES_PISO','C559',  'Eliminar materiales de piso', 2, true, null, null,0);


INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('REF', 'Refrigerador', 'refrigerador' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('TV', 'Televisión', 'television' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('TF', 'Teléfono fijo', 'telefono fijo' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('CAI', 'Celular con acceso a interner', 'celular con acceso a internet' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('COM', 'Computadora', 'computadora' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('LAP', 'Laptop', 'laptop' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('TAB', 'Tablet', 'tablet' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('RAD', 'Radio o equipo de sonido', 'radio o equipo de sonido' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('AAC', 'Aire acondicionado', 'aire acondicionado' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('MOT', 'Motocicleta', 'motocicleta' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_elementos_hogar (eho_codigo, eho_nombre, eho_nombre_busqueda, eho_habilitado, eho_ult_mod_fecha, eho_ult_mod_usuario, eho_version) 
values ('VEH', 'Vehículo', 'vehiculo' ,true, now(), 'admin', 0);


INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('SAC', 'Servicio de agua por cañería interna a la casa', 'servicio de agua por caneria interna a la casa' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('POZ', 'Pozo', 'pozo' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('PIP', 'Pipa', 'pipa' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('PCP', 'Pila, chorro público o cantarera', 'Ppila, chorro publico o cantarera' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('ACA', 'Acarreo (río, lago, nacimiento de agua)', 'acarreo (rio, lago, nacimiento de agua)' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('ALL', 'Aguas lluvias', 'aguas lluvias' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_fuentes_abastecimiento_agua (faa_codigo, faa_nombre, faa_nombre_busqueda, faa_habilitado, faa_ult_mod_fecha, faa_ult_mod_usuario, faa_version) 
values ('OTR', 'Otros', 'otros' ,true, now(), 'admin', 0);



INSERT INTO catalogo.sg_companias_telecomunicacion (cte_codigo, cte_nombre, cte_nombre_busqueda, cte_habilitado, cte_ult_mod_fecha, cte_ult_mod_usuario, cte_version) 
values ('CLA', 'Claro', 'claro' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_companias_telecomunicacion (cte_codigo, cte_nombre, cte_nombre_busqueda, cte_habilitado, cte_ult_mod_fecha, cte_ult_mod_usuario, cte_version) 
values ('TIG', 'Tigo', 'tigo' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_companias_telecomunicacion (cte_codigo, cte_nombre, cte_nombre_busqueda, cte_habilitado, cte_ult_mod_fecha, cte_ult_mod_usuario, cte_version) 
values ('MOV', 'Movistar', 'movistar' ,true, now(), 'admin', 0);


INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('TIE', 'Tierra', 'tierra' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('CEM', 'Cemento', 'cemento' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('LBO', 'Ladrillos de barro', 'ladrillos de barro' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('LCO', 'Ladrillos de cemento', 'ladrillos de cemento' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('LCA', 'Ladrillos de cerámica', 'ladrillos de cerámica' ,true, now(), 'admin', 0);
INSERT INTO catalogo.sg_materiales_piso (map_codigo, map_nombre, map_nombre_busqueda, map_habilitado, map_ult_mod_fecha, map_ult_mod_usuario, map_version) 
values ('OTR', 'Otros', 'otros' ,true, now(), 'admin', 0);


