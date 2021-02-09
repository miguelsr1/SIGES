-- borra calificaciones_estudiante con estudiante_fk y calificacion_fk repetido
DO $$
declare

cal_estudiantes centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
cal_estudiantes_auxiliar centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
vAux varchar;
contador int;
pk_mayor bigint;
begin
	contador:=0;
	for cal_estudiantes in(
		select * from centros_educativos.sg_calificaciones_estudiante calest
		where  (select count(*) from centros_educativos.sg_calificaciones_estudiante calest_aux where calest.cae_calificacion_fk =calest_aux.cae_calificacion_fk and
		calest.cae_estudiante_fk=calest_aux.cae_estudiante_fk and calest.cae_pk!=calest_aux.cae_pk) >1
	

	)loop	
				
				RAISE NOTICE '----------- CALIFICACION ORIGINAL (%)----',cal_estudiantes;
				delete from centros_educativos.sg_calificaciones_estudiante where cae_pk=cal_estudiantes.cae_pk;
				contador:=contador+1;
		
	end loop;
	RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;

ALTER TABLE centros_educativos.sg_calificaciones_estudiante ADD CONSTRAINT cae_estudiante_fk_calificacion_fk UNIQUE (cae_estudiante_fk, cae_calificacion_fk)



--SCRIPT BORRA CALIFICACIONES QUE NO CORRESPONDEN AL PERIODO DE CALIFICACION
DO $$
declare

cal_estudiantes centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
cal_estudiantes_auxiliar centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
calificaciones centros_educativos.sg_calificaciones%ROWTYPE;
vAux varchar;
contador int;
pk_mayor bigint;
begin
	contador:=0;
	for calificaciones in(
		select * from centros_educativos.sg_calificaciones calif 
		inner join centros_educativos.sg_componente_plan_estudio cpe on cpe.cpe_pk=calif.cal_componente_plan_estudio_fk
		inner join centros_educativos.sg_secciones sec on sec.sec_pk=calif.cal_seccion_fk
		inner join centros_educativos.sg_servicio_educativo ser on sec.sec_servicio_educativo_fk=ser.sdu_pk
		inner join centros_educativos.sg_grados gra on gra.gra_pk= ser.sdu_grado_fk
		inner join centros_educativos.sg_planes_estudio pla on pla.pes_pk=sec.sec_plan_estudio_fk
		left outer join centros_educativos.sg_rango_fecha rango on rango.rfe_pk=calif.cal_rango_fecha_fk
		inner join centros_educativos.sg_periodos_calificacion percal on percal.pca_pk=rango.rfe_periodo_calificacion_fk
		inner join centros_educativos.sg_rel_mod_ed_mod_aten relmod on relmod.rea_pk=pla.pes_relacion_modalidad_fk
		inner join catalogo.sg_modalidades_atencion modaten on relmod.rea_modalidad_atencion_fk=modaten.mat_pk
		inner join centros_educativos.sg_modalidades modalidad on modalidad.mod_pk=relmod.rea_modalidad_educativa_fk
		where 
		((select count(*) from centros_educativos.sg_componente_plan_grado compgra 
		where compgra.cpg_componente_plan_estudio_fk=cpe.cpe_pk and 
		compgra.cpg_grado_fk=gra.gra_pk and
		compgra.cpg_plan_estudio_fk=pla.pes_pk and 
		compgra.cpg_periodos_calificacion != percal.pca_numero)>=1) 

	)loop	
				
				RAISE NOTICE '----------- CALIFICACION (%)----',calificaciones;
				delete from centros_educativos.sg_calificaciones_estudiante where cae_calificacion_fk=calificaciones.cal_pk;
				delete from centros_educativos.sg_calificaciones where cal_pk=calificaciones.cal_pk;
				contador:=contador+1;
		
	end loop;
	RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;


---BORRA ASISTENCIAS QUE TENGAN MISMO ESTUDIANTE Y CABEZAL

DO $$
declare
asistencias centros_educativos.sg_asistencias%ROWTYPE;
asistenciasBorrar centros_educativos.sg_asistencias%ROWTYPE;
contador int;
vAux varchar;
BEGIN
vAux:=' ';

contador:=0;
FOR asistencias IN (
	select * from centros_educativos.sg_asistencias asis
	where (select count(*) from centros_educativos.sg_asistencias asis_aux 
			where asis.asi_pk!=asis_aux.asi_pk and asis.asi_control_fk=asis_aux.asi_control_fk and asis.asi_estudiante=asis_aux.asi_estudiante)>=1
	order by asis.asi_pk
	desc
	)
loop
if(not vAux like '% '||asistencias.asi_pk||' %'  )then
    vAux:=vAux || asistencias.asi_pk||' ';
	RAISE NOTICE '----------- NO_BORRA (%)----',asistencias;
	FOR asistenciasBorrar IN (
		select * from centros_educativos.sg_asistencias asis
		where asistencias.asi_pk!=asis.asi_pk and asistencias.asi_control_fk=asis.asi_control_fk and asistencias.asi_estudiante=asis.asi_estudiante
		)
	loop
		if(not vAux like '% '||asistenciasBorrar.asi_pk||' %'  )then
			vAux:=vAux || asistenciasBorrar.asi_pk||' ';
			RAISE NOTICE '--------------------- BORRA (%)----',asistenciasBorrar;
			delete from centros_educativos.sg_asistencias where asi_pk=asistenciasBorrar.asi_pk;
		end if;

	end loop;

