package stash;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс управляет хранилищем данных, которые передаются между шагами теста
 */
public class TestContext {
    /**
     * Коллекция, куда кладутся данные, формата ключ-объект
     */
    private final Map<String, Object> stash;

    /**
     * Инстанс класс
     */
    private static TestContext INSTANCE;

    private TestContext() {
        stash = new HashMap<>();
    }

    /**
     * Метод, возвращающий единственный созданный объект класса
     */
    public static TestContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestContext();
        }

        return INSTANCE;
    }

    /**
     * Метод кладет объект в хранилище
     * @param key - ключ
     * @param value - объект
     */
    public void put(String key, Object value) {
        stash.put(key, value);
    }

    /**
     * Метод, возвращающий объект из хранилища по ключу
     * @param key ключ
     * @return объект
     */
    public Object get(String key) {
        return stash.get(key);
    }

    /**
     * Метод проверяет наличие объекта по ключу
     * @param key ключ
     * @return булево значение
     */
    public Boolean contains(String key) {
        return stash.containsKey(key);
    }
}