package interfaceExample;

public class Capitalizer implements Function<String, String> {
    @Override
    public String apply(String param) {
        return param == null ? null : param.toUpperCase();
    }
}
