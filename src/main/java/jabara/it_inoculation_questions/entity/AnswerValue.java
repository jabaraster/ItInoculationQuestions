/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author jabaraster
 */
@Embeddable
public class AnswerValue implements Serializable {
    private static final long serialVersionUID           = -2978690585731318151L;

    private static final int  MAX_CHAR_COUNT_VALUE       = 20;
    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_OPTION_TEXT = 1000;

    /**
     * 
     */
    @Column(length = MAX_CHAR_COUNT_VALUE * 3, nullable = false)
    protected String          value;

    /**
     * 
     */
    @Column(length = MAX_CHAR_COUNT_OPTION_TEXT * 3, nullable = true)
    protected String          optionText;

    /**
     * 
     */
    public AnswerValue() {
        // 処理なし
    }

    /**
     * @param pValue -
     */
    public AnswerValue(final String pValue) {
        this.value = pValue;
    }

    /**
     * @param pValue -
     * @param pOptionText -
     */
    public AnswerValue(final String pValue, final String pOptionText) {
        this.value = pValue;
        this.optionText = pOptionText;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnswerValue other = (AnswerValue) obj;
        if (this.optionText == null) {
            if (other.optionText != null) {
                return false;
            }
        } else if (!this.optionText.equals(other.optionText)) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * @return the optionText
     */
    public String getOptionText() {
        return this.optionText;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.optionText == null ? 0 : this.optionText.hashCode());
        result = prime * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    /**
     * @param pOptionText the optionText to set
     */
    public void setOptionText(final String pOptionText) {
        this.optionText = pOptionText;
    }

    /**
     * @param pValue the value to set
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
        return "AnswerValue [value=" + this.value + ", optionText=" + this.optionText + "]";
    }
}
