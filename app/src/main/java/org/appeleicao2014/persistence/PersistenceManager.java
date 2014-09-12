package org.appeleicao2014.persistence;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.appeleicao2014.model.CommentCandidate;
import org.appeleicao2014.model.Party;
import org.appeleicao2014.R;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.model.Candidature;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.model.Statistics;
import org.appeleicao2014.ui.AppApplication;
import org.appeleicao2014.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thaleslima on 7/9/14.
 */
public class PersistenceManager {
    private static PersistenceManager mInstance;
    private SQLiteHelper mSQLiteHelper;
    private static StringBuilder mQuery = new StringBuilder();

    public static PersistenceManager getInstance() {
        if(mInstance == null)
            mInstance = new PersistenceManager();
        return mInstance;
    }

    private PersistenceManager() {
        mSQLiteHelper = new SQLiteHelper(AppApplication.getContext());
    }


    public synchronized void saveCandidates(List<Candidate> candidates, String idJob){

        if(candidates != null && candidates.size() > 0)
        {
            if (isCandidate(candidates.get(0).getId()))
            {
                return;
            }
        }

        saveCandidates(candidates, idJob, "");
    }

    public synchronized void saveCandidates(List<Candidate> candidates, String idJob, String idParty){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            mQuery.setLength(0);
            mQuery.append("update candidate set ");

            if(idParty.equals("S"))
                mQuery.append("flSearch = ?");
            else
                mQuery.append("idParty = ?");

            mQuery.append(" where id = ?");
            String update = mQuery.toString();

            mQuery.setLength(0);
            mQuery.append("insert into candidate (");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("number,");
            mQuery.append("title,");
            mQuery.append("CPF,");
            mQuery.append("enrollment,");
            mQuery.append("jobTitle,");
            mQuery.append("state,");
            mQuery.append("party,");
            mQuery.append("age,");
            mQuery.append("instruction,");
            mQuery.append("occupation,");
            mQuery.append("miniBio,");
            mQuery.append("positions,");
            mQuery.append("prediction,");
            mQuery.append("countertops,");
            mQuery.append("processes,");
            mQuery.append("currentHome,");
            mQuery.append("reelection,");
            mQuery.append("photo,");
            mQuery.append("idJob,");
            mQuery.append("name,");
            mQuery.append("idParty,");
            mQuery.append("flSearch,");
            mQuery.append("average");

            mQuery.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            db.beginTransaction();

            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());
            SQLiteStatement statementUpdate = db.compileStatement(update);

            boolean insert;

