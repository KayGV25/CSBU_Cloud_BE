#!/bin/bash
set -e

echo "==========================================="
echo "Starting CSBU Microservices Backend"
echo "==========================================="

export RDS_HOST=group3-rds.cubvcxxxfvcp.us-east-1.rds.amazonaws.com
export RDS_PORT=3306
export RDS_USER=group3admin
export RDS_PASSWORD='Group3123456789!'
export APP_TIMEZONE=UTC

BASE_DIR=$(pwd)

# ------------------------------------------
# Wait for service via log detection
# ------------------------------------------
wait_for_service() {
    local SERVICE_NAME=$1
    local LOG_FILE=$2
    local RETRIES=40

    echo "Waiting for $SERVICE_NAME to be ready..."

    for i in $(seq 1 $RETRIES); do
        if grep -q "Started" "$LOG_FILE"; then
            echo "✅ $SERVICE_NAME is UP"
            return 0
        fi
        echo "⏳ $SERVICE_NAME not ready yet ($i/$RETRIES)..."
        sleep 2
    done

    echo "❌ ERROR: $SERVICE_NAME failed to start"
    echo "Last logs:"
    tail -n 20 "$LOG_FILE"
    return 1
}

# ------------------------------------------
# Start service
# ------------------------------------------
run_service() {
    local SERVICE_DIR=$1
    local SERVICE_NAME=$2

    echo "-------------------------------------------"
    echo "Starting $SERVICE_NAME..."

    cd "$BASE_DIR/$SERVICE_DIR" || exit 1
    chmod +x ./mvnw

    LOG_FILE="${SERVICE_NAME}.log"

    nohup ./mvnw spring-boot:run \
        -Dspring-boot.run.jvmArguments="-Xms128m -Xmx256m -Duser.timezone=$APP_TIMEZONE" \
        > "${SERVICE_NAME}.log" 2>&1 &

    PID=$!
    echo "$SERVICE_NAME started with PID $PID"

    wait_for_service "$SERVICE_NAME" "$LOG_FILE" || exit 1

    cd "$BASE_DIR" || exit 1
}

# ------------------------------------------
# Start sequence
# ------------------------------------------

run_service "services/config-server" "config-server"
run_service "services/discovery" "discovery"
run_service "services/finance" "finance"
run_service "services/projects" "projects"
run_service "services/users-auth" "users-auth"

echo "==========================================="
echo "✅ All services are UP and running!"
echo "==========================================="
