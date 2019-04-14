package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.grouping.Group;
import seedu.address.model.participant.Birthday;
import seedu.address.model.participant.Email;
import seedu.address.model.participant.Major;
import seedu.address.model.participant.Name;
import seedu.address.model.participant.Participant;
import seedu.address.model.participant.Phone;
import seedu.address.model.participant.Sex;

public class ParticipantListPanelTest extends GuiUnitTest {
    private static final ObservableList<Participant> TYPICAL_PARTICIPANTS =
            FXCollections.observableList(getTypicalPersons());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Participant> selectedPerson = new SimpleObjectProperty<>();
    private PersonListPanelHandle personListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_PARTICIPANTS);

        for (int i = 0; i < TYPICAL_PARTICIPANTS.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_PARTICIPANTS.get(i));
            Participant expectedParticipant = TYPICAL_PARTICIPANTS.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedParticipant, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPersonChanged_selectionChanges() {
        initUi(TYPICAL_PARTICIPANTS);
        Participant secondParticipant = TYPICAL_PARTICIPANTS.get(INDEX_SECOND_PERSON.getZeroBased());
        guiRobot.interact(() -> selectedPerson.set(secondParticipant));
        guiRobot.pauseForHuman();

        PersonCardHandle expectedPerson = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedPerson = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code PersonListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Participant> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of participant cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code PersonListPanel}.
     */
    private ObservableList<Participant> createBackingList(int personCount) {
        ObservableList<Participant> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            Name name = new Name(i + "a");
            Sex sex = new Sex("F");
            Birthday birthday = new Birthday("12122000");
            Phone phone = new Phone("98765433");
            Email email = new Email("a@aa");
            Major major = new Major("cs");
            Group group = new Group("");
            Participant participant = new Participant(name, sex, birthday, phone, email,
                    major, group, Collections.emptySet());
            backingList.add(participant);
        }
        return backingList;
    }

    /**
     * Initializes {@code personListPanelHandle} with a {@code PersonListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code PersonListPanel}.
     */
    private void initUi(ObservableList<Participant> backingList) {
        PersonListPanel personListPanel =
                new PersonListPanel(backingList, selectedPerson, selectedPerson::set);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(personListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}