from Python_GUI.GUI.GUI import *


def part1(screen: GuiClass):
    num1 = screen.getFloat('num1')
    if int(num1) == num1:
        num1 = int(num1)
        screen.set("output", str(num1) + " is a whole number")
    else:
        screen.set("output", str(num1) + " is not a whole number")
    screen.set("output",
               "\n" + (str(num1) + " is a multiple of 5" if num1 > 0 and num1 % 5 == 0 else str(
                   num1) + " is not a multiple of 5"), append=True)
    screen.set("output", "\n" + (str(num1) + " is odd" if num1 % 2 == 0 else str(num1) + " is even"), append=True)
    screen.set("output",
               "\n" + (
                   str(num1) + " is positive" if num1 > 0 else str(num1) + " is negative" if num1 < 0 else str(
                       num1) + " is zero"), append=True)
    screen.set("output",
               "\n" + (str(num1) + " is within the range of 2010 to 2018 inclusive" if 2010 <= num1 <= 2018 else str(
                   num1) + " is not within the range of 2010 to 2018 inclusive"), append=True)
    screen.set("output", "\n" + (
        str(num1) + " is within the 100's ☺" if 100 <= num1 < 200 else str(num1) + " is not within the 100's"),
               append=True)


def part2(screen: GuiClass):
    num1 = screen.getFloat('num1')
    num2 = screen.getFloat('num2')
    screen.set("output2", (
        "first value is largest" if num1 > num2 else "second value is largest" if num2 > num1 else "the two values "
                                                                                                   "are equal"))
    screen.set("output2", "\n" + (
        "the second value is a multiple of the first value" if num1 == num2 or (num1 != 0 and num2 / num1 == int(
            num2 / num1)) else "the second value is not a multiple of the first value"), append=True)
    screen.set("output2", "\n" + (
        "the first value is a multiple of the first value" if num1 == num2 or (num2 != 0 and num1 / num2 == int(
            num1 / num2)) else "the first value is not a multiple of the first value"), append=True)


def main():
    GUI: GuiClass = GuiClass()
    GUI.addTitle("In class lab 3")
    GUI.addText("label1", "Numeric Value 1: ")
    GUI.addFloatInput('num1')
    GUI.addText("label2", "Numeric Value 2: ")
    GUI.addFloatInput('num2')
    GUI.addButton("Part 1", part1)
    GUI.addButton("Part 2", part2)
    GUI.addPrintWindow("output")
    GUI.addPrintWindow("output2")
    GUI.startGUI()


if __name__ == "__main__":
    main()