end if;

END LOOP;

END$$;

--CREA UN REGISTRO DE SG_ESTUDIO_REALIZADO VACÍO PARA PERSONAL_SEDE_EDUCATIVA QUE NO TENGA
DO $$
declare
personal_sin_dato_empleado centros_educativos.sg_personal_sede_educativa%ROWTYPE;
contador int;
BEGIN

FOR personal_sin_dato_empleado IN (
	select * from centros_educativos.sg_personal_sede_educativa pp where (select count(*) from centros_educativos.sg_estudios_realizados d where ere_personal_sede_fk = pp.pse_pk) = 0
	)
loop

insert into centros_educativos.sg_estudios_realizados (ere_personal_sede_fk, ere_ult_mod_fecha, ere_ult_mod_usuario,ere_version) values (personal_sin_dato_empleado.pse_pk, now(),'ADMIN_CDE',0);

RAISE NOTICE '----------- PER_PK (%)----',personal_sin_dato_empleado.pse_pk;


END LOOP;


END$$;

-- CORRIGE LAS CALIFICACIONES_ESTUDIANTE QUE TIENEN NUMÉRICA Y CONCEPTUAL DISTINTO DE VACÍO, BORRA LA QUE NO PERTENECE
DO $$
declare

cal_estudiantes centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
cal_estudiantes_auxiliar centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
escala_calificacion catalogo.sg_escalas_calificacion%ROWTYPE;
contador int;
begin
	contador:=0;
	for cal_estudiantes in(
		select * from centros_educativos.sg_calificaciones_estudiante calest
		where  calest.cae_calificacion_conceptual_fk is not  null and calest.cae_calificacion is not null 		

	)loop
	escala_calificacion:=(
		select esca
		from centros_educativos.sg_calificaciones_estudiante calest 
		inner join centros_educativos.sg_estudiantes est on est.est_pk=calest.cae_estudiante_fk
		inner join centros_educativos.sg_calificaciones cal on cal.cal_pk=calest.cae_calificacion_fk
		inner join centros_educativos.sg_secciones sec on sec.sec_pk=cal.cal_seccion_fk
		inner join centros_educativos.sg_servicio_educativo ser on ser.sdu_pk=sec.sec_servicio_educativo_fk
		inner join centros_educativos.sg_grados gra on gra.gra_pk=ser.sdu_grado_fk
		inner join centros_educativos.sg_planes_estudio pla on pla.pes_pk=sec.sec_plan_estudio_fk
		inner join centros_educativos.sg_componente_plan_estudio cpe on cpe.cpe_pk=cal.cal_componente_plan_estudio_fk
		join centros_educativos.sg_componente_plan_grado cpg on cpg.cpg_plan_estudio_fk=pla.pes_pk and cpg.cpg_grado_fk=gra.gra_pk and cpe.cpe_pk=cpg.cpg_componente_plan_estudio_fk
		inner join catalogo.sg_escalas_calificacion esca on esca.eca_pk=cpg.cpg_escala_calificacion_fk
		where calest.cae_pk=cal_estudiantes.cae_pk);
		RAISE NOTICE '----------- CALIFICACION ORIGINAL (%)----',cal_estudiantes;
		RAISE NOTICE '----------- escala (%)----',escala_calificacion;
	contador:=contador+1;
		if escala_calificacion.eca_tipo_escala='NUMERICA' then
			update centros_educativos.sg_calificaciones_estudiante set cae_calificacion_conceptual_fk=null where cae_pk=cal_estudiantes.cae_pk;
		else
			update centros_educativos.sg_calificaciones_estudiante set cae_calificacion=null where cae_pk=cal_estudiantes.cae_pk;
		end if;
	end loop;
	RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;


--CORRIGE LAS CALIFICACIONES_ESTUDIANTE QUE TIENEN CALIFICACIÓN NUMÉRICA TENIENDO ESCALA CONCEPTUAL Y VICEVERSA
--DEJA LA CALIFICACIÓN EN NULL


DO $$
declare

