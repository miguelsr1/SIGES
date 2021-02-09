--0.2.0
CREATE SCHEMA IF NOT EXISTS sistemas_integrados;


INSERT INTO seguridad.sg_categorias_operacion values (9,'sie', 'Sistemas Integrados','Sistemas Integrados',true,null,null,0);



--Sistemas integrados
CREATE SEQUENCE IF NOT EXISTS sistemas_integrados.sg_sistemas_integrados_sin_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    sistemas_integrados.sg_sistemas_integrados(
    sin_pk bigint NOT NULL DEFAULT NEXTVAL('sistemas_integrados.sg_sistemas_integrados_sin_pk_seq'::regclass), 
    sin_nombre character varying(100), 
    sin_habilitado boolean, 
    sin_descripcion character varying(4000),  
    sin_ult_mod_fecha timestamp without TIME zone, 
    sin_ult_mod_usuario CHARACTER varying(45), 
    sin_version INTEGER,
    CONSTRAINT sg_sistemas_integrados_pkey         PRIMARY KEY (sin_pk)
);
COMMENT ON TABLE  sistemas_integrados.sg_sistemas_integrados                      IS 'Tabla para almacenar los sistemas integrados.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_pk               IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_nombre          IS 'Nombre del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_habilitado      IS 'Inidica si esta habilitado el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_descripcion      IS 'Descripción del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_ult_mod_fecha    IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_ult_mod_usuario  IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_sistemas_integrados.sin_version          IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_sistemas_integrados_aud (
    sin_pk bigint NOT NULL, 
    sin_nombre character varying(100), 
    sin_habilitado boolean, 
    sin_descripcion character varying(4000),  
    sin_ult_mod_fecha timestamp without TIME zone, 
    sin_ult_mod_usuario CHARACTER varying(45), 
    sin_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_sistemas_integrados_aud_pkey PRIMARY KEY (sin_pk, rev), 
    CONSTRAINT sg_sistemas_integrados_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_SISTEMA_INTEGRADO','SE01',  'Crear sistema integrado', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_SISTEMA_INTEGRADO','SE02',  'Actualizar sistema integrado', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_SISTEMA_INTEGRADO','SE03',  'Eliminar sistema integrado', 9, true, null, null,0);



CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_sistemas_sedes (sin_pk bigint NOT NULL,sed_pk bigint NOT NULL, CONSTRAINT sg_sistemas_sistemas_integrados_fk FOREIGN KEY (sin_pk) REFERENCES sistemas_integrados.sg_sistemas_integrados (sin_pk), CONSTRAINT sg_sistemas_sedes_fk FOREIGN KEY (sed_pk) REFERENCES centros_educativos.sg_sedes (sed_pk));
CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_sistemas_sedes_aud(sin_pk bigint NOT NULL,sed_pk bigint NOT NULL, rev bigint, revtype smallint);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ESTRUCTURA_GOBERNANZA','SEP1',  'Solapa: estructura gobernanza', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ACTAS_OAE','SEP2',  'Solapa: actas oae', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_OTROS_ARCHIVOS','SEP3',  'Solapa: otros archivos', 9, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_SISTEMA_INTEGRADO','SEP4',  'Opcion para ver los sistemas integrados', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('HISTORIAL_SISTEMA_INTEGRADO','SEP5',  'Opcion para ver el historial de sistemas integrados', 9, true, null, null,0);



--documentos de los sistemas integrados
CREATE SEQUENCE IF NOT EXISTS sistemas_integrados.sg_documentos_sistemas_dsi_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    sistemas_integrados.sg_documentos_sistemas(
    dsi_pk bigint NOT NULL DEFAULT NEXTVAL('sistemas_integrados.sg_documentos_sistemas_dsi_pk_seq'::regclass), 
    dsi_sistema_integrado_fk bigint not null, 
    dsi_descripcion character varying(4000),
    dsi_nombre character varying(40),
    dsi_documento_fk bigint,
    dsi_tipo_documento_fk bigint, 
    dsi_categoria_documento character varying(50), 
    dsi_ult_mod_fecha timestamp without TIME zone, 
    dsi_ult_mod_usuario CHARACTER varying(45), 
    dsi_version INTEGER,
    CONSTRAINT sg_documentos_sistemas_pkey         PRIMARY KEY (dsi_pk),
    CONSTRAINT sg_documentos_sistemas_sistema_fk FOREIGN KEY (dsi_sistema_integrado_fk) 
        REFERENCES sistemas_integrados.sg_sistemas_integrados(sin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_documentos_sistemas_archivo_fk FOREIGN KEY (dsi_documento_fk) 
        REFERENCES public.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_documentos_sistemas_tipo_documento_fk FOREIGN KEY (dsi_tipo_documento_fk) 
        REFERENCES catalogo.sg_inf_tipo_documento(tid_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE  sistemas_integrados.sg_documentos_sistemas                          IS 'Tabla para almacenar los sistemas integrados.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_pk                   IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_sistema_integrado_fk IS 'Llave foránea que hace referencia al sistema integrado.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_descripcion          IS 'Descripción del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_nombre               IS 'Nombre del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_documento_fk         IS 'Llave foránea que hace referencia al documento.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_tipo_documento_fk    IS 'Llave foránea que hace referencia al tipo de documento.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_categoria_documento  IS 'Categoría del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_documentos_sistemas.dsi_version              IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_documentos_sistemas_aud (
    dsi_pk bigint NOT NULL, 
    dsi_sistema_integrado_fk bigint not null, 
    dsi_descripcion character varying(4000),
    dsi_nombre character varying(40),
    dsi_documento_fk bigint,
    dsi_tipo_documento_fk bigint, 
    dsi_categoria_documento character varying(50), 
    dsi_ult_mod_fecha timestamp without TIME zone, 
    dsi_ult_mod_usuario CHARACTER varying(45), 
    dsi_version INTEGER, 
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_documentos_sistemas_aud_pkey PRIMARY KEY (dsi_pk, rev), 
    CONSTRAINT sg_documentos_sistemas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_DOCUMENTO_SISTEMA','SE04',  'Crear un documento de los sistemas integrados', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_DOCUMENTO_SISTEMA','SE05',  'Actualizar un documento de los sistemas integrados', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_DOCUMENTO_SISTEMA','SE06',  'Eliminar un documento de los sistemas integrados', 9, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_SISTEMA_INTEGRADO','SEM01',  'Menu: consulta para los sistemas integrados', 9, true, null, null,0);



--acuerdos del sistema integrado
CREATE SEQUENCE IF NOT EXISTS sistemas_integrados.sg_acuerdos_sistemas_asi_pk_seq 
    INCREMENT 1 
    MINVALUE 1 
    MAXVALUE 9223372036854775807 
    START 1 
    CACHE 1 
    NO CYCLE;
CREATE TABLE IF NOT EXISTS    sistemas_integrados.sg_acuerdos_sistemas(
    asi_pk bigint NOT NULL DEFAULT NEXTVAL('sistemas_integrados.sg_acuerdos_sistemas_asi_pk_seq'::regclass), 
    asi_sistema_integrado_fk bigint not null, 
    asi_tipo_acuerdo_fk bigint not null,
    asi_numero_acuerdo bigint,
    asi_fecha_creacion date,
    asi_estado character varying(25), 
    asi_observaciones character varying(4000),
    asi_ult_mod_fecha timestamp without TIME zone, 
    asi_ult_mod_usuario CHARACTER varying(45), 
    asi_version INTEGER,
    CONSTRAINT sg_acuerdos_sistemas_pkey         PRIMARY KEY (asi_pk),
    CONSTRAINT sg_acuerdos_sistemas_sistema_fk FOREIGN KEY (asi_sistema_integrado_fk) 
        REFERENCES sistemas_integrados.sg_sistemas_integrados(sin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT sg_acuerdos_sistemas_tipo_acuerdo_fk FOREIGN KEY (asi_tipo_acuerdo_fk) 
        REFERENCES catalogo.sg_tipos_acuerdos(tao_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
);
COMMENT ON TABLE  sistemas_integrados.sg_acuerdos_sistemas                          IS 'Tabla para almacenar los sistemas integrados.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_pk                   IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_sistema_integrado_fk IS 'Llave foránea que hace referencia al sistema integrado.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_tipo_acuerdo_fk      IS 'Llave foránea que hace referencia al tipo de acuerdo.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_numero_acuerdo       IS 'Número del acuerdo.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_fecha_creacion       IS 'Fecha de creación del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_estado               IS 'Estado del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_observaciones        IS 'Observaciones del registro.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_ult_mod_fecha        IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_ult_mod_usuario      IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_acuerdos_sistemas.asi_version              IS 'Versión del registro';
CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_acuerdos_sistemas_aud (
    asi_pk bigint NOT NULL, 
    asi_sistema_integrado_fk bigint not null, 
    asi_tipo_acuerdo_fk bigint not null,
    asi_numero_acuerdo bigint,
    asi_fecha_creacion date,
    asi_estado character varying(25), 
    asi_observaciones character varying(4000),
    asi_ult_mod_fecha timestamp without TIME zone, 
    asi_ult_mod_usuario CHARACTER varying(45), 
    asi_version INTEGER,
    rev bigint, 
    revtype smallint, 
    CONSTRAINT sg_acuerdos_sistemas_aud_pkey PRIMARY KEY (asi_pk, rev), 
    CONSTRAINT sg_acuerdos_sistemas_revinfo_fk FOREIGN KEY (rev) REFERENCES public.revinfo(rev)
);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('CREAR_ACUERDO_SISTEMA','SE07',  'Crear un acuerdo del sistema integrado', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ACTUALIZAR_ACUERDO_SISTEMA','SE08',  'Actualizar un acuerdo del sistema integrado', 9, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('ELIMINAR_ACUERDO_SISTEMA','SE09',  'Eliminar un acuerdo del sistema integrado', 9, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ACUERDOS_SISTEMA','SEP6',  'Solapa: acuerdos', 9, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('HISTORIAL_ACUERDO_SISTEMA','SEP7',  'Opción para ver el historial de los acuerdos del sistema', 9, true, null, null,0);

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_ACUERDOS','SEM02',  'Menu: acuerdos', 9, true, null, null,0);


ALTER TABLE sistemas_integrados.sg_sistemas_integrados ADD COLUMN sin_codigo character varying(15);
ALTER TABLE sistemas_integrados.sg_sistemas_integrados_aud ADD COLUMN sin_codigo character varying(15);

--0.2.4

ALTER TABLE sistemas_integrados.sg_sistemas_integrados ADD COLUMN sin_promotor_fk bigint;
ALTER TABLE sistemas_integrados.sg_sistemas_integrados_aud ADD COLUMN sin_promotor_fk bigint;
ALTER TABLE sistemas_integrados.sg_sistemas_integrados ADD CONSTRAINT sg_sistemas_integrados_promotor_fk FOREIGN KEY (sin_promotor_fk) 
        REFERENCES catalogo.sg_si_promotores(pro_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE
        
--0.2.6

ALTER TABLE sistemas_integrados.sg_documentos_sistemas 
drop constraint sg_documentos_sistemas_tipo_documento_fK;


update sistemas_integrados.sg_documentos_sistemas
set dsi_tipo_documento_fk = null;

alter table sistemas_integrados.sg_documentos_sistemas
add constraint sg_documentos_sistemas_tipo_documento_fK 
foreign key (dsi_tipo_documento_fk) 
references catalogo.sg_si_tipo_documento(tid_pk);        

-- 0.3.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_ACTIVIDADES_SISTEMA_INTEGRADO','SEP8', 'Visualizar actividades del sistema integrado', 9, true, null, null,0);

--0.3.1
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_PROYECTOS_FINANCIAMIENTO_SISTEMA_INTEGRADO','SEP9', 'Visualizar proyectos financiamiento del sistema integrado', 9, true, null, null,0);

-- SgProyectoFinanciamientoSistemaIntegrado
CREATE SEQUENCE IF NOT EXISTS sistemas_integrados.sg_sg_rel_proyecto_financiamiento_sistema_integrado_rgp_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado 
(rps_pk bigint NOT NULL DEFAULT nextval('sistemas_integrados.sg_sg_rel_proyecto_financiamiento_sistema_integrado_rgp_pk_seq'::regclass),
rps_sistema_integrado_fk bigint,
rps_proyecto_financiamiento_fk bigint,
rps_habilitado boolean,
rps_ult_mod_fecha timestamp without time zone,
rps_ult_mod_usuario character varying(45),
rps_version integer,
CONSTRAINT sg_rel_proyecto_financiamiento_sistema_integrado_pkey PRIMARY KEY (rps_pk),
CONSTRAINT sg_rps_sistema_integrado_fkey FOREIGN KEY (rps_sistema_integrado_fk) REFERENCES sistemas_integrados.sg_sistemas_integrados(sin_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT rps_proyecto_financiamiento_fkey FOREIGN KEY (rps_proyecto_financiamiento_fk) REFERENCES catalogo.sg_proyecto_financiamiento_sistema_integrado(pfs_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE cascade);

COMMENT ON TABLE sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado IS 'Tabla para el registro de rel acuerdos propuestas pedagógicas.';
COMMENT ON COLUMN sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado.rps_pk IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado.rps_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
COMMENT ON COLUMN sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado.rps_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado.rps_ult_mod_usuario IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado.rps_version IS 'Versión del registro.';

CREATE TABLE IF NOT EXISTS sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado_aud 
(rps_pk bigint NOT NULL,
rps_sistema_integrado_fk bigint,
rps_proyecto_financiamiento_fk bigint,
rps_habilitado boolean,
rps_ult_mod_fecha timestamp without time zone,
rps_ult_mod_usuario character varying(45),
rps_version integer,
rev bigint,
revtype smallint);
ALTER TABLE sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado_aud ADD PRIMARY KEY (rps_pk, rev);

ALTER TABLE sistemas_integrados.sg_rel_proyecto_financiamiento_sistema_integrado ADD CONSTRAINT sg_rel_proyecto_financiamiento_sistema_integrado_uk UNIQUE (rps_sistema_integrado_fk,rps_proyecto_financiamiento_fk);

--0.3.2 
ALTER TABLE sistemas_integrados.sg_sistemas_integrados ADD COLUMN sin_nombre_busqueda CHARACTER VARYING(100);
ALTER TABLE sistemas_integrados.sg_sistemas_integrados_aud ADD COLUMN sin_nombre_busqueda CHARACTER VARYING(100);

update sistemas_integrados.sg_sistemas_integrados set sin_nombre_busqueda=lower(sin_nombre);

--1.0.0

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('VER_INDICADORES_SISTEMA_INTEGRADO','SEP10', 'Visualizar indicadores del sistema integrado', 9, true, null, null,0);


ALTER TABLE centros_educativos.sg_calificaciones ADD COLUMN cal_promedio_calificaciones numeric(10,2) NULL;
ALTER TABLE centros_educativos.sg_calificaciones_aud ADD COLUMN cal_promedio_calificaciones numeric(10,2) NULL;

-- average pre calculado para las califaicciones existentes
with calificaciones_promedio as (select c.cal_pk, AVG( CAST ( ce.cae_calificacion AS numeric(10,2) )) calAvg from centros_educativos.sg_calificaciones c
join centros_educativos.sg_calificaciones_estudiante ce on ce.cae_calificacion_fk=c.cal_pk
group by c.cal_pk
order by c.cal_pk)

update centros_educativos.sg_calificaciones cal
set cal_promedio_calificaciones=c.calAvg
from calificaciones_promedio c 
where c.cal_pk=cal.cal_pk

-- index for tipo plan
CREATE INDEX IF NOT EXISTS sg_tipo_comp_plan_idx ON centros_educativos.sg_componente_plan_estudio USING btree (cpe_tipo) ;

CREATE MATERIALIZED VIEW sistemas_integrados.sg_promedio_calificaciones_si AS

select 
sistemas.sin_pk,h1.sed_pk, h1.cic_pk,h1.niv_pk,h1.gra_pk, h1.cpe_tipo, h1.cpe_pk, h1.cpe_nombre, h1.opc_pk,h1.mat_pk,h1.smo_pk,h1.ped_pk,h1.mod_pk,
AVG(H1.cal_promedio_calificaciones)  from
(select cabezal.cal_pk,rfe_fecha_desde, sed.sed_pk, cic.cic_pk,niv.niv_pk,gra.gra_pk,cpe.cpe_pk, asig_area_asignatura_modulo_fk, mod_area_asignatura_modulo_fk, 
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
                    INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk and rf.rfe_fecha_desde < now())
					where (cpe.cpe_tipo = 'ASI' or cpe.cpe_tipo = 'MOD') and (asig_area_asignatura_modulo_fk = 1 or mod_area_asignatura_modulo_fk = 1) and cabezal.cal_tipo_periodo_calificacion = 'ORD' ) as h1
					
					INNER JOIN
					
					(SELECT * from centros_educativos.sg_sedes sed
					INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)
                                        INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND cal.cal_habilitado order by cal_fecha_inicio desc limit 1))) as h2
					
					ON (h1.sed_pk = h2.sed_pk and h1.rfe_fecha_desde >= h2.cal_fecha_inicio AND  h1.rfe_fecha_desde <= h2.cal_fecha_fin)					
					INNER JOIN sistemas_integrados.sg_sistemas_sedes sistemas ON (sistemas.sed_pk = h1.sed_pk)

					group by sistemas.sin_pk,h1.sed_pk, h1.cic_pk,h1.niv_pk,h1.gra_pk, h1.cpe_tipo, h1.cpe_pk, h1.opc_pk,h1.mat_pk,h1.smo_pk,h1.ped_pk,h1.mod_pk,h1.cpe_nombre;
									

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
                                        INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND cal.cal_habilitado order by cal_fecha_inicio desc limit 1))) as h2
					
					ON (h1.sed_pk = h2.sed_pk and h1.rfe_fecha_desde >= h2.cal_fecha_inicio AND  h1.rfe_fecha_desde <= h2.cal_fecha_fin)					
					LEFT OUTER JOIN sistemas_integrados.sg_sistemas_sedes sistemas ON (sistemas.sed_pk = h1.sed_pk)

					group by sistemas.sin_pk,h1.sed_pk, h1.ocu_pk, h1.niv_pk, h1.cic_pk,h1.gra_pk, h1.cpe_tipo, h1.cpe_pk, h1.opc_pk,h1.mat_pk,h1.smo_pk,h1.ped_pk,h1.mod_pk,h1.cpe_nombre;
																			