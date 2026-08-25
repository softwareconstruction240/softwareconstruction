package interfaceExample;

public class SpaceRemover implements Function<String, String> {
    @Override
    public String apply(String param) {
        return param == null ? null : param.replaceAll(" ", "");
    }
}