cal_estudiantes centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
cal_estudiantes_auxiliar centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
escala_calificacion catalogo.sg_escalas_calificacion%ROWTYPE;
contador int;
begin
	contador:=0;
	for cal_estudiantes in(
		select *
			from centros_educativos.sg_calificaciones_estudiante calest 
			inner join centros_educativos.sg_calificaciones cal on cal.cal_pk=calest.cae_calificacion_fk
			inner join centros_educativos.sg_secciones sec on sec.sec_pk=cal.cal_seccion_fk
			inner join centros_educativos.sg_servicio_educativo ser on ser.sdu_pk=sec.sec_servicio_educativo_fk
			inner join centros_educativos.sg_grados gra on gra.gra_pk=ser.sdu_grado_fk
			inner join centros_educativos.sg_planes_estudio pla on pla.pes_pk=sec.sec_plan_estudio_fk
			inner join centros_educativos.sg_componente_plan_estudio cpe on cpe.cpe_pk=cal.cal_componente_plan_estudio_fk
			join centros_educativos.sg_componente_plan_grado cpg on cpg.cpg_plan_estudio_fk=pla.pes_pk and cpg.cpg_grado_fk=gra.gra_pk and cpe.cpe_pk=cpg.cpg_componente_plan_estudio_fk
			inner join catalogo.sg_escalas_calificacion esca on esca.eca_pk=cpg.cpg_escala_calificacion_fk
			where (calest.cae_calificacion_conceptual_fk is not null and calest.cae_calificacion is null  and esca.eca_tipo_escala='NUMERICA') or
			(calest.cae_calificacion_conceptual_fk is  null and calest.cae_calificacion is not null  and esca.eca_tipo_escala='CONCEPTUAL')		
			
	)loop

		RAISE NOTICE '----------- CALIFICACION ORIGINAL (%)----',cal_estudiantes;
	contador:=contador+1;

			update centros_educativos.sg_calificaciones_estudiante set cae_calificacion_conceptual_fk=null, cae_calificacion=null where cae_pk=cal_estudiantes.cae_pk;

	end loop;
	RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;


---LOS USUARIOS QUE SON PERSONAL Y TIENEN EL CAMPO persona_fk DE LA TABLA seguridad.sg_usuarios EN NULL, LE SETEA EL VALOR QUE CORRESPONDE
DO $$
declare

persona_sin_usuario centros_educativos.sg_personas%ROWTYPE;
usuario seguridad.sg_usuarios%ROWTYPE;

contador int;
begin
	contador:=0;
	for persona_sin_usuario in(
		select * from centros_educativos.sg_personas per inner join centros_educativos.sg_personal_sede_educativa pse on per.per_pk=pse.pse_persona_fk
		where 
		per_dui is not null and
		exists
			(select 1 from seguridad.sg_usuarios usu 
			where usu.usu_codigo=per.per_dui and usu.usu_persona_pk is null  )	
		
			
	)loop

		RAISE NOTICE '----------- persona sin usuario (%)----',persona_sin_usuario;
	    contador:=contador+1;

			update seguridad.sg_usuarios set usu_persona_pk=persona_sin_usuario.per_pk where usu_codigo=persona_sin_usuario.per_dui ;

		

	end loop;
    RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;


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

---Borra calificaciones_estudiante que tengan mismo estudiante, rango fecha, componente plan estudio, calificacion y distinta seccion
--Deja solo uno de los registros
DO $$
declare

cal_estudiantes record;
cal_estudiantes_borra centros_educativos.sg_calificaciones_estudiante%ROWTYPE;

vAux varchar;
contador int;
pk_mayor bigint;
begin
	vAux:=' ';
	contador:=0;
	for cal_estudiantes in(
		select * from centros_educativos.sg_calificaciones_estudiante calest 
		inner join centros_educativos.sg_calificaciones cal on cal.cal_pk=calest.cae_calificacion_fk
		where  not  cal.cal_rango_fecha_fk isnull  and exists 
		(select 1 from centros_educativos.sg_calificaciones_estudiante calest_aux 
		inner join centros_educativos.sg_calificaciones cal_aux on cal_aux.cal_pk=calest_aux.cae_calificacion_fk
		where cal_aux.cal_rango_fecha_fk=cal.cal_rango_fecha_fk 
		and cal_aux.cal_componente_plan_estudio_fk=cal.cal_componente_plan_estudio_fk
		and cal_aux.cal_seccion_fk!=cal.cal_seccion_fk
		and calest_aux.cae_estudiante_fk=calest.cae_estudiante_fk
		and calest_aux.cae_pk!=calest.cae_pk
		and  ((calest.cae_calificacion_conceptual_fk is not null and calest.cae_calificacion is null  and calest.cae_calificacion_conceptual_fk=calest_aux.cae_calificacion_conceptual_fk)
		or (calest.cae_calificacion_conceptual_fk is  null and calest.cae_calificacion is not null  and calest.cae_calificacion=calest_aux.cae_calificacion)) ) 
		
	)loop	
		
		if(not vAux like '% '||cal_estudiantes.cae_pk||' %'  )then
		contador:=contador+1;
				vAux:=vAux || cal_estudiantes.cae_pk||' ';
				RAISE NOTICE '----------- CALIFICACION ORIGINAL (%)----',cal_estudiantes;

				for cal_estudiantes_borra in(
					select * from centros_educativos.sg_calificaciones_estudiante calest 
					inner join centros_educativos.sg_calificaciones cal on cal.cal_pk=calest.cae_calificacion_fk
					where cal_estudiantes.cal_rango_fecha_fk=cal.cal_rango_fecha_fk 
					and cal_estudiantes.cal_componente_plan_estudio_fk=cal.cal_componente_plan_estudio_fk
					and cal_estudiantes.cal_seccion_fk!=cal.cal_seccion_fk
					and cal_estudiantes.cae_estudiante_fk=calest.cae_estudiante_fk
					and cal_estudiantes.cae_pk!=calest.cae_pk
					and  ((calest.cae_calificacion_conceptual_fk=cal_estudiantes.cae_calificacion_conceptual_fk)
					or (calest.cae_calificacion=cal_estudiantes.cae_calificacion))
				)loop
					
						if(not vAux like '% '||cal_estudiantes_borra.cae_pk||' %'  )then
							contador:=contador+1;
							vAux:=vAux || cal_estudiantes_borra.cae_pk||' ';
							delete from centros_educativos.sg_calificaciones_estudiante where cae_pk=cal_estudiantes_borra.cae_pk;
							RAISE NOTICE '--------------------- BORRA (%)----',cal_estudiantes_borra;
						end if;
				
				
				end loop;

				
			end if;
		
	end loop;
