package developers.pocket.knife.ui.tools.regex;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RegExModelTest {
    private RegExModel subject;

    @Before
    public void before() {
        subject = new RegExModel();
    }

    @Test
    public void testGroups() {
        assertThat(subject.applyRegEx("a(s)*", "as"), is("1. match:as 1. group:s\n"));
    }
}
