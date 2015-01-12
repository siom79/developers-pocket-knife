package developers.pocket.knife.ui.tools.regex;

import org.junit.Before;
import org.junit.Ignore;
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
        assertThat(subject.applyRegEx("([a-zA-Z0-9]+)\\.(html)", "http://www.softwerkskammer.org/index.html").trim(), is("http://www.softwerkskammer.org/index.html\n\t1. match:index.html\n\t\t1. group:index\n\t\t2. group:html".trim()));
    }
}
