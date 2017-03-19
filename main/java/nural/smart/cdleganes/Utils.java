package nural.smart.cdleganes;

/**
 * Created by alvaro on 22/2/17.
 */

import android.text.Html;
import android.text.Spanned;

import java.util.Date;

public class Utils {

    public static String getTimeDiff(Date date){

        Date now = new Date();
        long diff = now.getTime() - date.getTime();

        long diffDay=diff/(24*60*60 * 1000);
        if(diffDay>0){
            return diffDay + "d";
        }

        diff=diff-(diffDay*24*60*60 * 1000);
        long diffHours=diff/(60*60 * 1000);
        if(diffHours>0){
            return diffHours + "h";
        }

        diff=diff-(diffHours*60*60 * 1000);
        long diffMinutes = diff / (60 * 1000);
        if(diffMinutes>0){
            return diffMinutes + "m";
        }

        diff=diff-(diffMinutes*60*1000);
        long diffSeconds = diff / 1000;
        if(diffSeconds>0){
            return diffSeconds + "s";
        }

        return "";

    }

    public static Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}
