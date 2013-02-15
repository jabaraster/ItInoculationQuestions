package jabara.it_inoculation_questions.model;

import jabara.general.Empty;

import java.io.Serializable;

/**
 * @author jabaraster
 */
public class TextAnswerColumn implements Serializable {
    private static final long            serialVersionUID = -7888860250496873577L;

    /**
     * 
     */
    public static final TextAnswerColumn EMPTY            = new TextAnswerColumn(Empty.STRING, Empty.STRING);

    private final String                 valuePrefix;
    private final String                 valueSuffix;

    /**
     * @param pValuePrefix 入力欄の前の文字列.
     * @param pValueSuffix 入力欄の後ろの文字列.
     */
    public TextAnswerColumn(final String pValuePrefix, final String pValueSuffix) {
        this.valuePrefix = pValuePrefix;
        this.valueSuffix = pValueSuffix;
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
        final TextAnswerColumn other = (TextAnswerColumn) obj;
        if (this.valuePrefix == null) {
            if (other.valuePrefix != null) {
                return false;
            }
        } else if (!this.valuePrefix.equals(other.valuePrefix)) {
            return false;
        }
        if (this.valueSuffix == null) {
            if (other.valueSuffix != null) {
                return false;
            }
        } else if (!this.valueSuffix.equals(other.valueSuffix)) {
            return false;
        }
        return true;
    }

    /**
     * @return the valuePrefix
     */
    public String getValuePrefix() {
        return this.valuePrefix;
    }

    /**
     * @return the valueSuffix
     */
    public String getValueSuffix() {
        return this.valueSuffix;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.valuePrefix == null ? 0 : this.valuePrefix.hashCode());
        result = prime * result + (this.valueSuffix == null ? 0 : this.valueSuffix.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "TextAnswerColumn [valuePrefix=" + this.valuePrefix + ", valueSuffix=" + this.valueSuffix + "]";
    }

}