package com.bebo.manga.Common;

import com.bebo.manga.model.Chapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.DownloadItem;
import com.bebo.manga.model.Mix;
import com.bebo.manga.model.ProfileSetting;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static List<Comic> comicList = new ArrayList<>();
    public static List<Mix> mixList = new ArrayList<>();
    public static List<Comic> comicOfflineList = new ArrayList<>();
    public static List<Chapter> chapterList;
    public static List<DownloadItem> downloadItems = new ArrayList<>();
    public static List<String> FAVORITE = new ArrayList<>();
    public static List<String> TREND = new ArrayList<>();
    public static List<String> SEE = new ArrayList<>();
    public static List<String> bannerList = new ArrayList<>();
    public static Comic comicSelected;
    public static Chapter chapterSelected;
    public static ProfileSetting profileSetting;
    public static int chapterIndex=-1;
    public static boolean online = true;
    public static boolean pause = false;


    public static String[] categories = {
            "Action",
            "Adult",
            "Adventure",
            "Comedy",
            "Completed",
            "Cooking",
            "Doujinshi",
            "Drama",
            "Drop",
            "Ecchi",
            "Fantasy",
            "Gender bender",
            "Harem",
            "Historical",
            "Horror",
            "Jose",
            "Latest",
            "Manhua",
            "Manhwa",
            "Material arts",
            "Mature",
            "Mecha",
            "Medical",
            "Mystery",
            "Newest",
            "One shot",
            "Ongoing",
            "Psychological",
            "Romance",
            "School life",
            "Sci fi",
            "Seinen",
            "Shoujo",
            "Shoujo a",
            "Shounen",
            "Shounen ai",
            "Slice of life",
            "Smut",
            "Sports",
            "Superhero",
            "Supernatural",
            "Top Read",
            "Tragedy",
            "Webtoons",
            "Yaoi",
            "Yuri"
    };

    public static String formatString(String name) {
        StringBuilder finalResult = new StringBuilder(name.length() > 15?name.substring(0, 15)+"...":name);
        return finalResult.toString();
    }
}
