# import cs160gui4
from tkinter import *
import tkinter as tk
from tkinter import ttk


class GuiClass:
    def __init__(self):
        self.inputs = {}
        self.functions = {}
        self.printWindow = {}
        self.spacers = []
        self.prompts = []
        self.numOfItems = 0
        self.useOKButton = False
        self.useCancelButton = False
        self.oKButtonInfo = {}
        self.cancelButtonInfo = {}
        self.root = tk.Tk()

    def setPrintWindow(self, label: str, startCol: int, startRow: int, endCol: int, endRow: int) -> None:
        self.printWindow[label] = {}
        self.printWindow[label]['startCol'] = startCol
        self.printWindow[label]['startRow'] = startRow
        self.printWindow[label]['endCol'] = endCol
        self.printWindow[label]['endRow'] = endRow

    def print(self, label: str, *params) -> None:
        print(params, "\n", type(params), "\n")
        self.printWindow[label]['Text'].config(state=NORMAL)
        for item in params:
            self.printWindow[label]['Text'].insert(END, item)
        self.printWindow[label]['Text'].config(state=DISABLED)

    def setFunction(self, label: str, col: int, row: int, function) -> None:
        self.functions[label] = {}
        self.functions[label]['function'] = function
        self.functions[label]['col'] = col
        self.functions[label]['row'] = row

    def setOKButton(self, text: str, col: int, row: int) -> None:
        self.useOKButton = True
        self.oKButtonInfo['text'] = text
        self.oKButtonInfo['col'] = col
        self.oKButtonInfo['row'] = row

    def setCancelButton(self, text: str, col: int, row: int) -> None:
        self.useCancelButton = True
        self.cancelButtonInfo['text'] = text
        self.cancelButtonInfo['col'] = col
        self.cancelButtonInfo['row'] = row

    def setText(self, prompt: str, col: int, row: int, endCol=-1, endRow=-1, align='left') -> None:
        self.prompts.append(
            {'prompt': prompt, 'align': align, 'col': col, 'row': row, 'endCol': endCol, 'endRow': endRow})

    def setSpacer(self, col: int, row: int, width: int) -> None:
        temp = {'width': width, 'col': col, 'row': row}
        self.spacers.append(temp)

    def setInputInfo(self, label: str, col: object, row: object, defValue: object, typeOfInput: object) -> object:
        self.inputs[label] = {}
        self.inputs[label]['value'] = defValue[0] if typeOfInput == 'combo' else defValue
        self.inputs[label]['initValue'] = defValue
        self.inputs[label]['type'] = typeOfInput
        self.inputs[label]['col'] = col
        self.inputs[label]['row'] = row
        self.numOfItems = self.numOfItems + 1
        self.inputs[label]['index'] = self.numOfItems

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

    def getSortedLabels(self):
        return sorted(list(self.inputs.keys()), key=lambda x: int(self.inputs[x]['index']))

    # we go here when the user exits the form and wants to keep the data
    def someOtherButtonPressed(self, event: EventType) -> None:
        # getting the internal name of the widget from the text displayed
        # need a better solution, may not be unique
        # first character in the name is a period - so it's removed
        name = str(event.widget).lower()
        name = name[1:]
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['Entry'].get()
        self.functions[name]['function'](self)

    def enterButtonPressed(self, event: EventType) -> None:
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['Entry'].get()
        self.root.quit()

    def cancelButtonPressed(self, event: EventType) -> None:
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['initValue']
        self.root.quit()

    def startInput(self) -> None:

        for index in range(len(self.prompts)):
            p = self.prompts[index]
            if p['endCol'] != -1:
                Label(self.root, text=p['prompt']).grid(sticky="WE", row=p['row'], column=p['col'], padx=5, pady=5,
                                                        columnspan=(p['endCol'] - p['col'] + 1))
            else:
                Label(self.root, text=p['prompt']).grid(sticky="NW", row=p['row'], column=p['col'], padx=5, pady=5)

        # write out spacers in grid
        for index in range(len(self.spacers)):
            s = self.spacers[index]
            Label(self.root, text='', width=s['width']).grid(sticky="NW", row=s['row'], column=s['col'])

        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            l = self.inputs[label]
            if l['type'] == 'combo':
                l['Entry'] = ttk.Combobox(self.root, values=l['initValue'], state="readonly")
                l['Entry'].current(0)
                l['Entry'].grid(column=l['col'], row=l['row'])
            else:
                l['Entry'] = Entry(self.root, width=20)
                l['Entry'].grid(sticky="NW", row=l['row'], column=l['col'], padx=5, pady=5)
                l['Entry'].insert(0, l['value'])

        for label in list(self.printWindow.keys()):
            f = Frame(self.root)
            pw = self.printWindow[label]
            pw['Text'] = Text(f)
            pw['Scroll'] = Scrollbar(f, command=pw['Text'].yview, orient=VERTICAL)
            pw['Text'].config(state=DISABLED)
            pw['Text'].config(yscrollcommand=pw['Scroll'].set)
            pw['Scroll'].pack(side=RIGHT, fill=Y)
            pw['Text'].pack()
            f.grid(sticky="NSEW", row=pw['startRow'], column=pw['startCol'], padx=5, pady=5,
                   columnspan=(pw['endCol'] - pw['startCol'] + 1))

        for funcLabel in self.functions:
            l = self.functions[funcLabel]
            l['Button'] = Button(self.root, takefocus=1, text=funcLabel, name=funcLabel.lower())
            l['Button'].grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                             row=l['row'], column=l['col'])
            l['Button'].bind("<Return>", self.someOtherButtonPressed)
            l['Button'].bind("<Button-1>", self.someOtherButtonPressed)

        # needs to go in here somewhere
        if self.useOKButton:
            okButton = Button(self.root, takefocus=1, text=self.oKButtonInfo['text'])
            okButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                          row=self.oKButtonInfo['row'], column=self.oKButtonInfo['col'])
            okButton.bind("<Return>", self.enterButtonPressed)
            okButton.bind("<Button-1>", self.enterButtonPressed)

        if self.useCancelButton:
            cancelButton = Button(self.root, takefocus=1, text=self.cancelButtonInfo['text'])
            cancelButton.grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                              row=self.cancelButtonInfo['row'], column=self.cancelButtonInfo['col'])
            cancelButton.bind("<Return>", self.cancelButtonPressed)
            cancelButton.bind("<Button-1>", self.cancelButtonPressed)

        self.root.mainloop()

        try:
            self.root.destroy()
        except TclError:
            # usually in the case of user closing the window
            pass

    def getInputs(self, title: str) -> None:
        self.startInput()

    def get(self, label: str) -> object:
        if self.inputs[label]['type'] == 'int':
            return int(self.inputs[label]['value'])
        elif self.inputs[label]['type'] == 'float':
            return float(self.inputs[label]['value'])
        else:
            return self.inputs[label]['value']

    def set(self, label: str, value: int) -> None:
        self.inputs[label]['Entry'].delete(0, END)
        self.inputs[label]['Entry'].insert(0, value)
