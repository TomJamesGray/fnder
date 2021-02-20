package com.gray;

import com.gray.datasources.LocalWiki;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalWikiTest {
    @Test
    public void testGetTags() throws IOException {
        Path wikiTest = Paths.get(System.getProperty("user.dir"),
                "test-wiki-data","one.md");
        String[] retrievedTags = LocalWiki.getTagsAndTitleForMdFile(wikiTest).getValue().toArray(new String[0]);
        String[] tags = {"tag1", "tag one", "tag two"};
        assertArrayEquals(tags, retrievedTags);
    }
}
