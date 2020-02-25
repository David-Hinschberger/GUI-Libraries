from Python_GUI.GuiClass import *


def addTwoValues(screen):
    int1 = screen.get("int1")
    int2 = screen.get("int2")
    total = int1 + int2
    screen.set("window1", total)


def floatAdd1(screen):
    screen.set("window1", screen.get("float1") + 1)
    screen.set("float1", screen.get("float1") + 1)

def printNums(screen):
    x = screen.get("int1")
    y = ''
    for i in range(x):
        y += str(i) + "\n"
    screen.set('window1', y)

def printNums2(screen):
    x = screen.get("int2")
    results = [str(i) for i in range(x)]
    screen.set('window1', ', '.join(results))

def originalOption():
    int1 = 10
    int2 = 20
    float1 = 123.45

    screen = GuiClass()

    screen.setText("Testing data entry", align=False)
    screen.setSpacer(col=2)

    screen.setText("Enter int 1")
    screen.setIntInput("int1", defValue=int1)

    screen.setText("Enter int 2")
    screen.setIntInput("int2", defValue=int2)

    screen.setText("Enter float 1")
    screen.setFloatInput("float1", defValue=float1)

    screen.setText("Enter string 1")
    screen.setStringInput("str1")

    fruitChoices = ["Apple", "Pear", "Grape", "Orange", 5, 6]
    screen.setComboBoxInput("Enter your favorite fruit", choices=fruitChoices)
    screen.setButton("add", function=addTwoValues)
    screen.setButton("Float + 1", function=floatAdd1)
    screen.setButton("fizzbuzz until int1", function=printNums)
    screen.setButton("fizzbuzz until int2", function=printNums2)

    screen.setText("Total")
    screen.setIntInput("total")

    screen.setPrintWindow("window1")

    screen.setTitle("Windows 11")

    print("Before inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", screen.get("float1"))
    print("String is ", screen.get("str1"))
    print("Total is ", screen.get("total"))
    print("Numeric total is ", (int1 + int2 + screen.get("float1")))
    print("Favorite fruit is ", screen.get("Enter your favorite fruit"))

    screen.startGUI()
    int1 = screen.get("int1")
    int2 = screen.get("int2")

    print("\n\nAfter inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", screen.get("float1"))
    print("String is ", screen.get("str1"))
    print("Total is ", screen.get("total"))
    print("Numeric total is ", (int1 + int2 + screen.get("float1")))

    print("Favorite fruit is ", screen.get("Enter your favorite fruit"))


originalOption()