RAISE NOTICE '---------------- CONTADOR(%)----',contador;
end$$;


--CAIFICACION
--Crea para las notas intitucionales conceptuales una nota de aprobación con los mismos datos
DO $$
declare

cal_estudiantes record;
cal_estudiantes_nota_ins centros_educativos.sg_calificaciones_estudiante%ROWTYPE;
cal_nota_ins centros_educativos.sg_calificaciones%ROWTYPE;
calificacion_conceptual catalogo.sg_calificaciones%ROWTYPE;
est_calificacion_conceptual catalogo.sg_calificaciones%ROWTYPE;

vAux varchar;
contador int;
pk_mayor bigint;
cal_pk_apr bigint;
resolucion varchar;
begin
	vAux:=' ';
	contador:=0;
	for cal_nota_ins in(
		select distinct cal_pk,cal_rango_fecha_fk,cal_seccion_fk,cal_componente_plan_estudio_fk from centros_educativos.sg_calificaciones cal 
		inner join centros_educativos.sg_calificaciones_estudiante calest  on cal.cal_pk=calest.cae_calificacion_fk
		where  cal.cal_tipo_periodo_calificacion='NOTIN' and calest.cae_calificacion_conceptual_fk is not null and calest.cae_calificacion is null 
		and not exists (select 1
						from  centros_educativos.sg_calificaciones cal_aux 
						where  cal_aux.cal_tipo_periodo_calificacion='APR' 
						and cal_aux.cal_seccion_fk=cal.cal_seccion_fk
						and cal_aux.cal_componente_plan_estudio_fk=cal.cal_componente_plan_estudio_fk)	
						
	)loop
	
	calificacion_conceptual:=(
				select cal_concpetual
				from centros_educativos.sg_calificaciones cal
				inner join centros_educativos.sg_secciones sec on sec.sec_pk=cal.cal_seccion_fk
				inner join centros_educativos.sg_servicio_educativo ser on ser.sdu_pk=sec.sec_servicio_educativo_fk
				inner join centros_educativos.sg_grados gra on gra.gra_pk=ser.sdu_grado_fk
				inner join centros_educativos.sg_planes_estudio pla on pla.pes_pk=sec.sec_plan_estudio_fk
				inner join centros_educativos.sg_componente_plan_estudio cpe on cpe.cpe_pk=cal.cal_componente_plan_estudio_fk
				join centros_educativos.sg_componente_plan_grado cpg on cpg.cpg_plan_estudio_fk=pla.pes_pk and cpg.cpg_grado_fk=gra.gra_pk and cpe.cpe_pk=cpg.cpg_componente_plan_estudio_fk
				inner join catalogo.sg_escalas_calificacion esca on esca.eca_pk=cpg.cpg_escala_calificacion_fk
				left outer join catalogo.sg_calificaciones cal_concpetual on cal_concpetual.cal_pk=esca.eca_valor_minimo
				where cal.cal_pk=cal_nota_ins.cal_pk);

		
	
		INSERT INTO centros_educativos.sg_calificaciones
		(cal_seccion_fk,cal_componente_plan_estudio_fk,cal_tipo_periodo_calificacion,cal_fecha,cal_ult_mod_usuario,cal_ult_mod_fecha,cal_version)
		VALUES(cal_nota_ins.cal_seccion_fk,cal_nota_ins.cal_componente_plan_estudio_fk,'APR',now(),'script',now(),0)returning cal_pk into cal_pk_apr;
 		
		RAISE NOTICE '--------------------- cal_existente (%)----',cal_nota_ins;
		RAISE NOTICE '--------------------- cal_nueva (%)----',cal_pk_apr;
		for cal_estudiantes_nota_ins in(
			select *  from centros_educativos.sg_calificaciones_estudiante where cae_calificacion_fk=cal_nota_ins.cal_pk
		)loop
			resolucion:='PENDIENTE';
			if cal_estudiantes_nota_ins.cae_calificacion_conceptual_fk is not null then
				est_calificacion_conceptual:=(select cal from catalogo.sg_calificaciones cal where cal_pk=cal_estudiantes_nota_ins.cae_calificacion_conceptual_fk);
				
				if(est_calificacion_conceptual.cal_orden is not null and calificacion_conceptual.cal_orden is not null) then
					resolucion:='APROBADO';
					if(calificacion_conceptual.cal_orden > est_calificacion_conceptual.cal_orden)then
						resolucion:='NO_APROBADO';
					end if;
				end if;
			end if;
			RAISE NOTICE '--------------------- NOTA INSTITUCIONAL (%)----',cal_estudiantes_nota_ins;
			RAISE NOTICE '--------------------- resolucion (%)----',resolucion;
			contador:=contador+1;
			INSERT INTO centros_educativos.sg_calificaciones_estudiante
			(cae_estudiante_fk,cae_calificacion_conceptual_fk,cae_calificacion_fk,cae_resolucion,cae_ult_mod_fecha,cae_version,cae_proceso_de_creacion)
			VALUES(cal_estudiantes_nota_ins.cae_estudiante_fk,cal_estudiantes_nota_ins.cae_calificacion_conceptual_fk,cal_pk_apr,resolucion,now(),0,'script');
		end loop;
	
	end loop;
