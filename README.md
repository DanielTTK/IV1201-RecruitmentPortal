# IV1201-RecruitmentPortal

## Miljö:

- Java 17
- VS Code (med extensions Java + Spring Boot Extension Pack)

## Instruktioner:

Vi kör Postgres i Neon (moln). Flyway-migreringar ligger i repo, DB är redan importerad/migrerad i Neon.

### 1) Skapa lokal konfigfil (gitignored)
1. `cd recruitment`
2. `cp .env.example .env.properties`
3. Öppna `.env.properties` och fyll i lösenordet från Discord


### Starta appen
```bash
./mvnw spring-boot:run
```

Öppna i webbläsaren:
```bash
http://localhost:8080
```
