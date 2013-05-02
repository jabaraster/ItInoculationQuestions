/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

/**
 * @author jabaraster
 */
@Entity
public class Answer extends EntityBase<Answer> {
    private static final long serialVersionUID     = 4438634181403267449L;

    private static final int  MAX_CHAR_COUNT_VALUE = 1000;

    /**
     * この回答が、何番目の設問に対する回答かを0始まりのインデックス値で保持します.
     */
    @Column(nullable = false)
    protected int             questionIndex;

    /**
     * 回答内容を表す文字列です.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    protected List<String>    values               = new ArrayList<String>();

    /**
     * 
     */
    public Answer() {
        //
    }

    /**
     * @param pQuestionIndex 何番目の設問に対する回答かを0始まりのインデックスで指定.
     */
    public Answer(final int pQuestionIndex) {
        this.questionIndex = pQuestionIndex;
    }

    /**
     * @param pQuestionIndex 何番目の設問に対する回答かを0始まりのインデックスで指定.
     * @param pValues 回答内容.
     */
    public Answer(final int pQuestionIndex, final List<String> pValues) {
        setQuestionIndex(pQuestionIndex);
        this.values.addAll(pValues);
    }

    /**
     * @return indexを返す.
     */
    public int getQuestionIndex() {
        return this.questionIndex;
    }

    /**
     * @return the values
     */
    public List<String> getValues() {
        return this.values;
    }

    /**
     * @param pIndex indexを設定.
     */
    public void setQuestionIndex(final int pIndex) {
        this.questionIndex = pIndex;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Answer [questionIndex=" + this.questionIndex + ", values=" + this.values + ", id=" + this.id + ", created=" + this.created
                + ", updated=" + this.updated + "]";
    }

    /**
     * @return the maxCharCountValue
     */
    public static int getMaxCharCountValue() {
        return MAX_CHAR_COUNT_VALUE;
    }
}
