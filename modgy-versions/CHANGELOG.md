0.0.3:
Реализована работа с номерами по ТЗ:
https://docs.google.com/document/d/1MQYY0PlyIIk6ahRfE8tBWzGeOj82k0rErAZxSiLepS8/edit?usp=drive_link

0.0.4:
Заменены ApiError на Error во всем коде. Исправлены спецификации Swagger для Rooms и Users
https://nkiblyk.kaiten.ru/space/167883/card/25885927

0.0.5:
Реализована работа с категориями номеров по ТЗ:
https://nkiblyk.kaiten.ru/space/167883/card/26311401

0.0.6:
Реализована работа с питомцами по ТЗ:
https://docs.google.com/document/d/1l4dELV7vZi2YMXWBppTEneTQ0VyG9SCHWPlP2seWwZQ/edit?usp=drive_link

**0.0.7: Реализована работа с пользователями приложения:**
- создание
- редактирование данных пользователя
- получение пользователя по id
- получение списка пользователей
    - всех активных
    - всех отключенных
    - всех независимо от статуса(isActive)
- изменение статуса(isActive) учётной записи

Карточка: *<https://nkiblyk.kaiten.ru/26310859>*  
Api: *<https://github.com/pymanceva/pet-hotel/blob/develop/Specification/pet-hotel-users-spec.yaml>*

**0.0.8:Реализована работа с бронированиями:**
- создание нового
- редактирование существующего
- получение бронирования по id
- удаление бронирования

Карточка тип "Бронирование": *<https://nkiblyk.kaiten.ru/documents/d/6153a5e4-32ac-4f17-adac-4bbac6d2e74d>*
Карточка тип "Закрытие": *<https://nkiblyk.kaiten.ru/documents/d/7a1c8a2d-b776-45bc-adcf-0a7091b5a9bc>*
API: *<https://github.com/pymanceva/pet-hotel/blob/add-bookings/Specification/pet-hotel-bookings-spec.yaml>*

**0.0.9: Исправлена работа RoomMapper в части маппинга дочерней сущности Category.**
Отредактирован RoomServiceImpl для работы с исправленным маппером.
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/27548948?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*

**0.0.10: Реализован поиск питомцев по кличке.**
Карточка: https://docs.google.com/document/d/1l4dELV7vZi2YMXWBppTEneTQ0VyG9SCHWPlP2seWwZQ/edit?usp=drive_link

**0.0.11: Добавлен UtilityService для получения сущностей всех типов и проверки прав доступа.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/27549007?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*

**0.0.12: Реализована работа с клиентами(хозяевами постояльцев) отеля:**
- создание
- получение списка всех клиентов
- получение по id
- редактирование данных клиента
- получение краткой информации о последних нескольких зарегистрированных клиентах
- получение краткой информации о клиенте по его id
- Проверка наличия в базе клиента с указанным номером телефона
- Поиск клиента по ФИО или номеру основного и дополнительного телефонов

Карточка: *<https://nkiblyk.kaiten.ru/26906268>*  
Api: *<https://github.com/pymanceva/pet-hotel/blob/efed8fb9bbd026f8cab858a21672a84bdb4e151f/specification/pet-hotel-owner-spec.yaml>*

**0.0.13: Добавлена проверка доступности номеров, поиск блокирующих и пересекающихся бронирований
и поиск номеров заданной категории, доступных в указанные даты.**
Карточки задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/27548967?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InJlc3BvbnNpYmxlIiwidmFsdWUiOjQ5NDU3MH1dfV19>*
*<https://nkiblyk.kaiten.ru/space/167883/card/27548993?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InJlc3BvbnNpYmxlIiwidmFsdWUiOjQ5NDU3MH1dfV19>*

**0.0.14: Добавлена проверка уникальности номера комнаты.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/32611738?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9LHsia2V5IjoibWVtYmVyIiwiY29tcGFyaXNvbiI6ImVxIiwidmFsdWUiOjQ5NDU3MH1dfV19>*

**0.0.15: Добавлена проверка уникальности наименования категории.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/34808737?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9LHsia2V5IjoibWVtYmVyIiwiY29tcGFyaXNvbiI6ImVxIiwidmFsdWUiOjQ5NDU3MH1dfV19>*

**0.0.16: Добавлено ограничение прав доступа на удаление клиента (доступно только при ROLE_BOSS).**

**0.0.17: Добавлена выдача всех неотмененных бронирований в заданные даты.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/24742955?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*

**0.0.18: Добавлена сортировка категорий по наименованию и номеров по категории и номеру.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/36132320?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*

**0.0.19: Клиенты и питомцы соединены.**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/34857595>*

**0.0.20: Согласовать ошибку от Бэка для кейса "Пользователь с таким email уже существует в системе"**
Карточка задачи: *<https://nkiblyk.kaiten.ru/32186763>*

**0.0.21: Добавлена выдача всех бронирований по конкретному питомцу или клиенту**
Карточка задачи: *<https://nkiblyk.kaiten.ru/space/167883/card/38244014?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*
*<https://nkiblyk.kaiten.ru/space/167883/card/38244001?filter=eyJrZXkiOiJhbmQiLCJ2YWx1ZSI6W3sia2V5IjoiYW5kIiwidmFsdWUiOlt7ImNvbXBhcmlzb24iOiJlcSIsImtleSI6InRhZyIsInZhbHVlIjo0OTA5MDF9XX1dfQ>*