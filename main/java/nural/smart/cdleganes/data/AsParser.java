package nural.smart.cdleganes.data;

/**
 * Created by alvaro on 13/2/17.
 */

public class AsParser extends XMLParser {

    public AsParser() {
        super();
        super.url = "http://masdeporte.as.com/tag/rss/cd_leganes/a";
        super.origen = "AS";
    }
}
