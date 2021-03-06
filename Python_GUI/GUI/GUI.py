from tkinter import PhotoImage, Grid, Text, TclError, EventType, Tk, filedialog
from tkinter.ttk import Combobox, Label, Frame, Scrollbar, Button, Entry
from typing import Union, Iterable
import os


class GuiClass:
    def __init__(self):
        self.__title = None
        self.__imagePath = None
        self.__inputs = {}
        self.__functions = {}
        self.__printWindow = {}
        self.__spacers = []
        self.__prompts = {}
        self.__fileSelections = {}
        self.__functionNameToLabel = {}
        self.__bgColor = "SystemButtonFace"
        self.__root = Tk()
        # Keeps track of rows added for each column
        self.__colRowCount = [0, 0, 0]

    def __getSortedLabels(self) -> Iterable:
        return sorted(list(self.__inputs.keys()), key=lambda x: int(self.__inputs[x]['index']))

    def __refreshInput(self) -> None:
        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            if self.__inputs[label]['type'] == 'readstring':
                continue
            self.__inputs[label]['value'] = self.__inputs[label]['Entry'].get()

    # we go here when the user exits the form and wants to keep the data
    def __buttonPressed(self, event: EventType) -> None:
        # getting the internal name of the widget from the text displayed
        # need a better solution, may not be unique
        # first character in the name is a period - so it's removed
        name = self.__functionNameToLabel[str(event.widget)[1:]]
        self.__refreshInput()
        self.__functions[name]['function'](self)

    def __inputHelper(self, label: str, col: int, row: int, defValue: Union[int, float, str, Iterable],
                      typeOfInput: str) -> None:
        self.__inputs[label] = {
            'value': defValue[0] if typeOfInput == 'combo' else defValue,
            'initValue': defValue,
            'type': typeOfInput,
            'col': col,
            'row': row,
            'index': len(self.__inputs) + 1}
    
    def __getFileName(self, event: EventType) -> None:
        name = str(event.widget)[1:]
        filename = filedialog.askopenfilename(initialdir=os.getcwd(), title=name)
        self.__fileSelections[f"##{name}##"]['filename'] = filename
        self.__fileSelections[f"##{name}##"]['entry'].configure(text= "No file selected." if (filename == "") else filename)

    @staticmethod
    def __validateFloat(value_if_allowed: str) -> bool:
        # lambda in java
        if len(value_if_allowed) == 0:
            return True
        if value_if_allowed:
            try:
                float(value_if_allowed)
                return True
            except ValueError:
                return False
        else:
            return False

    @staticmethod
    def __validateInt(value_if_allowed: str) -> bool:
        # lambda in java
        if len(value_if_allowed) == 0:
            return True
        if value_if_allowed:
            try:
                int(value_if_allowed)
                return True
            except ValueError:
                return False
        else:
            return False

    # window default seems to be 670x640 px
    def __startInput(self) -> None:
        # called setup in java
        if self.__title is not None:
            self.__root.title(self.__title)
        if self.__imagePath is not None:
            self.__root.iconphoto(True, PhotoImage(file=self.__imagePath))

        self.__root.configure(bg=self.__bgColor)
        Grid.rowconfigure(self.__root, max(self.__colRowCount), weight=1)
        Grid.columnconfigure(self.__root, 3, weight=1)
        Grid.columnconfigure(self.__root, 1, weight=1)
        Grid.columnconfigure(self.__root, 2, weight=1)

        for label in list(self.__printWindow.keys()):
            frame = Frame(self.__root)
            pw = self.__printWindow[label]
            pw['Text'] = Text(frame, wrap="word")
            pw['Scroll'] = Scrollbar(
                frame, command=pw['Text'].yview, orient="vertical")
            pw['Text'].config(state="disabled",
                              yscrollcommand=pw['Scroll'].set)
            pw['Scroll'].pack(side="right", fill="y")
            pw['Text'].pack(side="left", fill="both", expand="yes")
            frame.grid(sticky="NSEW", row=max(self.__colRowCount), column=pw['startCol'], padx=5, pady=5,
                       columnspan=(pw['endCol'] - pw['startCol'] + 1))
            Grid.rowconfigure(self.__root, max(self.__colRowCount), weight=1)
            for i in range(len(self.__colRowCount)):
                self.__colRowCount[i] += 1

        for p in self.__prompts.keys():
            self.__prompts[p]['Entry'] = Label(
                self.__root, text=self.__prompts[p]['prompt'])
            self.__prompts[p]['Entry'].grid(sticky="W" if self.__prompts[p]['alignLeft'] else 'E',
                                            row=self.__prompts[p]['row'], column=self.__prompts[p]['col'], padx=5,
                                            pady=5)

        # write out spacers in grid
        for index in range(len(self.__spacers)):
            s = self.__spacers[index]
            Label(self.__root, background=self.__bgColor).grid(
                row=s['row'], column=s['col'], padx=0, pady=5, ipady='1m')

        for sortedLabel in self.__getSortedLabels():
            label = self.__inputs[sortedLabel]
            if label['type'] == 'combo':
                label['Entry'] = Combobox(
                    self.__root, values=label['initValue'], state="readonly")
                label['Entry'].current(0)
                label['Entry'].grid(
                    sticky='EW', row=label['row'], column=label['col'], padx=5, pady=5)
            elif label['type'] == 'readstring':
                prompt = Label(self.__root, text="No file selected.")
                prompt.grid(sticky="W", row=self.__fileSelections[sortedLabel]['row'], column=2, padx=5,
                                                pady=5)
                self.__fileSelections[sortedLabel]['entry'] = prompt
            else:
                # verify command
                vcmd = (self.__root.register(self.__validateFloat), '%P') if label['type'] == 'float' else (
                    self.__root.register(self.__validateInt), '%P') if label['type'] == 'int' else None
                label['Entry'] = Entry(
                    self.__root, validate='key', validatecommand=vcmd, width=23)
                label['Entry'].grid(
                    sticky='EW', row=label['row'], column=label['col'], padx=5, pady=5)
                label['Entry'].insert(0, label['value'])

        for funcLabel in self.__functions:
            label = self.__functions[funcLabel]
            label['Button'] = Button(self.__root, takefocus=1, text=funcLabel,
                                     name=funcLabel[0].lower() + funcLabel[1:])
            label['Button'].grid(
                padx=5, pady=5, ipady='1m', sticky="NSEW", row=label['row'], column=label['col'])
            if 'function' not in label:
                label['Button'].bind("<Return>", self.__getFileName)
                label['Button'].bind("<Button-1>", self.__getFileName)
            else:
                label['Button'].bind("<Return>", self.__buttonPressed)
                label['Button'].bind("<Button-1>", self.__buttonPressed)

        # Refresh the input if the window is closed out
        self.__root.protocol("WM_DELETE_WINDOW", lambda: (
            self.__refreshInput(), self.__root.destroy()))
        self.__root.mainloop()
        try:
            self.__root.destroy()
        except TclError:
            # usually in the case of user closing the window
            pass

    def addPrintWindow(self, label: str) -> None:
        self.__printWindow[label] = {'startCol': 1, 'endCol': 4}

    def addButton(self, label: str, function: callable) -> None:
        self.__functions[label] = {
            'function': function, 'col': 3, 'row': self.__colRowCount[2]}
        self.__functionNameToLabel[label[0].lower() + label[1:]] = label
        self.__colRowCount[2] += 1

    def addText(self, identifier: str, prompt: str, alignLeft: bool = True) -> None:
        self.__prompts[identifier] = {'prompt': prompt,
                                      'alignLeft': alignLeft,
                                      'col': 1,
                                      'row': self.__colRowCount[0]}
        self.__colRowCount[0] += 1

    def addSpacer(self, col: int) -> None:
        temp = {'col': col, 'row': self.__colRowCount[col]}
        self.__colRowCount[col] += 1
        self.__spacers.append(temp)

    def addIntInput(self, label: str, defValue: int = 0) -> None:
        self.__inputHelper(label, 2, self.__colRowCount[1], defValue, 'int')
        self.__colRowCount[1] += 1

    def addStringInput(self, label: str, defValue: str = "") -> None:
        self.__inputHelper(label, 2, self.__colRowCount[1], defValue, 'str')
        self.__colRowCount[1] += 1

    def addFloatInput(self, label: str, defValue: float = 0.0) -> None:
        self.__inputHelper(label, 2, self.__colRowCount[1], defValue, 'float')
        self.__colRowCount[1] += 1

    def addComboBoxInput(self, prompt: str, choices: Iterable) -> None:
        row = max(self.__colRowCount[0], self.__colRowCount[1])
        self.__prompts[prompt] = {'prompt': prompt,
                                  'alignLeft': True,
                                  'col': 1,
                                  'row': row}
        self.__inputHelper(f"__{prompt}__", 2, row, choices, 'combo')
        self.__colRowCount[0] = row + 1
        self.__colRowCount[1] = row + 1

    def addFileInput(self, prompt: str) -> None:
        row = max(self.__colRowCount)
        prompt = prompt[0].lower() + prompt[1:]
        self.__prompts[prompt] = {'prompt': prompt,
                                  'alignLeft': True,
                                  'col': 1,
                                  'row': row}
        self.__inputHelper(f"##{prompt}##", 2, row, prompt, 'readstring')
        self.__functions[f"{prompt}"] = {'col': 3, 'row': row}
        self.__functionNameToLabel[f"##{prompt}##"] = prompt
        self.__colRowCount[0] = row + 1
        self.__colRowCount[1] = row + 1
        self.__colRowCount[2] = row + 1
        self.__fileSelections[f"##{prompt}##"] = {'row': row,
                                                  'prompt': prompt,
                                                  'filename': '',
                                                  'entry': None}

    def setTitle(self, title: str) -> None:
        self.__title = title

    def setIcon(self, imagePath: str) -> None:
        self.__imagePath = imagePath
    
    def setBackgroundColor(self, hexColor: str) -> None:
        self.__bgColor = hexColor

    def startGUI(self) -> None:
        self.__startInput()

    def getStr(self, label: str) -> str:
        if label in self.__printWindow:
            # ignore last char which would be a newline
            return self.__printWindow[label]['Text'].get(1.0, "end")[0:-1]
        elif f"__{label}__" in self.__inputs:
            return self.__inputs[f"__{label}__"]['value']
        elif label in self.__inputs:
            return self.__inputs[label]['value']
        else:
            return self.__prompts[label]['Entry']['text']

    def getInt(self, label: str) -> int:
        try:
            if label in self.__printWindow:
                # ignore last char which would be a newline
                return int(self.__printWindow[label]['Text'].get(1.0, "end")[0:-1])
            elif f"__{label}__" in self.__inputs:
                return int(self.__inputs[f"__{label}__"]['value'])
            elif label in self.__inputs:
                return int(self.__inputs[label]['value'])
            else:
                return int(self.__prompts[label]['Entry']['text'])
        except ValueError:
            retVal = 0
        return retVal

    def getFloat(self, label: str) -> float:
        try:
            if label in self.__printWindow:
                # ignore last char which would be a newline
                return float(self.__printWindow[label]['Text'].get(1.0, "end")[0:-1])
            elif f"__{label}__" in self.__inputs:
                return float(self.__inputs[f"__{label}__"]['value'])
            elif label in self.__inputs:
                return float(self.__inputs[label]['value'])
            else:
                return float(self.__prompts[label]['Entry']['text'])
        except ValueError:
            retVal = 0
        return retVal

    def getFile(self, label: str) -> str:
        return self.__fileSelections[f"##{label[0].lower() + label[1:]}##"]['filename']

    def set(self, label: str, value: Union[int, float, str], append: bool = False) -> None:
        if label in self.__printWindow:
            self.__printWindow[label]['Text'].config(state="normal")
            if not append:
                self.__printWindow[label]['Text'].delete(1.0, "end")
            self.__printWindow[label]['Text'].insert("end", value)
            self.__printWindow[label]['Text'].config(state="disabled")
        elif label in self.__prompts:
            if append:
                value = self.__prompts[label]['Entry']['text'] + value
            self.__prompts[label]['Entry'].configure(text=value)
        else:
            if not append:
                self.__inputs[label]['Entry'].delete(0, "end")
            self.__inputs[label]['Entry'].insert(0, value)

    def clear(self, label: str) -> None:
        if label in self.__printWindow:
            self.__printWindow[label]['Text'].config(state="normal")
            self.__printWindow[label]['Text'].delete(1.0, "end")
            self.__printWindow[label]['Text'].config(state="disabled")
        elif label in self.__prompts:
            self.__prompts[label]['Entry'].configure(text="")
        else:
            self.__inputs[label]['Entry'].delete(0, "end")
