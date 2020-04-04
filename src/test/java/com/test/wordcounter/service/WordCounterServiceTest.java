package com.test.wordcounter.service;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.test.wordcounter.externalservice.Translator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WordCounterServiceTest {

    private static final String SENTENCE = "The flower is a pure and beautiful creation of Nature." +
            "flower may be of different sizes, species, colour and " +
            "flower may also have shapes."+
            "flower is offered to Gods and presented to the dear and near ones"+
            "flower that has petals are the parts which produce pollen and seeds";

    public static final String FLOWER_IN_ENGLISH = "flower";
    public static final String FLOR_IN_SPANISH = "flor";
    public static final String BLUME_IN_GERMANY = "blume";

    @Mock
    private Translator translator;

    private WordCounterService toTest;

    @Before
    public void setUp() {
        toTest = new WordCounterServiceImpl(translator);
        when(translator.translate(anyString())).thenAnswer(i -> i.getArguments()[0]);
        toTest.add(SENTENCE);
    }

    @Test
    public void testWordWithZeroOccurrence() {
        assertEquals(0, toTest.counter("SomethingElse"));
    }

    @Test
    public void testWordWithOneOccurrence() {
        assertEquals(1, toTest.counter("colour"));
    }

    @Test
    public void testWordWithTwoOccurrence() {
        assertEquals(2, toTest.counter("is"));
    }

    @Test
    public void testWordAddedInDifferentLanguage() {
        assertEquals(4, toTest.counter("flower"));

        addFlowerInOtherLangs();
        assertFlowerInAllLang(6);
    }

    @Test
    public void testNewLineChars() {
        toTest.add("This\nis\nNewLine test");
        assertEquals(1, toTest.counter("NewLine"));
    }

    @Test
    public void testToIgnorePunctuation() {
        toTest.add("car : carpet as java : javascript!!&@$%^& : javascript");
        assertEquals(2, toTest.counter("javascript"));
    }

    @Test
    public void testToIgnoreNumbers() {
        toTest.add("testing, 1, 2 testing");
        assertEquals(2, toTest.counter("testing"));
        assertEquals(0, toTest.counter("2"));
    }

    @Test
    public void testNormalizeCase() {
        toTest.add("go Go GO Stop stop");
        assertEquals(3, toTest.counter("Go"));
        assertEquals(2, toTest.counter("STOP"));
    }

    @Test
    public void multipleSpacesNotDetectedAsAWord() {
        toTest.add(" multiple   whitespaces    multiple      commas");
        assertEquals(2, toTest.counter("multiple"));
    }


    @Test
    public void testWithQuotations() {
        toTest.add("Rakesh can't tell between 'large' and large.");
        assertEquals(2, toTest.counter("large"));
    }


    private void assertFlowerInAllLang(int counter) {
        assertEquals(counter, toTest.counter(FLOWER_IN_ENGLISH));
        assertEquals(counter, toTest.counter(FLOR_IN_SPANISH));
        assertEquals(counter, toTest.counter(BLUME_IN_GERMANY));
    }

    private void addFlowerInOtherLangs() {
        when(translator.translate(FLOR_IN_SPANISH)).thenReturn(FLOWER_IN_ENGLISH);
        when(translator.translate(BLUME_IN_GERMANY)).thenReturn(FLOWER_IN_ENGLISH);
        toTest.add(FLOR_IN_SPANISH);
        toTest.add(BLUME_IN_GERMANY);
    }

}
