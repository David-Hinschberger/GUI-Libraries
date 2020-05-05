from Python_GUI.GUI.GUI import *

locations = [0] * 4


def program(screen: GuiClass):
    user_num: float = screen.getFloat("num1")
    if user_num == 800:
        screen.set("output", f"""You had {locations[0]} Extremely cold location(s)
You had {locations[1]} Cold location(s)
You had {locations[2]} Warm location(s)
You had {locations[3]} Extremely Warm location(s)""")
        return
    screen.set("output", "The temperature entered is ")
    if user_num <= -25:
        locations[0] += 1
        screen.set("output", "extremely cold", append=True)
    elif user_num <= 10:
        locations[1] += 1
        screen.set("output", "cold", append=True)
    elif user_num <= 32:
        locations[2] += 1
        screen.set("output", "warm", append=True)
    else:
        locations[3] += 1
        screen.set("output", "extremely warm", append=True)
    screen.set("output",
               f"\nThe temperature entered is {abs(user_num - 20)} from the average temperature\n"
               f"The temperature celcius is {user_num * 1.8 + 32} in Fahrenheit", append=True)


def main():
    GUI = GuiClass()
    GUI.addTitle("In class lab 5")
    GUI.addText("label1", "Enter the temperature in Celcius:")
    GUI.addIntInput('num1')
    GUI.addButton("Do it!", program)
    GUI.addPrintWindow("output")
    GUI.startGUI()

    print(locations)


if __name__ == "__main__":
    main()
