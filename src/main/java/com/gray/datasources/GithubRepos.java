package com.gray.datasources;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.enterprise.inject.Default;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class GithubRepos implements BaseSource{
    public String reposUrl = "https://api.github.com/users/TomJamesGray/repos";
    private JSONArray jsonArrData;
    private URLResult[] repos;

    /**
     * Initialises data source by reading repo information from github API
     * @throws IOException
     */
    public GithubRepos() throws IOException {
        try {
            repos = generateGhReposArr(readJSON(reposUrl));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all the content in from a reader and returns it
     * @param rd Reader
     * @return String with all the reader content
     * @throws IOException
     */
    public static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static URLResult[] generateGhReposArr(JSONArray x){
        URLResult[] repos = new URLResult[x.size()];
        for (int i = 0; i < x.size(); i++){
            JSONObject repo = (JSONObject) x.get(i);
            repos[i] = new URLResult(repo.get("name").toString(),repo.get("svn_url").toString());
        }
        return repos;
    }

    /**
     * Reads in all JSON content from a url
     * @param url
     * @return
     * @throws IOException
     * @throws ParseException - When the JSON is badly formed
     */
    public static JSONArray readJSON(String url) throws IOException, ParseException {
        InputStream is = new URL(url).openStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String text = readAll(rd);
        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(text);
        return json;
    }

    /**
     * Compares search query against the repositories
     * @param query Search query
     * @param maxResults Maximum amount of results the search query should return
     * @return Array of data source results
     */
    @Override
    public DataSourceResult[] searchFor(String query, int maxResults) {
//        return new DataSourceResult[0];
        int[][] scores = new int[repos.length][2];
        for (int i = 0; i < repos.length; i++){
            scores[i] = new int[]{i, FuzzySearch.ratio(query, repos[i].title)};
        }
//        Sort on the search score
        Arrays.sort(scores, (a,b) -> Integer.compare(a[1],b[1]));
        List<URLResult> scoresOutput = new ArrayList<URLResult>();
        int i = scores.length - 1;
        final int minScore = 50;
        while(scoresOutput.size() < maxResults){
            if (scores[i][1] > minScore) {
                scoresOutput.add(repos[scores[i][0]]);
            }
            else{
                break;
            }
            i -= 1;
        }
        return (scoresOutput.toArray(new URLResult[0]));
//        return scores;
//        return new DataSourceResult[0];
    }

    public URLResult[] getRepos() {
        return repos;
    }

    @Override
    public String getSourceName() {
        return "Github Repos";
    }
}
