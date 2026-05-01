#!/bin/bash

echo "==========================================="
echo "Starting CSBU Microservices Backend on AWS"
echo "==========================================="

# 1. Fetch Public IPv4 of the EC2 instance using IMDSv2
echo "Fetching EC2 Public IPv4..."
TOKEN=$(curl -s -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600")
export PUBLIC_IP=$(curl -s -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/meta-data/public-ipv4)

if [ -z "$PUBLIC_IP" ] || [[ "$PUBLIC_IP" == *"404"* ]] || [[ "$PUBLIC_IP" == *"xml"* ]]; then
    echo "Warning: Could not fetch Public IP. Are you running this on an EC2 instance? Defaulting to localhost."
    export PUBLIC_IP="localhost"
fi

echo "Backend services will use Public IP: $PUBLIC_IP"

# 2. Set Env variables manually if RDS is remote
# If you launched RDS separately, change RDS_HOST to your AWS RDS Endpoint URL
export RDS_HOST=localhost
export RDS_PORT=5435
export RDS_USER=group3admin
export RDS_PASSWORD=Group3123456789!

# Setting Eureka environment variables so the public IP is used for cross-origin / URL links
export JVM_ARGS="-Xmx512m -Duser.timezone=UTC -Deureka.instance.hostname=$PUBLIC_IP -Deureka.instance.ip-address=$PUBLIC_IP -Deureka.instance.prefer-ip-address=true"

# Optional AWS Credentials & configuration (Recommended: Use EC2 IAM Role instead of hardcoding)
# export AWS_ACCESS_KEY_ID="your-access-key"
# export AWS_SECRET_ACCESS_KEY="your-secret-key"
export AWS_REGION="ap-southeast-1"

# Helper function to run services
run_service() {
    local SERVICE_NAME=$1
    local SERVICE_DIR=$2
    
    echo "Building and starting $SERVICE_NAME..."
    cd $SERVICE_DIR
    chmod +x ./mvnw
    
    # Optional: Build the JAR if you want to run via java -jar instead of mvnw to save memory
    # ./mvnw clean package -DskipTests
    # nohup java $JVM_ARGS -jar target/*.jar > ${SERVICE_NAME}.log 2>&1 &

    # Launching via Maven
    nohup ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="$JVM_ARGS" > ${SERVICE_NAME}.log 2>&1 &
    cd - > /dev/null
}

# 3. Launch Services in Order
run_service "config-server" "services/config-server"
echo "Waiting 20 seconds for config-server to boot..."
sleep 20

run_service "discovery" "services/discovery"
echo "Waiting 20 seconds for discovery to boot..."
sleep 20

run_service "finance" "services/finance"
run_service "projects" "services/projects"
run_service "users-auth" "services/users-auth"

echo "All services have been launched in the background!"
echo "To view logs, run for example: tail -f services/finance/finance.log"
