- autentykacja i autoryzacja - keycloak
https://medium.com/devops-dudes/securing-spring-boot-rest-apis-with-keycloak-1d760b2004e
curl -X POST 'http://localhost:8090/auth/realms/todo-realm/protocol/openid-connect/token' --header 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 'grant_type=password' --data-urlencode 'client_id=todo-application' --data-urlencode 'client_secret=e464889f-71d6-4f85-95d0-5c6d07ab4deb' --data-urlencode 'username=administrator' --data-urlencode 'password=admin'

- path variable to custom object https://www.baeldung.com/spring-mvc-custom-data-binder

- testy dla encji tag
- sprawdzić prometheusa po zmianie portu w management w springu
- rozdzial testow - dodanie tagow - testy integracyjne
- spring tools plugin
- add integration (spring) tests for removing/adding tags

- add web test client tets https://rieckpil.de/spring-webtestclient-for-efficient-testing-of-your-rest-api/
Also, make sure to have at least some happy-path tests that use HTTP and not a mocked servlet environment. Therefore you can make use of the WebTestClient for example.

- example for tests with security https://rieckpil.de/guide-to-testing-spring-boot-applications-with-mockmvc/

- persist data in database/docker add volume (database keycloak, database todo, prometheus)

- open api produkowanie dokumentacji

- one database for all tests? https://www.baeldung.com/spring-dynamicpropertysource (point 5)

- testing spring configuration: https://www.baeldung.com/spring-conditionalonproperty

- tags management

- wait for - poczekać aż baza danych na prawdę wstanie

- validation errors translations

- add grafana to prometheus scrapping
- add dashboards to grafana, prometehus, spring boot app
- add configuration file for grafana for datasource
- nauczyć się składni prometheusa
- nauczyć się składni grafany

- hateos

- find all tasks with query params

- todo due to

- uzytkownicy
- grupy
- przynależnośc taska do grupy, przynależnośc taska do użytkownika, różny stopień przynależności - owner, read, edit, delete

- support markdown in description/title

- code coverage - sonarqube - docker?
- budowanie całej aplikacji w dockerze w maven

- change log level for running spring boot application
- add custom metrics, number of finished tasks

- jager/open tracing
- isitio

- server.servlet.context.path=/resources

- api versions
- xml json response
- caching browser headers/spring/hibernate

- statistics for hibernate using actuator

- property based testing
- pit tests

- add integration tests with statistics for whole flow

- hibernate envers

- hikari cp configuration

- error prone + nullaway
https://github.com/uber/NullAway/wiki/Configuration#other-build-systems
http://errorprone.info/docs/installation
https://errorprone.info/bugpattern/SelfEquals

- tagi/category jako baza grafowa?

- date time provider dla dat w encji np w encji task

javax.persistence.query.timeout