RAISE NOTICE '--------------------- contador (%)----',contador;
end$$;


--Script para hacer roll-back de cuando se cierra año
update centros_educativos.sg_matriculas set mat_estado='ABIERTO', mat_fecha_hasta=null where
mat_estudiante_fk in (select esc_estudiante from centros_educativos.sg_escolaridad_estudiante) and
mat_pk in (select est_ultima_matricula_fk from centros_educativos.sg_estudiantes);

update centros_educativos.sg_secciones set sec_estado='ABIERTA' where
sec_pk in (select mat.mat_seccion_fk from centros_educativos.sg_escolaridad_estudiante esc inner join centros_educativos.sg_matriculas mat
on mat.mat_estudiante_fk=esc.esc_estudiante);


--- Script para setear grados precedentes dentro de una misma relmod

DO $$
declare
grado centros_educativos.sg_grados%ROWTYPE;
begin
	TRUNCATE TABLE centros_educativos.sg_rel_grado_precedente;
	for grado in(
		select * from centros_educativos.sg_grados gr
	)loop	
				
				RAISE NOTICE '-----------GRADO DESTINO (%)----',grado.gra_pk;
			
				INSERT INTO centros_educativos.sg_rel_grado_precedente(rgp_grado_destino_fk, rgp_grado_origen_fk, rgp_ult_mod_fecha, rgp_ult_mod_usuario, rgp_version)
				select grado.gra_pk, gg.gra_pk, now(), 'ADMIN', 0 from centros_educativos.sg_grados gg where gg.gra_relacion_modalidad_fk = grado.gra_relacion_modalidad_fk
				and gg.gra_orden < grado.gra_orden order by gg.gra_orden desc limit 1;
		
	end loop;
end$$;


UPDATE centros_educativos.sg_secciones s 
SET sec_cantidad_estudiantes_mat_abierta = 
(select count(*) from centros_educativos.sg_matriculas m
INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
where sec.sec_pk = s.sec_pk and m.mat_estado = 'ABIERTO')
from centros_educativos.sg_anio_lectivo ale
where s.sec_anio_lectivo_fk = ale.ale_pk and ale.ale_anio >= (date_part('year', CURRENT_DATE)-1)


UPDATE centros_educativos.sg_secciones s 
SET sec_cantidad_estudiantes_no_retirados = 
(select count(*) from centros_educativos.sg_matriculas m
INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)
where sec.sec_pk = s.sec_pk AND m.mat_retirada = false AND m.mat_anulada = false)
from centros_educativos.sg_anio_lectivo ale
where s.sec_anio_lectivo_fk = ale.ale_pk and ale.ale_anio >= (date_part('year', CURRENT_DATE)-1)



select count(*) from centros_educativos.sg_calificaciones_estudiante ce 
INNER JOIN centros_educativos.sg_calificaciones ca ON (ce.cae_calificacion_fk = ca.cal_pk)
INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (ca.cal_componente_plan_estudio_fk = cpe.cpe_pk)
where 
(ca.cal_tipo_periodo_calificacion = 'ORD' or ca.cal_tipo_periodo_calificacion = 'EXORD') and
cpe.cpe_es_paes = false and
cae_calificacion_conceptual_fk is null 
and (cae_calificacion = '0' or cae_calificacion = '0.0')



