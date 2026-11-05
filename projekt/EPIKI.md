# Epiki projektu DisKrak

Projekt jest podzielony na 10 głównych epików. Każdy epik to osobny moduł (pakiet Javy lub część
frontendu) kończący się commitem.

## Epic 1 - Baza danych SQL, konfiguracja i seed Krakowa

**Osoba:** Karol Machnik · **Stan:** gotowe · **Technologie:** PostgreSQL, Docker, Flyway, JPA/Hibernate

- `docker-compose.yml` z PostgreSQL 16 (`diskrak_db`, port 5432) i Adminerem.
- Projekt Spring Boot 3 (Java 21) z JPA, Flyway, Web i WebSocket; `ddl-auto: validate`.
- Migracje Flyway:
  - `V1` - dzielnice, przystanki (`stop_nodes`) i odcinki sieci (`track_segments`),
  - `V2` - katalog pojazdów, linie i kolejność przystanków na trasach,
  - `V3` - sloty zapisu gry oraz zapisani kierowcy, pojazdy i incydenty,
  - `V4` - godzinowe migawki analityczne i dziennik decyzji dyspozytora,
  - `V5` - seed Krakowa: 18 dzielnic, 4 modele pojazdów (Lajkonik z baterią, Krakowiak,
    Solaris Urbino 18 Electric, Jelcz M121M), 1548 przystanków z 3 zajezdniami,
    3992 odcinki i 507 wariantów linii (m.in. 4, 18, 50, 179).
- Encje JPA i repozytoria Spring Data w `persistence`, a `CityNetworkLoader` ładuje sieć z bazy
  do obiektów domenowych.

## Epic 2 - Graf miasta, podgląd tras i kreator sieci (`network`)

**Osoba:** Karol Machnik · **Stan:** gotowe · **Wzorce:** dziedziczenie, polimorficzny Dijkstra, Builder

- `Coordinates` - odległość wzorem Haversine'a i interpolacja pozycji.
- `StopNode` i dziedzicząca po nim `Depot` (zajezdnia z pojemnością i listą zaparkowanych pojazdów).
- `TrackSegment` - prędkość efektywna z uwzględnieniem korków i blokad, czas przejazdu.
- `CityGraph` - algorytm Dijkstry z predykatem `Predicate<TrackSegment>` (np. `vehicle::canTraverse`)
  oraz budowa nowego przystanku z podziałem odcinka w obu kierunkach (180 000 / 350 000 PLN).
- `Route` i `RoutePreviewDTO` - długość trasy, czas przejazdu, polilinia dla Leafleta i wąskie gardła.
- `RouteBuilder` - budowa linii stałych i zastępczych krok po kroku.

## Epic 3 - Flota, polimorfizm, sklep taborowy i budżet (`fleet`)

**Osoba:** Hubert Śliwa · **Stan:** do zrobienia · **Wzorce:** klasa abstrakcyjna, State, Factory Method

- Abstrakcyjna klasa `Vehicle` z podklasami `Tram`, `ElectricBus` i `DieselBus`; każda inaczej
  implementuje `canTraverse` i zużycie energii lub paliwa.
- Lajkonik z baterią przejeżdża odcinki bez zasilania; autobusy nie wjeżdżają na torowisko.
- Wzorzec State: `InDepotReserveState`, `InTransitState`, `DelayedState`, `ChargingState`, `BrokenDownState`.
- `BudgetManager` na `BigDecimal` (start 2 500 000 PLN) i `VehicleFactory` kupująca pojazdy
  według kodów `LAJKONIK`, `KRAKOWIAK`, `SOLARIS_E18`, `JELCZ_DIESEL`.

## Epic 4 - Kadry: losowi kierowcy, karty MPK i tachograf (`staff`)

**Osoba:** Hubert Śliwa · **Stan:** do zrobienia · **Wzorce:** enkapsulacja, Strategy, generator

- `ShiftTimeTracker` pilnuje limitu 4,5 h jazdy ciągłej oraz 8 h zmiany (+ maks. 2 h nadgodzin).
- Cechy kierowców jako Strategy: Mistrz Eko-Jazdy, Rajdowiec z Alej, Oaza Spokoju, Twardy Związkowiec.
- `Driver` z eksportem do `DriverCardDTO` i `DriverGenerator` losujący kadrę z polskimi danymi.

