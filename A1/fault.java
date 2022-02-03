import java.util.concurrent.*;
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import java.awt.Color; 
import javax.imageio.ImageIO;

//This class is used to create locks 
class Lock{

}

//MyRunnable implements the Runnable interface
class MyRunnable implements Runnable{

    //After a total of 'k' fault-lines have been created, flag = false to end the whole simulation
    volatile boolean flag = true;

    @Override
    public void run(){

        while(flag){
               int b1, x1, y1, b2, x2, y2;  

               //select point 1
               b1 = (int)(Math.random()*4);   
               if(b1 == 0 || b1 == 2){                   //boundary is parallel to x-axis
                   x1 = (int)(Math.random()*(fault.w - 2)) + 1;
                   if(b1 == 0){
                       y1 = 0;
                   }else y1 = fault.h - 1;      
               }else{                                    //boundary is parallel to y-axis 
                   if(b1 == 1){
                       x1 = 0;
                   }else x1 = fault.w - 1;
                   y1 = (int)(Math.random()*(fault.h - 2)) + 1;    
               }
       
               //select point 2
               while(true){                 
                   b2 = (int)(Math.random()*4);          //don't select the same boundary
                   if(b1 != b2) break; 
               }
               if(b2 == 0 || b2 == 2){                   //boundary is parallel to x-axis
                   x2 = (int)(Math.random()*(fault.w - 2)) + 1;
                   if(b2 == 0){
                       y2 = 0;
                   }else y2 = fault.h - 1;
               }else{                                    //boundary is parallel to y-axis
                   if(b2 == 1){
                       x2 = 0;
                   }else x2 = fault.w - 1;
                   y2 = (int)(Math.random()*(fault.h - 2)) + 1;
               }

            synchronized(this){
                //check if a total of 'k' fault-lines have already been created
                if(fault.k <= 0){
                    flag = false;
                    break;
                }
                fault.k--;
            }
       
            //choose random height in the range [0, 10]
            int height = (int)(Math.random()*11);
       
            //loop through coordinates in 2D space or virtual terrain
               for(int i = 0; i < fault.h; i++){
                   for(int j = 0; j < fault.w; j++){
                       int val = (x2 - x1)*(i - y1) - (j - x1)*(y2 - y1);  //i is y, j is x
                       if(val > 0){                  
                           //Two threads adjusting the height of the same point should not interfere with each other. 
                           //This only blocks point 'virtualTerrain[i][j]' using 'lock[i][j]' Lock object, other positions on the terrain can still be accessed.
                           synchronized(fault.locks[i][j]){           
                             fault.virtualTerrain[i][j] = fault.virtualTerrain[i][j] + height;
                           }
                       }
                   }
               }
        }
    }
}


public class fault{
    
    public static Lock[][] locks;
    public static int virtualTerrain[][];
    public static int w, h, k;
    public static void main(String[] args){

        int t;
        w = Integer.parseInt(args[0]);
        h = Integer.parseInt(args[1]);
        t = Integer.parseInt(args[2]);
        k = Integer.parseInt(args[3]);

        //create virtual terrain of width = w and height = h
        //w = number of columns and h = number of rows
        virtualTerrain = new int[h][w]; 

        //create w x h locks (one for each point on the terrain)
        locks = new Lock[h][w];
        for(int i = 0; i < fault.h; i++){
            for(int j = 0; j < fault.w; j++){
                locks[i][j] = new Lock();
            }
        }

        MyRunnable myRunnableObj = new MyRunnable();
        ExecutorService es = Executors.newFixedThreadPool(t); 

        long start = System.currentTimeMillis();
        for(int i = 0; i < t; i++){
            es.execute(myRunnableObj);
        }
        es.shutdown();
        //wait until all threads have finished executing
        try{
             es.awaitTermination(1, TimeUnit.MINUTES);
        }catch(InterruptedException ie){

        }
                
        //print execution time
        long end = System.currentTimeMillis();
        System.out.println("Execution time : " + (end - start) + "ms");



        //........................................THIS SECTION HANDLES OUTPUT IMAGE........................................

        //find minimum height and maximum height
        int max = virtualTerrain[0][0];
        int min = virtualTerrain[0][0];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(virtualTerrain[i][j] > max) max = virtualTerrain[i][j];
                if(virtualTerrain[i][j] < min) min = virtualTerrain[i][j];
            }
        }

        //this array holds values in the range [0,255]
        int myImage[][] = new int[h][w];
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                //normalizing virtualTerrain
                myImage[i][j] = (int)(Math.round((float)(virtualTerrain[i][j] - min)/(max - min) * 255)); 
            }
        }

        BufferedImage img = null;
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB); 
        int p;
        File f = null; 
        Color c;

        //loop through and set RGB
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                p = myImage[y][x];
                c = new Color(p, p, p);  //grayscale image
                int rgb = c.getRGB();
                img.setRGB(x, y, rgb);
            }
        }

        //Print image to file
        try
        { 
            f = new File("outputimage.png"); 
            ImageIO.write(img, "png", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println("Error: " + e); 
        } 
    } 
}