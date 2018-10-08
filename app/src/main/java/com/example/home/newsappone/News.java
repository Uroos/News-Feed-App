package com.example.home.newsappone;

/**
 * Created by Home on 12/19/2017.
 */

public class News {
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String authorName;

    public News(String msectionname,
                String mwebpublicationdate,
                String mwebtitle,
                String mweburl,
                String mauthorname) {
        sectionName = msectionname;
        webPublicationDate = mwebpublicationdate;
        webTitle = mwebtitle;
        webUrl = mweburl;
        authorName=mauthorname;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getAuthorName(){return authorName;}


}