## Epic 5 - Silnik symulacji, WebSocket i mapa bazowa (`simulation`)

**Osoba:** Karol Machnik · **Stan:** do zrobienia · **Technologie:** wielowątkowość, STOMP, Next.js, Leaflet

- `SimulationWorldState` - wspólny stan gry ładowany z bazy przy starcie (sieć, flota, kierowcy, budżet).
- `SimulationTickEngine` - krok co 500 ms: ruch po odcinkach grafu, zużycie energii, tachografy,
  pasażerowie i przychody z biletów; prędkości PAUSE / 1x / 5x / 15x.
- `WebSocketConfig` i `TelemetryBroadcaster` (`/ws-diskrak`, `/topic/telemetry`).
- Inicjalizacja frontendu Next.js z mapą Krakowa (CartoDB Dark Matter) i ruchem pojazdów na żywo.

## Epic 6 - Modularny system wypadków i scenariusze (`incidents`)

**Osoba:** Hubert Śliwa · **Stan:** do zrobienia · **Wzorce:** Composite / Effect

- `IncidentEffect` z parą `applyEffect` / `revertEffect` i 5 efektami: blokada torowiska,
  awaria zasilania, korek, tłum pasażerów, awaria pojazdu.
- `ModularIncident` składa wypadek z dowolnej liczby efektów i odlicza czas trwania.
- `ScenarioGenerator` z krakowskimi scenariuszami: Klątwa ulicy Długiej, wybrzuszenie szyny na
  Basztowej, awaria podstacji przy Rondzie Mogilskim, koncert w Tauron Arenie, paraliż na Alejach.

## Epic 7 - Grywalizacja: ważone dzielnice, Spotted MPK i ocena (`gamification`)

**Osoba:** Hubert Śliwa · **Stan:** do zrobienia · **Wzorce:** Observer, ważony model decyzyjny

- `District` z `DistrictPreferenceWeights` (punktualność, ekologia floty, tłok, szybkość linii
  zastępczych) reaguje na zdarzenia dyspozytorskie.
- `CitizenFeedbackGenerator` tworzy wpisy „Spotted: MPK Kraków”.
- `DispatcherRatingService` liczy ocenę 0-100, doświadczenie, rangę dyspozytora i godzinową dotację miasta.

## Epic 8 - Inteligentny asystent (`assistant`)

**Osoba:** Karol Machnik · **Stan:** do zrobienia · **Wzorce:** Command z `undo()`, Strategy

- Komendy: objazd linii, linia zastępcza, podmiana zmęczonego kierowcy, przytrzymanie pojazdu
  dla wyrównania taktu.
- Strategie: dynamiczny objazd, linia autobusowa „Za Tramwaj”, anti-bunching, podmiana kierowcy.
- `DispatchRecommendation` z oceną pewności, kosztem i listą komend; REST API do zatwierdzania,
  odrzucania i cofania decyzji.

## Epic 9 - System Save/Load i analityka słupkowa (`persistence`)

**Osoba:** Karol Machnik · **Stan:** do zrobienia · **Wzorce:** Memento, transakcje SQL

- `SimulationWorldMemento` i `SaveGameManager` zapisują pełny stan gry w tabeli `save_slots`
  i odtwarzają go bez restartu serwera.
- `AnalyticsCollector` buduje dane pod 4 wykresy: finanse, zadowolenie dzielnic, wydajność linii
  i czas pracy kierowców.

## Epic 10 - Kompletny interfejs dyspozytora (Next.js + HeroUI + Recharts)

**Osoba:** Karol Machnik · **Stan:** do zrobienia · **Technologie:** Next.js, HeroUI, Leaflet, Recharts

- Górny HUD z budżetem, rangą, oceną i zegarem symulacji.
- Lewy panel: karty rekomendacji asystenta z historią Undo, kreator sieci (podgląd tras, budowa
  przystanków, linie zastępcze) oraz lista kierowców z kartą MPK i tachografem.
- Prawy panel: nastroje dzielnic, feed Spotted MPK i wyzwalacz scenariuszy.
- Modale: sklep taborowy, wykresy słupkowe oraz zapis i wczytywanie gry.
