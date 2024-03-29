package cz.sspbrno.sql;

import cz.sspbrno.Config;
import cz.sspbrno.html.ParseLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInit {
    private Connection con;
    private Statement st;

    private Connection dbCon;
    private Statement dbSt;

    private static final String url = "jdbc:mysql://localhost/mysql?user=root";
    private static final String dbUrl = String.format("jdbc:mysql://localhost/%s?user=%s&password=%s", Config.db_user, Config.db_user, Config.db_passwd);

    public SQLInit() throws SQLException, IOException {

        this.con = DriverManager.getConnection(url);
        this.st = con.createStatement();
        String[] query = {
                String.format("CREATE USER '%s' IDENTIFIED BY '%s';", Config.db_user, Config.db_passwd),
                String.format("CREATE DATABASE %s;", Config.db_user),
                String.format("GRANT ALL PRIVILEGES ON *.* TO '%s';", Config.db_user)};
        for (String s : query) st.executeUpdate(s);
        con.close();
        this.dbCon = DriverManager.getConnection(dbUrl);
        this.dbSt = dbCon.createStatement();
        String[] dbQuery = {
                "CREATE TABLE demon ( Name varchar(45), Creator varchar(45), idDemon int(11) PRIMARY KEY);",
                "CREATE TABLE completionist ( idCompletionist int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, Player varchar(69), Device varchar(45), Position char(20), Percentage varchar(6) );",
                //"CREATE TABLE members ( idMembers int(11) PRIMARY KEY, Player varchar(69), Id varchar(45) );"
        };
        for (String s : dbQuery) dbSt.executeUpdate(s);
        dbCon.close();
        SQLConnect insert = new SQLConnect();
        insert.insert(new ParseLoader().splitElements());
        Config.writeIV("listcreated", true);
        insert.close();
    }
}
