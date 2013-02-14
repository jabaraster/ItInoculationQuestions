/**
 * 
 */
package jabara.it_inoculation_questions.service;

import jabara.it_inoculation_questions.entity.Answer;
import jabara.it_inoculation_questions.entity.Answers;
import jabara.it_inoculation_questions.service.impl.AnswerServicesImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(AnswerServicesImpl.class)
public interface IAnswersService {

    /**
     * @param pAnswers
     */
    void insertOrUpdate(Answers pAnswers);

    /**
     * @param pAnswer
     */
    void update(Answer pAnswer);
}
