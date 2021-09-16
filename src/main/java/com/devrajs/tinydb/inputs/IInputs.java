package com.devrajs.tinydb.inputs;

/**
 * Interface for query inputs either from code or from the user. For unit test,
 * inputs will be entered by the code. For manual input, input will be entered by the user.
 */
public interface IInputs {
    String getStringInput();
}
