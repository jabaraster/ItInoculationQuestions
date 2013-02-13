/**
 * 
 */
package jabara.it_inoculation_questions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jabaraster
 */
@XmlRootElement(name = "questions")
@XmlAccessorType(XmlAccessType.FIELD)
public class Questions implements Serializable, Iterable<Question> {
    private static final long    serialVersionUID = -3075104818637986801L;

    @XmlElement(name = Question.TAG_NAME)
    private final List<Question> questions;

    /**
     * 
     */
    public Questions() {
        this(new ArrayList<Question>());
    }

    /**
     * @param pQuestions 設問一覧.
     */
    public Questions(final List<Question> pQuestions) {
        this.questions = pQuestions;
    }

    /**
     * @param pQuestions 設問一覧.
     */
    public Questions(final Question... pQuestions) {
        this(Arrays.asList(pQuestions));
    }

    /**
     * @return the questions
     */
    public List<Question> getQuestions() {
        return this.questions;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Question> iterator() {
        return this.questions.iterator();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Questions [questions=" + this.questions + "]";
    }
}
