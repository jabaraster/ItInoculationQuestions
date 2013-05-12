/**
 * 
 */
package jabara.it_inoculation_questions.model;

import jabara.general.ArgUtil;
import jabara.general.Empty;
import jabara.it_inoculation_questions.util.QuestionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jabaraster
 */
@XmlRootElement(name = "question")
@XmlAccessorType(XmlAccessType.FIELD)
public class Question implements Serializable {
    private static final long     serialVersionUID = 4590573559335561709L;

    /**
     * 
     */
    public static final String    TAG_NAME         = "question";                //$NON-NLS-1$

    @XmlAttribute
    private QuestionType          type             = QuestionType.TEXT;

    @XmlAttribute
    private boolean               required         = true;

    private String                message          = "";                        //$NON-NLS-1$

    @XmlAttribute
    private int                   maxChar          = Integer.MAX_VALUE;

    @XmlElement(name = Selection.TAG_NAME)
    private final List<Selection> selections       = new ArrayList<Selection>();

    /**
     * 
     */
    public Question() {
        //
    }

    /**
     * @param pType 設問の回答タイプ.
     * @param pMessage 設問文.
     * @param pSelections 回答の選択肢一覧.
     */
    public Question(final QuestionType pType, final String pMessage, final Selection... pSelections) {
        setType(pType);
        setMessage(pMessage);
        this.selections.addAll(Arrays.asList(pSelections));
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
        final Question other = (Question) obj;
        if (this.maxChar != other.maxChar) {
            return false;
        }
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!this.message.equals(other.message)) {
            return false;
        }
        if (this.selections == null) {
            if (other.selections != null) {
                return false;
            }
        } else if (!this.selections.equals(other.selections)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    /**
     * @return typeが{@link QuestionType#TEXT}及び{@link QuestionType#TEXTAREA}の時の、入力欄の前後に付与する文字列を返します.
     */
    public TextAnswerColumn getAnswerColumnForText() {
        if (!(this.type == QuestionType.TEXT || this.type == QuestionType.TEXTAREA)) {
            throw new IllegalStateException("タイプが " + this.type + " のときにこのメソッドを呼び出してはいけません."); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (this.selections.isEmpty()) {
            return TextAnswerColumn.EMPTY;
        }
        return QuestionUtil.parseTextAnswerColumn(this.selections.get(0).getLabel());
    }

    /**
     * @return the maxChar
     */
    public int getMaxChar() {
        return this.maxChar;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return 選択肢一覧.
     */
    public List<Selection> getSelections() {
        return this.selections;
    }

    /**
     * @return the type
     */
    public QuestionType getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.maxChar;
        result = prime * result + (this.message == null ? 0 : this.message.hashCode());
        result = prime * result + (this.selections == null ? 0 : this.selections.hashCode());
        result = prime * result + (this.type == null ? 0 : this.type.hashCode());
        return result;
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * @param pMaxChar the maxChar to set
     */
    public void setMaxChar(final int pMaxChar) {
        this.maxChar = pMaxChar;
    }

    /**
     * @param pMessage the message to set
     */
    public void setMessage(final String pMessage) {
        this.message = pMessage == null ? Empty.STRING : pMessage;
    }

    /**
     * @param pRequired the required to set
     */
    public void setRequired(final boolean pRequired) {
        this.required = pRequired;
    }

    /**
     * @param pType the type to set
     */
    public void setType(final QuestionType pType) {
        ArgUtil.checkNull(pType, "pType"); //$NON-NLS-1$
        this.type = pType;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Question [type=" + this.type + ", required=" + this.required + ", message=" + this.message + ", maxChar=" + this.maxChar
                + ", selections=" + this.selections + "]";
    }
}
