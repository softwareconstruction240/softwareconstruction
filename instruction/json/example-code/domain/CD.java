package domain;

public class CD {

    private String title;
    private String artist;
    private String country;
    private String company;
    private float price;
    private int year;

    public CD() {

    }

    public CD(String title, String artist, String country, String company, float price, int year) {
        this.title = title;
        this.artist = artist;
        this.country = country;
        this.company = company;
        this.price = price;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCountry() {
        return country;
    }

    public String getCompany() {
        return company;
    }

    public float getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "CD{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", price=" + price +
                ", year=" + year +
                '}';
    }
}
