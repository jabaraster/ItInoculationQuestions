/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * @author jabaraster
 */
@Entity
public class AnswersSave extends EntityBase<AnswersSave> implements Iterable<Answer> {
    private static final long serialVersionUID = 7918875122200802870L;

    /**
     * 
     */
    @Column(nullable = false, unique = true)
    protected String          key;

    /**
     * 
     */
    @OneToMany
    @JoinColumn(name = "answers_save_id")
    @OrderBy("questionIndex")
    protected List<Answer>    answers          = new ArrayList<Answer>();

    /**
     * @param pIndex 位置. 0始まり.
     * @return pIndexの位置にある{@link Answer}.
     */
    public Answer getAnswer(final int pIndex) {
        return this.answers.get(pIndex);
    }

    /**
     * @return 回答の数.
     */
    public int getAnswersCount() {
        return this.answers.size();
    }

    /**
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Answer> iterator() {
        return this.answers.iterator();
    }

    /**
     * @return 新しい{@link Answer}オブジェクト. <br>
     *         indexを設定済み. <br>
     *         コレクションに追加される.
     */
    public Answer newAnswer() {
        final Answer answer = new Answer();
        answer.setQuestionIndex(this.answers.size());
        this.answers.add(answer);
        return answer;
    }

    /**
     * @param pKey the key to set
     */
    public void setKey(final String pKey) {
        this.key = pKey;
    }
}
