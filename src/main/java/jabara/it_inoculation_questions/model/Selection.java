/**
 * 
 */
package jabara.it_inoculation_questions.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

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
    public static final String TAG_NAME         = "selection";          //$NON-NLS-1$

    @XmlAttribute
    private String             value;

    @XmlValue
    private String             message;

    @XmlAttribute
    private boolean            other            = false;

    /**
     * 
     */
    public Selection() {
    }

    /**
     * @param pValue HTML上での値
     * @param pMessage 文言
     */
    public Selection(final String pValue, final String pMessage) {
        this.value = pValue;
        this.message = pMessage;
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
        if (this.message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!this.message.equals(other.message)) {
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
     * @return the message
     */
    public String getMessage() {
        return this.message;
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
        result = prime * result + (this.message == null ? 0 : this.message.hashCode());
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
     * @param pMessage the message to set
     */
    public void setMessage(final String pMessage) {
        this.message = pMessage;
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
        return "Selection [value=" + this.value + ", message=" + this.message + "]";
    }
}
