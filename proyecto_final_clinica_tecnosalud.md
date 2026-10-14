# PROYECTO FINAL
## Estructura de Datos / Lógica de Programación II

## Realice el siguiente programa

La Clínica **TECNOSALUD** desea llevar el control sobre los pacientes, citas, habitaciones y servicios que presta la clínica. Para ello solicita un programa que permita:

---

## Requerimientos de almacenamiento
*(usar listas enlazadas implementadas por el estudiante)*

### 1. Pacientes
- Documento
- Nombre
- Edad
- Género
- Teléfono
- EPS/seguro (si tiene)
- Tipo de sangre

### 2. Consultorios/Habitaciones
- Número de sala/consultorio
- Tipo: consulta externa / observación / hospitalización / quirófano
- Estado: disponible / ocupado

### 3. Servicios médicos ofrecidos
- Código
- Nombre del servicio
- Precio unitario

**Ejemplos de servicios y precios (referencia en COP):**

| Servicio | Precio |
|---|---|
| Consulta general | $70.000 |
| Consulta especializada | $150.000 |
| Exámenes de laboratorio (paquete básico) | $40.000 |
| Imagenología (ecografía) | $120.000 |
| Rayos X | $60.000 |
| Hospitalización por día (habitaciones estándar) | $200.000 |
| Cirugía menor (tarifa base) | $1.200.000 |
| Cirugía mayor (tarifa base) | $6.000.000 |
| Vacunación | $30.000 por dosis |
| Farmacia (medicamentos) | Variable (registro por ítem) |
| Ambulancia / traslado | $180.000 por traslado |

---

## Operaciones y comportamiento

Al llegar un paciente, el sistema debe registrar el **check-in** en una lista enlazada de ingresos/pacientes atendidos. Ese registro debe contener los datos personales, consultorio/habitación asignada (si aplica), y la lista de servicios que vaya adquiriendo (cada servicio se añade al registro del paciente).

- Debe existir una **cola de atención** para pacientes que esperan ser atendidos en consulta externa. Al atenderse un paciente, se saca de la cola y se crea su registro de atención.
- Para servicios que demandan turno limitado (p. ej. quirófano o sala de recuperación), habrá una **cola de pre-operatorio con máxima capacidad** (ejemplo: máximo 3 pacientes en espera de quirófano). Si la cola está llena, se debe rechazar y notificar.
- Para ciertos recursos (p. ej. quirófano) solo puede usarse **uno a la vez (capacidad 1)**. Gestionar la entrada/salida del recurso.

---

## Pagos y medios

- **Medios de pago:** Efectivo, Tarjeta débito, Tarjeta crédito, Seguro / EPS (cuando aplique, puede ser parcial).
- En un **arreglo (vector)** se debe llevar la cantidad de pacientes que pagaron por cada medio de pago (índices fijos por medio).
- En una **pila** se debe registrar la secuencia de servicios prestados por tipo para análisis (por ejemplo, *push* cuando se presta un servicio; *pop* para procesar facturas pendientes).

---

## Reglas de costos y descuentos

### 1. Tarifas por tipo de habitación/hospitalización por día (ejemplo)
- Observación: $120.000/día
- Estándar hospitalización: $200.000/día
- Suite privada: $450.000/día

### 2. Descuentos por edad
- **Menor de 5 años:** 20% de descuento en consultas y servicios (no aplica a cirugías de emergencia).
- **Mayor de 65 años:** 30% de descuento en consultas y hospitalización (cuando no cubre EPS).
- **Resto:** 0% (o descuentos especiales por convenio).

### 3. Pacientes con EPS/seguro
Si la EPS cubre el servicio, el pago directo del paciente es 0 (o copago fijo si se define).

### 4. Recargo por tarjeta
Los pagos con tarjeta pueden tener recargo por procesamiento del 2% (opcional según modalidad).

---

## Reportes y salidas solicitadas

El programa debe calcular y mostrar (lista mínima; incluir totales y porcentajes similares al enunciado original):

- Cantidad total de pacientes atendidos.
- Cantidad de pacientes por género.
- Monto total recaudado por género.
- Monto total recaudado por la clínica.
- Monto recaudado por cada medio de pago (y porcentaje respecto al total).
- Monto recaudado por cada servicio (y porcentaje respecto al total).
- Monto total de descuentos aplicados.
- Monto total recaudado por rango de edad (usar los rangos: `<5`, `5–64`, `>=65`).
- Paciente que más pagó y paciente que menos pagó (mostrar documento, nombre y monto).
- Porcentaje de pacientes atendidos por cada rango de edad.
- Porcentaje recaudado por cada servicio.
- Porcentaje de pacientes y de dinero según medio de pago.
- Porcentaje de pacientes por género.
- Cantidad de pacientes en cola de atención (consulta externa) en el momento.
- Cantidad de pacientes en cola de pre-operatorio.
- Promedio de pacientes que han requerido hospitalización por periodo (según registros).
- Promedio de tiempo de espera en cola de atención (si registra tiempos, opcionalmente estimado).

---

## Reglas adicionales y observaciones

- El programa debe operar mediante un **menú de opciones** (registrar paciente, agregar a cola, asignar consultorio/ingresar a sala, registrar servicio, registrar pago, mostrar reportes, etc.).
- **NO** usar las colecciones propias de Java para listas, pilas y colas (se deben implementar las estructuras manualmente).
- Guardar **historial simple** en listas enlazadas (por ejemplo, una lista por día o un log global).
- Permitir **cancelación de citas/reservas** y procesar **reembolsos** con reglas (por ejemplo: cancelación con menos de 24 horas => 50% retención).
- **Validar integridad:** no asignar una habitación ocupada, no procesar pago duplicado, etc.
