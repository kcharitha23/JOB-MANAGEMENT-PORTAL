$ErrorActionPreference = "Continue"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$out = Join-Path $projectRoot "target\run.log"
$err = Join-Path $projectRoot "target\run.err.log"

if (Test-Path $out) { Clear-Content -LiteralPath $out }
if (Test-Path $err) { Clear-Content -LiteralPath $err }

& (Join-Path $projectRoot "run-local.ps1") > $out 2> $err
