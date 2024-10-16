package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        ClearCommand command = new TestClearCommand(true);
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        ClearCommand command = new TestClearCommand(true);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }


    @Test
    public void execute_nonEmptyAddressBook_cancel() {
        ClearCommand command = new TestClearCommand(false);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CANCEL, expectedModel);
    }

    /**
     * A helper class to simulate user confirmation input,
     * avoiding the need for actual GUI interaction during testing.
     */
    private static class TestClearCommand extends ClearCommand {
        private final boolean confirmClear;

        public TestClearCommand(boolean confirmClear) {
            this.confirmClear = confirmClear;
        }

        @Override
        public CommandResult execute(Model model) {
            requireNonNull(model);

            if (confirmClear) {
                model.setAddressBook(new AddressBook());
                return new CommandResult(MESSAGE_SUCCESS);
            } else {
                return new CommandResult(MESSAGE_CANCEL);
            }
        }
    }

}
