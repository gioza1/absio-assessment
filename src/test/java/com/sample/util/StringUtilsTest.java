package com.sample.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringUtilsTest {
    @Test
    void testIsEmptyEmpty() {
        String string = "";
        Assert.assertTrue(StringUtils.isEmpty(string));
    }

    @Test
    void testIsEmptyNull() {
        String string = null;
        Assert.assertTrue(StringUtils.isEmpty(string));
    }

    @Test
    void testIsEmptySpace() {
        String string = " ";
        Assert.assertTrue(StringUtils.isEmpty(string));
    }

    @Test
    void testIsEmptyString() {
        String string = "abc";
        Assert.assertFalse(StringUtils.isEmpty(string));
    }

    @Test
    void testIsEmptyStringWithSpace() {
        String string = " abc ";
        Assert.assertFalse(StringUtils.isEmpty(string));
    }
}
