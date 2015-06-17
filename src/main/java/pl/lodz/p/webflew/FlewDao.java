/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X870
 */
public class FlewDao {

    private final Properties credentials;

    public FlewDao() {
        credentials = new Properties();
        credentials.setProperty("user", "snt");
        credentials.setProperty("password", "babubab");
    }

    public static FlewDao getFlewDao() {
        return new FlewDao();
    }

    public void registerPlayer(String nick) {
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"
                + "FLEW", credentials)) {
            Statement s = con.createStatement();
            String command = "INSERT INTO FLEWPlayers VALUES ('" + nick + "', 'foobar',"
                    + "'foobar','foobar','foobar','foobar',1,1)";
            int i = s.executeUpdate(command);
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(FlewDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void castVote(Vote v) throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"
                + "FLEW", credentials); Statement s = con.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ResultSet playerFound = s.executeQuery("SELECT * FROM FLEWPlayers WHERE nickForumowy='"
                    + v.getVoteOwner() + "'");
            playerFound.next();
            playerFound.updateString(2, v.getFirstPlace().toString());
            playerFound.updateString(3, v.getSecondPlace().toString());
            playerFound.updateString(4, v.getThirdPlace().toString());
            playerFound.updateString(5, v.getFourthPlace().toString());
            playerFound.updateString(6, v.getFifthPlace().toString());
            playerFound.updateInt(7, v.getWinnerResult());
            playerFound.updateInt(8, v.getRunnerUpResult());
            playerFound.updateRow();
        } 
    }

    public Set<String[]> queryForResults() {
        Set<String[]> retVal = new TreeSet<>(new Comparator<String[]>() {

            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareToIgnoreCase(o2[0]);
            }
        });
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"
                + "FLEW", credentials); Statement s = con.createStatement()) {
            String command = "SELECT * FROM FLEWPlayers";
            ResultSet rs = s.executeQuery(command);
            while (rs.next()) {
                String[] values = new String[8];
                values[0] = rs.getString(1);
                values[1] = rs.getString(2);
                values[2] = rs.getString(3);
                values[3] = rs.getString(4);
                values[4] = rs.getString(5);
                values[5] = rs.getString(6);
                values[6] = rs.getString(7);
                values[7] = rs.getString(8);
                retVal.add(values);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FlewDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }

    public String[] querySingleResult(String nick) {
        String[] retVal = new String[8];
        System.out.println(nick);
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"
                + "FLEW", credentials); Statement s = con.createStatement()) {
            String command = "SELECT * FROM FLEWPlayers WHERE nickForumowy='" + nick + "'";
            ResultSet rs = s.executeQuery(command);
            rs.next();
            retVal[0] = rs.getString(2);
            retVal[1] = rs.getString(3);
            retVal[2] = rs.getString(4);
            retVal[3] = rs.getString(5);
            retVal[4] = rs.getString(6);
            retVal[5] = rs.getString(7);
            retVal[6] = rs.getString(8);
        } catch (SQLException ex) {
            Logger.getLogger(FlewDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }
    
    public void deleteVote(String owner) throws SQLException{
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/"
                + "FLEW", credentials)) {
            Statement s = con.createStatement();
            String command = "DELETE FROM FLEWPlayers WHERE nickForumowy='"+owner+"'";
            int i = s.executeUpdate(command);
            s.close();
        } 
    }
}
