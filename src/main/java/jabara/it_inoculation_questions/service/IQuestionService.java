/**
 * 
 */
package jabara.it_inoculation_questions.service;

import jabara.general.NotFound;
import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.model.TextQuestionFound;
import jabara.it_inoculation_questions.service.impl.QuestionServiceImpl;

import java.io.InputStream;
import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(QuestionServiceImpl.class)
public interface IQuestionService {

    /**
     * @param pQuestionIndex 設問の位置.
     * @return 設問の文言.
     * @throws NotFound 該当の設問が見付からなかった場合.
     */
    String findQuestionMessage(int pQuestionIndex) throws NotFound;

    /**
     * 選択肢のvalue値から、該当選択肢の文言を返します.
     * 
     * @param pQuestionIndex 設問の位置.
     * @param pValue 選択肢のvalue値.
     * @return 選択肢の文言.
     * @throws NotFound 該当選択肢が見付からない場合.
     * @throws TextQuestionFound 回答がテキスト入力による設問だった場合.
     */
    String findSelectionMessage(int pQuestionIndex, String pValue) throws NotFound, TextQuestionFound;

    /**
     * @return -
     */
    String getQaName();

    /**
     * @return 設問一覧.
     */
    List<Question> getQuestions();

    /**
     * @return 設問ファイルが登録済みならtrue.
     */
    boolean isQuestionsRegistered();

    /**
     * @param pQaName -
     * @param pQaXmlData -
     */
    void registerQuestion(String pQaName, InputStream pQaXmlData);
}
