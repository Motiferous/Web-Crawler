//This class is used for one url. It contains an url address in String and the deepness of it.
public class CustomUrl {

    private final int deepness;//The deepness of this link.
    private final String url;//The url of this link.

    //Constructor.
    public CustomUrl(int deepness, String url) {
        this.deepness = deepness;
        this.url = url;
    }

    //Returns deepness of the link.
    public int getDeepness() {
        return deepness;
    }

    //Returns URL address.
    public String getUrl() {
        return url;
    }

}
