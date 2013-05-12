package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-05-11T21:47:19.628+0900")
@StaticMetamodel(Answer.class)
public class Answer_ extends EntityBase_ {
	public static volatile SingularAttribute<Answer, Integer> questionIndex;
	public static volatile ListAttribute<Answer, AnswerValue> values;
}
