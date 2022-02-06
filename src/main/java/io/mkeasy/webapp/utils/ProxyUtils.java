package io.mkeasy.webapp.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ProxyUtils {
    public static String getHtml(String url)throws Exception {
        Document doc = Jsoup.connect(url).get();
        for(Element e : doc.select("a"))
            e.attr("href", e.absUrl("href"));
        for(Element e : doc.select("img"))
            e.attr("src", e.absUrl("src"));
        return doc.html();
    }
    public static String getText(String url, String field)throws Exception {
        Document doc = Jsoup.connect(url).get();
        return doc.select(field).text();
    }
    public static void main1(String[] args) throws Exception {
        String url = "http://www.kpx.or.kr/www/contents.do?key=216";
        String res = getHtml(url);
        System.out.println(res);
    }
    public static void main(String[] args) throws Exception {
        String url = "http://www.kpx.or.kr/www/contents.do?key=216";
        String res = getText(url, "div.elec_box_01");
        System.out.println(res);
        res = getText(url,"div.max_elec");
        System.out.println(res);
        res = getText(url,"div.reserve_elec");
        System.out.println(res);
    }
}