DO $$
declare
cabezales_eliminados centros_educativos.sg_calificaciones_aud%ROWTYPE;
contador int;
contador_ex int;
err_context text;
begin
	contador:=0;
	contador_ex:=0;
	for cabezales_eliminados in(
		select * from centros_educativos.sg_calificaciones_aud cal 
		where cal_tipo_periodo_calificacion = 'GRA' 
		and revtype = 2 
		and cal_estado_procesamiento_promocion = 'SIN_PROCESAR' 
		and not exists (select 1 from centros_educativos.sg_calificaciones cc where cc.cal_tipo_periodo_calificacion = 'GRA' and cc.cal_seccion_fk = cal.cal_seccion_fk)
	) loop	
				contador:=contador+1;
				RAISE NOTICE '----------- PROCESANDO (%)----',contador;
				RAISE NOTICE '----------- CALIFICACION AUD ORIGINAL (%)----',cabezales_eliminados;
				
				BEGIN
				INSERT INTO centros_educativos.sg_calificaciones(
					cal_rango_fecha_fk,
					cal_seccion_fk,
					cal_componente_plan_estudio_fk,
					cal_fecha,
					cal_ult_mod_fecha,
					cal_ult_mod_usuario,
					cal_version,
					cal_tipo_periodo_calificacion,
					cal_tipo_calendario_escolar,
					cal_numero,
					cal_cerrado,
					cal_cant_estudiantes_calificados,
					cal_promedio_calificaciones,
					cal_estado_procesamiento_promocion)
				VALUES (cabezales_eliminados.cal_rango_fecha_fk,
						cabezales_eliminados.cal_seccion_fk,
						cabezales_eliminados.cal_componente_plan_estudio_fk,
						cabezales_eliminados.cal_fecha,
						cabezales_eliminados.cal_ult_mod_fecha,
						CONCAT('ADMIN412-', cabezales_eliminados.cal_ult_mod_usuario),
						cabezales_eliminados.cal_version,
						cabezales_eliminados.cal_tipo_periodo_calificacion,
						cabezales_eliminados.cal_tipo_calendario_escolar,
						cabezales_eliminados.cal_numero,
						cabezales_eliminados.cal_cerrado,
						cabezales_eliminados.cal_cant_estudiantes_calificados,
						cabezales_eliminados.cal_promedio_calificaciones,
						cabezales_eliminados.cal_estado_procesamiento_promocion);
				EXCEPTION 
				WHEN QUERY_CANCELED THEN
					RAISE NOTICE 'Query canceled';
				WHEN OTHERS THEN
						contador_ex:=contador_ex+1;
						GET STACKED DIAGNOSTICS err_context = PG_EXCEPTION_CONTEXT;
						RAISE NOTICE 'Query exception';
						RAISE INFO 'Error Name:%',SQLERRM;
						RAISE INFO 'Error State:%', SQLSTATE;
						RAISE INFO 'Error Context:%', err_context;
				END;
	end loop;
	RAISE NOTICE '---------------- CONTADOR(%)----',contador;
	RAISE NOTICE '---------------- EXCEPCIONES(%)----',contador_ex;
end$$;

--ARREGLA TITULOS CON NOMBRE DE SECTOR Y OPCION MAL
update centros_educativos.sg_titulo tit_or set tit_nombre_certificado=(
select  'BACHILLER TÉCNICO VOCACIONAL '|| UPPER(sect.sec_nombre)||' OPCIÓN '||opc.opc_nombre  from centros_educativos.sg_titulo tit inner join 
centros_educativos.sg_estudiantes est on est.est_pk=tit.tit_estudiante_fk 
inner join centros_educativos.sg_personas per on per.per_pk=est.est_persona
inner join centros_educativos.sg_solicitudes_impresion solim on solim.sim_pk=tit.tit_solicitud_impresion_fk
inner join centros_educativos.sg_secciones sec on solim.sim_seccion_fk=sec.sec_pk
inner join centros_educativos.sg_servicio_educativo ser on ser.sdu_pk=sec.sec_servicio_educativo_fk
inner join centros_educativos.sg_opciones opc on opc.opc_pk=ser.sdu_opcion_fk
inner join catalogo.sg_sectores_economicos sect on opc.opc_sector_economico=sect.sec_pk
where tit.tit_pk=tit_or.tit_pk)
where tit_or.tit_solicitud_impresion_fk in 
(select sim_pk from centros_educativos.sg_solicitudes_impresion sim where sim_definicion_titulo_fk=3 )



CREATE MATERIALIZED VIEW centros_educativos.sg_estudiantes_asistencias_2020 AS

select asi_estudiante as estudiante,
SUM(CASE WHEN not asi.asi_inasistencia THEN 1 ELSE 0 END) asistencias, 
SUM(CASE WHEN asi.asi_inasistencia and mot.min_motivo_justificado THEN 1 ELSE 0 END) inasistencias_justificadas, 
SUM(CASE WHEN asi.asi_inasistencia and not mot.min_motivo_justificado THEN 1 ELSE 0 END) inasistencias_no_justificadas
from centros_educativos.sg_asistencias asi
INNER JOIN centros_educativos.sg_control_asistencia_cabezal cac ON (asi.asi_control_fk = cac.cac_pk)
LEFT OUTER JOIN catalogo.sg_motivos_inasistencia mot ON (asi.asi_motivo_inasistencia = mot.min_pk)
where cac_fecha >= '2020-01-01' and cac_fecha <= '2020-12-31'
group by asi_estudiante;

