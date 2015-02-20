# SolveIT

<h3>Solve is an end to end Optical character recognition library<h3>


SolveIT is divided in 3 basic Components 
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



  Gray Image Using Averaging method:



  Grau Image Using luminance method:


- Image binarization:
  Image is binaryzed using the otsu's thresholding method and it assumes a gray scale Image as Input
  
  Original Image:

![](https://github.com/nikolis/SolveIT/blob/master/imagestest/gray.jpg)

  Binarized Image:
  
  ![](https://github.com/nikolis/SolveIT/blob/master/imagestest/binarizedImage.jpg)
 
