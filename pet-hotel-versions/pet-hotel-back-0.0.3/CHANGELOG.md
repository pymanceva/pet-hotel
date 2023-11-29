Реализована работа с номерами по ТЗ: 
https://docs.google.com/document/d/1MQYY0PlyIIk6ahRfE8tBWzGeOj82k0rErAZxSiLepS8/edit?usp=drive_link

Список эндпоинтов:
-POST/addRoom - добавить новый номер
-GET/getRoomById - получить номер по id
-PATCH/updateRoom - обновить инфо о номере
-GET/getAllRooms - получить список всех комнат доступных к брони или нет(по параметру isVisible)
-PATCH/hideRoomById - скрыть номер из работы
-PATCH/unhideRoomById - вернуть номер в работу
-DELETE/deleteRoomById - полностью удалить номер из БД
