import config.Config;

import java.io.*;
import java.sql.*;

public class Parse {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/dchatzakou/Documents/IdeaProjects/parseXLSfile/data/actions-data.tsv"), "UTF-8"));
        String line;

        //connect to db
        String JDBC_DRIVER = "org.postgresql.Driver";
        Class.forName("org.postgresql.Driver");

        Connection con = null;
        PreparedStatement stmt = null;
        Statement statement;

        try {
            con = DriverManager.getConnection(Config.dbURL, Config.uName, Config.uPass);
            con.setAutoCommit(false);
            statement = con.createStatement();

            //truncate old data
            statement.executeUpdate("TRUNCATE " + Config.tbl);
            con.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        int cnt = 1;

        while ((line = br.readLine()) != null){
            if(cnt != 1){
                System.out.println(line);
                String phenomenon = line.split("\t")[1];
                String category = line.split("\t")[2];
                String description = line.split("\t")[3];
                String phase = line.split("\t")[4];

                String body = line.split("\t")[5];
                if(body == "-"){ body = ""; }

                String implementing_body = line.split("\t")[6];
                if(implementing_body == "-"){ implementing_body = ""; }

                String participating_body = line.split("\t")[7];
                if(participating_body == "-"){ participating_body = ""; }

                String agency = "ALFRESCO_ADMINISTRATORS";

                System.out.println(phenomenon + "\t" + category + "\t" + description + "\t" + phase + "\t" + body + "\t" + implementing_body + "\t" + participating_body + "\t" + agency);

                try {
                    String sql = "INSERT INTO _actions(phenomenon,category,description,phase,body,implementing_body,participating_body,agency) VALUES (?,?,?,?,?,?,?,?)";

                    stmt = con.prepareStatement(sql);

                    stmt.setString(1, phenomenon);
                    stmt.setString(2, category);
                    stmt.setString(3, description);
                    stmt.setString(4, phase);
                    stmt.setString(5, body);
                    stmt.setString(6, implementing_body);
                    stmt.setString(7, participating_body);
                    stmt.setString(8, agency);

                    stmt.executeUpdate();
                    con.commit();

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

            }

            cnt++;
        }

        System.out.println(cnt);

        br.close();

        try {
            stmt.close();
            con.close();

            System.out.println("\n--- data inserted ---");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
