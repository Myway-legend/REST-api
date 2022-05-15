package com.deanery.web.component;


import com.deanery.entity.Person;
import com.deanery.repository.PeopleRepository;
import com.deanery.service.GroupService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class PersonEditor extends VerticalLayout implements KeyNotifier {

    private final PeopleRepository peopleRepository;
    private final GroupService groupService;

    private Person person;

    Binder<Person> binder = new Binder<>(Person.class);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField patherName = new TextField("Pather name");
    TextField group = new TextField("Group ID");
    TextField type = new TextField("Type");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    @Setter
    private PersonEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public PersonEditor(PeopleRepository peopleRepository, GroupService groupService) {
        this.peopleRepository = peopleRepository;
        this.groupService = groupService;
        add(firstName, lastName, patherName, group, type, actions);
        binder.bindInstanceFields(this);
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        setVisible(false);
    }

    private void delete() {
        peopleRepository.delete(person);
        changeHandler.onChange();
    }

    private void save() {
        if (!group.getValue().equals("")) {
            person.setGroupId(groupService.readGroupById(Long.parseLong(group.getValue())));
        }
        if (isPersonValid(person)) {
            peopleRepository.save(person);
        }
        changeHandler.onChange();
    }

    public void editPerson(Person person) {
        if (person == null) {
            setVisible(false);
            return;
        }

        if (person.getId() != null) {
            this.person = peopleRepository.findById(person.getId()).orElse(person);
        } else {
            this.person = person;
        }
        binder.setBean(this.person);
        setVisible(true);
    }

    private boolean isPersonValid(Person person) {
        return isEnglishName(person.getFirstName()) && isEnglishName(person.getLastName()) &&
                isEnglishName(person.getPatherName()) && isTypeValid(person.getType()) &&
                isGroupValid(person);
    }

    private boolean isEnglishName(String name) {
        return name.matches("[A-Z][a-z]{0,19}");
    }

    private boolean isTypeValid(String type) {
        return type.equals("S") || type.equals("P");
    }

    private boolean isGroupValid(Person person) {
        if (person.getType().equals("S")) {
            return groupService.readGroupById(person.getGroupId().getId()) != null;
        } else if (person.getType().equals("P")) {
            return person.getGroupId() == null;
        }
        return false;
    }
}
