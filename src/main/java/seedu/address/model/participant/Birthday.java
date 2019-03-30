package seedu.address.model.participant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's birthday in the address book
 */
public class Birthday {

    public static final String MESSAGE_CONSTRAINTS =
            "Birthday should contain eight numbers in the format of DDMMYYYY";
    public static final String VALIDATION_REGEX = "\\d{8,}";
    public final String value;

    public Birthday(String birthday) {
        requireNonNull(birthday);
        checkArgument(isValidBirthday(birthday), MESSAGE_CONSTRAINTS);
        value = birthday;
    }

    /**
     * Return true if a given string is a valid sex
     */
    public static boolean isValidBirthday(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public int getYear() {
        return Integer.parseInt(value.substring(4));
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && value.equals(((Birthday) other).value)); // state check
    }
}