package pl.longhorn.rebETL.util;

import org.jsoup.nodes.Element;
import pl.longhorn.rebETL.model.comment.HtmlComment;

public class HtmlCommentsFactory {
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
        return one.getElementById("comment-voting").getElementsByClass("amount").text();
    }

    private static String getId(Element one) {
        return one.getElementById("comment-voting").attr("comment-id");
    }

    private static String getText(Element one) {
        return one.getElementsByClass("comment-body").text();
    }

    private static String getProductRating(Element one) {
        return one.getElementsByClass("header").first().getElementsByTag("div").get(1).className();
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
}
