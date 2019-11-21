import tkinter as tk
from tkinter import ttk


def callbackFunc(event):
    print("New Element Selected")


app = tk.Tk()
app.geometry('600x300')

labelTop = tk.Label(app,
                    text="Choose your favourite fruit")
labelTop.grid(column=0, row=0)

comboExample = ttk.Combobox(app, values=["Apple", "Grape", "Orange", "Strawberry"])

comboExample.grid(column=0, row=1)
comboExample.current(1)

comboExample.bind("<<ComboboxSelected>>", callbackFunc)

app.mainloop()