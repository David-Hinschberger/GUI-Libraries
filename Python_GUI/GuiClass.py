from tkinter import PhotoImage, Grid, Text, DISABLED, RIGHT, LEFT, BOTH, Y, YES, VERTICAL, TclError, NORMAL, END, \
    EventType, Tk
from tkinter.ttk import Combobox, Label, Frame, Scrollbar, Button, Entry
from typing import Union, Iterable


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

    def __getSortedLabels(self) -> Iterable:
        return sorted(list(self.__inputs.keys()), key=lambda x: int(self.__inputs[x]['index']))

    def __refreshInput(self) -> None:
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

    # we go here when the user exits the form and wants to keep the data
    def __buttonPressed(self, event: EventType) -> None:
        # getting the internal name of the widget from the text displayed
        # need a better solution, may not be unique
        # first character in the name is a period - so it's removed
        name = self.__functionNameToLabel[str(event.widget)[1:]]
        self.__refreshInput()
        self.__functions[name]['function'](self)

    def __setInputHelper(self, label: str, col: int, row: int, defValue: Union[int, float, str, Iterable],
                         typeOfInput: str) -> None:
        self.__numOfItems = self.__numOfItems + 1
        self.__inputs[label] = {
            'value': defValue[0] if typeOfInput == 'combo' else defValue,
            'initValue': defValue,
            'type': typeOfInput,
            'col': col,
            'row': row,
            'index': self.__numOfItems + 1}

    def __startInput(self) -> None:
        if self.__title is not None:
            self.__root.title(self.__title)
        if self.__imagePath is not None:
            self.__root.iconphoto(True, PhotoImage(file=self.__imagePath))

        Grid.rowconfigure(self.__root, 7, weight=1)
        Grid.columnconfigure(self.__root, 3, weight=1)
        Grid.columnconfigure(self.__root, 1, weight=1)
        Grid.columnconfigure(self.__root, 2, weight=1)

        for index in range(len(self.__prompts)):
            p = self.__prompts[index]
            Label(self.__root, text=p['prompt']).grid(sticky="W" if p['align'] else 'E', row=p['row'],
                                                      column=p['col'], padx=5, pady=5)

        # write out spacers in grid
        for index in range(len(self.__spacers)):
            s = self.__spacers[index]
            Label(self.__root).grid(row=s['row'], column=s['col'], padx=5, pady=5, ipady='1m')

        sortedLabels = self.__getSortedLabels()
        for sortedLabel in sortedLabels:
            label = self.__inputs[sortedLabel]
            if label['type'] == 'combo':
                label['Entry'] = Combobox(self.__root, values=label['initValue'], state="readonly")
                label['Entry'].current(0)
                label['Entry'].grid(sticky='EW', row=label['row'], column=label['col'], padx=5, pady=5)
            else:
                label['Entry'] = Entry(self.__root, width=20)
                label['Entry'].grid(sticky='EW', row=label['row'], column=label['col'], padx=5, pady=5)
                label['Entry'].insert(0, label['value'])

        for label in list(self.__printWindow.keys()):
            frame = Frame(self.__root)
            pw = self.__printWindow[label]
            pw['Text'] = Text(frame)
            pw['Scroll'] = Scrollbar(frame, command=pw['Text'].yview, orient=VERTICAL)
            pw['Text'].config(state=DISABLED, yscrollcommand=pw['Scroll'].set)
            pw['Scroll'].pack(side=RIGHT, fill=Y)
            pw['Text'].pack(side=LEFT, fill=BOTH, expand=YES)
            frame.grid(sticky="NSEW", row=pw['startRow'], column=pw['startCol'], padx=5, pady=5,
                       columnspan=(pw['endCol'] - pw['startCol'] + 1))

        for funcLabel in self.__functions:
            label = self.__functions[funcLabel]
            label['Button'] = Button(self.__root, takefocus=1, text=funcLabel,
                                     name=funcLabel[0].lower() + funcLabel[1:])
            label['Button'].grid(padx=5, pady=5, ipady='1m', sticky="NSEW", row=label['row'], column=label['col'])
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

        # Do we want to refresh the input if the window is closed out?
        self.__root.protocol("WM_DELETE_WINDOW", lambda: (self.__refreshInput(), self.__root.destroy()))
        self.__root.mainloop()
        try:
            self.__root.destroy()
        except TclError:
            # usually in the case of user closing the window
            pass

    def setPrintWindow(self, label: str, startCol: int, startRow: int, endCol: int, endRow: int) -> None:
        self.__printWindow[label] = {'startCol': startCol, 'startRow': startRow, 'endCol': endCol, 'endRow': endRow}

    def setButton(self, label: str, function: callable, col: int, row: int) -> None:
        label = label
        self.__functionNameToLabel[label[0].lower() + label[1:]] = label
        self.__functions[label] = {'function': function, 'col': col, 'row': row}

    def setText(self, prompt: str, col: int, row: int, endCol: int = -1, endRow: int = -1,
                alignLeft: str = 'left') -> None:
        self.__prompts.append(
            {'prompt': prompt,
             'align': alignLeft,
             'col': col,
             'row': row,
             'endCol': endCol,
             'endRow': endRow})

    def setSpacer(self, col: int, row: int, width: int) -> None:
        temp = {'width': width,
                'col': col,
                'row': row}
        self.__spacers.append(temp)

    def setIntInput(self, label: str, col: int, row: int, defValue: int = 0) -> None:
        self.__setInputHelper(label, col, row, defValue, 'int')

    def setStringInput(self, label: str, col: int, row: int, defValue: str = "") -> None:
        self.__setInputHelper(label, col, row, defValue, 'str')

    def setFloatInput(self, label: str, col: int, row: int, defValue: float = 0.0) -> None:
        self.__setInputHelper(label, col, row, defValue, 'float')

    def setComboBoxInput(self, prompt: str, col: int, row: int, choices: Iterable) -> None:
        self.setText(prompt, col, row)
        self.__setInputHelper(prompt, col + 1, row, choices, 'combo')

    def setTitle(self, title: str) -> None:
        self.__title = title

    def startGUI(self) -> None:
        self.__startInput()

    def get(self, label: str) -> Union[int, float, str]:
        if self.__inputs[label]['type'] == 'int':
            return int(self.__inputs[label]['value'])
        elif self.__inputs[label]['type'] == 'float':
            return float(self.__inputs[label]['value'])
        else:
            return self.__inputs[label]['value']

    def set(self, label: str, value: Union[int, float, str]) -> None:
        if label in self.__printWindow:
            self.__printWindow[label]['Text'].config(state=NORMAL)
            self.__printWindow[label]['Text'].delete(1.0, END)
            self.__printWindow[label]['Text'].insert(END, value)
            self.__printWindow[label]['Text'].config(state=DISABLED)
        else:
            self.__inputs[label]['Entry'].delete(0, END)
            self.__inputs[label]['Entry'].insert(0, value)
