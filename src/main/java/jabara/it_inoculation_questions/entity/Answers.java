/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * @author jabaraster
 */
@Entity
public class Answers extends EntityBase<Answers> {
    private static final long serialVersionUID = 7918875122200802870L;

    /**
     * 
     */
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "answers_ref")
    protected List<Answer>    answers          = new ArrayList<Answer>();

    /**
     * @param pIndex
     * @return pIndexの位置にある{@link Answer}.
     */
    public Answer getAnswer(final int pIndex) {
        return this.answers.get(pIndex);
    }

    /**
     * @return 新しい{@link Answer}オブジェクト. <br>
     *         indexを設定済み. <br>
     *         コレクションに追加される.
     */
    public Answer newAnswer() {
        final Answer answer = new Answer();
        answer.setIndex(this.answers.size());
        this.answers.add(answer);
        return answer;
    }
}
