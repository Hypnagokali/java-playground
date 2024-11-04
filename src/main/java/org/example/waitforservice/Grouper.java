package org.example.waitforservice;

public class Grouper {

    public void doGrouping(int id) {
        System.out.println("Grouping data: " + id);
        doSomeWork();
        System.out.println("Done: " + id);
    }

    private static void doSomeWork() {
        try {
            Thread.sleep( 2000 );
        }  catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new RuntimeException( e );
        }
    }

}
