## BuiltInExtension

### `debug(bool)`
- Description: This is used to enable or disable debug mode.
- Parameter - Boolean (`bool`): True to enable debug mode, false to disable debug mode.
- Example:
```kotlin
debug(true);
```

### `eval(code)`
- Description: This is used to evaluate a string as code.
This will not inherit imports that are in the parent script.
- Parameter - String (`code`): The code to evaluate.
- Returns - Object: The result of the evaluation.
- Example:
```kotlin
eval('1 + 1');
```

### `experimental(bool)`
- Description: This is used to enable or disable experimental mode.
- Parameter - Boolean (`bool`): True to enable experimental mode, false to disable experimental mode.
- Example:
```kotlin
experimental(true);
```

### `getArucasVersion()`
- Description: This is used to get the version of Arucas that is currently running.
- Returns - String: The version of Arucas that is currently running.
- Example:
```kotlin
getArucasVersion();
```

### `getDate()`
- Description: This is used to get the current date formatted with dd/MM/yyyy in your local time.
- Returns - String: The current date formatted with dd/MM/yyyy.
- Example:
```kotlin
getDate();
```

### `getMilliTime()`
- Description: This is used to get the current time in milliseconds.
- Returns - Number: The current time in milliseconds.
- Example:
```kotlin
getMilliTime();
```

### `getNanoTime()`
- Description: This is used to get the current time in nanoseconds.
- Returns - Number: The current time in nanoseconds.
- Example:
```kotlin
getNanoTime();
```

### `getTime()`
- Description: This is used to get the current time formatted with HH:mm:ss in your local time.
- Returns - String: The current time formatted with HH:mm:ss.
- Example:
```kotlin
getTime();
```

### `getUnixTime()`
- Description: This is used to get the current time in seconds since the Unix epoch.
- Returns - Number: The current time in seconds since the Unix epoch.
- Example:
```kotlin
getUnixTime();
```

### `input(prompt)`
- Description: This is used to take an input from the user. The execution of
the script is paused until the user has inputted a value.
- Parameter - String (`prompt`): The prompt to show the user.
- Returns - String: The input from the user.
- Example:
```kotlin
name = input('What is your name?');
```

### `isDebug()`
- Description: This is used to determine whether the interpreter is in debug mode.
- Example:
```kotlin
isDebug();
```

### `isExperimental()`
- Description: This is used to determine whether the interpreter is in experimental mode.
- Example:
```kotlin
isExperimental();
```

### `isMain()`
- Description: This is used to check whether the script is the main script.
- Returns - Boolean: True if the script is the main script, false if it is not.
- Example:
```kotlin
isMain();
```

### `len(sizable)`
- Description: This is used to get the length of a collection or string.
- Parameter - String (`sizable`): The collection or string.
- Example:
```kotlin
len("Hello World");
```

### `print(value)`
- Description: This prints a value to the output handler.
- Parameter - Object (`value`): The value to print.
- Example:
```kotlin
print('Hello World');
```

### `print(value...)`
- Description: This prints a number of values to the console.
If there are no arguments then this will print a new line,
other wise it will print the contents without a new line.
- Parameter - Object (`value`): The value to print.
- Example:
```kotlin
print('Hello World', 'This is a test', 123); // prints 'Hello WorldThis is a test123'
```

### `printDebug(value)`
- Description: This logs something to the debug output.
It only prints if debug mode is enabled: `debug(true)`.
- Parameter - Object (`value`): The value to print.
- Example:
```kotlin
debug(true); // Enable debug for testing
if (true) {
    printDebug("Inside if statement");
}
```

### `random(bound)`
- Description: This is used to generate a random integer between 0 and the bound.
- Parameter - Number (`bound`): The maximum bound (exclusive).
- Returns - Number: The random integer.
- Example:
```kotlin
random(10);
```

### `range(bound)`
- Description: This is used to generate a range of integers starting from 0, incrementing by 1.
- Parameter - Number (`bound`): The maximum bound (exclusive).
- Returns - Iterable: An iterable object that returns the range of integers.
- Example:
```kotlin
range(10);
```

### `range(start, bound)`
- Description: This is used to generate a range of numbers starting
from a start value and ending at a bound value incrementing by 1.
- Parameters:
  - Number (`start`): The start value.
  - Number (`bound`): The maximum bound (exclusive).
- Returns - Iterable: An iterable object that returns the range of integers.
- Example:
```kotlin
range(0, 10);
```

### `range(start, bound, step)`
- Description: This is used to generate a range of numbers starting from a
start value and ending at a bound value incrementing by a step value.
- Parameters:
  - Number (`start`): The start value.
  - Number (`bound`): The maximum bound (exclusive).
  - Number (`step`): The step value.
- Returns - Iterable: An iterable object that returns the range of integers.
- Example:
```kotlin
range(0, 10, 2);
```

### `run(path)`
- Description: This is used to run a .arucas file, you can use on script to run other scripts.
- Parameter - String (`path`): As a file path.
- Returns - Object: Any value that the file returns.
- Example:
```kotlin
run('/home/user/script.arucas');
```

### `runFromString(code)`
- Deprecated: This should be replaced with 'eval(code)'
- Description: This is used to evaluate a string as code.
This will not inherit imports that are in the parent script.
- Parameter - String (`code`): The code to run.
- Example:
```kotlin
runFromString('print("Hello World");');
```

### `sleep(milliseconds)`
- Description: This pauses your program for a certain amount of milliseconds.
- Parameter - Number (`milliseconds`): The number of milliseconds to sleep.
- Example:
```kotlin
sleep(1000);
```

