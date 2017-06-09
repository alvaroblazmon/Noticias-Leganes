package nural.smart.cdleganes.data;

/**
 * Created by alvaro on 9/6/17.
 */

public class HoraBlanquiazul extends XMLParser {

    public HoraBlanquiazul() {
        super();
        super.url = "http://somoslega.com/rss";
        super.origen = "Hora Blanquiazul";
    }
    
    protected String readImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageURL = "";
        parser.require(XmlPullParser.START_TAG, ns, enclosureParser);
        imageURL = parser.getAttributeValue(null, urlParser);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, enclosureParser);
        //Devolvemos NULL en la image, ya que, no tienen imagen
        //de Hora Blazquiazul
        return null;
    }
}
