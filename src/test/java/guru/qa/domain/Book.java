package guru.qa.domain;

public class Book {
    private String title;
    private String author;
    private Integer year;
    private Integer count_of_pages;
    private String[] genres;

    public String isTitle() {
        return title;
    }

    public String isAuthor() {
        return author;
    }

    public Integer isYear() {
        return year;
    }

    public Integer isCountOfPages() {
        return count_of_pages;
    }

    public String[] isGenres() {
        return genres;
    }
}
