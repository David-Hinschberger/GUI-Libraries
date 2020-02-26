from Python_GUI.GuiClass import *


def addTwoValues(screen):
    int1 = screen.get("int1")
    int2 = screen.get("int2")
    total = int1 + int2
    screen.set("window1", total)


def floatAdd1(screen):
    screen.set("window1", screen.get("float1") + 1)
    screen.set("float1", screen.get("float1") + 1)


def fizzBuzz1(screen):
    y = ''
    for i in range(1, screen.get("int1")):
        if i % 3 == 0:
            y += "Fizz"
        if i % 5 == 0:
            y += "Buzz"
        if len(y) == 0 or y[-1] == '\n':
            y += str(i)
        y += '\n'
    screen.set('window1', y)


def fizzBuzz2(screen):
    results = ["FizzBuzz" if i % (3 * 5) == 0 else "Fizz" if i % 3 == 0 else "Buzz" if i % 5 == 0 else str(i) for i in
               range(1, screen.get("int2"))]
    screen.set('window1', ', '.join(results))


def main():
    int1 = 10
    int2 = 20
    float1 = 123.45

    GUI = GuiClass()

    GUI.setText("Testing data entry", col=1, row=0, endCol=2, alignLeft='center')

    GUI.setText("Enter int 1", col=1, row=1)
    GUI.setIntInput("int1", col=2, row=1, defValue=int1)

    GUI.setText("Enter int 2", col=1, row=2)
    GUI.setIntInput("int2", col=2, row=2, defValue=int2)

    GUI.setText("Enter float 1", col=1, row=3)
    GUI.setFloatInput("float1", col=2, row=3, defValue=float1)

    GUI.setText("Enter string 1", col=1, row=4)
    GUI.setStringInput("str1", col=2, row=4)

    fruitChoices = ["Apple", "Pear", "Grape", "Orange", 5, 6]
    GUI.setComboBoxInput("Enter your favorite fruit", col=1, row=6, choices=fruitChoices)

    GUI.setSpacer(col=3, row=1, width=2)
    GUI.setSpacer(col=3, row=6, width=2)
    GUI.setButton("add", col=3, row=1, function=addTwoValues)

    GUI.setSpacer(col=3, row=1, width=2)
    GUI.setSpacer(col=3, row=6, width=2)
    GUI.setButton("Float + 1", col=3, row=2, function=floatAdd1)
    GUI.setButton("Fizzbuzz until int1 (new lines)", col=3, row=3, function=fizzBuzz1)
    GUI.setButton("Fizzbuzz until int2 (no new lines)", col=3, row=4, function=fizzBuzz2)

    GUI.setText("Total", col=1, row=5)
    GUI.setIntInput("total", col=2, row=5)

    GUI.setPrintWindow("window1", startCol=1, startRow=7, endCol=4, endRow=9)

    GUI.setTitle("Windows 11")

    print("Before inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", GUI.get("float1"))
    print("String is ", GUI.get("str1"))
    print("Total is ", GUI.get("total"))
    print("Numeric total is ", (int1 + int2 + GUI.get("float1")))
    print("Favorite fruit is ", GUI.get("Enter your favorite fruit"))

    GUI.startGUI()
    int1 = GUI.get("int1")
    int2 = GUI.get("int2")

    print("\n\nAfter inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", GUI.get("float1"))
    print("String is ", GUI.get("str1"))
    print("Total is ", GUI.get("total"))
    print("Numeric total is ", (int1 + int2 + GUI.get("float1")))

    print("Favorite fruit is ", GUI.get("Enter your favorite fruit"))


if __name__ == "__main__":
    main()
    