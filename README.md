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
   across colors. Humans perceive green more strongly than red, and red more strongly than blue .For Our  perpuse though
   it does not make any significant difference.
  

Original Image:

![]( https://github.com/nikolis/SolveIT/blob/master/imagestest/original.jpg)

  Gray Image Using Averaging method:

![](https://github.com/nikolis/SolveIT/blob/master/imagestest/grayAVG.jpg)


- Image binarization:
  Image is binaryzed using the otsu's thresholding method and it assumes a gray scale Image as Input.The gray scale Image produced in the previus phase is used now as input

  Binarized Image:
  ![](https://github.com/nikolis/SolveIT/blob/master/imagestest/binarizedImage.jpg)
  
- Linear Filters 
  
 In our scope the foreground of an image is our base information which we are going to use to classify the digits and so even minor changes in the image could yield better recognition rate at the end. We are using Linear Filters to Reduce the amount of noise present in the picture and this is what we get as an output:

![](https://github.com/nikolis/SolveIT/blob/master/imagestest/binarizedImageFiltered.jpg)
