# Autor

**Wiktor Łącki**  
lackiwiktor@gmail.com  
https://github.com/lackiwiktor

---

## Środowisko wykonawcze

Projekt został zrealizowany w języku **Java 21** z wykorzystaniem systemu budowania **Gradle**.

---

## Zastosowane zależności

- **Lombok** – ułatwia tworzenie kodu poprzez automatyzację generowania konstruktorów, getterów, setterów itp.
- **Gson** – biblioteka do serializacji i deserializacji danych w formacie JSON do obiektów Java.
- **JUnit 5** – framework do tworzenia i uruchamiania testów jednostkowych.
- **Mockito** – narzędzie do tworzenia atrap w testach jednostkowych.
- **AssertJ** – biblioteka do weryfikacji wyników w testach.

---

## Opis działania algorytmu

Algorytm odpowiedzialny za wybór najlepszej metody płatności działa w następujący sposób:

1. Dla każdego zamówienia generowane są maksymalnie **trzy możliwe oferty** (strategie płatności).
2. Spośród nich wybierana jest **najlepsza oferta** czyli taka, która:
    - oferuje **najniższą cenę końcową**,
    - a w przypadku remisu – wykorzystuje **największą możliwą liczbę punktów lojalnościowych**.

Takie podejście oferuję wysoką wydajność i poprawne wyniki.

---

## Typy ofert

### 1. `FULL_POINTS`

Oferta, w której całość zamówienia opłacana jest punktami. Jeśli użytkownik ma wystarczające saldo, naliczany jest rabat
i tworzona jest oferta.

### 2. `PARTIAL_POINTS`

Częściowa płatność punktami (10% wartości zamówienia). Pozostała część kwoty opłacana jest inną metodą z wystarczającym
limitem.

### 3. `FULL_CARD`

Pełna płatność kartą. Jeśli klient posiada aktywną promocję, wybierana jest ta z największym rabatem. W przeciwnym razie
wybierana jest pierwsza dostępna metoda płatności z odpowiednim limitem.

---

## Jakość kodu

Najważniejsze komponenty projektu zostały:

- **odpowiednio udokumentowane**
- **pokryte testami jednostkowymi**
