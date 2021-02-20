package com.gray;

import com.gray.datasources.LocalWiki;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.graalvm.util.CollectionsUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalWikiTest {
    @Test
    public void testGetTags() throws IOException {
        Path wikiTest = Paths.get(System.getProperty("user.dir"),
                "test-wiki-data","one.md");
        String[] retrievedTags = LocalWiki.getTagsForMdFile(wikiTest).toArray(new String[0]);
        String[] tags = {"tag1", "tag one", "tag two"};
        assertArrayEquals(tags, retrievedTags);
    }
}
