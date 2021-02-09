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


ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_texto_uso type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_texto_uso type text;
ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_texto_normativa type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_texto_normativa type text;
ALTER TABLE catalogo.sg_ayudas ALTER COLUMN ayu_vinculos type text;
ALTER TABLE catalogo.sg_ayudas_aud ALTER COLUMN ayu_vinculos type text;


ALTER TABLE catalogo.sg_ayudas_aud ADD PRIMARY KEY (ayu_pk,rev);
ALTER TABLE catalogo.sg_configuraciones_aud ADD PRIMARY KEY (con_pk,rev);
ALTER TABLE catalogo.sg_departamentos_aud ADD PRIMARY KEY (dep_pk,rev);
ALTER TABLE catalogo.sg_municipios_aud ADD PRIMARY KEY (mun_pk,rev);
ALTER TABLE catalogo.sg_nacionalidades_aud ADD PRIMARY KEY (nac_pk,rev);
ALTER TABLE catalogo.sg_paises_aud ADD PRIMARY KEY (pai_pk,rev);
ALTER TABLE catalogo.sg_sexos_aud ADD PRIMARY KEY (sex_pk,rev);
ALTER TABLE catalogo.sg_zonas_aud ADD PRIMARY KEY (zon_pk,rev);


ALTER TABLE catalogo.sg_ayudas_aud                          ADD CONSTRAINT 	ayudas_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_configuraciones_aud                 ADD CONSTRAINT 	configuraciones_revinfo_fk                  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_departamentos_aud                   ADD CONSTRAINT 	departamentos_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_municipios_aud                      ADD CONSTRAINT 	municipios_revinfo_fk                       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_nacionalidades_aud                  ADD CONSTRAINT 	nacionalidades_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_paises_aud                          ADD CONSTRAINT 	paises_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_sexos_aud                           ADD CONSTRAINT 	sexos_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE catalogo.sg_zonas_aud                           ADD CONSTRAINT 	zonas_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

ALTER TABLE catalogo.sg_configuraciones DROP COLUMN IF EXISTS con_habilitado;
ALTER TABLE catalogo.sg_configuraciones_aud DROP COLUMN IF EXISTS con_habilitado;

ALTER TABLE catalogo.sg_configuraciones ADD COLUMN IF NOT EXISTS con_valor text;
ALTER TABLE catalogo.sg_configuraciones_aud ADD COLUMN IF NOT EXISTS con_valor text;

ALTER TABLE catalogo.sg_configuraciones ADD COLUMN IF NOT EXISTS con_es_editor BOOLEAN;
COMMENT ON COLUMN catalogo.sg_configuraciones.con_es_editor IS 'Indica si el valor a ingresar es por medio de un editor de texto o texto plano.';
ALTER TABLE catalogo.sg_configuraciones_aud ADD COLUMN IF NOT EXISTS con_es_editor BOOLEAN;


-- Las operaciones se guardan en la base de datos de SIGES

INSERT INTO seguridad.sg_categorias_operacion values (10,'sca', 'Sigo catálogo','Sigo catálogo',true,null,null,0);
-- Las operaciones llevan una categoria. Las de catalogo llevan la categoria 10.
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_SIGO_CATALOGO','W13',  '', 4, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DEPARTAMENTO','CS1',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DEPARTAMENTO','CS2',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DEPARTAMENTO','CS3',  '', 10, true, null, null,0);   
    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MUNICIPIO','CS4',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MUNICIPIO ','CS5',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MUNICIPIO','CS6',  '', 10, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PAIS','CS7',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PAIS ','CS8',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PAIS','CS9',  '', 10, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SEXO','CS10',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SEXO ','CS11',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SEXO','CS12',  '', 10, true, null, null,0); 


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ZONA','CS13',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ZONA ','CS14',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ZONA','CS15',  '', 10, true, null, null,0);          
    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AYUDA','CS16',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AYUDA','CS17'  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AYUDA','CS18',  '', 10, true, null, null,0);

    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONFIGURACIONES','CS19',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONFIGURACIONES', 'C20',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONFIGURACIONES', 'C21',  '', 10, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ARCHIVO ','CS22',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ARCHIVO','CS23',  '', 10, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ARCHIVO','CS24',  '', 10, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NACIONALIDADES ','CS25',  '', 10, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NACIONALIDADES','CS26',  '', 10, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NACIONALIDADES','CS27',  '', 10, true, null, null,0); 
