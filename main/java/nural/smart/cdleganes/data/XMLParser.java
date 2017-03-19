package nural.smart.cdleganes.data;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alvaro on 13/2/17.
 */

public class XMLParser {

    protected static final String ns = null;

    protected static final String rootParser = "rss";
    protected static final String subRootParser = "channel";
    protected static final String itemParser = "item";

    protected static final String titleParser = "title";
    protected static final String descriptionParser = "description";
    protected static final String linkParser = "link";
    protected static final String pubDateParser = "pubDate";
    protected static final String enclosureParser = "enclosure";

    protected static final String urlParser = "url";

    public static class Entry {
        public final String title;
        public final String link;
        public final Date date;
        public final String imageURL;
        public final String description;
        public final String origen;

        protected Entry(String title, String link, Date date, String imageURL, String description, String origen) {
            this.title = title;
            this.link = link;
            this.date = date;
            this.imageURL = imageURL;
            this.description = description;
            this.origen = origen;
        }
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    protected List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, rootParser);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            entries = readChannel(parser);
        }
        return entries;
    }

    protected List readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, subRootParser);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the entry tag
            if (name.equals(itemParser)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;

    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    protected Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, itemParser);
        String title = null;
        String description = null;
        String link = null;
        Date date = null;
        String imageURL = null;
        String origen = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(titleParser)) {
                title = readTitle(parser);
            } else if (name.equals(descriptionParser)) {
                description = readDescription(parser);
            } else if (name.equals(linkParser)) {
                link = readLink(parser);
            } else if (name.equals(pubDateParser)) {
                date = readDate(parser);
            } else if (name.equals(enclosureParser)) {
                imageURL = readImageURL(parser);
            } else {
                skip(parser);
            }
        }
        origen = getOrigen();
        return new Entry(title, link, date, imageURL, description, origen);
    }

    protected String getOrigen(){

        return "";
    }

    // Processes title tags in the feed.
    protected String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, titleParser);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, titleParser);
        return title;
    }

    // Processes title tags in the feed.
    protected String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, descriptionParser);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, descriptionParser);
        return title;
    }

    // Processes link tags in the feed.
    protected String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, linkParser);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, linkParser);
        return link;
    }

    // Processes summary tags in the feed.
    protected Date readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, pubDateParser);
        String date = readText(parser);

        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date startDate = new Date();
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        parser.require(XmlPullParser.END_TAG, ns, pubDateParser);
        return startDate;
    }

    // Processes link tags in the feed.
    protected String readImageURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String imageURL = "";
        parser.require(XmlPullParser.START_TAG, ns, enclosureParser);
        //String tag = parser.getName();
        //String relType = parser.getAttributeValue(null, "rel");
        //if (tag.equals("link")) {
        //if (relType.equals("alternate")){
        imageURL = parser.getAttributeValue(null, urlParser);
        parser.nextTag();
        //}
        //}
        parser.require(XmlPullParser.END_TAG, ns, enclosureParser);
        return imageURL;
    }

    // For the tags title and summary, extracts their text values.
    protected String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public String getURLMedio(){ return ""; }
}
