/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * @author jabaraster
 */
@Entity
public class Answers extends EntityBase<Answers> implements Iterable<Answer> {
    private static final long serialVersionUID = 366102388187764739L;

    /**
     * 
     */
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "answers_id")
    @OrderBy("questionIndex")
    protected List<Answer>    answers          = new ArrayList<Answer>();

    /**
     * @param pQuestionIndex 何番目の設問に対する回答かを0始まりのインデックスで指定.
     * @param pValues 回答内容を表す文字列.
     * @return 追加した回答.
     */
    public Answer addAnswer(final int pQuestionIndex, final List<AnswerValue> pValues) {
        final Answer a = new Answer(pQuestionIndex, pValues);
        this.answers.add(a);
        return a;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Answer> iterator() {
        return this.answers.iterator();
    }
}
