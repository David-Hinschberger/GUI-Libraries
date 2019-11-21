# import cs160gui4
from tkinter import *
import tkinter as tk
from tkinter import ttk


class GuiClass(tk.Tk):
    def __init__(self):
        super(GuiClass, self).__init__()
        self.inputs = {}
        self.functions = {}
        self.printWindow = {}
        self.spacers = []
        self.prompts = []
        # self.labels = []
        self.numOfItems = 0
        self.useOKButton = False
        self.useCancelButton = False
        self.oKButtonInfo = {}
        self.cancelButtonInfo = {}
        # self.__enterBoxText = None
        self.root = None
        #super(GuiClass, self).__init__()

    def setCombobox(self, prompt, col, row, choices):
        self.setText(prompt, col, row)
        #self.setInputInfo(prompt, col + 1, row, 0, 'str')
        combo = ttk.Combobox(self, values=choices)
        combo.current(1)
        combo.grid(column=col, row=row)
        #combo.bind("<<ComboboxSelected>>", callbackFunc)

    def setPrintWindow(self, label, startCol, startRow, endCol, endRow):
        self.printWindow[label] = {}
        self.printWindow[label]['startCol'] = startCol
        self.printWindow[label]['startRow'] = startRow
        self.printWindow[label]['endCol'] = endCol
        self.printWindow[label]['endRow'] = endRow

    def print(self, label, *params):
        print(params, "\n", type(params), "\n")
        self.printWindow[label]['Text'].config(state=NORMAL)
        # self.printWindow [label]['Text'].insert (END, "hello\n")
        for item in params:
            self.printWindow[label]['Text'].insert(END, item)
        self.printWindow[label]['Text'].config(state=DISABLED)

    def setFunction(self, label, col, row, function):
        self.functions[label] = {}
        self.functions[label]['function'] = function
        self.functions[label]['col'] = col
        self.functions[label]['row'] = row

    def setOKButton(self, text, col, row):
        self.useOKButton = True
        self.oKButtonInfo['text'] = text
        self.oKButtonInfo['col'] = col
        self.oKButtonInfo['row'] = row

    def setCancelButton(self, text, col, row):
        self.useCancelButton = True
        self.cancelButtonInfo['text'] = text
        self.cancelButtonInfo['col'] = col
        self.cancelButtonInfo['row'] = row

    def setText(self, prompt, col, row, endCol=-1, endRow=-1, align='left'):
        temp = {}
        temp['prompt'] = prompt
        temp['align'] = align
        temp['col'] = col
        temp['row'] = row
        temp['endCol'] = endCol
        temp['endRow'] = endRow
        self.prompts.append(temp)

    def setSpacer(self, col, row, width):
        temp = {}
        temp['width'] = width
        temp['col'] = col
        temp['row'] = row
        self.spacers.append(temp)

    def setInputInfo(self, label, col, row, defValue, typeOfInput):
        self.inputs[label] = {}
        self.inputs[label]['value'] = defValue
        self.inputs[label]['initValue'] = defValue
        self.inputs[label]['type'] = typeOfInput
        self.inputs[label]['col'] = col
        self.inputs[label]['row'] = row
        self.numOfItems = self.numOfItems + 1
        self.inputs[label]['index'] = self.numOfItems
        # self.labels.append (label)

    def getInt(self, label, col, row, defValue=0):
        self.setInputInfo(label, col, row, defValue, 'int')

    def getIntV(self, prompt, label, col, row, defValue=0):
        self.setText(prompt, col, row)
        self.setInputInfo(label, col + 1, row, defValue, 'int')

    def getString(self, label, col, row, defValue=""):
        self.setInputInfo(label, col, row, defValue, 'str')

    def getFloat(self, label, col, row, defValue=0.0):
        self.setInputInfo(label, col, row, defValue, 'float')

    def getSortedLabels(self):
        sortedLabels = list(self.inputs.keys())
        sortedLabels.sort(key=lambda x: int(self.inputs[x]['index']))
        return sortedLabels

    # we go here when the user exits the form and wants to keep the data
    def someOtherButtonPressed(self, event):
        # getting the internal name of the widget from the text displayed
        # need a better solution, may not be unique
        # first character in the name is a period - so it's removed
        name = str(event.widget).lower()
        name = name[1:]
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['Entry'].get()
        self.functions[name]['function'](self)

    def enterButtonPressed(self, event):
        # copy from the widgets into the dictionary
        # for index in range(len(self.inputs)):
        #   self.inputs[self.labels[index]]['value'] = self.inputs[self.labels[index]]['Entry'].get()
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['Entry'].get()
        self.root.quit()

    def cancelButtonPressed(self, event):
        # setting EVERY input  back to it's initial value
        # for index in range(len(self.inputs)):
        #   self.inputs[self.labels[index]]['value'] = self.inputs[self.labels[index]]['initValue']
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            self.inputs[label]['value'] = self.inputs[label]['initValue']
        self.root.quit()

    def startInput(self):
        self.root = Tk()
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

        # sortedLabels = list(self.inputs.keys())
        # sortedLabels.sort(key=lambda x:int(self.inputs[x]['index']))
        sortedLabels = self.getSortedLabels()
        for label in sortedLabels:
            l = self.inputs[label]
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

            # pw['Text'].grid(sticky="NSEW", row=pw['startRow'], column=pw['startCol'], padx=5, pady=5, columnspan=(pw['endCol'] - pw['startCol'] + 1))
            # print ("row=",pw['startRow'], "column=", pw['endCol'], "columnspan=", pw['endCol'] - pw['startCol'] + 1)
            # pw['Text'].insert (0, l['value'])

        for funcLabel in self.functions:
            l = self.functions[funcLabel]
            l['Button'] = Button(self.root, takefocus=1, text=funcLabel, name=funcLabel.lower())
            l['Button'].grid(padx='3m', pady='3m', ipadx='2m', ipady='1m', sticky="nesw",
                             row=l['row'], column=l['col'])
            l['Button'].bind("<Return>", self.someOtherButtonPressed)
            l['Button'].bind("<Button-1>", self.someOtherButtonPressed)
            # l['Button'].bind("<Return>"  , l['function'])
            # l['Button'].bind("<Button-1>", l['function'])

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

        self.root.destroy()

    def getInputs(self, msg, title):
        self.startInput()

    def get(self, label):
        if self.inputs[label]['type'] == 'int':
            return int(self.inputs[label]['value'])
        elif self.inputs[label]['type'] == 'float':
            return float(self.inputs[label]['value'])
        else:
            return self.inputs[label]['value']

    def set(self, label, value):
        l = str(len(self.inputs[label]['Entry'].get()))
        self.inputs[label]['Entry'].delete(0, END)
        # self.inputs[label]['Entry'].select_clear()
        self.inputs[label]['Entry'].insert(0, value)
