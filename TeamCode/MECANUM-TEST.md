# Esqueleto de prueba mecanum

Rama: `mecanumTest`. Solo estructura; no implementa conduccion ni conecta motores.

## Base oficial

- FTC SDK **v12.0**, dependencias FTC **12.0.0**.
- Fuente: https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases/tag/v12.0
- Commit de origen: `e14c2aeb33e84d4ea21697beeea9ac49557be870`.
- Gradle Wrapper 9.1.0, Android Gradle Plugin 8.13.2, JDK 17 o compatible.
- Android Studio Narwhal 3 Feature Drop o posterior, segun el release oficial.
- Se conserva el SDK, sus muestras, README, licencia y configuracion de compilacion.

## Arquitectura reservada

Dentro de `src/main/java/org/firstinspires/ftc/teamcode/`:

```text
opmodes/
  MecanumDriveTest.java       OpMode vacio, marcado @Disabled
commands/
  DriveMecanum.java           Lugar reservado para coordinacion de conduccion
drive/
  MecanumDrive.java           Subsistema vacio
  io/
    MecanumDriveIO.java       Interfaz vacia
    MecanumDriveIOReal.java   Implementacion vacia de hardware
    MecanumDriveIOFake.java   Implementacion vacia para pruebas
```

Flujo futuro: OpMode -> comando -> subsistema -> IO -> motores.
Las clases son intencionadamente vacias, salvo las declaraciones y los metodos
obligatorios del OpMode. No hay mapeo de hardware, potencias, mezcla mecanum,
lectura de controles ni contrato de metodos definido todavia.

No se agregan Pedro, FTCLib, Dashboard, odometria, IMU, vision ni otros subsistemas.
La carpeta de comandos solo reserva la capa de la arquitectura; todavia no tiene
scheduler ni dependencias adicionales. Las muestras incluidas en el SDK no son
implementaciones de nuestro robot.

## Abrir y verificar

Abrir la raiz del repositorio en Android Studio y sincronizar Gradle. Configurar
el SDK de Android local en el IDE; `local.properties` no se versiona.

En Windows, desde la raiz:

```powershell
.\gradlew.bat :TeamCode:assembleDebug
```

Compilar solo verifica el proyecto; no valida comportamiento mecanico. Este
esqueleto no debe aparecer en la lista de OpModes porque permanece `@Disabled`.
No quitar esa anotacion hasta implementar y revisar la prueba fisica.

Los archivos locales preexistentes `MecanumBasico.java`,
`PRIMERA-PRUEBA-MECANUM.md` y `Plan-programacion-BIOBUZZ-2026-2027.md` no forman
parte de este commit. Se conservan intactos. Si `MecanumBasico.java` sigue en el
arbol de fuentes local, Android Studio tambien lo compila; no es el esqueleto
deshabilitado nuevo ni aparece al clonar esta rama limpia.
