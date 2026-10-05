# Prueba mecanum y simulacion 2D

Rama: `mecanumTest`. El mismo comando y mezclador mecanum se ejecutan en computadora mediante `MecanumDriveIOSim` (antes `IOFake`). No necesita robot, Hub, pods ni Android para simular.

## Ejecutar en computadora

Requiere JDK 17 con `java` y `javac` en PATH y AdvantageScope de escritorio instalado. Desde la raiz del repositorio, en PowerShell:

```powershell
.\scripts\run-mecanum-sim.ps1
```

El script compila Java, ejecuta 72 comprobaciones y genera:

- `build/mecanum-sim/mecanum-demo.wpilog`: registro con pose estructurada para el campo 2D.
- `build/mecanum-sim/mecanum-demo.csv`: datos numericos para inspeccion adicional.

Los archivos generados no se versionan. El script no descarga dependencias. Es una reproduccion de una secuencia programada; no recibe gamepad en vivo.

## Ver el movimiento en AdvantageScope

1. File -> Open Log, abrir `build/mecanum-sim/mecanum-demo.wpilog`.
2. Abrir la pestaña **2D Field**. Expandir **Sim** en la barra izquierda.
3. Arrastrar **Pose (Pose2d)** a **Poses**. En el icono de esa fila seleccionar **Robot**. Quitar referencias de otros registros si existen.
4. Elegir el campo FTC **2026-2027 Field**; aceptar el aviso de soporte experimental. Tambien sirve el campo FTC Evergreen.
5. Seleccionar un tiempo en la barra superior y usar el boton de reproduccion. El robot debe cambiar de posicion y orientacion.

La pose usa metros y radianes, con origen en el centro del campo y la convencion FTC de la vista. El tipo `struct:Pose2d` incluye esquemas para decodificar esas unidades; no requiere convertir un arreglo manualmente. La orientacion de la imagen del campo puede hacer que el frente inicial aparezca hacia abajo.

Comprobaciones visuales:

| Tiempo | Resultado ideal |
|---|---|
| 0 s | x=0, y=0, rumbo=0 grados |
| 2 s | x=0.90 m, y=0 |
| 4 s | x=0.90 m, y=0.90 m |
| 8 s | Vuelve al origen |
| 10 s | Giro de 90 grados en el origen |
| 12 s | Avanza en el eje global Y con rumbo de 90 grados |
| 12-14 s | Combina traslacion y giro |
| 14-18 s | Deshabilitado: potencias cero y pose constante |

Para graficar salidas, arrastrar `Sim/WheelPowersFLFRRLRR` a **Line Graph**. El orden es delantera izquierda, delantera derecha, trasera izquierda, trasera derecha. `Sim/Phase` y `DriverStation/Enabled` permiten localizar cada fase y la parada.

Modificar la secuencia o los parametros en `TeamCode/src/sim/java/org/firstinspires/ftc/teamcode/sim/MecanumSimMain.java`, ejecutar el script otra vez y volver a abrir el registro.

## Arquitectura

```text
OpMode (pendiente de hardware) -> DriveMecanum -> MecanumDrive -> MecanumDriveIO
                                                                  |-- IOSim
                                                                  |-- IOReal (pendiente)
```

- `DriveMecanum`: coordina ejecutar y detener; todavia no utiliza scheduler FTCLib.
- `MecanumDrive`: deadband, normalizacion y limite de potencia; detiene ante entrada invalida o deshabilitacion.
- `WheelPowers`: valores inmutables FL, FR, RL, RR en [-1, 1].
- `MecanumDriveIO`: contrato de salida de las cuatro ruedas.
- `MecanumDriveIOSim`: convierte velocidades ideales de las ruedas en movimiento 2D e integra la pose. Su pose representa la verdad del modelo; no es odometria estimada.
- `MecanumDriveIOReal`: pendiente de configurar motores; arroja una excepcion si se intenta usar para evitar una implementacion silenciosamente inoperante.
- `MecanumDriveTest`: sigue vacio y `@Disabled`, pendiente de la prueba fisica.
- `src/sim/java`: ejecutor, pruebas y escritor WPILOG exclusivos de computadora, fuera del APK.

Entradas del comando: adelante positivo, izquierda positiva, giro antihorario positivo; referidas al robot. Al conectar gamepad FTC, convertir sus signos explicitamente. No hay Pedro, odometria, IMU, vision ni sensores adicionales.

## Alcance de la simulacion

Modelo cinematico ideal: sin inercia, deslizamiento, bateria, carga ni colisiones con el campo. Parametros demostrativos: 1.5 m/s a plena potencia de rueda, distancia entre ejes y ancho de 0.40 m, limite de potencia 0.30 y deadband 0.06. No son constantes calibradas para el robot.

Sirve para comprobar signos, mezcla mecanum, movimiento combinado y parada. No valida cableado, orientacion real de motores/rodillos, agarre, PID ni precision fisica. La configuracion y pruebas reales quedan pendientes.

## Base Android y validacion

Se conserva FTC SDK **v12.0**, dependencias FTC **12.0.0**, Gradle Wrapper **9.1.0** y Android Gradle Plugin **8.13.2**, con sus licencias y muestras.

Origen: [release oficial v12.0](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases/tag/v12.0), commit `e14c2aeb33e84d4ea21697beeea9ac49557be870`.

Abrir la raiz en Android Studio y configurar el SDK local (`local.properties` no se versiona). Compilar:

```powershell
.\gradlew.bat :TeamCode:assembleDebug
```

Validado: 72 comprobaciones de mezcla e integracion, compilacion Android y carga del WPILOG en AdvantageScope, verificando (0.90, 0.90, 0 grados) a 4 s y (0, 0, 90 grados) a 10 s sobre el campo FTC 2026-2027.

Los archivos locales preexistentes `MecanumBasico.java`, `PRIMERA-PRUEBA-MECANUM.md` y `Plan-programacion-BIOBUZZ-2026-2027.md` se conservan fuera de estos cambios. Si `MecanumBasico.java` sigue en el arbol local, Android Studio tambien lo compila; no aparece al clonar esta rama limpia.

Referencias: [campo 2D](https://docs.advantagescope.org/tab-reference/2d-field/), [coordenadas](https://docs.advantagescope.org/more-features/coordinate-systems/), [formato WPILOG](https://github.com/wpilibsuite/allwpilib/blob/v2026.2.1/wpiutil/doc/datalog.adoc).
