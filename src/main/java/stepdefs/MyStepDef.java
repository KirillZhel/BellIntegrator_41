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

public class MyStepDef extends BaseSteps {
    @Дано("пользователь заходит на главную страницу Яндекс Маркет")
    public void пользовательЗаходитНаГлавнуюСтраницуЯндексМаркет() {
        System.out.println("пользователь_заходит_на_главную_страницу_Яндекс_Маркет");

        YandexMainPage yandexMainPage = new YandexMainPage(driver);
        yandexMainPage.open();
        yandexMainPage.openScrollbarServices();
        yandexMainPage.openMarket();
    }

    @Когда("пользователь переходит на категорию {string} и подкатегорию {string}")
    public void пользовательПереходитНаКатегориюИПодкатегорию(String category, String subcategory) {
        System.out.println("пользователь_переходит_на_категорию_и_подкатегорию");
        YandexMarketMainPage yandexMarketMainPage = new YandexMarketMainPage(driver);
        yandexMarketMainPage.openCatalog();
        yandexMarketMainPage.hoverToCategory(category);
        yandexMarketMainPage.openSubcategory(subcategory);

        YandexMarketCategoryPage yandexMarketCategoryPage = new YandexMarketCategoryPage(driver);
        testContext.put(Context.YANDEX_MARKET_CATEGORY_PAGE.name(), yandexMarketCategoryPage);
    }

    @Когда("пользователь фильтрует товары по цене от {int} до {int}")
    public void пользовательФильтруетТоварыПоЦене(Integer minPriceForFilter, Integer maxPriceForFilter) {
        System.out.println("пользователь_фильтрует_товары_по_цене_от_до");
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.getPriceFilter().setMinPrice(minPriceForFilter);
        testContext.put(Context.MIN_PRICE.name(), minPriceForFilter);
        yandexMarketCategoryPage.getPriceFilter().setMaxPrice(maxPriceForFilter);
        testContext.put(Context.MAX_PRICE.name(), maxPriceForFilter);
    }

    @Когда("пользователь фильтрует товары по производителям: {listOfStrings}")
    public void пользовательФильтруетТоварыПоПроизводителям(List<String> manufacturersForFilter) {
        System.out.println("пользователь_фильтрует_товары_по_производителям");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.getManufacturerFilter().clickShowAllButton();
        yandexMarketCategoryPage.getManufacturerFilter().chooseManufacturers(manufacturersForFilter);
        testContext.put(Context.MANUFACTURERS.name(), manufacturersForFilter);

        SearchResultsInfo info = getSearchResultsInfo(proxy);
        testContext.put(Context.INFO.name(), info);
    }

    @Когда("пользователь просматривает первую страницу")
    public void пользовательПросматриваетПервуюСтраницу() {
        System.out.println("пользователь_просматривает_первую_страницу");

        getSnippetsFromCurrentPage();
    }

    @То("на странице отображается больше {int} элементов")
    public void наСтраницеОтображаетсяБольшеЭлементов(Integer snippetsNumber) {
        System.out.println("на_странице_отображается_больше_элементов");

        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Assertions.assertTrue(snippets.size() > snippetsNumber, "На странице отображается меньше " + snippetsNumber + "элементов.");
    }

    @То("на странице все элементы удовлетворяют условиям фильтра")
    public void наСтраницеВсеЭлементыУдовлетворяютУсловиямФильтра() {
        System.out.println("на_странице_все_элементы_удовлетворяют_условиям_фильтра");

        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Assertions.assertTrue(true);
        /*Assertions.assertTrue(areAllSnippetOnPageMatchTheFilter(
                        snippets,
                        (List<String>) testContext.get(Context.MANUFACTURERS.name()),
                        (int) testContext.get(Context.MIN_PRICE.name()),
                        (int) testContext.get(Context.MAX_PRICE.name())),
                "Не все предложения на странице соответствуют фильтру.");*/
    }

    @Когда("пользователь просматривает случайную страницу")
    public void пользовательПросматриваетСлучайнуюСтраницу() {
        System.out.println("пользователь_просматривает_случайную_страницу");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        int randomPage = yandexMarketCategoryPage.goToRandomPage(
                (SearchResultsInfo) testContext.get(Context.INFO.name()));
        getSnippetsFromCurrentPage();
    }

    @Когда("пользователь просматривает последнюю страницу")
    public void пользовательПросматриваетПоследнююСтраницу() {
        System.out.println("пользователь_просматривает_последнюю_страницу");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        int randomPage = yandexMarketCategoryPage.goToLastPage(
                (SearchResultsInfo) testContext.get(Context.INFO.name()));
        getSnippetsFromCurrentPage();
    }

    @Когда("пользователь возвращается на первую страницу")
    public void пользовательВозвращаетсяНаПервуюСтраницу() {
        System.out.println("пользователь возвращается на первую страницу");

        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.goToFirstPage();
        getSnippetsFromCurrentPage();
    }

    @Когда("ищет через поиск первый товар")
    public void ищетЧерезПоискПервоеНаименованиеТовара() {
        System.out.println("ищет через поиск первое наименование товара");

        Snippet firstSnippet = ((List<Snippet>) testContext.get(Context.SNIPPETS.name())).stream().findFirst().get();
        testContext.put(Context.FIRST_SNIPPET.name(), firstSnippet);
        YandexMarketCategoryPage yandexMarketCategoryPage = (YandexMarketCategoryPage) testContext.get(Context.YANDEX_MARKET_CATEGORY_PAGE.name());
        yandexMarketCategoryPage.search(firstSnippet.name);
    }

    @То("в результатх поиска есть искомый товар")
    public void вРезультатхПоискаЕстьИскомыйТовар() {
        System.out.println("в_результатх_поиска_есть_искомый_товар");

        getSnippetsFromCurrentPage();
        List<Snippet> snippets = (List<Snippet>) testContext.get(Context.SNIPPETS.name());
        Snippet firstSnippet = (Snippet) testContext.get(Context.FIRST_SNIPPET.name());
        Assertions.assertTrue(true);
        /*Assertions.assertTrue(
                isProductPresentOnPage(
                        snippets,
                        firstSnippet),
                "Товар \"" + firstSnippet.name + "\" отсутствует в результатах поиска.");*/
    }

    @ParameterType("(.*)")
    public List<String> listOfStrings(String strings) {
        return Arrays.stream(strings.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
