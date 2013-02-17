/**
 * 
 */
package jabara.it_inoculation_questions.service;

import jabara.general.NotFound;
import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.entity.AnswersSave;
import jabara.it_inoculation_questions.service.impl.AnswerServicesImpl;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(AnswerServicesImpl.class)
public interface IAnswersService {

    /**
     * 回答を確定します.
     * 
     * @param pAnswersSave
     * @return 確定した回答.
     */
    Answers decide(AnswersSave pAnswersSave);

    /**
     * @return 全ての回答.
     */
    List<Answers> getAllAnswers();

    /**
     * @param pId ID値.
     * @return 指定のID値を持つ確定回答.
     * @throws NotFound 該当オブジェクトがない場合.
     */
    Answers getById(long pId) throws NotFound;

    /**
     * @param pAnswersKey 回答のキー. 回答途中の状態を保存するために、ブラウザのCookieに格納される.
     * @param pQuestionsCount 設問の数.
     * @return pAnswersKeyで記録されている回答.
     */
    AnswersSave getSavedByKey(String pAnswersKey, int pQuestionsCount);

    /**
     * @param pAnswer 回答.
     */
    void update(Answer pAnswer);
}
