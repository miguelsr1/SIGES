INSERT INTO ss_oficina (ofi_id,ofi_nombre,ofi_fecha_creacion,ofi_origen,ofi_usuario,ofi_version)

VALUES

  (1,'SOFIS',now(),'init',1,1);

INSERT INTO ss_rol

(rol_id,rol_cod,rol_nombre,rol_vigente,rol_descripcion,rol_user_code,rol_origen,rol_version)

VALUES

  (1,'admin','Administrador',1,'El Administrador',1,'SS-ADMINIS-WEB',0);

INSERT INTO ss_usuario

(usu_id,usu_correo_electronico,usu_cod,usu_user_code,usu_origen,usu_version,usu_descripcion,usu_vigente,usu_cambio_estado_desc,usu_primer_nombre,usu_primer_apellido,usu_segundo_nombre,usu_segundo_apellido,usu_nro_doc,usu_telefono,usu_direccion,usu_oficina_por_defecto,usu_fecha_password,usu_administrador,usu_foto,usu_registrado,usu_password)

VALUES

  (1,'email','admin',1,'SS-ADMINIS-WEB',1,'Un

administrador',1,NULL,'Admin','Sofis','','','0','El telefono','La

direccion',1,'2010-02-03 00:00:00',1,NULL,1,'TxhG/pRA+eaFnd98BBl7RA==');


INSERT INTO ss_usu_ofi_roles

(usu_ofi_roles_id,usu_ofi_roles_usuario,usu_ofi_roles_oficina,usu_ofi_roles_rol,usu_ofi_roles_user_code,usu_ofi_roles_origen)

VALUES

  (1,1,1,1,1,'SS-ADMINIS-WEB');

--Operaciones de ejemplo
INSERT INTO `aladi_db`.`ss_categoper` (`cat_descripcion`, `cat_nombre`, `cat_origen`, `cat_user_code`, `cat_version`, `cat_vigente`) VALUES ('CatInicial', 'Categoría Inicial', 'SCRIPT-INICIAL', '0', '0', '1');

INSERT INTO ss_operacion
(ope_codigo,ope_descripcion,ope_nombre,ope_origen,ope_version,ope_vigente,ope_categoria_id,ope_tipocampo,ope_user_code)
VALUES
('ADMINIS','Administración','ADM','SCRIPT-INICIAL',0,1,1,0,0),
('ADMIN_TDO','Administración tipo documento','ADM Tipo Docuemtno','SCRIPT-INICIAL',0,1,1,0,0),
('ADMIN_ERR','Administración Errores','ADM Errores','SCRIPT-INICIAL',0,1,1,0,0),
('ADMIN_TEL','Administración tipos de teléfonos','ADM Tipo Teléfono','SCRIPT-INICIAL',0,1,1,0,0),
('ADM_CONF','Administración Configuración','ADM Configuración','SCRIPT-INICIAL',0,1,1,0,0),
('ADMIN_AYU','Administración Ayuda','ADM Ayuda','SCRIPT-INICIAL',0,1,1,0,0),
('ADMIN_NOT','Administración Noticias','ADM Noticias','SCRIPT-INICIAL',0,1,1,0,0);


INSERT INTO ss_rel_rol_operacion
(rel_rol_operacion_operacion_id,rel_rol_operacion_rol_id,rel_rol_operacion_editable,rel_rol_operacion_visible,rel_rol_operacion_origen,rel_rol_operacion_user_code)
VALUES
(1,1,1,1,'SCRIPT_INICIAL',0),
(2,1,1,1,'SCRIPT_INICIAL',0),
(3,1,1,1,'SCRIPT_INICIAL',0),
(4,1,1,1,'SCRIPT_INICIAL',0),
(5,1,1,1,'SCRIPT_INICIAL',0),
(6,1,1,1,'SCRIPT_INICIAL',0),
(7,1,1,1,'SCRIPT_INICIAL',0);




INSERT INTO ss_idiomas VALUES (1,'ES','Español',1,'2015-03-23 18:06:08',NULL,NULL,1);

