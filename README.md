# 🚗 Harmonogram Szkoły Jazdy (Scheduler)

Cześć! To jest prosty system do rezerwacji lekcji jazdy. Poniżej znajdziesz instrukcję "krok po kroku", jak to wszystko uruchomić, nawet jeśli nie czujesz się ekspertem od Javy.

---

## 🛠 Co musisz mieć zainstalowane?

Zanim zaczniesz, upewnij się, że masz na komputerze:
1. **Java 21** – upewnij się że JDK jest ustawione jak trzeba.
2. **Docker Desktop** – dzięki niemu jednym kliknięciem postawisz bazę danych.
3. **Dowolne IDE** – np. IntelliJ IDEA (najlepiej) lub VS Code.

---

## 🚀 Krok 1: Uruchomienie bazy danych

Projekt korzysta z bazy danych PostgreSQL. Nie musisz jej instalować ręcznie.
1. Otwórz terminal (lub wiersz poleceń) w folderze projektu.
2. Wpisz komendę:
   ```bash
   docker-compose up -d
   ```
3. To wszystko! Twoja baza danych już działa w tle.

---

## 🔐 Krok 2: Konfiguracja Clerk (Autoryzacja)

Clerk to system, który pozwala użytkownikom logować się do aplikacji (przez e-mail, Google itp.).

1. Wejdź na [clerk.com](https://clerk.com/) i załóż darmowe konto.
2. Stwórz nową aplikację (np. "School Scheduler").
3. W panelu Clerk przejdź do sekcji **Configure -> API Keys**.
4. Przejdź do **Configure -> Authentication -> Social Connections** i upewnij się, że "Google" lub "Email" są włączone.
5. **Kluczowe:** Musisz skonfigurować Clerk jako dostawcę OAuth2.
   - W Clerk wejdź w **Configure -> OAuth Applications**.
   - Dodaj nową aplikację lub znajdź dane dla "Standard OAuth".
   - Będziesz potrzebować: `Client ID`, `Client Secret` oraz `Issuer URL`.

6. Otwórz plik `src/main/resources/application.properties` i uzupełnij te pola:
   ```properties
   # Tu wpisz swój URL z Clerka (zazwyczaj kończy się na .clerk.accounts.dev)
   spring.security.oauth2.resourceserver.jwt.issuer-uri=https://TWOJA-APKA.clerk.accounts.dev
   spring.security.oauth2.client.provider.clerk.issuer-uri=https://TWOJA-APKA.clerk.accounts.dev

   # Tu wpisz swoje klucze z panelu Clerk
   spring.security.oauth2.client.registration.clerk.client-id=TWÓJ_CLIENT_ID
   spring.security.oauth2.client.registration.clerk.client-secret=TWÓJ_CLIENT_SECRET
   ```

7. W panelu Clerk, w ustawieniach aplikacji, musisz dodać **Allowed Callback URLs**:
   - `http://localhost:8080/login/oauth2/code/clerk`

---

## 👑 Jak zostać Administratorem?

W pliku `src/main/java/com/sokol/scheduler/service/UserService.java` znajduje się prosty mechanizm nadawania uprawnień:

```java
Role targetRole = "schedulerpb@gmail.com".equals(email) ? Role.ADMIN : Role.STUDENT;
```

Jeśli chcesz mieć dostęp do panelu admina (`/admin`), zmień powyższy adres e-mail na ten, którego użyjesz do logowania przez Clerk. Dzięki temu po pierwszym zalogowaniu Twoje konto automatycznie dostanie uprawnienia administratora.

---

## 🏃 Krok 3: Uruchomienie aplikacji

Kiedy baza działa i Clerk jest skonfigurowany, możesz odpalić projekt.

1. W terminalu wpisz:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(Jeśli jesteś na Windows i to nie działa, spróbuj `.\mvnw.cmd spring-boot:run`)*

2. Poczekaj, aż zobaczysz napis: `Started SchedulerApplication in ... seconds`.

3. Otwórz przeglądarkę i wejdź na:
   [http://localhost:8080](http://localhost:8080)

---

## 📝 Ważne informacje

*   **Baza danych:** Dane są zapisywane w Dockerze. Jeśli chcesz wyczyścić bazę, wpisz `docker-compose down -v`.
*   **Role:** Pierwsza osoba, która się zaloguje, dostanie domyślną rolę. Możesz zmienić role użytkowników bezpośrednio w bazie danych w tabeli `app_user`, aby nadać komuś uprawnienia `ADMIN`.
*   **Limity:** Student może zarezerwować max 1 godzinę dziennie i łącznie 30 godzin w całym kursie.

Powodzenia! W razie problemów sprawdź, czy Twoje klucze w `application.properties` nie mają zbędnych spacji na końcu.
