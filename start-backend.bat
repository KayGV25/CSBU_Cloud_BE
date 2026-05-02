@echo off
echo ===========================================
echo Starting CSBU Microservices Backend
echo ===========================================

set RDS_HOST=group3-rds.cubvcxxxfvcp.us-east-1.rds.amazonaws.com
set RDS_PORT=3306
set RDS_USER=group3admin
set RDS_PASSWORD=Group3123456789!
set APP_TIMEZONE=UTC

echo Starting config-server...
start "Config Server" cmd /k "cd services\config-server && set RDS_HOST=%RDS_HOST%&& set RDS_PORT=%RDS_PORT%&& set RDS_USER=%RDS_USER%&& set RDS_PASSWORD=%RDS_PASSWORD%&& mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=%APP_TIMEZONE%""

echo Waiting for config-server to initialize (15 seconds)...
timeout /t 15 /nobreak >nul

echo Starting discovery...
start "Discovery Service" cmd /k "cd services\discovery && set RDS_HOST=%RDS_HOST%&& set RDS_PORT=%RDS_PORT%&& set RDS_USER=%RDS_USER%&& set RDS_PASSWORD=%RDS_PASSWORD%&& mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=%APP_TIMEZONE%""

echo Waiting for discovery to initialize (15 seconds)...
timeout /t 15 /nobreak >nul

echo Starting finance, projects, and users-auth...
start "Finance Service" cmd /k "cd services\finance && set RDS_HOST=%RDS_HOST%&& set RDS_PORT=%RDS_PORT%&& set RDS_USER=%RDS_USER%&& set RDS_PASSWORD=%RDS_PASSWORD%&& mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=%APP_TIMEZONE%""
start "Projects Service" cmd /k "cd services\projects && set RDS_HOST=%RDS_HOST%&& set RDS_PORT=%RDS_PORT%&& set RDS_USER=%RDS_USER%&& set RDS_PASSWORD=%RDS_PASSWORD%&& mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=%APP_TIMEZONE%""
start "Users-Auth Service" cmd /k "cd services\users-auth && set RDS_HOST=%RDS_HOST%&& set RDS_PORT=%RDS_PORT%&& set RDS_USER=%RDS_USER%&& set RDS_PASSWORD=%RDS_PASSWORD%&& mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=%APP_TIMEZONE%""

echo All services have been launched in separate windows!
