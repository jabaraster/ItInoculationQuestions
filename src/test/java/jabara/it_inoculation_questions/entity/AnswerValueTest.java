/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
public class AnswerValueTest {

    /**
     * @author jabaraster
     */
    public static class OptionTextIsEmpty {

        private AnswerValue sut;

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void _setOptionText_int_String() {
            this.sut.setOptionText(2, "option"); //$NON-NLS-1$
            final int actual = this.sut.getOptionTextCount();
            assertThat(actual, is(3));
        }

        /**
         * 
         */
        @Before
        public void setUp() {
            this.sut = new AnswerValue();
        }
    }
}