### `stop()`
- Description: This is used to stop a script.
- Example:
```kotlin
stop();
```

### `suppressDeprecated(bool)`
- Description: This is used to enable or disable suppressing deprecation warnings.
- Parameter - Boolean (`bool`): True to enable, false to disable warnings.
- Example:
```kotlin
suppressDeprecated(true);
```
## ShaderExtension

### `area(consumer)`
- Description: Used to iterate over an area specified by the player.
This can be done with `/shadertoy area pos1 (<x> <y> <z>)?` and `/shadertoy area pos2 (<x> <y> <z>)?`,
or `/shadertoy area origin <x> <y> <z> size <sX> <sY> <sZ>`.
- Parameter - Function (`consumer`): This is the lambda function that gets iterated over the specified area. It takes 1-3 Vector3 parameters:
 1. Absolute coordinates of the block in the world.
 2. Normalized coordinates within the area (from -1, -1, -1 to 1, 1, 1).
 3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ).
.
- Example:
```kotlin
area(fun (aPos, nPos, lPos) {
    // Do something...
});
```

### `area(origin, size, consumer)`
- Description: Used to iterate over an area specified by an origin and size.
- Parameters:
  - Vector3 (`origin`): The origin vector.
  - Vector3 (`size`): The size vector.
  - Function (`consumer`): This is the lambda function that gets iterated over the specified area. It takes 1-3 Vector3 parameters:
 1. Absolute coordinates of the block in the world.
 2. Normalized coordinates within the area (from -1, -1, -1 to 1, 1, 1).
 3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ).
.
- Example:
```kotlin
            origin = new Vector3(0,0,0);
            size = new Vector3(10,10,10);
area(origin, size, fun(aPos, nPos, lPos) {
    // Do something...
});
```

### `area(originX, originY, originZ, sizeX, sizeY, sizeZ, consumer)`
- Description: Used to iterate over an area specified by an origin and size.
- Parameters:
  - Number (`originX`): The origin x coordinate.
  - Number (`originY`): The origin y coordinate.
  - Number (`originZ`): The origin z coordinate.
  - Number (`sizeX`): The size in x axis.
  - Number (`sizeY`): The size in y axis.
  - Number (`sizeZ`): The size in z axis.
  - Function (`consumer`): This is the lambda function that gets iterated over the specified area. It takes 1-3 Vector3 parameters:
 1. Absolute coordinates of the block in the world.
 2. Normalized coordinates within the area (from -1, -1, -1 to 1, 1, 1).
 3. Local area coordinates (0, 0, 0 at area origin and goes up to sizeX, sizeY, sizeZ).
.
- Example:
```kotlin
area(100, 100, 100, 200, 1, 200, fun(aPos, nPos, lPos) {
    // Do something...
});
```

### `dimension()`
- Description: Returns string for your current dimension.
For example 'minecraft:the_nether'.
- Returns - String: String representing your current dimension.
- Example:
```kotlin
world = dimension();
```

### `place(args...)`
- Description: This function allows you to place a block in a given world.
The parameters for this function are as follows:

position - this can either be as 3 numbers (x, y, z) or as a single Vector3

block - this is the same format you would use for a setblock command

dimension - this is an optional argument defining the dimension in which to place the block
by default this is the dimension of the player that executed the script.
.
- Parameter - Object (`args`): The placement arguments, see the function description.
- Examples:
```kotlin
place(0, 0, 0, 'minecraft:oak_planks');
```
```kotlin
place(new Vector3(0, 0, 0), 'oak_planks', 'minecraft:the_nether');
```
```kotlin
place(100, 64, 10, 'oak_sign[rotation = 4]{Text1: "Example"}', 'overworld');
```

### `query(args...)`
- Description: This queries a the data for a block at a given position in a given dimension.
The parameters for this function are as follows:

position - this can either be as 3 numbers (x, y, z) or as a single Vector3

type - this is optional, this is the type of query, this can be one of the following: 'default', 'block', 'state', 'nbt', see examples

dimension - this is an optional argument defining the dimension in which to place the block
by default this is the dimension of the player that executed the script.
- Parameter - Object (`args`): The query arguments, see function description.
- Returns - String: The return value depends on the type parameter:
 "default"- returns a string containing all the info
 "block"- returns a string describing the block
 "state"- returns a map containing the state info
 "nbt"- returns a map containing all the nbt data.
- Examples:
```kotlin
query(10, 0, 10); // -> 'minecraft:chest[facing=west,type=single,waterlogged=false]{Items:[{Count:64b,Slot:11b,id:"minecraft:spruce_fence_gate"},{Count:1b,Slot:14b,id:"minecraft:diamond_chestplate",tag:{Damage:0,Enchantments:[{id:"minecraft:protection",lvl:1s}],RepairCost:1,display:{Name:'{"text":"Why Are You Reading This?"}'}}}]}'
```
```kotlin
query(10, 0, 10,"block"); // -> 'minecraft:chest'
```
```kotlin
query(10, 0, 10,"state"); // -> {facing: west, type: single, waterlogged: false}
```
```kotlin
query(10, 0, 10,"nbt"); // -> {Items:[{Count:64, Slot:11, id:"minecraft:spruce_fence_gate"},{Count:1, Slot:14, id:"minecraft:diamond_chestplate", tag:{Damage:0,Enchantments:[{id:"minecraft:protection",lvl: 1}],RepairCost:1,display:{Name:'{"text":"Why Are You Reading This?"}'}}}]}
```