            for (Candidate candidate : candidates) {
                insert = true;

                if(!idParty.isEmpty() && isCandidate(db, candidate.getId())) {
                    statementUpdate.clearBindings();
                    statementUpdate.bindString(1, idParty.equals("S")? "S": idParty);
                    statementUpdate.bindString(2, candidate.getId());
                    statementUpdate.execute();
                    insert = false;
                }

                if(insert) {
                    statementInsert.clearBindings();
                    statementInsert.bindString(1, Util.value(candidate.getId()));
                    statementInsert.bindString(2, Util.value(candidate.getNickname()));
                    statementInsert.bindString(3, Util.value(candidate.getNumber()));
                    statementInsert.bindString(4, Util.value(candidate.getTitle()));
                    statementInsert.bindString(5, Util.value(candidate.getCPF()));
                    statementInsert.bindString(6, Util.value(candidate.getEnrollment()));
                    statementInsert.bindString(7, Util.value(candidate.getJobTitle()));
                    statementInsert.bindString(8, Util.value(candidate.getState()));
                    statementInsert.bindString(9, Util.value(candidate.getParty()));
                    statementInsert.bindString(10, Util.value(candidate.getAge()));
                    statementInsert.bindString(11, Util.value(candidate.getInstruction()));
                    statementInsert.bindString(12, Util.value(candidate.getOccupation()));
                    statementInsert.bindString(13, Util.value(candidate.getMiniBio()));
                    statementInsert.bindString(14, Util.value(candidate.getPositions()));
                    statementInsert.bindString(15, Util.value(candidate.getPrediction()));
                    statementInsert.bindString(16, Util.value(candidate.getCountertops()));
                    statementInsert.bindString(17, Util.value(candidate.getProcesses()));
                    statementInsert.bindString(18, Util.value(candidate.getCurrentHome()));
                    statementInsert.bindString(19, Util.value(candidate.getReelection()));
                    statementInsert.bindString(20, Util.value(candidate.getPhoto()));
                    statementInsert.bindString(21, Util.value(idJob));
                    statementInsert.bindString(22, Util.value(candidate.getName()));

                    if(!idParty.isEmpty())
                    {
                        statementInsert.bindString(23, idParty.equals("S") ? "": idParty);
                        statementInsert.bindString(24, idParty.equals("S") ? "S": "");
                    }
                    else
                    {
                        statementInsert.bindString(23, "");
                        statementInsert.bindString(24, "");
                    }
                    statementInsert.bindLong(24, 0);

                    statementInsert.execute();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void updateCandidates(List<CommentCandidate> candidates)
    {
        SQLiteDatabase db = null;

        try {
            mQuery.setLength(0);
            mQuery.append("update candidate set ");
            mQuery.append("average = ?");
            mQuery.append(" where id = ?");
            String update = mQuery.toString();

            db = mSQLiteHelper.getWritableDatabase();
            db.beginTransaction();
            SQLiteStatement statementUpdate = db.compileStatement(update);

            for (CommentCandidate candidate : candidates) {
                statementUpdate.clearBindings();
                statementUpdate.bindDouble(1, candidate.getAverage());
                statementUpdate.bindString(2, candidate.getId());
                statementUpdate.execute();
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void updateCandidatesAverage(String id, float average)
    {
        SQLiteDatabase db = null;

        try {
            mQuery.setLength(0);
            mQuery.append("update candidate set ");
            mQuery.append("average = ?");
            mQuery.append(" where id = ?");
            String update = mQuery.toString();

            db = mSQLiteHelper.getWritableDatabase();
            db.beginTransaction();
            SQLiteStatement statementUpdate = db.compileStatement(update);

            statementUpdate.clearBindings();
            statementUpdate.bindDouble(1, average);
            statementUpdate.bindString(2, id);
            statementUpdate.execute();

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    public synchronized void saveCandidate(Candidate candidate, String idJob){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            //String deleteCandidate = "delete from candidate where idJob = ?";

            mQuery.setLength(0);
            mQuery.append("insert into candidate (");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("number,");
            mQuery.append("title,");
            mQuery.append("CPF,");
            mQuery.append("enrollment,");
            mQuery.append("jobTitle,");
            mQuery.append("state,");
            mQuery.append("party,");
            mQuery.append("age,");
            mQuery.append("instruction,");
            mQuery.append("occupation,");
            mQuery.append("miniBio,");
            mQuery.append("positions,");
            mQuery.append("prediction,");
            mQuery.append("countertops,");
            mQuery.append("processes,");
            mQuery.append("currentHome,");
            mQuery.append("reelection,");
            mQuery.append("photo,");
            mQuery.append("idJob,");
            mQuery.append("name");
            mQuery.append(") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            db.beginTransaction();

            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());
            //SQLiteStatement statementDelete = db.compileStatement(deleteCandidate);

            //statementDelete.clearBindings();
            //statementDelete.bindString(1, idJob);
            //statementDelete.execute();

            statementInsert.clearBindings();
            statementInsert.bindString(1, Util.value(candidate.getId()));
            statementInsert.bindString(2, Util.value(candidate.getNickname()));
            statementInsert.bindString(3, Util.value(candidate.getNumber()));
            statementInsert.bindString(4, Util.value(candidate.getTitle()));
            statementInsert.bindString(5, Util.value(candidate.getCPF()));
            statementInsert.bindString(6, Util.value(candidate.getEnrollment()));
            statementInsert.bindString(7, Util.value(candidate.getJobTitle()));
            statementInsert.bindString(8, Util.value(candidate.getState()));
            statementInsert.bindString(9, Util.value(candidate.getParty()));
            statementInsert.bindString(10, Util.value(candidate.getAge()));
            statementInsert.bindString(11, Util.value(candidate.getInstruction()));
            statementInsert.bindString(12, Util.value(candidate.getOccupation()));
            statementInsert.bindString(13, Util.value(candidate.getMiniBio()));
            statementInsert.bindString(14, Util.value(candidate.getPositions()));
            statementInsert.bindString(15, Util.value(candidate.getPrediction()));
            statementInsert.bindString(16, Util.value(candidate.getCountertops()));
            statementInsert.bindString(17, Util.value(candidate.getProcesses()));
            statementInsert.bindString(18, Util.value(candidate.getCurrentHome()));
            statementInsert.bindString(19, Util.value(candidate.getReelection()));
            statementInsert.bindString(20, Util.value(candidate.getPhoto()));
            statementInsert.bindString(21, Util.value(idJob));
            statementInsert.bindString(22, Util.value(candidate.getName()));
            statementInsert.execute();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void removeCandidates(){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            String deleteCandidate = "delete from candidate";
            String deleteEquities = "delete from equity";
            String deleteStatistics = "delete from statistics";
            String deleteCandidature = "delete from candidature";
            db.beginTransaction();

            SQLiteStatement statementDelete = db.compileStatement(deleteCandidate);
            SQLiteStatement statementEquities = db.compileStatement(deleteEquities);
            SQLiteStatement statementStatistics = db.compileStatement(deleteStatistics);
            SQLiteStatement statementCandidature = db.compileStatement(deleteCandidature);

            statementDelete.execute();
            statementEquities.execute();
            statementStatistics.execute();
            statementCandidature.execute();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void saveEquities(List<Equity> equities, String idCandidate){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            String deleteCandidate = "delete from equity where idCandidate = ?";

            mQuery.setLength(0);
            mQuery.append("insert into equity (");
            mQuery.append("equity,");
            mQuery.append("amount,");
            mQuery.append("year,");
            mQuery.append("idCandidate");
            mQuery.append(") values (?,?,?,?)");

            db.beginTransaction();

            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());
            SQLiteStatement statementDelete = db.compileStatement(deleteCandidate);

            statementDelete.clearBindings();
            statementDelete.bindString(1, idCandidate);
            statementDelete.execute();

            for (Equity equity : equities) {
                statementInsert.clearBindings();
                statementInsert.bindString(1, Util.value(equity.getEquity()));
                statementInsert.bindString(2, Util.value(equity.getAmount()));
                statementInsert.bindString(3, Util.value(equity.getYear()));
                statementInsert.bindString(4, idCandidate);
                statementInsert.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void saveCandidatures(List<Candidature> candidatures, String idCandidate){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            String delete = "delete from candidature where idCandidate = ?";

            mQuery.setLength(0);
            mQuery.append("insert into candidature (");
            mQuery.append("electionYear,");
            mQuery.append("jobTitle,");
            mQuery.append("party,");
            mQuery.append("state,");
            mQuery.append("result,");
            mQuery.append("votes,");
            mQuery.append("financialResources,");
            mQuery.append("idCandidate");
            mQuery.append(") values (?,?,?,?,?,?,?,?)");

            db.beginTransaction();

            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());
            SQLiteStatement statementDelete = db.compileStatement(delete);

            statementDelete.clearBindings();
            statementDelete.bindString(1, idCandidate);
            statementDelete.execute();

            for (Candidature candidature : candidatures) {
                statementInsert.clearBindings();
                statementInsert.bindString(1, Util.value(candidature.getElectionYear()));
                statementInsert.bindString(2, Util.value(candidature.getJobTitle()));
                statementInsert.bindString(3, Util.value(candidature.getParty()));
                statementInsert.bindString(4, Util.value(candidature.getState()));
                statementInsert.bindString(5, Util.value(candidature.getResult()));
                statementInsert.bindString(6, Util.value(candidature.getVotes()));
                statementInsert.bindString(7, Util.value(candidature.getFinancialResources()));
                statementInsert.bindString(8, idCandidate);
                statementInsert.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized List<Candidature> getCandidatures(String idCandidate) {
        List<Candidature> candidatures = new ArrayList<Candidature>();
        SQLiteDatabase db = null;
        Candidature candidature ;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("electionYear,");
            mQuery.append("jobTitle,");
            mQuery.append("party,");
            mQuery.append("city,");
            mQuery.append("state,");
            mQuery.append("result,");
            mQuery.append("votes,");
            mQuery.append("financialResources,");
            mQuery.append("idCandidate");
            mQuery.append(" FROM candidature");
            mQuery.append(" WHERE  idCandidate = ?");
            mQuery.append(" ORDER BY electionYear");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { idCandidate });

            if (c.moveToFirst()) {
                do {
                    candidature = new Candidature();
                    candidatures.add(candidature);
                    candidature.setElectionYear(c.getString(0));
                    candidature.setJobTitle(c.getString(1));
                    candidature.setParty(c.getString(2));
                    candidature.setCity(c.getString(3));
                    candidature.setState(c.getString(4));
                    candidature.setResult(c.getString(5));
                    candidature.setVotes(c.getString(6));
                    candidature.setFinancialResources(c.getString(7));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return candidatures;
    }

    public synchronized boolean isCandidatures(String idCandidate) {
        boolean r = false;
        SQLiteDatabase db = null;


        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("electionYear");
            mQuery.append(" FROM candidature");
            mQuery.append(" WHERE  idCandidate = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { idCandidate });

            if (c.moveToFirst()) {
               r = true;
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return r;
    }

    public synchronized void saveStatistics(Statistics statistics, String idCandidate){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            String deleteCandidate = "delete from statistics where idCandidate = ?";

            mQuery.setLength(0);
            mQuery.append("insert into statistics (");
            mQuery.append("faultsPlenary,");
            mQuery.append("averagePlenary,");
            mQuery.append("faultsCommissions,");
            mQuery.append("averageCommissions,");
            mQuery.append("evolution,");
            mQuery.append("referenceYear,");
            mQuery.append("amendments,");
            mQuery.append("averageAmendments,");
            mQuery.append("idCandidate");
            mQuery.append(") values (?,?,?,?,?,?,?,?,?)");

            db.beginTransaction();

            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());
            SQLiteStatement statementDelete = db.compileStatement(deleteCandidate);

            statementDelete.clearBindings();
            statementDelete.bindString(1, idCandidate);
            statementDelete.execute();

            statementInsert.clearBindings();
            statementInsert.bindString(1, Util.value(statistics.getFaultsPlenary()));
            statementInsert.bindString(2, Util.value(statistics.getAveragePlenary()));
            statementInsert.bindString(3, Util.value(statistics.getFaultsCommissions()));
            statementInsert.bindString(4, Util.value(statistics.getAverageCommissions()));
            statementInsert.bindString(5, Util.value(statistics.getEvolution()));
            statementInsert.bindString(6, Util.value(statistics.getReferenceYear()));
            statementInsert.bindString(7, Util.value(statistics.getAmendments()));
            statementInsert.bindString(8, Util.value(statistics.getAverageAmendments()));
            statementInsert.bindString(9, Util.value(idCandidate));

            statementInsert.execute();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized List<Candidate> getCandidates(String idJob, String state, String party, String idParty) {
        List<Candidate> candidates = new ArrayList<Candidate>();
        SQLiteDatabase db = null;
        Candidate candidate ;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            List<String> ars = new ArrayList<String>();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("name,");
            mQuery.append("number,");
            mQuery.append("party,");
            mQuery.append("state,");
            mQuery.append("photo,");
            mQuery.append("idJob,");
            mQuery.append("average");
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE  idJob = ? and (state = ? or idJob = '1') ");
            ars.add(idJob);
            ars.add(state);

            if(!party.isEmpty()) {
                mQuery.append(" and party = ?");
                ars.add(party);
            }

            if(!idParty.isEmpty()) {
                if(idParty.equals("S"))
                    mQuery.append(" and flSearch = ?");
                else
                    mQuery.append(" and idParty = ?");

                ars.add(idParty);
            }

            mQuery.append(" ORDER BY average DESC, nickname ASC");

            String[] strArray = ars.toArray(new String[ars.size()]);
            Cursor c = db.rawQuery(mQuery.toString(), strArray);

            //Log.i(Constants.DEBUG, "getCandidates :" + mQuery.toString());

            if (c.moveToFirst()) {
                do {
                    candidate = new Candidate();
                    candidates.add(candidate);
                    candidate.setId(c.getString(0));
                    candidate.setNickname(c.getString(1));
                    candidate.setName(c.getString(2));
                    candidate.setNumber(c.getString(3));
                    candidate.setParty(c.getString(4));
                    candidate.setState(c.getString(5));
                    candidate.setPhoto(c.getString(6));
                    candidate.setIdJobTitle(c.getString(7));
                    candidate.setAverage(c.getFloat(8));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return candidates;
    }

    public synchronized Candidate getCandidate(String id) {
        Candidate candidate = null;
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id,");                   //1
            mQuery.append("nickname,");             //2
            mQuery.append("number,");               //3
            mQuery.append("title,");                //4
            mQuery.append("CPF,");                  //5
            mQuery.append("enrollment,");           //6
            mQuery.append("jobTitle,");             //7
            mQuery.append("state,");                //8
            mQuery.append("party,");                //9
            mQuery.append("age,");                  //10
            mQuery.append("instruction,");          //11
            mQuery.append("occupation,");           //12
            mQuery.append("miniBio,");              //13
            mQuery.append("positions,");            //14
            mQuery.append("prediction,");           //15
            mQuery.append("countertops,");          //16
            mQuery.append("processes,");            //17
            mQuery.append("currentHome,");          //18
            mQuery.append("reelection,");           //19
            mQuery.append("photo,");                //20
            mQuery.append("name");                  //21
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE  id = ?");
            mQuery.append(" ORDER BY id");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(id) });

            if (c.moveToFirst()) {
                candidate = new Candidate();
                candidate.setId(c.getString(0));
                candidate.setNickname(c.getString(1));
                candidate.setNumber(c.getString(2));
                candidate.setTitle(c.getString(3));
                candidate.setCPF(c.getString(4));
                candidate.setEnrollment(c.getString(5));
                candidate.setJobTitle(c.getString(6));
                candidate.setState(c.getString(7));
                candidate.setParty(c.getString(8));
                candidate.setAge(c.getString(9));
                candidate.setInstruction(c.getString(10));
                candidate.setOccupation(c.getString(11));
                candidate.setMiniBio(c.getString(12));
                candidate.setPositions(c.getString(13));
                candidate.setPrediction(c.getString(14));
                candidate.setCountertops(c.getString(15));
                candidate.setProcesses(c.getString(16));
                candidate.setCurrentHome(c.getString(17));
                candidate.setReelection(c.getString(18));
                candidate.setPhoto(c.getString(19));
                candidate.setName(c.getString(20));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return candidate;
    }

    public synchronized boolean isCandidate(String id) {
        SQLiteDatabase db = null;
        boolean r = false;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id");
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE id = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(id) });

            if (c.moveToFirst()) {
                r = true;
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return r;
    }

    public synchronized boolean isCandidate(SQLiteDatabase db, String id) {
        boolean r = false;
        try {
            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id");
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE id = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(id) });
            if (c.moveToFirst()) {
                r = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    public synchronized List<Equity> getEquities(String idCandidate) {
        List<Equity> equities = new ArrayList<Equity>();
        SQLiteDatabase db = null;
        Equity equity;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("equity,");
            mQuery.append("amount,");
            mQuery.append("year");
            mQuery.append(" FROM equity");
            mQuery.append(" WHERE idCandidate = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(idCandidate) });

            if (c.moveToFirst()) {
                do{
                    equity = new Equity();
                    equities.add(equity);
                    equity.setEquity(c.getString(0));
                    equity.setAmount(c.getString(1));
                    equity.setYear(c.getString(2));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return equities;
    }

    public synchronized Statistics getStatistics(String idCandidate) {
        Statistics statistics = null;
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("faultsPlenary,");
            mQuery.append("averagePlenary,");
            mQuery.append("faultsCommissions,");
            mQuery.append("averageCommissions,");
            mQuery.append("evolution,");
            mQuery.append("referenceYear,");
            mQuery.append("amendments,");
            mQuery.append("averageAmendments");
            mQuery.append(" FROM statistics");
            mQuery.append(" WHERE idCandidate = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(idCandidate) });

            if (c.moveToFirst()) {
                statistics = new Statistics();
                statistics.setFaultsPlenary(c.getString(0));
                statistics.setAveragePlenary(c.getString(1));
                statistics.setFaultsCommissions(c.getString(2));
                statistics.setAverageCommissions(c.getString(3));
                statistics.setEvolution(c.getString(4));
                statistics.setReferenceYear(c.getString(5));
                statistics.setAmendments(c.getString(6));
                statistics.setAverageAmendments(c.getString(7));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return statistics;
    }

    public synchronized void saveFavorite(Candidate candidate){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            mQuery.setLength(0);
            mQuery.append("insert into favorite (");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("name,");
            mQuery.append("number,");
            mQuery.append("party,");
            mQuery.append("state,");
            mQuery.append("photo,");
            mQuery.append("idJob");
            mQuery.append(") values (?,?,?,?,?,?,?,?)");

            db.beginTransaction();
            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());

            statementInsert.clearBindings();
            statementInsert.bindString(1, Util.value(candidate.getId()));
            statementInsert.bindString(2, Util.value(candidate.getNickname()));
            statementInsert.bindString(3, Util.value(candidate.getName()));
            statementInsert.bindString(4, Util.value(candidate.getNumber()));
            statementInsert.bindString(5, Util.value(candidate.getParty()));
            statementInsert.bindString(6, Util.value(candidate.getState()));
            statementInsert.bindString(7, Util.value(candidate.getPhoto()));
            statementInsert.bindString(8, Util.value(candidate.getIdJobTitle()));
            statementInsert.execute();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void removeFavorite(String idCandidate){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            String delete = "delete from favorite where id = ?";
            db.beginTransaction();
            SQLiteStatement statementInsert = db.compileStatement(delete);
            statementInsert.bindString(1, Util.value(idCandidate));
            statementInsert.execute();
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized void saveFavorites(List<Candidate> candidates){
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getWritableDatabase();

            mQuery.setLength(0);
            mQuery.append("insert into favorite (");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("name,");
            mQuery.append("number,");
            mQuery.append("party,");
            mQuery.append("state,");
            mQuery.append("photo,");
            mQuery.append("idJob");
            mQuery.append(") values (?,?,?,?,?,?,?,?)");

            db.beginTransaction();
            SQLiteStatement statementInsert = db.compileStatement(mQuery.toString());

            for (Candidate candidate : candidates) {
                statementInsert.clearBindings();
                statementInsert.bindString(1, Util.value(candidate.getId()));
                statementInsert.bindString(2, Util.value(candidate.getNickname()));
                statementInsert.bindString(3, Util.value(candidate.getName()));
                statementInsert.bindString(4, Util.value(candidate.getNumber()));
                statementInsert.bindString(5, Util.value(candidate.getParty()));
                statementInsert.bindString(6, Util.value(candidate.getState()));
                statementInsert.bindString(7, Util.value(candidate.getPhoto()));
                statementInsert.bindString(8, Util.value(candidate.getIdJobTitle()));
                statementInsert.execute();
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public synchronized List<Candidate> getFavorites() {
        List<Candidate> candidates = new ArrayList<Candidate>();
        SQLiteDatabase db = null;
        Candidate candidate;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id,");
            mQuery.append("nickname,");
            mQuery.append("name,");
            mQuery.append("number,");
            mQuery.append("party,");
            mQuery.append("state,");
            mQuery.append("photo,");
            mQuery.append("idJob");
            mQuery.append(" FROM favorite");

            Cursor c = db.rawQuery(mQuery.toString(), null);

            if (c.moveToFirst()) {
                do{
                    candidate = new Candidate();
                    candidates.add(candidate);
                    candidate.setId(c.getString(0));
                    candidate.setNickname(c.getString(1));
                    candidate.setName(c.getString(2));
                    candidate.setNumber(c.getString(3));
                    candidate.setParty(c.getString(4));
                    candidate.setState(c.getString(5));
                    candidate.setPhoto(c.getString(6));
                    candidate.setIdJobTitle(c.getString(7));
                }while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return candidates;
    }

    public synchronized Candidate getFavorite(String id) {
        SQLiteDatabase db = null;
        Candidate candidate = null;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id");
            mQuery.append(" FROM favorite");
            mQuery.append(" WHERE id = ?");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { String.valueOf(id)});

            if (c.moveToFirst()) {
                candidate = new Candidate();
                candidate.setId(c.getString(0));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return candidate;
    }

    public synchronized List<Party> getParties() {
        List<Party> parties = new ArrayList<Party>();
        SQLiteDatabase db = null;
        Party party;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id,");
            mQuery.append("party");
            mQuery.append(" FROM party");
            mQuery.append(" order by party asc");

            Cursor c = db.rawQuery(mQuery.toString(), null);

            party = new Party();
            parties.add(party);
            party.setId("");
            party.setParty(AppApplication.getContext().getString(R.string.all));

            if (c.moveToFirst()) {
                do{
                    party = new Party();
                    parties.add(party);
                    party.setId(c.getString(0));
                    party.setParty(c.getString(1));
                }while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return parties;
    }

    public synchronized List<Party> getParties(String idJob, String state) {
        List<Party> parties = new ArrayList<Party>();
        SQLiteDatabase db = null;
        Party party;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("distinct party");
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE  idJob = ? and (state = ? or idJob = '1')");
            mQuery.append(" ORDER BY party");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { idJob, state });

            party = new Party();
            parties.add(party);
            party.setId("");
            party.setParty(AppApplication.getContext().getString(R.string.all));

            if (c.moveToFirst()) {
                do {
                    party = new Party();
                    parties.add(party);
                    party.setParty(c.getString(0));

                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return parties;
    }

    public synchronized boolean isCandidates(String idJob, String state) {
        boolean r = false;
        SQLiteDatabase db = null;

        try {
            db = mSQLiteHelper.getReadableDatabase();

            mQuery.setLength(0);
            mQuery.append("SELECT ");
            mQuery.append("id");
            mQuery.append(" FROM candidate");
            mQuery.append(" WHERE  idJob = ? and (state = ? or idJob = '1')");

            Cursor c = db.rawQuery(mQuery.toString(), new String[] { idJob, state });

            if (c.moveToFirst()) {
                r = true;
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return r;
    }
}
