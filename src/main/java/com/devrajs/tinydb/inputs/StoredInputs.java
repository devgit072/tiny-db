package com.devrajs.tinydb.inputs;

import java.util.ArrayList;
import java.util.List;

public class StoredInputs implements IInputs {
    private List<String> inputs;
    int index;

    public StoredInputs() {
        inputs = new ArrayList<>();
        index = 0;
    }

    @Override
    public String getStringInput() {
        if (index > inputs.size() - 1) {
            throw new IndexOutOfBoundsException(String.format("Index %s is outside of current inputs size", index));
        }
        String val = inputs.get(index);
        index++;
        return val;
    }

    public StoredInputs add(String input) {
        inputs.add(input);
        return this;
    }
}
