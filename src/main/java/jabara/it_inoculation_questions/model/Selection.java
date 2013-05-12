/**
 * 
 */
package jabara.it_inoculation_questions.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jabaraster
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Selection implements Serializable {
    private static final long  serialVersionUID = -7524435082123192924L;

    /**
     * 
     */
    public static final String TAG_NAME         = "selection";            //$NON-NLS-1$

    @XmlAttribute
    private String             value;

    @XmlElement
    private String             label;

    @XmlAttribute
    private boolean            other            = false;

    @XmlElement(name = "option")
    private final List<String> optionTexts      = new ArrayList<String>();

    /**
     * 
     */
    public Selection() {
        // 処理なし
    }

    /**
     * @param pValue HTML上での値
     * @param pMessage 文言
     */
    public Selection(final String pValue, final String pMessage) {
        this.value = pValue;
        this.label = pMessage;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("hiding")
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
        final Selection other = (Selection) obj;
        if (this.label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!this.label.equals(other.label)) {
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
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @return optionTextsを返す.
     */
    public List<String> getOptionTexts() {
        return this.optionTexts;
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
        result = prime * result + (this.label == null ? 0 : this.label.hashCode());
        result = prime * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    /**
     * @return the other
     */
    public boolean isOther() {
        return this.other;
    }

    /**
     * @param pMessage the label to set
     */
    public void setLabel(final String pMessage) {
        this.label = pMessage;
    }

    /**
     * @param pOther the other to set
     */
    public void setOther(final boolean pOther) {
        this.other = pOther;
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
        return "Selection [value=" + this.value + ", label=" + this.label + "]";
    }
}
