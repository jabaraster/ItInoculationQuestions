package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-15T15:05:59.317+0900")
@StaticMetamodel(AnswersSave.class)
public class AnswersSave_ extends EntityBase_ {
	public static volatile SingularAttribute<AnswersSave, String> key;
	public static volatile ListAttribute<AnswersSave, Answer> answers;
}
