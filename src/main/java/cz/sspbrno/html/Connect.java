package cz.sspbrno.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Connect {
    private String file;

    public Connect() throws IOException {
        URL myURL = new URL("https://www.pointercrate.com/demonlist/");
        HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
        connection.setRequestProperty("Accept", "*/*");
        connection.connect();

        InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder file = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) file.append(line);
        connection.disconnect();
        br.close();
        this.file = file.toString();
    }

    public String getFile() {
        return file;
    }
}
