<div style="text-align: center; padding: 100px 0;">
    <h1 style="font-size: 3em;">Specyfikacja Wymagań Użytkownika (URS)</h1>
    <h2 style="font-size: 2em; color: #555;">System Harmonogramu Szkoły Jazdy</h2>
    <br><br><br>
    <p style="font-size: 1.2em;">Projekt: <strong>Scheduler</strong></p>
    <p style="font-size: 1.2em;">Data: 3 lutego 2026 r.</p>
    <p style="font-size: 1.2em;">Status: Dokumentacja Techniczna</p>
</div>

<div style="page-break-after: always;"></div>

## 1. Wstęp

### 1.1 Cel projektu
Głównym celem niniejszego projektu jest zaprojektowanie oraz wdrożenie kompleksowego, webowego systemu do zarządzania procesem planowania lekcji nauki jazdy, który zastąpi dotychczasowe, manualne metody rezerwacji bardziej wydajnym i przejrzystym rozwiązaniem cyfrowym. System ma za zadanie umożliwić kursantom samodzielne zarządzanie swoimi terminami w ramach przyznanych limitów godzinowych, jednocześnie dostarczając administratorom (instruktorom) zaawansowane narzędzia do nadzorowania dostępności terminów oraz monitorowania postępów poszczególnych użytkowników.

### 1.2 Zakres systemu
Niniejsza platforma obejmuje swoim zakresem moduł bezpiecznego uwierzytelniania użytkowników, interaktywny kalendarz wizualizujący dostępność slotów czasowych w czasie rzeczywistym, silnik walidacji reguł biznesowych zapobiegający nadużyciom oraz panel administracyjny do zarządzania bazą kursantów i ich uprawnieniami.

---

## 2. Role Użytkowników

| Rola | Szczegółowy Opis |
| :--- | :--- |
| **Kursant (Student)** | Osoba będąca w procesie szkolenia, posiadająca uprawnienia do przeglądania dostępnych terminów, dokonywania rezerwacji lekcji zgodnie z limitami oraz monitorowania własnej historii spotkań i pozostałego salda godzin. |
| **Administrator (Instruktor)** | Zarządca systemu odpowiedzialny za definiowanie ram czasowych dostępności, ręczne blokowanie terminów (np. z powodów serwisowych lub urlopów) oraz nadzorowanie i modyfikowanie salda godzin wszystkich kursantów. |

---

## 3. Reguły Biznesowe (BR)

*   **BR-01 (Stały Czas Trwania):** Każda pojedyncza lekcja jazdy jest jednostką niepodzielną i trwa dokładnie 60 minut, co wymusza sztywną strukturę siatki godzinowej w kalendarzu.
*   **BR-02 (Limit Dzienny):** W celu zapewnienia optymalnej efektywności nauki oraz równego dostępu do zasobów dla wszystkich uczestników, kursant może zarezerwować maksymalnie jedną godzinę zajęć w ciągu jednego dnia kalendarzowego.
*   **BR-03 (Limit Całkowity):** Nowo zarejestrowany kursant otrzymuje domyślny pakiet 30 godzin szkoleniowych, a system rygorystycznie blokuje możliwość rezerwacji kolejnych terminów w momencie, gdy saldo dostępnych godzin spadnie do zera, chyba że administrator podejmie decyzję o jego zwiększeniu.
*   **BR-04 (Wyłączność Slota):** Dany termin w kalendarzu może posiadać tylko jeden z trzech stanów: `DOSTĘPNY`, `ZAREZERWOWANY` lub `ZABLOKOWANY`, przy czym status rezerwacji przez jednego kursanta automatycznie czyni termin niewidocznym dla innych w kontekście możliwości zapisu.
*   **BR-05 (Prywatność Danych):** System chroni tożsamość użytkowników, co oznacza, że kursant widzi jedynie informację o niedostępności danego terminu (kolor szary), nie mając wglądu w to, kto konkretnie dokonał rezerwacji w danym czasie.

<div style="page-break-after: always;"></div>

## 4. Wymagania Funkcjonalne

### 4.1 Autoryzacja i Profile Użytkowników
*   **FR-01:** System musi zapewniać bezpieczny proces logowania z wykorzystaniem zewnętrznego dostawcy tożsamości (Clerk), gwarantując poufność danych dostępowych.
*   **FR-02:** Przy pierwszym zalogowaniu system automatycznie tworzy profil użytkownika w bazie danych, przypisując mu domyślną rolę kursanta oraz inicjalne saldo 30 godzin.
*   **FR-03:** Interfejs użytkownika musi w sposób ciągły wyświetlać imię, nazwisko oraz aktualną liczbę pozostałych do wykorzystania godzin w widocznym miejscu na ekranie.

