Introduction
------------
- This repository demo for batch job execution in spring batch

Run Locally
------------
- Pull postgres DB and run with ```docker run --name scdf-share-db -e POSTGRES_PASSWORD=scdf-share-db -e POSTGRES_USER=scdf-share-db -p 5432:5432 -d postgres```
- Create new schema for DB with value in ```spring.datasource.schema``` and ```spring.liquibase.schema```
- Build project ```./gradlew build```
- Run project ```./gradlew bootrun```
  - In ```Main.java``` change job parameter to execute