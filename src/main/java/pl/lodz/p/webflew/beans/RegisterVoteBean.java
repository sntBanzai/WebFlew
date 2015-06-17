 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.webflew.Candidates;
import pl.lodz.p.webflew.Forumowicz;

/**
 *
 * @author X870
 */
@ConversationScoped
@Named
public class RegisterVoteBean implements Serializable {

    @ConversationScoped
    static class AuxiliaryRegisterVB implements Serializable {

        private boolean secondDisabled = true;
        private boolean firstDisabled = false;
        private Set<Candidates> remainingCandidates = new HashSet<>(Arrays.asList(Candidates.values()));

        public boolean isSecondDisabled() {
            return secondDisabled;
        }

        public void setSecondDisabled(boolean secondDisabled) {
            this.secondDisabled = secondDisabled;
        }

        public boolean isFirstDisabled() {
            return firstDisabled;
        }

        public void setFirstDisabled(boolean firstDisabled) {
            this.firstDisabled = firstDisabled;
        }

        public Set<Candidates> getRemainingCandidates() {
            return remainingCandidates;
        }

        public void setRemainingCandidates(Set<Candidates> remainingCandidates) {
            this.remainingCandidates = remainingCandidates;
        }

    }

    @Inject
    private Conversation conversation;

    @Inject
    private AuxiliaryRegisterVB auxrvb;

    private String playersName;
    private String firstPlace;
    private String secondPlace;
    private String thirdPlace;
    private String fourthPlace;
    private String fifthPlace;
    private int winnersResult;
    private int runnerUpResult;
    private Forumowicz f;

    public RegisterVoteBean() {
    }

    public void conInit() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public AuxiliaryRegisterVB getauxrvb() {
        return auxrvb;
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
        System.out.println(fifthPlace);
    }

    public void updateRemaining(AjaxBehaviorEvent abe) {
        auxrvb.setRemainingCandidates(new HashSet<>(Arrays.asList(Candidates.values())));
        Iterator<Candidates> ic = auxrvb.getRemainingCandidates().iterator();
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

    public String getPlayersName() {
        return playersName;
    }

    public void setPlayersName(String playersName) {
        this.playersName = playersName;
    }

    public int getWinnersResult() {
        return winnersResult;
    }

    public void setWinnersResult(int winnersResult) {
        this.winnersResult = winnersResult;
    }

    public int getRunnerUpResult() {
        return runnerUpResult;
    }

    public void setRunnerUpResult(int runnerUpResult) {
        this.runnerUpResult = runnerUpResult;
    }

    public void createPlayer() {
        f = new Forumowicz(playersName);
        String str = "Pomyślnie zapisano gracza do bazy";
        FacesContext.getCurrentInstance().addMessage("playa",
                new FacesMessage("Poprawnie zarejestrowano gracza w bazie"));
        auxrvb.setSecondDisabled(false);
        auxrvb.setFirstDisabled(true);
    }

    public void saveToDatabase() {
        try {
            f.vote(new Candidates[]{Candidates.valueOf(firstPlace),
                Candidates.valueOf(secondPlace), Candidates.valueOf(thirdPlace),
                Candidates.valueOf(fourthPlace), Candidates.valueOf(fifthPlace)},
                    new int[]{winnersResult, runnerUpResult});
            auxrvb.setSecondDisabled(true);
            FacesContext.getCurrentInstance().addMessage("saveButton",
                    new FacesMessage("Poprawnie zapisano głos gracza w bazie"));
        } catch (SQLException sqle) {
            auxrvb.setSecondDisabled(true);
            FacesContext.getCurrentInstance().addMessage("saveButton",
                    new FacesMessage("Nastąpił błąd podczas zapisywania głosu do bazy"));
        }
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }
}
