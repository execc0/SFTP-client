# SFTP Domain Client
[SFTP-client](https://github.com/execc0/SFTP-client) 


Консольный клиент для работы с SFTP-сервером: получение, добавление и удаление пар "домен — IP" из JSON-файла.

## Требования

- Java 8 (JDK)
- Docker + Docker Compose (для тестового SFTP-сервера)

## 1. Поднять тестовый SFTP-сервер

В корне репозитория:

```bash
docker compose up -d
```

Поднимется контейнер `atmoz/sftp` на `localhost:2222`:

| Параметр | Значение |
|---|---|
| host | `localhost` |
| port | `2222` |
| login | `sftpuser` |
| password | `sftppass` |
| путь к файлу | `upload/addresses.json` |

Файл `sftp-data/addresses/addresses.json` уже лежит в репозитории и монтируется в контейнер автоматически.

На Linux перед первым запуском может понадобиться выставить права на файл, чтобы SFTP-пользователь внутри контейнера мог не только читать, но и перезаписывать файл (нужно для операций добавления/удаления пар):
```bash
chmod 666 sftp_data/addresses/addresses.json
```
Без этого запись в файл падать с ошибкой Permission denied.

Остановить сервер:

```bash
docker compose down
```

## 2. Собрать клиент

```bash
./gradlew :client:clean :client:build
```

Исполняемый jar появится в:

```
client/build/libs/client.jar
```

## 3. Запустить клиент

```bash
java -jar client/build/libs/client.jar <host> <port> <login> <password>
```

Пример:

```bash
java -jar client/build/libs/client.jar 127.0.0.1 2222 sftpuser password
```

После успешного подключения появится меню с доступными операциями:

```
1 - Get a list of domain-ip pairs
2 - Find a domain by ip
3 - Find an ip by domain
4 - Add a domain-ip pair
5 - Delete a domain-ip pair by ip
6 - Delete a domain-ip pair by domain
7 - Save changes
0 - Print this message
Any other key to exit the application
```
Обратите внимание, что для сохранения изменений необходимо выполнить отдельную операцию! 
## 4. Собрать и запустить тесты

Тесты лежат в отдельном модуле `client_tests` и зависят от `client`.

### Через Gradle

```bash
./gradlew :client_tests:test
```

Отчёт после прогона:
```
client_tests/build/reports/tests/test/index.html
```

### Через исполняемый jar (без Gradle)

```bash
./gradlew :client_tests:clean :client_tests:jar
java -jar client_tests/build/libs/client_tests.jar
```

Тесты объединены в TestNG test-suite: `client_tests/src/test/resources/testng.xml`.

## Сборка всего проекта одной командой

```bash
./gradlew clean build
```

Соберёт клиент и прогонит все тесты.

## Что покрыто тестами и почему

| Класс | Что проверяет |
|---|---|
| `DomainEntryServiceTest` | Бизнес-логика: добавление/удаление/поиск пар по домену и IP, уникальность домена и IP, сортировка списка по домену. Позитивные сценарии (валидные данные) и негативные (дубликаты, невалидный IP, поиск несуществующей записи) |
| `IPValidatorTest` | Корректность IPv4: валидные адреса, граничные значения (`0.0.0.0`, `255.255.255.255`), выход за диапазон (`256.x.x.x`), некорректный формат, ведущие нули (`01.1.1.001`) |
| `MinimalJsonParserTest` | Парсинг JSON-файла заданной структуры в список записей |
| `MinimalJsonWriterTest` | Сериализация списка записей обратно в JSON |
| `MinimalJsonRoundTripTest` | Запись - повторное чтение JSON не теряет и не искажает данные (round-trip проверка согласованности parser/writer) |


## Технические ограничения (сознательные упрощения для тестового окружения)

- Путь к файлу жестко зашит в Main классе, т.к в задании не было указано, что программа должна принимать на вход путь к файлу с адресами. 
- Парсер/writer JSON — минимальные, рассчитаны на структуру файла из задания, без полной валидации произвольного JSON-синтаксиса.
- Валидация IPv4 проверяет структурную корректность (диапазон октетов 0–255, без ведущих нулей); специальные/зарезервированные диапазоны (loopback, multicast, broadcast) отдельно не фильтруются, так как это не требуется условиями задания.
