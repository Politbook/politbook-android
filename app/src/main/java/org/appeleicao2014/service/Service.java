package org.appeleicao2014.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.appeleicao2014.R;
import org.appeleicao2014.exception.ConnectionException;
import org.appeleicao2014.model.Candidate;
import org.appeleicao2014.model.Candidature;
import org.appeleicao2014.model.Comment;
import org.appeleicao2014.model.CommentCandidate;
import org.appeleicao2014.model.CommentsUser;
import org.appeleicao2014.model.Equity;
import org.appeleicao2014.model.Statistics;
import org.appeleicao2014.model.User;
import org.appeleicao2014.util.Constants;
import org.appeleicao2014.util.Util;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thaleslima on 8/20/14.
 */
public class Service {
    public static List<Candidate> loadCandidates(Context context, String state, String jobTitle) throws Exception
    {
        String parameters = "?estado=" + state + "&cargo="+ jobTitle;

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Candidate> tables = new ArrayList<Candidate>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Candidate[].class)));

        return tables;
    }

    public static List<Candidate> loadCandidatesLess(Context context, String state, String jobTitle, int page, String party) throws Exception
    {
        String parameters = "?estado=" + state + "&cargo="+ jobTitle + "&_offset=" + page + "&partido=" +party;

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Candidate> tables = new ArrayList<Candidate>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Candidate[].class)));

        return tables;
    }

    public static List<Candidate> loadCandidatesSearch(Context context, String state, String jobTitle, int page, String name) throws Exception
    {
        String parameters = "?estado=" + state + "&cargo="+ jobTitle +"&_offset=" + page + "&nome=" + name;

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Candidate> tables = new ArrayList<Candidate>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Candidate[].class)));

        return tables;
    }

    public static Candidate loadCandidate(Context context, String id) throws Exception
    {
        String parameters = "/" + id;
        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();
        Candidate candidate = gson.fromJson(response, Candidate.class);
        return candidate;
    }

    public static List<Equity> loadCandidateEquity(Context context, String id) throws Exception
    {
        String parameters = "/" + id + "/";

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters + Constants.URL_GET_EQUITIES, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Equity> tables = new ArrayList<Equity>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Equity[].class)));

        return tables;
    }

    public static List<Candidature> loadCandidature(Context context, String id) throws Exception
    {
        String parameters = "/" + id + "/";

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters + Constants.URL_GET_CANDIDATURE, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Candidature> tables = new ArrayList<Candidature>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Candidature[].class)));

        return tables;
    }

    public static Statistics loadCandidateStatistics(Context context, String id) throws Exception
    {
        String parameters = "/" + id + "/";

        String response = openUrlGet(context, Constants.URL_WS + Constants.URL_GET_CANDIDATES + parameters + Constants.URL_GET_STATISTICS, Constants.PARAMETER_TOKEN, Constants.TOKEN);
        Gson gson = new Gson();

        List<Statistics> tables = new ArrayList<Statistics>();

        tables.addAll(Arrays.asList(gson.fromJson(response, Statistics[].class)));

        if(tables.size() > 0)
            return tables.get(0);

        return new Statistics();
    }


    public static CommentsUser loadCommentsUser(Context context, String idCandidate, int idUser) throws Exception
    {
        String response = openUrlGet(context, Constants.URL_WS_APP + Constants.URL_GET_COMMENTS + idCandidate + "/" + Constants.URL_GET_USERS + idUser, null, null);
        Gson gson = new Gson();

        CommentsUser tables = gson.fromJson(response, CommentsUser.class);


        return tables;
    }

    public static List<Comment> loadComments(Context context, String idCandidate, int page) throws Exception
    {
        String parameters = idCandidate + "/?_page=" + page + "&_offset=15";

        String response = openUrlGet(context, Constants.URL_WS_APP + Constants.URL_GET_COMMENTS + parameters, null, null);
        Gson gson = new Gson();

        List<Comment> tables = new ArrayList<Comment>();
        tables.addAll(Arrays.asList(gson.fromJson(response, Comment[].class)));

        return tables;
    }

    public static CommentsUser saveComment(Context context, Comment comment) throws Exception
    {
        String response = openUrlPost(context, Constants.URL_WS_APP + Constants.URL_GET_COMMENTS + comment.getIdCandidate()  + "/" +  Constants.URL_GET_USERS + comment.getIdUser(), comment);
        Gson gson = new Gson();
        return gson.fromJson(response, CommentsUser.class);
    }

    public static List<CommentCandidate> loadCandidatesRating(Context context, String state, String jobTitle) throws Exception
    {
        String parameters = "?state=" + state + "&idTitleJob="+ jobTitle;

        String response = openUrlGet(context, Constants.URL_WS_APP + Constants.URL_GET_COMMENTS_CANDIDATES + parameters, null, null);
        Gson gson = new Gson();

        List<CommentCandidate> tables = new ArrayList<CommentCandidate>();
        tables.addAll(Arrays.asList(gson.fromJson(response, CommentCandidate[].class)));

        return tables;
    }

    public static CommentsUser deleteComment(Context context, String idCandidate, int idUser) throws Exception
    {
        String response = openUrlDelete(context, Constants.URL_WS_APP + Constants.URL_GET_COMMENTS + idCandidate  + "/" +  Constants.URL_GET_USERS + idUser);
        Gson gson = new Gson();
        return gson.fromJson(response, CommentsUser.class);
    }


    public static User saveUser(Context context, User user) throws Exception
    {
        String response = openUrlPost(context, Constants.URL_WS_APP + Constants.URL_GET_USERS, user);
        Gson gson = new Gson();
        user = gson.fromJson(response, User.class);

        return user;
    }

    private static synchronized String openUrlGet(Context context, String url, String tokenName, String token) throws Exception {
        String body;

        Log.i(Constants.DEBUG, "Initiation: " + url);

        try {
            if (Util.isNetworkAvailable(context)) {

                URL url2 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("charset", "utf-8");

                if (token != null) {
                    connection.setRequestProperty(tokenName, token);
                }

                //open
                connection.connect();

                int HttpResult = connection.getResponseCode();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response2 = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response2.append(inputLine);
                }
                in.close();

                body = response2.toString();

                if (HttpResult != HttpStatus.SC_OK) {
                    String message = new JSONObject(body).getString("Message");
                    throw new Exception(message);
                }



               /* HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);

                if (token != null) {
                    get.setHeader(tokenName, token);
                }

                HttpResponse response = client.execute(get);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                body = responseHandler.handleResponse(response);

                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String message = new JSONObject(body).getString("message");
                    throw new Exception(message);
                }*/

            } else {
                throw new ConnectionException();
            }
        } catch (ConnectionException e){
            Log.i(Constants.DEBUG, "ConnectionException: " + e.getMessage());
            throw new Exception(context.getString(R.string.no_connection));
        } catch (Exception e) {
            Log.i(Constants.DEBUG, "Exception: " + e.getMessage());
            throw new Exception(context.getString(R.string.service_unavailable));
        }

        Log.i(Constants.DEBUG, "End: " + url);

        return body;
    }

    private static String openUrlPost(Context context, String url, Object src) throws Exception
    {
        String response;
        Log.i(Constants.DEBUG, "Initiation: POST " + url);

        try {
            if(Util.isNetworkAvailable(context))
            {
                Gson gson = new Gson();

                URL url2 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");

                //open
                connection.connect();
                OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                os.write(gson.toJson(src).getBytes());
                os.flush();

                int HttpResult = connection.getResponseCode();

                BufferedReader in;

                if(HttpResult == HttpStatus.SC_OK)
                {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                else
                {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String inputLine;
                StringBuilder response2 = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response2.append(inputLine);
                }
                in.close();

                response = response2.toString();

                if (HttpResult != HttpStatus.SC_OK) {
                    String message = new JSONObject(response).getString("Message");
                    throw new Exception(message);
                }
            } else {
                throw new ConnectionException();
            }
        } catch (ConnectionException e){
            Log.i(Constants.DEBUG, "ConnectionException: " + e.getMessage());
            throw new Exception(context.getString(R.string.no_connection));
        } catch (Exception e) {
            Log.i(Constants.DEBUG, "Exception: " + e.getMessage());
            throw new Exception(context.getString(R.string.service_unavailable));
        }
        Log.i(Constants.DEBUG, "End: POST " + url);

        return response;
    }


    private static String openUrlDelete(Context context, String url) throws Exception
    {
        String response;

        Log.i(Constants.DEBUG, "Initiation: DELETE " + url);

        try {
            if(Util.isNetworkAvailable(context))
            {
                URL url2 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");

                //open
                connection.connect();
                int HttpResult = connection.getResponseCode();

                BufferedReader in;

                if(HttpResult == HttpStatus.SC_OK)
                {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                else
                {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String inputLine;
                StringBuilder response2 = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response2.append(inputLine);
                }
                in.close();

                response = response2.toString();

                if (HttpResult != HttpStatus.SC_OK) {
                    String message = new JSONObject(response).getString("Message");
                    throw new Exception(message);
                }
            }
            else
            {

                throw new Exception(context.getString(R.string.service_unavailable));
            }
        }
        catch (Exception e){
            Log.i(Constants.DEBUG, e.getMessage());
            throw new Exception(context.getString(R.string.check_address));
        }

        Log.i(Constants.DEBUG, "End: DELETE " + url);

        return response;
    }
}
