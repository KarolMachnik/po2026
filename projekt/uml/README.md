# Diagramy UML projektu DisKrak

Diagramy są zapisane w PlantUML (`.puml`).

| Plik | Opis |
| --- | --- |
| `01-przypadki-uzycia.puml` | Przypadki użycia dyspozytora, asystenta i zegara symulacji |
| `02-sekwencja-podglad-trasy.puml` | Podgląd trasy linii (`Route.generatePreviewDTO`) |
| `02-sekwencja-budowa-przystanku.puml` | Budowa przystanku na odcinku (`CityGraph.buildCustomStopOnSegment`) |
| `02-sekwencja-linia-zastepcza.puml` | Zatwierdzenie rekomendacji i utworzenie linii zastępczej 704 (`RouteBuilder`) |
| `02-sekwencja-tick-symulacji.puml` | Pojedynczy krok symulacji z tachografem i przychodem z biletów |
| `02-sekwencja-zapis-gry.puml` | Zapis i wczytanie gry (Memento) |
| `03-diagram-klas.puml` | Klasy wszystkich pakietów backendu i relacje między nimi |
| `04-diagram-obiektow.puml` | Stan obiektów podczas wybrzuszenia szyny przy Teatrze Bagatela |
| `05-architektura.puml` | Komponenty frontendu, backendu i bazy danych |

Pakiety `network` i `persistence` na diagramie klas odpowiadają zaimplementowanemu kodowi (Epic 1 i 2).
Pozostałe pakiety opisują docelowy projekt z epików 3-10.
