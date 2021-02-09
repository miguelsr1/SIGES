# Generador de tokens para SIGES

## Modo de uso

* 1) Se pueden generar tokens mediante la interfaz web.

* 2) Se pueden generar tokens consumiendo un servicio rest.

> /rest/v1/tokengenerator/generate

Genera un token con:

Usuario: usuario

Ip: 192.168.1.5

Tiempo en minutos para expirar: 20

> /rest/v1/tokengenerator/generate?user=casuser&ip=192.168.1.1&expirationMinutes=10

Genera un token con:

Usuario: casuser

Ip: 192.168.1.1

Tiempo en minutos para expirar: 10


