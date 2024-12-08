
Este API Gateway fue desarrollado como parte del Proyecto Terminal para la carrera de Ingeniería en Telemática. Su propósito es actuar como un punto de entrada central para todas las solicitudes hacia los microservicios, manejando el balanceo de carga, la seguridad y la comunicación eficiente entre servicios.

## Desarrolladores

- **Cabrera Vázquez Itzel Berenice**
- **Flores Chavarría Diego**

## Propiedad Intelectual

Este código y los materiales relacionados están protegidos por las leyes de propiedad intelectual. Su uso, reproducción, distribución o modificación sin autorización está estrictamente prohibido.

---

## Descripción del Proyecto

El API Gateway es un componente esencial en la arquitectura de microservicios. Este proyecto incluye:

- Enrutamiento dinámico de solicitudes hacia microservicios registrados en Eureka.
- Integración con JWT para autenticación de usuarios.
- Balanceo de carga y manejo de timeouts para solicitudes HTTP.
- Configuración lista para despliegue en Docker.

---

## Configuración del Proyecto

### Requisitos Previos

- **Java 17 o superior**
- **Maven 3.6+**
- **Eureka Server (como servicio de descubrimiento)**
- **Docker (opcional, para despliegue)**

### Instalación y Ejecución

1. **Clonar el Repositorio**  
   ```bash
   git clone https://github.com/dfloresc16/gatewayms.git
   cd gatewayms