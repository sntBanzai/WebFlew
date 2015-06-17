/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.webflew.Candidates;
import pl.lodz.p.webflew.FlewDao;

/**
 *
 * @author X870
 */
@SessionScoped
@Named
public class EstablishResultsBean implements Serializable {

    @ConversationScoped
    @annotations.Result
    static class Result implements Serializable {

        private Set<Candidates> remainingCandidates = new HashSet<>(Arrays.asList(Candidates.values()));
        private String firstPlace;
        private String secondPlace;
        private String thirdPlace;
        private String fourthPlace;
        private String fifthPlace;
        private int winnerResult;
        private int runnerUpResult;
        private String renderTable = "false";

        public Set<Candidates> getRemainingCandidates() {
            return remainingCandidates;
        }

        public void setRemainingCandidates(Set<Candidates> remainingCandidates) {
            this.remainingCandidates = remainingCandidates;
        }

        public void updateRemaining(AjaxBehaviorEvent abe) {
            setRemainingCandidates(new HashSet<>(Arrays.asList(Candidates.values())));
            Iterator<Candidates> ic = getRemainingCandidates().iterator();
            while (ic.hasNext()) {
                Candidates c = ic.next();
                if (c.toString().equals(firstPlace)) {
                    ic.remove();
                }
                if (c.toString().equals(secondPlace)) {
                    ic.remove();
                }
                if (c.toString().equals(thirdPlace)) {
                    ic.remove();
                }
                if (c.toString().equals(fourthPlace)) {
                    ic.remove();
                }
                if (c.toString().equals(fifthPlace)) {
                    ic.remove();
                }

            }
        }

        public String getRenderTable() {
            return renderTable;
        }

        public void setRenderTable(String renderTable) {
            this.renderTable = renderTable;
        }

        public void showTable(AjaxBehaviorEvent abe) {
            setRenderTable("true");
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

        public String getFirstPlace() {
            return firstPlace;
        }

        public void setFirstPlace(String firstPlace) {
            this.firstPlace = firstPlace;
        }

        public String getSecondPlace() {
            return secondPlace;
        }

        public void setSecondPlace(String secondPlace) {
            this.secondPlace = secondPlace;
        }

        public String getThirdPlace() {
            return thirdPlace;
        }

        public void setThirdPlace(String thirdPlace) {
            this.thirdPlace = thirdPlace;
        }

        public String getFourthPlace() {
            return fourthPlace;
        }

        public void setFourthPlace(String fourthPlace) {
            this.fourthPlace = fourthPlace;
        }

        public String getFifthPlace() {
            return fifthPlace;
        }

        public void setFifthPlace(String fifthPlace) {
            this.fifthPlace = fifthPlace;
        }
    }

    @Inject
    @annotations.Result
    private Result result;

    @Inject
    private Conversation con;

    public void conInit() {
        if (con.isTransient()) {
            con.begin();
        }
    }
    
    public void endCon(){
        System.out.println("is it fired");
        if(!con.isTransient()) con.end();
    }

    public Result getResult() {
        return result;
    }

    public Set<String[]> establishResults() {
        Set<String[]> retVal = new TreeSet<>(new Comparator<String[]>(){

            @Override
            public int compare(String[] o1, String[] o2) {
                return (Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]))!=0?
                        Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]):o1[0].compareToIgnoreCase(o2[0]);
            }
            
        });
        FlewDao flewdao = FlewDao.getFlewDao();
        Set<String[]> bets = flewdao.queryForResults();
        String[] resultSet = {getResult().getFirstPlace(), getResult().getSecondPlace(),
            getResult().getThirdPlace(), getResult().getFourthPlace(), getResult().getFifthPlace(),
            ""+getResult().getWinnerResult(), ""+getResult().getRunnerUpResult()};
        for (String[] singleBet : bets) {
            String playa = singleBet[0];
            int points = 0;
            if (Candidates.valueOf(resultSet[0]).equals(Candidates.valueOf(singleBet[1]))) {
                points = points + 1;
                if (Integer.parseInt(resultSet[5]) == (Integer.parseInt(singleBet[6]))) {
                    points = points + 2;
                }
                if ((Integer.parseInt(resultSet[5]) == (Integer.parseInt(singleBet[6]) - 1))
                        || (Integer.parseInt(resultSet[5]) == (Integer.parseInt(singleBet[6]) + 1))) {
                    points = points + 1;
                }
            }
            if (Candidates.valueOf(resultSet[1]).equals(Candidates.valueOf(singleBet[2]))) {
                points = points + 1;
                if (Integer.parseInt(resultSet[6])== (Integer.parseInt(singleBet[7]))) {
                    points = points + 2;
                }
                if ((Integer.parseInt(resultSet[6]) == (Integer.parseInt(singleBet[7]) - 1))
                        || (Integer.parseInt(resultSet[6]) == (Integer.parseInt(singleBet[7]) + 1))) {
                    points = points + 1;
                }
            }
            if (Candidates.valueOf(resultSet[2]).equals(Candidates.valueOf(singleBet[3]))) {
                points = points + 2;
            }
            if (Candidates.valueOf(resultSet[3]).equals(Candidates.valueOf(singleBet[4]))) {
                points = points + 2;
            }
            if (Candidates.valueOf(resultSet[4]).equals(Candidates.valueOf(singleBet[5]))) {
                points = points + 2;
            }
            retVal.add(new String[]{playa, String.valueOf(points)});
        }
        return retVal;
    }

}
