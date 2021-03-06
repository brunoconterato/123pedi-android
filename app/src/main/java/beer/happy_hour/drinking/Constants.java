package beer.happy_hour.drinking;

/**
 * Created by brcon on 11/03/2017.
 */

public class Constants {
    //Hash para comelçar busca por categorias nos botões
    public static final String SEARCH_CATEGORY_HASH = "knibo76ui4senrn1dfu2dsfn234sufhya2347fsf";

    public static final String BASE_STOCK_URL = "http://api.happy-hour.beer/api/search/stocksearch";
    public static final String BASE_ORDER_URL = "http://api.happy-hour.beer/api/unregistered/orders";

    public static final String BASE_INFORMATION_SEARCH_URL = "http://api.happy-hour.beer/api/information/search_term";
    public static final int MIN_SEARCH_LENGHT_TO_API = 3;

    public static final String BASE_INFORMATION_MESSAGE_URL = "http://api.happy-hour.beer/api/information/user_message";

    public static final String BASE_INFORMATION_CART_ITEM_URL = "http://api.happy-hour.beer/api/information/cart_item";

    public static final String QUERY_KEY_STRING = "query_key";

    public static final String TAG_TASK_FRAGMENT = "load_stock_task_fragment";
    public static final String TAG_DOWNLOAD_IMAGE_TASK_FRAGMENT = "download_image_task_fragment";

    public static final int MAX_ITEMS_QUANTITY = 99;
}