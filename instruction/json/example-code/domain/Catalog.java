package domain;

import java.util.List;

public class Catalog {

    private List<CD> catalog;

    public Catalog() {

    }

    public Catalog(List<CD> cds) {
        catalog = cds;
    }

    public List<CD> getCds() {
        return catalog;
    }
}
