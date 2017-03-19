package nural.smart.cdleganes.data;

/**
 * Created by alvaro on 13/2/17.
 */

public class AsParser extends XMLParser {

    public static final String url = "http://masdeporte.as.com/tag/rss/cd_leganes/a";
    private static final String origen = "AS";

    protected String getOrigen(){
        return origen;
    }

    public String getURLMedio() { return url; }
}
