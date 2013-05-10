/**
 * 
 */
package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase;

import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author jabaraster
 */
@Entity
public class QuestionConfiguration extends EntityBase<QuestionConfiguration> {
    private static final long   serialVersionUID       = 1131406844915008725L;

    /**
     * 
     */
    public static final Charset ENCODING               = Charset.forName("utf-8"); //$NON-NLS-1$

    private static final int    MAX_CHAR_COUNT_QA_NAME = 100;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_QA_NAME * 3)
    protected String            qaName;

    /**
     * 
     */
    @Column(nullable = false)
    @Lob
    protected byte[]            configurationText;

    /**
     * @return configurationTextを返す.
     */
    public String getConfigurationText() {
        return new String(this.configurationText, ENCODING);
    }

    /**
     * @return qaNameを返す.
     */
    public String getQaName() {
        return this.qaName;
    }

    /**
     * @param pConfigurationText configurationTextを設定.
     */
    public void setConfigurationText(final String pConfigurationText) {
        this.configurationText = pConfigurationText.getBytes(ENCODING);
    }

    /**
     * @param pQaName qaNameを設定.
     */
    public void setQaName(final String pQaName) {
        this.qaName = pQaName;
    }
}
