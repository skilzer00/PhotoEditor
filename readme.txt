CustumizedUI:
I created a ColorChooser popup window that opens up whenever clicking on the customized round button
The color of the button changes when it is pressed or armed
The normal color of the button changes as well, according to the current drawing color.



Usage of Model class:
1. I have to features in my view classes MainWindow and NewFileDialog that use the method setCanvasSize()
2. Using the setNewCanvasSize to create a new canvas for openFile in MainWindow

2. I use the method setComponentSize() all when layout out the components in MainWindow
For example: when creating my toolboxPanel or other components such as the color buttons

3. I also have a Brush class that the MouseListener share use