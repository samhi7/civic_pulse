# CivicPulse Nexus Local Development Startup Script

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "             CivicPulse Nexus: Milestone 4                 " -ForegroundColor Green
Write-Host "      Smart Governance & Citizen Services Platform        " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

$rootDir = $PSScriptRoot
$javaExe = (Get-Command java).Source
$npmCmd = (Get-Command npm.cmd).Source
$logDir = "$rootDir\logs"
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir | Out-Null }

Write-Host "Cleaning up previous background instances..." -ForegroundColor Gray
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
Stop-Process -Name "node" -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 1

Write-Host "Spawning Backend Microservices..." -ForegroundColor Cyan

$services = @(
    @{ Name = "Gateway Service"; Module = "gateway-service"; Port = 8080 },
    @{ Name = "Citizen Service"; Module = "citizen-service"; Port = 8081 },
    @{ Name = "Grievance Service"; Module = "grievance-service"; Port = 8082 },
    @{ Name = "Service Management Service"; Module = "service-management-service"; Port = 8083 },
    @{ Name = "Welfare Service"; Module = "welfare-service"; Port = 8084 },
    @{ Name = "Budget Service"; Module = "budget-service"; Port = 8085 },
    @{ Name = "Reporting Service"; Module = "reporting-service"; Port = 8086 }
)

foreach ($s in $services) {
    $jarPath = "$rootDir\$($s.Module)\target\$($s.Module)-1.0.0-SNAPSHOT.jar"
    Write-Host "-> Launching $($s.Name) on port $($s.Port)..." -ForegroundColor Gray
    Start-Process $javaExe -ArgumentList "-jar", "`"$jarPath`"" -WorkingDirectory $rootDir -RedirectStandardOutput "$logDir\$($s.Module).log" -RedirectStandardError "$logDir\$($s.Module)-err.log"
}

Write-Host "Waiting 12 seconds for microservices to initialize..." -ForegroundColor Gray
Start-Sleep -Seconds 12

Write-Host "Spawning React Frontend..." -ForegroundColor Cyan
Write-Host "-> Launching Vite Dev Server on http://localhost:3000" -ForegroundColor Gray
Start-Process $npmCmd -ArgumentList "run", "dev" -WorkingDirectory "$rootDir\frontend" -RedirectStandardOutput "$logDir\frontend.log" -RedirectStandardError "$logDir\frontend-err.log"

Start-Sleep -Seconds 4

Write-Host ""
Write-Host "Checking service health..." -ForegroundColor Cyan
foreach ($s in $services) {
    $conn = Test-NetConnection -ComputerName 127.0.0.1 -Port $s.Port -WarningAction SilentlyContinue
    if ($conn.TcpTestSucceeded) {
        Write-Host "  [OK] $($s.Name) is listening on port $($s.Port)" -ForegroundColor Green
    } else {
        Write-Host "  [STARTING] $($s.Name) on port $($s.Port)" -ForegroundColor Yellow
    }
}

$feConn = Test-NetConnection -ComputerName 127.0.0.1 -Port 3000 -WarningAction SilentlyContinue
if ($feConn.TcpTestSucceeded) {
    Write-Host "  [OK] React Frontend is listening on http://localhost:3000" -ForegroundColor Green
} else {
    Write-Host "  [STARTING] React Frontend on port 3000" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Opening browser..." -ForegroundColor Cyan
Start-Process "http://localhost:3000"

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Green
Write-Host " CivicPulse Nexus is running!" -ForegroundColor Green
Write-Host " Smart Dashboard URL: http://localhost:3000" -ForegroundColor Green
Write-Host " Logs Directory:     $logDir" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Green
Write-Host "Keeping orchestrator active. Press Ctrl+C to stop services." -ForegroundColor Cyan

while ($true) {
    Start-Sleep -Seconds 60
}