### 4.2 Zarządzanie Kalendarzem i Rezerwacjami
*   **FR-04:** Aplikacja prezentuje harmonogram w formie przejrzystej siatki kalendarza, podzielonej na dni tygodnia oraz konkretne przedziały godzinowe.
*   **FR-05:** Wizualizacja statusów slotów musi być intuicyjna i oparta na kodzie kolorystycznym:
    *   **Zielony:** Termin dostępny do natychmiastowej rezerwacji.
    *   **Niebieski:** Termin zarezerwowany przez aktualnie zalogowanego kursanta.
    *   **Szary:** Termin niedostępny (zajęty przez inną osobę lub zablokowany przez instruktora).
*   **FR-06:** Kursant posiada możliwość odwołania własnej rezerwacji z odpowiednim wyprzedzeniem, co skutkuje automatycznym zwrotem godziny na jego saldo i przywróceniem dostępności terminu dla innych osób.

### 4.3 Narzędzia Administracyjne
*   **FR-07:** Administrator ma prawo do jednostronnego blokowania dowolnego wolnego terminu w kalendarzu, co jest niezbędne do wyłączenia godzin z użytku bez przypisywania ich do konkretnego ucznia.
*   **FR-08:** Panel administracyjny musi zawierać listę wszystkich zarejestrowanych kursantów wraz z funkcjonalnością edycji ich salda godzinowego oraz podglądu szczegółowej historii ich rezerwacji.

<div style="page-break-after: always;"></div>

## 5. Scenariusze Użytkowania (Notacja Gherkin)

<div style="page-break-inside: avoid; margin-bottom: 30px;">

### 5.1 Rezerwacja lekcji przez kursanta
```gherkin
Feature: Rezerwacja terminu przez kursanta
  As a zalogowany kursant
  I want to dokonać rezerwacji wolnej godziny w kalendarzu
  So that mogę realizować swój program szkolenia kierowców

  Scenario: Pomyślna rezerwacja dostępnego terminu
    Given jestem zalogowany jako kursant "Jan Kowalski"
    And posiadam aktualne saldo "15" pozostałych godzin
    And nie posiadam żadnej innej rezerwacji w dniu "2026-03-10"
    When wybiorę dostępny termin "10:00" w dniu "2026-03-10" i potwierdzę chęć zapisu
    Then system powinien zarejestrować moją rezerwację w bazie danych
    And moje saldo godzin powinno zostać pomniejszone do "14"
    And wybrany slot w kalendarzu powinien zmienić kolor na niebieski
```
</div>

<div style="page-break-inside: avoid; margin-bottom: 30px;">

### 5.2 Próba naruszenia limitu dziennego
```gherkin
Feature: Walidacja limitu jednej lekcji dziennie
  As a kursant systemu
  I want to spróbować zarezerwować drugą godzinę w tym samym dniu
  So that system zablokuje tę akcję zgodnie z regulaminem szkoły

  Scenario: Odmowa rezerwacji drugiego terminu tego samego dnia
    Given posiadam już potwierdzoną rezerwację na godzinę "08:00" w dniu "2026-03-12"
    When spróbuję kliknąć i zarezerwować kolejny wolny termin o godzinie "15:00" w tym samym dniu
    Then system powinien wyświetlić komunikat o błędzie: "Przekroczono dzienny limit rezerwacji (max 1h/dzień)"
    And moja próba zapisu powinna zostać odrzucona
    And moje saldo godzin nie powinno ulec zmianie
```
</div>

<div style="page-break-after: always;"></div>

<div style="page-break-inside: avoid; margin-bottom: 30px;">

### 5.3 Uwierzytelnianie i inicjalizacja profilu (Clerk)
```gherkin
Feature: Autoryzacja użytkownika via Clerk
  As a nowy użytkownik systemu
  I want to zalogować się za pomocą zewnętrznego dostawcy tożsamości
  So that uzyskam dostęp do funkcji rezerwacji

  Scenario: Pierwsze logowanie nowego kursanta
    Given nie posiadam jeszcze konta w lokalnej bazie danych systemu
    When pomyślnie uwierzytelnię się w usłudze Clerk
    Then system powinien automatycznie utworzyć mój profil w lokalnej tabeli "app_user"
    And przypisać mi domyślną rolę "STUDENT"
    And ustawić moje początkowe saldo na "30" godzin
    And przekierować mnie do głównego widoku harmonogramu lekcji
```
</div>

