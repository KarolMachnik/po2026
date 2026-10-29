### CodingBat_TestJUnit.java
Klasa zawierająca zestaw automatycznych testów jednostkowych napisanych we frameworku JUnit 4:
* Przed każdym testem metoda oznaczona adnotacją `@Before` (`setUp()`) tworzy świeżą instancję klasy `CodingBat`.
* Każda z czterech metod (`or35`, `close10`, `fizzBuzz`, `startWord`) posiada własną metodę testową oznaczoną adnotacją `@Test`.
* Wewnątrz testów weryfikowane są przypadki brzegowe i przykładowe wejścia/wyjścia z CodingBat przy użyciu asercji:
  * `assertTrue(...)` oraz `assertFalse(...)` dla wartości logicznych,
  * `assertEquals(...)` dla liczb całkowitych i łańcuchów znaków (`String`),
  * `assertArrayEquals(...)` dla tablic (`String[]`).