package jabara.it_inoculation_questions.web.ui.component;

import jabara.it_inoculation_questions.model.Selection;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

class SelectionChoiceRenderer implements IChoiceRenderer<Selection> {
    private static final long serialVersionUID = 2100614335551258343L;

    @Override
    public Object getDisplayValue(final Selection pObject) {
        return pObject.getMessage();
    }

    @Override
    public String getIdValue(final Selection pObject, @SuppressWarnings("unused") final int pIndex) {
        return pObject.getValue();
    }

}