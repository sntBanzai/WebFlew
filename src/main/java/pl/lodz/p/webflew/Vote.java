/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew;

/**
 *
 * @author X870
 */
public class Vote {

    public Vote(String nickName) {
        voteOwner = nickName;
    }
    
    private String voteOwner;
    private Candidates firstPlace;
    private Candidates secondPlace;
    private Candidates thirdPlace;
    private Candidates fourthPlace;
    private Candidates fifthPlace;
    private int winnerResult;
    private int runnerUpResult;

    public Candidates getFirstPlace() {
        return firstPlace;
    }

    public void setFirstPlace(Candidates firstPlace) {
        this.firstPlace = firstPlace;
    }

    public Candidates getSecondPlace() {
        return secondPlace;
    }

    public void setSecondPlace(Candidates secondPlace) {
        this.secondPlace = secondPlace;
    }

    public Candidates getThirdPlace() {
        return thirdPlace;
    }

    public void setThirdPlace(Candidates thirdPlace) {
        this.thirdPlace = thirdPlace;
    }

    public Candidates getFourthPlace() {
        return fourthPlace;
    }

    public void setFourthPlace(Candidates fourthPlace) {
        this.fourthPlace = fourthPlace;
    }

    public Candidates getFifthPlace() {
        return fifthPlace;
    }

    public void setFifthPlace(Candidates fifthPlace) {
        this.fifthPlace = fifthPlace;
    }

    public int getWinnerResult() {
        return winnerResult;
    }

    public void setWinnerResult(int winnerResult) {
        this.winnerResult = winnerResult;
    }

    public int getRunnerUpResult() {
        return runnerUpResult;
    }

    public void setRunnerUpResult(int runnerUpResult) {
        this.runnerUpResult = runnerUpResult;
    }

    public String getVoteOwner() {
        return voteOwner;
    }
    
    
}
