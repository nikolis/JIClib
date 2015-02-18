# SolveIT

<h3>Solve is an end to end Optical character recognition library<h3>


SolveIT is divided in 3 basic Phases 
  *  Pre proccessing 
  *  Feature Extraction 
  *  Classification 


### Pre proccessing 

The pre proccessing has the following functionality:

   - Colored Image to Gray scale Image conversion:
   The User has two avaliable options the  Averaging method that is just computing the average of the
   red, green and blue values  of every pixel and uses that as new grayLevel Intensity.The second choise is the 
   luminance method, the  luminance method plays off the fact that cone density in the human eye is not uniform     
   across colors. Humans perceive green more strongly than red, and red more strongly than blue.
   
  Example:
  

Original Image:

![](https://github.com/nikolis/SolveIT/blob/master/imagestest/download.jpg)

  Gray Image Using Averaging method:

![](https://github.com/nikolis/SolveIT/blob/master/imagestest/grayAVG.jpg)

  Grau Image Using luminance method:
![](https://github.com/nikolis/SolveIT/blob/master/imagestest/gray.jpg)

