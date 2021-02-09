CREATE SCHEMA IF NOT EXISTS seguridad;
CREATE SCHEMA IF NOT EXISTS auditoria;


CREATE SEQUENCE IF NOT EXISTS hibernate_sequence INCREMENT 1 START 1;
CREATE TABLE IF NOT EXISTS REVINFO (rev bigint, revtstmp bigint, CONSTRAINT revinfo_pkey PRIMARY KEY (rev));
INSERT INTO REVINFO (rev, revtstmp) values (nextval('hibernate_sequence'::regclass), extract(epoch FROM CURRENT_TIMESTAMP));


-- Auditoría operaciones. Van en el esquema auditoria..
CREATE SEQUENCE IF NOT EXISTS auditoria.sg_registros_auditoria_aud_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS auditoria.sg_registros_auditoria (aud_pk bigint NOT NULL DEFAULT nextval('sg_registros_auditoria_aud_pk_seq'::regclass), aud_ip character varying(45), aud_clase character varying(255), aud_operacion character varying(255), aud_entidad_id bigint, aud_comentario character varying(1000), aud_resultado_operacion character varying(100), aud_fecha timestamp without time zone, aud_excepcion character varying(1000), aud_codigo_usuario character varying(45), aud_hash_md5 character varying(300), CONSTRAINT sg_registros_auditoria_pkey PRIMARY KEY (aud_pk));

-- Para cada entidad crear, secuencia, tabla, tabla de auditoría.

