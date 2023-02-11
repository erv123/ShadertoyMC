# ShadertoyMC

This minecraft mod allows writing scripts in Arucas (https://github.com/senseiwells/Arucas) with the goal of placing blocks based on math.

The working logic is similar to how https://www.shadertoy.com/ works: an expression is calculated for each block with block coordinates as inputs and as a result of this expression a block is placed at the coordinates

FabricAPI required

## Easy start

### Step 1
Initialize area with `/shadertoy area pos1 pos2`

### Step 2
Create a new script with `/shadertoy new name`
This file will contain a working script and can be run immediatelly

### Step 3
Open and edit the file by clicking on the text (VSCode with Arucas extension reccomended)

### Step 4
Run the script with `/shadertoy run name`
