package sorting;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
//Towers that will be generated randomly
class Tower {
    public int x,y,height,width;
    public Tower(int x, int y, int width, int height){ //default constructor
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public Tower(Tower tower) {
        this.x = tower.x;
        this.y = tower.y;
        this.height = tower.height;
        this.width = tower.width;
    }
}

public class Sorting extends JPanel {
    final static JFrame frame = new JFrame("Sorting Visualisation");   //creating new window
    public static final ArrayList <Tower> towers = new ArrayList<>(); // to store all towers.
    SecureRandom random = new SecureRandom();   // later will be used to generate tower of random height.

    //default constructor

    public Sorting(){
        final int width = 944,height = 521;//width and height of window where the towers will get placed
        final int thickness = 15; // thickness of each tower
        for(int x = 0 ; x < width ; x+=thickness) {
            int randomHeight = random.nextInt(height);  //generating random height of tower
            towers.add(new Tower(x, randomHeight, thickness, height - randomHeight));//adding tower into arraylist
        }
    }

    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(0xF56FF));//clay : 0xFA5BA , blue : 0xF56FF, green : 0x5B51E
        synchronized (towers){  //doesn't let other thread takeover the control while painting on the screen
            for(Tower r : towers) g.fillRect(r.x,r.y,r.width,r.height); //drawing towers on screen.
        }
    }

    private static void bubbleSorting(){
        for(int i = 0; i < towers.size() ; i++){
            for(int j = 0; j < towers.size() - i - 1 ; j++){
                //don't be afraid this just swaps the two towers
                if(towers.get(j).y < towers.get(j + 1).y){

                    // did this because of reference need to deep copy "Tower temp" would only do shallow copy.
                    int y = towers.get(j).y;
                    int h = towers.get(j).height;

                    towers.get(j).y = towers.get(j+1).y;
                    towers.get(j).height = towers.get(j+1).height;
                    towers.get(j+1).y = y;
                    towers.get(j+1).height = h;

                    //slowing down the process so user can view the changes on the screen
                    delay();

                    //updating the gui to display swapping
                    frame.repaint();
                }//end of if statement
            }//end of j's loop
        }//end of i's loop
    }

    private static void insertionSorting(){
        for(int i = 1 ; i < towers.size() ; i++){
            int j;
            Tower Temp = new Tower(towers.get(i));
            for(j = i - 1 ;  j>=0 && Temp.y > towers.get(j).y ; j-- ){
                towers.get(j+1).y = towers.get(j).y;
                towers.get(j+1).height = towers.get(j).height;
                frame.repaint();
                delay();
            }
            towers.get(j+1).y = Temp.y;
            towers.get(j+1).height = Temp.height;
            frame.repaint();
            delay();
        }
    }

    private static void delay() {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void mergeSort(int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        ArrayList<Tower> towers1 = new ArrayList<>(n1);
        ArrayList<Tower> towers2 = new ArrayList<>(n2);

        int i ,  j , k;

        for(i = 0 ; i < n1 ; i++)  towers1.add(new Tower(towers.get(i + left)));
        for(j = 0 ; j < n2 ; j++)  towers2.add(new Tower(towers.get(mid + j + 1)));

        i = 0 ; j = 0 ; k = left;
        while(n1!=0 && n2!=0){
            if(towers1.get(i).y > towers2.get(j).y){
                towers.get(k).y = towers1.get(i).y;
                towers.get(k).height = towers1.get(i).height;
                k++ ; i++ ; n1--;delay();
            }else{
                towers.get(k).y = towers2.get(j).y;
                towers.get(k).height = towers2.get(j).height;
                k++ ; j++ ; n2--;delay();
            }
            frame.repaint();
        }

        while(n1!=0){
            towers.get(k).y = towers1.get(i).y;
            towers.get(k).height = towers1.get(i).height;
            k++ ; i++ ; n1--;delay();
            frame.repaint();
        }

        while(n2!=0){
            towers.get(k).y = towers2.get(j).y;
            towers.get(k).height = towers2.get(j).height;
            k++ ; j++ ; n2--;
            frame.repaint();delay();
        }
    }

    private static void merge(int left, int right) {
        if(left < right){
            int mid = (left + right) / 2;
            merge(left,mid);
            merge(mid+1,right);
            mergeSort(left,mid,right);
        }
    }


    private void quick(int left, int right) {
        int l = left;
        int r = right - 1;
        int size = right - left;

        if(size>1){
            int pivot = random.nextInt(size) + l;
            while(l<r)
            {
                while(towers.get(r).y>towers.get(pivot).y && r>l ){
                    r--;
                }
                while(towers.get(l).y<towers.get(pivot).y && l<=r ){
                    l++;
                }
                if(l<r){
                    Tower temp = new Tower(towers.get(l));
                    towers.set(l,new Tower(towers.get(r)));
                    towers.set(r,new Tower(temp));
//                    towers.get(l).y = towers.get(r).y;
//                    towers.get(l).height = towers.get(r).height;
//                    towers.get(r).y = temp.y;
//                    towers.get(r).height = temp.height;
//                    int t = data[l];
//                    data[l] = data[r];
//                    data[r] = t;
//                    swap(data[l],data[r]);
                    l++;
                    frame.repaint();
                    delay();
                }
            }
            quick(left,l);
            quick(r,right);
        }
    }

    public static void main(String Args[]){
        Sorting sorting = new Sorting();    //creating object of sorting
        frame.add(sorting); //adding sorting to window
//        Thread bubbleSort = new Thread(Sorting::bubbleSorting);
//        bubbleSort.start(); // staring the sorting algorithm
          Thread mergeSort = new Thread(()->Sorting.merge(0,towers.size()-1));
          mergeSort.start();
//        Thread insertionSort = new Thread(Sorting::insertionSorting);
//        insertionSort.start();
//        Thread quickSort = new Thread(()->sorting.quick(0,towers.size()));
//        quickSort.start();
//        don't pay too much attention to these, just setting the frames properties.
        frame.setSize(960,560);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }//end of main function

}//end of public class Sorting

