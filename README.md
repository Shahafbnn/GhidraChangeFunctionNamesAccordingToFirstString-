# GhidraChangeFunctionNamesAccordingToFirstString-
STILL HAS BUGS
Ghidra Function Renaming Script
This repository contains a script for the Ghidra software reverse engineering suite that automatically renames functions in a Ghidra project based on the first String object or wide character array found in each function.

The script iterates over all functions in the current program and checks if their names start with “FUN_”. If a function’s name starts with “FUN_”, the script checks if the function is a thunk. If the function is a thunk, the script renames it by adding the prefix “THUNK_” to its name. If the function is not a thunk, the script searches for the first String object or wide character array in the function. If a String object or wide character array is found, the script renames the function based on it. The script removes spaces and file extensions from the String object or wide character array and uses it as the new name for the function.

This script can be useful for Ghidra users who want to automatically rename functions in their Ghidra projects based on String objects or wide character arrays found within them. The script can save time and effort compared to manually renaming functions one by one.

Usage:

To use this script, simply import it into your Ghidra project and run it. The script will automatically rename functions in your project based on the criteria described above.

Contributing:

Contributions to this script are welcome! If you have any suggestions for improvements or bug fixes, please feel free to open an issue or submit a pull request.
