# import cs160gui4
from tkinter import *
from tkinter.ttk import *


class GuiClass:
    def __init__(self):
        self.__title = None
        self.__imagePath = None
        self.__inputs = {}
        self.__functions = {}
        self.__printWindow = {}
        self.__spacers = []
        self.__prompts = []
        self.__numOfItems = 0
        self.__useOKButton = False
        self.__useCancelButton = False
        self.__oKButtonInfo = {}
        self.__cancelButtonInfo = {}
        self.__root = Tk()

    def __getSortedLabels(self):
        return sorted(list(self.__inputs.keys()), key=lambda x: int(self.__inputs[x]['index']))

    def __refreshInput(self):
        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            self.__inputs[label]['value'] = self.__inputs[label]['Entry'].get()

    def setPrintWindow(self, label: str, startCol: int, startRow: int, endCol: int, endRow: int) -> None:
        self.__printWindow[label] = {'startCol': startCol, 'startRow': startRow, 'endCol': endCol, 'endRow': endRow}

    def setFunction(self, label: str, col: int, row: int, function) -> None:
        label = label.lower()
        self.__functions[label] = {}
        self.__functions[label]['function'] = function
        self.__functions[label]['col'] = col
        self.__functions[label]['row'] = row

    def setOKButton(self, text: str, col: int, row: int) -> None:
        self.__useOKButton = True
        self.__oKButtonInfo['text'] = text
        self.__oKButtonInfo['col'] = col
        self.__oKButtonInfo['row'] = row

    def setCancelButton(self, text: str, col: int, row: int) -> None:
        self.__useCancelButton = True
        self.__cancelButtonInfo['text'] = text
        self.__cancelButtonInfo['col'] = col
        self.__cancelButtonInfo['row'] = row

    def setText(self, prompt: str, col: int, row: int, endCol=-1, endRow=-1, align='left') -> None:
        self.__prompts.append(
            {'prompt': prompt, 'align': align, 'col': col, 'row': row, 'endCol': endCol, 'endRow': endRow})

    def setSpacer(self, col: int, row: int, width: int) -> None:
        temp = {'width': width, 'col': col, 'row': row}
        self.__spacers.append(temp)

    def setInputInfo(self, label: str, col: object, row: object, defValue: object, typeOfInput: object) -> object:
        self.__inputs[label] = {}
        self.__inputs[label]['value'] = defValue[0] if typeOfInput == 'combo' else defValue
        self.__inputs[label]['initValue'] = defValue
        self.__inputs[label]['type'] = typeOfInput
        self.__inputs[label]['col'] = col
        self.__inputs[label]['row'] = row
        self.__numOfItems = self.__numOfItems + 1
        self.__inputs[label]['index'] = self.__numOfItems

    def getInt(self, label: str, col: int, row: int, defValue=0) -> None:
        self.setInputInfo(label, col, row, defValue, 'int')

    def getIntV(self, prompt: str, label: str, col: int, row: int, defValue=0) -> None:
        self.setText(prompt, col, row)
        self.setInputInfo(label, col + 1, row, defValue, 'int')

    def getString(self, label: str, col: int, row: int, defValue="") -> None:
        self.setInputInfo(label, col, row, defValue, 'str')

    def getFloat(self, label: str, col: int, row: int, defValue=0.0) -> None:
        self.setInputInfo(label, col, row, defValue, 'float')

    def getCombobox(self, prompt: str, col: int, row: int, choices: list) -> None:
        self.setText(prompt, col, row)
        self.setInputInfo(prompt, col + 1, row, choices, 'combo')

    # we go here when the user exits the form and wants to keep the data
    def __buttonPressed(self, event: EventType) -> None:
        # getting the internal name of the widget from the text displayed
        # need a better solution, may not be unique
        # first character in the name is a period - so it's removed
        name = str(event.widget).lower()
        name = name[1:]
        self.__refreshInput()
        self.__functions[name]['function'](self)

    def __enterButtonPressed(self, event: EventType) -> None:
        self.__refreshInput()
        self.__root.quit()

    def __cancelButtonPressed(self, event: EventType) -> None:
        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            self.__inputs[label]['value'] = self.__inputs[label]['initValue']
        self.__root.quit()

    def setTitle(self, title: str):
        self.__title = title

    def startInput(self) -> None:
        if self.__title is not None:
            self.__root.title(self.__title)
        if self.__imagePath is not None:
            self.__root.iconphoto(True, PhotoImage(file=self.__imagePath))

        for index in range(len(self.__prompts)):
            p = self.__prompts[index]
            if p['endCol'] != -1:
                Label(self.__root, text=p['prompt']).grid(sticky="WE", row=p['row'], column=p['col'], padx=5, pady=5,
                                                          columnspan=(p['endCol'] - p['col'] + 1))
            else:
                Label(self.__root, text=p['prompt']).grid(sticky="NW", row=p['row'], column=p['col'], padx=5, pady=5)

        # write out spacers in grid
        for index in range(len(self.__spacers)):
            s = self.__spacers[index]
            Label(self.__root, text='', width=s['width']).grid(sticky="NW", row=s['row'], column=s['col'])

        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            l = self.__inputs[label]
            if l['type'] == 'combo':
                l['Entry'] = Combobox(self.__root, values=l['initValue'], state="readonly")
                l['Entry'].current(0)
                l['Entry'].grid(column=l['col'], row=l['row'])
            else:
                l['Entry'] = Entry(self.__root, width=20)
                l['Entry'].grid(sticky="NW", row=l['row'], column=l['col'], padx=5, pady=5)
                l['Entry'].insert(0, l['value'])

        for label in list(self.__printWindow.keys()):
            f = Frame(self.__root)
            pw = self.__printWindow[label]
            pw['Text'] = Text(f)
            pw['Scroll'] = Scrollbar(f, command=pw['Text'].yview, orient=VERTICAL)
            pw['Text'].config(state=DISABLED)
            pw['Text'].config(yscrollcommand=pw['Scroll'].set)
            pw['Scroll'].pack(side=RIGHT, fill=Y)
            pw['Text'].pack()
            f.grid(sticky="NSEW", row=pw['startRow'], column=pw['startCol'], padx=5, pady=5,
                   columnspan=(pw['endCol'] - pw['startCol'] + 1))

        for funcLabel in (func.lower() for func in self.__functions):
            l = self.__functions[funcLabel]
            l['Button'] = Button(self.__root, takefocus=1, text=funcLabel, name=funcLabel.lower())
            l['Button'].grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                             row=l['row'], column=l['col'])
            l['Button'].bind("<Return>", self.someOtherButtonPressed)
            l['Button'].bind("<Button-1>", self.someOtherButtonPressed)

        # needs to go in here somewhere
        if self.__useOKButton:
            okButton = Button(self.__root, takefocus=1, text=self.__oKButtonInfo['text'])
            okButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                          row=self.__oKButtonInfo['row'], column=self.__oKButtonInfo['col'])
            okButton.bind("<Return>", self.enterButtonPressed)
            okButton.bind("<Button-1>", self.enterButtonPressed)

        if self.__useCancelButton:
            cancelButton = Button(self.__root, takefocus=1, text=self.__cancelButtonInfo['text'])
            cancelButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                              row=self.__cancelButtonInfo['row'], column=self.__cancelButtonInfo['col'])
            cancelButton.bind("<Return>", self.cancelButtonPressed)
            cancelButton.bind("<Button-1>", self.cancelButtonPressed)

        self.__root.mainloop()

        try:
            self.__root.destroy()
        except TclError:
            # usually in the case of user closing the window
            pass

    def getInputs(self) -> None:
        self.startInput()

    def get(self, label: str) -> object:
        if self.__inputs[label]['type'] == 'int':
            return int(self.__inputs[label]['value'])
        elif self.__inputs[label]['type'] == 'float':
            return float(self.__inputs[label]['value'])
        else:
            return self.__inputs[label]['value']

    def set(self, label: str, value: object) -> None:
        self.__inputs[label]['Entry'].delete(0, END)
        self.__inputs[label]['Entry'].insert(0, value)
