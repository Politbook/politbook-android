package org.appeleicao2014.ui.fragment;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Comment;
import org.appeleicao2014.model.CommentCandidate;
import org.appeleicao2014.model.CommentsUser;
import org.appeleicao2014.model.User;
import org.appeleicao2014.persistence.PersistenceManager;
import org.appeleicao2014.session.Session;
import org.appeleicao2014.task.DeleteCommentsTask;
import org.appeleicao2014.task.LoadCommentsTask;
import org.appeleicao2014.task.LoadCommentsUserTask;
import org.appeleicao2014.task.SaveCommentTask;
import org.appeleicao2014.task.SaveUserTask;
import org.appeleicao2014.ui.AppApplication;
import org.appeleicao2014.ui.adapter.AdapterComment;
import org.appeleicao2014.util.Callback;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.EndlessScrollListener;
import org.appeleicao2014.util.GPSTracker;
import org.appeleicao2014.util.Util;

import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class FragmentComment extends Fragment implements Callback, EndlessScrollListener.OnLoadMoreListener {
    public static String KEY_ID_CAN = "idCandidate";
    public static String KEY_STATE_CAN = "estateCandidate";
    public static String KEY_TITLE_JOB_CAN = "titleJobCandidate";
    public static String KEY_NAME_CAN = "nameCandidate";
    public static String KEY_COMMENT = "comment";

    private ListView mlvComments;

    private LinearLayout mllRating;
    private TextView mtvNameUser;
    private RatingBar mrbRating;

    private RelativeLayout mllMyRating;
    private TextView mtvNameUser2;
    private RatingBar mrbRating2;
    private TextView mtvDate;
    private TextView mtvEdit;
    private TextView mtvComment;

    private TextView mtvRating;
    private RatingBar mrbRating3;
    private TextView mtvQtdUser;

    private String mId;
    private String mState;
    private String mTitleJob;
    private String mNameCandidate;

    private CommentsUser mCommentUser;

    private AdapterComment mAdapterComment;
    private ProgressBar mpbSocial;
    private ProgressBar mpbProgressBar;

    private LoginButton mbtSocialFacebook;
    private ProfilePictureView mivPhotoFacebook;
    private ProfilePictureView ivPhotoFacebook2;

    private ProgressBar mpbProgressBarLess;

    private LinearLayout mllMessage;
    private Button mbtConnection;

    private GraphUser mUser = null;

    private boolean onRatingBar = false;
    private boolean onDeleteRating = false;

    private static GPSTracker gpsTracker;

    private static GPSTracker gpsTracker()
    {
        try {
            if(gpsTracker == null)
                gpsTracker = new GPSTracker(AppApplication.getContext());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return gpsTracker;
    }

    private CommentsUser getCommentUser()
    {
        if(mCommentUser == null)
        {
            mCommentUser = new CommentsUser();
        }

        return mCommentUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);

        mlvComments = (ListView) rootView.findViewById(R.id.lvComments);
        mpbProgressBar = (ProgressBar) rootView.findViewById(R.id.pbProgressBar);
        mllMessage = (LinearLayout) rootView.findViewById(R.id.llMessage);
        mbtConnection = (Button) rootView.findViewById(R.id.btConnection);

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.comment_header, mlvComments, false);
        View footer = inflater.inflate(R.layout.comment_footer, mlvComments, false);

        mllRating = (LinearLayout) header.findViewById(R.id.llRating);
        mtvNameUser = (TextView) header.findViewById(R.id.tvNameUser);
        mrbRating = (RatingBar) header.findViewById(R.id.rbRating);

        mllMyRating = (RelativeLayout) header.findViewById(R.id.llMyRating);
        mtvNameUser2 = (TextView) header.findViewById(R.id.tvNameUser2);
        mrbRating2 = (RatingBar) header.findViewById(R.id.rbRating2);
        mtvDate = (TextView) header.findViewById(R.id.tvDate);
        mtvEdit = (TextView) header.findViewById(R.id.tvEdit);
        mtvComment = (TextView) header.findViewById(R.id.tvComment);

        mtvRating = (TextView) header.findViewById(R.id.tvRating);
        mrbRating3 = (RatingBar) header.findViewById(R.id.rbRating3);
        mtvQtdUser = (TextView) header.findViewById(R.id.tvQtdUser);
        mpbSocial = (ProgressBar) header.findViewById(R.id.pbSocial);

        mpbProgressBarLess = (ProgressBar) footer.findViewById(R.id.pbProgressBarLess);

        mlvComments.addHeaderView(header);
        mlvComments.addFooterView(footer);

        mivPhotoFacebook = (ProfilePictureView) rootView.findViewById(R.id.ivPhotoFacebook);
        ivPhotoFacebook2 = (ProfilePictureView) rootView.findViewById(R.id.ivPhotoFacebook2);

        if (Session.getInstance().getCurrentUser() == null) {
            mbtSocialFacebook = (LoginButton) rootView.findViewById(R.id.btSocialFacebook);
        }

        loadData(savedInstanceState);
        initListener();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_COMMENT, mCommentUser);
    }

    private void sendUser()
    {
        User user2 = new User();
        user2.setName(mUser.getName());
        user2.setIdSocial(mUser.getId());
        user2.setLastName(mUser.getLastName());
        user2.setBirthday(mUser.getBirthday());
        user2.setFirstName(mUser.getFirstName());
        user2.setBirthday(mUser.getBirthday());

        try{
            user2.setGender((String) mUser.getProperty("gender"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try{
            user2.setEmail((String) mUser.getProperty("email"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try{
            user2.setBirthday(mUser.getBirthday() + mUser.getProperty("age_range"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        new SaveUserTask(this.getActivity(), 4, this, user2).execute();
    }


    private void loadData(Bundle savedInstanceState) {
        Log.i(Constants.DEBUG, "FragmentComment.loadData");

        if(savedInstanceState != null)
        {
            mCommentUser = (CommentsUser) savedInstanceState.getSerializable(KEY_COMMENT);
        }

        initFields();

        mId = getArguments().getString(KEY_ID_CAN);
        mState = getArguments().getString(KEY_STATE_CAN);
        mTitleJob = getArguments().getString(KEY_TITLE_JOB_CAN);
        mNameCandidate = getArguments().getString(KEY_NAME_CAN);

        if(mCommentUser == null) {
            mlvComments.setOnScrollListener(new EndlessScrollListener(this, 2, 0));
        }
        else
        {
            mpbProgressBar.setVisibility(View.GONE);
            mpbSocial.setVisibility(View.GONE);

            initComments();
        }
    }

    private void loadComments()
    {
        Log.i(Constants.DEBUG, "FragmentComment.loadComments");
        int idUser = 0;

        mpbProgressBar.setVisibility(View.VISIBLE);

        if(Session.getInstance().getCurrentUser() != null)
        {
            idUser = Session.getInstance().getCurrentUser().getId();
        }

        new LoadCommentsUserTask(getActivity(), 1, this, mId, idUser).execute();
    }

    private void initFields()
    {
        mpbSocial.setVisibility(View.GONE);
        mllMyRating.setVisibility(View.GONE);
        mllRating.setVisibility(View.GONE);
        mpbProgressBar.setVisibility(View.GONE);
        mlvComments.setVisibility(View.GONE);
        mpbProgressBarLess.setVisibility(View.GONE);
    }

    private void initComments()
    {
        Log.i(Constants.DEBUG, "FragmentComment.initComments");

        mlvComments.setVisibility(View.VISIBLE);

        mAdapterComment = new AdapterComment(this.getActivity(), getCommentUser().getCommentsCandidate());

        mrbRating3.setRating(getCommentUser().getCandidateAverage());
        mtvQtdUser.setText("("+ getCommentUser().getCandidateQtd() +")");
        mtvRating.setText(Util.formatDecimal(getCommentUser().getCandidateAverage()));

        mlvComments.setAdapter(mAdapterComment);

        int currentPage = 0;
        int currentTotalItems = 0;

        try{

            currentTotalItems = getCommentUser().getCommentsCandidate().size();
            currentPage = currentTotalItems / 15;

            if(currentPage == 0)
            {
                currentPage = 2;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        mlvComments.setOnScrollListener(new EndlessScrollListener(this, currentPage, currentTotalItems));
        initCommentUser();
    }

    private void initCommentUser()
    {
        mpbSocial.setVisibility(View.GONE);

        if(Session.getInstance().getCurrentUser() == null)
        {
            if(mbtSocialFacebook != null) {
                mbtSocialFacebook.setVisibility(View.VISIBLE);
            }
        }
        else {
            if(mbtSocialFacebook != null) {
                mbtSocialFacebook.setVisibility(View.GONE);
            }
            if (getCommentUser().getCommentUser() == null) {
                mllRating.setVisibility(View.VISIBLE);
                mllMyRating.setVisibility(View.GONE);
                mtvNameUser.setText(Session.getInstance().getCurrentUser().getName());
                mivPhotoFacebook.setProfileId(Session.getInstance().getCurrentUser().getIdSocial());
            } else {
                mllMyRating.setVisibility(View.VISIBLE);
                mllRating.setVisibility(View.GONE);

                mrbRating2.setRating(getCommentUser().getCommentUser().getRating());
                mtvDate.setText(getCommentUser().getCommentUser().getDate());
                mtvComment.setText(getCommentUser().getCommentUser().getComment());

                mtvNameUser2.setText(Session.getInstance().getCurrentUser().getName());
                ivPhotoFacebook2.setProfileId(Session.getInstance().getCurrentUser().getIdSocial());
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListener() {
        if(mbtSocialFacebook != null) {
            mbtSocialFacebook.setPreOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mllMyRating.setVisibility(View.GONE);
                    mbtSocialFacebook.setVisibility(View.GONE);
                    mpbSocial.setVisibility(View.VISIBLE);
                    mllRating.setVisibility(View.GONE);
                }
            });
        }

        mrbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                onRatingBar = true;
                if (!onDeleteRating) {
                    openDialogComments();
                }
                onDeleteRating = false;
            }
        });

        mbtConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mllMessage.setVisibility(View.GONE);
                loadComments();
            }
        });

        mtvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogComments();
            }
        });

        if(mbtSocialFacebook != null) {
            mbtSocialFacebook.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                    mUser = user;

                    if (Session.getInstance().getCurrentUser() == null) {
                        if (mUser != null) {
                            sendUser();
                        }
                    } else {
                        if (mUser != null) {
                            if (!Session.getInstance().getCurrentUser().getName().equals(mUser.getName()) ||
                                !Session.getInstance().getCurrentUser().getIdSocial().equals((mUser.getId()))) {
                                sendUser();
                                return;
                            }
                        }
                        initCommentUser();
                    }
                }
            });
        }
    }

    @Override
    public void onPostExecute(int process, Object object, boolean error, String descriptionError) {
        mpbProgressBar.setVisibility(View.GONE);

        if (process == 1) {
            if (!error) {
                CommentsUser commentsUser = (CommentsUser) object;

                getCommentUser().getCommentsCandidate().clear();
                getCommentUser().getCommentsCandidate().addAll(commentsUser.getCommentsCandidate());
                getCommentUser().setCommentUser(commentsUser.getCommentUser());

                getCommentUser().setCandidateAverage(commentsUser.getCandidateAverage());
                getCommentUser().setCandidateQtd(commentsUser.getCandidateQtd());

                initComments();
            } else {
                initFields();
                mllMessage.setVisibility(View.VISIBLE);
            }
        } else if (process == 2) {
            if (!error) {
                CommentsUser commentsUser = (CommentsUser) object;
                getCommentUser().getCommentsCandidate().clear();
                getCommentUser().getCommentsCandidate().addAll(commentsUser.getCommentsCandidate());
                getCommentUser().setCommentUser(commentsUser.getCommentUser());

                getCommentUser().setCandidateAverage(commentsUser.getCandidateAverage());
                getCommentUser().setCandidateQtd(commentsUser.getCandidateQtd());

                PersistenceManager.getInstance().updateCandidatesAverage(mId,commentsUser.getCandidateAverage());

                initComments();
            } else {
                initCommentUser();
                Toast.makeText(this.getActivity(), descriptionError, Toast.LENGTH_SHORT).show();
            }
        } else if (process == 3) {
            if (!error) {
                CommentsUser commentsUser = (CommentsUser) object;
                getCommentUser().getCommentsCandidate().clear();
                getCommentUser().getCommentsCandidate().addAll(commentsUser.getCommentsCandidate());
                getCommentUser().setCommentUser(commentsUser.getCommentUser());

                getCommentUser().setCandidateAverage(commentsUser.getCandidateAverage());
                getCommentUser().setCandidateQtd(commentsUser.getCandidateQtd());

                PersistenceManager.getInstance().updateCandidatesAverage(mId,commentsUser.getCandidateAverage());
                initComments();
                onDeleteRating = false;
            } else {
                initCommentUser();
                Toast.makeText(this.getActivity(), descriptionError, Toast.LENGTH_SHORT).show();
            }
        } else if (process == 4) {
            if (!error) {
                User user2 = (User) object;
                if(user2 != null) {
                    Session.getInstance().setCurrentUser(user2);
                    new LoadCommentsUserTask(getActivity(), 1, this, mId, user2.getId()).execute();
                }
            } else {
                initFields();
                mllMessage.setVisibility(View.VISIBLE);
            }
        } else if (process == 5) {
            mpbProgressBarLess.setVisibility(View.GONE);
            if (!error) {
                List<Comment> items = (List<Comment>) object;
                getCommentUser().getCommentsCandidate().addAll(items);
                mAdapterComment.notifyDataSetChanged();
            } else {
                Toast.makeText(this.getActivity(), descriptionError, Toast.LENGTH_SHORT).show();
            }
        }

        mrbRating.setEnabled(true);
        mtvEdit.setEnabled(true);
        mllRating.setEnabled(true);

        Log.i(Constants.DEBUG, "FragmentComment.onPostExecute");
    }

    @Override
    public void onPreExecute(int process) {
        Log.i(Constants.DEBUG, "FragmentComment.onPreExecute");

        mllRating.setEnabled(false);
        mtvEdit.setEnabled(false);
        mrbRating.setEnabled(false);

        if(process == 4)
        {
            mpbProgressBar.setVisibility(View.GONE);
            mllMessage.setVisibility(View.GONE);
            mllMyRating.setVisibility(View.GONE);
            if(mbtSocialFacebook != null) {
                mbtSocialFacebook.setVisibility(View.GONE);
            }
            mpbSocial.setVisibility(View.VISIBLE);
            mllRating.setVisibility(View.GONE);
        }
        else if(process == 5)
        {
            mpbProgressBarLess.setVisibility(View.VISIBLE);
        }
    }

    private Callback getCallback()
    {
        return this;
    }

    private void openDialogComments()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getActivity());

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.comment_dialog, null);

        final RatingBar rbRating = (RatingBar) view.findViewById(R.id.rbRating);
        final Button btOK = (Button) view.findViewById(R.id.btOK);
        final Button btDelete = (Button) view.findViewById(R.id.btDelete);
        final EditText etComment = (EditText) view.findViewById(R.id.etComment);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        if(getCommentUser().getCommentUser() != null)
        {
            rbRating.setRating(getCommentUser().getCommentUser().getRating());
            etComment.setText(getCommentUser().getCommentUser().getComment());
        }
        else
        {
            btDelete.setVisibility(View.GONE);
        }

        if(onRatingBar)
        {
            rbRating.setRating(mrbRating.getRating());
        }

        onRatingBar = false;
        tvTitle.setText(getString(R.string.title_rating) + " " + Session.getInstance().getCurrentUser().getName());
        builder1.setView(view);

        final AlertDialog alert11 = builder1.create();
        alert11.show();

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setIdCandidate(mId);
                comment.setRating((int) rbRating.getRating());
                comment.setIdUser(Session.getInstance().getCurrentUser().getId());
                comment.setComment(etComment.getText().toString());

                try {
                    if(gpsTracker() != null && gpsTracker().canGetLocation())
                    {
                        comment.setLatitude(gpsTracker.getLatitude());
                        comment.setLongitude(gpsTracker.getLongitude());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                CommentCandidate candidate = new CommentCandidate();
                candidate.setName(mNameCandidate);
                candidate.setIdTitleJob(mTitleJob);
                candidate.setState(mState);

                comment.setCandidate(candidate);

                mllRating.setVisibility(View.GONE);
                mllMyRating.setVisibility(View.VISIBLE);
                mrbRating2.setRating(rbRating.getRating());
                mtvNameUser2.setText(Session.getInstance().getCurrentUser().getName());
                ivPhotoFacebook2.setProfileId(Session.getInstance().getCurrentUser().getIdSocial());
                mtvDate.setText("");
                mtvComment.setText("");

                new SaveCommentTask(view.getContext(), 2, getCallback(), comment).execute();

                alert11.dismiss();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Session.getInstance().getCurrentUser() != null && getCommentUser().getCommentUser() != null)
                {
                    mllRating.setVisibility(View.VISIBLE);
                    mllMyRating.setVisibility(View.GONE);
                    new DeleteCommentsTask(view.getContext(), 3, getCallback(), mId, Session.getInstance().getCurrentUser().getId()).execute();
                }

                onDeleteRating = true;
                mrbRating.setRating(0);

                alert11.dismiss();
            }
        });
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if(mCommentUser == null) {
            loadComments();
        }
        else if (mCommentUser.getCommentsCandidate().size() > 0){
            new LoadCommentsTask(getActivity(), 5, this, mId, page).execute();
        }
    }
}
