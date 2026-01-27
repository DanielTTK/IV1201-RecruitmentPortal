# IV1201-RecruitmentPortal

## Miljö:

- Java 17
- VS Code (med extensions Java + Spring Boot Extension Pack)
- Docker Desktop (startat)

## Instruktioner:

Vi kör Postgres lokalt i Docker för att underlätta utveckling just nu. Senare kommer den ligga i en molntjänst.

### 1) Lägg till databasen
1. Ladda ner `existing-database.sql` från [Canvas](https://canvas.kth.se/courses/59268/pages/project)
2. Flytta till:
  `recruitment/db/init/existing-database.sql`

### 2) Starta databasen

```bash
cd recruitment
docker compose up -d
```

### Starta appen
```bash
./mvnw spring-boot:run
```

Öppna i webbläsaren:
```bash
http://localhost:8080
```

### Stoppa appen:

```bash
docker compose down
Ctrl + C i terminalen
```
