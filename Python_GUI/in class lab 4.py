from Python_GUI.GUI.GUI import *


def partA(screen: GuiClass):
    screen.set("output", "Part 1:\n")
    screen.set("output", "\n".join(str(i) for i in range(10, 51)), append=True)
    screen.set("output", "\n\nPart 2:\n", append=True)
    screen.set("output", ' '.join(str(i) for i in range(20, -1, -1)), append=True)
    screen.set("output", "\n\nPart 3:\n", append=True)
    screen.set("output", "\n".join((str(i / 2) if int(i / 2) != i / 2 else str(int(i / 2))) for i in range(0, 21)),
               append=True)


positive_total = 0
positive_nums = 0
negative_total = 0
negative_nums = 0


def partB(screen: GuiClass):
    global positive_total
    global positive_nums
    global negative_total
    global negative_nums
    num1 = screen.getInt("num1")
    if num1 == 0:
        screen.set("output",
                   f"Average of positive values: {0 if positive_total == 0 else positive_total / positive_nums}\n")
        screen.set("output",
                   f"Average of negative values: {0 if negative_total == 0 else negative_total / negative_nums}",
                   append=True)
        screen.set("label2", "Values read: 0")
        positive_total = 0
        positive_nums = 0
        negative_total = 0
        negative_nums = 0

    else:
        screen.set("label2",
                   f"Values read: {int(screen.getStr('label2')[screen.getStr('label2').index(': ') + 2:]) + 1}")
        if num1 < 0:
            negative_total += num1
            negative_nums += 1
        else:
            positive_total += num1
            positive_nums += 1


def partC(screen: GuiClass):
    screen.set("output", "".join(['*' for i in range(screen.getInt("num1"))]))


def main():
    GUI = GuiClass()
    GUI.addTitle("In class lab 4")
    GUI.addText("label1", "Enter a value until 0:")
    GUI.addIntInput('num1')
    GUI.addText("label2", "Values read: 0")
    GUI.addButton("Part A", partA)
    GUI.addButton("Part B", partB)
    GUI.addButton("Part C", partC)
    GUI.addPrintWindow("output")
    GUI.startGUI()


if __name__ == "__main__":
    main()
