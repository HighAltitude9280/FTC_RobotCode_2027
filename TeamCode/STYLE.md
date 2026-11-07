# Convenciones de TeamCode

Estas reglas se aplican al código del equipo. Los nombres de APIs externas y los
identificadores de hardware conservan exactamente la escritura que necesitan.

## Nombres

| Elemento | Convención | Ejemplo |
| --- | --- | --- |
| Paquetes | minúsculas | `hardware.drivetrain` |
| Clases e interfaces | PascalCase | `Drivetrain`, `DrivetrainIOInputs` |
| Métodos y variables | camelCase | `updateInputs`, `targetRPM` |
| Constantes `static final` | UPPER_SNAKE_CASE | `TARGET_RPM`, `TICKS_PER_REV` |
| Parámetros modificables | camelCase | `kP`, `kI`, `kD`, `kF` |
| Variables de comandos | sufijo Command | `shooterCommand` |

- Escribir **Drivetrain** como una sola palabra; no `DriveTrain`.
- Conservar las siglas **IO**, **RPM**, **PIDF** e **IMU** en mayúsculas en los
  identificadores propios: `ShooterIO`, `getTargetRPM`, `setVelocityPIDF`.
- Usar `driveRobotCentric` tanto en el comando como en el subsistema.
- Los métodos deben describir lo que hacen: `runAtRPM` mantiene la velocidad del
  shooter; no implica que una pieza se haya alimentado o disparado.
- Dentro de `LowAltitudeConstants`, usar los grupos `Drivetrain`, `Intake` y
  `Shooter`. Evitar repetir el nombre del grupo en cada constante.
- Mantener las unidades explícitas: `headingRadians`, `headingDegrees`,
  `targetRPM`, `ticksPerSecond`.
- Al renombrar clases, cambiar también el archivo, imports, constructores y
  referencias. En Windows, los renombres que solo cambian mayúsculas deben pasar
  por un nombre temporal con `git mv` para que Git registre el cambio.

## Formato

Se utiliza **google-java-format 1.24.0, estilo AOSP**: cuatro espacios de sangría,
llaves de apertura en la misma línea, imports ordenados y sin imports sin uso.
El formateador determina los saltos de expresiones y parámetros largos. Separar
métodos con una línea vacía y evitar bloques de líneas vacías consecutivas.

Con el JAR del formateador disponible, aplicar a los archivos Java que se editan:

```text
java -jar google-java-format-1.24.0-all-deps.jar --aosp --replace Archivo.java
```

Para comprobar el formato sin modificar archivos:

```text
java -jar google-java-format-1.24.0-all-deps.jar --aosp --dry-run --set-exit-if-changed Archivo.java
```

## Comentarios y textos

- Identificadores en inglés y comentarios explicativos del equipo en español.
- Los procedimientos de Pedro conservan sus instrucciones en inglés.
- Usar la escritura **OpMode** en instrucciones y comentarios.
- Etiquetas técnicas estables: `Heading (rad)`, `Heading (deg)`,
  `Shooter Target RPM`, `Shooter Current RPM` y `Shooter RPM Error`.
- Las instrucciones al operador deben ser frases completas en español; conservar
  los nombres visibles de los OpModes para poder identificarlos.
- Los mensajes de error deben nombrar la prueba que realmente se ejecuta.

## Configuración y compatibilidad

Los parámetros PIDF de Panels se llaman ahora `LowAltitudeConstants.Shooter.kP`,
`kI`, `kD` y `kF`, en lugar de los campos `SHOOTER_P`, `SHOOTER_I`, `SHOOTER_D` y
`SHOOTER_F` del grupo `SHOOTER`. Revisar las referencias guardadas en la interfaz
de tuning después de actualizar. Se mantienen sus valores y su mutabilidad.

Los nombres de hardware (`frontLeft`, `frontRight`, `backLeft`, `backRight`,
`intake`, `shooter`, `imu`) no se cambian por estilo. Tampoco se sustituyen
automáticamente los nombres de ejemplo de los tuners (`lf`, `lr`, `rf`, `rr`).
Los recursos y plantillas del SDK conservan su formato original.
