package nural.smart.cdleganes.data;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

import nural.smart.cdleganes.Utils;

/**
 * Created by alvaro on 13/2/17.
 */

public class MarcaParser extends XMLParser {

    protected static final String descriptionParserOwn = "media:description";
    protected static final String enclosureParserOwn = "media:content";


    public MarcaParser() {
        super();
        super.url = "http://estaticos.marca.com/rss/futbol/leganes.xml";
        super.origen = "Marca";
    }

    protected Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, itemParser);
        String title = null;
        String description = null;
        String link = null;
        Date date = null;
        String imageURL = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(titleParser)) {
                title = readTitle(parser);
            } else if (name.equals(descriptionParserOwn)) {
                description = readDescription(parser);
            } else if (name.equals(linkParser)) {
                link = readLink(parser);
            } else if (name.equals(pubDateParser)) {
                date = readDate(parser);
            } else if (name.equals(enclosureParserOwn)) {
                imageURL = readImageURL(parser);
            } else {
                skip(parser);
            }
        }

        return new Entry(title, link, date, imageURL, description, origen);
    }

    protected String readImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageURL = "";
        parser.require(XmlPullParser.START_TAG, ns, enclosureParserOwn);
        imageURL = parser.getAttributeValue(null, urlParser);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, enclosureParserOwn);
        return imageURL;
    }

    protected String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, descriptionParserOwn);
        String title = readText(parser);
        title = title.replace("<strong>", "");
        title = title.replace("</strong>", "");
        title = title.replace("<dt>", "");
        title = title.replace("</dt>", "");
        title = title.replace("<dl class=\"interview\">", "");
        title = Utils.stripHtml(title).toString();
        parser.require(XmlPullParser.END_TAG, ns, descriptionParserOwn);
        return title;
    }

}
