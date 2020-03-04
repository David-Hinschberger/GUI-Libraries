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

    GUI.addText("Testing data entry", alignLeft=False)
    GUI.addSpacer(col=2)

    GUI.addText("Enter int 1")
    GUI.addIntInput("int1", defValue=int1)

    GUI.addText("Enter int 2")
    GUI.addIntInput("int2", defValue=int2)

    GUI.addText("Enter float 1")
    GUI.addFloatInput("float1", defValue=float1)

    GUI.addText("Enter string 1")
    GUI.addStringInput("str1")

    fruitChoices = ["Apple", "Pear", "Grape", "Orange", 5, 6]
    GUI.addComboBoxInput("Enter your favorite fruit", choices=fruitChoices)
    GUI.addButton("add", function=addTwoValues)
    GUI.addButton("Float + 1", function=floatAdd1)
    GUI.addButton("Fizzbuzz until int1 (new lines)", function=fizzBuzz1)
    GUI.addButton("Fizzbuzz until int2 (no new lines)", function=fizzBuzz2)

    GUI.addText("Total")
    GUI.addIntInput("total")

    GUI.addPrintWindow("window1")

    GUI.addTitle("Windows 11")

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
