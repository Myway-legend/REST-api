package com.deanery.web.component;

import com.deanery.entity.Mark;
import com.deanery.entity.Person;
import com.deanery.repository.MarksRepository;
import com.deanery.service.PersonService;
import com.deanery.service.SubjectService;
import com.deanery.web.InvalidDataNotification;
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
public class MarkEditor extends VerticalLayout implements KeyNotifier {

    private final MarksRepository marksRepository;
    private final PersonService personService;
    private final SubjectService subjectService;

    private Mark mark;

    Binder<Mark> binder = new Binder<>(Mark.class);
    TextField student = new TextField("Student ID");
    TextField subject = new TextField("Subject ID");
    TextField teacher = new TextField("Teacher ID");
    TextField value = new TextField("Value");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    @Setter
    private MarkEditor.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public MarkEditor(MarksRepository marksRepository, PersonService personService, SubjectService subjectService) {
        this.marksRepository = marksRepository;
        this.personService = personService;
        this.subjectService = subjectService;
        add(student, subject, teacher, value, actions);
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
        marksRepository.delete(mark);
        changeHandler.onChange();
    }

    private void save() {
        if (!student.getValue().equals("")) {
            mark.setStudentId(personService.readPersonById(Long.parseLong(student.getValue())));
        }
        if (!subject.getValue().equals("")) {
            mark.setSubjectId(subjectService.readSubjectById(Long.parseLong(subject.getValue())));
        }
        if (!teacher.getValue().equals("")) {
            mark.setTeacherId(personService.readPersonById(Long.parseLong(teacher.getValue())));
        }

        if (isMarkValid(mark)) {
            marksRepository.save(mark);
        } else {
            InvalidDataNotification.showError("Invalid data");
        }
        changeHandler.onChange();
    }

    public void editMark(Mark mark) {
        if (mark == null) {
            setVisible(false);
            return;
        }

        if (mark.getId() != null) {
            this.mark = marksRepository.findById(mark.getId()).orElse(mark);
        } else {
            this.mark = mark;
        }
        binder.setBean(this.mark);
        setVisible(true);
    }

    private boolean isMarkValid(Mark mark) {
        return isValueValid(mark.getValue()) &&
                isStudentValid(mark.getStudentId().getId()) &&
                isSubjectValid(mark.getSubjectId().getId()) &&
                isTeacherValid(mark.getTeacherId().getId());
    }
    private boolean isValueValid(String value) {
        return Long.parseLong(value) > 1 && Long.parseLong(value) < 6;
    }
    private boolean isStudentValid(Long studentId) throws IllegalArgumentException {
        Person person = personService.readPersonById(studentId);
        return person.getType().equals("S") ;
    }
    private boolean isSubjectValid(Long subjectId) throws IllegalArgumentException {
        subjectService.readSubjectById(subjectId);
        return true;
    }
    private boolean isTeacherValid(Long teacherId) throws IllegalArgumentException {
        Person person = personService.readPersonById(teacherId);
        return person.getType().equals("P");
    }
}
