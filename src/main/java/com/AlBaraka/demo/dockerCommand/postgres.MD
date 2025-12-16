
docker run -d   --name baraka   -p 3333:5432   -e POSTGRES_DB=albaraka_db   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres   -v postgres_data1:/var/lib/postgresql/data1   postgres:15
