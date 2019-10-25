import GuiClass

def addTwoValues (screen):
   #print (s)
   int1 = screen.get ("int1")
   int2 = screen.get ("int2")
   total = int1 + int2
   screen.set ("total", total)
   screen.set ("float1", screen.get("float1") + 1)
   #print ("From addTwoValues ", total)
   for index in range (1, 11):
      screen.print ("window1", index, "From addTwoValues", format(total, "5d"), format (total + 1, "5d"), "\n")
      
def main():
   int1 = 10
   int2 = 20
   float1 = 123.45
   
   screen = GuiClass.GuiClass()
   
   screen.setText ("Testing data entry", col=1, row=0, endCol=2, align='center')

   screen.setText ("Enter int 1", col=1, row=1)
   screen.getInt ("int1", col=2, row=1, defValue=int1)

   screen.getIntV ("Enter int 2", "int2", col=1, row=2, defValue=int2)
   #screen.setText ("Enter int 2", col=1, row=2)
   #screen.getInt ("int2", col=2, row=2, defValue=int2)

   screen.setText ("Enter float 1", col=1, row=3)
   screen.getFloat ("float1", col=2, row=3, defValue=float1)

   screen.setText ("Enter string 1", col=1, row=4)
   screen.getString ("str1", col=2, row=4)

   screen.setSpacer (col=3, row=1, width=2)   
   screen.setSpacer (col=3, row=6, width=2)   
   screen.setFunction ("add", col=4, row=1, function=addTwoValues)
   screen.setCancelButton ("Cancel", col=4, row=2)
   screen.setOKButton ("OK", col=4, row=4)
   
   screen.setText ("Total", col=1, row=5)
   screen.getInt ("total", col=2, row=5)
   
   screen.setPrintWindow ("window1", startCol=1, startRow=7, endCol=4, endRow=9)

   print ("Before inputs")
   print ("int1 is ", int1)   
   print ("int2 is ", int2)   
   print ("Float is ", screen.get ("float1"))
   print ("String is ", screen.get ("str1"))
   print ("Total is ", screen.get("total"))
   print ("Numeric total is ", (int1 + int2 + screen.get ("float1")))

   screen.getInputs("Enter this data", "Title")
   int1 = screen.get ("int1")
   int2 = screen.get ("int2")

   print ("\n\nAfter inputs")
   print ("int1 is ", int1)   
   print ("int2 is ", int2)   
   print ("Float is ", screen.get ("float1"))
   print ("String is ", screen.get ("str1"))
   print ("Total is ", screen.get("total"))
   print ("Numeric total is ", (int1 + int2 + screen.get ("float1")))
   
main()   
