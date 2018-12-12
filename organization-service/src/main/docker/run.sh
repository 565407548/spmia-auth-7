#!/bin/sh

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z eurekaserver  $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the database server to start on port $DATABASE_PORT"
echo "********************************************************"
while ! `nc -z database $DATABASE_PORT`; do sleep 3; done
echo "******** Database Server has started "

echo "********************************************************"
echo "Waiting for the configuration server to start on port $config-service_PORT"
echo "********************************************************"
while ! `nc -z config-service $config-service_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Starting Organization Service                           "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI             \
     -Dspring.cloud.config.uri=$config-service_URI                          \
     -Dspring.profiles.active=$PROFILE                                   \
     -Dsecurity.oauth2.resource.userInfoUri=$AUTHSERVER_URI               \
     -jar /usr/local/organizationservice/@project.build.finalName@.jar
