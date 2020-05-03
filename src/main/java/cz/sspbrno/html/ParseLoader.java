package cz.sspbrno.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class ParseLoader {
    private Document dList;
    private Elements dPlacement;
    private String input;
    private Connect con;

    public ParseLoader() throws IOException {
        this.con = new Connect();
        this.input = con.getFile();
        this.dList = Jsoup.parse(input, "UTF-8");
        this.dPlacement = dList.getElementsByClass("hover white");
    }

    public ArrayList<String[]> splitElements() {
        ArrayList<String[]> appended = new ArrayList<>();
        for (int i = 0; i < dPlacement.size(); i++) {
            String[] arr = new String[3];
            String[] mid = dPlacement.get(i).child(0).html()
                    .replace("<i>", "")
                    .replace("</i>", "")
                    .split(" - ");
            String[] a;
            try { a = mid[1].split("<br>"); }
            catch (ArrayIndexOutOfBoundsException e) { a = mid[0].split("<br>"); }
            arr[0] = a[0];
            try { arr[1] = a[1]; }
            catch (Exception e) { arr[1] = "NONE"; }
            arr[2] = String.valueOf(appended.size() + 1);
            appended.add(arr);
        }
        return appended;
    }
}
