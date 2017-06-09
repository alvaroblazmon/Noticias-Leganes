package nural.smart.cdleganes.data;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by alvaro on 26/3/17.
 */

public class XMLNetwork {

    private static final XMLParser[] medios = {new AsParser(),
            new LeganesParser(),
            new MarcaParser(),
            new MundoDeportivoParser(),
            new HoraBlanquiazul()};

    /* milliseconds */
    private static final int READTIMEOUT = 10000;
    private static final int CONNECTTIMEOUT = 15000;

    public static List<XMLParser.Entry> loadXmlFromNetwork() throws IOException {

        List<XMLParser.Entry> entries = new ArrayList();
        InputStream stream = null;


        for(XMLParser medio : medios){
            try {
                stream = downloadUrl(medio.getURLMedio());
                entries.addAll(medio.parse(stream));
            } catch (IOException ioException){
                //Log.e("XMLNetwork", ioException.getMessage());
            } catch (XmlPullParserException parserException) {
                //Log.e("XMLNetwork", parserException.getMessage());
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        }

        return entries;
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private static InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READTIMEOUT);
        conn.setConnectTimeout(CONNECTTIMEOUT);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    public static List<XMLParser.Entry> orderByDate(List<XMLParser.Entry> entries){

        Collections.sort(entries, new Comparator<XMLParser.Entry>() {
            @Override
            public int compare(XMLParser.Entry entry, XMLParser.Entry t1) {
                return t1.date.compareTo(entry.date);
            }
        });

        return entries;
    }

}
