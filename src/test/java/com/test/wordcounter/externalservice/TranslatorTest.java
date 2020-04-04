package com.test.wordcounter.externalservice;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {

    private Translator translator;

    @Before
    public void setUp() {
        translator = new Translator();
    }

    @Test
    public void testEnglishWord(){
        assertEquals("flower", translator.translate("flower"));
    }
    @Test
    public void testSpanishWord(){
        assertEquals("flower", translator.translate("flor"));
    }
    @Test
    public void testGermanhWord(){
        assertEquals("flower", translator.translate("blume"));
    }
}
