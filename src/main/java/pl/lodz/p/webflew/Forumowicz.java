/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew;

import java.sql.SQLException;

/**
 *
 * @author X870
 */
public class Forumowicz {

    private final String nick;
    private Vote vote;
    private static FlewDao dao = new FlewDao();

    public Forumowicz(String nick) {
        this.nick = nick;
        dao.registerPlayer(nick);
    }

    public void vote(Candidates[] candids, int[] firstTwoResults) throws SQLException {
        if (vote == null) {
            vote = new Vote(this.getNick());
        }
        vote.setFirstPlace(candids[0]);
        vote.setSecondPlace(candids[1]);
        vote.setThirdPlace(candids[2]);
        vote.setFourthPlace(candids[3]);
        vote.setFifthPlace(candids[4]);
        vote.setWinnerResult(firstTwoResults[0]);
        vote.setRunnerUpResult(firstTwoResults[1]);
        dao.castVote(vote);
    }

    public String getNick() {
        return nick;
    }

}
