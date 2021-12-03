<h1 align = "center"> Concurrency Assignment 3 by Rustam Talibzade </h1>
For this assignment I was asked to write a program where in a picture, abox of (given size x given size) should be changed to the average color.
We were asked to make this happen in 2 modes:
 - Single-threaded mode
 - Multi-threaded mode

As an input, my program takes 3 values in the command line
 - first one: the picture that will be changed
 - second one: the size of the boxes
 - third one: S or M. S is for single-threaded mode, M is for multi-threaded mode

In the single-threaded mode, the program start from the top left (0,0 coordinates) and pixels in the given box size are changed to the average color. The "blurring" process goes from left to the right, when the row finishes, boxes on the next row start "blurring". 

In the multi-threaded mode, multiple threads (the number of cores of the computer) run at the same time.
