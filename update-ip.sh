#!/bin/bash

# 1. Ask for the new IP address
echo "------------------------------------------"
echo "🚀 Car Rental System IP Updater"
echo "------------------------------------------"
echo -n "Enter the new Public IP: "
read NEW_IP

# Validate that the input is not empty
if [ -z "$NEW_IP" ]; then
    echo "❌ Error: IP address cannot be empty."
    exit 1
fi

# 2. Define the files that need updating
FRONTEND_AXIOS="Car Rental System Frontend/src/api/axios.js"
BACKEND_CORS="Car Rental System Backend/src/main/java/com/carrental/carrentalsystem/config/CorsConfig.java"
BACKEND_PROPS="Car Rental System Backend/src/main/resources/application.properties"
BACKEND_OAUTH="Car Rental System Backend/src/main/java/com/carrental/carrentalsystem/security/OAuth2AuthenticationSuccessHandler.java"

echo "🔧 Updating configuration files..."

# 3. Use regex to find and replace any IP-like string starting with http://
# This regex looks for 1-3 digits followed by dots, four times.
IP_REGEX="http://[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}"

sed -i "s|$IP_REGEX|http://$NEW_IP|g" "$FRONTEND_AXIOS"
sed -i "s|$IP_REGEX|http://$NEW_IP|g" "$BACKEND_CORS"
sed -i "s|$IP_REGEX|http://$NEW_IP|g" "$BACKEND_PROPS"
sed -i "s|$IP_REGEX|http://$NEW_IP|g" "$BACKEND_OAUTH"

echo "✅ Files updated successfully."

# 4. Rebuild the Docker containers
echo "🛠️ Rebuilding Docker containers (this may take a minute)..."
sudo docker compose up -d --build

echo "------------------------------------------"
echo "🎉 Deployment complete at http://$NEW_IP"
echo "------------------------------------------"
echo "⚠️  Reminder: Don't forget to update your GitHub OAuth Callback URL manually!"
