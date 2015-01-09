package featureexctraction;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ZernikeMoments
{
	BufferedImage inputImage ; 
	NormalizedImage normImage ;
	NormalizedImage insideDisc ; 
	int maxImageWidth = 500 ; 
	int maxImageHeight=500 ; 
	
	
	
	public ZernikeMoments(BufferedImage BI)
	{
		inputImage = BI ; 
		normImage = new NormalizedImage() ; 
		insideDisc= new NormalizedImage() ;
	}
	
	private class Complex
	{
		double real ; 
		double imag; 
	}
	
	private class NormalizedImage
	{
		double X[];
		double Y[];
		int   X_Index[];
		int   Y_Index[];
		int   length;
		
		public NormalizedImage()
		{
			X= new  double[maxImageHeight*maxImageWidth] ;
			Y= new  double[maxImageHeight*maxImageWidth] ;
			X_Index=new int [maxImageHeight*maxImageWidth];
			Y_Index=new int[maxImageHeight*maxImageWidth];
		}
	}
	
	public void mainProcces(int order,int lambdaChoise)
	{
		double lambda ,ZMpq; 
		int moments =0; 
		Complex v = new Complex()  ;  
		
		normalization(1);
		findInsideDiscImage();
		Complex z[][] = new Complex[order*order][order*order] ;
		double R,theta,r ; 
		for(int p=0; p<=order; p++)
		{
			for(int q=0; q<=order; q++)
			{
				Complex tempTem = new Complex() ; 
				tempTem.real=0 ; 
				tempTem.imag=0;
				z[p][q]=tempTem ;
				
				if(!((p-Math.abs(q))%2==0)&&(-p<=q)&&(q<=p))
				{
					moments++;
					for(int i=0; i<insideDisc.length; i++)
					{		
						r      = Math.sqrt(Math.pow(insideDisc.X[i],2)+Math.pow(insideDisc.Y[i],2));
						theta  = Math.atan2(insideDisc.Y[i],insideDisc.X[i]);
						R = directZernikePoly(p,q,r);
						v.real = Math.cos(q*theta);
						v.imag = -Math.sin(q*theta);
						double temp = (double)(inputImage.getRGB(insideDisc.X_Index[i],insideDisc.Y_Index[i])/255.0);
						z[p][q].real = z[p][q].real+v.real*R*temp;
						z[p][q].imag = z[p][q].imag+v.imag*R*temp;
					}
					
					if(lambdaChoise==1)
					{
						lambda = 2*(p+1)/(Math.PI*Math.pow(inputImage.getHeight()*inputImage.getWidth()-1,2));
					}else
					{
						lambda = (p+1)/Math.pow(inputImage.getHeight()*inputImage.getWidth()-1,2);
					}
					
					z[p][q].real = lambda*z[p][q].real;
					z[p][q].imag = lambda*z[p][q].imag;
					
					ZMpq=Math.sqrt(Math.pow(z[p][q].real,2)+Math.pow(z[p][q].imag,2));
					System.out.println(ZMpq) ; 
				}
			}
		}	
	}
	
	
	
	double directZernikePoly(int p, int q, double r)
	{
		double a1,a2,s,sum_index,numerator,denominator,R;
		
		a1=(p+Math.abs(q))/2;
		a2=(p-Math.abs(q))/2;

		sum_index=a2;
		R=0;
		
		for(s=0; s<=sum_index; s++)
		{
		  numerator    = Math.pow(-1,s)*factorial(p-s)*Math.pow(r,(p-2*s));
		  denominator  = factorial(s)*factorial(a1-s)*factorial(a2-s);
		  R = R+numerator/denominator;
		}
    return(R);
		
	}


	public static double factorial(double number)
	{
		if(number==0)
		{
			return 1;
		}
		else
		{
			return factorial(number-1)*number;
		}
	}
	
	public void normalization(int choice)
	{
	
		 double c1,c2;
		 int count =0; 
		if(choice==1)
		{
		   c1=Math.sqrt(2)/(inputImage.getWidth()*inputImage.getHeight()-1);
		   c2=-1/Math.sqrt(2);
		}
		else
		{
		   c1=2.0/(inputImage.getWidth()*inputImage.getHeight()-1);
		   c2=-1;
		}
		for(int i=0; i<inputImage.getWidth(); i++)
		{
			for(int j=0; j<inputImage.getHeight(); j++)
			{
				normImage.X[count]       = c1*i+c2;
				normImage.Y[count]       = c1*j+c2;

				normImage.X_Index[count] = i;
				normImage.Y_Index[count] = j;

				count++;	
			}
		}
		normImage.length = count;
	}
	
	public void findInsideDiscImage()
	{
		int count,temp  ; 
		double r ; 
		count=0; 
		temp=0 ; 
		for(int i=0; i<inputImage.getWidth(); i++)
		{
			for(int j=0; j<inputImage.getHeight(); j++)
			{
				r = Math.sqrt(Math.pow((normImage.X[temp]),2)+Math.pow((normImage.Y[temp]),2));
				
				if(r<=1)
				{
					insideDisc.X[count]       = normImage.X[temp];
					insideDisc.Y[count]       = normImage.Y[temp];

					insideDisc.X_Index[count] = i;
					insideDisc.Y_Index[count] = j;
				
					count++;
				}
				temp++;
			}
		}
		insideDisc.length=count;
	}
	
	public static void main(String args[])
	{
		BufferedImage image = null ;  
		try{
			image = ImageIO.read(new File("C:\\Users\\310176547\\Desktop\\meine\\Imaging\\images\\subImage2t2.jpg"));
		}catch(Exception e){
			e.printStackTrace();
		}			
		ZernikeMoments zer = new ZernikeMoments(image) ; 		
		zer.mainProcces(5,1);
	}
}