--SENTENCIA 6
CREATE INDEX IF NOT EXISTS sg_estudiantes_asistencias_2020_est_idx ON centros_educativos.sg_estudiantes_asistencias_2020 USING btree (estudiante);

CREATE MATERIALIZED VIEW centros_educativos.sg_promedios_grados_sedes_cal_apr_2019 AS
select 
sistemas.sin_pk,sed.sed_pk, org.ocu_pk, niv.niv_pk, cic.cic_pk, modedu.mod_pk, modat.mat_pk, submodat.smo_pk,
gra.gra_pk, op.opc_pk,progedu.ped_pk, cpe.cpe_tipo, cpe.cpe_pk, cpe.cpe_nombre,
AVG(cabezal.cal_promedio_calificaciones) as promedio
 					from centros_educativos.sg_calificaciones cabezal
                    INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)
					INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)
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
					INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) 
					INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk))
					LEFT OUTER JOIN sistemas_integrados.sg_sistemas_sedes sistemas ON (sistemas.sed_pk = sed.sed_pk)
					where cpg.cpg_objeto_promocion and cabezal.cal_tipo_periodo_calificacion = 'APR' and ale.ale_anio = 2019
					group by sistemas.sin_pk,sed.sed_pk, org.ocu_pk, niv.niv_pk, cic.cic_pk, modedu.mod_pk, modat.mat_pk, submodat.smo_pk,
					gra.gra_pk, cpe.cpe_tipo, cpe.cpe_pk, cpe.cpe_nombre, op.opc_pk,progedu.ped_pk;


CREATE MATERIALIZED VIEW centros_educativos.sg_promedios_grados_cal_apr_2019 AS
select 
org.ocu_pk, niv.niv_pk, cic.cic_pk, modedu.mod_pk, modat.mat_pk, submodat.smo_pk,
gra.gra_pk, op.opc_pk, progedu.ped_pk, cpe.cpe_tipo, cpe.cpe_pk, cpe.cpe_nombre,
AVG(cabezal.cal_promedio_calificaciones) as promedio
 					from centros_educativos.sg_calificaciones cabezal
                    INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)
					INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)
                    INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)
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
					INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) 
					INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk))
					where cpg.cpg_objeto_promocion and cabezal.cal_tipo_periodo_calificacion = 'APR' and ale.ale_anio = 2019
					group by org.ocu_pk, niv.niv_pk, cic.cic_pk, modedu.mod_pk, modat.mat_pk, submodat.smo_pk,
					gra.gra_pk, cpe.cpe_tipo, cpe.cpe_pk, cpe.cpe_nombre, op.opc_pk,progedu.ped_pk;


DO $$
declare
cabezalPk bigint;
contador int;
begin
	contador:=0;
	RAISE NOTICE '---- INICIO ACTUALIZAR PROMEDIOS Y MODAS ----';
	for cabezalPk in(	
		select cal_pk from centros_educativos.sg_calificaciones where cal_promedio_moda_desactualizados = true
	)loop	
						
		UPDATE centros_educativos.sg_calificaciones cabezal set cal_moda_calificaciones_conceptuales =
		(select mode() WITHIN GROUP (ORDER BY ce.cae_calificacion_conceptual_fk) from centros_educativos.sg_calificaciones_estudiante ce where ce.cae_calificacion_fk = cabezal.cal_pk)
		where cabezal.cal_pk = cabezalPk;

		 UPDATE centros_educativos.sg_calificaciones cabezal set cal_moda_calificaciones_conceptuales_masculino =
		(select mode() WITHIN GROUP (ORDER BY ce.cae_calificacion_conceptual_fk) from centros_educativos.sg_calificaciones_estudiante ce
		 INNER JOIN centros_educativos.sg_estudiantes est ON (est.est_pk = ce.cae_estudiante_fk)
		 INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)
		 where ce.cae_calificacion_fk = cabezal.cal_pk and per.per_sexo_fk = 1)
		 where cabezal.cal_pk = cabezalPk;

		 UPDATE centros_educativos.sg_calificaciones cabezal set cal_moda_calificaciones_conceptuales_femenino =
		(select mode() WITHIN GROUP (ORDER BY ce.cae_calificacion_conceptual_fk) from centros_educativos.sg_calificaciones_estudiante ce
		 INNER JOIN centros_educativos.sg_estudiantes est ON (est.est_pk = ce.cae_estudiante_fk)
		 INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)
		 where ce.cae_calificacion_fk = cabezal.cal_pk and per.per_sexo_fk = 2)
		 where cabezal.cal_pk = cabezalPk;


		UPDATE centros_educativos.sg_calificaciones cabezal set cal_promedio_calificaciones =
		(select AVG(ce.cae_calificacion::decimal) from centros_educativos.sg_calificaciones_estudiante ce where ce.cae_calificacion_fk = cabezal.cal_pk)
		where cabezal.cal_pk = cabezalPk;

		UPDATE centros_educativos.sg_calificaciones cabezal set cal_promedio_calificaciones_masculino =
		(select AVG(ce.cae_calificacion::decimal) from centros_educativos.sg_calificaciones_estudiante ce
		 INNER JOIN centros_educativos.sg_estudiantes est ON (est.est_pk = ce.cae_estudiante_fk)
		 INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)
		 where ce.cae_calificacion_fk = cabezal.cal_pk and per.per_sexo_fk = 1)
		 where cabezal.cal_pk = cabezalPk;

		UPDATE centros_educativos.sg_calificaciones cabezal set cal_promedio_calificaciones_femenino =
		(select AVG(ce.cae_calificacion::decimal) from centros_educativos.sg_calificaciones_estudiante ce
		 INNER JOIN centros_educativos.sg_estudiantes est ON (est.est_pk = ce.cae_estudiante_fk)
		 INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)
		 where ce.cae_calificacion_fk = cabezal.cal_pk and per.per_sexo_fk = 2)
		 where cabezal.cal_pk = cabezalPk;
		 
		 update centros_educativos.sg_calificaciones cabezal set cal_promedio_moda_desactualizados = false where cabezal.cal_pk = cabezalPk;
				
				
		contador:=contador+1;
		
	end loop;
	RAISE NOTICE '---- FIN ACTUALIZAR PROMEDIOS Y MODAS. CANTIDAD DE CABEZALES ACTUALIZADOS: % ----',contador;
