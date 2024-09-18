package ru.modgy.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExceptionMessages {
    private static final Map<String, String> privateDictionary = new HashMap<>();

    static {
        privateDictionary.put(
                "uq_user_email",
                "Ошибка добавления пользователя. Пользователь с таким email уже существует.");
        privateDictionary.put(
                "uq_category_name",
                "Ошибка добавления категории. Категория с таким именем уже существует.");
        privateDictionary.put(
                "positive_price_bookings",
                "Ошибка добавления бронирования. Цена не может быть отрицательной.");
        privateDictionary.put(
                "positive_amount_bookings",
                "Ошибка добавления бронирования. Стоимость не может быть отрицательной.");
        privateDictionary.put(
                "positive_prepayment_amount_bookings",
                "Ошибка добавления бронирования. Предоплата не может быть отрицательной.");
        privateDictionary.put(
                "check_out_later_than_in",
                "Ошибка добавления бронирования. Дата выезда не может быть раньше даты заезда.");
        privateDictionary.put(
                "UQ_OWNER_MAIN_PHONE",
                "Ошибка добавления клиента. Клиент с таким телефоном уже существует.");
    }

    public static final Map<String, String> dictionary = Collections.unmodifiableMap(privateDictionary);

    private ExceptionMessages() {
        throw new IllegalStateException("Utility class");
    }
}
