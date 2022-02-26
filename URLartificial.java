public class URLartificial {
    public int getDeepness() {
        return deepness;
    }

    public void setDeepness(int deepness) {
        this.deepness = deepness;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public URLartificial(int deepness, String url) {
        this.deepness = deepness;
        this.url = url;
    }

    private int deepness = 1;
    private String url;


}
