package cz.sspbrno;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static String db_user;
    public static String db_passwd;
    public static String discord_token;
    public static String discord_activity;
    private static final String path = "config.ini";

    static {
        try {
            ArrayList<String> config = readINI();
            db_user = config.get(0);
            db_passwd = config.get(1);
            discord_token = config.get(2);
            discord_activity = config.get(3);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static List<String> readFile(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    public static ArrayList<String> readINI() throws IOException {
        ArrayList<String> config = new ArrayList<>();
        List<String> list = readFile(path);
        for (String s : list)
            if (!s.startsWith("#") && !s.startsWith("IV")) config.add(s.split("=")[1]);
        return config;
    }

    public static String readIV(String value) throws IOException {
        String searched = "";
        List<String> values = readFile(path);
        for (String s : values) {
            if (s.startsWith("IV"))
                if (s.contains(value)) searched = s.split("=")[1];
        }
        return searched;
    }

    public static void writeIV(String var, boolean value) throws IOException {
        ArrayList<String> config = new ArrayList<>(Files.readAllLines(Paths.get(path)));
        int number = value ? 1 : 0;
        for (int i = 0; i < config.size(); i++) {
            if (config.get(i).startsWith("IV")) {
                String[] lol = config.get(i).split("=");
                if (lol[0].replace("IV", "").equalsIgnoreCase(var))
                    lol[1] = String.valueOf(number);
                config.set(i, String.join("=", lol));
            }
        }
        writeToFile(config);
    }

    private static void writeToFile(ArrayList<String> list) throws IOException {
        System.out.println(Arrays.toString(list.toArray()));
        File f = new File("temp" + path);
        Files.write(Paths.get(f.getPath()), list);
        Files.delete(Paths.get(path));
        f.renameTo(new File(path));
    }
}

