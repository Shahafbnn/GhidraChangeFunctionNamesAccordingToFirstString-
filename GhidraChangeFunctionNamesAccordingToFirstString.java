import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.*;
import ghidra.program.model.symbol.SourceType;

public class ChangeNamesAccordingToFirstString extends GhidraScript {

    @Override
    public void run() throws Exception {
        // Get the function manager for the current program
        FunctionManager functionManager = currentProgram.getFunctionManager();
        // Get an iterator for all functions in the current program
        FunctionIterator functions = functionManager.getFunctions(true);
        // Iterate over all functions
        for (Function function : functions) {
            // Check if the function name starts with "FUN_"
            if (function.getName().startsWith("FUN_")) {
                // Check if the function is a thunk
                if (isThunk(function)) {
                    // If the function is a thunk, rename it by adding the prefix "THUNK_" to its name
                    String newName = "THUNK_" + function.getName();
                    function.setName(newName, SourceType.USER_DEFINED);
                } else {
                    // If the function is not a thunk, get the first String object found within the function
                    String newName = getFirstString(function);
                    // If a String object was found
                    if (newName != null) {
                        // Remove spaces from the String object
                        newName = newName.replace(" ", "");
                        // Remove file extensions from the String object
                        int slashIndex = newName.lastIndexOf('/');
                        if (slashIndex != -1) {
                            newName = newName.substring(slashIndex + 1);
                        }
                        int dotIndex = newName.lastIndexOf('.');
                        if (dotIndex != -1) {
                            newName = newName.substring(0, dotIndex);
                        }
                        // Rename the function based on the modified String object
                        function.setName(newName, SourceType.USER_DEFINED);
                    }
                }
            }
        }
    }

    private boolean isThunk(Function function) throws Exception {
        // Check if the function has any parameters
        if (function.getParameterCount() > 0) {
            return false;
        }
        // Get an iterator for all instructions in the function body
        Listing listing = currentProgram.getListing();
        InstructionIterator instructions = listing.getInstructions(function.getBody(), true);
        int count = 0;
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            count++;
            // Check if there are more than 5 instructions in the function body
            if (count > 5) {
                return false;
            }
            // Check if any of the instructions are jump instructions
            if (instruction.getFlowType().isJump()) {
                return true;
            }
        }
        return false;
    }

    private String getFirstString(Function function) throws Exception {
        Listing listing = currentProgram.getListing();
        // Get an iterator for all instructions in the function body
        InstructionIterator instructions = listing.getInstructions(function.getBody(), true);
        while (instructions.hasNext()) {
            Instruction instruction = instructions.next();
            // Iterate over all operands of each instruction
            for (int i = 0; i < instruction.getNumOperands(); i++) {
                Object[] opObjects = instruction.getOpObjects(i);
                for (Object opObject : opObjects) {
                    // Check if any of the operands are String objects
                    if (opObject instanceof String) {
                        return (String) opObject;
                    }
                }
            }
        }
        // Get an iterator for all data objects in the function body
        DataIterator dataObjects = listing.getDefinedData(function.getBody(), true);
        while (dataObjects.hasNext()) {
            Data dataObject = dataObjects.next();
            // Check if any of the data objects are arrays of characters or wide characters
            if (dataObject.getDataType() instanceof ArrayDataType) {
                ArrayDataType arrayDataType = (ArrayDataType) dataObject.getDataType();
                if (arrayDataType.getDataType() instanceof CharDataType ||
                    arrayDataType.getDataType() instanceof WideCharDataType) {
                    return dataObject.getDefaultValueRepresentation();
                }
            }
        }
        return null;
    }

}
