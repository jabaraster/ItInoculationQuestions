/**
 * 
 */
package jabara.it_inoculation_questions.service;

import jabara.it_inoculation_questions.model.Question;
import jabara.it_inoculation_questions.service.impl.QuestionServiceImpl;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(QuestionServiceImpl.class)
public interface IQuestionService {

    /**
     * @return 設問一覧.
     */
    List<Question> getQuestions();
}
