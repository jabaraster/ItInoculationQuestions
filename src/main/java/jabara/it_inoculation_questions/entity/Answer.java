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
    private static final long serialVersionUID = 4438634181403267449L;

    /**
     * 
     */
    @Column(nullable = false)
    protected int             index;

    /**
     * 
     */
    @Column(nullable = true)
    protected String          value;

    /**
     * 
     */
    public Answer() {
        //
    }

    /**
     * @param pIndex
     */
    public Answer(final int pIndex) {
        this.index = pIndex;
    }

    /**
     * @return indexを返す.
     */
    public int getIndex() {
        return this.index;
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
    public void setIndex(final int pIndex) {
        this.index = pIndex;
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
        return "Answer [index=" + this.index + ", value=" + this.value + ", id=" + this.id + ", created=" + this.created + ", updated="
                + this.updated + "]";
    }
}
