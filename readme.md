# Mars Rover
## Description
Interpretation of the Mars Rover Challenge using Swing and JavaFX3D.

## Getting Started
### Executing program

* Run the project's Main Class via your IDE (no binaries yet)

### Starting Up
#### - Via Swing Interface
* Enter your preferred plateau size (in coordinates)
* Enter the desired coordinates for your rover
* Choose a facing for your rover
* Click 'Generate Plateau' to begin

#### - Via Terminal
* Close the startup Swing interface and click 'Yes' in the dialogue box that appears
* Follow the instructions printed to the terminal to proceed, using the following format for inputs:
```
Plateau coordinates: "<int> <int>" (representing max x and z coordinates, respectively)
Rover coordinates: "<int> <int> <N/E/S/W>" (representing the rover's starting x and z coordinates, respectively, followed by its initial facing)
```
### Camera Control
* Left-click + drag: Multi-axis control, currently pivoting on a static, unchangeable point
* Middle-click + drag: Lateral movement
* Right-click + drag:  Zoom in = drag down ; Zoom out = drag up
  

  (Multipliers: Hold SHIFT to increase movement speed, CTRL to decrease)

### Rover Control
####Keyboard
* M: Move forward one grid space
* B: Move backward one grid space
* R: Turn right 90°
* L: Turn left 90°
####Terminal
The above characters can be used to send their respective instructions to the rover.
The rover will execute a string of consecutive characters' instructions in turn.


## Author
ex. [ChrixWX92](https://github.com/ChrixWX92)

## Version History

* 0.1
    * Initial Release

