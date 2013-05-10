package jabara.it_inoculation_questions.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-05-10T23:00:25.211+0900")
@StaticMetamodel(QuestionConfiguration.class)
public class QuestionConfiguration_ extends EntityBase_ {
	public static volatile SingularAttribute<QuestionConfiguration, String> qaName;
	public static volatile SingularAttribute<QuestionConfiguration, byte[]> configurationText;
}
