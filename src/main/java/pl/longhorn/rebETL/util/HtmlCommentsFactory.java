package pl.longhorn.rebETL.util;

import lombok.val;
import org.jsoup.nodes.Element;
import pl.longhorn.rebETL.model.comment.HtmlComment;

public class HtmlCommentsFactory {
    private static final int NUMBER_PRODUCT_RATING_ELEMENT_INDEX = 1;
    private static final int PRIME = 31;

    public static HtmlComment from(Element one) {
        return HtmlComment.builder()
                .id(getId(one))
                .nick(getNick(one))
                .date(getDate(one))
                .text(getText(one))
                .commentRating(getCommentRating(one))
                .productRating(getProductRating(one))
                .build();
    }

    private static String getCommentRating(Element one) {
        val votingElement = one.getElementById("comment-voting");
        return votingElement == null ? "" : votingElement.getElementsByClass("amount").text();
    }

    private static String getId(Element one) {
        val elementWithId = one.getElementById("comment-voting");
        if (elementWithId == null) {
            return createId(one.toString());
        }
        return elementWithId.attr("comment-id");
    }

    private static String getText(Element one) {
        return one.getElementsByClass("comment-body").text();
    }

    private static String getProductRating(Element one) {
        val elements = one.getElementsByClass("header").first().getElementsByTag("div");
        if (elements.size() == NUMBER_PRODUCT_RATING_ELEMENT_INDEX + 1) {
            return elements.get(1).className();
        }
        return null;
    }

    private static String getDate(Element one) {
        return one.getElementsByClass("header").first().getElementsByTag("span").first().text();
    }

    private static String getNick(Element one) {
        return one.getElementsByClass("header")
                .first()
                .getElementsByTag("b")
                .first()
                .text();
    }

    private static String createId(String string) {
        return string.hashCode() * PRIME + "";
    }
}
