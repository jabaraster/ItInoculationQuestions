/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;

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
    @Column(nullable = true, length = MAX_CHAR_COUNT_VALUE * 3)
    protected String          value;

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
     * @param pValue 回答内容を表す文字列.
     */
    public Answer(final int pQuestionIndex, final String pValue) {
        setQuestionIndex(pQuestionIndex);
        setValue(pValue);
    }

    /**
     * @return indexを返す.
     */
    public int getQuestionIndex() {
        return this.questionIndex;
    }

    /**
     * @return valueを返す.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param pIndex indexを設定.
     */
    public void setQuestionIndex(final int pIndex) {
        this.questionIndex = pIndex;
    }

    /**
     * @param pValue valueを設定.
     */
    public void setValue(final String pValue) {
        this.value = pValue;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Answer [questionIndex=" + this.questionIndex + ", value=" + this.value + ", id=" + this.id + ", created=" + this.created
                + ", updated=" + this.updated + "]";
    }
}