<div style="page-break-inside: avoid; margin-bottom: 30px;">

### 5.4 Panel Administracyjny - Pulpit Zarządzania Kursantami (Dashboard)
```gherkin
Feature: Administracyjne zarządzanie listą kursantów
  As a administrator systemu
  I want to mieć pełny wgląd w listę uczniów oraz możliwość edycji ich parametrów
  So that mogę sprawnie zarządzać procesem szkolenia i rozliczeniami

  Scenario: Przeglądanie listy i aktualizacja salda godzin kursanta
    Given jestem zalogowany jako administrator
    When wejdę do panelu "Dashboard" (Pulpit)
    Then powinienem zobaczyć tabelę z listą wszystkich zarejestrowanych kursantów, ich adresami e-mail oraz aktualnym saldem godzin
    When wybiorę kursanta "Jan Kowalski" i zmienię jego saldo z "10" na "15" godzin
    Then system powinien natychmiast zaktualizować te dane w bazie
    And wyświetlić potwierdzenie: "Saldo kursanta zostało pomyślnie zaktualizowane"
```
</div>

<div style="page-break-after: always;"></div>

<div style="page-break-inside: avoid; margin-bottom: 30px;">

### 5.5 Panel Administracyjny - Pełne Zarządzanie Grafikiem (Schedule)
```gherkin
Feature: Administracyjny nadzór nad harmonogramem
  As a administrator systemu
  I want to widzieć szczegółowy grafik wszystkich lekcji oraz mieć możliwość zarządzania dostępnością
  So that mogę koordynować pracę szkoły i blokować terminy w razie potrzeby

  Scenario: Podgląd rezerwacji i blokowanie terminów
    Given jestem zalogowany jako administrator
    When otworzę widok "Schedule" (Grafik) w panelu administratora
    Then powinienem widzieć kalendarz, w którym sloty zarezerwowane przez kursantów wyświetlają ich imiona i nazwiska
    When zauważę wolny termin o godzinie "14:00" w przyszły wtorek i wybiorę opcję "Zablokuj"
    Then status tego terminu powinien zmienić się na "ZABLOKOWANY"
    And żaden kursant nie będzie mógł dokonać rezerwacji w tym czasie, widząc ten slot jako niedostępny (szary)
```
</div>

<div style="page-break-after: always;"></div>

## 6. Model Danych (Struktura Techniczna)

### 6.1 Użytkownik (`app_user`)
Model przechowujący kluczowe informacje o profilu osoby korzystającej z systemu:
*   `id` (Klucz główny, Long)
*   `clerkId` (Unikalny identyfikator z systemu Clerk, String)
*   `email` (Adres poczty elektronicznej, String)
*   `firstName`, `lastName` (Dane osobowe, String)
*   `role` (Rola systemowa: `STUDENT` lub `ADMIN`)
*   `remainingHours` (Aktualny stan licznika godzin, Integer, domyślnie 30)

### 6.2 Lekcja (`lesson`)
Obiekt reprezentujący pojedyncze zdarzenie w harmonogramie:
*   `id` (Klucz główny, Long)
*   `startTime` (Dokładna data i godzina rozpoczęcia, LocalDateTime)
*   `student` (Relacja do tabeli użytkowników, opcjonalna w przypadku blokad)
*   `status` (Stan terminu: `AVAILABLE`, `BOOKED`, `BLOCKED`)

---

## 7. Wymagania pozafunkcjonalne (NFR)

*   **NFR-01 (Wydajność):** Każda operacja zapisu lub odczytu stanu kalendarza nie może trwać dłużej niż 500ms, aby uniknąć frustracji użytkownika i zminimalizować ryzyko konfliktów rezerwacji (race conditions).
*   **NFR-02 (Responsywność):** Interfejs użytkownika musi być w pełni responsywny (RWD), zapewniając komfortową obsługę na urządzeniach mobilnych, co jest kluczowe dla kursantów dokonujących rezerwacji "w trasie".
*   **NFR-03 (Bezpieczeństwo):** Cała komunikacja pomiędzy przeglądarką a serwerem musi być szyfrowana przy użyciu protokołu TLS, a dostęp do punktów końcowych API administratora musi być rygorystycznie sprawdzany pod kątem posiadanych uprawnień.