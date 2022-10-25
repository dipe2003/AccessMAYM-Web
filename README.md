# AccessMAYM-Web
Version simple refactorizada de MAYM-Web

Sistema de Gestión de Acciones Correctivas, Preventivas, Mejoras y Fortalezas basado en el proceso de Medición, Analisis y Mejora (ISO 9001, BRCGS, etc) y Gestión de Oportunidades (ISO 17025).
Consta de un módulo backend con manejo de base de datos (mysql) y un módulo frontend web.

# Funciones - "resumen"
El sistema permite el ingreso de hallazgos (internos y externos) por ejemplo de clientes, auditorias, controles, etc. que se definen dentro de la configuración.
En cada hallazgo, luego de realizado su análisis de causa, se agregan actividades para realizarse definiéndose sus responsables y fehas estimadas. Con estos datos se establecen los responsables de comprobar la correcta implementación y eficacia.
Finalmente se realiza el seguimiento del registro, operación que consta de indicar las fecha en que se realiza cada actividad y las correspondientes comprobaciones. A medida que se van agregando las fechas el sistema automáticamente cambia el estado de cada acción pasando por Pendiente, En Proceso de Implementacion, En Proceso de Verificación y Cerrada.
Se incluye un estado adicional de Desestimada, que debe ser indicado de forma manual, donde se marca el registro como no aplicable para el seguimiento y se incluye el motivo como observación.
