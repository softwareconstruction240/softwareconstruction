package domain.gson;

import com.google.gson.annotations.SerializedName;

public class CD {

    @SerializedName("TITLE")
    private String title;

    @SerializedName("ARTIST")
    private String artist;

    @SerializedName("COUNTRY")
    private String country;

    @SerializedName("COMPANY")
    private String company;

    @SerializedName("PRICE")
    private float price;

    @SerializedName("YEAR")
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
