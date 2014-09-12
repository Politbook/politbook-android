package org.appeleicao2014.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thaleslima on 9/7/14.
 */
public class CommentsUser implements Serializable{
    private float candidateAverage;
    private int candidateQtd;

    private Comment commentUser;
    private List<Comment> commentsCandidate;


    public float getCandidateAverage() {
        return candidateAverage;
    }

    public void setCandidateAverage(float candidateAverage) {
        this.candidateAverage = candidateAverage;
    }

    public int getCandidateQtd() {
        return candidateQtd;
    }

    public void setCandidateQtd(int candidateQtd) {
        this.candidateQtd = candidateQtd;
    }

    public Comment getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(Comment commentUser) {
        this.commentUser = commentUser;
    }

    public List<Comment> getCommentsCandidate() {
        if(commentsCandidate == null)
        {
            commentsCandidate = new ArrayList<Comment>();
        }

        return commentsCandidate;
    }

    public void setCommentsCandidate(List<Comment> commentsCandidate) {
        this.commentsCandidate = commentsCandidate;
    }
}
