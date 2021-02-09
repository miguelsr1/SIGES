CREATE SCHEMA IF NOT EXISTS catalogo;
CREATE SCHEMA IF NOT EXISTS auditoria;
CREATE SCHEMA IF NOT EXISTS public;
CREATE SCHEMA IF NOT EXISTS centros_educativos;

CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence INCREMENT 1 START 1;
CREATE TABLE IF NOT EXISTS public.REVINFO (rev bigint, revtstmp bigint, usuario CHARACTER varying(255), CONSTRAINT revinfo_pkey PRIMARY KEY (rev));
INSERT INTO public.REVINFO (rev, revtstmp) values (nextval('public.hibernate_sequence'::regclass), extract(epoch FROM CURRENT_TIMESTAMP));

-- Auditoría operaciones. Van en el esquema auditoria.
CREATE SEQUENCE IF NOT EXISTS auditoria.sg_registros_auditoria_aud_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS auditoria.sg_registros_auditoria (aud_pk bigint NOT NULL DEFAULT nextval('auditoria.sg_registros_auditoria_aud_pk_seq'::regclass), aud_ip character varying(45), aud_clase character varying(255), aud_operacion character varying(255), aud_entidad_id bigint, aud_comentario character varying(1000), aud_resultado_operacion character varying(100), aud_fecha timestamp without time zone, aud_excepcion character varying(1000), aud_codigo_usuario character varying(45), aud_hash_md5 character varying(300), CONSTRAINT sg_registros_auditoria_pkey PRIMARY KEY (aud_pk));


-- Organizacion curricular
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_organizaciones_curricular_ocu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organizaciones_curricular(ocu_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_organizaciones_curricular_ocu_pk_seq'::regclass), ocu_codigo character varying(4), ocu_nombre character varying(255),ocu_nombre_busqueda character varying(255), ocu_descripcion character varying(255), ocu_habilitado boolean, ocu_ult_mod_fecha timestamp without time zone, ocu_ult_mod_usuario character varying(45), ocu_version integer, CONSTRAINT sg_organizaciones_curricular_pkey PRIMARY KEY (ocu_pk), CONSTRAINT ocu_codigo_uk UNIQUE (ocu_codigo), CONSTRAINT ocu_nombre_uk UNIQUE (ocu_nombre)); COMMENT ON TABLE centros_educativos.sg_organizaciones_curricular IS 'Tabla para el registro de las organizaciones curriculares.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_pk IS 'Número correlativo autogenerado.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_codigo IS 'Código de la organización curricular.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_nombre IS 'Nombre de la organización curricular.';  COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_descripcion IS 'Descripcion de la organización curricular.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_habilitado IS 'Indica si la organización curricular se encuentra habilitado(true) o inhabilitado(false).'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_ult_mod_usuario IS 'Último usuario que modificó el registro.'; COMMENT ON COLUMN centros_educativos.sg_organizaciones_curricular.ocu_version IS 'Versión de la organización curricular';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organizaciones_curricular_aud (ocu_pk bigint NOT NULL, ocu_codigo character varying(4), ocu_nombre character varying(255),ocu_nombre_busqueda character varying(255), ocu_descripcion character varying(255), ocu_habilitado boolean, ocu_ult_mod_fecha timestamp without time zone, ocu_ult_mod_usuario character varying(45), ocu_version integer, rev bigint, revtype smallint);

-- Niveles
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_niveles_niv_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_niveles (niv_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_niveles_niv_pk_seq'::regclass),niv_organizacion_curricular bigint NOT NULL, niv_codigo character varying(4), niv_nombre character varying(255), niv_orden int, niv_admite_ciclos boolean, niv_obligatorio boolean, niv_habilitado boolean, niv_ult_mod_fecha timestamp without time zone, niv_ult_mod_usuario character varying(45), niv_version integer, CONSTRAINT sg_niveles_pkey PRIMARY KEY (niv_pk), CONSTRAINT sg_niveles_fkey FOREIGN KEY (niv_organizacion_curricular) REFERENCES centros_educativos.sg_organizaciones_curricular(ocu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT niv_codigo_uk UNIQUE (niv_codigo), CONSTRAINT niv_nombre_orgcurricular_uk UNIQUE (niv_nombre,niv_organizacion_curricular));
    COMMENT ON TABLE centros_educativos.sg_niveles IS 'Tabla para el registro de los niveles de las organizaciones curriculares.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_organizacion_curricular IS 'Clave foránea que hace referencia a la organización curricular.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_orden IS 'Orden del registro.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_admite_ciclos IS 'Identifica si el registro admite ciclos(true) o no (false).';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_obligatorio IS 'Indica si el registro es obligatorio.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_niveles.niv_version IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_niveles_aud (niv_pk bigint NOT NULL, niv_organizacion_curricular bigint NOT NULL, niv_codigo character varying(4), niv_nombre character varying(255), niv_orden int, niv_admite_ciclos boolean, niv_obligatorio boolean, niv_habilitado boolean, niv_ult_mod_fecha timestamp without time zone, niv_ult_mod_usuario character varying(45), niv_version integer, rev bigint, revtype smallint);

-- Ciclos
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_ciclos_cic_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_ciclos (cic_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_ciclos_cic_pk_seq'::regclass),cic_nivel bigint NOT NULL, cic_codigo character varying(4), cic_nombre character varying(255), cic_orden int, cic_admite_modalidad boolean, cic_obligatorio boolean, cic_habilitado boolean, cic_ult_mod_fecha timestamp without time zone, cic_ult_mod_usuario character varying(45), cic_version integer, CONSTRAINT sg_ciclos_pkey PRIMARY KEY (cic_pk), CONSTRAINT sg_ciclos_fkey FOREIGN KEY (cic_nivel) REFERENCES centros_educativos.sg_niveles(niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT cic_codigo_uk UNIQUE (cic_codigo), CONSTRAINT cic_nombre_nivel_uk UNIQUE (cic_nombre,cic_nivel));
    COMMENT ON TABLE centros_educativos.sg_ciclos IS 'Tabla para el registro de los ciclos.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_nivel IS 'Clave foránea que hace referencia al nivel que pertenece el ciclo.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_orden IS 'Orden del registro.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_admite_modalidad IS 'Identifica si el registro admite modalidad(true) o no (false).';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_obligatorio IS 'Indica si el registro es obligatorio.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_ciclos.cic_version IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_ciclos_aud (cic_pk bigint NOT NULL,cic_nivel bigint NOT NULL, cic_codigo character varying(4), cic_nombre character varying(255), cic_orden int, cic_admite_modalidad boolean, cic_obligatorio boolean, cic_habilitado boolean, cic_ult_mod_fecha timestamp without time zone, cic_ult_mod_usuario character varying(45), cic_version integer, rev bigint, revtype smallint);

-- Modalidad
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_modalidades_mod_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modalidades (mod_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_modalidades_mod_pk_seq'::regclass),mod_ciclo bigint NOT NULL, mod_codigo character varying(4), mod_nombre character varying(500), mod_orden int,mod_admite_opcion boolean, mod_habilitado boolean, mod_ult_mod_fecha timestamp without time zone, mod_ult_mod_usuario character varying(45), mod_version integer, CONSTRAINT sg_modalidades_pkey PRIMARY KEY (mod_pk), CONSTRAINT sg_modalidades_fkey FOREIGN KEY (mod_ciclo) REFERENCES centros_educativos.sg_ciclos(cic_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT mod_codigo_uk UNIQUE (mod_codigo), CONSTRAINT mod_nombre_ciclo_uk UNIQUE (mod_nombre,mod_ciclo));
    COMMENT ON TABLE centros_educativos.sg_modalidades IS 'Tabla para el registro de las modalidades.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_ciclo IS 'Clave foránea que hace referencia al ciclo que pertenece la modalidad.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_orden IS 'Orden del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_admite_opcion IS 'Indica si el registro se admite opcion(true) o no admite opcion(false).';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modalidades.mod_version IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modalidades_aud (mod_pk bigint NOT NULL,mod_ciclo bigint NOT NULL, mod_codigo character varying(4), mod_nombre character varying(500), mod_orden int,mod_admite_opcion boolean, mod_habilitado boolean, mod_ult_mod_fecha timestamp without time zone, mod_ult_mod_usuario character varying(45), mod_version integer, rev bigint, revtype smallint);

-- Tipos Calendario
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_tipos_calendario_tca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_tipos_calendario (tca_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_tipos_calendario_tca_pk_seq'::regclass), tca_codigo character varying(45), tca_habilitado boolean, tca_nombre character varying(255), tca_nombre_busqueda character varying(255), tca_ult_mod_fecha timestamp without time zone, tca_ult_mod_usuario character varying(45), tca_version integer, CONSTRAINT sg_tipos_calendario_pkey PRIMARY KEY (tca_pk), CONSTRAINT tca_codigo_uk UNIQUE (tca_codigo), CONSTRAINT tca_nombre_uk UNIQUE (tca_nombre));
COMMENT ON TABLE centros_educativos.sg_tipos_calendario IS 'Tabla para el registro de tipos calendario.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_codigo IS 'Código del registro.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_tipos_calendario.tca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_tipos_calendario_aud (tca_pk bigint NOT NULL, tca_codigo character varying(45), tca_habilitado boolean, tca_nombre character varying(255), tca_nombre_busqueda character varying(255), tca_ult_mod_fecha timestamp without time zone, tca_ult_mod_usuario character varying(45), tca_version integer, rev bigint, revtype smallint);


-- Sedes
------------------------------------
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sedes_sed_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes(sed_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_sedes_sed_pk_seq'::regclass),sed_codigo CHARACTER varying(5),sed_nombre CHARACTER varying(500), sed_nombre_busqueda CHARACTER varying(500), sed_telefono CHARACTER varying(14),sed_telefono_movil CHARACTER varying(14),sed_correo_electronico CHARACTER varying(255),sed_anio_inicio_act integer,sed_propietario CHARACTER varying(500),sed_nota CHARACTER varying(4000),sed_centro_adscrito bigint,sed_clasificacion_fk bigint,sed_habilitado BOOLEAN,sed_ult_mod_fecha timestamp without TIME zone,sed_ult_mod_usuario CHARACTER varying(45),sed_version INTEGER,sed_tipo varchar NOT NULL, sed_migracion BOOLEAN,sed_tipo_calendario_fk bigint, sed_direccion_fk bigint, CONSTRAINT sg_sedes_pkey PRIMARY KEY (sed_pk),CONSTRAINT sed_codigo_uk UNIQUE (sed_codigo));
COMMENT ON TABLE centros_educativos.sg_sedes IS 'Se registran los datos de las sedes de educación, estas pueden ser: sedes, centros educativos oficiales o privados, círculos de alfabetización o infancia.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_codigo IS 'Código del registro';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_nombre_busqueda IS 'Nombre del registro normalizado para búsqueda.'; 
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_telefono IS 'Número de teléfono fijo de contacto.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_telefono_movil IS 'Número de teléfono móvil de contacto.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_correo_electronico IS 'Dirección de correo electronico de contacto';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_anio_inicio_act IS 'Año en que inicio de funcionamiento de la sede.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_propietario IS 'Nombre del propietario de la sede.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_nota IS 'Espacio para escribir aclaraciones u observaciones';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_centro_adscrito IS 'Identificador de la sede que es de tipo centro educativo, que administra o autoriza a otra.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_tipo_calendario_fk IS 'Tipo de calendario a la que pertenece la sede.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_clasificacion_fk IS 'Campo que almacena el tipo de clasificación a la que pertenece la sede.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_migracion IS 'Campo que se utiliza para indicar si el registro fue migrado de los sistemas anteriores al SIGES.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_tipo IS 'Campo para indicar el tipo de entidad java a la que pertenece el registro.';
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_direccion_fk IS 'Clave foránea que hace referencia a la dirección donde está la ubicada sede.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_aud(sed_pk bigint NOT NULL,sed_codigo CHARACTER varying(5),sed_nombre CHARACTER varying(500), sed_nombre_busqueda CHARACTER varying(500), sed_telefono CHARACTER varying(14),sed_telefono_movil CHARACTER varying(14),sed_correo_electronico CHARACTER varying(255),sed_anio_inicio_act integer,sed_propietario CHARACTER varying(500),sed_nota CHARACTER varying(4000),sed_centro_adscrito bigint,sed_clasificacion_fk bigint,sed_habilitado BOOLEAN,sed_ult_mod_fecha timestamp without TIME zone,sed_ult_mod_usuario CHARACTER varying(45),sed_version INTEGER,sed_tipo varchar NOT NULL, sed_migracion BOOLEAN,sed_tipo_calendario_fk bigint, sed_direccion_fk INTEGER,rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos( sed_pk int8 NOT NULL, sed_ub_alf_fecha_fin_op date NULL, CONSTRAINT sg_sedes_circulos_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sedes_circulos_sg_sedes_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes(sed_pk)) WITH ( OIDS=FALSE ) ;
COMMENT ON TABLE centros_educativos.sg_sedes_circulos IS 'Se registran los datos de las sedes de educación, que tiene características de círculos de alfabetización o de infancia.';
COMMENT ON COLUMN centros_educativos.sg_sedes_circulos.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes.';
COMMENT ON COLUMN centros_educativos.sg_sedes_circulos.sed_ub_alf_fecha_fin_op IS 'Campo para almacenar la fecha de fin de operación del círculo.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos_aud( sed_pk int8 NOT NULL, sed_ub_alf_fecha_fin_op date NULL,rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced(sed_pk int8 NOT NULL, ced_legalizado bool NOT NULL DEFAULT false, CONSTRAINT sg_sedes_ced_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sedes_ced_sg_sedes_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes(sed_pk)) WITH ( OIDS=FALSE ) ;
COMMENT ON TABLE centros_educativos.sg_sedes_ced IS 'Se registran los datos de las sedes de educación, que tiene características de centros educativos oficiales o privados.';
COMMENT ON COLUMN centros_educativos.sg_sedes_ced.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes.';
COMMENT ON COLUMN centros_educativos.sg_sedes_ced.ced_legalizado IS 'Campo para indicar si el centro educativo está legalmente autorizado por el MINED.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced_aud(sed_pk int8 NOT NULL, ced_legalizado bool NOT NULL DEFAULT false, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced_ofi(sed_pk int8 NOT NULL,CONSTRAINT sg_sedes_ced_ofi_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sed_ced_ofi_sg_sed_ced_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes_ced(sed_pk)) WITH ( OIDS=FALSE );
COMMENT ON TABLE centros_educativos.sg_sedes_ced_ofi IS 'Se registran los datos de las sedes de educación, que tiene características de centros educativos oficiales.';
COMMENT ON COLUMN centros_educativos.sg_sedes_ced_ofi.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes_ced.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced_ofi_aud( sed_pk int8 NOT NULL,rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced_pri( sed_pk int8 NOT NULL,CONSTRAINT sg_sedes_ced_pri_pk PRIMARY KEY (sed_pk), pri_subvencionada bool NULL DEFAULT false, CONSTRAINT sg_sed_ced_pri_sg_sed_ced_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes_ced(sed_pk)) WITH ( OIDS=FALSE );
COMMENT ON TABLE centros_educativos.sg_sedes_ced_pri IS 'Se registran los datos de las sedes de educación, que tiene características de centros educativos privados.';
COMMENT ON COLUMN centros_educativos.sg_sedes_ced_pri.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes_ced.';
COMMENT ON COLUMN centros_educativos.sg_sedes_ced_pri.pri_subvencionada IS 'Campo para indicar si el centro educativo privado recibe apoyo del MINED.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_ced_pri_aud( sed_pk int8 NOT NULL, pri_subvencionada bool, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos_alfa( sed_pk int8 NOT NULL,CONSTRAINT sg_sedes_circulos_alfa_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sed_circul_alfa_sg_sed_circul_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes_circulos(sed_pk)) WITH ( OIDS=FALSE );
COMMENT ON TABLE centros_educativos.sg_sedes_circulos_alfa IS 'Se registran los datos de las sedes de educación, que tiene características de círculos de alfabetización.';
COMMENT ON COLUMN centros_educativos.sg_sedes_circulos_alfa.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes_circulos.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos_alfa_aud( sed_pk int8 NOT NULL, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos_infa( sed_pk int8 NOT NULL,CONSTRAINT sg_sedes_circulos_infa_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sed_circul_infa_sg_sed_circul_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes_circulos(sed_pk)) WITH ( OIDS=FALSE );
COMMENT ON TABLE centros_educativos.sg_sedes_circulos_infa IS 'Se registran los datos de las sedes de educación, que tiene características de círculos de infancia.';
COMMENT ON COLUMN centros_educativos.sg_sedes_circulos_infa.sed_pk IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes_circulos.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_circulos_infa_aud( sed_pk int8 NOT NULL, rev bigint, revtype smallint);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_educame( sed_pk int8 NOT NULL, CONSTRAINT sg_sedes_educame_pk PRIMARY KEY (sed_pk), CONSTRAINT sg_sedes_educame_sg_sedes_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes(sed_pk)) WITH ( OIDS=FALSE );
    COMMENT ON TABLE  centros_educativos.sg_sedes_educame           IS 'Se registran los datos de las sedes de educame.';
    COMMENT ON COLUMN centros_educativos.sg_sedes_educame.sed_pk    IS 'Campo de identificación del registro y clave foránea que hace referencia a la tabla sg_sedes.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_educame_aud( sed_pk int8 NOT NULL, rev bigint, revtype smallint);


CREATE UNIQUE INDEX IF NOT EXISTS sg_sedes_sed_pk_idx ON centros_educativos.sg_sedes USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sedes_circulos_sed_pk_idx ON centros_educativos.sg_sedes_circulos USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sedes_ced_sed_pk_idx ON centros_educativos.sg_sedes_ced USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sed_ced_ofi_sed_pk_idx ON centros_educativos.sg_sedes_ced_ofi USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sed_ced_pri_sed_pk_idx ON centros_educativos.sg_sedes_ced_pri USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sed_circulos_alfa_pk_idx ON centros_educativos.sg_sedes_circulos_alfa USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sed_circulos_infa_pk_idx ON centros_educativos.sg_sedes_circulos_infa USING btree (sed_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_sedes_educame_sed_pk_idx ON centros_educativos.sg_sedes_educame USING btree (sed_pk) ;
CREATE INDEX IF NOT EXISTS sg_sedes_sed_tipo_idx ON centros_educativos.sg_sedes (sed_tipo) ;


-- Opciones
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_opciones_opc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_opciones (opc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_opciones_opc_pk_seq'::regclass),opc_modalidad_fk bigint NOT NULL,opc_sector_economico bigint,opc_codigo character varying(4), opc_nombre character varying(500), opc_nombre_busqueda character varying(255),opc_descripcion CHARACTER varying(255), opc_habilitado boolean, opc_ult_mod_fecha timestamp without time zone, opc_ult_mod_usuario character varying(45), opc_version integer, CONSTRAINT sg_opciones_pkey PRIMARY KEY (opc_pk),CONSTRAINT sg_opciones_modalidad FOREIGN KEY (opc_modalidad_fk) REFERENCES centros_educativos.sg_modalidades(mod_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_opciones_sector_economico_fkey FOREIGN KEY (opc_sector_economico) REFERENCES catalogo.sg_sectores_economicos(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT opc_codigo_uk UNIQUE (opc_codigo), CONSTRAINT opc_nombre_codigo_uk UNIQUE (opc_nombre,opc_sector_economico),CONSTRAINT opc_nombre_modalidad_uk UNIQUE (opc_nombre,opc_modalidad_fk));
    COMMENT ON TABLE centros_educativos.sg_opciones IS 'Tabla para el registro de las opciones.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_modalidad_fk IS 'Clave foránea que hace referencia a la modalidad que pertenece la opcion.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_sector_economico IS 'Clave foránea que hace referencia al sector económico que pertenece la opción.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_nombre_busqueda      IS 'Nombre del registro normalizado para búsquedas.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_opciones.opc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_opciones_aud (opc_pk bigint NOT NULL,opc_modalidad_fk bigint NOT NULL,opc_sector_economico bigint, opc_codigo character varying(4), opc_nombre character varying(500),opc_nombre_busqueda character varying(255),opc_descripcion CHARACTER varying(255), opc_habilitado boolean, opc_ult_mod_fecha timestamp without time zone, opc_ult_mod_usuario character varying(45), opc_version integer, rev bigint, revtype smallint);

-- Persona
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_personas_per_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas (per_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_personas_per_pk_seq'::regclass), per_primer_nombre character varying(50), per_segundo_nombre character varying(50), per_tercer_nombre character varying(50), per_primer_apellido character varying(50), per_segundo_apellido character varying(50), per_tercer_apellido character varying(50), per_nombre_busqueda CHARACTER VARYING(500), per_fecha_nacimiento date, per_email character varying(100), per_etnia_fk bigint, per_estado_civil_fk bigint, per_sexo_fk bigint, per_tipo_sangre_fk bigint, per_cantidad_hijos int, per_habilitado boolean, per_dui bigint,per_cun bigint,per_nie bigint,per_partida_nacimiento bigint,per_partida_nacimiento_folio character varying(255),per_partida_nacimiento_libro character varying(255),per_departamento_nacimiento_fk bigint,per_municipio_nacimiento_fk bigint,per_ult_mod_fecha timestamp without time zone, per_ult_mod_usuario character varying(45), per_version integer, CONSTRAINT sg_personas_pkey PRIMARY KEY (per_pk), CONSTRAINT sg_personas_etnia_fkey FOREIGN KEY (per_etnia_fk) REFERENCES catalogo.sg_etnias(etn_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_personas_estado_civil_fkey FOREIGN KEY (per_estado_civil_fk) REFERENCES catalogo.sg_estados_civil(eci_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_personas_sexo_fkey FOREIGN KEY (per_sexo_fk) REFERENCES catalogo.sg_sexos(sex_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_personas_tipo_sangre_fkey FOREIGN KEY (per_estado_civil_fk) REFERENCES catalogo.sg_tipos_sangre(tsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_personas_departamento_fkey FOREIGN KEY (per_departamento_nacimiento_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_personas_municipio_fkey FOREIGN KEY (per_municipio_nacimiento_fk) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_personas                         IS 'Tabla para el registro de personas.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_primer_nombre       IS 'Primer nombre de la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_segundo_nombre      IS 'Segundo nombre de la persona';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_tercer_nombre       IS 'Tercer nombre de la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_primer_apellido     IS 'Primer apellido de la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_segundo_apellido    IS 'Segundo apellido de la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_tercer_apellido     IS 'Tercer apellido de la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_nombre_busqueda     IS 'Nombre completo normalizado para búsqueda.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_fecha_nacimiento    IS 'Fecha de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_email               IS 'Dirección de correo electronico.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_etnia_fk            IS 'Clave foránea que hace referencia a la etnia.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_estado_civil_fk     IS 'Clave foránea que hace referencia al estado civil.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_sexo_fk             IS 'Clave foránea que hace referencia al sexo.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_tipo_sangre_fk      IS 'Clave foránea que hace referencia al tipo de sangre.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_cantidad_hijos      IS 'Número de hijos.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_habilitado          IS 'Indica si el grado se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_dui                 IS 'Número del documento único de identificación.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_cun                 IS 'CUN.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_nie                 IS 'NIE.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_partida_nacimiento  IS 'Número de partida de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_partida_nacimiento_folio           IS 'Folio de la partida de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_partida_nacimiento_libro           IS 'Libro de la partida de nacimiento).';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_departamento_nacimiento_fk         IS 'Clave foránea que hace referencia al departamento de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_municipio_nacimiento_fk            IS 'Clave foránea que hace referencia al municipio de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_personas.per_version             IS 'Versión del grado.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_aud (per_pk bigint NOT NULL, per_primer_nombre character varying(50), per_segundo_nombre character varying(50), per_tercer_nombre character varying(50), per_primer_apellido character varying(50), per_segundo_apellido character varying(50), per_tercer_apellido character varying(50), per_nombre_busqueda CHARACTER VARYING(500), per_fecha_nacimiento date, per_email character varying(100), per_etnia_fk bigint, per_estado_civil_fk bigint, per_sexo_fk bigint, per_tipo_sangre_fk bigint, per_cantidad_hijos int, per_habilitado boolean, per_dui bigint,per_cun bigint,per_nie bigint,per_partida_nacimiento bigint,per_partida_nacimiento_folio character varying(255),per_partida_nacimiento_libro character varying(255),per_departamento_nacimiento_fk bigint,per_municipio_nacimiento_fk bigint,per_ult_mod_fecha timestamp without time zone, per_ult_mod_usuario character varying(45), per_version integer, rev bigint, revtype smallint);

-- Direcciones
----------------------------------
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_direcciones_dir_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_direcciones(dir_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_direcciones_dir_pk_seq'::regclass),dir_direccion CHARACTER varying(500),dir_departamento bigint,dir_municipio bigint,dir_canton bigint,dir_caserio bigint,dir_zona bigint,dir_latitud numeric(11,9),dir_longitud numeric(11,9),dir_ult_mod_fecha timestamp without TIME zone,dir_ult_mod_usuario CHARACTER varying(45),dir_version INTEGER,dir_sede_fk bigint,dir_persona_fk bigint,CONSTRAINT sg_direcciones_pkey PRIMARY KEY (dir_pk),CONSTRAINT sg_dir_sede_fkey FOREIGN KEY (dir_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_dir_persona_fkey FOREIGN KEY (dir_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_direcciones_aud(dir_pk bigint NOT NULL, dir_direccion CHARACTER varying(500), dir_departamento bigint, dir_municipio bigint, dir_canton bigint, dir_caserio bigint, dir_zona bigint, dir_latitud numeric(11,9), dir_longitud numeric(11,9), dir_ult_mod_fecha timestamp without TIME zone, dir_ult_mod_usuario CHARACTER varying(45), dir_version INTEGER, dir_sed_fk bigint, dir_persona_fk bigint, rev bigint, revtype smallint);

COMMENT ON TABLE centros_educativos.sg_direcciones IS 'Tabla para el registro de direcciones.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_direccion IS 'Campo donde se almacena la dirección';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_departamento IS 'Lleve foránea con que se identifica el departamento donde se ubica la dirección.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_municipio IS 'Lleve foránea con que se identifica el municipio donde se ubica la dirección.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_canton IS 'Clave foránea que identifica el cantón donde se ubica la dirección.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_caserio IS 'Clave foránea que identifica el caserío donde se ubica la dirección.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_zona IS 'Clave foránea que identifica la zona en donde se ubica la dirección.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_latitud IS 'Almacena el valor de latitud para la geolocalización';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_longitud IS 'Almacena el valor de longitud para la geolocalización';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_ult_mod_fecha IS 'Última fecha en la que fue modificado el registro.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_ult_mod_usuario IS 'Último usuario en modificar el registro.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_version IS 'Versión del centros_educativos que se uso para el registo.';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_sede_fk IS 'Clave foránea que identifica a que sede pertenece este registro';
COMMENT ON COLUMN centros_educativos.sg_direcciones.dir_persona_fk IS 'Clave foránea que identifica a que persona pertenece este registro';

-- Telefono
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_telefonos_tel_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_telefonos (tel_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_telefonos_tel_pk_seq'::regclass), tel_persona bigint, tel_telefono character varying(15), tel_tipo_telefono_fk bigint, tel_ult_mod_fecha timestamp without time zone, tel_ult_mod_usuario character varying(45), tel_version integer, CONSTRAINT sg_telefonos_pkey PRIMARY KEY (tel_pk), CONSTRAINT sg_telefonos_fkey_persona FOREIGN KEY (tel_persona) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_telefonos_tipo_telefono_fkey FOREIGN KEY (tel_tipo_telefono_fk) REFERENCES catalogo.sg_tipos_telefono(tto_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_telefonos                         IS 'Tabla para el registro de los telefonos.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_persona             IS 'Clave foránea que hace referencia a la persona que pertenece el registro.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_telefono            IS 'Número de teléfono';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_tipo_telefono_fk       IS 'Clave foránea que hace referencia al tipo de teléfono.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_telefonos.tel_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_telefonos_aud (tel_pk bigint NOT NULL, tel_persona bigint, tel_telefono character varying(15), tel_tipo_telefono_fk bigint, tel_ult_mod_fecha timestamp without time zone, tel_ult_mod_usuario character varying(45), tel_version integer, rev bigint, revtype smallint);

-- Estudiante
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estudiantes_est_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudiantes (est_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_estudiantes_est_pk_seq'::regclass), est_persona bigint, est_dis_km_centro NUMERIC(10,2), est_fac_riesgo CHARACTER VARYING(255), est_trabaja BOOLEAN, est_embarazo BOOLEAN, est_tipo_trabajo_fk bigint, est_medio_transporte_fk bigint, est_habilitado boolean, est_ult_mod_fecha timestamp without time zone, est_ult_mod_usuario character varying(45), est_version integer, CONSTRAINT sg_estudiantes_pkey PRIMARY KEY (est_pk), CONSTRAINT sg_estudiantes_personas_fkey FOREIGN KEY (est_persona) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudiantes_medio_transporte_fkey FOREIGN KEY (est_medio_transporte_fk) REFERENCES catalogo.sg_medios_transporte(mtr_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudiantes_tipo_trabajo_fkey FOREIGN KEY (est_tipo_trabajo_fk) REFERENCES catalogo.sg_tipos_trabajo(ttr_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_estudiantes                         IS 'Tabla para el registro de los estudiantes.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_persona             IS 'Clave foranea que hace referencia a la persona que pertenece el el estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_dis_km_centro       IS 'Distancia en kilometros al centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_fac_riesgo          IS 'FAC riesgo.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_trabaja             IS 'Bandera que indica si el estudiante trabaja.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_embarazo            IS 'Bandera que indica si el estudiante esta embarazado.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_tipo_trabajo_fk        IS 'Clave foránea que hace referencia al tipo de trabajo.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_medio_transporte_fk    IS 'Clave foránea que hace referencia al medio de transporte.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_habilitado          IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudiantes.est_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudiantes_aud (est_pk bigint, est_persona bigint, est_dis_km_centro NUMERIC(10,2), est_fac_riesgo CHARACTER VARYING(255), est_trabaja BOOLEAN, est_embarazo BOOLEAN, est_tipo_trabajo_fk bigint, est_medio_transporte_fk bigint, est_habilitado boolean, est_ult_mod_fecha timestamp without time zone, est_ult_mod_usuario character varying(45), est_version integer, rev bigint, revtype smallint);

-- Identificación
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_identificaciones_ide_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_identificaciones (ide_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_identificaciones_ide_pk_seq'::regclass), ide_persona bigint, ide_tipo_documento bigint, ide_pais_emisor bigint, ide_fecha_expedicion date, ide_fecha_vencimiento date, ide_numero_documento CHARACTER VARYING(25), ide_folio CHARACTER VARYING(25), ide_libro CHARACTER VARYING(25), ide_ult_mod_fecha timestamp without time zone, ide_ult_mod_usuario character varying(45), ide_version integer, CONSTRAINT sg_identificaciones_pkey PRIMARY KEY (ide_pk), CONSTRAINT sg_identificaciones_personas_fkey FOREIGN KEY (ide_persona) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_identificaciones_tipo_documento_fkey FOREIGN KEY (ide_tipo_documento) REFERENCES catalogo.sg_tipos_identificacion(tin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_identificaciones_paises_fkey FOREIGN KEY (ide_pais_emisor) REFERENCES catalogo.sg_paises(pai_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT ide_numero_tipo_pais_uk UNIQUE (ide_numero_documento,ide_tipo_documento,ide_pais_emisor));
    COMMENT ON TABLE  centros_educativos.sg_identificaciones                         IS 'Tabla para el registro de los documentos de identificación de una persona.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_persona             IS 'Clave foránea que hace referencia a la persona que pertenece la identificaicón.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_tipo_documento      IS 'Clave foránea que hace referencia al tipo de documento que pertenece la identificación.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_pais_emisor         IS 'Clave foránea que hace referencia al país emisor de la identificación.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_fecha_expedicion    IS 'Fecha de expedición de la identificación.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_fecha_vencimiento   IS 'Fecha de vencimiento de la identificación.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_numero_documento    IS 'Número de la identificación.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_folio               IS 'En el caso que sea partida de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_libro               IS 'En el caso que sea partida de nacimiento.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_identificaciones.ide_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_identificaciones_aud (ide_pk bigint, ide_persona bigint, ide_tipo_documento bigint, ide_pais_emisor bigint, ide_fecha_expedicion date, ide_fecha_vencimiento date, ide_numero_documento CHARACTER VARYING(25), ide_folio CHARACTER VARYING(25), ide_libro CHARACTER VARYING(25), ide_ult_mod_fecha timestamp without time zone, ide_ult_mod_usuario character varying(45), ide_version integer, rev bigint, revtype smallint);

-- Familiares
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_familiares_fam_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_familiares (fam_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_familiares_fam_pk_seq'::regclass), fam_persona bigint, fam_estudiante bigint, fam_tipo_parentesco bigint, fam_habilitado boolean, fam_ult_mod_fecha timestamp without time zone, fam_ult_mod_usuario character varying(45), fam_version integer, CONSTRAINT sg_familiares_pkey PRIMARY KEY (fam_pk), CONSTRAINT sg_familiares_personas_fkey FOREIGN KEY (fam_persona) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_familiares_estudiante_fkey FOREIGN KEY (fam_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_familiares_tipo_parentesco_fkey FOREIGN KEY (fam_tipo_parentesco) REFERENCES catalogo.sg_tipos_parentesco(tpa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_familiares                         IS 'Tabla para el registro de los familiares del estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_persona             IS 'Clave foránea que hace referencia a la persona que pertenece el familiar.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_estudiante          IS 'Clave foránea que hace referencia al estudiante que pertenece el familiar.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_tipo_parentesco     IS 'Clave foránea que hace referencia al tipo de parentesco.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_habilitado          IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_familiares.fam_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_familiares_aud (fam_pk bigint NOT NULL, fam_persona bigint, fam_estudiante bigint, fam_tipo_parentesco bigint, fam_habilitado boolean, fam_ult_mod_fecha timestamp without time zone, fam_ult_mod_usuario character varying(45), fam_version integer, rev bigint, revtype smallint);

-- Contacto emergencia
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_contactos_emergencia_cem_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_contactos_emergencia (cem_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_contactos_emergencia_cem_pk_seq'::regclass), cem_persona bigint, cem_estudiante bigint, cem_ult_mod_fecha timestamp without time zone, cem_ult_mod_usuario character varying(45), cem_version integer, CONSTRAINT sg_contactos_emergencia_pkey PRIMARY KEY (cem_pk), CONSTRAINT sg_contactos_emergencia_personas_fkey FOREIGN KEY (cem_persona) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_contactos_emergencia_estudiantes_fkey FOREIGN KEY (cem_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_contactos_emergencia                         IS 'Tabla para el registro de los contactos de emergencia del estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_persona             IS 'Clave foránea que hace referencia a la persona.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_estudiante          IS 'Clave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_contactos_emergencia.cem_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_contactos_emergencia_aud (cem_pk bigint NOT NULL, cem_persona bigint, cem_estudiante bigint, cem_ult_mod_fecha timestamp without time zone, cem_ult_mod_usuario character varying(45), cem_version integer, rev bigint, revtype smallint);

-- Fichas de Salud
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_fichas_salud_fsa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_fichas_salud (fsa_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_fichas_salud_fsa_pk_seq'::regclass), fsa_estudiante bigint, fsa_vacunas_completo character varying(50), fsa_antecedente_parto character varying(50),fsa_lactancia boolean,fsa_parto_prematuro boolean,fsa_edad_gestacional  character varying(50),fsa_es_alergico_medicamento boolean,fsa_alergico_medicamentos  character varying(255),fsa_es_alergico_alimento boolean,fsa_alergico_alimentos  character varying(255),fsa_medicamento_prescriptor  character varying(500),fsa_enfermedades_padece  character varying(500),fsa_medicamento_permanente  character varying(500),fsa_situaciones_desencadenantes  character varying(500),fsa_manifestaciones_fisicas_psicologicas  character varying(500),fsa_tiempos_comida  character varying(40),fsa_consume_frutas_verduras boolean,fsa_frutas_verduras_dia character varying(40),fsa_consume_agua boolean,fsa_agua_vasos  character varying(40),fsa_tiempo_tv numeric(10,2),fsa_tiempo_tareas numeric(10,2),fsa_tiempo_dormir numeric(10,2),fsa_tiempo_familia numeric(10,2),fsa_tiempo_recreacion numeric(10,2),fsa_tiempo_ejercicio numeric(10,2),fsa_tipo_actividad_fisica character varying(100),fsa_comentarios  character varying(255), fsa_ult_mod_fecha timestamp without time zone, fsa_ult_mod_usuario character varying(45), fsa_version integer,CONSTRAINT sg_fichas_salud_pkey PRIMARY KEY (fsa_pk), CONSTRAINT sg_fichas_salud_estudiante_fkey FOREIGN KEY (fsa_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_fichas_salud                         IS 'Tabla para el registro de los telefonos.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_estudiante          IS 'Clave foranea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_vacunas_completo    IS 'Esquema de vacunación completo a la edad si(true) no (false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_antecedente_parto   IS 'Antecedentes del parto.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_lactancia           IS 'Lactancia materna exclusiva.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_parto_prematuro       IS 'Parto prematuro si(true) no(false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_edad_gestacional      IS 'Edad gestacional al terminar el embarazo.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_es_alergico_medicamento       IS 'Es alérgico a algún medicamneto si(true) no(false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_alergico_medicamentos      IS 'Medicamentos al que es alérgico.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_es_alergico_alimento          IS 'Es alérgico a algún alimento si(true) no(false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_alergico_alimentos         IS 'Alimento al que es alérgico.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_medicamento_prescriptor     IS 'Medicamentos prescritos que deben asegurarse en el centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_enfermedades_padece               IS 'Enfermedades o alergias diagnosticadas que padece.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_medicamento_permanente     IS 'Medicamento prescrito de carácter permanente.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_situaciones_desencadenantes            IS 'Situaciones significativas desencadenantes que afectan el proceso educativo.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_manifestaciones_fisicas_psicologicas   IS 'Manifestaciones fiscias y psicologicas observadas que afectan su desarrollo y aprendizaje.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempos_comida               IS 'Tiempos de comida al día.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_consume_frutas_verduras    IS 'Identifica si consume frutas y verduras si(true) no(false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_frutas_verduras_dia        IS 'Cuantas veces al día consume frutas y verduras.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_consume_agua               IS 'Identifica si consume agua al día si(true) no(false).';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_agua_vasos                 IS 'Cuantos vasos de agua ingiere al día.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_tv            IS 'Cantidad de horas frente a la pantalla.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_tareas              IS 'Cantidad de horas dedicado a las tareas.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_dormir                IS 'Cantidad de horas que duerme en el día.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_familia             IS 'Cantidad de horas dedicado a compartir en familia.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_recreacion          IS 'Cantidad de horas de recreacion.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tiempo_ejercicio           IS 'Tiempo de ejercicio o actividad fisica al día.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_tipo_actividad_fisica      IS 'Tipo de actividad fisica que hace.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_comentarios                IS 'Observaciones/Comentarios.';

    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_fichas_salud.fsa_version             IS 'Versión del teléfono.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_fichas_salud_aud (fsa_pk bigint NOT NULL, fsa_estudiante bigint, fsa_vacunas_completo character varying(50), fsa_antecedente_parto character varying(50),fsa_lactancia boolean,fsa_parto_prematuro boolean,fsa_edad_gestacional  character varying(50),fsa_es_alergico_medicamento boolean,fsa_alergico_medicamentos  character varying(255),fsa_es_alergico_alimento boolean,fsa_alergico_alimentos  character varying(255),fsa_medicamento_prescriptor  character varying(500),fsa_enfermedades_padece  character varying(500),fsa_medicamento_permanente  character varying(500),fsa_situaciones_desencadenantes  character varying(500),fsa_manifestaciones_fisicas_psicologicas  character varying(500),fsa_tiempos_comida  character varying(40),fsa_consume_frutas_verduras boolean,fsa_frutas_verduras_dia character varying(40),fsa_consume_agua boolean,fsa_agua_vasos  character varying(40),fsa_tiempo_tv numeric(10,2),fsa_tiempo_tareas numeric(10,2),fsa_tiempo_dormir numeric(10,2),fsa_tiempo_familia numeric(10,2),fsa_tiempo_recreacion numeric(10,2),fsa_tiempo_ejercicio numeric(10,2),fsa_tipo_actividad_fisica character varying(100),fsa_comentarios  character varying(255), fsa_ult_mod_fecha timestamp without time zone, fsa_ult_mod_usuario character varying(45), fsa_version integer, rev bigint, revtype smallint);



-- Manifestacion Violencia
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_manifestacion_violencia_mvi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_manifestacion_violencia (mvi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_manifestacion_violencia_mvi_pk_seq'::regclass), mvi_estudiante bigint, mvi_tipo_manifestacion bigint, mvi_fecha date, mvi_observaciones CHARACTER VARYING(255), mvi_tratamiento CHARACTER VARYING(255), mvi_ult_mod_fecha timestamp without time zone, mvi_ult_mod_usuario character varying(45), mvi_version integer, CONSTRAINT sg_manifestacion_violencia_pkey PRIMARY KEY (mvi_pk), CONSTRAINT sg_manifestacion_violencia_estudiante_fkey FOREIGN KEY (mvi_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_manifestacion_violencia_tipo_manifestacion_fkey FOREIGN KEY (mvi_tipo_manifestacion) REFERENCES catalogo.sg_tipos_manifestacion(tma_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_manifestacion_violencia                         IS 'Tabla para el registro de las manifestaciones de violencia.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_estudiante          IS 'Clave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_tipo_manifestacion  IS 'Clave foránea que hace referencia al tipo de manifestación.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_fecha               IS 'Fecha de la manifestación de violencia.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_observaciones       IS 'Observaciones.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_tratamiento         IS 'Tratamiento.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_manifestacion_violencia_aud (mvi_pk bigint NOT NULL, mvi_estudiante bigint, mvi_tipo_manifestacion bigint, mvi_fecha date, mvi_observaciones CHARACTER VARYING(255), mvi_tratamiento CHARACTER VARYING(255), mvi_ult_mod_fecha timestamp without time zone, mvi_ult_mod_usuario character varying(45), mvi_version integer, rev bigint, revtype smallint);

--RelacionModalidadEducativaModalidadAtencion
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_mod_ed_mod_aten_rea_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_mod_ed_mod_aten (rea_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_mod_ed_mod_aten_rea_pk_seq'::regclass), rea_modalidad_educativa_fk bigint, rea_modalidad_atencion_fk bigint, rea_sub_modalidad_atencion_fk bigint null, rea_habilitado boolean, rea_ult_mod_fecha timestamp without time zone, rea_ult_mod_usuario character varying(45), rea_version integer, CONSTRAINT sg_rel_mod_ed_mod_aten_pkey PRIMARY KEY (rea_pk));
    COMMENT ON TABLE  centros_educativos.sg_rel_mod_ed_mod_aten                         IS 'Tabla para el registro de la relación entre modalidad educativa y modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_modalidad_educativa_fk          IS 'Clave foránea que hace referencia a la modalidad educativa.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_modalidad_atencion_fk  IS 'Clave foránea que hace referencia a la modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_sub_modalidad_atencion_fk               IS 'Clave foránea que hace referencia a la sub modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_habilitado         IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_mod_ed_mod_aten_aud (rea_pk bigint NOT NULL, rea_modalidad_educativa_fk bigint, rea_modalidad_atencion_fk bigint, rea_sub_modalidad_atencion_fk bigint null, rea_habilitado boolean, rea_ult_mod_fecha timestamp without time zone, rea_ult_mod_usuario character varying(45), rea_version integer, rev bigint, revtype smallint);

-- Grado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_grados_gra_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_grados (gra_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_grados_gra_pk_seq'::regclass), gra_codigo character varying(10), gra_nombre character varying(255), gra_descripcion character varying(500), gra_relacion_modalidad_fk bigint, gra_orden int, gra_habilitado boolean, gra_ult_mod_fecha timestamp without time zone, gra_ult_mod_usuario character varying(45), gra_version integer, CONSTRAINT sg_grados_pkey PRIMARY KEY (gra_pk), CONSTRAINT gra_codigo_uk UNIQUE (gra_codigo),CONSTRAINT sg_grado_relacion_ed_atencion_fkey FOREIGN KEY (gra_relacion_modalidad_fk) REFERENCES centros_educativos.sg_rel_mod_ed_mod_aten(rea_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_grados IS 'Tabla para el registro de los grados.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_relacion_modalidad_fk IS 'Clave foránea que hace referencia a la relación entre modalidad educativa y modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_orden IS 'Orden del registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_grados.gra_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_grados_aud (gra_pk bigint NOT NULL, gra_codigo character varying(10), gra_nombre character varying(255), gra_descripcion character varying(500), gra_relacion_modalidad_fk bigint, gra_orden int, gra_habilitado boolean, gra_ult_mod_fecha timestamp without time zone, gra_ult_mod_usuario character varying(45), gra_version integer, rev bigint, revtype smallint);

-- PlanEstudio
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_planes_estudio_pes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_planes_estudio (pes_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_planes_estudio_pes_pk_seq'::regclass),pes_opcion_fk bigint,pes_programa_educativo_fk bigint, pes_codigo character varying(15), pes_nombre character varying(255),pes_nombre_busqueda character varying(255), pes_descripcion CHARACTER VARYING(500), pes_vigente boolean, pes_relacion_modalidad_fk bigint,pes_ult_mod_fecha timestamp without time zone, pes_ult_mod_usuario character varying(45), pes_version integer, CONSTRAINT sg_planes_estudio_pkey PRIMARY KEY (pes_pk), CONSTRAINT pes_codigo_uk UNIQUE (pes_codigo),CONSTRAINT sg_planes_estudio_relacion_ed_atencion_fkey FOREIGN KEY (pes_relacion_modalidad_fk) REFERENCES centros_educativos.sg_rel_mod_ed_mod_aten(rea_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_pes_opcion_fkey FOREIGN KEY (pes_opcion_fk) REFERENCES centros_educativos.sg_opciones(opc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_pes_programa_educativo_fkey FOREIGN KEY (pes_programa_educativo_fk) REFERENCES catalogo.sg_programas_educativos(ped_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_planes_estudio IS 'Tabla para el registro de los planes de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_opcion_fk IS 'Opcion del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_programa_educativo_fk IS 'Programa Educativo del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_codigo IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_nombre_busqueda IS 'Nombre busqueda del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_vigente IS 'Indica si el registro se encuentra vigente(true) o no(false).';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_relacion_modalidad_fk IS 'Clave foránea que hace referencia a la relación entre modalidad educativa y modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_planes_estudio.pes_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_planes_estudio_aud (pes_pk bigint NOT NULL,pes_opcion_fk bigint,pes_programa_educativo_fk bigint,pes_codigo character varying(15), pes_nombre character varying(255),pes_nombre_busqueda character varying(255),  pes_descripcion CHARACTER VARYING(500),pes_vigente boolean, pes_relacion_modalidad_fk bigint,pes_ult_mod_fecha timestamp without time zone, pes_ult_mod_usuario character varying(45), pes_version integer , rev bigint, revtype smallint);

-- ComponentePlanEstudio
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_componente_plan_estudio_cpe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componente_plan_estudio (cpe_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_componente_plan_estudio_cpe_pk_seq'::regclass), cpe_codigo CHARACTER VARYING(10), cpe_nombre CHARACTER VARYING(255),cpe_nombre_busqueda CHARACTER VARYING(255), cpe_nombre_publicable CHARACTER VARYING(255), cpe_descripcion CHARACTER VARYING(500), cpe_contenido_tematico CHARACTER VARYING(4000), cpe_tipo CHARACTER VARYING(25),cpe_habilitado BOOLEAN, cpe_ult_mod_fecha timestamp without time zone, cpe_ult_mod_usuario character varying(45), cpe_version integer, CONSTRAINT sg_componente_plan_estudio_pkey PRIMARY KEY (cpe_pk),CONSTRAINT cpe_codigo_uk UNIQUE (cpe_codigo)) ;
    COMMENT ON TABLE  centros_educativos.sg_componente_plan_estudio                         IS 'Tabla para el registro de los componentes del plan de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_codigo          IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_nombre         IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_nombre_busqueda         IS 'Nombre busqueda del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_nombre_publicable               IS 'Nombre publicable del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_descripcion        IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_contenido_tematico IS 'Contenido temático del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_tipo IS 'Tipo de registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_habilitado         IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_estudio.cpe_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componente_plan_estudio_aud (cpe_pk bigint NOT NULL, cpe_codigo CHARACTER VARYING(10), cpe_nombre CHARACTER VARYING(255),cpe_nombre_busqueda CHARACTER VARYING(255), cpe_nombre_publicable CHARACTER VARYING(255), cpe_descripcion CHARACTER VARYING(500), cpe_contenido_tematico CHARACTER VARYING(4000), cpe_tipo CHARACTER VARYING(25),cpe_habilitado BOOLEAN, cpe_ult_mod_fecha timestamp without time zone, cpe_ult_mod_usuario character varying(45), cpe_version integer, rev bigint, revtype smallint);

-- ComponentePlanGrado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_componente_plan_grado_cpg_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componente_plan_grado (cpg_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_componente_plan_grado_cpg_pk_seq'::regclass), cpg_plan_estudio_fk bigint,cpg_componente_plan_estudio_fk bigint,cpg_grado_fk bigint,cpg_programa_educativo_fk bigint, cpg_opcion_fk bigint, cpg_objeto_promocion boolean, cpg_escala_calificacion_fk bigint,cpg_nombre_publicable CHARACTER VARYING(255),cpg_cantidad_horas_semanales int,cpg_codigo_sirai bigint,cpg_requiere_nie BOOLEAN, cpg_periodos_calificacion integer, cpg_cantidad_primera_prueba integer, cpg_cantidad_primera_suficiencia integer, cpg_cantidad_segunda_prueba integer, cpg_cantidad_segunda_suficiencia integer, cpg_ult_mod_fecha timestamp without time zone, cpg_ult_mod_usuario character varying(45), cpg_version integer, CONSTRAINT sg_componente_plan_grado_pkey PRIMARY KEY (cpg_pk), CONSTRAINT sg_componente_plan_grado_plan_estudio_fkey FOREIGN KEY (cpg_plan_estudio_fk) REFERENCES centros_educativos.sg_planes_estudio(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_componente_plan_grado_escala_calificacion_fkey FOREIGN KEY (cpg_escala_calificacion_fk) REFERENCES catalogo.sg_escalas_calificacion(eca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_componente_plan_grado_grado_fkey FOREIGN KEY (cpg_grado_fk) REFERENCES centros_educativos.sg_grados(gra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_componente_plan_grado_opcion_fkey FOREIGN KEY (cpg_opcion_fk) REFERENCES centros_educativos.sg_opciones(opc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_componente_plan_grado_componente_plan_estudio_fkey FOREIGN KEY (cpg_componente_plan_estudio_fk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_componente_plan_grado_programa_educativo_fkey FOREIGN KEY (cpg_programa_educativo_fk) REFERENCES catalogo.sg_programas_educativos(ped_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_componente_plan_grado IS 'Tabla para el registro de los componentes del plan de en grado.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_plan_estudio_fk IS 'Clave foránea que hace referencia al plan de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_grado_fk IS 'Clave foránea que hace referencia al grado.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_componente_plan_estudio_fk IS 'Clave foránea que hace referencia al Componente Plan Estudio.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_programa_educativo_fk    IS 'Clave foránea que hace referencia al programa educativo.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_opcion_fk    IS 'Clave foránea que hace referencia a opcion.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_objeto_promocion IS 'Objeto de promoción.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_escala_calificacion_fk IS 'Clave foránea que hace referencia a la escala de calificación.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_periodos_calificacion IS 'Periodos de calificación.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_cantidad_horas_semanales IS 'Cantidad horas semanales.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_nombre_publicable IS 'Nombre publicable del registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_codigo_sirai IS 'Codigo sirai.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_requiere_nie IS 'Requiere NIE.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_cantidad_primera_prueba IS 'Cantidad de primera prueba .';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_cantidad_primera_suficiencia IS 'Cantidad de primera prueba por prueba de suficiencia.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_cantidad_segunda_prueba IS 'Cantidad de segunda prueba.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_cantidad_segunda_suficiencia IS 'Cantidad de segunda prueba por prueba de suficiencia.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componente_plan_grado_aud (cpg_pk bigint NOT NULL, cpg_plan_estudio_fk bigint, cpg_objeto_promocion boolean, cpg_escala_calificacion_fk bigint, cpg_componente_plan_estudio_fk bigint,cpg_grado_fk bigint,cpg_programa_educativo_fk bigint,cpg_opcion_fk bigint, cpg_periodos_calificacion integer,cpg_nombre_publicable CHARACTER VARYING(255),cpg_cantidad_horas_semanales int,cpg_codigo_sirai bigint,cpg_requiere_nie BOOLEAN, cpg_cantidad_primera_prueba integer, cpg_cantidad_primera_suficiencia integer, cpg_cantidad_segunda_prueba integer, cpg_cantidad_segunda_suficiencia integer, cpg_ult_mod_fecha timestamp without time zone, cpg_ult_mod_usuario character varying(45), cpg_version integer, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignaturas (cpe_pk bigint NOT NULL,asig_area_asignatura_modulo_fk bigint,CONSTRAINT sg_asignaturas_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_asignaturas_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk),CONSTRAINT sg_asig_area_asignatura_modulo_fk FOREIGN KEY (asig_area_asignatura_modulo_fk) REFERENCES catalogo.sg_areas_asignatura_modulo(aam_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE) ;
    COMMENT ON TABLE  centros_educativos.sg_asignaturas        IS 'Tabla para el registro de las asignaturas.';
    COMMENT ON COLUMN centros_educativos.sg_asignaturas.cpe_pk IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asignaturas_aud (cpe_pk bigint NOT NULL,asig_area_asignatura_modulo_fk bigint, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_areas (cpe_pk bigint NOT NULL,ind_area_indicador_fk bigint,CONSTRAINT sg_areas_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_areas_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk),CONSTRAINT sg_ind_area_indicador_fk FOREIGN KEY (ind_area_indicador_fk) REFERENCES catalogo.sg_areas_indicador(ari_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE) ;
    COMMENT ON TABLE  centros_educativos.sg_areas         IS 'Tabla para el registro de las área.';
    COMMENT ON COLUMN centros_educativos.sg_areas.cpe_pk  IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_areas_aud (cpe_pk bigint NOT NULL,ind_area_indicador_fk bigint, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulos (cpe_pk bigint NOT NULL, mod_area_asignatura_modulo_fk bigint,CONSTRAINT sg_modulos_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_modulos_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk), CONSTRAINT sg_mod_area_asignatura_modulo_fk FOREIGN KEY (mod_area_asignatura_modulo_fk) REFERENCES catalogo.sg_areas_asignatura_modulo(aam_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE) ;
    COMMENT ON TABLE  centros_educativos.sg_modulos        IS 'Tabla para el registro de los módulos.';
    COMMENT ON COLUMN centros_educativos.sg_modulos.cpe_pk IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulos_aud (cpe_pk bigint NOT NULL,mod_area_asignatura_modulo_fk bigint, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_especial (cpe_pk bigint NOT NULL,CONSTRAINT sg_actividades_especial_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_actividad_especial_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_actividades_especial    IS 'Tabla para el registro de actividad especial.';
    COMMENT ON COLUMN centros_educativos.sg_actividades_especial.cpe_pk     IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_especial_aud (cpe_pk bigint NOT NULL, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_pruebas (cpe_pk bigint NOT NULL,CONSTRAINT sg_pruebas_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_pruebas_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_pruebas            IS 'Tabla para el registro de las pruebas.';
    COMMENT ON COLUMN centros_educativos.sg_pruebas.cpe_pk     IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_pruebas_aud (cpe_pk bigint NOT NULL, rev bigint, revtype smallint);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_tiempo_extendido (cpe_pk bigint NOT NULL,CONSTRAINT sg_actividades_tiempo_extendido_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_actividades_te_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_actividades_tiempo_extendido         IS 'Tabla para el registro de las actividades de tiempo extendido.';
    COMMENT ON COLUMN centros_educativos.sg_actividades_tiempo_extendido.cpe_pk  IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_tiempo_extendido_aud (cpe_pk bigint NOT NULL, rev bigint, revtype smallint);



CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_pk_idx ON centros_educativos.sg_componente_plan_estudio USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_asignatura_pk_idx ON centros_educativos.sg_asignaturas USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_areas_pk_idx ON centros_educativos.sg_areas USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_modulos_pk_idx ON centros_educativos.sg_modulos USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_actividad_especial_pk_idx ON centros_educativos.sg_actividades_especial USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_pruebas_pk_idx ON centros_educativos.sg_pruebas USING btree (cpe_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_estudio_actividad_tiempo_ext_pk_idx ON centros_educativos.sg_actividades_tiempo_extendido USING btree (cpe_pk) ;



--RelacionOpcionProgramaEducativo
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_opcion_prog_ed_roe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_opcion_prog_ed (roe_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_opcion_prog_ed_roe_pk_seq'::regclass), roe_opcion_fk bigint, roe_programa_educativo_fk bigint, roe_habilitado boolean,roe_ult_mod_fecha timestamp without time zone, roe_ult_mod_usuario character varying(45), roe_version integer, CONSTRAINT sg_rel_opcion_prog_ed_pkey PRIMARY KEY (roe_pk), CONSTRAINT sg_rel_opcion_prog_ed_opcion_fkey FOREIGN KEY (roe_opcion_fk) REFERENCES centros_educativos.sg_opciones(opc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_rel_opcion_prog_ed_peduactivo_fkey FOREIGN KEY (roe_programa_educativo_fk) REFERENCES catalogo.sg_programas_educativos(ped_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_rel_opcion_prog_ed                              IS 'Tabla para el registro de la relación entre opción y programa educativo.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_pk                       IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_opcion_fk                IS 'Clave foránea que hace referencia a la opción.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_programa_educativo_fk    IS 'Clave foránea que hace referencia al programa educativo.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_habilitado               IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_ult_mod_fecha            IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_ult_mod_usuario          IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_opcion_prog_ed.roe_version                  IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_opcion_prog_ed_aud (roe_pk bigint NOT NULL, roe_opcion_fk bigint, roe_programa_educativo_fk bigint, roe_habilitado boolean,roe_ult_mod_fecha timestamp without time zone, roe_ult_mod_usuario character varying(45), roe_version integer, rev bigint, revtype smallint);

-- Diplomado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_diplomados_dip_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomados (dip_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_diplomados_dip_pk_seq'::regclass), dip_codigo CHARACTER VARYING(10), dip_nombre CHARACTER VARYING(255),dip_nombre_busqueda CHARACTER VARYING(255), dip_descripcion CHARACTER VARYING(500), dip_habilitado BOOLEAN, dip_ult_mod_fecha timestamp without time zone, dip_ult_mod_usuario character varying(45), dip_version integer, CONSTRAINT sg_diplomados_pkey PRIMARY KEY (dip_pk), CONSTRAINT dip_codigo_uk UNIQUE (dip_codigo)) ;
    COMMENT ON TABLE  centros_educativos.sg_diplomados                         IS 'Tabla para el registro de los diplomados.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_codigo              IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_nombre              IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_nombre_busqueda     IS 'Nombre busqueda del registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_descripcion         IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_habilitado          IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_diplomados.dip_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_diplomados_aud (dip_pk bigint NOT NULL, dip_codigo CHARACTER VARYING(10), dip_nombre CHARACTER VARYING(255),dip_nombre_busqueda CHARACTER VARYING(255),  dip_descripcion CHARACTER VARYING(500), dip_habilitado BOOLEAN, dip_ult_mod_fecha timestamp without time zone, dip_ult_mod_usuario character varying(45), dip_version integer, rev bigint, revtype smallint);


-- ModuloDiplomado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_modulos_diplomado_dip_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulos_diplomado (mdi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_modulos_diplomado_dip_pk_seq'::regclass), mdi_codigo CHARACTER VARYING(10), mdi_nombre CHARACTER VARYING(255), mdi_descripcion CHARACTER VARYING(500), mdi_diplomado_fk bigint,mdi_ult_mod_fecha timestamp without time zone, mdi_ult_mod_usuario character varying(45), mdi_version integer, CONSTRAINT sg_modulos_diplomado_pkey PRIMARY KEY (mdi_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_modulos_diplomado                         IS 'Tabla para el registro de los modulos de los diplomados.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_codigo              IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_nombre              IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_descripcion         IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_diplomado_fk         IS 'Clave foránea que hace referencia al diplomado.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulos_diplomado.mdi_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulos_diplomado_aud (mdi_pk bigint NOT NULL, mdi_codigo CHARACTER VARYING(10), mdi_nombre CHARACTER VARYING(255), mdi_descripcion CHARACTER VARYING(500), mdi_diplomado_fk bigint,mdi_ult_mod_fecha timestamp without time zone, mdi_ult_mod_usuario character varying(45), mdi_version integer, rev bigint, revtype smallint);


--  PersonalSedeEducativa 
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_personal_sede_educativa_pse_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personal_sede_educativa (pse_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_personal_sede_educativa_pse_pk_seq'::regclass), pse_codigo CHARACTER VARYING(10), pse_persona_fk bigint, pse_escolaridad_fk bigint, pse_acceso_internet BOOLEAN, pse_acceso_equipo_informatico BOOLEAN, pse_persona_contacto_fk bigint,  pse_afp_fk bigint, pse_pensionado BOOLEAN, pse_nivel_escalafon_fk bigint, pse_categoria_escalafon_fk bigint, pse_tipo CHARACTER VARYING(25), pse_ult_mod_fecha timestamp without time zone, pse_ult_mod_usuario character varying(45), pse_version integer, CONSTRAINT sg_personal_sede_educativa_pkey PRIMARY KEY (pse_pk), CONSTRAINT pse_codigo_uk UNIQUE (pse_codigo),CONSTRAINT sg_personal_sede_educativa_persona_fk FOREIGN KEY (pse_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk),CONSTRAINT sg_personal_sede_educativa_escolaridad_fk FOREIGN KEY (pse_escolaridad_fk) REFERENCES catalogo.sg_escolaridades(esc_pk),CONSTRAINT sg_personal_sede_educativa_persona_contacto_fk FOREIGN KEY (pse_persona_contacto_fk) REFERENCES centros_educativos.sg_personas(per_pk),CONSTRAINT sg_personal_sede_educativa_afp_fk FOREIGN KEY (pse_afp_fk) REFERENCES catalogo.sg_afp(afp_pk),CONSTRAINT sg_personal_sede_educativa_nivel_escalafon_fk FOREIGN KEY (pse_nivel_escalafon_fk) REFERENCES catalogo.sg_nivel_escalafon(nes_pk),CONSTRAINT sg_personal_sede_educativa_categoria_escalafon_fk FOREIGN KEY (pse_categoria_escalafon_fk) REFERENCES catalogo.sg_categoria_escalafon(ces_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_personal_sede_educativa                                 IS 'Tabla para el registro del personal del centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_pk                          IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_codigo                      IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_persona_fk                  IS 'Clave foránea que hace referencia a la persona.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_escolaridad_fk              IS 'Clave foránea que hace referencia a la escolaridad.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_acceso_internet             IS 'Indica si el registro tiene acceso a internet.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_acceso_equipo_informatico   IS 'Indica si el registro tiene equipo de informática.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_persona_contacto_fk         IS 'Clave foránea que hace referencia a la persona de contacto.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_afp_fk                      IS 'Clave foránea que hace referencia a la AFP.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_pensionado                  IS 'Indica si el registro esta pensionado.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_nivel_escalafon_fk          IS 'Clave foránea que hace referencia al nivel de escalafon.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_categoria_escalafon_fk      IS 'Clave foránea que hace referencia a la categoría del escalafon.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_tipo                        IS 'Tipo de registro.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_ult_mod_fecha               IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_ult_mod_usuario             IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_personal_sede_educativa.pse_version                     IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personal_sede_educativa_aud (pse_pk bigint NOT NULL, pse_codigo CHARACTER VARYING(10), pse_persona_fk bigint, pse_escolaridad_fk bigint, pse_acceso_internet BOOLEAN, pse_acceso_equipo_informatico BOOLEAN, pse_persona_contacto_fk bigint,  pse_afp_fk bigint, pse_pensionado BOOLEAN, pse_nivel_escalafon_fk bigint, pse_categoria_escalafon_fk bigint, pse_tipo CHARACTER VARYING(25), pse_ult_mod_fecha timestamp without time zone, pse_ult_mod_usuario character varying(45), pse_version integer, rev bigint, revtype smallint);

--Docente
CREATE TABLE IF NOT EXISTS centros_educativos.sg_docente (pse_pk bigint NOT NULL, CONSTRAINT sg_docente_pkey PRIMARY KEY (pse_pk), CONSTRAINT sg_docente_personal_sede_fk FOREIGN KEY (pse_pk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_docente        IS 'Tabla para el registro de los docentes.';
    COMMENT ON COLUMN centros_educativos.sg_docente.pse_pk IS 'Clave foránea que hace referencia al personal sede educativa.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_docente_aud (pse_pk bigint NOT NULL, rev bigint, revtype smallint);

--ATPI
CREATE TABLE IF NOT EXISTS centros_educativos.sg_atpi (pse_pk bigint NOT NULL, CONSTRAINT sg_atpi_pkey PRIMARY KEY (pse_pk), CONSTRAINT sg_atpi_personal_sede_fk FOREIGN KEY (pse_pk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_atpi        IS 'Tabla para el registro de ATPI.';
    COMMENT ON COLUMN centros_educativos.sg_atpi.pse_pk IS 'Clave foránea que hace referencia al personal sede educativa.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_atpi_aud (pse_pk bigint NOT NULL, rev bigint, revtype smallint);

--Administrativo
CREATE TABLE IF NOT EXISTS centros_educativos.sg_administrativo (pse_pk bigint NOT NULL, CONSTRAINT sg_administrativo_pkey PRIMARY KEY (pse_pk), CONSTRAINT sg_administrativo_personal_sede_fk FOREIGN KEY (pse_pk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_administrativo        IS 'Tabla para el registro de los administrativos.';
    COMMENT ON COLUMN centros_educativos.sg_administrativo.pse_pk IS 'Clave foránea que hace referencia al personal sede educativa.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_administrativo_aud (pse_pk bigint NOT NULL, rev bigint, revtype smallint);


CREATE UNIQUE INDEX IF NOT EXISTS sg_personal_sede_educativa_pk_idx                     ON centros_educativos.sg_personal_sede_educativa    USING btree (pse_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_personal_sede_educativa_docente_pk_idx             ON centros_educativos.sg_docente                    USING btree (pse_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_personal_sede_educativa_atpi_pk_idx                ON centros_educativos.sg_atpi                       USING btree (pse_pk) ;
CREATE UNIQUE INDEX IF NOT EXISTS sg_personal_sede_educativa_administrativo_pk_idx      ON centros_educativos.sg_administrativo             USING btree (pse_pk) ;


-- Servicio educativo
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_servicio_educativo_sdu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_servicio_educativo (sdu_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_servicio_educativo_sdu_pk_seq'::regclass),sdu_opcion_fk bigint,sdu_programa_educativo_fk bigint, sdu_grado_fk bigint, sdu_estado CHARACTER VARYING(25), sdu_fecha_habilitado date, sdu_numero_tramite CHARACTER VARYING(20), sdu_sede_fk bigint, sdu_ult_mod_fecha timestamp without time zone, sdu_ult_mod_usuario character varying(45), sdu_version integer, CONSTRAINT sg_servicio_educativo_pkey PRIMARY KEY (sdu_pk),CONSTRAINT sg_servicio_educativo_grado_fk FOREIGN KEY (sdu_grado_fk) REFERENCES centros_educativos.sg_grados(gra_pk),CONSTRAINT sg_servicio_educativo_sede_fk FOREIGN KEY (sdu_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),CONSTRAINT sg_sdu_opcion_fkey FOREIGN KEY (sdu_opcion_fk) REFERENCES centros_educativos.sg_opciones(opc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sdu_programa_educativo_fkey FOREIGN KEY (sdu_programa_educativo_fk) REFERENCES catalogo.sg_programas_educativos(ped_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE) ;
    COMMENT ON TABLE  centros_educativos.sg_servicio_educativo                          IS 'Tabla para el registro del servicio educativo.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_opcion_fk            IS 'Opcion del registro.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_programa_educativo_fk IS 'Programa Educativo del registro.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_grado_fk             IS 'Clave foránea que hace referencia al grado.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_estado               IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_fecha_habilitado     IS 'Fecha de habilitación.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_numero_tramite       IS 'Número de tramite.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_sede_fk              IS 'Clave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_servicio_educativo_aud (sdu_pk bigint NOT NULL,sdu_opcion_fk bigint,sdu_programa_educativo_fk bigint, sdu_grado_fk bigint, sdu_estado CHARACTER VARYING(25), sdu_fecha_habilitado date, sdu_numero_tramite CHARACTER VARYING(20), sdu_sede_fk bigint, sdu_ult_mod_fecha timestamp without time zone, sdu_ult_mod_usuario character varying(45), sdu_version integer, rev bigint, revtype smallint);


-- Año lectivo
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_anio_lectivo_ale_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_anio_lectivo (ale_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_anio_lectivo_ale_pk_seq'::regclass), ale_anio integer, ale_descripcion CHARACTER VARYING(255), ale_estado CHARACTER VARYING(20), ale_ult_mod_fecha timestamp without time zone, ale_ult_mod_usuario character varying(45), ale_version integer, CONSTRAINT sg_anio_lectivo_pkey PRIMARY KEY (ale_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_anio_lectivo                          IS 'Tabla para el registro del año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_pk                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_anio                 IS 'Año del registro.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_descripcion          IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_estado               IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_anio_lectivo.ale_version              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_anio_lectivo_aud (ale_pk bigint NOT NULL, ale_anio integer, ale_descripcion CHARACTER VARYING(255), ale_estado CHARACTER VARYING(20), ale_ult_mod_fecha timestamp without time zone, ale_ult_mod_usuario character varying(45), ale_version integer, rev bigint, revtype smallint);


-- Secciones
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_secciones_sec_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_secciones(sec_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_secciones_sec_pk_seq'::regclass), sec_codigo CHARACTER VARYING(10), sec_nombre CHARACTER VARYING(255), sec_estado CHARACTER VARYING(50),sec_jornada_fk bigint,sec_plan_estudio_fk bigint, sec_anio_lectivo_fk bigint, sec_servicio_educativo_fk bigint, sec_ult_mod_fecha timestamp without TIME zone, sec_ult_mod_usuario CHARACTER varying(45), sec_version INTEGER, CONSTRAINT sg_secciones_pkey PRIMARY KEY (sec_pk), CONSTRAINT sg_secciones_anio_lectivo_fkey FOREIGN KEY (sec_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_secciones_servicio_edu_fkey FOREIGN KEY (sec_servicio_educativo_fk) REFERENCES centros_educativos.sg_servicio_educativo(sdu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sec_plan_estudio_fk FOREIGN KEY (sec_plan_estudio_fk) REFERENCES centros_educativos.sg_planes_estudio(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sec_jornada_fkey FOREIGN KEY (sec_jornada_fk) REFERENCES catalogo.sg_jornadas_laborales(jla_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_secciones                           IS 'Tabla para almacenar las secciones.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_codigo                IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_nombre                IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_estado                IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_plan_estudio_fk       IS 'Clave foránea que hace referencia a la tabla de plan estudio.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_anio_lectivo_fk       IS 'Clave foránea que hace referencia a la tabla de año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_servicio_educativo_fk IS 'Clave foránea que hace referencia a la tabla de servicio educativo.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_secciones.sec_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_secciones_aud(sec_pk bigint, sec_codigo CHARACTER VARYING(10), sec_nombre CHARACTER VARYING(255), sec_estado CHARACTER VARYING(50),sec_jornada_fk bigint,sec_plan_estudio_fk bigint, sec_anio_lectivo_fk bigint, sec_servicio_educativo_fk bigint, sec_ult_mod_fecha timestamp without TIME zone, sec_ult_mod_usuario CHARACTER varying(45), sec_version INTEGER,rev bigint, revtype smallint);

CREATE INDEX IF NOT EXISTS sg_secciones_anio_lectivo_idx ON centros_educativos.sg_secciones USING btree (sec_anio_lectivo_fk);

-- Control de asistencia cabezal
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_control_asistencia_cabezal_cac_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_control_asistencia_cabezal (cac_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_control_asistencia_cabezal_cac_pk_seq'::regclass), cac_seccion_fk bigint, cac_fecha date, cac_ult_mod_fecha timestamp without time zone, cac_ult_mod_usuario character varying(45), cac_version integer, CONSTRAINT sg_control_asistencia_cabezal_pkey PRIMARY KEY (cac_pk), CONSTRAINT sg_control_asistencia_cabezal_seccion_fkey FOREIGN KEY (cac_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT cac_fecha_seccion_uk UNIQUE (cac_fecha,cac_seccion_fk));
    COMMENT ON TABLE  centros_educativos.sg_control_asistencia_cabezal                         IS 'Tabla para el registro de los controles de asistencia por sección.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_seccion_fk          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_fecha               IS 'Fecha del registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_control_asistencia_cabezal_aud (cac_pk bigint NOT NULL, cac_seccion_fk bigint, cac_fecha date, cac_ult_mod_fecha timestamp without time zone, cac_ult_mod_usuario character varying(45), cac_version integer, rev bigint, revtype smallint);

-- Asistencia
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_asistencias_asi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asistencias (asi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_asistencias_asi_pk_seq'::regclass), asi_control_fk bigint, asi_estudiante bigint, asi_seccion bigint, asi_fecha date, asi_inasistencia BOOLEAN, asi_motivo_inasistencia bigint, asi_observacion CHARACTER VARYING(255), asi_ult_mod_fecha timestamp without time zone, asi_ult_mod_usuario character varying(45), asi_version integer, CONSTRAINT sg_asistencias_pkey PRIMARY KEY (asi_pk),CONSTRAINT sg_asistencias_control_asistencia_cabezal_fkey FOREIGN KEY (asi_control_fk) REFERENCES centros_educativos.sg_control_asistencia_cabezal(cac_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencias_estudiante_fkey FOREIGN KEY (asi_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencias_motivo_inasistencia_fkey FOREIGN KEY (asi_motivo_inasistencia) REFERENCES catalogo.sg_motivos_inasistencia(min_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_asistencias                         IS 'Tabla para el registro de las inasistencias.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_control_fk          IS 'Clave foránea que hace referencia al control de asistencia cabezal.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_estudiante          IS 'Clave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_seccion         IS 'Clave foránea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_fecha               IS 'Fecha de la asistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_inasistencia        IS 'Indica si es una inasistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_motivo_inasistencia IS 'Clave foránea que hace referencia al tipo de inasistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_observacion         IS 'Observación de la inasistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias.asi_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asistencias_aud (asi_pk bigint NOT NULL, asi_control_fk bigint, asi_estudiante bigint, asi_seccion bigint, asi_fecha date, asi_inasistencia BOOLEAN, asi_motivo_inasistencia bigint, asi_observacion CHARACTER VARYING(255), asi_ult_mod_fecha timestamp without time zone, asi_ult_mod_usuario character varying(45), asi_version integer, rev bigint, revtype smallint);


-- Matricula
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_matriculas_mat_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_matriculas(mat_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_matriculas_mat_pk_seq'::regclass), mat_seccion_fk bigint, mat_estudiante_fk bigint,mat_motivo_retiro_fk bigint, mat_estado CHARACTER VARYING(50),mat_observaciones character varying(500),mat_fecha_hasta DATE, mat_ult_mod_fecha timestamp without TIME zone, mat_ult_mod_usuario CHARACTER varying(45), mat_version INTEGER, CONSTRAINT sg_matriculas_pkey PRIMARY KEY (mat_pk), CONSTRAINT sg_matriculas_seccion_fkey FOREIGN KEY (mat_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_matriculas_estudiante_fkey FOREIGN KEY (mat_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_mat_motivo_retiro_fk FOREIGN KEY (mat_motivo_retiro_fk) REFERENCES catalogo.sg_motivos_retiro(mre_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_matriculas                           IS 'Tabla para almacenar las matriculas.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_seccion_fk            IS 'Clave foránea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_estudiante_fk         IS 'Clave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_estado                IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_fecha_hasta           IS 'Fecha hasta del registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_matriculas_aud(mat_pk bigint, mat_seccion_fk bigint, mat_estudiante_fk bigint,mat_motivo_retiro_fk bigint, mat_estado CHARACTER VARYING(50),mat_observaciones character varying(500),mat_fecha_hasta DATE, mat_ult_mod_fecha timestamp without TIME zone, mat_ult_mod_usuario CHARACTER varying(45), mat_version INTEGER,rev bigint, revtype smallint);


-- Noticias
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_noticias_not_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_noticias(not_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_noticias_not_pk_seq'::regclass), not_codigo CHARACTER VARYING(4), not_contenido CHARACTER VARYING(5000), not_fecha_desde date, not_fecha_hasta date, not_habilitado boolean, not_ult_mod_fecha timestamp without TIME zone, not_ult_mod_usuario CHARACTER varying(45), not_version INTEGER, CONSTRAINT sg_noticias_pkey PRIMARY KEY (not_pk));
    COMMENT ON TABLE  centros_educativos.sg_noticias                            IS 'Tabla para el registro de noticias.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_pk                     IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_codigo                 IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_contenido              IS 'Contenido de la noticia.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_fecha_desde            IS 'Fecha desde.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_fecha_hasta            IS 'Fecha hasta.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_habilitado             IS 'Indica si el registro se enceuntra habilitado(true) ó no(false).';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_ult_mod_fecha          IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_ult_mod_usuario        IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_noticias.not_version                IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_noticias_aud(not_pk bigint, not_codigo CHARACTER VARYING(4), not_contenido CHARACTER VARYING(5000), not_fecha_desde date, not_fecha_hasta date, not_habilitado boolean, not_ult_mod_fecha timestamp without TIME zone, not_ult_mod_usuario CHARACTER varying(45), not_version INTEGER, rev bigint, revtype smallint);

-- Gestion año lectivo
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_gestion_anio_lectivo_gal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_gestion_anio_lectivo(gal_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_gestion_anio_lectivo_gal_pk_seq'::regclass), gal_codigo CHARACTER VARYING(4), gal_anio INTEGER NOT NULL, gal_fecha_desde DATE NOT NULL, gal_fecha_hasta DATE NOT NULL, gal_anio_actual BOOLEAN, gal_habilitado BOOLEAN, gal_ult_mod_fecha timestamp without TIME zone, gal_ult_mod_usuario CHARACTER varying(45), gal_version INTEGER, CONSTRAINT sg_gestion_anio_lectivo_pkey PRIMARY KEY (gal_pk));
    COMMENT ON TABLE  centros_educativos.sg_gestion_anio_lectivo                            IS 'Tabla para el registro de la gestión de años lectivos.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_pk                     IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_codigo                 IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_anio                   IS 'Año del registro.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_fecha_desde            IS 'Fecha desde.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_fecha_hasta            IS 'Fecha hasta.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_anio_actual            IS 'Año actual.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_habilitado             IS 'Indica si el registro se enceuntra habilitado(true) ó no(false).';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_ult_mod_fecha          IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_ult_mod_usuario        IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_gestion_anio_lectivo.gal_version                IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_gestion_anio_lectivo_aud(gal_pk bigint, gal_codigo CHARACTER VARYING(4), gal_anio INTEGER NOT NULL, gal_fecha_desde DATE NOT NULL, gal_fecha_hasta DATE NOT NULL, gal_anio_actual BOOLEAN, gal_habilitado BOOLEAN, gal_ult_mod_fecha timestamp without TIME zone, gal_ult_mod_usuario CHARACTER varying(45), gal_version INTEGER, rev bigint, revtype smallint);



-- Calendario
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_calendario_cal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calendarios(cal_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_calendario_cal_pk_seq'::regclass), cal_anio_lectivo_fk bigint, cal_tipo_calendario_fk bigint, cal_codigo CHARACTER VARYING(4), cal_nombre CHARACTER VARYING(255),cal_nombre_busqueda CHARACTER VARYING(255),  cal_descripcion CHARACTER VARYING(500),cal_habilitado BOOLEAN,cal_fecha_inicio DATE, cal_fecha_fin DATE, cal_ult_mod_fecha timestamp without TIME zone, cal_ult_mod_usuario CHARACTER varying(45), cal_version INTEGER, CONSTRAINT sg_calendarios_pkey PRIMARY KEY (cal_pk),CONSTRAINT sg_cal_anio_lectivo_fkey FOREIGN KEY (cal_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_cal_tipo_calendario_fkey FOREIGN KEY (cal_tipo_calendario_fk) REFERENCES catalogo.sg_tipos_calendario_escolar(tce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_calendarios                            IS 'Tabla para el registro de Calendarios.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_pk                     IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_codigo                 IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_nombre                 IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_nombre_busqueda        IS 'Nombre busqueda del registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_descripcion            IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_habilitado             IS 'Indica si el registro se enceuntra habilitado(true) ó no(false).';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_ult_mod_fecha          IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_ult_mod_usuario        IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_version                IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calendarios_aud(cal_pk bigint,cal_anio_lectivo_fk bigint, cal_tipo_calendario_fk bigint,cal_codigo CHARACTER VARYING(4),cal_nombre CHARACTER VARYING(255),cal_nombre_busqueda CHARACTER VARYING(255), cal_descripcion CHARACTER VARYING(500), cal_habilitado BOOLEAN,cal_fecha_inicio DATE, cal_fecha_fin DATE, cal_ult_mod_fecha timestamp without TIME zone, cal_ult_mod_usuario CHARACTER varying(45), cal_version INTEGER, rev bigint, revtype smallint);

-- Actividades Calendario
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_actividad_calendario_aca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividad_calendario (aca_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_actividad_calendario_aca_pk_seq'::regclass),aca_calendario_fk bigint, aca_tipo CHARACTER VARYING(20), aca_nombre character varying(255), aca_nombre_busqueda character varying(255),aca_descripcion character varying(255),aca_fecha_desde DATE NOT NULL, aca_fecha_hasta DATE NOT NULL,  aca_ult_mod_fecha timestamp without time zone, aca_ult_mod_usuario character varying(45), aca_version integer, CONSTRAINT sg_actividad_calendario_pkey PRIMARY KEY (aca_pk),CONSTRAINT aca_nombre_uk UNIQUE (aca_nombre),CONSTRAINT sg_aca_calendario_fkey FOREIGN KEY (aca_calendario_fk) REFERENCES centros_educativos.sg_calendarios(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_actividad_calendario IS 'Tabla para el registro de actividades calendario.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_descripcion IS 'Descripción del registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_fecha_desde IS 'Fecha inicio del registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_fecha_hasta IS 'Fecha fin del registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividad_calendario_aud (aca_pk bigint NOT NULL,aca_tipo CHARACTER VARYING(20),aca_calendario_fk bigint, aca_nombre character varying(255), aca_nombre_busqueda character varying(255), aca_descripcion character varying(255),aca_fecha_desde DATE NOT NULL, aca_fecha_hasta DATE NOT NULL,aca_ult_mod_fecha timestamp without time zone, aca_ult_mod_usuario character varying(45), aca_version integer, rev bigint, revtype smallint);

-- Periodos calendario
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_periodos_calendario_ces_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_periodos_calendario (ces_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_periodos_calendario_ces_pk_seq'::regclass),ces_nivel_fk bigint,ces_modalidad_atencion_fk bigint,ces_calendario_fk bigint, ces_tipo CHARACTER VARYING(20),ces_fecha_desde DATE NOT NULL,ces_fecha_hasta DATE NOT NULL,ces_ult_mod_fecha timestamp without time zone,ces_ult_mod_usuario character varying(45),ces_version integer,CONSTRAINT sg_periodos_calendario_pkey PRIMARY KEY (ces_pk),CONSTRAINT sg_ces_nivel_fkey FOREIGN KEY (ces_nivel_fk) REFERENCES centros_educativos.sg_niveles(niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE ,CONSTRAINT sg_ces_modalidad_atencion_fkey FOREIGN KEY (ces_modalidad_atencion_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_ces_calendario_fk FOREIGN KEY (ces_calendario_fk) REFERENCES centros_educativos.sg_calendarios(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_periodos_calendario IS 'Tabla para el registro de calendarios escolares.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_nivel_fk IS 'Nivel del registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_fecha_desde IS 'Fecha inicio del registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_fecha_hasta IS 'Fecha fin del registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_modalidad_atencion_fk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_periodos_calendario_aud (ces_pk bigint NOT NULL,ces_nivel_fk bigint,ces_modalidad_atencion_fk bigint,ces_calendario_fk bigint, ces_tipo CHARACTER VARYING(20),ces_fecha_desde DATE NOT NULL,ces_fecha_hasta DATE NOT NULL, ces_ult_mod_fecha timestamp without time zone, ces_ult_mod_usuario character varying(45), ces_version integer, rev bigint, revtype smallint);

-- SG_ESTUDIANTES_VALORACION
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estudiantes_valoracion_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_estudiantes_valoracion( esv_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_estudiantes_valoracion_pk_seq'::regclass), esv_enum_visible CHARACTER VARYING(20) NOT NULL, esv_fecha_publicacion TIMESTAMP NOT NULL, esv_valoracion CHARACTER varying(500) NOT NULL, esv_estudiante_fk bigint NOT NULL, esv_ult_mod_fecha timestamp without TIME zone, esv_ult_mod_usuario CHARACTER varying(45), esv_version INTEGER, CONSTRAINT sg_estudiantes_valoracion_pkey PRIMARY KEY (esv_pk));
COMMENT ON TABLE  centros_educativos.sg_estudiantes_valoracion                          IS 'Tabla para el registro de las valoraciones hechas al estudiante.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_pk                   IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_enum_visible         IS 'Es el tipo de visibilidad que tendara el registro, puede ser público o privado.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_fecha_publicacion    IS 'Fecha en la que se publicó la valoración.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_valoracion           IS 'Campo en el que se almacena la valoración.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_estudiante_fk        IS 'Llave foránea del estudiante al que pertenece la valoración.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_version              IS 'Versión del registro.';
ALTER TABLE centros_educativos.sg_estudiantes_valoracion ADD CONSTRAINT sg_estu_valoracion_estu_fkey FOREIGN KEY (esv_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_estudiantes_valoracion_aud(esv_pk bigint, esv_enum_visible CHARACTER VARYING(20) NOT NULL, esv_fecha_publicacion TIMESTAMP NOT NULL,esv_valoracion CHARACTER varying(500) NOT NULL,esv_estudiante_fk bigint NOT NULL,esv_ult_mod_fecha timestamp without TIME zone, esv_ult_mod_usuario CHARACTER varying(45), esv_version INTEGER, rev bigint, revtype smallint);



-- Movimientos
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_movimientos_mov_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_movimientos (mov_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_movimientos_mov_pk_seq'::regclass), mov_estudiante_fk bigint, mov_grado_fk bigint, mov_seccion_fk bigint, mov_centro_educativo_fk bigint, mov_movimiento CHARACTER VARYING(50), mov_habilitado boolean, mov_ult_mod_fecha timestamp without time zone, mov_ult_mod_usuario character varying(45), mov_version integer, CONSTRAINT sg_movimientos_pkey PRIMARY KEY (mov_pk), CONSTRAINT sg_movimientos_estudiante_fkey FOREIGN KEY (mov_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_movimientos_grado_fkey FOREIGN KEY (mov_grado_fk) REFERENCES centros_educativos.sg_grados(gra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_movimientos_seccion_fkey FOREIGN KEY (mov_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_movimientos_centro_educativo_fkey FOREIGN KEY (mov_centro_educativo_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_movimientos                         IS 'Tabla para el registro de los movimientos del estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_estudiante_fk          IS 'Llave foranea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_grado_fk               IS 'Llave foranea que hace referencia al grado.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_seccion_fk             IS 'Llave foranea que hace referencia a la seccion.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_centro_educativo_fk    IS 'Llave foranea que hace referencia al centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_movimiento              IS 'Enumerado INGRESO,SALIDA,CAMBIO SECCION.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_habilitado              IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_movimientos.mov_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_movimientos_aud (mov_pk bigint NOT NULL, mov_estudiante_fk bigint, mov_grado_fk bigint, mov_seccion_fk bigint, mov_centro_educativo_fk bigint, mov_movimiento CHARACTER VARYING(50), mov_habilitado boolean, mov_ult_mod_fecha timestamp without time zone, mov_ult_mod_usuario character varying(45), mov_version integer, rev bigint, revtype smallint);



-- Periodos Calificacion
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_periodos_calificacion_pca_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_periodos_calificacion (pca_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_periodos_calificacion_pca_pk_seq'::regclass),pca_modalidad_educativa_fk bigint,pca_modalidad_atencion_fk bigint,pca_calendario_fk bigint,pca_nombre character varying(255), pca_numero int,pca_ult_mod_fecha timestamp without time zone,pca_ult_mod_usuario character varying(45),pca_version integer,CONSTRAINT sg_periodos_calificacion_pkey PRIMARY KEY (pca_pk),CONSTRAINT sg_pca_modalidad_educativa_fk FOREIGN KEY (pca_modalidad_educativa_fk) REFERENCES centros_educativos.sg_modalidades(mod_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE ,CONSTRAINT sg_pca_modalidad_atencion_fkey FOREIGN KEY (pca_modalidad_atencion_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_pca_calendario_fk FOREIGN KEY (pca_calendario_fk) REFERENCES centros_educativos.sg_calendarios(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_periodos_calificacion IS 'Tabla para el registro de calendarios escolares.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_modalidad_educativa_fk IS 'Nivel del registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_modalidad_atencion_fk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_periodos_calificacion_aud (pca_pk bigint NOT NULL,pca_modalidad_educativa_fk bigint,pca_modalidad_atencion_fk bigint,pca_calendario_fk bigint,pca_nombre character varying(255), pca_numero int, pca_ult_mod_fecha timestamp without time zone, pca_ult_mod_usuario character varying(45), pca_version integer, rev bigint, revtype smallint);


-- Rangos Fecha
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rango_fecha_rfe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_rango_fecha(rfe_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_rango_fecha_rfe_pk_seq'::regclass),rfe_periodo_calificacion_fk bigint,  rfe_codigo CHARACTER VARYING(4), rfe_fecha_desde DATE NOT NULL, rfe_fecha_hasta DATE NOT NULL,  rfe_habilitado BOOLEAN, rfe_ult_mod_fecha timestamp without TIME zone, rfe_ult_mod_usuario CHARACTER varying(45), rfe_version INTEGER, CONSTRAINT sg_rango_fecha_pkey PRIMARY KEY (rfe_pk),CONSTRAINT sg_rfe_periodo_calificacion_fk FOREIGN KEY (rfe_periodo_calificacion_fk) REFERENCES  centros_educativos.sg_periodos_calificacion(pca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );
    COMMENT ON TABLE  centros_educativos.sg_rango_fecha                            IS 'Tabla para el registro de la gestión de años lectivos.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_pk                     IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_codigo                 IS 'Código del registro.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_fecha_desde            IS 'Fecha desde.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_fecha_hasta            IS 'Fecha hasta.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_habilitado             IS 'Indica si el registro se enceuntra habilitado(true) ó no(false).';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_ult_mod_fecha          IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_ult_mod_usuario        IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rango_fecha.rfe_version                IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_rango_fecha_aud(rfe_pk bigint,rfe_periodo_calificacion_fk bigint,  rfe_codigo CHARACTER VARYING(4), rfe_fecha_desde DATE NOT NULL, rfe_fecha_hasta DATE NOT NULL,  rfe_habilitado BOOLEAN, rfe_ult_mod_fecha timestamp without TIME zone, rfe_ult_mod_usuario CHARACTER varying(45), rfe_version INTEGER, rev bigint, revtype smallint);
-- Horarios Escolares
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_horarios_escolares_hes_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_horarios_escolares (hes_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_horarios_escolares_hes_pk_seq'::regclass),hes_seccion_fk bigint, hes_cantidad int,  hes_ult_mod_fecha timestamp without time zone, hes_ult_mod_usuario character varying(45), hes_version integer, CONSTRAINT sg_horarios_escolares_pkey PRIMARY KEY (hes_pk),CONSTRAINT sg_hes_seccion_fk FOREIGN KEY (hes_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_horarios_escolares IS 'Tabla para el registro de horarios escolares.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_seccion_fk IS 'Seccion de registro.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_cantidad IS 'Cantidad de registro.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_horarios_escolares_aud (hes_pk bigint NOT NULL,hes_seccion_fk bigint,hes_cantidad int,  hes_ult_mod_fecha timestamp without time zone, hes_ult_mod_usuario character varying(45), hes_version integer, rev bigint, revtype smallint);

-- Celdas Día Hora
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_celda_dia_hora_cdh_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_celda_dia_hora (cdh_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_celda_dia_hora_cdh_pk_seq'::regclass),cdh_componente_plan_grado_fk bigint,cdh_horario_escolar_fk bigint,cdh_dia CHARACTER VARYING(20),cdh_hora int,  cdh_ult_mod_fecha timestamp without time zone, cdh_ult_mod_usuario character varying(45), cdh_version integer, CONSTRAINT sg_celda_dia_hora_pkey PRIMARY KEY (cdh_pk),CONSTRAINT sg_cdh_componente_plan_grado_fk FOREIGN KEY (cdh_componente_plan_grado_fk) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_cdh_horario_escolar_fk FOREIGN KEY (cdh_horario_escolar_fk) REFERENCES centros_educativos.sg_horarios_escolares(hes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_celda_dia_hora IS 'Tabla para el registro de celdas día hora.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_componente_plan_grado_fk IS 'Relacion Componente Plan Grado de Registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_horario_escolar_fk IS 'Relacion horario escolar de Registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_dia IS 'RDía de Registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_hora IS 'Hora de Registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora.cdh_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_celda_dia_hora_aud (cdh_pk bigint NOT NULL,cdh_componente_plan_grado_fk bigint,cdh_horario_escolar_fk bigint, cdh_dia CHARACTER VARYING(20),cdh_hora int, cdh_ult_mod_fecha timestamp without time zone, cdh_ult_mod_usuario character varying(45), cdh_version integer, rev bigint, revtype smallint);

-- Datos de empleado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_datos_empleado_dem_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_datos_empleado (dem_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_datos_empleado_dem_pk_seq'::regclass), dem_codigo_empleado character varying(45), dem_estado character varying(50), dem_fecha_ingreso_gob date,dem_fecha_ingreso_mined date,dem_nivel_fk bigint,dem_categoria_fk bigint,dem_ult_mod_fecha timestamp without time zone, dem_ult_mod_usuario character varying(45), dem_version integer, CONSTRAINT sg_datos_empleado_pkey PRIMARY KEY (dem_pk), CONSTRAINT sg_datos_empleado_nivel_fkey FOREIGN KEY (dem_nivel_fk) REFERENCES catalogo.sg_nivel_empleado(nem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_empleado_categoria_fkey FOREIGN KEY (dem_categoria_fk) REFERENCES catalogo.sg_categoria_empleado(cem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_datos_empleado                         IS 'Tabla para el registro de los controles de asistencia por sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_codigo_empleado          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_estado          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_fecha_ingreso_gob          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_fecha_ingreso_mined          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_nivel_fk               IS 'Fecha del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_categoria_fk          IS 'Llave foranea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_datos_empleado_aud (dem_pk bigint NOT NULL, dem_codigo_empleado character varying(45), dem_estado character varying(50), dem_fecha_ingreso_gob date,dem_fecha_ingreso_mined date,dem_nivel_fk bigint,dem_categoria_fk bigint,dem_ult_mod_fecha timestamp without time zone, dem_ult_mod_usuario character varying(45), dem_version integer, rev bigint, revtype smallint);

-- Datos de contratacion
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_datos_contratacion_dco_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_datos_contratacion (dco_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_datos_contratacion_dco_pk_seq'::regclass), dco_dato_empleado_fk bigint, dco_ley_salario_fk bigint, dco_fuente_financiamiento_fk bigint, dco_institucion_paga_salario_fk bigint, dco_institucion_paga_salario_otro character varying(255), dco_tipo_institucion_paga_fk bigint, dco_hora_ingreso time, dco_hora_salida time, dco_salario_mensual numeric(10,2), dco_tipo_nombramiento_fk bigint, dco_modo_pago_fk bigint, dco_estado_contratacion character varying(50), dco_centro_educativo_fk bigint, dco_desde date, dco_hasta date, dco_cargo_fk bigint, dco_jornada_fk bigint, dco_tipo_contrato_fk bigint, dco_ult_mod_fecha timestamp without time zone, dco_ult_mod_usuario character varying(45), dco_version integer, CONSTRAINT sg_datos_contratacion_pkey PRIMARY KEY (dco_pk), CONSTRAINT sg_datos_contratacion_centro_educativo_fkey FOREIGN KEY (dco_centro_educativo_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_cargo_fkey FOREIGN KEY (dco_cargo_fk) REFERENCES catalogo.sg_cargo(car_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_jornada_fkey FOREIGN KEY (dco_jornada_fk) REFERENCES catalogo.sg_jornadas_laborales(jla_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_tipo_contrato_fkey FOREIGN KEY (dco_tipo_contrato_fk) REFERENCES catalogo.sg_tipo_contrato(tco_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_datos_empleado_fkey FOREIGN KEY (dco_dato_empleado_fk) REFERENCES centros_educativos.sg_datos_empleado(dem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_ley_salario_fkey FOREIGN KEY (dco_ley_salario_fk) REFERENCES catalogo.sg_ley_salario(lsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_fuente_financiamiento_fkey FOREIGN KEY (dco_fuente_financiamiento_fk) REFERENCES catalogo.sg_fuente_financiamiento(ffi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_institucion_paga_salario_fkey FOREIGN KEY (dco_institucion_paga_salario_fk) REFERENCES catalogo.sg_institucion_paga_salario(ips_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_tipo_institucion_paga_fkey FOREIGN KEY (dco_tipo_institucion_paga_fk) REFERENCES catalogo.sg_tipo_institucion_paga(tip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_tipo_nombramiento_fkey FOREIGN KEY (dco_tipo_nombramiento_fk) REFERENCES catalogo.sg_tipo_nombramiento(tno_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_datos_contratacion_modo_pago_fkey FOREIGN KEY (dco_modo_pago_fk) REFERENCES catalogo.sg_modo_pago(mpa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_datos_contratacion                                          IS 'Tabla para el registro de los datos de contratación.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_pk                                   IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_ley_salario_fk                       IS 'Llave foranea que hace referencia a la ley de salarios.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_fuente_financiamiento_fk             IS 'Llave foranea que hace referencia a la fuente de financiamiento.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_institucion_paga_salario_fk          IS 'Llave foranea que hace referencia a la institucion paga salario.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_institucion_paga_salario_otro        IS 'Institucion paga salario otro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_tipo_institucion_paga_fk             IS 'Llave foranea que hace referencia al tipo de institucion paga.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_hora_ingreso                         IS 'Hora de ingreso del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_hora_salida                          IS 'Hora de salida del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_salario_mensual                      IS 'Salario mensual del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_tipo_nombramiento_fk                 IS 'Llave foranea que hace referencia al tipo de nombramiento.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_modo_pago_fk                         IS 'Llave foranea que hace referencia al modo de pago.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_estado_contratacion                  IS 'Estado de contratación del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_dato_empleado_fk                     IS 'Llave foranea que hace referencia a los datos de empleado.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_centro_educativo_fk                  IS 'Llave foranea que hace referencia al centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_desde                                IS 'Fecha desde del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_hasta                                IS 'Fecha hasta del registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_cargo_fk                             IS 'Llave foranea que hace referencia al cargo.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_jornada_fk                           IS 'Llave foranea que hace referencia a la jornada.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_tipo_contrato_fk                     IS 'Llave foranea que hace referencia al tipo de contrato.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_ult_mod_fecha                        IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_ult_mod_usuario                      IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_datos_contratacion.dco_version                              IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_datos_contratacion_aud (dco_pk bigint NOT NULL, dco_dato_empleado_fk bigint, dco_ley_salario_fk bigint, dco_fuente_financiamiento_fk bigint, dco_institucion_paga_salario_fk bigint, dco_institucion_paga_salario_otro character varying(255), dco_tipo_institucion_paga_fk bigint, dco_hora_ingreso time, dco_hora_salida time, dco_salario_mensual numeric(10,2), dco_tipo_nombramiento_fk bigint, dco_modo_pago_fk bigint, dco_estado_contratacion character varying(50), dco_centro_educativo_fk bigint, dco_desde date, dco_hasta date, dco_cargo_fk bigint, dco_jornada_fk bigint, dco_tipo_contrato_fk bigint, dco_ult_mod_fecha timestamp without time zone, dco_ult_mod_usuario character varying(45), dco_version integer, rev bigint, revtype smallint);


-- Experiencia Laboral
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_experiencia_laboral_ela_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_experiencia_laboral (ela_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_experiencia_laboral_ela_pk_seq'::regclass), ela_dato_empleado_fk bigint, ela_institucion character varying(255), ela_tipo_institucion character varying(50), ela_direccion character varying(500), ela_cargo character varying(50), ela_desde date, ela_hasta date, ela_ult_mod_fecha timestamp without time zone, ela_ult_mod_usuario character varying(45), ela_version integer, CONSTRAINT sg_experiencia_laboral_pkey PRIMARY KEY (ela_pk),CONSTRAINT sg_experiencia_laboral_dato_empleado_fkey FOREIGN KEY (ela_dato_empleado_fk) REFERENCES centros_educativos.sg_datos_empleado(dem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_experiencia_laboral                         IS 'Tabla para el registro de la experiencia laboral.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_dato_empleado_fk          IS 'Llave foranea que hace referencia al dato de empleado.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_institucion          IS 'Nombre la institución.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_tipo_institucion          IS 'Tipo de institución.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_direccion          IS 'Dirección del registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_cargo          IS 'Cargo del registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_desde               IS 'Fecha desde del registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_hasta          IS 'Fecha hasta del registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_experiencia_laboral.ela_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_experiencia_laboral_aud (ela_pk bigint NOT NULL,ela_dato_empleado_fk bigint, ela_institucion character varying(255), ela_tipo_institucion character varying(50), ela_direccion character varying(500), ela_cargo character varying(50), ela_desde date, ela_hasta date, ela_ult_mod_fecha timestamp without time zone, ela_ult_mod_usuario character varying(45), ela_version integer, rev bigint, revtype smallint);


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_archivos_ach_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivos (ach_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_archivos_ach_pk_seq'::regclass), ach_nombre character varying(255), ach_content_type character varying(255), ach_descripcion character varying(1000), ach_ext character varying(50), ach_ult_mod_fecha timestamp without time zone, ach_ult_mod_usuario character varying(45), ach_version integer, PRIMARY KEY (ach_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivos_aud (ach_pk bigint NOT NULL, ach_nombre character varying(255), ach_content_type character varying(255), ach_descripcion character varying(1000), ach_ext character varying(50), ach_ult_mod_fecha timestamp without time zone, ach_ult_mod_usuario character varying(45), ach_version integer, rev bigint, revtype smallint);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_jornadas (sed_pk bigint NOT NULL,jla_pk bigint NOT NULL, CONSTRAINT sg_sedes_jornadas_sede_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes (sed_pk), CONSTRAINT sg_sedes_jornadas_jornada_fk FOREIGN KEY (jla_pk) REFERENCES catalogo.sg_jornadas_laborales (jla_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_jornadas_aud(sed_pk bigint NOT NULL,jla_pk bigint NOT NULL, rev bigint, revtype smallint);


-- Estudios realizados
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estudios_realizados_ere_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudios_realizados (ere_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_estudios_realizados_ere_pk_seq'::regclass), ere_internet boolean, ere_sistema_gestion_contenido_fk bigint, ere_nivel_manejo_sgc character varying(10), ere_habilidades character varying(5000), ere_destrezas character varying(5000), ere_escolaridad_fk bigint, ere_ult_mod_fecha timestamp without time zone, ere_ult_mod_usuario character varying(45), ere_version integer, CONSTRAINT sg_estudios_realizados_pkey PRIMARY KEY (ere_pk), CONSTRAINT sg_estudios_realizados_sgc_fkey FOREIGN KEY (ere_sistema_gestion_contenido_fk) REFERENCES catalogo.sg_sistemas_gestion_contenido(sgc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_realizados_escolaridad_fkey FOREIGN KEY (ere_escolaridad_fk) REFERENCES catalogo.sg_escolaridades(esc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_estudios_realizados                         IS 'Tabla para el registro de los estudios realizados.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_internet          IS 'Indica si conoce (true) de internet o no (false).';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_sistema_gestion_contenido_fk          IS 'Llave foranea que hace referencia al sistema de gestion de contenido.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_nivel_manejo_sgc          IS 'Nivel del manejo del sistema de gestión de contenido.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_habilidades          IS 'Habilidades del registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_destrezas               IS 'Destrezas del registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_escolaridad_fk          IS 'Llave foranea que hace referencia a la escolaridad.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudios_realizados_aud (ere_pk bigint NOT NULL, ere_internet boolean, ere_sistema_gestion_contenido_fk bigint, ere_nivel_manejo_sgc character varying(10), ere_habilidades character varying(5000), ere_destrezas character varying(5000), ere_escolaridad_fk bigint, ere_ult_mod_fecha timestamp without time zone, ere_ult_mod_usuario character varying(45), ere_version integer, rev bigint, revtype smallint);


-- Idiomas
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_idiomas_realizados_ire_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_idiomas_realizados (ire_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_idiomas_realizados_ire_pk_seq'::regclass), ire_estudios_realizados_fk bigint, ire_idioma_fk bigint, ire_nivel_idioma_fk bigint, ire_observaciones character varying(5000), ire_ult_mod_fecha timestamp without time zone, ire_ult_mod_usuario character varying(45), ire_version integer, CONSTRAINT sg_idiomas_realizados_pkey PRIMARY KEY (ire_pk), CONSTRAINT sg_idiomas_realizados_estudios_realizados_fkey FOREIGN KEY (ire_estudios_realizados_fk) REFERENCES centros_educativos.sg_estudios_realizados(ere_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_idiomas_realizados_idioma_fkey FOREIGN KEY (ire_idioma_fk) REFERENCES catalogo.sg_idiomas(idi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_idiomas_realizados_nivel_idioma_fkey FOREIGN KEY (ire_nivel_idioma_fk) REFERENCES catalogo.sg_niveles_idioma(nid_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_idiomas_realizados                         IS 'Tabla para el registro de los estudios realizados.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_estudios_realizados_fk          IS 'Indica si conoce (true) de internet o no (false).';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_idioma_fk          IS 'Llave foranea que hace referencia al sistema de gestion de contenido.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_nivel_idioma_fk          IS 'Nivel del manejo del sistema de gestión de contenido.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_observaciones          IS 'Habilidades del registro.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_idiomas_realizados.ire_version                 IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_idiomas_realizados_aud (ire_pk bigint NOT NULL, ire_estudios_realizados_fk bigint, ire_idioma_fk bigint, ire_nivel_idioma_fk bigint, ire_observaciones character varying(5000), ire_ult_mod_fecha timestamp without time zone, ire_ult_mod_usuario character varying(45), ire_version integer, rev bigint, revtype smallint);


-- Estudios superiores
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estudios_superiores_esu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudios_superiores (esu_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_estudios_superiores_esu_pk_seq'::regclass), esu_estudios_fk bigint,esu_tipo_fk bigint, esu_pais_fk bigint, esu_carrera_fk bigint, esu_especialidad_fk bigint, esu_institucion_estudio_fk bigint, esu_desde date, esu_hasta date, esu_modalidad_fk bigint, esu_anio_titulacion integer, esu_ult_mod_fecha timestamp without time zone, esu_ult_mod_usuario character varying(45), esu_version integer, CONSTRAINT sg_estudios_superiores_pkey PRIMARY KEY (esu_pk), CONSTRAINT sg_estudios_superiores_estudios_realizados_fkey  FOREIGN KEY (esu_estudios_fk)               REFERENCES centros_educativos.sg_estudios_realizados(ere_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_tipo_fkey                 FOREIGN KEY (esu_tipo_fk)                   REFERENCES catalogo.sg_tipos_estudio(tes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_pais_fkey                 FOREIGN KEY (esu_pais_fk)                   REFERENCES catalogo.sg_paises(pai_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_carrera_fkey              FOREIGN KEY (esu_carrera_fk)                REFERENCES catalogo.sg_carreras(crr_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_especialidad_fkey         FOREIGN KEY (esu_especialidad_fk)           REFERENCES catalogo.sg_especialidades(esp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_institucion_estudio_fkey  FOREIGN KEY (esu_institucion_estudio_fk)    REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudios_superiores_modalidad_fkey            FOREIGN KEY (esu_modalidad_fk)              REFERENCES catalogo.sg_modalidades_estudio(mes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_estudios_superiores                                 IS 'Tabla para el registro de los estudios superiores.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_pk                          IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_estudios_fk                 IS 'Llave foranea que hace referencia a los estudios realizados.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_tipo_fk                     IS 'Llave foranea que hace referencia al tipo de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_pais_fk                     IS 'Llave foranea que hace referencia al pais.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_carrera_fk                  IS 'Llave foranea que hace referencia a la carrera.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_especialidad_fk             IS 'Llave foranea que hace referencia a la especialidad.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_institucion_estudio_fk      IS 'Llave foranea que hace referencia a la institucion de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_desde                       IS 'Fecha desde del registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_hasta                       IS 'Fecha hasta del registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_modalidad_fk                IS 'Llave foranea que hace referencia a la modalidad.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_anio_titulacion             IS 'Año de titulación del registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_ult_mod_fecha               IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_ult_mod_usuario             IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudios_superiores.esu_version                     IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudios_superiores_aud (esu_pk bigint NOT NULL, esu_estudios_fk bigint,esu_tipo_fk bigint, esu_pais_fk bigint, esu_carrera_fk bigint, esu_especialidad_fk bigint, esu_institucion_estudio_fk bigint, esu_desde date, esu_hasta date, esu_modalidad_fk bigint, esu_anio_titulacion integer, esu_ult_mod_fecha timestamp without time zone, esu_ult_mod_usuario character varying(45), esu_version integer,  rev bigint, revtype smallint);


-- Capacitaciones
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_capacitaciones_cap_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_capacitaciones (cap_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_capacitaciones_cap_pk_seq'::regclass), cap_estudios_fk bigint, cap_nombre_capacitacion CHARACTER VARYING(255), cap_tipo_capacitacion_fk bigint, cap_alcance_capacitacion_fk bigint, cap_institucion_estudio_fk bigint, cap_desde date, cap_hasta date, cap_duracion_horas integer, cap_modalidad_fk bigint, cap_curso_acreditado boolean, cap_ult_mod_fecha timestamp without time zone, cap_ult_mod_usuario character varying(45), cap_version integer, CONSTRAINT sg_capacitaciones_pkey PRIMARY KEY (cap_pk), CONSTRAINT sg_capacitaciones_estudios_realizados_fkey  FOREIGN KEY (cap_estudios_fk)               REFERENCES centros_educativos.sg_estudios_realizados(ere_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_capacitaciones_tipo_capacitacion_fkey    FOREIGN KEY (cap_tipo_capacitacion_fk)      REFERENCES catalogo.sg_tipos_capacitacion(tca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_capacitaciones_alcance_fkey              FOREIGN KEY (cap_alcance_capacitacion_fk)   REFERENCES catalogo.sg_alcance_capacitacion(aca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_capacitaciones_institucion_estudio_fkey  FOREIGN KEY (cap_institucion_estudio_fk)    REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_capacitaciones_modalidad_fkey            FOREIGN KEY (cap_modalidad_fk)              REFERENCES catalogo.sg_modalidades_estudio(mes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_capacitaciones                                 IS 'Tabla para el registro de las capacitaciones.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_pk                          IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_estudios_fk                 IS 'Llave foranea que hace referencia a los estudios realizados.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_nombre_capacitacion         IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_tipo_capacitacion_fk        IS 'Llave foranea que hace referencia al tipo de capacitación.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_alcance_capacitacion_fk     IS 'Llave foranea que hace referencia al alcance de la capacitación.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_institucion_estudio_fk      IS 'Llave foranea que hace referencia a la institución de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_desde                       IS 'Fecha desde del registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_hasta                       IS 'Fecha hasta del registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_duracion_horas              IS 'Duración en horas del registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_modalidad_fk                IS 'Llave foranea que hace referencia a la modalidad.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_curso_acreditado            IS 'Indica si la capacitación es acreditada (true) o no (false).';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_ult_mod_fecha               IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_ult_mod_usuario             IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_capacitaciones.cap_version                     IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_capacitaciones_aud (cap_pk bigint NOT NULL, cap_estudios_fk bigint, cap_nombre_capacitacion CHARACTER VARYING(255), cap_tipo_capacitacion_fk bigint, cap_alcance_capacitacion_fk bigint, cap_institucion_estudio_fk bigint, cap_desde date, cap_hasta date, cap_duracion_horas integer, cap_modalidad_fk bigint, cap_curso_acreditado boolean, cap_ult_mod_fecha timestamp without time zone, cap_ult_mod_usuario character varying(45), cap_version integer,  rev bigint, revtype smallint);


-- Calificaciones
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_calificaciones_cal_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificaciones (cal_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_calificaciones_cal_pk_seq'::regclass),cal_rango_fecha_fk bigint, cal_seccion_fk bigint,cal_componente_plan_estudio_fk bigint,cal_fecha DATE NOT NULL,cal_ult_mod_fecha timestamp without time zone, cal_ult_mod_usuario character varying(45), cal_version integer, CONSTRAINT sg_calificaciones_pkey PRIMARY KEY (cal_pk),CONSTRAINT sg_cal_seccion_fk FOREIGN KEY (cal_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_cal_componente_plan_estudio_fk FOREIGN KEY (cal_componente_plan_estudio_fk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_cal_rango_fecha_fk FOREIGN KEY (cal_rango_fecha_fk) REFERENCES centros_educativos.sg_rango_fecha(rfe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_calificaciones IS 'Tabla para el registro de calificaciones.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_fecha IS 'Fecha del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_rango_fecha_fk IS 'Rango Fecha del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_seccion_fk IS 'Seccion del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificaciones_aud (cal_pk bigint NOT NULL,cal_rango_fecha_fk bigint,cal_seccion_fk bigint,cal_componente_plan_estudio_fk bigint,cal_fecha DATE NOT NULL, cal_ult_mod_fecha timestamp without time zone, cal_ult_mod_usuario character varying(45), cal_version integer, rev bigint, revtype smallint);

-- Calificaciones Estudiante
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_calificaciones_estudiante_calest_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificaciones_estudiante (calest_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_calificaciones_estudiante_calest_pk_seq'::regclass),calest_estudiante_fk bigint,calest_calificacion_fk bigint, calest_calificacion character varying(45), calest_observacion character varying(255),calest_ult_mod_fecha timestamp without time zone, calest_ult_mod_usuario character varying(45), calest_version integer, CONSTRAINT sg_calificaciones_estudiante_pkey PRIMARY KEY (calest_pk),CONSTRAINT sg_calest_estudiante_fk FOREIGN KEY (calest_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_calest_calificacion_fk FOREIGN KEY (calest_calificacion_fk) REFERENCES centros_educativos.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_calificaciones_estudiante IS 'Tabla para el registro de calificaciones estudiante.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_calificacion IS 'Calificacion del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_observacion IS 'Observacion del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_estudiante_fk IS 'Estudiante del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_calificacion_fk IS 'Calificacion del registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.calest_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_calificaciones_estudiante_aud (calest_pk bigint NOT NULL,calest_estudiante_fk bigint, calest_calificacion_fk bigint,calest_calificacion character varying(45),calest_observacion character varying(255),calest_ult_mod_fecha timestamp without time zone, calest_ult_mod_usuario character varying(45), calest_version integer, rev bigint, revtype smallint);

-- Formacion docente
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_formaciones_docente_fdo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_formaciones_docente (fdo_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_formaciones_docente_fdo_pk_seq'::regclass), fdo_nivel_fk bigint, fdo_categoria_fk bigint, fdo_especialidad_fk bigint, fdo_modulo_fk bigint, fdo_fecha_desde date, fdo_fecha_hasta date, fdo_departamento_fk bigint, fdo_sede_fk bigint, fdo_aprobado boolean, fdo_calificacion_final character varying(10), fdo_ult_mod_fecha timestamp without time zone, fdo_ult_mod_usuario character varying(45), fdo_version integer, CONSTRAINT sg_formaciones_docente_pkey PRIMARY KEY (fdo_pk), CONSTRAINT sg_formaciones_docente_nivel_fkey            FOREIGN KEY (fdo_nivel_fk)          REFERENCES catalogo.sg_nivel_formacion_docente(nfd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_formaciones_docente_categoria_fkey        FOREIGN KEY (fdo_categoria_fk)      REFERENCES catalogo.sg_categoria_formacion_docente(cfd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_formaciones_docente_especialidad_fkey     FOREIGN KEY (fdo_especialidad_fk)   REFERENCES catalogo.sg_especialidades(esp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_formaciones_docente_modulo_fkey           FOREIGN KEY (fdo_modulo_fk)         REFERENCES catalogo.sg_modulo_formacion_docente(mfd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_formaciones_docente_departamento_fkey     FOREIGN KEY (fdo_departamento_fk)   REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_formaciones_docente_sede_fkey             FOREIGN KEY (fdo_sede_fk)           REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_formaciones_docente                          IS 'Tabla para el registro de la formación docente.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_nivel_fk            IS 'Clave foránea que hace referencia al nivel.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_categoria_fk        IS 'Clave foránea que hace referencia al categoría.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_especialidad_fk     IS 'Clave foránea que hace referencia a la especialidad.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_modulo_fk           IS 'Clave foránea que hace referencia al módulo.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_fecha_desde         IS 'Fecha desde';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_fecha_hasta         IS 'Fecha hasta';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_departamento_fk     IS 'Clave foránea que hace referencia al departamento.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_sede_fk             IS 'Clave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_aprobado            IS 'Aprobado.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_calificacion_final  IS 'Calficicación final del registro.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_formaciones_docente_aud (fdo_pk bigint NOT NULL, fdo_nivel_fk bigint, fdo_categoria_fk bigint, fdo_especialidad_fk bigint, fdo_modulo_fk bigint, fdo_fecha_desde date, fdo_fecha_hasta date, fdo_departamento_fk bigint, fdo_sede_fk bigint, fdo_aprobado boolean, fdo_calificacion_final character varying(10), fdo_ult_mod_fecha timestamp without time zone, fdo_ult_mod_usuario character varying(45), fdo_version integer, rev bigint, revtype smallint);


-- Nie
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_nie_nie_pk_seq INCREMENT 1 START 10000000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_nie (nie_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_nie_nie_pk_seq'::regclass),nie_fecha DATE, nie_ult_mod_fecha timestamp without time zone, nie_ult_mod_usuario character varying(45), nie_version integer, CONSTRAINT sg_nie_pkey PRIMARY KEY (nie_pk));
COMMENT ON TABLE centros_educativos.sg_nie IS 'Tabla para el registro de nie.';
COMMENT ON COLUMN centros_educativos.sg_nie.nie_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_nie.nie_fecha IS 'Fecha del registro.';
COMMENT ON COLUMN centros_educativos.sg_nie.nie_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_nie.nie_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_nie.nie_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_nie_aud (nie_pk bigint NOT NULL,nie_fecha DATE, nie_ult_mod_fecha timestamp without time zone, nie_ult_mod_usuario character varying(45), nie_version integer, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_direcciones_aud ADD COLUMN dir_caserio_texto character varying(255);
ALTER TABLE centros_educativos.sg_direcciones ADD COLUMN dir_caserio_texto character varying(255);
ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_sede_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_sede_fk IS 'Sede a la que es adscrita';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_sede_fk bigint;

CREATE UNIQUE INDEX IF NOT EXISTS sg_personas_nie ON centros_educativos.sg_personas USING btree (per_nie);
CREATE UNIQUE INDEX IF NOT EXISTS sg_personas_dui ON centros_educativos.sg_personas USING btree (per_dui);
CREATE UNIQUE INDEX IF NOT EXISTS sg_personas_cun ON centros_educativos.sg_personas USING btree (per_cun); 

ALTER TABLE centros_educativos.sg_familiares ADD "fam_referente" boolean;
ALTER TABLE centros_educativos.sg_familiares_aud ADD "fam_referente" boolean;

ALTER TABLE centros_educativos.sg_modulos_diplomado ADD CONSTRAINT mdi_codigo_uk UNIQUE (mdi_codigo);
ALTER TABLE centros_educativos.sg_anio_lectivo ADD CONSTRAINT ale_anio_uk UNIQUE (ale_anio);
ALTER TABLE centros_educativos.sg_noticias ADD CONSTRAINT not_codigo_uk UNIQUE (not_codigo);

ALTER TABLE centros_educativos.sg_personas ADD "per_nip" bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD "per_nip" bigint;

--Alter table: sg_datos_empleado
ALTER TABLE centros_educativos.sg_datos_empleado ADD COLUMN dem_personal_sede_fk bigint;
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD COLUMN dem_personal_sede_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_personal_sede_fk IS 'Llave foranea que hace referencia al personal de la sede educativa.';
ALTER TABLE centros_educativos.sg_datos_empleado ADD CONSTRAINT sg_datos_empleado_personal_sede_fkey FOREIGN KEY (dem_personal_sede_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--Alter table: sg_estudios_realizados
ALTER TABLE centros_educativos.sg_estudios_realizados ADD COLUMN ere_personal_sede_fk bigint;
ALTER TABLE centros_educativos.sg_estudios_realizados_aud ADD COLUMN ere_personal_sede_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_estudios_realizados.ere_personal_sede_fk          IS 'Llave foranea que hace referencia al personal de la sede.';
ALTER TABLE centros_educativos.sg_estudios_realizados ADD CONSTRAINT sg_estudios_realizados_personal_sede_fkey FOREIGN KEY (ere_personal_sede_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--Alter table: sg_formaciones_docente
ALTER TABLE centros_educativos.sg_formaciones_docente ADD COLUMN fdo_personal_sede_fk bigint;
ALTER TABLE centros_educativos.sg_formaciones_docente_aud ADD COLUMN fdo_personal_sede_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_formaciones_docente.fdo_personal_sede_fk            IS 'Clave foránea que hace referencia al personal sede.';
ALTER TABLE centros_educativos.sg_formaciones_docente ADD CONSTRAINT sg_formaciones_docente_personal_sede_fkey FOREIGN KEY (fdo_personal_sede_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sed_tipo_calendario_fk FOREIGN KEY (sed_tipo_calendario_fk) REFERENCES catalogo.sg_tipos_calendario_escolar(tce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_modalidad_educativa_fkey FOREIGN KEY (rea_modalidad_educativa_fk) REFERENCES centros_educativos.sg_modalidades(mod_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_modalidad_atencion_fkey FOREIGN KEY (rea_modalidad_atencion_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_sub_modalidad_atencion_fkey FOREIGN KEY (rea_sub_modalidad_atencion_fk) REFERENCES catalogo.sg_sub_modalidades(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_nacionalidad_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_personas.per_nacionalidad_fk    IS 'Clave foránea que hace referencia a nacionalidad de persona.';
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_persona_nacionalidad_fkey FOREIGN KEY (per_nacionalidad_fk) REFERENCES catalogo.sg_nacionalidades(nac_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_nacionalidad_fk bigint;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_foto_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_foto_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_personas.per_foto_fk IS 'Llave foránea que hace referencia a la foto de la persona.';
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_foto_fk FOREIGN KEY (per_foto_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_direcciones DROP CONSTRAINT sg_dir_persona_fkey;
ALTER TABLE centros_educativos.sg_direcciones DROP COLUMN dir_persona_fk;
ALTER TABLE centros_educativos.sg_direcciones_aud DROP COLUMN dir_persona_fk;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_direccion_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_direccion_fk bigint;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_dir_fk FOREIGN KEY (per_direccion_fk) REFERENCES centros_educativos.sg_direcciones(dir_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_archivos ADD COLUMN ach_path CHARACTER varying(500);
ALTER TABLE centros_educativos.sg_archivos_aud ADD COLUMN ach_path CHARACTER varying(500);

ALTER TABLE centros_educativos.sg_componente_plan_grado DROP COLUMN cpg_requiere_nie;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud DROP COLUMN cpg_requiere_nie;

ALTER TABLE centros_educativos.sg_grados ADD COLUMN gra_requiere_nie BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_grados.gra_requiere_nie   IS 'requiere nie del registro.';
ALTER TABLE centros_educativos.sg_grados_aud ADD COLUMN gra_requiere_nie BOOLEAN;

ALTER TABLE centros_educativos.sg_componente_plan_grado DROP COLUMN cpg_opcion_fk;
ALTER TABLE centros_educativos.sg_componente_plan_grado DROP COLUMN cpg_programa_educativo_fk;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud DROP COLUMN cpg_opcion_fk;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud DROP COLUMN cpg_programa_educativo_fk;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_modalidad_educativa_fkey FOREIGN KEY (rea_modalidad_educativa_fk) REFERENCES centros_educativos.sg_modalidades(mod_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_modalidad_atencion_fkey FOREIGN KEY (rea_modalidad_atencion_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD CONSTRAINT sg_rel_sub_modalidad_atencion_fkey FOREIGN KEY (rea_sub_modalidad_atencion_fk) REFERENCES catalogo.sg_sub_modalidades(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes DROP COLUMN sed_habilitado;
ALTER TABLE centros_educativos.sg_sedes_aud DROP COLUMN sed_habilitado;

ALTER TABLE centros_educativos.sg_personas
ALTER COLUMN per_dui type CHARACTER VARYING(20) 
USING per_dui::VARCHAR;

ALTER TABLE centros_educativos.sg_personas_aud
ALTER COLUMN per_dui type CHARACTER VARYING(20) 
USING per_dui::VARCHAR;

ALTER TABLE centros_educativos.sg_noticias ADD COLUMN not_titulo CHARACTER VARYING(255);
ALTER TABLE centros_educativos.sg_noticias ADD COLUMN not_orden INTEGER;
ALTER TABLE centros_educativos.sg_noticias ADD COLUMN not_resaltada BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_noticias.not_titulo   IS 'Título del registro';
COMMENT ON COLUMN centros_educativos.sg_noticias.not_orden   IS 'Orden del registro';
COMMENT ON COLUMN centros_educativos.sg_noticias.not_resaltada   IS 'Indica si el registro es resaltado (true) o no (false)';
ALTER TABLE centros_educativos.sg_noticias_aud ADD COLUMN not_titulo CHARACTER VARYING(255);
ALTER TABLE centros_educativos.sg_noticias_aud ADD COLUMN not_orden INTEGER;
ALTER TABLE centros_educativos.sg_noticias_aud ADD COLUMN not_resaltada BOOLEAN;

ALTER TABLE centros_educativos.sg_calendarios DROP COLUMN sg_cal_tipo_calendario_fk;
ALTER TABLE centros_educativos.sg_calendarios ADD CONSTRAINT sg_cal_tipo_calendario_fk FOREIGN KEY (cal_tipo_calendario_fk) REFERENCES catalogo.sg_tipos_calendario_escolar(tce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_calendarios DROP COLUMN cal_tipo_calendario_fk;
ALTER TABLE centros_educativos.sg_calendarios_AUD DROP COLUMN cal_tipo_calendario_fk;
ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_tipo_calendario_fk bigint;
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_tipo_calendario_fk bigint;
ALTER TABLE centros_educativos.sg_calendarios ADD CONSTRAINT sg_cal_tipo_calendario_fk FOREIGN KEY (cal_tipo_calendario_fk) REFERENCES catalogo.sg_tipos_calendario_escolar(tce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT estudiante_persona_uk UNIQUE (est_pk, est_persona);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nacimiento_posee_no_presenta BOOLEAN;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nacimiento_posee_no_presenta BOOLEAN;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_dependencia_economica_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_ocupacion_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_dependencia_economica_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_ocupacion_fk bigint;

ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_dependencia_economica_fk FOREIGN KEY (est_dependencia_economica_fk) REFERENCES catalogo.sg_dependencias_economica(dec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_ocupacion_fk FOREIGN KEY (est_ocupacion_fk) REFERENCES catalogo.sg_ocupaciones(ocu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_familiares DROP COLUMN fam_habilitado;
ALTER TABLE centros_educativos.sg_familiares_aud DROP COLUMN fam_habilitado;

ALTER TABLE centros_educativos.sg_familiares ADD COLUMN all_es_familiar BOOLEAN;
ALTER TABLE centros_educativos.sg_familiares_aud ADD COLUMN all_es_familiar BOOLEAN;

ALTER TABLE centros_educativos.sg_familiares RENAME TO sg_allegados;
ALTER TABLE centros_educativos.sg_familiares_aud RENAME TO sg_allegados_aud;

ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_pk TO all_pk;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_referente TO all_referente;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_ult_mod_fecha TO all_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_ult_mod_usuario TO all_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_version TO all_version;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_estudiante TO all_estudiante;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_persona TO all_persona;
ALTER TABLE centros_educativos.sg_allegados RENAME COLUMN fam_tipo_parentesco TO all_tipo_parentesco;

ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_pk TO all_pk;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_referente TO all_referente;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_ult_mod_fecha TO all_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_ult_mod_usuario TO all_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_version TO all_version;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_estudiante TO all_estudiante;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_persona TO all_persona;
ALTER TABLE centros_educativos.sg_allegados_aud RENAME COLUMN fam_tipo_parentesco TO all_tipo_parentesco;

ALTER TABLE centros_educativos.sg_allegados ADD CONSTRAINT familiares_estudiante_persona UNIQUE (all_pk, all_persona, all_estudiante);

ALTER TABLE centros_educativos.sg_direcciones ADD COLUMN dir_tipo_calle_fk bigint;
ALTER TABLE centros_educativos.sg_direcciones_aud ADD COLUMN dir_tipo_calle_fk bigint;

ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_tipo_calle_fk FOREIGN KEY (dir_tipo_calle_fk) REFERENCES catalogo.sg_tipos_calle(tll_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_correo_electronico_2 CHARACTER varying(255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_correo_electronico_2 CHARACTER varying(255);

ALTER TABLE centros_educativos.sg_asignaturas ADD CONSTRAINT sg_asignaturas_area_asig_mod FOREIGN KEY (asig_area_asignatura_modulo_fk) REFERENCES catalogo.sg_areas_asignatura_modulo(aam_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_modulos ADD CONSTRAINT sg_modulos_area_asig_mod FOREIGN KEY (mod_area_asignatura_modulo_fk) REFERENCES catalogo.sg_areas_asignatura_modulo(aam_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_sede_servicio_infra_rss_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_servicio_infra (rss_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_sede_servicio_infra_rss_pk_seq'::regclass), rss_sede_fk bigint, rss_servicio_infra_fk bigint, rss_tiene_servicio boolean, rss_ult_mod_fecha timestamp without time zone, rss_ult_mod_usuario character varying(45), rss_version integer);
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_servicio_infra_aud (rss_pk bigint NOT NULL, rss_sede_fk bigint, rss_servicio_infra_fk bigint, rss_tiene_servicio boolean, rss_ult_mod_fecha timestamp without time zone, rss_ult_mod_usuario character varying(45), rss_version integer, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_rel_sede_servicio_infra ADD CONSTRAINT sg_rel_sede_servicio_infra_uk UNIQUE (rss_sede_fk, rss_servicio_infra_fk);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_sitio_web CHARACTER varying(255);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_sitio_web CHARACTER varying(255);

ALTER TABLE centros_educativos.sg_secciones ALTER COLUMN sec_codigo type VARCHAR(60);
ALTER TABLE centros_educativos.sg_secciones_aud ALTER COLUMN sec_codigo type VARCHAR(60);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_discapacidades (per_pk bigint NOT NULL,dis_pk bigint NOT NULL, CONSTRAINT sg_personas_discapacidades_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_discapacidades_discapacidad_fk FOREIGN KEY (dis_pk) REFERENCES catalogo.sg_discapacidades (dis_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_discapacidades_aud(per_pk bigint NOT NULL,dis_pk bigint NOT NULL, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_allegados ADD COLUMN all_personal_fk bigint;
ALTER TABLE centros_educativos.sg_allegados_aud ADD COLUMN all_personal_fk bigint;
ALTER TABLE centros_educativos.sg_allegados ADD CONSTRAINT sg_allegados_personal_fkey FOREIGN KEY (all_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_allegados DROP CONSTRAINT sg_allegados_personal_fkey;
ALTER TABLE centros_educativos.sg_allegados DROP CONSTRAINT familiares_estudiante_persona;
ALTER TABLE centros_educativos.sg_allegados DROP CONSTRAINT sg_familiares_estudiante_fkey;

ALTER TABLE centros_educativos.sg_allegados DROP COLUMN all_personal_fk;
ALTER TABLE centros_educativos.sg_allegados_aud DROP COLUMN all_personal_fk;
ALTER TABLE centros_educativos.sg_allegados DROP COLUMN all_estudiante;
ALTER TABLE centros_educativos.sg_allegados_aud DROP COLUMN all_estudiante;

ALTER TABLE centros_educativos.sg_allegados ADD COLUMN all_persona_ref bigint;
ALTER TABLE centros_educativos.sg_allegados_aud ADD COLUMN all_persona_ref bigint;
ALTER TABLE centros_educativos.sg_allegados ADD CONSTRAINT sg_allegados_persona_ref_fkey FOREIGN KEY (all_persona_ref) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_allegados ADD CONSTRAINT sg_allegados_persona_persona UNIQUE (all_pk, all_persona, all_persona_ref);


ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_pk TO cae_pk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_calificacion  TO cae_calificacion;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_observacion  TO cae_observacion;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_estudiante_fk  TO cae_estudiante_fk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_calificacion_fk  TO cae_calificacion_fk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_ult_mod_fecha  TO cae_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_ult_mod_usuario  TO cae_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante RENAME COLUMN calest_version  TO cae_version;

ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_pk TO cae_pk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_calificacion  TO cae_calificacion;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_observacion  TO cae_observacion;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_estudiante_fk  TO cae_estudiante_fk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_calificacion_fk  TO cae_calificacion_fk;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_ult_mod_fecha  TO cae_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_ult_mod_usuario  TO cae_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud RENAME COLUMN calest_version  TO cae_version;

ALTER TABLE centros_educativos.sg_secciones ADD CONSTRAINT sg_secciones_anio_sede UNIQUE (sec_codigo,sec_anio_lectivo_fk,sec_servicio_educativo_fk);

ALTER TABLE centros_educativos.sg_niveles DROP COLUMN niv_admite_ciclos;
ALTER TABLE centros_educativos.sg_ciclos DROP COLUMN cic_admite_modalidad;

ALTER TABLE centros_educativos.sg_control_asistencia_cabezal ADD COLUMN cac_estudiantes_presentes INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal ADD COLUMN cac_estudiantes_en_lista INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal ADD COLUMN cac_estudiantes_ausentes_justificados INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal ADD COLUMN cac_estudiantes_ausentes_sin_justificar INTEGER;
COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_estudiantes_presentes   IS 'Cantidad de estudiantes presentes';
COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_estudiantes_en_lista   IS 'Cantidad de estudiantes en lista';
COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_estudiantes_ausentes_justificados   IS 'Cantidad de estudiantes ausentes justificados';
COMMENT ON COLUMN centros_educativos.sg_control_asistencia_cabezal.cac_estudiantes_ausentes_sin_justificar   IS 'Cantidad de estudiantes ausentes sin justificar';
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud ADD COLUMN cac_estudiantes_presentes INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud ADD COLUMN cac_estudiantes_en_lista INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud ADD COLUMN cac_estudiantes_ausentes_justificados INTEGER;
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud ADD COLUMN cac_estudiantes_ausentes_sin_justificar INTEGER;

DROP TABLE centros_educativos.sg_contactos_emergencia;
DROP TABLE centros_educativos.sg_contactos_emergencia_aud;

ALTER TABLE centros_educativos.sg_allegados ADD COLUMN all_contacto_emergencia boolean;
COMMENT ON COLUMN centros_educativos.sg_allegados.all_contacto_emergencia   IS 'Allegado es contacto de emergencia.';
ALTER TABLE centros_educativos.sg_allegados_aud ADD COLUMN all_contacto_emergencia boolean;

ALTER TABLE centros_educativos.sg_direcciones DROP COLUMN IF EXISTS dir_sede_fk;
ALTER TABLE centros_educativos.sg_direcciones_aud DROP COLUMN IF EXISTS dir_sede_fk;

ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_departamento_fkey FOREIGN KEY (dir_departamento) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_municipio_fkey FOREIGN KEY (dir_municipio) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_canton_fkey FOREIGN KEY (dir_canton) REFERENCES catalogo.sg_cantones(can_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_caserio_fkey FOREIGN KEY (dir_caserio) REFERENCES catalogo.sg_caserios(cas_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_direcciones ADD CONSTRAINT sg_direcciones_zona_fkey FOREIGN KEY (dir_zona) REFERENCES catalogo.sg_zonas(zon_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

DROP INDEX IF EXISTS centros_educativos.sg_personas_nie;
DROP INDEX IF EXISTS centros_educativos.sg_personas_dui;
DROP INDEX IF EXISTS centros_educativos.sg_personas_cun;

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_nie UNIQUE (per_nie);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_dui UNIQUE (per_dui);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_nip UNIQUE (per_nip);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_cun UNIQUE (per_cun);

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_departamento_fkey FOREIGN KEY (per_departamento_nacimiento_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_municipio_fkey FOREIGN KEY (per_municipio_nacimiento_fk) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_tipo_sangre_fkey FOREIGN KEY (per_tipo_sangre_fk) REFERENCES catalogo.sg_tipos_sangre(tsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_sexo_fkey FOREIGN KEY (per_sexo_fk) REFERENCES catalogo.sg_sexos(sex_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_estado_civil_fkey FOREIGN KEY (per_estado_civil_fk) REFERENCES catalogo.sg_estados_civil(eci_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_etnia_fkey FOREIGN KEY (per_etnia_fk) REFERENCES catalogo.sg_etnias(etn_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


COMMENT ON TABLE  centros_educativos.sg_datos_empleado                         IS 'Tabla para el registro de los datos de empleado.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_pk                  IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_codigo_empleado          IS 'Código del empleado.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_estado          IS 'Estado del empleado.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_fecha_ingreso_gob          IS 'Fecha de ingreso.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_fecha_ingreso_mined          IS 'Fecha de ingreso mined..';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_nivel_fk               IS 'Lláve foranea que hace referencia al nivel.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_categoria_fk          IS 'Llave foranea que hace referencia a la categoría.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_ult_mod_fecha           IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_ult_mod_usuario         IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_version                 IS 'Versión del registro.';


ALTER TABLE centros_educativos.sg_datos_empleado ADD CONSTRAINT sg_datos_empleado_categoria_fkey FOREIGN KEY (dem_categoria_fk) REFERENCES catalogo.sg_categoria_empleado(cem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_datos_empleado ADD CONSTRAINT sg_datos_empleado_nivel_fkey FOREIGN KEY (dem_nivel_fk) REFERENCES catalogo.sg_nivel_empleado(nem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes DROP COLUMN sed_sede_fk;
ALTER TABLE centros_educativos.sg_sedes_aud DROP COLUMN sed_sede_fk;

ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sedes_sede_adscrita_fkey FOREIGN KEY (sed_centro_adscrito) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sedes_direccion_fkey FOREIGN KEY (sed_direccion_fk) REFERENCES centros_educativos.sg_direcciones(dir_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sedes_clasificacion_fkey FOREIGN KEY (sed_clasificacion_fk) REFERENCES catalogo.sg_clasificaciones(cla_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_provisional boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_provisional boolean;

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_fecha_registro timestamp without time zone;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_fecha_registro timestamp without time zone;

ALTER TABLE centros_educativos.sg_manifestacion_violencia ADD CONSTRAINT sg_manifestacion_violencia_tipo_fkey FOREIGN KEY (mvi_tipo_manifestacion) REFERENCES catalogo.sg_tipos_manifestacion(tma_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_allegados ADD CONSTRAINT sg_allegados_tipo_parentesco_fkey FOREIGN KEY (all_tipo_parentesco) REFERENCES catalogo.sg_tipos_parentesco(tpa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_asistencias DROP COLUMN asi_fecha;
ALTER TABLE centros_educativos.sg_asistencias DROP COLUMN asi_seccion;
ALTER TABLE centros_educativos.sg_asistencias_aud DROP COLUMN asi_fecha;
ALTER TABLE centros_educativos.sg_asistencias_aud DROP COLUMN asi_seccion;
ALTER TABLE centros_educativos.sg_asistencias ADD CONSTRAINT sg_asistencias_motivo_inasistencia_fkey FOREIGN KEY (asi_motivo_inasistencia) REFERENCES catalogo.sg_motivos_inasistencia(min_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_direcciones ALTER COLUMN dir_direccion SET NOT NULL; 
ALTER TABLE centros_educativos.sg_direcciones ALTER COLUMN dir_departamento SET NOT NULL; 
ALTER TABLE centros_educativos.sg_direcciones ALTER COLUMN dir_municipio SET NOT NULL; 

ALTER TABLE centros_educativos.sg_identificaciones ALTER COLUMN ide_tipo_documento SET NOT NULL; 
ALTER TABLE centros_educativos.sg_identificaciones ALTER COLUMN ide_pais_emisor SET NOT NULL; 
ALTER TABLE centros_educativos.sg_identificaciones ALTER COLUMN ide_numero_documento SET NOT NULL; 


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_profesion_fk bigint;
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_escolaridad_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_profesion_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_escolaridad_fk bigint;

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_profesion_fkey FOREIGN KEY (per_profesion_fk) REFERENCES catalogo.sg_profesiones(pro_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_escolaridad_fkey FOREIGN KEY (per_escolaridad_fk) REFERENCES catalogo.sg_escolaridades(esc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_servicio_educativo ADD COLUMN sdu_fecha_solicitada date;
COMMENT ON COLUMN centros_educativos.sg_servicio_educativo.sdu_fecha_solicitada   IS 'Fecha solicitado de registro.';
ALTER TABLE centros_educativos.sg_servicio_educativo_aud ADD COLUMN sdu_fecha_solicitada date;

ALTER TABLE centros_educativos.sg_sedes_ced ADD COLUMN ced_tipo_organismo_administrativo_fk bigint;
ALTER TABLE centros_educativos.sg_sedes_ced_aud ADD COLUMN ced_tipo_organismo_administrativo_fk bigint;
ALTER TABLE centros_educativos.sg_sedes_ced ADD CONSTRAINT sg_sedes_tipo_organismo_administrativo_fkey FOREIGN KEY (ced_tipo_organismo_administrativo_fk) REFERENCES catalogo.sg_tipos_organismo_administrativo(toa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes_ced ADD COLUMN ced_fines_de_lucro boolean;
ALTER TABLE centros_educativos.sg_sedes_ced_aud ADD COLUMN ced_fines_de_lucro boolean;

ALTER TABLE centros_educativos.sg_datos_contratacion DROP COLUMN dco_estado_contratacion;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud DROP COLUMN dco_estado_contratacion ;
ALTER TABLE centros_educativos.sg_datos_contratacion ADD COLUMN dco_estado_contratacion  boolean;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_estado_contratacion  boolean;

CREATE TABLE IF NOT EXISTS centros_educativos.sg_dato_contratacion_jornada_laboral (dco_pk bigint NOT NULL,jla_pk bigint NOT NULL, CONSTRAINT sg_dato_contratacion_jornada_laboral_dato_contratacion_fk FOREIGN KEY (dco_pk) REFERENCES centros_educativos.sg_datos_contratacion (dco_pk), CONSTRAINT sg_dato_contratacion_jornada_laboral_jornada_laboral_fk  FOREIGN KEY (jla_pk) REFERENCES catalogo.sg_jornadas_laborales (jla_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_dato_contratacion_jornada_laboral_aud(dco_pk bigint NOT NULL,jla_pk bigint NOT NULL, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_experiencia_laboral DROP COLUMN ela_tipo_institucion;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud DROP COLUMN ela_tipo_institucion ;
ALTER TABLE centros_educativos.sg_experiencia_laboral ADD COLUMN ela_tipo_institucion bigint;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD COLUMN ela_tipo_institucion bigint;
ALTER TABLE centros_educativos.sg_experiencia_laboral ADD CONSTRAINT sg_experiencia_laboral_tipo_institucion_pago_fkey FOREIGN KEY (ela_tipo_institucion) REFERENCES catalogo.sg_tipo_institucion_paga(tip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_primer_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_segundo_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_tercer_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_primer_apellido type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_segundo_apellido type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_tercer_apellido type VARCHAR(100);

ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_primer_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_segundo_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_tercer_nombre type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_primer_apellido type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_segundo_apellido type VARCHAR(100);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_tercer_apellido type VARCHAR(100);

ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_sexo_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_fecha_nacimiento SET NOT NULL;
ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_estado_civil_fk SET NOT NULL;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_nit character varying(20);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_naturalizada boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_nit character varying(20);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_naturalizada boolean;

ALTER TABLE centros_educativos.sg_estudios_superiores ADD COLUMN esu_carrera_txt CHARACTER VARYING(500);
ALTER TABLE centros_educativos.sg_estudios_superiores ADD COLUMN esu_institucion_estudio_txt CHARACTER VARYING(500);
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD COLUMN esu_carrera_txt CHARACTER VARYING(500);
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD COLUMN esu_institucion_estudio_txt CHARACTER VARYING(500);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_nup character varying(20);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_isss character varying(20);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_inpep character varying(20);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_nup character varying(20);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_isss character varying(20);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_inpep character varying(20);

ALTER TABLE centros_educativos.sg_estudiantes DROP CONSTRAINT sg_estudiantes_ocupacion_fk;
ALTER TABLE centros_educativos.sg_estudiantes DROP COLUMN est_ocupacion_fk;
ALTER TABLE centros_educativos.sg_estudiantes_aud DROP COLUMN est_ocupacion_fk;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_ocupacion_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_ocupacion_fk bigint;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_ocupacion_fk FOREIGN KEY (per_ocupacion_fk) REFERENCES catalogo.sg_ocupaciones(ocu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_manifestacion_violencia ALTER COLUMN mvi_tratamiento type VARCHAR(500);
ALTER TABLE centros_educativos.sg_manifestacion_violencia_aud ALTER COLUMN mvi_tratamiento type VARCHAR(500);

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_fecha_ingreso date;
COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_fecha_ingreso IS 'Fecha ingreso al centro educativo.';
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_fecha_ingreso date;

ALTER TABLE centros_educativos.sg_personal_sede_educativa DROP COLUMN pse_escolaridad_fk;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud DROP COLUMN pse_escolaridad_fk;

-- 0.22.0

ALTER TABLE centros_educativos.sg_personal_sede_educativa DROP CONSTRAINT sg_personal_sede_educativa_afp_fk;
ALTER TABLE centros_educativos.sg_personal_sede_educativa DROP COLUMN pse_afp_fk;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud DROP COLUMN pse_afp_fk;

ALTER TABLE centros_educativos.sg_datos_empleado ADD COLUMN dem_afp_fk bigint;
ALTER TABLE centros_educativos.sg_datos_empleado ALTER COLUMN dem_afp_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD COLUMN dem_afp_fk bigint;
ALTER TABLE centros_educativos.sg_datos_empleado ADD CONSTRAINT sg_datos_empleado_afp_fk FOREIGN KEY (dem_afp_fk) REFERENCES catalogo.sg_afp(afp_pk);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_primer_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_primer_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_segundo_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_segundo_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tercer_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tercer_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_primer_apellido_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_primer_apellido_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_segundo_apellido_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_segundo_apellido_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tercer_apellido_busqueda CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tercer_apellido_busqueda CHARACTER VARYING(100);


ALTER TABLE centros_educativos.sg_niveles_aud DROP COLUMN niv_admite_ciclos;
ALTER TABLE centros_educativos.sg_ciclos_aud DROP COLUMN cic_admite_modalidad;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_habilitado boolean;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_habilitado boolean;

ALTER TABLE centros_educativos.sg_capacitaciones DROP CONSTRAINT sg_capacitaciones_institucion_estudio_fkey;
ALTER TABLE centros_educativos.sg_capacitaciones RENAME COLUMN cap_institucion_estudio_fk TO cap_institucion_estudio;
ALTER TABLE centros_educativos.sg_capacitaciones_aud RENAME COLUMN cap_institucion_estudio_fk TO cap_institucion_estudio;
ALTER TABLE centros_educativos.sg_capacitaciones ALTER COLUMN cap_institucion_estudio type CHARACTER VARYING(255);
ALTER TABLE centros_educativos.sg_capacitaciones_aud ALTER COLUMN cap_institucion_estudio type CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_experiencia_laboral ALTER COLUMN ela_cargo type CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ALTER COLUMN ela_cargo type CHARACTER VARYING(100);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_direccion_notificacion CHARACTER VARYING(500);
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_direccion_notificacion CHARACTER VARYING(500);
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_direccion_notificacion IS 'Dirección para notificaciones.';

ALTER TABLE centros_educativos.sg_identificaciones ALTER COLUMN ide_numero_documento type CHARACTER VARYING(50);
ALTER TABLE centros_educativos.sg_identificaciones_aud ALTER COLUMN ide_numero_documento type CHARACTER VARYING(50);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_lugar_trabajo CHARACTER VARYING(255);
COMMENT ON COLUMN centros_educativos.sg_personas.per_lugar_trabajo IS 'Lugar de trabajo.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_lugar_trabajo CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_estudiantes DROP COLUMN est_trabaja;
ALTER TABLE centros_educativos.sg_estudiantes DROP COLUMN est_tipo_trabajo_fk;
ALTER TABLE centros_educativos.sg_estudiantes_aud DROP COLUMN est_trabaja;
ALTER TABLE centros_educativos.sg_estudiantes_aud DROP COLUMN est_tipo_trabajo_fk;


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_trabaja BOOLEAN;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_trabaja BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_personas.per_trabaja IS 'Bandera que indica si el estudiante trabaja.';
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tipo_trabajo_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tipo_trabajo_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tipo_trabajo_fk IS 'Clave foránea que hace referencia al tipo de trabajo.';

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_persona_tipo_trabajo_fkey FOREIGN KEY (per_tipo_trabajo_fk) REFERENCES catalogo.sg_tipos_trabajo(ttr_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_secciones ADD CONSTRAINT sec_nombre_jornada_anio_uk UNIQUE (sec_nombre, sec_jornada_fk, sec_anio_lectivo_fk);

ALTER TABLE centros_educativos.sg_organizaciones_curricular_aud     ADD CONSTRAINT 	organizaciones_curricular_revinfo_fk        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_modalidades_aud                   ADD CONSTRAINT 	modalidades_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_niveles_aud                       ADD CONSTRAINT 	niveles_revinfo_fk                          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_ciclos_aud                        ADD CONSTRAINT 	ciclos_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_tipos_calendario_aud              ADD CONSTRAINT 	tipos_calendario_revinfo_fk                 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_aud                ADD CONSTRAINT 	sedes_circulos_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_aud                         ADD CONSTRAINT 	sedes_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_infa_aud           ADD CONSTRAINT 	sedes_circulos_infa_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_ced_ofi_aud                 ADD CONSTRAINT 	sedes_ced_ofi_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_ced_aud                     ADD CONSTRAINT 	sedes_ced_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_ced_pri_aud                 ADD CONSTRAINT 	sedes_ced_pri_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_alfa_aud           ADD CONSTRAINT 	sedes_circulos_alfa_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_opciones_aud                      ADD CONSTRAINT 	opciones_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_telefonos_aud                     ADD CONSTRAINT 	telefonos_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_allegados_aud                     ADD CONSTRAINT 	allegados_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_identificaciones_aud              ADD CONSTRAINT 	identificaciones_revinfo_fk                 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_personas_aud                      ADD CONSTRAINT 	personas_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_direcciones_aud                   ADD CONSTRAINT 	direcciones_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_contactos_emergencia_aud          ADD CONSTRAINT 	contactos_emergencia_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_fichas_salud_aud                  ADD CONSTRAINT 	fichas_salud_revinfo_fk                     FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_manifestacion_violencia_aud       ADD CONSTRAINT 	manifestacion_violencia_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_grados_aud                        ADD CONSTRAINT 	grados_revinfo_fk                           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten_aud           ADD CONSTRAINT 	rel_mod_ed_mod_aten_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_planes_estudio_aud                ADD CONSTRAINT 	planes_estudio_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud       ADD CONSTRAINT 	componente_plan_estudio_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_actividades_especial_aud          ADD CONSTRAINT 	actividades_especial_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_asignaturas_aud                   ADD CONSTRAINT 	asignaturas_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_actividades_tiempo_extendido_aud  ADD CONSTRAINT 	actividades_tiempo_extendido_revinfo_fk     FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_modulos_aud                       ADD CONSTRAINT 	modulos_revinfo_fk                          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_areas_aud                         ADD CONSTRAINT 	areas_revinfo_fk                            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud         ADD CONSTRAINT 	componente_plan_grado_revinfo_fk            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_pruebas_aud                       ADD CONSTRAINT 	pruebas_revinfo_fk                          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_rel_opcion_prog_ed_aud            ADD CONSTRAINT 	rel_opcion_prog_ed_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_diplomados_aud                    ADD CONSTRAINT 	diplomados_revinfo_fk                       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud             ADD CONSTRAINT 	modulos_diplomado_revinfo_fk                FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_docente_aud                       ADD CONSTRAINT 	docente_revinfo_fk                          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_administrativo_aud                ADD CONSTRAINT 	administrativo_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud       ADD CONSTRAINT 	personal_sede_educativa_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_atpi_aud                          ADD CONSTRAINT 	atpi_revinfo_fk                             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_anio_lectivo_aud                  ADD CONSTRAINT 	anio_lectivo_revinfo_fk                     FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_asistencias_aud                   ADD CONSTRAINT 	asistencias_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_noticias_aud                      ADD CONSTRAINT 	noticias_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_matriculas_aud                    ADD CONSTRAINT 	matriculas_revinfo_fk                       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_secciones_aud                     ADD CONSTRAINT 	secciones_revinfo_fk                        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_servicio_educativo_aud            ADD CONSTRAINT 	servicio_educativo_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_gestion_anio_lectivo_aud          ADD CONSTRAINT 	gestion_anio_lectivo_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud    ADD CONSTRAINT 	control_asistencia_cabezal_revinfo_fk       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_calendarios_aud                   ADD CONSTRAINT 	calendarios_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_estudiantes_valoracion_aud        ADD CONSTRAINT 	estudiantes_valoracion_revinfo_fk           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_periodos_calendario_aud           ADD CONSTRAINT 	periodos_calendario_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_actividad_calendario_aud          ADD CONSTRAINT 	actividad_calendario_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_rango_fecha_aud                   ADD CONSTRAINT 	rango_fecha_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_horarios_escolares_aud            ADD CONSTRAINT 	horarios_escolares_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_movimientos_aud                   ADD CONSTRAINT 	movimientos_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud         ADD CONSTRAINT 	periodos_calificacion_revinfo_fk            FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_celda_dia_hora_aud                ADD CONSTRAINT 	celda_dia_hora_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud            ADD CONSTRAINT 	datos_contratacion_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud           ADD CONSTRAINT 	experiencia_laboral_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_jornadas_aud                ADD CONSTRAINT 	sedes_jornadas_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_archivos_aud                      ADD CONSTRAINT 	archivos_revinfo_fk                         FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_idiomas_realizados_aud            ADD CONSTRAINT 	idiomas_realizados_revinfo_fk               FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_nie_aud                           ADD CONSTRAINT 	nie_revinfo_fk                              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_calificaciones_aud                ADD CONSTRAINT 	calificaciones_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_estudios_superiores_aud           ADD CONSTRAINT 	estudios_superiores_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_capacitaciones_aud                ADD CONSTRAINT 	capacitaciones_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud     ADD CONSTRAINT 	calificaciones_estudiante_revinfo_fk        FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_formaciones_docente_aud           ADD CONSTRAINT 	formaciones_docente_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_estudios_realizados_aud           ADD CONSTRAINT 	estudios_realizados_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_datos_empleado_aud                ADD CONSTRAINT 	datos_empleado_revinfo_fk                   FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_sedes_educame_aud                 ADD CONSTRAINT 	sedes_educame_revinfo_fk                    FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_estudiantes_aud                   ADD CONSTRAINT 	estudiantes_revinfo_fk                      FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_rel_sede_servicio_infra_aud       ADD CONSTRAINT 	rel_sede_servicio_infra_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_personas_discapacidades_aud       ADD CONSTRAINT 	personas_discapacidades_revinfo_fk          FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_familiares_aud                    ADD CONSTRAINT 	familiares_revinfo_fk                       FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE centros_educativos.sg_dato_contratacion_jornada_laboral_aud	 ADD CONSTRAINT 	dato_contratacion_jornada_laboral_revinfo_fk	 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_docente_otras_fuentes (pse_pk bigint NOT NULL, CONSTRAINT sg_docente_otras_fuentes_pkey PRIMARY KEY (pse_pk), CONSTRAINT sg_docente_otras_fuentes_personal_sede_fk FOREIGN KEY (pse_pk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_docente_otras_fuentes        IS 'Tabla para el registro de los docentes otra fuentes.';
    COMMENT ON COLUMN centros_educativos.sg_docente_otras_fuentes.pse_pk IS 'Clave foránea que hace referencia al personal sede educativa.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_docente_otras_fuentes_aud (pse_pk bigint NOT NULL, rev bigint, revtype smallint);
CREATE UNIQUE INDEX IF NOT EXISTS sg_personal_sede_educativa_docente_otras_fuentes_pk_idx             ON centros_educativos.sg_docente_otras_fuentes                    USING btree (pse_pk) ;


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_embarazo BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_personas.per_embarazo IS 'Bandera que indica si el registro esta embarazado.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_embarazo BOOLEAN;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_fecha_parto DATE;
COMMENT ON COLUMN centros_educativos.sg_personas.per_fecha_parto IS 'Fecha probable del parto.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_fecha_parto DATE;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_jornada_laboral CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_personas.per_jornada_laboral IS 'Jornada laboral del registro.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_jornada_laboral CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_salario NUMERIC(10,2);
COMMENT ON COLUMN centros_educativos.sg_personas.per_salario IS 'Salario del registro.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_salario NUMERIC(10,2);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_propiedad_vivienda CHARACTER VARYING(40);
COMMENT ON COLUMN centros_educativos.sg_personas.per_propiedad_vivienda IS 'Propiedad de la vivienda del registro.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_propiedad_vivienda CHARACTER VARYING(40);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_servicios_basicos CHARACTER VARYING(40);
COMMENT ON COLUMN centros_educativos.sg_personas.per_servicios_basicos IS 'Servicios basicos del registro.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_servicios_basicos CHARACTER VARYING(40);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_acceso_internet BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_personas.per_acceso_internet IS 'Bandera que indica si el registro tiene acceso al internet.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_acceso_internet BOOLEAN;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_recibe_remesas BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_personas.per_recibe_remesas IS 'Bandera que indica si el registro recibe remesas.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_recibe_remesas BOOLEAN;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tipo_servicio_sanitario_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tipo_servicio_sanitario_fk IS 'Llave foránea que hace referencia al tipo de servicio sanitario.';
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_persona_tipo_servicio_sanitario_fkey FOREIGN KEY (per_tipo_servicio_sanitario_fk) REFERENCES catalogo.sg_tipos_servicio_sanitario(tss_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tipo_servicio_sanitario_fk bigint;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_familiares_emigrados int;
COMMENT ON COLUMN centros_educativos.sg_personas.per_familiares_emigrados IS 'Indica la número de personas del grupo familiar que han emigrado.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_familiares_emigrados int;

ALTER TABLE centros_educativos.sg_secciones ADD CONSTRAINT sg_sec_plan_estudio_fk FOREIGN KEY (sec_plan_estudio_fk) REFERENCES centros_educativos.sg_planes_estudio(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_nombre_partida_nacimiento CHARACTER VARYING(1000);
COMMENT ON COLUMN centros_educativos.sg_personas.per_nombre_partida_nacimiento IS 'Nombre de la persona que aparece en la partida de nacimiento.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_nombre_partida_nacimiento CHARACTER VARYING(1000);

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_observacion_provisional character varying(500);
COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_observacion_provisional IS 'Observación provisional del registro.';
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_observacion_provisional character varying(500);

ALTER TABLE centros_educativos.sg_periodos_calendario ADD COLUMN ces_nombre character varying(500);
COMMENT ON COLUMN centros_educativos.sg_periodos_calendario.ces_nombre IS 'Nombre del registro.';
ALTER TABLE centros_educativos.sg_periodos_calendario_aud ADD COLUMN ces_nombre character varying(500);

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_tipo_periodo_calificacion varchar;
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_tipo_periodo_calificacion IS 'Tipo de período calificación del registro.';
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_tipo_periodo_calificacion varchar;

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_tipo_calendario_escolar varchar;
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_tipo_calendario_escolar IS 'Tipo calendario escolar registro.';
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_tipo_calendario_escolar varchar;

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_numero integer;
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_numero IS 'Número del registro.';
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_numero integer;

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_nup UNIQUE (per_nup);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_nit UNIQUE (per_nit);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_isss UNIQUE (per_isss);
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_inpep UNIQUE (per_inpep);-- Reportes Director

ALTER TABLE centros_educativos.sg_personas 
RENAME COLUMN per_partida_nacimiento_posee_no_presenta TO per_partida_nacimiento_presenta;
ALTER TABLE centros_educativos.sg_personas_aud
RENAME COLUMN per_partida_nacimiento_posee_no_presenta TO per_partida_nacimiento_presenta;
COMMENT ON COLUMN centros_educativos.sg_personas.per_partida_nacimiento_presenta IS 'Registro presenta partida nacimiento.';

ALTER TABLE centros_educativos.sg_estudiantes DROP CONSTRAINT estudiante_persona_uk;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT estudiante_persona_uk UNIQUE (est_persona);

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_reporte_director_rdi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reporte_director (rdi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_reporte_director_rdi_pk_seq'::regclass),rdi_sede_fk bigint,rdi_datos_sede  boolean, rdi_con_observaciones_sede  boolean, rdi_observacion_sede character varying(255),rdi_datos_estudiante  boolean, rdi_con_observaciones_estudiante  boolean, rdi_observacion_estudiante character varying(255), rdi_datos_personal  boolean, rdi_con_observaciones_personal  boolean, rdi_observacion_personal character varying(255), rdi_ult_mod_fecha timestamp without time zone, rdi_ult_mod_usuario character varying(45), rdi_version integer, CONSTRAINT sg_reporte_director_pkey PRIMARY KEY (rdi_pk), CONSTRAINT sg_rdi_sede_fk FOREIGN KEY (rdi_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_reporte_director IS 'Tabla para el registro de reportes director.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_sede_fk IS 'sede del registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_sede IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_sede IS 'Indica si la sede tiene observacion.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_sede IS 'Observacion de la sede.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_estudiante IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_estudiante IS 'Indica si la sede tiene observacion.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_estudiante IS 'Observacion del estudiante.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_personal IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_personal IS 'Indica si tiene observación personal.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_personal IS 'Observacion de la sede.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reporte_director_aud (rdi_pk bigint NOT NULL,rdi_sede_fk bigint,rdi_datos_sede  boolean, rdi_con_observaciones_sede  boolean, rdi_observacion_sede character varying(255),rdi_datos_estudiante  boolean, rdi_con_observaciones_estudiante  boolean, rdi_observacion_estudiante character varying(255), rdi_datos_personal  boolean, rdi_con_observaciones_personal  boolean, rdi_observacion_personal character varying(255), rdi_ult_mod_fecha timestamp without time zone, rdi_ult_mod_usuario character varying(45), rdi_version integer, rev bigint, revtype smallint);


ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_calculo_nota_institucional CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_calculo_nota_institucional IS 'Tipo de calculo nota institucional del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_calculo_nota_institucional CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_funcion_redondeo CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_funcion_redondeo IS 'Tipo de funcion de redondeo del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_funcion_redondeo CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_precision integer;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_precision  IS 'Precision del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_precision integer;

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_calificacion_conceptual_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.cae_calificacion_conceptual_fk IS 'Llave foránea que hace referencia a la calificacion.';
ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD CONSTRAINT sg_calificacion_estudiante_calificacion_fk FOREIGN KEY (cae_calificacion_conceptual_fk) REFERENCES catalogo.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_calificacion_conceptual_fk bigint;

CREATE INDEX sg_actividad_calendario_cal_fk_idx  ON centros_educativos.sg_actividad_calendario  USING btree  (aca_calendario_fk);
CREATE INDEX sg_allegados_persona_fx_idx   ON centros_educativos.sg_allegados (all_persona ASC NULLS LAST);
CREATE INDEX sg_asistencias_control_fk_idx   ON centros_educativos.sg_asistencias (asi_control_fk ASC NULLS LAST);
CREATE INDEX sg_asistencias_estudiante_fk_idx   ON centros_educativos.sg_asistencias (asi_estudiante ASC NULLS LAST);
CREATE INDEX sg_calificaciones_seccion_fk_idx  ON centros_educativos.sg_calificaciones (cal_seccion_fk ASC NULLS LAST);
CREATE INDEX sg_calificaciones_componente_fk_idx   ON centros_educativos.sg_calificaciones (cal_componente_plan_estudio_fk ASC NULLS LAST);
CREATE INDEX sg_calificaciones_rango_fecha_fk_idx   ON centros_educativos.sg_calificaciones (cal_rango_fecha_fk ASC NULLS LAST);
CREATE INDEX sg_calificaciones_estudiante_estudiante_fk_idx   ON centros_educativos.sg_calificaciones_estudiante (cae_estudiante_fk ASC NULLS LAST);
CREATE INDEX sg_calificaciones_estudiante_cal_fk_idx   ON centros_educativos.sg_calificaciones_estudiante (cae_calificacion_fk ASC NULLS LAST);
CREATE INDEX sg_ciclos_niv_fx_idx   ON centros_educativos.sg_ciclos (cic_nivel ASC NULLS LAST);
CREATE INDEX sg_componente_plan_grado_ple_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_plan_estudio_fk ASC NULLS LAST);
CREATE INDEX sg_componente_plan_grado_cpe_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_componente_plan_estudio_fk ASC NULLS LAST);
CREATE INDEX sg_componente_plan_grado_grado_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_grado_fk ASC NULLS LAST);
CREATE INDEX sg_control_asistencia_cabezal_sec_fk_idx   ON centros_educativos.sg_control_asistencia_cabezal (cac_seccion_fk ASC NULLS LAST);
CREATE INDEX sg_estudiantes_per_fk_idx   ON centros_educativos.sg_estudiantes (est_persona ASC NULLS LAST);
CREATE INDEX sg_estudiantes_valoracion_est_fk_idx   ON centros_educativos.sg_estudiantes_valoracion (esv_estudiante_fk ASC NULLS LAST);
CREATE INDEX sg_grados_rel_fk_idx   ON centros_educativos.sg_grados (gra_relacion_modalidad_fk ASC NULLS LAST);
CREATE INDEX sg_horarios_escolares_sec_fk_idx   ON centros_educativos.sg_horarios_escolares (hes_seccion_fk ASC NULLS LAST);
CREATE INDEX sg_identificaciones_per_fk_idx   ON centros_educativos.sg_identificaciones (ide_persona ASC NULLS LAST);
CREATE INDEX sg_manifestacion_violencia_mvi_estudiante_fk_idx   ON centros_educativos.sg_manifestacion_violencia (mvi_estudiante ASC NULLS LAST);
CREATE INDEX sg_matriculas_est_fk_idx   ON centros_educativos.sg_matriculas (mat_estudiante_fk ASC NULLS LAST);
CREATE INDEX sg_matriculas_sec_fk_idx   ON centros_educativos.sg_matriculas (mat_seccion_fk ASC NULLS LAST);
CREATE INDEX sg_periodos_calificacion_mod_fk_idx   ON centros_educativos.sg_periodos_calificacion (pca_modalidad_educativa_fk ASC NULLS LAST);
CREATE INDEX sg_periodos_calificacion_mat_fk_idx   ON centros_educativos.sg_periodos_calificacion (pca_modalidad_atencion_fk ASC NULLS LAST);
CREATE INDEX sg_periodos_calificacion_cal_fk_idx   ON centros_educativos.sg_periodos_calificacion (pca_calendario_fk ASC NULLS LAST);
CREATE INDEX sg_personal_sede_educativa_per_fk_idx   ON centros_educativos.sg_personal_sede_educativa (pse_persona_fk ASC NULLS LAST);
CREATE INDEX sg_personas_nie_idx   ON centros_educativos.sg_personas (per_nie ASC NULLS LAST);
CREATE INDEX sg_personas_dui_idx   ON centros_educativos.sg_personas (per_dui ASC NULLS LAST);
CREATE INDEX sg_rango_fecha_pca_fk_idx   ON centros_educativos.sg_rango_fecha (rfe_periodo_calificacion_fk ASC NULLS LAST);
CREATE INDEX sg_rel_mod_ed_mod_aten_mod_fk_idx   ON centros_educativos.sg_rel_mod_ed_mod_aten (rea_modalidad_educativa_fk ASC NULLS LAST);
CREATE INDEX sg_rel_mod_ed_mod_aten_ate_fk_idx   ON centros_educativos.sg_rel_mod_ed_mod_aten (rea_sub_modalidad_atencion_fk ASC NULLS LAST);
CREATE INDEX sg_secciones_sed_fk_idx   ON centros_educativos.sg_secciones (sec_servicio_educativo_fk ASC NULLS LAST);
CREATE INDEX sg_sedes_dir_fk_idx ON centros_educativos.sg_sedes (sed_direccion_fk ASC NULLS LAST);
CREATE INDEX sg_sedes_codigo_idx ON centros_educativos.sg_sedes (sed_codigo ASC NULLS LAST);
CREATE INDEX sg_servicio_Educativo_sed_fk_idx ON centros_educativos.sg_servicio_educativo (sdu_sede_fk ASC NULLS LAST);
CREATE INDEX sg_servicio_educativo_ped_fk_idx ON centros_educativos.sg_servicio_educativo (sdu_programa_educativo_fk ASC NULLS LAST);


ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_obs_anu_retiro CHARACTER VARYING(255);
COMMENT ON COLUMN centros_educativos.sg_matriculas.mat_obs_anu_retiro IS 'Observación de la anulación del retiro.';
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_obs_anu_retiro CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_personas 
RENAME COLUMN per_partida_nacimiento_posee_no_presenta TO per_partida_nacimiento_presenta;
ALTER TABLE centros_educativos.sg_personas_aud
RENAME COLUMN per_partida_nacimiento_posee_no_presenta TO per_partida_nacimiento_presenta;
COMMENT ON COLUMN centros_educativos.sg_personas.per_partida_nacimiento_presenta IS 'Registro presenta partida nacimiento.';
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nacimiento_presenta BOOLEAN;

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_reporte_director_rdi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reporte_director (rdi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_reporte_director_rdi_pk_seq'::regclass),rdi_sede_fk bigint,rdi_datos_sede  boolean, rdi_con_observaciones_sede  boolean, rdi_observacion_sede character varying(255),rdi_datos_estudiante  boolean, rdi_con_observaciones_estudiante  boolean, rdi_observacion_estudiante character varying(255), rdi_datos_personal  boolean, rdi_con_observaciones_personal  boolean, rdi_observacion_personal character varying(255), rdi_ult_mod_fecha timestamp without time zone, rdi_ult_mod_usuario character varying(45), rdi_version integer, CONSTRAINT sg_reporte_director_pkey PRIMARY KEY (rdi_pk), CONSTRAINT sg_rdi_sede_fk FOREIGN KEY (rdi_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_reporte_director IS 'Tabla para el registro de reportes director.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_sede_fk IS 'sede del registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_sede IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_sede IS 'Indica si la sede tiene observacion.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_sede IS 'Observacion de la sede.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_estudiante IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_estudiante IS 'Indica si la sede tiene observacion.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_estudiante IS 'Observacion de la sede.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_datos_personal IS 'Indica si la sede se reportó.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_con_observaciones_personal IS 'Indica si la sede tiene observacion.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_observacion_personal IS 'Observacion de la sede.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reporte_director.rdi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reporte_director_aud (rdi_pk bigint NOT NULL,rdi_sede_fk bigint,rdi_datos_sede  boolean, rdi_con_observaciones_sede  boolean, rdi_observacion_sede character varying(255),rdi_datos_estudiante  boolean, rdi_con_observaciones_estudiante  boolean, rdi_observacion_estudiante character varying(255), rdi_datos_personal  boolean, rdi_con_observaciones_personal  boolean, rdi_observacion_personal character varying(255), rdi_ult_mod_fecha timestamp without time zone, rdi_ult_mod_usuario character varying(45), rdi_version integer, rev bigint, revtype smallint);

ALTER TABLE centros_educativos.sg_reporte_director ALTER COLUMN rdi_observacion_sede type text;
ALTER TABLE centros_educativos.sg_reporte_director_aud ALTER COLUMN rdi_observacion_sede type text;
ALTER TABLE centros_educativos.sg_reporte_director ALTER COLUMN rdi_con_observaciones_personal type text;
ALTER TABLE centros_educativos.sg_reporte_director_aud ALTER COLUMN rdi_con_observaciones_personal type text;
ALTER TABLE centros_educativos.sg_reporte_director ALTER COLUMN rdi_con_observaciones_estudiante type text;
ALTER TABLE centros_educativos.sg_reporte_director_aud ALTER COLUMN rdi_con_observaciones_estudiante type text;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_calculo_nota_institucional CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_calculo_nota_institucional IS 'Tipo de calculo nota institucional del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_calculo_nota_institucional CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_funcion_redondeo CHARACTER VARYING(20);
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_funcion_redondeo IS 'Tipo de funcion de redondeo del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_funcion_redondeo CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_precision integer;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_precision  IS 'Precision del registro.';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_precision integer;

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_calificacion_conceptual_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.cae_calificacion_conceptual_fk IS 'Llave foránea que hace referencia a la calificacion.';
ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD CONSTRAINT sg_calificacion_estudiante_calificacion_fk FOREIGN KEY (cae_calificacion_conceptual_fk) REFERENCES catalogo.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_calificacion_conceptual_fk bigint;

ALTER TABLE centros_educativos.sg_estudiantes DROP CONSTRAINT estudiante_persona_uk;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT estudiante_persona_uk UNIQUE (est_persona);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CICLO','E1',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CICLO','E2',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CICLO','E3',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIRECCION','E4',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIRECCION','E5',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIRECCION','E6',  '', 1, true, null, null,0);
    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_GRADO','E7',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_GRADO','E8',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_GRADO','E9',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODALIDAD','E10',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODALIDAD','E11',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODALIDAD','E12',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIVEL','E13',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIVEL','E14',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIVEL','E15',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_OPCION','E16',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_OPCION','E17',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_OPCION','E18',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ORGANIZACION_CURRICULAR','E19',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ORGANIZACION_CURRICULAR','E20',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ORGANIZACION_CURRICULAR','E21',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SEDES','E24',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PERSONA','E25',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PERSONA','E26',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PERSONA','E27',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TELEFONO','E28',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TELEFONO','E29',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TELEFONO','E30',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTUDIANTE','E31',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTUDIANTE','E32',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTUDIANTE','E33',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IDENTIFICACIONES','E34',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IDENTIFICACIONES','E35',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IDENTIFICACIONES','E36',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CENTRO_EDUCATIVO_PRIVADO','E37',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CENTRO_EDUCATIVO_PRIVADO','E38',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CENTRO_EDUCATIVO_PRIVADO','E39',  '', 1, true, null, null,0);   
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RELACION_MODALIDAD_EDUCATIVA_ATENCION','E40',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RELACION_MODALIDAD_EDUCATIVA_ATENCION','E41',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RELACION_MODALIDAD_EDUCATIVA_ATENCION','E42',  '', 1, true, null, null,0); 
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PLANES_ESTUDIO','E43',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PLANES_ESTUDIO','E44',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PLANES_ESTUDIO','E45',  '', 1, true, null, null,0);          
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_COMPONENTES_GRADO','E46',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_COMPONENTES_GRADO','E47',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_COMPONENTES_GRADO','E48',  '', 1, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FICHAS_SALUD','E49',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FICHAS_SALUD','E50',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FICHAS_SALUD','E51',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MANIFESTACIONES_VIOLENCIA','E52',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MANIFESTACIONES_VIOLENCIA','E53',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MANIFESTACIONES_VIOLENCIA','E54',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASISTENCIAS','E55',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASISTENCIAS','E56',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASISTENCIAS','E57',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CENTRO_EDUCATIVO_OFICIAL','E58',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CENTRO_EDUCATIVO_OFICIAL','E59',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CENTRO_EDUCATIVO_OFICIAL','E60',  '', 1, true, null, null,0);    
    
   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASIGNATURA','E61',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASIGNATURA','E62',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASIGNATURA','E63',  '', 1, true, null, null,0);       
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RELACION_OPCION_PROGRAMA_EDUCATIVO','E64',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RELACION_OPCION_PROGRAMA_EDUCATIVO','E65',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RELACION_OPCION_PROGRAMA_EDUCATIVO','E66',  '', 1, true, null, null,0);   

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIPLOMAS','E67',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIPLOMAS','E68',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIPLOMAS','E69',  '', 1, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODULOS_DIPLOMA','E70',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODULOS_DIPLOMA','E71',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODULOS_DIPLOMA','E72',  '', 1, true, null, null,0);   
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_AREAS','E73',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_AREAS','E74',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_AREAS','E75',  '', 1, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODULOS','E76',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODULOS','E77',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODULOS','E78',  '', 1, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACTIVIDADES_ESPECIALES','E79',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACTIVIDADES_ESPECIALES','E80',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACTIVIDADES_ESPECIALES','E81',  '', 1, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PRUEBAS','E82',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PRUEBAS','E83',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PRUEBAS','E84',  '', 1, true, null, null,0);     
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACTIVIDADES_TIEMPO_EXTENDIDO','E85',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACTIVIDADES_TIEMPO_EXTENDIDO','E86',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACTIVIDADES_TIEMPO_EXTENDIDO','E87',  '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SEDE_CIRCULO_INFANCIA','E88',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SEDE_CIRCULO_INFANCIA','E89',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SEDE_CIRCULO_INFANCIA','E90',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SEDE_CIRCULO_ALFABETIZACION','E91',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SEDE_CIRCULO_ALFABETIZACION','E92',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SEDE_CIRCULO_ALFABETIZACION','E93',  '', 1, true, null, null,0);

    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DOCENTE','E94',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DOCENTE','E95',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DOCENTE','E96',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ATPI','E97',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ATPI','E98',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ATPI','E99',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ADMINISTRATIVO','E100',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ADMINISTRATIVO','E101',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ADMINISTRATIVO','E102',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SERVICIO_EDUCATIVO','E103',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SERVICIO_EDUCATIVO','E104',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SERVICIO_EDUCATIVO','E105',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_REPORTE_MATRICULA_SECCION','E106',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PERSONAL_SEDE_EDUCATIVA','E108',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ANIO_LECTIVO','E109',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ANIO_LECTIVO','E110',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ANIO_LECTIVO','E111',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SECCION','E112',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SECCION','E113',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SECCION','E114',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MATRICULA','E115',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MATRICULA','E116',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MATRICULA','E117',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NOTICIA','E118',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NOTICIA','E119',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NOTICIA','E120',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_GESTION_ANIO_LECTIVO','E121',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_GESTION_ANIO_LECTIVO','E122',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_GESTION_ANIO_LECTIVO','E123',  '', 1, true, null, null,0);

  
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALENDARIOS','E124',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALENDARIOS','E125',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALENDARIOS','E126',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TIPOS_CALENDARIO','E127',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TIPOS_CALENDARIO','E128',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TIPOS_CALENDARIO','E129',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACTIVIDAD_CALENDARIO','E130',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACTIVIDAD_CALENDARIO','E131',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACTIVIDAD_CALENDARIO','E132',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALENDARIOS_ESCOLARES','E133',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALENDARIOS_ESCOLARES','E134',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALENDARIOS_ESCOLARES','E135',  '', 1, true, null, null,0);
       
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTUDIANTE_VALORACION','E136',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTUDIANTE_VALORACION','E137',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTUDIANTE_VALORACION','E138',  '', 1, true, null, null,0);    
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MOVIMIENTOS','E139',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MOVIMIENTOS','E140',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MOVIMIENTOS','E141',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PERIODOS_CALIFICACION','E142',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PERIODOS_CALIFICACION','E143',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PERIODOS_CALIFICACION','E144',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_RANGOS_FECHA','E145',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_RANGOS_FECHA','E146',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_RANGOS_FECHA','E147',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONTROL_ASISTENCIA_CABEZAL','E148',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONTROL_ASISTENCIA_CABEZAL','E149',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONTROL_ASISTENCIA_CABEZAL','E150',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DATO_EMPLEADO','E151',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DATO_EMPLEADO','E152',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DATO_EMPLEADO','E153',  '', 1, true, null, null,0);
    

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CELDAS_DIA_HORA','E154',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CELDAS_DIA_HORA','E155',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CELDAS_DIA_HORA','E156',  '', 1, true, null, null,0);
            
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACIONES_ESTUDIANTE','E157',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACIONES_ESTUDIANTE','E158',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACIONES_ESTUDIANTE','E159',  '', 1, true, null, null,0);
            
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACIONES','E160',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CALIFICACIONES','E161',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CALIFICACIONES','E162',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DATO_CONTRATACION','E163',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DATO_CONTRATACION','E164',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DATO_CONTRATACION','E165',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTUDIOS_REALIZADOS','E166',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTUDIOS_REALIZADOS','E167',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTUDIOS_REALIZADOS','E168',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_IDIOMAS_REALIZADOS','E169',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_IDIOMAS_REALIZADOS','E170',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_IDIOMAS_REALIZADOS','E171',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTUDIOS_SUPERIORES','E172',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTUDIOS_SUPERIORES','E173',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTUDIOS_SUPERIORES','E174',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CAPACITACIONES','E175',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CAPACITACIONES','E176',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CAPACITACIONES','E177',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EXPERIENCIA_LABORAL','E178',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EXPERIENCIA_LABORAL','E179',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EXPERIENCIA_LABORAL','E180',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_HORARIOS_ESCOLARES','E181',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_HORARIOS_ESCOLARES','E182',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_HORARIOS_ESCOLARES','E183',  '', 1, true, null, null,0);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FAMILIARES','E184',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FAMILIARES','E185',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FAMILIARES','E186',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONTACTOS_EMERGENCIA','E187',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONTACTOS_EMERGENCIA','E188',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONTACTOS_EMERGENCIA','E189',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NIE','E190',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NIE','E191',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NIE','E192',  '', 1, true, null, null,0);
 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FORMACIONES_DOCENTE','E193',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FORMACIONES_DOCENTE','E194',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FORMACIONES_DOCENTE','E195',  '', 1, true, null, null,0);   

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_EDUCAME','E196', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_EDUCAME','E197', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_EDUCAME','E198', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DOCENTE_OTRAS_FUENTES','E199', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DOCENTE_OTRAS_FUENTES','E200', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DOCENTE_OTRAS_FUENTES','E201', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REPORTE_DIRECTOR','E202', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REPORTE_DIRECTOR','E203', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REPORTE_DIRECTOR','E204', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_MATRICULA_PROVISIONAL','E205', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_SECCION_ESTUDIANTE','E206', '', 1, true, null, null,0); 


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SELLO_FIRMA','E207', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SELLO_FIRMA','E208', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SELLO_FIRMA','E209', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SELLO_FIRMA_TITULAR','E210', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SELLO_FIRMA_TITULAR','E211', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SELLO_FIRMA_TITULAR','E212', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_IMPRESION','E213', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_IMPRESION','E214', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_IMPRESION','E215', '', 1, true, null, null,0); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SEDES','EB1',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ESTUDIANTES','EB2',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ACTIVIDAD_CALENDARIO','EB3',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ANIO_LECTIVO','EB4',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SERVICIO_EDUCATIVO','EB5',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TELEFONO','EB6',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALENDARIOS_ESCOLARES','EB7',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACIONES','EB8',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RELACION_OPCION_PROGRAMA_EDUCATIVO','EB9',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SECCION','EB10',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ASISTENCIAS','EB11',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALENDARIOS','EB12',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACIONES_ESTUDIANTE','EB13',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CAPACITACIONES','EB14',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CELDAS_DIA_HORA','EB15',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CICLO','EB16',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_COMPONENTES_GRADO','EB17',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONTACTOS_EMERGENCIA','EB18',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONTROL_ASISTENCIA_CABEZAL','EB19',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DATO_CONTRATACION','EB20',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DATO_EMPLEADO','EB21',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIPLOMAS','EB22',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIRECCION','EB23',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RELACION_MODALIDAD_EDUCATIVA_ATENCION','EB24',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_RANGOS_FECHA','EB25',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ESTUDIANTE_VALORACION','EB26',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ESTUDIOS_REALIZADOS','EB27',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ESTUDIOS_SUPERIORES','EB28',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_EXPERIENCIA_LABORAL','EB29',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FAMILIARES','EB30',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FICHAS_SALUD','EB31',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_FORMACIONES_DOCENTE','EB32',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_GESTION_ANIO_LECTIVO','EB33',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_GRADO','EB34',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_HORARIOS_ESCOLARES','EB35',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_IDENTIFICACIONES','EB36',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_IDIOMAS_REALIZADOS','EB37',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MANIFESTACIONES_VIOLENCIA','EB38',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MATRICULA','EB39',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MODALIDAD','EB40',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PLANES_ESTUDIO','EB41',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MODULOS_DIPLOMA','EB42',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MOVIMIENTOS','EB43',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NIE','EB44',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NIVEL','EB45',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NOTICIA','EB46',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_OPCION','EB47',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ORGANIZACION_CURRICULAR','EB48',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PERIODOS_CALIFICACION','EB49',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PERSONA','EB50',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PERSONAL_SEDE_EDUCATIVA','EB51',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REPORTE_DIRECTOR','EB52',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SELLO_FIRMA','EB53',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SELLO_FIRMA_TITULAR','EB54',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_IMPRESION','EB55',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_GENERACION_TITULO','EB56',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_BUSCAR_REL_SEDE_RIESGO_AFECTACION','EB57',  '', 1, true, null, null,0);


-- Menú
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ORGANIZACION_CURRICULAR','EM1',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PLANES_ESTUDIO','EM2',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_COMPONENTES_GRADO','EM3',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DIPLOMAS','EM4',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_GESTION_ANIO_LECTIVO','EM5',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALENDARIOS_ESCOLARES','EM6',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALENDARIOS','EM7',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SEDES','EM8',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SECCION','EM9',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SERVICIO_EDUCATIVO','EM10',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_HORARIOS_ESCOLARES','EM11',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ESTUDIANTES','EM12',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_MATRICULA','EM13',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONTROL_ASISTENCIA_CABEZAL','EM14',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ASISTENCIAS','EM15',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACIONES','EM16',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACIONES_ESTUDIANTE','EM17',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PERIODOS_CALIFICACION','EM18',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PERSONAL_SEDE_EDUCATIVA','EM19',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PERSONA','EM20',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_NOTICIA','EM21',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ANIO_LECTIVO','EM22',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REVISION_INFORMACION','EM23',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REVISION_DATOS','EM24',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SELLOS_FIRMAS','EM25',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SELLOS_FIRMAS_TITULAR','EM26',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOLICITUDES_IMPRESIONES','EM27',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_GENERACION_TITULO','EM28',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_PERSONALES_ESTUDIANTE','EP1',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_DATOS_PERSONALES_ESTUDIANTE','EP2',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_RESPONSABLE_Y_FAMILIARES_ESTUDIANTE','EP3',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_RESPONSABLE_Y_FAMILIARES_ESTUDIANTE','EP4',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_MANIFESTACION_VIOLENCIA_ESTUDIANTE','EP5',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_MANIFESTACION_VIOLENCIA_ESTUDIANTE','EP6',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_VALORACION_DOCENTE_ESTUDIANTE','EP7',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_VALORACION_DOCENTE_ESTUDIANTE','EP8',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BITACORA_MATRICULA_ESTUDIANTE','EP9',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_BITACORA_MATRICULA_ESTUDIANTE','EP10',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_PERSONA_PERSONAL_SEDE','EP11',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_PERSONA_PERSONAL_SEDE','EP12',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_EMPLEADO_PERSONAL_SEDE','EP13',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_EMPLEADO_PERSONAL_SEDE','EP14',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ESTUDIOS_REALIZADOS_PERSONAL_SEDE','EP15',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_ESTUDIOS_REALIZADOS_PERSONAL_SEDE','EP16',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_FORMACION_DOCENTE_PERSONAL_SEDE','EP17',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_FORMACION_DOCENTE_PERSONAL_SEDE','EP18',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_FAMILIARES_PERSONAL_SEDE','EP19',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_FAMILIARES_PERSONAL_SEDE','EP20',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_IDENTIDAD_PERSONA','EP21',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_DATOS_SENSIBLES_PERSONA','EP22',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_ESCALAFON','EP23',  '', 1, true, null, null,0);


--2.0.0


-- Sellos Firmas
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sellos_firmas_sfi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sellos_firmas (sfi_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_sellos_firmas_sfi_pk_seq'::regclass),sfi_persona_fk bigint,sfi_sello_archivo_fk bigint,sfi_tipo_representante_fk bigint,sfi_sede_fk bigint,sfi_firma_archivo_fk bigint, sfi_habilitado boolean,sfi_comentario character varying(500),sfi_fecha_desde date,sfi_fecha_hasta date,sfi_aclaracion_firma character varying(255),sfi_numero_documento character varying(255),sfi_fecha_documento date,sfi_institucion character varying(255), sfi_ult_mod_fecha timestamp without time zone, sfi_ult_mod_usuario character varying(45), sfi_version integer, CONSTRAINT sg_sellos_firmas_pkey PRIMARY KEY (sfi_pk),CONSTRAINT sg_sfi_sede_fkey FOREIGN KEY (sfi_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sfi_tipo_representante_fkey FOREIGN KEY (sfi_tipo_representante_fk) REFERENCES catalogo.sg_tipo_representante(tre_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sfi_firma_archivo_fkey FOREIGN KEY (sfi_firma_archivo_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sfi_sello_archivo_fkey FOREIGN KEY (sfi_sello_archivo_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sfi_persona_fkey FOREIGN KEY (sfi_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_sellos_firmas IS 'Tabla para el registro de sellos firmas.';
COMMENT ON COLUMN centros_educativos.sg_sellos_firmas.sfi_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_sellos_firmas.sfi_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_sellos_firmas.sfi_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sellos_firmas.sfi_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sellos_firmas.sfi_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sellos_firmas_aud (sfi_pk bigint NOT NULL,sfi_persona_fk bigint,sfi_sello_archivo_fk bigint,sfi_tipo_representante_fk bigint,sfi_sede_fk bigint,sfi_firma_archivo_fk bigint, sfi_habilitado boolean,sfi_comentario character varying(500),sfi_fecha_desde date,sfi_fecha_hasta date,sfi_aclaracion_firma character varying(255),sfi_numero_documento character varying(255),sfi_fecha_documento date,sfi_institucion character varying(255), sfi_ult_mod_fecha timestamp without time zone, sfi_ult_mod_usuario character varying(45), sfi_version integer, rev bigint, revtype smallint);


-- Sellos Firmas Titulares
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sello_firma_titular_sft_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sello_firma_titular (sft_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_sello_firma_titular_sft_pk_seq'::regclass), sft_primer_nombre character varying(255), sft_segundo_nombre character varying(255),sft_primer_apellido character varying(255),sft_segundo_apellido character varying(255),sft_nombre_busqueda character varying(500),sft_firma_archivo_fk bigint,sft_cargo_titular_fk bigint,sft_fecha_desde date,sft_fecha_hasta date,sft_observaciones character varying(500),sft_sello_archivo_fk bigint, sft_habilitado boolean,  sft_ult_mod_fecha timestamp without time zone, sft_ult_mod_usuario character varying(45), sft_version integer, CONSTRAINT sg_sft_firma_archivo_fkey FOREIGN KEY (sft_firma_archivo_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sft_cargo_titular_fkey FOREIGN KEY (sft_cargo_titular_fk) REFERENCES catalogo.sg_cargo_titular(cti_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_sft_sello_archivo_fkey FOREIGN KEY (sft_sello_archivo_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );
COMMENT ON TABLE centros_educativos.sg_sello_firma_titular IS 'Tabla para el registro de sellos firmas titulares.';
COMMENT ON COLUMN centros_educativos.sg_sello_firma_titular.sft_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_sello_firma_titular.sft_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_sello_firma_titular.sft_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sello_firma_titular.sft_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sello_firma_titular.sft_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sello_firma_titular_aud (sft_pk bigint NOT NULL,sft_primer_nombre character varying(255), sft_segundo_nombre character varying(255),sft_primer_apellido character varying(255),sft_segundo_apellido character varying(255),sft_nombre_busqueda character varying(500),sft_firma_archivo_fk bigint,sft_cargo_titular_fk bigint,sft_fecha_desde date,sft_fecha_hasta date,sft_observaciones character varying(500),sft_sello_archivo_fk bigint, sft_habilitado boolean, sft_ult_mod_fecha timestamp without time zone, sft_ult_mod_usuario character varying(45), sft_version integer, rev bigint, revtype smallint);


-- Solicitudes Impresion
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_solicitudes_impresion_sim_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitudes_impresion (sim_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_solicitudes_impresion_sim_pk_seq'::regclass),sim_seccion_fk bigint, sim_fecha_solicitud  date,  sim_usuario_fk bigint, sim_estado CHARACTER VARYING(50), sim_ult_mod_fecha timestamp without time zone, sim_ult_mod_usuario character varying(45), sim_version integer, CONSTRAINT sg_solicitudes_impresion_pkey PRIMARY KEY (sim_pk),CONSTRAINT sg_sim_seccion_fk FOREIGN KEY (sim_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE ,CONSTRAINT sg_sim_usuario_fk FOREIGN KEY (sim_usuario_fk) REFERENCES seguridad.sg_usuarios(usu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );
COMMENT ON TABLE centros_educativos.sg_solicitudes_impresion IS 'Tabla para el registro de solicitudes impresion.';
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitudes_impresion_aud (sim_pk bigint NOT NULL,sim_seccion_fk bigint, sim_fecha_solicitud  date,  sim_usuario_fk bigint, sim_estado CHARACTER VARYING(50),  sim_ult_mod_fecha timestamp without time zone, sim_ult_mod_usuario character varying(45), sim_version integer, rev bigint, revtype smallint);


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estudiante_impresion_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudiante_impresion (eim_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_estudiante_impresion_pk_seq'::regclass), eim_solicitud_impresion_fk bigint, eim_estudiante_fk bigint, eim_habilitado_imprimir boolean,eim_ult_mod_fecha timestamp without time zone, eim_ult_mod_usuario character varying(45), eim_version integer, CONSTRAINT sg_estudiante_impresion_pkey PRIMARY KEY (eim_pk), CONSTRAINT sg_estudiante_impresion_solicitud_impresion_fkey FOREIGN KEY (eim_solicitud_impresion_fk) REFERENCES centros_educativos.sg_solicitudes_impresion(sim_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_estudiante_impresion_estudiante_fkey FOREIGN KEY (eim_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes (est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_estudiante_impresion                              IS 'Tabla para el registro de la relación entre opción y programa educativo.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_pk                       IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_solicitud_impresion_fk   IS 'Clave foránea que hace referencia a la solicitud impresion.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_estudiante_fk    IS 'Clave foránea que hace referencia alestudiante.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_habilitado_imprimir               IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_ult_mod_fecha            IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_ult_mod_usuario          IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_version                  IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estudiante_impresion_aud (eim_pk bigint NOT NULL, eim_solicitud_impresion_fk bigint, eim_estudiante_fk bigint, eim_habilitado_imprimir boolean,eim_ult_mod_fecha timestamp without time zone, eim_ult_mod_usuario character varying(45), eim_version integer, rev bigint, revtype smallint);


ALTER TABLE centros_educativos.sg_estudiante_impresion ADD COLUMN eim_numero_correlativo integer;
COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_numero_correlativo  IS 'Número correlativo de estudiante.';
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud ADD COLUMN eim_numero_correlativo integer;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_inicio_numero_correlativo integer;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_inicio_numero_correlativo  IS 'Número correlativo inicial.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_inicio_numero_correlativo integer;


ALTER TABLE centros_educativos.sg_estudiante_impresion ADD COLUMN eim_codigo_hash CHARACTER VARYING(500);
COMMENT ON COLUMN centros_educativos.sg_estudiante_impresion.eim_codigo_hash  IS 'Código hash del registro.';
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud ADD COLUMN eim_codigo_hash CHARACTER VARYING(500);

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_fecha_enviado_imprimir date;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_fecha_enviado_imprimir IS 'Fecha enviado imprimir.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_fecha_enviado_imprimir date;

ALTER TABLE centros_educativos.sg_horarios_escolares ADD CONSTRAINT hes_seccion_uk UNIQUE (hes_seccion_fk);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_BUSCAR_ESTUDIANTES_RETIRADOS','EP24',  '', 1, true, null, null,0);


-- 2.2.0

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_fecha_realizado date;
COMMENT ON COLUMN centros_educativos.sg_calificaciones_estudiante.cae_fecha_realizado IS 'Fecha realizado prueba.';
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_fecha_realizado date;

CREATE TABLE IF NOT EXISTS centros_educativos.sg_grados_definicion_titulo (gra_pk bigint NOT NULL,dti_pk bigint NOT NULL, CONSTRAINT sg_grados_definicion_titulo_grados_fk FOREIGN KEY (gra_pk) REFERENCES centros_educativos.sg_grados (gra_pk), CONSTRAINT sg_grados_definicion_titulo_definicion_titulo_fk FOREIGN KEY (dti_pk) REFERENCES catalogo.sg_definiciones_titulo (dti_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_grados_definicion_titulo_aud(gra_pk bigint NOT NULL,dti_pk bigint NOT NULL, rev bigint, revtype smallint);


ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_definicion_titulo_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_definicion_titulo_fk IS 'Llave foránea que hace referencia a la definición de título.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_definicion_titulo_fk FOREIGN KEY (sim_definicion_titulo_fk) REFERENCES catalogo.sg_definiciones_titulo (dti_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_definicion_titulo_fk bigint;

ALTER TABLE centros_educativos.sg_sello_firma_titular ADD CONSTRAINT sg_sello_firma_titular_pkey PRIMARY KEY (sft_pk);

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_sello_firma_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_sello_firma_fk IS 'Llave foránea que hace referencia a sello firma.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_sello_firma_fk FOREIGN KEY (sim_sello_firma_fk) REFERENCES centros_educativos.sg_sellos_firmas(sfi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_sello_firma_fk bigint;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_sello_firma_titular_ministro_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_sello_firma_titular_ministro_fk IS 'Llave foránea que hace referencia a sello firma titular ministro.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_sello_firma_titular_ministro_fk FOREIGN KEY (sim_sello_firma_titular_ministro_fk) REFERENCES centros_educativos.sg_sello_firma_titular(sft_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_sello_firma_titular_ministro_fk bigint;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_sello_firma_titular_autentica_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_impresion.sim_sello_firma_titular_autentica_fk IS 'Llave foránea que hace referencia a sello firma titular autentica.';
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_sello_firma_titular_autentica_fk FOREIGN KEY (sim_sello_firma_titular_autentica_fk) REFERENCES centros_educativos.sg_sello_firma_titular(sft_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_sello_firma_titular_autentica_fk bigint;

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_acceso_internet boolean;
COMMENT ON COLUMN centros_educativos.sg_secciones.sec_acceso_internet IS 'Acceso a internet de registro.';
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_acceso_internet boolean;

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_acceso_computadora boolean;
COMMENT ON COLUMN centros_educativos.sg_secciones.sec_acceso_computadora IS 'Acceso a computadora de registro.';
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_acceso_computadora boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_casos_violacion integer;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_casos_violacion IS 'Cantidad de casos de violación.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_casos_violacion integer;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_religiosa boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_religiosa IS 'Indica si sede es religiosa.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_religiosa boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_religion CHARACTER VARYING(255);
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_religion IS 'Indica religión de sede.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_religion CHARACTER VARYING(255);


ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_internet boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_internet IS 'Acceso a internet de registro.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_internet boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_velocidad_internet CHARACTER VARYING(255);
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_velocidad_internet IS 'Indica religión de sede.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_velocidad_internet CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_proveedor_internet CHARACTER VARYING(255);
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_proveedor_internet IS 'Indica religión de sede.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_proveedor_internet CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_consejo_consultivo_educacion boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_consejo_consultivo_educacion IS 'Indica si sede tiene consejo consultivo educacion.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_consejo_consultivo_educacion boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_comite_pedagogico boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_comite_pedagogico IS 'Indica si sede tiene comite pedagógico.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_comite_pedagogico boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_comite_padres_madres boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_comite_padres_madres IS 'Indica si sede es religiosa.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_comite_padres_madres boolean;

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_comite_estudiantil boolean;
COMMENT ON COLUMN centros_educativos.sg_sedes.sed_comite_estudiantil IS 'Indica si sede es religiosa.';
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_comite_estudiantil boolean;

-- Rel Sede Riesgo Afectacion
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_sede_riesgo_afectacion_rar_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_riesgo_afectacion (rar_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_sede_riesgo_afectacion_rar_pk_seq'::regclass),rar_grado_afectacion_fk bigint, rar_tipo_riesgo_fk bigint,   rar_ult_mod_fecha timestamp without time zone, rar_ult_mod_usuario character varying(45), rar_version integer, CONSTRAINT sg_rel_sede_riesgo_afectacion_pkey PRIMARY KEY (rar_pk), CONSTRAINT sg_rar_grado_afectacion_fk FOREIGN KEY (rar_grado_afectacion_fk) REFERENCES catalogo.sg_grados_afectacion(gaf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_rar_tipo_riesgo_fk FOREIGN KEY (rar_tipo_riesgo_fk) REFERENCES catalogo.sg_tipos_riesgo(tri_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_rel_sede_riesgo_afectacion IS 'Tabla para el registro de rel sede riesgo afectacion.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_grado_afectacion_fk IS 'Grado afectacion del registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_tipo_riesgo_fk IS 'tipo_riesgo del registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_sede_riesgo_afectacion.rar_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_sede_riesgo_afectacion_aud (rar_pk bigint NOT NULL, rar_grado_afectacion_fk bigint, rar_tipo_riesgo_fk bigint, rar_ult_mod_fecha timestamp without time zone, rar_ult_mod_usuario character varying(45), rar_version integer, rev bigint, revtype smallint);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_factor_riesgo (sed_pk bigint NOT NULL,rar_pk bigint NOT NULL, CONSTRAINT sg_sedes_factor_riesgo_sede_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes (sed_pk), CONSTRAINT sg_sedes_factor_riesgo_factor_riesgo_fk FOREIGN KEY (rar_pk) REFERENCES centros_educativos.sg_rel_sede_riesgo_afectacion (rar_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_sedes_factor_riesgo_aud(sed_pk bigint NOT NULL,rar_pk bigint NOT NULL, rev bigint, revtype smallint);


-- Solicitudes Traslado
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_solicitudes_traslado_sot_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitudes_traslado (sot_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_solicitudes_traslado_sot_pk_seq'::regclass), sot_sede_solicitante_fk bigint, sot_estudiante_fk bigint,  sot_servicio_educativo_solicitado_fk bigint, sot_sede_origen_fk bigint,  sot_servicio_educativo_origen_fk bigint,  sot_estado CHARACTER VARYING(45),  sot_usuario_solicitud_fk bigint,  sot_fecha_solicitud date,  sot_observacion CHARACTER VARYING(500),  sot_resolucion CHARACTER VARYING(500), sot_motivo_traslado_fk bigint, sot_fecha_traslado date, sot_seccion_fk bigint,  sot_ult_mod_fecha timestamp without time zone, sot_ult_mod_usuario character varying(45), sot_version integer, CONSTRAINT sg_solicitudes_traslado_pkey PRIMARY KEY (sot_pk), CONSTRAINT sot_sede_solicitante_sede_fk FOREIGN KEY (sot_sede_solicitante_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_estudiante_estudiantes_fk FOREIGN KEY (sot_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_servicio_educativo_solicitado_servicio_educativo_fk FOREIGN KEY (sot_servicio_educativo_solicitado_fk) REFERENCES centros_educativos.sg_servicio_educativo(sdu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_sede_origen_sedes_fk FOREIGN KEY (sot_sede_origen_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_servicio_educativo_origen_servicio_educativo_fk FOREIGN KEY (sot_servicio_educativo_origen_fk) REFERENCES centros_educativos.sg_servicio_educativo(sdu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_usuario_solicitud_usuario_fk FOREIGN KEY (sot_usuario_solicitud_fk) REFERENCES seguridad.sg_usuarios(usu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_motivo_traslado_motivo_traslado_fk FOREIGN KEY (sot_motivo_traslado_fk) REFERENCES catalogo.sg_motivos_traslado(mot_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sot_solicitud_traslado_seccion_fk FOREIGN KEY (sot_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_solicitudes_traslado IS 'Tabla para el registro de las solicitudes de traslado.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_sede_solicitante_fk IS 'Llave foránea que hace referencia a la sede solicitante.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_estudiante_fk IS 'Llave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_servicio_educativo_solicitado_fk IS 'Llave foránea que hace referencia al servicio educativo.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_sede_origen_fk IS 'Llave foránea que hace referencia a la sede origen.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_servicio_educativo_origen_fk IS 'Llave foránea que hace referencia al servicio educativo origen.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_estado IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_usuario_solicitud_fk IS 'Llave foránea que hace referencia al usuario que realiza la solicitud.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_fecha_solicitud IS 'Fecha del registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_observacion IS 'Observación del registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_resolucion IS 'Resolución del registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_motivo_traslado_fk IS 'Llave foránea que hace referencia al motivo de traslado.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_fecha_traslado IS 'Fecha de traslado.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_seccion_fk IS 'Llave foránea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitudes_traslado.sot_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitudes_traslado_aud (sot_pk bigint NOT NULL, sot_sede_solicitante_fk bigint, sot_estudiante_fk bigint,  sot_servicio_educativo_solicitado_fk bigint, sot_sede_origen_fk bigint,  sot_servicio_educativo_origen_fk bigint,  sot_estado CHARACTER VARYING(45),  sot_usuario_solicitud_fk bigint,  sot_fecha_solicitud date,  sot_observacion CHARACTER VARYING(500),  sot_resolucion CHARACTER VARYING(500),sot_motivo_traslado_fk bigint , sot_fecha_traslado date, sot_seccion_fk bigint , sot_ult_mod_fecha timestamp without time zone, sot_ult_mod_usuario character varying(45), sot_version integer, rev bigint, revtype smallint, CONSTRAINT solicitudes_traslado_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CAMBIO_JORNADA','EM29',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_TRASLADO','EB58',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOLICITUD_TRASLADO','EM30',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_SEDE_RIESGO_AFECTACION','E216', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_SEDE_RIESGO_AFECTACION','E217', '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_SEDE_RIESGO_AFECTACION','E218', '', 1, true, null, null,0); 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_TRASLADO','E219',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_TRASLADO','E220',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_TRASLADO','E221',  '', 1, true, null, null,0);

-- 2.3.2

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('SOLICITAR_TRASLADO','E222',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_TRASLADO','E223',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AUTORIZAR_TRASLADO_ESPECIAL','E224',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CONFIRMAR_TRASLADO','E225',  '', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_calificaciones ADD CONSTRAINT sg_calificacion_seccion_rango_fecha_componente_plan_estudio UNIQUE (cal_seccion_fk,cal_rango_fecha_fk,cal_componente_plan_estudio_fk);
ALTER TABLE centros_educativos.sg_calificaciones ADD CONSTRAINT sg_calificacion_seccion_componente_plan_est_tipo_calendario_num UNIQUE (cal_seccion_fk,cal_componente_plan_estudio_fk,cal_tipo_calendario_escolar,cal_numero);


ALTER TABLE centros_educativos.sg_allegados ADD COLUMN all_dependiente boolean;
COMMENT ON COLUMN centros_educativos.sg_allegados.all_dependiente IS 'Indica si el allegado es dependiente.';
ALTER TABLE centros_educativos.sg_allegados_aud ADD COLUMN all_dependiente boolean;

-- 2.4.0


CREATE INDEX sg_personas_primer_nombre_b ON centros_educativos.sg_personas USING btree(per_primer_nombre_busqueda);
CREATE INDEX sg_personas_segundo_nombre_b ON centros_educativos.sg_personas USING btree(per_segundo_nombre_busqueda);
CREATE INDEX sg_personas_tercer_nombre_b ON centros_educativos.sg_personas USING btree(per_tercer_nombre_busqueda);
CREATE INDEX sg_personas_primer_apellido_b ON centros_educativos.sg_personas USING btree(per_primer_apellido_busqueda);
CREATE INDEX sg_personas_segundo_apellido_b ON centros_educativos.sg_personas USING btree(per_segundo_apellido_busqueda);
CREATE INDEX sg_personas_tercer_apellido_b ON centros_educativos.sg_personas USING btree(per_tercer_apellido_busqueda);


-- 2.4.2

CREATE UNIQUE INDEX IF NOT EXISTS sg_sello_firma_sede_hab_uk_idx ON centros_educativos.sg_sellos_firmas (sfi_sede_fk, sfi_tipo_representante_fk) where sfi_habilitado = true;
CREATE UNIQUE INDEX IF NOT EXISTS sg_calendario_tipo_anio_uk_idx ON centros_educativos.sg_calendarios (cal_anio_lectivo_fk, cal_tipo_calendario_fk);

UPDATE seguridad.sg_operaciones SET ope_codigo = 'R5' where ope_codigo = 'E106';

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_creacion_usuario character varying(45);
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_creacion_usuario character varying(45);

UPDATE seguridad.sg_operaciones set ope_categoria_operacion_fk = 1 where ope_codigo like 'E%';
UPDATE seguridad.sg_operaciones set ope_categoria_operacion_fk = 1 where ope_codigo like 'R%';
UPDATE seguridad.sg_operaciones set ope_categoria_operacion_fk = 2 where ope_codigo like 'C%';

ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_cantidad_dias_lectivo integer;
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_cantidad_dias_lectivo integer;

ALTER TABLE centros_educativos.sg_formaciones_docente ALTER COLUMN fdo_calificacion_final TYPE VARCHAR(20);
ALTER TABLE centros_educativos.sg_formaciones_docente_aud ALTER COLUMN fdo_calificacion_final TYPE VARCHAR(20);

ALTER TABLE centros_educativos.sg_experiencia_laboral ALTER COLUMN ela_direccion TYPE VARCHAR(255);
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ALTER COLUMN ela_direccion TYPE VARCHAR(255);

ALTER TABLE centros_educativos.sg_rango_fecha ADD CONSTRAINT rfe_codigo_periodo_uk UNIQUE (rfe_codigo, rfe_periodo_calificacion_fk);

-- 2.5.0

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_formula_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_formula_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_formula_fk bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_componente_plan_grado_formulas_fkey FOREIGN KEY (cpg_formula_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


-- 2.6.0

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_validacion_academica boolean default false;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_validacion_academica boolean default false;

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_validacion_academica_fecha timestamp without time zone;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_validacion_academica_fecha timestamp without time zone;

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_validacion_academica_usuario CHARACTER VARYING(45);
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_validacion_academica_usuario CHARACTER VARYING(45);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RETIRAR_MATRICLA','E226',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ANULAR_RETIRO_MATRICULA','E227',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_ACADEMICAMENTE_MATRICULA','E228',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ESTUDIANTES_VALIDACION','EM31',  '', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_actividad_calendario_aud ADD PRIMARY KEY (aca_pk,rev);
ALTER TABLE centros_educativos.sg_actividades_especial_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_actividades_tiempo_extendido_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_administrativo_aud ADD PRIMARY KEY (pse_pk,rev);
ALTER TABLE centros_educativos.sg_allegados_aud ADD PRIMARY KEY (all_pk,rev);
ALTER TABLE centros_educativos.sg_anio_lectivo_aud ADD PRIMARY KEY (ale_pk,rev);
ALTER TABLE centros_educativos.sg_archivos_aud ADD PRIMARY KEY (ach_pk,rev);
ALTER TABLE centros_educativos.sg_areas_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_asignaturas_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_asistencias_aud ADD PRIMARY KEY (asi_pk,rev);
ALTER TABLE centros_educativos.sg_atpi_aud ADD PRIMARY KEY (pse_pk,rev);
ALTER TABLE centros_educativos.sg_calendarios_aud ADD PRIMARY KEY (cal_pk,rev);
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD PRIMARY KEY (cal_pk,rev);
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD PRIMARY KEY (cae_pk,rev);
ALTER TABLE centros_educativos.sg_capacitaciones_aud ADD PRIMARY KEY (cap_pk,rev);
ALTER TABLE centros_educativos.sg_celda_dia_hora_aud ADD PRIMARY KEY (cdh_pk,rev);
ALTER TABLE centros_educativos.sg_ciclos_aud ADD PRIMARY KEY (cic_pk,rev);
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD PRIMARY KEY (cpg_pk,rev);
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud ADD PRIMARY KEY (cac_pk,rev);
ALTER TABLE centros_educativos.sg_dato_contratacion_jornada_laboral_aud ADD PRIMARY KEY (dco_pk, jla_pk, rev);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD PRIMARY KEY (dco_pk,rev);
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD PRIMARY KEY (dem_pk,rev);
ALTER TABLE centros_educativos.sg_diplomados_aud ADD PRIMARY KEY (dip_pk,rev);
ALTER TABLE centros_educativos.sg_docente_aud ADD PRIMARY KEY (pse_pk,rev);
ALTER TABLE centros_educativos.sg_docente_otras_fuentes_aud ADD PRIMARY KEY (pse_pk,rev);
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud ADD PRIMARY KEY (eim_pk,rev);
ALTER TABLE centros_educativos.sg_estudiantes_valoracion_aud ADD PRIMARY KEY (esv_pk,rev);
ALTER TABLE centros_educativos.sg_estudios_realizados_aud ADD PRIMARY KEY (ere_pk,rev);
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD PRIMARY KEY (esu_pk,rev);
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD PRIMARY KEY (ela_pk,rev);
ALTER TABLE centros_educativos.sg_fichas_salud_aud ADD PRIMARY KEY (fsa_pk,rev);
ALTER TABLE centros_educativos.sg_formaciones_docente_aud ADD PRIMARY KEY (fdo_pk,rev);
ALTER TABLE centros_educativos.sg_gestion_anio_lectivo_aud ADD PRIMARY KEY (gal_pk,rev);
ALTER TABLE centros_educativos.sg_grados_aud ADD PRIMARY KEY (gra_pk,rev);
ALTER TABLE centros_educativos.sg_grados_definicion_titulo_aud ADD PRIMARY KEY (gra_pk, dti_pk, rev);
ALTER TABLE centros_educativos.sg_horarios_escolares_aud ADD PRIMARY KEY (hes_pk,rev);
ALTER TABLE centros_educativos.sg_identificaciones_aud ADD PRIMARY KEY (ide_pk,rev);
ALTER TABLE centros_educativos.sg_idiomas_realizados_aud ADD PRIMARY KEY (ire_pk,rev);
ALTER TABLE centros_educativos.sg_manifestacion_violencia_aud ADD PRIMARY KEY (mvi_pk,rev);
ALTER TABLE centros_educativos.sg_matriculas_aud ADD PRIMARY KEY (mat_pk,rev);
ALTER TABLE centros_educativos.sg_modalidades_aud ADD PRIMARY KEY (mod_pk,rev);
ALTER TABLE centros_educativos.sg_modulos_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_modulos_diplomado_aud ADD PRIMARY KEY (mdi_pk,rev);
ALTER TABLE centros_educativos.sg_movimientos_aud ADD PRIMARY KEY (mov_pk,rev);
ALTER TABLE centros_educativos.sg_nie_aud ADD PRIMARY KEY (nie_pk,rev);
ALTER TABLE centros_educativos.sg_niveles_aud ADD PRIMARY KEY (niv_pk,rev);
ALTER TABLE centros_educativos.sg_noticias_aud ADD PRIMARY KEY (not_pk,rev);
ALTER TABLE centros_educativos.sg_opciones_aud ADD PRIMARY KEY (opc_pk,rev);
ALTER TABLE centros_educativos.sg_organizaciones_curricular_aud ADD PRIMARY KEY (ocu_pk,rev);
ALTER TABLE centros_educativos.sg_periodos_calendario_aud ADD PRIMARY KEY (ces_pk,rev);
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud ADD PRIMARY KEY (pca_pk,rev);
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud ADD PRIMARY KEY (pse_pk,rev);
ALTER TABLE centros_educativos.sg_personas_discapacidades_aud ADD PRIMARY KEY (per_pk, dis_pk, rev);
ALTER TABLE centros_educativos.sg_planes_estudio_aud ADD PRIMARY KEY (pes_pk,rev);
ALTER TABLE centros_educativos.sg_pruebas_aud ADD PRIMARY KEY (cpe_pk,rev);
ALTER TABLE centros_educativos.sg_rango_fecha_aud ADD PRIMARY KEY (rfe_pk,rev);
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten_aud ADD PRIMARY KEY (rea_pk,rev);
ALTER TABLE centros_educativos.sg_rel_opcion_prog_ed_aud ADD PRIMARY KEY (roe_pk,rev);
ALTER TABLE centros_educativos.sg_rel_sede_riesgo_afectacion_aud ADD PRIMARY KEY (rar_pk,rev);
ALTER TABLE centros_educativos.sg_rel_sede_servicio_infra_aud ADD PRIMARY KEY (rss_pk,rev);
ALTER TABLE centros_educativos.sg_reporte_director_aud ADD PRIMARY KEY (rdi_pk,rev);
ALTER TABLE centros_educativos.sg_secciones_aud ADD PRIMARY KEY (sec_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_ced_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_ced_ofi_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_ced_pri_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_alfa_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_circulos_infa_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_educame_aud ADD PRIMARY KEY (sed_pk,rev);
ALTER TABLE centros_educativos.sg_sedes_factor_riesgo_aud ADD PRIMARY KEY (sed_pk, rar_pk, rev);
ALTER TABLE centros_educativos.sg_sedes_jornadas_aud ADD PRIMARY KEY (sed_pk, jla_pk, rev);
ALTER TABLE centros_educativos.sg_sello_firma_titular_aud ADD PRIMARY KEY (sft_pk,rev);
ALTER TABLE centros_educativos.sg_sellos_firmas_aud ADD PRIMARY KEY (sfi_pk,rev);
ALTER TABLE centros_educativos.sg_servicio_educativo_aud ADD PRIMARY KEY (sdu_pk,rev);
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD PRIMARY KEY (sim_pk,rev);
ALTER TABLE centros_educativos.sg_solicitudes_traslado_aud ADD PRIMARY KEY (sot_pk,rev);
ALTER TABLE centros_educativos.sg_telefonos_aud ADD PRIMARY KEY (tel_pk,rev);
ALTER TABLE centros_educativos.sg_tipos_calendario_aud ADD PRIMARY KEY (tca_pk,rev);
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD PRIMARY KEY (est_pk, rev);
ALTER TABLE centros_educativos.sg_personas_aud ADD PRIMARY KEY (per_pk, rev);
ALTER TABLE centros_educativos.sg_direcciones_aud ADD PRIMARY KEY (dir_pk, rev);
ALTER TABLE centros_educativos.sg_sedes_aud ADD PRIMARY KEY (sed_pk, rev);


ALTER TABLE centros_educativos.sg_calendarios ADD CONSTRAINT sg_calendario_codigo_uk UNIQUE (cal_codigo);

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_codigo_constancia CHARACTER VARYING(100);
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_codigo_constancia CHARACTER VARYING(100);

--2.7.0

-- Matriculas parciales
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_matriculas_parciales_mat_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_matriculas_parciales(mat_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_matriculas_parciales_mat_pk_seq'::regclass), mat_est_primer_nombre CHARACTER VARYING(100), mat_est_segundo_nombre CHARACTER VARYING(100), mat_est_primer_apellido CHARACTER VARYING(100), mat_est_segundo_apellido CHARACTER VARYING(100), mat_est_nie bigint, mat_ult_mod_fecha timestamp without TIME zone, mat_creacion_usuario CHARACTER varying(45), mat_json text, mat_ult_mod_usuario CHARACTER varying(45), mat_version INTEGER, CONSTRAINT sg_matriculas_parciales_pkey PRIMARY KEY (mat_pk));
    COMMENT ON TABLE  centros_educativos.sg_matriculas_parciales                           IS 'Tabla para almacenar las matriculas.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas_parciales.mat_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas_parciales.mat_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas_parciales.mat_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_matriculas_parciales.mat_version               IS 'Versión del registro';

-- 2.7.1

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ANULAR_VALIDACION_MATRICULA','E229',  '', 1, true, null, null,0);
ALTER TABLE centros_educativos.sg_calendarios ADD CONSTRAINT sg_calendario_nombre_uk UNIQUE (cal_nombre);
INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('pce', 'PATRON_CORREO_ELECTRONICO', 'patron_correo_electronico', '2019-01-08 14:43:24.140', 'casuser', 0, '<p>(?:[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])</p>');


-- 2.7.2

ALTER TABLE centros_educativos.sg_personas
ALTER COLUMN per_nombre_busqueda type CHARACTER VARYING(650);
ALTER TABLE centros_educativos.sg_personas_aud
ALTER COLUMN per_nombre_busqueda type CHARACTER VARYING(650);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_DATOS_SENSIBLES_SEDE','EP25',  '', 1, true, null, null,0);

DELETE FROM catalogo.sg_configuraciones WHERE con_nombre='PATRON_CORREO_ELECTRONICO';

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('PATRON_CORREO', 'PATRON_CORREO_ELECTRONICO', 'patron_correo_electronico', '2019-01-08 14:43:24.140', 'casuser', 0, '(?:[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])');

-- 2.7.3

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONFIRMACION_MATRICULAS','EM32',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONFIRMACIONES_MATRICULAS','EM33',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CONFIRMAR_MATRICULAS','E230',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONFIRMACIONES_MATRICULAS','EB59',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ESTUDIANTES_SIN_NIE','EM34',  '', 1, true, null, null,0);

-- Confirmaciones de Matrículas
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_confirmaciones_matriculas_cma_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_matriculas (cma_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_confirmaciones_matriculas_cma_pk_seq'::regclass), cma_fecha_confirmacion timestamp without time zone, cma_usuario_confirmacion character varying(45), cma_sede_fk bigint, cma_anio_lectivo_fk bigint, cma_ult_mod_fecha timestamp without time zone, cma_ult_mod_usuario character varying(45), cma_version integer, CONSTRAINT sg_confirmaciones_matriculas_pkey PRIMARY KEY (cma_pk));
COMMENT ON TABLE centros_educativos.sg_confirmaciones_matriculas IS 'Tabla para el registro de confirmaciones de matrículas.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_fecha_confirmacion IS 'Fecha de confirmación.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_usuario_confirmacion IS 'Usuario que confirmó.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_sede_fk IS 'Sede del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_anio_lectivo_fk IS 'Año lectivo del registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_confirmaciones_matriculas_aud (cma_pk bigint NOT NULL, cma_fecha_confirmacion timestamp without time zone, cma_usuario_confirmacion character varying(45), cma_sede_fk bigint, cma_anio_lectivo_fk bigint, cma_ult_mod_fecha timestamp without time zone, cma_ult_mod_usuario character varying(45), cma_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas_aud ADD PRIMARY KEY (cma_pk, rev);


ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD CONSTRAINT sg_confmat_sede_anio_uk UNIQUE (cma_sede_fk, cma_anio_lectivo_fk);
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD CONSTRAINT sg_confirmaciones_matriculas_sede_fkey FOREIGN KEY (cma_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD CONSTRAINT sg_confirmaciones_matriculas_anio_lectivo_fkey FOREIGN KEY (cma_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- 2.9.0

ALTER TABLE centros_educativos.sg_matriculas ALTER COLUMN mat_validacion_academica SET DEFAULT false;

--2.9.1

DELETE FROM catalogo.sg_configuraciones WHERE con_nombre='PATRON_CORREO_ELECTRONICO';

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('PATRON_CORREO', 'Patrón de correo electrónico', 'patron de correo electronico', '2019-01-08 14:43:24.140', 'casuser', 0, '(?:[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&amp;''*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])');


ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_persona_partida_nacimiento_uk UNIQUE (per_partida_nacimiento, per_partida_nacimiento_folio,per_partida_nacimiento_libro);
ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_retirada boolean default false;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_retirada boolean;

-- 2.10.0


ALTER TABLE centros_educativos.sg_actividad_calendario ADD COLUMN aca_sede_fk bigint;
ALTER TABLE centros_educativos.sg_actividad_calendario_aud ADD COLUMN aca_sede_fk bigint;   

ALTER TABLE centros_educativos.sg_actividad_calendario ADD COLUMN aca_departamento_fk bigint;
ALTER TABLE centros_educativos.sg_actividad_calendario_aud ADD COLUMN aca_departamento_fk bigint;

ALTER TABLE centros_educativos.sg_actividad_calendario ADD COLUMN aca_ambito character varying(50);
ALTER TABLE centros_educativos.sg_actividad_calendario_aud ADD COLUMN aca_ambito character varying(50);

ALTER TABLE centros_educativos.sg_actividad_calendario ADD CONSTRAINT sg_actividad_calendario_sede_fkey FOREIGN KEY (aca_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_actividad_calendario ADD CONSTRAINT sg_actividad_calendario_depto_fkey FOREIGN KEY (aca_departamento_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CALENDARIOS','E231',  '', 1, true, null, null,0);


-- 2.10.2

UPDATE catalogo.sg_tipos_calendario_escolar
SET tce_codigo='01'
WHERE tce_nombre='Nacional';

UPDATE catalogo.sg_tipos_calendario_escolar
SET tce_codigo='02'
WHERE tce_nombre='Internacional';

--2.10.6


DELETE from seguridad.sg_roles_operacion where rop_operacion_fk = (SELECT ope_pk from seguridad.sg_operaciones where ope_codigo = 'EM14');
DELETE from seguridad.sg_operaciones where ope_codigo = 'EM14';

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_ultima_matricula_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_ultima_matricula_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_ult_matricula_fkey FOREIGN KEY (est_ultima_matricula_fk) REFERENCES centros_educativos.sg_matriculas(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
UPDATE  centros_educativos.sg_estudiantes e set est_ultima_matricula_fk = (select mat_pk from centros_educativos.sg_matriculas INNER JOIN centros_educativos.sg_estudiantes f on (mat_estudiante_fk = f.est_pk) where f.est_pk = e.est_pk order by mat_fecha_ingreso desc, mat_pk desc limit 1);


ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_ultima_sede_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_ultima_sede_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_ult_sede_fkey FOREIGN KEY (est_ultima_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

UPDATE  centros_educativos.sg_estudiantes e set est_ultima_sede_fk = (
select sed_pk from centros_educativos.sg_sedes 
INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) 
INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) 
INNER JOIN centros_educativos.sg_matriculas ON (mat_seccion_fk = sec_pk) where mat_pk = e.est_ultima_matricula_fk);


CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_matricula_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    UPDATE centros_educativos.sg_estudiantes set est_ultima_matricula_fk = new.mat_pk where est_pk = new.mat_estudiante_fk;
    RETURN new;
END;
$BODY$
language plpgsql;


CREATE TRIGGER update_ultima_matricula_estudiante_trigger
AFTER INSERT ON centros_educativos.sg_matriculas
FOR EACH ROW
EXECUTE PROCEDURE centros_educativos.update_ultima_matricula_estudiante();



CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_sede_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    UPDATE centros_educativos.sg_estudiantes set est_ultima_sede_fk = (select sed_pk from centros_educativos.sg_sedes INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) 
INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) where sec_pk = new.mat_seccion_fk) where est_pk = new.mat_estudiante_fk;
    RETURN new;
END;
$BODY$
language plpgsql;

CREATE TRIGGER update_ultima_sede_estudiante_trigger
AFTER INSERT ON centros_educativos.sg_matriculas
FOR EACH ROW
EXECUTE PROCEDURE centros_educativos.update_ultima_sede_estudiante();

-- 2.10.12
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nacimiento_complemento character varying(10);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nacimiento_complemento character varying(10);  

ALTER TABLE centros_educativos.sg_personas DROP CONSTRAINT if exists sg_persona_partida_nacimiento_uk;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_persona_partida_nacimiento_uk UNIQUE (per_partida_nacimiento, per_partida_nacimiento_folio,per_partida_nacimiento_libro,per_partida_nacimiento_complemento);

-- 2.10.14
ALTER TABLE centros_educativos.sg_personas DROP CONSTRAINT if exists sg_persona_partida_nacimiento_uk;

-- 2.10.15

INSERT INTO catalogo.sg_tipos_identificacion_aud(
	tin_pk, tin_codigo, tin_habilitado, tin_nombre, tin_nombre_busqueda, tin_ult_mod_fecha, tin_ult_mod_usuario, tin_version, rev, revtype)
	SELECT tin_pk, tin_codigo, tin_habilitado, tin_nombre, tin_nombre_busqueda, tin_ult_mod_fecha, tin_ult_mod_usuario, tin_version, 1, 0 
	from catalogo.sg_tipos_identificacion;

-- 2.11.0 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONFIRMACION_MATRICULA','E232',  '', 1, true, null, null,0);


-- 2.11.2

ALTER TABLE centros_educativos.sg_personas DROP CONSTRAINT sg_personas_tipo_sangre_fkey;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_tipo_sangre_fkey FOREIGN KEY (per_tipo_sangre_fk) REFERENCES catalogo.sg_tipos_sangre(tsa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_estado_civil_fkey FOREIGN KEY (per_estado_civil_fk) REFERENCES catalogo.sg_estados_civil(eci_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


UPDATE centros_educativos.sg_matriculas set mat_creacion_usuario = mat_ult_mod_usuario where mat_creacion_usuario is null;
UPDATE centros_educativos.sg_matriculas set mat_validacion_academica = false where mat_validacion_academica is null;
UPDATE centros_educativos.sg_matriculas set mat_retirada = true where mat_retirada is null and mat_estado = 'CERRADO';
UPDATE centros_educativos.sg_matriculas set mat_retirada = false where mat_retirada is null and mat_estado = 'ABIERTO';


-- 2.11.3


ALTER TABLE centros_educativos.sg_secciones ALTER COLUMN sec_servicio_educativo_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_matriculas ALTER COLUMN mat_retirada SET NOT NULL;
ALTER TABLE seguridad.sg_roles_operacion ALTER COLUMN rop_cascada SET NOT NULL;


-- 2.12.0

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nacimiento_anio integer;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nacimiento_anio integer;

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_nacimiento_archivo bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_nacimiento_archivo bigint;

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_partida_nac_arch_fkey FOREIGN KEY (per_partida_nacimiento_archivo) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_departamento_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_departamento_fk bigint;
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_partida_municipio_fk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_partida_municipio_fk bigint;

ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_partida_nac_depto_fkey FOREIGN KEY (per_partida_departamento_fk) REFERENCES catalogo.sg_departamentos(dep_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_personas ADD CONSTRAINT sg_personas_partida_nac_mun_fkey FOREIGN KEY (per_partida_municipio_fk) REFERENCES catalogo.sg_municipios(mun_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODIFICAR_NIE_PERSONA','EP34',  '', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_MODICAR_FECHA_MATRICULA','EP35',  '', 1, true, null, null,0);

-- 2.14.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CAMBIAR_SECCION_MASIVO','E316',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CAMBIAR_JORNADA_MASIVO','EM50',  '', 1, true, null, null,0);

-- 2.14.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_RESPONSABLE_SI_ES_PERSONAL_SEDE','EP31',  '', 1, true, null, null,0);

-- 2.15.0

ALTER TABLE centros_educativos.sg_actividad_calendario ADD COLUMN aca_dia_lectivo BOOLEAN;
ALTER TABLE centros_educativos.sg_actividad_calendario_aud ADD COLUMN aca_dia_lectivo BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_actividad_calendario.aca_dia_lectivo IS 'Indica si es un día lectivo.';

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_anulada boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_anulada boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ANULAR_MATRICULA','E317',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_resolucion character varying(50);
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_resolucion character varying(50);


-- 2.18.1

update centros_educativos.sg_matriculas set mat_anulada = false where mat_anulada is null;

CREATE INDEX sg_matriculas_estado_idx  ON centros_educativos.sg_matriculas (mat_estado ASC NULLS LAST);


DO $$
declare


persona_sin_nie centros_educativos.sg_personas%ROWTYPE;

rev_aux bigint;
nie_aux bigint;

contador int;

BEGIN

contador=0;

FOR persona_sin_nie IN(

		select * from centros_educativos.sg_personas per 
		inner join centros_educativos.sg_estudiantes est on est.est_persona=per.per_pk
		inner join centros_educativos.sg_matriculas mat on est.est_ultima_matricula_fk=mat.mat_pk
		inner join centros_educativos.sg_secciones sec on mat.mat_seccion_fk=sec.sec_pk
		inner join centros_educativos.sg_servicio_educativo ser on sec.sec_servicio_educativo_fk=ser.sdu_pk
		inner join centros_educativos.sg_grados gra on ser.sdu_grado_fk=gra.gra_pk
		where gra.gra_requiere_nie=false and per_nie is null 
		

) loop
contador:=contador+1;
nie_aux:=0;
RAISE NOTICE '----------- PERSONA (%)----',persona_sin_nie.per_pk;

insert into centros_educativos.sg_nie (nie_fecha,nie_ult_mod_fecha,nie_ult_mod_usuario,nie_version) values (now(),now(),'MOD_NIE',0) returning nie_pk into rev_aux;

-- rev_aux= (select nie_pk from centros_educativos.sg_nie order by nie_pk desc limit 1);
RAISE NOTICE '----------- NIE (%)----',rev_aux;
nie_aux:= (select count(*) from centros_educativos.sg_personas where per_nie = rev_aux);

WHILE nie_aux!=0 LOOP
   insert into centros_educativos.sg_nie (nie_fecha,nie_ult_mod_fecha,nie_ult_mod_usuario,nie_version) values (now(),now(),'MOD_NIE',0) returning nie_pk into rev_aux;
   nie_aux:= (select count(*) from centros_educativos.sg_personas where per_nie = rev_aux);
END LOOP;

update centros_educativos.sg_personas set per_nie=rev_aux, per_ult_mod_usuario= per_ult_mod_usuario||'-MOD_NIE' where per_pk=persona_sin_nie.per_pk;

END LOOP;

RAISE NOTICE '----------- contador (%)----',contador;

END$$;


CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_matricula_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_matricula_fk = new.mat_pk where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;


DROP TRIGGER IF EXISTS update_ultima_matricula_estudiante_trigger ON centros_educativos.sg_matriculas;

CREATE TRIGGER update_ultima_matricula_estudiante_trigger
AFTER INSERT OR UPDATE ON centros_educativos.sg_matriculas
FOR EACH ROW
EXECUTE PROCEDURE centros_educativos.update_ultima_matricula_estudiante();



CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_sede_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_sede_fk = (select sed_pk from centros_educativos.sg_sedes INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) where sec_pk = new.mat_seccion_fk) where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;

DROP TRIGGER IF EXISTS update_ultima_sede_estudiante_trigger ON centros_educativos.sg_matriculas;

CREATE TRIGGER update_ultima_sede_estudiante_trigger
AFTER INSERT OR UPDATE ON centros_educativos.sg_matriculas
FOR EACH ROW
EXECUTE PROCEDURE centros_educativos.update_ultima_sede_estudiante();

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('UNIR_ESTUDIANTES','E321',  '', 1, true, null, null,0);

CREATE UNIQUE INDEX sg_servicio_educativo_sede_grado_opcion_programa_uk ON centros_educativos.sg_servicio_educativo (sdu_sede_fk, sdu_grado_fk, sdu_opcion_fk, sdu_programa_educativo_fk)
WHERE sdu_opcion_fk IS NOT NULL and sdu_programa_educativo_fk IS NOT NULL;

CREATE UNIQUE INDEX sg_servicio_educativo_sede_sin_grado_opcion_programa_uk ON centros_educativos.sg_servicio_educativo (sdu_sede_fk, sdu_grado_fk)
WHERE sdu_opcion_fk IS NULL and sdu_programa_educativo_fk IS NULL; 

UPDATE centros_educativos.sg_matriculas set mat_retirada = false, mat_motivo_retiro_fk= null, mat_ult_mod_usuario='modificado1'
where mat_ult_mod_usuario like 'MIGRA%' and mat_motivo_retiro_fk=99 and mat_estado = 'CERRADO';

update centros_educativos.sg_matriculas set mat_anulada=true, mat_motivo_retiro_fk=null, mat_ult_mod_usuario='modificado3'
where (mat_motivo_retiro_fk=99) and mat_fecha_ingreso>='2000-01-01'; 

update centros_educativos.sg_matriculas set mat_anulada=true, mat_motivo_retiro_fk=null, mat_ult_mod_usuario='modificado4'
where (mat_motivo_retiro_fk=2 ) and mat_fecha_ingreso>'2000-01-01'; 

delete from catalogo.sg_motivos_retiro where mre_pk = 2;
delete from catalogo.sg_motivos_retiro where mre_pk = 99;

update centros_educativos.sg_sedes pp
set sed_nombre_busqueda = (select replace(sed_nombre_busqueda, 'ñ', 'n') from centros_educativos.sg_sedes where sed_pk = pp.sed_pk);

update centros_educativos.sg_personas pp set 
(per_nombre_busqueda,
per_primer_nombre_busqueda, 
per_segundo_nombre_busqueda, 
per_tercer_nombre_busqueda, 
per_primer_apellido_busqueda,
per_segundo_apellido_busqueda,
per_tercer_apellido_busqueda) = 
(Select 
replace(per_nombre_busqueda, 'ñ', 'n'),
replace(per_primer_nombre_busqueda, 'ñ', 'n'),
replace(per_segundo_nombre_busqueda, 'ñ', 'n'),
replace(per_tercer_nombre_busqueda, 'ñ', 'n'),
replace(per_primer_apellido_busqueda, 'ñ', 'n'),
replace(per_segundo_apellido_busqueda, 'ñ', 'n'),
replace(per_tercer_apellido_busqueda, 'ñ', 'n')
from centros_educativos.sg_personas where per_pk = pp.per_pk
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PERSONA_SIMILAR','E322',  '', 1, true, null, null,0);

CREATE INDEX sg_sol_traslado_estado_idx  ON centros_educativos.sg_solicitudes_traslado (sot_estado ASC NULLS LAST);
CREATE INDEX sg_sol_traslado_sed_solic_fk_idx ON centros_educativos.sg_solicitudes_traslado (sot_servicio_educativo_solicitado_fk ASC NULLS LAST);
CREATE INDEX sg_sol_traslado_sed_origen_fk_idx ON centros_educativos.sg_solicitudes_traslado (sot_servicio_educativo_origen_fk ASC NULLS LAST);
CREATE INDEX sg_sol_traslado_motivo_fk_idx  ON centros_educativos.sg_solicitudes_traslado (sot_motivo_traslado_fk ASC NULLS LAST);

-- 2.18.2

ALTER TABLE centros_educativos.sg_ciclos ALTER COLUMN cic_nivel SET NOT NULL;
ALTER TABLE centros_educativos.sg_estudiantes ALTER COLUMN est_persona SET NOT NULL;
ALTER TABLE centros_educativos.sg_estudiante_impresion ALTER COLUMN eim_estudiante_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_grados ALTER COLUMN gra_relacion_modalidad_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_matriculas ALTER COLUMN mat_seccion_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_modalidades ALTER COLUMN mod_ciclo SET NOT NULL;
ALTER TABLE centros_educativos.sg_niveles ALTER COLUMN niv_organizacion_curricular SET NOT NULL;
ALTER TABLE centros_educativos.sg_opciones ALTER COLUMN opc_modalidad_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ALTER COLUMN rea_modalidad_educativa_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ALTER COLUMN rea_modalidad_atencion_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_rel_opcion_prog_ed ALTER COLUMN roe_programa_educativo_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_secciones ALTER COLUMN sec_anio_lectivo_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_secciones ALTER COLUMN sec_servicio_educativo_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_servicio_educativo ALTER COLUMN sdu_grado_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_servicio_educativo ALTER COLUMN sdu_sede_fk SET NOT NULL;
ALTER TABLE catalogo.sg_cantones ALTER COLUMN can_municipio_fk SET NOT NULL;
ALTER TABLE catalogo.sg_caserios ALTER COLUMN cas_canton_fk SET NOT NULL;
ALTER TABLE catalogo.sg_municipios ALTER COLUMN mun_departamento_fk SET NOT NULL; 
ALTER TABLE centros_educativos.sg_matriculas ALTER COLUMN mat_estudiante_fk SET NOT NULL; 


--2.18.3

CREATE UNIQUE INDEX sg_servicio_educativo_sede_grado_opcion_programa_uk ON centros_educativos.sg_servicio_educativo (sdu_sede_fk, sdu_grado_fk, sdu_opcion_fk, sdu_programa_educativo_fk)
WHERE sdu_opcion_fk IS NOT NULL and sdu_programa_educativo_fk IS NOT NULL;

CREATE UNIQUE INDEX sg_servicio_educativo_sede_sin_grado_opcion_programa_uk ON centros_educativos.sg_servicio_educativo (sdu_sede_fk, sdu_grado_fk)
WHERE sdu_opcion_fk IS NULL and sdu_programa_educativo_fk IS NULL; 

ALTER TABLE centros_educativos.sg_personas ADD per_lucene_index_updated boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD per_lucene_index_updated boolean;
UPDATE centros_educativos.sg_personas set per_lucene_index_updated = true;
CREATE INDEX sg_personas_lucene_index_not_updated_idx ON centros_educativos.sg_personas(per_pk) WHERE per_lucene_index_updated = false;


-- 3.0.0

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_casos_violacion_cvi_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_casos_violacion(cvi_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_casos_violacion_cvi_pk_seq'::regclass), cvi_sede_fk bigint, cvi_anio_fk bigint, cvi_cantidad_casos integer, cvi_ult_mod_fecha timestamp without TIME zone, cvi_ult_mod_usuario CHARACTER varying(45), cvi_version INTEGER, CONSTRAINT sg_casos_violacion_pkey PRIMARY KEY (cvi_pk), CONSTRAINT sg_casos_violacion_sede_fk FOREIGN KEY (cvi_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_casos_violacion_anio_fk FOREIGN KEY (cvi_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_casos_violacion                           IS 'Tabla para almacenar los casos de violación en una sede.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_sede_fk               IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_anio_fk               IS 'Llave foranea que hace referencia al año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_cantidad_casos        IS 'Cantidad de casos de violación.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_casos_violacion_aud (cvi_pk bigint NOT NULL, cvi_sede_fk bigint, cvi_anio_fk bigint, cvi_cantidad_casos integer, cvi_ult_mod_fecha timestamp without TIME zone, cvi_ult_mod_usuario CHARACTER varying(45), cvi_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_casos_violacion_aud_pkey PRIMARY KEY (cvi_pk, rev), CONSTRAINT sg_casos_violacion_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_acciones_prevenir_embarazo_ape_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_acciones_prevenir_embarazo(ape_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_acciones_prevenir_embarazo_ape_pk_seq'::regclass), ape_sede_fk bigint, ape_anio_fk bigint, ape_institucion CHARACTER VARYING(255), ape_tipo_accion_fk bigint, ape_ult_mod_fecha timestamp without TIME zone, ape_ult_mod_usuario CHARACTER varying(45), ape_version INTEGER, CONSTRAINT sg_acciones_prevenir_embarazo_pkey PRIMARY KEY (ape_pk), CONSTRAINT sg_acciones_prevenir_embarazo_sede_fk FOREIGN KEY (ape_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_acciones_prevenir_embarazo_anio_fk FOREIGN KEY (ape_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_acciones_prevenir_embarazo_tipo_accion_fk FOREIGN KEY (ape_tipo_accion_fk) REFERENCES catalogo.sg_tipos_accion(tac_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_acciones_prevenir_embarazo                           IS 'Tabla para almacenar las acciones para prevenir el embarazo.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_sede_fk               IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_anio_fk               IS 'Llave foranea que hace referencia al año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_institucion           IS 'Nombre de la institución.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_tipo_accion_fk        IS 'Llave foranea que hace referencia al tipo de acción.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_acciones_prevenir_embarazo_aud (ape_pk bigint NOT NULL, ape_sede_fk bigint, ape_anio_fk bigint, ape_institucion CHARACTER VARYING(255), ape_tipo_accion_fk bigint, ape_ult_mod_fecha timestamp without TIME zone, ape_ult_mod_usuario CHARACTER varying(45), ape_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_acciones_prevenir_embarazo_aud_pkey PRIMARY KEY (ape_pk, rev), CONSTRAINT sg_acciones_prevenir_embarazo_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_asistencia_sedes_ase_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_asistencia_sedes(ase_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_asistencia_sedes_ase_pk_seq'::regclass), ase_sede_fk bigint, ase_anio_fk bigint, ase_tipo_apoyo_fk bigint, ase_tipo_proveedor_fk bigint, ase_nombre_proveedor CHARACTER VARYING(255), ase_ult_mod_fecha timestamp without TIME zone, ase_ult_mod_usuario CHARACTER varying(45), ase_version INTEGER, CONSTRAINT sg_asistencia_sedes_pkey PRIMARY KEY (ase_pk), CONSTRAINT sg_asistencia_sedes_sede_fk FOREIGN KEY (ase_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencia_sedes_anio_fk FOREIGN KEY (ase_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencia_sedes_tipo_apoyo_fk FOREIGN KEY (ase_tipo_apoyo_fk) REFERENCES catalogo.sg_tipos_apoyo(tap_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencia_sedes_tipo_proveedor_fk FOREIGN KEY (ase_tipo_proveedor_fk) REFERENCES catalogo.sg_tipos_proveedor(tpr_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_asistencia_sedes                           IS 'Tabla para almacenar las asistencias.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_sede_fk               IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_anio_fk               IS 'Llave foranea que hace referencia al año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_tipo_apoyo_fk         IS 'Llave foranea que hace referencia al tipo de apoyo.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_tipo_proveedor_fk     IS 'Llave foranea que hace referencia al tipo de proveedor.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_nombre_proveedor      IS 'Nombre del proveedor.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencia_sedes.ase_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asistencia_sedes_aud (ase_pk bigint NOT NULL, ase_sede_fk bigint, ase_anio_fk bigint, ase_tipo_apoyo_fk bigint, ase_tipo_proveedor_fk bigint, ase_nombre_proveedor CHARACTER VARYING(255), ase_ult_mod_fecha timestamp without TIME zone, ase_ult_mod_usuario CHARACTER varying(45), ase_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_asistencia_sedes_aud_pkey PRIMARY KEY (ase_pk, rev), CONSTRAINT sg_asistencia_sedes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_factor_riesgo_sedes_fri_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_factor_riesgo_sedes(fri_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_factor_riesgo_sedes_fri_pk_seq'::regclass), fri_sede_fk bigint, fri_anio_fk bigint, fri_tipo_riesgo_fk bigint, fri_grado_riesgo_fk bigint, fri_fecha_inicio date, fri_fecha_fin date, fri_ult_mod_fecha timestamp without TIME zone, fri_ult_mod_usuario CHARACTER varying(45), fri_version INTEGER, CONSTRAINT sg_factor_riesgo_sedes_pkey PRIMARY KEY (fri_pk), CONSTRAINT sg_factor_riesgo_sedes_sede_fk FOREIGN KEY (fri_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_factor_riesgo_sedes_anio_fk FOREIGN KEY (fri_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_factor_riesgo_sedes_tipo_riesgo_fk FOREIGN KEY (fri_tipo_riesgo_fk) REFERENCES catalogo.sg_tipos_riesgo(tri_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_factor_riesgo_sedes_grado_riesgo_fk FOREIGN KEY (fri_grado_riesgo_fk) REFERENCES catalogo.sg_grados_riesgo(gre_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_factor_riesgo_sedes                           IS 'Tabla para almacenar los factores de riesgo.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_sede_fk               IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_anio_fk               IS 'Llave foranea que hace referencia al año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_tipo_riesgo_fk        IS 'Llave foranea que hace referencia al tipo de riesgo.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_grado_riesgo_fk       IS 'Llave foranea que hace referencia al grado de riesgo.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_fecha_inicio          IS 'Fecha de inicio del registro.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_fecha_fin             IS 'Fecha fin del registro.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_factor_riesgo_sedes.fri_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_factor_riesgo_sedes_aud (fri_pk bigint NOT NULL, fri_sede_fk bigint, fri_anio_fk bigint, fri_tipo_riesgo_fk bigint, fri_grado_riesgo_fk bigint, fri_fecha_inicio date, fri_fecha_fin date, fri_ult_mod_fecha timestamp without TIME zone, fri_ult_mod_usuario CHARACTER varying(45), fri_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_factor_riesgo_sedes_aud_pkey PRIMARY KEY (fri_pk, rev), CONSTRAINT sg_factor_riesgo_sedes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Proyectos institucionales
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_proyectos_institucionales_sede_pro_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_proyectos_institucionales_sede (pro_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_proyectos_institucionales_sede_pro_pk_seq'::regclass), pro_sede_fk bigint, pro_proyecto_institucional_fk bigint, pro_fecha_otorgado date, pro_observaciones character varying(500),pro_beneficio_fk bigint, pro_asociacion_fk bigint,  pro_ult_mod_fecha timestamp without time zone, pro_ult_mod_usuario character varying(45), pro_version integer, CONSTRAINT sg_proyectos_institucionales_sede_pkey PRIMARY KEY (pro_pk), CONSTRAINT sg_proyectos_institucionales_sede_sede_fk FOREIGN KEY (pro_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk), CONSTRAINT sg_proyectos_institucionales_sede_proyectos_catalogo_fk FOREIGN KEY (pro_proyecto_institucional_fk) REFERENCES catalogo.sg_proyectos_institucionales(pin_pk), CONSTRAINT sg_proyectos_institucionales_sede_beneficio_fk FOREIGN KEY (pro_beneficio_fk) REFERENCES catalogo.sg_beneficios(bnf_pk), CONSTRAINT sg_proyectos_institucionales_sede_asociacion_fk FOREIGN KEY (pro_asociacion_fk) REFERENCES catalogo.sg_asociaciones(aso_pk));
    COMMENT ON TABLE centros_educativos.sg_proyectos_institucionales_sede IS 'Tabla para el registro de los proyectos institucionales.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_sede_fk IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_proyecto_institucional_fk IS 'Llave foranea que hace referencia al proyecto institucional al cual pertenece.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_fecha_otorgado IS 'Fecha de otorgación.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_observaciones IS 'Observaciones del registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_beneficio_fk IS 'Llave foranea que hace referencia al beneficio.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_asociacion_fk IS 'Llave foranea que hace referencia a la asociacion.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_institucionales_sede.pro_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_proyectos_institucionales_sede_aud (pro_pk bigint NOT NULL, pro_sede_fk bigint, pro_proyecto_institucional_fk bigint, pro_fecha_otorgado date, pro_observaciones character varying(500),pro_beneficio_fk bigint, pro_asociacion_fk bigint,  pro_ult_mod_fecha timestamp without time zone, pro_ult_mod_usuario character varying(45), pro_version integer, rev bigint, revtype smallint, CONSTRAINT sg_proyectos_institucionales_sede_aud_pkey PRIMARY KEY (pro_pk,rev),  CONSTRAINT sg_proyectos_institucionales_sede_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Proyectos institucionales para estudiantes
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_proyectos_inst_estudiantes_pie_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_proyectos_inst_estudiantes (pie_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_proyectos_inst_estudiantes_pie_pk_seq'::regclass), pie_proyecto_institucional_fk bigint, pie_estudiante_fk bigint, pie_fecha_otorgado date, pie_observaciones character varying(500), pie_ult_mod_fecha timestamp without time zone, pie_ult_mod_usuario character varying(45), pie_version integer, CONSTRAINT sg_proyectos_inst_estudiantes_pkey PRIMARY KEY (pie_pk), CONSTRAINT sg_proyectos_inst_estudiantes_proyectos_fk FOREIGN KEY (pie_proyecto_institucional_fk) REFERENCES centros_educativos.sg_proyectos_institucionales_sede(pro_pk), CONSTRAINT sg_proyectos_inst_estudiantes_estudiantes_fk FOREIGN KEY (pie_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk));
    COMMENT ON TABLE centros_educativos.sg_proyectos_inst_estudiantes IS 'Tabla para el registro de los proyectos institucionales asignados a los estudiantes.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_proyecto_institucional_fk IS 'Llave foranea que hace referencia al proyecto institucional al cual pertenece.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_estudiante_fk IS 'Llave foranea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_fecha_otorgado IS 'Fecha de otorgación.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_observaciones IS 'Observaciones del registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_proyectos_inst_estudiantes.pie_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_proyectos_inst_estudiantes_aud (pie_pk bigint NOT NULL, pie_proyecto_institucional_fk bigint, pie_estudiante_fk bigint, pie_fecha_otorgado date, pie_observaciones character varying(500), pie_ult_mod_fecha timestamp without time zone, pie_ult_mod_usuario character varying(45), pie_version integer, rev bigint, revtype smallint, CONSTRAINT sg_proyectos_inst_estudiantes_aud_pkey PRIMARY KEY (pie_pk,rev),  CONSTRAINT sg_proyectos_inst_estudiantes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


ALTER TABLE centros_educativos.sg_horarios_escolares ADD COLUMN hes_unico_docente boolean;
ALTER TABLE centros_educativos.sg_horarios_escolares ADD COLUMN hes_docente_fk bigint;
ALTER TABLE centros_educativos.sg_horarios_escolares_aud ADD COLUMN hes_unico_docente boolean;
ALTER TABLE centros_educativos.sg_horarios_escolares_aud ADD COLUMN hes_docente_fk bigint;
ALTER TABLE centros_educativos.sg_horarios_escolares ADD CONSTRAINT sg_horarios_escolares_docente_fkey FOREIGN KEY (hes_docente_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_unico_docente IS 'Identifica si el horario esta asignado a un solo docente.';
COMMENT ON COLUMN centros_educativos.sg_horarios_escolares.hes_docente_fk IS 'Llave foránea que hace referencia al docente, en el caso de que el horario es impartido por un único docente.';

-- Componentes docentes
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_componentes_docentes_cdo_pk_seq INCREMENT 1 START 10000000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componentes_docentes (cdo_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_componentes_docentes_cdo_pk_seq'::regclass), cdo_horario_escolar_fk bigint, cdo_componente_fk bigint, cdo_docente_fk bigint, cdo_ult_mod_fecha timestamp without time zone, cdo_ult_mod_usuario character varying(45), cdo_version integer, CONSTRAINT sg_componentes_docentes_pkey PRIMARY KEY (cdo_pk), CONSTRAINT sg_componentes_docentes_horario_docente_fk FOREIGN KEY (cdo_horario_escolar_fk) REFERENCES centros_educativos.sg_horarios_escolares(hes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_componentes_docentes_componente_fk FOREIGN KEY (cdo_componente_fk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_componentes_docentes_docente_fk FOREIGN KEY (cdo_docente_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_componentes_docentes IS 'Tabla para el registro de hdo.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_horario_escolar_fk IS 'Llave foránea que hace referencia al horario escolar.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_componente_fk IS 'Llave foránea que hace referencia al componente del plan de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_docente_fk IS 'Llave foránea que hace referencia al docente.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_componentes_docentes.cdo_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_componentes_docentes_aud (cdo_pk bigint NOT NULL, cdo_horario_escolar_fk bigint, cdo_componente_fk bigint, cdo_docente_fk bigint, cdo_ult_mod_fecha timestamp without time zone, cdo_ult_mod_usuario character varying(45), cdo_version integer, rev bigint, revtype smallint, CONSTRAINT sg_componentes_docentes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));



CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_plazas_pla_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_plazas(pla_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_plazas_pla_pk_seq'::regclass), pla_sede_solicitante_fk bigint, pla_tipo_plaza CHARACTER VARYING(25), pla_nivel_fk bigint, pla_modalidad_educativa_fk bigint, pla_modalidad_atencion_fk bigint, pla_opcion_fk bigint, pla_componente_plan_estudio_fk bigint, pla_especialidad_fk bigint, pla_fuente_financiamiento_fk bigint, pla_fecha_desde date, pla_fecha_hasta date, pla_jornada_fk bigint, pla_descripcion CHARACTER VARYING(400), pla_estado CHARACTER VARYING(25), pla_tipo_nombramiento_fk bigint, pla_ult_mod_fecha timestamp without TIME zone, pla_ult_mod_usuario CHARACTER varying(45), pla_version INTEGER, CONSTRAINT sg_plazas_pkey                   PRIMARY KEY (pla_pk), CONSTRAINT sg_plazas_sede_fk                FOREIGN KEY (pla_sede_solicitante_fk)           REFERENCES centros_educativos.sg_sedes(sed_pk)                    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_nivel_fk               FOREIGN KEY (pla_nivel_fk)                      REFERENCES centros_educativos.sg_niveles(niv_pk)                    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_modalidad_educativa_fk FOREIGN KEY (pla_modalidad_educativa_fk)        REFERENCES centros_educativos.sg_modalidades(mod_pk)                MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_modalidad_atencion_fk  FOREIGN KEY (pla_modalidad_atencion_fk)         REFERENCES catalogo.sg_modalidades_atencion(mat_pk)                 MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_opcion_fk              FOREIGN KEY (pla_opcion_fk)                     REFERENCES centros_educativos.sg_opciones(opc_pk)                   MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_componente_fk          FOREIGN KEY (pla_componente_plan_estudio_fk)    REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk)    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_especialidad_fk        FOREIGN KEY (pla_especialidad_fk)               REFERENCES catalogo.sg_especialidades(esp_pk)                       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_fuente_fk              FOREIGN KEY (pla_fuente_financiamiento_fk)      REFERENCES catalogo.sg_fuente_financiamiento(ffi_pk)                MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_jornada_fk             FOREIGN KEY (pla_jornada_fk)                    REFERENCES catalogo.sg_jornadas_laborales(jla_pk)                   MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plazas_nombramiento_fk        FOREIGN KEY (pla_tipo_nombramiento_fk)          REFERENCES catalogo.sg_cargo(car_pk)                                MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_plazas                           IS 'Tabla para almacenar las plazas de la sede.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_sede_solicitante_fk  IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_tipo_plaza               IS 'Tipo de plaza.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_nivel_fk               IS 'Llave foranea que hace referencia al nivel.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_modalidad_educativa_fk        IS 'Llave foranea que hace referencia a la modalidad educativa.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_modalidad_atencion_fk       IS 'Llave foranea que hace referencia a la modalidad de atención.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_opcion_fk       IS 'Llave foranea que hace referencia a la opción.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_componente_plan_estudio_fk       IS 'Llave foranea que hace referencia al componente de plan de estudio.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_especialidad_fk       IS 'Llave foranea que hace referencia a la especialidad.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_fuente_financiamiento_fk       IS 'Llave foranea que hace referencia a la fuente de financiamiento.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_fecha_desde          IS 'Fecha desde que se tendrá el financiamiento.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_fecha_hasta             IS 'Fecha hasta que se tendrá el financiamiento.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_jornada_fk       IS 'Llave foranea que hace referencia a la jornada.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_descripcion       IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_estado       IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_tipo_nombramiento_fk       IS 'Llave foranea que hace referencia al tipo de nombramiento.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_plazas.pla_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_plazas_aud (pla_pk bigint NOT NULL, pla_sede_solicitante_fk bigint, pla_tipo_plaza CHARACTER VARYING(25), pla_nivel_fk bigint, pla_modalidad_educativa_fk bigint, pla_modalidad_atencion_fk bigint, pla_opcion_fk bigint, pla_componente_plan_estudio_fk bigint, pla_especialidad_fk bigint, pla_fuente_financiamiento_fk bigint, pla_fecha_desde date, pla_fecha_hasta date, pla_jornada_fk bigint, pla_descripcion CHARACTER VARYING(400), pla_estado CHARACTER VARYING(25), pla_tipo_nombramiento_fk bigint, pla_ult_mod_fecha timestamp without TIME zone, pla_ult_mod_usuario CHARACTER varying(45), pla_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_plazas_aud_pkey PRIMARY KEY (pla_pk, rev), CONSTRAINT sg_plazas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));
     

-- Control de asistencia personal cabezal
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_control_asistencia_personal_cabezal_cpc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_control_asistencia_personal_cabezal (cpc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_control_asistencia_personal_cabezal_cpc_pk_seq'::regclass), cpc_sede_fk bigint, cpc_fecha date, cpc_personal_en_lista INTEGER, cpc_personal_presente INTEGER, cpc_personal_ausentes_justificados INTEGER, cpc_personal_ausentes_sin_justificar INTEGER, cpc_ult_mod_fecha timestamp without time zone, cpc_ult_mod_usuario character varying(45), cpc_version integer, CONSTRAINT sg_control_asistencia_personal_cabezal_pkey PRIMARY KEY (cpc_pk), CONSTRAINT sg_control_asistencia_personal_cabezal_sede_fkey FOREIGN KEY (cpc_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT cpc_fecha_sede_uk UNIQUE (cpc_fecha,cpc_sede_fk));
    COMMENT ON TABLE  centros_educativos.sg_control_asistencia_personal_cabezal                                         IS 'Tabla para el registro de los controles de asistencia por sede.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_pk                                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_sede_fk                             IS 'Llave foranea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_fecha                               IS 'Fecha del registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_personal_en_lista                   IS 'Cantidad de estudiantes en la lista.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_personal_presente                   IS 'Cantidad de estudiantes presentes.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_personal_ausentes_justificados      IS 'Cantidad de estudiantes ausentes con justificación.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_personal_ausentes_sin_justificar    IS 'Cantidad de estudiantes ausentes sin justificación.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_control_asistencia_personal_cabezal.cpc_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_control_asistencia_personal_cabezal_aud (cpc_pk bigint NOT NULL, cpc_sede_fk bigint, cpc_fecha date, cpc_personal_en_lista INTEGER, cpc_personal_presente INTEGER, cpc_personal_ausentes_justificados INTEGER, cpc_personal_ausentes_sin_justificar INTEGER, cpc_ult_mod_fecha timestamp without time zone, cpc_ult_mod_usuario character varying(45), cpc_version integer, rev bigint, revtype smallint, CONSTRAINT control_asistencia_personal_cabezal_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Asistencia personal
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_asistencias_personal_ape_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asistencias_personal (ape_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_asistencias_personal_ape_pk_seq'::regclass), ape_control_fk bigint, ape_personal_fk bigint, ape_cargo_fk bigint, ape_inasistencia BOOLEAN, ape_motivo_inasistencia_fk bigint, ape_observacion CHARACTER VARYING(100), ape_ult_mod_fecha timestamp without time zone, ape_ult_mod_usuario character varying(45), ape_version integer, CONSTRAINT sg_asistencias_personal_pkey PRIMARY KEY (ape_pk), CONSTRAINT sg_asistencias_personal_cabezal_fkey FOREIGN KEY (ape_control_fk) REFERENCES centros_educativos.sg_control_asistencia_personal_cabezal(cpc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencias_personal_personal_fkey FOREIGN KEY (ape_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencias_personal_cargo_fkey FOREIGN KEY (ape_cargo_fk) REFERENCES catalogo.sg_cargo(car_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_asistencias_personal_motivo_inasistencia_fkey FOREIGN KEY (ape_motivo_inasistencia_fk) REFERENCES catalogo.sg_motivos_inasistencia_personal(mip_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_asistencias_personal                         IS 'Tabla para el registro de las inasistencias del personal de la sede educativa.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_control_fk          IS 'Clave foránea que hace referencia al control de asistencia cabezal del personal.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_personal_fk          IS 'Clave foránea que hace referencia al personal.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_cargo_fk          IS 'Clave foránea que hace referencia al cargo.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_inasistencia        IS 'Indica si es una inasistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_motivo_inasistencia_fk IS 'Clave foránea que hace referencia al tipo de inasistencia del personal.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_observacion         IS 'Observación de la inasistencia.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_asistencias_personal.ape_version             IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_asistencias_personal_aud (ape_pk bigint NOT NULL, ape_control_fk bigint, ape_personal_fk bigint, ape_cargo_fk bigint, ape_inasistencia BOOLEAN, ape_motivo_inasistencia_fk bigint, ape_observacion CHARACTER VARYING(100), ape_ult_mod_fecha timestamp without time zone, ape_ult_mod_usuario character varying(45), ape_version integer, rev bigint, revtype smallint, CONSTRAINT control_asistencia_personal_cabezal_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACCION_PREVENIR_EMBARAZO','E233',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACCION_PREVENIR_EMBARAZO','E234',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACCION_PREVENIR_EMBARAZO','E235',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASISTENCIA_SEDE','E236',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASISTENCIA_SEDE','E237',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASISTENCIA_SEDE','E238',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_FACTOR_RIESGO_SEDE','E239',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_FACTOR_RIESGO_SEDE','E240',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_FACTOR_RIESGO_SEDE','E241',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL','E244',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL','E245',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL','E246',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ASISTENCIA_PERSONAL','E247',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ASISTENCIA_PERSONAL','E248',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ASISTENCIA_PERSONAL','E249',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PLAZA','E250',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PLAZA','E251',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PLAZA','E252',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('APROBAR_PLAZA','E253',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CASO_VIOLACION','E265',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CASO_VIOLACION','E266',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CASO_VIOLACION','E267',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL','EB60',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ASISTENCIA_PERSONAL','EB61',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_PLAZA','EB62',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONTROL_ASISTENCIA_PERSONAL','EM35',  '', 1, true, null, null,0);   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PLAZA','EM36',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_componente_plan_estudio DROP COLUMN cpe_contenido_tematico;
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud DROP COLUMN cpe_contenido_tematico;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_contenido_tematico CHARACTER VARYING(4000);
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_contenido_tematico CHARACTER VARYING(4000);
    
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTO_INSTITUCIONAL_SEDE','E268',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTO_INSTITUCIONAL_SEDE','E269',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTO_INSTITUCIONAL_SEDE','E270',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROYECTO_INST_ESTUDIANTE','E271',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROYECTO_INST_ESTUDIANTE','E272',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROYECTO_INST_ESTUDIANTE','E273',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_COMPONENTE_DOCENTE','E274',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_COMPONENTE_DOCENTE','E275',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_COMPONENTE_DOCENTE','E276',  '', 1, true, null, null,0);

-- 3.1.1

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_consulta_guardada_cgr_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_consulta_guardada (cgr_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_consulta_guardada_cgr_pk_seq'::regclass), cgr_habilitado boolean, cgr_nombre character varying(255), cgr_nombre_busqueda character varying(255),cgr_consulta text,cgr_usuario character varying(255), cgr_filtro character varying(255), cgr_ult_mod_fecha timestamp without time zone, cgr_ult_mod_usuario character varying(45), cgr_version integer, CONSTRAINT sg_consulta_guardada_pkey PRIMARY KEY (cgr_pk));
COMMENT ON TABLE centros_educativos.sg_consulta_guardada IS 'Tabla para el registro de consultas guardadas.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_nombre IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_nombre_busqueda IS 'Nombre del registro normalizado para búsquedas.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_consulta_guardada.cgr_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_consulta_guardada_aud (cgr_pk bigint NOT NULL,  cgr_habilitado boolean, cgr_nombre character varying(255), cgr_nombre_busqueda character varying(255),cgr_consulta text,cgr_usuario character varying(255),cgr_filtro character varying(255), cgr_ult_mod_fecha timestamp without time zone, cgr_ult_mod_usuario character varying(45), cgr_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_consulta_guardada_aud ADD PRIMARY KEY (cgr_pk, rev);

ALTER TABLE centros_educativos.sg_consulta_guardada ADD CONSTRAINT sec_usuario_consulta_uk UNIQUE (cgr_nombre,cgr_filtro, cgr_usuario);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONSULTA_GUARDADA','E278',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CONSULTA_GUARDADA','E279',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CONSULTA_GUARDADA','E280',  '', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_sedes DROP COLUMN sed_casos_violacion;
ALTER TABLE centros_educativos.sg_sedes_aud DROP COLUMN sed_casos_violacion;

ALTER TABLE centros_educativos.sg_casos_violacion DROP COLUMN cvi_cantidad_casos;
ALTER TABLE centros_educativos.sg_casos_violacion_aud DROP COLUMN cvi_cantidad_casos;

ALTER TABLE centros_educativos.sg_casos_violacion ADD COLUMN cvi_violaciones boolean;
ALTER TABLE centros_educativos.sg_casos_violacion_aud ADD COLUMN cvi_violaciones boolean;
COMMENT ON COLUMN centros_educativos.sg_casos_violacion.cvi_violaciones IS 'Indica si tuvo conocimiento de violaciones.';


ALTER TABLE centros_educativos.sg_sedes     DROP COLUMN     sed_velocidad_internet;
ALTER TABLE centros_educativos.sg_sedes_aud DROP COLUMN     sed_velocidad_internet;
ALTER TABLE centros_educativos.sg_sedes     ADD COLUMN      sed_velocidad_internet_fk bigint;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN      sed_velocidad_internet_fk bigint;
ALTER TABLE centros_educativos.sg_sedes     ADD CONSTRAINT  sg_sedes_velocidad_fkey FOREIGN KEY (sed_velocidad_internet_fk) REFERENCES catalogo.sg_velocidades_internet(vin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_acciones_prevenir_embarazo        ADD COLUMN ape_descripcion character varying(255);
ALTER TABLE centros_educativos.sg_acciones_prevenir_embarazo_aud    ADD COLUMN ape_descripcion character varying(255);
COMMENT ON COLUMN centros_educativos.sg_acciones_prevenir_embarazo.ape_descripcion IS 'Descripción del registro.';


ALTER TABLE centros_educativos.sg_sedes     DROP COLUMN sed_consejo_consultivo_educacion;
ALTER TABLE centros_educativos.sg_sedes_aud DROP COLUMN sed_consejo_consultivo_educacion;


-- Organismos de Coordinación Escolar
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_organismos_ce_sedes_ocs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organismos_ce_sedes (ocs_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_organismos_ce_sedes_ocs_pk_seq'::regclass), ocs_sede_fk bigint, ocs_org_coordinacion_escolar_fk bigint, ocs_consejo_consultivo boolean, ocs_funcionando boolean, ocs_ult_mod_fecha timestamp without time zone, ocs_ult_mod_usuario character varying(45), ocs_version integer, CONSTRAINT sg_organismos_ce_sedes_pkey PRIMARY KEY (ocs_pk), CONSTRAINT sg_organismos_ce_sedes_sede_fkey FOREIGN KEY (ocs_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_organismos_ce_sedes_organismo_fkey FOREIGN KEY (ocs_org_coordinacion_escolar_fk) REFERENCES catalogo.sg_organismos_coordinacion_escolar(oce_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_organismos_ce_sedes IS 'Tabla para el registro de organismos de coordinación escolar.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_sede_fk IS 'Llave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_org_coordinacion_escolar_fk IS 'Llave foránea que hace referencia al organismo de coordinación escolar.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_consejo_consultivo IS 'Indica si el registro tiene consejo consultivo de educación.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_funcionando IS 'Indica si esta funcionando en el centro.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_organismos_ce_sedes.ocs_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organismos_ce_sedes_aud (ocs_pk bigint NOT NULL, ocs_sede_fk bigint, ocs_org_coordinacion_escolar_fk bigint, ocs_consejo_consultivo boolean, ocs_funcionando boolean, ocs_ult_mod_fecha timestamp without time zone, ocs_ult_mod_usuario character varying(45), ocs_version integer, rev bigint, revtype smallint, CONSTRAINT sg_organismos_ce_sedes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ORGANISMO_CE_SEDE','E283',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ORGANISMO_CE_SEDE','E284',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ORGANISMO_CE_SEDE','E285',  '', 1, true, null, null,0);

--Cierre de años lectivos
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CIERRE_ANIO_LECTIVO_SEDE','EB63',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CIERRE_ANIO_LECTIVO_SEDE','E263',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CIERRE_ANIO_LECTIVO_SEDE','E264',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CIERRE_ANIO_LECTIVO','EM39',  '', 1, true, null, null,0);

CREATE SEQUENCE centros_educativos.sg_cierre_anio_lectivo_sede_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;

CREATE TABLE centros_educativos.sg_cierre_anio_lectivo_sede (
    cal_pk bigserial NOT NULL, -- Número correlativo autogenerado.
    cal_ale_fk int8 NOT NULL, -- Clave foranea de Año Lectivo.
    cal_sed_fk int8 NOT NULL, -- Clave foranea de Sede o centro educativo.
    cal_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
    cal_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
    cal_version int4 NULL,
    CONSTRAINT sg_cierre_anio_lectivo_sede_pk PRIMARY KEY (cal_pk),
    CONSTRAINT sg_cierre_anio_lectivo_sede_sg_anio_lectivo_fk FOREIGN KEY (cal_pk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk),
    CONSTRAINT sg_cierre_anio_lectivo_sede_sg_sedes_fk FOREIGN KEY (cal_sed_fk) REFERENCES centros_educativos.sg_sedes(sed_pk)
);
COMMENT ON TABLE centros_educativos.sg_cierre_anio_lectivo_sede IS 'Tabla para el registro del cierre del año lectivo de un centro educativo.';

-- Column comments

COMMENT ON COLUMN centros_educativos.sg_cierre_anio_lectivo_sede.cal_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_cierre_anio_lectivo_sede.cal_ale_fk IS 'Clave foranea de Año Lectivo.';
COMMENT ON COLUMN centros_educativos.sg_cierre_anio_lectivo_sede.cal_sed_fk IS 'Clave foranea de Sede o centro educativo.';
COMMENT ON COLUMN centros_educativos.sg_cierre_anio_lectivo_sede.cal_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_cierre_anio_lectivo_sede.cal_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ALTER cal_pk SET DEFAULT nextval('centros_educativos.sg_cierre_anio_lectivo_sede_pk_seq');
CREATE TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud (
    cal_pk bigserial NOT NULL, -- Número correlativo autogenerado.
    cal_ale_fk int8 NOT NULL, -- Clave foranea de Año Lectivo.
    cal_sed_fk int8 NOT NULL, -- Clave foranea de Sede o centro educativo.
    cal_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
    cal_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
    cal_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_cierre_anio_lectivo_aud_pkey PRIMARY KEY (cal_pk, rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PLAZAS_DISPONIBLES','EM40',  '', 1, true, null, null,0);


--Datos de Salud del Estudiante
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DATOS_SALUD_ESTUDIANTE','EP28',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_DATOS_SALUD_ESTUDIANTE','EP29',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DATO_SALUD','E281',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DATO_SALUD','E282',  '', 1, true, null, null,0);


CREATE SEQUENCE centros_educativos.sg_estudiante_datos_salud_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;


CREATE TABLE centros_educativos.sg_datos_salud_estudiante (
	dse_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	dse_ale_fk int8 NOT NULL, -- Clave foranea de Año Lectivo.
	dse_tap_fk int8 NOT NULL, -- Clave foranea de Tipo de Apoyo.
	dse_est_fk int8 NOT NULL, -- Clave foranea de Estudiante
	dse_descripcion varchar(255) NULL, -- Descripción opcional.
	dse_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	dse_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	dse_version int4 NULL,
	CONSTRAINT sg_datos_salud_estudiante_pk PRIMARY KEY (dse_pk),
	CONSTRAINT sg_datos_salud_estudiante_sg_anio_lectivo_fk FOREIGN KEY (dse_ale_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk),
	CONSTRAINT sg_datos_salud_estudiante_sg_estudiantes_fk FOREIGN KEY (dse_est_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk),
	CONSTRAINT sg_datos_salud_estudiante_sg_tipos_apoyo_fk FOREIGN KEY (dse_tap_fk) REFERENCES catalogo.sg_tipos_apoyo(tap_pk)
);


COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_ale_fk IS 'Clave foranea de Año Lectivo.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_tap_fk IS 'Clave foranea de Tipo de Apoyo.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_est_fk IS 'Clave foranea de Estudiante.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_descripcion IS 'Descripción opcional.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_datos_salud_estudiante.dse_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE centros_educativos.sg_datos_salud_estudiante ALTER dse_pk SET DEFAULT nextval('centros_educativos.sg_estudiante_datos_salud_pk_seq');

CREATE TABLE centros_educativos.sg_datos_salud_estudiante_aud (
    dse_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	dse_ale_fk int8 NOT NULL, -- Clave foranea de Año Lectivo.
	dse_tap_fk int8 NOT NULL, -- Clave foranea de Tipo de Apoyo.
	dse_est_fk int8 NOT NULL, -- Clave foranea de Estudiante
	dse_descripcion varchar(255) NULL, -- Descripción opcional.
	dse_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	dse_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	dse_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_datos_salud_estudiante_aud_pkey PRIMARY KEY (dse_pk, rev)
);

-- 3.2.0

-- Propuesta pedagógica
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_propuestas_pedagogicas_ppe_pk_seq INCREMENT 1 START 10000000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_propuestas_pedagogicas (ppe_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_propuestas_pedagogicas_ppe_pk_seq'::regclass), ppe_sede_fk bigint, ppe_nombre character varying(100), ppe_caracterizacion character varying(4000), ppe_problemas_priorizados character varying(4000), ppe_perfil_estudiante character varying(4000), ppe_retos_pedagogicos character varying(4000), ppe_acuerdos_pedagogicos character varying(4000), ppe_metas character varying(4000), ppe_indicadores character varying(4000), ppe_fecha_inicio date,ppe_fecha_fin date, ppe_archivo_fk bigint, ppe_ult_mod_fecha timestamp without time zone, ppe_ult_mod_usuario character varying(45), ppe_version integer, CONSTRAINT sg_propuestas_pedagogicas_pkey PRIMARY KEY (ppe_pk), CONSTRAINT sg_propuestas_pedagogicas_sedes_fk FOREIGN KEY (ppe_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,  CONSTRAINT sg_propuestas_pedagogicas_archivos_fk FOREIGN KEY (ppe_archivo_fk) REFERENCES centros_educativos.sg_archivos (ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_propuestas_pedagogicas IS 'Tabla para el registro de las Propuestas pedagógicas.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_sede_fk IS 'Llave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_nombre IS 'Nombre de la propuesta pedagógica.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_caracterizacion IS 'Texto enriquecido de la caracterización.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_problemas_priorizados IS 'Texto enriquecido de los problemas priorizados.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_perfil_estudiante IS 'Texto enriquecido de los perfiles del estudiante a formar.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_retos_pedagogicos IS 'Texto enriquecido de los retos pedagógicos.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_acuerdos_pedagogicos IS 'Texto enriquecido de los acuerdos pedagógicos.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_metas IS 'Texto enriquecido de las metas.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_indicadores IS 'Texto enriquecido de los indicadores.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_fecha_inicio IS 'Fecha de inicio del registro.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_fecha_fin IS 'Fecha final del registro.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_archivo_fk IS 'Llave foránea que hace referencia al archivo del registro.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_propuestas_pedagogicas.ppe_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_propuestas_pedagogicas_aud (ppe_pk bigint NOT NULL, ppe_sede_fk bigint, ppe_nombre character varying(100), ppe_caracterizacion character varying(4000), ppe_problemas_priorizados character varying(4000), ppe_perfil_estudiante character varying(4000), ppe_retos_pedagogicos character varying(4000), ppe_acuerdos_pedagogicos character varying(4000), ppe_metas character varying(4000), ppe_indicadores character varying(4000), ppe_fecha_inicio date,ppe_fecha_fin date, ppe_archivo_fk bigint, ppe_ult_mod_fecha timestamp without time zone, ppe_ult_mod_usuario character varying(45), ppe_version integer, rev bigint, revtype smallint, CONSTRAINT sg_propuestas_pedagogicas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PROPUESTA_PEDAGOGICA','E254',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PROPUESTA_PEDAGOGICA','E255',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PROPUESTA_PEDAGOGICA','E256',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PROPUESTA_PEDAGOGICA','EM37',  '', 1, true, null, null,0);



--Plan Escolar Anual
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_plan_escolar_anual_pea_pk_seq INCREMENT 1 START 10000000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_plan_escolar_anual (pea_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_plan_escolar_anual_pea_pk_seq'::regclass), pea_sede_fk bigint, pea_propuesta_pedagogica_fk bigint, pea_anio_lectivo_fk bigint, pea_fecha_presentado date, pea_evaluacion_uno character varying(255), pea_porcentaje_logro_uno integer, pea_evaluacion_dos character varying(255), pea_porcentaje_logro_dos integer, pea_estado character varying(25), pea_fecha_valido date, pea_usuario_valido_fk bigint, pea_ult_mod_fecha timestamp without time zone, pea_ult_mod_usuario character varying(45), pea_version integer, CONSTRAINT sg_plan_escolar_anual_pkey PRIMARY KEY (pea_pk),CONSTRAINT pea_sede_propuesta_uk UNIQUE (pea_sede_fk, pea_propuesta_pedagogica_fk, pea_anio_lectivo_fk), CONSTRAINT sg_plan_escolar_anual_sedes_fk FOREIGN KEY (pea_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plan_escolar_anual_propuesta_fk FOREIGN KEY (pea_propuesta_pedagogica_fk) REFERENCES centros_educativos.sg_propuestas_pedagogicas(ppe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_plan_escolar_anual_anio_lectivo_fk FOREIGN KEY (pea_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_pea_usuario_valido_fk FOREIGN KEY (pea_usuario_valido_fk) REFERENCES seguridad.sg_usuarios(usu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_plan_escolar_anual IS 'Tabla para el registro de los planes escolares anuales.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_sede_fk IS 'Llave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_propuesta_pedagogica_fk IS 'Llave foránea que hace referencia a la propuesta pedagógica.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_anio_lectivo_fk IS 'Llave foránea que hace referencia al año lectivo.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_fecha_presentado IS 'Registro de la fecha en que fue presentado a la comunidad.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_evaluacion_uno IS 'Registro de la primera evaluación.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_porcentaje_logro_uno IS 'Porcentaje de logro de la evaluación uno';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_evaluacion_dos IS 'Registro de la segunda evaluación.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_porcentaje_logro_dos IS 'Porcentaje de logro de la evaluación dos';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_estado IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_fecha_valido IS 'Fecha de validación del registro.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_usuario_valido_fk IS 'Llave foránea que hace referencia al usuario que válido el registro.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_plan_escolar_anual.pea_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_plan_escolar_anual_aud (pea_pk bigint NOT NULL, pea_sede_fk bigint, pea_propuesta_pedagogica_fk bigint, pea_anio_lectivo_fk bigint, pea_fecha_presentado date, pea_evaluacion_uno character varying(255), pea_porcentaje_logro_uno integer, pea_evaluacion_dos character varying(255), pea_porcentaje_logro_dos integer, pea_estado character varying(25), pea_fecha_valido date, pea_usuario_valido_fk bigint, pea_ult_mod_fecha timestamp without time zone, pea_ult_mod_usuario character varying(45), pea_version integer, rev bigint, revtype smallint, CONSTRAINT sg_plan_escolar_anual_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PLAN_ESCOLAR_ANUAL','E257',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PLAN_ESCOLAR_ANUAL','E258',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PLAN_ESCOLAR_ANUAL','E259',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PLAN_ESCOLAR_ANUAL','EM38',  '', 1, true, null, null,0);

-- Detalle Plan Escolar
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_detalle_plan_escolar_dpe_pk_seq INCREMENT 1 START 10000000 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_detalle_plan_escolar (dpe_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_detalle_plan_escolar_dpe_pk_seq'::regclass), dpe_plan_escolar_anual_fk bigint, dpe_actividad character varying(500), dpe_responsables character varying(255), dpe_recursos_fk bigint, dpe_costo_estimado numeric(10,2), dpe_fuente_financiamiento_fk bigint, dpe_fecha_inicio date, dpe_fecha_fin date, dpe_validado boolean, dpe_comentario_valido character varying(255), dpe_ult_mod_fecha timestamp without time zone, dpe_ult_mod_usuario character varying(45), dpe_version integer, CONSTRAINT sg_detalle_plan_escolar_pkey PRIMARY KEY (dpe_pk), CONSTRAINT sg_detalle_plan_escolar_plan_anual_fk FOREIGN KEY (dpe_plan_escolar_anual_fk) REFERENCES centros_educativos.sg_plan_escolar_anual(pea_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_detalle_plan_escolar_recurso_fk FOREIGN KEY (dpe_recursos_fk) REFERENCES catalogo.sg_recursos(rcs_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_detalle_plan_escolar_fuente_financiamiento_fk FOREIGN KEY (dpe_fuente_financiamiento_fk) REFERENCES catalogo.sg_fuente_financiamiento(ffi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_detalle_plan_escolar IS 'Tabla para el registro de los planes escolares anuales.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_plan_escolar_anual_fk IS 'Llave foránea que hace referencia al plan escolar anual.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_actividad IS 'Actividad del registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_responsables IS 'Responsables del registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_recursos_fk IS 'Llave foránea que hace referencia a los recursos.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_costo_estimado IS 'Costo estimado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_fuente_financiamiento_fk IS 'Llave foránea que hace referencia a la fuente de financiamiento.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_fecha_inicio IS 'Fecha de inicio.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_fecha_fin IS 'Fecha final.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_validado IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_comentario_valido IS 'Comentario del registro cuando es validado..';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_detalle_plan_escolar.dpe_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_detalle_plan_escolar_aud (dpe_pk bigint NOT NULL, dpe_plan_escolar_anual_fk bigint, dpe_actividad character varying(500), dpe_responsables character varying(255), dpe_recursos_fk bigint, dpe_costo_estimado numeric(10,2), dpe_fuente_financiamiento_fk bigint, dpe_fecha_inicio date, dpe_fecha_fin date, dpe_validado boolean, dpe_comentario_valido character varying(255), dpe_ult_mod_fecha timestamp without time zone, dpe_ult_mod_usuario character varying(45), dpe_version integer, rev bigint, revtype smallint, CONSTRAINT sg_detalle_plan_escolar_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DETALLE_PLAN_ESCOLAR','E260',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DETALLE_PLAN_ESCOLAR','E261',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DETALLE_PLAN_ESCOLAR','E262',  '', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_FOTOS_PEA','EP26',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_FOTOS_PEA','EP27',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VALIDAR_PEA','EP30',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DATOS_SALUD_ESTUDIANTE','EB64',  '', 1, true, null, null,0);


-- Rel Grado Plan Estudio
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_grado_plan_estudio_rgp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_grado_plan_estudio (rgp_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_grado_plan_estudio_rgp_pk_seq'::regclass),rgp_grado_fk bigint, rgp_plan_estudio_fk bigint,rgp_formula_fk bigint, rgp_habilitado boolean, rgp_ult_mod_fecha timestamp without time zone, rgp_ult_mod_usuario character varying(45), rgp_version integer, CONSTRAINT sg_rel_grado_plan_estudio_pkey PRIMARY KEY (rgp_pk), CONSTRAINT sg_grado_rel_grado_plan_estudio_fkey FOREIGN KEY (rgp_grado_fk) REFERENCES centros_educativos.sg_grados(gra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_plan_esutdio_rel_grado_plan_estudio_fkey FOREIGN KEY (rgp_plan_estudio_fk) REFERENCES centros_educativos.sg_planes_estudio(pes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_formula_rel_grado_plan_estudio_fkey FOREIGN KEY (rgp_formula_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_rel_grado_plan_estudio IS 'Tabla para el registro de rel grado plan estudio.';
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_grado_plan_estudio_aud (rgp_pk bigint NOT NULL,rgp_grado_fk bigint, rgp_plan_estudio_fk bigint,rgp_formula_fk bigint,  rgp_habilitado boolean,rgp_ult_mod_fecha timestamp without time zone, rgp_ult_mod_usuario character varying(45), rgp_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD PRIMARY KEY (rgp_pk, rev);

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD CONSTRAINT grado_plan_estudi_uk UNIQUE (rgp_grado_fk,rgp_plan_estudio_fk);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_GRADO_PLAN_ESTUDIO','E286',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_GRADO_PLAN_ESTUDIO','E287',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_GRADO_PLAN_ESTUDIO','E288',  '', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_PROMOCIONES','EM41',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_promocion_grado character varying(50);
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_promocion_grado character varying(50);

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_promovido_calificacion character varying(50);
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_promovido_calificacion character varying(50);

-- 3.3.0

--INSCRIPCIÓN DIPLOMADO
ALTER TABLE centros_educativos.sg_modalidades ADD mod_aplica_diplomado bool NULL;
ALTER TABLE centros_educativos.sg_modalidades_aud ADD mod_aplica_diplomado bool NULL;


CREATE SEQUENCE centros_educativos.sg_diplomado_estudiante_pk_seq;

CREATE TABLE centros_educativos.sg_diplomados_estudiante (
	dep_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	dep_anio_lectivo_fk int8 NOT NULL, -- Clave Foranea de Anio Lectivo.
	dep_estudiante_fk int8 NOT NULL, -- Clave Foranea de Estudiante.
	dep_diplomado_fk int8 NOT NULL, -- Clave Foranea de Estudiante.
	dep_ult_mod_usuario varchar(45) NULL, -- Última fecha en la que se modificó el registro.
	dep_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	dep_version int4 NULL,
	CONSTRAINT sg_diplomados_estudiante_pk PRIMARY KEY (dep_pk),
	CONSTRAINT sg_diplomados_estudiante_sg_anio_lectivo_fk FOREIGN KEY (dep_anio_lectivo_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk),
	CONSTRAINT sg_diplomados_estudiante_sg_diplomados_fk FOREIGN KEY (dep_diplomado_fk) REFERENCES centros_educativos.sg_diplomados(dip_pk),
	CONSTRAINT sg_diplomados_estudiante_sg_estudiantes_fk FOREIGN KEY (dep_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk)
);


COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_anio_lectivo_fk IS 'Clave Foranea de Anio Lectivo.';
COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_estudiante_fk IS 'Clave Foranea de Estudiante.';
COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_diplomado_fk IS 'Clave Foranea de Estudiante.';
COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_diplomados_estudiante.dep_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE centros_educativos.sg_diplomados_estudiante ALTER dep_pk SET DEFAULT nextval('centros_educativos.sg_diplomado_estudiante_pk_seq');

CREATE TABLE centros_educativos.sg_diplomados_estudiante_aud (
    dep_pk int8 NOT NULL,
	dep_anio_lectivo_fk int8 NOT NULL,
	dep_estudiante_fk int8 NOT NULL,
    dep_diplomado_fk int8 NOT NULL,
	dep_ult_mod_usuario varchar(45) NULL,
	dep_ult_mod_fecha timestamp NULL,
	dep_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_diplomados_estudiante_aud_pkey PRIMARY KEY (dep_pk, rev)
);


--Reglas de Equivalencias

CREATE SEQUENCE centros_educativos.sg_regla_equivalencia_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;

CREATE TABLE centros_educativos.sg_regla_equivalencia (
	req_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	req_nombre varchar(255) NOT NULL, -- Nombre requerido.
	req_descripcion varchar(1000) NULL, -- Descripción opcional.
	req_normativa varchar(255) NULL, -- Normativa.
	req_habilitado bool NULL, -- Estado.
	req_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	req_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	req_version int4 NULL,
	CONSTRAINT sg_regla_equivalencia_pk PRIMARY KEY (req_pk)

);

-- Column comments
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_nombre IS 'Nombre requerido.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_descripcion IS 'Descripción opcional.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_normativa IS 'Normativa.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_habilitado IS 'Estado.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia.req_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';


ALTER TABLE centros_educativos.sg_regla_equivalencia ALTER req_pk SET DEFAULT nextval('centros_educativos.sg_regla_equivalencia_pk_seq');


CREATE TABLE centros_educativos.sg_regla_equivalencia_aud (
    req_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	req_nombre varchar(255) NOT NULL, -- Nombre requerido.
	req_descripcion varchar(1000) NULL, -- Descripción opcional.
	req_normativa varchar(255) NULL, -- Normativa.
	req_habilitado bool NULL, -- Estado.
	req_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	req_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	req_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_regla_equivalencia_aud_pkey PRIMARY KEY (req_pk, rev)
);


--Detalle de Reglas
CREATE SEQUENCE centros_educativos.sg_regla_equivalencia_detalle_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 10
    CACHE 1
    NO CYCLE;

CREATE TABLE centros_educativos.sg_regla_equivalencia_detalle (
	red_pk bigserial NOT NULL,
	red_grado_fk bigserial NOT NULL,
	red_plan_estudio_fk bigserial NOT NULL,
	red_regla_equivalencia_fk bigserial NOT NULL,
	red_habilitado bool NULL,
	red_operacion int4 not null,
	red_ult_mod_usuario varchar(45) NULL,
	red_ult_mod_fecha timestamp NULL,
	red_version int4 NULL,
	CONSTRAINT sg_regla_equivalencia_detalle_gra_plan_ope_uk  UNIQUE (red_grado_fk,red_plan_estudio_fk,red_operacion),
	CONSTRAINT sg_regla_equivalencia_detalle_sg_grados_fk FOREIGN KEY (red_grado_fk) REFERENCES centros_educativos.sg_grados(gra_pk),
	CONSTRAINT sg_regla_equivalencia_detalle_sg_planes_estudio_fk FOREIGN KEY (red_plan_estudio_fk) REFERENCES centros_educativos.sg_planes_estudio(pes_pk),
	CONSTRAINT sg_regla_equivalencia_detalle_sg_regla_equivalencia_fk FOREIGN KEY (red_regla_equivalencia_fk) REFERENCES centros_educativos.sg_regla_equivalencia(req_pk)
);

COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_grado_fk IS 'Calve foranea de Grado.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_plan_estudio_fk IS 'Calve foranea de Plan de estudio.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_regla_equivalencia_fk IS 'Clave foranea de Reglas de Equivalencia. Tabla Padre.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_habilitado IS 'Estado.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_ult_mod_usuario IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_regla_equivalencia_detalle.red_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ALTER red_pk SET DEFAULT nextval('centros_educativos.sg_regla_equivalencia_detalle_pk_seq');

CREATE TABLE centros_educativos.sg_regla_equivalencia_detalle_aud (
    red_pk bigserial NOT NULL,
	red_grado_fk bigserial NOT NULL,
	red_plan_estudio_fk bigserial NOT NULL,
	red_regla_equivalencia_fk bigserial NOT NULL,
	red_habilitado bool NULL,
	red_ult_mod_usuario varchar(45) NULL,
	red_ult_mod_fecha timestamp NULL,
	red_version int4 NULL,
    rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_regla_equivalencia_detalle_aud_pkey PRIMARY KEY (red_pk, rev)
);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_tiene_identificacion boolean;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_tiene_identificacion boolean;
COMMENT ON COLUMN centros_educativos.sg_personas.per_tiene_identificacion IS 'Indica si la persona posee al menos una identificación.';
update centros_educativos.sg_personas as per 	set per_tiene_identificacion = not (per_cun is null and per_nie is null and per_dui is null and per_nit is null and per_nup is null and per_isss is null and per_inpep is null and per_nip is null and (select count(*) from centros_educativos.sg_identificaciones as iden where iden.ide_persona = per.per_pk ) = 0);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividad_fotos (dpe_pk bigint NOT NULL,ach_pk bigint NOT NULL, CONSTRAINT sg_actividad_fotos_actividad_fk FOREIGN KEY (dpe_pk) REFERENCES centros_educativos.sg_detalle_plan_escolar (dpe_pk), CONSTRAINT sg_actividad_fotos_archivos_fk FOREIGN KEY (ach_pk) REFERENCES centros_educativos.sg_archivos (ach_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividad_fotos_aud(dpe_pk bigint NOT NULL,ach_pk bigint NOT NULL, rev bigint, revtype smallint);

-- Organismos de Administración Escolar
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_organismo_administracion_escolar_oae_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organismo_administracion_escolar (oae_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_organismo_administracion_escolar_oae_pk_seq'::regclass),oae_sede_fk bigint, oae_acta_integracion integer,oae_fecha_acta_integracion date,oae_fecha_vencimiento date,oea_estado character varying(50),oae_ult_mod_fecha timestamp without time zone, oae_ult_mod_usuario character varying(45), oae_version integer, CONSTRAINT sg_organismo_administracion_escolar_pkey PRIMARY KEY (oae_pk), CONSTRAINT sg_organismo_administracion_escolar_sede_fkey FOREIGN KEY (oae_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_organismo_administracion_escolar IS 'Tabla para el registro de organismos de administración escolar.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_sede_fk IS 'Sede del registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_acta_integracion IS 'número acta de integración del registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_fecha_acta_integracion IS 'Fecha acta integración del registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_fecha_vencimiento IS 'Fecha vencimiento del registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oea_estado IS 'Estado del registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_organismo_administracion_escolar.oae_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_organismo_administracion_escolar_aud (oae_pk bigint NOT NULL,oae_sede_fk bigint, oae_acta_integracion integer,oae_fecha_acta_integracion date,oae_fecha_vencimiento date,oea_estado character varying(50), oae_ult_mod_fecha timestamp without time zone, oae_ult_mod_usuario character varying(45), oae_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD PRIMARY KEY (oae_pk, rev);

-- Persona Organismos de Administración
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_persona_organismo_administracion_poa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_persona_organismo_administracion (poa_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_persona_organismo_administracion_poa_pk_seq'::regclass),poa_cargo bigint, poa_dui CHARACTER VARYING(20) , poa_nombre character varying(100),poa_nombre_busqueda character varying(100),poa_ocupacion character varying(100),poa_domicilio character varying(100),  poa_habilitado boolean, poa_organismo_administrativo_escolar bigint,poa_ult_mod_fecha timestamp without time zone, poa_ult_mod_usuario character varying(45), poa_version integer, CONSTRAINT sg_persona_organismo_administracion_pkey PRIMARY KEY (poa_pk),CONSTRAINT sg_persona_organismo_administracion_cargo_fkey FOREIGN KEY (poa_cargo) REFERENCES catalogo.sg_cargo_oae(coa_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_persona_organismo_administracion_oae_fkey FOREIGN KEY (poa_organismo_administrativo_escolar) REFERENCES centros_educativos.sg_organismo_administracion_escolar(oae_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_persona_organismo_administracion IS 'Tabla para el registro de persona organismos de administración.';
COMMENT ON COLUMN centros_educativos.sg_persona_organismo_administracion.poa_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_persona_organismo_administracion.poa_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_persona_organismo_administracion.poa_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_persona_organismo_administracion.poa_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_persona_organismo_administracion.poa_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_persona_organismo_administracion_aud (poa_pk bigint NOT NULL, poa_cargo bigint, poa_dui CHARACTER VARYING(20) , poa_nombre character varying(100),poa_nombre_busqueda character varying(100),poa_ocupacion character varying(100),poa_domicilio character varying(100),poa_organismo_administrativo_escolar bigint, poa_habilitado boolean, poa_ult_mod_fecha timestamp without time zone, poa_ult_mod_usuario character varying(45), poa_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD PRIMARY KEY (poa_pk, rev);


-- Modulos Formacion Docente
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_modulo_formacion_docente_mfd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulo_formacion_docente (mfd_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_modulo_formacion_docente_mfd_pk_seq'::regclass), mfd_codigo CHARACTER VARYING(10), mfd_nombre CHARACTER VARYING(255), mfd_nombre_busqueda CHARACTER VARYING(255), mfd_parte_pnfd BOOLEAN, mfd_habilitado BOOLEAN, mfd_ult_mod_fecha timestamp without time zone, mfd_ult_mod_usuario character varying(45), mfd_version integer, CONSTRAINT sg_modulo_formacion_docente_pkey PRIMARY KEY (mfd_pk), CONSTRAINT mfd_codigo_uk UNIQUE (mfd_codigo), CONSTRAINT mfd_nombre_uk UNIQUE (mfd_nombre));
    COMMENT ON TABLE centros_educativos.sg_modulo_formacion_docente IS 'Tabla para el registro de celdas día hora.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_codigo IS 'Relacion Componente Plan Grado de Registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_nombre IS 'RDía de Registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_nombre_busqueda IS 'Fila de Registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_parte_pnfd IS 'Hora del Registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_habilitado IS 'Hora del Registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_modulo_formacion_docente.mfd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_modulo_formacion_docente_aud (mfd_pk bigint NOT NULL, mfd_codigo CHARACTER VARYING(10), mfd_nombre CHARACTER VARYING(255), mfd_nombre_busqueda CHARACTER VARYING(255), mfd_parte_pnfd BOOLEAN, mfd_habilitado BOOLEAN, mfd_ult_mod_fecha timestamp without time zone, mfd_ult_mod_usuario character varying(45), mfd_version integer, rev bigint, revtype smallint, CONSTRAINT sg_mfd_aud_pk PRIMARY KEY (mfd_pk, rev), CONSTRAINT sg_mfd_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Cursos para Docentes
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_cursos_docentes_cds_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_cursos_docentes (cds_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_cursos_docentes_cds_pk_seq'::regclass), cds_modulo_fk bigint, cds_nombre character varying(45), cds_descripcion character varying(255), cds_facilitador character varying(100), cds_fecha_inicio date, cds_fecha_fin date, cds_categoria_fk bigint, cds_tipo_modulo_fk bigint, cds_nivel_fk bigint, cds_especialidad_fk bigint, cds_cupo integer, cds_sede_fk bigint, cds_otra_sede boolean, cds_sede_nombre character varying(100), cds_sede_direccion_fk bigint, cds_admite_inscripcion_solicitud boolean, cds_estado character varying(45), cds_ult_mod_fecha timestamp without time zone, cds_ult_mod_usuario character varying(45), cds_version integer, CONSTRAINT sg_cursos_docentes_pkey PRIMARY KEY (cds_pk), CONSTRAINT sg_cursos_docentes_categoria_fk FOREIGN KEY (cds_categoria_fk) REFERENCES catalogo.sg_categorias_cursos(ccu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_modulo_formacion_fk FOREIGN KEY (cds_tipo_modulo_fk) REFERENCES catalogo.sg_tipos_modulos (tmo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_nivel_fk FOREIGN KEY (cds_nivel_fk) REFERENCES centros_educativos.sg_niveles (niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_especialidad_fk FOREIGN KEY (cds_especialidad_fk) REFERENCES catalogo.sg_especialidades (esp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_sede_fk FOREIGN KEY (cds_sede_fk) REFERENCES centros_educativos.sg_sedes (sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_direccion_fk FOREIGN KEY (cds_sede_direccion_fk) REFERENCES centros_educativos.sg_direcciones (dir_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_cursos_docentes_modulo_fk FOREIGN KEY (cds_modulo_fk) REFERENCES centros_educativos.sg_modulo_formacion_docente (mfd_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_cursos_docentes IS 'Tabla para el registro de confirmaciones de matrículas.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_modulo_fk IS 'Llave foránea que hace referencia al módulo de formación docente.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_facilitador IS 'Nombre del facilitador.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_fecha_inicio IS 'Fecha de inicio.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_fecha_fin IS 'Fecha final.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_categoria_fk IS 'Llave foránea que hace referencia a la categoria.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_tipo_modulo_fk IS 'Llava foránea que hace referencia al tipo de módulo.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_nivel_fk IS 'Llave foránea que hace referencia al nivel.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_especialidad_fk IS 'Llave foránea que hace referencia a la especialidad.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_cupo IS 'Cupos disponibles.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_sede_fk IS 'Llave foránea que hace referencia .';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_otra_sede IS 'Indica si es otra sede.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_sede_nombre IS 'Nombre de la sede.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_sede_direccion_fk IS 'Llave foránea que hace referencia a la dirección de la sede.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_admite_inscripcion_solicitud IS 'Indica si admite inscripciones bajo solicitud.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_estado IS 'Estado del registro';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_cursos_docentes.cds_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_cursos_docentes_aud (cds_pk bigint NOT NULL, cds_modulo_fk bigint, cds_nombre character varying(45), cds_descripcion character varying(255), cds_facilitador character varying(100), cds_fecha_inicio date, cds_fecha_fin date, cds_categoria_fk bigint, cds_tipo_modulo_fk bigint, cds_nivel_fk bigint, cds_especialidad_fk bigint, cds_cupo integer,cds_sede_fk bigint, cds_otra_sede boolean, cds_sede_nombre character varying(100), cds_sede_direccion_fk bigint, cds_admite_inscripcion_solicitud boolean, cds_estado character varying(45), cds_ult_mod_fecha timestamp without time zone, cds_ult_mod_usuario character varying(45), cds_version integer, rev bigint, revtype smallint, CONSTRAINT sg_cursos_docentes_aud_pk PRIMARY KEY (cds_pk, rev), CONSTRAINT sg_cursos_docentes_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

-- Celdas Día Hora Cursos
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_celda_dia_hora_curso_cdc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_celda_dia_hora_curso (cdc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_celda_dia_hora_curso_cdc_pk_seq'::regclass), cdc_curso_fk bigint,cdc_dia CHARACTER VARYING(20), cdc_fila int,  cdc_hora time,cdc_ult_mod_fecha timestamp without time zone, cdc_ult_mod_usuario character varying(45), cdc_version integer, CONSTRAINT sg_celda_dia_hora_curso_pkey PRIMARY KEY (cdc_pk),  CONSTRAINT sg_cdc_curso_fk FOREIGN KEY (cdc_curso_fk) REFERENCES centros_educativos.sg_cursos_docentes(cds_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_celda_dia_hora_curso IS 'Tabla para el registro de celdas día hora.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_curso_fk IS 'Llave foránea que hace referencia al curso.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_dia IS 'Día de Registro.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_fila IS 'Fila de Registro.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_hora IS 'Hora del Registro.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_celda_dia_hora_curso.cdc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_celda_dia_hora_curso_aud (cdc_pk bigint NOT NULL, cdc_curso_fk bigint,cdc_dia CHARACTER VARYING(20), cdc_fila int,  cdc_hora time, cdc_ult_mod_fecha timestamp without time zone, cdc_ult_mod_usuario character varying(45), cdc_version integer, rev bigint, revtype smallint, CONSTRAINT sg_cdc_aud_pk PRIMARY KEY (cdc_pk, rev), CONSTRAINT sg_cdc_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Solicitudes de curso docente
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_solicitud_curso_docente_scd_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitud_curso_docente (scd_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_solicitud_curso_docente_scd_pk_seq'::regclass), scd_curso_fk bigint, scd_personal_fk bigint, scd_estado character varying(25),  scd_ult_mod_fecha timestamp without time zone, scd_ult_mod_usuario character varying(45), scd_version integer, CONSTRAINT sg_solicitud_curso_docente_pkey PRIMARY KEY (scd_pk),  CONSTRAINT sg_scd_curso_fk FOREIGN KEY (scd_curso_fk) REFERENCES centros_educativos.sg_cursos_docentes(cds_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,  CONSTRAINT sg_scd_personal_fk FOREIGN KEY (scd_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_solicitud_curso_docente IS 'Tabla para el registro de celdas día hora.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_curso_fk IS 'Llave foránea que hace referencia al curso.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_personal_fk IS 'Llave foránea que hace referencia al personal del centro educativo.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_estado IS 'Estado del registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_solicitud_curso_docente.scd_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_solicitud_curso_docente_aud (scd_pk bigint NOT NULL, scd_curso_fk bigint, scd_personal_fk bigint, scd_estado character varying(25), scd_ult_mod_fecha timestamp without time zone, scd_ult_mod_usuario character varying(45), scd_version integer, rev bigint, revtype smallint, CONSTRAINT sg_scd_aud_pk PRIMARY KEY (scd_pk, rev), CONSTRAINT sg_scd_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


-- Notificaciones
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_notificaciones_nfs_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_notificaciones (nfs_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_notificaciones_nfs_pk_seq'::regclass), nfs_tipo CHARACTER VARYING(25), nfs_sede_fk bigint, nfs_seccion_fk bigint,  nfs_estudiante_fk bigint, nfs_notificacion CHARACTER VARYING(400), nfs_texto_breve CHARACTER VARYING(100), nfs_fecha date, nfs_usuario_fk bigint, nfs_ult_mod_fecha timestamp without time zone, nfs_ult_mod_usuario character varying(45), nfs_version integer, CONSTRAINT sg_notificaciones_pkey PRIMARY KEY (nfs_pk),  CONSTRAINT sg_nfs_sede_fk FOREIGN KEY (nfs_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,  CONSTRAINT sg_nfs_seccion_fk FOREIGN KEY (nfs_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,  CONSTRAINT sg_nfs_estudiante_fk FOREIGN KEY (nfs_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,  CONSTRAINT sg_nfs_usuario_fk FOREIGN KEY (nfs_usuario_fk) REFERENCES seguridad.sg_usuarios(usu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE centros_educativos.sg_notificaciones IS 'Tabla para el registro de celdas día hora.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_tipo IS 'Tipo de notificación.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_sede_fk IS 'Llave foránea que hace referencia a la sede.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_seccion_fk IS 'Llave foránea que hace referencia a la sección.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_estudiante_fk IS 'Llave foránea que hace referencia al estudiante.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_notificacion IS 'Texto de la notificación a enviar.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_texto_breve IS 'Texto breve de la notificación.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_fecha IS 'Fecha del registro.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_usuario_fk IS 'Usuario del Registro.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_notificaciones.nfs_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_notificaciones_aud (nfs_pk bigint NOT NULL, nfs_tipo CHARACTER VARYING(25), nfs_sede_fk bigint, nfs_seccion_fk bigint,  nfs_estudiante_fk bigint, nfs_notificacion CHARACTER VARYING(400), nfs_texto_breve CHARACTER VARYING(100), nfs_fecha date, nfs_usuario_fk bigint, nfs_ult_mod_fecha timestamp without time zone, nfs_ult_mod_usuario character varying(45), nfs_version integer, rev bigint, revtype smallint, CONSTRAINT sg_nfs_aud_pk PRIMARY KEY (nfs_pk, rev), CONSTRAINT sg_nfs_aud_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CURSOS_DOCENTES','E289',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_CURSOS_DOCENTES','E290',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_CURSOS_DOCENTES','E291',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ORGANISMO_ADMINISTRACION_ESCOLAR','E292',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ORGANISMO_ADMINISTRACION_ESCOLAR','E293',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ORGANISMO_ADMINISTRACION_ESCOLAR','E294',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_PERSONA_ORGANISMO_ADMINISTRACION','E295',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_PERSONA_ORGANISMO_ADMINISTRACION','E296',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_PERSONA_ORGANISMO_ADMINISTRACION','E297',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_MODULO_FORMACION_DOCENTE','E298',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_MODULO_FORMACION_DOCENTE','E299',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_MODULO_FORMACION_DOCENTE','E300',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EVALUAR_ORGANISMO_ADMINISTRACION_ESCOLAR','E301',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_CURSO_DOCENTE','E302',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_CURSO_DOCENTE','E303',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_CURSO_DOCENTE','E304',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_NOTIFICACION','E305',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_NOTIFICACION','E306',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_NOTIFICACION','E307',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INCRIPCION_DIPLOMADO','E308',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INCRIPCION_DIPLOMADO','E309',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INCRIPCION_DIPLOMADO','E310',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('APROBAR_SOLICITUD_CURSO_DOCENTE','E311',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('RECHAZAR_SOLICITUD_CURSO_DOCENTE','E312',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CONTROL_ASISTENCIA_CABEZAL_ARCHIVO','E315',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REGLA_EQUIVALENCIA','E318',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REGLA_EQUIVALENCIA','E319',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REGLA_EQUIVALENCIA','E320',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REGLAS_EQUIVALENCIAS','EM42',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_MODULO_FORMACION_DOCENTE','EM43',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CURSOS_DOCENTES','EM44',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ORGANISMOS_ADMINISTRACION_ESCOLAR','EM45',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_EVALUACIONES_ORGANISMOS_ADMINISTRACION_ESCOLAR','EM46',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CURSOS_DOCENTES_DISPONIBLES','EM47',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_NOTIFICACION','EM48',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INSCRIBIR_DIPLOMADO','EM49',  '', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CURSOS_DOCENTES','EB65',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MODULO_FORMACION_DOCENTE','EB66',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_CURSO_DOCENTE','EB68',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REGLA_EQUIVALENCIA','EB70',  '', 1, true, null, null,0);

-- 3.3.2

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_usuario_pk bigint;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_usuario_pk bigint;

ALTER TABLE centros_educativos.sg_factor_riesgo_sedes DROP COLUMN fri_anio_fk;
ALTER TABLE centros_educativos.sg_factor_riesgo_sedes_aud DROP COLUMN fri_anio_fk;

--3.5.1

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_POR_ESTUDIANTE','EM51',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle_aud ADD COLUMN red_operacion int4;


-- 3.6.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_CALIFICACION_ARCHIVO','E323',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ARCHIVO_CALIFICACIONES','E324',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ARCHIVO_CALIFICACIONES','E325',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ARCHIVO_CALIFICACIONES','E326',  '', 1, true, null, null,0);


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_archivo_calificaciones_acc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivo_calificaciones ( acc_pk bigint NOT NULL DEFAULT nextval( 'centros_educativos.sg_archivo_calificaciones_acc_pk_seq' :: regclass ), acc_estado character varying(25), acc_archivo bigint, acc_descripcion character varying(1000), acc_ult_mod_fecha timestamp without time zone, acc_ult_mod_usuario character varying(45), acc_version integer, PRIMARY KEY (acc_pk), CONSTRAINT sg_archivo_calificaciones_archivo FOREIGN KEY (acc_archivo) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE );
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivo_calificaciones_aud ( acc_pk bigint, acc_estado character varying(25), acc_archivo bigint, acc_descripcion character varying(1000), acc_ult_mod_fecha timestamp without time zone, acc_ult_mod_usuario character varying(45), acc_version integer, rev bigint, revtype smallint );
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PROCESAR_ARCHIVO_CALIFICACION','E327',  '', 1, true, null, null,0);

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) VALUES('IMPORTAR_CALIFICACIONES_ASINC', 'Importar calificaciones asincronico', 'importar calificaciones asincronico', 0, '0', false);

--3.7.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CALIFICAR_ARCHIVO','EP36',  '', 1, true, null, null,0);

DELETE FROM seguridad.sg_operaciones where ope_codigo='E323';

-- 3.8.0

ALTER TABLE centros_educativos.sg_datos_contratacion ADD COLUMN dco_razones_interinato CHARACTER VARYING(255);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_razones_interinato CHARACTER VARYING(255);

ALTER TABLE centros_educativos.sg_datos_contratacion DROP COLUMN dco_estado_contratacion;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud DROP COLUMN dco_estado_contratacion;


DO $$
declare


usuarios_sin_persona seguridad.sg_usuarios%ROWTYPE;

per_pk_aux bigint;
personal_pk_aux bigint;


BEGIN



FOR usuarios_sin_persona IN(

		select * from seguridad.sg_usuarios usu where usu.usu_persona_pk isnull
		

) loop

RAISE NOTICE '----------- usuario (%)----',usuarios_sin_persona.usu_pk;
 per_pk_aux:=(select per_pk from centros_educativos.sg_personas where per_dui=usuarios_sin_persona.usu_codigo);
if per_pk_aux is not null then
	RAISE NOTICE '----------------------------------------------- persona (%)----',per_pk_aux;
	update centros_educativos.sg_personas set per_usuario_pk=usuarios_sin_persona.usu_pk where per_pk=per_pk_aux;
	update seguridad.sg_usuarios set usu_persona_pk=per_pk_aux where usu_pk=usuarios_sin_persona.usu_pk;
	
	personal_pk_aux=(select pse_pk from centros_educativos.sg_personal_sede_educativa where pse_persona_fk=per_pk_aux);
	if personal_pk_aux is not null then
		update seguridad.sg_usuarios set usu_personal_pk=personal_pk_aux where usu_pk=usuarios_sin_persona.usu_pk;
		RAISE NOTICE '--------------------------------------------------- personal (%)----',personal_pk_aux;
	end if;

end if;
END LOOP;


END$$;

--3.8.2
CREATE INDEX sg_personas_fecha_nac_idx  ON centros_educativos.sg_personas (per_fecha_nacimiento ASC NULLS LAST);

INSERT INTO seguridad.sg_roles
(rol_codigo, rol_nombre, rol_descripcion, rol_habilitado, rol_ult_mod_fecha, rol_ult_mod_usuario, rol_version, rol_delegable)
VALUES('DOCAULA', 'Docente de aula', '', true, now(), 'casuser', 0, false);

-- 3.9.0

ALTER TABLE centros_educativos.sg_manifestacion_violencia ADD COLUMN mvi_creacion_usuario character varying(45);
ALTER TABLE centros_educativos.sg_manifestacion_violencia_aud ADD COLUMN mvi_creacion_usuario character varying(45);
COMMENT ON COLUMN centros_educativos.sg_manifestacion_violencia.mvi_creacion_usuario IS 'Usuario que creó el registro.';

ALTER TABLE centros_educativos.sg_estudiantes_valoracion ADD COLUMN esv_creacion_usuario character varying(45);
ALTER TABLE centros_educativos.sg_estudiantes_valoracion_aud ADD COLUMN esv_creacion_usuario character varying(45);
COMMENT ON COLUMN centros_educativos.sg_estudiantes_valoracion.esv_creacion_usuario IS 'Usuario que creó el registro.';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_VALORACION_DOCENTE_PROPIA','EP37',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_MANIFESTACION_VIOLENCIA_PROPIA','EP38',  '', 1, true, null, null,0);


-- 3.9.1

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN est_ultima_seccion_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN est_ultima_seccion_fk bigint;
ALTER TABLE centros_educativos.sg_estudiantes ADD CONSTRAINT sg_estudiantes_ult_seccion_fkey FOREIGN KEY (est_ultima_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


UPDATE  centros_educativos.sg_estudiantes e set est_ultima_seccion_fk = (
    select mat_seccion_fk from centros_educativos.sg_matriculas where mat_pk = e.est_ultima_matricula_fk
);


CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_seccion_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_seccion_fk = (new.mat_seccion_fk) where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;

CREATE TRIGGER update_ultima_seccion_estudiante_trigger
AFTER INSERT OR UPDATE ON centros_educativos.sg_matriculas
FOR EACH ROW
EXECUTE PROCEDURE centros_educativos.update_ultima_seccion_estudiante();

-- 3.9.3

CREATE INDEX sg_datos_empleado_personal_sede_fk_idx  ON centros_educativos.sg_datos_empleado (dem_personal_sede_fk ASC NULLS LAST);
CREATE INDEX sg_datos_contratacion_dato_empleado_fk_idx  ON centros_educativos.sg_datos_contratacion (dco_dato_empleado_fk ASC NULLS LAST);
CREATE INDEX sg_datos_contratacion_centro_educativo_fk_idx  ON centros_educativos.sg_datos_contratacion (dco_centro_educativo_fk ASC NULLS LAST);

-- 3.10.0

ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN  acc_rango_fecha_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN  acc_rango_fecha_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD CONSTRAINT sg_acc_rango_fecha_fk FOREIGN KEY (acc_rango_fecha_fk) REFERENCES centros_educativos.sg_rango_fecha(rfe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN  acc_seccion_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN  acc_seccion_fk bigint;

ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN  acc_componente_plan_estudio_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN  acc_componente_plan_estudio_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD CONSTRAINT sg_acc_componente_plan_estudio_fk FOREIGN KEY (acc_componente_plan_estudio_fk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN  acc_escala_calificaciones_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN  acc_escala_calificaciones_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD CONSTRAINT sg_acc_escala_calificaciones_fk FOREIGN KEY (acc_escala_calificaciones_fk) REFERENCES catalogo.sg_escalas_calificacion(eca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_grados ADD COLUMN  gra_edad_minima integer;
ALTER TABLE centros_educativos.sg_grados_aud ADD COLUMN  gra_edad_minima integer;
ALTER TABLE centros_educativos.sg_grados ADD COLUMN  gra_edad_maxima integer;
ALTER TABLE centros_educativos.sg_grados_aud ADD COLUMN  gra_edad_maxima integer;

--Servicio social
ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN  est_realizo_servicio_social boolean;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN  est_realizo_servicio_social boolean;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN  est_fecha_servicio_social date;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN  est_fecha_servicio_social date;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN  est_cantidad_horas_servicio_social integer;
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN  est_cantidad_horas_servicio_social integer;

ALTER TABLE centros_educativos.sg_estudiantes ADD COLUMN  est_descripcion_servicio_social CHARACTER varying(1000);
ALTER TABLE centros_educativos.sg_estudiantes_aud ADD COLUMN  est_descripcion_servicio_social CHARACTER varying(1000);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_SERVICIO_SOCIAL','E337',  '', 1, true, null, null,0);

--3.11.0

UPDATE centros_educativos.sg_estudiantes set est_realizo_servicio_social=false;


-- 3.12.0

ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_codigo character varying(10);
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_codigo character varying(10);

ALTER TABLE centros_educativos.sg_plazas ADD CONSTRAINT pla_codigo_uk UNIQUE (pla_codigo);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOLICITUDES_PLAZA','EM55',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_DOCENTES','EM56',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_datos_contratacion        ADD COLUMN dco_modelo_contrato character varying(10);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud    ADD COLUMN dco_modelo_contrato character varying(10);

ALTER TABLE centros_educativos.sg_datos_contratacion        ADD COLUMN dco_codigo_plaza character varying(10);
ALTER TABLE centros_educativos.sg_datos_contratacion_aud    ADD COLUMN dco_codigo_plaza character varying(10);

UPDATE centros_educativos.sg_datos_contratacion set dco_modelo_contrato = 'ACUERDO' where dco_modelo_contrato is null;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_NOMBRAMIENTO_PERSONAL_SEDE','EP39',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_NOMBRAMIENTO_PERSONAL_SEDE','EP40',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_EXPERIENCIA_LABORAL_PERSONAL_SEDE','EP41',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_EXPERIENCIA_LABORAL_PERSONAL_SEDE','EP42',  '', 1, true, null, null,0);

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_personal_especialidades_rpe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_rel_personal_especialidades(rpe_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_rel_personal_especialidades_rpe_pk_seq'::regclass), rpe_personal_fk bigint not null, rpe_especialidad_fk bigint not null, rpe_ult_mod_fecha timestamp without TIME zone, rpe_ult_mod_usuario CHARACTER varying(45), rpe_version INTEGER, CONSTRAINT sg_rel_personal_especialidades_pkey PRIMARY KEY (rpe_pk), CONSTRAINT sg_rel_personal_especialidades_peresonal_fk FOREIGN KEY (rpe_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa (pse_pk), CONSTRAINT sg_rel_personal_especialidades_especialidad_fk FOREIGN KEY (rpe_especialidad_fk) REFERENCES catalogo.sg_especialidades (esp_pk));
    COMMENT ON TABLE  centros_educativos.sg_rel_personal_especialidades                           IS 'Tabla para almacenar la relacion entre el personal y las especialidades.';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_personal_fk           IS 'Llave foranea que hace referencia al personal.';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_especialidad_fk       IS 'Llave foranea que hace referencia a la especialidad';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_rel_personal_especialidades.rpe_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_personal_especialidades_aud (rpe_pk bigint NOT NULL, rpe_personal_fk bigint, rpe_especialidad_fk bigint, rpe_ult_mod_fecha timestamp without TIME zone, rpe_ult_mod_usuario CHARACTER varying(45), rpe_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_rel_personal_especialidades_aud_pkey PRIMARY KEY (rpe_pk, rev), CONSTRAINT sg_rel_personal_especialidades_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_PERSONAL_ESPECIALIDAD','E331',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_PERSONAL_ESPECIALIDAD','E332',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_PERSONAL_ESPECIALIDAD','E333',  '', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_exclusivo_seccion bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_exclusivo_seccion bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_exclusivo_seccion FOREIGN KEY (cpg_exclusivo_seccion) REFERENCES centros_educativos.sg_secciones(sec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_especial_seccion (cpe_pk bigint NOT NULL,CONSTRAINT sg_actividades_especial_seccion_pkey PRIMARY KEY (cpe_pk), CONSTRAINT sg_actividad_especial_seccion_componente_fk FOREIGN KEY (cpe_pk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_actividades_especial_seccion    IS 'Tabla para el registro de actividad especial.';
    COMMENT ON COLUMN centros_educativos.sg_actividades_especial_seccion.cpe_pk     IS 'Clave foránea que hace referencia al componente de plan de estudio.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_actividades_especial_seccion_aud (cpe_pk bigint NOT NULL, rev bigint, revtype smallint);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACTIVIDAD_ESPECIAL','E338',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EDITAR_ACTIVIDAD_ESPECIAL','E339',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACTIVIDAD_ESPECIAL','E340',  '', 1, true, null, null,0);

-- 3.12.4

CREATE INDEX sg_estudiante_ult_matricula_fk_idx ON centros_educativos.sg_estudiantes USING btree (est_ultima_matricula_fk);
CREATE INDEX sg_sec_servicio_educativo_fk_idx ON centros_educativos.sg_secciones USING btree (sec_servicio_educativo_fk);

CREATE INDEX sg_estudiante_ult_seccion_fk_idx ON centros_educativos.sg_estudiantes USING btree (est_ultima_seccion_fk);
CREATE INDEX sg_estudiante_ult_sede_fk_idx ON centros_educativos.sg_estudiantes USING btree (est_ultima_sede_fk);

alter table centros_educativos.sg_datos_empleado alter column dem_afp_fk drop not null;


update seguridad.sg_operaciones set ope_nombre = 'VER_EMPLEADO_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de empleado de un personal de sede' where ope_nombre = 'VER_EMPLEADO_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_EMPLEADO_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de empleado de un personal de sede' where ope_nombre = 'EDITAR_EMPLEADO_PERSONAL_SEDE';

update seguridad.sg_operaciones set ope_nombre = 'VER_NOMBRAMIENTO_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de nombramientos y contratos de un personal de sede' where ope_nombre = 'VER_NOMBRAMIENTO_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_NOMBRAMIENTO_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de nombramientos y contratos de un personal de sede' where ope_nombre = 'EDITAR_NOMBRAMIENTO_PERSONAL_SEDE';

update seguridad.sg_operaciones set ope_nombre = 'VER_EXPERIENCIA_LABORAL_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de experiencia laboral de un personal de sede' where ope_nombre = 'VER_EXPERIENCIA_LABORAL_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_EXPERIENCIA_LABORAL_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de experiencia laboral de un personal de sede' where ope_nombre = 'EDITAR_EXPERIENCIA_LABORAL_PERSONAL_SEDE';


update seguridad.sg_operaciones set ope_nombre = 'VER_ESTUDIOS_REALIZADOS_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de estudios realizados de un personal de sede' where ope_nombre = 'VER_ESTUDIOS_REALIZADOS_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_ESTUDIOS_REALIZADOS_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de estudios realizados de un personal de sede' where ope_nombre = 'EDITAR_ESTUDIOS_REALIZADOS_PERSONAL_SEDE';


update seguridad.sg_operaciones set ope_nombre = 'VER_FORMACION_DOCENTE_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de formación docente de un personal de sede' where ope_nombre = 'VER_FORMACION_DOCENTE_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_FORMACION_DOCENTE_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de formación docente de un personal de sede' where ope_nombre = 'EDITAR_FORMACION_DOCENTE_PERSONAL_SEDE';

update seguridad.sg_operaciones set ope_nombre = 'VER_FAMILIARES_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite visualizar el tab de familiares de un personal de sede' where ope_nombre = 'VER_FAMILIARES_PERSONAL_SEDE';
update seguridad.sg_operaciones set ope_nombre = 'EDITAR_FAMILIARES_PERSONAL_SEDE_TAB', ope_descripcion = 'Permite editar el tab de familiares de un personal de sede' where ope_nombre = 'EDITAR_FAMILIARES_PERSONAL_SEDE';

-- 3.13.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CREAR_CONTRATO_OTRO','EP51',  'Solapa nombramientos y contratos: mostrar botón crear contrato del tipo otro.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_CONTRATO_OTRO','EP52',  'Solapa nombramientos y contratos: mostrar botón actualizar contrato del tipo otro.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_ELIMINAR_CONTRATO_OTRO','EP53',  'Solapa nombramientos y contratos: mostrar botón eliminar contrato del tipo otro.', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CREAR_CONTRATO','EP44',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_CONTRATO','EP45',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_ELIMINAR_CONTRATO','EP46',  '', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CREAR_ACUERDO','EP47',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_ACUERDO','EP48',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_ELIMINAR_ACUERDO','EP49',  '', 1, true, null, null,0);

-- 3.14.0

--INMUBELES O TERRENOS
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_INMUEBLES_O_TERRENOS','EM54',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_INMUEBLE_O_TERRENO','E328',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_INMUEBLE_O_TERRENO','E329',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_INMUEBLE_O_TERRENO','E330',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_INMUEBLE_O_TERRENO','EB71',  '', 1, true, null, null,0);

CREATE SEQUENCE centros_educativos.sg_inmuebles_sedes_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE centros_educativos.sg_inmuebles_sedes (
	tis_pk int8 NOT NULL, -- Número correlativo autogenerado.
        tis_codigo varchar(10) NOT NULL, -- Codigo del registro
	tis_propietario bool NULL, -- Indica si el mined es propietario(true) o no(false).
	tis_area_total numeric(10,2) NULL, -- Indica el area total en metros cuadrados.
	tis_area_disponible numeric(10,2) NULL, -- Indica el area en metros cuadrados que no ha esta construida.
	tis_observaciones varchar(1000) NULL, -- Observaciones.
	tis_terreno_principal bool NULL, -- Indica si es el terreno principal de la sede.
	tis_sede_fk int8 NULL, -- LLave foranea de la Sede.
        tis_direccion_fk int8 NULL, --LLave foranea de la Dirección.
	tis_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	tis_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	tis_version int2 NULL, -- Versión del registro
	CONSTRAINT inmuebles_sedes_pk PRIMARY KEY (tis_pk),
	CONSTRAINT tis_codigo_uk UNIQUE (tis_codigo),
	CONSTRAINT sg_inmuebles_sedes_fk FOREIGN KEY (tis_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk),
	CONSTRAINT tis_direccion_fk FOREIGN KEY (tis_direccion_fk) REFERENCES centros_educativos.sg_direcciones(dir_pk)
);
CREATE INDEX sg_inmuebles_sedes_tis_sede_fk_idx ON centros_educativos.sg_inmuebles_sedes USING btree (tis_sede_fk);

-- Column comments

COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_codigo IS 'Código del registro.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_propietario IS 'Indica si el mined es propietario(true) o no(false).';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_area_total IS 'Indica el area total en metros cuadrados.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_area_disponible IS 'Indica el area en metros cuadrados que no ha esta construida.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_observaciones IS 'Observaciones.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_terreno_principal IS 'Indica si es el terreno principal de la sede.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_sede_fk IS 'LLave foranea de la Sede.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_direccion_fk IS 'LLave foranea de la Dirección.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_sedes.tis_version IS 'Versión del registro';

ALTER TABLE centros_educativos.sg_inmuebles_sedes ALTER tis_pk SET DEFAULT nextval('centros_educativos.sg_inmuebles_sedes_pk_seq');

CREATE TABLE centros_educativos.sg_inmuebles_sedes_aud (
	tis_pk int8 NOT NULL,
        tis_codigo varchar(10) NOT NULL,
	tis_propietario bool NULL,
	tis_area_total numeric(10,2) NULL,
	tis_area_disponible numeric(10,2) NULL, 
	tis_observaciones varchar(1000) NULL, 
	tis_terreno_principal bool NULL, 
	tis_sede_fk int8 NULL,
        tis_direccion_fk int8 NULL,
	tis_ult_mod_fecha timestamp NULL, 
	tis_ult_mod_usuario varchar(45) NULL, 
	tis_version int2 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_inmuebles_sedes_aud_pkey PRIMARY KEY (tis_pk, rev)
);

CREATE SEQUENCE centros_educativos.sg_inmuebles_vulnerabilidades_pk_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;



--VULNERABILIDADES DE INMUEBLES
CREATE TABLE centros_educativos.sg_inmuebles_vulnerabilidades (
	ivu_pk bigserial NOT NULL, -- Número correlativo autogenerado.
	ivu_inmuebles_sedes_fk int8 NULL, -- LLave foranea de Inlueble Sedes.
	ivu_vulnerabilidad_fk int8 NULL, -- LLave Foranea de Vulnerabildiades.
	ivu_ult_mod_fecha timestamp NULL, -- Última fecha en la que se modificó el registro.
	ivu_ult_mod_usuario varchar(45) NULL, -- Último usuario que modificó el registro.
	ivu_version int2 NULL, -- Versión del registro
	CONSTRAINT sg_inmuebles_vulnerabilidades_pk PRIMARY KEY (ivu_pk),
	CONSTRAINT inmuebles_sedes_fk FOREIGN KEY (ivu_inmuebles_sedes_fk) REFERENCES centros_educativos.sg_inmuebles_sedes(tis_pk),
	CONSTRAINT vulnerabilidad_fk FOREIGN KEY (ivu_vulnerabilidad_fk) references catalogo.sg_vulnerabilidades(vul_pk)
);

-- Column comments
COMMENT ON TABLE centros_educativos.sg_inmuebles_vulnerabilidades IS 'Tabla para el registro de vulnerabilidades.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_inmuebles_sedes_fk IS 'Llave foránea de Inmueble sedes.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_vulnerabilidad_fk IS 'Llave foránea de vulnerabildiades.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_inmuebles_vulnerabilidades.ivu_version IS 'Versión del registro';


ALTER TABLE centros_educativos.sg_inmuebles_vulnerabilidades ALTER ivu_pk SET DEFAULT nextval('centros_educativos.sg_inmuebles_vulnerabilidades_pk_seq');

CREATE TABLE centros_educativos.sg_inmuebles_vulnerabilidades_aud (
	ivu_pk int8 NOT NULL,
	ivu_inmuebles_sedes_fk int8 NULL,
	ivu_vulnerabilidad_fk int8 NULL,
	ivu_ult_mod_fecha timestamp NULL,
	ivu_ult_mod_usuario varchar(45) NULL,
	ivu_version int2 NULL,
	rev int8 NOT NULL,
    revtype int2 NULL,
    CONSTRAINT sg_inmuebles_vulnerabilidades_aud_pkey PRIMARY KEY (ivu_pk, rev)
);

ALTER TABLE centros_educativos.sg_calificaciones_estudiante add column cae_proceso_de_creacion character varying(20);
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud add column cae_proceso_de_creacion character varying(20);

ALTER TABLE centros_educativos.sg_datos_contratacion ADD COLUMN dco_actividades_docentes boolean;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_actividades_docentes boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NOTIFICACION','EB67',  '', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD CONSTRAINT cae_estudiante_fk_calificacion_fk UNIQUE (cae_estudiante_fk,cae_calificacion_fk);
CREATE INDEX sg_estudiantes_habilitado_idx ON centros_educativos.sg_estudiantes USING btree (est_habilitado);

--3.14.1

update centros_educativos.sg_rango_fecha set rfe_habilitado =true;

--3.15.2
CREATE INDEX sg_calificaciones_tipo_periodo_idx   ON centros_educativos.sg_calificaciones (cal_tipo_periodo_calificacion ASC NULLS LAST);
CREATE INDEX sg_calificaciones_tipo_calendario_idx   ON centros_educativos.sg_calificaciones (cal_tipo_calendario_escolar ASC NULLS LAST);

DELETE FROM centros_educativos.sg_calificaciones_estudiante where cae_calificacion_conceptual_fk is null and cae_calificacion is null;

DO $$
declare
personal_sin_dato_empleado centros_educativos.sg_personal_sede_educativa%ROWTYPE;
contador int;
BEGIN



FOR personal_sin_dato_empleado IN (
	select * from centros_educativos.sg_personal_sede_educativa pp where (select count(*) from centros_educativos.sg_datos_empleado d where dem_personal_sede_fk = pp.pse_pk) = 0
	)
loop

insert into centros_educativos.sg_datos_empleado (dem_personal_sede_fk, dem_ult_mod_fecha, dem_ult_mod_usuario,dem_version) values (personal_sin_dato_empleado.pse_pk, now(),'ADMIN_CDE',0);

RAISE NOTICE '----------- PER_PK (%)----',personal_sin_dato_empleado.pse_pk;


END LOOP;

RAISE NOTICE '----------- contador (%)----',contador;

END$$;

-- 3.15.3

ALTER TABLE centros_educativos.sg_personal_sede_educativa ADD COLUMN pse_dato_empleado_fk bigint;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud ADD COLUMN pse_dato_empleado_fk bigint;

update centros_educativos.sg_personal_sede_educativa set pse_dato_empleado_fk =
(select d.dem_pk from centros_educativos.sg_datos_empleado d where d.dem_personal_sede_fk = pse_pk);

ALTER TABLE centros_educativos.sg_personal_sede_educativa ADD CONSTRAINT sg_personal_dato_empleado_fkey FOREIGN KEY (pse_dato_empleado_fk) REFERENCES centros_educativos.sg_datos_empleado(dem_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- 3.16.3

CREATE INDEX sg_horarios_escolares_docente_fk_idx ON centros_educativos.sg_horarios_escolares (hes_docente_fk ASC NULLS LAST);
CREATE INDEX sg_componentes_docente_personal_fk_idx ON centros_educativos.sg_componentes_docentes (cdo_docente_fk ASC NULLS LAST);
CREATE INDEX sg_componentes_docente_horario_fk_idx ON centros_educativos.sg_componentes_docentes (cdo_horario_escolar_fk ASC NULLS LAST);
ALTER TABLE centros_educativos.sg_componentes_docentes ADD CONSTRAINT sg_componentes_docente_personal_fkey FOREIGN KEY (cdo_docente_fk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_componentes_docentes ADD CONSTRAINT sg_componentes_docente_horario_fkey FOREIGN KEY (cdo_horario_escolar_fk) REFERENCES centros_educativos.sg_horarios_escolares(hes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_componentes_docentes ADD CONSTRAINT sg_componentes_docente_componente_fkey FOREIGN KEY (cdo_componente_fk) REFERENCES centros_educativos.sg_componente_plan_estudio(cpe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
update centros_educativos.sg_datos_contratacion set dco_actividades_docentes = false where dco_actividades_docentes is null;

update centros_educativos.sg_personal_sede_educativa set  pse_dato_empleado_fk = (Select dem_pk from centros_educativos.sg_datos_empleado where dem_personal_sede_fk = pse_pk);

-- 3.17.0

ALTER TABLE centros_educativos.sg_periodos_calificacion ADD COLUMN pca_sub_modalidad_educativa_fk bigint;
ALTER TABLE centros_educativos.sg_periodos_calificacion ADD CONSTRAINT sg_periodo_calificacion_sub_modalidad_fkey FOREIGN KEY (pca_sub_modalidad_educativa_fk) REFERENCES catalogo.sg_sub_modalidades(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud ADD COLUMN pca_sub_modalidad_educativa_fk bigint;


-- 3.17.2

ALTER TABLE centros_educativos.sg_asistencias ADD CONSTRAINT sg_asistencias_estudiante_cabezal UNIQUE (asi_control_fk,asi_estudiante);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CREAR_SERVICIO_EDUCATIVO','EP55',  'Servicio educativo: renderizar el boton para crear un nuevo servicio', 1, true, null, null,0);

alter table centros_educativos.sg_periodos_calendario add column ces_habilitado boolean;
alter table centros_educativos.sg_periodos_calendario_aud add column ces_habilitado boolean;
update centros_educativos.sg_periodos_calendario set ces_habilitado=true;


alter table centros_educativos.sg_periodos_calendario add column ces_sub_modalidad_atencion_fk bigint;
alter table centros_educativos.sg_periodos_calendario_aud add column ces_sub_modalidad_atencion_fk bigint;
ALTER TABLE centros_educativos.sg_periodos_calendario ADD CONSTRAINT sg_periodo_calendario_sub_modalidad_fkey FOREIGN KEY (ces_sub_modalidad_atencion_fk) REFERENCES catalogo.sg_sub_modalidades(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


CREATE INDEX sg_personal_dato_empleado_idx ON centros_educativos.sg_personal_sede_educativa (pse_dato_empleado_fk ASC NULLS LAST);

-- 3.17.5

DROP TABLE centros_educativos.sg_movimientos;
DROP TABLE centros_educativos.sg_movimientos_aud;

CREATE INDEX sg_personas_nip_idx ON centros_educativos.sg_personas (per_nip ASC NULLS LAST); 

-- 3.17.6

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) VALUES('TAMANIO_ARCHIVO_IMPORT', 'Tamaño permitido de archivo de importación en bytes', 'Tamaño permitido de archivo en bytes', 0, '1048576', false);
INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) VALUES('TIPO_ARCHIVO_IMPORT', 'Tipo permitido de archivo de importación', 'Tamaño permitido de archivo en bytes', 0, '/(\.|\/)(xlsx)$/', false);

-- 3.18.0

ALTER TABLE centros_educativos.sg_estudios_realizados ADD CONSTRAINT sg_estudios_realizados_personal_pk_uk_idx UNIQUE (ere_personal_sede_fk);

CREATE INDEX sg_matriculas_fecha_ingreso_idx
    ON centros_educativos.sg_matriculas USING btree
    (mat_fecha_ingreso)
    TABLESPACE pg_default;
	
CREATE INDEX sg_matriculas_motivo_retiro_idx
    ON centros_educativos.sg_matriculas USING btree
    (mat_motivo_retiro_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_servicio_educativo_grado_idx
    ON centros_educativos.sg_servicio_educativo USING btree
    (sdu_grado_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_servicio_educativo_opcion_idx
    ON centros_educativos.sg_servicio_educativo USING btree
    (sdu_opcion_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_servicio_educativo_programa_idx
    ON centros_educativos.sg_servicio_educativo USING btree
    (sdu_programa_educativo_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_servicio_estado_idx
    ON centros_educativos.sg_servicio_educativo USING btree
    (sdu_estado)
    TABLESPACE pg_default;	
	
CREATE INDEX sg_rel_grado_plan_estudio_grado_idx
    ON centros_educativos.sg_rel_grado_plan_estudio USING btree
    (rgp_grado_fk)
    TABLESPACE pg_default;	
	
CREATE INDEX sg_rel_grado_plan_estudio_plan_estudio_idx
    ON centros_educativos.sg_rel_grado_plan_estudio USING btree
    (rgp_plan_estudio_fk)
    TABLESPACE pg_default;	
	
CREATE INDEX sg_modalidades_codigo__idx
    ON centros_educativos.sg_modalidades USING btree
    (mod_codigo)
    TABLESPACE pg_default;	
	
CREATE INDEX sg_modalidades_orden_idx
    ON centros_educativos.sg_modalidades USING btree
    (mod_orden)
    TABLESPACE pg_default;
	
CREATE INDEX sg_rel_mod_ed_mod_aten_sub_modalidad_idx
    ON centros_educativos.sg_rel_mod_ed_mod_aten USING btree
    (rea_sub_modalidad_atencion_fk)
    TABLESPACE pg_default;
	
	
CREATE INDEX sg_rel_opcion_prog_ed_opcion_idx
    ON centros_educativos.sg_rel_opcion_prog_ed USING btree
    (roe_opcion_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_rel_opcion_prog_ed_programa_educativo_idx
    ON centros_educativos.sg_rel_opcion_prog_ed USING btree
    (roe_programa_educativo_fk)
    TABLESPACE pg_default;	
	
CREATE INDEX sg_planes_estudio_opcion_idx
    ON centros_educativos.sg_planes_estudio USING btree
    (pes_opcion_fk)
    TABLESPACE pg_default;	

CREATE INDEX sg_planes_estudio_programa_educativo_idx
    ON centros_educativos.sg_planes_estudio USING btree
    (pes_programa_educativo_fk)
    TABLESPACE pg_default;
	
CREATE INDEX sg_planes_estudio_relacion_modalidad_idx
    ON centros_educativos.sg_planes_estudio USING btree
    (pes_relacion_modalidad_fk)
    TABLESPACE pg_default;


ALTER TABLE centros_educativos.sg_componente_plan_grado     ADD COLUMN cpg_calificacion_ingresada_ce boolean;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_calificacion_ingresada_ce boolean;
UPDATE centros_educativos.sg_componente_plan_grado set cpg_calificacion_ingresada_ce = true;

ALTER TABLE centros_educativos.sg_sellos_firmas     DROP COLUMN sfi_sello_archivo_fk;
ALTER TABLE centros_educativos.sg_sellos_firmas_aud DROP COLUMN sfi_sello_archivo_fk;

ALTER TABLE centros_educativos.sg_sellos_firmas     RENAME COLUMN sfi_firma_archivo_fk TO sfi_firma_sello_archivo_fk;
ALTER TABLE centros_educativos.sg_sellos_firmas_aud RENAME COLUMN sfi_firma_archivo_fk TO sfi_firma_sello_archivo_fk;

ALTER TABLE centros_educativos.sg_datos_empleado ADD COLUMN dem_empleado_mineducyt boolean;
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD COLUMN dem_empleado_mineducyt boolean;
COMMENT ON COLUMN centros_educativos.sg_datos_empleado.dem_empleado_mineducyt IS 'Indica si el registro es empleado MINEDUCYT.';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_INDICAR_SI_ES_EMPLEADO','EP56',  'Indica si es empleado MINEDUCYT', 1, true, null, null,0);

update centros_educativos.sg_datos_empleado p
set dem_empleado_mineducyt = false;

update centros_educativos.sg_datos_empleado de
set dem_empleado_mineducyt = true where exists (select 1 from centros_educativos.sg_datos_contratacion dc where dc.dco_modelo_contrato = 'ACUERDO' and dc.dco_dato_empleado_fk = de.dem_pk);


-- 3.18.1
CREATE INDEX sg_personal_sede_educativa_tipo
    ON centros_educativos.sg_personal_sede_educativa USING btree
    (pse_tipo);

CREATE INDEX sg_modalidades_ciclo_idx
    ON centros_educativos.sg_modalidades USING btree
    (mod_ciclo);

CREATE INDEX sg_personas_nie_null ON centros_educativos.sg_personas (per_nie) WHERE per_nie IS NULL;


CREATE INDEX sg_reporte_dir_sede_idx
    ON centros_educativos.sg_reporte_director USING btree
    (rdi_sede_fk);

CREATE INDEX sg_reporte_dir_datos_sede_idx
    ON centros_educativos.sg_reporte_director USING btree
    (rdi_datos_sede);

CREATE INDEX sg_reporte_dir_datos_personal_idx
    ON centros_educativos.sg_reporte_director USING btree
    (rdi_datos_personal);

CREATE INDEX sg_reporte_dir_datos_estudiante_idx
    ON centros_educativos.sg_reporte_director USING btree
    (rdi_datos_estudiante);

DELETE FROM seguridad.sg_usuario_rol where pur_usuario_fk = 1 and pur_rol_fk != 1;

-- 3.18.3

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) VALUES('CANT_FILAS_ARCHIVO_IMPORT', 'Cantidad de filas archivo de importación', 'cantidad de filas archivo de importacion', 0, '500', false);

-- 3.18.7


CREATE INDEX sg_componentes_docente_componente_fk_idx
ON centros_educativos.sg_componentes_docentes USING btree
(cdo_componente_fk)
TABLESPACE pg_default;
	
CREATE INDEX sg_componente_plan_grado_escala_calificacion_fk_idx
ON centros_educativos.sg_componente_plan_grado USING btree
(cpg_escala_calificacion_fk)
TABLESPACE pg_default;	



-- 3.19.0

DROP INDEX centros_educativos.sg_rel_mod_ed_mod_aten_ate_fk_idx;
CREATE INDEX sg_rel_mod_ed_mod_aten_ate_fk_idx   ON centros_educativos.sg_rel_mod_ed_mod_aten (rea_modalidad_atencion_fk ASC NULLS LAST);

INSERT INTO catalogo.sg_configuraciones("con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version") VALUES ('ACERCA_DE', 'Acerca del SIGES', '[Acerca de]', 'acerca del siges', CURRENT_TIMESTAMP, 'admin', '0');

-- 3.21.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_COMPONENTE_DOCENTE','EB75',  'Componente docente: buscar', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_componentes_docentes ADD CONSTRAINT componente_horario_docente_uk UNIQUE (cdo_horario_escolar_fk,cdo_componente_fk);

-- 3.22.0

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ALTER COLUMN red_operacion type CHARACTER VARYING(20);
ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle_aud ALTER COLUMN red_operacion type CHARACTER VARYING(20);



--Agregar opción y programa educativo
ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ADD COLUMN red_opcion_fk bigint;
ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ADD COLUMN red_programa_educativo_fk bigint;

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle_aud ADD COLUMN red_opcion_fk bigint;
ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle_aud ADD COLUMN red_programa_educativo_fk bigint;

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ADD CONSTRAINT 
sg_regla_equivalencia_detalle_opcion_fkey FOREIGN KEY (red_opcion_fk) 
REFERENCES centros_educativos.sg_opciones(opc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ADD CONSTRAINT 
sg_regla_equivalencia_detalle_programa_fkey FOREIGN KEY (red_programa_educativo_fk) 
REFERENCES  catalogo.sg_programas_educativos(ped_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

-- 3.22.1


ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio  ADD COLUMN rgp_considerar_orden boolean;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rgp_considerar_orden boolean;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_orden integer;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_orden integer;


ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle DROP CONSTRAINT sg_regla_equivalencia_detalle_gra_plan_ope_uk;

-- 3.23.0

ALTER TABLE centros_educativos.sg_grados add column gra_aplica_estadisticas boolean;
ALTER TABLE centros_educativos.sg_grados_aud add column gra_aplica_estadisticas boolean;

ALTER TABLE centros_educativos.sg_estudios_realizados add column ere_maximo_nivel_educativo_alcanzado_fk bigint;
ALTER TABLE centros_educativos.sg_estudios_realizados_aud add column ere_maximo_nivel_educativo_alcanzado_fk bigint;
ALTER TABLE centros_educativos.sg_estudios_realizados ADD CONSTRAINT sg_estudios_realizados_maximo_nivel_educativo_fkey FOREIGN KEY (ere_maximo_nivel_educativo_alcanzado_fk) REFERENCES catalogo.sg_maximo_nivel_educativo_alcanzado(mne_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE INDEX sg_datos_contratacion_fecha_desde_idx ON centros_educativos.sg_datos_contratacion USING btree(dco_desde);
CREATE INDEX sg_datos_contratacion_fecha_hasta_idx ON centros_educativos.sg_datos_contratacion USING btree(dco_hasta);

ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_estado character varying(45);
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_fecha_fallecimiento date;
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_motivo_fallecimiento_fk bigint;

ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_estado character varying(45);
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_fecha_fallecimiento date;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_motivo_fallecimiento_fk bigint;

ALTER TABLE centros_educativos.sg_personas 
    ADD CONSTRAINT sg_personas_motivo_fallec_fkey FOREIGN KEY (per_motivo_fallecimiento_fk) 
    REFERENCES catalogo.sg_motivos_fallecimiento(mfa_pk) 
    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_ESTADO_PERSONA','EP57',  'Se puede editar el estado de la persona', 1, true, null, null,0);
                    
--Para búsquedas de personas
CREATE INDEX sg_personas_per_isss_idx ON centros_educativos.sg_personas USING btree(per_isss);     
CREATE INDEX sg_personas_per_nit_idx ON centros_educativos.sg_personas USING btree(per_nit);     

--Para búsquedas de docentes
CREATE INDEX sg_datos_contratacion_modelo_contrato_idx ON centros_educativos.sg_datos_contratacion USING btree(dco_modelo_contrato);     

UPDATE centros_educativos.sg_personas SET per_estado = 'VIVE';

ALTER TABLE centros_educativos.sg_regla_equivalencia_detalle ADD CONSTRAINT sg_regla_equivalencia_detalle_gra_plan_ope_uk  UNIQUE (red_regla_equivalencia_fk,red_grado_fk,red_plan_estudio_fk,red_operacion);

ALTER TABLE centros_educativos.sg_personal_sede_educativa ADD COLUMN pse_fecha_registro date;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud ADD COLUMN pse_fecha_registro date;

-- 3.23.1

ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_nip TYPE VARCHAR(30);
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_nip TYPE VARCHAR(30);

DO $$
declare


personaNIP centros_educativos.sg_personas%ROWTYPE;
rev_aux bigint;


cant_inscond_act integer;
cant_relgrualu_act integer;

vApp character(50);
vUsu varchar(15);
vAno smallint;
vFchMod date;
varNip varchar(100);

BEGIN


vAPP:='script_arreglo_dui';
vUsu:='ADMIN_MIGRA2';
vFchMod:= now();




FOR personaNIP IN(

SELECT
*
FROM centros_educativos.sg_personas
WHERE  per_nip is NOT null and length(per_nip)<=6

) loop

RAISE NOTICE '----------- PROCESANDO PERSONA ( % )-----',personaNIP;
RAISE notice '----------- NIP TRANSFORMADO % -----', personaNIP.per_NIP;

-- TRANSAFORMACION DUI
varNip:= lpad(personaNIP.per_nip::text, 7, '0');
RAISE NOTICE '----------- NIP TRANSFORMADO % -----',varNip;

UPDATE centros_educativos.sg_personas set per_nip=varNip
WHERE per_pk=personaNIP.per_pk;




END LOOP;


END$$; 


ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_activo boolean;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_activo boolean;


UPDATE centros_educativos.sg_sedes SET sed_activo = sed_habilitado;




--3.23.5


ALTER TABLE centros_educativos.sg_componente_plan_grado     ADD COLUMN cpg_codigo_externo integer;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_codigo_externo integer;

-- Archivo de calificación PAES
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_archivo_calificacion_paes_acp_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_archivo_calificacion_paes(
    acp_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_archivo_calificacion_paes_acp_pk_seq'::regclass), 
    acp_archivo_fk bigint not null, 
    acp_rango_fecha_fk bigint not null, 
    acp_estado_archivo character varying(45), 
    acp_resultado character varying(5000), 
    acp_ult_mod_fecha timestamp without TIME zone, 
    acp_ult_mod_usuario CHARACTER varying(45), 
    acp_version INTEGER, 
    CONSTRAINT sg_archivo_calificacion_paes_pkey         PRIMARY KEY (acp_pk), 
    CONSTRAINT sg_archivo_calificacion_paes_archivo_fk  FOREIGN KEY (acp_archivo_fk)      
        REFERENCES centros_educativos.sg_archivos(ach_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, 
    CONSTRAINT sg_archivo_calificacion_paes_rango_fk    FOREIGN KEY (acp_rango_fecha_fk)   
        REFERENCES centros_educativos.sg_rango_fecha(rfe_pk)    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE  centros_educativos.sg_archivo_calificacion_paes                      IS 'Tabla para almacenar los archivos a cargar de la PAES.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_pk               IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_archivo_fk       IS 'Llave foranea que hace referencia al archivo.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_rango_fecha_fk   IS 'Llave foranea que hace referencia al rango de fechas del periodo.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_estado_archivo   IS 'Estado del registro.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_archivo_calificacion_paes.acp_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivo_calificacion_paes_aud (
    acp_pk bigint NOT NULL, 
    acp_archivo_fk bigint not null, 
    acp_rango_fecha_fk bigint not null, 
    acp_estado_archivo character varying(45), 
    acp_resultado character varying(5000), 
    acp_ult_mod_fecha timestamp without TIME zone, 
    acp_ult_mod_usuario CHARACTER varying(45), 
    acp_version INTEGER,  
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_archivo_calificacion_paes_aud_pkey PRIMARY KEY (acp_pk, rev), 
    CONSTRAINT sg_archivo_calificacion_paes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_PAES','EM57',  'MENU: opción calificación paes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_PROCESAR_ARCHIVO_PAES','EP58',  'Para renderizar el boton para hacer pruebas de procesar el archivo PAES', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ARCHIVO_CALIFICACION_PAES','E347',  'Calificaciones: crear un archivo de calificacion PAES', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ARCHIVO_CALIFICACION_PAES','E348',  'Calificaciones: editar un archivo de calificacion PAES', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ARCHIVO_CALIFICACION_PAES','E349',  'Calificaciones: eliminar un archivo de calificacion PAES', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_personal_sede_educativa DROP COLUMN pse_fecha_registro;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud DROP COLUMN pse_fecha_registro;


ALTER TABLE centros_educativos.sg_datos_empleado ADD COLUMN dem_fecha_registro date;
ALTER TABLE centros_educativos.sg_datos_empleado_aud ADD COLUMN dem_fecha_registro date;


--3.23.6
--MODIFICACIONES PARA QUE HAYA UNA ÚNICA ENTIDAD DE ARCHIVO
alter table centros_educativos.sg_archivos set schema public;
alter table centros_educativos.sg_archivos_aud set schema public;
alter SEQUENCE centros_educativos.sg_archivos_ach_pk_seq set schema public;

update centros_educativos.sg_personas set per_foto_fk=null where per_foto_fk <=4;
delete from sg_archivos where ach_pk<=4;

update catalogo.sg_plantilla set pla_archivo_fk =null;
alter table catalogo.sg_plantilla drop constraint sg_pla_archivo_fkey;
alter table catalogo.sg_plantilla add constraint sg_pla_archivo_fkey FOREIGN KEY (pla_archivo_fk) REFERENCES sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE cascade;
drop table catalogo.sg_archivo;
drop table catalogo.sg_archivo_aud;


--3.24.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SEDES_INCLUIR_ADSCRITAS','EB76',  'Al buscar sedes, permite retornar las adscriptas.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_TRASLADOS','EP60',  'Puede ver alertas referentes a traslados.', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_periodos_calificacion ADD COLUMN pca_permite_calificar_sin_nie boolean;
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud ADD COLUMN pca_permite_calificar_sin_nie boolean;
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_permite_calificar_sin_nie IS 'Indica si se permite calificar estudiantes sin NIE en este período.';

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rgp_permite_calificar_sin_mat_validada boolean;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rgp_permite_calificar_sin_mat_validada boolean;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_permite_calificar_sin_mat_validada IS 'Indica si se permite calificar estudiantes sin matrícula validada en este Grado - Plan Estudio.';

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rgp_permite_calificar_con_mat_provisional boolean;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rgp_permite_calificar_con_mat_provisional boolean;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_permite_calificar_con_mat_provisional IS 'Indica si se permite calificar estudiantes con matrícula provisional en este Grado - Plan Estudio.';

UPDATE centros_educativos.sg_periodos_calificacion set pca_permite_calificar_sin_nie = true;
UPDATE centros_educativos.sg_periodos_calificacion_aud set pca_permite_calificar_sin_nie = true;
UPDATE centros_educativos.sg_rel_grado_plan_estudio set rgp_permite_calificar_sin_mat_validada = true;
UPDATE centros_educativos.sg_rel_grado_plan_estudio_aud set rgp_permite_calificar_sin_mat_validada = true;
UPDATE centros_educativos.sg_rel_grado_plan_estudio set rgp_permite_calificar_con_mat_provisional = true;
UPDATE centros_educativos.sg_rel_grado_plan_estudio_aud set rgp_permite_calificar_con_mat_provisional = true;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('COPIAR_SERVICIOS_SEDE_PADRE','E350',  'Para renderizar el boton para copiar los servicios educativos de la sede padre, cuando es adscrita', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_HALLAR_ESTUDIANTES','EM58',  'MENU: opción para la busqueda de estudiantes', 1, true, null, null,0);

-- 3.26.0

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio  ADD COLUMN rgp_permite_validar_matricula_sin_nie boolean;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rgp_permite_validar_matricula_sin_nie boolean;

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio  ADD COLUMN rgp_requiere_validacion_academica boolean;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rgp_requiere_validacion_academica boolean;

UPDATE centros_educativos.sg_rel_grado_plan_estudio SET rgp_permite_validar_matricula_sin_nie = true;
UPDATE centros_educativos.sg_rel_grado_plan_estudio SET rgp_requiere_validacion_academica = true;


INSERT INTO seguridad.sg_categorias_operacion values (6,'est', 'Estadística','Estadística',true,null,null,0);
INSERT INTO seguridad.sg_categorias_operacion values (7,'inf', 'Infraestructura','Infraestructura',true,null,null,0);
INSERT INTO seguridad.sg_categorias_operacion values (8,'reh', 'Registro Histórico','Registro Histórico',true,null,null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPORTE_SEDES_ASISTENCIAS','EM59', 'MENU: opción para generar reportes asistencias mensuales por sección para sede', 1, true, null, null,0);

-- 3.26.2

ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_permite_calcular_nota_institucional boolean;
ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_permite_calcular_nota_aprobacion boolean;

ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_permite_calcular_nota_institucional boolean;
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_permite_calcular_nota_aprobacion boolean;
UPDATE seguridad.sg_operaciones set ope_codigo = 'EP59' where ope_codigo = 'EP60';
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_CALCULO_NOTAS_CALENDARIO','EP60',  'Permite la edición de los permisos de calculo de notas en calendarios', 1, true, null, null,0);

UPDATE centros_educativos.sg_calendarios SET cal_permite_calcular_nota_institucional = true, cal_permite_calcular_nota_aprobacion = true;

-- 3.26.5

CREATE INDEX sg_personas_cant_hijos_idx ON centros_educativos.sg_personas USING btree(per_cantidad_hijos);   
CREATE INDEX sg_personas_embarazo_idx ON centros_educativos.sg_personas USING btree(per_embarazo);  
CREATE INDEX sg_personas_estado_civil_idx ON centros_educativos.sg_personas USING btree(per_estado_civil_fk);  

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ALERTA','EB79',  'Alertas: buscar', 1, true, null, null,0);

-- Acuerdos de sedes
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_acuerdos_sedes_ase_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_acuerdos_sedes(
    ase_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_acuerdos_sedes_ase_pk_seq'::regclass), 
    ase_sede_fk bigint not null, 
    ase_tipo_acuerdo_fk bigint not null, 
    ase_id_resolucion integer, 
    ase_numero_acuerdo character varying(10), 
    ase_fecha_registro date, 
    ase_nombre_responsable  character varying(100), 
    ase_fecha_generacion date, 
    ase_observacion  character varying(4000), 
    ase_numero_solicitud  character varying(10), 
    ase_codigo_nominacion  character varying(10), 
    ase_ult_mod_fecha timestamp without TIME zone, 
    ase_ult_mod_usuario CHARACTER varying(45), 
    ase_version INTEGER, 
    CONSTRAINT sg_acuerdos_sedes_pkey         PRIMARY KEY (ase_pk), 
    CONSTRAINT sg_acuerdos_sedes_sede_fk  FOREIGN KEY (ase_sede_fk)      
        REFERENCES centros_educativos.sg_sedes(sed_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, 
    CONSTRAINT sg_acuerdos_sedes_tipo_acuerdo_fk    FOREIGN KEY (ase_tipo_acuerdo_fk)   
        REFERENCES catalogo.sg_tipos_acuerdos(tao_pk)    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE  centros_educativos.sg_acuerdos_sedes                      IS 'Tabla para almacenar los acuerdos de las sedes.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_pk               IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_sede_fk       IS 'Llave foranea que hace referencia a la sede.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_tipo_acuerdo_fk   IS 'Llave foranea que hace referencia al tipo de acuerdo.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_id_resolucion   IS 'Id de la resolución.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_numero_acuerdo   IS 'Número del acuerdo.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_fecha_registro   IS 'Fecha del registro.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_nombre_responsable   IS 'Nombre del responsable del acuerdo.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_observacion   IS 'Observaciones.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_numero_solicitud   IS 'Número de la solicitud.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_codigo_nominacion   IS 'Código de nominación.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_acuerdos_sedes.ase_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_acuerdos_sedes_aud (
    ase_pk bigint NOT NULL, 
    ase_sede_fk bigint not null, 
    ase_tipo_acuerdo_fk bigint not null, 
    ase_id_resolucion integer, 
    ase_numero_acuerdo character varying(10), 
    ase_fecha_registro date, 
    ase_nombre_responsable  character varying(100), 
    ase_fecha_generacion date, 
    ase_observacion  character varying(4000), 
    ase_numero_solicitud  character varying(10), 
    ase_codigo_nominacion  character varying(10), 
    ase_ult_mod_fecha timestamp without TIME zone, 
    ase_ult_mod_usuario CHARACTER varying(45), 
    ase_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_acuerdos_sedes_aud_pkey PRIMARY KEY (ase_pk, rev), 
    CONSTRAINT sg_acuerdos_sedes_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ACUERDO_SEDE','EB78',  'Solapa sede: buscar acuerdos de la sede', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACUERDO_SEDE','E353',  'Solapa sede: crear un acuerdo de sedes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACUERDO_SEDE','E354',  'Solapa sede: actualizar acuerdo de sedes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACUERDO_SEDE','E355',  'Solapa sede: eliminar acuerdo de sedes', 1, true, null, null,0);

-- 3.27.0

UPDATE seguridad.sg_operaciones set ope_nombre = 'BUSCAR_ALERTA_TEMPRANA' where ope_nombre ='BUSCAR_ALERTA';
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ALERTAS_TEMPRANAS','EM60',  'MENU: opción para ver alertas tempranas', 1, true, null, null,0);


-- 3.27.3

CREATE INDEX IF NOT EXISTS sg_rel_grado_plan_estudio_req_val_acade_idx ON centros_educativos.sg_rel_grado_plan_estudio USING btree (rgp_requiere_validacion_academica) ;
CREATE INDEX IF NOT EXISTS sg_matriculas_mat_anulada_idx ON centros_educativos.sg_matriculas USING btree (mat_anulada) ;
CREATE INDEX IF NOT EXISTS sg_matriculas_mat_valid_academ_idx ON centros_educativos.sg_matriculas USING btree (mat_validacion_academica) ;

CREATE INDEX IF NOT EXISTS sg_sedes_sed_adscrita_idx ON centros_educativos.sg_sedes USING btree (sed_centro_adscrito) ;

-- 3.28.3

update centros_educativos.sg_componente_plan_grado set cpg_calificacion_ingresada_ce=true;


--Importar historial bit matricula
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_archivos_bit_matriculas_abm_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_archivos_bit_matriculas(
    abm_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_archivos_bit_matriculas_abm_pk_seq'::regclass), 
    abm_sede_fk bigint not null, 
    abm_dia_ingreso integer not null, 
    abm_mes_ingreso integer not null, 
    abm_dia_egreso integer, 
    abm_mes_egreso integer, 
    abm_archivo_fk bigint not null, 
    abm_estado CHARACTER VARYING(25), 
    abm_descripcion CHARACTER VARYING(1000), 
    abm_ult_mod_fecha timestamp without TIME zone, 
    abm_ult_mod_usuario CHARACTER varying(45), 
    abm_version INTEGER, 
    CONSTRAINT sg_archivos_bit_matriculas_pkey         PRIMARY KEY (abm_pk), 
    CONSTRAINT sg_archivos_bit_matriculas_sede_fk  FOREIGN KEY (abm_sede_fk)      
        REFERENCES centros_educativos.sg_sedes(sed_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, 
    CONSTRAINT sg_archivos_bit_matriculas_archivo_fk    FOREIGN KEY (abm_archivo_fk)   
        REFERENCES public.sg_archivos(ach_pk)    MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE  centros_educativos.sg_archivos_bit_matriculas                      IS 'Tabla para almacenar los acuerdos de las sedes.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_pk               IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_sede_fk          IS 'Llave foranea que hace referencia a la sede.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_dia_ingreso      IS 'Día de ingreso.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_mes_ingreso      IS 'Mes de ingreso.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_dia_egreso       IS 'Día de egreso.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_mes_egreso       IS 'Mes de egreso.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_archivo_fk       IS 'Llave foranea que hace referencia al archivo de carga.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_estado           IS 'Estado del archivo.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_descripcion           IS 'Descripción del registro.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_archivos_bit_matriculas.abm_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_archivos_bit_matriculas_aud (
    abm_pk bigint NOT NULL, 
    abm_sede_fk bigint not null, 
    abm_dia_ingreso integer not null, 
    abm_mes_ingreso integer not null, 
    abm_dia_egreso integer, 
    abm_mes_egreso integer, 
    abm_archivo_fk bigint not null, 
    abm_estado CHARACTER VARYING(25), 
    abm_descripcion CHARACTER VARYING(1000),
    abm_ult_mod_fecha timestamp without TIME zone, 
    abm_ult_mod_usuario CHARACTER varying(45), 
    abm_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_archivos_bit_matriculas_aud_pkey PRIMARY KEY (abm_pk, rev), 
    CONSTRAINT sg_archivos_bit_matriculas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ARCHIVO_BIT_MATRICULA','E357',  'Crear un archivo para la importación del historial de matriculas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ARCHIVO_BIT_MATRICULA','E358',  'Actualizar un archivo para la importación del historial de matriculas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ARCHIVO_BIT_MATRICULA','E359',  'Eliminar un archivo para la importación del historial de matriculas', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_IMPORT_BIT_MATRICULA','EM62',  'MENU: opción para importar el historial de las matriculas de estudiantes', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_importado boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_importado boolean;

--3.29.0

ALTER TABLE centros_educativos.sg_sedes ADD COLUMN sed_implementadora_fk bigint;
ALTER TABLE centros_educativos.sg_sedes_aud ADD COLUMN sed_implementadora_fk bigint;

ALTER TABLE centros_educativos.sg_sedes ADD CONSTRAINT sg_sedes_implementadora_fkey FOREIGN KEY (sed_implementadora_fk) 
    REFERENCES catalogo.sg_implementadoras(imp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE INDEX IF NOT EXISTS sg_sedes_sed_adscrita_idx ON centros_educativos.sg_sedes USING btree (sed_centro_adscrito) ;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_formula_habilitacion_PP_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_formula_habilitacion_PP_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_formula_habilitacion_PP_fk bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_componente_plan_grado_formulas_habilitacion_PP_fkey FOREIGN KEY (cpg_formula_habilitacion_PP_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_formula_habilitacion_PS_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_formula_habilitacion_PS_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_formula_habilitacion_PS_fk bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_componente_plan_grado_formulas_habilitacion_PS_fkey FOREIGN KEY (cpg_formula_habilitacion_PS_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_formula_habilitacion_SP_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_formula_habilitacion_SP_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_formula_habilitacion_SP_fk bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_componente_plan_grado_formulas_habilitacion_SP_fkey FOREIGN KEY (cpg_formula_habilitacion_SP_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_formula_habilitacion_SS_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_componente_plan_grado.cpg_formula_habilitacion_SS_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_formula_habilitacion_SS_fk bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_componente_plan_grado_formulas_habilitacion_SS_fkey FOREIGN KEY (cpg_formula_habilitacion_SS_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rpg_formula_habilitacion_PP_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rpg_formula_habilitacion_PP_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rpg_formula_habilitacion_PP_fk bigint;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD CONSTRAINT sg_rel_grado_plan_estudio_formulas_habilitacion_PP_fkey FOREIGN KEY (rpg_formula_habilitacion_PP_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rpg_formula_habilitacion_PS_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rpg_formula_habilitacion_PS_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rpg_formula_habilitacion_PS_fk bigint;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD CONSTRAINT sg_rel_grado_plan_estudio_formulas_habilitacion_PS_fkey FOREIGN KEY (rpg_formula_habilitacion_PS_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rpg_formula_habilitacion_SP_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rpg_formula_habilitacion_SP_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rpg_formula_habilitacion_SP_fk bigint;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD CONSTRAINT sg_rel_grado_plan_estudio_formulas_habilitacion_SP_fkey FOREIGN KEY (rpg_formula_habilitacion_SP_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD COLUMN rpg_formula_habilitacion_SS_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rpg_formula_habilitacion_SS_fk IS 'Clave foránea a la fórmula';
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud ADD COLUMN rpg_formula_habilitacion_SS_fk bigint;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio ADD CONSTRAINT sg_rel_grado_plan_estudio_formulas_habilitacion_SS_fkey FOREIGN KEY (rpg_formula_habilitacion_SS_fk) REFERENCES catalogo.sg_formulas(fom_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_sedes ALTER COLUMN sed_codigo TYPE VARCHAR(15);
ALTER TABLE centros_educativos.sg_sedes_aud ALTER COLUMN sed_codigo TYPE VARCHAR(15);

update centros_educativos.sg_componente_plan_grado set cpg_calificacion_ingresada_ce=true;


update centros_educativos.sg_sedes set sed_clasificacion_fk = null where sed_tipo = 'CIR_ALF';

--3.29.1
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes ADD COLUMN acp_periodo_calificacion_fk bigint;
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes_aud ADD COLUMN acp_periodo_calificacion_fk bigint;

ALTER TABLE centros_educativos.sg_archivo_calificacion_paes ADD CONSTRAINT sg_archivo_calificacion_paes_periodo_fk FOREIGN KEY (acp_periodo_calificacion_fk) REFERENCES  centros_educativos.sg_periodos_calificacion(pca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--3.30.0

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_repetidor boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_repetidor boolean;

ALTER TABLE centros_educativos.sg_experiencia_laboral ADD COLUMN ela_anio_servicio integer;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD COLUMN ela_anio_servicio integer;

ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_parametro_formula_prueba_PP bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_parametro_formula_prueba_PP bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_parametro_formula_prueba_PP FOREIGN KEY (cpg_parametro_formula_prueba_PP) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_parametro_formula_prueba_PS bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_parametro_formula_prueba_PS bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_parametro_formula_prueba_PS FOREIGN KEY (cpg_parametro_formula_prueba_PS) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_parametro_formula_prueba_SP bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_parametro_formula_prueba_SP bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_parametro_formula_prueba_SP FOREIGN KEY (cpg_parametro_formula_prueba_SP) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_parametro_formula_prueba_SS bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_parametro_formula_prueba_SS bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_parametro_formula_prueba_SS FOREIGN KEY (cpg_parametro_formula_prueba_SS) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


--3.30.1
ALTER TABLE centros_educativos.sg_allegados ADD COLUMN all_vive_con_allegado boolean;
ALTER TABLE centros_educativos.sg_allegados_aud ADD COLUMN all_vive_con_allegado boolean;

--3.31.0
--Eliminar las calificaciones de estudiantes cuyos componentes de plan de grado han sido eliminados
delete from centros_educativos.sg_calificaciones_estudiante where cae_calificacion_fk in (
	select
		cal.cal_pk
	from
		centros_educativos.sg_componente_plan_estudio plan 
	left outer join centros_educativos.sg_calificaciones cal on
		cal.cal_componente_plan_estudio_fk = plan.cpe_pk 
	left outer join centros_educativos.sg_componente_plan_grado grado on 
		grado.cpg_componente_plan_estudio_fk = plan.cpe_pk
	where grado.cpg_pk is null and cal.cal_pk is not null
);

--Eliminar las calificaciones cuyos componentes de plan de grado han sido eliminados
delete from centros_educativos.sg_calificaciones where cal_pk in (
	select
		cal.cal_pk
	from
		centros_educativos.sg_componente_plan_estudio plan 
	left outer join centros_educativos.sg_calificaciones cal on
		cal.cal_componente_plan_estudio_fk = plan.cpe_pk 
	left outer join centros_educativos.sg_componente_plan_grado grado on 
		grado.cpg_componente_plan_estudio_fk = plan.cpe_pk
	where grado.cpg_pk is null and cal.cal_pk is not null
);

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_copiada_anio_siguiente boolean;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_copiada_anio_siguiente boolean;
update centros_educativos.sg_secciones set sec_copiada_anio_siguiente = false;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('COPIAR_SECCIONES','E363',  'Permite copiar las secciones de un año al siguiente', 1, true, null, null,0);

--3.31.1 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CAMBIAR_ESTADO_SECCION','EP64',  'Permite cambiar el estado de una sección', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_MATRICULA_SIGUIENTE_ANIO','EM63',  'MENU: opción para crear matrículas de siguiente año estudiantes', 1, true, null, null,0);-- Escolaridad estudiante
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_escolaridad_estudiante_esc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_escolaridad_estudiante (esc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_escolaridad_estudiante_esc_pk_seq'::regclass),esc_estudiante bigint, esc_servicio_educativo bigint, esc_anio bigint, esc_resultado character varying(40),esc_asistencias integer, esc_inasistencias integer,esc_ult_mod_fecha timestamp without time zone, esc_ult_mod_usuario character varying(45), esc_version integer, CONSTRAINT sg_escolaridad_estudiante_pkey PRIMARY KEY (esc_pk),CONSTRAINT sg_escolaridad_estudiante_estudiante_fkey FOREIGN KEY (esc_estudiante) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_escolaridad_estudiante_servicio_educativo_fkey FOREIGN KEY (esc_servicio_educativo) REFERENCES centros_educativos.sg_servicio_educativo(sdu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_escolaridad_estudiante_anio_fkey FOREIGN KEY (esc_anio) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_escolaridad_estudiante IS 'Tabla para el registro de escolaridad estudiante.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_estudiante IS 'Estudiante del registro.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_servicio_educativo IS 'Servicio educativo del registro.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_anio IS 'Anio del registro.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_resultado IS 'Resultado de promoción.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_asistencias IS 'Cantidad asistencia.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_inasistencias IS 'Cantidad inasistencia.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_escolaridad_estudiante_aud (esc_pk bigint NOT NULL,esc_estudiante bigint, esc_servicio_educativo bigint, esc_anio bigint, esc_resultado character varying(40),esc_asistencias integer, esc_inasistencias integer, esc_ult_mod_fecha timestamp without time zone, esc_ult_mod_usuario character varying(45), esc_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD PRIMARY KEY (esc_pk, rev);

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_cerrado BOOLEAN;
COMMENT ON COLUMN centros_educativos.sg_calificaciones.cal_cerrado IS 'Cabezal de calificacion cerrado.';
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_cerrado BOOLEAN;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESCOLARIDAD_ESTUDIANTE','E364',  'Crea escolaridad estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESCOLARIDAD_ESTUDIANTE','E365',  'Actualizar escolaridad estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESCOLARIDAD_ESTUDIANTE','E366',  'Eliminar escolaridad estudiante', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ESCOLARIDAD_ESTUDIANTE','EB81',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CERRAR_ANIO_PROMOCION','EP62',  'Habilita botón de "cierre año" en promociones', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_grados ADD COLUMN gra_grado_siguiente bigint;
ALTER TABLE centros_educativos.sg_grados_aud ADD COLUMN gra_grado_siguiente bigint;
ALTER TABLE centros_educativos.sg_grados ADD CONSTRAINT sg_gra_grado_siguiente FOREIGN KEY (gra_grado_siguiente) REFERENCES centros_educativos.sg_grados(gra_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONSULTA_SECCION_CIERRE_ANIO','EM64',  'MENU: opción para consulta cierre anio', 1, true, null, null,0);


-- 3.32.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_REL_GRADO_PRECEDENCIA','E367',  'Permite modificar la precedencia de un grado', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_grados DROP CONSTRAINT sg_gra_grado_siguiente;
ALTER TABLE centros_educativos.sg_grados DROP COLUMN gra_grado_siguiente;
ALTER TABLE centros_educativos.sg_grados_aud DROP COLUMN gra_grado_siguiente;


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_grado_precedente_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_rel_grado_precedente(
    rgp_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_rel_grado_precedente_pk_seq'::regclass), 
    rgp_grado_destino_fk bigint not null,
    rgp_grado_origen_fk bigint not null, 
    rgp_ult_mod_fecha timestamp without TIME zone, 
    rgp_ult_mod_usuario CHARACTER varying(45), 
    rgp_version INTEGER, 
    CONSTRAINT sg_rel_grado_precedente_pkey         PRIMARY KEY (rgp_pk), 
    CONSTRAINT sg_rel_grado_precedente_destino_fk  FOREIGN KEY (rgp_grado_destino_fk)      
        REFERENCES centros_educativos.sg_grados(gra_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, 
    CONSTRAINT sg_rel_grado_precedente_origen_fk  FOREIGN KEY (rgp_grado_origen_fk)      
        REFERENCES centros_educativos.sg_grados(gra_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_grado_precedente_aud (
    rgp_pk bigint NOT NULL, 
    rgp_grado_destino_fk bigint not null,
    rgp_grado_origen_fk bigint not null, 
    rgp_ult_mod_fecha timestamp without TIME zone, 
    rgp_ult_mod_usuario CHARACTER varying(45), 
    rgp_version INTEGER,
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_rel_grado_precedente_aud_pkey PRIMARY KEY (rgp_pk, rev), 
    CONSTRAINT sg_rel_grado_precedente_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_INGRESO_MATRICULA_NACIONAL', 'Día y mes por defecto para el ingreso de matrículas calendario nacional', 'dia y mes por defecto para el ingreso de matriculas calendario nacional', '2019-08-08 14:43:24.140', 'casuser', 0, '21/1');

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_INGRESO_MATRICULA_INTERNACIONAL', 'Día y mes por defecto para el ingreso de matrículas calendario internacional', 'dia y mes por defecto para el ingreso de matriculas calendario internacional', '2019-08-08 14:43:24.140', 'casuser', 0, '12/8');

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_REPORTES_PENTAHO','W9', '', 4, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_GIS','W10', '', 4, true, null, null,0);


--3.32.1
ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_aula_fk bigint;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_aula_fk bigint;
ALTER TABLE centros_educativos.sg_secciones ADD CONSTRAINT sg_sec_aula_fk FOREIGN KEY (sec_aula_fk) REFERENCES infraestructuras.sg_aulas(aul_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CARGA_AULA_SECCION','EP65',  'Permite asignar aulas a seccion', 1, true, null, null,0);


--3.33.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_LEGALIZACION_INMUEBLE','W12',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_OTROS_DATOS_SEDE','EP66',  'Permite ver la pestaña de otros datos en sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_TRASLADOS_SEDE','EP67',  'Permite ver la pestaña de traslados en sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_BENEFICIOS_SEDE','EP68',  'Permite ver la pestaña de beneficios en sede', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_repetidor boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_repetidor boolean;

--3.33.5
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_prerregistro boolean;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_persona_fk bigint;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD CONSTRAINT sg_persona_organismo_administracion_persona_fkey FOREIGN KEY (poa_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_prerregistro boolean;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_persona_fk bigint;


--3.33.6
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_CAMBIAR_MODALIDAD_CAMBIO_SECCION','EP69',  'Permite elegir una sección con otra modalidad en el cambio de sección', 1, true, null, null,0);
update  centros_educativos.sg_componente_plan_grado set cpg_precision=0 where cpg_precision is null;


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('MENSAJE_PROMOCION', 'Mensaje que se agrega en pantalla promociones referente a calculo promocion','mensaje que se agrega en pantalla promociones referente a calculo promocion','Recuerde que para que el cálculo de la promoción se realice correctamente deberá, previamente, calcular la aprobación de cada estudiante en cada componente. De otra manera el resultado quedará como pendiente');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('MENSAJE_CIERRE_ANIO', 'Mensaje que se agrega en pantalla promociones referente a cierre de año','mensaje que se agrega en pantalla promociones referente a cierre de año','Recuerde que una vez cerrado el año no será posible: modificar calificaciones, agregar nuevas calificaciones, modificar controles de asistencia, agregar controles de asistencia. Asímismo de no realizar el cierre de año, no será posible la emisión del certificado de promoción para aquellos estudiantes que corresponda.');


--3.33.8
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('UNIR_PERSONAL','E368',  'Permite unir personal', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_permite_cierre_anio boolean;
COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_permite_cierre_anio IS 'Permite cierre anio';
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_permite_cierre_anio boolean;
ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_permite_matricular_siguiente_anio boolean;
COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_permite_matricular_siguiente_anio IS 'Permite matricular siguiente anio';
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_permite_matricular_siguiente_anio boolean;
ALTER TABLE centros_educativos.sg_calendarios ADD COLUMN cal_permite_copiar_seccion boolean;
COMMENT ON COLUMN centros_educativos.sg_calendarios.cal_permite_copiar_seccion IS 'Permite copiar seccion';
ALTER TABLE centros_educativos.sg_calendarios_aud ADD COLUMN cal_permite_copiar_seccion boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERMITE_CIERRE_ANIO','EP70',  'Permite editar check de cierre de año en Calendario', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERMITE_MATRICULAR_SIGUIENTE_ANIO','EP71',  'Permite editar check de matricular siguiente en Calendario', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERMITE_COPIAR_SECCION','EP72',  'Permite editar check de copiar seccion en Calendario', 1, true, null, null,0);

-- 3.34.0.hf2

CREATE UNIQUE INDEX IF NOT EXISTS sg_componente_plan_grado_codigo_externo ON centros_educativos.sg_componente_plan_grado (cpg_codigo_externo)
WHERE cpg_codigo_externo IS NOT NULL;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONSULTA_ASISTENCIAS_SEDES','EM65',  'MENU: opción para consulta de asistencias de sede por nivel', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CONSULTA_CALIFICACIONES_PENDIENTES','EM66',  'MENU: opción para consulta de calificaciones pendientes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ASISTENCIAS_POR_SEDE','EB83',  'Buscar cantidad de asistencias por sede', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CALIFICACIONES_PENDIENTES_POR_SEDE','EB84',  'Buscar calificaciones pendientes por sede', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_cantidad_estudiantes_mat_abierta integer;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_cantidad_estudiantes_mat_abierta integer;
ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_cant_estudiantes_calificados integer;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_cant_estudiantes_calificados integer;
CREATE INDEX IF NOT EXISTS sg_calific_cant_estud_idx ON centros_educativos.sg_calificaciones USING btree (cal_cant_estudiantes_calificados);
CREATE INDEX sg_componente_plan_grado_periodos_cal_idx   ON centros_educativos.sg_componente_plan_grado (cpg_periodos_calificacion ASC NULLS LAST);
CREATE INDEX sg_periodos_calificacion_numero_idx   ON centros_educativos.sg_periodos_calificacion (pca_numero ASC NULLS LAST);
CREATE INDEX IF NOT EXISTS sg_secciones_plan_estudio_fk_idx ON centros_educativos.sg_secciones USING btree (sec_plan_estudio_fk);
CREATE INDEX IF NOT EXISTS sg_secciones_estado_idx ON centros_educativos.sg_secciones USING btree (sec_estado);
CREATE INDEX IF NOT EXISTS sg_secciones_cant_mat_abier_idx ON centros_educativos.sg_secciones USING btree (sec_cantidad_estudiantes_mat_abierta);


UPDATE centros_educativos.sg_calificaciones cabezal SET cal_cant_estudiantes_calificados = 
(select count(*) from centros_educativos.sg_calificaciones_estudiante cae
where cae.cae_calificacion_fk = cabezal.cal_pk);

-- 3.34.0.hf3


DROP INDEX centros_educativos.sg_secciones_cant_mat_abier_idx;
ALTER TABLE centros_educativos.sg_secciones DROP COLUMN sec_cantidad_estudiantes_mat_abierta;
ALTER TABLE centros_educativos.sg_secciones_aud DROP COLUMN sec_cantidad_estudiantes_mat_abierta;

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_cantidad_estudiantes_no_retirados integer;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_cantidad_estudiantes_no_retirados integer;
CREATE INDEX IF NOT EXISTS sg_secciones_cant_no_retirados_idx ON centros_educativos.sg_secciones USING btree (sec_cantidad_estudiantes_no_retirados);
CREATE INDEX IF NOT EXISTS sg_matriculas_retirada_idx ON centros_educativos.sg_matriculas USING btree (mat_retirada) ;


UPDATE centros_educativos.sg_secciones s SET sec_cantidad_estudiantes_no_retirados = 
(select count(*) from centros_educativos.sg_matriculas m
INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
where sec.sec_pk = s.sec_pk and m.mat_retirada = false and m.mat_anulada = false);

-- 3.34.0.hf5

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DEPARTAMENTO','EB85',  'Buscar departamento', 1, true, null, null,0);


--Alfabetizador
CREATE TABLE IF NOT EXISTS centros_educativos.sg_alfabetizador (pse_pk bigint NOT NULL, CONSTRAINT sg_alfabetizador_pkey PRIMARY KEY (pse_pk), CONSTRAINT sg_alfabetizador_personal_sede_fk FOREIGN KEY (pse_pk) REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)) ;
    COMMENT ON TABLE  centros_educativos.sg_alfabetizador        IS 'Tabla para el registro de los alfabetizadores.';
    COMMENT ON COLUMN centros_educativos.sg_alfabetizador.pse_pk IS 'Clave foránea que hace referencia al personal sede educativa.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_alfabetizador_aud (pse_pk bigint NOT NULL, rev bigint, revtype smallint);


UPDATE centros_educativos.sg_sedes SET sed_activo = sed_habilitado;
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ALFABETIZADOR','E372',  'Crear personal de tipo alfabetizador', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ALFABETIZADOR','E373',  'Actualizar personal de tipo alfabetizador', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ALFABETIZADOR','E374',  'Eliminar personal de tipo alfabetizador', 1, true, null, null,0);
delete from seguridad.sg_usuario_rol where pur_usuario_fk = 1 and pur_rol_fk != 1;

CREATE SEQUENCE centros_educativos.sg_codigo_sede_alfa START 1;


CREATE OR REPLACE FUNCTION centros_educativos.generate_codigo_sedes_alfa()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    VOLATILE NOT LEAKPROOF 
AS $BODY$
DECLARE
 dptoCodMunCod character varying(4);
 codAux character varying(15);
 corrAux character varying(3);

BEGIN   

   IF NEW.sed_tipo = 'CIR_ALF' THEN
   
	dptoCodMunCod :='';
	
	SELECT mun.mun_codigo into dptoCodMunCod FROM centros_educativos.sg_direcciones dir
	JOIN catalogo.sg_municipios mun on dir.dir_municipio = mun.mun_pk
	WHERE dir.dir_pk = NEW.sed_direccion_fk;
	
	select nextval('centros_educativos.sg_codigo_sede_alfa') into corrAux;
	corrAux:= trim(lpad(corrAux, 3, '0'));	
	codAux := concat(dptoCodMunCod, corrAux);
	NEW.sed_codigo = codAux;

   END IF;
 
   RETURN NEW;
   
END;
$BODY$;


CREATE TRIGGER codigo_sedes_alfa
    BEFORE INSERT
    ON centros_educativos.sg_sedes
    FOR EACH ROW
    EXECUTE PROCEDURE centros_educativos.generate_codigo_sedes_alfa();



--3.35.0
alter table centros_educativos.sg_datos_empleado drop constraint sg_datos_empleado_personal_sede_fkey;
alter table centros_educativos.sg_datos_empleado drop column dem_personal_sede_fk;

--3.37.0
CREATE SCHEMA alertas;

CREATE SEQUENCE IF NOT EXISTS alertas.sg_alertas_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS alertas.sg_alertas ( 
 ale_pk bigint NOT NULL DEFAULT nextval('alertas.sg_alertas_pk_seq' :: regclass ),
 ale_variable character varying(100),
 ale_riesgo character varying(100),
 ale_estudiante_fk bigint,
 ale_ult_mod_fecha timestamp without time zone,
 CONSTRAINT sg_alertas_pkey PRIMARY KEY (ale_pk));

CREATE INDEX sg_alertas_est_fk_idx ON alertas.sg_alertas USING btree(ale_estudiante_fk);  

ALTER TABLE alertas.sg_alertas ADD CONSTRAINT sg_alertas_est_fkey FOREIGN KEY (ale_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

CREATE INDEX sg_alertas_variable_idx ON alertas.sg_alertas USING btree(ale_variable); 

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ALERTAS_TEMPRANAS_CONFIG','EM61',  'MENU: opción para configurar alertas tempranas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MODIFICAR_CONFIG_ALERTAS_TEMPRANAS','E356',  'Modificar configuración de alertas', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_NOTIFICACION_ALERTAS_TEMPRANAS','EP63',  'Muestra notificación con cantidad de alertas tempranas en página de inicio', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ALERTAS_ESTUDIANTE','EP61',  'Permite ver la solapa Alertas en estudiante.', 1, true, null, null,0);

CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_pk_seq'::regclass), 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_pkey PRIMARY KEY (cnf_pk)
);
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_aud(
    cnf_pk bigint NOT NULL, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_config_alerta_aud_pkey PRIMARY KEY (cnf_pk, rev), 
    CONSTRAINT sg_config_alerta_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

ALTER TABLE alertas.sg_config_alerta ADD COLUMN cnf_riesgo_madre CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta_aud ADD COLUMN cnf_riesgo_madre CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta ADD COLUMN cnf_riesgo_embarazo CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta_aud ADD COLUMN cnf_riesgo_embarazo CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta ADD COLUMN cnf_riesgo_acompaniado CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta_aud ADD COLUMN cnf_riesgo_acompaniado CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta ADD COLUMN cnf_riesgo_matrimonio CHARACTER varying(100);
ALTER TABLE alertas.sg_config_alerta_aud ADD COLUMN cnf_riesgo_matrimonio CHARACTER varying(100);

INSERT INTO alertas.sg_config_alerta(cnf_ult_mod_fecha, cnf_ult_mod_usuario,
 cnf_version, cnf_riesgo_madre, cnf_riesgo_embarazo, cnf_riesgo_acompaniado, cnf_riesgo_matrimonio) values (now(), 'ADMIN', 0, 'MUY_ALTO', 'MUY_ALTO', 'MUY_ALTO', 'MUY_ALTO');


CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_manif_viol_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_manif_viol(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_manif_viol_pk_seq'::regclass), 
    cnf_cabezal bigint not null,
    cnf_categoria_violencia_fk bigint not null, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_mv_pkey PRIMARY KEY (cnf_pk),
    CONSTRAINT sg_config_alerta_mv_cabezal_fk FOREIGN KEY (cnf_cabezal)   
        REFERENCES alertas.sg_config_alerta(cnf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_config_alerta_mv_cat_fk FOREIGN KEY (cnf_categoria_violencia_fk)   
        REFERENCES catalogo.sg_categoria_violencia(cav_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS alertas.sg_cnf_manif_violencia_r_muy_alto(
    cnf_pk bigint NOT NULL,
    tma_pk bigint NOT NULL,
    CONSTRAINT sg_manif_viol_r_muy_alto_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_manif_viol (cnf_pk),
    CONSTRAINT sg_manif_viol_r_muy_alto_tipo_fk FOREIGN KEY (tma_pk) REFERENCES catalogo.sg_tipos_manifestacion (tma_pk)
);

CREATE TABLE IF NOT EXISTS alertas.sg_cnf_manif_violencia_r_alto(
    cnf_pk bigint NOT NULL,
    tma_pk bigint NOT NULL,
    CONSTRAINT sg_manif_viol_r_alto_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_manif_viol (cnf_pk),
    CONSTRAINT sg_manif_viol_r_alto_tipo_fk FOREIGN KEY (tma_pk) REFERENCES catalogo.sg_tipos_manifestacion (tma_pk)
);

CREATE TABLE IF NOT EXISTS alertas.sg_cnf_manif_violencia_r_medio(
    cnf_pk bigint NOT NULL,
    tma_pk bigint NOT NULL,
    CONSTRAINT sg_manif_viol_r_medio_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_manif_viol (cnf_pk),
    CONSTRAINT sg_manif_viol_r_medio_tipo_fk FOREIGN KEY (tma_pk) REFERENCES catalogo.sg_tipos_manifestacion (tma_pk)
);


CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_trabajo_pk_seq
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_trabajo(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_trabajo_pk_seq'::regclass), 
    cnf_cabezal bigint not null,
    cnf_categoria_trabajo bigint not null, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_t_pkey PRIMARY KEY (cnf_pk),
    CONSTRAINT sg_config_alerta_t_cabezal_fk FOREIGN KEY (cnf_cabezal)   
        REFERENCES alertas.sg_config_alerta(cnf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS alertas.sg_cnf_trabajo_r_muy_alto(
    cnf_pk bigint NOT NULL,
    ttr_pk bigint NOT NULL,
    CONSTRAINT sg_trabajo_r_muy_alto_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_trabajo (cnf_pk),
    CONSTRAINT sg_trabajo_r_muy_alto_tipo_fk FOREIGN KEY (ttr_pk) REFERENCES catalogo.sg_tipos_trabajo (ttr_pk)
);

CREATE TABLE IF NOT EXISTS alertas.sg_cnf_trabajo_r_alto(
    cnf_pk bigint NOT NULL,
    ttr_pk bigint NOT NULL,
    CONSTRAINT sg_trabajo_r_alto_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_trabajo (cnf_pk),
    CONSTRAINT sg_trabajo_r_alto_tipo_fk FOREIGN KEY (ttr_pk) REFERENCES catalogo.sg_tipos_trabajo (ttr_pk)
);

CREATE TABLE IF NOT EXISTS alertas.sg_cnf_trabajo_r_medio(
    cnf_pk bigint NOT NULL,
    ttr_pk bigint NOT NULL,
    CONSTRAINT sg_trabajo_r_medio_cnf_fk FOREIGN KEY (cnf_pk) REFERENCES alertas.sg_config_alerta_trabajo (cnf_pk),
    CONSTRAINT sg_trabajo_r_medio_tipo_fk FOREIGN KEY (ttr_pk) REFERENCES catalogo.sg_tipos_trabajo (ttr_pk)
);

ALTER TABLE alertas.sg_config_alerta_trabajo ALTER COLUMN cnf_categoria_trabajo TYPE VARCHAR(100);


CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_asistencia_pk_seq
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_asistencia(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_asistencia_pk_seq'::regclass), 
    cnf_cabezal bigint not null,
    cnf_ciclo_fk bigint not null, 
    cnf_riesgo_muy_alto integer, 
    cnf_riesgo_alto integer, 
    cnf_riesgo_medio integer, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_a_pkey PRIMARY KEY (cnf_pk),
    CONSTRAINT sg_config_alerta_a_cabezal_fk FOREIGN KEY (cnf_cabezal)   
        REFERENCES alertas.sg_config_alerta(cnf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_sobreedad_pk_seq
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_sobreedad(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_sobreedad_pk_seq'::regclass), 
    cnf_cabezal bigint not null,
    cnf_ciclo_fk bigint not null, 
    cnf_riesgo_muy_alto integer, 
    cnf_riesgo_alto integer, 
    cnf_riesgo_medio integer, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_s_pkey PRIMARY KEY (cnf_pk),
    CONSTRAINT sg_config_alerta_s_cabezal_fk FOREIGN KEY (cnf_cabezal)   
        REFERENCES alertas.sg_config_alerta(cnf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX sg_asistencias_inasis_idx  ON centros_educativos.sg_asistencias (asi_inasistencia);


ALTER TABLE centros_educativos.sg_organizaciones_curricular ADD COLUMN ocu_aplica_alertas_tempranas boolean;
ALTER TABLE centros_educativos.sg_organizaciones_curricular_aud ADD COLUMN ocu_aplica_alertas_tempranas boolean;
CREATE INDEX sg_alertas_riesgo_idx ON alertas.sg_alertas USING btree(ale_riesgo); 


CREATE SEQUENCE IF NOT EXISTS alertas.sg_config_alerta_desempenio_pk_seq
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS alertas.sg_config_alerta_desempenio(
    cnf_pk bigint NOT NULL DEFAULT NEXTVAL('alertas.sg_config_alerta_desempenio_pk_seq'::regclass), 
    cnf_cabezal bigint not null,
    cnf_ciclo_fk bigint not null, 
    cnf_riesgo_muy_alto integer, 
    cnf_riesgo_alto integer, 
    cnf_riesgo_medio integer, 
    cnf_ult_mod_fecha timestamp without TIME zone, 
    cnf_ult_mod_usuario CHARACTER varying(45), 
    cnf_version INTEGER, 
    CONSTRAINT sg_config_alerta_des_pkey PRIMARY KEY (cnf_pk),
    CONSTRAINT sg_config_alerta_des_cabezal_fk FOREIGN KEY (cnf_cabezal)   
        REFERENCES alertas.sg_config_alerta(cnf_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE OR REPLACE FUNCTION alertas.procesar_alertas_madre(alerta_cnf alertas.sg_config_alerta) RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta MADRE/PADRE';
	if (alerta_cnf.cnf_riesgo_madre != 'SIN_RIESGO') then
		INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
		select 'MADRE', alerta_cnf.cnf_riesgo_madre, est_pk, now() from centros_educativos.sg_estudiantes e
		INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
		INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
		INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
		INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
		INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
		INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
		INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
		INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
		INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
		INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
		where ocu.ocu_aplica_alertas_tempranas and m.mat_estado = 'ABIERTO' and p.per_cantidad_hijos is not null and p.per_cantidad_hijos > 0;
	end if;

    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_embarazo(alerta_cnf alertas.sg_config_alerta) RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta EMBARAZO';
	if (alerta_cnf.cnf_riesgo_embarazo != 'SIN_RIESGO') then
		INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
		select 'EMBARAZO', alerta_cnf.cnf_riesgo_embarazo, est_pk, now() from centros_educativos.sg_estudiantes e
		INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
		INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
		INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
		INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
		INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
		INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
		INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
		INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
		INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
		INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
		where ocu.ocu_aplica_alertas_tempranas and m.mat_estado = 'ABIERTO' and p.per_embarazo is true;	
	end if;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_acompaniado(alerta_cnf alertas.sg_config_alerta) RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta ACOMPAÑADO';
	if (alerta_cnf.cnf_riesgo_acompaniado != 'SIN_RIESGO') then
            INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
            select 'ACOMPANIADO', alerta_cnf.cnf_riesgo_acompaniado, est_pk, now() from centros_educativos.sg_estudiantes e
            INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
            INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
            INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
            INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
            INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
            INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
            INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
            INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
            INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
            INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
            where ocu.ocu_aplica_alertas_tempranas and m.mat_estado = 'ABIERTO' and p.per_estado_civil_fk = 13;
        end if;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_matrimonio(alerta_cnf alertas.sg_config_alerta) RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta MATRIMONIO';
        if (alerta_cnf.cnf_riesgo_matrimonio != 'SIN_RIESGO') then
            INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
            select 'MATRIMONIO', alerta_cnf.cnf_riesgo_matrimonio, est_pk, now() from centros_educativos.sg_estudiantes e
            INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
            INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
            where ocu.ocu_aplica_alertas_tempranas and m.mat_estado = 'ABIERTO' and p.per_estado_civil_fk = 2;
        end if;
    END;
$$ LANGUAGE plpgsql;

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
			INNER JOIN centros_educativos.sg_estudiantes e ON (e.est_pk = a.asi_estudiante)		
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
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

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_sobreedad(alerta_cnf_id INTEGER) RETURNS void AS $$
DECLARE
alertas_sobreedad alertas.sg_config_alerta_sobreedad%ROWTYPE;
    BEGIN
        RAISE NOTICE 'Procesando alerta SOBREEDAD';
		FOR alertas_sobreedad IN(Select * from alertas.sg_config_alerta_sobreedad where cnf_cabezal = alerta_cnf_id
		) loop
			RAISE NOTICE '----------- ASI PK (%)----',alertas_sobreedad.cnf_pk;
			RAISE NOTICE '----------- CICLO PK (%)----',alertas_sobreedad.cnf_ciclo_fk;
			RAISE NOTICE '----------- MUY ALTO (%)----',alertas_sobreedad.cnf_riesgo_muy_alto;
			RAISE NOTICE '----------- ALTO (%)----',alertas_sobreedad.cnf_riesgo_alto;
			RAISE NOTICE '----------- MEDIO (%)----',alertas_sobreedad.cnf_riesgo_medio;
			
			IF (alertas_sobreedad.cnf_riesgo_muy_alto IS NOT NULL 
				OR alertas_sobreedad.cnf_riesgo_alto IS NOT NULL 
				OR alertas_sobreedad.cnf_riesgo_medio IS NOT NULL) then
		
			INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
			select 'SOBREEDAD',
			CASE WHEN date_part('year', age(p.per_fecha_nacimiento)) >= (gra.gra_edad_maxima + alertas_sobreedad.cnf_riesgo_muy_alto) THEN 'MUY_ALTO' 
    			WHEN date_part('year', age(p.per_fecha_nacimiento)) >= (gra.gra_edad_maxima + alertas_sobreedad.cnf_riesgo_alto) THEN 'ALTO' 
				WHEN date_part('year', age(p.per_fecha_nacimiento)) >= (gra.gra_edad_maxima + alertas_sobreedad.cnf_riesgo_medio) THEN 'MEDIO' 
			END, e.est_pk, now()
			from centros_educativos.sg_estudiantes e	
			INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)	
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)	
                        INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas and cic.cic_pk = alertas_sobreedad.cnf_ciclo_fk 
			and gra.gra_edad_maxima is not null and p.per_fecha_nacimiento <= (now() - (interval '1 year' * (gra.gra_edad_maxima + LEAST(alertas_sobreedad.cnf_riesgo_muy_alto, alertas_sobreedad.cnf_riesgo_alto, alertas_sobreedad.cnf_riesgo_medio))));
			end if;	
		END LOOP;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION alertas.procesar_alertas_manif_violencia() RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta MANIFESTACIÓN VIOLENCIA';
		-- Se asume que hay una sola config de alerta en este caso. Si hubiera más de una hay que filtrar por la pk de la config.
		INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
		select 'MANIFESTACION_VIOLENCIA', CASE WHEN MIN(riesgo) = 1 then 'MUY_ALTO' 
			WHEN MIN(riesgo) = 2 then 'ALTO' 
			WHEN MIN(riesgo) = 3 then 'MEDIO'
			END, estudiante, now() from			
			(
			select 1 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_manifestacion_violencia mv ON (e.est_pk = mv.mvi_estudiante)
			INNER JOIN catalogo.sg_tipos_manifestacion tmv ON (mv.mvi_tipo_manifestacion = tmv.tma_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas and (date_part('year', mv.mvi_fecha) = date_part('year', m.mat_fecha_ingreso))
			and tmv.tma_pk IN (select tma_pk from alertas.sg_cnf_manif_violencia_r_muy_alto)				
			UNION ALL
			select 2 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_manifestacion_violencia mv ON (e.est_pk = mv.mvi_estudiante)
			INNER JOIN catalogo.sg_tipos_manifestacion tmv ON (mv.mvi_tipo_manifestacion = tmv.tma_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas  and (date_part('year', mv.mvi_fecha) = date_part('year', m.mat_fecha_ingreso))
			and tmv.tma_pk IN (select tma_pk from alertas.sg_cnf_manif_violencia_r_alto)
			UNION ALL
			select 3 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_manifestacion_violencia mv ON (e.est_pk = mv.mvi_estudiante)
			INNER JOIN catalogo.sg_tipos_manifestacion tmv ON (mv.mvi_tipo_manifestacion = tmv.tma_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)																												
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas  and (date_part('year', mv.mvi_fecha) = date_part('year', m.mat_fecha_ingreso))
			and tmv.tma_pk IN (select tma_pk from alertas.sg_cnf_manif_violencia_r_medio)
			) z group by estudiante;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_trabajo() RETURNS void AS $$
DECLARE
    BEGIN
        RAISE NOTICE 'Procesando alerta TRABAJO';
		-- Se asume que hay una sola config de alerta en este caso. Si hubiera más de una hay que filtrar por la pk de la config.
		INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
		select 'TRABAJO', CASE WHEN MIN(riesgo) = 1 then 'MUY_ALTO' 
			WHEN MIN(riesgo) = 2 then 'ALTO' 
			WHEN MIN(riesgo) = 3 then 'MEDIO'
			END, estudiante, now() from			
			(
			select 1 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
			INNER JOIN catalogo.sg_tipos_trabajo tt ON (p.per_tipo_trabajo_fk = tt.ttr_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)																												
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas and p.per_fecha_nacimiento > (now() - (interval '21 year'))
			and tt.ttr_pk IN (select ttr_pk from alertas.sg_cnf_trabajo_r_muy_alto)				
			UNION ALL
			select 2 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
			INNER JOIN catalogo.sg_tipos_trabajo tt ON (p.per_tipo_trabajo_fk = tt.ttr_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)																												
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas and p.per_fecha_nacimiento > (now() - (interval '21 year'))
			and tt.ttr_pk IN (select ttr_pk from alertas.sg_cnf_trabajo_r_alto)				
			UNION ALL
			select 3 as riesgo,
			e.est_pk as estudiante
			FROM centros_educativos.sg_estudiantes e 
			INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)
			INNER JOIN catalogo.sg_tipos_trabajo tt ON (p.per_tipo_trabajo_fk = tt.ttr_pk)
			INNER JOIN centros_educativos.sg_matriculas m ON (e.est_ultima_matricula_fk = m.mat_pk)
			INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
			INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
			INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
			INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
			INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
			INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)
			INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
			INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)																												
			where m.mat_estado = 'ABIERTO' and ocu.ocu_aplica_alertas_tempranas and p.per_fecha_nacimiento > (now() - (interval '21 year'))
			and tt.ttr_pk IN (select ttr_pk from alertas.sg_cnf_trabajo_r_medio)
			) z group by estudiante;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas() RETURNS void AS $$
DECLARE
unlocked boolean;
err_context text;
lock_id INTEGER DEFAULT 78;
alerta_cnf alertas.sg_config_alerta;
alerta_cnf_id INTEGER DEFAULT 1;
    BEGIN
        unlocked := (SELECT pg_try_advisory_lock(lock_id));
        if (unlocked = false) then
            RAISE NOTICE 'Procesar alertas: Locked. Skipping...';
            return;
        end if;
	ALTER SEQUENCE alertas.sg_alertas_pk_seq RESTART WITH 1; 
	TRUNCATE alertas.sg_alertas;
		
	SELECT * INTO alerta_cnf from alertas.sg_config_alerta where cnf_pk = alerta_cnf_id;
    
        PERFORM alertas.procesar_alertas_madre(alerta_cnf);
        PERFORM alertas.procesar_alertas_embarazo(alerta_cnf);
	PERFORM alertas.procesar_alertas_acompaniado(alerta_cnf);
        PERFORM alertas.procesar_alertas_matrimonio(alerta_cnf);
        PERFORM alertas.procesar_alertas_asistencia(alerta_cnf_id);
        PERFORM alertas.procesar_alertas_sobreedad(alerta_cnf_id);
        PERFORM alertas.procesar_alertas_manif_violencia();
        PERFORM alertas.procesar_alertas_trabajo();
        --PERFORM alertas.procesar_alertas_desempenio(alerta_cnf_id);
			
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

update  centros_educativos.sg_componente_plan_grado set cpg_precision=0 where cpg_precision is null;
-- Días lectivos de horario escolar
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_dia_lectivo_horario_escolar_dlh_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_dia_lectivo_horario_escolar (dlh_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_dia_lectivo_horario_escolar_dlh_pk_seq'::regclass),dlh_dia character varying(45) ,dlh_es_lectivo boolean,dlh_horario_escolar_fk bigint, dlh_ult_mod_fecha timestamp without time zone, dlh_ult_mod_usuario character varying(45), dlh_version integer, CONSTRAINT sg_dia_lectivo_horario_escolar_pkey PRIMARY KEY (dlh_pk),CONSTRAINT dlh_dia_lectivo_horario_escolar_fk FOREIGN KEY (dlh_horario_escolar_fk) REFERENCES centros_educativos.sg_horarios_escolares(hes_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_dia_lectivo_horario_escolar IS 'Tabla para el registro de días lectivos de horario escolar.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_dia IS 'Día de semana de registro.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_es_lectivo IS 'Booleano que indica si es lectivo.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_horario_escolar_fk IS 'Foreing key a horario escolar.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_dia_lectivo_horario_escolar.dlh_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_dia_lectivo_horario_escolar_aud (dlh_pk bigint NOT NULL,dlh_dia character varying(45) ,dlh_es_lectivo boolean,dlh_horario_escolar_fk bigint, dlh_ult_mod_fecha timestamp without time zone, dlh_ult_mod_usuario character varying(45), dlh_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_dia_lectivo_horario_escolar_aud ADD PRIMARY KEY (dlh_pk, rev);


ALTER TABLE centros_educativos.sg_dia_lectivo_horario_escolar ADD CONSTRAINT fia_lectivo_dia_horario_uk UNIQUE (dlh_horario_escolar_fk,dlh_dia);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DIA_LECTIVO_HORARIO_ESCOLAR','E369',  'Crea dia lectivo horario escolar', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DIA_LECTIVO_HORARIO_ESCOLAR','E370',  'Actualiza día lectivo horario escolar', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DIA_LECTIVO_HORARIO_ESCOLAR','E371',  'Elimina día lectivo horario escolar', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DIA_LECTIVO_HORARIO_ESCOLAR','EB82',  'Buscar dia lectivo horario escolar', 1, true, null, null,0);


CREATE INDEX sg_control_asistencia_fecha_idx ON centros_educativos.sg_control_asistencia_cabezal USING btree (cac_fecha);

 

ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN acc_archivo_borrado boolean;
COMMENT ON COLUMN centros_educativos.sg_archivo_calificaciones.acc_archivo_borrado IS 'Archivo borrado';
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN acc_archivo_borrado boolean;

CREATE UNIQUE INDEX sg_componente_plan_grado_codigo_externo ON centros_educativos.sg_componente_plan_grado (cpg_codigo_externo)
WHERE cpg_codigo_externo IS NOT NULL;

--3.37.2
ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_matricula_fk bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_matricula_fk bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD CONSTRAINT sg_esc_matricula_fkey FOREIGN KEY (esc_matricula_fk) REFERENCES centros_educativos.sg_matriculas(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ESCOLARIDAD_ESTUDIANTE','EP73',  'Permite ver la pestaña escolaridad en estudiante', 1, true, null, null,0);

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_CIERRE_MATRICULA_NACIONAL', 'Día y mes por defecto para el cierre de matrículas calendario nacional', 'dia y mes por defecto para el cierre de matriculas calendario nacional', '2019-08-08 14:43:24.140', 'casuser', 0, '01/12');

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_CIERRE_MATRICULA_INTERNACIONAL', 'Día y mes por defecto para el cierre de matrículas calendario internacional', 'dia y mes por defecto para el cierre de matriculas calendario internacional', '2019-08-08 14:43:24.140', 'casuser', 0, '01/08');


INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('HABILITAR_CONTROL_DIA_LECTIVO', 'Configuración para habilitar la funcionalidad de días lectivos en centros educativos (1-Habilita funcionalidad, 0-Deshabilita)', 'Configuración para habilitar la funcionalidad de días lectivos en centros educativos (1-Habilita funcionalidad, 0-Deshabilita)', '2019-08-08 14:43:24.140', 'casuser', 0, '0');



--3.37.3
INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('BOTON_ESTADISTICAS_PORTAL', 'Habilita que se muestre el botón Estadísticas en portal (1-Habilita,0-Deshabilita)', 'Habilita que se muestre el botón Estadísticas en portal (1-Habilita,0-Deshabilita)', '2019-08-08 14:43:24.140', 'casuser', 0, '0');

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('BOTON_DATOS_ABIERTOS_PORTAL', 'Habilita que se muestre el botón Datos Abiertos en portal (1-Habilita,0-Deshabilita)', 'Habilita que se muestre el botón Datos Abiertos en portal (1-Habilita,0-Deshabilita)', '2019-08-08 14:43:24.140', 'casuser', 0, '0');

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_creado_cierre boolean;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_creado_cierre boolean;
COMMENT ON COLUMN centros_educativos.sg_escolaridad_estudiante.esc_creado_cierre IS 'Indica si el registro fue creado por el proceso cierre de año.';

UPDATE centros_educativos.sg_escolaridad_estudiante SET esc_creado_cierre=true;

-- 3.39.0

-- Rel Acuerdos propuesta pedagica
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sg_rel_acuerdos_prop_pedagogica_rgp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_acuerdos_prop_pedagogica 
(rap_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_sg_rel_acuerdos_prop_pedagogica_rgp_pk_seq'::regclass),
rap_propuesta_pedagogica_fk bigint,
rap_acuerdo_sede_fk bigint,
rap_habilitado boolean,
rap_ult_mod_fecha timestamp without time zone,
rap_ult_mod_usuario character varying(45),
rap_version integer,
CONSTRAINT sg_rel_acuerdos_prop_pedagogica_pkey PRIMARY KEY (rap_pk),
CONSTRAINT sg_rap_propuesta_pedagogica_fkey FOREIGN KEY (rap_propuesta_pedagogica_fk) REFERENCES centros_educativos.sg_propuestas_pedagogicas(ppe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT rap_acuerdo_sede_fkey FOREIGN KEY (rap_acuerdo_sede_fk) REFERENCES centros_educativos.sg_acuerdos_sedes(ase_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE cascade);

COMMENT ON TABLE centros_educativos.sg_rel_acuerdos_prop_pedagogica IS 'Tabla para el registro de rel acuerdos propuestas pedagógicas.';
COMMENT ON COLUMN centros_educativos.sg_rel_acuerdos_prop_pedagogica.rap_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_acuerdos_prop_pedagogica.rap_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN centros_educativos.sg_rel_acuerdos_prop_pedagogica.rap_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_acuerdos_prop_pedagogica.rap_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_acuerdos_prop_pedagogica.rap_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_acuerdos_prop_pedagogica_aud 
(rap_pk bigint NOT NULL,
rap_propuesta_pedagogica_fk bigint,
rap_acuerdo_sede_fk bigint,
rap_habilitado boolean,
rap_ult_mod_fecha timestamp without time zone,
rap_ult_mod_usuario character varying(45),
rap_version integer,
rev bigint,
revtype smallint);
ALTER TABLE centros_educativos.sg_rel_acuerdos_prop_pedagogica_aud ADD PRIMARY KEY (rap_pk, rev);

ALTER TABLE centros_educativos.sg_rel_acuerdos_prop_pedagogica ADD CONSTRAINT sg_rel_acuerdos_prop_pedagogica_uk UNIQUE (rap_propuesta_pedagogica_fk,rap_acuerdo_sede_fk);


ALTER TABLE centros_educativos.sg_detalle_plan_escolar ADD COLUMN dpe_aplica_sistemas_integrados boolean default false;
ALTER TABLE centros_educativos.sg_detalle_plan_escolar_aud ADD COLUMN dpe_aplica_sistemas_integrados boolean default false;

--3.39.2
ALTER TABLE centros_educativos.sg_estudiante_impresion RENAME COLUMN eim_habilitado_imprimir TO eim_anulada;
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud RENAME COLUMN eim_habilitado_imprimir TO eim_anulada;

ALTER TABLE centros_educativos.sg_estudiante_impresion ADD COLUMN eim_motivo_anulacion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud ADD COLUMN eim_motivo_anulacion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_estudiante_impresion ADD CONSTRAINT sg_eim_motivo_anulacion_titulo_fk FOREIGN KEY (eim_motivo_anulacion_titulo_fk) REFERENCES catalogo.sg_motivo_anulacion_titulo(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('MENSAJE_SOLICITUD_IMPRESION', 'Mensaje de solicitud impresión', 'Mensaje de solicitud impresión', '2019-08-08 14:43:24.140', 'casuser', 0, 'Mensaje de solicitud impresión');


ALTER TABLE centros_educativos.sg_estudiante_impresion ADD COLUMN eim_observacion_anulada character varying(120);
ALTER TABLE centros_educativos.sg_estudiante_impresion_aud ADD COLUMN eim_observacion_anulada character varying(120);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_TITULOS','EP74',  'Permite ver alertas referentes a títulos', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_tipo_impresion CHARACTER VARYING(50);
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_tipo_impresion CHARACTER VARYING(50);


-- Titulos
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_titulo_tit_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_titulo (tit_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_titulo_tit_pk_seq'::regclass),
 tit_estudiante_fk bigint, 
tit_nombre_estudiante character varying(255), 
tit_nombre_estudiante_partida character varying(255), 
tit_nombre_certificado character varying(255),
tit_fecha_validez date,
tit_fecha_emision date,
tit_sello_firma_director_fk bigint,
tit_sello_firma_titular_fk bigint,
tit_nombre_director character varying(255),
tit_nombre_ministro character varying(255),
tit_anio integer,
tit_sede_fk bigint,
tit_sede_codigo character varying(255),
tit_sede_nombre character varying(255),
tit_servicio_educativo_fk bigint,
tit_solicitud_impresion_fk bigint, 
tit_usuario_envia_imprimir character varying(255),
tit_hash CHARACTER VARYING(500),
tit_ult_mod_fecha timestamp without time zone, tit_ult_mod_usuario character varying(45), tit_version integer, 
CONSTRAINT sg_titulo_pkey PRIMARY KEY (tit_pk),
CONSTRAINT sg_tit_sello_firma_director_fk FOREIGN KEY (tit_sello_firma_director_fk) REFERENCES centros_educativos.sg_sellos_firmas (sfi_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_tit_sello_firma_titular_fk FOREIGN KEY (tit_sello_firma_titular_fk) REFERENCES centros_educativos.sg_sello_firma_titular (sft_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_tit_estudiante_fk FOREIGN KEY (tit_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes (est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_tit_sede_fk FOREIGN KEY (tit_sede_fk) REFERENCES centros_educativos.sg_sedes (sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_tit_servicio_educativo_fk FOREIGN KEY (tit_servicio_educativo_fk) REFERENCES centros_educativos.sg_servicio_educativo (sdu_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_tit_solicitud_impresion_fk FOREIGN KEY (tit_solicitud_impresion_fk) REFERENCES centros_educativos.sg_solicitudes_impresion (sim_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_titulo IS 'Tabla para el registro de titulos.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_estudiante_fk IS 'Estudiante del registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_nombre_estudiante IS 'Nombre del estudiante.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_nombre_estudiante_partida IS 'Nombre estudiante registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_fecha_validez IS 'Fecha validez en registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_fecha_emision IS 'Fecha emision en registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sello_firma_director_fk IS 'Sello firma director.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sello_firma_titular_fk IS 'Sello firma titular.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_nombre_director IS 'Nombre de director.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_nombre_ministro IS 'Nombre de ministro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_anio IS 'Anio.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sede_fk IS 'Sede.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sede_codigo IS 'código de sede.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sede_nombre IS 'Nombre de sede.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_servicio_educativo_fk IS 'Servicio educativo.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_solicitud_impresion_fk IS 'Solicitud impresión.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_hash IS 'Hash.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_titulo_aud (tit_pk bigint NOT NULL,
 tit_estudiante_fk bigint, 
tit_nombre_estudiante character varying(255), 
tit_nombre_estudiante_partida character varying(255), 
tit_nombre_certificado character varying(255),
tit_fecha_validez date,
tit_fecha_emision date,
tit_sello_firma_director_fk bigint,
tit_sello_firma_titular_fk bigint,
tit_nombre_director character varying(255),
tit_nombre_ministro character varying(255),
tit_anio integer,
tit_sede_fk bigint,
tit_sede_codigo character varying(255),
tit_sede_nombre character varying(255),
tit_servicio_educativo_fk bigint,
tit_solicitud_impresion_fk bigint, 
tit_usuario_envia_imprimir character varying(255),
tit_hash CHARACTER VARYING(500), tit_ult_mod_fecha timestamp without time zone, tit_ult_mod_usuario character varying(45), tit_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_titulo_aud ADD PRIMARY KEY (tit_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_TITULO','E375',  'Crear título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_TITULO','E376',  'Actualizar título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_TITULO','E377',  'Eliminar título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_TITULO','EB86',  'Buscar titulo', 1, true, null, null,0);


CREATE INDEX IF NOT EXISTS sg_titulo_hash_idx ON centros_educativos.sg_titulo USING btree (tit_hash) ;

--3.39.3
ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_retornada boolean default false;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_retornada boolean;

-- 3.40.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_CONFIRMACION_MATRICULA_EN_SEDE','EB87',  'Buscar confirmación de matricula en sede', 1, true, null, null,0);

--3.40.1

-- Acuerdos
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_acuerdo_acu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_acuerdo (acu_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_acuerdo_acu_pk_seq'::regclass),acu_propuesta_pedagogica_fk bigint, acu_nombre_acuerdo character varying(255), acu_descripcion character varying(4000), acu_estado character varying(255), acu_fecha date,acu_aplica_sistemas_integrados boolean ,acu_ult_mod_fecha timestamp without time zone, acu_ult_mod_usuario character varying(45), acu_version integer, CONSTRAINT sg_acuerdo_pkey PRIMARY KEY (acu_pk),CONSTRAINT sg_acu_propuesta_pedagogica_fkey FOREIGN KEY (acu_propuesta_pedagogica_fk) REFERENCES centros_educativos.sg_propuestas_pedagogicas(ppe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_acuerdo IS 'Tabla para el registro de acuerdos.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_nombre_acuerdo IS 'Nombre de acuerdo.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_descripcion IS 'Descripcion.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_estado IS 'Estado.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_fecha IS 'Fecha.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_aplica_sistemas_integrados IS 'Aplica sistemas integrados.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_acuerdo.acu_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_acuerdo_aud (acu_pk bigint NOT NULL,acu_propuesta_pedagogica_fk bigint, acu_nombre_acuerdo character varying(255), acu_descripcion character varying(4000), acu_estado character varying(255), acu_fecha date,acu_aplica_sistemas_integrados boolean, acu_ult_mod_fecha timestamp without time zone, acu_ult_mod_usuario character varying(45), acu_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_acuerdo_aud ADD PRIMARY KEY (acu_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACUERDO','E378',  'Crear acuerdo', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACUERDO','E379',  'Actualizar acuerdo', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACUERDO','E380',  'Eliminar acuerdo', 1, true, null, null,0);

drop table centros_educativos.sg_rel_acuerdos_prop_pedagogica;
drop table centros_educativos.sg_rel_acuerdos_prop_pedagogica_aud;

--3.40.3
INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('BOTON_BUSCAR_POR_CERCANIA_PORTAL', 'En portal permite visualizar el botón buscar por cercania (1-visualizar 0-ocultar)', 'en portal permite visualizar el boton buscar por cercania (1-visualizar 0-ocultar)', '2019-01-08 14:43:24.140', 'casuser', 0, '0');

--3.41.0
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal add column cac_proceso_de_creacion character varying(20);
ALTER TABLE centros_educativos.sg_control_asistencia_cabezal_aud add column cac_proceso_de_creacion character varying(20);

--3.41.1
ALTER TABLE centros_educativos.sg_datos_contratacion ADD COLUMN dco_tipo_alfabetizador_fk bigint;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_tipo_alfabetizador_fk bigint;
ALTER TABLE centros_educativos.sg_datos_contratacion ADD CONSTRAINT sg_dco_tipo_alfabetizador_fk FOREIGN KEY (dco_tipo_alfabetizador_fk) REFERENCES catalogo.sg_tipo_alfabetizador(tal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--3.41.2
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SERVICIO_SOCIAL','EM67',  'MENU: opción para carga masiva de servicio social', 1, true, null, null,0);

--3.41.4
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_motivo_reimpresion_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_motivo_reimpresion_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_motivo_reimpresion_fk FOREIGN KEY (sim_motivo_reimpresion_fk) REFERENCES catalogo.sg_motivo_reimpresion(mri_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REIMPRESION_TITULO','EM68',  'MENU: opción para reimpresion título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REIMPRESION_TITULO','E381',  'Crea reimpresion titulo', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_promedio_calificaciones numeric(10,2) NULL;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_promedio_calificaciones numeric(10,2) NULL;

--3.41.8
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_docentes_documentos_ddo_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_docentes_documentos(ddo_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_docentes_documentos_ddo_pk_seq'::regclass), ddo_personal_fk bigint not null, ddo_tipo_documento_fk bigint not null, ddo_descripcion character varying(255), ddo_archivo_fk bigint not null, ddo_ult_mod_fecha timestamp without TIME zone, ddo_ult_mod_usuario CHARACTER varying(45), ddo_version INTEGER, CONSTRAINT sg_docentes_documentos_pkey PRIMARY KEY (ddo_pk), CONSTRAINT sg_docentes_documentos_peresonal_fk FOREIGN KEY (ddo_personal_fk) REFERENCES centros_educativos.sg_personal_sede_educativa (pse_pk), CONSTRAINT sg_docentes_documentos_tipo_documento_fk FOREIGN KEY (ddo_tipo_documento_fk) REFERENCES catalogo.sg_tipos_documentos_docente (tdd_pk), CONSTRAINT sg_docentes_documentos_archivo_fk FOREIGN KEY (ddo_archivo_fk) REFERENCES public.sg_archivos (ach_pk));
    COMMENT ON TABLE  centros_educativos.sg_docentes_documentos                           IS 'Tabla para almacenar los documentos de los docentes.';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_personal_fk           IS 'Llave foranea que hace referencia al personal.';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_tipo_documento_fk     IS 'Llave foranea que hace referencia al tipo de documento';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_descripcion           IS 'Descripción del registro';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_archivo_fk            IS 'Llave foranea que hace referencia al archivo';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_docentes_documentos.ddo_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_docentes_documentos_aud (ddo_pk bigint NOT NULL, ddo_personal_fk bigint not null, ddo_tipo_documento_fk bigint not null, ddo_descripcion character varying(255), ddo_archivo_fk bigint not null, ddo_ult_mod_fecha timestamp without TIME zone, ddo_ult_mod_usuario CHARACTER varying(45), ddo_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_docentes_documentos_aud_pkey PRIMARY KEY (ddo_pk, rev), CONSTRAINT sg_docentes_documentos_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DOCENTE_DOCUMENTO','E334',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DOCENTE_DOCUMENTO','E335',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DOCENTE_DOCUMENTO','E336',  '', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_DOCUMENTACION_PERSONAL_SEDE','EP43',  '', 1, true, null, null,0);
ALTER TABLE centros_educativos.sg_componente_plan_estudio     ADD COLUMN cpe_codigo_externo integer;
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud ADD COLUMN cpe_codigo_externo integer;

CREATE UNIQUE INDEX sg_componente_plan_estudio_codigo_externo ON centros_educativos.sg_componente_plan_estudio (cpe_codigo_externo)
WHERE cpe_codigo_externo IS NOT NULL;

-- 3.42.0

ALTER TABLE centros_educativos.sg_experiencia_laboral ADD COLUMN ela_validada boolean;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD COLUMN ela_validada boolean; 

ALTER TABLE centros_educativos.sg_idiomas_realizados ADD COLUMN ire_validado boolean;
ALTER TABLE centros_educativos.sg_idiomas_realizados_aud ADD COLUMN ire_validado boolean;

ALTER TABLE centros_educativos.sg_estudios_superiores ADD COLUMN esu_validado boolean;
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD COLUMN esu_validado boolean;

ALTER TABLE centros_educativos.sg_capacitaciones ADD COLUMN cap_validado boolean;
ALTER TABLE centros_educativos.sg_capacitaciones_aud ADD COLUMN cap_validado boolean;

ALTER TABLE centros_educativos.sg_docentes_documentos ADD COLUMN ddo_validado boolean;
ALTER TABLE centros_educativos.sg_docentes_documentos_aud ADD COLUMN ddo_validado boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VALIDAR_INFORMACION_FICHA_DOCENTE','E382',  'Permite validar información de la ficha docente', 1, true, null, null,0);

-- 3.42.1

ALTER TABLE centros_educativos.sg_docentes_documentos ADD COLUMN ddo_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_docentes_documentos_aud ADD COLUMN ddo_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_docentes_documentos ADD COLUMN ddo_ult_val_usuario CHARACTER varying(45);
ALTER TABLE centros_educativos.sg_docentes_documentos_aud ADD COLUMN ddo_ult_val_usuario CHARACTER varying(45);

ALTER TABLE centros_educativos.sg_idiomas_realizados ADD COLUMN ire_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_idiomas_realizados_aud ADD COLUMN ire_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_idiomas_realizados ADD COLUMN ire_ult_val_usuario CHARACTER varying(45);
ALTER TABLE centros_educativos.sg_idiomas_realizados_aud ADD COLUMN ire_ult_val_usuario CHARACTER varying(45);


ALTER TABLE centros_educativos.sg_estudios_superiores ADD COLUMN esu_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD COLUMN esu_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_estudios_superiores ADD COLUMN esu_ult_val_usuario CHARACTER varying(45);
ALTER TABLE centros_educativos.sg_estudios_superiores_aud ADD COLUMN esu_ult_val_usuario CHARACTER varying(45);

ALTER TABLE centros_educativos.sg_experiencia_laboral ADD COLUMN ela_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD COLUMN ela_ult_val_fecha timestamp without TIME zone; 
ALTER TABLE centros_educativos.sg_experiencia_laboral ADD COLUMN ela_ult_val_usuario CHARACTER varying(45);
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud ADD COLUMN ela_ult_val_usuario CHARACTER varying(45); 

ALTER TABLE centros_educativos.sg_capacitaciones ADD COLUMN cap_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_capacitaciones_aud ADD COLUMN cap_ult_val_fecha timestamp without TIME zone;
ALTER TABLE centros_educativos.sg_capacitaciones ADD COLUMN cap_ult_val_usuario CHARACTER varying(45);
ALTER TABLE centros_educativos.sg_capacitaciones_aud ADD COLUMN cap_ult_val_usuario CHARACTER varying(45);

--3.42.3


CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_grado_plan_definicion_titulo (rgp_pk bigint NOT NULL,dti_pk bigint NOT NULL, CONSTRAINT sg_rel_grado_plan_definicion_titulo_rel_grado_plan_fk FOREIGN KEY (rgp_pk) REFERENCES centros_educativos.sg_rel_grado_plan_estudio (rgp_pk), CONSTRAINT sg_rel_grado_plan_definicion_titulo_def_titulo_fk FOREIGN KEY (dti_pk) REFERENCES catalogo.sg_definiciones_titulo (dti_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_grado_plan_definicion_titulo_aud(rgp_pk bigint NOT NULL,dti_pk bigint NOT NULL, rev bigint, revtype smallint);


INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('MENSAJE_PANTALLA_SEDE_PORTAL', 'Mensaje que se muestra en la pantalla de sede de Portal', 'mensaje que se muestra en la pantalla de sede de portal', '2019-08-08 14:43:24.140', 'casuser', 0, 'Los datos de matrícula son los registrados al día de hoy de en el SIGES y no datos de estadística oficiales.');

--3.42.4
ALTER TABLE centros_educativos.sg_componente_plan_estudio ADD COLUMN cpe_es_paes boolean;
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud ADD COLUMN cpe_es_paes boolean;

--3.42.5

-- Reposición Título
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_reposicion_titulo_ret_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reposicion_titulo (ret_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_reposicion_titulo_ret_pk_seq'::regclass), ret_nombre_estudiante_partida character varying(255),  ret_anio integer,ret_sede_fk bigint,ret_sede_nombre character varying(255),ret_estado_reposicion character varying(255),ret_usuario_envia_imprimir character varying(255), ret_numero_resolucion integer, ret_numero_registro integer, ret_ult_mod_fecha timestamp without time zone, ret_ult_mod_usuario character varying(45), ret_version integer, CONSTRAINT sg_reposicion_titulo_pkey PRIMARY KEY (ret_pk), CONSTRAINT sg_ret_sede_fk FOREIGN KEY (ret_sede_fk) REFERENCES centros_educativos.sg_sedes (sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_reposicion_titulo IS 'Tabla para el registro de reposición título.';
COMMENT ON COLUMN centros_educativos.sg_reposicion_titulo.ret_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_reposicion_titulo.ret_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reposicion_titulo.ret_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_reposicion_titulo.ret_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_reposicion_titulo_aud (ret_pk bigint NOT NULL, ret_nombre_estudiante_partida character varying(255), ret_anio integer,ret_sede_fk bigint,ret_sede_nombre character varying(255),ret_estado_reposicion character varying(255),ret_usuario_envia_imprimir character varying(255), ret_numero_resolucion integer, ret_numero_registro integer, ret_ult_mod_fecha timestamp without time zone, ret_ult_mod_usuario character varying(45), ret_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD PRIMARY KEY (ret_pk, rev);

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_reposicion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_reposicion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD CONSTRAINT sg_sim_reposicion_titulo_fk FOREIGN KEY (sim_reposicion_titulo_fk) REFERENCES centros_educativos.sg_reposicion_titulo (ret_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REPOSICION_TITULO','E383',  'Crea reposición de título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REPOSICION_TITULO','E384',  'Actualiza reposición de título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REPOSICION_TITULO','E385',  'Elimina reposición de título', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_REPOSICION_TITULO','EM69',  'MENU: Reposición título', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_anulada boolean;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_anulada boolean;

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_motivo_anulacion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_motivo_anulacion_titulo_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo ADD CONSTRAINT sg_ret_motivo_anulacion_titulo_fk FOREIGN KEY (ret_motivo_anulacion_titulo_fk) REFERENCES catalogo.sg_motivo_anulacion_titulo(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_observacion_anulada character varying(120);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_observacion_anulada character varying(120);

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_nombre_estudiante_partida_busqueda character varying(255);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_nombre_estudiante_partida_busqueda character varying(255);

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_sede_nombre_busqueda character varying(255);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_sede_nombre_busqueda character varying(255);

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) VALUES('TAMANIO_ARCHIVO_IMPORT_PAES', 'Tamaño permitido de archivo de importación para PAES en bytes', 'tamaño permitido de archivo de importacion para paes en bytes', 0, '1048576', false);


--3.42.6
ALTER TABLE centros_educativos.sg_matriculas ADD COLUMN mat_cerrado_por_cierre_anio boolean;
ALTER TABLE centros_educativos.sg_matriculas_aud ADD COLUMN mat_cerrado_por_cierre_anio boolean;

CREATE INDEX sg_solicitud_impresion_estado_idx ON centros_educativos.sg_solicitudes_impresion USING btree (sim_estado);
CREATE INDEX sg_solicitud_impresion_tipo_idx ON centros_educativos.sg_solicitudes_impresion USING btree (sim_tipo_impresion);

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_anulado boolean;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_anulado boolean;

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_motivo_reimpresion_fk bigint;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_motivo_reimpresion_fk bigint;
ALTER TABLE centros_educativos.sg_titulo ADD CONSTRAINT sg_tit_motivo_reimpresion_fk FOREIGN KEY (tit_motivo_reimpresion_fk) REFERENCES catalogo.sg_motivo_reimpresion(mri_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


CREATE INDEX sg_titulo_anulada_idx ON centros_educativos.sg_titulo USING btree (tit_anulado);
CREATE INDEX sg_titulo_anio_idx ON centros_educativos.sg_titulo USING btree (tit_anio);
CREATE INDEX sg_titulo_estudiante_idx ON centros_educativos.sg_titulo USING btree (tit_estudiante_fk);
CREATE INDEX sg_titulo_sede_idx ON centros_educativos.sg_titulo USING btree (tit_sede_fk);

--

CREATE INDEX IF NOT EXISTS sg_comp_plan_grado_obj_prom_idx ON centros_educativos.sg_componente_plan_grado USING btree (cpg_objeto_promocion) ;
CREATE INDEX IF NOT EXISTS sg_comp_plan_grado_exc_sec_idx ON centros_educativos.sg_componente_plan_grado USING btree (cpg_exclusivo_seccion) ;

CREATE INDEX IF NOT EXISTS sg_escala_calificacion_tipo_idx ON catalogo.sg_escalas_calificacion USING btree (eca_tipo_escala) ;

CREATE OR REPLACE FUNCTION public.first_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $1;
$$;
 
-- And then wrap an aggregate around it
CREATE AGGREGATE public.FIRST (
        sfunc    = public.first_agg,
        basetype = anyelement,
        stype    = anyelement
);
 
-- Create a function that always returns the last non-NULL item
CREATE OR REPLACE FUNCTION public.last_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $2;
$$;
 
-- And then wrap an aggregate around it
CREATE AGGREGATE public.LAST (
        sfunc    = public.last_agg,
        basetype = anyelement,
        stype    = anyelement
);


CREATE MATERIALIZED VIEW centros_educativos.sg_estudiantes_notin_actuales AS

select 
h1.cpe_pk, h1.cpe_nombre, h1.cae_estudiante_fk, h1.sed_pk, h1.sec_pk, h1.eca_minimo_aprobacion, h1.cpg_calculo_nota_institucional, 
CASE 
        WHEN cpg_calculo_nota_institucional = 'MAY' THEN MAX(h1.cae_calificacion::decimal) 
        WHEN cpg_calculo_nota_institucional = 'PROM' THEN AVG(h1.cae_calificacion::decimal) 
        WHEN cpg_calculo_nota_institucional = 'MED' THEN AVG(h1.cae_calificacion::decimal) 
        WHEN cpg_calculo_nota_institucional = 'ULT' THEN LAST(h1.cae_calificacion::decimal order by h1.rfe_fecha_desde asc) 
      ELSE AVG(h1.cae_calificacion::decimal) 
END as nota_institucional
from
(select calest.cae_estudiante_fk, cabezal.cal_pk, rf.rfe_fecha_desde, sed.sed_pk, sec.sec_pk, cpe.cpe_pk,
cpe.cpe_nombre, cpg.cpg_calculo_nota_institucional, escala.eca_minimo_aprobacion, calest.cae_calificacion
                from centros_educativos.sg_calificaciones_estudiante calest 
                INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk)
                                INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)
                                INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)
                    INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk)
                    INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)
                INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) 
                INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk))
                INNER JOIN catalogo.sg_escalas_calificacion escala ON (escala.eca_pk = cpg.cpg_escala_calificacion_fk)
                INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk and rf.rfe_fecha_desde < now())
                where calest.cae_calificacion is not null and (cpg.cpg_objeto_promocion) and cabezal.cal_tipo_periodo_calificacion = 'ORD') as h1
                    
                    INNER JOIN
                    
                    (SELECT * from centros_educativos.sg_sedes sed
                    INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
                    INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND cal.cal_habilitado order by cal_fecha_inicio desc limit 1))) as h2
                    
                    ON (h1.sed_pk = h2.sed_pk and h1.rfe_fecha_desde >= h2.cal_fecha_inicio AND  h1.rfe_fecha_desde <= h2.cal_fecha_fin)            
                    group by  h1.cpe_pk, h1.cpe_nombre, h1.cae_estudiante_fk, h1.eca_minimo_aprobacion, h1.sed_pk, h1.sec_pk, h1.cpg_calculo_nota_institucional;
                    

CREATE INDEX IF NOT EXISTS sg_estudiantes_notin_actuales_est_idx ON centros_educativos.sg_estudiantes_notin_actuales USING btree (cae_estudiante_fk);
CREATE INDEX IF NOT EXISTS sg_estudiantes_notin_actuales_min_aprob_idx ON centros_educativos.sg_estudiantes_notin_actuales USING btree (eca_minimo_aprobacion);
CREATE INDEX IF NOT EXISTS sg_estudiantes_notin_actuales_nota_ins_idx ON centros_educativos.sg_estudiantes_notin_actuales USING btree (nota_institucional);

CREATE OR REPLACE FUNCTION alertas.procesar_alertas_desempenio(alerta_cnf_id INTEGER) RETURNS void AS $$
DECLARE
alertas_desempenio alertas.sg_config_alerta_desempenio%ROWTYPE;
    BEGIN
        RAISE NOTICE 'Procesando alerta DESEMPENIO';
        FOR alertas_desempenio IN(Select * from alertas.sg_config_alerta_desempenio where cnf_cabezal = alerta_cnf_id
        ) loop
            RAISE NOTICE '----------- CNF PK (%)----',alertas_desempenio.cnf_pk;
            RAISE NOTICE '----------- CICLO PK (%)----',alertas_desempenio.cnf_ciclo_fk;
            RAISE NOTICE '----------- MUY ALTO (%)----',alertas_desempenio.cnf_riesgo_muy_alto;
            RAISE NOTICE '----------- ALTO (%)----',alertas_desempenio.cnf_riesgo_alto;
            RAISE NOTICE '----------- MEDIO (%)----',alertas_desempenio.cnf_riesgo_medio;
            IF (alertas_desempenio.cnf_riesgo_muy_alto IS NOT NULL 
                OR alertas_desempenio.cnf_riesgo_alto IS NOT NULL 
                OR alertas_desempenio.cnf_riesgo_medio IS NOT NULL) then
                
            INSERT into alertas.sg_alertas (ale_variable, ale_riesgo, ale_estudiante_fk, ale_ult_mod_fecha)
            select 'DESEMPENIO',
            CASE WHEN count(*) >= alertas_desempenio.cnf_riesgo_muy_alto THEN 'MUY_ALTO' 
                WHEN count(*) >= alertas_desempenio.cnf_riesgo_alto THEN 'ALTO' 
                WHEN count(*) >= alertas_desempenio.cnf_riesgo_medio THEN 'MEDIO' 
            END,
            est_notin_actuales.cae_estudiante_fk, now()
            from centros_educativos.sg_estudiantes_notin_actuales est_notin_actuales
            INNER JOIN centros_educativos.sg_secciones sec ON (est_notin_actuales.sec_pk = sec.sec_pk)
            INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)
            INNER JOIN centros_educativos.sg_grados gra ON (se.sdu_grado_fk = gra.gra_pk)
            INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)
            INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)
            INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)  
            INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)
            INNER JOIN centros_educativos.sg_organizaciones_curricular ocu ON (niv.niv_organizacion_curricular = ocu.ocu_pk)
            where ocu.ocu_aplica_alertas_tempranas and cic.cic_pk = alertas_desempenio.cnf_ciclo_fk and 
                        est_notin_actuales.eca_minimo_aprobacion > est_notin_actuales.nota_institucional
            group by est_notin_actuales.cae_estudiante_fk
            having count(*) >= LEAST(alertas_desempenio.cnf_riesgo_muy_alto, alertas_desempenio.cnf_riesgo_alto, alertas_desempenio.cnf_riesgo_medio);  
            end if; 
        END LOOP;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION alertas.procesar_alertas() RETURNS void AS $$
DECLARE
unlocked boolean;
err_context text;
lock_id INTEGER DEFAULT 78;
alerta_cnf alertas.sg_config_alerta;
alerta_cnf_id INTEGER DEFAULT 1;
    BEGIN
        unlocked := (SELECT pg_try_advisory_lock(lock_id));
        if (unlocked = false) then
            RAISE NOTICE 'Procesar alertas: Locked. Skipping...';
            return;
        end if;
    ALTER SEQUENCE alertas.sg_alertas_pk_seq RESTART WITH 1; 
    TRUNCATE alertas.sg_alertas;
        
    SELECT * INTO alerta_cnf from alertas.sg_config_alerta where cnf_pk = alerta_cnf_id;
    
        PERFORM alertas.procesar_alertas_madre(alerta_cnf);
        PERFORM alertas.procesar_alertas_embarazo(alerta_cnf);
    PERFORM alertas.procesar_alertas_acompaniado(alerta_cnf);
        PERFORM alertas.procesar_alertas_matrimonio(alerta_cnf);
        PERFORM alertas.procesar_alertas_asistencia(alerta_cnf_id);
        PERFORM alertas.procesar_alertas_sobreedad(alerta_cnf_id);
        PERFORM alertas.procesar_alertas_manif_violencia();
        PERFORM alertas.procesar_alertas_trabajo();
        PERFORM alertas.procesar_alertas_desempenio(alerta_cnf_id);
            
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

-- 3.43.0

ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_integrada boolean default false;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_integrada boolean;

-- 3.43.1
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes ALTER COLUMN acp_resultado type text;
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes_aud ALTER COLUMN acp_resultado type text;

--3.44.0
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD COLUMN cpg_parametro_formula_aprobacion bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado_aud ADD COLUMN cpg_parametro_formula_aprobacion bigint;
ALTER TABLE centros_educativos.sg_componente_plan_grado ADD CONSTRAINT sg_cpg_parametro_formula_aprobacion FOREIGN KEY (cpg_parametro_formula_aprobacion) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--3.44.3
ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_sello_firma_titular_ministro_fk bigint;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_sello_firma_titular_ministro_fk bigint;
ALTER TABLE centros_educativos.sg_titulo ADD CONSTRAINT sg_tit_sello_firma_titular_ministro_fk FOREIGN KEY (tit_sello_firma_titular_ministro_fk) REFERENCES centros_educativos.sg_sello_firma_titular (sft_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_titulo_entregado_departamental boolean;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_titulo_entregado_departamental boolean;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_fecha_entregado_departamental date;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_fecha_entregado_departamental date;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_titulo_entregado_centro_educativo boolean;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_titulo_entregado_centro_educativo boolean;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_fecha_entregado_centro_educativo date;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_fecha_entregado_centro_educativo date;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_SOLICITUD_IMPRESION_ENTREGADO_DEPARTAMENTAL','EP75',  'Permite editar el dato entregado departamental en sol impresion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_SOLICITUD_IMPRESION_ENTREGADO_CENTRO','EP76',  'Permite editar el dato entregado centro educativo en sol impresion', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_estudiante_impresion ADD constraint estudiante_solicitud_uk UNIQUE (eim_solicitud_impresion_fk,eim_estudiante_fk);

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_nombre_titular character varying(255);
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_nombre_titular character varying(255);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SELLOS_FIRMAS_AUTENTICA','EM70',  'MENU: Sellos y firmas de auténtica', 1, true, null, null,0);

--3.44.4
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_CALIFICACION_POR_ESTUDIANTE_PAES','EM71',  'MENU: Calificación por estudiante PAES', 1, true, null, null,0);


--3.44.10

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('MENSAJE_PROMEDIO_CALIFICACIONES_SEDE_PORTAL', 'Mensaje que se muestra en la pantall sede de Portal debajo de promedio calificaciones', 'mensaje que se muestra en la pantall sede de portal debajo de promedio calificaciones', '2019-10-10 14:43:24.140', 'casuser', 0, 'Seleccione un grado para visualizar los promedios');

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_SWING_TITULOS','W15',  '', 4, true, null, null,0);


ALTER TABLE centros_educativos.sg_periodos_calificacion ADD COLUMN pca_es_prueba boolean default false;
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud ADD COLUMN pca_es_prueba boolean;

--3.44.11
update centros_educativos.sg_solicitudes_impresion set sim_estado='PENDIENTE_IMPRESION' where sim_estado='GENERADA';

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_impresora character varying(255);
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_impresora character varying(255);


--3.44.13 
INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('MAXIMA_EDAD_PERMITIDA_ALTA_ESTUDIANTES', 'Máxima edad permitida al dar de alta estudiantes', 'maxima edad permitida al dar de alta estudiantes', '2019-10-10 14:43:24.140', 'casuser', 0, '30');

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('MAXIMA_EDAD_PERMITIDA_ALTA_PERSONAL', 'Máxima edad permitida al dar de alta personal', 'maxima edad permitida al dar de alta personal', '2019-10-10 14:43:24.140', 'casuser', 0, '60');

--3.44.14
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_CALIFICACION_PERIODO_COMPONENTE_TIPO_PRUEBA','EP77',  'Permite en la pantalla calificación período ver las componentes de tiṕo prueba', 1, true, null, null,0);

--3.45.0
UPDATE catalogo.sg_configuraciones set con_codigo = 'MAXIMA_EDAD_PERMITIDA_ALTA_PERSONA',
con_nombre='Máxima edad permitida al dar de alta persona',
con_nombre_busqueda='maxima edad permitida al dar de alta persona'
 where con_codigo = 'MAXIMA_EDAD_PERMITIDA_ALTA_PERSONAL';

-- 3.46.0
ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_asociacion_fk bigint;
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_asociacion_fk bigint;
ALTER TABLE centros_educativos.sg_secciones ADD CONSTRAINT sg_seccion_asociacion_fk FOREIGN KEY (sec_asociacion_fk) REFERENCES catalogo.sg_asociaciones(aso_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_secciones ADD COLUMN sec_regimen CHARACTER VARYING(20);
ALTER TABLE centros_educativos.sg_secciones_aud ADD COLUMN sec_regimen CHARACTER VARYING(20);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_MATRICULAS_EN_SECCION','EB88',  'Buscar matriculas en sección', 1, true, null, null,0);

--3.46.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_CALIFICACIONES_IMPORTADAS','E386',  'Permite prococesar calificaciones importadas', 1, true, null, null,0);

--3.48.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_CALIFICACIONES_PAES','E387',  'Permite prococesar calificaciones importadas PAES', 1, true, null, null,0);

select ope_pk from seguridad.sg_operaciones where ope_codigo = 'PUEDE_PROCESAR_ARCHIVO_PAES';

DELETE FROM seguridad.sg_roles_operacion where rop_operacion_fk = (select ope_pk from seguridad.sg_operaciones where ope_codigo = 'EP58');
DELETE FROM seguridad.sg_operaciones  where ope_codigo = 'EP58';
ALTER TABLE centros_educativos.sg_archivo_calificaciones ADD COLUMN acc_error_log text;
COMMENT ON COLUMN centros_educativos.sg_archivo_calificaciones.acc_error_log IS 'Error que genera log';
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ADD COLUMN acc_error_log text;

ALTER TABLE centros_educativos.sg_archivo_calificaciones ALTER COLUMN  acc_descripcion SET DATA TYPE text;
ALTER TABLE centros_educativos.sg_archivo_calificaciones_aud ALTER COLUMN  acc_descripcion SET DATA TYPE text;

CREATE UNIQUE INDEX sg_calificacion_nota_institucional_aprobacion_uk ON centros_educativos.sg_calificaciones (cal_seccion_fk,cal_componente_plan_estudio_fk,cal_tipo_periodo_calificacion)
WHERE cal_tipo_periodo_calificacion in ('NOTIN','APR');

create unique index sg_calificacion_promocion_uk on centros_educativos.sg_calificaciones (cal_seccion_fk) where cal_tipo_periodo_calificacion='GRA';

ALTER TABLE centros_educativos.sg_calificaciones ALTER COLUMN cal_seccion_fk SET NOT NULL;

CREATE INDEX sg_componente_plan_grado_exc_sec_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_exclusivo_seccion);
CREATE INDEX sg_areas_indicador_fk_idx   ON centros_educativos.sg_areas (ind_area_indicador_fk);
CREATE INDEX sg_asignaturas_area_modulo_fk_idx   ON centros_educativos.sg_asignaturas (asig_area_asignatura_modulo_fk);
CREATE INDEX sg_modulos_area_asignatura_fk_idx   ON centros_educativos.sg_modulos (mod_area_asignatura_modulo_fk);


--3.49.2 
ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_estado_procesamiento_promocion CHARACTER VARYING(20);
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_estado_procesamiento_promocion CHARACTER VARYING(20);
CREATE INDEX sg_comp_plan_grado_formula_pp_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_formula_habilitacion_pp_fk);
CREATE INDEX sg_comp_plan_grado_formula_ps_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_formula_habilitacion_ps_fk);
CREATE INDEX sg_comp_plan_grado_formula_sp_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_formula_habilitacion_sp_fk);
CREATE INDEX sg_comp_plan_grado_formula_ss_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_formula_habilitacion_ss_fk);
CREATE INDEX sg_comp_plan_grado_formula_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_formula_fk);



CREATE INDEX sg_comp_plan_grado_param_formula_pp_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_parametro_formula_prueba_pp);
CREATE INDEX sg_comp_plan_grado_param_formula_ps_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_parametro_formula_prueba_ps);
CREATE INDEX sg_comp_plan_grado_param_formula_sp_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_parametro_formula_prueba_sp);
CREATE INDEX sg_comp_plan_grado_param_formula_ss_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_parametro_formula_prueba_ss);
CREATE INDEX sg_comp_plan_grado_param_formula_apr_fk_idx   ON centros_educativos.sg_componente_plan_grado (cpg_parametro_formula_aprobacion);


-- Estructura calificación paes
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_estructura_calificacion_paes_ecp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estructura_calificacion_paes (ecp_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_estructura_calificacion_paes_ecp_pk_seq'::regclass), ecp_nie character varying(45), ecp_calificacion character varying(45), ecp_codigo_cpe character varying(255),ecp_archivo_calificacion_paes_fk bigint, ecp_ult_mod_fecha timestamp without time zone, ecp_ult_mod_usuario character varying(45), ecp_version integer, CONSTRAINT sg_estructura_calificacion_paes_pkey PRIMARY KEY (ecp_pk),CONSTRAINT sg_estructura_calificacion_paes_archivo_calificacion_paes_fk FOREIGN KEY (ecp_archivo_calificacion_paes_fk) REFERENCES centros_educativos.sg_archivo_calificacion_paes(acp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_estructura_calificacion_paes IS 'Tabla para el registro de estructura calificación paes.';
COMMENT ON COLUMN centros_educativos.sg_estructura_calificacion_paes.ecp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_estructura_calificacion_paes.ecp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_estructura_calificacion_paes.ecp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_estructura_calificacion_paes.ecp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_estructura_calificacion_paes_aud (ecp_pk bigint NOT NULL, ecp_nie character varying(45), ecp_calificacion character varying(45), ecp_codigo_cpe character varying(255), ecp_archivo_calificacion_paes_fk bigint,ecp_ult_mod_fecha timestamp without time zone, ecp_ult_mod_usuario character varying(45), ecp_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes_aud ADD PRIMARY KEY (ecp_pk, rev);

ALTER TABLE centros_educativos.sg_estructura_calificacion_paes ADD COLUMN ecp_estado CHARACTER VARYING(20);
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes_aud ADD COLUMN ecp_estado CHARACTER VARYING(20);

ALTER TABLE centros_educativos.sg_estructura_calificacion_paes ADD COLUMN ecp_resultado CHARACTER VARYING(500);
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes_aud ADD COLUMN ecp_resultado CHARACTER VARYING(500);

ALTER TABLE centros_educativos.sg_archivo_calificacion_paes ALTER COLUMN acp_rango_fecha_fk DROP NOT NULL ;
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes_aud ALTER COLUMN acp_rango_fecha_fk DROP NOT NULL ;


ALTER TABLE centros_educativos.sg_archivo_calificacion_paes DROP CONSTRAINT sg_archivo_calificacion_paes_rango_fk ;
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes DROP CONSTRAINT sg_archivo_calificacion_paes_periodo_fk ;

create unique index sg_calificacion_paes_uk on centros_educativos.sg_calificaciones (cal_seccion_fk,cal_componente_plan_estudio_fk) where cal_tipo_periodo_calificacion='PAES';

--3.50.1

ALTER TABLE centros_educativos.sg_estructura_calificacion_paes ALTER COLUMN ecp_resultado TYPE varchar(500);
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes_aud ALTER COLUMN ecp_resultado TYPE varchar(500);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ESTRUCTURA_CALIFICACION_PAES','EM73',  'MENU: Estructura calificación PAES', 1, true, null, null,0);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ESTRUCTURA_CALIFICACION_PAES','E388',  'crear estructura calificación paes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ESTRUCTURA_CALIFICACION_PAES','E389',  'actualizar estructura calificación paes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ESTRUCTURA_CALIFICACION_PAES','E390',  'eliminar estructura calificación paes', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_archivo_calificacion_paes add COLUMN acp_anio integer default 2019;
ALTER TABLE centros_educativos.sg_archivo_calificacion_paes_aud add COLUMN acp_anio integer;

ALTER TABLE centros_educativos.sg_estructura_calificacion_paes ADD COLUMN ecp_persona_fk bigint;
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes_aud ADD COLUMN ecp_persona_fk bigint;
ALTER TABLE centros_educativos.sg_estructura_calificacion_paes ADD CONSTRAINT sg_ecp_persona_fk FOREIGN KEY (ecp_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--3.52.0
CREATE INDEX sg_sol_traslado_fecha_sol_idx  ON centros_educativos.sg_solicitudes_traslado (sot_fecha_solicitud);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EJECUTAR_PROCESAMIENTO_PROMOCIONES','E391',  'Renderiza el botón de procesamiento promociones', 1, true, null, null,0);
ALTER TABLE centros_educativos.sg_calificaciones ALTER COLUMN cal_fecha DROP NOT NULL ;
ALTER TABLE centros_educativos.sg_calificaciones_aud ALTER COLUMN cal_fecha DROP NOT NULL ;


-- 3.53.0


ALTER TABLE centros_educativos.sg_sello_firma_titular     DROP COLUMN sft_sello_archivo_fk;
ALTER TABLE centros_educativos.sg_sello_firma_titular_aud DROP COLUMN sft_sello_archivo_fk;

ALTER TABLE centros_educativos.sg_sello_firma_titular     RENAME COLUMN sft_firma_archivo_fk TO sft_firma_sello_archivo_fk;
ALTER TABLE centros_educativos.sg_sello_firma_titular_aud RENAME COLUMN sft_firma_archivo_fk TO sft_firma_sello_archivo_fk;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('GENERAR_TITULO_ESTUDIANTE','R1', '', 5, true, null, null,0);


-- 3.56.1


CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_matricula_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_matricula_fk = new.mat_pk where est_pk = new.mat_estudiante_fk;
    END IF;
    IF new.mat_anulada THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_matricula_fk = (select mat_pk from centros_educativos.sg_matriculas m INNER JOIN centros_educativos.sg_estudiantes e on (m.mat_estudiante_fk = e.est_pk) where e.est_pk = new.mat_estudiante_fk and m.mat_anulada = false order by m.mat_fecha_ingreso desc, m.mat_pk desc limit 1)
        where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;


CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_sede_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_sede_fk = (select sed_pk from centros_educativos.sg_sedes INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) where sec_pk = new.mat_seccion_fk) 
        where est_pk = new.mat_estudiante_fk;
    END IF;
    IF new.mat_anulada THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_sede_fk = (select sed_pk from centros_educativos.sg_sedes INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) where sec_pk = (select mat_seccion_fk from centros_educativos.sg_matriculas m INNER JOIN centros_educativos.sg_estudiantes e on (m.mat_estudiante_fk = e.est_pk) where e.est_pk = new.mat_estudiante_fk and m.mat_anulada = false order by m.mat_fecha_ingreso desc, m.mat_pk desc limit 1)) 
        where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;

CREATE OR REPLACE FUNCTION centros_educativos.update_ultima_seccion_estudiante() RETURNS TRIGGER AS
$BODY$
BEGIN
    IF new.mat_estado = 'ABIERTO' THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_seccion_fk = (new.mat_seccion_fk) 
        where est_pk = new.mat_estudiante_fk;
    END IF;
    IF new.mat_anulada THEN
        UPDATE centros_educativos.sg_estudiantes set est_ultima_seccion_fk = (select mat_seccion_fk from centros_educativos.sg_matriculas m INNER JOIN centros_educativos.sg_estudiantes e on (m.mat_estudiante_fk = e.est_pk) where e.est_pk = new.mat_estudiante_fk and m.mat_anulada = false order by m.mat_fecha_ingreso desc, m.mat_pk desc limit 1)
        where est_pk = new.mat_estudiante_fk;
    END IF;
    RETURN new;
END;
$BODY$
language plpgsql;


-- 3.57.0

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_pregunta_cierre_anio_sede_rpc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_pregunta_cierre_anio_sede (
rpc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_pregunta_cierre_anio_sede_rpc_pk_seq'::regclass),
 rpc_cierre_anio_lectivo_sede_fk bigint,
 rpc_pregunta_cierre_anio_fk bigint,
 rpc_pregunta_validada boolean,
 rpc_ult_mod_fecha timestamp without time zone,
 rpc_ult_mod_usuario character varying(45),
 rpc_version integer);

CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_pregunta_cierre_anio_sede_aud (
rpc_pk bigint NOT NULL,
 rpc_cierre_anio_lectivo_sede_fk bigint,
 rpc_pregunta_cierre_anio_fk bigint,
 rpc_pregunta_validada boolean,
 rpc_ult_mod_fecha timestamp without time zone,
 rpc_ult_mod_usuario character varying(45),
 rpc_version integer,
 rev bigint,
 revtype smallint);

ALTER TABLE centros_educativos.sg_rel_pregunta_cierre_anio_sede
ADD CONSTRAINT sg_rel_pregunta_cierre_anio_sede_pregunta_fk  FOREIGN KEY (rpc_pregunta_cierre_anio_fk) REFERENCES catalogo.sg_pregunta_cierre_anio(pca_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_rel_pregunta_cierre_anio_sede
ADD CONSTRAINT sg_rel_pregunta_cierre_anio_sede_cierre_fk  FOREIGN KEY (rpc_cierre_anio_lectivo_sede_fk) REFERENCES centros_educativos.sg_cierre_anio_lectivo_sede(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


create unique index sg_cierre_anio_sede_uk on centros_educativos.sg_cierre_anio_lectivo_sede (cal_ale_fk, cal_sed_fk);

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede
DROP CONSTRAINT sg_cierre_anio_lectivo_sede_sg_anio_lectivo_fk;

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede
ADD CONSTRAINT sg_cierre_anio_lectivo_sede_sg_anio_lectivo_fk  FOREIGN KEY (cal_ale_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_fecha_validez date;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_fecha_validez date;

UPDATE  centros_educativos.sg_estudiantes set est_ult_mod_usuario = CONCAT('SCRULT1--', est_ult_mod_usuario)
where est_pk IN (
	select est_pk from centros_educativos.sg_estudiantes e where est_ultima_matricula_fk != 
	(select mat_pk from centros_educativos.sg_matriculas m 
 	where m.mat_estudiante_fk = e.est_pk and m.mat_anulada = false 
 	order by mat_fecha_ingreso desc, mat_pk desc limit 1)
);

UPDATE  centros_educativos.sg_estudiantes e set est_ultima_matricula_fk = 
(select mat_pk from centros_educativos.sg_matriculas m 
 where m.mat_estudiante_fk = e.est_pk and m.mat_anulada = false 
 order by mat_fecha_ingreso desc, mat_pk desc limit 1)
where est_ult_mod_usuario like 'SCRULT1--%'
;

UPDATE  centros_educativos.sg_estudiantes e set est_ultima_sede_fk = (
select sed_pk from centros_educativos.sg_sedes 
INNER JOIN centros_educativos.sg_servicio_educativo ON (sed_pk = sdu_sede_fk) 
INNER JOIN centros_educativos.sg_secciones on (sdu_pk = sec_servicio_educativo_fk) 
INNER JOIN centros_educativos.sg_matriculas ON (mat_seccion_fk = sec_pk) where mat_pk = e.est_ultima_matricula_fk)
where est_ult_mod_usuario like 'SCRULT1--%';

UPDATE  centros_educativos.sg_estudiantes e set est_ultima_seccion_fk = (
    select mat_seccion_fk from centros_educativos.sg_matriculas where mat_pk = e.est_ultima_matricula_fk
)
where est_ult_mod_usuario like 'SCRULT1--%';


UPDATE centros_educativos.sg_estudiantes e set est_ult_mod_usuario = REPLACE(est_ult_mod_usuario, 'SCRULT1--', '')
where est_ult_mod_usuario like 'SCRULT1--%';

-- 3.58.0

UPDATE catalogo.sg_configuraciones set con_codigo = 'MENSAJE_CIERRE_SECCION' where con_codigo = 'MENSAJE_CIERRE_ANIO';

INSERT INTO catalogo.sg_configuraciones("con_codigo", "con_nombre", "con_valor", "con_nombre_busqueda", "con_ult_mod_fecha", "con_ult_mod_usuario", "con_version") VALUES ('MENSAJE_CIERRE_ANIO', 'Mensaje cierre de año', '[EDIT MSG]', 'mensaje cierre de ano', CURRENT_TIMESTAMP, 'admin', '0');


ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ALTER COLUMN cal_ale_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ALTER COLUMN cal_sed_fk SET NOT NULL;


ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_promociones_pendientes_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_promociones_pendientes_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_promovidos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_promovidos_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_no_promovidos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_no_promovidos_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_asistencias integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_inasistencias integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_inasistencias_just integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_solicitudes_titulos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede ADD COLUMN cal_solicitudes_titulos_fem integer;

ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_promociones_pendientes_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_promociones_pendientes_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_promovidos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_promovidos_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_no_promovidos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_no_promovidos_fem integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_asistencias integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_inasistencias integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_inasistencias_just integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_solicitudes_titulos_masc integer;
ALTER TABLE centros_educativos.sg_cierre_anio_lectivo_sede_aud ADD COLUMN cal_solicitudes_titulos_fem integer;

CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_resumen_cierre_anio_seccion_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_resumen_cierre_anio_seccion (
 rcs_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_resumen_cierre_anio_seccion_pk_seq'::regclass),
 rcs_cierre_anio_sede_fk bigint,
 rcs_seccion_fk bigint,
 rcs_promociones_pendientes_masc integer,
 rcs_promociones_pendientes_fem integer,
 rcs_promovidos_masc integer,
 rcs_promovidos_fem integer,
 rcs_no_promovidos_masc integer,
 rcs_no_promovidos_fem integer,
 rcs_asistencias integer,
 rcs_inasistencias integer,
 rcs_inasistencias_just integer,
 rcs_solicitudes_titulos_masc integer,
 rcs_solicitudes_titulos_fem integer,
 rcs_ult_mod_fecha timestamp without time zone,
 rcs_ult_mod_usuario character varying(45),
 rcs_version integer);




CREATE TABLE IF NOT EXISTS centros_educativos.sg_resumen_cierre_anio_seccion_aud (
rcs_pk bigint NOT NULL,
rcs_cierre_anio_sede_fk bigint,
 rcs_seccion_fk bigint,
 rcs_promociones_pendientes_masc integer,
 rcs_promociones_pendientes_fem integer,
 rcs_promovidos_masc integer,
 rcs_promovidos_fem integer,
 rcs_no_promovidos_masc integer,
 rcs_no_promovidos_fem integer,
 rcs_asistencias integer,
 rcs_inasistencias integer,
 rcs_inasistencias_just integer,
 rcs_solicitudes_titulos_masc integer,
 rcs_solicitudes_titulos_fem integer,
 rcs_ult_mod_fecha timestamp without time zone,
 rcs_ult_mod_usuario character varying(45),
 rcs_version integer,
 rev bigint,
 revtype smallint);

ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion ALTER COLUMN rcs_cierre_anio_sede_fk SET NOT NULL;
ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion ALTER COLUMN rcs_seccion_fk SET NOT NULL;

update centros_educativos.sg_secciones set sec_version = 0 where sec_version is null;

ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion_aud ADD PRIMARY KEY (rcs_pk, rev);
ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion_aud  ADD CONSTRAINT sg_resumen_cierre_anio_seccion_revinfo_fk  FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion
ADD CONSTRAINT sg_resumen_cierre_anio_seccion_cierre_sede_fk  FOREIGN KEY (rcs_cierre_anio_sede_fk) REFERENCES centros_educativos.sg_cierre_anio_lectivo_sede(cal_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_resumen_cierre_anio_seccion
ADD CONSTRAINT sg_resumen_cierre_anio_seccion_seccion_fk  FOREIGN KEY (rcs_seccion_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

--3.59.2
CREATE UNIQUE INDEX sg_calificacion_nota_institucional_aprobacion_all_uk ON centros_educativos.sg_calificaciones (cal_seccion_fk,cal_componente_plan_estudio_fk,cal_tipo_periodo_calificacion)
WHERE cal_tipo_periodo_calificacion in ('NOTIN','APR');

ALTER TABLE centros_educativos.sg_anio_lectivo ADD COLUMN ale_corriente boolean default false;
ALTER TABLE centros_educativos.sg_anio_lectivo_aud ADD COLUMN ale_corriente boolean;

UPDATE centros_educativos.sg_anio_lectivo set ale_corriente = true where ale_anio = 2019;
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion_ipc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion (ipc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_info_procesamiento_calificacion_ipc_pk_seq'::regclass), ipc_error text,ipc_ult_mod_fecha timestamp without time zone, ipc_ult_mod_usuario character varying(45), ipc_version integer, CONSTRAINT sg_info_procesamiento_calificacion_ipc_pkey PRIMARY KEY (ipc_pk));
COMMENT ON TABLE centros_educativos.sg_info_procesamiento_calificacion IS 'Tabla para el registro de informacion de procesamiento de calificacion.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion.ipc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion.ipc_error IS 'Error de procesamiento de calificacion';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion.ipc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion.ipc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion.ipc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion_aud (ipc_pk bigint NOT NULL, ipc_error text,ipc_ult_mod_fecha timestamp without time zone, ipc_ult_mod_usuario character varying(45), ipc_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_info_procesamiento_calificacion_aud ADD PRIMARY KEY (ipc_pk, rev);

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_info_procesamiento_calificacion_fk bigint;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_info_procesamiento_calificacion_fk bigint;
ALTER TABLE centros_educativos.sg_calificaciones ADD CONSTRAINT sg_cal_info_procesamiento_calificacion_fk FOREIGN KEY (cal_info_procesamiento_calificacion_fk) REFERENCES centros_educativos.sg_info_procesamiento_calificacion(ipc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion_est_ipe_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion_est (ipe_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_info_procesamiento_calificacion_est_ipe_pk_seq'::regclass), ipe_error text,ipe_ult_mod_fecha timestamp without time zone, ipe_ult_mod_usuario character varying(45), ipe_version integer, CONSTRAINT sg_info_procesamiento_calificacion_est_ipe_pkey PRIMARY KEY (ipe_pk));
COMMENT ON TABLE centros_educativos.sg_info_procesamiento_calificacion_est IS 'Tabla para el registro de informacion de procesamiento de calificacion.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_error IS 'Error de procesamiento de calificacion';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_info_procesamiento_calificacion_est_aud (ipe_pk bigint NOT NULL, ipe_error text,ipe_ult_mod_fecha timestamp without time zone, ipe_ult_mod_usuario character varying(45), ipe_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_info_procesamiento_calificacion_est_aud ADD PRIMARY KEY (ipe_pk, rev);

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD COLUMN cae_info_procesamiento_calificacion_est_fk bigint;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante_aud ADD COLUMN cae_info_procesamiento_calificacion_est_fk bigint;
ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD CONSTRAINT sg_cae_info_procesamiento_calificacion_est_fk FOREIGN KEY (cae_info_procesamiento_calificacion_est_fk) REFERENCES centros_educativos.sg_info_procesamiento_calificacion_est(ipe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_info_procesamiento_calificacion_est ADD COLUMN ipe_promocion_pendiente boolean;
COMMENT ON COLUMN centros_educativos.sg_info_procesamiento_calificacion_est.ipe_promocion_pendiente IS 'Promoción calificado pendiente.';
ALTER TABLE centros_educativos.sg_info_procesamiento_calificacion_est_aud ADD COLUMN ipe_promocion_pendiente boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_PONER_EN_PENDIENTE_PROMOCIONES','EP78',  'Permite ver el botón de pasar a pendiente en la pantalla promocion', 1, true, null, null,0);

--3.61.7

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_NOMINA_DOCENTE','EM74',  'MENU: opción para consulta de nóminas docentes', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_NOMINAS_DOCENTES','EB89',  'Buscar nóminas docentes', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_TITULOS','EM75',  'MENU: opción para ver títulos', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_nombre_estudiante_busqueda CHARACTER VARYING(255);
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_nombre_estudiante_busqueda CHARACTER VARYING(255);


update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=lower(tit_nombre_estudiante);
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'ñ','n');
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'á','a');
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'é','e');
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'í','i');
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'ó','o');
update centros_educativos.sg_titulo tit set tit_nombre_estudiante_busqueda=replace(tit_nombre_estudiante_busqueda,'ú','u');

--3.61.8
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PERMITE_MATRICULAR_SIN_NIE','E392',  'En método de matriculación tradicional permite matricular sin nie', 1, true, null, null,0);

--3.62.0
ALTER TABLE centros_educativos.sg_niveles ADD COLUMN niv_nombre_seccion_libre boolean default false;
ALTER TABLE centros_educativos.sg_niveles_aud ADD COLUMN niv_nombre_seccion_libre boolean;

--3.62.3
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CALIFICACIONES_SUPERUSUARIO','E393',  'Permite calificar períodos no habilitados', 1, true, null, null,0);

--3.36.0
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_miembros_vigentes boolean;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_miembros_vigentes boolean;

CREATE UNIQUE INDEX sg_rel_mod_ed_mod_aten_sin_sub_mod_uk ON centros_educativos.sg_rel_mod_ed_mod_aten (rea_modalidad_educativa_fk,rea_modalidad_atencion_fk)
WHERE  rea_sub_modalidad_atencion_fk is null;

CREATE UNIQUE INDEX sg_rel_mod_ed_mod_aten_con_sub_mod_uk ON centros_educativos.sg_rel_mod_ed_mod_aten (rea_modalidad_educativa_fk,rea_modalidad_atencion_fk,rea_sub_modalidad_atencion_fk)
WHERE  rea_sub_modalidad_atencion_fk is not null;

--3.64.0
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_miembros_vigentes boolean;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_miembros_vigentes boolean;

ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_prerregistro boolean;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_persona_fk bigint;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD CONSTRAINT sg_persona_organismo_administracion_persona_fkey FOREIGN KEY (poa_persona_fk) REFERENCES centros_educativos.sg_personas(per_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_prerregistro boolean;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_persona_fk bigint;


ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ALTER COLUMN oae_acta_integracion TYPE CHARACTER VARYING(10);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ALTER COLUMN oae_acta_integracion TYPE CHARACTER VARYING(10);


ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN IF NOT EXISTS poa_nit character varying(20);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN IF NOT EXISTS poa_correo character varying(100);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN IF NOT EXISTS poa_telefono character varying(10);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN IF NOT EXISTS poa_desde date;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN IF NOT EXISTS poa_hasta date;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN IF NOT EXISTS poa_nit character varying(20);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN IF NOT EXISTS poa_correo character varying(100);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN IF NOT EXISTS poa_telefono character varying(10);
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN IF NOT EXISTS poa_desde date;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN IF NOT EXISTS poa_hasta date;


ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN IF NOT EXISTS oae_numero_acuerdo character varying(20);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN IF NOT EXISTS oae_fecha_acuerdo date;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN IF NOT EXISTS oae_ampliar_datos character varying(255);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN IF NOT EXISTS oae_motivo_rechazo character varying(255);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN IF NOT EXISTS oae_numero_acuerdo character varying(20);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN IF NOT EXISTS oae_fecha_acuerdo date;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN IF NOT EXISTS oae_ampliar_datos character varying(255);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN IF NOT EXISTS oae_motivo_rechazo character varying(255);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR','EB72',  'Organismo de Administración Escolar: buscar', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar RENAME COLUMN oea_estado TO oae_estado;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud RENAME COLUMN oea_estado TO oae_estado;


ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_tipo_organismo_administrativo_fk bigint;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_tipo_organismo_administrativo_fk bigint;

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar
ADD CONSTRAINT sg_oae_tipo_fk  FOREIGN KEY (oae_tipo_organismo_administrativo_fk) REFERENCES catalogo.sg_tipos_organismo_administrativo(toa_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_nombre character varying(255);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_nombre character varying(255);

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_fecha_legalizacion date;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_fecha_legalizacion date;

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_fecha_cierre date;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_fecha_cierre date;

ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ADD COLUMN oae_acuerdo_cierre character varying(255);
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ADD COLUMN oae_acuerdo_cierre character varying(255);


--3.66.0
ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_generada_equivalencia boolean;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_generada_equivalencia boolean;

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_numero_tramite character varying (255);
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_numero_tramite character varying (255);

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_anio integer;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_anio integer;

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_grado bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_grado bigint;    

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_opcion bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_opcion bigint;    

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_programa bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_programa bigint;       

    
ALTER TABLE centros_educativos.sg_escolaridad_estudiante
ADD CONSTRAINT sg_escolaridad_grado_fk  FOREIGN KEY (esc_eq_grado) REFERENCES centros_educativos.sg_grados(gra_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_escolaridad_estudiante
ADD CONSTRAINT sg_escolaridad_programa_fk  FOREIGN KEY (esc_eq_programa) REFERENCES catalogo.sg_programas_educativos(ped_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_escolaridad_estudiante
ADD CONSTRAINT sg_escolaridad_opcion_fk  FOREIGN KEY (esc_eq_opcion) REFERENCES centros_educativos.sg_opciones(opc_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


CREATE UNIQUE INDEX sg_escolaridad_eq_numero_tramite_uk ON centros_educativos.sg_escolaridad_estudiante (esc_eq_numero_tramite);

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_opcion_bachillerato character varying(200);
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_opcion_bachillerato character varying(200);

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_titulo_anterior_2008_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_titulo_anterior_2008_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo  ADD CONSTRAINT sg_ret_titulo_anterior_2008_fk  FOREIGN KEY (ret_titulo_anterior_2008_fk) REFERENCES catalogo.sg_titulo_anterior(tan_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_dui_solicitante CHARACTER VARYING(20) ;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_dui_solicitante CHARACTER VARYING(20) ;

ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_fecha_legalizacion_titulo date;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_fecha_legalizacion_titulo date;


ALTER TABLE centros_educativos.sg_reposicion_titulo ADD COLUMN ret_titulo_anterior_2008_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo_aud ADD COLUMN ret_titulo_anterior_2008_fk bigint;
ALTER TABLE centros_educativos.sg_reposicion_titulo  ADD CONSTRAINT sg_ret_titulo_anterior_2008_fk  FOREIGN KEY (ret_titulo_anterior_2008_fk) REFERENCES catalogo.sg_titulo_anterior(tan_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_dui_estudiante CHARACTER VARYING(20) ;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_dui_estudiante CHARACTER VARYING(20) ;

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_fecha_legalizacion_titulo date;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_fecha_legalizacion_titulo date;

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_titulo_anterior_2008 character varying(255);
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_titulo_anterior_2008 character varying(255);

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_numero_resolucion integer;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_numero_resolucion integer;

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_numero_registro character varying(100);
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_numero_registro character varying(100);


ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_resolucion_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_resolucion_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_impresion  ADD CONSTRAINT sg_sim_resolucion_fk FOREIGN KEY (sim_resolucion_fk) REFERENCES sg_archivos(ach_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_reposicion_estudiante_siges boolean default false;
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_reposicion_estudiante_siges boolean;


insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('TIPO_ARCHIVO_REPOSICION_TITULO', 'tipo de archivo de reposición de título','tipo de archivo de reposicion de titulo','/(\.|\/)(zip|pdf)$/');

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('TAMANIO_ARCHIVO_REPOSICION_TITULO', 'Tamaño permitido de archivo de reposición de título', 'Tamaño permitido de archivo de reposicion de titulo', 0, '1048576', false);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('REPOSICION_TITULO','EP79',  'Permite reponer titulo en pantalla de gestión de título', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_titulo ADD COLUMN tit_reposicion boolean;
ALTER TABLE centros_educativos.sg_titulo_aud ADD COLUMN tit_reposicion boolean;

ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_numero_registro character varying(100);
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_numero_registro character varying(100);

update centros_educativos.sg_solicitudes_impresion set sim_numero_registro = (select ret_numero_registro from centros_educativos.sg_reposicion_titulo
where ret_pk= sim_reposicion_titulo_fk);

ALTER TABLE centros_educativos.sg_reposicion_titulo DROP COLUMN ret_numero_registro;


ALTER TABLE centros_educativos.sg_solicitudes_impresion ADD COLUMN sim_numero_resolucion character varying(100);
ALTER TABLE centros_educativos.sg_solicitudes_impresion_aud ADD COLUMN sim_numero_resolucion character varying(100);

update centros_educativos.sg_solicitudes_impresion set sim_numero_resolucion = (select ret_numero_resolucion from centros_educativos.sg_reposicion_titulo
where ret_pk= sim_reposicion_titulo_fk);

ALTER TABLE centros_educativos.sg_reposicion_titulo DROP COLUMN ret_numero_resolucion;

-- 3.67.0

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_nombre_sede character varying(200);
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_nombre_sede character varying(200);


ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_sexo bigint;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_sexo bigint;


--3.68.0
ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_nombre_sede character varying(200);
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_nombre_sede character varying(200);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('REABRIR_MATRICULA','E400',  'Permite cambiar estado abierto en matrícula', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_persona_organismo_administracion ADD COLUMN poa_sexo bigint;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud ADD COLUMN poa_sexo bigint;

--3.68.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_DATOS_ENTREGA_GENERACION_TITULO','EP80',  'Permite editar los datos entregado sede y departamento en generacion titulo', 1, true, null, null,0);

--3.69.0

ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_plan_estudio bigint;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_plan_estudio bigint;       

    
ALTER TABLE centros_educativos.sg_escolaridad_estudiante
ADD CONSTRAINT sg_escolaridad_plan_estudio_fk  FOREIGN KEY (esc_eq_plan_estudio) REFERENCES centros_educativos.sg_planes_estudio(pes_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_escolaridad_estudiante ADD COLUMN esc_eq_fecha_tramite date;
ALTER TABLE centros_educativos.sg_escolaridad_estudiante_aud ADD COLUMN esc_eq_fecha_tramite date;

--3.70.2
ALTER TABLE centros_educativos.sg_titulo add column tit_sec_fk bigint;
ALTER TABLE centros_educativos.sg_titulo_aud add column tit_sec_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_titulo.tit_sec_fk IS 'Relacion de titulo con seccion.';
ALTER TABLE centros_educativos.sg_titulo  ADD CONSTRAINT sg_tit_sec_fk FOREIGN KEY (tit_sec_fk) REFERENCES centros_educativos.sg_secciones(sec_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


--3.70.6
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio add column rgp_anual boolean default true;
ALTER TABLE centros_educativos.sg_rel_grado_plan_estudio_aud add column rgp_anual boolean;
COMMENT ON COLUMN centros_educativos.sg_rel_grado_plan_estudio.rgp_anual IS 'Secciones de grado y plan son anuales o  no.';

ALTER TABLE centros_educativos.sg_secciones add column sec_tipo_periodo character varying(25);
ALTER TABLE centros_educativos.sg_secciones_aud add column sec_tipo_periodo character varying(25);
COMMENT ON COLUMN centros_educativos.sg_secciones.sec_tipo_periodo IS 'TIpo de periodo de secciones (anual, semestral, etc).';

ALTER TABLE centros_educativos.sg_secciones add column sec_numero_periodo integer;
ALTER TABLE centros_educativos.sg_secciones_aud add column sec_numero_periodo integer;
COMMENT ON COLUMN centros_educativos.sg_secciones.sec_numero_periodo IS 'Numero de periodo al que pertenece la seccion.';

ALTER TABLE centros_educativos.sg_periodos_calificacion add column pca_tipo_periodo character varying(25);
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud add column pca_tipo_periodo character varying(25);
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_tipo_periodo IS 'Tipo de periodo (anual, semestral, etc).';

ALTER TABLE centros_educativos.sg_periodos_calificacion add column pca_numero_periodo integer;
ALTER TABLE centros_educativos.sg_periodos_calificacion_aud add column pca_numero_periodo integer;
COMMENT ON COLUMN centros_educativos.sg_periodos_calificacion.pca_numero_periodo IS 'Numero de periodo al que pertenece la seccion.';

CREATE INDEX sg_sec_tipo_periodo_fk_idx ON centros_educativos.sg_secciones USING btree (sec_tipo_periodo);
CREATE INDEX sg_sec_numero_periodo_fk_idx ON centros_educativos.sg_secciones USING btree (sec_numero_periodo);

update centros_educativos.sg_secciones set sec_tipo_periodo='ANUAL' where sec_tipo_periodo is null;
update centros_educativos.sg_periodos_calificacion set pca_tipo_periodo='ANUAL' where pca_tipo_periodo is null;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERIODO_ANUAL_COHORTE','EP81',  'Permite editar en pantalla de sección los datos de cohorte', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERIODO_ANUAL_COHORTE_EN_DATOS_DE_GRADO','EP82',  'Permite editar en datos de grado el check para activar anual', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_PERIODO_ANUAL_COHORTE_EN_PERIODO_CALIFICACION','EP83',  'Permite editar en pantalla periodo calificacion los datos de cohorte', 1, true, null, null,0);


--3.70.7
INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_CIERRE_MATRICULA_NACIONAL_COHORTE', 'Día y mes por defecto para el cierre de matrículas calendario nacional de primer cohorte', 'dia y mes por defecto para el cierre de matriculas calendario nacional de primer cohorte', '2019-08-08 14:43:24.140', 'casuser', 0, '01/06');

INSERT INTO catalogo.sg_configuraciones
(con_codigo, con_nombre, con_nombre_busqueda, con_ult_mod_fecha, con_ult_mod_usuario, con_version, con_valor)
VALUES('DIA_MES_CIERRE_MATRICULA_INT_COHORTE', 'Día y mes por defecto para el cierre de matrículas calendario internacional de primer cohorte', 'dia y mes por defecto para el cierre de matriculas calendario internacional de primer cohorte', '2019-08-08 14:43:24.140', 'casuser', 0, '01/02');

-- 3.71.0


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('HABILITADO_CALIFICACIONES_PAES','E401',  'Habilita las operaciones X_ARCHIVO_CALIFICACION_PAES, X_ESTRUCTURA_CALIFICACION_PAES solamente sobre calificaciones paes.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('HABILITADO_CALIFICACIONES_EXTERNAS','E402',  'Habilita las operaciones X_ARCHIVO_CALIFICACION_PAES, X_ESTRUCTURA_CALIFICACION_PAES solamente sobre calificaciones externas.', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_componente_plan_estudio ADD COLUMN cpe_tipo_paes character varying(40);
ALTER TABLE centros_educativos.sg_componente_plan_estudio_aud ADD COLUMN cpe_tipo_paes character varying(40);

update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'PAES' where cpe_codigo_externo = 5001;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'PAES' where cpe_codigo_externo = 5002;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'PAES' where cpe_codigo_externo = 5003;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'PAES' where cpe_codigo_externo = 5004;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'EXTERNA' where cpe_codigo_externo = 1101;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'EXTERNA' where cpe_codigo_externo = 1102;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'EXTERNA' where cpe_codigo_externo = 1103;
update centros_educativos.sg_componente_plan_estudio set cpe_tipo_paes = 'EXTERNA' where cpe_codigo_externo = 1104;

--3.71.3 
ALTER table centros_educativos.sg_titulo  ALTER COLUMN tit_numero_resolucion TYPE character varying(100);
ALTER table centros_educativos.sg_titulo_aud  ALTER COLUMN tit_numero_resolucion TYPE character varying(100);

ALTER table centros_educativos.sg_solicitudes_impresion  ALTER COLUMN sim_numero_resolucion TYPE character varying(100);
ALTER table centros_educativos.sg_solicitudes_impresion_aud  ALTER COLUMN sim_numero_resolucion TYPE character varying(100);

-- 3.71.5

ALTER TABLE revinfo add column origen character varying (30);

-- 3.72.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_DATOS_GENERALES_CENTRO_EDUCATIVO','E403',  'Habilita consumo de datos generales de un centro educativo. Utilizado por SAT.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_PLANTA_CENTRO_EDUCATIVO','E404',  'Habilita consumo de planta de docentes/directores de centro educativo. Utilizado por SAT.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_SECCIONES_CENTRO_EDUCATIVO','E405',  'Habilita consumo de secciones de un centro educativo. Utilizado por SAT.', 1, true, null, null,0);

-- 3.73.0

ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten ADD COLUMN rea_modalidad_atencion_flexible boolean;
ALTER TABLE centros_educativos.sg_rel_mod_ed_mod_aten_aud ADD COLUMN rea_modalidad_atencion_flexible boolean;
COMMENT ON COLUMN centros_educativos.sg_rel_mod_ed_mod_aten.rea_modalidad_atencion_flexible IS 'Campo que indica si la modalidad de la relación es flexible.';


UPDATE centros_educativos.sg_rel_mod_ed_mod_aten set rea_modalidad_atencion_flexible = (rea_sub_modalidad_atencion_fk is not null);


CREATE INDEX sg_rel_mod_aten_flexible_idx ON centros_educativos.sg_rel_mod_ed_mod_aten USING btree (rea_modalidad_atencion_flexible);


-- 3.74.0

ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD COLUMN cma_archivo_firmado_fk bigint;
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas_aud ADD COLUMN cma_archivo_firmado_fk bigint;
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_archivo_firmado_fk IS 'Llave foránea que hace referencia al archivo firmado.';
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD CONSTRAINT sg_conf_mat_archivo_firmado_fk FOREIGN KEY (cma_archivo_firmado_fk) REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD COLUMN cma_firmada boolean;
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas_aud ADD COLUMN cma_firmada boolean;
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_firmada IS 'Indica si la confirmación está firmada.';


ALTER TABLE centros_educativos.sg_confirmaciones_matriculas ADD COLUMN cma_firma_transaction_id character varying(50);
ALTER TABLE centros_educativos.sg_confirmaciones_matriculas_aud ADD COLUMN cma_firma_transaction_id character varying(50);
COMMENT ON COLUMN centros_educativos.sg_confirmaciones_matriculas.cma_firma_transaction_id IS 'Id de la transacción en el servicio de firma electrónica';


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_DATOS_GENERALES_CENTRO_EDUCATIVO','E403',  'Habilita consumo de datos generales de un centro educativo. Utilizado por SAT.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_PLANTA_CENTRO_EDUCATIVO','E404',  'Habilita consumo de planta de docentes/directores de centro educativo. Utilizado por SAT.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_SECCIONES_CENTRO_EDUCATIVO','E405',  'Habilita consumo de secciones de un centro educativo. Utilizado por SAT.', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_moda_calificaciones_conceptuales integer;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_moda_calificaciones_conceptuales integer;

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_moda_calificaciones_conceptuales_femenino integer;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_moda_calificaciones_conceptuales_femenino integer;

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_moda_calificaciones_conceptuales_masculino integer;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_moda_calificaciones_conceptuales_masculino integer;


ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_promedio_calificaciones_masculino numeric(10,2) NULL;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_promedio_calificaciones_masculino numeric(10,2) NULL;

ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_promedio_calificaciones_femenino numeric(10,2) NULL;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_promedio_calificaciones_femenino numeric(10,2) NULL;


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('OBTENER_INDICADORES_CENTRO_EDUCATIVO','E413',  'Habilita consumo de indicadores de centro educativo. Utilizado por SAT.', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_promedio_moda_desactualizados boolean NULL;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_promedio_moda_desactualizados boolean NULL;

CREATE INDEX sg_cal_promedio_moda_des_idx ON centros_educativos.sg_calificaciones USING btree (cal_promedio_moda_desactualizados);


-- 3.75.0

																																																																																			
ALTER TABLE centros_educativos.sg_datos_contratacion     ADD COLUMN dco_estado_fk bigint;
ALTER TABLE centros_educativos.sg_datos_contratacion_aud ADD COLUMN dco_estado_fk bigint;
																																														
ALTER TABLE centros_educativos.sg_datos_contratacion ADD CONSTRAINT sg_datos_contratacion_estado_fkey 
    FOREIGN KEY (dco_estado_fk) 
    REFERENCES catalogo.sg_estados_datos_contratacion(edc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_postulacion_inicio date;
ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_postulacion_fin date;
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_postulacion_inicio date;
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_postulacion_fin date;

																											
																													  
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_aplicaciones_plaza_apl_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_aplicaciones_plaza(apl_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_aplicaciones_plaza_apl_pk_seq'::regclass), apl_plaza_fk bigint not null, apl_personal_fk bigint not null, apl_codigo_usuario character varying(25), apl_estado character varying(25), apl_observacion CHARACTER VARYING(500), apl_fecha_aplico timestamp without TIME zone, apl_ult_mod_fecha timestamp without TIME zone, apl_ult_mod_usuario CHARACTER varying(45), apl_version INTEGER, CONSTRAINT sg_aplicaciones_plaza_pkey         PRIMARY KEY (apl_pk), CONSTRAINT sg_aplicaciones_plaza_plaza_fk     FOREIGN KEY (apl_plaza_fk)      REFERENCES centros_educativos.sg_plazas(pla_pk)       MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE, CONSTRAINT sg_aplicaciones_plaza_personal_fk  FOREIGN KEY (apl_personal_fk)   REFERENCES centros_educativos.sg_personal_sede_educativa(pse_pk)     MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  centros_educativos.sg_aplicaciones_plaza                      IS 'Tabla para almacenar las aplicaciones a las plazas.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_pk               IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_plaza_fk         IS 'Llave foranea que hace referencia a la plaza.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_personal_fk      IS 'Llave foranea que hace referencia al personal que aplico.';																								
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_codigo_usuario   IS 'Código de usuario que aplico.';																   
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_estado           IS 'Estado del registro.';																		 
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_observacion      IS 'Observación del registro.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_fecha_aplico     IS 'Fecha de la aplicacion.';																																									   
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_aplicaciones_plaza.apl_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_aplicaciones_plaza_aud (apl_pk bigint NOT NULL, apl_plaza_fk bigint not null, apl_personal_fk bigint not null, apl_codigo_usuario character varying(25), apl_estado character varying(25), apl_observacion CHARACTER VARYING(500), apl_fecha_aplico timestamp without TIME zone, apl_ult_mod_fecha timestamp without TIME zone, apl_ult_mod_usuario CHARACTER varying(45), apl_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_aplicaciones_plaza_aud_pkey PRIMARY KEY (apl_pk, rev), CONSTRAINT sg_aplicaciones_plaza_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));
						   
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_APLICACION_PLAZA','E341',  'Aplicación plazas: mostrar botón aplicar y crear postulación.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_APLICACION_PLAZA','E342',  'Aplicación plazas: actualizar postulación.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_APLICACION_PLAZA','E343',  'Aplicación plazas: eliminar postulación.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_APLICACION_PLAZA','EB71',  'Aplicación plazas: buscar las aplicaciones de una plaza', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_POSTULACIONES','EP50',  'Plazas: boton para ver las postulaciones de la plaza', 1, true, null, null,0);

																																																													   
																																																																 
ALTER TABLE centros_educativos.sg_rel_personal_especialidades ADD COLUMN rpe_fecha_graduacion date;
ALTER TABLE centros_educativos.sg_rel_personal_especialidades ADD COLUMN rpe_cum numeric(10,2);
ALTER TABLE centros_educativos.sg_rel_personal_especialidades_aud ADD COLUMN rpe_fecha_graduacion date;
ALTER TABLE centros_educativos.sg_rel_personal_especialidades_aud ADD COLUMN rpe_cum numeric(10,2);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_MATRIZ_COMPARATIVA','EP54',  'Plazas: botón para ver la matriz comparativa de la plaza.', 1, true, null, null,0);
																						  
																				

DELETE FROM seguridad.sg_roles_operacion where rop_operacion_fk = (select ope_pk from seguridad.sg_operaciones where ope_codigo = 'EB71');
DELETE FROM seguridad.sg_operaciones where ope_codigo = 'EB71';

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_APLICACION_PLAZA','EB74',  'Aplicación plazas: buscar las aplicaciones de una plaza', 1, true, null, null,0);
																																																																										  
																																																																									  																																																																							  

ALTER TABLE centros_educativos.sg_plazas RENAME COLUMN pla_estado TO pla_estado_solicitud;
ALTER TABLE centros_educativos.sg_plazas_aud RENAME COLUMN pla_estado TO pla_estado_solicitud;

ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_id_plaza integer;
ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_anio_partida integer;
ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_nombre character varying(255);
ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_caracterizacion character varying(10);
ALTER TABLE centros_educativos.sg_plazas ADD COLUMN pla_estado_plaza character varying(10);
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_id_plaza         IS 'Id de plaza.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_anio_partida     IS 'Año de la partida.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_nombre           IS 'Nombre de la plaza.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_caracterizacion  IS 'Caracterización de la plaza.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_estado_plaza     IS 'Estado de la plaza.';
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_id_plaza integer;
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_anio_partida integer;
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_nombre character varying(255);
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_caracterizacion character varying(10);
ALTER TABLE centros_educativos.sg_plazas_aud ADD COLUMN pla_estado_plaza character varying(10);

 

ALTER TABLE centros_educativos.sg_personas ALTER COLUMN per_nip type VARCHAR(50);
																										
ALTER TABLE centros_educativos.sg_personas_aud ALTER COLUMN per_nip type VARCHAR(50);

																															   
--Modificar nombre tabla Plazas a Solicitudes Plazas
																							
ALTER TABLE centros_educativos.sg_plazas RENAME TO sg_solicitudes_plazas;
ALTER TABLE centros_educativos.sg_plazas_aud RENAME TO sg_solicitudes_plazas_aud;

ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_pk                           TO spl_pk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_sede_solicitante_fk          TO spl_sede_solicitante_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_tipo_plaza                   TO spl_tipo_plaza;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_nivel_fk                     TO spl_nivel_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_modalidad_educativa_fk       TO spl_modalidad_educativa_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_modalidad_atencion_fk        TO spl_modalidad_atencion_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_opcion_fk                    TO spl_opcion_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_componente_plan_estudio_fk   TO spl_componente_plan_estudio_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_especialidad_fk              TO spl_especialidad_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_fuente_financiamiento_fk     TO spl_fuente_financiamiento_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_fecha_desde                  TO spl_fecha_desde;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_fecha_hasta                  TO spl_fecha_hasta;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_jornada_fk                   TO spl_jornada_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_descripcion                  TO spl_descripcion;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_estado_solicitud             TO spl_estado;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_tipo_nombramiento_fk         TO spl_tipo_nombramiento_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_ult_mod_fecha                TO spl_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_ult_mod_usuario              TO spl_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_version                      TO spl_version;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_codigo                       TO spl_codigo;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_postulacion_inicio           TO spl_postulacion_inicio;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_postulacion_fin              TO spl_postulacion_fin;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_id_plaza                     TO spl_id_plaza;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_anio_partida                 TO spl_anio_partida;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_nombre                       TO spl_nombre;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_caracterizacion              TO spl_caracterizacion;
ALTER TABLE centros_educativos.sg_solicitudes_plazas RENAME COLUMN pla_estado_plaza                 TO spl_estado_plaza;

ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_pk                           TO spl_pk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_sede_solicitante_fk          TO spl_sede_solicitante_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_tipo_plaza                   TO spl_tipo_plaza;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_nivel_fk                     TO spl_nivel_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_modalidad_educativa_fk       TO spl_modalidad_educativa_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_modalidad_atencion_fk        TO spl_modalidad_atencion_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_opcion_fk                    TO spl_opcion_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_componente_plan_estudio_fk   TO spl_componente_plan_estudio_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_especialidad_fk              TO spl_especialidad_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_fuente_financiamiento_fk     TO spl_fuente_financiamiento_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_fecha_desde                  TO spl_fecha_desde;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_fecha_hasta                  TO spl_fecha_hasta;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_jornada_fk                   TO spl_jornada_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_descripcion                  TO spl_descripcion;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_estado_solicitud             TO spl_estado;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_tipo_nombramiento_fk         TO spl_tipo_nombramiento_fk;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_ult_mod_fecha                TO spl_ult_mod_fecha;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_ult_mod_usuario              TO spl_ult_mod_usuario;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_version                      TO spl_version;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_codigo                       TO spl_codigo;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_postulacion_inicio           TO spl_postulacion_inicio;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_postulacion_fin              TO spl_postulacion_fin;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_id_plaza                     TO spl_id_plaza;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_anio_partida                 TO spl_anio_partida;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_nombre                       TO spl_nombre;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_caracterizacion              TO spl_caracterizacion;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud RENAME COLUMN pla_estado_plaza                 TO spl_estado_plaza;

ALTER SEQUENCE centros_educativos.sg_plazas_pla_pk_seq RENAME TO sg_solicitudes_plazas_pla_pk_seq;
ALTER INDEX centros_educativos.sg_plazas_pkey RENAME TO sg_solicitudes_plazas_pkey;
ALTER INDEX centros_educativos.sg_plazas_aud_pkey RENAME TO sg_solicitudes_plazas_aud_pkey;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_PLAZA','E344',  'Solicitud plazas: crear una nueva solicitud.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_PLAZA','E345',  'Solicitud plazas: actualizar una solicitud.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_PLAZA','E346',  'Solicitud plazas: eliminar una solicitud.', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_PLAZA','EB73',  'Solicitud plaza: buscar solicitudes de plaza.', 1, true, null, null,0);

-- Plazas
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_plazas_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_plazas(
    pla_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_plazas_pk_seq'::regclass), 
    pla_nombre character varying(100), 
    pla_id_puesto bigint not null, 
    pla_partida integer, 
    pla_subpartida integer, 
    pla_estado character varying(45), 
    pla_situacion character varying(45), 
    pla_anio_partida integer, 
    pla_ult_mod_fecha timestamp without TIME zone, 
    pla_ult_mod_usuario CHARACTER varying(45), 
    pla_version INTEGER, 
    CONSTRAINT sg_plazas_pkey PRIMARY KEY (pla_pk)
);
COMMENT ON TABLE  centros_educativos.sg_plazas                      IS 'Tabla para almacenar las plazas.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_pk               IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_nombre           IS 'Nombre del registro.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_id_puesto        IS 'Id del puesto.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_partida          IS 'Código partida';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_subpartida       IS 'Código subpartida';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_estado           IS 'Estado del registro';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_situacion        IS 'Situación del registro';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_anio_partida     IS 'Año de la partida';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_plazas.pla_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_plazas_aud (
    pla_pk bigint, 
    pla_nombre character varying(100),
    pla_id_puesto bigint, 
    pla_partida integer, 
    pla_subpartida integer, 
    pla_estado character varying(45), 
    pla_situacion character varying(45), 
    pla_anio_partida integer, 
    pla_ult_mod_fecha timestamp without TIME zone, 
    pla_ult_mod_usuario CHARACTER varying(45), 
    pla_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_plazas_aud_pkey PRIMARY KEY (pla_pk, rev), 
    CONSTRAINT sg_plazas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

-- Detalles de matricula
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_detalle_matricula_dem_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_detalle_matricula (dem_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_detalle_matricula_dem_pk_seq'::regclass),dem_sede_fk bigint,dem_anio_fk bigint, dem_nivel_fk bigint, dem_costo_matricula integer, dem_cantidad_mensualidad integer,dem_costo_mensualidad integer,  dem_ult_mod_fecha timestamp without time zone, dem_ult_mod_usuario character varying(45), dem_version integer, 
CONSTRAINT sg_detalle_matricula_pkey PRIMARY KEY (dem_pk), 
CONSTRAINT sg_dem_sede_fk FOREIGN KEY (dem_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_dem_anio_fk FOREIGN KEY (dem_anio_fk) REFERENCES centros_educativos.sg_anio_lectivo(ale_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_dem_nivel_fk FOREIGN KEY (dem_nivel_fk) REFERENCES centros_educativos.sg_niveles(niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT dem_anio_servicio_uk UNIQUE (dem_sede_fk,dem_anio_fk,dem_nivel_fk));
COMMENT ON TABLE centros_educativos.sg_detalle_matricula IS 'Tabla para el registro de detalles de matricula.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_anio_fk IS 'Relación con anio lectivo.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_nivel_fk IS 'Relación con nivel.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_costo_matricula IS 'Costo matrícula.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_costo_mensualidad IS 'Costo mensualidad.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_cantidad_mensualidad IS 'Cantidad mensualidad.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_detalle_matricula.dem_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_detalle_matricula_aud (dem_pk bigint NOT NULL,dem_sede_fk bigint,dem_anio_fk bigint, dem_nivel_fk bigint, dem_costo_matricula integer, dem_cantidad_mensualidad integer,dem_costo_mensualidad integer,  dem_ult_mod_fecha timestamp without time zone, dem_ult_mod_usuario character varying(45), dem_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_detalle_matricula_aud ADD PRIMARY KEY (dem_pk, rev);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_DETALLE_MATRICULA','EB80',  'Buscar detalle matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DETALLE_MATRICULA','E360',  'Crear detalle matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DETALLE_MATRICULA','E361',  'Actualizar detalle matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DETALLE_MATRICULA','E362',  'Eliminar detalle matrícula', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_experiencia_laboral DROP COLUMN ela_anio_servicio;
ALTER TABLE centros_educativos.sg_experiencia_laboral_aud DROP COLUMN ela_anio_servicio;

ALTER TABLE centros_educativos.sg_personal_sede_educativa       ADD COLUMN pse_anio_servicio integer;
ALTER TABLE centros_educativos.sg_personal_sede_educativa_aud   ADD COLUMN pse_anio_servicio integer;


ALTER TABLE centros_educativos.sg_plazas       ADD COLUMN pla_sede_fk bigint;
ALTER TABLE centros_educativos.sg_plazas_aud   ADD COLUMN pla_sede_fk bigint;
ALTER TABLE centros_educativos.sg_plazas  ADD CONSTRAINT sg_pla_sede_fk FOREIGN KEY (pla_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_solicitudes_plazas       ADD COLUMN spl_plaza_existente boolean;
COMMENT ON COLUMN centros_educativos.sg_solicitudes_plazas.spl_plaza_existente IS 'Indica si registro pertenece a plaza existente.';
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud   ADD COLUMN spl_plaza_existente boolean;

ALTER TABLE centros_educativos.sg_solicitudes_plazas       ADD COLUMN spl_plaza_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_plazas_aud   ADD COLUMN spl_plaza_fk bigint;
ALTER TABLE centros_educativos.sg_solicitudes_plazas  ADD CONSTRAINT sg_spl_plaza_fk FOREIGN KEY (spl_plaza_fk) REFERENCES centros_educativos.sg_plazas(pla_pk) 
MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE centros_educativos.sg_plazas       ADD COLUMN pla_nombre_busqueda character varying(255);
ALTER TABLE centros_educativos.sg_plazas_aud   ADD COLUMN pla_nombre_busqueda character varying(255);

update centros_educativos.sg_plazas  set pla_nombre_busqueda=lower(pla_nombre);
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'ñ','n');
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'á','a');
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'é','e');
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'í','i');
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'ó','o');
update centros_educativos.sg_plazas  set pla_nombre_busqueda=replace(pla_nombre,'ú','u');

ALTER TABLE centros_educativos.sg_plazas       ADD COLUMN pla_codigo_busqueda character varying(255);
ALTER TABLE centros_educativos.sg_plazas_aud   ADD COLUMN pla_codigo_busqueda character varying(255);

update centros_educativos.sg_plazas pla  set pla_codigo_busqueda = (select pla_partida||'-'||pla_subpartida from centros_educativos.sg_plazas pl_aux
where pl_aux.pla_pk=pla.pla_pk);

INSERT INTO seguridad.sg_operaciones
( ope_codigo, ope_nombre, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version)
VALUES( 'EB69', 'BUSCAR_INCRIPCION_DIPLOMADO', NULL, 1, true, '2020-03-03 13:06:14.721', 'casuser', 0);


ALTER TABLE centros_educativos.sg_aplicaciones_plaza       ADD COLUMN apl_seleccionado_en_matriz boolean;
ALTER TABLE centros_educativos.sg_aplicaciones_plaza_aud   ADD COLUMN apl_seleccionado_en_matriz boolean;

update centros_educativos.sg_aplicaciones_plaza set apl_seleccionado_en_matriz=false; 
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_EDITAR_DOCENTE_EN_MATRIZ_COMPARATIVA','EP84',  'Permite seleccionar docente en matríz comparativa', 1, true, null, null,0);

ALTER TABLE centros_educativos.sg_aplicaciones_plaza       ADD COLUMN apl_rev_personal_cuando_aplica bigint;
ALTER TABLE centros_educativos.sg_aplicaciones_plaza_aud   ADD COLUMN apl_rev_personal_cuando_aplica bigint;


CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_especialidades_personal_al_aplicar_epa_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS    centros_educativos.sg_especialidades_personal_al_aplicar(epa_pk bigint NOT NULL DEFAULT NEXTVAL('centros_educativos.sg_especialidades_personal_al_aplicar_epa_pk_seq'::regclass), epa_aplicacion_plaza_fk bigint not null, epa_especialidad_fk bigint not null,epa_fecha_graduacion date,epa_cum numeric(10,2), epa_ult_mod_fecha timestamp without TIME zone, epa_ult_mod_usuario CHARACTER varying(45), epa_version INTEGER, CONSTRAINT sg_especialidades_personal_al_aplicar_pkey PRIMARY KEY (epa_pk), CONSTRAINT sg_especialidades_personal_al_aplicar_aplicacion_plaza_fk FOREIGN KEY (epa_aplicacion_plaza_fk) REFERENCES centros_educativos.sg_aplicaciones_plaza (apl_pk), CONSTRAINT sg_especialidades_personal_al_aplicar_especialidad_fk FOREIGN KEY (epa_especialidad_fk) REFERENCES catalogo.sg_especialidades (esp_pk));
    COMMENT ON TABLE  centros_educativos.sg_especialidades_personal_al_aplicar                           IS 'Tabla para almacenar la relacion entre el personal y las especialidades.';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_pk                    IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_aplicacion_plaza_fk   IS 'Llave foranea que hace referencia al personal.';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_especialidad_fk       IS 'Llave foranea que hace referencia a la especialidad';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_ult_mod_fecha         IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_ult_mod_usuario       IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN centros_educativos.sg_especialidades_personal_al_aplicar.epa_version               IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_especialidades_personal_al_aplicar_aud (epa_pk bigint NOT NULL,  epa_aplicacion_plaza_fk bigint not null, epa_especialidad_fk bigint not null,epa_fecha_graduacion date,epa_cum numeric(10,2), epa_ult_mod_fecha timestamp without TIME zone, epa_ult_mod_usuario CHARACTER varying(45), epa_version INTEGER,  rev bigint, revtype smallint, CONSTRAINT sg_especialidades_personal_al_aplicar_aud_pkey PRIMARY KEY (epa_pk, rev), CONSTRAINT sg_especialidades_personal_al_aplicar_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev));

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_PUBLICAR_SOLICITUD_PLAZA','EP85',  'Permite publicar solicitud de plaza', 1, true, null, null,0);


ALTER TABLE centros_educativos.sg_organismo_administracion_escolar ALTER COLUMN oae_acta_integracion TYPE varchar USING oae_acta_integracion::varchar;
ALTER TABLE centros_educativos.sg_organismo_administracion_escolar_aud ALTER COLUMN oae_acta_integracion TYPE varchar USING oae_acta_integracion::varchar;


----

--Operación para dejar sin efecto a persona jurídica en organismo de administración escolar
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_DESHABILITAR_PERSONA_JURIDICA','EP86',  'Permite dejar sin efecto a la persona jurídica', 1, true, null, null,0);

--Se crea tabla para solicitudes para deshabilitar personas jurídicas
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_sol_deshabilitar_perjur_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
create table centros_educativos.sg_sol_deshabilitar_perjur (
	dpj_pk  bigint NOT NULL DEFAULT nextval('centros_educativos.sg_sol_deshabilitar_perjur_pk_seq'::regclass),
	dpj_motivo character varying(2000),
	dpj_estado character varying(50),
	dpj_acta_fk bigint not null,
	dpj_numero_acuerdo varchar(20) NULL,
	dpj_fecha_acuerdo date NULL,
	dpj_acuerdo_fk bigint,
	dpj_org_admin_esc bigint not null,
	dpj_ult_mod_fecha timestamp without time zone,
	dpj_ult_mod_usuario character varying(45),
	dpj_version integer,
	constraint sg_sol_deshabilitar_perjur_pkey primary key (dpj_pk),
	constraint sg_sol_deshabilitar_perjur_acta_fk foreign key (dpj_acta_fk) references public.sg_archivos (ach_pk),
	constraint sg_sol_deshabilitar_perjur_acuerdo_fk foreign key (dpj_acuerdo_fk) references public.sg_archivos (ach_pk),
	constraint sg_sol_org_admin_esc_fk foreign key (dpj_org_admin_esc) references centros_educativos.sg_organismo_administracion_escolar (oae_pk)
);
COMMENT ON TABLE centros_educativos.sg_sol_deshabilitar_perjur IS 'Tabla para almacenar solicitudes para dejar sin efecto personas jurídica.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_motivo IS 'Motivo para hacer la solicitud.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_estado IS 'Estado de la solicitud.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_acta_fk IS 'Acta del OAE.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_numero_acuerdo IS 'Número de acuerdo de aprobación de la solicitud.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_fecha_acuerdo IS 'Fecha de acuerdo de aprobación.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_acuerdo_fk IS 'Acuerdo de aprobación de la solicitud.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_org_admin_esc IS 'ID del OAE.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_ult_mod_fecha IS 'Fecha de última modificación.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_ult_mod_usuario IS 'Usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_sol_deshabilitar_perjur.dpj_version IS 'Versión del registro.';

create table centros_educativos.sg_sol_deshabilitar_perjur_aud (
	dpj_pk  bigint NOT NULL,
	dpj_motivo character varying(2000),
	dpj_estado character varying(50),
	dpj_acta_fk bigint not null,
	dpj_numero_acuerdo varchar(20) NULL,
	dpj_fecha_acuerdo date NULL,
	dpj_acuerdo_fk bigint,
	dpj_org_admin_esc bigint not null,
	dpj_ult_mod_fecha timestamp without time zone,
	dpj_ult_mod_usuario character varying(45),
	dpj_version integer,
	rev bigint,
	revtype smallint,
	constraint sg_sol_deshabilitar_perjuraud_pkey primary key (dpj_pk,rev),
	constraint sg_sol_deshabilitar_perjur_rev_fk foreign key (rev) references public.revinfo(rev)
);

--Permiso para ver el menú de solicitudes de OAE
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOL_DESHABILITAR_OAE','EM77',  'MENU: ver solicitudes', 1, true, null, null,0);
--Permiso para realizar búsqueda de solicitudes
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUDES_OAE','EB133',  'Buscar Organismo de Administración Escolar', 1, true, null, null,0);

--Se agrega referencia de quien reemplazó al miembro de la OAE
alter table centros_educativos.sg_persona_organismo_administracion add id_per_reemplazo bigint;

alter table centros_educativos.sg_persona_organismo_administracion add constraint sgPersonaOAEReemplazoFK 
foreign key (id_per_reemplazo) 
references centros_educativos.sg_persona_organismo_administracion (poa_pk);

alter table centros_educativos.sg_persona_organismo_administracion_aud add id_per_reemplazo bigint;

-- 4.0.0


ALTER TABLE centros_educativos.sg_anio_lectivo ADD COLUMN ale_habilitado_promedios boolean default false;
ALTER TABLE centros_educativos.sg_anio_lectivo_aud ADD COLUMN ale_habilitado_promedios boolean;

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_TABLERO_REPORTES','W18',  '', 4, true, null, null,0);


CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_trastornos_aprendizaje (per_pk bigint NOT NULL,tra_pk bigint NOT NULL, CONSTRAINT sg_personas_trastornos_apr_persona_fk FOREIGN KEY (per_pk) REFERENCES centros_educativos.sg_personas (per_pk), CONSTRAINT sg_personas_trastornos_apr_tra_fk FOREIGN KEY (tra_pk) REFERENCES catalogo.sg_trastornos_aprendizaje (tra_pk));
CREATE TABLE IF NOT EXISTS centros_educativos.sg_personas_trastornos_aprendizaje_aud(per_pk bigint NOT NULL,tra_pk bigint NOT NULL, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_personas_trastornos_aprendizaje ADD PRIMARY KEY (per_pk, tra_pk);
ALTER TABLE centros_educativos.sg_personas_trastornos_aprendizaje_aud ADD PRIMARY KEY (per_pk, tra_pk, rev);


ALTER TABLE centros_educativos.sg_personas ADD COLUMN per_vive_con_cantidad integer;
ALTER TABLE centros_educativos.sg_personas_aud ADD COLUMN per_vive_con_cantidad integer;

--4.0.2
CREATE INDEX sg_horarios_escolares_unico_idx ON centros_educativos.sg_horarios_escolares USING btree (hes_unico_docente);

-- Habilitación de Período de calificación
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_calificacion_hpc_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_calificacion (hpc_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_habilitacion_periodo_calificacion_hpc_pk_seq'::regclass), hpc_matricula_fk bigint, hpc_fecha_solicitud date, hpc_estado character varying(50), hpc_observacion character varying(500),hpc_estudiante_fk bigint,hpc_observacion_aprobacion_rechazo character varying(500), hpc_ult_mod_fecha timestamp without time zone, hpc_ult_mod_usuario character varying(45), hpc_version integer, CONSTRAINT sg_habilitacion_periodo_calificacion_pkey PRIMARY KEY (hpc_pk)
,CONSTRAINT sg_habilitacion_periodo_calificacion_matricula_fkey FOREIGN KEY (hpc_matricula_fk) REFERENCES centros_educativos.sg_matriculas(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
,CONSTRAINT sg_habilitacion_periodo_calificacion_estudiante_fkey FOREIGN KEY (hpc_estudiante_fk) REFERENCES centros_educativos.sg_estudiantes(est_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_habilitacion_periodo_calificacion IS 'Tabla para el registro de habilitación de período de calificación.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_matricula_fk IS 'Matricula del registro.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_fecha_solicitud IS 'Fecha de solicitud.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_estado IS 'Estado de solicitud.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_observacion IS 'Observacio de solicitud.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_estudiante_fk IS 'Estudiante de solicitud.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_observacion_aprobacion_rechazo IS 'Obsercavion aprobacion rechazo.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_calificacion.hpc_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_calificacion_aud (hpc_pk bigint NOT NULL,  hpc_matricula_fk bigint, hpc_fecha_solicitud date, hpc_estado character varying(50), hpc_observacion character varying(500),hpc_estudiante_fk bigint, hpc_observacion_aprobacion_rechazo character varying(500), hpc_ult_mod_fecha timestamp without time zone, hpc_ult_mod_usuario character varying(45), hpc_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_habilitacion_periodo_calificacion_aud ADD PRIMARY KEY (hpc_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SOLICITUD_CORRECCION_CALIFICACION','E406',  'Crear habilitacion periodo calificacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SOLICITUD_CORRECCION_CALIFICACION','E407',  'Actualizar habilitacion periodo calificacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SOLICITUD_CORRECCION_CALIFICACION','E408',  'Eliminar habilitacion periodo calificacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_SOLICITUD_CORRECCION_CALIFICACION','EB92',  'Buscar habilitación período calificacion', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SOLICITUD_CORRECCION_CALIFICACION','EM76',  'MENU: Periodo calificacion', 1, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EVALUAR_SOLICITUD_CORRECCION_CALIFICACION','E409',  'Evaluar periodo calificacion', 1, true, null, null,0);


-- Relación entre periodo y habilitación periodo
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_periodos_habilitacion_periodo_rph_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_periodos_habilitacion_periodo (rph_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_periodos_habilitacion_periodo_rph_pk_seq'::regclass), 
 rph_habilitacion_periodo_calificacion_fk bigint,rph_tipo_periodo_calificacion character varying(50), rph_rango_fecha_fk bigint,rph_tipo_calendario_escolar character varying(50),rph_numero_extraordinario integer, rph_componente_plan_grado_fk bigint, rph_calificacion_numerica character varying(45),rph_calificacion_conceptual bigint,rph_info_procesamiento character varying(100),rph_ult_mod_fecha timestamp without time zone, rph_ult_mod_usuario character varying(45), rph_version integer, CONSTRAINT sg_rel_periodos_habilitacion_periodo_pkey PRIMARY KEY (rph_pk),
CONSTRAINT sg_rel_periodos_habilitacion_periodo_habilitacion_periodo_fkey FOREIGN KEY (rph_habilitacion_periodo_calificacion_fk) REFERENCES centros_educativos.sg_habilitacion_periodo_calificacion(hpc_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_periodos_habilitacion_periodo_habilitacion_componente_plan_grado_fkey FOREIGN KEY (rph_componente_plan_grado_fk) REFERENCES centros_educativos.sg_componente_plan_grado(cpg_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_periodos_habilitacion_periodo_habilitacion_calificacion_fkey FOREIGN KEY (rph_calificacion_conceptual) REFERENCES catalogo.sg_calificaciones(cal_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_periodos_habilitacion_periodo_rango_fecha_fkey FOREIGN KEY (rph_rango_fecha_fk) REFERENCES centros_educativos.sg_rango_fecha(rfe_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
COMMENT ON TABLE centros_educativos.sg_rel_periodos_habilitacion_periodo IS 'Tabla para el registro de relación entre periodo y habilitación periodo.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_habilitacion_periodo_calificacion_fk IS 'Habilitacion periodo calificacion.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_tipo_periodo_calificacion IS 'Tipo periodo calificacion.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_rango_fecha_fk IS 'Rango fecha.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_tipo_calendario_escolar IS 'Tipo calendario escolar.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_periodos_habilitacion_periodo.rph_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_periodos_habilitacion_periodo_aud (rph_pk bigint NOT NULL,rph_habilitacion_periodo_calificacion_fk bigint,rph_numero_extraordinario integer, rph_componente_plan_grado_fk bigint, rph_calificacion_numerica character varying(45),rph_calificacion_conceptual bigint,rph_info_procesamiento character varying(100),rph_tipo_periodo_calificacion character varying(50), rph_rango_fecha_fk bigint,rph_tipo_calendario_escolar character varying(50),  rph_ult_mod_fecha timestamp without time zone, rph_ult_mod_usuario character varying(45), rph_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_periodos_habilitacion_periodo_aud ADD PRIMARY KEY (rph_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_PERIODOS_HABILITACION_PERIODO','E410',  'Crear rel periodos habilitacion periodo', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_PERIODOS_HABILITACION_PERIODO','E411',  'Actualizar rel periodos habilitacion periodo', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_PERIODOS_HABILITACION_PERIODO','E412',  'Eliminar rel periodos habilitacion periodo', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_PERIODOS_HABILITACION_PERIODO','EB93',  'Buscar rel periodos habilitacion periodo', 1, true, null, null,0);


-- Habilitación período matrícula
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_matricula_hmp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_matricula (hmp_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_habilitacion_periodo_matricula_hmp_pk_seq'::regclass), 
hmp_sede_fk bigint,hmp_fecha_solicitud date, hmp_estado character varying(50), hmp_observacion character varying(500),hmp_fecha_desde date,hmp_fecha_hasta date,hmp_observacion_aprobacion_rechazo character varying(500), hmp_ult_mod_fecha timestamp without time zone, hmp_ult_mod_usuario character varying(45), hmp_version integer, CONSTRAINT sg_habilitacion_periodo_matricula_pkey PRIMARY KEY (hmp_pk), 
CONSTRAINT sg_habilitacion_periodo_matricula_sede_fk FOREIGN KEY (hmp_sede_fk) REFERENCES centros_educativos.sg_sedes(sed_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_habilitacion_periodo_matricula IS 'Tabla para el registro de habilitación período matrícula.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_sede_fk IS 'Sede.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_fecha_solicitud IS 'Fecha solicitud.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_estado IS 'Estado.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_observacion IS 'Observacion.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_fecha_desde IS 'Fecha desde.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_fecha_hasta IS 'Fecha hasta.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_habilitacion_periodo_matricula.hmp_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_habilitacion_periodo_matricula_aud (hmp_pk bigint NOT NULL, hmp_sede_fk bigint,hmp_fecha_solicitud date, hmp_estado character varying(50), hmp_observacion character varying(500),hmp_fecha_desde date,hmp_fecha_hasta date,hmp_observacion_aprobacion_rechazo character varying(500),  hmp_ult_mod_fecha timestamp without time zone, hmp_ult_mod_usuario character varying(45), hmp_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_habilitacion_periodo_matricula_aud ADD PRIMARY KEY (hmp_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_HABILITACION_PERIODO_MATRICULA','E414',  'Crear solicitud de habilitación de período matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_HABILITACION_PERIODO_MATRICULA','E415',  'Actualizar solicitud de habilitación de período matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_HABILITACION_PERIODO_MATRICULA','E416',  'Eliminar solicitud de habilitación de período matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_HABILITACION_PERIODO_MATRICULA','E417',  'Buscar solicitud de habilitación de período matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('EVALUAR_HABILITACION_PERIODO_MATRICULA','E418',  'Evaluar solicitud de habilitación de período matrícula', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_HABILITACION_PERIODO_MATRICULA','EM78',  'MENU:Períodos de matrícula', 1, true, null, null,0);




-- Rel Habilitación matrícula nivel
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_rel_habilitacion_matricula_nivel_rhn_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_habilitacion_matricula_nivel (rhn_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_rel_habilitacion_matricula_nivel_rhn_pk_seq'::regclass), 
rhn_habilitacion_periodo_matricula_fk bigint, rhn_nivel_fk bigint, rhn_modalidad_atencion_fk bigint, rhn_submodalidad_fk bigint, rhn_ult_mod_fecha timestamp without time zone, rhn_ult_mod_usuario character varying(45), rhn_version integer, CONSTRAINT sg_rel_habilitacion_matricula_nivel_pkey PRIMARY KEY (rhn_pk), 
CONSTRAINT sg_rel_habilitacion_matricula_nivel_habilitacion_matricula_fkey FOREIGN KEY (rhn_habilitacion_periodo_matricula_fk) REFERENCES centros_educativos.sg_habilitacion_periodo_matricula(hmp_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_habilitacion_matricula_nivel_nivel_fkey FOREIGN KEY (rhn_nivel_fk) REFERENCES centros_educativos.sg_niveles(niv_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_habilitacion_matricula_nivel_modalidad_atencion_fkey FOREIGN KEY (rhn_modalidad_atencion_fk) REFERENCES catalogo.sg_modalidades_atencion(mat_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT sg_rel_habilitacion_matricula_nivel_submodalidad_fkey FOREIGN KEY (rhn_submodalidad_fk) REFERENCES catalogo.sg_sub_modalidades(smo_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE centros_educativos.sg_rel_habilitacion_matricula_nivel IS 'Tabla para el registro de rel habilitación matrícula nivel.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_habilitacion_periodo_matricula_fk IS 'Habilitacion periodo matricula.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_nivel_fk IS 'Nivel.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_modalidad_atencion_fk IS 'Modalidad de atencion.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_submodalidad_fk IS 'Submodalidad.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_rel_habilitacion_matricula_nivel.rhn_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS centros_educativos.sg_rel_habilitacion_matricula_nivel_aud (rhn_pk bigint NOT NULL,rhn_habilitacion_periodo_matricula_fk bigint, rhn_nivel_fk bigint, rhn_modalidad_atencion_fk bigint, rhn_submodalidad_fk bigint, rhn_ult_mod_fecha timestamp without time zone, rhn_ult_mod_usuario character varying(45), rhn_version integer, rev bigint, revtype smallint);
ALTER TABLE centros_educativos.sg_rel_habilitacion_matricula_nivel_aud ADD PRIMARY KEY (rhn_pk, rev);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_REL_HABILITACION_MATRICULA_NIVEL','E419',  'Crear relacion entre habilitacion matricula y nivel', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_REL_HABILITACION_MATRICULA_NIVEL','E420',  'Actualizar relacion entre habilitacion matricula y nivel', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_REL_HABILITACION_MATRICULA_NIVEL','E421',  'Eliminar relacion entre habilitacion matricula y nivel', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('BUSCAR_REL_HABILITACION_MATRICULA_NIVEL','E422',  'Crear relacion entre habilitacion matricula y nivel', 1, true, null, null,0);

--4.0.7
--Tabla para almacenar los items seleccionados para cada OAE
CREATE SEQUENCE IF NOT EXISTS centros_educativos.sg_oae_items_evaluar_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

create table centros_educativos.sg_oae_items_evaluar (
	oai_pk bigint NOT NULL DEFAULT nextval('centros_educativos.sg_oae_items_evaluar_pk_seq'::regclass),
	oai_item_fk bigint not null,
	oai_organismo_fk bigint not null,
	oai_ult_mod_fecha timestamp NULL,
	oai_ult_mod_usuario varchar(45) NULL,
	oai_version int4 NULL,
	constraint sg_oae_items_evaluar_pk primary key (oai_pk),
	constraint sg_oae_item_fk foreign key (oai_item_fk) references catalogo.sg_items_evaluar_organismos (ieo_pk),
	constraint sg_oae_oae_fk foreign key (oai_organismo_fk) references centros_educativos.sg_organismo_administracion_escolar (oae_pk)
);

COMMENT ON TABLE centros_educativos.sg_oae_items_evaluar IS 'Tabla para almacenar items de una OAE.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_item_fk IS 'Id del item.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_organismo_fk IS 'Id del organismo.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_ult_mod_fecha IS 'Fecha de última modificación.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_ult_mod_usuario IS 'Usuario que modificó el registro.';
COMMENT ON COLUMN centros_educativos.sg_oae_items_evaluar.oai_version IS 'Versión del registro.';

create table centros_educativos.sg_oae_items_evaluar_aud (
	oai_pk bigint not null,
	oai_item_fk bigint not null,
	oai_organismo_fk bigint not null,
	oai_ult_mod_fecha timestamp NULL,
	oai_ult_mod_usuario varchar(45) NULL,
	oai_version int4 NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	CONSTRAINT sg_oae_items_evaluar_aud_pkey PRIMARY KEY (oai_pk, rev)
);

--Permisos para ver alertas
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_APROBADA','EP87',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_MASDATOS','EP88',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_RECHAZADA','EP89',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('PUEDE_VER_ALERTAS_OAE_ENVIADA','EP90',  'Permite ver las alertas de cambio de estado de una OAE', 1, true, null, null,0);

--Se corrige el nombre del campo
ALTER TABLE centros_educativos.sg_persona_organismo_administracion RENAME COLUMN id_per_reemplazo TO poa_per_reemplazo;
ALTER TABLE centros_educativos.sg_persona_organismo_administracion_aud RENAME COLUMN id_per_reemplazo TO poa_per_reemplazo;


ALTER TABLE centros_educativos.sg_rel_periodos_habilitacion_periodo       ADD COLUMN rph_calificacion_actual_valor character varying(45);
ALTER TABLE centros_educativos.sg_rel_periodos_habilitacion_periodo_aud   ADD COLUMN rph_calificacion_actual_valor character varying(45);

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('SOLICITUD_CAMBIO_CALIFICACION_CAL', 'Menaje en pantalla Solicitud cambio calificacion fieldset calificacion','menaje en pantalla solicitud cambio calificacion','Mensaje 2');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('SOLICITUD_CAMBIO_CALIFICACION_MAT', 'Menaje en pantalla Solicitud cambio calificacion fieldset matricula','menaje en pantalla solicitud cambio calificacion','Mensaje 1');

insert into catalogo.sg_configuraciones(con_codigo, con_nombre, con_nombre_busqueda,con_valor)
values('SOLICITUD_HABILITACION_PERIODO_MAT', 'Menaje en pantalla Solicitud habilitación período matrícula','Menaje en pantalla Solicitud habilitación período matrícula','Mensaje 1');