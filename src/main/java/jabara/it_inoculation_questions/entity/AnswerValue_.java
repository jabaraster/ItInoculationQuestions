package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-05-11T21:53:20.333+0900")
@StaticMetamodel(AnswerValue.class)
public class AnswerValue_ extends EntityBase_ {
	public static volatile SingularAttribute<AnswerValue, String> value;
	public static volatile ListAttribute<AnswerValue, String> optionText;
}
