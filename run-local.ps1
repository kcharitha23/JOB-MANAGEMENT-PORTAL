$ErrorActionPreference = "Continue"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$m2 = Join-Path $env:USERPROFILE ".m2\repository"

[xml]$factoryPath = Get-Content (Join-Path $projectRoot ".factorypath")
$jars = $factoryPath.factorypath.factorypathentry | ForEach-Object {
    $_.id -replace "^M2_REPO/", ($m2 + "/") -replace "/", "\"
}

$h2Jar = Join-Path $m2 "com\h2database\h2\2.3.232\h2-2.3.232.jar"
if (Test-Path $h2Jar) {
    $jars += $h2Jar
}

$classpath = @((Join-Path $projectRoot "target\classes")) + $jars -join ";"

java --enable-native-access=ALL-UNNAMED -cp $classpath com.jobportal.JobPortalApplication `
    "--spring.datasource.url=jdbc:h2:mem:job_portal;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false" `
    "--spring.datasource.username=sa" `
    "--spring.datasource.password=" `
    "--spring.datasource.driver-class-name=org.h2.Driver" `
    "--spring.jpa.hibernate.ddl-auto=update" `
    "--spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
