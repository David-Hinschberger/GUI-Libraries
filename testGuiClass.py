from Python_GUI.GuiClass import *


def addTwoValues(screen):
    int1 = screen.get("int1")
    int2 = screen.get("int2")
    total = int1 + int2
    screen.set("window1", total)


def floatAdd1(screen):
    screen.set("window1", screen.get("float1") + 1)
    # screen.set("float1", screen.get("float1") + 1)


def originalOption():
    int1 = 10
    int2 = 20
    float1 = 123.45

    screen = GuiClass()

    screen.setText("Testing data entry", col=1, row=0, endCol=2, align='center')

    screen.setText("Enter int 1", col=1, row=1)
    screen.setIntInput("int1", col=2, row=1, defValue=int1)

    screen.setText("Enter int 2", col=1, row=2)
    screen.setIntInput("int2", col=2, row=2, defValue=int2)

    screen.setText("Enter float 1", col=1, row=3)
    screen.setFloatInput("float1", col=2, row=3, defValue=float1)

    screen.setText("Enter string 1", col=1, row=4)
    screen.setStringInput("str1", col=2, row=4)

    fruitChoices = ["Apple", "Pear", "Grape", "Orange", 5, 6]
    screen.setComboBoxInput("Enter your favorite fruit", col=3, row=5, choices=fruitChoices)

    screen.setSpacer(col=3, row=1, width=2)
    screen.setSpacer(col=3, row=6, width=2)
    screen.setButton("add", col=4, row=1, function=addTwoValues)

    screen.setSpacer(col=3, row=1, width=2)
    screen.setSpacer(col=3, row=6, width=2)
    screen.setButton("Float + 1", col=4, row=2, function=floatAdd1)

    screen.setText("Total", col=1, row=5)
    screen.setIntInput("total", col=2, row=5)

    screen.setPrintWindow("window1", startCol=1, startRow=7, endCol=4, endRow=9)

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


# Need to fill this in with the option without specifying rows and columns
def secondaryOption():
    pass


originalOption()
