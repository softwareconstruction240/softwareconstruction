package domain;

import java.util.ArrayList;
import java.util.List;

public class CDFactory {
    // This method is used by the generators to get a list of CDs to write out. It is not needed for parsing of JSon or XML.
    public static List<CD> getCDs() {
        List<CD> cds = new ArrayList<>(25);

        cds.add( new CD("Hide your heart", "Bonnie Tyler", "UK", "CBS Records", 9.90f, 1988));
        cds.add( new CD("Greatest Hits", "Dolly Parton", "USA", "RCA", 9.90f, 1982));
        cds.add( new CD("Still got the blues", "Gary Moore", "UK", "Virgin records", 10.20f, 1990));
        cds.add( new CD("Eros", "Eros Ramazzotti", "EU", "BMG", 9.90f, 1997));
        cds.add( new CD("One night only", "Bee Gees", "UK", "Polydor", 10.90f, 1998));
        cds.add( new CD("Sylvias Mother", "Dr.Hook", "UK", "CBS", 8.10f, 1973));
        cds.add( new CD("Maggie May", "Rod Stewart", "UK", "Pickwick", 8.50f, 1990));
        cds.add( new CD("Romanza", "Andrea Bocelli", "EU", "Polydor", 10.80f, 1996));
        cds.add( new CD("When a man loves a woman", "Percy Sledge", "USA", "Atlantic", 8.70f, 1987));
        cds.add( new CD("Black angel", "Savage Rose", "EU", "Mega", 10.90f, 1995));
        cds.add( new CD("1999 Grammy Nominees", "Many", "USA", "Grammy", 10.20f, 1999));
        cds.add( new CD("For the good times", "Kenny Rogers", "UK", "Mucik Master", 8.70f, 1995));
        cds.add( new CD("Big Willie style", "Will Smith", "USA", "Columbia", 9.90f, 1997));
        cds.add( new CD("Tupelo Honey", "Van Morrison", "UK", "Polydor", 8.20f, 1971));
        cds.add( new CD("Soulsville", "Jorn Hoel", "Norway", "WEA", 7.90f, 1996));
        cds.add( new CD("The very best of", "Cat Stevens", "UK", "Island", 8.90f, 1990));
        cds.add( new CD("Stop", "Sam Brown", "UK", "A and M", 8.90f, 1988));
        cds.add( new CD("Bridge of Spies", "T'Pau", "UK", "Siren", 7.90f, 1987));
        cds.add( new CD("Private Dancer", "Tina Turner", "UK", "Capitol", 8.90f, 1983));
        cds.add( new CD("Midt om natten", "Kim Larsen", "EU", "Medley", 7.80f, 1983));
        cds.add( new CD("Pavarotti Gala Concert", "Luciano Pavarotti", "UK", "DECCA", 9.90f, 1991));
        cds.add( new CD("The dock of the bay", "Otis Redding", "USA", "Atlantic", 7.90f, 1987));
        cds.add( new CD("Picture book", "Simply Red", "EU", "Elektra", 7.20f, 1985));
        cds.add( new CD("Red", "The Communards", "UK", "London", 7.80f, 1987));
        cds.add( new CD("Unchain my heart", "Joe Cocker", "USA", "EMI", 8.20f, 1987));

        return cds;
    }
}
