package stepdefs;

import io.cucumber.java.ParameterType;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import models.SearchResultsInfo;
import models.Snippet;
import org.junit.jupiter.api.Assertions;
import pages.YandexMainPage;
import pages.YandexMarketCategoryPage;
import pages.YandexMarketMainPage;
import stash.Context;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static stepdefs.SupportingSteps.*;

/**
 * Класс, содержащий реализацию шагов теста
 * @author Кирилл Желтышев
 */
public class MyStepDef extends BaseSteps {
    /**
     * Метод, реализующий шаг в котором происходит переход с ya.ru на главную страницу Яндекс Маркета
     * @author Кирилл Желтышев
     */
    @Дано("пользователь заходит на главную страницу Яндекс Маркет")
    public void пользовательЗаходитНаГлавнуюСтраницуЯндексМаркет() {
        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.openScrollbarServices();
        yandexMainPage.openMarket();
    }

    /**
     * Метод, реализующий шаг в котором происходит переход главной страницы Яндекс Маркета на страницу категории товаров
     * @author Кирилл Желтышев
     */
    @Когда("пользователь переходит на категорию {string} и подкатегорию {string}")
    public void пользовательПереходитНаКатегориюИПодкатегорию(String category, String subcategory) {
        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.openCatalog();
        yandexMarketMainPage.hoverToCategory(category);
        yandexMarketMainPage.openSubcategory(subcategory);

        YandexMarketCategoryPage yandexMarketCategoryPage = new YandexMarketCategoryPage(driver);
        testContext.put(Context.YANDEX_MARKET_CATEGORY_PAGE.name(), yandexMarketCategoryPage);
    }

    /**
     * Метод, реализующий шаг в котором происходит установка фильтра товаров по цене
     * @author Кирилл Желтышев
     */
    @Когда("пользователь фильтрует товары по цене от {int} до {int}")
    public void пользовательФильтруетТоварыПоЦене(Integer minPriceForFilter, Integer maxPriceForFilter) {
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.getPriceFilter().setMinPrice(minPriceForFilter);
        testContext.put(Context.MIN_PRICE.name(), minPriceForFilter);
        yandexMarketCategoryPage.getPriceFilter().setMaxPrice(maxPriceForFilter);
        testContext.put(Context.MAX_PRICE.name(), maxPriceForFilter);
    }

    /**
     * Метод, реализующий шаг в котором происходит установка фильтра товаров по производителям
     * @author Кирилл Желтышев
     */
    @Когда("пользователь фильтрует товары по производителям: {listOfStrings}")
    public void пользовательФильтруетТоварыПоПроизводителям(List<String> manufacturersForFilter) {
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.getManufacturerFilter().clickShowAllButton();
        yandexMarketCategoryPage.getManufacturerFilter().chooseManufacturers(manufacturersForFilter);
        testContext.put(Context.MANUFACTURERS.name(), manufacturersForFilter);

        SearchResultsInfo info = getSearchResultsInfo(proxy);
        testContext.put(Context.INFO.name(), info);
    }

    /**
     * Метод, реализующий шаг в котором происходит сохранение всех карточек товаров в переменную с текущей страницы
     * @author Кирилл Желтышев
     */
    @Когда("пользователь просматривает первую страницу")
    public void пользовательПросматриваетПервуюСтраницу() {
        getSnippetsFromCurrentPageAndSaveToStash();
    }

    /**
     * Метод, реализующий шаг в котором происходит проверка количества представленных товаров на странице
     * @author Кирилл Желтышев
     */
    @То("на странице отображается больше {int} элементов")
    public void наСтраницеОтображаетсяБольшеЭлементов(Integer snippetsNumber) {
        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Assertions.assertTrue(snippets.size() > snippetsNumber, "На странице отображается меньше " + snippetsNumber + "элементов.");
    }

    /**
     * Метод, реализующий шаг в котором происходит проверка товаров на соответствие фильтрам
     * @author Кирилл Желтышев
     */
    @То("на странице все элементы удовлетворяют условиям фильтра")
    public void наСтраницеВсеЭлементыУдовлетворяютУсловиямФильтра() {
        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(
                        snippets,
                        (List<String>) testContext.get(Context.MANUFACTURERS.name()),
                        (int) testContext.get(Context.MIN_PRICE.name()),
                        (int) testContext.get(Context.MAX_PRICE.name())),
                "Не все предложения на странице соответствуют фильтру.");
    }

    /**
     * Метод, реализующий шаг в котором происходит переход на случайную страницу и сохранение всех карточек товаров в переменную
     * @author Кирилл Желтышев
     */
    @Когда("пользователь просматривает случайную страницу")
    public void пользовательПросматриваетСлучайнуюСтраницу() {
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        int randomPage = yandexMarketCategoryPage.goToRandomPage(
                (SearchResultsInfo) testContext.get(Context.INFO.name()));
        getSnippetsFromCurrentPageAndSaveToStash();
    }

    /**
     * Метод, реализующий шаг в котором происходит переход на последнюю страницу и сохранение всех карточек товаров в переменную
     * @author Кирилл Желтышев
     */
    @Когда("пользователь просматривает последнюю страницу")
    public void пользовательПросматриваетПоследнююСтраницу() {
        System.out.println("пользователь_просматривает_последнюю_страницу");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        int randomPage = yandexMarketCategoryPage.goToLastPage(
                (SearchResultsInfo) testContext.get(Context.INFO.name()));
        getSnippetsFromCurrentPageAndSaveToStash();
    }

    /**
     * Метод, реализующий шаг в котором происходит переход на первую страницу и сохранение всех карточек товаров в переменную
     * @author Кирилл Желтышев
     */
    @Когда("пользователь возвращается на первую страницу")
    public void пользовательВозвращаетсяНаПервуюСтраницу() {
        System.out.println("пользователь возвращается на первую страницу");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.goToFirstPage();
        getSnippetsFromCurrentPageAndSaveToStash();
    }

    /**
     * Метод, реализующий шаг в котором происходит поиск товара через поиск по названию
     * @author Кирилл Желтышев
     */
    @Когда("ищет через поиск первый товар")
    public void ищетЧерезПоискПервоеНаименованиеТовара() {
        System.out.println("ищет через поиск первое наименование товара");

        Snippet firstSnippet = ((List<Snippet>) testContext.get(Context.SNIPPETS.name())).stream().findFirst().get();
        testContext.put(Context.FIRST_SNIPPET.name(), firstSnippet);
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.search(firstSnippet.name);
    }

    /**
     * Метод, реализующий шаг в котором происходит проверка присутствия искомого товара в выдаче
     * @author Кирилл Желтышев
     */
    @То("в результатх поиска есть искомый товар")
    public void вРезультатхПоискаЕстьИскомыйТовар() {
        System.out.println("в_результатх_поиска_есть_искомый_товар");

        getSnippetsFromCurrentPageAndSaveToStash();
        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Snippet firstSnippet = (Snippet) testContext.get(Context.FIRST_SNIPPET.name());
        Assertions.assertTrue(
                isProductPresentOnPage(
                        snippets,
                        firstSnippet),
                "Товар \"" + firstSnippet.name + "\" отсутствует в результатах поиска.");
    }

    /**
     * Метод парсинга строки в список строк
     * @author Кирилл Желтышев
     */
    @ParameterType("(.*)")
    public List<String> listOfStrings(String strings) {
        return Arrays.stream(strings.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
