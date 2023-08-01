package domain.gson.complex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalog {

    @SerializedName("CATALOG")
    private List<CDWrapper> catalog;

    public Catalog() {

    }

    public Catalog(List<CD> cds) {
        catalog = new ArrayList<>();

        for(CD cd : cds) {
            catalog.add(new CDWrapper(cd));
        }
    }

    public List<CD> getCds() {
        List<CD> cds = new ArrayList<>();

        for(CDWrapper wrapper : catalog) {
            cds.add(wrapper.getCd());
        }

        // A one line alternative to the above that does the same thing using functional programming.
        // List<CD> cds = catalog.stream().map(x -> x.getCd()).collect(Collectors.toList());

        return cds;
    }

    private static class CDWrapper {
        @SerializedName("CD")
        private CD cd;

        public CDWrapper(CD cd) {
            this.cd = cd;
        }

        public CD getCd() {
            return cd;
        }

        @Override
        public String toString() {
            return cd.toString();
        }
    }
}