end$$;

--SCRIPT PARA DARLE PLAN DE ESTUDIO A SECCIONES Q NO TIENEN
update centros_educativos.sg_secciones sec_aux set sec_plan_estudio_fk=
(select pla.pes_pk  from centros_educativos.sg_planes_estudio pla where 
( pla.pes_opcion_fk is not null and pla.pes_programa_educativo_fk is not null and 
(pla.pes_relacion_modalidad_fk, pla.pes_opcion_fk,pla.pes_programa_educativo_fk) in 
(select rel.rea_pk,serv.sdu_opcion_fk,serv.sdu_programa_educativo_fk from centros_educativos.sg_secciones sec inner join centros_educativos.sg_servicio_educativo serv on sec.sec_servicio_educativo_fk=serv.sdu_pk
inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
where sec_pk= sec_aux.sec_pk  ))
or
( pla.pes_opcion_fk is  null and pla.pes_programa_educativo_fk is  null and 
(pla.pes_relacion_modalidad_fk) in 
(select rel.rea_pk from centros_educativos.sg_secciones sec inner join centros_educativos.sg_servicio_educativo serv on sec.sec_servicio_educativo_fk=serv.sdu_pk
inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
where sec_pk= sec_aux.sec_pk  ))
order by  pla.pes_pk desc
limit 1) 
, sec_ult_mod_usuario='SCRIPT_PLAN_VACIO'
where sec_aux.sec_plan_estudio_fk is null

--SCRIPT PARA BUSCAR secciones que tienen plan de estudio vacío, y más de un plan posible

DO $$
declare

cal_estudiantes record;
secciones centros_educativos.sg_secciones%ROWTYPE;
contador int;
vAux varchar;
pk_mayor bigint;
pur_pk_aux bigint;
begin
	contador:=0;
	for secciones in(
		select * from centros_educativos.sg_secciones where sec_plan_estudio_fk is null 

	)loop	
		



   pur_pk_aux:=( select count(pla.pes_pk)  from centros_educativos.sg_planes_estudio pla where 
	( pla.pes_opcion_fk is not null and pla.pes_programa_educativo_fk is not null and 
	(pla.pes_relacion_modalidad_fk, pla.pes_opcion_fk,pla.pes_programa_educativo_fk) in 
	(select rel.rea_pk,serv.sdu_opcion_fk,serv.sdu_programa_educativo_fk from centros_educativos.sg_secciones sec inner join centros_educativos.sg_servicio_educativo serv on sec.sec_servicio_educativo_fk=serv.sdu_pk
	inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
	inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
	where sec_pk= secciones.sec_pk ))
	or
	( pla.pes_opcion_fk is  null and pla.pes_programa_educativo_fk is  null and 
	(pla.pes_relacion_modalidad_fk) in 
	(select rel.rea_pk from centros_educativos.sg_secciones sec inner join centros_educativos.sg_servicio_educativo serv on sec.sec_servicio_educativo_fk=serv.sdu_pk
	inner join centros_educativos.sg_grados gra on gra.gra_pk=serv.sdu_grado_fk
	inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk
	where sec_pk= secciones.sec_pk)))
	;
	RAISE NOTICE '---------------- SECCION(%)----',pur_pk_aux;
	if pur_pk_aux > 1 then 
	contador:=contador+1;
		RAISE NOTICE '----------------------------------------------------------------- SECCION(%)----',secciones;
	end if;
	end loop;
	RAISE NOTICE '----------------------------------------------------------------- CONTADOR(%)----',contador;
end$$;	
end


--VISTA MATERIALIZADA PARA SERVICIOS DE MATRICULA
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

REFRESH MATERIALIZED VIEW centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac;