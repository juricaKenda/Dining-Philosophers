# Dining Philosophers (Out of this world)

This program represents the classic dining philosophers problem with an extra twist. The dining philosophers problem is an example problem often used in concurrent algorithm design to illustrate synchronization issues and techniques for resolving them.
The philosophers are sitting at a table and all they can ever do is thing or eat. There is a limited amount of chopsticks (which they need to eat) on the table, and each philosopher has a different number of hands (there is the extra twist).
Problem is solved using the monitor functions and mutex locks.
![](/Visuals/DiningPhilosophersVisuals.png)
## Getting Started

To get started with looking into the code and testing this program simply download the "DiningPhilosophers" file in this repository. Along with that you will also need to download (and perhaps reconfigure) the "testFile.txt" file. See more detailed explanations below.

### Prerequisites

None.

```
Standard Java libraries.
```

### Installing

This program is intented to be run and tested in IDE or Command Prompt, and not as an application.

Step one:

```
Download the files (DiningPhilosophers.java and testFile.txt)
```
Step two:

```
Open the DiningPhilosophers.java file and reconfigure the Path to testFile.txt the way it is located in your computer. You will find the Path String in the very constructor of the class.
```
Step three:

```
Open the testFile.txt and reconfigure the numbers.
Text file should be formatted in this manner : 
FIRST_LINE -> numberOfPhilosophers " " number of chopsticks
SECOND_LINE -> numberOfHands1 " " numberOfHands2 " " ... for each philosopher

where " " represents an empty space between the data. 
```
Final step:

```
Run the main class within the DiningPhilosophers file and observe the result. 
```

## Running the tests

There are no specific tests to run for this program other than observing the results and making sure they obey the rules. 
In the display you will se which philosophers are eating at the same time. At the very top of the display is a guide which reminds you how many hands does each philosopher have. If the number of hands currently eating is greater than the number of chopsticks on the table, something is obviously wrong.

## Authors

* **Jurica Kenda** - *Initial work* - [JuricaKenda](https://github.com/juricaKenda)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

