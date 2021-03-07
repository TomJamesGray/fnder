package com.gray;

import com.gray.datasources.GithubRepos;
import com.gray.datasources.URLResult;
import jdk.jfr.internal.test.WhiteBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GithubRepoTest {
    public JSONArray egJson;
    @Before
    public void setUpJsonArr() throws IOException, ParseException {
        Path jsonTest = Paths.get(System.getProperty("user.dir"),
                "src","test","resources","githubApiTest.json");
        egJson = GithubRepos.readJSON(jsonTest.toUri().toURL().toString());
    }
    /**
     * Tests reading of Json array file, with key "id" and value
     * equal to the index of the object
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testReadJson(){
        for (int i = 0; i < egJson.size(); i++){
            JSONObject x = (JSONObject) egJson.get(i);
            assert(x.get("id").toString().equals(Integer.toString(i)));
        }
    }

    @Test
    public void testGetRepos() {
        URLResult[] testResults = GithubRepos.generateGhReposArr(egJson);
        for (int i = 0; i < testResults.length; i++){
            assertEquals(testResults[i].url,"repo" + i + "-url");
            assertEquals(testResults[i].title,"repo"+ i);
        }
    }
}
