package com.gray.datasources;

import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class WikipediaSearch implements BaseSource{
    private URIBuilder queryUrl;
    public WikipediaSearch() throws URISyntaxException {
        queryUrl = new URIBuilder("https://en.wikipedia.org/w/api.php");
        queryUrl.addParameter("action","opensearch");
        queryUrl.addParameter("format","json");
    }

    public URLResult[] queryWikipedia(String query,int resultNo) throws Exception {
        URIBuilder queryUrlNew = queryUrl;
        queryUrlNew.addParameter("limit", Integer.toString(resultNo));
        queryUrlNew.addParameter("search",query);
//        Open input stream
        InputStream is = queryUrlNew.build().toURL().openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//        TODO refactor so JSON type methods are in a seperate class
        String rawJson = GithubRepos.readAll(rd);
        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(rawJson);
//        Generate URLResult[] from json response
        JSONArray namesJson = (JSONArray) json.get(1);
        JSONArray urlsJson = (JSONArray) json.get(3);
        if (namesJson.size() != urlsJson.size()){
//            Make new exception type
            throw new Exception("Names json and urls json aren't equal sizes");
        }
        URLResult[] wikiRes = new URLResult[namesJson.size()];
        for (int i = 0; i < namesJson.size(); i++){
            wikiRes[i] = new URLResult((String) namesJson.get(i),(String) urlsJson.get(i));
        }
        return wikiRes;
    }

    @Override
    public DataSourceResult[] searchFor(String query, int maxResults) {
        try {
            return queryWikipedia(query,maxResults);
        } catch (Exception e) {
            e.printStackTrace();
        }
//       TODO Check titles are similar enough to search query
        return new DataSourceResult[0];
    }

    @Override
    public String getSourceName() {
        return "Wikipedia";
    }
}
