# import cs160gui4
from tkinter import *
from tkinter.ttk import *


class GuiClass:
    def __init__(self):
        self.__title = None
        self.__imagePath = None
        self.__inputs = {}
        self.__functions = {}
        self.__functionNameToLabel = {}
        self.__printWindow = {}
        self.__spacers = []
        self.__prompts = []
        self.__numOfItems = 0
        self.__oKButtonInfo = {}
        self.__cancelButtonInfo = {}
        self.__root = Tk()
        # No plans to use in the future.
        self.__useOKButton = False
        self.__useCancelButton = False

    def __getSortedLabels(self):
        return sorted(list(self.__inputs.keys()), key=lambda x: int(self.__inputs[x]['index']))

    def __refreshInput(self):
        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            self.__inputs[label]['value'] = self.__inputs[label]['Entry'].get()

    # No plans to use in the future.
    def setOKButton(self, text: str, col: int, row: int) -> None:
        self.__useOKButton = True
        self.__oKButtonInfo['text'] = text
        self.__oKButtonInfo['col'] = col
        self.__oKButtonInfo['row'] = row

    # No plans to use in the future.
    def setCancelButton(self, text: str, col: int, row: int) -> None:
        self.__useCancelButton = True
        self.__cancelButtonInfo['text'] = text
        self.__cancelButtonInfo['col'] = col
        self.__cancelButtonInfo['row'] = row

    # No plans to use in the future.
    def __enterButtonPressed(self) -> None:
        self.__refreshInput()
        self.__root.quit()

    # No plans to use in the future.
    def __cancelButtonPressed(self) -> None:
        sortedLabels = self.__getSortedLabels()
        for label in sortedLabels:
            self.__inputs[label]['value'] = self.__inputs[label]['initValue']
        self.__root.quit()

    def setPrintWindow(self, label: str, startCol: int, startRow: int, endCol: int, endRow: int) -> None:
        self.__printWindow[label] = {'startCol': startCol, 'startRow': startRow, 'endCol': endCol, 'endRow': endRow}

    def setFunction(self, label: str, col: int, row: int, function) -> None:
        label = label
        self.__functionNameToLabel[label[0].lower()+label[1:]] = label
        self.__functions[label] = {'function': function, 'col': col, 'row': row}

    def setText(self, prompt: str, col: int, row: int, endCol=-1, endRow=-1, align='left') -> None:
        self.__prompts.append(
            {'prompt': prompt,
             'align': align,
             'col': col,
             'row': row,
             'endCol': endCol,
             'endRow': endRow})

    def setSpacer(self, col: int, row: int, width: int) -> None:
        temp = {'width': width,
                'col': col,
                'row': row}
        self.__spacers.append(temp)

    def setInputInfo(self, label: str, col: object, row: object, defValue: object, typeOfInput: object) -> None:
        self.__numOfItems = self.__numOfItems + 1
        self.__inputs[label] = {
            'value': defValue[0] if typeOfInput == 'combo' else defValue,
            'initValue': defValue,
            'type': typeOfInput,
            'col': col,
            'row': row,
            'index': self.__numOfItems + 1}

    def getInt(self, label: str, col: int, row: int, defValue=0) -> None:
        self.setInputInfo(label, col, row, defValue, 'int')

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
        name = self.__functionNameToLabel[str(event.widget)[1:]]
        self.__refreshInput()
        self.__functions[name]['function'](self)

    def setTitle(self, title: str):
        self.__title = title

    def startInput(self) -> None:
        if self.__title is not None:
            self.__root.title(self.__title)
        if self.__imagePath is not None:
            self.__root.iconphoto(True, PhotoImage(file=self.__imagePath))

        for index in range(len(self.__prompts)):
            p = self.__prompts[index]
            Label(self.__root, text=p['prompt']).grid(sticky="NW", row=p['row'], column=p['col'], padx=5, pady=5)

        # write out spacers in grid
        for index in range(len(self.__spacers)):
            s = self.__spacers[index]
            Label(self.__root, width=s['width']).grid(sticky="NW", row=s['row'], column=s['col'], padx=5, pady=5)

        sortedLabels = self.__getSortedLabels()
        for sortedLabel in sortedLabels:
            label = self.__inputs[sortedLabel]
            if label['type'] == 'combo':
                label['Entry'] = Combobox(self.__root, values=label['initValue'], state="readonly")
                label['Entry'].current(0)
                label['Entry'].grid(column=label['col'], row=label['row'])
            else:
                label['Entry'] = Entry(self.__root, width=20)
                label['Entry'].grid(sticky="NW", row=label['row'], column=label['col'], padx=5, pady=5)
                label['Entry'].insert(0, label['value'])

        for label in list(self.__printWindow.keys()):
            frame = Frame(self.__root)
            pw = self.__printWindow[label]
            pw['Text'] = Text(frame)
            pw['Scroll'] = Scrollbar(frame, command=pw['Text'].yview, orient=VERTICAL)
            pw['Text'].config(state=DISABLED, yscrollcommand=pw['Scroll'].set)
            pw['Scroll'].pack(side=RIGHT, fill=Y)
            pw['Text'].pack()
            frame.grid(sticky="NSEW", row=pw['startRow'], column=pw['startCol'], padx=5, pady=5,
                       columnspan=(pw['endCol'] - pw['startCol'] + 1))

        for funcLabel in self.__functions:
            label = self.__functions[funcLabel]
            label['Button'] = Button(self.__root, takefocus=1, text=funcLabel, name=funcLabel[0].lower()+funcLabel[1:])
            label['Button'].grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="NSEW", row=label['row'],
                                 column=label['col'])
            label['Button'].bind("<Return>", self.__buttonPressed)
            label['Button'].bind("<Button-1>", self.__buttonPressed)

        # No plans to use in the future.
        # # needs to go in here somewhere
        # if self.__useOKButton:
        #     okButton = Button(self.__root, takefocus=1, text=self.__oKButtonInfo['text'])
        #     okButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
        #                   row=self.__oKButtonInfo['row'], column=self.__oKButtonInfo['col'])
        #     okButton.bind("<Return>", self.enterButtonPressed)
        #     okButton.bind("<Button-1>", self.enterButtonPressed)
        #
        # if self.__useCancelButton:
        #     cancelButton = Button(self.__root, takefocus=1, text=self.__cancelButtonInfo['text'])
        #     cancelButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
        #                       row=self.__cancelButtonInfo['row'], column=self.__cancelButtonInfo['col'])
        #     cancelButton.bind("<Return>", self.cancelButtonPressed)
        #     cancelButton.bind("<Button-1>", self.cancelButtonPressed)

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
