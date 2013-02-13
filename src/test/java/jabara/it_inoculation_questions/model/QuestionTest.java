/**
 * 
 */
package jabara.it_inoculation_questions.model;

import static jabara.it_inoculation_questions.model.QuestionType.SELECT;
import static jabara.it_inoculation_questions.model.QuestionType.TEXT;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;

import org.junit.Test;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "nls", "static-method" })
public class QuestionTest {

    /**
     * 
     */
    @Test
    public void _marshal() {
        final Question q1 = new Question();
        q1.setType(QuestionType.SELECT);
        q1.setMessage("Q1です。");
        q1.getSelections().add(new Selection("1", "A1です。"));
        q1.getSelections().add(new Selection("2", "A2です。"));

        final StringWriter sw = new StringWriter();
        final Questions qs = new Questions( //
                new Question(SELECT, "Q1です。", new Selection("1", "A1です。"), new Selection("2", "A2です。")) //
                , new Question(TEXT, "Q2です。", new Selection("1", "A1です。"), new Selection("2", "A2です。")) //
        );
        JAXB.marshal(qs, sw);
        final String encoded = new String(sw.getBuffer());
        System.out.println(encoded);

        final Questions decoded = JAXB.unmarshal(new StringReader(encoded), Questions.class);

        assertEquals(qs.getQuestions(), decoded.getQuestions());
    }

    /**
     * 
     */
    @Test
    public void _unmarshal() {
        final Questions questions = JAXB.unmarshal(QuestionTest.class.getResource("/questions.xml"), Questions.class);
        for (final Question q : questions) {
            System.out.println(q);
        }
    }
}
