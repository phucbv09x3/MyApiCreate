package manager;

import model.GetLinkMusic;
import model.ItemSong;
import model.Lyric;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SongManager {
    public Object searchSong(String songName, int currentPage) {
        if (songName == null || songName.trim().equals("")) {
            return getListFirstSong();
        }
        List<ItemSong> onlines = new ArrayList<>();
        try {
            Document c = Jsoup.connect("https://chiasenhac.vn/tim-kiem?q="
                    + songName.replace(" ", "+") +
                    "&page_music=" + currentPage + "&filter=all").get();
            Elements els = c.select("div.tab-content").first().select("ul.list-unstyled");
            int list = 0;
            for (int i = 0; i <= els.size() - 1; i++) {
                Element e = els.get(i);
                Elements childEls = e.select("li.media");

                for (Element child : childEls) {
                    try {
                        String linkSong =
                                child.select("a").first().attr("href");
                        String linkImg =
                                child.select("a").first().select("img").attr("src");
                        String title = child.select("a").first().attr("title");
                        String singer = child.select("div.author").text();
                        onlines.add(new ItemSong(list, linkImg, title, singer, linkSong,""));
                        list++;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return onlines;
    }

    private Object getListFirstSong() {
        List<ItemSong> onlines = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://chiasenhac.vn/bang-xep-hang/tuan.html").get();
            Elements els = doc.select("div.tab-content").select("ul.list-unstyled");
            Elements childEls = els.select("li.media");
            int i = 0;
            for (Element child : childEls) {
                String linkSong = "https://chiasenhac.vn" + child.select("a").first().attr("href");
                String linkImage = child.select("a").first().select("img").attr("src");
                String nameSong = child.select("a").first().attr("title");
                String nameSinger = child.select("div.author").text();
                String linkMusic="";
                onlines.add(new ItemSong(i, linkImage, nameSong, nameSinger, linkSong,linkMusic));
                i++;
            }
        } catch (IOException e) {

        }

        return onlines;
    }

    public Object getLinkMusic(String linkSong) {
        try {
            Document c = Jsoup.connect(linkSong).get();
            Elements els =
                    c.select("div.container").select("a.download_item");
            if (els.size() >= 2) {
                return
                        new GetLinkMusic(els.get(1).attr("href"));
            } else {
                return
                        new GetLinkMusic(els.get(0).attr("href"));
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getListSong(String name) {
        List<ItemSong> listSong = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://chiasenhac.vn/tim-kiem?q=" + name + "&filter=ca-si").get();
            String url = "https://chiasenhac.vn" + doc.getElementById("nav-artist").select("ul.list-unstyled").select("a").first().attr("href");
            if (url.trim().equals("")) {
                return "Không tìm thấy ca sĩ";
            } else {
                Document docs = Jsoup.connect(url).get();
                Elements els = docs.getElementById("music").select("ul.list-unstyled").select("li.media");
                int i = 0;
                for (Element child : els) {
                    String linkSong = child.select("a").first().attr("href");
                    String linkImage = child.select("a").first().select("img").attr("src");
                    String nameSong = child.select("a").first().attr("title");

                    listSong.add(new ItemSong(i, linkImage, nameSong, name, linkSong,""));
                    i++;
                }
            }
        } catch (IOException e) {
        }
        return listSong;
    }

    public Object getLyric(String linkSong) {
        try {
            Document doc = Jsoup.connect(linkSong).get();
            Element els = doc.getElementById("fulllyric");
            String text = els.toString();
            text = text.replaceAll("<br>", "");
            text = text.replace("<div id=\"fulllyric\">", "");
            text = text.replace("</div>", "");
            if (text.trim().equals("")) {
                return null;
            } else {
                return new Lyric(text);
            }
        } catch (IOException e) {
        }
        return null;
    }

    public Object getListSame(String link) {
        List<ItemSong> listSong = new ArrayList<>();
        try {
            Document docs = Jsoup.connect(link).get();
            Elements element = docs.getElementsByClass("col-md-3").select("ul.list-unstyled").select("li.media");
            int i = 0;
            for (Element els : element) {
                String linkSong = els.select("a").first().attr("href");
                String linkImage = els.select("a").first().select("img").attr("src");
                String nameSong = els.select("a").first().attr("title");
                String nameSinger = els.select("a").get(2).text();
                listSong.add(new ItemSong(i, linkImage, nameSong, nameSinger, linkSong,""));
                i++;
            }
        } catch (IOException e) {
        }
        return listSong;
    }

}
