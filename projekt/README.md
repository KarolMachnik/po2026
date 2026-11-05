# DisKrak - Inteligentna Dyspozytornia MPK Kraków

Projekt zaliczeniowy z Programowania Obiektowego 2026/27.

Zespół: **Karol Machnik**, **Hubert Śliwa**

DisKrak to symulator dyspozytorni komunikacji miejskiej osadzony w prawdziwej sieci MPK Kraków.
Gracz wciela się w dyspozytora: pilnuje ruchu tramwajów i autobusów na mapie, zarządza budżetem,
taborem i kierowcami z tachografem, buduje nowe przystanki i linie zastępcze „Za Tramwaj”,
reaguje na wypadki (zablokowana ul. Długa, wybrzuszenie szyny na Basztowej, tłum pod Tauron Areną)
i korzysta z asystenta, który przygotowuje karty rekomendacji z możliwością cofnięcia decyzji.
Dzielnice Krakowa oceniają decyzje dyspozytora według własnych wag wrażliwości, a stan gry można
zapisać w bazie i przeanalizować na wykresach słupkowych.

## Technologie

| Warstwa | Technologie |
| --- | --- |
| Backend | Java 21, Spring Boot 3.5, Spring Data JPA (Hibernate), Flyway, WebSocket (STOMP) |
| Baza danych | PostgreSQL 16 w Dockerze |
| Frontend | Next.js (TypeScript), HeroUI, Leaflet, Recharts |
| Diagramy | PlantUML |

## Struktura katalogu

```
projekt/
├── README.md               opis projektu
├── EPIKI.md                opis 10 głównych epików i ich stan
├── docker-compose.yml      PostgreSQL 16 + Adminer
├── uml/                    diagramy PlantUML
└── backend/                aplikacja Spring Boot (Maven)
    ├── pom.xml
    └── src/
        ├── main/java/pl/agh/po/diskrak/
        │   ├── DisKrakApplication.java
        │   ├── network/        graf miasta, Dijkstra, linie, kreator sieci
        │   └── persistence/    encje JPA, repozytoria, ładowanie sieci z bazy
        ├── main/resources/
        │   ├── application.yml
        │   └── db/migration/   migracje Flyway V1-V5
        └── test/java/          testy JUnit 5
```

Kolejne epiki dodadzą pakiety `fleet`, `staff`, `simulation`, `incidents`, `gamification`,
`assistant` oraz katalog `frontend/`.

## Dane Krakowa

Migracja `V5__seed_krakow_mpk_data.sql` zawiera sieć zbudowaną na podstawie rozkładów GTFS MPK Kraków:

| Dane | Liczba |
| --- | --- |
| Dzielnice | 18 |
| Przystanki i węzły (w tym 3 zajezdnie) | 1548 |
| Odcinki sieci | 3992 |
| Warianty linii (tramwajowe, autobusowe, zastępcze) | 507 |
| Modele pojazdów | 4 |

## Uruchomienie

Wymagania: Java 21, Docker.

```bash
docker compose up -d
cd backend
./mvnw spring-boot:run
```

Przy starcie Flyway tworzy schemat i wczytuje dane Krakowa. Backend działa na porcie `8080`,
Adminer (podgląd bazy) na `http://localhost:8081` - serwer `postgres`, użytkownik `diskrak_user`,
hasło `diskrak_pass`, baza `diskrak_db`.

Testy:

```bash
cd backend
./mvnw test
```

## Stan prac

| Epic | Zakres | Osoba | Stan |
| --- | --- | --- | --- |
| 1 | Baza danych SQL i seed Krakowa | Karol Machnik | gotowe |
| 2 | Graf miasta i kreator sieci | Karol Machnik | gotowe |
| 3 | Flota, sklep taborowy i budżet | Hubert Śliwa | do zrobienia |
| 4 | Kadry, karty kierowców i tachograf | Hubert Śliwa | do zrobienia |
| 5 | Silnik symulacji i mapa live | Karol Machnik | do zrobienia |
| 6 | Modularny system wypadków | Hubert Śliwa | do zrobienia |
| 7 | Grywalizacja, dzielnice i Spotted MPK | Hubert Śliwa | do zrobienia |
| 8 | Inteligentny asystent | Karol Machnik | do zrobienia |
| 9 | Save/Load i analityka słupkowa | Karol Machnik | do zrobienia |
| 10 | Frontend Next.js i HeroUI | Karol Machnik | do zrobienia |

Szczegóły każdego epiku: [EPIKI.md](EPIKI.md). Diagramy: [uml/](uml/README.md).
