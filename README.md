# ShadertoyMC
CURRENTLY THIS IS A SINGLEPLAYER ONLY MOD

This minecraft mod allows writing scripts in [Arucas](https://github.com/senseiwells/Arucas) with the goal of placing
blocks based on math.

The working logic is similar to how [shadertoy](https://www.shadertoy.com/) works: an expression is calculated for each
block with
block coordinates as inputs and as a result of this expression a block is placed at the coordinates.

FabricAPI required

## Easy start

### Step 1

Create a new script with `/shadertoy new <name>`

This file will contain a working script and can be run immediately, but the area on operation is defined within the file
and might not match your desired are.

### Step 2

Set the area with one of the
following:  `/area pos1 <pos>` + `/area pos2 <pos>`, `/area origin <origin> size <sizeX> <sizeY> <sizeZ>`, `/area <pos1> <pos2>`.

### Step 3

The file can be located in `.minecraft/config/shadertoy`. Recommended text editor: VSCode with Arucas extension
recommended.

Files can also be opened with `/shadertoy open <name>`.

### Step 4

Run the script with `/shadertoy run <name>`.
