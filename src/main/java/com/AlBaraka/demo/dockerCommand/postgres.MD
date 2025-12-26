
docker run -d   --name baraka   -p 3333:5432   -e POSTGRES_DB=albaraka_db   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres   -v postgres_data1:/var/lib/postgresql/data1   postgres:15






# for keykloc 

docker run --name baraka-oauth -p 6600:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin barakakey:latest

there is image of keyloc server configured  with realm (baraka) , client (public-client-1) , scope (operation.read)
use to create and run  container 