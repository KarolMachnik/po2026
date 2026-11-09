0. Architektura i Pakiety
agh.po.lab4.animals - zawiera abstrakcyjną klasę bazową Animal oraz wygenerowane klasy poszczególnych gatunków (np. Dog, Snake, Parrot).

agh.po.lab4.zadania - zawiera klasy uruchomieniowe: główny program Zoo oraz narzędzie deweloperskie AnimalEditor.

1. Jak to działa? (Workflow)
Cały system opiera się na dwóch oddzielnych programach, które współpracują ze sobą na poziomie plików i mechanizmu refleksji:

Uruchomienie Kreatora (AnimalEditor): Użytkownik odpala program GUI, wprowadza dane nowego zwierzęcia i klika zapisz.

Generowanie Kodu: AnimalEditor fizycznie tworzy plik .java w odpowiednim folderze pakietu animals.

Kompilacja: Środowisko programistyczne automatycznie kompiluje nowy plik .java do pliku .class.

Uruchomienie Symulacji (Zoo): Główny program Zoo.java za pomocą refleksji skanuje skompilowane pliki w poszukiwaniu klas zwierząt, pobiera je wszystkie i z losowych gatunków tworzy tablicę 100 obiektów, działając całkowicie automatycznie i bez konieczności jakiejkolwiek zmiany własnego kodu.

2. Opis komponentów
A. Edytor Zwierząt (AnimalEditor.java)
Jest to prosta aplikacja okienkowa zbudowana z użyciem biblioteki Java Swing. Pełni rolę generatora kodu źródłowego i panelu zarządzania klasami.

Interfejs Użytkownika (GUI): Posiada pola tekstowe do wprowadzenia parametrów gatunku: nazwy klasy, polskiej nazwy, liczby nóg i wydawanego dźwięku.

Generowanie klas (saveAnimalFile): Program zczytuje dane z pól tekstowych i wstrzykuje je do przygotowanego szablonu znakowego, tworząc pełnoprawny kod języka Java z zachowaniem dziedziczenia (extends Animal) oraz nadpisywania metod (@Override). Kod ten jest zapisywany bezpośrednio na dysk do pliku w ścieżce po2026/lab/src/main/java/agh/po/lab4/animals/.

Analiza istniejących klas (listExistingFiles): Metoda przeszukuje folder w poszukiwaniu plików .java, a następnie czyta je linijka po linijce, wyciągając wartości w wywołaniach konstruktorów oraz w metodach poprzez manipulację ciągami znaków (substring, contains).

B. Główna Symulacja (Zoo.java)
Program tworzy tablicę 100 zwierząt i oblicza łączną liczbę ich nóg. Jego najważniejszą cechą jest pełna niezależność od twardo wpisanych nazw klas oraz ich ilości.

Dynamiczne ładowanie:

Kod korzysta z narzędzia ClassLoader do pobrania ścieżki zasobów systemowych dla skompilowanego pakietu agh.po.lab4.animals.

Skanuje wszystkie pliki binarne .class w tym katalogu.

Odfiltrowuje i ignoruje abstrakcyjną klasę bazową Animal.class.

Na podstawie nazw plików program dynamicznie buduje nazwy klas (np. agh.po.lab4.animals.Dog) i ładuje je do pamięci środowiska za pomocą metody Class.forName(), grupując je na liście animalClasses.

Dynamiczne Instancjonowanie:

W pętli głównej program losowo wybiera jedną z wczytanych z dysku klas.

Używając clazz.getConstructor(String.class).newInstance(...), tworzy nowy obiekt wybranej w locie klasy, przekazując wygenerowane unikalne imię (z licznikiem iteracji) jako argument konstruktora.

Polimorfizm: Kod iteruje po utworzonej tablicy zwierząt, wykonując operacje getDescription() oraz makeSound(). Abstrakcja zapewnia, że niezależnie od tego jaki gatunek kryje się w danym slocie tablicy, wywołany zostanie właściwy, wygenerowany dla niego kod.

C. Abstrakcja (Animal.java)
Klasa bazowa narzucająca architekturę. Wymusza na podklasach implementację metod getDescription() oraz makeSound(), dostarcza podstawowe pola (name, legs) i logikę (getter). Zapewnia to spójny interfejs (kontrakt), na którym operuje zarówno generator klas, jak i dynamiczny mechanizm losowania.

3. Instrukcja użytkowania
Uruchom jako pierwszy plik AnimalEditor.java. Dodaj za pomocą formularza interesujące Cię gatunki zwierząt.

Po zatwierdzeniu upewnij się, że nowe pliki .java pojawiły się w strukturze projektu.

Poczekaj ułamek sekundy na zakończenie auto-kompilacji wykonywanej w tle przez środowisko.

Uruchom aplikację z pliku Zoo.java. Program wykryje wszystkie nowo utworzone gatunki w pakiecie, rozlosuje je pomiędzy 100 elementów tablicy, wypisze ich głosy na standardowe wyjście, a na końcu zwróci podsumowanie sumy wszystkich nóg.