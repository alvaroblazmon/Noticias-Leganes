package nural.smart.cdleganes.data;

/**
 * Created by alvaro on 4/2/17.
 */

public class LeganesParser extends XMLParser {

    public static final String url = "http://www.deportivoleganes.com/rss";
    public static final String origen = "Oficial Leganés";

    protected String getOrigen(){
        return origen;
    }

    public String getURLMedio(){ return url; }

}
