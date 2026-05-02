#!/bin/bash

echo "==========================================="
echo "Starting CSBU Microservices Backend"
echo "==========================================="

export RDS_HOST=group3-rds.cubvcxxxfvcp.us-east-1.rds.amazonaws.com
export RDS_PORT=3306
export RDS_USER=group3admin
export RDS_PASSWORD='Group3123456789!'
export APP_TIMEZONE=UTC

# Helper function to run services
run_service() {
    local SERVICE_DIR=$1
    local SERVICE_NAME=$2
    
    echo "Starting $SERVICE_NAME..."
    cd "$SERVICE_DIR" || exit
    chmod +x ./mvnw
    
    nohup ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=$APP_TIMEZONE" > "${SERVICE_NAME}.log" 2>&1 &
    echo "$SERVICE_NAME started with PID $!"
    cd - > /dev/null || exit
}

run_service "services/config-server" "config-server"

echo "Waiting for config-server to initialize (15 seconds)..."
sleep 15

run_service "services/discovery" "discovery"

echo "Waiting for discovery to initialize (15 seconds)..."
sleep 15

echo "Starting finance, projects, and users-auth..."
run_service "services/finance" "finance"
run_service "services/projects" "projects"
run_service "services/users-auth" "users-auth"

echo "==========================================="
echo "All services have been launched in the background!"
echo "To view logs, run for example: tail -f services/finance/finance.log"
echo "To stop services, you can run: pkill -f 'spring-boot:run'"
echo "==========================================="
