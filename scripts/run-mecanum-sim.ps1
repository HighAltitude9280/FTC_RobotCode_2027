param([string]$OutputDirectory = '')
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$classes = Join-Path $projectRoot 'build/mecanum-sim/classes'
if (-not $OutputDirectory) { $OutputDirectory = Join-Path $projectRoot 'build/mecanum-sim' }
$sourceRoot = Join-Path $projectRoot 'TeamCode/src/main/java/org/firstinspires/ftc/teamcode'
$simRoot = Join-Path $projectRoot 'TeamCode/src/sim/java'
New-Item -ItemType Directory -Force -Path $classes | Out-Null
$sources = @(
    (Join-Path $sourceRoot 'drive/WheelPowers.java'),
    (Join-Path $sourceRoot 'drive/MecanumDrive.java'),
    (Join-Path $sourceRoot 'drive/io/MecanumDriveIO.java'),
    (Join-Path $sourceRoot 'drive/io/MecanumDriveIOSim.java'),
    (Join-Path $sourceRoot 'commands/DriveMecanum.java')
) + @(Get-ChildItem -LiteralPath $simRoot -Filter '*.java' -Recurse | ForEach-Object FullName)
& javac --release 8 -encoding UTF-8 -d $classes $sources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
& java -cp $classes org.firstinspires.ftc.teamcode.sim.MecanumSimTests
if ($LASTEXITCODE -ne 0) { throw 'Simulation tests failed' }
& java -cp $classes org.firstinspires.ftc.teamcode.sim.MecanumSimMain $OutputDirectory
if ($LASTEXITCODE -ne 0) { throw 'Simulation failed' }
