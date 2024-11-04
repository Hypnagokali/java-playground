package org.example.waitforservice;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GrouperService {

    private static final int MAX_NUMBER_OF_PARALLEL_GROUPERS = 4;

    private final Grouper grouper;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition noMoreGroupersCondition = lock.newCondition();
    private int runningGroupers = 0;

    public GrouperService(Grouper grouper) {
        this.grouper = grouper;
    }

    public void importDataAndGroup(int id) {
        System.out.println("Thread access GrouperService: " + id);
        System.out.println("All Threads are allowed to serialize their data");
        waitIfNoGrouperIsAvailable(id);
        this.grouper.doGrouping( id );
        freeGrouper( id );
    }

    private void freeGrouper(int id) {
        System.out.println("Thread " + id + " signals all: 'I am done'");
        try {
            lock.lock();
            this.runningGroupers--;
            noMoreGroupersCondition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    private void waitIfNoGrouperIsAvailable(int id) {
        try {
            lock.lock();
            while ( runningGroupers >= MAX_NUMBER_OF_PARALLEL_GROUPERS ) {
                System.out.println("Thread " + id + " has to wait");
                doWait();
            }
            this.runningGroupers++;
        }
        finally {
            lock.unlock();
        }
    }

    private void doWait() {
        try {
            noMoreGroupersCondition.await();
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new RuntimeException( e );
        }
    }
}