-- CategoriaOperacion
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_categorias_operacion_cop_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS seguridad.sg_categorias_operacion (cop_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_categorias_operacion_cop_pk_seq'::regclass), cop_codigo character varying(10), cop_nombre character varying(255), cop_descripcion character varying(500), cop_habilitado boolean, cop_ult_mod_fecha timestamp without time zone, cop_ult_mod_usuario character varying(45), cop_version integer, CONSTRAINT sg_categorias_operacion_pkey PRIMARY KEY (cop_pk), CONSTRAINT cop_codigo_uk UNIQUE (cop_codigo));
    COMMENT ON TABLE  seguridad.sg_categorias_operacion IS 'Tabla para el registro de categorías de operación.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_codigo IS 'Código del registro.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_categorias_operacion.cop_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS seguridad.sg_categorias_operacion_aud (cop_pk bigint NOT NULL, cop_codigo character varying(10), cop_nombre character varying(255), cop_descripcion character varying(500), cop_habilitado boolean, cop_ult_mod_fecha timestamp without time zone, cop_ult_mod_usuario character varying(45), cop_version integer, rev bigint, revtype smallint);


-- Operacion
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_operaciones_ope_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE    TABLE IF NOT EXISTS seguridad.sg_operaciones (ope_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_operaciones_ope_pk_seq'::regclass), ope_codigo character varying(10), ope_nombre character varying(255), ope_descripcion character varying(500), ope_categoria_operacion_fk bigint, ope_habilitado boolean, ope_ult_mod_fecha timestamp without time zone, ope_ult_mod_usuario character varying(45), ope_version integer, CONSTRAINT sg_operaciones_pkey PRIMARY KEY (ope_pk), CONSTRAINT ope_codigo_uk UNIQUE (ope_codigo),CONSTRAINT sg_operaciones_categoria_operacion_fkey FOREIGN KEY (ope_categoria_operacion_fk) REFERENCES seguridad.sg_categorias_operacion(cop_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  seguridad.sg_operaciones IS 'Tabla para el registro de operaciones.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_codigo IS 'Código del registro.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_categoria_operacion_fk IS 'Clave foránea que hace referencia a la categoría de operación.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_operaciones.ope_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS seguridad.sg_operaciones_aud (ope_pk bigint NOT NULL, ope_codigo character varying(10), ope_nombre character varying(255), ope_descripcion character varying(500), ope_categoria_operacion_fk bigint, ope_habilitado boolean, ope_ult_mod_fecha timestamp without time zone, ope_ult_mod_usuario character varying(45), ope_version integer, rev bigint, revtype smallint);

-- Rol
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_roles_rol_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE    TABLE IF NOT EXISTS seguridad.sg_roles (rol_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_roles_rol_pk_seq'::regclass), rol_codigo character varying(10), rol_nombre character varying(255), rol_descripcion character varying(500),rol_habilitado boolean, rol_ult_mod_fecha timestamp without time zone, rol_ult_mod_usuario character varying(45), rol_version integer, CONSTRAINT sg_roles_pkey PRIMARY KEY (rol_pk), CONSTRAINT rol_codigo_uk UNIQUE (rol_codigo));
    COMMENT ON TABLE  seguridad.sg_roles IS 'Tabla para el registro de los roles.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_codigo IS 'Código del registro.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_descripcion IS 'Descripción del registro.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN seguridad.sg_roles.rol_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_roles.rol_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS seguridad.sg_roles_aud (rol_pk bigint NOT NULL, rol_codigo character varying(10), rol_nombre character varying(255), rol_descripcion character varying(500),rol_habilitado boolean, rol_ult_mod_fecha timestamp without time zone, rol_ult_mod_usuario character varying(45), rol_version integer, rev bigint, revtype smallint);

-- RolOperacion
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_roles_operacion_rop_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE    TABLE IF NOT EXISTS seguridad.sg_roles_operacion (rop_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_roles_operacion_rop_pk_seq'::regclass), rop_operacion_fk bigint, rop_rol_fk bigint, rop_cascada boolean, rop_habilitado boolean, rop_ult_mod_fecha timestamp without time zone, rop_ult_mod_usuario character varying(45), rop_version integer, CONSTRAINT sg_roles_operacion_pkey PRIMARY KEY (rop_pk),CONSTRAINT sg_roles_operacion_ope_fkey FOREIGN KEY (rop_operacion_fk) REFERENCES seguridad.sg_operaciones(ope_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE,CONSTRAINT sg_roles_operacion_rol_fkey FOREIGN KEY (rop_rol_fk) REFERENCES seguridad.sg_roles(rol_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE);
    COMMENT ON TABLE  seguridad.sg_roles_operacion IS 'Tabla para el registro de la relación entre roles y operaciones.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_operacion_fk IS 'Clave foránea que hace referencia a la operación.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_rol_fk IS 'Clave foránea que hace referencia al rol.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_cascada IS 'Cascada.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_roles_operacion.rop_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS seguridad.sg_roles_operacion_aud (rop_pk bigint NOT NULL, rop_operacion_fk bigint, rop_rol_fk bigint, rop_cascada boolean, rop_habilitado boolean, rop_ult_mod_fecha timestamp without time zone, rop_ult_mod_usuario character varying(45), rop_version integer, rev bigint, revtype smallint);


-- Usuario
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_usuarios_usu_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE    TABLE IF NOT EXISTS seguridad.sg_usuarios (usu_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_usuarios_usu_pk_seq'::regclass), usu_codigo CHARACTER VARYING(50), usu_nombre CHARACTER VARYING(255), usu_habilitado boolean, usu_ult_mod_fecha timestamp without time zone, usu_ult_mod_usuario character varying(45), usu_version integer,  usu_password character varying(45),CONSTRAINT sg_usuarios_pkey PRIMARY KEY (usu_pk), CONSTRAINT usu_codigo_uk UNIQUE (usu_codigo));
    COMMENT ON TABLE  seguridad.sg_usuarios IS 'Tabla para el registro de usuarios.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_codigo IS 'Código del registro.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_nombre IS 'Nombre del registro.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_habilitado IS 'Indica si el registro se encuentra habilitado(true) o inhabilitado(false).';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_version IS 'Versión del registro.';
    COMMENT ON COLUMN seguridad.sg_usuarios.usu_password IS 'El password del usuario';

CREATE TABLE IF NOT EXISTS seguridad.sg_usuarios_aud (usu_pk bigint NOT NULL, usu_codigo CHARACTER VARYING(50), usu_nombre CHARACTER VARYING(255), usu_habilitado boolean, usu_ult_mod_fecha timestamp without time zone, usu_ult_mod_usuario character varying(45), usu_version integer,usu_password character varying(45), rev bigint, revtype smallint);




-- Contexto
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_contextos_con_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS seguridad.sg_contextos (con_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_contextos_con_pk_seq'::regclass), con_ambito CHARACTER VARYING(50), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer,con_contexto_id bigint, CONSTRAINT sg_contextos_pkey PRIMARY KEY (con_pk)) ;
    COMMENT ON TABLE  seguridad.sg_contextos                         IS 'Tabla para el registro de los contextos.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_pk                  IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_ambito                  IS 'Tipo del ambito asociado al contexto con contexto?id.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_version             IS 'Versión del registro.';
    COMMENT ON COLUMN seguridad.sg_contextos.con_contexto_id             IS 'El id del conexto ejemplo Sección, Depratamental, etc.';

CREATE TABLE IF NOT EXISTS seguridad.sg_contextos_aud (con_pk bigint NOT NULL, con_ambito CHARACTER VARYING(50), con_ult_mod_fecha timestamp without time zone, con_ult_mod_usuario character varying(45), con_version integer,con_contexto_id bigint, rev bigint, revtype smallint);


CREATE UNIQUE INDEX IF NOT EXISTS sg_contexto_pk_idx                    ON seguridad.sg_contextos              USING btree (con_pk) ;



-- Usuario-Rol
CREATE SEQUENCE IF NOT EXISTS seguridad.sg_usuario_rol_pur_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE    TABLE IF NOT EXISTS seguridad.sg_usuario_rol (pur_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_usuario_rol_pur_pk_seq'::regclass), pur_usuario_fk bigint, pur_rol_fk bigint, pur_contexto_fk bigint, pur_ult_mod_fecha timestamp without time zone, pur_ult_mod_usuario character varying(45), pur_version integer, CONSTRAINT sg_personas_usuario_rol_pkey PRIMARY KEY (pur_pk), CONSTRAINT sg_personas_usuario_rol_fk FOREIGN KEY (pur_rol_fk) REFERENCES seguridad.sg_roles(rol_pk), CONSTRAINT sg_personas_usuario_contexto_fk FOREIGN KEY (pur_contexto_fk) REFERENCES seguridad.sg_contextos(con_pk),CONSTRAINT sg_usuario_fk FOREIGN KEY (pur_usuario_fk) REFERENCES seguridad.sg_usuarios(usu_pk));
    COMMENT ON TABLE  seguridad.sg_usuario_rol IS 'Tabla para el registro de la relación Persona Usuaria y rol.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_pk IS 'Número correlativo autogenerado.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_usuario_fk IS 'Clave foránea que hace referencia al usuario.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_rol_fk IS 'Clave foránea que hace referencia al rol.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_contexto_fk IS 'Clave foránea que hace referencia al contexto y ambito.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_ult_mod_fecha IS 'Última fecha en la que se modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_ult_mod_usuario IS 'Último usuario que modificó el registro.';
    COMMENT ON COLUMN seguridad.sg_usuario_rol.pur_version IS 'Versión del registro.';
CREATE TABLE IF NOT EXISTS seguridad.sg_usuario_rol_aud (pur_pk bigint NOT NULL, pur_usuario_fk bigint, pur_rol_fk bigint,  pur_contexto_fk bigint, pur_ult_mod_fecha timestamp without time zone, pur_ult_mod_usuario character varying(45), pur_version integer, rev bigint, revtype smallint);

create or replace view seguridad.permisos as select 
u.usu_codigo as usuario_codigo,
pur_usuario_fk as usuario_id , 
rol.rol_codigo as rol_codigo, 
rol.rol_nombre as rol_nombre,
op.ope_codigo as operacion_codigo,
op.ope_nombre as operacion_nombre,
rop.rop_cascada as operacion_cascada, 
ctx.con_ambito as ambito, 
ctx.con_contexto_id as contexto
from 
seguridad.sg_roles rol, 
seguridad.sg_contextos ctx, 
seguridad.sg_operaciones op,
seguridad.sg_roles_operacion rop,
seguridad.sg_usuario_rol urol,
seguridad.sg_usuarios u
where 
urol.pur_contexto_fk = ctx.con_pk and
urol.pur_rol_fk = rol.rol_pk and
urol.pur_usuario_fk = u.usu_pk and
rop.rop_operacion_fk = op.ope_pk and
rop.rop_rol_fk = rol.rol_pk and
rol.rol_habilitado = true and
op.ope_habilitado = true and
u.usu_habilitado = true;

create table seguridad.sg_usuarios_preguntas (id bigserial, usu_codigo varchar(255), question varchar(455), answer varchar(455));

ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_email CHARACTER varying(255);
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_email CHARACTER varying(255);

ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_foto_fk bigint;
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_foto_fk bigint;

ALTER TABLE seguridad.sg_usuarios ADD CONSTRAINT sg_usuarios_foto_fk FOREIGN KEY (usu_foto_fk) REFERENCES centros_educativos.sg_archivos(ach_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE seguridad.sg_usuarios ALTER COLUMN usu_password TYPE character varying(120);
ALTER TABLE seguridad.sg_usuarios_aud ALTER COLUMN usu_password TYPE character varying(120);


ALTER TABLE seguridad.sg_categorias_operacion_aud	 ADD CONSTRAINT 	categorias_operacion_revinfo_fk	 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_contextos_aud                   ADD CONSTRAINT 	contextos_revinfo_fk             FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_operaciones_aud                 ADD CONSTRAINT 	operaciones_revinfo_fk           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_roles_aud                       ADD CONSTRAINT 	roles_revinfo_fk                 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_roles_operacion_aud             ADD CONSTRAINT 	roles_operacion_revinfo_fk	 FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_usuario_rol_aud                 ADD CONSTRAINT 	usuario_rol_revinfo_fk           FOREIGN KEY (rev) REFERENCES public.revinfo(rev);
ALTER TABLE seguridad.sg_usuarios_aud                    ADD CONSTRAINT 	usuarios_revinfo_fk              FOREIGN KEY (rev) REFERENCES public.revinfo(rev);

ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_acepta_terminos boolean;
COMMENT ON COLUMN seguridad.sg_usuarios.usu_acepta_terminos IS 'El usuario acepta los términos y condiciones de uso de SIGES.';
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_acepta_terminos boolean;

ALTER TABLE seguridad.sg_categorias_operacion ADD CONSTRAINT sg_categoria_operacion_nombre_uk UNIQUE (cop_nombre);
ALTER TABLE seguridad.sg_operaciones ADD CONSTRAINT sg_operacion_nombre_uk UNIQUE (ope_nombre);
ALTER TABLE seguridad.sg_roles ADD CONSTRAINT sg_rol_nombre_uk UNIQUE (rol_nombre);

ALTER TABLE seguridad.sg_roles_operacion ADD CONSTRAINT sg_roles_operacion_rol_operacion_uk UNIQUE (rop_operacion_fk, rop_rol_fk);

---2.4.0

ALTER TABLE seguridad.sg_roles ADD COLUMN rol_delegable boolean;
ALTER TABLE seguridad.sg_roles_aud ADD COLUMN rol_delegable boolean;

ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_super_usuario boolean;
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_super_usuario boolean;

UPDATE seguridad.sg_usuarios SET usu_super_usuario= 'false';
UPDATE seguridad.sg_usuarios SET usu_super_usuario= 'true' where usu_codigo='casuser';
UPDATE seguridad.sg_roles set rol_delegable = true;

UPDATE seguridad.sg_roles set rol_delegable = false where rol_codigo like 'ADM%'; 

-- 3.0.0
INSERT INTO seguridad.sg_roles(
	rol_codigo, rol_nombre, rol_habilitado, rol_ult_mod_fecha, rol_ult_mod_usuario, rol_version, rol_delegable)
	VALUES ('PADRE', 'Padres', true, now(), 'casuser', 0, false);

create or replace view seguridad.permisos as select 
u.usu_codigo as usuario_codigo,
pur_usuario_fk as usuario_id , 
rol.rol_codigo as rol_codigo, 
rol.rol_nombre as rol_nombre,
op.ope_codigo as operacion_codigo,
op.ope_nombre as operacion_nombre,
rop.rop_cascada as operacion_cascada, 
ctx.con_ambito as ambito, 
ctx.con_contexto_id as contexto,
op.ope_categoria_operacion_fk as operacion_categoria
from 
seguridad.sg_roles rol, 
seguridad.sg_contextos ctx, 
seguridad.sg_operaciones op,
seguridad.sg_roles_operacion rop,
seguridad.sg_usuario_rol urol,
seguridad.sg_usuarios u
where 
urol.pur_contexto_fk = ctx.con_pk and
urol.pur_rol_fk = rol.rol_pk and
urol.pur_usuario_fk = u.usu_pk and
rop.rop_operacion_fk = op.ope_pk and
rop.rop_rol_fk = rol.rol_pk and
rol.rol_habilitado = true and
op.ope_habilitado = true and
u.usu_habilitado = true;


-- 3.2.0
ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_persona_pk bigint;
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_persona_pk bigint;
ALTER TABLE seguridad.sg_usuarios ADD CONSTRAINT sg_usuario_persona_uk UNIQUE (usu_persona_pk);

CREATE OR REPLACE FUNCTION seguridad.insertar_usuario_responsable_trigger() RETURNS TRIGGER AS
$BODY$
begin
        if new.usu_persona_pk is not null then
		update centros_educativos.sg_personas set per_usuario_pk = new.usu_pk where per_pk=new.usu_persona_pk;
	end if;
	RETURN new; 
    
END;
$BODY$
language plpgsql;

CREATE TRIGGER insertar_usuario_responsable_trigger
AFTER INSERT on seguridad.sg_usuarios
FOR EACH ROW
EXECUTE PROCEDURE seguridad.insertar_usuario_responsable_trigger();


--3.3.0

ALTER TABLE seguridad.sg_operaciones ALTER COLUMN ope_descripcion TYPE text;
ALTER TABLE seguridad.sg_operaciones_aud ALTER COLUMN ope_descripcion TYPE text;


-- 3.3.1

ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_personal_pk bigint;
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_personal_pk bigint;

CREATE OR REPLACE FUNCTION seguridad.insertar_usuario_personal_trigger() RETURNS TRIGGER AS
$BODY$
declare
	personal_pk bigint;
	usuario_per_pk bigint;
begin
        if new.usu_persona_pk is not null then
        	usuario_per_pk:=new.usu_persona_pk;
        	personal_pk:=(select pse_pk from centros_educativos.sg_personal_sede_educativa where pse_persona_fk=usuario_per_pk limit 1 );
        	
        	if personal_pk is not null then
        		update seguridad.sg_usuarios  set usu_personal_pk =personal_pk where usu_pk=new.usu_pk ;
		
		end if;
		end if;
		RETURN new; 
    
END;
$BODY$
language plpgsql;

CREATE TRIGGER insertar_usuario_personal_trigger
AFTER INSERT on seguridad.sg_usuarios
FOR EACH ROW
EXECUTE PROCEDURE seguridad.insertar_usuario_personal_trigger();

--FUNCION PARA QUE TODOS LOS USUARIO SIN PERSONA_PK SEAN ACTUALIZADOS
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

--Función anónima,los usuarios que tienen cargado usu_persona_pk, se les crea usuario_rol, donde rol es Autogestión y contexto tiene id persona_pk

DO $$
declare


usuarios_con_persona seguridad.sg_usuarios%ROWTYPE;
nuevo_contexto seguridad.sg_contextos%ROWTYPE;

per_pk_aux bigint;
personal_pk_aux bigint;
rol_pk_aux bigint;
pur_pk_aux bigint;
con_pk_aux bigint;

BEGIN

rol_pk_aux:=(select rol_pk from seguridad.sg_roles where rol_codigo='AUTOGEST');

if rol_pk_aux is null then
	INSERT INTO seguridad.sg_roles
	(rol_codigo, rol_nombre, rol_descripcion, rol_habilitado, rol_version, rol_delegable)
	VALUES('AUTOGEST', 'Autogestión', '', true,  0, true) returning rol_pk into rol_pk_aux;

end if;


FOR usuarios_con_persona IN(

		select * from seguridad.sg_usuarios usu 
		where usu.usu_persona_pk is not null and usu_pk not in (select pur_usuario_fk from seguridad.sg_usuario_rol where pur_rol_fk= rol_pk_aux)
	
		

) loop
	RAISE NOTICE '----------- usuario (%)----',usuarios_con_persona.usu_pk;
        pur_pk_aux:=(select pur_pk from seguridad.sg_usuario_rol where pur_usuario_fk=usuarios_con_persona.usu_pk and pur_rol_fk=rol_pk_aux);
	

	if pur_pk_aux is null then
		INSERT INTO seguridad.sg_contextos
						(con_ambito,con_contexto_id,con_ult_mod_fecha,con_version)
						VALUES('PERSONA', usuarios_con_persona.usu_persona_pk,now(),0) returning con_pk into con_pk_aux;
                RAISE NOTICE '----------- contexto (%)----',con_pk_aux;
	
		INSERT INTO seguridad.sg_usuario_rol
		(pur_usuario_fk, pur_rol_fk, pur_contexto_fk,pur_ult_mod_fecha,pur_ult_mod_usuario, pur_version)
		VALUES(usuarios_con_persona.usu_pk, rol_pk_aux,con_pk_aux ,now(),'autogestion', 0);
		
	end if;	

END LOOP;


END$$;


CREATE OR REPLACE FUNCTION seguridad.insertar_usuario_persona_rol_autogestion() RETURNS TRIGGER AS
$BODY$
declare
	personal_pk bigint;
	usuario_per_pk bigint;
	rol_pk_aux bigint;
	con_pk_aux bigint;
begin
        if new.usu_persona_pk is not null then
        	rol_pk_aux:=(select rol_pk from seguridad.sg_roles where rol_codigo='AUTOGEST');
        	usuario_per_pk:=new.usu_persona_pk;
        	INSERT INTO seguridad.sg_contextos
						(con_ambito,con_contexto_id,con_ult_mod_fecha,con_version)
						VALUES('PERSONA',usuario_per_pk,now(),0) returning con_pk into con_pk_aux;
			INSERT INTO seguridad.sg_usuario_rol
			(pur_usuario_fk, pur_rol_fk, pur_contexto_fk,pur_ult_mod_fecha,pur_ult_mod_usuario, pur_version)
			VALUES(new.usu_pk, rol_pk_aux,con_pk_aux ,now(),'autogestion_t', 0);
		
		end if;

		RETURN new; 
    
END;
$BODY$
language plpgsql;

CREATE TRIGGER insertar_usuario_persona_rol_autogestion_trigger
AFTER INSERT on seguridad.sg_usuarios
FOR EACH ROW
EXECUTE PROCEDURE seguridad.insertar_usuario_persona_rol_autogestion();

INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_CATALOGO','W1',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_CENTROS_EDUCATIVOS','W2',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_SEGURIDAD','W3',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_SIMPLE','W4',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_ACTIVO_FIJO','W5',  '', 4, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('TIENE_ACCESO_ESTADISTICAS','W6',  '', 4, true, null, null,0);

-- 3.6.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_AUDITORIA_LOGIN','SM1',  '', 3, true, null, null,0);
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('MENU_AUDITORIA','SM2',  '', 3, true, null, null,0);


CREATE INDEX IF NOT EXISTS sg_registros_auditoria_usuario_idx ON auditoria.sg_registros_auditoria USING btree (aud_codigo_usuario);

CREATE INDEX sg_usuarios_codigo_idx ON seguridad.sg_usuarios USING btree (usu_codigo);
CREATE INDEX sg_usu_rol_usuario_fk_idx ON seguridad.sg_usuario_rol USING btree (pur_usuario_fk);
CREATE INDEX sg_usu_rol_rol_fk_idx ON seguridad.sg_usuario_rol USING btree (pur_rol_fk);
CREATE INDEX sg_usu_rol_contexto_fk_idx ON seguridad.sg_usuario_rol USING btree (pur_contexto_fk);
CREATE INDEX sg_rol_ope_rol_fk_idx ON seguridad.sg_roles_operacion USING btree (rop_rol_fk);
CREATE INDEX sg_rol_ope_ope_fk_idx ON seguridad.sg_roles_operacion USING btree (rop_operacion_fk);


-- 3.8.0



CREATE SEQUENCE IF NOT EXISTS seguridad.sg_reglas_contextos_rec_pk_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE TABLE IF NOT EXISTS seguridad.sg_reglas_contextos (rec_pk bigint NOT NULL DEFAULT nextval('seguridad.sg_reglas_contextos_rec_pk_seq'::regclass),
 rec_nombre CHARACTER VARYING(255),
 rec_regla CHARACTER VARYING(5000),
 rec_ult_mod_fecha timestamp without time zone,
 rec_ult_mod_usuario character varying(45),
 rec_version integer,
 CONSTRAINT sg_reglas_contextos_pkey PRIMARY KEY (rec_pk)) ;

COMMENT ON TABLE  seguridad.sg_reglas_contextos                         IS 'Tabla para el registro de las reglas de contextos.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_pk                  IS 'Número correlativo autogenerado.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_ult_mod_fecha       IS 'Última fecha en la que se modificó el registro.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_ult_mod_usuario     IS 'Último usuario que modificó el registro.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_version             IS 'Versión del registro.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_nombre             IS 'Nombre del registro.';
COMMENT ON COLUMN seguridad.sg_reglas_contextos.rec_regla             IS 'Consulta sql para aplicar la regla.';

CREATE TABLE IF NOT EXISTS seguridad.sg_reglas_contextos_aud (rec_pk bigint NOT NULL,
 rec_nombre CHARACTER VARYING(255),
 rec_regla CHARACTER VARYING(5000),
 rec_ult_mod_fecha timestamp without time zone,
 rec_ult_mod_usuario character varying(45),
 rec_version integer,
 rev bigint,
 revtype smallint);


ALTER TABLE seguridad.sg_contextos ADD COLUMN con_regla_fk bigint;
ALTER TABLE seguridad.sg_contextos_aud ADD COLUMN con_regla_fk bigint;

ALTER TABLE seguridad.sg_contextos ADD CONSTRAINT sg_contexto_regla_fk FOREIGN KEY (con_regla_fk) REFERENCES seguridad.sg_reglas_contextos(rec_pk) MATCH FULL ON DELETE RESTRICT ON UPDATE CASCADE;


create or replace view seguridad.permisos as select 
u.usu_codigo as usuario_codigo,
pur_usuario_fk as usuario_id , 
rol.rol_codigo as rol_codigo, 
rol.rol_nombre as rol_nombre,
op.ope_codigo as operacion_codigo,
op.ope_nombre as operacion_nombre,
rop.rop_cascada as operacion_cascada, 
ctx.con_ambito as ambito, 
ctx.con_contexto_id as contexto,
op.ope_categoria_operacion_fk as operacion_categoria,
rc.rec_regla as regla
from 
seguridad.sg_roles rol, 
seguridad.sg_contextos ctx LEFT OUTER JOIN seguridad.sg_reglas_contextos rc ON (ctx.con_regla_fk = rc.rec_pk), 
seguridad.sg_operaciones op,
seguridad.sg_roles_operacion rop,
seguridad.sg_usuario_rol urol,
seguridad.sg_usuarios u
where 
urol.pur_contexto_fk = ctx.con_pk and
urol.pur_rol_fk = rol.rol_pk and
urol.pur_usuario_fk = u.usu_pk and
rop.rop_operacion_fk = op.ope_pk and
rop.rop_rol_fk = rol.rol_pk and
rol.rol_habilitado = true and
op.ope_habilitado = true and
u.usu_habilitado = true;

-- 3.9.0
INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('AGREGAR_USUARIO_A_PENTAHO','S22', '', 3, true, null, null,0);
ALTER TABLE seguridad.sg_usuarios ADD COLUMN usu_enviado_pentaho boolean default false;
ALTER TABLE seguridad.sg_usuarios_aud ADD COLUMN usu_enviado_pentaho boolean default false;

--- CAS PASSWORD EXPIRE - MVILCHE
ALTER TABLE seguridad.sg_usuarios ADD usu_pass_expired int4 NOT NULL DEFAULT 0;

-- 3.13.0


INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('ROLES_AMBITO_PERSONA', 'Códigos de roles que pueden ser asignados a ámbito persona', 'codigos de roles que pueden ser asignados a ambito persona', 0, 'AUTOGEST|PADRE', false);


UPDATE seguridad.sg_operaciones set ope_habilitado = false where ope_codigo IN ('S19', 'S20', 'S21'); -- borrar luego

-- 3.15.0

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('GENERACION_CERTIFICADOS_ACTIVADA', 'Generación de certificados para usuarios activada', 'generación de certificados para usuarios activada', 0, 'false', false);

INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('DOMINIO_MAIL_GENERACION_CERTIFICADOS', 'Dominio de correo electrónico al que se le generan certificados', 'dominio de correo electronico al que se le generan certificados', 0, 'mined.edu.sv', false);


INSERT INTO catalogo.sg_configuraciones (con_codigo, con_nombre, con_nombre_busqueda,  con_version, con_valor, con_es_editor) 
VALUES('TEXTO_CERTIFICADO_AUTOGESTION', 'Texto que se muestra en pantalla de generación de certificados en autogestión', 'texto que se muestra en pantalla de generacion de certificados en autogestion', 0, 'Al generar un certificado, todos sus certificados anteriores serán revocados.', false);


INSERT INTO seguridad.sg_operaciones(ope_nombre, ope_codigo, ope_descripcion, ope_categoria_operacion_fk, ope_habilitado, ope_ult_mod_fecha, ope_ult_mod_usuario, ope_version) VALUES ('REVOCAR_CERTIFICADOS_USUARIO','S23',  '', 3, true, null, null,0);


DROP TRIGGER insertar_usuario_responsable_trigger ON seguridad.sg_usuarios;

CREATE TRIGGER insertar_usuario_responsable_trigger
AFTER INSERT OR UPDATE OF usu_persona_pk on seguridad.sg_usuarios
FOR EACH ROW
EXECUTE PROCEDURE seguridad.insertar_usuario_responsable_trigger();

DROP TRIGGER insertar_usuario_personal_trigger ON seguridad.sg_usuarios;

CREATE TRIGGER insertar_usuario_personal_trigger
AFTER INSERT OR UPDATE OF usu_persona_pk on seguridad.sg_usuarios
FOR EACH ROW
WHEN (pg_trigger_depth() = 0)
EXECUTE PROCEDURE seguridad.insertar_usuario_personal_trigger();


UPDATE seguridad.sg_usuarios set usu_personal_pk = (select pse_pk from centros_educativos.sg_personal_sede_educativa where pse_persona_fk=usu_persona_pk limit 1 )
where usu_persona_pk is not null and usu_personal_pk is null
and exists (select 1 from centros_educativos.sg_personal_sede_educativa where pse_persona_fk = usu_persona_pk);

update centros_educativos.sg_personas set per_usuario_pk = (select usu_pk from seguridad.sg_usuarios where usu_persona_pk = per_pk)
where EXISTS (select usu_pk from seguridad.sg_usuarios where usu_persona_pk = per_pk) and per_usuario_pk is null;

-- 3.15.5
ALTER TABLE seguridad.sg_usuarios add column usu_pass_expired integer;
ALTER TABLE seguridad.sg_usuarios_aud add column usu_pass_expired integer;

ALTER TABLE seguridad.sg_usuarios add column usu_update_pass  timestamp without time zone;
ALTER TABLE seguridad.sg_usuarios_aud add column usu_update_pass  timestamp without time zone;
