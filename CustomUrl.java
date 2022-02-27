public class CustomUrl {
    public int getDeepness() {
        return deepness;
    }

    public String getUrl() {
        return url;
    }

    public CustomUrl(int deepness, String url) {
        this.deepness = deepness;
        this.url = url;
    }

    private int deepness;
    private String url;


}
