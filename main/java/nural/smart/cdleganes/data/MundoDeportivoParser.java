package nural.smart.cdleganes.data;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by alvaro on 26/3/17.
 */

public class MundoDeportivoParser extends XMLParser {

    public MundoDeportivoParser() {
        super();
        super.url = "http://www.mundodeportivo.com/feed/rss/futbol/leganes";
        super.origen = "Mundo Deportivo";
    }

    protected String readImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageURL = "";
        parser.require(XmlPullParser.START_TAG, ns, enclosureParser);
        imageURL = parser.getAttributeValue(null, urlParser);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, enclosureParser);
        //Devolvemos NULL en la image, ya que, el tamaño de la imagen
        //de Mundo Deportivo es muy pequeña
        return null;
    }

}
