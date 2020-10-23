package manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongController {
    @Autowired
    private SongManager songManager;
    @GetMapping("/api/searchSong")
    public Object searchSong(
            @RequestParam(value = "songName", required = false) String songName,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage
    ){
        return songManager.searchSong(songName,currentPage);
    }

    @GetMapping("/api/linkMusic")
    public Object getLinkMusic(
            @RequestParam(value = "linkSong") String linkSong
    ){
        System.out.printf("Link " + linkSong);

        return songManager.getLinkMusic(linkSong);
    }
    @GetMapping("/api/getLyric")
    public Object getLyric(
            @RequestParam(value = "linkSong") String linkSong
    ){
        return songManager.getLyric(linkSong);
    }
    @GetMapping("/api/listSong")
    public Object getListSong(
            @RequestParam(value = "artistName") String name
    ){
        return songManager.getListSong(name);
    }
    @GetMapping("/api/listSongSame")
    public Object getListSongSame(
            @RequestParam(value = "linkSong") String link
    ){
        return songManager.getListSame(link);
    }

    @GetMapping("/")
    String hello(){
        return "My name is PhucBv";
    }
}
