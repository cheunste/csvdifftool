package com.company;

import com.company.pcvue.fields.VarexpVariable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is a buffer class that is used as a temporary storage
 * for importing varexp variables from the file to a DB.
 * This class might be used for exporting as well
 * <p>
 * This class is meant as a middleman to two classes and is meant to be used as a thread
 */

public class Buffer {

    //This flag is to communicate when one object is completely done
    public boolean isDone;
    public int QUEUE_LIMIT = 1000;
    Queue<VarexpVariable> bufferQueue = new ConcurrentLinkedQueue<VarexpVariable>();
    //This flag is to let other objects know when the objects in the queue is ready to be consumed
    private boolean isReady;

    public Buffer() {
        this.isDone = false;
        this.isReady = false;
    }

    public VarexpVariable get() {
        VarexpVariable temp = bufferQueue.poll();
        return temp;
    }

    public void put(VarexpVariable var) {
        bufferQueue.add(var);
        if (bufferQueue.size() >= QUEUE_LIMIT) {
            ready();
        }
    }

    //return size of queue.
    public int getSize() {
        return bufferQueue.size();
    }

    //This is used to let the class know that another class is done with their job.
    public void setDoneFlag() {
        this.isDone = true;
    }

    //Clear flag
    public void clearDoneFlag() {
        this.isDone = false;
    }

    //Is buffer empty
    public boolean isEmpty() {
        return bufferQueue.isEmpty();
    }

    public void ready() {
        this.isReady = true;
    }

    public void notReady() {
        this.isReady = false;
    }

    public boolean isBufferReady() {
        return this.isReady;
    }

    public int getQueueLimit() {
        return this.QUEUE_LIMIT;
    }

}
