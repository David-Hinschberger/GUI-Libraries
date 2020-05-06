from GUI.GUI import *


def addTwoValues(screen: GuiClass):
    int1 = screen.getInt("int1")
    int2 = screen.getInt("int2")
    total = int1 + int2
    screen.set("window1", total)


def floatAdd1(screen: GuiClass):
    screen.set("window1", screen.getFloat("float1") + 1)
    screen.set("float1", screen.getFloat("float1") + 1)


def fizzBuzz1(screen: GuiClass):
    y = ''
    for i in range(1, screen.getInt("int1")):
        if i % 3 == 0:
            y += "Fizz"
        if i % 5 == 0:
            y += "Buzz"
        if len(y) == 0 or y[-1] == '\n':
            y += str(i)
        y += '\n'
    screen.set('window1', y)


def main():
    int1 = 10
    int2 = 20
    float1 = 123.45

    GUI = GuiClass()

    GUI.addText("label1", "Testing data entry", alignLeft=False)
    GUI.addSpacer(col=2)
    GUI.addFileInput("Choose a File")

    GUI.addText("label2", "Enter int 1")
    GUI.addIntInput("int1", defValue=int1)

    GUI.addText("label3", "Enter int 2")
    GUI.addIntInput("int2", defValue=int2)

    GUI.addText("label4", "Enter float 1")
    GUI.addFloatInput("float1", defValue=float1)

    GUI.addText("label5", "Enter string 1")
    GUI.addStringInput("str1")

    fruitChoices = ["Apple", "Pear", "Grape", "Orange", 5, 6]
    GUI.addComboBoxInput("Enter your favorite fruit", choices=fruitChoices)
    GUI.addButton("add", function=addTwoValues)
    GUI.addButton("Float + 1", function=floatAdd1)
    GUI.addButton("Fizzbuzz until int1", function=fizzBuzz1)

    GUI.addText("label6", "Total")
    GUI.addIntInput("total")

    GUI.addPrintWindow("window1")

    GUI.setTitle("Windows 11")
    GUI.setBackgroundColor("#4e00ff")

    print("Before inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", GUI.getFloat("float1"))
    print("String is ", GUI.getStr("str1"))
    print("Total is ", GUI.getInt("total"))
    print("Numeric total is ", (int1 + int2 + GUI.getFloat("float1")))
    print("Favorite fruit is ", GUI.getStr("Enter your favorite fruit"))

    GUI.startGUI()
    int1 = GUI.getInt("int1")
    int2 = GUI.getInt("int2")

    print("\n\nAfter inputs")
    print("int1 is ", int1)
    print("int2 is ", int2)
    print("Float is ", GUI.getFloat("float1"))
    print("String is ", GUI.getStr("str1"))
    print("Total is ", GUI.getInt("total"))
    print("Numeric total is ", (int1 + int2 + GUI.getFloat("float1")))
    print("Favorite fruit is ", GUI.getStr("Enter your favorite fruit"))

    print(GUI.getFile("Choose a File"))


if __name__ == "__main__":
    main()
