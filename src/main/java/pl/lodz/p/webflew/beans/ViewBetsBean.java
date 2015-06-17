/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.webflew.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import pl.lodz.p.webflew.FlewDao;

/**
 *
 * @author X870
 */
@SessionScoped
@Named
public class ViewBetsBean implements Serializable {

    @ConversationScoped
    static class InnerViewResultsBean implements Serializable {

        private FlewDao flewDao = FlewDao.getFlewDao();
        public Set<String[]> populateTable() {
            return flewDao.queryForResults();
        }

    }

    @Inject
    private InnerViewResultsBean ivrb;

    @Inject
    private Conversation conversation;

    public void conInit() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public InnerViewResultsBean getIvrb() {
        return ivrb;
    }
    
    public void endCon(){
        if(!conversation.isTransient()) conversation.end();
    }

    public String deleteVote(String owner){
        try {
            getIvrb().flewDao.deleteVote(owner);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("voteDeletion", 
                    new FacesMessage("Nastąpił błąd przy próbie usunięcia głosu z bazy."));
        }
        return "viewBets.xhtml";
    }

}
