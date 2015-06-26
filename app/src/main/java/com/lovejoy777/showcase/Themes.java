package com.lovejoy777.showcase;

/**
 * Created by lovejoy777 on 23/06/15.
 */
public class Themes {
    private String title;
    private String description;
    private String author;
    private String link;
    private String googleplus;
    private String promo;
    private String screenshot_1;
    private String screenshot_2;
    private String screenshot_3;

    private String version;
    private String icon;

        public Themes() {
            // TODO Auto-generated constructor stub
        }

        public Themes(String title, String description, String author, String link, String googleplus, String promo, String screenshot_1, String screenshot_2, String screenshot_3, String version, String icon) {
            super();
            this.title = title;
            this.description = description;
            this.author = author;
            this.link = link;
            this.googleplus = googleplus;
            this.promo = promo;
            this.screenshot_1 = screenshot_1;
            this.screenshot_2 = screenshot_2;
            this.screenshot_3 = screenshot_3;

            this.version = version;
            this.icon = icon;
        }


        public String gettitle() {
            return title;
        }

        public void settitle(String title) {
            this.title = title;
        }

        public String getdescription() {
            return description;
        }

        public void setdescription(String description) {
            this.description = description;
        }

        public String getauthor() {
            return author;
        }

        public void setauthor(String author) {
            this.author = author;
        }

        public String getlink() {
            return link;
        }

        public void setlink(String link) {
            this.link = link;
        }

    public String getgoogleplus() {
        return googleplus;
    }

    public void setgoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }



    public String getversion() {
        return version;
    }

    public void setversion(String version) {
        this.version = version;
    }

    public String geticon() {
        return icon;
    }

    public void seticon(String icon) {
        this.icon = icon;
    }

    public String getpromo() {
        return promo;
    }

    public void setpromo(String promo) {
        this.promo = promo;
    }

        public String getscreenshot_1() {
            return screenshot_1;
        }

        public void setscreenshot_1(String screenshot_1) {
            this.screenshot_1 = screenshot_1;
        }

    public String getscreenshot_2() {
        return screenshot_2;
    }

    public void setscreenshot_2(String screenshot_2) {
        this.screenshot_2 = screenshot_2;
    }

    public String getscreenshot_3() {
        return screenshot_3;
    }

    public void setscreenshot_3(String screenshot_3) {
        this.screenshot_3 = screenshot_3;
    }

}
