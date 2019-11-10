package pl.longhorn.rebETL.util;

import lombok.val;
import pl.longhorn.rebETL.model.exception.IllegalUrlException;

import java.util.regex.Pattern;

public class UrlHelper {

    private static final Pattern CATEGORY_ID_PATTERN = Pattern.compile("/([0-9]+),([0-9]+)");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("/([0-9]+)/");
    private static final int PRODUCT_GROUP_ID = 1;
    private static final int CATEGORY_MATCHER_ID = 2;

    public static String getPathToAllComments(String url) {
        String categoryId = getCategoryId(url);
        String productId = getProductId(url);
        return "https://www.rebel.pl/e4u.php/1,ModProducts/ShowComments/" + categoryId + "/" + productId;
    }

    private static String getCategoryId(String url) {
        val matcher = CATEGORY_ID_PATTERN.matcher(url);
        if (matcher.find(CATEGORY_MATCHER_ID)) {
            return matcher.group(CATEGORY_MATCHER_ID);
        } else {
            throw new IllegalUrlException();
        }
    }

    private static String getProductId(String url) {
        val matcher = PRODUCT_ID_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(PRODUCT_GROUP_ID);
        }
        throw new IllegalUrlException();
    }
}
