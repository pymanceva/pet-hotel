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
