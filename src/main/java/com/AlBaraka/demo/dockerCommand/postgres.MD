
docker run -d   --name baraka   -p 3333:5432   -e POSTGRES_DB=albaraka_db   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres     postgres:15






### for keykloc 

there is image of keyloc server configured  with realm (baraka) , client (public-client-1) , scope (operation.read)
use to create and run  container 




 ### used this command to transfer the keaylok configuration to ontside the docker image 
docker exec 10d71cfe5dc6 /opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/export

docker cp <container_id>:/opt/keycloak/data/export ./keycloak-export


 - now we have the  config in the hsot machine  we used in the Dockerfile 

  ``` bash 
FROM quay.io/keycloak/keycloak:24.0.4

COPY keycloak-export /opt/keycloak/data/import

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]

```


build the image  : docker build -t barakakey:latest .


### we creatingthe container using this command
docker run --name baraka-oauth -p 6600:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin barakakey:latest



d

### for the baraka  app api 

docker run -d --name baraka-container10 --network baraka-net -p 1212:1212 -e SPRING_PROFILES_ACTIVE=docker baraka-image-10



























docker run -d   --name baraka-oauth   --network baraka-net   -p 6600:8080   -e KEYCLOAK_ADMIN=admin   -e KEYCLOAK_ADMIN_PASSWORD=admin   -e KC_PROXY=edge   -e KC_HOSTNAME=localhost   -e KC_HOSTNAME_PORT=6600   -e KC_HOSTNAME_STRICT=false   -e KC_HTTP_ENABLED=true   barakakey:latest
