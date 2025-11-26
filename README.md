# 🧬 Mutant Detector API

API REST para detectar mutantes basándose en su secuencia de ADN. Proyecto desarrollado para el examen técnico de MercadoLibre.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3
- H2 Database (Base de datos en memoria)
- JUnit 5 & Mockito (Testing)
- Lombok
- Swagger / OpenAPI

## 🛠️ Instrucciones de Ejecución

### Prerrequisitos
Tener instalado Java 17 o superior.

### Ejecutar localmente
1. Clonar el repositorio.
2. Navegar a la carpeta raíz.
3. Ejecutar el comando:

```bash
# En Windows:
./gradlew.bat bootRun

# En Linux/Mac:
./gradlew bootRun

## 📊 Diagrama de Secuencia (Arquitectura)

Este diagrama representa el flujo de la operación principal de detección:

```mermaid
sequenceDiagram
    autonumber
    actor Cliente as Cliente (Postman/API)
    participant Controller as MutantController
    participant Validator as @Valid (Validator)
    participant Service as MutantService
    participant Repo as DnaRecordRepository
    participant DB as Base de Datos (H2)
    participant Detector as MutantDetector

    Note over Cliente, Controller: Inicio del Flujo POST /mutant

    Cliente->>Controller: Enviar JSON {dna: [...]}
    
    rect rgb(240, 248, 255)
        Note right of Controller: 1. Validación Previa
        Controller->>Validator: Verifica formato y reglas
        alt ADN Inválido
            Validator-->>Controller: Error de Validación
            Controller-->>Cliente: 400 Bad Request
        end
    end

    Controller->>Service: analyzeDna(dna)

    rect rgb(255, 250, 205)
        Note right of Service: 2. Lógica de Caché
        Service->>Service: Calcular Hash SHA-256
        Service->>Repo: findByDnaHash(hash)
        Repo->>DB: SELECT * FROM dna_records...
        DB-->>Repo: Resultado (Optional)
        Repo-->>Service: Retorna registro (si existe)
    end

    alt Registro Existe (Cache Hit)
        Service-->>Controller: Retorna resultado guardado
    else Registro NO Existe (Cache Miss)
        rect rgb(230, 255, 230)
            Note right of Service: 3. Análisis Real
            Service->>Detector: isMutant(dna)
            Detector->>Detector: Buscar Horiz/Vert/Diag
            Detector-->>Service: true / false
        end
        
        rect rgb(255, 228, 225)
            Note right of Service: 4. Persistencia
            Service->>Repo: save(nuevoRegistro)
            Repo->>DB: INSERT INTO dna_records...
        end
        Service-->>Controller: Retorna nuevo resultado
    end

    alt Es Mutante
        Controller-->>Cliente: 200 OK
    else Es Humano
        Controller-->>Cliente: 403 Forbidden
    end
```
