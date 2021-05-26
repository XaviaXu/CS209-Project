# CS209 Project OO Design Doc

**Project  Group**: 16

**Team members**: Li Yuanzhao, Xu Xinyu, Wu Shangxuan, Zhang Jiayi, Ren Yiwei, Jiang Yuchen



## Project Design

![UML-CS209 (2)](design-doc.assets/UML-CS209 (2).png)



## Object oriented design

There are some details we want to explain to the UML graph.

### Status classes

The status classes are the class for store the runtime status. There are three status classes: `SquareProblem`, `MS`, and `SK`. 

#### SquareProblem

`SquareProblem` is an **abstract class**, which has the basic variables and methods for status storing and generating. 

#### MS

`MS` is a class for magic square status storing. MS is extends the `SquareProblem` class, and there are also some variables and methods for magic square problem. 

#### SK

`SK` is a class for sudoku status storing. `SK` is also extends the `SquareProblem`, but it is simpler than `MS`. The new variables and methods in `SK` is similar to `MS`. 



### Mutate functions

Our solver will use many mutate functions. To call the mutate functions more easier, we design a **interface** `MutateNew`, and all the mutate functions are wrapped by a class, such as `MutateNewS0`, `MutateNewS11`,  and implement the `MutateNew`. All the mutate functions will receive a MS object as parameter and return whether the new square be accept. 



## Solver

For two solver, the algorithm we used in sudoku is a simple simulate annealing algorithm, and we use a hyper-heuristic method for magic square. 

### Magic Square

#### Simulate Annealing

For the magic square solver, we implement a simulate annealing algorithm with hyper-heuristic solver. The simulate annealing part is strengthen by *multiple try*. The pseudocode shows there

```pseudocode
t = Ts
ms = initial Sudoku
while t > Te && cost(sk) > 0:
	newMs = ms.swapCells()
	if cost(newMs) < cost(ms):
		ms = newMs
    else:
    	accept newMs in probability = exp((cost(ms) - cost(newMs)) / t)
    if cost(ms) == 0:
    	stop and print results
    t = t * D
```

Where `Ts` is initial temperature, `Te` is ending temperature. `D` is delta, which can control the speed of annealing. S is times of multiple try. 

#### Hyper-heuristic

hyper heuristic is a thought to combined multiple heuristic function. There are two parts: high level heuristic (HLH) and low level heuristic (LLH)

##### HLH

The HLH we used is Random Permutation (RP). RP will generate a random permutation, then select the LLH function in the order of generated permutation. The pseudocode is

```pseudocode
candiList = get random permutation

void hlh(MS ms):
	i = candiList.getNext()
	mutateList.get(i).mutate(ms)
```

##### LLH

LLHs is a set of mutate functions, in our solver design, there are 5 LLHs under the HLH's control. We plot a row or column to gray when the sum of this row/column is not correct, and plot the crossing cell of a gray row and a gray column to black. And let S1 be the set of dark cells, S2 is the set of gray and dark cells.

<img src="empirical-doc.assets/image-20210526012303547.png" alt="image-20210526012303547" style="zoom: 80%;" />

The 5 LLH are

| LLH function | Describe                                    |
| ------------ | ------------------------------------------- |
| S0           | Swap two cells randomly                     |
| S11          | Swap a cell in S1 and a cell in S2 randomly |
| S12          | Swap a cell in S2 and a cell in S2 randomly |
| S13          | Swap a cell in S2 and another cell randomly |
| S21          | Swap two rows or two columns randomly       |

### Sudoku

Sudoku is simpler than MagicSquare, the algorithm is a simple simulate annealing.

#### Simulate Annealing

For the magic square solver, we implement a simulate annealing algorithm with hyper-heuristic solver. The simulate annealing part is strengthen by *multiple try*. The pseudocode shows there

```pseudocode
t = Ts
sk = initial Sudoku
while t > Te && cost(sk) > 0:
	newSk = sk.swapCells()
	if cost(newSk) < cost(sk):
		sk = newSk
    else:
    	accept newSk in probability = exp((cost(sk) - cost(newSk)) / t)
    if cost(sk) == 0:
    	stop and print results
    t = t * D
```

Where `Ts` is initial temperature, `Te` is ending temperature. `D` is delta, which can control the speed of annealing. S is times of multiple try. 

## GUI Design

GUI is consist of three views: `RootWindow`, `Sudoku`, and `MagicSquare`

### RootWindow

In the `RootWindow`, user can left click to the buttons to choose to enter the `MagicSquare` or `Sudoku`

<img src="design-doc.assets/image-20210526000140423.png" alt="image-20210526000140423" style="zoom: 67%;" />



### MagicSquare

In `MagicSquare` view, user can drag to change the size of square. User can play the game using left mouse button, which can select and exchange numbers in two cells, also color cells pink. Right mouse button can set a cell as constraint and color the cell yellow. The `pause` and `continue` during the runtime are supported.

![image-20210526000248795](design-doc.assets/image-20210526000248795.png)

`File` tool in menu bar can save and load the square.

![image-20210526010237757](design-doc.assets\SL_MS.png)

### Sudoku

In `Sudoku` view, user can set the value of each cell, or just select a preset square to start a game. User can feel free to input or delete numbers after initialization to play the game. If there are duplicate number in the same row, column or diagonal, these cells will turns to red.  The `pause` and `continue` during the runtime are supported.

![image-20210526000256468](design-doc.assets/image-20210526000256468.png)

`File` tool in menu bar can save and load the square.

![image-20210526010237757](design-doc.assets\SL_sk.png)