Anymessenger application
============================


## Quick start

[SBT 1.0.x](http://www.scala-sbt.org/download.html) and JDK 1.8 are required to build the project.

```
$ https://github.com/Blinovskiy/anymessenger.git
$ cd anymessenger
$ sbt
sbt:anymessenger> reStart
```

Main settings:
```./src/main/resources/application.conf```


## Run in development mode

Docker postgres run command:

```
docker run --name postgresdb -e POSTGRES_PASSWORD=anympass -d -p 5432:5432 postgres
``` 

Apply migration:

```
./db/migrations/0.0.1/0.0.1-DB_INIT.sql
```

Build UI:
```
npm run-script build
```

Or using npm dev server (localhost:3000):
```
npm start
```

Start:

```
sbt
sbt:anymessenger> reStart
```
See existed API in [api.md](./!docs/api.md)

Stop:

```
sbt:anymessenger> reStop
```

Run with continuous recompilation/restart when sources are changed (press Enter to exit source monitoring mode):

```
sbt:anymessenger> ~reStart
```

Run tests:

```
sbt:anymessenger> test
```


## Generate Slick Tables

```
sbt:anymessenger> devTools/generate
```
Output is written to `slick_model/src/main/scala`

For changing sourced DB configuration please check `slick_gen/Config.scala` source file


## Build artifacts

```
sbt pack
```

Output is written to `target/scala-2.12/anymessenger.jar`.


Resources
=========

- Slick 3.2.1 Documentation
  <http://slick.lightbend.com/doc/3.2.1/>

- HTTP for Scala
  <http://http4s.org/>

- Docker postgres database
  <https://docs.docker.com/samples/library/postgres/>