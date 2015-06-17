/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew.beans;

import annotations.ResultRebiult;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Set;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.webflew.Candidates;
import pl.lodz.p.webflew.FlewDao;
import pl.lodz.p.webflew.Vote;
import pl.lodz.p.webflew.beans.EstablishResultsBean.Result;

/**
 *
 * @author X870
 */
@SessionScoped
@Named
public class ManageVoteBean implements Serializable {

    @ConversationScoped
    @ResultRebiult
    static class ResultRebuilt extends Result {

        public void rebuildResult() {
            String[] toRebuild = flewDao.querySingleResult(nickRecieved);
            setFirstPlace(toRebuild[0]);
            setSecondPlace(toRebuild[1]);
            setThirdPlace(toRebuild[2]);
            setFourthPlace(toRebuild[3]);
            setFifthPlace(toRebuild[4]);
            setWinnerResult(Integer.parseInt(toRebuild[5]));
            setRunnerUpResult(Integer.parseInt(toRebuild[6]));
        }

    }

    @Inject
    private Conversation con;

    @Inject
    @ResultRebiult
    private ResultRebuilt manageableVote;

    private static String nickRecieved;

    private static FlewDao flewDao = FlewDao.getFlewDao();

    public void conInit() {
        if (con.isTransient()) {
            con.begin();
        }
    }

    public void conEnd() {
        if (!con.isTransient()) {
            con.end();
        }
    }

    public Result getManageableVote() {
        return manageableVote;
    }

    public String getNickRecieved() {
        return nickRecieved;
    }

    public void setNickRecieved(String nickRecieved) {
        this.nickRecieved = nickRecieved;
        System.out.println(nickRecieved);
    }

    public void saveChangedVote() {
        Vote upgraded = new Vote(nickRecieved);
        upgraded.setFirstPlace(Candidates.valueOf(manageableVote.getFirstPlace()));
        upgraded.setSecondPlace(Candidates.valueOf(manageableVote.getSecondPlace()));
        upgraded.setThirdPlace(Candidates.valueOf(manageableVote.getThirdPlace()));
        upgraded.setFourthPlace(Candidates.valueOf(manageableVote.getFourthPlace()));
        upgraded.setFifthPlace(Candidates.valueOf(manageableVote.getFifthPlace()));
        upgraded.setWinnerResult(manageableVote.getWinnerResult());
        upgraded.setRunnerUpResult(manageableVote.getRunnerUpResult());
        try {
            flewDao.castVote(upgraded);
            FacesContext.getCurrentInstance().addMessage("saveButton", new FacesMessage("Poprawnie zapisano zmiany w bazie."));
        } catch (SQLException sqle) {
            FacesContext.getCurrentInstance().addMessage("saveButton", new FacesMessage("Wystąpił problem podczas zapisywania zmian w bazie."));
        }
    }

}
